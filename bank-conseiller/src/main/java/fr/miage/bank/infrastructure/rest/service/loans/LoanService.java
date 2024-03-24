package fr.miage.bank.infrastructure.rest.service.loans;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.StatusHistory;

import java.util.List;

import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

public interface LoanService {
    public Loan modify(long idLoan, Loan loan);

    public Loan create(Loan loan);

    
   public List<StatusHistory> getStatusHistory(long idLoan);

    public Loan send(long idLoan);

    public Loan accept(Long idLoan);

    public Loan reject (Long idLoan);

    public Loan valid(Long idLoan);

    public Loan refuse(Long idLoan);

    public CreditDeadline createNewEcheanceCredit(Loan loan);
}
