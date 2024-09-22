package kz.danekerscode.jpareaderwriterds.service;

import kz.danekerscode.jpareaderwriterds.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAspectJAutoProxy
@EnableTransactionManagement
@SpringBootTest
@RunWith(SpringRunner.class)
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService.clearReader();
        customerService.clearWriter();
    }

    @Test
    void findAllFromReader_hasSize2() {
        customerService.saveToWriter(new Customer("Marry Doe"));
        customerService.saveToWriter(new Customer("Jane Doe"));
        assertEquals(2, customerService.findAllFromWriter().size());
    }

    @Test
    void findAllFromWriter_hasSize3() {
        customerService.saveToReader(new Customer("Mike Tyson"));
        customerService.saveToReader(new Customer("Jock"));
        customerService.saveToReader(new Customer("Some"));
        assertEquals(3, customerService.findAllFromReader().size());
    }

}