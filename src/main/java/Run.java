import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Run {

    public static void main(String[] args) throws IOException {
        String fileName = "transactions.csv";
        TransactionManager manager = new TransactionManager(fileName);

        // Add a new transaction
        LocalDate date = LocalDate.of(2023, 5, 4);
        LocalTime time = LocalTime.of(9, 30);
        String description = "Coffee";
        String vendor = "Starbucks";
        BigDecimal amount = BigDecimal.valueOf(4.99);
        Transaction newTransaction = new Transaction(amount, date, time, description, vendor);
        manager.addTransaction(newTransaction);

        // Get all transactions
        List<Transaction> allTransactions = manager.getAllTransactions();
        for (Transaction transaction : allTransactions) {
            System.out.println(transaction.toString());
        }

        // Get transactions on a specific date
        LocalDate searchDate = LocalDate.of(2023, 5, 4);
        List<Transaction> transactionsOnDate = manager.getTransactionsOnDate(searchDate);
        for (Transaction transaction : transactionsOnDate) {
            System.out.println(transaction);
        }

        // Get transactions with a specific vendor
        String searchVendor = "Starbucks";
        List<Transaction> transactionsWithVendor = manager.getTransactionsWithVendor(searchVendor);
        for (Transaction transaction : transactionsWithVendor) {
            System.out.println(transaction);
        }

        // Update a transaction
        Transaction oldTransaction = allTransactions.get(0);
        Transaction updatedTransaction = new Transaction(BigDecimal.valueOf(10.99), date, time, "Lunch", vendor);
        manager.updateTransaction(oldTransaction, updatedTransaction);

        // Delete a transaction
        manager.deleteTransaction(updatedTransaction);
    }
}