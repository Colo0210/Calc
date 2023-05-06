import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class TransactionManager {
    private List<Transaction> transactions;
    private CSVFileHandler fileHandler;

    public TransactionManager(String fileName) throws IOException {
        fileHandler = new CSVFileHandler(fileName);
        transactions = fileHandler.readTransactionsFromFile("transactions.csv");
    }

    public void addTransaction(Transaction newTransaction) {
        transactions.add(newTransaction);
        try {
            fileHandler.writeTransactionToFile(newTransaction);
        } catch (IOException e) {
            System.err.println("Error writing transaction to file");
        }
    }

    public boolean deleteTransaction(Transaction transaction) {
        if (transactions.remove(transaction)) {
            try {
                fileHandler.deleteTransactionFromFile(transaction);
            } catch (IOException e) {
                System.err.println("Error deleting transaction from file");
            }
            return true;
        } else {
            return false;
        }
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<Transaction> getTransactionsOnDate(LocalDate date) throws IOException {
        return fileHandler.searchTransactionsByDate(date, date);
    }

    public List<Transaction> getTransactionsWithVendor(String vendor) throws IOException {
        return fileHandler.searchTransactionsByVendor(vendor);
    }

    public boolean updateTransaction(Transaction oldTransaction, Transaction updatedTransaction) {
        int index = transactions.indexOf(oldTransaction);
        if (index >= 0) {
            transactions.set(index, updatedTransaction);
            try {
                fileHandler.updateTransactionInFile(oldTransaction, updatedTransaction);
                return true;
            } catch (IOException e) {
                System.err.println("Error updating transaction in file");
            }
        }
        return false;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            total = total.add(transaction.getAmount());
        }
        return total;
    }

    public BigDecimal getAverageAmount() {
        BigDecimal total = getTotalAmount();
        return total.divide(BigDecimal.valueOf(transactions.size()), 2, RoundingMode.HALF_UP);
    }

    public Transaction getHighestTransaction() {
        Transaction maxTransaction = transactions.get(0);
        for (Transaction transaction : transactions) {
            if (transaction.getAmount().compareTo(maxTransaction.getAmount()) > 0) {
                maxTransaction = transaction;
            }
        }
        return maxTransaction;
    }

    public Transaction getLowestTransaction() {
        Transaction minTransaction = transactions.get(0);
        for (Transaction transaction : transactions) {
            if (transaction.getAmount().compareTo(minTransaction.getAmount()) < 0) {
                minTransaction = transaction;
            }
        }
        return minTransaction;
    }

    public List<Transaction> sortByDate() throws IOException {
        List<Transaction> result = fileHandler.readTransactionsFromFile("transactions.csv");
        Collections.sort(result);
        return result;
    }

    public List<Transaction> sortByAmount() {
        List<Transaction> result = null;
        try {
            result = fileHandler.readTransactionsFromFile("transactions.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result.sort((t1, t2) -> t1.getAmount().compareTo(t2.getAmount()));
        return result;
    }

    public List<Transaction> searchTransactionsByDate(LocalDate startDate, LocalDate endDate) throws IOException {
        return fileHandler.searchTransactionsByDate(startDate, endDate);
    }

    public boolean deleteTransaction(int index) {
        if (index < 0 || index >= transactions.size()) {
            return false;
        }
        Transaction transactionToRemove = transactions.get(index);
        if (transactions.remove(transactionToRemove)) {
            try {
                fileHandler.deleteTransactionFromFile(transactionToRemove);
                return true;
            } catch (IOException e) {
                System.err.println("Error deleting transaction from file");
            }
        }
        return false;
    }
}