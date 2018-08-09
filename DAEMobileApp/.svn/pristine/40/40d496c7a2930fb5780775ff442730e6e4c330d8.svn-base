appServices.factory('apiService', [
    '$rootScope',
    '$q',
    'loggerService',
    'deviceService',
    '$http',
    function ($rootScope, $q, logger, device, $http) {

        /**
         * Ritorno solo il data dell'oggetto HTTP
         * @param {Object} response
         * @returns {Object}
         */
        var _handleSuccess = function (response) {
            return $q.resolve(response.data);
        };

        /**
         * Ritorno l'oggetto d'errore così come me lo ha parsato l'interceptor
         * @param {Object} response
         * @returns {Object}
         */
        var _handleError = function (response) {
            return $q.reject(response);
        };

        /**
         * Funzione che inizializza alcuni parametri della request HTTP
         * @param {String} apiName - Nome dell'API da configurare
         * @returns {Object}
         * @private
         */
        var _prepareRequest = function (apiName) {
            var baseUrl = $rootScope.config.API.baseUrl;
            var options = {
                method: $rootScope.config.API[apiName].method,
                url: baseUrl + $rootScope.config.API[apiName].uri,
                headers: {
                    "Content-Type": $rootScope.config.API[apiName].contentType
                },
                timeout: $rootScope.config.API[apiName].timeout
            };
            return options;
        };

        var api = {

            /**
             * Recupera il file di lingua richeisto dal server
             * @param {string} langIsoCode - Codice di lingua richiesto
             */
            getTranslation: function (langIsoCode) {
                var request = $http({
                    method: $rootScope.config.API.getTranslation.method,
                    url: ($rootScope.config.API.getTranslation.uri + "?t=" + (new Date()).getTime())
                        .replace("{LANG}", langIsoCode),
                    headers: {
                        "Content-Type": $rootScope.config.API.getTranslation.contentType
                    },
                    timeout: $rootScope.config.API.getTranslation.timeout
                });
                return request.then(_handleSuccess, _handleError);
            },


            /**
             * Invia al server i dati per l'autenticazione e in caso di successo ritorna
             * il token di validazione delle chiamate successive
             * @param {Object} credentials - Credenziali per il login
             */
            login: function (credentials) {
                var options = _prepareRequest("authenticateUser");
                options.data = {
                    "username": credentials.username,
                    "password": credentials.password,
                    "deviceInfo": credentials.deviceInfo
                };

                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo per distruggere il token di autenticazione
             */
            logout: function () {
                var options = _prepareRequest("logout");
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Invia al server i dati per l'autenticazione e in caso di successo ritorna
             * il token di validazione delle chiamate successive
             * La password non è in chiaro ma hashata l'algoritmo SHA256
             * @param {Object} credentials - Credenziali per il login
             */
            loginByHash: function (credentials) {
                var options = _prepareRequest("authenticateUser");
                options.data = {
                    "username": credentials.username,
                    "passwordHash": credentials.password,
                    "deviceInfo": credentials.deviceInfo
                };

                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo per il reset della password dell'utente (First Responder)
             * @param {String} email - Email dell'utente
             */
            resetPassword: function (email) {
                var options = _prepareRequest("resetPassword");
                options.data = {
                    "emailAddress": email
                };

                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo per la modifica della password dell'utente (First Responder)
             * @param {Object} credentials - vecchia e nuova password
             */
            changePassword: function (credentials) {
                var options = _prepareRequest("changePassword");
                options.data = {
                    "oldPassword": credentials.oldPassword,
                    "newPassword": credentials.newPassword
                };

                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di censire un nuovo Dae
             */
            addNewDae: function (dae) {
                var options = _prepareRequest("addNewDae");
                options.data = dae;
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di segnalare e aggiornare un guasto al DAE
             */
            reportFault: function (damage) {
                var options = _prepareRequest("reportFault");
                options.data = damage;
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa dei DAE dislocati sul territorio
             */
            getAllDAE: function () {
                var options = _prepareRequest("getAllDAE");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa dei DAE dislocati
             * sul territorio priva di informazioni aggiuntive
             */
            getAllDAEForMap: function () {
                var options = _prepareRequest("getAllDAEForMap");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di ricerca i Dae mediante un filtro
             */
            getEventList: function () {
                var options = _prepareRequest("getEventList");
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa dei DAE dislocati sul territorio
             */
            searchDAEByFilter: function (location, pageSize, fromIndex) {
                var options = _prepareRequest("searchDAEByFilter");
                options.data = {
                    "location": {
                        "latitudine": location.lat,
                        "longitudine": location.lng
                    },
                    "pageSize": pageSize,
                    "fromIndex": fromIndex,
                    "statoValidazione": "VALIDATO",
                    "fetchRule": "APP_MOBILE"
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di ricerca i Dae mediante un filtro.
             * I DAE vengono filtrati passando una stringa geoJSON che indica il riquadro corrente della mappa
             */
            searchDAEByGeoJSON: function (geoJSON) {
                var options = _prepareRequest("searchDAEByFilter");
                options.data = {
                    "location": {
                        "srid": 4326,
                        "geoJSON": geoJSON
                    },
                    "fetchRule": "MINIMAL"
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa delle Province d'italia
             */
            getAllProvincie: function () {
                var options = _prepareRequest("getAllProvincie");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa delle Province della regione Emilia Romagna
             */
            getAllProvinceByCompetence: function () {
                var options = _prepareRequest("getAllProvinceByCompetence");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa degli stati
             */
            getAllStato: function () {
                var options = _prepareRequest("getAllStato");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista delle tipologie di manutenzione
             */
            getAllTipoManutenzione: function () {
                var options = _prepareRequest("getAllTipoManutenzione");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa delle categorie di First Responder
             */
            getAllCategorie: function () {
                var options = _prepareRequest("getAllCategorie");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa degli Enti Certificatori
             */
            getAllEnteCertificatore: function () {
                var options = _prepareRequest("getAllEnteCertificatore");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista completa delle Professioni
             */
            getAllProfessione: function () {
                var options = _prepareRequest("getAllProfessione");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce comune, provincia ed indirizzo più vicini alle coordinate passate
             */
            reverseGeocoding: function (lat, lng) {
                var options = _prepareRequest("reverseGeocoding");
                options.data = {
                    "latitudine": lat,
                    "longitudine": lng
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista dei comuni filtrati
             */
            searchComuniByFilter: function (nomeProvincia) {
                var options = _prepareRequest("searchComuniByFilter");
                options.data = {
                    "id": null,
                    "nomeComune": null,
                    "codiceIstat": null,
                    "nomeProvincia": nomeProvincia
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista delle strade filtrate
             */
            searchStradeByFilter: function (codiceIstat, query) {
                var options = _prepareRequest("searchStradeByFilter");
                options.data = {
                    "id": null,
                    "name": null,
                    "codiceIstatComune": codiceIstat,
                    "name": query
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che restituisce la lista delle tipologie di struttura
             */
            getAllTipoStruttura: function (codiceIstat) {
                var options = _prepareRequest("getAllTipoStruttura");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di ottenere le informazionoi relative al First Responder Loggato
             */
            getLoggedFirstResponderDetails: function () {
                var options = _prepareRequest("getLoggedFirstResponderDetails");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo per impostare l'orario di inizio e fine del Non disturbare
             */
            setDoNotDisturb: function (settings) {
                var options = _prepareRequest("setDoNotDisturb");
                options.data = {
                    "from": settings.from,
                    "to": settings.to
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Pulisce lo stato di non disturbare
             */
            removeDoNotDisturb: function () {
                var options = _prepareRequest("removeDoNotDisturb");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo per impostare la modalità Silenzioso
             */
            setSilent: function (settings) {
                var options = _prepareRequest("setSilent");
                options.data = {
                    "from": settings.from,
                    "to": settings.to
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Rimuove la modalità Silenzioso
             */
            removeSilent: function () {
                var options = _prepareRequest("removeSilent");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },


            /**
             * Metodo che consente di modificare la disponibilità di un First Responder
             */
            updateAvailabilityFR: function (data) {
                var options = _prepareRequest("updateAvailabilityFR");
                options.data = {
                    "available": data
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di registrare un nuovo First Responder
             */
            addNewFirstResponder: function (newFR) {
                var options = _prepareRequest("addNewFirstResponder");
                options.data = newFR;
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di registrare un nuovo First Responder
             */
            updateFirstResponder: function (profile) {
                var options = _prepareRequest("updateFirstResponder");
                options.data = profile;
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di aggiornare l'immagine del certificato un First Responder
             */
            updateImageCertificato: function (imageData) {
                var options = _prepareRequest("updateImageCertificato");
                options.data = {
                    "base64Image": imageData
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di aggiornare l'immagine profilo un First Responder
             */
            updateImageProfilo: function (imageData) {
                var options = _prepareRequest("updateImageProfilo");
                options.data = {
                    "base64Image": imageData
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di modificare la disponibilità di un First Responder
             */
            updateFRLocation: function (location) {
                var options = _prepareRequest("updateFRLocation");
                options.data = {
                    "latitudine": location.latitude,
                    "longitudine": location.longitude,
                    "distanza": location.distanza | location.distanceFromEvent,
                    "altitudine": location.longitude | ""
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di ottenere le info dell'evento
             */
            getEventById: function (eventId) {
                var options = _prepareRequest("getEventById");
                options.data = {
                    "eventId": eventId
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di verificare se un particolare First Responder può
             * intervenire per il soccorso ad un particolare evento
             */
            checkIntervention: function (eventId, currentPosition) {
                var options = _prepareRequest("checkIntervention");
                options.data = {
                    "event": {
                        "id": eventId
                    },
                    "location": {
                        "latitudine": currentPosition.lat,
                        "longitudine": currentPosition.lng,
                    }
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di ottenere la lista degli eventi gestiti
             * o di tutti gli eventi potenzialmente gestibili
             */
            getMyEvents: function () {
                var options = _prepareRequest("getMyEvents");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente ad un First Responder di rifiutare l'evento
             */
            rejectEvent: function (eventId, cartellino, coRiferimento) {
                var options = _prepareRequest("rejectEvent");
                options.data = {
                    "eventId": eventId,
                    "cartellino": cartellino,
                    "coRiferimento": coRiferimento
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente ad un First Responder di rifiutare l'evento
             */
            getTipologiaSegnalazioni: function () {
                var options = _prepareRequest("tipologiaSegnalazioni");
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di inviare le coordinate del terminale contestualmente alla chiamata al 118
             */
            sendPositionToCO: function (lat, lng, acc) {
                var options = _prepareRequest("sendPositionToCO");
                options.data = {
                    "latitudine": lat,
                    "longitudine": lng,
                    "accuratezza": acc
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di comunicare che un FR è arrivato nei pressi dell'emergenza
             */
            placeArrival: function (data) {
                var options = _prepareRequest("placeArrival");
                options.data = {
                    "eventId": data.eventId,
                    "coordinate": {
                        "latitudine": data.lat,
                        "longitudine": data.lng
                    }
                };
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di ottenere il disclaimer attuale
             */
            getPrivacy: function () {
                var options = _prepareRequest("getCurrentPrivacyAgreement");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

            /**
             * Metodo che consente di accettare il disclaimer
             */
            acceptPrivacy: function () {
                var options = _prepareRequest("acceptPrivacyAgreement");
                options.data = {};
                var request = $http(options);
                return request.then(_handleSuccess, _handleError);
            },

        };

        return api;
    }
]);