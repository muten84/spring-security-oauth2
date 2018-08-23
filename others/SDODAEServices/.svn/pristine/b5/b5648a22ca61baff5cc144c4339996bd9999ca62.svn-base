'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function(angular, _, constats) {

    angular.module("service").factory('anagraficaService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce',
    function($http, logger, $q, $location, abstractSerivce) {

            var api = {};
            api.basePath = "anagraficheService";

            angular.extend(api, abstractSerivce);

            var _innerFilter = function(res, input, fieldName) {
                if (!input) {
                    return res.data;
                }
                var response = _.filter(res.data, function(o) {
                    /*debugger; */
                    /*return eval("o." + fieldName + ".indexOf('" + input + "') >= 0");*/
                    return o.descrizione.indexOf(input) >= 0;
                });
                return response;
            };

            api.getAllStato = function() {
                var options = this._prepareRequest("POST", "getAllStato");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getAllTipoManutenzione = function() {
                var options = this._prepareRequest("POST", "getAllTipoManutenzione");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };
            
            api.getAllTipoDisponibilita = function() {
                var options = this._prepareRequest("POST", "getAllTipoDisponibilita");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getAllProvince = function() {
                var options = this._prepareRequest("POST", "getAllProvince");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getAllProvinceByCompetence = function() {
                var options = this._prepareRequest("POST", "getAllProvinceByCompetence");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchComuniByFilter = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "searchComuniByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getAllTipoStruttura = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "getAllTipoStruttura");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchProfessioneByFilter = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "searchProfessioneByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchStradeByFilter = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "searchStradeByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchLocalitaByFilter = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "searchLocalitaByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            
            api.getAllCategorie = function() {
                var options = this._prepareRequest("POST", "getAllCategory");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchCategorie = function(filter) {
                /*debugger;*/
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "getAllCategory");
                options.data = filter;
                return $http(options).then(function(res) {
                    var response = _innerFilter(res, filter.name, "descrizione");
                    return $q.resolve(response);
                }, this._handleError);
            };

            api.getAllEnteCertificatore = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "getAllEnteCertificatore");
                /* options.data = filter;*/
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.reverseGeocoding = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "reverseGeocoding");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.geocodeAddress = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "geocodeAddress");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };



            api.searchStaticDataByType = function(type) {
                if (_.isUndefined(type)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("GET", "searchStaticDataByType/" + type);
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            return api;
    }]);

});
