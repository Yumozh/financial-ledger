package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    static Scanner scan = new Scanner(System.in);
    static ArrayList<Transaction> transactionsList = readTransactions();
    static LocalDate today = LocalDate.now();
    static LocalDate firstDayOfMonth = today.withDayOfMonth(1);

    // ANSI Color Codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    private static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        displayUserGreeting();
        runHomeScreen();
    }

    private static void displayHeader(String title) {
        System.out.println(BOLD + YELLOW + "=== " + title + " ===" + RESET);
        System.out.println();
    }

    private static void displayUserGreeting() {
        System.out.println(BOLD + CYAN + """
                ||====================================================================||
                ||//$\\\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\//$\\\\||
                ||(100)==================| FEDERAL RESERVE NOTE |================(100)||
                ||\\\\$//        ~         '------========--------'                \\\\$//||
                ||<< /        /$\\              // ____ \\\\                         \\ >>||
                ||>>|  12    //L\\\\            // ///..) \\\\         L38036133B   12 |<<||
                ||<<|        \\\\ //           || <||  >\\  ||                        |>>||
                ||>>|         \\$/            ||  $$ --/  ||        One Hundred     |<<||
                ||<<|      L38036133B        *\\\\  |\\_/  //* series                 |>>||
                ||>>|  12                     *\\\\/___\\_//*   1989                  |<<||
                ||<<\\      Treasurer     ______/Franklin\\________     Secretary 12 />>||
                ||//$\\                 ~|UNITED STATES OF AMERICA|~               /$\\\\||
                ||(100)===================  ONE HUNDRED DOLLARS =================(100)||
                ||\\\\$//\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\\\$//||
                ||====================================================================||
            """ + RESET);
        System.out.println(BOLD + YELLOW + "=== Welcome to LC Impact Accounting Ledger ===" + RESET);
        System.out.println(BOLD + GREEN + "Track your spending and manage your finances with ease.\n" + RESET);
    }

    private static void runHomeScreen() {
        boolean isRunning = true;

        while (isRunning) {
            displayHeader("HOME SCREEN");
            System.out.print(BOLD + BLUE + """
                    1) Open Ledger
                    2) Make Payment (Debit)
                    3) Add Deposit
                    X) Exit
                    Enter command: \s""" + RESET);

            String choice = scan.nextLine().toLowerCase().trim();

            switch (choice) {
                case "1" -> runLedgerScreen();
                case "2" -> addNewPayment();
                case "3" -> addNewDeposit();
                case "x" -> {
                    System.out.println(BOLD + GREEN + "\nThank you for using the LC Impact Accounting Ledger. Goodbye! 👋" + RESET);
                    isRunning = false;
                }
                default -> System.out.println(RED + "❌ Invalid input. Please try again.\n" + RESET);
            }
        }
    }

    private static void runLedgerScreen() {
        boolean inLedgerScreen = true;

        while (inLedgerScreen) {
            displayHeader("TRANSACTION LEDGER");
            System.out.print(BOLD + BLUE + """
                    1) All Reports
                    A) Display all Transactions
                    B) Display all Deposits
                    C) Display all Payments
                    R) Back to Home
                    Enter command: \s""" + RESET);

            String choice = scan.nextLine().toLowerCase().trim();

            switch (choice) {
                case "1" -> runReports();
                case "a" -> printTransactionsList(transactionsList);
                case "b" -> printTransactionsList(filterDeposits(transactionsList));
                case "c" -> printTransactionsList(filterPayments(transactionsList));
                case "r" -> inLedgerScreen = false;
                default -> System.out.println(RED + "❌ Invalid input. Please try again.\n" + RESET);
            }
        }
    }

    private static void runReports() {
        boolean inReports = true;

        while (inReports) {
            displayHeader("ALL REPORTS");
            System.out.print(BOLD + BLUE + """
                    Options:
                      A) Month To Date Report
                      B) Previous Month Report
                      C) Year To Date Report
                      D) Previous Year Report
                      E) Search By Vendor Report
                      F) Custom Search
                      1) Back to Ledger
                      Enter command: \s""" + RESET);

            String choice = scan.nextLine().toLowerCase().trim();

            switch (choice) {
                case "a" -> displayByMonthToDate();
                case "b" -> displayPreviousMonth();
                case "c" -> displayYearToDate();
                case "d" -> displayPreviousYear();
                case "e" -> displayByVendorName();
                case "f" -> displayFilteredTransactionList();
                case "1" -> inReports = false;
                default -> System.out.println(RED + "❌ Invalid input. Please try again.\n" + RESET);
            }
        }
    }

    private static void displayFilteredTransactionList() {
        displayHeader("FILTER TRANSACTIONS");
        System.out.println(BOLD + YELLOW + "Fill in the fields you want to filter by. Press Enter to skip.\n" + RESET);

        System.out.print(CYAN + "Start Date (YYYY-MM-DD): " + RESET);
        String startDateInput = scan.nextLine().trim();

        System.out.print(CYAN + "End Date (YYYY-MM-DD): " + RESET);
        String endDateInput = scan.nextLine().trim();

        System.out.print(CYAN + "Description: " + RESET);
        String descriptionInput = scan.nextLine().trim();

        System.out.print(CYAN + "Vendor: " + RESET);
        String vendorInput = scan.nextLine().trim();

        System.out.print(CYAN + "Amount: " + RESET);
        String amountInput = scan.nextLine().trim();

        ArrayList<Transaction> filteredList = new ArrayList<>();

        for (Transaction t : transactionsList) {
            boolean matches = true;

            if (!startDateInput.isEmpty()) {
                try {
                    LocalDate start = LocalDate.parse(startDateInput);
                    if (t.getDate().isBefore(start)) {
                        matches = false;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println(RED + "❌ Invalid date format. Use YYYY-MM-DD." + RESET);
                    return;
                }
            }

            if (!endDateInput.isEmpty() && matches) {
                try {
                    LocalDate end = LocalDate.parse(endDateInput);
                    if (t.getDate().isAfter(end)) {
                        matches = false;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println(RED + "❌ Invalid date format. Use YYYY-MM-DD." + RESET);
                    return;
                }
            }

            if (!descriptionInput.isEmpty() && matches) {
                if (!t.getDescription().equalsIgnoreCase(descriptionInput)) {
                    matches = false;
                }
            }

            if (!vendorInput.isEmpty() && matches) {
                if (!t.getVendor().equalsIgnoreCase(vendorInput)) {
                    matches = false;
                }
            }

            if (!amountInput.isEmpty() && matches) {
                try {
                    if (t.getAmount() != Double.parseDouble(amountInput)) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(RED + "❌ Invalid amount format. Use a number." + RESET);
                    return;
                }
            }

            if (matches) {
                filteredList.add(t);
            }
        }

        if (filteredList.isEmpty()) {
            System.out.println(RED + "❌ No transactions match the filters.\n" + RESET);
        } else {
            printTransactionsList(filteredList);
        }
    }

    private static ArrayList<Transaction> readTransactions() {
        ArrayList<Transaction> transactionsList = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean isFirstLine = true; // Flag to skip header row

            while ((line = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }

                String[] parts = line.split("\\|");
                if (parts.length != 5) {
                    System.out.println(YELLOW + "Skipping malformed line (incorrect number of fields): " + line + RESET);
                } else {
                    try {
                        // Parse each field individually by its index in the parts array
                        LocalDate date = LocalDate.parse(parts[0].trim());
                        LocalTime time = LocalTime.parse(parts[1].trim());
                        String description = parts[2].trim();
                        String vendor = parts[3].trim();
                        double amount = Double.parseDouble(parts[4].trim());

                        transactionsList.add(new Transaction(date, time, description, vendor, amount));
                    } catch (Exception e) {
                        System.out.println(RED + "❌ Error parsing transaction: " + e.getMessage() + RESET);
                        System.out.println(YELLOW + "Skipping malformed line: " + line + RESET);
                    }
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(YELLOW + "No existing transactions found. Starting with an empty ledger." + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error reading transactions: " + e.getMessage() + RESET);
        }

        return transactionsList;
    }

    private static void saveTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv"))) {
            writer.write("date|time|description|vendor|amount");
            writer.newLine();
            for (Transaction t : transactionsList) {
                writer.write(String.format("%s|%s|%s|%s|%.2f%n",
                        t.getDate(),
                        t.getTime(),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount()));
            }
        } catch (IOException e) {
            System.out.println(RED + "Error saving transactions: " + e.getMessage() + RESET);
        }
    }

    private static void addNewPayment() {
        displayHeader("ADD NEW PAYMENT");

        System.out.print(CYAN + "Enter date (YYYY-MM-DD): " + RESET);
        LocalDate date = getValidDate();

        System.out.print(CYAN + "Enter time (HH:mm:ss): " + RESET);
        LocalTime time = getValidTime();

        System.out.print(CYAN + "Enter description: " + RESET);
        String description = scan.nextLine().trim();

        System.out.print(CYAN + "Enter vendor: " + RESET);
        String vendor = scan.nextLine().trim();

        System.out.print(CYAN + "Enter amount: $" + RESET);
        double amount = getValidAmount();
        // Payments should reduce the balance, so store as a negative amount
        amount = -Math.abs(amount);

        Transaction payment = new Transaction(date, time, description, vendor, amount);
        transactionsList.add(payment);
        saveTransactions();

        System.out.println(GREEN + "\n✅ Payment added successfully!" + RESET);
    }

    private static void addNewDeposit() {
        displayHeader("ADD NEW DEPOSIT");

        System.out.print(CYAN + "Enter date (YYYY-MM-DD): " + RESET);
        LocalDate date = getValidDate();

        System.out.print(CYAN + "Enter time (HH:mm:ss): " + RESET);
        LocalTime time = getValidTime();

        System.out.print(CYAN + "Enter description: " + RESET);
        String description = scan.nextLine().trim();

        System.out.print(CYAN + "Enter vendor: " + RESET);
        String vendor = scan.nextLine().trim();

        System.out.print(CYAN + "Enter amount: $" + RESET);
        double amount = getValidAmount();
        // Deposits should increase the balance, so store as a positive amount
        amount = Math.abs(amount);

        Transaction deposit = new Transaction(date, time, description, vendor, amount);
        transactionsList.add(deposit);
        saveTransactions();

        System.out.println(GREEN + "\n✅ Deposit added successfully!" + RESET);
    }

    private static LocalDate getValidDate() {
        while (true) {
            try {
                return LocalDate.parse(scan.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println(RED + "❌ Invalid date format. Use YYYY-MM-DD.\n" + RESET);
                System.out.print(CYAN + "Enter date (YYYY-MM-DD): " + RESET);
            }
        }
    }

    private static LocalTime getValidTime() {
        while (true) {
            try {
                return LocalTime.parse(scan.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println(RED + "❌ Invalid time format. Use HH:mm:ss.\n" + RESET);
                System.out.print(CYAN + "Enter time (HH:mm:ss): " + RESET);
            }
        }
    }

    private static double getValidAmount() {
        while (true) {
            try {
                return Double.parseDouble(scan.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(RED + "❌ Invalid amount. Use a number.\n" + RESET);
                System.out.print(CYAN + "Enter amount: $" + RESET);
            }
        }
    }

    private static ArrayList<Transaction> filterDeposits(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }
        return deposits;
    }

    private static ArrayList<Transaction> filterPayments(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        return payments;
    }

    private static void displayByVendorName() {
        displayHeader("SEARCH BY VENDOR");
        System.out.print(CYAN + "Enter the name of the vendor you would like to search for: " + RESET);
        String vendorSearchName = scan.nextLine().trim();

        ArrayList<Transaction> filtered = new ArrayList<>();
        for (Transaction transaction : transactionsList) {
            if (transaction.getVendor().equalsIgnoreCase(vendorSearchName)) {
                filtered.add(transaction);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println(RED + "❌ No transactions found for vendor: " + vendorSearchName + RESET);
        } else {
            printTransactionsList(filtered);
        }
    }

    private static void displayByMonthToDate() {
        LocalDate startOfMonth = today.withDayOfMonth(1);
        ArrayList<Transaction> filtered = new ArrayList<>();

        for (Transaction t : transactionsList) {
            if (!t.getDate().isBefore(startOfMonth)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println(RED + "❌ No transactions found for this month." + RESET);
        } else {
            System.out.println(BOLD + BLUE + "\n=== Month-to-Date Transactions ===" + RESET);
            printTransactionsList(filtered);
        }
    }

    private static void displayPreviousMonth() {
        LocalDate previousMonth = today.minusMonths(1);
        LocalDate startOfPreviousMonth = previousMonth.withDayOfMonth(1);
        LocalDate endOfPreviousMonth = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());

        ArrayList<Transaction> filtered = new ArrayList<>();

        for (Transaction t : transactionsList) {
            if (!t.getDate().isBefore(startOfPreviousMonth) && !t.getDate().isAfter(endOfPreviousMonth)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println(RED + "❌ No transactions found for " + previousMonth.getMonth() + "." + RESET);
        } else {
            System.out.println(BOLD + BLUE + "\n=== " + previousMonth.getMonth() + " Transactions ===" + RESET);
            printTransactionsList(filtered);
        }
    }

    private static void displayYearToDate() {
        LocalDate startOfYear = today.withDayOfYear(1);
        ArrayList<Transaction> filtered = new ArrayList<>();

        for (Transaction t : transactionsList) {
            if (!t.getDate().isBefore(startOfYear)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println(RED + "❌ No transactions found for this year." + RESET);
        } else {
            System.out.println(BOLD + BLUE + "\n=== Year-to-Date Transactions ===" + RESET);
            printTransactionsList(filtered);
        }
    }

    private static void displayPreviousYear() {
        int previousYear = today.getYear() - 1;
        ArrayList<Transaction> filtered = new ArrayList<>();

        for (Transaction t : transactionsList) {
            if (t.getDate().getYear() == previousYear) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println(RED + "❌ No transactions found for " + previousYear + "." + RESET);
        } else {
            System.out.println(BOLD + BLUE + "\n=== " + previousYear + " Transactions ===" + RESET);
            printTransactionsList(filtered);
        }
    }

    private static void printTransactionsList(ArrayList<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println(RED + "❌ No transactions found.\n" + RESET);
            return;
        }

        System.out.println(BOLD + BLUE + "\n=== Transaction List ===" + RESET);
        System.out.printf(BOLD + "%-12s | %-10s | %-25s | %-20s | %-10s%n" + RESET,
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println(BOLD + "--------------------------------------------------------------------------------" + RESET);

        for (Transaction t : transactions) {
            String amountStr = t.getAmount() > 0 ? GREEN + "$" + t.getAmount() + RESET : RED + "$" + t.getAmount() + RESET;
            System.out.printf("%-12s | %-10s | %-25s | %-20s | %s%n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    amountStr);
        }
        System.out.println();
    }
}