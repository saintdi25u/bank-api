package fr.miage.bank.infrastructure.rest.shared;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class CreditRates {

    public static final HashMap<CreditType, Double> rates = new HashMap<CreditType, Double>() {
        {
            put(CreditType.CONSOMMATION, 6.00);
            put(CreditType.IMMOBILIER, 3.00);
            put(CreditType.AUTOMOBILE, 5.00);
            put(CreditType.PERSONNEL, 7.00);
            put(CreditType.TRAVAUX, 5.00);
            put(CreditType.PRO, 8.00);
        }
    };
}
