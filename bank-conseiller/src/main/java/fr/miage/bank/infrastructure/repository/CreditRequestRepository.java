package fr.miage.bank.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Repository;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
     List<CreditRequest> findByStatusNotIn(List<StatusEnum> status);

     List<CreditRequest> findByCustomerAndStatusNotIn(Customer customer, List<StatusEnum> status);
}
