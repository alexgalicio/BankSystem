package transactions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Transaction {
    private String transactionID;
    private String type;
    private String date;
    private double amount;
    private String sender;

    public Transaction(String type, double amount, String sender) {
        this.transactionID = generateTransactionId();
        this.type = type;
        this.date = getCurrentTimestamp();
        this.amount = amount;
        this.sender = sender;
    }

    private String generateTransactionId() {
        String prefix = "OOP";
        int digitsCount = 5;

        StringBuilder referenceNumber = new StringBuilder(prefix);
        LocalDate currentDate = LocalDate.now();
        String yearDigit = currentDate.format(DateTimeFormatter.ofPattern("yy"));
        referenceNumber.append(yearDigit);

        Random random = new Random();

        for (int i = 0; i < digitsCount; i++) {
            int digit = random.nextInt(10);
            referenceNumber.append(digit);
        }

        return referenceNumber.toString();
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        String dateString = sdf.format(new Date());

        dateString = addOrdinalSuffix(dateString);

        return dateString;
    }

    private String addOrdinalSuffix(String dateString) {
        String[] parts = dateString.split(" ");
        String day = parts[0];

        int dayValue = Integer.parseInt(day);
        String suffix;

        if (dayValue >= 11 && dayValue <= 13) {
            suffix = "th";
        } else {
            suffix = switch (dayValue % 10) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }

        parts[0] = day + suffix;

        return String.join(" ", parts);
    }

    public String getTransactionId() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
