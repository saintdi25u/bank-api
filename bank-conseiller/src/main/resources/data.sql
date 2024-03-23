-- INSERT CUSTOMER
INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'corentinstd@gmail.com',
    'Corentin',
    'SAINT-DIZIER',
    '2001-29-07',
    'Rue de la ville, Nancy',
    'Student'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'hugok@gmail.com',
    'Hugo',
    'KIRBACH',
    '2000-01-02',
    'Rue de la grande ville, Metz',
    'DJ'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'operrin@loria.fr',
    'Olivier',
    'Perrin',
    '1980-16-11',
    'Impasse de la blockchain, Nancy',
    'Enseignant maître'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'jvirch@gmail.com',
    'John',
    'VIRICH',
    '2001-05-20',
    'Avenue de la musculation, Dieuze',
    'Coach'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'tlhuillier@gmail.com',
    'Thomas',
    'L''HUILLIER',
    '2001-07-26',
    'Rue de la rame, Nancy',
    'Rameur'
);
-- INSERT LOANS

-- INSERT CUSTOMER
INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'corentinstd@gmail.com',
    'Corentin',
    'SAINT-DIZIER',
    '2001-29-07',
    'Rue de la ville, Nancy',
    'Student'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'hugok@gmail.com',
    'Hugo',
    'KIRBACH',
    '2000-01-02',
    'Rue de la grande ville, Metz',
    'DJ'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'operrin@loria.fr',
    'Olivier',
    'Perrin',
    '1980-16-11',
    'Impasse de la blockchain, Nancy',
    'Enseignant maître'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'jvirch@gmail.com',
    'John',
    'VIRICH',
    '2001-05-20',
    'Avenue de la musculation, Dieuze',
    'Coach'
);

INSERT INTO customer (email, first_name, last_name, birth_date, adress, job)
VALUES (
    'tlhuillier@gmail.com',
    'Thomas',
    'L''HUILLIER',
    '2001-07-26',
    'Rue de la rame, Nancy',
    'Rameur'
);
-- INSERT LOANS with STATUS HISTORY

INSERT INTO loan (id,last_modified, loan_amount, loan_duration, request_date, revenue3dernierre_annee, credit_deadline_id, customer_id,loan_type, proposal_advisor, status)
VALUES (
        1,
        current_date,
        50000,
        20,
        current_date,
        39000,
        null,
        1,
        'CONSOMMATION',
        'DEBUT',
        'DEBUT'
       );
INSERT INTO status_history(modification_date, id_loan, new_status, old_status)
VALUES (
        current_date,
        1,
        'DEBUT',
        'DEBUT'
       );




INSERT INTO loan (id,last_modified, loan_amount, loan_duration, request_date, revenue3dernierre_annee, credit_deadline_id, customer_id,loan_type, proposal_advisor, status)
VALUES (
        2,
        current_date,
        130000,
        35,
        current_date,
        24000,
        null,
        2,
        'IMMOBILIER',
        'REJET',
        'VALIDATION'
       );

INSERT INTO status_history(modification_date, id_loan, new_status, old_status)
VALUES (
        to_date('2024-03-04'),
        2,
        'DEBUT',
        ''
       );
INSERT INTO status_history(modification_date, id_loan, old_status,new_status)
VALUES (
        to_date('2024-03-04'),
        2,
        'DEBUT',
        'ETUDE'
       );
INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-05'),
        2,
        'ETUDE',
        'VALIDATION'
       );

INSERT INTO loan (id,last_modified, loan_amount, loan_duration, request_date, revenue3dernierre_annee, credit_deadline_id, customer_id,loan_type, proposal_advisor, status)
VALUES (
        3,
        current_date,
        130000,
        30,
        current_date,
        42000,
        null,
        3,
        'PRO',
        'ACCEPTATION',
        'VALIDATION'
       );

INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-06'),
        3,
        '',
        'DEBUT'
       );
INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-06'),
        3,
        'DEBUT',
        'ETUDE'
       );

INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-08'),
        3,
        'ETUDE',
        'VALIDATION'
       );


INSERT INTO loan (id,last_modified, loan_amount, loan_duration, request_date, revenue3dernierre_annee, credit_deadline_id, customer_id,loan_type, proposal_advisor, status)
VALUES (
        4,
        current_date,
        50000,
        25,
        current_date,
        36000,
        null,
        4,
        'CONSOMMATION',
        'ACCEPTATION',
        'ACCEPTATION'
       );

INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-12'),
        4,
        '',
        'DEBUT'
       );


INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-13'),
        4,
        'DEBUT',
        'ETUDE'
       );

INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-13'),
        4,
        'ETUDE',
        'VALIDATION'
       );
INSERT INTO status_history(modification_date, id_loan, old_status, new_status)
VALUES (
        to_date('2024-03-15'),
        4,
        'VALIDATION',
        'ACCEPTATION'
       );

-- INSERT CREDIT DEADLINE



