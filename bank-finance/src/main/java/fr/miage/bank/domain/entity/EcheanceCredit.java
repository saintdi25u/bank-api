package fr.miage.bank.domain.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class EcheanceCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private CreditRequest creditRequest;

    @NotNull
    private LocalDate dateDebutCredit;
    @NotNull
    private LocalDate dateFinCredit;

    @NotNull
    private double montantEcheance;
    @NotNull
    private double montantCapital;
    @NotNull
    private double tauxCredit;

    public void setDateDebutCredit(LocalDate dateDebutCredit) {
        this.dateDebutCredit = dateDebutCredit;
    }

    public void setDateFinCredit(LocalDate dateFinCredit) {
        this.dateFinCredit = dateFinCredit;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMontantCapital(double montantCapital) {
        this.montantCapital = montantCapital;
    }

    public void setMontantEcheance(double montantEcheance) {
        this.montantEcheance = montantEcheance;
    }

    public void setTauxCredit(double tauxCredit) {
        this.tauxCredit = tauxCredit;
    }

    public double getTauxCredit() {
        return tauxCredit;
    }

    public long getId() {
        return id;
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public LocalDate getDateDebutCredit() {
        return dateDebutCredit;
    }

    public LocalDate getDateFinCredit() {
        return dateFinCredit;
    }

    public double getMontantCapital() {
        return montantCapital;
    }

    public double getMontantEcheance() {
        return montantEcheance;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest = creditRequest;
    }

}
