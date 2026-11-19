/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.convoapp;

import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.Test;
/**
 *
 * @author admin
 */
public class MessageIT {
    

    private static final String SUCCESS_CELL = "Cell phone number successfully captured.";
    private static final String FAIL_CELL = "Cell phone number is incorrectly formatted or does not contain an "
            + "international code. Please correct the number and try again.";

    private Message message1;
    private Message message2;

   

    // ---- Message length ----
    @Test
    void messageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(message1.getMessageText()));
    }

    @Test
    void messageLengthFailure() {
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(longMessage));
    }

    // ---- Recipient number ----
    @Test
    void recipientCellSuccess() {
        assertEquals(SUCCESS_CELL, message1.checkRecipientCell());
    }

    @Test
    void recipientCellFailure() {
        assertEquals(FAIL_CELL, message2.checkRecipientCell());
    }

    // ---- Message ID ----
    @Test
    void messageIDIsTenCharactersOrLess() {
        assertTrue(message1.checkMessageID());
        assertEquals(10, Message.generateMessageID().length());
    }

    // ---- Message hash ----
    @Test
    void messageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    void messageHashesInLoop() {
        Message[] messages = {message1, message2};
        String[] expected = {"00:0:HITONIGHT", "01:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    // ---- sentMessage options ----
    @Test
    void sendMessageOption() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    void disregardMessageOption() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    void storeMessageOption() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // ---- Totals ----
    @Test
    void totalMessagesSent() {
        message1.sentMessage(1);   // sent
        message2.sentMessage(2);   // discarded
        assertEquals(1, Message.returnTotalMessages());
        assertTrue(Message.printMessages().contains("0012345678"));
    }

    // ---- JSON storage ----
    @Test
    void storeMessageWritesToJson() throws IOException {
        message1.storeMessage();
        message2.storeMessage();

        List<Message> stored = Message.loadStoredMessages();
        assertEquals(2, stored.size());
        assertEquals("0012345678", stored.get(0).getMessageID());
        assertEquals("Hi Keegan, did you receive the payment?", stored.get(1).getMessageText());
    }

    }
    
  

