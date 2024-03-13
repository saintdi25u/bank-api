package fr.miage.bank.infrastructure.rest.controllers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import fr.miage.bank.domain.entity.Loan;
import fr.miage.bank.domain.entity.StatusHistory;
import fr.miage.bank.domain.entity.CreditDeadline;
import fr.miage.bank.infrastructure.repository.LoanRepository;
import fr.miage.bank.infrastructure.rest.assembler.LoanModelAssembler;
import fr.miage.bank.infrastructure.rest.assembler.StatusHistoryModelAssembler;
import fr.miage.bank.infrastructure.rest.service.exception.ResourceNotFound;
import fr.miage.bank.infrastructure.rest.service.loans.LoanServiceImpl;
import fr.miage.bank.infrastructure.rest.assembler.CreditDeadlineModelAssembler;
import fr.miage.bank.infrastructure.rest.shared.StatusEnum;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static java.util.stream.Collectors.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/credits")
public class LoanController {


    private LoanRepository loanRepository;
    private LoanModelAssembler loanModelAssembler;
    private LoanServiceImpl loanServiceImpl;;
    private CreditDeadlineModelAssembler echeanceCreditModelAssembler;
    private StatusHistoryModelAssembler statusHistoryModelAssembler;
    RestTemplate template;
    LoadBalancerClientFactory clientFactory;


    public LoanController(RestTemplate template, LoadBalancerClientFactory clientFactory,
            LoanRepository creditRequestRepository,
            LoanServiceImpl creditRequestService, LoanModelAssembler creditRequestModelAssembler,
            CreditDeadlineModelAssembler echeanceCreditModelAssembler, 
            StatusHistoryModelAssembler statusHistoryModelAssembler) {
        this.template = template;
        this.clientFactory = clientFactory;
        this.loanRepository = creditRequestRepository;
        this.loanServiceImpl = creditRequestService;
        this.loanModelAssembler = creditRequestModelAssembler;
        this.echeanceCreditModelAssembler = echeanceCreditModelAssembler;
        this.statusHistoryModelAssembler = statusHistoryModelAssembler;
    }

    // Permet de récupérér toutes les demandes de crédit
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Loan>>> getAll() {
        return ResponseEntity.ok(loanModelAssembler.toCollectionModel(loanRepository.findAll()));
    }

    // Permet de récupérer toutes les demandes de crédit en cours
    @GetMapping("/pending")
    public ResponseEntity<CollectionModel<EntityModel<Loan>>> getAllLoansPending() {
        List<Loan> loans = loanRepository
                .findByStatusNotIn(Arrays.asList(StatusEnum.ACCEPTATION, StatusEnum.REJET)).stream()
                .collect(toList());
        return ResponseEntity.ok(loanModelAssembler.toCollectionModel(loans));
    }

