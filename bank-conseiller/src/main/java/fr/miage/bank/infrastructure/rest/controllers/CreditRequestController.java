package fr.miage.bank.infrastructure.rest.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ecwid.consul.v1.Response;
import com.netflix.discovery.converters.Auto;
import com.thoughtworks.xstream.io.path.Path;

import fr.miage.bank.domain.entity.CreditRequest;
import fr.miage.bank.domain.entity.EcheanceCredit;
import fr.miage.bank.infrastructure.repository.CreditRequestRepository;
import fr.miage.bank.infrastructure.repository.EcheanceCreditRepository;
import fr.miage.bank.infrastructure.rest.assembler.CreditRequestModelAssembler;
import fr.miage.bank.infrastructure.rest.assembler.EcheanceCreditModelAssembler;
import fr.miage.bank.infrastructure.rest.service.CreditRequestServiceImpl;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.sleuth.TraceKeys.Http;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.text.html.parser.Entity;

import static java.util.stream.Collectors.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/credits")
public class CreditRequestController {

    private CreditRequestRepository creditRequestRepository;
    private CreditRequestModelAssembler creditRequestModelAssembler;
    private CreditRequestServiceImpl creditRequestService;;
    private EcheanceCreditModelAssembler echeanceCreditModelAssembler;
    RestTemplate template;
    LoadBalancerClientFactory clientFactory;

    public CreditRequestController(RestTemplate template, LoadBalancerClientFactory clientFactory,
            CreditRequestRepository creditRequestRepository,
            CreditRequestServiceImpl creditRequestService, CreditRequestModelAssembler creditRequestModelAssembler,
            EcheanceCreditModelAssembler echeanceCreditModelAssembler) {
        this.template = template;
        this.clientFactory = clientFactory;
        this.creditRequestRepository = creditRequestRepository;
        this.creditRequestService = creditRequestService;
        this.creditRequestModelAssembler = creditRequestModelAssembler;
        this.echeanceCreditModelAssembler = echeanceCreditModelAssembler;
    }

    // Permet de récupérér toutes les demandes de crédit
    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<CreditRequest>>> getAllCreditRequest() {
        return ResponseEntity.ok(creditRequestModelAssembler.toCollectionModel(creditRequestRepository.findAll()));
    }

    // Permet de récupérer toutes les demandes de crédit en cours
    @GetMapping("pending")
    public ResponseEntity<CollectionModel<EntityModel<CreditRequest>>> getAllCreditRequestPending() {
        List<CreditRequest> creditRequests = creditRequestRepository
                .findByStatusNotIn(Arrays.asList(StatusEnum.ACCEPTATION, StatusEnum.REJET)).stream()
                .collect(toList());
        return ResponseEntity.ok(creditRequestModelAssembler.toCollectionModel(creditRequests));
    }

    // Permet de créer une demande de crédit
    @PostMapping()
    public ResponseEntity<?> createCreditRequest(@RequestBody CreditRequest creditRequest) {
        CreditRequest newCreditRequest = creditRequestService.createCreditRequest(creditRequest);
        return ResponseEntity.created(linkTo(getClass()).slash(newCreditRequest.getId()).toUri())
                .body(creditRequestModelAssembler.toModel(newCreditRequest));
    }

    // Permet de modifier une demande de crédit
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CreditRequest>> modifyCreditRequest(@PathVariable("id") long creditRequestId,
            @RequestBody CreditRequest creditRequest) {
        CreditRequest creditRequest2 = creditRequestRepository.findById(creditRequestId).orElseThrow();
        if (creditRequest2.getStatus() != StatusEnum.DEBUT) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(creditRequestModelAssembler
                .toModel(creditRequestService.modifyCreditRequest(creditRequestId, creditRequest)));
    }

    // Permet d'envoyer une demande de crédit pour étude
    @PutMapping("/{id}/send")
    @Transactional
    public ResponseEntity<EntityModel<CreditRequest>> sendCreditRequest(@PathVariable("id") long creditRequestId) {
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        if (creditRequest.getStatus() != StatusEnum.DEBUT) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity
                .ok(creditRequestModelAssembler.toModel(creditRequestService.sendCreditRequest(creditRequestId)));
    }

    // Permet de récupérer le status d'une demande de crédit
    @GetMapping("/{id}/status")
    public ResponseEntity<Enum<StatusEnum>> getStatusCreditRequest(@PathVariable("id") long creditRequestId) {
        return ResponseEntity.ok(creditRequestService.getStatusCreditRequest(creditRequestId));
    }

