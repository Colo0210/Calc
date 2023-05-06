import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Transaction implements Comparable<Transaction> {
    private static int nextId = 1;
    private String type;
    private int id;
    private BigDecimal amount;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;

    public Transaction(BigDecimal amount, LocalDate date, LocalTime time, String description, String vendor) {
        this.id = nextId++;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.type = "";
    }


    public Transaction(String amount, String date, String time, String description, String vendor) throws InvalidInputException {
        if (!isValidDate(date)) {
            throw new InvalidInputException("Invalid date format");
        }
        if (!isValidAmount(amount)) {
            throw new InvalidInputException("Invalid amount");
        }
        if (!isValidTime(time)) {
            throw new InvalidInputException("Invalid time format");
        }

        this.id = nextId++;
        this.amount = new BigDecimal(amount, Locale.US);
        this.date = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        this.time = LocalTime.parse(time, DateTimeFormatter.ISO_TIME);
        this.description = description;
        this.vendor = vendor;
        this.type = "";
    }

    public static boolean isValidAmount(String amount) {
        try {
            new BigDecimal(amount, Locale.US);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return getTransactionsByDateRange().stream()
                .filter(transaction -> {
                    LocalDate date = (LocalDate) transaction.getDate();
                    return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                            (date.isEqual(endDate) || date.isBefore(endDate));
                })
                .collect(Collectors.toList());
    }

    private List<Transaction> getTransactionsByDateRange() {
        return null;
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Comparable<ChronoLocalDate> getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Transaction other) {
        int dateComparison = date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return time.compareTo(other.time);
    }

    public static boolean isValidTime(String timeString) {
        try {
            LocalTime.parse(timeString, DateTimeFormatter.ISO_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidTransaction(String[] parts) {
        if (parts.length != 5) {
            return false;
        }
        if (!isValidAmount(parts[0])) {
            return false;
        }
        if (!isValidDate(parts[1])) {
            return false;
        }
        if (!isValidTime(parts[2])) {
            return false;
        }
        return true;
    }

    public static Transaction fromString(String transactionString) throws InvalidTransactionException {
        String[] parts = transactionString.split(",");
        if (!isValidTransaction(parts)) {
            throw new InvalidTransactionException("Invalid transaction format: " + transactionString);
        } else {
            BigDecimal amount = new BigDecimal(parts[0], Locale.US);
            LocalDate date = LocalDate.parse(parts[1], DateTimeFormatter.ISO_DATE);
            LocalTime time = LocalTime.parse(parts[2], DateTimeFormatter.ISO_TIME);
            String description = parts[3];
            String vendor = parts[4];
            return new Transaction(amount, date, time, description, vendor);
        }
    }

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionException extends Exception {
        public InvalidTransactionException(String message) {
            super(message);
        }
    }
}