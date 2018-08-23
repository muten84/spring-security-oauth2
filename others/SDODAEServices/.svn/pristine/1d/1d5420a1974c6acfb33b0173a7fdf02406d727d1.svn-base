'use strict';

define(['angular', "moment", "lodash"], function (angular, moment, _) {
    angular.module("controller").controller('FrUpdateCtrl', [
        "$scope", "$filter", "daeService",
        "anagraficaService", "frService", "loggerService",
        "$rootScope", "alertService", "currentFr",
        "modalService", "$document", "toastr",
        "$state",
        function ($scope, $filter, daeService,
            anagraficaService, frService, loggerService,
            $rootScope, alertService, currentFr,
            modalService, $document, toastr,
            $state) {

            $scope.model = {};

            /*$scope.model.activeProfile = false;*/

            $rootScope.$broadcast("main.changeTitle", {
                title: "Gestione Dati DAE Responder",
                icon: "fa fa-user-md"
            });
            /*
                        $scope.$watch('isActive', function(newVal,oldVal) {
                            debugger;

                        });
            */
            /*  $scope.$watch('model.statoProfilo', function(newVal, oldVal){
                  debugger;
                  $scope.isActive = _.eq(_.lowerCase(newVal), "attivo");
              });*/
            $scope.formatDate = function (date) {
                return moment(date).format('HH:mm DD/MM/YYYY');
            };
            $scope.struttura = {
                disabilita: false
            };

            $scope.setModel = function (el) {
                $scope.model = el;
                $scope.model.comuniCompetenza = _.orderBy($scope.model.comuniCompetenza, ['nomeComune'], ['asc']);
            };

            if (!_.isUndefined(currentFr)) {
                $scope.setModel(currentFr);
                if (!_.isUndefined($scope.model.certificatoFr) && !_.isNull($scope.model.certificatoFr)) {
                    $scope.rilasciatoDa = {
                        descrizione: $scope.model.certificatoFr.rilasciatoDa
                    };
                }
                frService.setCurrentFr(undefined);
                /*$scope.model.activeProfile = _.eq(_.lowerCase($scope.model.statoProfilo), "attivo");*/
                /*currentFr.then(function (response) {
                });*/
            }

            /*TEST MODEL*/
            /*frService.searchFirstResponderByFilter({ id: "4028b881580178020158017863ae000a" }).then(function (response) {

                $scope.model = response[0];

            })*/
            /*Combo COMUNI */
            $scope.comuni = [];
            $scope.refreshComune = function (input) {
                if (_.isUndefined(input) || input.length < 3) {
                    $scope.comuni = [];
                    return [];
                }

                var filter = {
                    "nomeComune": input
                };
                return anagraficaService.searchComuniByFilter(filter).then(function (response) {
                    $scope.comuni = response;
                    return $scope.comuni;
                });
            };

            /*Combo INDIRIZZO*/
            $scope.strade = [];
            $scope.refreshStrade = function (search) {
                if (_.isUndefined(search) || search.length < 3) {
                    $scope.strade = [];
                    return [];
                }
                var filter = {
                    "name": search
                };
                if ($scope.model.comuneResidenza) {
                    filter.nomeComune = $scope.model.comuneResidenza.nomeComune;
                }
                return anagraficaService.searchStradeByFilter(filter).then(function (response) {
                    /*debugger;*/
                    $scope.strade = response;
                });
            };

            /*LISTA PROFESSIONI*/
            $scope.refreshProfessioni = function (search) {
                if (_.isUndefined(search)) return [];
                return anagraficaService.searchProfessioneByFilter({
                    name: search
                }).then(function (response) {
                    /*debugger;*/
                    $scope.professioni = response;
                });
            };

            //precarico tutte le professioni
            anagraficaService.searchProfessioneByFilter({
                name: null
            }).then(function (response) {
                /*debugger;*/
                $scope.professioni = response;
            });

            /*LISTA ENTI CERTIFICATORI*/
            $scope.refreshEntiCertificatori = function (search) {
                if (_.isUndefined(search)) return [];
                return anagraficaService.getAllEnteCertificatore({
                    name: search
                }).then(function (response) {
                    /*debugger;*/
                    $scope.enti = response;
                });
            };

            /* GESTIONE date piker */
            $scope.dataConsegPopup = {
                opened: false
            };

            $scope.openConsegDate = function () {
                $scope.dataConsegPopup.opened = true;
            };

            $scope.today = function () {
                $scope.model.certificatoFr.dataConseguimento = new Date();
            };

            $scope.clear = function () {
                $scope.model.certificatoFr.dataConseguimento = null;
            };
            $scope.options = {
                /*  customClass: getDayClass,*/

                showWeeks: false
            };

            function getDayClass(data) {
                var date = data.date,
                    mode = data.mode;
                if (mode === 'day') {
                    var dayToCheck = new Date(date).setHours(0, 0, 0, 0);
                    for (var i = 0; i < $scope.events.length; i++) {
                        var currentDay = new Date($scope.events[i].date)
                            .setHours(0, 0, 0, 0);
                        if (dayToCheck === currentDay) {
                            return $scope.events[i].status;
                        }
                    }
                }
                return '';
            }
            /* */

            /*combo categorie*/
            $scope.refreshCategorie = function (input) {
                if (_.isUndefined(input)) {
                    return [];
                }
                var categoryFilter = {
                    name: input
                };
                return anagraficaService.searchCategorie(categoryFilter).then(function (response) {
                    /*debugger;*/
                    /* response = _.filter(response, function(o) {
                         return o.descrizione.indexOf(input)>=0;
                     });*/
                    $scope.categorie = response;
                });
            };

            /*gestione selezione della strada*/
            $scope.selectStreet = function (street) {
                if (!street) {

                }
                $scope.model.comuneResidenza = street.comune;
            };

            /*gestione selezione del comune*/
            $scope.selectMunicipality = function (mun) {

                //if (!mun) {
                $scope.model.indirizzo = undefined;
                //}
            };

            /*gestione elemento selezionato e dato vuoto*/
            $scope.checkSelected = function (type, exp) {
                /*debugger;*/
                if (type === "comune") {
                    if (!$scope.comuni && $scope.comuni.length === 0) {
                        $scope.$eval(exp);
                    }
                }
            };

            $scope.checkChildren = function (id) {
                var invalids = $document.find('#' + id + ' .ng-invalid');
                if (invalids && !_.isEmpty(invalids)) {
                    return false;
                } else {
                    return true;
                }
            };

            /* $scope.changeActive = function(){
                 $scope.isActive = !$scope.isActive;
                 $scope.changeProfileStatus();
             }*/

            /* var _setStatoProfilo = function () {

                 if ($scope.model.activeProfile) {
                     $scope.model.statoProfilo = "ATTIVO";
                 }
                 else {
                     $scope.model.statoProfilo = "IN_ATTESA_DI_ATTIVAZIONE";
                 }
             }*/

            /*var _refreshActiveProfile = function () {
                $scope.model.activeProfile = _.eq(_.lowerCase($scope.model.statoProfilo), "attivo");
            }*/

            /*salvataggio*/
            $scope.saveFr = function () {
                if (!$scope.firstResponder.$invalid) {
                    frService.saveFirstResponder($scope.model).then(function (response) {
                        if (response.response === false) {
                            if (response.message) {
                                toastr.error(response.message, 'Errore');
                            }
                        } else {
                            toastr.success('Salvataggio dei dati avvenuto con successo', 'Salvataggio avvenuto');
                            $scope.setModel(response);
                            $scope.rilasciatoDa = {
                                descrizione: $scope.model.certificatoFr.rilasciatoDa
                            };
                        }
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
            /*fine salvataggio*/

            /*gestione is medico*/
            $scope.selectProfessione = function (professione) {
                if (!professione) {
                    $scope.isMedico = false;
                    return;
                }
                var desc = _.clone(professione.descrizione);
                var lowerP = _.lowerCase(desc);
                $scope.isMedico = lowerP.indexOf("medico") >= 0;
            };

            $scope.selectRilasciatoDa = function (rilasciatoDa) {
                if (rilasciatoDa) {
                    $scope.model.certificatoFr.rilasciatoDa = rilasciatoDa.descrizione;
                } else {
                    $scope.model.certificatoFr.rilasciatoDa = undefined;
                }
            };

            /*reload del first responder*/
            $scope.reloadFr = function () {
                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Tutti i dati sulla maschera verranno persi",
                    ok: function () {
                        frService.reloadFirstReponder($scope.model.id).then(function (response) {
                            $scope.setModel(response);
                        });
                    },
                    cancel: function () {}
                });
            };

            /*validazione dei dati*/
            $scope.validate = function (value, checkMedico) {
                console.log($scope.firstResponder);
                return !_.isEmpty(value);
            };

            $scope.validateForMedico = function (value) {
                if ($scope.isMedico && _.isEmpty(value)) {
                    return false;
                }
                return true;

            };

            $scope.hasCert = function () {
                if (!_.isUndefined($scope.model.certificatoFr) && !_.isNull($scope.model.certificatoFr)) {
                    return !_.isUndefined($scope.model.certificatoFr.immagine);
                }
                return false;
            };

            $scope.open = function (url) {
                window.open(url + '?size=LARGE');
            };

            $scope.elimina = function () {
                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Premendo ok il First Responder verrà eliminato",
                    ok: function () {
                        var fr = {
                            id: $scope.model.id
                        };
                        frService.deleteFR(fr).then(function (res) {
                            $scope.setModel({});

                            $state.transitionTo('main.frSearch');
                        });
                    },
                    cancel: function () {}
                });
            };

            /*$scope.validateForMedico = function(value){}*/

            $scope.aggiungiComuneCompetenza = function () {
                if (!$scope.model.comuniCompetenza) {
                    $scope.model.comuniCompetenza = [];
                }
                if ($scope.model.comuneCompetenza) {
                    var present = false;
                    $scope.model.comuniCompetenza.forEach(function (el) {
                        if (el.id === $scope.model.comuneCompetenza.id) {
                            present = true;
                        }
                    });
                    if (!present) {

                        //aggiungo il comune selezionato alla lista
                        $scope.model.comuniCompetenza.push(
                            $scope.model.comuneCompetenza
                        );

                        $scope.model.comuniCompetenza = _.orderBy($scope.model.comuniCompetenza, ['nomeComune'], ['asc']);

                        $scope.model.comuneCompetenza = null;
                    } else {
                        modalService.openModal("warningModal.html", "Attenzione", {
                            message: {
                                title: "Verifica dati inseriti",
                                text: "Comune già presente nella lista"
                            }
                        });
                    }
                } else {
                    modalService.openModal("warningModal.html", "Attenzione", {
                        message: {
                            title: "Verifica dati inseriti",
                            text: "Attenzione selezionare un comune dalla combobox"
                        }
                    });
                }
            };

            $scope.rimuoviComuneCompetenza = function (comuneConp, index) {
                _.pullAt($scope.model.comuniCompetenza, [index]);
            };
        }
    ]);
});