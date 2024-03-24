package fr.miage.bank.infrastructure.rest.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.*;
import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.infrastructure.rest.controllers.LoanController;
import fr.miage.bank.infrastructure.rest.controllers.CustomerController;
import fr.miage.bank.infrastructure.rest.controllers.CreditDeadlineController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.StreamSupport;

@Component
public class LoanModelAssembler
                implements RepresentationModelAssembler<Loan, EntityModel<Loan>> {

        @SuppressWarnings("null")
        @Override
        public EntityModel<Loan> toModel(Loan loan) {
                var model = EntityModel.of(loan);
                switch (loan.getStatus()) {
                        case DEBUT:
                                model.add(linkTo(methodOn(LoanController.class)
                                                .modify(loan.getId(), loan))
                                                .withSelfRel().withType("PUT"));
                                model.add(linkTo(methodOn(LoanController.class)
                                                .send(loan.getId()))
                                                .withSelfRel().withType("POST"));

                                break;
                        case ETUDE:
                                model.add(linkTo(methodOn(LoanController.class)
                                                .accept(
                                                        loan.getId()))
                                                .withSelfRel().withType("POST"));
                                model.add(linkTo(methodOn(LoanController.class)
                                                .refuse(
                                                        loan.getId()))
                                                .withSelfRel().withType("POST"));
                                model.add(linkTo(methodOn(LoanController.class)
                                                .getValidationLoanByFinanceService(
                                                        loan.getId()))
                                                .withSelfRel().withType("GET"));
                                break;

                        case VALIDATION:

                                model.add(linkTo(methodOn(LoanController.class)
                                                .validation(
                                                        loan.getId()))
                                                .withSelfRel().withType("POST"));
                                model.add(linkTo(methodOn(LoanController.class)
                                                .reject(
                                                        loan.getId()))
                                                .withSelfRel().withType("POST"));
                                break;
                        case ACCEPTATION:
                                model.add(linkTo(methodOn(CreditDeadlineController.class)
                                                .getCreditDeadline(
                                                        loan.getId()))
                                                .withSelfRel().withType("GET"));
                                break;
                        case REJET:
                                break;
                }

               // model.add(linkTo(methodOn(CreditRequestController.class).getStatusCreditRequest(creditRequest.getId()))
                 //               .withSelfRel().withType("GET"));
                model.add(linkTo(methodOn(LoanController.class).getAll())
                                .withRel("list").withType("GET"));
                model.add(linkTo(methodOn(LoanController.class).getAllLoansPending())
                                .withRel("list").withType("GET"));

                model.add(linkTo(methodOn(CustomerController.class).get(loan.getCustomer().getCustomer_id()))
                                .withRel("customer").withType("GET"));

                model.add(linkTo(methodOn(LoanController.class).getByIdDetails(loan.getId()))
                                .withSelfRel().withType("GET"));
                
                // model.add(linkTo(methodOn(CustomerController.class).getAll())
                // .withRel("customers").withType("GET")); // A LAISSER OU PAS ?

                return model;

        }

        @SuppressWarnings("null")
        @Override
        public CollectionModel<EntityModel<Loan>> toCollectionModel(
                        Iterable<? extends Loan> entities) {
                var creditModel = StreamSupport
                                .stream(entities.spliterator(), false)
                                .map(this::toModel)
                                .toList();
                return CollectionModel.of(creditModel,
                                linkTo(methodOn(LoanController.class).getAllLoansPending())
                                                .withSelfRel().withType("GET"));
        }
}
