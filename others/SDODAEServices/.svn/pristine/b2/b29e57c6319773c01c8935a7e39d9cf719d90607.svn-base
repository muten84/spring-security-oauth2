'use strict';

define(
    ['angular', "moment", "lodash", "constants"],
    function (angular, moment, _, constants) {
        angular.module("controller").controller('GroupInsertCtrl', [
            "$scope", "loggerService", "$rootScope",
            "anagraficaService", "$state", 'notificationService',
            "validateService", "userService",
            "modalService", "$document", "authenticationService",
            "toastr",
            function ($scope, loggerService, $rootScope,
                anagraficaService, $state, notificationService,
                validateService, userService,
                modalService, $document, authenticationService,
                toastr) {

                $rootScope.$broadcast("main.changeTitle", {
                    title: "Gestione Gruppi",
                    icon: "fa fa-users"
                });

                angular.extend($scope, {
                    gruppo: {
                        province: [],
                        comuni: []
                    },
                    province: [],
                    comuni: [],
                    gridOptions: {
                        enableHorizontalScrollbar: 0,
                        enableVerticalScrollbar: 0,
                        enableRowSelection: true,
                        enableRowHeaderSelection: false,
                        multiSelect: false,
                        enableFullRowSelection: true,
                        enablePaginationControls: false,
                        modifierKeysToMultiSelect: false,
                        onRegisterApi: function (gridApi) {
                            //set gridApi on scope
                            $scope.gridApi = gridApi;
                            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                if (row.isSelected) {
                                    $scope.gruppo = angular.copy(row.entity);
                                }
                            });
                        },
                        columnDefs: [{
                            displayName: 'Gruppo',
                            name: 'nome'
                        }, {
                            displayName: 'Comuni',
                            name: 'comuni',
                            cellTemplate: "<div class='ui-grid-cell-contents'>{{grid.appScope.formatComuni(row.entity)}}</div>"
                        }, {
                            displayName: 'Province',
                            name: 'province',
                            cellTemplate: "<div class='ui-grid-cell-contents'>{{grid.appScope.formatProvince(row.entity)}}</div>"
                        }]
                    },
                    validate: validateService.validate,
                    formatProvince: function (gruppo) {
                        var value = "";
                        gruppo.province.forEach(function (item) {
                            value += item.nomeProvincia + ", ";
                        });
                        return value;
                    },
                    formatComuni: function (gruppo) {
                        var value = "";
                        if (gruppo.comuni) {
                            gruppo.comuni.forEach(function (item) {
                                value += item.nomeComune + ", ";
                            });
                        }
                        return value;
                    },
                    aggiungiProvincia: function () {
                        var prov = $scope.gruppo.provincia;
                        if (prov) {
                            //cerco nella lista la presenza di una provincia già esistente
                            var find = _.find($scope.gruppo.province, {
                                nomeProvincia: prov.nomeProvincia
                            });
                            //se ho selezionato una provincia dalla combo e non è presente la aggiungo
                            if (!find) {
                                $scope.gruppo.province.push({
                                    nomeProvincia: prov.nomeProvincia
                                });
                            }
                        }
                        //azzero la combo
                        $scope.gruppo.provincia = null;
                    },
                    aggiungiComune: function () {
                        var com = $scope.gruppo.comune;
                        if (!$scope.gruppo.comuni) {
                            $scope.gruppo.comuni = [];
                        }

                        if (com) {
                            //cerco nella lista la presenza di una provincia già esistente
                            var find = _.find($scope.gruppo.comuni, {
                                nomeComune: com.nomeComune
                            });
                            //se ho selezionato una provincia dalla combo e non è presente la aggiungo
                            if (!find) {
                                $scope.gruppo.comuni.push({
                                    nomeComune: com.nomeComune
                                });
                            }
                        }
                        //azzero la combo
                        $scope.gruppo.comune = null;
                    },
                    rimuoviProvincia: function (prov, index) {
                        _.pullAt($scope.gruppo.province, [index]);
                    },
                    rimuoviComune: function (com, index) {
                        _.pullAt($scope.gruppo.comuni, [index]);
                    },
                    pulisci: function () {

                        modalService.openYesNoModal({
                            title: "Attenzione",
                            text: "Tutti i dati sulla maschera verranno persi",
                            ok: function () {
                                $scope.gruppo = {
                                    province: []
                                };
                            },
                            cancel: function () {}
                        });

                    },
                    saveGruppo: function () {
                        if ($scope.gruppoForm.$invalid) {
                            /*debugger;*/
                            modalService.openModal("warningModal.html", "Attenzione", {
                                message: {
                                    title: "Verifica dati inseriti",
                                    text: "Attenzione verificare i dati della form"
                                }
                            });
                            return;
                        }

                        if ($scope.gruppo.comuni.length === 0 && $scope.gruppo.province.length === 0) {
                            modalService.openModal("warningModal.html", "Attenzione", {
                                message: {
                                    title: "Verifica dati inseriti",
                                    text: "Attenzione inserire almeno un Comune o una Provincia"
                                }
                            });
                            return;
                        }

                        userService.saveGruppo($scope.gruppo).then(function (response) {
                            $scope.gruppo = response;
                            toastr.success('Salvataggio dei dati avvenuto con successo. Accertarsi che il profilo appena configurato corrisponda alle specifiche richieste.', 'Salvataggio avvenuto');
                            $scope.reload();
                        });
                    },
                    reload: function () {
                        userService.getAllGruppi().then(function (result) {
                            $scope.gridOptions.data = result;
                        });
                    },
                    /*Combo Comuni */
                    refreshComune: function (input) {
                        if (input === undefined || input === null || !input || input.length < 3) {
                            $scope.comuni = [];
                            return [];
                        }

                        var filter = {
                            "nomeComune": input
                        };
                        return anagraficaService.searchComuniByFilter(filter).then(function (response) {
                            $scope.comuni = response;
                        });
                    }
                });

                $scope.reload();

                anagraficaService.getAllProvince().then(function (result) {
                    $scope.province = result;
                });

            }
        ]);
    });