appControllers.controller('InterventionRequestsCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$stateParams',
    '$state',
    'deviceService',
    'googleMapsFactory',
    '$cordovaGeolocation',
    'alertService',
    '$q',
    '$ionicHistory',
    'eventData',
    'BackgroundGeolocationService',
    'utilityService',
    function ($rootScope, $scope, logger, api, user, $timeout, 
        $stateParams, $state, device, googleMapsFactory, $cordovaGeolocation, 
        alertService, $q, $ionicHistory, eventData, bgGeo, utils) {
        logger.info("Caricamento InterventionRequestsCtrl...");

        var vm = this;
        vm.checkIntervention = checkIntervention;
        vm.rejectIntervention = rejectIntervention;

        $scope.$on('$ionicView.loaded', function () {
            vm.event = eventData;
        });

        $scope.$on('$ionicView.enter', function () {
            logger.info($stateParams);
            vm.event = $state.$current.locals["menuContent@app"].eventData;
            // vm.event.arrivalTime = null;
            // if (vm.event.dataArrivoAmbulanza && vm.event.tempoArrivoAmbulanza){
            //     vm.event.arrivalTime = moment(vm.event.dataArrivoAmbulanza).add(vm.event.tempoArrivoAmbulanza, 'minutes').format('DD/MM/YYYY HH:mm');
            // }
        });

        function checkIntervention(eventId) {
            device.showLoader();
         

            api.checkIntervention(eventId, vm.event.currentPosition).then(
                function (data) {
                    device.hideLoader();
                    if (data.canManageIntervention) {
                        alertService.manageEvent(true);
                        alertService.setLocation(vm.event.coordinate.latitudine, vm.event.coordinate.longitudine);
                        bgGeo.changeConfiguration('alert', bgGeolocationCallback, bgGeolocationErrorCallback());
                        $state.go("app.interventionDetail", { eventId: $stateParams.eventId });
                    }
                    else {
                        alertService.remove();
                        device.alert("La centrale operativa ha rifiutato la tua richiesta di intervento", "balanced").then(
                            function () {
                                $state.go("app.home");
                            }
                        );
                    }
                    logger.info(JSON.stringify(data));
                },
                function (error) {
                    device.hideLoader();
                    device.alert("Si è verificato un errore", "balanced").then(
                        function () {
                            $state.go("app.home");
                        }
                    );
                    logger.error(error)
                }
            );
        }

        function bgGeolocationErrorCallback(error) {
            logger.info("--BGGEOLOCATION ERROR CALLBACK--", error);
        }

        function bgGeolocationCallback(location) {
            logger.info("--BGGEOLOCATION CALLBACK--", location);
            vm.accuracy = location.accuracy;
            vm.lat = location.latitude;
            vm.lng = location.longitude;
            var destination = alertService.getLocation();
            if (destination) {
                location.distanceFromEvent = utils.calculateDistance(destination, location);
            }
            else {
                location.distanceFromEvent = 0;
            }
            utils.manageEmergencyEvent(location).then(
                function () {
                    backgroundGeoLocation.finish();
                    $state.go("app.interventionConfirmed");
                }
            );
            utils.updateFRLocation(location);
            backgroundGeoLocation.finish();
        }

        function rejectIntervention() {
            device.showLoader();
            api.rejectEvent(alertService.getEventId(), alertService.getCartellino(), alertService.getCoRiferimento()).then(
                function () {
                    device.hideLoader();
                    $timeout(function () {
                        $state.go("app.home");
                    }, 400)
                },
                function (error) {
                    device.hideLoader();
                    device.alert("Si è verificato un errore", "balanced").then(
                        function () {
                            $state.go("app.home");
                        }
                    );
                    logger.error(error)
                }
            );
        }

        function checkCurrentLocation() {
            var location = {};
            var q = $q.defer();
            var posOptions = {
                timeout: 5000,
                enableHighAccuracy: true,
                maximumAge: 0
            };
            if (device.isCordova()) {
                device.checkIsLocationAvailable().then(
                    function (status) {
                        if (status) {
                            $cordovaGeolocation
                                .getCurrentPosition(posOptions)
                                .then(function (position) {
                                    logger.info(position);
                                    location.lat = position.coords.latitude;
                                    location.lng = position.coords.longitude;
                                    q.resolve(location);
                                }, function (error) {
                                    q.reject(error);
                                    logger.error(error);
                                });
                        }
                        else {
                            q.reject("Connessione dati o GPS non attivi");
                        }
                    },
                    function (error) {
                        q.reject(error);
                    }
                );
            }
            else {
                $cordovaGeolocation
                    .getCurrentPosition(posOptions)
                    .then(function (position) {
                        logger.info(position);
                        location.lat = position.coords.latitude;
                        location.lng = position.coords.longitude;
                        q.resolve(location);
                    }, function (error) {
                        q.reject(error);
                        logger.error(error);
                    });
            }
            return q.promise;
        }

    }]);