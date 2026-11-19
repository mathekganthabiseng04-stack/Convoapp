/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.convoapp;
import java.util.Scanner;
/**
 *
 * @author admin
 */
public class Convoapp {

    public static void main(String[] args) {
        


        Scanner input = new Scanner(System.in);
        Login login = null;
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
            login = new Login(name, username, phonenumber, password);

            // RUN VALIDATION CHECKS
            boolean isNameValid = login.checkname();
            boolean isUsernameValid = login.checkusername();
            boolean isPhoneValid = login.checkphonenumber();
            boolean isPasswordValid = login.checkpassword();

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
        boolean loginSuccessful = false;
        
        do {  
            System.out.println("--- LOGIN ---");
            System.out.print("Enter Username or Phone Number:");
            String loginIdentifier = input.nextLine();

            System.out.print("Enter your password :");
            String isloginpassword = input.nextLine();

            // Check if login  match either username OR phonenumber
            // We use your existing checkusername method, and also verify against the registered phonenumber
            if (login.checkusername(loginIdentifier, isloginpassword)) {
                
                System.out.println("Login successful! " + loginIdentifier + " WELCOME TO WECHAT!");
                loginSuccessful = true; // Exits the login loop
            } else {
                System.out.println("Invalid username/phone number or password.");
            }

        } while (!loginSuccessful);
    }
    public class QuickChat {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Only logged-in users may send messages.
        // TODO: replace this stub with your Part 1 login/registration code.
        if (!loginStub(sc)) {
            System.out.println("Login failed. You cannot send messages.");
            return;
        }

        // 2. Welcome message
        System.out.println("Welcome to QuickChat.");

        // 5. User defines how many messages they want to enter
        int maxMessages = readInt(sc, "How many messages do you wish to enter? ");
        int enteredMessages = 0;

        // 4. Run until the user quits
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");

            int option = readInt(sc, "Option: ");

            switch (option) {
                case 1 -> {
                    if (enteredMessages >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        break;
                    }
                    if (handleNewMessage(sc)) {
                        enteredMessages++;
                    }
                }
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> running = false;
                default -> System.out.println("Please choose 1, 2 or 3.");
            }
        }

        // Total accumulated once all messages have been sent
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        System.out.println(Message.printMessages());
    }

    /** Captures one message. Returns true if the message was used (sent/stored/discarded). */
    private static boolean handleNewMessage(Scanner sc) {
        // Recipient
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

        // Message text (max 250 characters)
        String text;
        while (true) {
            System.out.print("Enter your message: ");
            text = sc.nextLine();
            if (text.length() <= 250) {
                System.out.println("Message sent");
                break;
            }
            System.out.println("Please enter a message of less than 250 characters.");
        }

        Message msg = new Message(recipient, text);
        System.out.println("Message ID generated: " + msg.getMessageID());

        // Send / Disregard / Store
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int choice = readInt(sc, "Choice: ");
        String outcome = msg.sentMessage(choice);
        System.out.println(outcome);

        if (choice == 1) {
            System.out.println(msg.toDisplayString());
        } else if (choice == 2) {
            while (readInt(sc, "") != 0) {
                System.out.println("Press 0 to delete the message.");
            }
            System.out.println("Message deleted.");
        } else if (choice != 3) {
            return false; // invalid choice, message not used
        }
        return true;
    }

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

    /** Placeholder for your Part 1 login. Replace with your own Login class. */
    private static boolean loginStub(Scanner sc) {
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        return !user.isBlank() && !pass.isBlank();
    }
}
}
