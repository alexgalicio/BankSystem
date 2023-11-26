package main;

import transactions.TransactionManager;

import java.util.*;

import static util.Interface.printTitle;
import static util.TextFormatter.*;
import static util.TextFormatter.print;

public class Admin {
    private Scanner scanner;
    private TransactionManager transactionManager;

    public Admin(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        scanner = new Scanner(System.in);
    }

    public void create(List<User> userList) {
        printTitle("                       Create New Account                       ");
        System.out.print("Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Email Address: ");
        String email = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        int birthYear = getValidBirthYear();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthYear;

        if (age < 18) {
            printError("\nYou must be 18 years old or older to create an account.");
            return;
        }

        String username;
        boolean isUsernameTaken;
        do {
            System.out.print("Username: ");
            username = scanner.nextLine();
            if (username.length() > 6) {
                printError("\nUsername must be 6 characters or fewer. Please try again.");
                isUsernameTaken = true;
            } else {
                isUsernameTaken = findUserByUsername(userList, username) != null;
                if (isUsernameTaken) {
                    printError("\nUsername already exists. Please choose a different username.");
                }
            }
        } while (username.length() > 6 || isUsernameTaken);


        String password;
        do {
            System.out.print("Password: ");
            password = scanner.nextLine();
            if (password.length() > 6) {
                printError("\nPassword must be 6 characters or fewer. Please try again.");
            }
        } while (password.length() > 6);

        User newUser = new User(username, password);
        newUser.setName(name);
        newUser.setEmailAddress(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setAge(age);

        userList.add(newUser);

        printSuccess("\nAccount has been successfully created!");
    }

    private void search(List<User> userList) {
        System.out.print("Search: ");
        String keyword = scanner.nextLine();

        List<User> searchResults = new ArrayList<>();
        for (User user : userList) {
            if (user.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    user.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                    String.valueOf(user.getAccountNumber()).contains(keyword)) {
                searchResults.add(user);
            }
        }

        if (searchResults.isEmpty()) {
            printError("\nNo matching users found.");
        } else {
            printHeader("\nMatching Users\n");
            for (User user : searchResults) {
                transactionManager.printUserDetails(user);
            }
        }
    }

    private void edit(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            System.out.print("\nName: ");
            String name = scanner.nextLine();
            user.setName(name);

            System.out.print("Email Address: ");
            String email = scanner.nextLine();
            user.setEmailAddress(email);

            System.out.print("Phone Number: ");
            String phoneNumber = scanner.nextLine();
            user.setPhoneNumber(phoneNumber);

            System.out.print("Age: ");
            int age = scanner.nextInt();
            user.setAge(age);
            scanner.nextLine();

            printSuccess("\nAccount updated successfully.");
        } else {
            printError("\nUser not found.");
        }
    }

