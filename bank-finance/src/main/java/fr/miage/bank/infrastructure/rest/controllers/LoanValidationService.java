package fr.miage.bank.infrastructure.rest.controllers;

import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class LoanValidationService {

    private Environment environment;


    public LoanValidationService(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/{id}/validate")
    public String getValidationLoan(@RequestParam(value = "amount") double amount) {
        System.out.println("Validation des revenus");
        if (amount < 25000.0) {
            return new String("ko");
        } else {
            return new String("ok");
        }
    }

}
