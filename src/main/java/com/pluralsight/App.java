package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        //User welcome
        System.out.println("====================================");
        System.out.println("Hello! Ready to track your spending?");
        System.out.println("Your Financial Overview Starts Here.");
        System.out.println("====================================");
        System.out.println();

//        runHomeScreen();
        System.out.println(addDeposit(scan));


        //Add Deposit
//        FileWriter fileWriter = new FileWriter("transactions.csv");




        //Add from newest to older display!!!!!!
        //Ledger - Read and Display
        ArrayList<Transaction> transactionsList = readTransactions();
        printTransactionsList(transactionsList);

        //Ledger - Deposits only
        System.out.println();
        System.out.println("====================");
        System.out.println("DEPOSITS LIST");
        System.out.println("====================");
        ArrayList<Transaction> depositList = findDeposits(transactionsList);
        printTransactionsList(depositList);

        //Ledger - Payments only
        System.out.println();
        System.out.println("====================");
        System.out.println("PAYMENTS LIST");
        System.out.println("====================");
        ArrayList<Transaction> paymentsList = findPayments(transactionsList);
        printTransactionsList(paymentsList);



    }
    public static String addDeposit(Scanner scan) {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        String formattedDate = today.format(formatter);

        System.out.println("Enter a brief description (e.g., Salary): ");
        String depositDescription = scan.nextLine();
        System.out.println("Who is the sender/source/vendor? (e.g., Employer):");
        String depositVendor = scan.nextLine();
        System.out.println("Enter deposit amount ($):");
        double depositAmount = scan.nextDouble();

        return formattedDate + " | " + depositVendor + " | " + depositVendor + " | " + depositAmount;
    }

    private static void runHomeScreen() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.print("""
                \n[LEVEL 1: PARENT SCREEN]
                1) Open Child Screen A (Has Grandchild)
                2) Open Child Screen B (No Grandchild)
                X) Exit Application
                Enter command: \s""");

            String choice = scan.nextLine().toLowerCase().trim();
            switch (choice) {
                case "1" -> runLadgerScreen();
//                case "2" -> runChildBScreen();
                case "x" -> isRunning = false;
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private static void runLadgerScreen() {
        boolean inChildA = true;
        while (inChildA) {
            System.out.print("""
                \n  [LEVEL 2: CHILD SCREEN A]
                  1) Open Grandchild Screen
                  A) Display All Transactions
                  R) Return to Parent
                  Enter command: \s""");

            String choice = scan.nextLine().toLowerCase().trim();
            switch (choice) {
//                case "1" -> runGrandchildScreen();
                case "a" -> System.out.println("  [Action] Logic executed in Child A.");
                case "r" -> inChildA = false;
                default -> System.out.println("  Invalid input.");
            }
        }
    }

    //Create payments list
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
