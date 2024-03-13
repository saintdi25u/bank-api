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
public class CreditDeadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToOne(mappedBy = "creditDeadline")
    private Loan loan;

    @NotNull
    private LocalDate loanStartDate;
    @NotNull
    private LocalDate loanEndDate;

    @NotNull
    private double creditDeadlineAmount;
    @NotNull
    private double capitalAmount;
    @NotNull
    private double rateLoan;

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

    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(LocalDate loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public LocalDate getLoanEndDate() {
        return loanEndDate;
    }

    public void setLoanEndDate(LocalDate loanEndDate) {
        this.loanEndDate = loanEndDate;
    }

    public double getCreditDeadlineAmount() {
        return creditDeadlineAmount;
    }

    public void setCreditDeadlineAmount(double creditDeadlineAmount) {
        this.creditDeadlineAmount = creditDeadlineAmount;
    }

    public double getCapitalAmount() {
        return capitalAmount;
    }

    public void setCapitalAmount(double capitalAmount) {
        this.capitalAmount = capitalAmount;
    }

    public double getRateLoan() {
        return rateLoan;
    }

    public void setRateLoan(double rateLoan) {
        this.rateLoan = rateLoan;
    }

}
