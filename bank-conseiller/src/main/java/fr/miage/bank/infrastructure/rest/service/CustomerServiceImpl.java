package fr.miage.bank.infrastructure.rest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.repository.CreditRequestRepository;
import fr.miage.bank.infrastructure.repository.CustomerRepository;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @Override
    public Customer modify(long idUser, Customer customer) {
        Customer customerToModify = customerRepository.findById(idUser).orElseThrow(() -> new ResourceNotFound());
        customerToModify.setAdresse(customer.getAdresse());
        customerToModify.setDateNaissance(customer.getDateNaissance());
        customerToModify.setNom(customer.getNom());
        customerToModify.setPrenom(customer.getPrenom());
        customerToModify.setEmail(customer.getEmail());
        customerToModify.setEmploie(customer.getEmploie());
        return customerRepository.save(customerToModify);
    }

    @Override
    public Customer create(Customer customer) {
        customer.setCreditRequests(new ArrayList<CreditRequest>());
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<CreditRequest> getCreditRequestsPending(Long userId) {
        List<CreditRequest> creditRequests = creditRequestRepository
                .findByCustomerAndStatusNotIn(customerRepository.findById(userId).get(),
                        Arrays.asList(StatusEnum.ACCEPTATION, StatusEnum.REJET))
                .stream().toList();
        return creditRequests;
    }

}
