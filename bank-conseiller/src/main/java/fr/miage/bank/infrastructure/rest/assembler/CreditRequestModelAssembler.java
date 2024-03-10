package fr.miage.bank.infrastructure.rest.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.*;
import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.infrastructure.rest.controllers.CreditRequestController;
import fr.miage.bank.infrastructure.rest.controllers.CustomerController;
import fr.miage.bank.infrastructure.rest.controllers.EcheanceController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.StreamSupport;

@Component
public class CreditRequestModelAssembler
                implements RepresentationModelAssembler<CreditRequest, EntityModel<CreditRequest>> {

        @Override
        public EntityModel<CreditRequest> toModel(CreditRequest creditRequest) {
                var model = EntityModel.of(creditRequest);
                switch (creditRequest.getStatus()) {
                        case DEBUT:
                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .modifyCreditRequest(creditRequest.getId(), creditRequest))
                                                .withSelfRel().withType("PUT"));
                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .sendCreditRequest(creditRequest.getId()))
                                                .withSelfRel().withType("PUT"));

                                break;
                        case ETUDE:
                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .validationCreditRequestByConseiller(
                                                                creditRequest.getId()))
                                                .withSelfRel().withType("POST"));
                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .rejetCreditRequestByConseiller(
                                                                creditRequest.getId()))
                                                .withSelfRel().withType("POST"));
                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .getValidationCreditRequestByFinanceService(
                                                                creditRequest.getId()))
                                                .withSelfRel().withType("GET"));
                                break;

                        case VALIDATION:

                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .validationCreditRequestByResponsable(
                                                                creditRequest.getId()))
                                                .withSelfRel().withType("POST"));
                                model.add(linkTo(methodOn(CreditRequestController.class)
                                                .refusCreditRequestByResponsable(
                                                                creditRequest.getId()))
                                                .withSelfRel().withType("POST"));
                                break;
                        case ACCEPTATION:
                                model.add(linkTo(methodOn(EcheanceController.class)
                                                .getEcheance(
                                                                creditRequest.getId()))
                                                .withSelfRel().withType("GET"));
                                break;
                        case REJET:
                                break;
                }

                model.add(linkTo(methodOn(CreditRequestController.class).getStatusCreditRequest(creditRequest.getId()))
                                .withSelfRel().withType("GET"));
                model.add(linkTo(methodOn(CreditRequestController.class).getAllCreditRequest())
                                .withRel("list").withType("GET"));
                model.add(linkTo(methodOn(CreditRequestController.class).getAllCreditRequestPending())
                                .withRel("list").withType("GET"));

                model.add(linkTo(methodOn(CustomerController.class).get(creditRequest.getCustomer().getCustomer_id()))
                                .withRel("customer").withType("GET"));

                // model.add(linkTo(methodOn(CustomerController.class).getAll())
                // .withRel("customers").withType("GET")); // A LAISSER OU PAS ?

                return model;

        }

        @Override
        public CollectionModel<EntityModel<CreditRequest>> toCollectionModel(
                        Iterable<? extends CreditRequest> entities) {
                var creditModel = StreamSupport
                                .stream(entities.spliterator(), false)
                                .map(this::toModel)
                                .toList();
                return CollectionModel.of(creditModel,
                                linkTo(methodOn(CreditRequestController.class).getAllCreditRequestPending())
                                                .withSelfRel().withType("GET"));
        }
}
