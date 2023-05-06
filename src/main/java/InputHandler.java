import java.math.BigDecimal;
import java.util.Scanner;

public class InputHandler {

    private Scanner scanner;

    public InputHandler() {
        scanner = new Scanner(System.in);
    }

    public int getMenuOption() {
        int option = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Please select an option:");
            System.out.println("1. View accounts");
            System.out.println("2. Add account");
            System.out.println("3. Remove account");
            System.out.println("4. View transactions");
            System.out.println("5. Add transaction");
            System.out.println("6. Remove transaction");
            System.out.println("7. Quit");

            System.out.print("Enter option number: ");
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                if (option >= 1 && option <= 7) {
                    validInput = true;
                } else {
                    System.out.println("Invalid option. Please enter a number between 1 and 7.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        scanner.nextLine(); // consume the newline character
        return option;
    }

    public String getAccountNumber() {
        System.out.print("Enter account number: ");
        return scanner.nextLine();
    }

    public String getCustomerName() {
        System.out.print("Enter customer name: ");
        return scanner.nextLine();
    }

    public String getCustomerAddress() {
        System.out.print("Enter customer address: ");
        return scanner.nextLine();
    }

    public String getCustomerPhoneNumber() {
        System.out.print("Enter customer phone number: ");
        return scanner.nextLine();
    }

    public BigDecimal getTransactionAmount() {
        BigDecimal amount = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter transaction amount: ");
            if (scanner.hasNextBigDecimal()) {
                amount = scanner.nextBigDecimal();
                validInput = true;
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        scanner.nextLine(); // consume the newline character
        return amount;
    }

    public String getTransactionDescription() {
        System.out.print("Enter transaction description: ");
        return scanner.nextLine();
    }

    public String getTransactionVendor() {
        System.out.print("Enter transaction vendor: ");
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }

}