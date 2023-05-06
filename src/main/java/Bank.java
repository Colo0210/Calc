import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private static int idCounter = 0;
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private Map<Integer, Customer> customers;

    public Bank(String name, String address, String phoneNumber) {
        this.id = generateUniqueId();
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.customers = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private static synchronized int generateUniqueId() {
        return ++idCounter;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer getCustomerById(int customerId) {
        return customers.get(customerId);
    }
}