package fr.miage.bank.infrastructure.rest.service;

import java.util.List;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.Customer;

public interface CustomerService {

    public Customer modify(long idUser, Customer customer);

    public Customer create(Customer customer);

    public List<Customer> findAll();

    public List<CreditRequest> getCreditRequestsPending(Long userId);

}
