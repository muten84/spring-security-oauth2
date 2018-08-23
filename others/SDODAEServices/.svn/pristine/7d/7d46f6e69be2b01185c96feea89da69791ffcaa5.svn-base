'use strict';
/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants"], function(angular, _, constats) {
    angular.module("service").factory('authenticationService', 
    ["$http", "loggerService", "$q", "$location", 'abstractSerivce', "$injector",
        function($http, logger, $q, $location, abstractSerivce, $injector) {
            var api = {};
            angular.extend(api, abstractSerivce);
            api.basePath = "authenticationService";
            api._handleAuthentication = function(response) {
                var toastr = $injector.get('toastr');
                if (response.data.error) {
                    if (response.data.message) {
                        toastr.error(response.data.message, 'Utente non autenticato');
                    } else {
                        toastr.error('Utente non autenticato verificare le credenziali', 'Utente non autenticato');
                    }
                }
                logger.debug("success : " + response.data);
                return $q.resolve(response.data);
            };
            api.getBaseUrl = function() {
                return this._getUrlBase();
            };
            api.isAuthenticated = function() {
                var options = this._prepareRequest("GET", "isAuthenticated");
                return $http(options).then(this._handleSuccess);
            };
            api.authenticateUser = function(credential) {
                var options = this._prepareRequest("POST", "authenticateUserPortal");
                options.data = {
                    "username": credential.username,
                    "password": credential.password
                };
                return $http(options).then(this._handleAuthentication, this._handleError);
            };
            api.logout = function() {
                var options = this._prepareRequest("GET", "logoutPortal");
                return $http(options).then(this._handleSuccess, this._handleError);
            };
            api.resetPassword = function(email) {
                var options = this._prepareRequest("POST", "resetPassword");
                options.data = {
                    "emailAddress": email
                };
                return $http(options).then(this._handleSuccess, this._handleError);
            };
            return api;
        }
    ]);
});
