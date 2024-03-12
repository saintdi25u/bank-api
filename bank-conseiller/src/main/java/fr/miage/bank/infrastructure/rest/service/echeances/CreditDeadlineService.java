package fr.miage.bank.infrastructure.rest.service.echeances;

import fr.miage.bank.domain.entity.CreditDeadline;

public interface CreditDeadlineService {

    public CreditDeadline getByCreditRequest(CreditDeadline echeanceCredit);
}
