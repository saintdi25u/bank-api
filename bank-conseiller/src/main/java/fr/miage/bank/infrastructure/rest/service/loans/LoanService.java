package fr.miage.bank.infrastructure.rest.service.loans;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

public interface LoanService {
    public Loan modify(long idCreditRequest, Loan creditRequest);

    public Loan create(Loan creditRequest);

   // public Enum<StatusEnum> getStatusCreditRequest(long idCreditRequest);

    public Loan send(long idCreditRequest);

    public Loan accept(Long idCreditRequest);

    public Loan reject (Long idCreditRequest);

    public Loan valid(Long idCreditRequest);

    public Loan refuse(Long idCreditRequest);

    public CreditDeadline createNewEcheanceCredit(Loan creditRequest);
}
