package fr.miage.bank.infrastructure.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.repository.LoanRepository;
import fr.miage.bank.infrastructure.rest.assembler.CreditDeadlineModelAssembler;
import fr.miage.bank.infrastructure.rest.service.echeances.CreditDeadlineService;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;

@RestController
@RequestMapping("/api/credits/echeances")
public class CreditDeadlineController {

    @Autowired
    private CreditDeadlineService creditDeadlineService;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/{id}")
    public ResponseEntity<CreditDeadline> getCreditDeadline(@PathVariable("id") long idLoan) {
        Loan loan = loanRepository.findById(idLoan)
                .orElseThrow(() -> new ResourceNotFound());
        if (loan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(creditDeadlineService.getByCreditRequest(loan.getCreditDeadline()));
    }

}
