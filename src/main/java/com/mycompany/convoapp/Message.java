/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.convoapp;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;

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

    // ---------- Shared (static) state ----------
    // The sent / disregarded / stored / hash / ID arrays now live in MessageManager.
    private static int messagesCreated = 0;          // used as the "message number" (starts at 0)
    private static Path storageFile = Paths.get("messages.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Random RANDOM = new Random();

    // ---------- Instance fields (these are what get written to JSON) ----------
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String sender = "Developer";

    /** Normal constructor: auto-generates the ID and message number. */
    public Message(String recipient, String messageText) {
        this(generateMessageID(), messagesCreated++, recipient, messageText);
    }

    /** Constructor that lets you supply the ID and number (useful for unit tests). */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // ---------- Required methods ----------

    /** Ensures the message ID is not more than ten characters. */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Ensures the recipient number starts with an international code (+xx)
     * and is not more than ten digits after the code.
     */
    public String checkRecipientCell() {
        return validateRecipientCell(recipient);
    }

    public static String validateRecipientCell(String number) {
        // "+" then 1-3 digit country code, then 1-10 digits
        if (number != null && number.matches("^\\+\\d{1,3}\\d{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

    /** Hash = first two digits of ID : message number : FIRSTWORDLASTWORD (all caps). */
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, Math.min(2, messageID.length()));

        String[] words = messageText.trim().split("\\s+");
        String first = cleanWord(words[0]);
        String last = cleanWord(words[words.length - 1]);

        return (firstTwo + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    /**
     * Lets the user choose what to do with the message.
     * 1 = send, 2 = disregard, 3 = store.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                MessageManager.addSent(this);
                return "Message successfully sent.";
            case 2:
                MessageManager.addDisregarded(this);
                return "Press 0 to delete the message.";
            case 3:
                try {
                    storeMessage();                         // write to messages.json
                    MessageManager.loadStoredFromJson();    // read it back into the stored array
                    return "Message successfully stored.";
                } catch (IOException e) {
                    return "Message could not be stored: " + e.getMessage();
                }
            default:
                return "Invalid option.";
        }
    }

    /** Returns every message sent while the program is running. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : MessageManager.getSentMessages()) {
            sb.append(m.toDisplayString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /** Returns the total number of messages sent. */
    public static int returnTotalMessages() {
        return MessageManager.getSentMessages().size();
    }

    // ---------- Extra helpers ----------

    /** Checks the 250 character limit (used by the unit tests). */
    public static String checkMessageLength(String text) {
        int length = text == null ? 0 : text.length();
        if (length <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
    }

    /** Full details in the required order: ID, Hash, Recipient, Message. */
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

    // ---------- JSON storage ----------

    /** Appends this message to the JSON file (creates the file if needed). */
    public void storeMessage() throws IOException {
        List<Message> all = loadStoredMessages();
        all.add(this);
        writeStoredFile(all);
    }

   
    public static void writeStoredFile(List<Message> messages) throws IOException {
        Files.writeString(storageFile, GSON.toJson(messages), StandardCharsets.UTF_8);
    }

    //Reads all messages  stored in the JSON file.
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

    // Resets counters and all the arrays
    public static void resetState() {
        MessageManager.resetAll();
        messagesCreated = 0;
    }

    // ---------- Getters and setters ----------
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    public String getSender() {
        // Gson skips field initialisers, so an older messages.json without a sender gives null
        return sender == null ? "Developer" : sender;
    }

    public void setSender(String sender) { this.sender = sender; }
}