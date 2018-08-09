define(["angular","moment"], function (angular,moment) {
    angular.module('controller').controller("VehicleModalCtrl", ["$uibModal", "$scope", "$log", "$document", "$rootScope", "alertService",
        function ($uibModal, $scope, $log, $document, $rootScope, alertService) {
            var $ctrl = this;
            $ctrl.open = function (size, data, template, controller, parentSelector) {
                var parentElem = parentSelector ?
                    angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
                var modalInstance = $uibModal.open({
                    animation: $ctrl.animationsEnabled,
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: template || 'modalContent.html',
                    controller: controller || 'ModalInstanceCtrl',
                    controllerAs: '$ctrl',
                    backdrop: "static",
                    windowClass: 'app-modal-window',
                    size: size || "xlg",
                    appendTo: parentElem,
                    resolve: {
                        items: function () {
                            return $ctrl.items;
                        },
                        currentEvent: function () {
                            return data.currentEvent;
                        },
                        currentIntervention: function () {
                            return data.currentIntervention;
                        }
                    }
                });

                modalInstance.result.then(function (selectedItem) {
                    var i = data.currentIntervention;
                    $ctrl.selected = selectedItem;
                    $rootScope.$broadcast("wdodisrec.updateIntervention", data.currentIntervention);
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $ctrl.toggleAnimation = function () {
                $ctrl.animationsEnabled = !$ctrl.animationsEnabled;
            };

            var h1 = $rootScope.$on("wdodisrec.opnHospital", function (event, data) {
                $ctrl.open("sm", { currentIntervention: data }, "hospitalModal.html", "HospitalInstanceCtrl");
            });

            var h2 = $rootScope.$on("wdodisrec.openIntervention", function (event, data) {

                /*window.setTimeout(function () { $ctrl.open("lg",data); }, 1000);*/

                $ctrl.open("xlg", { currentEvent: data });
            });

            var h3 = $rootScope.$on("wdodisrec.hospitalSelected", function (event, data) {

            })

            $scope.$on("$destroy", function () {
                h1();
                h2();
                h3();
            });
        }]);

    angular.module("controller").controller("AlertsCtrl", ["$scope", function ($scope) {

        $scope.alerts = [
            { type: 'error', msg: 'Oh snap! Change a few things up and try submitting again.' },
            { type: 'success', msg: 'Well done! You successfully read this important alert message.' }
        ];

        $scope.addAlert = function () {
            $scope.alerts.push({ msg: "Another alert!" });
        };

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };
    }]);

    angular.module("controller").controller("HospitalInstanceCtrl",
        ["$rootScope", "$uibModalInstance", "apiService", "$scope", "currentIntervention",
            function ($rootScope, $uibModalInstance, apiService, $scope, currentIntervention) {
                var $ctrl = this;
                $ctrl.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $ctrl.ok = function () {
                    $uibModalInstance.close($ctrl.selectedHospital);
                };

                $ctrl.hospitals = [];

                $ctrl.selectedHospital = {
                    name: currentIntervention.destination,
                    id: currentIntervention.destinationId
                }

                $ctrl.currentIntervention = currentIntervention;

                $ctrl.selectHospital = function (hosp) {
                    $ctrl.currentIntervention.destination = hosp.name;
                    $ctrl.currentIntervention.destinationId = hosp.id;
                    $ctrl.selectedHospital = hosp;
                    $rootScope.$broadcast("wdodisrec.hospitalSelected", hosp);
                }

                $ctrl.searchHospital = function (search) {
                    apiService.searchHospital(search).then(function (resp) {
                        $ctrl.hospitals = resp;
                    });
                }
            }]);

    angular.module('controller').controller('ModalInstanceCtrl',
        ["$uibModalInstance", "items", "currentEvent", "$scope", "apiService", "$rootScope", "alertService", "loggerService",
            function ($uibModalInstance, items, currentEvent, $scope, apiService, $rootScope, alertService,logger) {
                var $ctrl = this;

                $ctrl.vehicles = [];

                $ctrl.interventions = currentEvent.interventionsDr || [];

                $ctrl.currentIntervention = {};

                $ctrl.ok = function () {
                    $rootScope.$broadcast("wdodisrec.reload", {});
                    $uibModalInstance.dismiss('cancel');

                };

                $ctrl.reloadEvent = function () {
                    var code = currentEvent.emergencyId;
                    apiService.getEventByEmergencyId(code).then(function (response) {
                        var eventDR = response;
                        var dbEvent = angular.fromJson(eventDR.eventData);
                        $ctrl.interventions = dbEvent.interventionsDr || [];
                    });
                }

                $ctrl.selectCurrentIntervention = function (i) {
                    $ctrl.currentIntervention = i;
                }

                $ctrl.openHospitalModal = function () {
                    $rootScope.$broadcast("wdodisrec.opnHospital", $ctrl.currentIntervention);
                }

                $ctrl.cancel = function () {
                    $rootScope.$broadcast("wdodisrec.reload", {});
                    $uibModalInstance.dismiss('cancel');

                };

                $ctrl.addIntervention = function () {
                    $ctrl.interventions.push({
                        id: centralName + "_" + ($ctrl.interventions.length + 1)
                    });
                }

                $ctrl.searchVehicleByName = function (vehicle) {
                    apiService.searchVehicle(vehicle).then(function (response) {
                        $ctrl.vehicles = response;
                    });
                }

                $ctrl.phones = [];

                $ctrl.searchPhones = function (desc) {
                    apiService.searchPhones(desc).then(function (response) {
                        $ctrl.phones = response;
                    });
                }

                var handler = $rootScope.$on("wdodisrec.updateIntervention", function (event, newData) {
                    $ctrl.saveIntervention(newData);
                })

                $scope.$on("$destroy", function () {
                    handler();
                });

                var _checkTime =  function (time) {
                    var currentDate = moment();
                    var timeDate = moment(time);
                    logger.info("check time: "+time);
                    
                }

                $ctrl.saveIntervention = function (interventionData) {
                    if (_.isEmpty(interventionData.vehicleName)) {
                        alertService.add("error", "Inserire il nome del mezzo");
                        return;
                    }
                    _checkTime(interventionData.activedIntervention);
                    apiService.getEventByEmergencyId(currentEvent.emergencyId).then(function (response) {
                        /*debugger;*/
                        var eventDR = response;
                        var dbEvent = angular.fromJson(eventDR.eventData);
                        if (!dbEvent.interventionsDr) {
                            dbEvent.interventionsDr = [];
                            //var updatedInt = angular.merge({}, interventionData);
                            //intervs.push(updatedInt);
                        }
                        var intervs = dbEvent.interventionsDr;
                        var found = false;

                        intervs.forEach(function (i) {

                            //caso intervento esistente
                            if (i && (i.vehicleName === interventionData.vehicleName)) {
                                found = true;
                                var updatedInt = angular.merge(i, interventionData);
                                /*copy dates from interventionData to i */

                                //check if is a Number or a Date and treat it as its own "type"

                                updatedInt = _updateInterventionData(interventionData, updatedInt);

                                //debugger;
                                //its time to update event with all new datas

                            }
                        });

                        if (!found) {
                            var updatedInt = angular.merge({}, interventionData);
                            intervs.push(updatedInt);

                        }
                        apiService.saveEvent(dbEvent).then(function (response) {
                            $ctrl.reloadEvent();
                        });
                    });

                };

                var _updateInterventionData = function (interventionData, updatedInt) {
                    //data attivazione
                    if (!angular.isNumber(interventionData.activedIntervention)) {
                        if (angular.isDate(interventionData.activedIntervention)) {
                            updatedInt.activedIntervention = interventionData.activedIntervention.getTime();
                        }
                    }
                    else {
                        updatedInt.activedIntervention = interventionData.activedIntervention;
                    }

                    //data partenza
                    if (!angular.isNumber(interventionData.sendVehicle)) {
                        if (angular.isDate(interventionData.sendVehicle)) {
                            updatedInt.sendVehicle = interventionData.sendVehicle.getTime();
                        }
                    }
                    else {
                        updatedInt.sendVehicle = interventionData.sendVehicle;
                    }

                    //data arrivo sul luogo
                    if (!angular.isNumber(interventionData.placeArrival)) {
                        if (angular.isDate(interventionData.placeArrival)) {
                            updatedInt.placeArrival = interventionData.placeArrival.getTime();
                        }
                    }
                    else {
                        updatedInt.placeArrival = interventionData.placeArrival;
                    }

                    //data arrivo sul luogo
                    if (!angular.isNumber(interventionData.placeArrival)) {
                        if (angular.isDate(interventionData.placeArrival)) {
                            updatedInt.placeArrival = interventionData.placeArrival.getTime();
                        }
                    }
                    else {
                        updatedInt.placeArrival = interventionData.placeArrival;
                    }

                    //data carico pazienti                                    
                    if (!angular.isNumber(interventionData.placeDeparture)) {
                        if (angular.isDate(interventionData.placeDeparture)) {
                            updatedInt.placeDeparture = interventionData.placeDeparture.getTime();
                        }
                    }
                    else {
                        updatedInt.placeDeparture = interventionData.placeDeparture;
                    }

                    //data destinazione                                    
                    if (!angular.isNumber(interventionData.hospitalArrival)) {
                        if (angular.isDate(interventionData.hospitalArrival)) {
                            updatedInt.hospitalArrival = interventionData.hospitalArrival.getTime();
                        }
                    }
                    else {
                        updatedInt.hospitalArrival = interventionData.hospitalArrival;
                    }

                    //data libero operativo                                    
                    if (!angular.isNumber(interventionData.closedIntervention)) {
                        if (angular.isDate(interventionData.closedIntervention)) {
                            updatedInt.closedIntervention = interventionData.closedIntervention.getTime();
                        }
                    }
                    else {
                        updatedInt.closedIntervention = interventionData.closedIntervention;
                    }

                    return updatedInt;
                }

            }]);
});