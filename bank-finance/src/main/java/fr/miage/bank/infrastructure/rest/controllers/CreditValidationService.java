package fr.miage.bank.infrastructure.rest.controllers;

import org.springframework.web.bind.annotation.RestController;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.infrastructure.repository.CreditRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CreditValidationService {

    private Environment environment;

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    public CreditValidationService(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/finance/{id}/validation/revenus")
    public String getValidationCreditRequest(@PathVariable(value = "id") long creditRequestId) {
        System.out.println("Validation des revenus");
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        if (creditRequest.getRevenue3dernierreAnnee() < 25000.0) {
            return new String("ko");
        } else {
            return new String("ok");
        }
    }

}
