package main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    // user basic info
    private String username;
    private String password;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private int age;

    // bank info
    private long accountNumber;
    private double balance;
    private String creationDate;
    public AccountStatus accountStatus;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = null;
        this.emailAddress = null;
        this.phoneNumber = null;

        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
        this.creationDate = getCurrentDate();
        this.accountStatus = AccountStatus.ACTIVE;
    }

    private long generateAccountNumber() {
        return  username.hashCode() + System.currentTimeMillis();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        return sdf.format(new Date());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
