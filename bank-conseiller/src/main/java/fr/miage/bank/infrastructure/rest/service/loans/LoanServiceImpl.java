package fr.miage.bank.infrastructure.rest.service.loans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netflix.discovery.converters.Auto;

import ch.qos.logback.core.status.Status;
import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.StatusHistory;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.repository.LoanRepository;
import fr.miage.bank.infrastructure.repository.StatusHistoryRepository;
import fr.miage.bank.infrastructure.repository.CustomerRepository;
import fr.miage.bank.infrastructure.repository.CreditDeadlineRepository;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import fr.miage.bank.infrastructure.rest.shared.CreditRates;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    CreditDeadlineRepository creditDeadlineRepository;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    StatusHistoryRepository statusHistoryRepository;


    @Override
    public Loan create(Loan loan) {
        Customer customer = customerRepository.findById(loan.getCustomer().getCustomer_id())
                .orElseThrow(() -> new ResourceNotFound());
        if (customer.getAdress() == null || customer.getJob() == null) {
            return null;
        }
        LocalDate currentDate = this.getCurrentDate("yyyy-MM-dd");
        loan.setRequestDate(currentDate);
        loan.setLastModified(currentDate);
        loan.setStatus(StatusEnum.DEBUT);
        loan.setProposalAdvisor(StatusEnum.DEBUT);
        loan.setCustomer(customer);

        Loan toSave = loanRepository.save(loan);
        statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.DEBUT, null, this.getCurrentDate("yyyy-MM-dd")));
        return toSave;

    }

    public Loan modify(long loanId, Loan loan) {
        Loan loanToModify = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());
        if (loanToModify.getStatus() != StatusEnum.DEBUT) {
            throw new IllegalStateException();
        }
        loanToModify.setLoanType(loan.getLoanType());
        loanToModify.setLoanDuration(loan.getLoanDuration());
        loanToModify.setLoanAmount(loan.getLoanAmount());
        loanToModify.setRevenue3dernierreAnnee(loan.getRevenue3dernierreAnnee());
        loanToModify.setLastModified(this.getCurrentDate("yyyy-MM-dd"));
        return loanRepository.save(loanToModify);
    }

    // public Enum<StatusEnum> getStatusloan(long loadId) {
    //     Loan loan = loanRepository.findById(loadId)
    //             .orElseThrow(() -> new ResourceNotFound());
    //     return loan.getStatus();
    // }

    public Loan send(long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());
        System.out.println(loan.getStatus());
        if (loan.getStatus() != StatusEnum.DEBUT) {
            return loan;
        } else {
            loan.setStatus(StatusEnum.ETUDE);
            loan.setProposalAdvisor(StatusEnum.ETUDE);
            statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.DEBUT, StatusEnum.ETUDE, this.getCurrentDate("yyyy-MM-dd")));
            return loanRepository.save(loan);
        }
    }

    public Loan accept(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());

        if (loan.getStatus() != StatusEnum.ETUDE) {
            throw new IllegalStateException();
        }
        statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.ETUDE, StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd")));
        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), StatusEnum.ACCEPTATION,
                loan.getCustomer());
    }

    public Loan refuse(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());

        if (loan.getStatus() != StatusEnum.ETUDE) {
            throw new IllegalStateException();
        }
        statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.ETUDE, StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd")));

        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), StatusEnum.REJET,
                loan.getCustomer());
    }

    public Loan valid(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());
        if (loan.getStatus() != StatusEnum.VALIDATION) {
            throw new IllegalStateException();
        }

        statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.VALIDATION, StatusEnum.ACCEPTATION, this.getCurrentDate("yyyy-MM-dd")));

        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.ACCEPTATION, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), loan.getProposalAdvisor(),
                loan.getCustomer());
    }

    @Override
    public Loan reject(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());

        if (loan.getStatus() != StatusEnum.VALIDATION) {
            throw new IllegalStateException();
        }

        statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.VALIDATION, StatusEnum.REJET, this.getCurrentDate("yyyy-MM-dd")));

        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.REJET, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), loan.getProposalAdvisor(),
                loan.getCustomer());
    }

    public CreditDeadline createNewEcheanceCredit(Loan loan) {
        CreditDeadline echeanceCredit = new CreditDeadline();
        echeanceCredit.setLoanStartDate(this.getCurrentDate("yyyy-MM-dd"));
        echeanceCredit.setLoanEndDate(this.addMonthToDate(this.getCurrentDate("yyyy-MM-dd"),
                loan.getLoanDuration()));
        echeanceCredit.setCapitalAmount(loan.getLoanAmount());
        echeanceCredit.setRateLoan(CreditRates.rates.get(loan.getLoanType()));
        echeanceCredit.setCreditDeadlineAmount(this.getMensualité(loan.getLoanAmount(),
                loan.getLoanDuration(), echeanceCredit.getRateLoan()));
        echeanceCredit.setLoan(loan);
        return creditDeadlineRepository.save(echeanceCredit);
    }

    private LocalDate addMonthToDate(LocalDate date, int month) {
        return date.plusMonths(month);
    }

    private double getMensualité(double montant, int duree, double taux) {
        taux = taux / 100;
        int nbPaiement = duree * 12;
        double mensualite = (montant * taux) / (1 - Math.pow(1 + taux, -nbPaiement));
        return mensualite;
    }

    private LocalDate getCurrentDate(String pattern) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = currentDate.format(formatter);
        currentDate = LocalDate.parse(formattedDate, formatter);
        return currentDate;
    }

    @Override
    public List<StatusHistory> getStatusHistory(long idLoan) {
        List<StatusHistory> statusHistory = statusHistoryRepository.findByLoan(loanRepository.findById(idLoan).get()).stream().toList();
        System.out.println(statusHistory);
        return statusHistory;
    }

}
