'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function(angular, _, constats) {

    angular.module("service").factory('notificationService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce',
        function($http, logger, $q, $location, abstractSerivce) {

            var api = {};
            api.basePath = "notificationService";

            angular.extend(api, abstractSerivce);

            api.searchNotificationByFilter = function(filter) {
                var options = this._prepareRequest("POST", "searchNotificationByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.sendNotificationToFirstResponderList = function(message, responders) {
                var options = this._prepareRequest("POST", "sendNotificationToFirstResponderList");
                options.data = {
                    message: message,
                    firstResponders: responders
                };
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.sendNotificationByFilter = function(message, filter) {
                var options = this._prepareRequest("POST", "sendNotificationByFilter");
                options.data = {
                    message: message,
                    filter: filter
                };
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.deleteNotification = function(id) {
                var options = this._prepareRequest("POST", "deleteNotification/" + id);
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.getNotificationFR = function(id){
                var options = this._prepareRequest("POST", "getNotificationFR/" + id);
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            return api;
        }
    ]);
});
