package fr.miage.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.EcheanceCredit;

public interface EcheanceCreditRepository extends JpaRepository<EcheanceCredit, Long> {

    EcheanceCredit findByCreditRequest(CreditRequest creditRequest);
}
