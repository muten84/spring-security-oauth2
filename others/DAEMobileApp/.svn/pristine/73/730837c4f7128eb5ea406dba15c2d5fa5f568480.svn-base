// Controller of menu toggle.
// Learn more about Sidenav directive of angular material
// https://material.angularjs.org/latest/#/demo/material.components.sidenav
appControllers.controller('MenuCtrl', [
    '$scope',
    '$rootScope',
    '$timeout',
    '$mdUtil',
    '$mdSidenav',
    'loggerService',
    '$ionicHistory',
    '$state',
    '$ionicPlatform',
    '$mdDialog',
    '$mdBottomSheet',
    '$mdMenu',
    '$mdSelect',
    'deviceService',
    'userService',
    '$cordovaInAppBrowser',
    'alertService',
    'apiService',
    '$cordovaNetwork',
    'BackgroundGeolocationService',
    'utilityService',
    function ($scope, $rootScope, $timeout, $mdUtil, $mdSidenav, logger, $ionicHistory,
        $state, $ionicPlatform, $mdDialog, $mdBottomSheet, $mdMenu, $mdSelect,
        device, user, $cordovaInAppBrowser, alertService, api, $cordovaNetwork, bgGeo, utils) {
        logger.info("Caricamento MenuCtrl...");

        var vm = this;
        vm.logout = logout;
        vm.openExternalLink = openExternalLink;
        vm.navigateTo = navigateTo;
        vm.goToDae = goToDae;
        vm.closeSideNav = closeSideNav;
        vm.goToState = goToState;
        vm.appVersion = _VERSION_;

        $scope.$on('$ionicView.enter', function () {
            vm.isLogged = user.isLogged();
            if (user.isLogged()) {
                vm.name = user.getUser().nome;
                vm.surname = user.getUser().cognome;
                vm.email = user.getUser().email;
            }
        });

        vm.toggleLeft = buildToggler('left');

        // buildToggler is for create menu toggle.
        // Parameter :
        // navID = id of navigation bar.
        function buildToggler(navID) {
            var debounceFn = $mdUtil.debounce(function () {
                $mdSidenav(navID).toggle();
            }, 0);
            return debounceFn;
        };// End buildToggler.

        /**
         * Chiude la sessione dell'utente previa conferma
         */
        function logout() {

            device.confirm("CONFIRM_LOGOUT", {
                okText: "SI",
                okType: "button-balanced"
            }).then(function (confirmed) {
                if (confirmed) {
                    device.showLoader();
                    api.logout().then(
                        function () {
                            user.closeSession();
                            alertService.clearEventList();
                            $timeout(function () {
                                if ($ionicHistory.currentStateName() != window.globalVariable.startPage.state) {
                                    navigateTo("app.home");
                                }
                                else {
                                    $state.reload();
                                }
                                $mdSidenav('left').close();
                            }, 1000);
                        },
                        function () {
                            device.alert("Servizio momentaneamente non disponibile", "balanced").then(
                                function () {
                                    $mdSidenav('left').close();
                                }
                            );
                        }
                    );
                }
                else {
                    $mdSidenav('left').close();
                }
            });
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

            $mdSidenav('left').close();
            $cordovaInAppBrowser.open(link, target, options)
                .then(function (event) {
                    // success
                })
                .catch(function (event) {
                    // error
                });
        }

        function navigateTo(stateName) {
            $timeout(function () {
                $mdSidenav('left').close();
                if ($ionicHistory.currentStateName() != stateName) {
                    $ionicHistory.nextViewOptions({
                        disableAnimate: true,
                        disableBack: true
                    });
                    $state.go(stateName);
                }
            }, ($scope.isAndroid == false ? 300 : 0));
        }

        function goToState(stateName) {
            $timeout(function () {
                $mdSidenav('left').close();
                $ionicHistory.nextViewOptions({
                    disableAnimate: true,
                    disableBack: false
                });
                $state.go(stateName);
            }, ($scope.isAndroid == false ? 300 : 0));
        }

        function goToDae() {
            if (device.isCordova()) {
                if ($cordovaNetwork.isOnline()) {
                    cordova.plugins.diagnostic.isLocationEnabled(function (enabled) {
                        if (enabled) {
                            if (vm.isLogged) {
                                $timeout(function () {
                                    $mdSidenav('left').close();
                                    $ionicHistory.nextViewOptions({
                                        disableAnimate: true,
                                        disableBack: false
                                    });
                                    $state.go("app.daeList");
                                }, ($scope.isAndroid == false ? 300 : 0));
                            }
                            else {
                                $mdSidenav('left').close();
                                $timeout(function () {
                                    device.alert("Devi effettuare la login per accedere alla lista dei DAE", "balanced");
                                }, 300);
                            }
                        }
                        else {
                            $mdSidenav('left').close();
                            $timeout(function () {
                                device.alert("Devi attivare il GPS per accedere alla lista dei DAE", "balanced");
                            }, 300);
                        }
                    }, function (error) {
                        console.error("The following error occurred: " + error);
                    });
                }
                else {
                    $mdSidenav('left').close();
                    $timeout(function () {
                        device.alert("Devi attivare una connessione dati per accedere alla lista dei DAE", "balanced");
                    }, 300);
                }
            }
            else {
                if (vm.isLogged) {
                    $mdSidenav('left').close();
                    $state.go("app.daeList");
                }
                else {
                    device.alert("Devi effettuare la login per accedere alla lista dei DAE", "balanced");
                }
            }
        }

        function closeSideNav() {
            $mdSidenav('left').close();
        }

        $rootScope.$on('$cordovaLocalNotification:click', function (event, notification, state) {
            logger.info("$CORDOVALOCALNOTIFICATION:CLICK: " + JSON.stringify(notification));
            var data = JSON.parse(notification.data);

            logger.info("data: " + JSON.stringify(data));
            device.showLoader();
            api.placeArrival(data).then(
                function (data) {
                    logger.info(JSON.stringify(data));
                    device.hideLoader();
                    //       alertService.resetEvent();
                    //      alertService.clearEventList();
                    alertService.getCurrentEvent().frCloseDate = new Date();
                    bgGeo.changeConfiguration(bgGeo.getCurrentBgGeoConfiguration(), bgGeolocationCallback, bgGeolocationErrorCallback);
                    $state.go("app.interventionConfirmed");
                },
                function (error) {
                    logger.info(JSON.stringify(error));
                    device.hideLoader();
                }
            );

        });

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

        $scope.$on("NewAlertNotification", function (event, data) {
            logger.info("MenuCtrl - NewAlertNotification");
            if (user.isLogged() && user.isAvailable) {
                if (!alertService.isManaged()) {
                    if ($ionicHistory.currentStateName() != window.globalVariable.startPage.state) {
                        device.alert("Emergenza in corso", "balanced").then(
                            function () {
                                navigateTo("app.home");
                            }
                        );
                    }
                    else {
                        api.getMyEvents().then(
                            function (data) {
                                alertService.addEvents(data);
                            },
                            function (error) {
                                logger.info(error);
                            }
                        );
                    }
                }
            }
        });

        $scope.$on("UpdateAlertNotification", function (event, data) {
            logger.info("MenuCtrl - UpdateAlertNotification");
            logger.info("alertService.getEventId:" + alertService.getEventId());
            if (user.isLogged() && user.isAvailable) {
                if (alertService.getMessage()) {
                    device.alert(alertService.getMessage(), "balanced").then(
                        function () {
                            if (alertService.getEventId()) {
                                if (alertService.isManaged()) {
                                    if ($ionicHistory.currentStateName() != "app.interventionDetail") {
                                        $state.go("app.interventionDetail", { eventId: alertService.getEventId() });
                                    }
                                    else {
                                        $state.reload();
                                    }
                                }
                                else {
                                    if ($ionicHistory.currentStateName() != "app.interventions") {
                                        $state.go("app.interventions", { eventId: alertService.getEventId() });
                                    }
                                    else {
                                        $state.reload();
                                    }
                                }
                            }
                        }
                    );
                }
            }
        });

        $scope.$on("AbortAlertNotification", function (event, data) {
            logger.info("MenuCtrl - AbortAlertNotification");
            if (user.isLogged() && user.isAvailable) {
                device.alert(data.message, "balanced").then(
                    function () {
                        if ($ionicHistory.currentStateName() != window.globalVariable.startPage.state) {
                            navigateTo("app.home");
                        }
                    }
                );
            }
        });

        $scope.$on("ResumeApp", function (event, data) {
            logger.info("MenuCtrl - ResumeApp");
            var counter = $rootScope.eventCounter;
            if (user.isLogged() && user.isAvailable) {
                api.getMyEvents().then(
                    function (data) {
                        alertService.addEvents(data);
                        if (!data || data.length === 0) {
                            //se non ho nessun evento e mi trovo nella schermata del dettaglio intervento
                            if ($state.current.name == 'app.interventionConfirmed') {
                                navigateTo("app.home");
                            }
                        } else if (!alertService.isManaged()) {
                            if ($rootScope.eventCounter != 0 && counter < $rootScope.eventCounter) {
                                device.alert("Nuove emergenze in corso", "balanced").then(
                                    function () {
                                        if ($ionicHistory.currentStateName() != window.globalVariable.startPage.state) {
                                            navigateTo("app.home");
                                        }
                                    }
                                );
                            }
                        }
                    },
                    function (error) {
                        logger.error(error);
                    }
                );
            }
        });

        //  $ionicPlatform.registerBackButtonAction(callback, priority, [actionId])
        //
        //     Register a hardware back button action. Only one action will execute
        //  when the back button is clicked, so this method decides which of
        //  the registered back button actions has the highest priority.
        //
        //     For example, if an actionsheet is showing, the back button should
        //  close the actionsheet, but it should not also go back a page view
        //  or close a modal which may be open.
        //
        //  The priorities for the existing back button hooks are as follows:
        //  Return to previous view = 100
        //  Close side menu         = 150
        //  Dismiss modal           = 200
        //  Close action sheet      = 300
        //  Dismiss popup           = 400
        //  Dismiss loading overlay = 500
        //
        //  Your back button action will override each of the above actions
        //  whose priority is less than the priority you provide. For example,
        //  an action assigned a priority of 101 will override the ‘return to
        //  previous view’ action, but not any of the other actions.
        //
        //  Learn more at : http://ionicframework.com/docs/api/service/$ionicPlatform/#registerBackButtonAction

        $ionicPlatform.registerBackButtonAction(function () {

            if ($mdSidenav("left").isOpen()) {
                //If side navigation is open it will close and then return
                $mdSidenav('left').close();
            }
            else if (jQuery('md-bottom-sheet').length > 0) {
                //If bottom sheet is open it will close and then return
                $mdBottomSheet.cancel();
            }
            else if (jQuery('[id^=dialog]').length > 0) {
                //If popup dialog is open it will close and then return
                $mdDialog.cancel();
            }
            else if (jQuery('md-menu-content').length > 0) {
                //If md-menu is open it will close and then return
                $mdMenu.hide();
            }
            else if (jQuery('md-select-menu').length > 0) {
                //If md-select is open it will close and then return
                $mdSelect.hide();
            }

            else {

                // If control :
                // side navigation,
                // bottom sheet,
                // popup dialog,
                // md-menu,
                // md-select
                // is not opening, It will show $mdDialog to ask for
                // Confirmation to close the application or go to the view of lasted state.

                // Check for the current state that not have previous state.
                // It will show $mdDialog to ask for Confirmation to close the application.

                if ($ionicHistory.backView() == null) {

                    //Check is popup dialog is not open.
                    if (jQuery('[id^=dialog]').length == 0) {

                        // mdDialog for show $mdDialog to ask for
                        // Confirmation to close the application.

                        $mdDialog.show({
                            controller: 'DialogController',
                            templateUrl: 'templates/dialog/confirm.html',
                            targetEvent: null,
                            locals: {
                                displayOption: {
                                    title: _APPNAME_,
                                    content: "Vuoi chiudere l'applicazione?",
                                    ok: "Conferma",
                                    cancel: "Annulla"
                                }
                            }
                        }).then(function () {
                            //If user tap Confirm at the popup dialog.
                            //Application will close.
                            ionic.Platform.exitApp();
                        }, function () {
                            // For cancel button actions.
                        }); //End mdDialog
                    }
                }
                else {
                    //Go to the view of lasted state.
                    $ionicHistory.goBack();
                }
            }

        }, 100);
        //End of $ionicPlatform.registerBackButtonAction

    }])
    ; // End of menu toggle controller.