package fr.miage.bank.infrastructure.rest.service.loans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    // Permet de créer une demande de crédit
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
        statusHistoryRepository
                .save(new StatusHistory(loan, StatusEnum.DEBUT, null, this.getCurrentDate("yyyy-MM-dd")));
        return toSave;

    }

    // Permet de modifier une demande de crédit
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

    // Permet d'envoyer une demande de crédit pour étude
    public Loan send(long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());
        System.out.println(loan.getStatus());
        if (loan.getStatus() != StatusEnum.DEBUT) {
            return loan;
        } else {
            loan.setStatus(StatusEnum.ETUDE);
            loan.setProposalAdvisor(StatusEnum.ETUDE);
            statusHistoryRepository.save(
                    new StatusHistory(loan, StatusEnum.DEBUT, StatusEnum.ETUDE, this.getCurrentDate("yyyy-MM-dd")));
            return loanRepository.save(loan);
        }
    }

    // Permet d'accepter une demande de crédit par le conseiller
    public Loan accept(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());

        if (loan.getStatus() != StatusEnum.ETUDE) {
            throw new IllegalStateException();
        }
        statusHistoryRepository.save(
                new StatusHistory(loan, StatusEnum.ETUDE, StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd")));
        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), StatusEnum.ACCEPTATION,
                loan.getCustomer(), loan.getCreditDeadline());
    }

    // Permet de refuser une demande de crédit par le conseiller
    public Loan refuse(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());

        if (loan.getStatus() != StatusEnum.ETUDE) {
            throw new IllegalStateException();
        }
        statusHistoryRepository.save(
                new StatusHistory(loan, StatusEnum.ETUDE, StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd")));

        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), StatusEnum.REJET,
                loan.getCustomer(), loan.getCreditDeadline());
    }

    // Permet de valider une demande de crédit par le responsable
    public Loan valid(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());
        if (loan.getStatus() != StatusEnum.VALIDATION) {
            throw new IllegalStateException();
        }

        statusHistoryRepository.save(new StatusHistory(loan, StatusEnum.VALIDATION, StatusEnum.ACCEPTATION,
                this.getCurrentDate("yyyy-MM-dd")));

        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.ACCEPTATION, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), loan.getProposalAdvisor(),
                loan.getCustomer(), loan.getCreditDeadline());
    }

    // Permet de rejeter une demande de crédit par le responsable
    @Override
    public Loan reject(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFound());

        if (loan.getStatus() != StatusEnum.VALIDATION) {
            throw new IllegalStateException();
        }

        statusHistoryRepository.save(
                new StatusHistory(loan, StatusEnum.VALIDATION, StatusEnum.REJET, this.getCurrentDate("yyyy-MM-dd")));

        return new Loan(loan.getId(), loan.getLoanAmount(),
                loan.getLoanDuration(), loan.getRevenue3dernierreAnnee(),
                StatusEnum.REJET, this.getCurrentDate("yyyy-MM-dd"),
                loan.getRequestDate(), loan.getLoanType(), loan.getProposalAdvisor(),
                loan.getCustomer(), loan.getCreditDeadline());
    }

    // Permet de créer une échéance de crédit
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

    // Permet d'ajouter un nombre de mois à une date
    private LocalDate addMonthToDate(LocalDate date, int month) {
        return date.plusMonths(month);
    }

    // Permet de récupérer le montant des mensualités
    private double getMensualité(double montant, int duree, double taux) {
        double tauxInteretPeriodique = taux / 12 / 100; 
        int dureeMois = duree * 12; 
        double mensualite = montant * (tauxInteretPeriodique * Math.pow(1 + tauxInteretPeriodique, dureeMois))
                           / (Math.pow(1 + tauxInteretPeriodique, dureeMois) - 1);
        
        return mensualite;
    }

    // Permet de récupérer la date actuelle en fonction d'un pattern
    private LocalDate getCurrentDate(String pattern) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = currentDate.format(formatter);
        currentDate = LocalDate.parse(formattedDate, formatter);
        return currentDate;
    }

    
    @Override
    public List<StatusHistory> getStatusHistory(long idLoan) {
        List<StatusHistory> statusHistory = statusHistoryRepository.findByLoan(loanRepository.findById(idLoan).get())
                .stream().toList();
        return statusHistory;
    }

}
