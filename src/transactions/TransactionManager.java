package transactions;

import main.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.TEXT_COLOR;
import static util.Interface.*;
import static util.TextFormatter.*;

public class TransactionManager {
    private List<Transaction> transactions;
    private Scanner scanner;
    private final double feePercentage = 0.05;

    public String[] currencies = {
            "Pound Sterling", "Swiss Franc", "Canadian Dollar",
            "United States Dollar", "Kuwaiti Dinar", "Omani Rial",
            "Euro", "Japanese Yen"
    };

    public double[] exchangeRates = {
            68.89, 62.23, 40.63, 56.14, 181.79, 145.87, 60.07, 0.37
    };

    public TransactionManager() {
        this.transactions = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void deposit(User user) {
        printTitle("                            Deposit                             ");
        double depositAmount = getUserAmount("DEPOSIT");

        if (depositAmount < 100 || depositAmount > 50000) {
            printError("\nInvalid amount. Deposit amount must be between 100 and 50,000.");
            return;
        }
        user.setBalance(user.getBalance() + depositAmount);
        Transaction depositTransaction = new Transaction("DEPOSIT", depositAmount, user.getUsername());
        transactions.add(depositTransaction);
        printSuccess("\nYour deposit of " + formatAmount(depositAmount) + " has been processed successfully.");
        System.out.println("New balance: " + formatAmount(user.getBalance()));
    }

    public void withdraw(User user) {
        printTitle("                            Withdraw                            ");
        double withdrawalAmount = getUserAmount("WITHDRAWAL");

        if (withdrawalAmount < 100 || withdrawalAmount > 50000) {
            printError("\nInvalid amount. Withdrawal amount must be between 100 and 50,000.");
            return;
        }

        if (withdrawalAmount > user.getBalance()) {
            printError("\nInsufficient Balance.");
            return;
        }

        user.setBalance(user.getBalance() - withdrawalAmount);

        Transaction withdrawTransaction = new Transaction("WITHDRAWAL", withdrawalAmount, user.getUsername());
        transactions.add(withdrawTransaction);

        printSuccess("\nYour deposit of " + formatAmount(withdrawalAmount) + " has been processed successfully.");
        System.out.println("Remaining balance: " + formatAmount(user.getBalance()));
    }

    public void transfer(User sender, User receiver) {
        if (sender.getUsername().equals(receiver.getUsername())) {
            printError("\nYou can't transfer money to your own account.");
            return;
        }

        double transferAmount = getUserAmount("TRANSFER");

        if (transferAmount < 100 || transferAmount > sender.getBalance()) {
            printError("\nInvalid transfer amount or insufficient balance.");
            return;
        }

        double fee = transferAmount * feePercentage;
        double transferAmountWithFee = transferAmount - fee;

        sender.setBalance(sender.getBalance() - transferAmount);
        receiver.setBalance(receiver.getBalance() + transferAmountWithFee);

        Transaction senderTransaction = new Transaction("TRANSFER OUT", -transferAmount, sender.getUsername());
        transactions.add(senderTransaction);

        Transaction receiverTransaction = new Transaction("TRANSFER IN ", transferAmountWithFee, receiver.getUsername());
        transactions.add(receiverTransaction);

        printSuccess("\nTransfer of " + formatAmount(transferAmountWithFee) + " from " + sender.getUsername() + " to "
                + receiver.getUsername() + " was successful.");
        System.out.println("Transaction Fee (5%): " + formatAmount(fee));
        System.out.println("Remaining balance: " + formatAmount(sender.getBalance()));
    }

    public void exchangeCurrency(User user) {
        printTitle("                        Exchange Currency                       ");
        for (int i = 0; i < currencies.length; i++) {
            printCurrency("[", (i + 1), "] ", currencies[i], " - ", exchangeRates[i]);
        }

        int fromCurrencyIndex;

        while (true) {
            try {
                System.out.print("\nChoose source currency: ");
                fromCurrencyIndex = scanner.nextInt() - 1;

                if (fromCurrencyIndex < 0 || fromCurrencyIndex >= currencies.length) {
                    printError("\nInvalid choice. Please choose a valid currency.");
                } else {
                    double exchangeAmount = getUserAmount("EXCHANGE");

                    if (exchangeAmount < 100 || exchangeAmount > 50000) {
                        printError("\nInvalid amount. The transaction amount must be between 100 and 50,000.");
                        pause();
                        return;
                    }

                    double exchangedAmountInPesos = exchangeAmount * exchangeRates[fromCurrencyIndex];

                    double fee = exchangedAmountInPesos * feePercentage;
                    double exchangedAmountWithFee = exchangedAmountInPesos - fee;

                    Transaction exchangeTransaction = new Transaction("EXCHANGE", exchangeAmount, user.getUsername());
                    transactions.add(exchangeTransaction);

                    printSuccess("\n" + formatAmount(exchangeAmount) + " " + currencies[fromCurrencyIndex] + " exchanged to "
                            + formatAmount(exchangedAmountWithFee) + " Pesos.");
                    System.out.println("Transaction Fee (5%): " + formatAmount(fee));
                    break;
                }
            } catch (java.util.InputMismatchException e) {
                scanner.nextLine();
                printError("\nInvalid input. Please enter an integer.");
            }
        }
    }

    public void payBills(User user) {
        printTitle("                          Bill Payment                          ");
        print("[", "1", "] ", "Electricity");
        print("[", "2", "] ", "Water");
        print("[", "3", "] ", "WiFi");

        String billType = getUserChoice();

        if (billType.equals("1") || billType.equals("2") || billType.equals("3")) {
            System.out.print("Enter reference code: ");
            String referenceCode = scanner.next();
            double billAmount = generateRandomAmount(user);
            double fee = billAmount * feePercentage;

            System.out.println("\nReference Code: " + referenceCode);
            System.out.println("Bill Amount: " + formatAmount(billAmount));
            System.out.println("Transaction Fee: " + formatAmount(fee));
            System.out.println("Total Amount to Pay: " + formatAmount(billAmount + fee));

            System.out.print("\nDo you want to proceed with the payment? [Y/N]: ");
            String confirmation = scanner.next();

            if (confirmation.equalsIgnoreCase("y")) {
                double totalAmountToPay = billAmount + fee;
                user.setBalance(user.getBalance() - totalAmountToPay);

                Transaction billPaymentTransaction = new Transaction("BILL PAYMENT", totalAmountToPay, user.getUsername());
                transactions.add(billPaymentTransaction);

                printSuccess("\nBill payment successful.");
                System.out.println("Remaining balance: " + formatAmount(user.getBalance()));
            } else {
                printError("\nBill payment canceled.");
            }
        } else {
            printError("Invalid bill type option.");
        }
    }

    public void printTransactionHistory(User user) {
        System.out.println();
        List<Transaction> transactions = getTransactions();

        boolean hasTransactions = false;

        for (Transaction transaction : transactions) {
            if (transaction.getSender().equals(user.getUsername())) {
                if (!hasTransactions) {
                    printHeader("Transaction History for @" + user.getUsername());
                    hasTransactions = true;
                    System.out.println("\n==================================================================================");
                    System.out.print("|  ");
                    System.out.print(colorize("Transaction ID", TEXT_COLOR(40)));
                    System.out.print("  |       ");
                    System.out.print(colorize("Type", TEXT_COLOR(40)));
                    System.out.print("       |    ");
                    System.out.print(colorize("Amount", TEXT_COLOR(40)));
                    System.out.print("   |            ");
                    System.out.print(colorize("Date", TEXT_COLOR(40)));
                    System.out.print(" \t\t |");
                    System.out.println("\n==================================================================================");
                }
                printTransactionDetail(transaction);
            }
        }

        if (!hasTransactions) {
            printWarning("No transactions for @" + user.getUsername() + " yet.\n");
        }
    }

    public void printUserDetails(User user) {
        printDetails("Username:\t\t", user.getUsername());
        printDetails("Name:\t\t\t", user.getName());
        printDetails("Email:\t\t\t", user.getEmailAddress());
        printDetails("Phone Number:\t\t", user.getPhoneNumber());
        printDetails("Age:\t\t\t", String.valueOf(user.getAge()));
        printDetails("Balance:\t\t", String.valueOf(formatAmount(user.getBalance())));
        printDetails("Account Number:\t\t", String.valueOf(user.getAccountNumber()));
        printDetails("Creation Date:\t\t", user.getCreationDate());
        System.out.println();
    }

    public void aboutUs() {
        cls();
        printHeader(colorize("Group No. 2", TEXT_COLOR(40)));
        System.out.println(colorize("\nMembers:", TEXT_COLOR(40)));
        System.out.println(colorize("\tClemente, Daniel Francis", TEXT_COLOR(165)));
        System.out.println(colorize("\tDe Jesus, Novie Anne", TEXT_COLOR(165)));
        System.out.println(colorize("\tGalicio, Alexis Joy B.", TEXT_COLOR(165)));
        System.out.println(colorize("\tGonzales, Jew-Alec Zandrea", TEXT_COLOR(165)));
        System.out.println(colorize("\tPangan, Daniel", TEXT_COLOR(165)));
        System.out.println(colorize("\nInfo:", TEXT_COLOR(40)));
        System.out.println("\t\tproject description. project description. project description.");
        System.out.println("\tproject description. project description. project description.");
        System.out.println("\tproject description. project description. project description.");
        pause();
    }

    private double generateRandomAmount(User user) {
        double balance = user.getBalance();
        double percentage = 0.03 + (0.02 * new Random().nextDouble()); // 3% to 5% of their balance
        return balance * percentage;
    }

    public double formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(amount));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
