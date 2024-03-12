package fr.miage.bank.infrastructure.rest.service.customers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.repository.LoanRepository;
import fr.miage.bank.infrastructure.repository.CustomerRepository;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository creditRequestRepository;

    @Override
    public Customer modify(long idUser, Customer customer) {
        Customer customerToModify = customerRepository.findById(idUser).orElseThrow(() -> new ResourceNotFound());
        customerToModify.setAdress(customer.getAdress());
        customerToModify.setBirthDate(customer.getBirthDate());
        customerToModify.setLastName(customer.getLastName());
        customerToModify.setFirstName(customer.getFirstName());
        customerToModify.setEmail(customer.getEmail());
        customerToModify.setJob(customer.getJob());
        return customerRepository.save(customerToModify);
    }

    @Override
    public Customer create(Customer customer) {
        customer.setLoans(new ArrayList<Loan>());
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Loan> getLoansPending(Long userId) {
        List<Loan> loan = creditRequestRepository
                .findByCustomerAndStatusNotIn(customerRepository.findById(userId).get(),
                        Arrays.asList(StatusEnum.ACCEPTATION, StatusEnum.REJET))
                .stream().toList();
        return loan;
    }

}