    // permet de récupérer une demande de crédit par son id
    @GetMapping("/{id}")
    public EntityModel<CreditRequest> getCreditRequestById(@PathVariable(value = "id") Long creditRequestId) {
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        return creditRequestModelAssembler.toModel(creditRequest);
    }

    // Permet au conseiller de spécifier qu'il accepte le dossier
    @PostMapping("conseiller/{id}/acceptation")
    @Transactional
    public EntityModel<CreditRequest> validationCreditRequestByConseiller(
            @PathVariable(value = "id") long creditRequestId) {
        var creditRequest = creditRequestRepository
                .save(creditRequestService.validationCreditRequestByConseiller(creditRequestId));
        return creditRequestModelAssembler.toModel(creditRequest);
    }

    // Permet au conseiller de spécifier qu'il refuse le dossier
    @PostMapping("/conseiller/{id}/rejet")
    @Transactional
    public EntityModel<CreditRequest> rejetCreditRequestByConseiller(@PathVariable(value = "id") long creditRequestId) {
        var creditRequest = creditRequestRepository
                .save(creditRequestService.refusCreditRequestByConseiller(creditRequestId));
        return creditRequestModelAssembler.toModel(creditRequest);
    }

    // Permet au responsable de valider la demande de crédit
    @PostMapping("/responsable/{id}/acceptation")
    @Transactional
    public ResponseEntity<EntityModel<EcheanceCredit>> validationCreditRequestByResponsable(
            @PathVariable(value = "id") long creditRequestId) {
        var creditRequest = creditRequestRepository
                .save(creditRequestService.validationCreditRequestByResponsable(creditRequestId));
        EcheanceCredit echeanceCredit = creditRequestService.createNewEcheanceCredit(creditRequest);
        return ResponseEntity.created(linkTo(getClass()).slash(echeanceCredit.getId()).toUri())
                .body(echeanceCreditModelAssembler.toModel(echeanceCredit));
    }

    // Permet au responsable de rejeter la demande de crédit
    @PostMapping("/responsable/{id}/rejet")
    @Transactional
    public ResponseEntity<EntityModel<CreditRequest>> refusCreditRequestByResponsable(
            @PathVariable(value = "id") long creditRequestId) {
        var creditRequest = creditRequestRepository
                .save(creditRequestService.validationCreditRequestByResponsable(creditRequestId));
        return ResponseEntity.created(linkTo(getClass()).slash(creditRequest.getId()).toUri())
                .body(EntityModel.of(creditRequest));
    }

    // Permet de demander aux services finance l'étude des revenues
    @GetMapping("/conseiller/{id}/validation/revenus")
    @CircuitBreaker(name = "conseillerservice", fallbackMethod = "fallbackValidationCall")
    @Retry(name = "conseillerservice")
    public EntityModel<CreditRequest> getValidationCreditRequestByFinanceService(
            @PathVariable(value = "id") long creditRequestId) {
        CreditRequest creditRequestFind = creditRequestRepository.findById(creditRequestId).orElseThrow();
        RoundRobinLoadBalancer lb = clientFactory.getInstance("finance-service", RoundRobinLoadBalancer.class);
        ServiceInstance instance = lb.choose().block().getServer();
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/finance/{id}/validation/revenus";
        String response = template.getForObject(url, String.class, creditRequestId);
        System.out.println(response);
        switch (response) {
            case "ok":
                creditRequestFind.setStatus(StatusEnum.VALIDATION);
                creditRequestRepository.save(creditRequestFind);
                break;
            case "ko":
                creditRequestFind.setStatus(StatusEnum.REJET);
                creditRequestRepository.save(creditRequestFind);
                break;
            default:
                break;
        }
        creditRequestFind.setLastModified(this.getCurrentDate("yyyy-MM-dd"));
        return creditRequestModelAssembler.toModel(creditRequestFind);
    }

    // Permet d'executer cette fonction si le service finance est injoignable
    public EntityModel<CreditRequest> fallbackValidationCall(long creditRequestId, Throwable t) {
        System.out.println("Fallback method called");
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        return creditRequestModelAssembler.toModel(creditRequest);
    }

    // Fonction utilitaire permettant de récupérer la date courante selon le format
    // souhaitter
    private LocalDate getCurrentDate(String pattern) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = currentDate.format(formatter);
        currentDate = LocalDate.parse(formattedDate, formatter);
        return currentDate;
    }

}
