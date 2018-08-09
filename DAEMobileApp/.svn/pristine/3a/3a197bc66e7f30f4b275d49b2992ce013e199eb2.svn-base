
/**
 * Stampa messaggi a secoda del livello di log impostato in fase di inizializzazione
 *
 * Usare la variabile `overrideLogLevel` per forzare il log su un certo livello
 *
 * Ogni log deve contenere una stringa e facoltativamente un oggetto
 * Vengono stampati tutti i messaggi con livello <= di `logLevel`
 *
 * @example
 * logger.error("errore", {status : 404});
 * logger.warn("warning");
 * logger.info("info");
 * logger.debug("debug", {data: "OK"});
 *
 * @memberof iLinkQuotation
 * @ngdoc Services
 * @name loggerService
 *
 **************************************************************************************/

angular.module(_SERVICES_).factory('loggerService', function () {

    var isCordova = !!window.cordova;
    var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
    var isFirefox = typeof InstallTrigger !== 'undefined';
    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
    var isIE = /*@cc_on!@*/false || !!document.documentMode;
    var isEdge = !isIE && !!window.StyleMedia;
    var isChrome = !!window.chrome && !!window.chrome.webstore;
    var isBlink = (isChrome || isOpera) && !!window.CSS;

    /**
     * Definizione dei livelli possibili
     * @memberof loggerService
     * @private
     * @type {{ERROR: number, WARN: number, INFO: number, DEBUG: number}}
     */
    var _levels = {
        "ERROR" : 1,    // Stampa solo gli errori
        "WARN"  : 2,    //    "   anche i warning
        "INFO"  : 3,    //    "   anche le info
        "DEBUG" : 4     //    "   anche i debug e i group
    };

    /**
     * Stampa la stringa e l'oggetto (se richiesto) con la giusta funzione
     * @param {String} func - Funzione/Prefisso di log da utilizzare
     * @param {String} msg - Testo del messaggio da stampare
     * @param {Object|null} obj - Eventuale oggetto da stampare
     * @private
     */
    var _print = function (func, msg, obj) {
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
            //line = line.lastIndexOf("?") != -1 ? line.substring(0, line.lastIndexOf("?")) : line;
            //line = line.replace(/^.*\/\/[^\/]+/, '');
        }
        else if(isFirefox){
            line = new Error().stack.split("\n")[3].split("@")[1];
            line = line.lastIndexOf("?") != -1 ? line.substring(0, line.lastIndexOf("?")) : line;
            line = line.replace(/^.*\/\/[^\/]+/, '');
        }

        if(isCordova){
            if(obj){
                try{
                    obj = JSON.stringify(obj);
                    console.log(func.toUpperCase() + ": " + msg + obj);
                }catch(e){
                    console.log(func.toUpperCase() + ": " + msg);
                }
            }
            else{
                console.log(func.toUpperCase() + ": " + msg);
            }
        }
        else {
            if(obj)
                console[func](line + ": " + msg, obj);
            else
                console[func](line + ": " + msg);
        }
    };

    return {

        /**
         * Livello di logging richiesto dall'applicazione
         * @memberof loggerService
         */
        logLevel: "DEBUG",

        /** Override del livello di logging richiesto
         * @memberof loggerService
         */
        overrideLogLevel: null,

        /** Inizializza il logger
         * @memberof loggerService
         * @param {String} requestedLogLevel - Livello di log da impostare
         */
        init: function (requestedLogLevel) {

            // Se ho richiesto un override del livello di log applico quello
            if (this.overrideLogLevel) {
                this.logLevel = this.overrideLogLevel;
            } else {
                // Se è stato richiesto un livello specifico che è tra quelli disponibili lo applico
                if (requestedLogLevel && _levels[requestedLogLevel.toUpperCase()]) {
                    this.logLevel = requestedLogLevel.toUpperCase();
                }
            }

            console.log("Servizio logger impostato a livello <" + this.logLevel + ">");
        },

        /** Stampa sempre gli errori
         * @memberof loggerService
         * @param {String} msg - Testo del log
         * @param {Object|null} obj - Eventuale oggetto da loggare
         */
        error: function (msg, obj) {
            _print("error", msg, obj);
        },

        /** Stampa anche i warning solo quando il livello è >= WARN
         * @memberof loggerService
         * @param {String} msg - Testo del log
         * @param {Object|null} obj - Eventuale oggetto da loggare
         */
        warn: function (msg, obj) {
            if (_levels[this.logLevel] >= _levels["WARN"]) {
                _print("warn", msg, obj);
            }
        },

        /** Stampa anche gli info solo quando il livello è >= INFO
         * @memberof loggerService
         * @param {String} msg - Testo del log
         * @param {Object|null} obj - Eventuale oggetto da loggare
         */
        info: function (msg, obj) {
            if (_levels[this.logLevel] >= _levels["INFO"]) {
                _print("info", msg, obj);
            }
        },

        /** Stampa anche i debug solo quando il livello è >= INFO
         * @memberof loggerService
         * @param {String} msg - Testo del log
         * @param {Object|null} obj - Eventuale oggetto da loggare
         */
        debug: function (msg, obj) {
            if (_levels[this.logLevel] >= _levels["DEBUG"]) {
                _print("debug", msg, obj);
            }
        },

        /** Cancella la console solo su browser
         * @memberof loggerService
         */
        clear: function () {
            if (typeof device === "undefined") {
                console.clear();
            }
        }
    };
});