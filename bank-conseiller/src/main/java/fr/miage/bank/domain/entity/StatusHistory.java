package fr.miage.bank.domain.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.miage.bank.infrastructure.rest.shared.StatusEnum;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_loan")
    private Loan loan;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusEnum oldStatus;

    @Enumerated(EnumType.STRING) 
    private StatusEnum newStatus;

    @NotNull
    private LocalDate modificationDate;


    public StatusHistory() {
    }
    
    public StatusHistory(Loan loan, StatusEnum oldStatus, StatusEnum newStatus, LocalDate modificationDate) {
        this.loan = loan;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.modificationDate = modificationDate;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public Loan getLoan() {
        return loan;
    }


    public void setLoan(Loan loan) {
        this.loan = loan;
    }


    public StatusEnum getOldStatus() {
        return oldStatus;
    }


    public void setOldStatus(StatusEnum oldStatus) {
        this.oldStatus = oldStatus;
    }


    public StatusEnum getNewStatus() {
        return newStatus;
    }


    public void setNewStatus(StatusEnum newStatus) {
        this.newStatus = newStatus;
    }


    public LocalDate getModificationDate() {
        return modificationDate;
    }


    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    


}
