package fr.miage.bank.infrastructure.rest.service.echeances;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.repository.LoanRepository;
import fr.miage.bank.infrastructure.repository.CreditDeadlineRepository;

@Service
public class CreditDeadlineServiceImpl implements CreditDeadlineService {

    @Autowired
    private CreditDeadlineRepository creditDeadlineRepository;

    @Autowired
    private LoanRepository loanRepository;

    public CreditDeadlineServiceImpl(CreditDeadlineRepository echeanceCreditRepository,
            LoanRepository creditRequestRepository) {
        this.creditDeadlineRepository = echeanceCreditRepository;
        this.loanRepository = creditRequestRepository;
    }

    public CreditDeadline getByCreditRequest(CreditDeadline echeanceCredit) {
        return creditDeadlineRepository.findByLoan(echeanceCredit.getLoan());
    }

}
