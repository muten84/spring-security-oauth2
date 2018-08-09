/* Formatted on 25/07/2017 12:19:41 (QP5 v5.256.13226.35538) */
CREATE TABLE DAE_STATIC_DATA
(
   ID         VARCHAR2 (64) NOT NULL,
   TYPE       VARCHAR2 (64),
   VALUE      VARCHAR2 (64),
   ordering   NUMBER,
   CONSTRAINT PK_DAE_STATIC_DATA_ID PRIMARY KEY (ID)
);



INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('st_1',
             'TIPOLOGIA_SEGNALAZIONI',
             'COORDINATE ERRATE',
             0);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('st_2',
             'TIPOLOGIA_SEGNALAZIONI',
             'DAE DANNEGGIATO',
             1);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('st_3',
             'TIPOLOGIA_SEGNALAZIONI',
             'DAE ASSENTE',
             2);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('st_4',
             'TIPOLOGIA_SEGNALAZIONI',
             'SEGNALE DI ALLARME',
             3);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('st_5',
             'TIPOLOGIA_SEGNALAZIONI',
             'ALTRO (VEDI NOTE)',
             4);



INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_1',
             'STATO_GUASTI',
             'APERTA',
             0);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_2',
             'STATO_GUASTI',
             'DA_VERIFICARE',
             1);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_3',
             'STATO_GUASTI',
             'VERIFICATA',
             2);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_4',
             'STATO_GUASTI',
             'ERRATA',
             3);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_5',
             'STATO_GUASTI',
             'AZIONI_INTRAPRESE',
             4);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_6',
             'STATO_GUASTI',
             'RISOLTA',
             5);

INSERT INTO DAE_STATIC_DATA (ID,
                             TYPE,
                             VALUE,
                             ordering)
     VALUES ('stg_7',
             'STATO_GUASTI',
             'CHIUSA',
             6);

COMMIT;