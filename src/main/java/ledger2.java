import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public  Ledger2 {
    private final List<Transaction> transactions;

    public Ledger2r() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getDeposits() {
        List<Transaction> collect = transactions.stream().filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        return collect;
    }

    public List<Transaction> getPayments() {
        List<Transaction> collect = transactions.stream().filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toList());
        return collect;
    }

    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactions.stream()
                .filter(t -> {
                    LocalDate date = LocalDate.from(t.getDate());
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByVendor(String vendor) {
        return transactions.stream().filter(t -> t.getVendor().equals(vendor)).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByAmount(BigDecimal amount) {
        return transactions.stream().filter(t -> t.getAmount().equals(amount)).collect(Collectors.toList());
    }

    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            balance = balance.add(transaction.getAmount());
        }
        return balance;
    }

    public List<Transaction> getMonthToDateTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        return getTransactionsByDateRange(startOfMonth.toString(), today.toString());
    }

    public List<Transaction> getPreviousMonthTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate startOfPrevMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPrevMonth = today.withDayOfMonth(1).minusDays(1);
        return getTransactionsByDateRange(startOfPrevMonth.toString(), endOfPrevMonth.toString());
    }

    public List<Transaction> getYearToDateTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        return getTransactionsByDateRange(startOfYear.toString(), today.toString());
    }

    public List<Transaction> getPreviousYearTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate startOfPrevYear = today.minusYears(1).withDayOfYear(1);
        LocalDate endOfPrevYear = today.withDayOfYear(1).minusDays(1);
        return getTransactionsByDateRange(startOfPrevYear.toString(), endOfPrevYear.toString());
    }

    public void updateTransaction(Transaction oldTransaction, Transaction newTransaction) {
        int index = transactions.indexOf(oldTransaction);
        if (index >= 0) {
            transactions.set(index, newTransaction);
        }
    }

    public List<Transaction> getTransactionsByAmountRange(BigDecimal startAmount, BigDecimal endAmount) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (amount.compareTo(BigDecimal.valueOf(startAmount)) >= 0) {
                if (amount.compareTo(BigDecimal.valueOf(endAmount)) <= 0) {
                    result.add(transaction);
                }
            }
        }
        return result;
    }
}

