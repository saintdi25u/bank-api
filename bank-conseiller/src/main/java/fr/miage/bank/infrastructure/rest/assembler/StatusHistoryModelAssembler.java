package fr.miage.bank.infrastructure.rest.assembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.domain.entity.StatusHistory;
import fr.miage.bank.infrastructure.rest.controllers.CustomerController;
import fr.miage.bank.infrastructure.rest.controllers.LoanController;


@Component
public class StatusHistoryModelAssembler
                implements RepresentationModelAssembler<StatusHistory, EntityModel<StatusHistory>> {

        @Override
        public EntityModel<StatusHistory> toModel(StatusHistory statusHistory) {
                var model = EntityModel.of(statusHistory);
                model.add(linkTo(methodOn(LoanController.class).getById(statusHistory.getLoan().getId()))
                                .withSelfRel().withType("GET"));
                return model;
        }

         @Override
        public CollectionModel<EntityModel<StatusHistory>> toCollectionModel(
                        Iterable<? extends StatusHistory> entities) {
                var creditModel = StreamSupport
                                .stream(entities.spliterator(), false)
                                .map(this::toModel)
                                .toList();
                return CollectionModel.of(creditModel,
                                linkTo(methodOn(CustomerController.class).getAll())
                                                .withRel("users").withType("GET"));
        }
        



}