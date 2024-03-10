package fr.miage.bank.infrastructure.rest.service;

import fr.miage.bank.domain.entity.EcheanceCredit;

public interface EcheanceService {

    public EcheanceCredit getByCreditRequest(EcheanceCredit echeanceCredit);
}