    // Permet de créer une demande de crédit
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Loan loan) {
        try {
            Loan newLoan = loanServiceImpl.create(loan);
            return ResponseEntity.created(linkTo(getClass()).slash(newLoan.getId()).toUri())
                .body(loanModelAssembler.toModel(newLoan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("Informations essentielles manquantes");
        }
  

        
    }

    // Permet de modifier une demande de crédit
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Loan>> modify(@PathVariable("id") long loanId,
            @RequestBody Loan creditRequest) {
        Loan loan2 = loanRepository.findById(loanId).orElseThrow();
        if (loan2.getStatus() != StatusEnum.DEBUT) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(loanModelAssembler
                .toModel(loanServiceImpl.modify(loanId, creditRequest)));
    }

    // Permet d'envoyer une demande de crédit pour étude
    @PostMapping("/{id}")//TODO : Test
    @Transactional
    public ResponseEntity<EntityModel<Loan>> send(@PathVariable("id") long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        if (loan.getStatus() != StatusEnum.DEBUT) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity
                .ok(loanModelAssembler.toModel(loanServiceImpl.send(loanId)));
    }

    // Permet de récupérer les crédits d'un état
   // @GetMapping("/{id}/status")
    //public ResponseEntity<Enum<StatusEnum>> getStatusCreditRequest(@PathVariable("id") long creditRequestId) {
      //  return ResponseEntity.ok(creditRequestService.getStatusCreditRequest(creditRequestId));
    //}

    // permet de récupérer une demande de crédit par son id
    @GetMapping("/{id}")
    public EntityModel<Loan> getById(@PathVariable(value = "id") Long loanId) {
        Loan loan =   loanRepository.findById(loanId).orElseThrow();
        return loanModelAssembler.toModel(loan);
    }
    
    @GetMapping("/{id}/details")
    public ResponseEntity<CollectionModel<EntityModel<StatusHistory>>>  getByIdDetails(@PathVariable(value = "id") Long loanId) {
        System.out.println(loanServiceImpl.getStatusHistory(loanId));
        return ResponseEntity.ok(statusHistoryModelAssembler.toCollectionModel(loanServiceImpl.getStatusHistory(loanId)));
    }
    

    // Permet au conseiller de spécifier qu'il accepte le dossier
    @PostMapping("{id}/accept")
    @Transactional
    public EntityModel<Loan> accept(
            @PathVariable(value = "id") long loanId) {
        var loan = loanRepository
                .save(loanServiceImpl.accept(loanId));
        return loanModelAssembler.toModel(loan);
    }

    // Permet au conseiller de spécifier qu'il refuse le dossier
    @PostMapping("/{id}/refuse")
    @Transactional
    public EntityModel<Loan> refuse(@PathVariable(value = "id") long loanId) {
        var loan = loanRepository
                .save(loanServiceImpl.refuse(loanId));
        return loanModelAssembler.toModel(loan);
    }

    // Permet au responsable de valider la demande de crédit
    @PostMapping("/{id}/valid")
    @Transactional
    public ResponseEntity<EntityModel<CreditDeadline>> validation(
            @PathVariable(value = "id") long loanId) {
        var loan = loanRepository
                .save(loanServiceImpl.valid(loanId));
        CreditDeadline creditDeadline = loanServiceImpl.createNewEcheanceCredit(loan);
        return ResponseEntity.created(linkTo(getClass()).slash(creditDeadline.getId()).toUri())
                .body(echeanceCreditModelAssembler.toModel(creditDeadline));
    }

    // Permet au responsable de rejeter la demande de crédit
    @PostMapping("/{id}/reject")
    @Transactional
    public ResponseEntity<EntityModel<Loan>> reject(
            @PathVariable(value = "id") long loanId) {
        var loan = loanRepository
                .save(loanServiceImpl.reject(loanId));
        return ResponseEntity.created(linkTo(getClass()).slash(loan.getId()).toUri())
                .body(EntityModel.of(loan));
    }

    // Permet de demander aux services finance l'étude des revenues
    @GetMapping("/{id}/validate")
    @CircuitBreaker(name = "conseillerservice", fallbackMethod = "fallbackValidationCall")
    @Retry(name = "conseillerservice")
    public EntityModel<Loan> getValidationLoanByFinanceService(
            @PathVariable(value = "id") long loanId) {
        Loan loanFind = loanRepository.findById(loanId).orElseThrow();
        RoundRobinLoadBalancer lb = clientFactory.getInstance("finance-service", RoundRobinLoadBalancer.class);
        ServiceInstance instance = lb.choose().block().getServer();
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/{id}/validate?amount=" +loanFind.getRevenue3dernierreAnnee();
        String response = template.getForObject(url, String.class, loanId);
        System.out.println(response);
        switch (response) {
            case "ok":
                loanFind.setStatus(StatusEnum.VALIDATION);
                loanRepository.save(loanFind);
                break;
            case "ko":
                loanFind.setStatus(StatusEnum.REJET);
                loanRepository.save(loanFind);
                break;
            default:
                break;
        }
        loanFind.setLastModified(this.getCurrentDate("yyyy-MM-dd"));
        return loanModelAssembler.toModel(loanFind);
    }

    // Permet d'executer cette fonction si le service finance est injoignable
    public EntityModel<Loan> fallbackValidationCall(long loanId, Throwable t) {
        System.out.println("Fallback method called");
        Loan creditRequest = loanRepository.findById(loanId).orElseThrow();
        return loanModelAssembler.toModel(creditRequest);
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
