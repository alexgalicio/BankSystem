package util;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;
import static util.Interface.printTitle;

public class TextFormatter {

    public static String colorizeString(String openingBracket, String number, String closingBracket, String text, Attribute bracketColor, Attribute textColor) {
        return colorize(openingBracket, bracketColor) + number + colorize(closingBracket, bracketColor) + colorize(text, textColor);
    }

    public static void printCurrency(String openingBracket, int number, String closingBracket, String text, String dash, double currency) {
        System.out.println(colorize(openingBracket, CYAN_TEXT()) + number + colorize(closingBracket, CYAN_TEXT()) + colorize(text, CYAN_TEXT()) + dash + colorize(String.valueOf(currency), CYAN_TEXT()));
    }

    public static void print(String openingBracket, String number, String closingBracket, String text) {
        System.out.println(colorizeString(openingBracket, number, closingBracket, text, CYAN_TEXT(), CYAN_TEXT()));
    }

    public static void printError(String errorMessage) {
        System.out.println(colorize(errorMessage, TEXT_COLOR(196)));
    }

    public static void printSuccess(String successMessage) {
        System.out.println(colorize(successMessage, TEXT_COLOR(46)));
    }

    public static void printWarning(String warningMessage) {
        System.out.println(colorize(warningMessage, Attribute.TEXT_COLOR(226)));
    }

    public static void printDetails(String header, String message) {
        System.out.println(colorize(header, BOLD()) + message);
    }

    public static void printHeader(String header) {
        System.out.println(colorize(header, BLUE_TEXT()));
    }

    public static void invalidInput() {
        Scanner scanner = new Scanner(System.in);
        printError("Invalid input. Please try again.");
        System.out.println("\nPress Enter key to continue...");
        scanner.nextLine();
    }

    public static void pause() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPress Enter key to continue...");
        scanner.nextLine();
    }

    public static String getUserChoice() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(colorize("\nPlease enter your choice: ", TEXT_COLOR(165)));
        String choice = scanner.nextLine();
        System.out.println();

//        try {
//            choice = scanner.nextLine();
//            System.out.println();
//        } catch (InputMismatchException e) {
//            printError("Invalid input. Please enter an integer.");
//            scanner.nextLine();
//
//            return getUserChoice();
//        }
        return choice;
    }

    public static double getUserAmount(String transactionType) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Amount: ");
        double amount;

        try {
            amount = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            printError("\nInvalid input. Please enter an integer.");
            scanner.nextLine();
            pause();

            switch (transactionType) {
                case "DEPOSIT" -> printTitle("                            Deposit                             ");
                case "WITHDRAWAL" -> printTitle("                            Withdraw                            ");
                case "TRANSFER" -> printTitle("                          Bank Transfer                         ");
                case "EXCHANGE" -> printTitle("                        Exchange Currency                       ");
                case "BILL PAYMENT" -> printTitle("                          Bill Payment                          ");
            }

            return getUserAmount(transactionType);
        }
        return amount;
    }

    public static void transactionMenu() {
        System.out.println();
        print("[", "1", "] ", "Deposit");
        print("[", "2", "] ", "Withdraw");
        print("[", "3", "] ", "Transfer");
        print("[", "4", "] ", "Foreign Exchange");
        print("[", "5", "] ", "Pay Bills");
        print("[", "6", "] ", "Account Details");
        print("[", "7", "] ", "About Us");
        print("[", "8", "] ", "Logout");
    }
}
