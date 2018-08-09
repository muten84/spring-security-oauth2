Casi base
====================

Login
---------------------

### Attori
    - Portale
    - Operatore

### Steps
    1 - Operatore si collega al link inviato via mail
    2 - L'operatore non risulta loggato
    3 - Il Portale propone la pagina di login

### Esito

## Apertura documento da mail
### Attori
   - Portale
   - Operatore
### Precondizioni: 
 - operatore già loggato sul portale
### Steps
    1 - L'operatore riceve la mail di creazione documento
    2 - L'operatore clicca sul link della mail ricevuta
    3 - L'operatore risulta loggato quindi il portale propone la lista dei documenti con il documento appena inviato in lista
### Esito

## Ricerca documenti dal portale
### Attori
   - Portale
   - Operatore
### Precondizioni: 
 - operatore già loggato sul portale
### Steps
    1 - L'operatore riceve la mail di creazione documento
    2 - L'operatore clicca sul link della mail ricevuta
    3 - L'operatore risulta loggato quindi il portale propone la lista dei documenti con il documento appena inviato in lista
### Esito

## Apertura documento
### Attori
   - Portale
   - Operatore
### Precondizioni: 
    1 - operatore già loggato sul portale
    2 - lista documenti contenente il documento precedentemente inviato dalla CTO
### Steps
    1 - L'operatore clicca sul pulsante di apertura Documento
    2 - Il Portale scarica e renderizza il PDF in una modale.
    3 - L'operatore chiude la modale dopo aver eventualmente stampato il documento
    4-  Il portale propone i dati del documento in lista con stato APERTO e la relativa data di apertura
### Esito


## Consulta documento già aperto
### Attori
   - Portale
   - Operatore
### Precondizioni: 
    1 - operatore già loggato sul portale
    2 - lista documenti contenente il documento precedentemente APERTO dall'operatore

### Steps
    1 - L'operatore clicca sul pulsante di apertura Documento.
    2 - Il Portale scarica e renderizza il PDF in una modale.
    3 - L'operatore chiude la modale dopo aver consultato le informazioni sul documento.
    4 - Lo stato del documento resta invariato rispetto allo stato iniziale 
### Esito

## Logout
### Attori
   - Portale
   - Operatore
### Precondizioni: 
    1 - operatore già loggato sul portale    

### Steps
    1 - L'operatore clicca sul pulsante di logout.
    2 - Il Portale propone la maschera di login.
    3 - Qualsiasi tentativo di accesso al documento sarà negato con codice http 401.    
### Esito


# Test di integrazione

## Creazione e invio documento da CTO
 
### Attori
    - CTO
    - DocumentService
    - Portale  
### Steps
    1 - CTO crea prenotazione
    2 - La prenotazione compare sul sinottico
    3 - CTO invoca DocumentService per creare il documento
    4 - DocumentService risponde con esito positivo 200 OK
    5 - CTO invoca DocumentService per inviare la notifica
    6 - DocumentService risponde con esito positivo 200 OK
### Esito

## Chiusura normale prenotazione da CTO e documento con stato APERTO 
### Attori
     - CTO     
     - Portale 
     - DocumentService
     - Operatore
### Precondizioni:
    1 - Prenotazione creata e inviata da CTO
    2 - Documento APERTO da operatore 
### Steps
    1 - CTO chiude prenotazione con motivo chiusurale "NORMALE" 
    2 - Viene mostrato popup all'operatore per conferma chiusura
    3 - CTO invoca DocumentService per chiudere il documento    
    4 - DocumentService risponde con esito positivo 200 OK
    5 - Dal portale aggiornando la lista il documento non sarà più visibile    
### Esito

## Chiusura anomala prenotazione da CTO e documento con stato APERTO
### Attori
     - CTO     
     - Portale 
     - DocumentService
     - Operatore
### Precondizioni:
    1 - Prenotazione creata e inviata da CTO
    2 - Documento APERTO da operatore 
### Steps
    1 - CTO chiude prenotazione con motivo chiusurale "ANOMALA"
    2 - CTO invoca DocumentService per chiudere il documento    
    3 - DocumentService risponde con esito positivo 200 OK
    4 - Dal portale aggiornando la lista il documento non sarà più visibile    
### Esito