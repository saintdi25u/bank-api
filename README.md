# Use case de l'application en fonction de l'état.

## DEBUT

- l'Utilisateur peut modifier sa demande tant qu'il ne l'a pas envoyé --> oui
- L'utilisateur peut envoyer sa demande lorsqu'il a fini de la compléter --> oui
- L'utilisateur peut récupérer l'état de sa demande --> COMMENT l'IMPLEMENTER DANS UN FORMAT HATEOAS


## ETUDE

- Le conseiller peut voir toutes les demandes, meme ceux cloturé --> TOUS
- Le conseiller peut voir toutes les demandes en cours ---> TOUS 
- Le conseiller peut voir une demande en particulier par son id ---> TOUS
- Le conseiller peut valider une demande de crédit --> oui
- Le conseiller peut rejeter une demande de crédit --> oui
- Le conseiller peut demander au service des finances publiques la validation des revenues de l'utilisateur --> oui

## VALIDATION

- Le responsable peut valider la décision du conseiller
- Le reponsable peut refuser la décision du conseiller

