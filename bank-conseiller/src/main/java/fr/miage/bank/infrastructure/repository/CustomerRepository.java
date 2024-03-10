package fr.miage.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.miage.bank.domain.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
