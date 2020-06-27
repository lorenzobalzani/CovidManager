USE CovidManager;

/**
  Inserimento nuovo cittadino
 */
INSERT INTO CITTADINO (CF, nome, cognome, dataDiNascita, genere, comuneResidenza, telefono)
VALUES ('BLZLNZ99B17H199T', 'Lorenzo', 'Balzani', '1999-02-17', 'Maschio', 'Ravenna',
        '3883629479');

INSERT INTO CITTADINO (CF, nome, cognome, dataDiNascita, genere, comuneResidenza, telefono)
VALUES ('RSSLNZ99C17A199T', 'Lorenzo', 'Rossi', '1999-03-17', 'Maschio', 'Ravenna',
        '3883629479');

/**
  Inserimento medico di base, sue credenziali e assegnamento di un paziente
 */
INSERT INTO CITTADINO (CF, nome, cognome, dataDiNascita, genere, comuneResidenza, telefono)
VALUES ('RSSMROZ58B18A199T', 'Mario', 'Rossi', '1958-02-18', 'Maschio', 'Ravenna',
        '3883629479');

INSERT INTO MEDICO_DI_BASE(CF)
VALUES ('RSSMROZ58B18A199T');

INSERT INTO CREDENZIALI(CF, username, hashedPassword, tipo)
VALUES ('RSSMROZ58B18A199T', 'mariorossi', 'password', 'MEDICO_DI_BASE');

UPDATE CITTADINO SET ID_MED=(SELECT ID_MED
                             FROM MEDICO_DI_BASE M
                             WHERE M.CF='RSSMROZ58B18A199T') WHERE CF='BLZLNZ99B17H199T';

UPDATE CITTADINO SET ID_MED=(SELECT ID_MED
                             FROM MEDICO_DI_BASE M
                             WHERE M.CF='RSSMROZ58B18A199T') WHERE CF='RSSLNZ99C17A199T';

/**
  Pazienti del medico di base Mario Rossi con il loro ultimo tampone
 */
SELECT C.CF, C.cognome, T.esito, T.data
FROM CITTADINO C , TAMPONE T
WHERE ID_MED = (SELECT ID_MED
                FROM MEDICO_DI_BASE M
                WHERE M.CF='RSSMROZ58B18A199T')
  AND T.CF = C.CF AND T.data = (SELECT MAX(T.data)
                                WHERE T.CF = C.CF);

/**
  Search statement
 */
SELECT DISTINCT * FROM CITTADINO C , STATO_SALUTE S
WHERE ID_MED=(SELECT ID_MED
              FROM MEDICO_DI_BASE M
              WHERE M.CF='c') AND (nome LIKE '%" + text +"%' OR cognome LIKE '%testo%'
OR tipo LIKE '%text%' OR data LIKE '%text%' OR C.CF LIKE '%text%')
AND S.CF = C.CF AND S.data = (SELECT MAX(data)s
                             FROM STATO_SALUTE S
                             WHERE S.CF = C.CF);
/**
  Reperimento referti per i propri pazienti
 */
SELECT *
FROM REFERTO R
WHERE CF = 'RSSLNZ99C17A199T';

/**
  Cambio nome utente
 */
UPDATE CREDENZIALI SET username='newUsername' WHERE CF='CF';

/**
  Inserimento ospedale
 */
INSERT INTO OSPEDALE (nomeOspedale, indirizzo, mailDirezione)
VALUES ('Santa Maria delle Croci', 'Viale Vincenzo Randi 5, Ravenna',
        'ospedaleravenna@auslromagna.it');

/**
  Inserimento piano
 */
INSERT INTO PIANO
VALUES ((SELECT idOspedale FROM OSPEDALE WHERE nomeOspedale='Santa Maria delle Croci'), 3);

/**
  Inserimento medico responsabile Luca Pellegri reparto Covid
 */
INSERT INTO CITTADINO
VALUES ('CLNFLS37C18A189T', 'Luca', 'Pellegri', '1958-05-19', 'Uomo', 'Cesena', '3338974456', null);

INSERT INTO MEDICO_RESPONSABILE(CF)
VALUES ('CLNFLS37C18A189T');

/**
  Inserimento medico responsabile Lucia Formigli reparto Covid
 */
