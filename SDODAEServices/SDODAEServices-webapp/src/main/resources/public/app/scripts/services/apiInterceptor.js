"use strict";
//TODO: attivare i taost message
define(['angular'], function (angular) {
    angular.module("service").factory("apiInterceptor", ["loggerService", "$q", "$window", "$rootScope", "$injector",
        function (logger, $q, $window, $rootScope, $injector) {
            var interceptor = {
                request: function (config) {
                    config.headers = config.headers || {};
                    var token = $window.sessionStorage.token;
                    if (token) {
                        config.headers.authorization = "Bearer {{TOKEN}}".replace('{{TOKEN}}', token);
                    }
                    // Se l'utente è loggato allora aggiungo l'header di autenticazione tra gli header
                    /*  if (user.isLogged()) {
                          config.headers.authorization = "Bearer {{TOKEN}}".replace('{{TOKEN}}', user.getToken());
                      }*/
                    return config;
                },

                response: function (response) {
                    /*toastr.success('Esito operazione', 'operazione effettuata con successo');*/
                    return $q.resolve(response);
                },

                responseError: function (error) {
                    var $state = $injector.get('$state');
                    var toastr = $injector.get('toastr');
                    //   if (error.status === 500) {
                    //       $rootScope.$broadcast('application.error');
                    //    }

                    if (error.status === 401 && $state.current.name != "login") {
                        $state.transitionTo("login");
                    }
                    else if (error.status === 401) {
                        toastr.error('Utente non autenticato verificare le credenziali', 'Utente non autorizzato');
                    }
                    logger.error("[apiInterceptor:responseError]", error);
                    return $q.reject(error);
                }
            };

            return interceptor;
        }]);
});
