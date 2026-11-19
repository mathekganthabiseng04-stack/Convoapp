/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.convoapp;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
 

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author admin
 */

  

public class Message {

    private static final Gson GSON = new Gson();
    private static final List<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;
    private static int messagesCreated = 0;          // used as the "message number" (starts at 0)
    private static Path storageFile = Paths.get("messages.json");

    private static final Random RANDOM = new Random();

    
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

   
    public Message(String recipient, String messageText) {
        this(generateMessageID(), messagesCreated++, recipient, messageText);
    }

    
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }



    // Ensures the message ID is not more than ten characters. 
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

   
    public String checkRecipientCell() {
        return validateRecipientCell(recipient);
    }

    public static String validateRecipientCell(String number) {
        // "+" then 1-3 digit country code, then 1-10 digits
        if (number != null && number.matches("^\\+27\\d{1,3}\\d{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

   
    public final String createMessageHash() {
        String firstTwo = messageID.substring(0, Math.min(2, messageID.length()));

        String[] words = messageText.trim().split("\\s+");
        String first = cleanWord(words[0]);
        String last = cleanWord(words[words.length - 1]);

        return (firstTwo + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    // Lets the user choose what to do with the message.
    //1 = send, 2 = disregard, 3 = store.
    
    public String sentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            }
            case 2 -> {
                return "Press 0 to delete the message.";
            }
            case 3 -> {
                try {
                    storeMessage();
                    return "Message successfully stored.";
                } catch (IOException e) {
                    return "Message could not be stored: " + e.getMessage();
                }
            }
            default -> {
                return "Invalid option.";
            }
        }
    }

  
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.toDisplayString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    // Returns the total number of messages sent. 
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }


    
    public static String checkMessageLength(String text) {
        int length = text == null ? 0 : text.length();
        if (length <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
    }

    // Full details in the required order: ID, Hash, Recipient, Message. 
    public String toDisplayString() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText
                + "\n";
    }

    public static String generateMessageID() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    private static String cleanWord(String word) {
        // remove punctuation such as "?" or "," so "tonight?" becomes "tonight"
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // JSON storage 
    public void storeMessage() throws IOException {
        List<Message> all = loadStoredMessages();
        all.add(this);
        Files.writeString(storageFile, GSON.toJson(all), StandardCharsets.UTF_8);
    }

    /** Reads all messages currently stored in the JSON file.
     * @return 
     * @throws java.io.IOException */
    public static List<Message> loadStoredMessages() throws IOException {
        if (!Files.exists(storageFile)) {
            return new ArrayList<>();
        }
        String json = Files.readString(storageFile, StandardCharsets.UTF_8);
        if (json.isBlank()) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
        List<Message> loaded = GSON.fromJson(json, listType);
        return loaded == null ? new ArrayList<>() : loaded;
    }

    public static void setStorageFile(Path path) {
        storageFile = path;
    }

    
    public static void resetState() {
        sentMessages.clear();
        totalMessagesSent = 0;
        messagesCreated = 0;
    }

    
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
}

