/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.convoapp;

import java.util.Scanner;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author admin
 */
public class Convoapp {

    private static String currentUser = "Unknown";

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
       
        Login isLogin = null;
        boolean registrationSuccessful = false;

        // LOOP 1: REGISTRATION SECTION (Repeats until all inputs are valid)
        do {
            System.out.println("\n--- REGISTRATION ---");
            System.out.print("Enter your name: ");
            String name = input.nextLine();

            System.out.print("Enter your username: ");
            String username = input.nextLine();

            System.out.print("Enter your phonenumber: ");
            String phonenumber = input.nextLine();

            System.out.print("Enter your password :");
            String password = input.nextLine();

            // Create an instance to validate these inputs
        
            
            // Create an instance to validate these inputs
        isLogin = new Login(name, username, phonenumber, password);

// RUN VALIDATION CHECKS
boolean isNameValid = isLogin.checkname();
boolean isUsernameValid = isLogin.checkusername();
boolean isPhoneValid = isLogin.checkphonenumber();
boolean isPasswordValid = isLogin.checkpassword();

            // Display individual status messages
            if (isNameValid) {
                System.out.println(" Name sucessfully stored.");
            } else {
                System.out.println("Name unsuccessfully stored(Must be 5 characters or fewer and contain _).");
            }

            if (isUsernameValid) {
                System.out.println("Username successfully stored.");
            } else {
                System.out.println("Username  unsuccessfully stored (Must be 5 characters or more).");
            }

            if (isPhoneValid) {
                System.out.println(" Phone number successfully stored.");
            } else {
                System.out.println(" Phone number unsuccessfully stored.");
            }

            if (isPasswordValid) {
                System.out.println(" Password successfully stored.");
            } else {
                System.out.println(" Password unsuccessfully stored(Needs 8+ chars, upper, lower, number, symbol).");
            }

            // Check if every single requirement passed
            if (isNameValid && isUsernameValid && isPhoneValid && isPasswordValid) {
                System.out.println("Registration successful! " + name + " is registered.");
                registrationSuccessful = true; // Exits the registration loop
            } else {
                System.out.println(" Registration failed due to invalid details. Please try again from the start.");
            }

        } while (!registrationSuccessful);

        // LOOP 2: LOGIN SECTION (Now accepts Username OR Phone Number)
      // LOOP 2: LOGIN SECTION (accepts Username OR Phone Number)
boolean loginSuccessful = false;
int attempts = 0;

do {
    System.out.println("--- LOGIN ---");
    System.out.print("Enter Username or Phone Number: ");
    String loginIdentifier = input.nextLine().trim();

    System.out.print("Enter your password: ");
    String loginPassword = input.nextLine();

    // Use your existing Login method, and store the result so the loop can end
    loginSuccessful = isLogin.checkLogin(loginIdentifier, loginPassword);

    if (loginSuccessful) {
        currentUser = loginIdentifier;      // used as the message sender
        System.out.println("Welcome to Convoapp.");
    } else {
        System.out.println("Login failed. Please try again.");
        if (attempts >= 3) {
            System.out.println("Too many failed attempts. You cannot send messages.");
            return;                          // stop the program after 3 wrong attempts
        }
    }
} while (!loginSuccessful);
        // The user decides how many messages they want to enter
        int maxMessages = readInt(input, "How many messages do you wish to enter? ");
        int enteredMessages = 0;

        // Run until the user selects Quit
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");

            int option = readInt(input, "Option: ");

            switch (option) {
                case 1:
                    if (enteredMessages >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                    } else {
                        handleNewMessage(input);
                        enteredMessages++;
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    break;
                case 4:
                    storedMessagesMenu(input);
                    break;
                default:
                    System.out.println("Please choose 1, 2, 3 or 4.");
            }
        }

        // Total number of messages sent, plus their full details
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        System.out.println(Message.printMessages());
        System.out.println("Goodbye.");
    }

    // ---------- Sending a message ----------
    private static void handleNewMessage(Scanner sc) {
        // Recipient: repeat until the number is valid
        String recipient;
        while (true) {
            System.out.print("Recipient cell number (e.g. +27718693002): ");
            recipient = sc.nextLine().trim();
            String result = Message.validateRecipientCell(recipient);
            System.out.println(result);
            if (result.equals("Cell phone number successfully captured.")) {
                break;
            }
        }

        // Message text: maximum 250 characters
        String text;
        while (true) {
            System.out.print("Enter your message: ");
            text = sc.nextLine();
            if (text.isBlank()) {
                System.out.println("The message cannot be empty.");
            } else if (text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
            }else{
                break;
            
            }  
        }
        Message msg = new Message(recipient, text);
        msg.setSender(currentUser);
        System.out.println("Message ID generated: " + msg.getMessageID());

        // Send / Disregard / Store: repeat until a valid choice is made
        int choice;
        while (true) {
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            choice = readInt(sc, "Choice: ");
            if (choice >= 1 && choice <= 3) {
                break;
            }
            System.out.println("Please choose 1, 2 or 3.");
        }

        System.out.println(msg.sentMessage(choice));

        if (choice == 1) {
            // Show the full details after sending
            System.out.println(msg.toDisplayString());
        } else if (choice == 2) {
            while (readInt(sc, "") != 0) {
                System.out.println("Press 0 to delete the message.");
            }
            System.out.println("Message deleted.");
        }
    }

    // ---------- Fourth main menu option: Stored Messages ----------
    private static void storedMessagesMenu(Scanner sc) {
        // Re-read the JSON file so the stored array is always up to date
        try {
            MessageManager.loadStoredFromJson();
        } catch (IOException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\nStored Messages:");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages for a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("0) Back");

            switch (readInt(sc, "Choice: ")) {
                case 1 ->
                    System.out.println(MessageManager.displaySenderAndRecipient());
                case 2 ->
                    System.out.println(MessageManager.longestStoredMessage());
                case 3 -> {
                    System.out.print("Enter message ID: ");
                    System.out.println(MessageManager.searchByMessageID(sc.nextLine().trim()));
                }
                case 4 -> {
                    System.out.print("Enter recipient number: ");
                    List<String> found = MessageManager.searchByRecipient(sc.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : found) {
                            System.out.println("\"" + text + "\"");
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Enter message hash: ");
                    System.out.println(MessageManager.deleteByHash(sc.nextLine().trim()));
                }
                case 6 ->
                    System.out.println(MessageManager.reportStored());
                case 0 ->
                    back = true;
                default ->
                    System.out.println("Please choose a number from the menu.");
            }
        }
    }

    //  Keeps asking until the user types a whole number.

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

   
}
