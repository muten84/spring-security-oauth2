'use strict';

define(['angular', "moment", "constants", "lodash"], function(angular, moment, constants, _) {
    angular.module("controller").controller('DaeFaultCtrl', ["$scope", "$filter", "daeService",
        "anagraficaService", "loggerService", "$rootScope", "alertService", "modalService",
        "currentDae", "$document", "validateService", "toastr",
        function($scope, $filter, daeService, anagraficaService,
            loggerService, $rootScope, alertService, modalService,
            currentDae, $document, validateService, toastr) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Gestione Segnalazioni Problemi DAE",
                icon: "fa fa-exclamation-triangle"
            });

            angular.extend($scope, {
                options: {
                    /*  customClass: getDayClass,*/
                    minDate: new Date(0),
                    showWeeks: false
                },
                statiGuasto: [],
                tipologieSegnalazione: [],
                validate: validateService.validate,
                checkChildren: function(id) {
                    var invalids = $document.find('#' + id + ' .ng-invalid');
                    if (invalids && !_.isEmpty(invalids)) {
                        return false;
                    } else {
                        return true;
                    }
                },
                formatDate: function(date) {
                    return moment(date).format('DD/MM/YYYY HH:mm');
                },
                setDae: function(dae) {
                    if (!dae.guasti) {
                        dae.guasti = [];
                    }
                    ordinaGuasti(dae);
                    $scope.dae = dae;
                },
                aggiungiGuasto: function() {
                    var found = false;
                    //controllo se sul dae ci sono altri guasti non chiusi, nel caso mostro una popup
                    $scope.dae.guasti.forEach(function(el) {
                        if (el.statoAttuale !== 'CHIUSA') {
                            found = true;
                        }
                    });
                    if (found) {
                        //se ne trovo almeno una chiusa visualizzo una popup
                        modalService.openYesNoModal({
                            title: "Attenzione",
                            text: "Sono già presenti delle segnalazioni attive sul DAE. <br/> Vuoi aggiungere lo stesso una nuova segnalazione?",
                            ok: function() {
                                $scope.aggiungiGuastoProcedi();
                            },
                            cancel: function() {
                                // console.log("cancellato");
                            }
                        });
                        return;
                    } else {
                        $scope.aggiungiGuastoProcedi();
                    }
                },
                aggiungiGuastoProcedi: function() {
                    $scope.dae.guasti.push({
                        dae: {
                            id: $scope.dae.id
                        },
                        dataSegnalazione: new Date(),
                        statoAttuale: 'APERTA'
                    });
                    ordinaGuasti($scope.dae);
                },
                salvaGuasto: function(index) {
                    //prendo il dae dalla maschera e invio i dati al server
                    $scope.dae.guasti[index].dae = {
                        id: $scope.dae.id
                    };
                    daeService.reportFault($scope.dae.guasti[index])
                        .then(function(result) {
                            $scope.dae.guasti[index] = result;
                            ordinaGuasti($scope.dae);
                            toastr.success('Salvataggio dei dati avvenuto con successo', 'Salvataggio avvenuto');
                        });
                }
            });

            function ordinaGuasti(dae) {
                dae.guasti = _.orderBy(dae.guasti, ["dataSegnalazione"], ['desc']);
                dae.guasti.forEach(function(g) {
                    g.trace = _.orderBy(g.trace, ["dataModifica"], ['desc']);
                });

                return dae;
            }
            //Carico il DAE dal db e riempio la maschera
            if (!_.isUndefined(currentDae)) {
                $scope.setDae(currentDae[0]);
            }

            anagraficaService.searchStaticDataByType('STATO_GUASTI').then(function(resp) {
                $scope.statiGuasto = resp;
            });
            anagraficaService.searchStaticDataByType('TIPOLOGIA_SEGNALAZIONI').then(function(resp) {
                $scope.tipologieSegnalazione = resp;
            });

        }
    ]);
});
