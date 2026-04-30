# Capstone Project 1: Financial Ledger

The Financial Ledger is a Java-based accounting application designed to help users efficiently track
and manage their personal finances.

The application enables users to:

- Record financial transactions (deposits and payments)
- Generate reports based on different criteria
- Persist data across sessions using a CSV file (transactions.csv)

This tool is intended to provide a simple yet structured approach to financial tracking through a command-line
interface.

## Features

### Transaction Management

- Add new deposits and payments through a guided, user-friendly process
- Automatically store transactions in transactions.csv for persistence

### Ledger View:

View all transactions in a structured format
Filter transactions by:
- All entries
- Deposits only
- Payments only

### Custom Reports

Generate reports based on:
Month-to-date
- Previous month
- Year-to-date
- Previous year
- Vendor name

### Search Engine

Flexible filtering system supporting multiple criteria:
- Vendor name
- Start and end date
- Amount
- Description

**Code examples if filtering report by Vendor name:**

```private static void displayByVendorName() {
           System.out.println("Enter the name of the vendor you would like to search for:");
           String vendorSearchName = scan.nextLine();
           for(Transaction transaction : transactionsList){
               if (transaction.getVendor().equalsIgnoreCase(vendorSearchName) ){
                   transaction.formatAndPrintTransaction();
               }
           }
       }
###Interacton Diagram
[LEVEL 1: HOME SCREEN]
        |
        |── ADD DEPOSIT
        |       ├── Enter date (YYYY-MM-DD)
        |       ├── Enter time (HH:mm:ss)
        |       ├── Enter description (e.g., Payroll)
        |       ├── Enter vendor (e.g., Employer Name)
        |       └── Enter amount ($)
        |
        |── MAKE PAYMENT
        |       ├── Enter date (YYYY-MM-DD)
        |       ├── Enter time (HH:mm:ss)
        |       ├── Enter description
        |       ├── Enter vendor
        |       └── Enter amount ($)
        |
        |── DISPLAY TRANSACTIONS
        |       ├── All Entries
        |       ├── Deposits Only
        |       ├── Payments Only
        |       ├── Customized Reports
        |       |       ├── Month-to-Date
        |       |       ├── Previous Month
        |       |       ├── Year-to-Date
        |       |       ├── Previous Year
        |       |       ├── By Vendor
        |       |       └── Back
        |       └── Back to Home
        |
        |── EXIT APPLICATION

