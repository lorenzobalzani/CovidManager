-- *********************************************
-- * Standard SQL generation
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.1
-- * Generator date: Dec  4 2018
-- * Generation date: Tue Jun 30 11:52:16 2020
-- * LUN file: \\.psf\Home\Documents\Università\2° anno\2° semestre\Basi di dati\Progetto\covid19\DB\DBProject.lun
-- * Schema: CovidManager/SQL
-- *********************************************

-- Database Section

drop database CovidManager;

create database CovidManager;

use CovidManager;

-- Create Database

create table CITTADINO
(
    CF              varchar(17) not null,
    nome            varchar(20) not null,
    cognome         varchar(20) not null,
    dataDiNascita   date        not null,
    genere          varchar(20) not null,
    comuneResidenza varchar(40) not null,
    telefono        varchar(20) not null,
    ID_MED          int         null,
    constraint ID_CITTADINO_IND
        unique (CF)
);

create index REF_CITTA_MEDIC_IND
    on CITTADINO (ID_MED);

alter table CITTADINO
    add primary key (CF);

create table CONTATTO_REGISTRATO
(
    CON_CF            varchar(17) not null,
    CF                varchar(17) not null,
    ID_TEM            int 		  not null,
    longitudine       float       not null,
    latitudine        float       not null,
    altitudine        float       not null,
    constraint ID_CONTATTO_REGISTRATO_IND
        unique (CON_CF, CF, ID_TEM),
    constraint REF_CONTA_CITTA
        foreign key (CON_CF) references CITTADINO (CF),
    constraint REF_CONTA_CITTA_1_FK
        foreign key (CF) references CITTADINO (CF),
    CONSTRAINT CHK_differentPeople CHECK (CF <> CON_CF)
);

create index REF_CONTA_CITTA_1_IND
    on CONTATTO_REGISTRATO (CF);

alter table CONTATTO_REGISTRATO
    add primary key (CON_CF, CF, ID_TEM);

create table CREDENZIALI
(
    username       varchar(50)                                                                                               not null,
    CF             varchar(17)                                                                                               not null,
    hashedPassword varchar(150)                                                                                              not null,
    tipo           enum ('MEDICO_DI_BASE', 'MEDICO_RESPONSABILE', 'OPERATORE_DI_TAMPONE', 'EPIDEMIOLOGO', 'CONTACT_TRACING') null,
    constraint ID_CREDENZIALI_IND
        unique (username),
    constraint SID_CREDE_CITTA_ID
        unique (CF),
    constraint SID_CREDE_CITTA_IND
        unique (CF),
    constraint SID_CREDE_CITTA_FK
        foreign key (CF) references CITTADINO (CF),
    CONSTRAINT CHK_usernameLength  CHECK (LENGTH(username) > 5)
);

alter table CREDENZIALI
    add primary key (username);

create table DIARIO_CLINICO
(
    idDiario      int auto_increment,
    CF            varchar(17)   not null,
    dataCreazione datetime      not null,
    dataRimozione datetime      null,
    testoDiario   varchar(1000) not null,
    constraint ID_DIARIO_CLINICO_IND
        unique (idDiario),
    constraint SID_DIARI_CITTA_ID
        unique (CF),
    constraint SID_DIARI_CITTA_IND
        unique (CF),
    constraint SID_DIARI_CITTA_FK
        foreign key (CF) references CITTADINO (CF),
    CONSTRAINT CHK_dates  CHECK (IF(dataRimozione IS NOT NULL,
        dataRimozione>dataCreazione, TRUE))
);

alter table DIARIO_CLINICO
    add primary key (idDiario);

create table EPIDEMIOLOGO
(
    ID_EPI int auto_increment,
    CF     varchar(17) not null,
    constraint ID_EPIDEMIOLOGO_IND
        unique (ID_EPI),
    constraint REF_EPIDE_CITTA_FK
        foreign key (CF) references CITTADINO (CF)
);

create index REF_EPIDE_CITTA_IND
    on EPIDEMIOLOGO (CF);

alter table EPIDEMIOLOGO
    add primary key (ID_EPI);

create table MEDICO_DI_BASE
(
    ID_MED int auto_increment,
    CF     varchar(17) not null,
    constraint ID_MEDICO_DI_BASE_IND
        unique (ID_MED),
    constraint REF_MEDIC_CITTA_1_FK
        foreign key (CF) references CITTADINO (CF)
);

