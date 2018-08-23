'use strict';
/*
questa direttiva può essere utile quando si vuole gestire un comportamento generico su tutte le ui-select*/
define(["angular"], function(angular) {
    angular.module('directive').directive('responsabile', ["anagraficaService", function(anagraficaService) {
        return {
            restrict: 'E',
            scope: {
                model: "=",
                open: "=",
                prefix: "@"
            },
            templateUrl: 'partials/dae/responsabile.html',
            controller: function($scope) {
                $scope.validate = $scope.$parent.validate;
                $scope.checkChildren = $scope.$parent.checkChildren;
                $scope.comuni = [];

                $scope.refreshComune = function(input) {
                    if (input === undefined || input === null || !input) return [];
                    if (input.length < 3) return [];

                    var filter = {
                        "nomeComune": input
                    };
                    return anagraficaService.searchComuniByFilter(filter).then(function(response) {
                        $scope.comuni = response;
                    });
                };
            }
        };
    }]);
});
