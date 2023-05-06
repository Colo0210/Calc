import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class LedgerRunner {
    private static final String LEDGER_FILE = "ledger.csv";
    private static final String MENU = "Enter one of the following options:\n"
            + "1 - View transactions\n"
            + "2 - Add transaction\n"
            + "3 - Delete transaction\n"
            + "4 - View transactions by date range\n"
            + "5 - View transactions for current month to date\n"
            + "0 - Exit program\n";

    private static final Scanner scanner = new Scanner(System.in);
    private static final CSVFileHandler csvFileHandler = new CSVFileHandler(LEDGER_FILE);

    public static void main(String[] args) {
        System.out.println("Welcome to your ledger!");
        boolean exit = false;
        while (!exit) {
            System.out.println(MENU);
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    viewTransactions();
                    break;
                case "2":
                    addTransaction();
                    break;
                case "3":
                    deleteTransaction();
                    break;
                case "4":
                    viewTransactionsByDateRange();
                    break;
                case "5":
                    viewMonthToDateTransactions();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void viewTransactions() {
        List<Transaction> transactions = csvFileHandler.read();
        Collections.sort(transactions);
        transactions.forEach(System.out::println);
    }

    private static void addTransaction() {
        System.out.print("Enter the amount: ");
        String amountString = scanner.nextLine();
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        System.out.print("Enter the time (HH:MM:SS): ");
        String timeString = scanner.nextLine();
        System.out.print("Enter the description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the vendor: ");
        String vendor = scanner.nextLine();

        try {
            Transaction transaction = new Transaction(amountString, dateString, timeString, description, vendor);
            csvFileHandler.append(transaction);
            System.out.println("Transaction added: " + transaction);
        } catch (Transaction.InvalidInputException | CsvFileHandler.CsvFileException e) {
            System.out.println("Unable to add transaction: " + e.getMessage());
        }
    }

    private static void deleteTransaction() {
        System.out.print("Enter the ID of the transaction to delete: ");
        String idString = scanner.nextLine();
        try {
            int id = Integer.parseInt(idString);
            Transaction transaction = csvFileHandler.delete(id);
            if (transaction != null) {
                System.out.println("Transaction deleted: " + transaction);
            } else {
                System.out.println("Transaction not found with ID: " + id);
            }
        } catch (NumberFormatException | CsvFileHandler.CsvFileException e) {
            System.out.println("Unable to delete transaction: " + e.getMessage());
        }
    }

    private static void viewTransactionsByDateRange() {
        System.out.print("Enter the start date (YYYY-MM-DD): ");
        String startDateString = scanner.nextLine();
        System.out.print("Enter the end date (YYYY-MM-DD): ");
        String endDateString = scanner.nextLine();

        try {
            List<Transaction> transactions = csvFileHandler.getByDateRange(startDateString, endDateString);
            Collections.sort(transactions);
            System.out.println("Transactions between " + startDateString + " and " + endDateString + ":");
            System.out.println("-----------------------------------------------");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
            System.out.println();
        } catch (CsvFileHandler.CsvFileHandlerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewMonthToDateTransactions() {
        List<Transaction> transactions = ledger.getMonthToDateTransactions();

        Collections.sort(transactions);
        System.out.println("Month-to-Date Transactions:");
        System.out.println("--------------------------");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println();
    }

    private static void viewTransactionsByType() {
        System.out.print("Enter the transaction type: ");
        String type = scanner.nextLine();

        try {
            List<Transaction> transactions = csvFileHandler.getByType(type);

            Collections.sort(transactions);
            System.out.println("Transactions of type " + type + ":");
            System.out.println("----------------------------------");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
            System.out.println();
        } catch (CSVFileHandler.CsvFileHandlerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addTransaction() {
        System.out.print("Enter the transaction amount: ");
        String amountString = scanner.nextLine();
        System.out.print("Enter the transaction date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        System.out.print("Enter the transaction time (HH:MM:SS): ");
        String timeString = scanner.nextLine();
        System.out.print("Enter the transaction description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the transaction vendor: ");
        String vendor = scanner.nextLine();

        try {
            Transaction transaction = new Transaction(amountString, dateString, timeString, description, vendor);
            ledger.addTransaction(transaction);
            csvFileHandler.append(transaction.toCSV());
            System.out.println("Transaction added successfully.");
            System.out.println();
        } catch (Transaction.InvalidInputException | CsvFileHandler.CsvFileHandlerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteTransaction() {
        System.out.print("Enter the transaction ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        try {
            Transaction transaction = ledger.getTransactionById(id);
            ledger.deleteTransaction(transaction);
            csvFileHandler.delete(transaction);
            System.out.println("Transaction deleted successfully.");
            System.out.println();
        } catch (CsvFileHandler.CsvFileHandlerException | Ledger.TransactionNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void setType() {
        System.out.print("Enter the transaction ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        System.out.print("Enter the transaction type: ");
        String type = scanner.nextLine();

        try {
            Transaction transaction = ledger.getTransactionById(id);
            transaction.setType(type);
            csvFileHandler.update(transaction);
            System.out.println("Transaction type updated successfully.");
            System.out.println();
        } catch (CsvFileHandler.CsvFileHandlerException | Ledger.TransactionNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void saveAndQuit() {
        try {
            csvFileHandler.save();
            System.out.println("Ledger saved successfully.");
            System.out.println("Goodbye!");
        } catch (CsvFileHandler.CsvFileHandlerException e) {
            System.out.println("Error while saving ledger: " + e.getMessage());
            System.out.println("Exiting without saving changes.");
            System.out.println("Goodbye!");
        }
        System.exit(0);
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Ledger Menu:");
        System.out.println("1. Add Transaction");
        System.out.println("2. View Transactions by Date Range");
        System.out.println("3. View Transactions for Current Month");
        System.out.println("4. Categorize Transactions");
        System.out.println("5. Save and Quit");
        System.out.println();
    }

    private static void addTransaction() {
        System.out.print("Enter amount: ");
        String amountString = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        System.out.print("Enter time (HH:MM:SS): ");
        String timeString = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        try {
            Transaction transaction = new Transaction(amountString, dateString, timeString, description, vendor);
            ledger.addTransaction(transaction);
            System.out.println("Transaction added successfully.");
        } catch (Transaction.InvalidInputException e) {
            System.out.println("Invalid transaction: " + e.getMessage());
        }
    }

    private static void viewTransactionsByDateRange() {
        System.out.print("Enter the start date (YYYY-MM-DD): ");
        String startDateString = scanner.nextLine();
        System.out.print("Enter the end date (YYYY-MM-DD): ");
        String endDateString = scanner.nextLine();
        try {
            List<Transaction> transactions = csvFileHandler.getByDateRange(startDateString, endDateString);
            Collections.sort(transactions);
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toCSV());
            }
        } catch (CsvFileHandler.CsvFileHandlerException e) {
            System.out.println("Error while accessing CSV file: " + e.getMessage());
        }
    }

    private static void viewTransactionsForCurrentMonth() {
        List<Transaction> transactions = ledger.getMonthToDateTransactions();
        Collections.sort(transactions);
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toCSV());
        }
    }

    private static void categorizeTransactions() {
        List<Transaction> transactions = ledger.getUncategorizedTransactions();
        Collections.sort(transactions);
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toCSV());
        }
        System.out.println("Enter the ID of the transaction you wish to categorize (or 'q' to quit):");
        String input = scanner.nextLine();
        while (!input.equals("q")) {
            try {
                int id = Integer.parseInt(input);
                Transaction transaction = ledger.getTransactionById(id);
                if (transaction == null) {
                    System.out.println("Invalid transaction ID. Please try again.");
                } else {
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    transaction.setType(category);
                    System.out.println("Transaction categorized successfully.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid transaction ID.");
            }
            System.out.println("Enter the ID of the transaction you wish to categorize (or 'q' to quit):");
            input = scanner.nextLine();
        }
    }

    private static void saveAndQuit() {
        try {
            csvFileHandler.save();
            System.out.println("Ledger saved successfully.");
            System.out.println("Goodbye!");
        } catch (CsvFileHandler.CsvFileHandlerException e) {
            System.out.println("Error while saving ledger: " + e.getMessage());
            System.out.println("Exiting without saving changes.");
        }
        scanner.close();
        System.exit(0);
    }
}