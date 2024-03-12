package fr.miage.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.CreditDeadline;

public interface CreditDeadlineRepository extends JpaRepository<CreditDeadline, Long> {

    CreditDeadline findByLoan(Loan loan);
}
