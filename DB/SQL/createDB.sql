-- *********************************************
-- * Standard SQL generation
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.1
-- * Generator date: Dec  4 2018
-- * Generation date: Thu Jun 25 13:14:26 2020
-- * LUN file: \\.psf\Home\Documents\Università\2° anno\2° semestre\Basi di dati\Progetto\Covid.lun
-- * Schema: Schema Logico/SQL1
-- *********************************************


-- Database Section
-- ________________

drop database CovidManager;

create database CovidManager;

Use CovidManager;


-- DBSpace Section
-- _______________


-- Tables Section
-- _____________

create table CITTADINO (
     CF varchar(17) not null,
     nome varchar(20) not null,
     cognome varchar(20) not null,
     dataDiNascita date not null,
     genere varchar(20) not null,
     comuneResidenza varchar(20) not null,
     telefono varchar(20) not null,
     ID_MED int,
     constraint ID_CITTADINO_ID primary key (CF));

create table CONTATTO_REGISTRATO (
     CON_CF varchar(17) not null,
     CF varchar(17) not null,
     dataInizio date not null,
     dataFine date not null,
     durataEsposizione float not null,
     longitudine float not null,
     latitudine float not null,
     altitudine float not null,
     constraint ID_CONTATTO_REGISTRATO_ID primary key (CON_CF, CF, dataInizio, dataFine));

create table CREDENZIALI (
     username varchar(50) not null,
     CF varchar(17) not null,
     hashedPassword varchar(150) not null,
     tipo varchar(20) not null,
     constraint ID_CREDENZIALI_ID primary key (username),
     constraint SID_CREDE_CITTA_ID unique (CF));

create table DIARIO_CLINICO (
     idDiario int auto_increment not null,
     CF varchar(17) not null,
     dataCreazione date not null,
     dataRimozione date,
     testoDiario varchar(1000) not null,
     constraint ID_DIARIO_CLINICO_ID primary key (idDiario),
     constraint SID_DIARI_CITTA_ID unique (CF));

create table EPIDEMIOLOGO (
     ID_EPI int auto_increment not null,
     CF varchar(17) not null,
     constraint ID_EPIDEMIOLOGO_ID primary key (ID_EPI));

create table MEDICO_DI_BASE (
     ID_MED int auto_increment not null,
     CF varchar(17) not null,
     constraint ID_MEDICO_DI_BASE_ID primary key (ID_MED));

create table MEDICO_RESPONSABILE (
     ID_RESP int auto_increment not null,
     CF varchar(17) not null,
     constraint ID_MEDICO_RESPONSABILE_ID primary key (ID_RESP));

create table OPERATORE_DI_TAMPONE (
     ID_OPE int auto_increment not null,
     CF varchar(17) not null,
     constraint ID_OPERATORE_DI_TAMPONE_ID primary key (ID_OPE));

create table OSPEDALE (
     idOspedale int auto_increment not null,
     nomeOspedale varchar(50) not null,
     indirizzo varchar(50) not null,
     mailDirezione varchar(50) not null,
     constraint ID_OSPEDALE_ID primary key (idOspedale));

create table PIANO (
     idOspedale int not null,
     numeroPiano int not null,
     constraint ID_PIANO_ID primary key (idOspedale, idPiano));

create table REFERTO_DECESSO (
     idReferto int auto_increment not null,
     CF varchar(17) not null,
     data date not null,
     ora varchar(5) not null,
     constraint ID_REFERTO_DECESSO_ID primary key (idReferto),
     constraint SID_REFER_CITTA_ID unique (CF));

create table REFERTO_RICOVERO (
     CF varchar(17) not null,
     tipo varchar(20) not null,
     codiceGravita int not null,
     dataInizio date not null,
     dataFine date,
     idOspedale int not null,
     numeroPiano int not null,
     idReparto int not null,
     constraint ID_REFERTO_RICOVERO_ID primary key (CF, tipo, dataInizio));

create table REPARTO_COVID (
     idOspedale int not null,
     numeroPiano int not null,
     idReparto int not null,
     ID_RESP int not null,
     numeroPostiLetto int not null,
     numeroPostiTI int not null,
     constraint ID_REPARTO_COVID_ID primary key (idOspedale, numeroPiano, idReparto),
     constraint SID_REPAR_MEDIC_ID unique (ID_RESP));

create table STATO_SALUTE (
     CF varchar(17) not null,
     data date not null,
     tipo varchar(20) not null,
     constraint ID_STATO_SALUTE_ID primary key (CF, data, tipo));

create table TAMPONE (
     CF varchar(17) not null,
     data date not null,
     esito varchar(20) not null,
     ID_OPE int,
     constraint ID_TAMPONE_ID primary key (CF, data));


-- Constraints Section
-- ___________________

alter table CITTADINO add constraint REF_CITTA_MEDIC_FK
     foreign key (ID_MED)
     references MEDICO_DI_BASE(ID_MED);

