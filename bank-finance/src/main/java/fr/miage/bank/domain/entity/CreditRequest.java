package fr.miage.bank.domain.entity;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.cglib.core.Local;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class CreditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = "Le montant du crédit doit être positif")
    private double montantCredit;

    @Positive(message = "La durée du crédit doit être un nombre positif")
    private int dureeCredit;
    @Positive(message = "Les revenues des 3 dernières années doit être un nombre positif")
    private double revenue3dernierreAnnee;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    private CreditType creditType;

    @NotNull
    private LocalDate lastModified;
    @NotNull
    private LocalDate dateDemande;

    @OneToOne(mappedBy = "creditRequest")
    private EcheanceCredit echeanceCredit;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private StatusEnum propositionConseiller;

    public StatusEnum getPropositionConseiller() {
        return propositionConseiller;
    }

    public void setPropositionConseiller(StatusEnum propositionConseiller) {
        this.propositionConseiller = propositionConseiller;
    }

    public double getMontantCredit() {
        return montantCredit;
    }

    public void setMontantCredit(double montantCredit) {
        this.montantCredit = montantCredit;
    }

    public int getDureeCredit() {
        return dureeCredit;
    }

    public void setDureeCredit(int dureeCredit) {
        this.dureeCredit = dureeCredit;
    }

    public double getRevenue3dernierreAnnee() {
        return revenue3dernierreAnnee;
    }

    public void setRevenue3dernierreAnnee(double revenue3dernierreAnnee) {
        this.revenue3dernierreAnnee = revenue3dernierreAnnee;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

}