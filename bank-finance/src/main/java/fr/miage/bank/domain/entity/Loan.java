package fr.miage.bank.domain.entity;

import java.time.LocalDate;

import javax.validation.constraints.Positive;
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
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = "Le montant du crédit doit être positif")
    private double loanAmount;

    @Positive(message = "La durée du crédit doit être un nombre positif")
    private int loanDuration;
    @Positive(message = "Les revenues des 3 dernières années doit être un nombre positif")
    private double revenue3dernierreAnnee;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @NotNull
    private LocalDate lastModified;
    @NotNull
    private LocalDate requestDate;

    @OneToOne(mappedBy = "loan")
    private CreditDeadline creditDeadline;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private StatusEnum proposalAdvisor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(int loanDuration) {
        this.loanDuration = loanDuration;
    }

    public double getRevenue3dernierreAnnee() {
        return revenue3dernierreAnnee;
    }

    public void setRevenue3dernierreAnnee(double revenue3dernierreAnnee) {
        this.revenue3dernierreAnnee = revenue3dernierreAnnee;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public CreditDeadline getCreditDeadline() {
        return creditDeadline;
    }

    public void setCreditDeadline(CreditDeadline creditDeadline) {
        this.creditDeadline = creditDeadline;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public StatusEnum getProposalAdvisor() {
        return proposalAdvisor;
    }

    public void setProposalAdvisor(StatusEnum proposalAdvisor) {
        this.proposalAdvisor = proposalAdvisor;
    }


}