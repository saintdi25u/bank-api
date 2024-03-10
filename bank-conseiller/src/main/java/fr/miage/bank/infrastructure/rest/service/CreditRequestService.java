package fr.miage.bank.infrastructure.rest.service;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.EcheanceCredit;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

public interface CreditRequestService {
    public CreditRequest modifyCreditRequest(long idCreditRequest, CreditRequest creditRequest);

    public CreditRequest createCreditRequest(CreditRequest creditRequest);

    public Enum<StatusEnum> getStatusCreditRequest(long idCreditRequest);

    public CreditRequest sendCreditRequest(long idCreditRequest);

    public CreditRequest validationCreditRequestByConseiller(Long idCreditRequest);

    public CreditRequest refusCreditRequestByConseiller(Long idCreditRequest);

    public CreditRequest validationCreditRequestByResponsable(Long idCreditRequest);

    public CreditRequest refusCreditRequestByResponsable(Long idCreditRequest);

    public EcheanceCredit createNewEcheanceCredit(CreditRequest creditRequest);
}