create index REF_MEDIC_CITTA_1_IND
    on MEDICO_DI_BASE (CF);

alter table MEDICO_DI_BASE
    add primary key (ID_MED);

alter table CITTADINO
    add constraint REF_CITTA_MEDIC_FK
        foreign key (ID_MED) references MEDICO_DI_BASE (ID_MED);

create table MEDICO_RESPONSABILE
(
    ID_RESP int auto_increment,
    CF      varchar(17) not null,
    constraint ID_MEDICO_RESPONSABILE_IND
        unique (ID_RESP),
    constraint REF_MEDIC_CITTA_FK
        foreign key (CF) references CITTADINO (CF)
);

create index REF_MEDIC_CITTA_IND
    on MEDICO_RESPONSABILE (CF);

alter table MEDICO_RESPONSABILE
    add primary key (ID_RESP);

create table OPERATORE_DI_TAMPONE
(
    ID_OPE int auto_increment,
    CF     varchar(17) not null,
    constraint ID_OPERATORE_DI_TAMPONE_IND
        unique (ID_OPE),
    constraint REF_OPERA_CITTA_FK
        foreign key (CF) references CITTADINO (CF)
);

create index REF_OPERA_CITTA_IND
    on OPERATORE_DI_TAMPONE (CF);

alter table OPERATORE_DI_TAMPONE
    add primary key (ID_OPE);

create table OSPEDALE
(
    idOspedale    int auto_increment,
    nomeOspedale  varchar(50) not null,
    indirizzo     varchar(50) not null,
    mailDirezione varchar(50) not null,
    constraint ID_OSPEDALE_IND
        unique (idOspedale),
    CONSTRAINT CHK_dates  CHECK (email)
);

alter table OSPEDALE
    add primary key (idOspedale);

create table PIANO
(
    idOspedale  int not null,
    numeroPiano int not null,
    constraint ID_PIANO_IND
        unique (idOspedale, numeroPiano),
    constraint REF_PIANO_OSPED
        foreign key (idOspedale) references OSPEDALE (idOspedale)
);

alter table PIANO
    add primary key (idOspedale, numeroPiano);

create table REFERTO
(
    CF            varchar(17)                                                                                                not null,
    tipo          enum ('Inizio ricovero', 'Fine ricovero', 'Inizio terapia intensiva', 'Fine terapia intensiva', 'Decesso') not null,
    codiceGravita int                                                                                                        not null,
    data          datetime                                                                                                       not null,
    idOspedale    int                                                                                                        not null,
    numeroPiano   int                                                                                                        not null,
    idReparto     int                                                                                                        not null,
    primary key (CF, tipo, data),
    constraint REF_REFERTO_REPARTO
        foreign key (idOspedale, numeroPiano, idReparto)
            references REPARTO_COVID (idOspedale, numeroPiano, idReparto)
);

create table REPARTO_COVID
(
    idOspedale       int not null,
    numeroPiano          int not null,
    idReparto        int not null,
    ID_RESP          int not null,
    numeroPostiLetto int not null,
    numeroPostiTI    int not null,
    constraint ID_REPARTO_COVID_IND
        unique (idOspedale, numeroPiano, idReparto),
    constraint SID_REPAR_MEDIC_ID
        unique (ID_RESP),
    constraint SID_REPAR_MEDIC_IND
        unique (ID_RESP),
    constraint REF_REPAR_PIANO
        foreign key (idOspedale, numeroPiano) references PIANO (idOspedale, numeroPiano),
    constraint SID_REPAR_MEDIC_FK
        foreign key (ID_RESP) references MEDICO_RESPONSABILE (ID_RESP)
);

alter table REPARTO_COVID
    add primary key (idOspedale, numeroPiano, idReparto);

create table TAMPONE
(
    CF     varchar(17) not null,
    data   datetime    not null,
    esito  enum ('Positivo', 'Negativo') not null,
    ID_OPE int         null,
    constraint ID_TAMPONE_IND
        unique (CF, data),
    constraint REF_TAMPO_CITTA
        foreign key (CF) references CITTADINO (CF),
    constraint REF_TAMPO_OPERA_FK
        foreign key (ID_OPE) references OPERATORE_DI_TAMPONE (ID_OPE)
);

create index REF_TAMPO_OPERA_IND
    on TAMPONE (ID_OPE);

alter table TAMPONE
    add primary key (CF, data);
