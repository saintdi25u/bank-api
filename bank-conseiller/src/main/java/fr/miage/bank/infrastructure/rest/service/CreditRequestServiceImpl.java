package fr.miage.bank.infrastructure.rest.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netflix.discovery.converters.Auto;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.domain.entity.EcheanceCredit;
import fr.miage.bank.infrastructure.repository.CreditRequestRepository;
import fr.miage.bank.infrastructure.repository.CustomerRepository;
import fr.miage.bank.infrastructure.repository.EcheanceCreditRepository;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import fr.miage.bank.infrastructure.rest.shared.CreditRates;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;

@Service
public class CreditRequestServiceImpl implements CreditRequestService {

    @Autowired
    CreditRequestRepository creditRequestRepository;

    @Autowired
    EcheanceCreditRepository echeanceCreditRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CreditRequest createCreditRequest(CreditRequest creditRequest) {
        Customer customer = customerRepository.findById(creditRequest.getCustomer().getCustomer_id())
                .orElseThrow(() -> new ResourceNotFound());
        if (customer.getAdresse() == null || customer.getEmploie() == null) {
            return null;
        }
        LocalDate currentDate = this.getCurrentDate("yyyy-MM-dd");
        creditRequest.setDateDemande(currentDate);
        creditRequest.setLastModified(currentDate);
        creditRequest.setStatus(StatusEnum.DEBUT);
        creditRequest.setPropositionConseiller(StatusEnum.DEBUT);
        creditRequest.setCustomer(customer);
        return creditRequestRepository.save(creditRequest);

    }

    public CreditRequest modifyCreditRequest(long idCreditRequest, CreditRequest creditRequest) {
        CreditRequest creditRequestToModify = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());
        if (creditRequestToModify.getStatus() != StatusEnum.DEBUT) {
            throw new IllegalStateException();
        }
        creditRequestToModify.setCreditType(creditRequest.getCreditType());
        creditRequestToModify.setDureeCredit(creditRequest.getDureeCredit());
        creditRequestToModify.setMontantCredit(creditRequest.getMontantCredit());
        creditRequestToModify.setRevenue3dernierreAnnee(creditRequest.getRevenue3dernierreAnnee());
        creditRequestToModify.setLastModified(this.getCurrentDate("yyyy-MM-dd"));
        return creditRequestRepository.save(creditRequestToModify);
    }

    public Enum<StatusEnum> getStatusCreditRequest(long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());
        return creditRequest.getStatus();
    }

    public CreditRequest sendCreditRequest(long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());
        System.out.println(creditRequest.getStatus());
        if (creditRequest.getStatus() != StatusEnum.DEBUT) {
            return creditRequest;
        } else {
            creditRequest.setStatus(StatusEnum.ETUDE);
            creditRequest.setPropositionConseiller(StatusEnum.ETUDE);
            return creditRequestRepository.save(creditRequest);
        }
    }

    public CreditRequest validationCreditRequestByConseiller(Long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());

        if (creditRequest.getStatus() != StatusEnum.ETUDE) {
            throw new IllegalStateException();
        }
        return new CreditRequest(creditRequest.getId(), creditRequest.getMontantCredit(),
                creditRequest.getDureeCredit(), creditRequest.getRevenue3dernierreAnnee(),
                StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd"),
                creditRequest.getDateDemande(), creditRequest.getCreditType(), StatusEnum.ACCEPTATION,
                creditRequest.getCustomer());
    }

    public CreditRequest refusCreditRequestByConseiller(Long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());

        if (creditRequest.getStatus() != StatusEnum.ETUDE) {
            throw new IllegalStateException();
        }
        return new CreditRequest(creditRequest.getId(), creditRequest.getMontantCredit(),
                creditRequest.getDureeCredit(), creditRequest.getRevenue3dernierreAnnee(),
                StatusEnum.VALIDATION, this.getCurrentDate("yyyy-MM-dd"),
                creditRequest.getDateDemande(), creditRequest.getCreditType(), StatusEnum.REJET,
                creditRequest.getCustomer());
    }

    public CreditRequest validationCreditRequestByResponsable(Long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());
        if (creditRequest.getStatus() != StatusEnum.VALIDATION) {
            throw new IllegalStateException();
        }

        return new CreditRequest(creditRequest.getId(), creditRequest.getMontantCredit(),
                creditRequest.getDureeCredit(), creditRequest.getRevenue3dernierreAnnee(),
                StatusEnum.ACCEPTATION, this.getCurrentDate("yyyy-MM-dd"),
                creditRequest.getDateDemande(), creditRequest.getCreditType(), creditRequest.getPropositionConseiller(),
                creditRequest.getCustomer());
    }

    @Override
    public CreditRequest refusCreditRequestByResponsable(Long idCreditRequest) {
        CreditRequest creditRequest = creditRequestRepository.findById(idCreditRequest)
                .orElseThrow(() -> new ResourceNotFound());

        if (creditRequest.getStatus() != StatusEnum.VALIDATION) {
            throw new IllegalStateException();
        }
        return new CreditRequest(creditRequest.getId(), creditRequest.getMontantCredit(),
                creditRequest.getDureeCredit(), creditRequest.getRevenue3dernierreAnnee(),
                StatusEnum.REJET, this.getCurrentDate("yyyy-MM-dd"),
                creditRequest.getDateDemande(), creditRequest.getCreditType(), creditRequest.getPropositionConseiller(),
                creditRequest.getCustomer());
    }

    public EcheanceCredit createNewEcheanceCredit(CreditRequest creditRequest) {
        EcheanceCredit echeanceCredit = new EcheanceCredit();
        echeanceCredit.setDateDebutCredit(this.getCurrentDate("yyyy-MM-dd"));
        echeanceCredit.setDateFinCredit(this.addMonthToDate(this.getCurrentDate("yyyy-MM-dd"),
                creditRequest.getDureeCredit()));
        echeanceCredit.setMontantCapital(creditRequest.getMontantCredit());
        echeanceCredit.setTauxCredit(CreditRates.rates.get(creditRequest.getCreditType()));
        echeanceCredit.setMontantEcheance(this.getMensualité(creditRequest.getMontantCredit(),
                creditRequest.getDureeCredit(), echeanceCredit.getTauxCredit()));
        echeanceCredit.setCreditRequest(creditRequest);
        return echeanceCreditRepository.save(echeanceCredit);
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

}
