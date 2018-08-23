"use strict";

define(['angular'], function (angular) {
    angular.module("service").factory("apiInterceptor", ["loggerService", "$q", "$cookies", function (logger, $q, $cookies) {
        var interceptor = {
            request: function (config) {
                config.headers = config.headers || {};
                var cookie = $cookies.get("principal");
                var principal = angular.fromJson(cookie);
                if(cookie){
                    config.headers.authorization = "Bearer {{TOKEN}}".replace('{{TOKEN}}', principal.token);
                }                
                // Se l'utente è loggato allora aggiungo l'header di autenticazione tra gli header
              /*  if (user.isLogged()) {
                    config.headers.authorization = "Bearer {{TOKEN}}".replace('{{TOKEN}}', user.getToken());
                }*/
                return config;
            },

            /*response: function (response) {
                return $q.resolve(response);
            },*/

            /*responseError: function (error) {
                logger.error("[apiInterceptor:responseError]", error);
                return $q.reject(error);
            }*/
        };

        return interceptor;
    }]);
});