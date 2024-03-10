package fr.miage.bank.infrastructure.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.EcheanceCredit;
import fr.miage.bank.infrastructure.repository.CreditRequestRepository;
import fr.miage.bank.infrastructure.rest.assembler.EcheanceCreditModelAssembler;
import fr.miage.bank.infrastructure.rest.service.EcheanceService;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;

@RestController
@RequestMapping("/api/credits/echeances")
public class EcheanceController {

    @Autowired
    private EcheanceService echeanceService;

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    @GetMapping("/{id}")
    public ResponseEntity<EcheanceCredit> getEcheance(@PathVariable("id") long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());
        return ResponseEntity.ok(echeanceService.getByCreditRequest(creditRequest.getEcheanceCredit()));
    }

}
