# Frontend development

### SpringBoot

Per lo sviluppo lato frontend è stato integrato tutto con Spring Boot, che oltre a lanciare la parte di backend offre una serie di tools utili allo sviluppo rapido dell'applicazione. Inoltre è compatibile con gli archivi WebJars. Per lanciare l'applicazione SpringBoot lanciare il seguente goald di maven:
 
 
	mvn spring-boot:run
	
### Restart e Livereload
Sul progetto generato dall'archetipo è già abilitato l'autorestart e il livereload. Per sfruttare il livereload bisogna installare l'estensione del browser che supporta il LiveReload standard (ad esempio per Chrome installare l'estensione https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei ). Successivamente lanciare il watcher :

	mvn fizzed-watcher:run

Il watcher si preoccupa di ricompilare i files ad ogni modifica e il livereload si preopccupa di cattuare l'evento di restart e vi farà il refresh automatico della pagina nel browser. Se usate Eclipse non avete bisogno di usare il watcher di maven.

### Struttura del progetto frontend
    +-- app
        |   +-- fonts
        |   +-- images
        |   +-- partials
        |   +-- scripts
            |   +-- controllers
            |   +-- services
            |   +-- filters
            |   +-- factories
            |   +-- decorators
            |   +-- constants
            |   --controllers.js
            |   --services.js
            |   --factories.js
            |   --decorators.js
            |   --filters.js
            |   --constants.js
            |   --app.js
            |   --main.js
        | -- index.html    
        | +-- styles
        | +-- views
    +-- doc
    +-- sass
    +-- test
    --bower.json
    --package.json
    --Gruntfile.js

La struttura anche se corposa in realtà è molto semplice, in pratica l'applicazione angular è contenuta nella cartella "app" che contiene tutti i componenti creabili con AngularJs (controller, services, etc), insieme alle view (files.html), e l'index.html.

### index.html e boot dell'applicazione
L'index.html in pratica carica un unico file css e un unico file javascript questo perché il css viene unificato in un unico file tramite il preprocessore SASS e il JS viene caricato in modalità asincrona grazie al famoso tool RequireJS. 
Tutte le dipendenze dell'applicazione devon essere dichiarate nel *bower.json* per poi essere generate nel *main.js*. Il main oltre a configurare requirejs fa il boot dell'applicazione, istruendo RequireJs al download dei moduli necessari, tra questi ovviamente c'è anche Angular. Infine il boot provvede ad avviare il bootstrap di angular.

### Gestione delle dipendenze lato client
La gestione delle dipendenze lato client è supportata dal sistema wei webjars che è completamente compatibile con SpringBoot e con qualsiasi ServletContainer. Di fatto un webjar è un normalissimo jar che non ha nessun file .class e solo file javascript ed eventualmente css. Tra le dipendenze del progetto esiste un jar che si chiama "webjar-locator" che si preoccupa di esplodere in automatico un webjar quando viene richiesto col path /webjars/nomeartefatto/risorsa. Ad esempio il webjar angularjs.1.5.8 sarà disponibile al path http//127.0.0.1:8080/webjars/angularjs/angular.js. Il path è un link simbolico alla risorsa e non c'è bisogno di specificare la versione della libreria nel path.

### Direttive

## areas-sidebar
Per utilizzare la sidebar una volta importata la direttiva bisogna dichiarare il seguente codice html nella propria view: 

        <areas-sidebar config="partials/sidebar.json">              
    
        </areas-sidebar>

tramite l'attributo config possiamo iniettare la configurazione della sidebar. Una configurazione tipo della sidebar può essere:
        [
            {
                "title": "Entry1",
                "iconClass": "fa fa-info-circle",
                "target": "dashboard",
                "childs": [{
                    "text": "CHILD ENTRY",
                    "target": "dashboard"
                },
                {
                    "text": "CHILD ENTRY 2",
                    "target": "dashboard"
                }        
                ]
            },
            {
                "title": "Entry2",
                "iconClass": "fa fa-info-circle"
            }
        ] 

### SASS
Se si vuole apportare qualche modifica al css non modificare mai il file main.css, poiché si tratta di un file generato dal preprocessore SASS. Per rigenerare il css bisogna modificare i files *scss* presenti nella cartella *sass* in seguito Grunt penserà poi ad rigenerare il file per voi. Se avete il livereload attivo con *grunt serve* vi basterà scatenare una modifica al file  *app.scss*.

### Avviare Chome in unsecure mode
Per diversi motivi potrebbe essere necessario avviare Chrome in modalità unsecure le opzioni da linea di comando per avviarlo con tale modalità sono le seguetni:
 
	--args --disable-web-security --user-data-dir "C:\temp\chromeDevSession"

## Testing

...

    
    
        