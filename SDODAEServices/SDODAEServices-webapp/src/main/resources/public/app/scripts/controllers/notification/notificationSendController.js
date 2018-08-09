'use strict';

define(['angular', "moment", "lodash", "constants"], function(angular, moment, _, constants) {
    angular.module("controller").controller('NotificationSendCtrl', [
        "$scope", "loggerService", "$rootScope",
        "anagraficaService", "$state", 'notificationService',
        "modalService", "validateService", "toastr",
        function($scope, loggerService, $rootScope,
            anagraficaService, $state, notificationService,
            modalService, validateService, toastr) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Invio Notifiche",
                icon: "fa fa-paper-plane-o"
            });

            angular.extend($scope, {
                filter: {},
                gruppi: ['TUTTI', 'DISPONIBILI', 'PROVINCIA', 'CATEGORIA']
            });

            $scope.pulisci = function() {
                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Tutti i dati sulla maschera verranno persi",
                    ok: function() {
                        $scope.filter = {};
                    },
                    cancel: function() {}
                });
            };
            //riempio la lista delle province
            anagraficaService.getAllProvince().then(function(result) {
                $scope.province = result;
            });
            //riempio tutte le categorie
            anagraficaService.getAllCategorie().then(function(result) {
                $scope.categorie = result;
            });
            //metodo per validare
            $scope.validate = validateService.validate;

            $scope.validateCombo = function(value, combo) {
                if (combo === $scope.filter.gruppo) {
                    //valido solo se ho selezionato il tipo nell'altra combo
                    return validateService.validate(value);
                } else {
                    return true;
                }
            };

            $scope.validateGruppo = function(value) {
                $scope.filter.provincia = null;
                $scope.filter.categoria = null;
                return validateService.validate(value);
            };

            $scope.$watch("filter.gruppo", function(newVal, oldVal) {
                $scope.filter.provincia = undefined;
                $scope.filter.categoria = undefined;
            }, true);

            $scope.send = function() {
                if (!$scope.notificationForm.$invalid) {
                    // creo un filtro per effettuar ela ricerca dei FR,
                    // lo passo al server insieme al messaggio da inviare agli FR filtrati
                    var filter = {};
                    if ($scope.filter.gruppo === 'TUTTI') {
                        // non metto nulla nel filtro per inviare a tutti
                    } else if ($scope.filter.gruppo === 'DISPONIBILI') {
                        filter.isAvailable = true;
                    } else if ($scope.filter.gruppo === 'PROVINCIA') {
                        filter.province = $scope.filter.provincia;
                    } else if ($scope.filter.gruppo === 'CATEGORIA') {
                        filter.categoriaDescription = $scope.filter.categoria;
                    }

                    notificationService.sendNotificationByFilter($scope.filter.messaggio, filter).then(function(result) {
                        $scope.pulisci();
                        toastr.success('Messaggio inviato ', 'Messaggio inviato ');

                    });
                } else {
                    /*debugger;*/
                    modalService.openModal("warningModal.html", "Attenzione", {
                        message: {
                            title: "Verifica dati inseriti",
                            text: "Attenzione verificare i dati della form"
                        }
                    });
                }
            };
        }
    ]);
});
