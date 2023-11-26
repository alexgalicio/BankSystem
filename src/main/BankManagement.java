package main;

import transactions.TransactionManager;

import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;
import static util.Interface.*;
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

    private void setDefaultUser() {
        User defaultUser = new User("user", "user");
        defaultUser.setName("Default User");
        defaultUser.setEmailAddress("default@example.com");
        defaultUser.setPhoneNumber("09974033048");
        defaultUser.setAge(25);
        defaultUser.setAccountNumber(1103651839);
        defaultUser.setBalance(10000);

        users.add(defaultUser);

        User newUser = new User("alexis", "alexis");
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
            printTitle("                   Welcome to the Login Menu                    ");

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
                default -> {
                    printError("Invalid input. Please try again.");
                    pause();
                }
            }
        }
    }

    private void loginUser() {
        printTitle("                          User Login                            ");

        print("[", "1", "] ", "Login");
        print("[", "2", "] ", "Signup");
        print("[", "3", "] ", "Back");

        String choice = getUserChoice();


        switch (choice) {
            case "1" -> {
                int loginAttempts = 3;
                while (loginAttempts > 0) {
                    printTitle("                          User Login                            ");
                    System.out.print("Username: ");
                    String username = scanner.nextLine();

                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    User user = findUser(username, password);

                    if (user == null) {
                        loginAttempts--;
                        if (loginAttempts > 0) {
                            printWarning("\nIncorrect username or password. " + (loginAttempts > 1 ? loginAttempts +
                                    " attempts remaining." : "1 attempt remaining."));
                            pause();
                        }
                    } else if (user.getAccountStatus() == AccountStatus.FROZEN) {
                        printError("\nAccount is frozen. Unable to login.");
                        pause();
                        return;
                    } else {
                        verifyLoginDetails();
                        userMenu(user);
                        return;
                    }
                }
                printError("\nMaximum login attempts reached. Exiting program.");
                System.exit(0);
            }
            case "2" -> {
                admin.create(users);
                pause();
            }
            case "3" -> {
            }
            default -> {
                printError("\nInvalid input. Please try again.");
                pause();
            }
        }
    }

    private void userMenu(User user) {
        while (true) {
            userTitleText("                          User Menu                           ", user.getUsername(),
                    "                          [", "]                              ");
            transactionMenu();
            String choice = getUserChoice();

            switch (choice) {
                case "1" -> {
                    transactionManager.deposit(user);
                    pause();
                }
                case "2" -> {
                    transactionManager.withdraw(user);
                    pause();
                }
                case "3" -> {
                    printTitle("                          Bank Transfer                         ");
                    System.out.print("Receiver: ");
                    String receiverUsername = scanner.next();

                    User receiver = findUserByUsername(receiverUsername);
                    if (receiver != null) {
                        transactionManager.transfer(user, receiver);
                    } else {
                        printError("\nReceiver not found.");
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
                    return;
                }
                default -> {
                    printError("Invalid input. Please try again.");
                    pause();
                }
            }
        }
    }

    private void loginAdmin() {
        printTitle("                          Admin Login                           ");

        int loginAttempts = 3;

        while (true) {
            printTitle("                          Admin Login                           ");
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
                    pause();
                } else {
                    printError("Maximum login attempts reached. Exiting program.");
                    System.exit(0);
                }
            }
        }
    }

    private void adminMenu() {
        while (true) {
            userTitleText("                          Admin Menu                          ", "Admin",
                    "                           [", "]                             ");

            System.out.println();
            print("[", "1", "] ", "Account Management");
            print("[", "2", "] ", "Transaction");
            print("[", "3", "] ", "Account Statement");
            print("[", "4", "] ", "Account Status");
            print("[", "5", "] ", "Logout");

            String choice = getUserChoice();

            switch (choice) {
                case "1" -> {
                    admin.accountManagement(users);
                }
                case "2" -> {
                    admin.performTransaction(users);
                }
                case "3" -> {
                    admin.accountStatement(users);
                }
                case "4" -> admin.accountStatus(users);
                case "5" -> {
                    return;
                }
                default -> {
                    printError("Invalid input. Please try again.");
                    pause();
                }
            }
        }
    }

    private boolean isAdminAuthenticated(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }

    private void verifyLoginDetails() {
        System.out.println("\nVerifying your login details, please wait...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printSuccess("\nLogin successful!");
        pause();
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
