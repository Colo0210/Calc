import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import static java.time.LocalTime.*;

public class CLI {
    private final Scanner scanner;
    private final Ledger ledger;
    private final CSVFileHandler csvFileHandler;

    public CLI(Ledger ledger, CSVFileHandler csvFileHandler) {
        this.scanner = new Scanner(System.in);
        this.ledger = ledger;
        this.csvFileHandler = csvFileHandler;
    }

    public void run() throws Transaction.InvalidInputException {
        boolean running = true;
        while (running) {
            homeScreen();
            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "D" -> addDepositScreen();
                case "P" -> addPaymentScreen();
                case "L" -> ledgerScreen();
                case "R" -> reportsScreen();
                case "U" -> updateTransactionScreen();
                case "X" -> running = false;
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
        scanner.close();
    }

    private void homeScreen() {
        System.out.println("=========================");
        System.out.println("= Financial Tracker 1.0 =");
        System.out.println("=========================");
        System.out.println("D) Add Deposit");
        System.out.println("P) Add Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("R) Reports");
        System.out.println("U) Update Transaction");
        System.out.println("X) Exit");
        System.out.print("Please select an option: ");
    }

    private void addDepositScreen() throws Transaction.InvalidInputException {
        System.out.println("=========================");
        System.out.println("=       Add Deposit     =");
        System.out.println("=========================");
        String date = validateDateInput("Please enter the date (YYYY-MM-DD): ");
        String time = validateTimeInput("Please enter the time (HH:MM:SS): ");
        String description = getInput("Please enter the description: ");
        String vendor = getInput("Please enter the vendor name: ");
        String amount = validateAmountInput("Please enter the amount: ");
        Transaction deposit = new Transaction(date, time, description, vendor, String.valueOf(Double.parseDouble(amount)));
        ledger.addTransaction(deposit);
        try {
            csvFileHandler.writeTransactionToFile(deposit);
        } catch (Exception e) {
            System.err.println("Error writing transaction to file: " + e.getMessage());
        }
    }

    private void addPaymentScreen() throws Transaction.InvalidInputException {
        System.out.println("=========================");
        System.out.println("=       Add Payment     =");
        System.out.println("=========================");
        String date = validateDateInput("Please enter the date (YYYY-MM-DD): ");
        String time = validateTimeInput("Please enter the time (HH:MM:SS): ");
        String description = getInput("Please enter the description: ");
        String vendor = getInput("Please enter the vendor name: ");
        String amount = validateAmountInput("Please enter the amount (negative number): ");
        Transaction payment = new Transaction(date, time, description, vendor, String.valueOf(Double.parseDouble(amount)));
        ledger.addTransaction(payment);
        try {
            csvFileHandler.writeTransactionToFile(payment);
        } catch (Exception e) {
            System.err.println("Error writing transaction to file: " + e.getMessage());
        }
    }

    private void ledgerScreen() {
        System.out.println("=========================");
        System.out.println("=         Ledger        =");
        System.out.println("=========================");
        String input = getInput("Show (A)ll, (D)eposits, (P)ayments, or (F)iltered Transactions? ");
        switch (input) {
            case "A" -> displayTransactions(ledger.getTransactions());
            case "D" -> displayTransactions(ledger.getDeposits());
            case "P" -> displayTransactions(ledger.getPayments());
            case "F" -> filterTransactionsScreen();
            default -> System.out.println("Invalid input. Please try again.");
        }
    }

    private void reportsScreen() {
        System.out.println("=========================");
        System.out.println("=        Reports        =");
        System.out.println("=========================");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");
        String input = getInput("Please select a report: ");
        switch (input) {
            case "1":
                displayTransactions(ledger.getMonthToDateTransactions());
                break;
            case "2":
                displayTransactions(ledger.getPreviousMonthTransactions());
                break;
            case "3":
                displayTransactions(ledger.getYearToDateTransactions());
                break;
            case "4":
                displayTransactions(ledger.getPreviousYearTransactions());
                break;
            case "5":
                searchByVendorScreen();
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid input. Please try again.");
        }
    }

    private void updateTransactionScreen() throws Transaction.InvalidInputException {
        System.out.println("=========================");
        System.out.println("=  Update Transaction   =");
        System.out.println("=========================");
        String date = validateDateInput("Please enter the date (YYYY-MM-DD): ");
        String time = validateTimeInput("Please enter the time (HH:MM:SS): ");
        String description = getInput("Please enter the description: ");
        String vendor = getInput("Please enter the vendor name: ");
        String amount = validateAmountInput("Please enter the amount: ");
        Transaction oldTransaction = getTransactionByDateAndTime(date, time);
        if (oldTransaction == null) {
            System.out.println("Transaction not found.");
            return;
        }
        Transaction newTransaction = new Transaction(date, time, description, vendor, String.valueOf(Double.parseDouble(amount)));
        ledger.updateTransaction(oldTransaction, newTransaction);
        try {
            csvFileHandler.updateTransactionInFile(oldTransaction, newTransaction);
        } catch (Exception e) {
            System.err.println("Error updating transaction in file: " + e.getMessage());
        }
    }

    private void deleteTransactionScreen() {
        System.out.println("=========================");
        System.out.println("=  Delete Transaction   =");
        System.out.println("=========================");
        String date = validateDateInput("Please enter the date (YYYY-MM-DD): ");
        String time = validateTimeInput("Please enter the time (HH:MM:SS): ");
        Transaction transaction = getTransactionByDateAndTime(date, time);
        if (transaction == null) {
            System.out.println("Transaction not found.");
            return;
        }
        ledger.removeTransaction(transaction);
        try {
            csvFileHandler.deleteTransactionFromFile(transaction);
        } catch (Exception e) {
            System.err.println("Error deleting transaction from file: " + e.getMessage());
        }
    }

    private void filterTransactionsScreen() {
        System.out.println("=========================");
        System.out.println("=  Filter Transactions  =");
        System.out.println("=========================");
        String input = getInput("Filter by (D)ate range, (V)endor, or (A)mount range? ");
        switch (input) {
            case "D":
                filterTransactionsByDateRangeScreen();
                break;
            case "V":
                filterTransactionsByVendorScreen();
                break;
            case "A":
                filterTransactionsByAmountRangeScreen();
                break;
            default:
                System.out.println("Invalid input. Please try again.");
        }
    }

    private void searchByVendorScreen() {
        String vendor = getInput("Please enter the vendor name: ");
        List<Transaction> transactions = ledger.getTransactionsByVendor(vendor);
        displayTransactions(transactions);
    }

    private void filterTransactionsByDateRangeScreen() {
        String startDate = validateDateInput("Please enter the start date (YYYY-MM-DD): ");
        String endDate = validateDateInput("Please enter the end date (YYYY-MM-DD): ");
        List<Transaction> transactions = ledger.getTransactionsByDateRange(startDate, endDate);
        displayTransactions(transactions);
    }

    private void filterTransactionsByVendorScreen() {
        String vendor = getInput("Please enter the vendor name: ");
        List<Transaction> transactions = ledger.getTransactionsByVendor(vendor);
        displayTransactions(transactions);
    }

    private void filterTransactionsByAmountRangeScreen() {
        String minAmount = validateAmountInput("Please enter the minimum amount: ");
        String maxAmount = validateAmountInput("Please enter the maximum amount: ");
        List<Transaction> transactions = ledger.getTransactionsByAmountRange(Double.parseDouble(minAmount), Double.parseDouble(maxAmount));
        displayTransactions(transactions);
    }

    private void displayTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        transactions.sort(Transaction::compareTo);
        System.out.println("===================================================================================");
        System.out.printf("%-12s | %-12s | %-30s | %-20s | %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("===================================================================================");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
        System.out.println("===================================================================================");
        System.out.printf("%-75s %10s%n", "Total:", formatAmount(ledger.getBalance()));
    }

    private Transaction getTransactionByDateAndTime(String date, String time) {
        List<Transaction> transactions = ledger.getTransactions();
        for (Transaction transaction : transactions) {
            if (transaction.getDate().equals(date) && transaction.getTime().equals(time)) {
                return transaction;
            }
        }
        return null;
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String validateDateInput(String prompt) {
        String input;
        do {
            input = getInput(prompt);
            if (!Transaction.isValidDate(String.valueOf(LocalDate.parse(input)))) {
                System.out.println("Invalid date. Please try again.");
            }
        } while (!Transaction.isValidDate(String.valueOf(LocalDate.parse(input))));
        return input;
    }

    private String validateTimeInput(String prompt) {
        String input;
        do {
            input = getInput(prompt);
            if (!Transaction.isValidTime(String.valueOf(parse(input)))) {
                System.out.println("Invalid time. Please try again.");
            }
        } while (!Transaction.isValidTime(String.valueOf(parse(input))));
        return input;
    }

    private String validateAmountInput(String prompt) {
        String input;
        do {
            input = getInput(prompt);
            if (!Transaction.isValidAmount(String.valueOf(Double.parseDouble(input)))) {
                System.out.println("Invalid amount. Please try again.");
            }
        } while (!Transaction.isValidAmount(String.valueOf(Double.parseDouble(input))));
        return input;
    }

    private String formatAmount(BigDecimal amount) {
        return String.format("$%,.2f", amount);
    }
}
