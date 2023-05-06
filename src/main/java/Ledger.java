import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Ledger {
    private final List<Transaction> transactions;

    public Ledger() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public void updateTransaction(Transaction oldTransaction, Transaction newTransaction) {
        transactions.remove(oldTransaction);
        transactions.add(newTransaction);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<Transaction> getDeposits() {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

    public List<Transaction> getPayments() {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactions.stream()
                .filter(transaction -> {
                    LocalDate date = LocalDate.parse((CharSequence) transaction.getDate());
                    return (date.isEqual(start) || date.isAfter(start)) &&
                            (date.isEqual(end) || date.isBefore(end));
                })
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByVendor(String vendor) {
        return transactions.stream()
                .filter(transaction -> transaction.getVendor().equalsIgnoreCase(vendor))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByAmountRange(double minAmount, double maxAmount) {
        BigDecimal min = BigDecimal.valueOf(minAmount);
        BigDecimal max = BigDecimal.valueOf(maxAmount);
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(min) >= 0 && transaction.getAmount().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            BigDecimal amount = new BigDecimal(String.valueOf(transaction.getAmount()));
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                balance = balance.add(amount);
            } else {
                balance = balance.subtract(amount);
            }
        }
        return balance;
    }

    public List<Transaction> getMonthToDateTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        return getTransactionsByDateRange(firstDayOfMonth.toString(), today.toString());
    }

    public List<Transaction> getPreviousMonthTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfPreviousMonth = LocalDate.of(today.getYear(), today.getMonth().minus(1), 1);
        LocalDate lastDayOfPreviousMonth = LocalDate.of(today.getYear(), today.getMonth(), 1).minusDays(1);
        return getTransactionsByDateRange(firstDayOfPreviousMonth.toString(), lastDayOfPreviousMonth.toString());
    }

    public List<Transaction> getYearToDateTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfYear = LocalDate.of(today.getYear(), Month.JANUARY, 1);
        return getTransactionsByDateRange(firstDayOfYear.toString(), today.toString());
    }

    public List<Transaction> getPreviousYearTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfPreviousYear = LocalDate.of(today.getYear() - 1, Month.JANUARY, 1);
        LocalDate lastDayOfPreviousYear = LocalDate.of(today.getYear() - 1, Month.DECEMBER, 31);
        return getTransactionsByDateRange(firstDayOfPreviousYear.toString(), lastDayOfPreviousYear.toString());
    }

    public List<Transaction> getTransactionsByAmount(BigDecimal amount) {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().equals(amount))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(minAmount) >= 0 && transaction.getAmount().compareTo(maxAmount) <= 0)
                .collect(Collectors.toList());
    }
}