alter table CONTATTO_REGISTRATO add constraint REF_CONTA_CITTA_1_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table CONTATTO_REGISTRATO add constraint REF_CONTA_CITTA
     foreign key (CON_CF)
     references CITTADINO(CF);

alter table CREDENZIALI add constraint SID_CREDE_CITTA_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table DIARIO_CLINICO add constraint SID_DIARI_CITTA_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table EPIDEMIOLOGO add constraint REF_EPIDE_CITTA_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table MEDICO_DI_BASE add constraint REF_MEDIC_CITTA_1_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table MEDICO_RESPONSABILE add constraint REF_MEDIC_CITTA_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table OPERATORE_DI_TAMPONE add constraint REF_OPERA_CITTA_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table PIANO add constraint REF_PIANO_OSPED
     foreign key (idOspedale)
     references OSPEDALE(idOspedale);

alter table REFERTO_DECESSO add constraint SID_REFER_CITTA_FK
     foreign key (CF)
     references CITTADINO(CF);

alter table REFERTO_RICOVERO add constraint REF_REFER_CITTA
     foreign key (CF)
     references CITTADINO(CF);

alter table REFERTO_RICOVERO add constraint REF_REFER_REPAR_FK
     foreign key (idOspedale, numeroPiano, idReparto)
     references REPARTO_COVID(idOspedale, numeroPiano, idReparto);

alter table REPARTO_COVID add constraint REF_REPAR_PIANO
     foreign key (idOspedale, numeroPiano)
     references PIANO(idOspedale, numeroPiano);

alter table REPARTO_COVID add constraint SID_REPAR_MEDIC_FK
     foreign key (ID_RESP)
     references MEDICO_RESPONSABILE(ID_RESP);

alter table STATO_SALUTE add constraint REF_STATO_CITTA
     foreign key (CF)
     references CITTADINO(CF);

alter table TAMPONE add constraint REF_TAMPO_CITTA
     foreign key (CF)
     references CITTADINO(CF);

alter table TAMPONE add constraint REF_TAMPO_OPERA_FK
     foreign key (ID_OPE)
     references OPERATORE_DI_TAMPONE(ID_OPE);


-- Index Section
-- _____________

create unique index ID_CITTADINO_IND
     on CITTADINO (CF);

create index REF_CITTA_MEDIC_IND
     on CITTADINO (ID_MED);

create unique index ID_CONTATTO_REGISTRATO_IND
     on CONTATTO_REGISTRATO (CON_CF, CF, dataInizio, dataFine);

create index REF_CONTA_CITTA_1_IND
     on CONTATTO_REGISTRATO (CF);

create unique index ID_CREDENZIALI_IND
     on CREDENZIALI (username);

create unique index SID_CREDE_CITTA_IND
     on CREDENZIALI (CF);

create unique index ID_DIARIO_CLINICO_IND
     on DIARIO_CLINICO (idDiario);

create unique index SID_DIARI_CITTA_IND
     on DIARIO_CLINICO (CF);

create index REF_EPIDE_CITTA_IND
     on EPIDEMIOLOGO (CF);

create unique index ID_EPIDEMIOLOGO_IND
     on EPIDEMIOLOGO (ID_EPI);

create unique index ID_MEDICO_DI_BASE_IND
     on MEDICO_DI_BASE (ID_MED);

create index REF_MEDIC_CITTA_1_IND
     on MEDICO_DI_BASE (CF);

create index REF_MEDIC_CITTA_IND
     on MEDICO_RESPONSABILE (CF);

create unique index ID_MEDICO_RESPONSABILE_IND
     on MEDICO_RESPONSABILE (ID_RESP);

create unique index ID_OPERATORE_DI_TAMPONE_IND
     on OPERATORE_DI_TAMPONE (ID_OPE);

create index REF_OPERA_CITTA_IND
     on OPERATORE_DI_TAMPONE (CF);

create unique index ID_OSPEDALE_IND
     on OSPEDALE (idOspedale);

create unique index ID_PIANO_IND
     on PIANO (idOspedale, numeroPiano);

create unique index ID_REFERTO_DECESSO_IND
     on REFERTO_DECESSO (idReferto);

create unique index SID_REFER_CITTA_IND
     on REFERTO_DECESSO (CF);

create unique index ID_REFERTO_RICOVERO_IND
     on REFERTO_RICOVERO (CF, tipo, dataInizio);

create index REF_REFER_REPAR_IND
     on REFERTO_RICOVERO (idOspedale, numeroPiano, idReparto);

create unique index ID_REPARTO_COVID_IND
     on REPARTO_COVID (idOspedale, numeroPiano, idReparto);

create unique index SID_REPAR_MEDIC_IND
     on REPARTO_COVID (ID_RESP);

create unique index ID_STATO_SALUTE_IND
     on STATO_SALUTE (CF, data, tipo);

create unique index ID_TAMPONE_IND
     on TAMPONE (CF, data);

create index REF_TAMPO_OPERA_IND
     on TAMPONE (ID_OPE);
