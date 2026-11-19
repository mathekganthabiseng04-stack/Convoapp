/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.convoapp;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageManagerTest {

    private static final String TEXT_1 = "Did you get the cake?";
    private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT_3 = "Yohoooo, I am at your gate.";
    private static final String TEXT_4 = "It is dinner time!";
    private static final String TEXT_5 = "Ok, I am leaving without you.";

    // Fields (declared outside any method) so every test can use them
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;
    private Message m5;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("messages.json"));

        // Part 3 test data (developer entries, so recipients are not validated here)
        m1 = new Message("1000000001", 0, "+27834557896", TEXT_1);
        m2 = new Message("2000000002", 1, "+27838884567", TEXT_2);
        m3 = new Message("3000000003", 2, "+27834484567", TEXT_3);
        m4 = new Message("4000000004", 3, "0838884567", TEXT_4);
        m5 = new Message("5000000005", 4, "+27838884567", TEXT_5);

        m1.sentMessage(1);   // Sent
        m2.sentMessage(3);   // Stored
        m3.sentMessage(2);   // Disregard
        m4.sentMessage(1);   // Sent
        m5.sentMessage(3);   // Stored
    }

    

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(List.of(TEXT_1, TEXT_4), MessageManager.getSentMessageTexts());
    }

    @Test
    void disregardedAndStoredArraysPopulated() {
        assertEquals(1, MessageManager.getDisregardedMessages().size());
        assertEquals(2, MessageManager.getStoredMessages().size());
    }

    @Test
    void hashAndIdArraysPopulated() {
        assertEquals(5, MessageManager.getMessageIDs().size());
        assertEquals(5, MessageManager.getMessageHashes().size());
        assertTrue(MessageManager.getMessageIDs().contains("4000000004"));
        assertTrue(MessageManager.getMessageHashes().contains(m2.getMessageHash()));
    }

 

    @Test
    void displayLongestMessage() {
        assertEquals(TEXT_2, MessageManager.longestStoredMessage());
    }

  
    @Test
    void searchForMessageID() {
        assertEquals(TEXT_4, MessageManager.findByMessageID("4000000004").getMessageText());
    }

    @Test
    void searchForUnknownMessageID() {
        assertTrue(MessageManager.searchByMessageID("9999999999").startsWith("No message found"));
    }

    
    @Test
    void searchMessagesByRecipient() {
        assertEquals(List.of(TEXT_2, TEXT_5), MessageManager.searchByRecipient("+27838884567"));
    }

    

    @Test
    void deleteMessageUsingHash() throws IOException {
        String result = MessageManager.deleteByHash(m2.getMessageHash());

        assertEquals("Message: \"" + TEXT_2 + "\" successfully deleted.", result);
        assertEquals(1, MessageManager.getStoredMessages().size());
        assertEquals(1, Message.loadStoredMessages().size());   // the JSON file is updated too
    }

    @Test
    void deleteWithUnknownHash() {
        assertTrue(MessageManager.deleteByHash("XX:9:NOTHING").startsWith("No stored message"));
    }

    
    @Test
    void displayReport() {
        String report = MessageManager.reportStored();
        assertTrue(report.contains(m2.getMessageHash()));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains(TEXT_2));

        String sentReport = MessageManager.reportSent();
        assertTrue(sentReport.contains(m1.getMessageHash()));
        assertTrue(sentReport.contains(TEXT_1));
    }
}