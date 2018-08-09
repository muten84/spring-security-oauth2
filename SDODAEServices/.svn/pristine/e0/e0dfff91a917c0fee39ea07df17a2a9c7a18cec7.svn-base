'use strict';

define(['angular', "moment", "lodash"], function(angular, moment, _) {
    angular.module("controller").controller('FrInsertCtrl', ["$scope",
        "$filter",
        "daeService",
        "anagraficaService",
        "frService",
        "loggerService",
        "$rootScope",
        "alertService",
        "currentFr",
        "modalService",
        function($scope, $filter, daeService,
            anagraficaService, frService,
            loggerService, $rootScope,
            alertService, currentFr, modalService) {

            $scope.model = {};

            $rootScope.$broadcast("main.changeTitle", {
                title: "Inserimento First Responder",
                icon: "fa fa-user-md"
            });

            $scope.struttura = {
                disabilita: false
            };

            if (!_.isUndefined(currentFr)) {
                $scope.model = currentFr[0];
                /*currentFr.then(function (response) {

                });*/
            }


            /*TEST MODEL*/
            /*frService.searchFirstResponderByFilter({ id: "4028b881580178020158017863ae000a" }).then(function (response) {

                $scope.model = response[0];

            })*/
            /*Combo COMUNI */
            $scope.comuni = [];
            $scope.refreshComune = function(input) {
                if (_.isUndefined(input)) return [];
                if (input.length < 3) return [];

                var filter = {
                    "nomeComune": input
                };
                return anagraficaService.searchComuniByFilter(filter).then(function(response) {
                    $scope.comuni = response;
                    return $scope.comuni;
                });
            };

            /*Combo INDIRIZZO*/
            $scope.strade = [];
            $scope.refreshStrade = function(search) {
                if (_.isUndefined(search)) return [];
                if (search.length < 3) return [];
                var filter = {
                    "name": search
                };
                if(  $scope.model.comuneResidenza){
                    filter.nomeComune =   $scope.model.comuneResidenza.nomeComune;
                }
                return anagraficaService.searchStradeByFilter(filter).then(function(response) {
                    /*debugger;*/
                    $scope.strade = response;
                });
            };

            /*LISTA PROFESSIONI*/
            $scope.refreshProfessioni = function(search) {
                if (_.isUndefined(search)) return [];
                return anagraficaService.searchProfessioneByFilter({ name: search }).then(function(response) {
                    /*debugger;*/
                    $scope.professioni = response;
                });
            };

            /*LISTA ENTI CERTIFICATORI*/
            $scope.refreshEntiCertificatori = function(search) {
                if (_.isUndefined(search)) return [];
                return anagraficaService.getAllEnteCertificatore({ name: search }).then(function(response) {
                    /*debugger;*/
                    $scope.enti = response;
                });
            };

            /* GESTIONE date piker */
            $scope.dataConsegPopup = {
                opened: false
            };

            $scope.openConsegDate = function() {
                $scope.dataConsegPopup.opened = true;
            };

            $scope.today = function() {
                $scope.model.certificatoFr.dataConseguimento = new Date();
            };

            $scope.clear = function() {
                $scope.model.certificatoFr.dataConseguimento = null;
            };
            $scope.options = {
                /*  customClass: getDayClass,*/
                minDate: new Date(),
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
            $scope.refreshCategorie = function(input) {
                if (_.isUndefined(input)) {
                    return [];
                }
                var categoryFilter = { name: input };
                return anagraficaService.searchCategorie(categoryFilter).then(function(response) {
                    /*debugger;*/
                    /* response = _.filter(response, function(o) {
                         return o.descrizione.indexOf(input)>=0;
                     });*/
                    $scope.categorie = response;
                });
            };

            /*gestione selezione della strada*/
            $scope.selectStreet = function(street){
                if(!street){

                }
                $scope.model.comuneResidenza = street.comune;

            };

            /*gestione elemento selezionato e dato vuoto*/
            $scope.checkSelected = function(type, exp) {
                /*debugger;*/
                if (type === "comune") {
                    if (!$scope.comuni && $scope.comuni.length === 0) {
                        $scope.$eval(exp);
                    }
                }
            };

            /*salvataggio*/
            $scope.saveFr = function() {
                if (!$scope.firstResponder.$invalid) {
                    frService.saveFirstResponder($scope.model).then(function(response) {
                        $scope.model = response;
                    });
                }
                else {
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
            $scope.selectProfessione = function(professione) {
                if(!professione){
                    $scope.isMedico = false;
                    return;
                }
                var desc = _.clone(professione.descrizione);
                var lowerP = _.lowerCase(desc);
                $scope.isMedico = lowerP.indexOf("medico") >= 0;
            };

            /*reload del first responder*/
            $scope.reloadFr = function() {
                var filter = { id: $scope.model.id };
                frService.searchFirstResponderByFilter(filter).then(function(response) {
                    /*debugger;*/
                    $scope.model = response[0];
                });
            };


            /*validazione dei dati*/
            $scope.validate = function(value, checkMedico) {
                console.log($scope.firstResponder);
                return !_.isEmpty(value);
            };

            $scope.validateForMedico = function(value){
                if($scope.isMedico && _.isEmpty(value)){
                    return false;
                }
                return true;

            };

            /*$scope.validateForMedico = function(value){}*/

        }
    ]);
});
