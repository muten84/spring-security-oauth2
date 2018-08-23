
appServices.factory("apiInterceptor", [
    '$rootScope',
    '$q',
    'loggerService',
    'userService',
    '$translate',
    '$injector',
    '$templateCache',
    function ($rootScope, $q, logger, user, $translate, $injector, $templateCache) {

        var interceptor = {

            // Interceptor per la request di ogni chiamata alle API
            request: function (config) {
                config.headers = config.headers || {};

                // Se l'utente è loggato allora aggiungo l'header di autenticazione tra gli header
                if(user.isLogged()){
                    config.headers.authorization = "Bearer {{TOKEN}}".replace('{{TOKEN}}', user.getToken());
                }
                return config;
            },

            /**
             * Interceptor per ogni chiamata alle API andata a buon fine
             * @param {Object} response - Response della chiamata
             * @returns {Object} data - Contenuto della response
             */
            response: function(response){
                return $q.resolve(response);
            },

            /**
             * Interceptor per ogni chiamata alle API NON andata a buon fine
             * @param {Object} response - Response della chiamata
             * @returns {Object} data - Contenuto della response
             */
            responseError: function(response){
                logger.error("[apiInterceptor:responseError]", response);

                var error = {
                    status : -1,
                    message : ""
                };

                if (!(window.navigator && window.navigator.onLine)) {
                    error.message = $translate.instant("OFFLINE_ACTION_NOT_COMPLETED");
                    return $q.reject(error);

                } else if (response.status) {
                    error.status = response.status;

                    switch (response.status) {
                        case 401 :

                            // Se sono loggato allora vuol dire che il mio token è scaduto
                            // Quindi se ho l'autologin abilitato tento una login silente
                            // Distruggendo prima la sessione attuale
                            if(user.isLogged() && user.autologin()){
                                user.closeSession();

                                // Ora che ho distrutto la sessione posso tentare una login silente
                                // Nel caso dovessi ricevere un altro 401 (per esempio se l'utente ha cambiato la password)
                                // non rientrerei in questo if perché user.isLogged() ritornerebbe false
                                // portandomi alla login con errore LOGIN_UNAUTHORIZED

                                return user.restartSession().then(
                                    function(){
                                        // Se la login silente è andata a buon fine rieseguo l'ultima chiamata al server
                                        // ritornando la promise
                                        return $injector.get("$http")(response.config);
                                    },
                                    function(){
                                        // Se la login silente non è andata a buon fine allora riporto alla login
                                        if(!$rootScope.DEV){
                                            $injector.get("$state").go("app.login");
                                        }
                                        error.message = $translate.instant("LOGIN_UNAUTHORIZED");
                                        return $q.reject(error);
                                    }
                                );
                            }
                            // Altrimenti vado alla login mostrando un errore
                            // Se sono già sulla login non ci sarà il cambio di stato
                            else{
                                if(!$rootScope.DEV){
                                    $injector.get("$state").go("app.login");
                                }
                                error.message = $translate.instant("LOGIN_UNAUTHORIZED");
                                return $q.reject(error);
                            }
                            break;

                        case undefined :
                        case -1:
                        case 0 :
                        case 404 :
                            // error.message = $translate.instant("SYSTEM_NOT_AVAILABLE");
                            error.message = "Servizio momentaneamente non disponibile";
                            return $q.reject(error);
                        case 500 :
                        default :
                            //error.message = "Error (" + response.status + ")";
                            // error.message = $translate.instant("SYSTEM_NOT_AVAILABLE");
                            error.message = "Sistemi momentaneamente non disponibili";
                            return $q.reject(error);
                    }
                }
                else {
                    return $q.reject(error);
                }
            }
        };

        return interceptor;
    }
]);
