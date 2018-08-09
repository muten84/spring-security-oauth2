'use strict';

define(['angular', "lodash", "constants"], function (angular, _, constats) {

    angular.module("service").factory('stateService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce',
        "$window",
        function ($http, logger, $q, $location, abstractSerivce, $window) {
            var api = {};

            api.saveState = function (key, state) {
                if (!state || _.isUndefined(state)) {
                    return;
                }
                var json = angular.toJson(state);
                $window.sessionStorage.setItem(key, json);
            };

            api.getState = function (key) {
                var json = $window.sessionStorage.getItem(key);
                if (!json || _.isUndefined(json)) {
                    return undefined;
                }
                var obj = angular.fromJson(json);
                return obj;
            };

            api.resetState = function (key, resetValue) {
                if (_.isUndefined(resetValue)) {
                    $window.sessionStorage.setItem(key, "{}");
                    return;
                }
                var json = angular.toJson(resetValue);
                $window.sessionStorage.setItem(key, json);
            };

            api.removeState = function (key) {
                $window.sessionStorage.removeItem(key);
            };

            return api;
        }
    ]);
});