'use strict';

define(['angular', "lodash", "constants", "moment"], function(angular, _, constants, moment) {

    angular.module("service").factory('abstractSerivce', [
        "$http", "loggerService", "$q", "$location",
        "$window", "toastr", "$indexedDB",
        function($http, logger, $q, $location,
            $window, toastr, $indexedDB) {

            var timeout = constants.responseTimeoutinMillis || 10000;

            var api = {};

            /**
             * Restituisce solo il data dell'oggetto HTTP
             *
             * @param {Object} response
             *                
             * @returns {Object}
             */
            api._handleSuccess = function(response) {
                /*toastr.success('', 'OK');*/
                logger.debug("success : " + response.data);
                return $q.resolve(response.data);
            };
            /**
             * Restituisce l'oggetto d'errore così come me lo ha parsato
             * l'interceptor
             *
             * @param {Object} response
             * @returns {Object}
             */
            api._handleError = function(response) {
                //se il server ha restituiito un messaggio di errore specifico visualizzo quello
                if (response.data && response.data.message) {
                    toastr.error(response.data.message, 'Errore');
                } else {
                    //altrimenti visualizzo un messaggio generico
                    toastr.error('Errore imprevisto mentre si processava la richiesta lato server.', 'Errore imprevisto');
                }
                logger.debug("error : " + response);
                /*(response.data.error, 'Error');*/
                return $q.reject(response);
            };

            api._getUrlBase = function() {
                var protocol = $location.protocol();
                var host = $location.host();
                var port = $location.port();
                var contextApp = $window.location.pathname.split("/")[1];
                if (constants.devMode) {
                    contextApp = constants.devContextApp || $window.location.pathname.split("/")[1];
                }
                var startPath = "";
                if (port == "80" || port == "443") {
                    startPath = protocol + "://" + host + "/";
                } else {
                    startPath = protocol + "://" + host + ":" + port + "/";
                }
                return startPath + contextApp;
            };

            api._getUrlService = function() {
                return this._getUrlBase() + "/" + constants.restBasePath + "/" + this.basePath + "/";
            };

            api._prepareRequest = function(method, op) {
                var options = {
                    method: method,
                    url: this._getUrlService() + op,
                    headers: {
                        "Content-Type": "application/json"
                    },
                    timeout: timeout
                };
                return options;
            };
            // see https://github.com/bramski/angular-indexedDB
            api.loadCached = function(options) {
                return $q(function(resolve, reject) {
                    try {
                        $indexedDB.openStore(constants.storeNameService, function(store) {
                            //cerco nello store la presenza di dati precedentemente caricati
                            store.find(options.url).then(function(obj) {
                                var start = moment(obj.time);
                                var end = moment();
                                //se i dati caricati risalgono a 2 ore fa li ricarico dal server
                                if (end.diff(start, 'minutes', true) > constants.maxMinutesCache) {
                                    loadAndCache(options, resolve, reject);
                                } else {
                                    //altrimenti restituisco i dati in cache
                                    resolve({
                                        data: obj.data
                                    });
                                }
                            }, function() {
                                loadAndCache(options, resolve, reject);
                            });
                        });
                    } catch (e) {
                        loadAndCache(options, resolve, reject);
                    }
                });
            };

            function loadAndCache(options, resolve, reject) {
                //carico i dati dal server
                $http(options).then(function(result) {
                    try {
                        $indexedDB.openStore(constants.storeNameService, function(store) {
                            store.upsert({
                                "url": options.url,
                                "data": result.data,
                                "time": new Date()
                            }).then(function(e) {
                                resolve(result);
                            });
                        });
                    } catch (e) {
                        resolve(result);
                    }
                }, function(err) {
                    reject(err);
                });
            }

            return api;
        }
    ]);

});
