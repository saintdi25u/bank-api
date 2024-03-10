package fr.miage.bank.domain.entity;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import ch.qos.logback.core.status.Status;
import fr.miage.bank.infrastructure.rest.shared.CreditType;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CreditRequest {

    public CreditRequest(long id,
            @Positive(message = "Le montant du crédit doit être positif") double montantCredit,
            @Positive(message = "La durée du crédit doit être un nombre positif") int dureeCredit,
            @Positive(message = "Les revenues des 3 dernières années doit être un nombre positif") double revenue3dernierreAnnee,
            StatusEnum status,
            @NotNull LocalDate lastModified, @NotNull LocalDate dateDemande,
            CreditType creditType,
            StatusEnum propositionConseiller,
            Customer customer) {
        this.id = id;
        this.montantCredit = montantCredit;
        this.dureeCredit = dureeCredit;
        this.revenue3dernierreAnnee = revenue3dernierreAnnee;
        this.status = status;
        this.lastModified = lastModified;
        this.dateDemande = dateDemande;
        this.creditType = creditType;
        this.customer = customer;
        this.propositionConseiller = propositionConseiller;
    }

    public CreditRequest() {
    }

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
    private StatusEnum propositionConseiller;

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

    public EcheanceCredit getEcheanceCredit() {
        return echeanceCredit;
    }

    public Customer getCustomer() {
        return customer;
    }

    public StatusEnum getPropositionConseiller() {
        return propositionConseiller;
    }

    public void setPropositionConseiller(StatusEnum propositionConseiller) {
        this.propositionConseiller = propositionConseiller;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setId(long id) {
        this.id = id;
    }

    // public void setNom(String nom) {
    // this.nom = nom;
    // }

    // public void setPrenom(String prenom) {
    // this.prenom = prenom;
    // }

    // public String getAdresse() {
    // return adresse;
    // }

    public long getId() {
        return id;
    }

    // public String getNom() {
    // return nom;
    // }

    // public String getPrenom() {
    // return prenom;
    // }

    // public String getDateNaissance() {
    // return dateNaissance;
    // }

    // public void setDateNaissance(String dateNaissance) {
    // this.dateNaissance = dateNaissance;
    // }

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

    // public String getEmplois() {
    // return emplois;
    // }

    // public void setEmplois(String emplois) {
    // this.emplois = emplois;
    // }

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

    // public Status getPropositionConseiller() {
    // return propositionConseiller;
    // }

    // public void setPropositionConseiller(Status propositionConseiller) {
    // this.propositionConseiller = propositionConseiller;
    // }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

}
