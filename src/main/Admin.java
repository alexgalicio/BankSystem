package main;

import transactions.TransactionManager;

import java.util.*;

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
        System.out.println("Please add your personal information to create an account.");

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
            printError("You must be 18 years old or older to create an account.");
            return;
        }

        String username;
        boolean isUsernameTaken;
        do {
            System.out.print("Username: ");
            username = scanner.nextLine();
            if (username.length() > 6) {
                printError("Username must be 6 characters or fewer. Please try again.");
                isUsernameTaken = true;
            } else {
                isUsernameTaken = findUserByUsername(userList, username) != null;
                if (isUsernameTaken) {
                    printError("Username already exists. Please choose a different username.");
                }
            }
        } while (username.length() > 6 || isUsernameTaken);


        String password;
        do {
            System.out.print("Password: ");
            password = scanner.nextLine();
            if (password.length() > 6) {
                printError("Password must be 6 characters or fewer. Please try again.");
            }
        } while (password.length() > 6);

        User newUser = new User(username, password);
        newUser.setName(name);
        newUser.setEmailAddress(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setAge(age);

        userList.add(newUser);

        printSuccess("Account has been successfully created!");
        scanner.nextLine();
    }

    private void search(List<User> userList) {
        printHeader("SEARCH ACCOUNT");
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
            printError("No matching users found.");
        } else {
            printHeader("\nMatching Users");
            for (User user : searchResults) {
                printDetails("Name: ", user.getName());
                printDetails("Email Address: ", user.getEmailAddress());
                printDetails("Phone Number: ", user.getPhoneNumber());
                printDetails("Age: ", String.valueOf(user.getAge()));
                printDetails("Username: ", user.getUsername());
                printDetails("Date of Creation: ", user.getCreationDate());
                System.out.println();
            }
        }
    }

    private void edit(List<User> userList) {
        printHeader("EDIT ACCOUNT");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            System.out.print("Name: ");
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

            printSuccess("Account updated successfully.");
        } else {
            printError("User not found.");
        }
    }

    private void delete(List<User> userList) {
        printHeader("DELETE ACCOUNT");
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            System.out.print("Are you sure you want to delete @" + user.getUsername() + "? [Y/N]: ");
            String confirmation = scanner.next();

            if (confirmation.equalsIgnoreCase("Y")) {
                userList.remove(user);
                printSuccess("User @" + user.getUsername() + " is successfully deleted.");
            } else {
                printError("User deletion canceled.");
            }
        } else {
            printError("User not found.");
        }
    }

    public void accountManagement(List<User> userList) {
        print("[", "1", "] ", "Create New Account");
        print("[", "2", "] ", "Search Account");
        print("[", "3", "] ", "Edit Account");
        print("[", "4", "] ", "Delete Account");
        print("[", "5", "] ", "Back");

        String choice = getUserChoice();

        switch (choice) {
            case "1" -> create(userList);
            case "2" -> search(userList);
            case "3" -> edit(userList);
            case "4" -> delete(userList);
            case "5" -> {
                return;
            }
            default -> printError("Invalid choice. Please try again.");
        }
    }

    private void generateStatement(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            transactionManager.printTransactionHistory(user);
        } else {
            printError("User not found.");
        }
    }

    private void viewAllStatements(List<User> userList) {
        for (User user : userList) {
            transactionManager.printTransactionHistory(user);
        }
    }

    public void accountStatement(List<User> userList) {
        print("[", "1", "] ", "Generate Statement");
        print("[", "2", "] ", "View All Statements");
        print("[", "3", "] ", "Back");

        String choice = getUserChoice();

        printHeader("ACCOUNT STATEMENT");
        switch (choice) {
            case "1" -> generateStatement(userList);
            case "2" -> viewAllStatements(userList);
            case "3" -> {
                return;
            }
            default -> printError("Invalid choice. Please try again.");
        }
    }

    private void freezeAccount(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            if (user.getAccountStatus() == AccountStatus.FROZEN) {
                printWarning("Account is already frozen.");
            } else {
                user.setAccountStatus(AccountStatus.FROZEN);
                printSuccess("The account for user @" + user.getUsername() + " has been successfully frozen.");
            }
        } else {
            printError("User not found.");
        }
    }

    private void reactivateAccount(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            if (user.getAccountStatus() == AccountStatus.ACTIVE) {
                printWarning("Account is already active.");
            } else {
                user.setAccountStatus(AccountStatus.ACTIVE);
                printSuccess("The account for user @" + user.getUsername() + " has been successfully reactivated.");
            }
        } else {
            printError("User not found.");
        }
    }

    public void accountStatus(List<User> userList) {
        print("[", "1", "] ", "Freeze/Deactivate Account");
        print("[", "2", "] ", "Reactivate Account");
        print("[", "3", "] ", "Back");

        String choice = getUserChoice();

        printHeader("ACCOUNT STATUS");
        switch (choice) {
            case "1" -> freezeAccount(userList);
            case "2" -> reactivateAccount(userList);
            case "3" -> {
                return;
            }
            default -> printError("Invalid choice. Please try again.");
        }
    }

    public void performTransaction(List<User> userList) {
        System.out.print("Username: ");
        String username = scanner.next();

        User user = findUserByUsername(userList, username);

        if (user != null) {
            if (user.getAccountStatus() == AccountStatus.FROZEN) {
                printError("Account is frozen. Unable to perform transactions.");
            } else {
                transactionMenu();

                String choice = getUserChoice();

                switch (choice) {
                    case "1" -> transactionManager.deposit(user);
                    case "2" -> transactionManager.withdraw(user);
                    case "3" -> {
                        System.out.println("BANK TRANSFER");
                        System.out.print("Receiver: ");
                        String receiverUsername = scanner.next();

                        User receiver = findUserByUsername(userList, receiverUsername);
                        if (receiver != null) {
                            transactionManager.transfer(user, receiver);
                        } else {
                            printError("Receiver not found.");
                        }
                    }
                    case "4" -> transactionManager.exchangeCurrency(user);
                    case "5" -> transactionManager.payBills(user);

                    case "6" -> {
                        transactionManager.printUserDetails(user);
                        transactionManager.printTransactionHistory(user);
                    }
                    case "7" -> {
                        return;
                    }
                    default -> printError("Invalid choice. Please try again.");
                }
            }
        } else {
            printError("User not found.");
        }
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
