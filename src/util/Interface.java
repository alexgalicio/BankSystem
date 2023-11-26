package util;

import java.io.Console;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.diogonunes.jcolor.Attribute;
import transactions.Transaction;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;
import static util.TextFormatter.printDetails;

public class Interface {

    public static void printTitle(String titleText) {
        cls();

        System.out.println();
        System.out.print(colorize("   _.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._   ", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" ,'", TEXT_COLOR(57)));
        System.out.print(colorize("_.-._.-._.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._", TEXT_COLOR(135)));
        System.out.print(colorize("`. ", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(                                                                )", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.print(colorize(" )", TEXT_COLOR(135)));
        System.out.print(colorize("                    Bank Management System                    ", TEXT_COLOR(40)));
        System.out.print(colorize("( ", TEXT_COLOR(135)));
        System.out.print(colorize("(", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(", TEXT_COLOR(135)));
        System.out.print(colorize(titleText, TEXT_COLOR(165)));
        System.out.print(colorize(")", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.print(colorize(" )                                                              ( ", TEXT_COLOR(135)));
        System.out.print(colorize("(", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(_.-._.-._.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._)", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" `._.-._.-._.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._.' ", TEXT_COLOR(57)));
        System.out.println();
        System.out.println();
    }

    public static void userTitleText(String userTitle, String titleText, String left, String right) {
        cls();

        System.out.println();
        System.out.print(colorize("   _.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._   ", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" ,'", TEXT_COLOR(57)));
        System.out.print(colorize("_.-._.-._.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._", TEXT_COLOR(135)));
        System.out.print(colorize("`. ", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(                                                                )", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.print(colorize(" )                    ", TEXT_COLOR(135)));
        System.out.print(colorize("Bank Management System", TEXT_COLOR(40)));
        System.out.print(colorize("                    ( ", TEXT_COLOR(135)));
        System.out.print(colorize("(", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(                           ", TEXT_COLOR(135)));
        System.out.print(colorize("          ", YELLOW_TEXT()));
        System.out.print(colorize("                           )", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.print(colorize(" )", TEXT_COLOR(135)));
        System.out.print(colorize(userTitle, TEXT_COLOR(165)));
        System.out.print(colorize("( ", TEXT_COLOR(135)));
        System.out.print(colorize("(", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(", TEXT_COLOR(135)));

        int titleLength = titleText.length();
        int spacesBeforeTitle = Math.max(0, 6 - titleLength);
        for (int i = 0; i < spacesBeforeTitle; i++) {
            System.out.print(" ");
        }
        System.out.print(colorize(left, TEXT_COLOR(165)));
        System.out.print(colorize(titleText, TEXT_COLOR(255)));
        System.out.print(colorize(right, TEXT_COLOR(165)));
        System.out.print(colorize(")", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.print(colorize(" )                                                              ( ", TEXT_COLOR(135)));
        System.out.print(colorize("(", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize("( ", TEXT_COLOR(57)));
        System.out.print(colorize("(_.-._.-._.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._)", TEXT_COLOR(135)));
        System.out.print(colorize(" )", TEXT_COLOR(57)));
        System.out.println();
        System.out.print(colorize(" `._.-._.-._.-._.-._.-._.-_.-._.-._.-._.-._.-._.-._.-._.-._.-._.-._.' ", TEXT_COLOR(57)));
        System.out.println();
    }

    public static void printTransactionDetail(Transaction transaction) {
        System.out.print("|    ");
        System.out.print(transaction.getTransactionId());
        System.out.print("    |   ");

        System.out.print(transaction.getType());
        if (transaction.getType().length() == 8) {
            System.out.print("\t      |   ");
        } else if (transaction.getType().length() == 7) {
            System.out.print("\t      |   ");
        } else if (transaction.getType().length() == 10) {
            System.out.print("     |   ");
        } else {
            System.out.print("   |   ");
        }

        System.out.print(formatAmount(transaction.getAmount()));
        String digits = Double.toString(transaction.getAmount()).replaceAll("[^0-9-]", "");
        int amountLength = digits.length();

        if (amountLength == 4) {
            System.out.print("     |   ");
        } else if (amountLength == 5)
            System.out.print("    |   ");
        else if (amountLength == 6)
            System.out.print("   |   ");
        else
            System.out.print("  |   ");

        System.out.print(transaction.getDate());
        System.out.print("   |");
        System.out.println();
        System.out.print("|--------------------------------------------------------------------------------|");
        System.out.println();
    }

    public static double formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(amount));
    }


    public static void clearScreen() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    public static void cls() {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
