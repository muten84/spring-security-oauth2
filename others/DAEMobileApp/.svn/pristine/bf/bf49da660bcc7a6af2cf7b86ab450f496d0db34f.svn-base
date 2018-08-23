appServices.factory("userService", [
    '$rootScope',
    'loggerService',
    'localStorageService',
    'loggerService',
    '$injector',
    '$q',
    '$cordovaNetwork',
    'PushWoosh',
    function ($rootScope, loggerService, storage, logger,
        $injector, $q, $cordovaNetwork, pushwoosh) {

        var logged = false,
            token = null,
            pushToken = null,
            privacyAccepted = false,
            availability = false,
            sessionTime = null,
            location = {},
            FRLocations = [],
            details = null;

        var user = {
            updateFRLocations: function (data) {
                FRLocations.push(data);
            },

            resetFRLocations: function () {
                FRLocations = [];
            },

            getFRLocations: function () {
                return FRLocations;
            },
            /**
             * Verifica se l'utente è loggato
             * @returns {boolean}
             */
            isLogged: function () {
                return logged;
            },

            /**
             * Verifica se l'utente Ha accettato la privacy
             * @returns {boolean}
             */
            isPrivacyAccepted: function () {
                return privacyAccepted;
            },

            /**
             * Imposta le coordinate correnti dell'utente
             * @param {String} lat - latitudine
             * @param {String} lng - longitudine
             */
            setCurrentPosition: function (lat, lng) {
                location.lat = lat;
                location.lng = lng;
            },

            /**
             * Ritorna la posizione dell'utente
             * @returns {object}
             */
            getCurrentPosition: function () {
                return location;
            },

            /**
             * Verifica se l'utente è disponibile
             * @returns {boolean}
             */
            isAvailable: function () {
                return availability;
            },

            /**
             * Verifica se l'utente è disponibile
             * @returns {boolean}
             */
            setAvailability: function (val) {
                availability = val;
            },
            /**
             * Verifica se l'utente non è ancora loggato
             * @returns {boolean}
             */
            isAnonymus: function () {
                return !user.isLogged();
            },

            /**
             * Ritorna il token ricevuto dal server per l'autenticazione
             * @returns {string}
             */
            getToken: function () {
                return token;
            },

            /**
             * Apre la sessione dell'utente impostando il token e il logged
             * @param {String} newToken - Token di autenticazione
             * @param {Number} time - Session time dell'autenticazione
             * @param {Boolean} available - Disponibilità dell'utente
             */
            startSession: function (newToken, time, available, privacyStatus) {
                logger.info("[userService:startSession]", arguments);

                token = newToken;
                sessionTime = time;
                availability = available;
                privacyAccepted = privacyStatus;
                logged = true;
            },

            /**
             * Ritorna se l'utente ha autorizzato il salvataggio credenziali
             */
            autologin: function () {
                return storage.get("AUTOLOGIN");
            },

            /**
             * PUSHWOOSH REGISTRATION
             */
            pushwooshRegistration: function () {
                var q = $q.defer();
                if (ionic.Platform.isWebView()) {
                    if ($cordovaNetwork.isOnline()) {
                        pushwoosh.register(function (result) {
                            logger.info('PushWoosh register ');
                            logger.info('PushWoosh result: ' + result.type);
                            if (result.type == 'registration') {
                                logger.info('token [ ' + JSON.stringify(result) + ' ]');
                                logger.info('device [ ' + result.device + ' ]');
                                // storage.add('device', result.device);
                                if (result.id) {
                                    //vm.credentials.deviceInfo.pushToken = result.id;
                                    storage.add('registrationId', result.id);
                                    q.resolve(result.id);
                                }
                                else {
                                    q.reject("Servizio momentaneamente non disponibile, riprova");
                                }
                            }
                        });
                    }
                    else {
                        q.reject("OFFLINE_ACTION_NOT_COMPLETED");
                    }
                }
                else {
                    //vm.credentials.deviceInfo.pushToken = "111111111111111";
                    q.resolve("111111111111111");
                }
                return q.promise;
            },

            /**
             * Effettua una nuova login utilizzando le credenziali memorizzate nel localStorage
             */
            restartSession: function () {
                logger.info("[userService:restartSession]");
                return user.pushwooshRegistration().then(
                    function (pushToken) {
                        // Recupero le ultime credenziali d'accesso dell'utente
                        var credentials = storage.get("USER");
                        // Se ho tutto per effettuare una nuova login procedo
                        if (credentials) {
                            credentials.deviceInfo.pushToken = pushToken;
                            return $injector.get("apiService").loginByHash(credentials).then(
                                function (loginResponse) {
                                    if (!loginResponse.error) {
                                        user.startSession(loginResponse.token, loginResponse.startSessionTime, loginResponse.available);
                                    }
                                    else {
                                        user.showErrorPopup(loginResponse.error);
                                        return $q.reject(loginResponse.error);
                                    }
                                } 
                            );
                        }
                        // Altrimenti
                        else {
                            return $q.reject();
                        }
                    },
                    function (error) {
                        return $q.reject(error);
                    }
                );

            },

        

            /**
             * Chiude la sessione dell'utente cancellando il token e impostandolo anonimo
             */
            closeSession: function () {
                token = null;
                sessionTime = null;
                logged = false;
                storage.remove("AUTOLOGIN");
                storage.remove("USER");
            },

            /**
             * Recupera le credenziali d'accesso dal localStorage
             */
            getUserCredentials: function () {
                return storage.get("USER");
            },

            /**
             * Imposta le credenziali d'accesso nel localStorage
             * @param {Object} credentials - Credenziali d'accesso utente
             * @param {Boolean} toHash - Se true la password va hashata prima di essere memorizzata
             */
            setUserCredentials: function (credentials, toHash) {
                var cred = _.clone(credentials);
                if (toHash) {
                    // Per "sicurezza" faccio girare lo SHA256 sulla password dell'utente prima di memorizzarla nel localStorage
                    cred.password = CryptoJS.SHA256(cred.password).toString();
                }
                storage.set("USER", cred);
                storage.set("AUTOLOGIN", true);
            },

            /**
             * Recupera i dettagli dell'utente loggato
             */
            getUser: function () {
                return angular.copy(details);
            },

            updateUser: function (profile) {
                details.civico = profile.civico;
                details.indirizzo.id = profile.indirizzo.id;
                details.indirizzo.name = profile.indirizzo.name;
                details.comuneResidenza.id = profile.comuneResidenza.id;
                details.comuneResidenza.nomeComune = profile.comuneResidenza.nomeComune;
                details.comuneResidenza.codiceIstat = profile.comuneResidenza.codiceIstat;
                details.comuneResidenza.cap = profile.comuneResidenza.cap;
                details.comuneResidenza.provincia.id = profile.comuneResidenza.provincia.id;
                details.comuneResidenza.provincia.nomeProvincia = profile.comuneResidenza.provincia.nomeProvincia;
                details.immagine.id = profile.immagine.id;
                details.immagine.data = profile.immagine.data;
                details.immagine.url = profile.immagine.url;
                details.numCellulare = profile.numCellulare;
                details.professione.id = profile.professione.id;
                details.professione.descrizione = profile.professione.descrizione;
                details.comuniCompetenza = profile.comuniCompetenza;
            },

            /**
             * Imposta i dati dell'utente loggato
             * @param {Object} userDetails - Response dell'API getuserDetails da memorizzare
             */
            setUser: function (userDetails) {
                details = userDetails;
                return details;
            },

            /**
             * Verifica se l'utente ha accettato i termini della privacy
             */
            privacyAccepted: function () {
                return false;
            }
        };

        return user;
    }
]);