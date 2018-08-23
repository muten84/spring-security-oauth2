
/**************************************************************************************
 * Logger
 * Stampa messaggi a secoda del livello di log definito nel file di configurazione
 * Usare la variabile overrideLogLevel per forzare il log su un certo livello
 *
 * Uso:
 *  Ogni log deve contenere una stringa e facoltativamente un oggetto
 *  Vengono stampati tutti i messaggi con livello <= di logLevel
 *  I livelli possibili sono:
 *    ERROR = 1
 *    WARN  = 2
 *    INFO  = 3
 *    DEBUG = 4
 *  Nel file di configurazione esterno occorre definire la costante logLevel valorizzata
 *  con la chiave del livello (ERROR, WARN, ...)
 *  Su browser il metodo clear cancella tutto il log precedente
 *
 * Esempi:
 *    --- Errore
 *    logger.error("errore", $rootScope.config);
 *    --- Warning
 *    logger.warn("warning", $rootScope.config);
 *    --- Info
 *    logger.info("info", $rootScope.config);
 *    --- Debug
 *    logger.debug("debug", $rootScope.config);
 *    --- Raggruppamenti di log
 *    logger.groupStart("GRUPPO");
 *    logger.groupEnd();
 *
 **************************************************************************************/

angular.module("service").factory('loggerService', function () {

    var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
    var isFirefox = typeof InstallTrigger !== 'undefined';
    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
    var isIE = /*@cc_on!@*/false || !!document.documentMode;
    var isEdge = !isIE && !!window.StyleMedia;
    var isChrome = !!window.chrome && !!window.chrome.webstore;
    var isBlink = (isChrome || isOpera) && !!window.CSS;

    var levels = {
        "ERROR" : 1,    // Stampa solo gli errori
        "WARN"  : 2,    //    "   anche i warning
        "INFO"  : 3,    //    "   anche le info
        "DEBUG" : 4     //    "   anche i debug e i group
    };

    // Stampa la stringa e l'oggetto (se richiesto) con la giusta funzione
    var print = function (func, msg, obj) {
        // Se msg è un oggetto inizializzo msg a vuoto
        if (typeof msg == "object") {
            obj = msg;
            msg = "";
        }
        var line = "";

        if(isChrome){
            line = new Error().stack.split("\n")[3];
            line = (line.indexOf(' (') >= 0) ? line.split(' (')[1].substring(0, line.length - 1) : line.split('at ')[1];
            line = line.lastIndexOf(")") != -1 ? line.substring(0, line.lastIndexOf(")")) : line;
            line = line.lastIndexOf("?") != -1 ? line.substring(0, line.lastIndexOf("?")) : line;
            //line = line.replace(/^.*\/\/[^\/]+/, '');
        }
        else if(isFirefox){
            line = new Error().stack.split("\n")[3].split("@")[1];
            line = line.lastIndexOf("?") != -1 ? line.substring(0, line.lastIndexOf("?")) : line;
            line = line.replace(/^.*\/\/[^\/]+/, '');
        }

        // Se sono su cordova eseguo lo stingify dell'oggetto qualora ci fosse
        if(!!window.cordova && obj) {
            try{
                obj = JSON.stringify(obj);
            }catch(e){}
        }

        // Stampa anche l'oggetto
        if (obj)
            window["console"][func](line + ": " + msg, obj);
        // Stampa sono la stringa
        else
            window["console"][func](line + ": " + msg);
    };

    return {
        // Livello di logging richiesto dall'applicazione
        logLevel: "DEBUG",
        // Override del livello di logging richiesto
        overrideLogLevel: null,
        // Inizializza il logger
        init: function (requestedLogLevel) {

            // Se ho richiesto un override del livello di log applico quello
            if (this.overrideLogLevel) {
                this.logLevel = this.overrideLogLevel;
            } else {
                // Se è stato richiesto un livello specifico che è tra quelli disponibili lo applico
                if (requestedLogLevel && levels[requestedLogLevel.toUpperCase()]) {
                    this.logLevel = requestedLogLevel.toUpperCase();
                }
            }

            console.log("Servizio logger impostato a livello <" + this.logLevel + ">");
        },
        // Stampa sempre gli errori
        error: function (msg, obj) {
            print("error", msg, obj);
        },
        // Stampa anche i warning solo quando il livello è >= WARN
        warn: function (msg, obj) {
            if (levels[this.logLevel] >= levels["WARN"]) {
                print("warn", msg, obj);
            }
        },
        // Stampa anche gli info solo quando il livello è >= INFO
        info: function (msg, obj) {
            if (levels[this.logLevel] >= levels["INFO"]) {
                print("info", msg, obj);
            }
        },
        // Stampa anche i debug solo quando il livello è >= DEBUG
        debug: function (msg, obj) {
            if (levels[this.logLevel] >= levels["DEBUG"]) {
                print("debug", msg, obj);
            }
        },
        // Stampa i raggruppamenti solo quando il livello è >= DEBUG
        groupStart: function (msg, obj) {
            if (levels[this.logLevel] >= levels["DEBUG"]) {
                print("groupCollapsed", msg, obj);
            }
        },
        groupEnd: function () {
            if (levels[this.logLevel] >= levels["DEBUG"]) {
                print("groupEnd", "");
            }
        },
        // Cancella la console solo su browser
        clear: function () {
            if (typeof device === "undefined") {
                console.clear();
            }
        }
    };
});