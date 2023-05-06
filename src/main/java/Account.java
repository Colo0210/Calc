import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id;
    private List<Transaction> transactions;
    private static int counter = 0;

    public Account() {
        this.id = ++counter;
        this.transactions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            balance = balance.add(transaction.getAmount());
        }
        return balance;
    }


    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate date = (LocalDate) transaction.getDate();
            if (date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate))) {
                result.add(transaction);
            }
        }
        return result;
    }

    public List<Transaction> getTransactionsByVendor(String vendor) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equals(vendor)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Account account = new Account();
        System.out.println("Account ID: " + account.getId());

        Transaction t1 = new Transaction(BigDecimal.valueOf(100.00), LocalDate.now(), LocalTime.now(), "Deposit", "John Smith");
        account.addTransaction(t1);
        Transaction t2 = new Transaction(BigDecimal.valueOf(50.00), LocalDate.now(), LocalTime.now(), "Withdrawal", "Jane Doe");
        account.addTransaction(t2);

        System.out.println("Transactions:");
        for (Transaction transaction : account.getTransactions()) {
            System.out.println(transaction);
        }

        System.out.println("Transactions between " + LocalDate.now().minusDays(1) + " and " + LocalDate.now());
        for (Transaction transaction : account.getTransactionsByDateRange(LocalDate.now().minusDays(1), LocalDate.now())) {
            System.out.println(transaction);
        }

        System.out.println("Transactions by vendor John Smith:");
        for (Transaction transaction : account.getTransactionsByVendor("John Smith")) {
            System.out.println(transaction);
        }
    }
}