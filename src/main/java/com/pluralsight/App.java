package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App {
    static Scanner scan = new Scanner(System.in);
    static ArrayList<Transaction> transactionsList = readTransactions();
    static LocalDate today = LocalDate.now();
    static LocalDate firstDayOfMonth = today.withDayOfMonth(1);

    public static void main(String[] args) {
        displayUserGreeting();
        runHomeScreen();
    }

    private static void displayUserGreeting() {
        System.out.println("====================================");
        System.out.println("Hello! Ready to track your spending?");
        System.out.println("Your Financial Overview Starts Here.");
        System.out.println("====================================");
        System.out.println();
    }

    private static void runHomeScreen() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.print("""
                \nHOME SCREEN
                1) Open Ledger
                2) Make Payment
                3) Add Deposit
                X) Exit Application
                Enter command: \s""");

            String choice = scan.nextLine().toLowerCase().trim();

            switch (choice) {
                case "1" -> runLedgerScreen();
                case "2" -> addNewPayment();
                case "3" -> addNewDeposit();
                case "x" -> isRunning = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private static void runLedgerScreen() {
        boolean inLedgerScreen = true;

        while (inLedgerScreen) {
            System.out.print("""
                \n     LEDGER SCREEN
                  1) Open all Reports
                  A) Display all Transactions
                  B) Display all Deposits
                  C) Display all Payments
                  R) Return to Home Screen
                  Enter command: \s""");

            String choice = scan.nextLine().toLowerCase().trim();

            switch (choice) {
                case "1" -> runReports();
                case "a" -> printTransactionsList(transactionsList);
                case "b" -> printTransactionsList(displayAllDeposits(transactionsList));
                case "c" -> printTransactionsList(displayAllPayments(transactionsList));
                case "r" -> inLedgerScreen = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private static void runReports() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n    ALL REPORTS");
            System.out.print("""
                \n    Options:
                    A) Month To Date Report
                    B) Previous Month Report
                    C) Year To Date Report
                    D) Previous Year Report
                    E) Search By Vendor Report
                    F) Custom Search
                    1) Back to Ledger
                    Enter command: \s""");

            String choice = scan.nextLine().toLowerCase().trim();

            switch (choice) {
                case "a" -> displayByMonthToDate();
                case "b" -> displayPreviousMonth();
                case "c" -> displayYearToDate();
                case "d" -> displayPreviousYear();
                case "e" -> displayByVendorName();
                case "f" -> displayFilteredTransactionList();
                case "1" -> inReports = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private static void displayFilteredTransactionList(){
        displayHeader("Fill in the fields you want to filter by. Press Enter to skip.");

        System.out.println("Start Date (YYYY-MM-DD):");
        String startDateInput = scan.nextLine().trim();

        System.out.print("End Date (YYYY-MM-DD): ");
        String endDateInput = scan.nextLine().trim();

        System.out.print("Description: ");
        String descriptionInput = scan.nextLine().trim();

        System.out.print("Vendor: ");
        String vendorInput = scan.nextLine().trim();

        System.out.print("Amount: ");
        String amountInput = scan.nextLine();

        ArrayList<Transaction> filteredList = new ArrayList<>();

        for (int i = transactionsList.size() - 1; i >= 0; i--) {
            Transaction t = transactionsList.get(i);

            if (!startDateInput.isEmpty()) {
                LocalDate start = LocalDate.parse(startDateInput);
                if (t.getDate().isBefore(start)) {
                    continue;
                }
            }

            if (!endDateInput.isEmpty()) {
                LocalDate end = LocalDate.parse(endDateInput);
                if (t.getDate().isAfter(end)) {
                    continue;
                }
            }

            if(!descriptionInput.isEmpty()){
                if(!t.getDescription().equalsIgnoreCase(descriptionInput)){
                    continue;
                }
            }

            if(!vendorInput.isEmpty()){
                if(!t.getVendor().equalsIgnoreCase(vendorInput)){
                    continue;
                }
            }

            if(!amountInput.isEmpty()){
                if(t.getAmount()!= Double.parseDouble(amountInput)){
                    continue;
                }
            }
            filteredList.add(t);
        }
        printTransactionsList(filteredList);
    }

    private static ArrayList<Transaction> readTransactions(){
        ArrayList<Transaction> transactionsList = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine();
            String line = bufferedReader.readLine();

            while (line != null) {

                String[] fields = line.split(Pattern.quote("|"));
                Transaction currentTransaction = generateTransactionAndFill(fields);
                transactionsList.add( currentTransaction);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("The file could not be read. Please make sure the file is available and not locked and then try again.");
            e.printStackTrace();
        }

        return transactionsList;
    }

    private static void displayByVendorName() {
        System.out.println("Enter the name of the vendor you would like to search for:");
        String vendorSearchName = scan.nextLine();
        for(Transaction transaction : transactionsList){
            if (transaction.getVendor().equalsIgnoreCase(vendorSearchName) ){
                transaction.formatAndPrintTransaction();
            }
        }
    }
    private static void displayPreviousYear() {
        LocalDate startOfCurrentYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        int lastYear = LocalDate.now().getYear() - 1;
        LocalDate startOfPreviousYear = LocalDate.of(lastYear, 1, 1);
        displayHeader("LEDGER REPORTS PREVIOUS YEAR");

        for(Transaction transaction : transactionsList){
            if (transaction.getDate().isBefore(startOfCurrentYear) && transaction.getDate().isAfter(startOfPreviousYear)){
                transaction.formatAndPrintTransaction();
            }
        }
    }
    private static void displayYearToDate() {
        LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        displayHeader("LEDGER REPORT YEAR TO DATE");
        for(Transaction transaction : transactionsList){
            if (transaction.getDate().isBefore(today) && transaction.getDate().isAfter(startOfYear)){
                transaction.formatAndPrintTransaction();
            }
        }
    }
    private static void displayPreviousMonth() {
        displayHeader("LEDGER REPORT PREVIOUS MONTH");
        LocalDate firstDayPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
        for(Transaction transaction : transactionsList){
            if (transaction.getDate().isBefore(firstDayOfMonth) && transaction.getDate().isAfter(firstDayPreviousMonth)){
                transaction.formatAndPrintTransaction();
            }
        }
    }
    private static void displayByMonthToDate() {
        displayHeader("LEDGER REPORT MONTH TO DATE");
        for(Transaction transaction : transactionsList){
            if (transaction.getDate().isBefore(today) && transaction.getDate().isAfter(firstDayOfMonth)){
                transaction.formatAndPrintTransaction();
            }
        }
    }

    private static String initiateNewPayment() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        String formattedDate = today.format(formatter);
        System.out.println();
        System.out.println("Enter a brief description of the payment (e.g., Rent, Groceries): ");
        String paymentDescription = scan.nextLine().trim();
        System.out.println("Who is the receiver/vendor? (e.g., Amazon, Landlord): ");
        String paymentVendor = scan.nextLine().trim();
        System.out.println("Enter the payment amount ($): ");
        double paymentAmount = scan.nextDouble();
        scan.nextLine();

        return formattedDate + "|" + paymentDescription + "|" + paymentVendor + "|" + paymentAmount + "\n";
    }
    public static String initiateNewDeposit() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        String formattedDate = today.format(formatter);
        System.out.println();
        System.out.println("Enter a brief description (e.g., Salary): ");
        String depositDescription = scan.nextLine().trim();
        System.out.println("Who is the sender/source/vendor? (e.g., Employer):");
        String depositVendor = scan.nextLine().trim();
        System.out.println("Enter deposit amount ($):");
        double depositAmount = scan.nextDouble();
        scan.nextLine();

        return formattedDate + "|" + depositDescription + "|" + depositVendor + "|" + depositAmount + "\n";
    }

    private static void addNewPayment() {
        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String newPayment = initiateNewPayment();

            bufferedWriter.write(newPayment);
            bufferedWriter.close();
            String[] fields = newPayment.split(Pattern.quote("|"));
            transactionsList.add(generateTransactionAndFill(fields));
            displayHeader("✔ Payment added successfully!");
        } catch (IOException e) {
            System.out.println("ERROR: An unexpected error occurred");
            throw new RuntimeException(e);
        }
    }
    private static void addNewDeposit (){
        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String newDeposit = initiateNewDeposit();

            bufferedWriter.write(newDeposit);
            bufferedWriter.close();
            String[] fields = newDeposit.split(Pattern.quote("|"));
            transactionsList.add(generateTransactionAndFill(fields));
            displayHeader("✔ Deposit added successfully!");
        } catch (IOException e) {
            System.out.println("ERROR: An unexpected error occurred");
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Transaction> displayAllPayments(ArrayList<Transaction> transactionsList){
        displayHeader("ALL PAYMENTS");
        ArrayList<Transaction> paymentsList = new ArrayList<>();

        for(Transaction transaction : transactionsList){
            if (transaction.getAmount() < 0){
                paymentsList.add(transaction);
            }
        }
        return paymentsList;
    }
    private static ArrayList<Transaction> displayAllDeposits(ArrayList<Transaction> transactionsList){
        displayHeader("ALL DEPOSITS");
        ArrayList<Transaction> depositList = new ArrayList<>();

        for(Transaction transaction : transactionsList){
            if (transaction.getAmount() > 0){
                depositList.add(transaction);
            }
        }
        return depositList;
    }

    private static void printTransactionsList(ArrayList<Transaction> transactionsList){
        System.out.println();
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %-10s %n",
                "Date", "Time", "Description", "Vendor", "Amount");
        for (int i = transactionsList.size() - 1; i >= 0; i--){
            Transaction transaction = transactionsList.get(i);
            transaction.formatAndPrintTransaction();
        }
    }
    private static Transaction generateTransactionAndFill(String[]fields){
        Transaction currentTransaction = new Transaction();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        currentTransaction.setDate(LocalDate.parse(fields[0].trim(), formatter));
        currentTransaction.setTime(LocalTime.parse(fields[1].trim(), timeFormatter));
        currentTransaction.setDescription(fields[2].trim());
        currentTransaction.setVendor(fields[3].trim());
        currentTransaction.setAmount(Double.parseDouble(fields[4].trim()));

        return currentTransaction;
    }

    private static void displayHeader(String title){
        System.out.println("\n==============================================================================");
        System.out.println("           " + title.toUpperCase());
        System.out.println("================================================================================");
    }
}
