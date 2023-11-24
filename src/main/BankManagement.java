package main;

import transactions.TransactionManager;

import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;
import static util.TextFormatter.*;

public class BankManagement {
    private List<User> users;
    private TransactionManager transactionManager;
    private Admin admin;
    private Scanner scanner;

    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    public BankManagement() {
        users = new ArrayList<>();
        transactionManager = new TransactionManager();
        admin = new Admin(transactionManager);
        scanner = new Scanner(System.in);

        setDefaultUser();
        menu();
    }

    public void setDefaultUser() {
        User defaultUser = new User("user", "user");
        defaultUser.setName("Default User");
        defaultUser.setEmailAddress("default@example.com");
        defaultUser.setPhoneNumber("09974033048");
        defaultUser.setAge(25);
        defaultUser.setAccountNumber(1103651839);
        defaultUser.setBalance(10000);

        users.add(defaultUser);

        User newUser = new User("alex", "alex");
        newUser.setName("Alex Galicio");
        newUser.setEmailAddress("alexisjoygalicio@gmail.com");
        defaultUser.setPhoneNumber("09975182932");
        defaultUser.setAge(20);
        newUser.setAccountNumber(1103651839);
        newUser.setBalance(100);

        users.add(newUser);
    }

    private void menu() {
        while (true) {
            print("[", "1", "] ", "User");
            print("[", "2", "] ", "Admin");
            print("[", "3", "] ", "Exit");

            String choice = getUserChoice();

            switch (choice) {
                case "1" -> loginUser();
                case "2" -> loginAdmin();
                case "3" -> {
                    System.out.println(colorize("Thank You!", BOLD(), BACK_COLOR(214), BLACK_TEXT()));
                    System.exit(0);
                }
                default -> printError("Invalid choice. Please try again.");
            }
        }
    }

    private void loginUser() {
        print("[", "1", "] ", "Login");
        print("[", "2", "] ", "Signup");
        print("[", "3", "] ", "Back");

        String choice = getUserChoice();

        switch (choice) {
            case "1" -> {
                int loginAttempts = 3;
                while (loginAttempts > 0) {
                    System.out.println("Please enter you login details");
                    System.out.print("Username: ");
                    String username = scanner.nextLine();

                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    User user = findUser(username, password);

                    if (user == null) {
                        loginAttempts--;
                        if (loginAttempts > 0) {
                            printWarning("Incorrect username or password. " + (loginAttempts > 1 ? loginAttempts +
                                    " attempts remaining." : "1 attempt remaining."));
                        }
                    } else if (user.getAccountStatus() == AccountStatus.FROZEN) {
                        printError("Account is frozen. Unable to login.");
                        return;
                    } else {
                        verifyLoginDetails();
                        userMenu(user);
                        return;
                    }
                }
                printError("Maximum login attempts reached. Exiting program.");
                System.exit(0);
            }
            case "2" -> admin.create(users);
            case "3" -> {
                return;
            }
            default -> printError("Invalid choice. Please try again.");
        }
    }

    private void userMenu(User user) {
        System.out.println(colorize("\nWelcome back, " + user.getName() + "! ", TEXT_COLOR(93)));

        while (true) {
            transactionMenu();
            String choice = getUserChoice();

            switch (choice) {
                case "1" -> {
                    transactionManager.deposit(user);
                }
                case "2" -> {
                    transactionManager.withdraw(user);
                }
                case "3" -> {
                    printHeader("BANK TRANSFER");
                    System.out.print("Receiver: ");
                    String receiverUsername = scanner.next();

                    User receiver = findUserByUsername(receiverUsername);
                    if (receiver != null) {
                        transactionManager.transfer(user, receiver);
                    } else {
                        printError("Receiver not found.");
                    }
                }
                case "4" -> {
                    transactionManager.exchangeCurrency(user);
                }
                case "5" -> {
                    transactionManager.payBills(user);
                }
                case "6" -> {
                    transactionManager.printUserDetails(user);
                    transactionManager.printTransactionHistory(user);
                }
                case "7" -> {
                    aboutUs();
                }
                case "8" -> {
                    return;
                }
                default -> printError("Invalid choice. Please try again.");
            }
        }
    }

    private void loginAdmin() {
        int loginAttempts = 3;

        while (true) {
            System.out.print("Username: ");
            String adminUsername = scanner.nextLine();

            System.out.print("Password: ");
            String adminPassword = scanner.nextLine();

            if (isAdminAuthenticated(adminUsername, adminPassword)) {
                verifyLoginDetails();

                adminMenu();
                return;
            } else {
                loginAttempts--;
                if (loginAttempts > 0) {
                    printWarning("Incorrect username or password. " + (loginAttempts > 1 ? loginAttempts +
                            " attempts remaining." : "1 attempt remaining."));
                } else {
                    printError("Maximum login attempts reached. Exiting program.");
                    System.exit(0);
                }
            }
        }
    }

    private void adminMenu() {
        System.out.println(colorize("\nWelcome back, Admin!", TEXT_COLOR(93)));

        while (true) {
            System.out.println();
            print("[", "1", "] ", "Account Management");
            print("[", "2", "] ", "Transaction");
            print("[", "3", "] ", "Account Statement");
            print("[", "4", "] ", "Account Status");
            print("[", "5", "] ", "Logout");

            String choice = getUserChoice();

            switch (choice) {
                case "1" -> admin.accountManagement(users);
                case "2" -> admin.performTransaction(users);
                case "3" -> admin.accountStatement(users);
                case "4" -> admin.accountStatus(users);
                case "5" -> {
                    return;
                }
                default -> printError("Invalid choice. Please try again.");
            }
        }
    }

    private void aboutUs() {
        System.out.println("Group No. 2");
        System.out.println("Clemente, Daniel Francis");
        System.out.println("De Jesus, Novie Anne");
        System.out.println("Galicio, Alexis Joy B.");
        System.out.println("Gonzales, Jew-Alec Zandrea");
        System.out.println("Pangan, Daniel");
        System.out.println("Info:");
        System.out.println("some info about the project.");
    }

    private boolean isAdminAuthenticated(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }

    private void verifyLoginDetails() {
        System.out.println("Verifying your login details, please wait...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printSuccess("Login successful!");
        scanner.nextLine();
    }

    private User findUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

}
