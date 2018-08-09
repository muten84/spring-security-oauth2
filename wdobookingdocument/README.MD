# WDOBookingDocument

## Pre-requisiti
Gli unici prerequisiti per l'utilizzo degli strumenti di sviluppo sono aver installato [nodejs](https://nodejs.org/) ed npm e successivamente installare globalmente i seguenti pacchetti:

    npm -g install grunt-cli bower
    /*yo generator-karma generator-angular*/ SOLO SE SI DEVE RIGENERARE UN ALTRO PROGETTO COME QUESTO 
    bower install bootstrap-sass
    
## Development
	
Solo la prima volta che viene scaricato il progetto bisogna lanciare i seguenti comandi:

	npm install
	bower install
in questo modo si installano tutti i pacchetti utili allo sviluppo e ele dipendenze javascript del progetto come angular, bootstrap etc.

Successivamente e ogni volta che si aggiunge una dipendenza bisogna lanciare il comando:
	
	grunt bowerRequirejs
	
Per avviare l'applicazione ci sono diversi task di Grunt il più utile è il comando:

	grunt serve

che vi avvia un server http e il watcher sui file che modificate per avere le modifiche in tempo reale.

## Struttura del progetto
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

### SASS
Se si vuole apportare qualche modifica al css non modificare mai il file main.css, poiché si tratta di un file generato dal preprocessore SASS. Per rigenerare il css bisogna modificare i files *scss* presenti nella cartella *sass* in seguito Grunt penserà poi ad rigenerare il file per voi. Se avete il livereload attivo con *grunt serve* vi basterà scatenare una modifica al file  *app.scss*.

### Avviare Chome in unsecure mode
Per diversi motivi potrebbe essere necessario avviare Chrome in modalità unsecure le opzioni da liena di comando per avviarlo con tale modalità sono le seguetni:
 
	--args --disable-web-security --user-data-dir "C:\temp\chromeDevSession"

## Testing

...

    
    
        