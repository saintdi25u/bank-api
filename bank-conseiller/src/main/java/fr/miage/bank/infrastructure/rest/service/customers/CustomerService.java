package fr.miage.bank.infrastructure.rest.service.customers;

import java.util.List;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.Customer;

public interface CustomerService {

    public Customer modify(long idUser, Customer customer);

    public Customer create(Customer customer);

    public List<Customer> findAll();

    public List<Loan> getLoansPending(Long userId);

}
