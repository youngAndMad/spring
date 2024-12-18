package kz.danekerscode.basejparepo.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/fill")
    void fill(@RequestParam int count) {
        for (int i = 0; i < count; i++) {
            Customer entity = new Customer();
            entity.setUuid(UUID.randomUUID().toString());
            customerRepository.save(entity);
        }
    }

    @GetMapping
    Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

}
