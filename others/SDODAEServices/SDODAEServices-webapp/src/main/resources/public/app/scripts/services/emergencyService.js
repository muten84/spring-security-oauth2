'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function (angular, _, constats) {

    angular.module("service").factory('emergencyService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce', function ($http, logger, $q, $location, abstractSerivce) {

        var api = {};
        api.basePath = "emergencyService";

        angular.extend(api, abstractSerivce);

        api.setCurrentEventId = function (currentEventId) {
            this.currentFrId = currentEventId;
        };

        api.getCurrentEventId = function () {
            return this.currentFrId;
        };

        api.searchEventByFilter = function (filter) {
            var options = this._prepareRequest("POST", "searchEventByFilter");
            options.data = filter;
            return $http(options).then(this._handleSuccess, this._handleError);
        };

        api.saveEvent = function (event) {
            var options = this._prepareRequest("POST", "saveEvent");
            options.data = event;
            return $http(options).then(this._handleSuccess, this._handleError);
        };

        api.searchNotifiche = function (eventId) {
            var options = this._prepareRequest("POST", "searchNotifiche");
            options.data = {
                event: eventId
            };
            return $http(options).then(this._handleSuccess, this._handleError);
        };



        return api;
    }]);

});