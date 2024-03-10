package fr.miage.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.miage.bank.domain.entity.CreditRequest;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}
