package fr.miage.bank.infrastructure.rest.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.domain.entity.EcheanceCredit;
import fr.miage.bank.infrastructure.rest.controllers.CreditRequestController;
import fr.miage.bank.infrastructure.rest.controllers.CustomerController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EcheanceCreditModelAssembler
        implements RepresentationModelAssembler<EcheanceCredit, EntityModel<EcheanceCredit>> {
    @Override
    public EntityModel<EcheanceCredit> toModel(EcheanceCredit echeanceCredit) {
        var model = EntityModel.of(echeanceCredit);

        model.add(linkTo(methodOn(CreditRequestController.class)
                .getAllCreditRequestPending()).withRel("creditsPending").withType("GET"));

        return model;
    }
}