INSERT INTO CITTADINO
VALUES ('CSNFLS27C184139T', 'Lucia', 'Formigli', '1954-06-20', 'Donna', 'Modena', '3348934353', null);

INSERT INTO MEDICO_RESPONSABILE(CF)
VALUES ('CSNFLS27C184139T');

/**
  Inserimento reparto Covid
 */
INSERT INTO REPARTO_COVID
VALUES ((SELECT idOspedale FROM OSPEDALE WHERE nomeOspedale='Santa Maria delle Croci'),
        3, 0,
        (SELECT ID_RESP FROM MEDICO_RESPONSABILE WHERE CF='CLNFLS37C18A189T'), 50, 10);

INSERT INTO REPARTO_COVID
VALUES ((SELECT idOspedale FROM OSPEDALE WHERE nomeOspedale='Santa Maria delle Croci'),
        1, 1,
        (SELECT ID_RESP FROM MEDICO_RESPONSABILE WHERE CF='CSNFLS27C184139T'), 50, 10);

/**
  Inserimento referto
 */
INSERT INTO REFERTO
VALUES ('RSSLNZ99C17A199T', 'Inizio ricovero', '2', '2020-06-30',
        (SELECT idOspedale FROM OSPEDALE WHERE nomeOspedale='Santa Maria delle Croci'),
        3, 0);

/**
  Visualizzazione referto per paziente
 */
SELECT CF, tipo, codiceGravita, data, nomeOspedale, numeroPiano, idReparto
FROM REFERTO R
JOIN OSPEDALE
WHERE CF = 'RSSLNZ99C17A199T'
ORDER BY data;

/**
  Visualizzazione dei referti di tutti i reparti Covid di un dato ospedale
 */
SELECT C.CF, C.nome, C.cognome, C.dataDiNascita, tipo, idReparto, numeroPiano, codiceGravita, data, dataFine
FROM REFERTO R, CITTADINO C
WHERE idReparto IN (SELECT idReparto
                    FROM REPARTO_COVID
                    WHERE numeroPiano IN (SELECT numeroPiano
                                          FROM PIANO
                                          WHERE idOspedale = (SELECT idOspedale
                                                              FROM OSPEDALE
                                                              WHERE nomeOspedale = 'Santa Maria delle Croci')
                    )
)
AND C.CF = R.CF
ORDER BY data, codiceGravita;

/**
  Visualizzazione dei referti di un dato reparto Covid di un dato ospedale
 */
SELECT C.CF, C.nome, C.cognome, C.dataDiNascita, tipo, codiceGravita, data
FROM REFERTO R, CITTADINO C
WHERE idReparto = '0' AND numeroPiano = '1' AND idOspedale = (SELECT idOspedale
                                                              FROM OSPEDALE
                                                              WHERE nomeOspedale = 'Santa Maria delle Croci')
AND C.CF = R.CF
ORDER BY data, codiceGravita;

/**
  Inserimento nuovo tampone:
    1.Creazione tampone associato all'operatore e al cittadino designato;
    2. Aggiornamento stato salute cittadino
 */
INSERT INTO TAMPONE VALUES ('BLZLNZ99B17H199T', '2020-03-20', 'Positivo', 1);

/**
  Selezione tutti i distinti comuni dei cittadini
 */
SELECT DISTINCT comuneResidenza
FROM CITTADINO;

/**
  Ultimo esito tampone per ogni persona
 */
SELECT CF, esito, data
FROM TAMPONE T
WHERE T.data = (SELECT MAX(T1.data)
                FROM TAMPONE T1
                WHERE T1.CF = T.CF);

/**
  Numero tamponi positivi e negativi
 */
SELECT esito, COUNT(esito) AS ContaEsito
FROM TAMPONE T
WHERE T.data = (SELECT MAX(T1.data)
                FROM TAMPONE T1
                WHERE T1.CF = T.CF)
GROUP BY esito;

/**
  Numero decessi
 */
SELECT tipo, COUNT(tipo) AS ContaEsito
FROM REFERTO R
WHERE R.data = (SELECT MAX(R1.data)
                FROM REFERTO R1
                WHERE R1.CF = R.CF)
GROUP BY tipo;




