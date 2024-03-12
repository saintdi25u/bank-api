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
public class Loan {

    public Loan(long id,
            @Positive(message = "Le montant du crédit doit être positif") double loanAmount,
            @Positive(message = "La durée du crédit doit être un nombre positif") int loanDuration,
            @Positive(message = "Les revenues des 3 dernières années doit être un nombre positif") double revenue3dernierreAnnee,
            StatusEnum status,
            @NotNull LocalDate lastModified, @NotNull LocalDate requestDate,
            CreditType loanType,
            StatusEnum proposalAdvisor,
            Customer customer) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
        this.revenue3dernierreAnnee = revenue3dernierreAnnee;
        this.status = status;
        this.lastModified = lastModified;
        this.requestDate = requestDate;
        this.loanType = loanType;
        this.customer = customer;
        this.proposalAdvisor = proposalAdvisor;
    }

    public Loan() {
    }

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
    private StatusEnum proposalAdvisor;

    @Enumerated(EnumType.STRING)
    private CreditType loanType;

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

public void setLoanDuration(int loanDuration) {
    this.loanDuration = loanDuration;
}
public int getLoanDuration() {
    return loanDuration;
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

    public StatusEnum getProposalAdvisor() {
        return proposalAdvisor;
    }

    public void setProposalAdvisor(StatusEnum proposalAdvisor) {
        this.proposalAdvisor = proposalAdvisor;
    }

  public CreditType getLoanType() {
      return loanType;
  }
  public void setLoanType(CreditType loanType) {
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

 

}
