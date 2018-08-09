appControllers.controller('HomeCtrl', [
    '$rootScope',
    '$scope',
    '$timeout',
    '$state',
    '$stateParams',
    '$ionicHistory',
    'loggerService',
    'deviceService',
    'userService',
    '$cordovaInAppBrowser',
    '$mdBottomSheet',
    '$cordovaGeolocation',
    'apiService',
    'BackgroundGeolocationService',
    '$interval',
    'alertService',
    '$cordovaNetwork',
    'utilityService',
    function ($rootScope, $scope, $timeout, $state, $stateParams, $ionicHistory,
        logger, device, user, $cordovaInAppBrowser, $mdBottomSheet, $cordovaGeolocation,
        api, bgGeo, $interval, alertService, $cordovaNetwork, utils) {

        logger.info("Caricamento HomeCtrl...");

        var vm = this;
        var currentBgGeoConfiguration = 'default';
        vm.openExternalLink = openExternalLink;
        vm.navigateTo = navigateTo;
        vm.showListBottomSheet = showListBottomSheet;
        vm.goToState = goToState;
        vm.goToDae = goToDae;
        vm.goToEvent = goToEvent;
        vm.callTo = callTo;
        vm.updateAvailability = updateAvailability;
        vm.alertText = "Nessuna emergenza in corso";
        vm.backgroundInterval = undefined;
        vm.gpsEnabled = false;

        $scope.$on('$ionicView.enter', function () {
            vm.navTitle = '<img alt="Logo" src="img/logo.png">';

            // controllo se l'utente è disponibile
            vm.isAvailable = user.isAvailable();

            /**
             * nascondo lo splashscreen 
             * mostro la status bar
             * nascondo il loader
             */
            device.hideSplashscreen();
            if (device.isCordova())
                StatusBar.show();
            device.hideLoader();

            /*
             Se l'utente è loggato setto il nome e cognome
             Se c'è una emergenza in gestione cambio la testo in home page
             altrimenti stop del blink e degli effetti sonori
             */
            vm.isLogged = user.isLogged();
            if (user.isLogged()) {
                vm.name = user.getUser().nome;
                vm.surname = user.getUser().cognome;
                alertService.resetEvent();
                alertService.clearEventList();
                api.getMyEvents().then(
                    function (data) {
                        alertService.addEvents(data);
                        logger.info(data);

                        initializeBGGeolocation();
                    },
                    function (error) {
                        logger.error(error);
                    }
                );
            } else {
                stopInterval();
            }
        });

        function initializeBGGeolocation() {
            logger.info("-- initializeBGGeolocation --");
            var options = {};
            if (device.isCordova()) {
                bgGeo.init().then(
                    function () {
                        vm.gpsEnabled = true;
                        var posOptions = {
                            timeout: 10000,
                            enableHighAccuracy: true,
                            maximumAge: 0
                        };
                        $cordovaGeolocation
                            .getCurrentPosition(posOptions)
                            .then(function (position) {
                                logger.info(position);
                                vm.accuracy = position.coords.accuracy;
                                vm.roundedAccuracy = vm.accuracy.toFixed();
                                vm.lat = position.coords.latitude;
                                vm.lng = position.coords.longitude;
                                user.setCurrentPosition(vm.lat, vm.lng);
                            }, function (error) {
                                logger.error(error);
                            });
                    },
                    function (error) {
                        logger.error(error);
                    }
                );

                if (alertService.isManaged()) {
                    currentBgGeoConfiguration = 'alert';
                    logger.info("--CARICO CONFIGURAZIONE PER EMERGENZA--", currentBgGeoConfiguration);
                } else {
                    currentBgGeoConfiguration = 'default';
                    logger.info("--CARICO CONFIGURAZIONE DI DEFAULT--", currentBgGeoConfiguration);
                }

                if (user.isLogged() && user.isAvailable()) {
                    bgGeo.configure(currentBgGeoConfiguration, bgGeolocationCallback, bgGeolocationErrorCallback);
                    bgGeo.start();
                }
                else {
                    bgGeo.stop();
                }
            }
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

        function updateAvailability(val) {
            device.showLoader();
            api.updateAvailabilityFR(val).then(
                function (data) {
                    device.hideLoader();
                    if (data.success) {
                        if (val) {
                            user.setAvailability(val);
                            if (device.isCordova()) {
                                bgGeo.start();
                            }
                            api.getMyEvents().then(
                                function (data) {
                                    alertService.addEvents(data);
                                },
                                function (error) {
                                    logger.error(JSON.stringify(error));
                                }
                            );
                        }
                        else {
                            if (device.isCordova()) {
                                bgGeo.stop();
                            }
                            stopInterval();
                        }
                    }
                    logger.info(data);
                },
                function (error) {
                    device.hideLoader();
                    device.alert(error.message, "balanced");
                    logger.error(error);
                }
            );
        }

        function openExternalLink(link) {
            var options = {
                location: 'no',
                clearcache: 'yes',
                toolbar: 'yes',
                zoom: 'yes',
                enableViewportScale: 'yes',
                closebuttoncaption: "Chiudi"
            };
            var target = '_blank';

            if (device.isAndroid()) {
                target = '_system';
            }
            $cordovaInAppBrowser.open(link, target, options);
        }

        function navigateTo(stateName) {
            $timeout(function () {
                if ($ionicHistory.currentStateName() != stateName) {
                    $ionicHistory.nextViewOptions({
                        disableAnimate: false,
                        disableBack: true
                    });
                    $state.go(stateName);
                }
            }, ($scope.isAnimated ? 300 : 0));
        }

        function showListBottomSheet($event) {
            $mdBottomSheet.show({
                templateUrl: 'templates/bottomSheet/chiama.html',
                targetEvent: $event,
                scope: $scope.$new(false)
            });
        }

        function callTo(number) {
            $mdBottomSheet.hide();
            $cordovaGeolocation
                .getCurrentPosition()
                .then(function (position) {
                    logger.info(position);
                    vm.lat = position.coords.latitude;
                    vm.lng = position.coords.longitude;
                    vm.accuracy = position.coords.accuracy
                    api.sendPositionToCO(vm.lat, vm.lng, vm.accuracy).then(
                        function (response) {
                            logger.info(response);
                            window.open("tel:" + number);
                        },
                        function (error) {
                            logger.error(error);
                            window.open("tel:" + number);
                        }
                    );
                }, function (error) {
                    logger.error(error);
                    window.open("tel:" + number);
                });
        }

        function goToState(state) {
            $state.go(state);
        }

        function goToDae() {
            if (device.isCordova()) {
                if ($cordovaNetwork.isOnline()) {
                    cordova.plugins.diagnostic.isLocationEnabled(function (enabled) {
                        if (enabled) {
                            if (vm.isLogged) {
                                $state.go("app.daeList");
                            }
                            else {
                                device.alert("Devi effettuare la login per accedere alla lista dei DAE", "balanced");
                            }
                        }
                        else {
                            device.alert("Devi attivare il GPS per accedere alla lista dei DAE", "balanced");
                        }
                    }, function (error) {
                        console.error("The following error occurred: " + error);
                    });
                }
                else {
                    device.alert("Devi attivare una connessione dati per accedere alla lista dei DAE", "balanced");
                }
            }
            else {
                if (vm.isLogged) {
                    $state.go("app.daeList");
                }
                else {
                    device.alert("Devi effettuare la login per accedere alla lista dei DAE", "balanced");
                }
            }
        }

        function goToEvent() {
            if (alertService.getEventId()) {
                stop();

                if (alertService.isClosed()) {
                    $state.go("app.interventionConfirmed", { eventId: alertService.getEventId() });
                } else if (alertService.isManaged()) {
                    $state.go("app.interventionDetail", { eventId: alertService.getEventId() });
                }
                else {
                    $state.go("app.interventions", { eventId: alertService.getEventId() });
                }

            }
        }

        function stopInterval() {
            var $div2blink = $("#divToBlink");
            vm.alertText = "Nessuna emergenza in corso";
            if (angular.isDefined(vm.backgroundInterval)) {
                $interval.cancel(vm.backgroundInterval);
                if ($div2blink.hasClass("alert")) {
                    $div2blink.toggleClass("alert");
                }
                stop();
                vm.backgroundInterval = undefined;
            }
        }

        function startInterval() {
            var $div2blink = $("#divToBlink");
            if (alertService.isManaged()) {
                vm.alertText = "Emergenza in gestione";
            }
            else {
                var partial = ($rootScope.eventCounter > 1) ? "Emergenze in corso" : "Emergenza in corso";
                vm.alertText = $rootScope.eventCounter + " " + partial;
            }
            if (!angular.isDefined(vm.backgroundInterval)) {
                play();
                vm.backgroundInterval = $interval(function () {
                    $div2blink.toggleClass("alert");
                }, 500);
            }
        }

        var media = null;

        function play() {
            if (device.isCordova()) {
                var mp3URL = getMediaURL("res/android_push_sound.mp3");
                media = new Media(mp3URL, null, null);
                media.setVolume(0.6);
                media.play({ playAudioWhenScreenIsLocked: true });
            }
            vm.started = true;

        }

        function stop() {
            if (device.isCordova()) {
                media.stop();
                media.release();
            }
            vm.started = false;
        }

        function getMediaURL(s) {
            if (device.isAndroid()) return "/android_asset/www/" + s;
            return s;
        }

        $rootScope.$watch('eventCounter', function (current, old) {
            if (current != null) {
                if (vm.isLogged && vm.isAvailable) {
                    if (current == 0) {
                        stopInterval();
                    }
                    else {
                        startInterval();
                    }
                }
            }
        });
    }
]);

