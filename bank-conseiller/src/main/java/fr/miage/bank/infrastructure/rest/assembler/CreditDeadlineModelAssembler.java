package fr.miage.bank.infrastructure.rest.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.rest.controllers.LoanController;
import fr.miage.bank.infrastructure.rest.controllers.CustomerController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CreditDeadlineModelAssembler
        implements RepresentationModelAssembler<CreditDeadline, EntityModel<CreditDeadline>> {
    @Override
    public EntityModel<CreditDeadline> toModel(CreditDeadline creditDeadline) {
        var model = EntityModel.of(creditDeadline);

        model.add(linkTo(methodOn(LoanController.class)
                .getAllLoansPending()).withRel("creditsPending").withType("GET"));

        return model;
    }
}
