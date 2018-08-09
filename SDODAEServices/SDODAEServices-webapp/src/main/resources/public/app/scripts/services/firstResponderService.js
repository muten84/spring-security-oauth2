'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function(angular, _, constats) {

    angular.module("service").factory('frService', ["$http", "loggerService", "$q",
        "$location", 'abstractSerivce', "toastr",
        function($http, logger, $q, $location, abstractSerivce, toastr) {
            var currentFrId;
            var api = {};
            api.basePath = "frservice";

            angular.extend(api, abstractSerivce);

            api.searchFirstResponderByFilter = function(frFilter) {
                if (_.isUndefined(frFilter)) {
                    frFilter = {};
                }
                var options = this._prepareRequest("POST", "searchFirstResponderByFilter");
                options.data = frFilter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.saveFirstResponder = function(fr) {
                /*debugger;*/
                if (_.isUndefined(fr)) {
                    return;
                }
                if (fr.activeProfile) {
                    fr.statoProfilo = "ATTIVO";
                } else {
                    fr.statoProfilo = "IN_ATTESA_DI_ATTIVAZIONE";
                }

                var options = this._prepareRequest("POST", "saveFirstResponder");
                options.data = fr;
                var parentPromise = $http(options).then(this._handleSuccess, this._handleError);
                return parentPromise.then(function(response) {
                    if (response.error) {
                        if (response.message) {
                            toastr.error(response.message, 'Utente non autorizzato');
                        } else {
                            toastr.error('Utente non autorizzato verificare le credenziali', 'Utente non autorizzato');
                        }
                        return $q.reject(response);
                    }
                    var res;
                    if (response.response === false) {
                        //non faccio nulla
                        res = response;
                    } else {
                        res = _fillProfileStatus(response);
                    }
                    return $q.resolve(res);
                });
            };

            api.setCurrentFr = function(frId) {
                currentFrId = frId;
            };

            api.getCurrentFr = function() {
                return currentFrId;
            };

            api.reloadFirstReponder = function(id) {
                var filter = {
                    id: id
                };
                return api.searchFirstResponderByFilter(filter).then(function(response) {
                    var res = _fillProfileStatus(response[0]);
                    return $q.resolve(res);
                });
            };

            api.getLoggedFirstResponderDetails = function() {
                var options = this._prepareRequest("POST", "getLoggedFirstResponderDetails");
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.deleteFR = function(fr) {
                var options = this._prepareRequest("POST", "deleteFR");
                options.data = fr;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            var _fillProfileStatus = function(fr) {
                fr.activeProfile = _.eq(_.lowerCase(fr.statoProfilo), "attivo");
                return fr;
            };

            return api;

        }
    ]);
});
