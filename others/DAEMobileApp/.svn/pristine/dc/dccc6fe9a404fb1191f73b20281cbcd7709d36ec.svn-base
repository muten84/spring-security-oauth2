angular.module(_APP_).config([
    '$stateProvider',
    '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        //$stateProvider is using for add or edit HTML view to navigation bar.
        //
        //Schema :
        //state_name(String)      : Name of state to use in application.
        //page_name(String)       : Name of page to present at localhost url.
        //cache(Bool)             : Cache of view and controller default is true. Change to false if you want page reload when application navigate back to this view.
        //html_file_path(String)  : Path of html file.
        //controller_name(String) : Name of Controller.
        //
        //Learn more about ionNavView at http://ionicframework.com/docs/api/directive/ionNavView/
        //Learn more about  AngularUI Router's at https://github.com/angular-ui/ui-router/wiki
        $stateProvider
            .state('app', {
                url: "/app",
                abstract: true,
                templateProvider: function ($templateCache) {
                    return $templateCache.get('menu.html');
                },
                controller: 'MenuCtrl',
                controllerAs: 'vm'
            })
            .state('app.login', {
                url: "/login",
                params: {
                    isAnimated: false
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('login.html');
                        },
                        controller: 'LoginCtrl',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.registration', {
                url: "/registration",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('registration.html');
                        },
                        controller: 'RegistrationCtrl',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.profile', {
                url: "/profile",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('profile.html');
                        },
                        controller: "ProfileCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.updateProfile', {
                url: "/updateProfile",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('updateProfile.html');
                        },
                        controller: "UpdateProfileCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.changePassword', {
                url: "/changePassword",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('changePassword.html');
                        },
                        controller: 'ChangePasswordCtrl',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.home', {
                url: "/home",
                params: {
                    isAnimated: false
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('home.html');
                        },
                        controller: 'HomeCtrl',
                        controllerAs: 'vm',
                        resolve: {
                            preloadData: [
                                '$rootScope', '$state', 'userService', 'loggerService', 'apiService',
                                function ($rootScope, $state, user, logger, api) {

                                    // Se ho già una sessione attiva proseguo
                                    if (!user.isLogged()) {
                                        return user.restartSession().then(
                                            function () {
                                                return api.getLoggedFirstResponderDetails().then(
                                                    function (profile) {
                                                        logger.info(profile);
                                                        user.setUser(profile);
                                                    },
                                                    function (error) {
                                                        logger.error(error);
                                                    }
                                                );
                                            },
                                            function (error) {
                                                if (error) {
                                                    // se  avvenuto un errore in fase di login 
                                                    // carico la pagina di errore
                                                    $state.go("app.errorPage", { error: error });
                                                }
                                            }
                                        );
                                    }
                                    return true;
                                }
                            ]
                        }
                    }
                }
            })
            .state('app.daeList', {
                url: "/daelist",
                params: {
                    'event': null,
                    'tabId': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('dae-list.html');
                        },
                        controller: "DaeListCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.alertDaeList', {
                url: "/alertDaeList",
                params: {
                    'event': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('alert-dae-list.html');
                        },
                        controller: "AlertDaeListCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.newDae', {
                url: "/newDae",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('new-dae.html');
                        },
                        controller: "NewDaeCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.daeDetail', {
                url: "/daeDetail",
                params: {
                    'dae': null,
                    'tabId': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('dae-detail.html');
                        },
                        controller: "DaeDetailCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.damageReporting', {
                url: "/damageReporting",
                params: {
                    'dae': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('damageReporting.html');
                        },
                        controller: "DamageReportingCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.eventList', {
                url: "/eventList",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('eventList.html');
                        },
                        controller: "EventListCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.settings', {
                url: "/settings",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('settings.html');
                        },
                        controller: "SettingsCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.daeMapDetail', {
                url: "/daeMapDetail",
                params: {
                    'dae': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('daeMapDetail.html');
                        },
                        controller: "DaeMapDetailCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.interventions', {
                url: "/interventions",
                params: {
                    'eventId': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('interventionRequests.html');
                        },
                        controller: "InterventionRequestsCtrl",
                        cache: false,
                        controllerAs: 'vm',
                        resolve: {
                            eventData: function ($q, $cordovaGeolocation, deviceService, $stateParams, googleMapsFactory, loggerService, apiService, userService) {
                                var myLocation = {};
                                return checkCurrentLocation().then(
                                    function (position) {
                                        myLocation.lat = position.coords.latitude;
                                        myLocation.lng = position.coords.longitude;
                                        return getEvent($stateParams.eventId).then(
                                            function (data) {
                                                data.currentPosition = myLocation;
                                                return data;
                                            },
                                            function (error) {
                                                device.alert("Servizio momentaneamente non disponibile, riprova", "balanced");
                                                return $q.reject();
                                            }
                                        );
                                    },
                                    function (error) {
                                        myLocation = userService.getCurrentPosition();
                                        if (_.isEmpty(myLocation)) {
                                            deviceService.alert("Connessione dati o GPS non attivi", "balanced");
                                            return $q.reject();
                                        }
                                        else {
                                            return getEvent($stateParams.eventId).then(
                                                function (data) {
                                                    data.currentPosition = myLocation;
                                                    return data;
                                                },
                                                function (error) {
                                                    deviceService.alert("Servizio momentaneamente non disponibile, riprova", "balanced");
                                                    return $q.reject();
                                                }
                                            );
                                        }

                                    }
                                );

                                function getEvent(eventId) {
                                    deviceService.showLoader();
                                    var q = $q.defer();
                                    var obj = null;
                                    apiService.getEventById(eventId).then(
                                        function (data) {
                                            loggerService.info("getEvent: " + JSON.stringify(data));
                                            obj = data;
                                            googleMapsFactory.then(
                                                function () {
                                                    var service = new google.maps.DistanceMatrixService();
                                                    service.getDistanceMatrix(
                                                        {
                                                            origins: [myLocation.lat + "," + myLocation.lng],
                                                            destinations: [obj.coordinate.latitudine + "," + obj.coordinate.longitudine],
                                                            travelMode: 'DRIVING',
                                                            unitSystem: google.maps.UnitSystem.METRIC,
                                                            avoidHighways: false,
                                                            avoidTolls: false
                                                        }, callback);

                                                    function callback(response, status) {
                                                        deviceService.hideLoader();
                                                        loggerService.info("service.getDistanceMatrix response", response);
                                                        loggerService.info("service.getDistanceMatrix status", status);
                                                        if (status == "OK") {
                                                            obj.distance = response.rows[0].elements[0].distance.text;
                                                        }
                                                        else {
                                                            obj.distance = "ND";
                                                        }
                                                        q.resolve(obj);
                                                    }
                                                }
                                            );
                                        },
                                        function (error) {
                                            deviceService.hideLoader();
                                            loggerService.error(error)
                                            q.reject();
                                        }
                                    );
                                    return q.promise;
                                }

                                function checkCurrentLocation() {
                                    var q = $q.defer();
                                    var posOptions = {
                                        timeout: 10000,
                                        enableHighAccuracy: true,
                                        maximumAge: 0
                                    };
                                    if (deviceService.isCordova()) {
                                        deviceService.checkIsLocationAvailable().then(
                                            function (status) {
                                                if (status) {
                                                    $cordovaGeolocation
                                                        .getCurrentPosition(posOptions)
                                                        .then(function (position) {
                                                            loggerService.info(position);
                                                            q.resolve(position);
                                                        }, function (error) {
                                                            q.reject(error);
                                                            loggerService.error(error);
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
                                                loggerService.info(position);
                                                q.resolve(position);
                                            }, function (error) {
                                                q.reject(error);
                                                loggerService.error(error);
                                            });
                                    }
                                    return q.promise;
                                }
                            }
                        }
                    }
                }
            })
            .state('app.interventionDetail', {
                url: "/interventionDetail",
                params: {
                    'eventId': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('interventionDetail.html');
                        },
                        controller: "InterventionDetailCtrl",
                        controllerAs: 'vm',
                        resolve: {
                            eventData: function ($q, $cordovaGeolocation, deviceService, $stateParams, googleMapsFactory, loggerService, apiService, userService) {
                                var myLocation = {};
                                return checkCurrentLocation().then(
                                    function (position) {
                                        myLocation.lat = position.coords.latitude;
                                        myLocation.lng = position.coords.longitude;
                                        return getEvent($stateParams.eventId).then(
                                            function (data) {
                                                data.currentPosition = myLocation;
                                                return data;
                                            },
                                            function (error) {
                                                deviceService.alert("Servizio momentaneamente non disponibile, riprova", "balanced");
                                                return $q.reject();
                                            }
                                        );
                                    },
                                    function (error) {
                                        myLocation = userService.getCurrentPosition();
                                        if (_.isEmpty(myLocation)) {
                                            deviceService.alert("Connessione dati o GPS non attivi", "balanced");
                                            return $q.reject();
                                        }
                                        else {
                                            return getEvent($stateParams.eventId).then(
                                                function (data) {
                                                    data.currentPosition = myLocation;
                                                    return data;
                                                },
                                                function (error) {
                                                    deviceService.alert("Servizio momentaneamente non disponibile, riprova", "balanced");
                                                    return $q.reject();
                                                }
                                            );
                                        }
                                    }
                                );

                                function getEvent(eventId) {
                                    var q = $q.defer();
                                    deviceService.showLoader();
                                    var obj = null;
                                    apiService.getEventById(eventId).then(
                                        function (data) {
                                            loggerService.info("getEvent: " + JSON.stringify(data));
                                            obj = data;
                                            googleMapsFactory.then(
                                                function () {
                                                    var service = new google.maps.DistanceMatrixService();
                                                    service.getDistanceMatrix(
                                                        {
                                                            origins: [myLocation.lat + "," + myLocation.lng],
                                                            destinations: [obj.coordinate.latitudine + "," + obj.coordinate.longitudine],
                                                            travelMode: 'DRIVING',
                                                            unitSystem: google.maps.UnitSystem.METRIC,
                                                            avoidHighways: false,
                                                            avoidTolls: false
                                                        }, callback);

                                                    function callback(response, status) {
                                                        deviceService.hideLoader();
                                                        loggerService.info("service.getDistanceMatrix response", response);
                                                        loggerService.info("service.getDistanceMatrix status", status);
                                                        if (status == "OK") {
                                                            obj.distance = response.rows[0].elements[0].distance.text;
                                                        }
                                                        else {
                                                            obj.distance = "ND";
                                                        }
                                                        q.resolve(obj);
                                                    }
                                                }
                                            );
                                        },
                                        function (error) {
                                            deviceService.hideLoader();
                                            loggerService.error(error)
                                            q.reject();
                                        }
                                    );
                                    return q.promise;
                                }

                                function checkCurrentLocation() {
                                    var q = $q.defer();
                                    var posOptions = {
                                        timeout: 10000,
                                        enableHighAccuracy: true,
                                        maximumAge: 0
                                    };
                                    if (deviceService.isCordova()) {
                                        deviceService.checkIsLocationAvailable().then(
                                            function (status) {
                                                if (status) {
                                                    $cordovaGeolocation
                                                        .getCurrentPosition(posOptions)
                                                        .then(function (position) {
                                                            loggerService.info(position);
                                                            q.resolve(position);
                                                        }, function (error) {
                                                            q.reject(error);
                                                            loggerService.error(error);
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
                                                loggerService.info(position);
                                                q.resolve(position);
                                            }, function (error) {
                                                q.reject(error);
                                                loggerService.error(error);
                                            });
                                    }
                                    return q.promise;
                                }
                            }
                        }
                    }
                }
            })
            .state('app.interventionConfirmed', {
                url: "/interventionConfirmed",
                params: {
                    'eventId': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('interventionConfirmed.html');
                        },
                        controller: "InterventionConfirmedCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.rhythm', {
                url: "/rhythm",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('rhythm.html');
                        },
                        controller: "RhythmCtrl",
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.update', {
                url: "/update",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('update.html');
                        },
                        controller: 'UpdateCtrl',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('app.testLocation', {
                url: "/testLocation",
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('testLocation.html');
                        },
                        controller: "TestLocationCtrl",
                        controllerAs: 'vm'
                    }
                }
            }).state('app.errorPage', {
                url: "/errorPage",
                params: {
                    'error': null
                },
                views: {
                    'menuContent': {
                        templateProvider: function ($templateCache) {
                            return $templateCache.get('errorPage.html');
                        },
                        controller: "ErrorPageCtrl",
                        controllerAs: 'vm'
                    }
                }
            });// End $stateProvider

        //Use $urlRouterProvider.otherwise(Url);
        $urlRouterProvider.otherwise(window.globalVariable.startPage.url);
    }]);