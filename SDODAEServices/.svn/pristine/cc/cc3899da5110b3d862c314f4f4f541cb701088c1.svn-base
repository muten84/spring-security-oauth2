'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function (angular, _, constats) {

    angular.module("service").factory('daeService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce', 'toastr',
        function ($http, logger, $q, $location, abstractSerivce, toastr) {

            var api = {};
            api.basePath = "daeService";

            angular.extend(api, abstractSerivce);

            api.setCurrentDaeId = function (currentDaeId) {
                this.currentFrId = currentDaeId;
            };

            api.getCurrentDaeId = function () {
                return this.currentFrId;
            };

            api.getAllDAE = function () {
                var options = this._prepareRequest("POST", "getAllDAE");
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getAllDAEForMap = function () {
                var options = this._prepareRequest("POST", "getAllDAEForMap");
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchDAEByFilter = function (filter) {
                var options = this._prepareRequest("POST", "searchDAEByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.saveDae = function (dae) {
                var options = this._prepareRequest("POST", "saveDae");
                options.data = dae;
                return $http(options).then(function (response) {
                    if (response.data.error) {
                        if (response.data.message) {
                            toastr.error(response.data.message, 'Impossibile Salvare');
                        } else {
                            toastr.error('Utente non autorizzato verificare le credenziali', 'Utente non autorizzato');
                        }
                        return $q.reject(response);
                    } else {
                        return $q.resolve(response.data);
                    }
                }, this._handleError);
            };

            api.saveDisponibilita = function (disp) {
                var options = this._prepareRequest("POST", "saveDisponibilita");
                options.data = disp;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.deleteDisponibilita = function (disp) {
                var options = this._prepareRequest("POST", "deleteDisponibilita");
                options.data = disp;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.reportFault = function (fault) {
                var options = this._prepareRequest("POST", "reportFault");
                options.data = fault;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.deleteDAE = function (dae) {
                var options = this._prepareRequest("POST", "deleteDAE");
                options.data = dae;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getProgrammaHistory = function (id) {
                var options = this._prepareRequest("GET", "getProgrammaHistory/" + id);
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            return api;
        }
    ]);

});