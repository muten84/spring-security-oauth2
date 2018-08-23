'use strict';

define(['angular', "moment", "lodash", "constants"], function(angular, moment, _, constants) {
    angular.module("controller").controller('ConfigurationCtrl', [
        "$scope", "loggerService", "$rootScope", "anagraficaService",
        "$state", 'configurationService', "modalService", "validateService",
        function($scope, loggerService, $rootScope,
            anagraficaService, $state, configurationService, modalService, validateService) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Modifica Configurazioni",
                icon: "fa fa-wrench"
            });

            $scope.configurations = [];

            $scope.saveConfiguration = function(conf, index) {
                configurationService.saveConfiguration(conf).then(function(value) {
                    $scope.configurations[index] = value;
                });
            };

            $scope.pulisci = function(index) {
                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Tutti i dati sulla maschera verranno persi",
                    ok: function() {
                        $scope.configurations[index] = angular.copy($scope.original[index]);
                    },
                    cancel: function() {}
                });
            };

            configurationService.getAllConfiguration().then(function(value) {
                $scope.configurations = value;
                $scope.original = angular.copy(value);
            });
        }
    ]);
});
