package fr.miage.bank.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

public interface LoanRepository extends JpaRepository<Loan, Long> {
     List<Loan> findByStatusNotIn(List<StatusEnum> status);

     List<Loan> findByCustomerAndStatusNotIn(Customer customer, List<StatusEnum> status);
}
