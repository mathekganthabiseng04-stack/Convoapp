/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.convoapp.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Messangemanager {


public class MessageManager {

    // The five arrays required by the assignment
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();   // read from the JSON file
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();

    // ---------- Populating the arrays ----------

    public static void addSent(Message m) {
        sentMessages.add(m);
        refreshIdAndHashArrays();
    }

    public static void addDisregarded(Message m) {
        disregardedMessages.add(m);
        refreshIdAndHashArrays();
    }

    //Reads messages.json into the stored messages array. 
    public static void loadStoredFromJson() throws IOException {
        storedMessages.clear();
        storedMessages.addAll(Message.loadStoredMessages());
        refreshIdAndHashArrays();
    }

   
    private static void refreshIdAndHashArrays() {
        messageHashes.clear();
        messageIDs.clear();
        for (Message m : allMessages()) {
            messageHashes.add(m.getMessageHash());
            messageIDs.add(m.getMessageID());
        }
    }

    private static List<Message> allMessages() {
        List<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        return all;
    }

    // - Sender and recipient of all stored messages 

    public static String displaySenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(m.getSender())
              .append(" | Recipient: ").append(m.getRecipient())
              .append(System.lineSeparator());
        }
        return sb.toString();
    }

    //  Longest stored message 

    public static String longestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    // ---------- 2c. Search by message ID ----------

    /** Returns the message with this ID, or null if it is not found. */
    public static Message findByMessageID(String id) {
        for (Message m : allMessages()) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public static String searchByMessageID(String id) {
        Message m = findByMessageID(id);
        if (m == null) {
            return "No message found with ID: " + id;
        }
        return "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText();
    }

    // ---------- 2d. Search by recipient (sent or stored) ----------

    public static List<String> searchByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m.getMessageText());
            }
        }
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m.getMessageText());
            }
        }
        return results;
    }

    // ---------- 2e. Delete a stored message using its hash ----------

    public static String deleteByHash(String hash) {
        Message target = null;
        for (Message m : storedMessages) {
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                target = m;
                break;
            }
        }
        if (target == null) {
            return "No stored message found with hash: " + hash;
        }

        storedMessages.remove(target);
        try {
            Message.writeStoredFile(storedMessages);   // keep the JSON file in sync
        } catch (IOException e) {
            return "Could not update the JSON file: " + e.getMessage();
        }
        refreshIdAndHashArrays();
        return "Message: \"" + target.getMessageText() + "\" successfully deleted.";
    }

    // ---------- 2f. Reports ----------

    public static String reportStored() {
        return buildReport("STORED MESSAGES REPORT", storedMessages);
    }

    public static String reportSent() {
        return buildReport("SENT MESSAGES REPORT", sentMessages);
    }

    private static String buildReport(String title, List<Message> list) {
        StringBuilder sb = new StringBuilder(title).append(System.lineSeparator());
        if (list.isEmpty()) {
            sb.append("No messages.").append(System.lineSeparator());
        }
        for (Message m : list) {
            sb.append("-----------------------------").append(System.lineSeparator())
              .append("Message ID:   ").append(m.getMessageID()).append(System.lineSeparator())
              .append("Message Hash: ").append(m.getMessageHash()).append(System.lineSeparator())
              .append("Sender:       ").append(m.getSender()).append(System.lineSeparator())
              .append("Recipient:    ").append(m.getRecipient()).append(System.lineSeparator())
              .append("Message:      ").append(m.getMessageText()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    // ---------- Getters (used by the unit tests) ----------

    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }

    public static List<String> getSentMessageTexts() {
        List<String> texts = new ArrayList<>();
        for (Message m : sentMessages) {
            texts.add(m.getMessageText());
        }
        return texts;
    }

    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }
}
}