    private void delete(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            System.out.print("\nAre you sure you want to delete @" + user.getUsername() + "? [Y/N]: ");
            String confirmation = scanner.next();

            if (confirmation.equalsIgnoreCase("Y")) {
                userList.remove(user);
                printSuccess("\nUser @" + user.getUsername() + " is successfully deleted.");
            } else {
                printError("\nUser deletion canceled.");
            }
        } else {
            printError("\nUser not found.");
        }
    }

    public void accountManagement(List<User> userList) {
        printTitle("                       Account Management                       ");
        print("[", "1", "] ", "Create New Account");
        print("[", "2", "] ", "Search Account");
        print("[", "3", "] ", "Edit Account");
        print("[", "4", "] ", "Delete Account");
        print("[", "5", "] ", "Back");

        String choice = getUserChoice();

        switch (choice) {
            case "1" -> {
                create(userList);
                pause();
            }
            case "2" -> {
                printTitle("                         Search Account                         ");
                search(userList);
                pause();
            }
            case "3" -> {
                printTitle("                          Edit Account                          ");
                edit(userList);
                pause();
            }
            case "4" -> {
                printTitle("                         Delete Account                         ");
                delete(userList);
                pause();
            }
            case "5" -> {
            }
            default -> {
                printError("Invalid input. Please try again.");
                pause();
            }
        }
    }

    private void generateStatement(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            transactionManager.printTransactionHistory(user);
        } else {
            printError("\nUser not found.");
        }
    }

    private void viewAllStatements(List<User> userList) {
        for (User user : userList) {
            transactionManager.printTransactionHistory(user);
        }
    }

    public void accountStatement(List<User> userList) {
        printTitle("                       Account Statement                        ");
        print("[", "1", "] ", "Generate Statement");
        print("[", "2", "] ", "View All Statements");
        print("[", "3", "] ", "Back");

        String choice = getUserChoice();

        switch (choice) {
            case "1" -> {
                printTitle("                       Generate Statement                       ");
                generateStatement(userList);
                pause();
            }
            case "2" -> {
                printTitle("                       View All Statement                       ");
                viewAllStatements(userList);
                pause();
            }
            case "3" -> {
            }
            default -> {
                printError("Invalid choice. Please try again.");
                pause();
            }
        }
    }

    private void freezeAccount(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            if (user.getAccountStatus() == AccountStatus.FROZEN) {
                printWarning("\nAccount is already frozen.");
            } else {
                user.setAccountStatus(AccountStatus.FROZEN);
                printSuccess("\nThe account for user @" + user.getUsername() + " has been successfully frozen.");
            }
        } else {
            printError("\nUser not found.");
        }
    }

    private void reactivateAccount(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            if (user.getAccountStatus() == AccountStatus.ACTIVE) {
                printWarning("\nAccount is already active.");
            } else {
                user.setAccountStatus(AccountStatus.ACTIVE);
                printSuccess("\nThe account for user @" + user.getUsername() + " has been successfully reactivated.");
            }
        } else {
            printError("\nUser not found.");
        }
    }

    public void accountStatus(List<User> userList) {
        printTitle("                         Account Status                         ");

        print("[", "1", "] ", "Freeze/Deactivate Account");
        print("[", "2", "] ", "Reactivate Account");
        print("[", "3", "] ", "Back");

        String choice = getUserChoice();

        printHeader("ACCOUNT STATUS");
        switch (choice) {
            case "1" -> {
                printTitle("                         Freeze Account                         ");
                freezeAccount(userList);
            }
            case "2" -> {
                printTitle("                       Reactivate Account                       ");
                reactivateAccount(userList);
            }
            case "3" -> {
            }
            default -> {
                printError("Invalid choice. Please try again.");
            }
        }
        pause();
    }

    public void performTransaction(List<User> userList) {
        printTitle("                         Transactions                         ");

        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            if (user.getAccountStatus() == AccountStatus.FROZEN) {
                printError("\nAccount is frozen. Unable to perform transactions.");
            } else {
                transactionMenu();

                String choice = getUserChoice();

                switch (choice) {
                    case "1" -> {
                        transactionManager.deposit(user);
                    }
                    case "2" -> {
                        transactionManager.withdraw(user);
                        pause();
                    }
                    case "3" -> {
                        printTitle("                          Bank Transfer                         ");
                        System.out.print("Receiver: ");
                        String receiverUsername = scanner.next();

                        User receiver = findUserByUsername(userList, receiverUsername);
                        if (receiver != null) {
                            transactionManager.transfer(user, receiver);
                        } else {
                            printError("Receiver not found.");
                        }
                        pause();
                    }
                    case "4" -> {
                        transactionManager.exchangeCurrency(user);
                        pause();
                    }
                    case "5" -> {
                        transactionManager.payBills(user);
                        pause();
                    }

                    case "6" -> {
                        printTitle("                         Account Details                        ");
                        transactionManager.printUserDetails(user);
                        transactionManager.printTransactionHistory(user);
                        pause();
                    }
                    case "7" -> {
                        transactionManager.aboutUs();
                    }
                    case "8" -> {
                    }
                    default -> {
                        printError("Invalid choice. Please try again.");
                    }
                }
            }
        } else {
            printError("\nUser not found.");
        }
        pause();
    }

    private int getValidBirthYear() {
        int birthYear;

        System.out.print("Birth Year: ");
        try {
            birthYear = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            printError("Invalid input. Please enter a valid birth year.");
            scanner.nextLine();

            return getValidBirthYear();
        }

        return birthYear;
    }

    private User findUserByUsername(List<User> userList, String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
