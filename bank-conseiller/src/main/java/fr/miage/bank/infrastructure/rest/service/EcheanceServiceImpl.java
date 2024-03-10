package fr.miage.bank.infrastructure.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.discovery.converters.Auto;

import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.domain.entity.EcheanceCredit;
import fr.miage.bank.infrastructure.repository.CreditRequestRepository;
import fr.miage.bank.infrastructure.repository.EcheanceCreditRepository;

@Service
public class EcheanceServiceImpl implements EcheanceService {

    @Autowired
    private EcheanceCreditRepository echeanceCreditRepository;

    @Autowired
    private CreditRequestRepository creditRequestRepository;

    public EcheanceServiceImpl(EcheanceCreditRepository echeanceCreditRepository,
            CreditRequestRepository creditRequestRepository) {
        this.echeanceCreditRepository = echeanceCreditRepository;
        this.creditRequestRepository = creditRequestRepository;
    }

    public EcheanceCredit getByCreditRequest(EcheanceCredit echeanceCredit) {
        return echeanceCreditRepository.findByCreditRequest(echeanceCredit.getCreditRequest());
    }

}
