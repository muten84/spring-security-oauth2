'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function (angular, _, constats) {

    angular.module("service").factory('configurationService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce',
        function ($http, logger, $q, $location, abstractSerivce) {

            var api = {};
            api.basePath = "configurationService";

            angular.extend(api, abstractSerivce);

            api.getAllConfiguration = function () {
                var options = this._prepareRequest("GET", "getAllConfiguration");
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.saveConfiguration = function (conf) {
                var options = this._prepareRequest("POST", "saveConfiguration");
                options.data = conf;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.saveDashboardPosition = function (positions) {
                var options = this._prepareRequest("POST", "saveDashboardPosition");
                options.data = positions;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getDashboardPosition = function () {
                var options = this._prepareRequest("GET", "getDashboardPosition");
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            return api;
        }
    ]);
});