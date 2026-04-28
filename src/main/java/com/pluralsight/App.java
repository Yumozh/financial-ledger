package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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

        //Add from newest to older display!!!!!!
        //Ledger - Read and Display
        ArrayList<Transaction> transactionsList = readTransactions();
        printTransactionsList(transactionsList);

        //Ledger - Deposits only
        ArrayList<Transaction> depositList = findDeposits(transactionsList);



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
