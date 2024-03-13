package fr.miage.bank.infrastructure.rest.controllers;

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

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.repository.CustomerRepository;
import fr.miage.bank.infrastructure.rest.assembler.LoanModelAssembler;
import fr.miage.bank.infrastructure.rest.assembler.UserModelAssembler;
import fr.miage.bank.infrastructure.rest.service.customers.CustomerService;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import jakarta.transaction.Transactional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
public class CustomerController {

    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private UserModelAssembler customerModelAssembler;
    private LoanModelAssembler loanModelAssembler;

    public CustomerController(CustomerService userService, UserModelAssembler userModelAssembler,
            CustomerRepository customerRepository, LoanModelAssembler loanModelAssembler) {
        this.customerService = userService;
        this.customerModelAssembler = userModelAssembler;
        this.customerRepository = customerRepository;
        this.loanModelAssembler = loanModelAssembler;
    }

    // Permet de récupérer tous les utilisateurs qui ont deja fait un crédit
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> getAll() {
        return ResponseEntity.ok(customerModelAssembler.toCollectionModel(customerRepository.findAll()));
    }

    // Permet de récupérer toutes les demandes de crédit en cours pour un
    // utilisateur
    @GetMapping("/{id}/credits")
    public ResponseEntity<?> getLoansPending(
            @PathVariable("id") long userId) {
        if (!customerRepository.findById(userId).isPresent()) {
            return ResponseEntity.badRequest().body("L'utilisateur n'existe pas");
        }
        return ResponseEntity
                .ok(loanModelAssembler.toCollectionModel(customerService.getLoansPending(userId)));
    }

    // Permet de récupérer un utilisateur par son id
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") long userId) {
        if (!customerRepository.findById(userId).isPresent()) {
            return ResponseEntity.badRequest().body("L'utilisateur n'existe pas");
        }
        return ResponseEntity.ok(customerModelAssembler
                .toModel(customerRepository.findById(userId).get()));
    }

    // Permet de créer un utilisateur
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Customer customer) {
        if (customer.getEmail() == "" || customer.getFirstName() == "" || customer.getLastName() == ""
                || customer.getBirthDate() == "" || customer.getAdress() == "") {
            return ResponseEntity.badRequest().body("Il ne peut pas y avoir d'informations manquantes");
        }
        customerService.create(customer);
        return ResponseEntity.created(linkTo(getClass()).slash(customer.getCustomer_id()).toUri())
                .body(customerModelAssembler.toModel(customer));
    }

    // Permet de modifier un utilisateur
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> modify(@PathVariable("id") long userId, @RequestBody Customer user) {
        if (user.getEmail() == "" || user.getFirstName() == "" || user.getLastName() == "" || user.getBirthDate() == ""
                || user.getAdress() == "") {
            return ResponseEntity.badRequest().body("Il ne peut pas y avoir d'informations manquantes");
        }
        return ResponseEntity.ok(customerModelAssembler.toModel(customerService.modify(userId, user)));
    }

}
