"use strict";

define(["angular"], function (angular) {

    angular.module('filter').filter('docstate', function () {
        var map = {
            "CREATED" : "CREATO",
            "SENT" : "INVIATO",
            "OPENED":  "APERTO"
        };
        
        return function (input) {
            return map[input];  
        };
    });

});