define(['angular', "moment"], function (angular, moment) {
    angular.module("controller").controller('EventGridCtrl',
        ["$scope", "$filter", "apiService", "loggerService", "$rootScope", "alertService",
            function ($scope, $filter, apiService, loggerService, $rootScope, alertService) {

                $scope.addressFilter = {
                    munName: "",
                    munId: "",
                    locName: "",
                    eventCode: ""
                }

                var currentEvent = {};

                $scope.reload = true;
                $scope.showClosed = false;

                $scope.activated = true;

                $scope.events = [];

                $scope.streets = [];

                $scope.municipalities = [];

                $scope.pathologies = [];

                $scope.criticities = [];

                $scope.formVisible = true;

                $scope.searchEvent = "";

                $scope.myHTML =
                    'I am an <code>HTML</code>string with ' +
                    '<a href="#">links!</a> and other <em>stuff</em>';

                /*$scope.isSearchEventCollapsed = true;*/

                loadAll();

                $scope.displaySummary = function (ev) {
                    var s = ($filter('limitTo')(ev.place, 1) || "") +
                        ($filter('limitTo')(ev.pathology, 3) || "") +
                        ($filter('limitTo')(ev.criticity, 1) || "");
                    return s || "N.D.";
                    /*ev.place | limitTo: 1}}
					   {{ev.pathology | limitTo: 3}}
					   {{ev.criticity | limitTo: 1}}*/
                }

                $scope.$watch('reload', function (newValue, oldValue) {
                    if (newValue === oldValue) {
                        return;
                    }
                    /*alert("hey, reload has changed:" +newValue+ " - "+oldValue);*/
                    loadAll();
                });


                $scope.$watch('showClosed', function (newValue, oldValue) {
                    if (newValue === oldValue) {
                        return;
                    }
                    /*alert("hey, reload has changed:" +newValue+ " - "+oldValue);*/
                    loadAll(newValue);
                });

                $scope.setDirty = function () {
                    $scope.reload = !$scope.reload;
                }

                function loadAll(showClosed) {
                    apiService.loadEvents().then(function (data) {
                        $scope.formVisible = true;
                        if (!showClosed) {
                            $scope.events = _.filter(data, function (o) {
                                //debugger;
                                loggerService.info(o.emergencyId + " - " + o.isClosed);
                                if (!(_.has(o, 'isClosed'))) {
                                    return true;
                                }
                                return !o.isClosed;
                            });
                        }
                        else {
                            $scope.events = data;
                        }
                        /*$scope.events = data;*/
                        $scope.listPathologies();
                        $scope.listPlaces();
                        $scope.listCriticities();
                    });
                }

                $scope.onExpandStreet = function (isOpen, streetName) {
                    if (isOpen) {
                        /*$scope.addressFilter.locName = locName;
                        $scope.addressFilter.munName = munName;*/

                        $scope.searchStreetByName((streetName || $scope.addressFilter.streetName || currentEvent.streetName), "", "");
                    }
                }

                $scope.onExpandLocality = function (isOpen, locName) {
                    if (isOpen) {
                        $scope.addressFilter.locName = locName;
                        /*$scope.addressFilter.munName = munName;*/
                        $scope.searchLocalities((locName || $scope.addressFilterlocName), "");
                    }
                }

                $scope.onExpandMunicipality = function (isOpen, munName) {
                    if (isOpen) {
                        /*$scope.addressFilter.munName = munName;*/
                        $scope.searchMunicipalities(munName);
                    }
                }


                // add user
                $scope.addEvent = function () {
                    return apiService.countEmergencies().then(function (cnt) {
                        $scope.events.push({
                            id: centralName + "_" + (cnt),
                            isNew: true
                        });
                    });
                };

                $scope.searchStreetByName = function (searchData, munName, localityName) {
                    loggerService.info("munName: " + $scope.addressFilter.munName + " - " + $scope.addressFilter.locName + " - " + searchData);
                    apiService.searchStreet($scope.addressFilter.munName, $scope.addressFilter.locName, searchData).then(function (data) {
                        if (data.length === 0) {
                            data.unshift({ name: searchData });
                        }
                        $scope.streets = data;
                    });
                };

                $scope.searchMunicipalities = function (munName) {
                    apiService.searchMunicipalities(munName).then(function (data) {
                        $scope.municipalities = data;
                    });
                }

                $scope.searchLocalities = function (locName, mun) {
                    apiService.searchLocalities(locName, mun).then(function (data) {
                        $scope.localities = data;
                    });
                }

                $scope.selectLocality = function (loc) {
                    if (!loc) {
                        $scope.addressFilter.locName = "";
                        $scope.addressFilter.locId = "";
                        return;
                    }
                    loggerService.info("selectLocality: " + loc.name + " - " + loc.id);
                    $scope.addressFilter.locName = loc.name;
                    $scope.addressFilter.locId = loc.id;
                    $scope.addressFilter.munName = loc.municipalityName;
                    $scope.addressFilter.munId = loc.municipalityId;
                    currentEvent.municipalityName = loc.municipalityName;
                    currentEvent.municipalityId = loc.municipalityId;
                }

                $scope.selectMunicipality = function (mun) {
                    if (!mun) {
                        $scope.addressFilter.munName = "";
                        $scope.addressFilter.munId = "";
                        return;
                    }
                    loggerService.info("selectMunicipality: " + mun);
                    $scope.addressFilter.munName = mun.name;
                    $scope.addressFilter.munId = mun.id;
                    $scope.addressFilter.locName = null;
                    $scope.addressFilter.locId = null;
                    if (currentEvent.municipalityName !== mun.name) {
                        currentEvent.localityName = null;
                        currentEvent.localityId = null;
                        currentEvent.streetName = null;
                        currentEvent.streetId = null;
                    }
                    currentEvent.municipalityName = mun.name;
                    currentEvent.municipalityId = mun.id;

                }

                $scope.selectStreet = function (street) {
                    if (!street) {
                        return;
                    }
                    loggerService.info("selectStreet: " + street);
                    $scope.addressFilter.streetName = street.name;
                    $scope.addressFilter.munName = street.municipalityName;
                    $scope.addressFilter.munId = street.municipalityId;
                    $scope.addressFilter.locName = street.localityName;
                    $scope.addressFilter.locId = street.localityId;
                    currentEvent.localityName = street.localityName;
                    currentEvent.localityId = street.localityId;
                    currentEvent.streetId = street.id;
                    currentEvent.municipalityName = street.municipalityName;
                    currentEvent.municipalityId = street.municipalityId;
                }

                $rootScope.$on("wdodisrec.newevent", function (event, data) {
                    $scope.addEvent();
                });

                $rootScope.$on("wdodisrec.reload", function (event, data) {
                    $scope.setDirty();
                });

                $scope.currentForm = function (rowform, ev) {
                    rowform.$activate("municipalityName");
                    $scope.addressFilter = {
                        munName: ev.municipalityName,
                        locName: ev.localityName,
                        eventCode: ev.emergency
                    }
                    $scope.formVisible = !$scope.formVisible;
                    currentEvent = ev;
                }

                $scope.cleanCurrentForm = function () {
                    $scope.formVisible = !$scope.formVisible;
                    currentEvent = null;
                }

                $scope.saveEvent = function (data, id) {

                    if (_.isEmpty(data.pathology)
                        || _.isEmpty(data.criticity)
                        || _.isEmpty(data.place)) {
                        alertService.add("warning", "Giudizio di sintesi incompleto");
                    }

                    return apiService.countEmergencies().then(function (cnt) {
                        loggerService.info("saving event: " + cnt);
                        data.id = id || cnt;
                        return apiService.getEventByEmergencyId(data.emergencyId).then(function (response) {
                            if (_.isEmpty(response)) {
                                return apiService.saveEvent(data).then(function (success) {
                                    $scope.setDirty();
                                });
                            }
                            var eventDR = response;
                            var dbEvent = angular.fromJson(eventDR.eventData);
                            if (dbEvent) {
                                var updatedEvent = angular.merge(dbEvent, data);
                                apiService.saveEvent(updatedEvent).then(function (success) {
                                    $scope.setDirty();
                                });
                            }
                            $scope.formVisible = !$scope.formVisible;
                        });
                    });



                }

                $scope.openVehicleGrid = function (data) {
                    apiService.getEventByEmergencyId(data.emergencyId).then(function (response) {
                        var eventDR = response;
                        var dbEvent = angular.fromJson(eventDR.eventData);
                        $rootScope.$broadcast("wdodisrec.openIntervention", dbEvent);
                    })

                }

                $scope.closeEvent = function (data) {
                    apiService.getEventByEmergencyId(data.emergencyId).then(function (response) {
                        var eventDR = response;
                        var dbEvent = angular.fromJson(eventDR.eventData);
                        if (_.isUndefined(dbEvent)) {
                            $scope.setDirty();
                            return;
                        }
                        dbEvent.dateClose = moment();
                        dbEvent.isClosed = true;
                        apiService.saveEvent(dbEvent).then(function (success) {
                            $scope.setDirty();
                        });
                    })
                }

                $scope.getCurrentState = function (int) {
                    var states = [
                        { "state": "IN", "statusDate": int.activedIntervention },
                        { "state": "PA", "statusDate": int.sendVehicle },
                        { "state": "AR", "statusDate": int.placeArrival },
                        { "state": "CA", "statusDate": int.placeDeparture },
                        { "state": "D1", "statusDate": int.hospitalArrival },
                        { "state": "DISP", "statusDate": int.closedIntervention }
                    ]
                    var filtered = _.filter(states, function (o) { return !_.isUndefined(o.statusDate); });
                    var sorted = _.sortBy(filtered, function (o) {
                        return o.statusDate
                    });
                    return sorted[(sorted.length - 1)];

                }


                $scope.openSummaryJudge = function (data) {
                    var list = {
                        pathologies: $scope.pathologies,
                        places: $scope.places,
                        criticities: $scope.criticities,

                    }
                    $rootScope.$broadcast("wdodisrec.openSummaryJudge", list, currentEvent);
                    /*apiService.getEventByEmergencyId(data.emergencyId).then(function (response) {
                        var eventDR = response;
                        var dbEvent = angular.fromJson(eventDR.eventData);
                        
                        
                    })*/
                }

                $scope.listPlaces = function () {
                    apiService.listStaticData("LUOGO").then(function (response) {
                        $scope.places = response;
                    });
                }

                $scope.listPathologies = function () {
                    apiService.listStaticData("PATOLOGIA").then(function (response) {
                        $scope.pathologies = response;
                    });
                }

                $scope.listCriticities = function () {
                    apiService.listStaticData("CRITICITA").then(function (response) {
                        $scope.criticities = response;
                    });
                }


                function _getPaginatedItems(items, page) {
                    var page = page || 1,
                        per_page = 3,
                        offset = (page - 1) * per_page,
                        paginatedItems = _.rest(items, offset).slice(0, per_page);
                    return {
                        page: page,
                        per_page: per_page,
                        total: items.length,
                        total_pages: Math.ceil(items.length / per_page),
                        data: paginatedItems
                    };
                }

            }]);
});