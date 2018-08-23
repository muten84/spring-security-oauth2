CREATE TABLE DAE_DAE_HISTORY
(
   ID                          VARCHAR2 (64 BYTE) NOT NULL,
   OPERATION                   VARCHAR2 (64 BYTE) NOT NULL,
   UTENTE_ID                   VARCHAR2 (64 BYTE) NOT NULL,
   OPERATION_DATE              DATE,
   -- DAE
   DAE_ID                      VARCHAR2 (64 BYTE) NOT NULL,
   OPERATIVO                   NUMBER (1) DEFAULT 0 NOT NULL,
   MATRICOLA                   VARCHAR2 (64 BYTE),
   MODELLO                     VARCHAR2 (128 BYTE),
   TIPO                        VARCHAR2 (64 BYTE),
   ALIAS                       VARCHAR2 (64 BYTE),
   TIPOLOGIA_STRUTTURA         VARCHAR2 (64 BYTE) NOT NULL,
   NOMESEDE                    VARCHAR2 (150 BYTE),
   UBICAZIONE                  VARCHAR2 (256 BYTE),
   NOTE_DI_ACCESSO_ALLA_SEDE   VARCHAR2 (256 BYTE),
   IMMAGINE                    VARCHAR2 (64 BYTE),
   CERTIFICATO_DAE             VARCHAR2 (64 BYTE),
   NOTE_GENERALI               VARCHAR2 (512 BYTE),
   SCADENZA_DAE                DATE,
   DELETED                     NUMBER DEFAULT 0,
   TIMESTAMP_INSERIMENTO       TIMESTAMP (6),
   DATA_INSERIMENTO            DATE,
   STATO_VALIDAZIONE           VARCHAR2 (16 BYTE),
   CURRENT_STATO_ID            VARCHAR2 (32 BYTE),
   DISPONIBILITA_PERMANENTE    NUMBER,
   --RESPONSABILE
   NOME                        VARCHAR2 (64 BYTE),
   COGNOME                     VARCHAR2 (64 BYTE),
   TELEFONO                    VARCHAR2 (64 BYTE),
   EMAIL                       VARCHAR2 (64 BYTE),
   --POSIZIONE
   COMUNE                      VARCHAR2 (64 BYTE),
   INDIRIZZO                   VARCHAR2 (64 BYTE),
   CIVICO                      VARCHAR2 (10 BYTE),
   LUOGO_PUBBLICO              VARCHAR2 (64 BYTE),
   TIPO_LUOGO_PUBBLICO         VARCHAR2 (20 BYTE),
   --GPSLOCATION
   LATITUDINE                  NUMBER,
   LONGITUDINE                 NUMBER,
   ALTITUDINE                  NUMBER,
   ERROR                       NUMBER,
   TIME_STAMP                  TIMESTAMP (6) DEFAULT SYSDATE,
   --CONSTRAINT
   CONSTRAINT PK_DAE_DAE_HISTORY_ID PRIMARY KEY (ID)
);