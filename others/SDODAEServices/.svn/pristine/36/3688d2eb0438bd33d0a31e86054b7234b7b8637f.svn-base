'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function(angular, _, constats) {

    angular.module("service").factory('mailService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce',
        function($http, logger, $q, $location, abstractSerivce) {

            var api = {};
            api.basePath = "mailService";

            angular.extend(api, abstractSerivce);

            api.getAllMailTemplate = function() {

                var options = this._prepareRequest("POST", "getAllMailTemplate");
                options.data = {};
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.saveMailTemplate = function(template) {
                if (_.isUndefined(template)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "saveMailTemplate");
                options.data = template;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.sendMail = function(mail) {
                if (_.isUndefined(mail)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "sendMail");
                options.data = mail;
                // la mail deve essere strutturata secondo il bean:
                // it.eng.areas.ems.sdodaeservices.rest.model.MailTest
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            api.searchMailByFilter = function(filter) {
                if (_.isUndefined(filter)) {
                    return $q.reject({});
                }
                var options = this._prepareRequest("POST", "searchMailByFilter");
                options.data = filter;
                return $http(options).then(this._handleSuccess, this._handleError);
            };

            return api;
        }
    ]);
});
