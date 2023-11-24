package transactions;

import main.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
        printHeader("DEPOSIT");
        double depositAmount = getUserAmount();

        if (depositAmount < 100 || depositAmount > 50000) {
            printError("Invalid amount. Deposit amount must be between 100 and 50,000.");
            return;
        }
        user.setBalance(user.getBalance() + depositAmount);
        Transaction depositTransaction = new Transaction("DEPOSIT", depositAmount, user.getUsername());
        transactions.add(depositTransaction);
        printSuccess("Your deposit of " + formatAmount(depositAmount) + " has been processed successfully.");
        System.out.println("New balance: " + formatAmount(user.getBalance()));
    }

    public void withdraw(User user) {
        printHeader("WITHDRAW");
        double withdrawalAmount = getUserAmount();

        if (withdrawalAmount < 100 || withdrawalAmount > 50000) {
            printError("Invalid amount. Withdrawal amount must be between 100 and 50,000.");
            return;
        }

        if (withdrawalAmount > user.getBalance()) {
            printError("Insufficient Balance.");
            return;
        }

        user.setBalance(user.getBalance() - withdrawalAmount);

        Transaction withdrawTransaction = new Transaction("WITHDRAWAL", withdrawalAmount, user.getUsername());
        transactions.add(withdrawTransaction);

        printSuccess("Your deposit of " + formatAmount(withdrawalAmount) + " has been processed successfully.");
        System.out.println("Remaining balance: " + formatAmount(user.getBalance()));
    }

    public void transfer(User sender, User receiver) {
        if (sender.getUsername().equals(receiver.getUsername())) {
            printError("You can't transfer money to your own account.");
            return;
        }

        double transferAmount = getUserAmount();

        if (transferAmount < 100 || transferAmount > sender.getBalance()) {
            printError("Invalid transfer amount or insufficient balance.");
            return;
        }

        double fee = transferAmount * feePercentage;
        double transferAmountWithFee = transferAmount - fee;

        sender.setBalance(sender.getBalance() - transferAmount);
        receiver.setBalance(receiver.getBalance() + transferAmountWithFee);

        Transaction senderTransaction = new Transaction("TRANSFER OUT", -transferAmount, sender.getUsername());
        transactions.add(senderTransaction);

        Transaction receiverTransaction = new Transaction("TRANSFER IN", transferAmountWithFee, receiver.getUsername());
        transactions.add(receiverTransaction);

        printSuccess("Transfer of " + formatAmount(transferAmountWithFee) + " from " + sender.getUsername() + " to "
                + receiver.getUsername() + " was successful.");
        System.out.println("Transaction Fee (5%): " + formatAmount(fee));
        System.out.println("Remaining balance: " + formatAmount(sender.getBalance()));
    }

    public void exchangeCurrency(User user) {
        printHeader("CURRENCIES");
        for (int i = 0; i < currencies.length; i++) {
            printCurrency("[", (i + 1), "] ", currencies[i], " - ", exchangeRates[i]);
        }

        System.out.print("Choose source currency: ");
        int fromCurrencyIndex = scanner.nextInt() - 1;

        if (fromCurrencyIndex < 0 || fromCurrencyIndex >= currencies.length) {
            printError("Invalid choice. Please choose a valid currency.");
            return;
        }

        double exchangeAmount = getUserAmount();

        if (exchangeAmount < 100 || exchangeAmount > 50000) {
            printError("Invalid amount. The transaction amount must be between 100 and 50,000.");
            return;
        }

        double exchangedAmountInPesos = exchangeAmount * exchangeRates[fromCurrencyIndex];

        double fee = exchangedAmountInPesos * feePercentage;
        double exchangedAmountWithFee = exchangedAmountInPesos - fee;

        Transaction exchangeTransaction = new Transaction("EXCHANGE", exchangeAmount, user.getUsername());
        transactions.add(exchangeTransaction);

        printSuccess(formatAmount(exchangeAmount) + " " + currencies[fromCurrencyIndex] + " exchanged to "
                + formatAmount(exchangedAmountWithFee) + " Pesos.");
        System.out.println("Transaction Fee (5%): " + formatAmount(fee));
    }

    public void payBills(User user) {
        printHeader("BILL PAYMENT");
        print("[", "1", "] ", "Electricity");
        print("[", "2", "] ", "Water");
        print("[", "3", "] ", "WiFi");

        String billType = getUserChoice();

        if (billType.equals("1") || billType.equals("2") || billType.equals("3")) {
            System.out.print("Enter reference code: ");
            String referenceCode = scanner.next();
            double billAmount = generateRandomAmount(user);
            double fee = billAmount * feePercentage;

            System.out.println("Reference Code: " + referenceCode);
            System.out.println("Bill Amount: " + formatAmount(billAmount));
            System.out.println("Transaction Fee: " + formatAmount(fee));
            System.out.println("Total Amount to Pay: " + formatAmount(billAmount + fee));

            System.out.print("Do you want to proceed with the payment? [Y/N]: ");
            String confirmation = scanner.next();

            if (confirmation.equalsIgnoreCase("y")) {
                double totalAmountToPay = billAmount + fee;
                user.setBalance(user.getBalance() - totalAmountToPay);

                Transaction billPaymentTransaction = new Transaction("BILL PAYMENT", totalAmountToPay, user.getUsername());
                transactions.add(billPaymentTransaction);

                printSuccess("Bill payment successful.");
                System.out.println("Remaining balance: " + formatAmount(user.getBalance()));
            } else {
                printError("Bill payment canceled.");
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
                }
                printTransactionDetails(transaction);
            }
        }

        if (!hasTransactions) {
            printWarning("No transactions for " + user.getUsername() + " yet.");
            System.out.println();
        }
    }

    public void printTransactionDetails(Transaction transaction) {
        printDetails("Transaction ID: ", transaction.getTransactionId());
        printDetails("Type: ", transaction.getType());
        printDetails("Amount: ", String.valueOf(formatAmount(transaction.getAmount())));
        printDetails("Date: ", transaction.getDate());
        System.out.println();
    }

    public void printUserDetails(User user) {
        printHeader("ACCOUNT DETAILS");
        printDetails("Username: ", user.getUsername());
        printDetails("Name: ", user.getName());
        printDetails("Email: ", user.getEmailAddress());
        printDetails("Phone Number: ", user.getPhoneNumber());
        printDetails("Age: ", String.valueOf(user.getAge()));
        printDetails("Balance: ", String.valueOf(formatAmount(user.getBalance())));
        printDetails("Account Number: ", String.valueOf(user.getAccountNumber()));
        printDetails("Creation Date: ", user.getCreationDate());
        System.out.println();
    }

    private double generateRandomAmount(User user) {
        double balance = user.getBalance();
        double percentage = 0.03 + (0.02 * new Random().nextDouble()); // 3% to 5% of their balance
        return balance * percentage;
    }

    private double formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(amount));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
