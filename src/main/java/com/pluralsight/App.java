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

    public static void main(String[] args) {

        runHomeScreen();

//        //Fix from newest to older display!!!!!!
//        //Ledger - Read and Display
//        printTransactionsList(transactionsList);
//
//        //Add Deposit to file transaction and store in the list
//        addNewDeposit ();
//
//        //Add Payment to file transaction and store in the list
//        //Fix always negative number!!!!
//        addNewPayment();
//
//        //Ledger - Deposits only
//        System.out.println();
//        System.out.println("====================");
//        System.out.println("DEPOSITS LIST");
//        System.out.println("====================");
//        ArrayList<Transaction> depositList = findDeposits(transactionsList);
//        printTransactionsList(depositList);
//
//        //Ledger - Payments only
//        System.out.println();
//        System.out.println("====================");
//        System.out.println("PAYMENTS LIST");
//        System.out.println("====================");
//        ArrayList<Transaction> paymentsList = findPayments(transactionsList);
//        printTransactionsList(paymentsList);



    }
    private static void runHomeScreen() {
        boolean isRunning = true;
        while (isRunning) {
            //User welcome
            System.out.println("====================================");
            System.out.println("Hello! Ready to track your spending?");
            System.out.println("Your Financial Overview Starts Here.");
            System.out.println("====================================");
            System.out.println();

            System.out.print("""
                \nHOME SCREEN
                1) Open Ledger A (Has Grandchild)
                2) Make Payment B (No Grandchild)
                3) Add Deposit
                X) Exit Application
                Enter command: \s""");

            String choice = scan.nextLine().toLowerCase().trim();
            switch (choice) {
                case "1" -> runLedgerScreen();
                case "2" -> addNewPayment();
                case "3" -> addNewDeposit ();
                case "x" -> isRunning = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private static void runLedgerScreen() {
        boolean inChildA = true;
        while (inChildA) {
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
//                case "1" -> runGrandchildScreen();
                case "a" -> printTransactionsList(transactionsList);
                case "b" -> printTransactionsList(findDeposits(transactionsList));
                case "c" -> printTransactionsList(findPayments(transactionsList));
                case "r" -> inChildA = false;
                default -> System.out.println("  Invalid input.");
            }
        }
    }
    //Add Payment to file transaction and store in the list
    private static void addNewPayment() {
        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String newPayment = initiateNewPayment();

            bufferedWriter.write(newPayment);
            bufferedWriter.close();
            String[] fields = newPayment.split(Pattern.quote("|"));
            transactionsList.add(generateTransactionAndFill(fields));

        } catch (IOException e) {
            System.out.println("ERROR: An unexpected error occurred");
            throw new RuntimeException(e);
        }
    }
    //Add Deposit to file transaction and store in the list
    private static void addNewDeposit (){
        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String newDeposit = initiateNewDeposit();

            bufferedWriter.write(newDeposit);
            bufferedWriter.close();
            String[] fields = newDeposit.split(Pattern.quote("|"));
            transactionsList.add(generateTransactionAndFill(fields));

        } catch (IOException e) {
            System.out.println("ERROR: An unexpected error occurred");
            throw new RuntimeException(e);
        }
    }
    //Initiate new payment based on user input
    private static String initiateNewPayment() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        String formattedDate = today.format(formatter);

        System.out.println("Enter a brief description of the payment (e.g., Rent, Groceries): ");
        String paymentDescription = scan.nextLine();
        System.out.println("Who is the receiver/vendor? (e.g., Amazon, Landlord): ");
        String paymentVendor = scan.nextLine();
        System.out.println("Enter the payment amount ($): ");
        double paymentAmount = scan.nextDouble();
        scan.nextLine();

        return formattedDate + "|" + paymentDescription + "|" + paymentVendor + "|" + paymentAmount;
    }
    //Initiate new deposit based on user input
    public static String initiateNewDeposit() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        String formattedDate = today.format(formatter);

        System.out.println("Enter a brief description (e.g., Salary): ");
        String depositDescription = scan.nextLine();
        System.out.println("Who is the sender/source/vendor? (e.g., Employer):");
        String depositVendor = scan.nextLine();
        System.out.println("Enter deposit amount ($):");
        double depositAmount = scan.nextDouble();
        scan.nextLine();

        return formattedDate + "|" + depositDescription + "|" + depositVendor + "|" + depositAmount;
    }

    //Generate a list with all payments
    private static ArrayList<Transaction> findPayments(ArrayList<Transaction> transactionsList){
        ArrayList<Transaction> paymentsList = new ArrayList<>();

        for(Transaction transaction : transactionsList){
            if (transaction.getAmount() < 0){
                paymentsList.add(transaction);
            }
        }
        return paymentsList;
    }
    // Create a list of deposits
    private static ArrayList<Transaction> findDeposits(ArrayList<Transaction> transactionsList){
        ArrayList<Transaction> depositList = new ArrayList<>();

        for(Transaction transaction : transactionsList){
            if (transaction.getAmount() > 0){
                depositList.add(transaction);
            }
        }
        return depositList;
    }
    // Read transactions from a file
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
    // Display transactions the list
    private static void printTransactionsList(ArrayList<Transaction> transactionsList){
        for (Transaction transaction : transactionsList){
            System.out.printf("%-12s | %-10s | %-25s | %-20s | $%.2f %n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
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
}
