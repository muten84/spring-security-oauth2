# WDOEMSMOBILE

##Struttura progetto
Il progetto è strutturato in una parte puramente web presente nella cartella src e definita come progetto ng-cli e una parte nativa presente nella cartella electron per avviare la parte web come eseguibile standalone.

###Startup parte web
Per avviare la parte web usare il comando npx ng serve

###Startup parte nativa
Per avviare la parte nativa si può usare il comando npx electron-forge start

###Agent
L'agent è una componente nativa potenzialmente multipiattaforma e che mette in comunicazione la parte web con il backend sfruttando le linee guida delle PWA una volta scariato l'agent si può avviare col comando:
java -Djava.library.path=/esel/terminal/bin -Dtinylog.configuration=/esel/terminal/etc/tinylog.properties -Xms10m -Xmx32m -jar mobile-agent.jar -r http://qalitax.eng.it:9129/sdo118Artifacts-manager/ -g EE -a terminal -l /esel/terminal/download -i dummy.bat -p 8989


####Template struttura nativa
La cartella template contiene la struttura che viene ospitata dalla parte nativa per permettere agli applicativi di funzionare in modalità offline.
Il template prevede:
 - la cartella bin : dove sono contenuti tutti gli eseguibili (agent, laucher, eseguibile electron e relativi batch di esecuzione)
 - la cartella app: contiene la lista delle applicazioni lanciabili offline e le relativi risorse in termini di codice js, html font immagini etc.
 - la cartella db: contiene le risorse in termini di dati che permettono l'operatività anche offline degli applicativi presenti nelal cartella app quando hanno bisogno di attingere da tali risorse
 - la cartella lib: contiene le librerie da cui l'agent dipende e che per motivi di ottimizzazione degli aggiornamenti può essere necessario tenere separate dall'agent stesso.
 - la cartella web contiene i file utili ad electron per configurare la procedura di esecuzione del browser.

##Compilazione per produzione
npx ng build --prod --aot=false -bh app --locale=it_IT --missing-translation=ignore --named-chunks=true --build-optimizer=false --vendor-chunk=true --common-chunk=true --sourcemap=false -dop=true --extract-css

npx ng build --prod -bh app

###Electron
Se ci fosse bisogo di rebuildare electron bisogna dare il comando:
npm run package

##Icone
Le icone che utilizziamo sono i fa e le angular material icon in più abbiamo aggiunto le materialdesignicons di seguito i riferimenti:
 - https://materialdesignicons.com/getting-started

## Avanzare la versione di angular
Per avanzare la versione di angualr e altre librerie fare riferimento a questa guida.

## Procedura di reset software del dispositivo:
Per resettare il dispositivo si può invocare la relativa chiamata "resetAll". 
Se dovessero esserci imprevisti alla procedura di resetAll eseguire i seguenti comandi:
  - "stopProcess/terminal-electron.exe/1"
  - "startProcess/electron.bat/"
Se anche questa procedura non dovesse andare a buon fine, eseguire il comando:
  - "/restart" per comandare un riavvio hardware del device che non esegue un reset software


## Procedura di aggiornamento dell'agent:
Per produrre l'agent bisogna buildare il pacchetto dal relativo pom di maven.
Creare l'archivio mobile_agent e relativo manifest (manca script di automazione che lo crea in automatico)
Deployare l'archivio di aggiornamento dell'agent sul repository configurato nel servizio di gestione artefatti sdo118ArtifactsManager (attualmente su 103).
Verificare aggiornamento su 

## Procedura di aggiornamento del terminale web:
Per produrre un pacchetto del terminale web bisogna avviare la build come descritto in Compilazione per produzione. Creare l'archivio con il suffisso warall se si vuole deplyare un archivio completo.
Creare l'archivio con il suffisso wardelta se si vuole aggiornare solo i file javascript index.html e css e lasciare invariati i font e le immaggini e gli asset.
Deployare l'archivio di aggiornamento della webapp sul repository configurato nel servizio di gestione artefatti sdo118ArtifactsManager (attualmente su 103).

#Lista comandi dell'agent:
Servizi richiamabili in GET:
 
Device/Agent
  - /test: permette di verificare se l'agent è vivo 
  - /deviceId: permette di ottenere il seriale del dispositivo  
  - /getVersion: permette di ottenere la versione installata dell'agent
  - /clearAppCache: permette di pulire la cache del browser (electron deve essere spento)
  - /getAgentState: permette di ottenere lo stato corrente dell'agent (non DEVE essere ERROR).
  - /unlock: permette di lanciare una shell dei comandi e una shell explorer (per visualizzarle Electron non deve essere impostato in always-on-top a true)
  - /restart: permette di comandare un riavvio ritardato del dispositivo a N secondi.
  - /shutdown: permette di comandare uno spegnimento ritardato del dispositivo a N secondi.
  - /beepAlarm: permette di testare il controllo sonoro del terminale in fase di start
  - /stopBeepAlarm: permette di testare il controllo sonoro del terminale in fase di stop
  
Controllo Electron:
 - "stopProcess/terminal-electron.exe/1": ferma l'istanza corrente di Electron
 - "startProcess/electron.bat/": avvia una nuova istanza di Electron


Gestione artefatti:
  - /resetLocalStore: resetta lo store locale dove sono contenuti gli artefatti scaricati del dispositivo
  - /getArtifact/{artifactId}: permette di ottenere informazioni sull'artfatto con id artifactId installato

Gestione messaggi:
  - /messageStore: permette di interrogare lo store dei messaggi processati dal terminale. (fix eliminare i messaggi troppo vecchi).
  - /publishTest: permette di inviare una publish di test e verificare la comunicazione con un broker MQTTT.
  - /isMQTTConnected: permette di verificare se il canale di pushing del termianle è correttamente attivato.



 Gestione Document Store:
  - resetDocumentStore: permette di resettare lo store json delle risorse scaricate in fase di scelta della centrale.