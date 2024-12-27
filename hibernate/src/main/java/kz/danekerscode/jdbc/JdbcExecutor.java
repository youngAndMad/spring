package kz.danekerscode.jdbc;

import java.util.Iterator;

public class JdbcExecutor {

    public static void main(String[] args) {
        var customerDao = CustomerDao.getInstance();

        System.out.println("Saving new customer");
        var customer = customerDao.save("John Doe", 1000.0);
        System.out.println(customer);
        System.out.println("Fetching all customers");
        Iterator<Customer> customerIterator = customerDao.findAll();

        while (customerIterator.hasNext()) {
            System.out.println(customerIterator.next());
        }
    }
}
