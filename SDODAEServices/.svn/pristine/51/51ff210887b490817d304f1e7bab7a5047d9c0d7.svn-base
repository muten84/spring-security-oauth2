'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(
    ['angular', "lodash", "constants"],
    function (angular, _, constats) {
        angular.module("service").factory('userService', [
            "$http", "loggerService", "$q", "$location", 'abstractSerivce', 'toastr',
            function ($http, logger, $q, $location, abstractSerivce, toastr) {

                var api = {
                    basePath: "userService",
                    setCurrentUserId: function (currentUserId) {
                        this.currentUserId = currentUserId;
                    },
                    getCurrentUserId: function () {
                        return this.currentUserId;
                    },
                    getAllRuoli: function () {
                        var options = this._prepareRequest("GET", "getAllRuoli");
                        return $http(options).then(this._handleSuccess, this._handleError);
                    },
                    getAllGruppi: function () {
                        var options = this._prepareRequest("GET", "getAllGruppi");
                        return $http(options).then(this._handleSuccess, this._handleError);
                    },
                    getLoggedUtenteDetails: function () {
                        var options = this._prepareRequest("GET", "getLoggedUtenteDetails");
                        return $http(options).then(this._handleSuccess, this._handleError);
                    },
                    searchUtenteByFilter: function (filter) {
                        if (_.isUndefined(filter)) {
                            return;
                        }
                        var options = this._prepareRequest("POST", "searchUtenteByFilter");
                        options.data = filter;
                        return $http(options).then(this._handleSuccess, this._handleError);
                    },
                    saveGruppo: function (gruppo) {
                        if (_.isUndefined(gruppo)) {
                            return;
                        }
                        var options = this._prepareRequest("POST", "saveGruppo");
                        options.data = gruppo;
                        return $http(options).then(this._handleSuccess, this._handleError);
                    },
                    saveUtente: function (user) {
                        if (_.isUndefined(user)) {
                            return;
                        }
                        var options = this._prepareRequest("POST", "saveUtente");
                        options.data = user;
                        return $http(options).then(function (response) {
                            if (response.data.error) {
                                if (response.data.message) {
                                    toastr.error(response.data.message, 'Utente non autorizzato');
                                } else {
                                    toastr.error('Utente non autorizzato verificare le credenziali', 'Utente non autorizzato');
                                }
                                return $q.reject(response);
                            } else {
                                return $q.resolve(response.data);
                            }
                        }, this._handleError);
                    },
                    saveUtenteProfilo: function (user) {
                        if (_.isUndefined(user)) {
                            return;
                        }
                        var options = this._prepareRequest("POST", "saveUtenteProfilo");
                        options.data = user;
                        return $http(options).then(this._handleSuccess, this._handleError);
                    },
                    changePassword: function (oldPassword, newPassword) {
                        if (_.isUndefined(oldPassword) || _.isUndefined(newPassword)) {
                            return;
                        }
                        var options = this._prepareRequest("POST", "changePassword");
                        options.data = {
                            oldPassword: oldPassword,
                            newPassword: newPassword
                        };
                        return $http(options)
                            .then(this._handleSuccess, this._handleError)
                            .then(function (response) {
                                //se il server ha restituiito un messaggio di errore specifico visualizzo quello                                
                                if (response && !response.response && response.message) {
                                    toastr.error(response.message, 'Errore');
                                    /*Luigi ho dovuto portare il reject nell'if perché prima lo faceva sempre e non
                                    veniva restituito il risultato al controller*/
                                    return $q.reject(response);
                                }
                                /*Luigi  se non c'è errore restituisci una promise al controller che verrà risolta
                                con la risposta senza errore*/
                                return $q.resolve(response);

                            });
                    }
                };

                angular.extend(api, abstractSerivce);

                return api;
            }
        ]);
    });