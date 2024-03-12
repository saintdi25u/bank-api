package fr.miage.bank.infrastructure.rest.controllers;

import org.springframework.web.bind.annotation.RestController;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.infrastructure.repository.LoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class LoanValidationService {

    private Environment environment;

    @Autowired
    private LoanRepository loanRepository;

    public LoanValidationService(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/{id}/validate")
    public String getValidationLoan(@PathVariable(value = "id") long loanId) {
        System.out.println("Validation des revenus");
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        if (loan.getRevenue3dernierreAnnee() < 25000.0) {
            return new String("ko");
        } else {
            return new String("ok");
        }
    }

}
