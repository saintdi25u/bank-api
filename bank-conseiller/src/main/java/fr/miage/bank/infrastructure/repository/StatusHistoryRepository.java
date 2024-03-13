package fr.miage.bank.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.StatusHistory;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long>{
     List<StatusHistory> findByLoan(Loan loan);
}
