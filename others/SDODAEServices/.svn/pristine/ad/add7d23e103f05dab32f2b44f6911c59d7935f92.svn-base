'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function(angular, _, constats) {

    angular.module("service").factory('chartService', [
        "$http", "loggerService", "$q", "$location", 'abstractSerivce',
        function($http, logger, $q, $location, abstractSerivce) {

            var api = {};
            api.basePath = "charts";

            angular.extend(api, abstractSerivce);

            api.fetchFrSubscriptions = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "frSubscriptions");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "frSubscriptions");
                    return this.loadCached(options).then(this._chandleSuccess, this._handleError);
                }
            };

            api.fetchDaeSubscriptions = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "daeSubscriptions");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "daeSubscriptions");
                    return this.loadCached(options).then(this._handleSuccess, this._handleError);
                }
            };

            api.fetchDaeActivations = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "daeActivationsByProvince");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "daeActivationsByProvince");
                    return this.loadCached(options).then(this._handleSuccess, this._handleError);
                }
            };

            api.fetchDaeActivationsByDay = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "daeActivationsByDay");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "daeActivationsByDay");
                    return this.loadCached(options).then(this._handleSuccess, this._handleError);
                }
            };

            api.fetchFrActivationsByType = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "frActivationsByType");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "frActivationsByType");
                    return this.loadCached(options).then(this._handleSuccess, this._handleError);
                }
            };

            api.fetchDaeValidation = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "daeValidation");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "daeValidation");
                    return this.loadCached(options).then(this._handleSuccess, this._handleError);
                }
            };

            api.fetchDaeNumbers = function(filter) {
                var options;
                if (filter) {
                    options = this._prepareRequest("POST", "fetchDaeNumbers");
                    options.data = filter;
                    return $http(options).then(this._chandleSuccess, this._handleError);
                } else {
                    options = this._prepareRequest("GET", "fetchDaeNumbers");
                    return this.loadCached(options).then(this._handleSuccess, this._handleError);
                }
            };
            

            return api;
        }
    ]);

});
