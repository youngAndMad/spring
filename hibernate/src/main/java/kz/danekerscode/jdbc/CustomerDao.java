package kz.danekerscode.jdbc;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Iterator;

public class CustomerDao {
    private static volatile CustomerDao instance;

    private CustomerDao() {
    }

    public static CustomerDao getInstance() {
        if (instance == null) {
            synchronized (CustomerDao.class) {
                if (instance == null) {
                    instance = new CustomerDao();
                }
            }
        }
        return instance;
    }

    @SneakyThrows
    public Iterator<Customer> findAll() {
        var connection = JdbcConnectionPool.get();

        var statement = connection.createStatement();
        statement.execute("SELECT * FROM customer");
        ResultSet resultSet = statement.getResultSet();

        return new Iterator<>() {
            @SneakyThrows
            @Override
            public boolean hasNext() {
                return resultSet.next();
            }

            @SneakyThrows
            @Override
            public Customer next() {
                return mapCustomer(resultSet);
            }
        };
    }

    @SneakyThrows
    public Customer save(String name, double salary) {
        var connection = JdbcConnectionPool.get();

        var statement = connection.prepareStatement("INSERT INTO customer (name, salary) VALUES (?, ?) returning *");
        statement.setString(1, name);
        statement.setDouble(2, salary);

        var resultSet = statement.executeQuery();

        var hasElement = resultSet.next();

        if (hasElement) {
            return mapCustomer(resultSet);
        } else {
            throw new RuntimeException("Customer not saved");
        }
    }

    @SneakyThrows
    private static Customer mapCustomer(ResultSet resultSet) {
        var customer = new Customer();
        customer.setId(resultSet.getLong("id"));
        customer.setName(resultSet.getString("name"));
        customer.setSalary(resultSet.getDouble("salary"));
        return customer;
    }
}
