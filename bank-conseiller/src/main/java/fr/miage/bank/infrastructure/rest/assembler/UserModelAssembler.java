package fr.miage.bank.infrastructure.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import fr.miage.bank.domain.entity.Customer;
import fr.miage.bank.infrastructure.rest.controllers.CustomerController;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

        @SuppressWarnings("null")
        @Override
        public EntityModel<Customer> toModel(Customer customer) {
                var model = EntityModel.of(customer);
                model.add(linkTo(methodOn(CustomerController.class)
                                .modify(customer.getCustomer_id(), customer)).withSelfRel().withType("PUT"));
                model.add(linkTo(methodOn(CustomerController.class)
                                .get(customer.getCustomer_id())).withSelfRel().withType("GET"));

                model.add(linkTo(methodOn(CustomerController.class)
                                .create(customer)).withRel("users").withType("GET"));

                model.add(linkTo(methodOn(CustomerController.class)
                                .getLoansPending(customer.getCustomer_id())).withSelfRel().withRel("credits")
                                .withType("GET"));
                return model;
        }

        @SuppressWarnings("null")
        @Override
        public CollectionModel<EntityModel<Customer>> toCollectionModel(
                        Iterable<? extends Customer> entities) {
                var creditModel = StreamSupport
                                .stream(entities.spliterator(), false)
                                .map(this::toModel)
                                .toList();
                return CollectionModel.of(creditModel,
                                linkTo(methodOn(CustomerController.class).getAll())
                                                .withRel("users").withType("GET"));
        }

}
