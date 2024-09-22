package kz.danekerscode.jpareaderwriterds.service;

import jakarta.persistence.EntityManager;
import kz.danekerscode.jpareaderwriterds.annotation.UseDS;
import kz.danekerscode.jpareaderwriterds.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kz.danekerscode.jpareaderwriterds.config.DatasourceType.READER;
import static kz.danekerscode.jpareaderwriterds.config.DatasourceType.WRITER;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final EntityManager em;

    @UseDS(WRITER)
    @Transactional
    public void clearWriter() {
        em.createQuery("delete from Customer").executeUpdate();
    }

    @Transactional
    @UseDS(READER)
    public void clearReader() {
        em.createQuery("delete from Customer").executeUpdate();
    }

    @UseDS(WRITER)
    @Transactional
    public void saveToWriter(Customer customer) {
        em.persist(customer);
        em.flush();
    }

    @UseDS(READER)
    @Transactional
    public void saveToReader(Customer customer) {
        em.persist(customer);
        em.flush();
    }

    @UseDS(WRITER)
    public List<Customer> findAllFromWriter() {
        return em.createQuery("select c from Customer c", Customer.class)
                .getResultList();
    }

    @UseDS(READER)
    public List<Customer> findAllFromReader() {
        return em.createQuery("select c from Customer c", Customer.class)
                .getResultList();
    }
}
