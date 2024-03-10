package fr.miage.bank.infrastructure.rest.controllers;

import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.Response;
import com.netflix.discovery.converters.Auto;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.repository.CustomerRepository;
import fr.miage.bank.infrastructure.rest.assembler.CreditRequestModelAssembler;
import fr.miage.bank.infrastructure.rest.assembler.UserModelAssembler;
import fr.miage.bank.infrastructure.rest.service.CustomerService;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import jakarta.transaction.Transactional;

import static java.util.stream.Collectors.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private UserModelAssembler customerModelAssembler;
    private CreditRequestModelAssembler creditRequestModelAssembler;

    public CustomerController(CustomerService userService, UserModelAssembler userModelAssembler,
            CustomerRepository customerRepository, CreditRequestModelAssembler creditRequestModelAssembler) {
        this.customerService = userService;
        this.customerModelAssembler = userModelAssembler;
        this.customerRepository = customerRepository;
        this.creditRequestModelAssembler = creditRequestModelAssembler;
    }

    // Permet de récupérer tous les utilisateurs qui ont deja fait un crédit
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> getAll() {
        return ResponseEntity.ok(customerModelAssembler.toCollectionModel(customerRepository.findAll()));
    }

    // Permet de récupérer toutes les demandes de crédit en cours pour un
    // utilisateur
    @GetMapping("/{id}/credits")
    public ResponseEntity<CollectionModel<EntityModel<CreditRequest>>> getAllCreditRequestPending(
            @PathVariable("id") long userId) {
        return ResponseEntity
                .ok(creditRequestModelAssembler.toCollectionModel(customerService.getCreditRequestsPending(userId)));
    }

    // Permet de récupérer un utilisateur par son id
    @GetMapping("/{id}")
    public EntityModel<Customer> get(@PathVariable("id") long userId) {
        return customerModelAssembler
                .toModel(customerRepository.findById(userId).orElseThrow(() -> new ResourceNotFound()));
    }

    // Permet de créer un utilisateur
    @PostMapping
    public ResponseEntity<EntityModel<Customer>> create(
            @RequestBody Customer customer) {
        customerService.create(customer);
        return ResponseEntity.created(linkTo(getClass()).slash(customer.getCustomer_id()).toUri())
                .body(customerModelAssembler.toModel(customer));
    }

    // Permet de modifier un utilisateur
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EntityModel<Customer>> modify(@PathVariable("id") long userId, @RequestBody Customer user) {
        return ResponseEntity.ok(customerModelAssembler.toModel(customerService.modify(userId, user)));
    }

}
