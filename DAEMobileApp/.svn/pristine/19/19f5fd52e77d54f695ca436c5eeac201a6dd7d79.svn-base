appServices.factory('BackgroundGeolocationService', [
    '$rootScope',
    '$q',
    'localStorageService',
    'loggerService',
    'deviceService',
    function ($rootScope, $q, storage, logger, device) {
        //Enable background geolocation
        // var options = {
        //     stationaryRadius: 20,
        //     distanceFilter: 30,
        //     desiredAccuracy: 0,
        //     debug: false,
        //     locationService: 'ANDROID_DISTANCE_FILTER',
        //     stopOnTerminate: false,
        //     notificationTitle: 'Background tracking',
        //     notificationText: 'enabled',
        //     notificationIconColor: '#FEDD1E',
        //     notificationIconLarge: 'mappointer_large',
        //     notificationIconSmall: 'mappointer_small',
        //     locationProvider: backgroundGeolocation.provider.ANDROID_DISTANCE_FILTER_PROVIDER,
        //     interval: 10000,
        //     fastestInterval: 5000,
        //     activitiesInterval: 10000,
        //     startOnBoot: false,
        //     startForeground: true,
        //     stopOnStillActivity: true,
        //     activityType: 'AutomotiveNavigation',
        //     url: 'http://192.168.81.15:3000/locations',
        //     syncUrl: 'http://192.168.81.15:3000/sync',
        //     syncThreshold: 100,
        //     httpHeaders: {
        //         'X-FOO': 'bar'
        //     },
        //     pauseLocationUpdates: false,
        //     saveBatteryOnBackground: false,
        //     maxLocations: 100
        // };
        var started = false;
        var currentBgGeoConfiguration = 'default';
        return {
            init: function () {
                var q = $q.defer();
                device.checkLocationAuthorizationStatus().then(
                    function (status) {
                        logger.info("checkLocationAuthorizationStatus: ", status);
                        if (status) {
                            device.checkIsLocationAvailable().then(
                                function (status) {
                                    logger.info("checkIsLocationAvailable: ", status);
                                    if (status) {
                                        q.resolve();
                                    }
                                    else {
                                        q.reject();
                                    }
                                },
                                function (error) {
                                    logger.error("checkIsLocationAvailable: ", error);
                                    q.reject(error);
                                }
                            );
                        }
                        else {
                            q.reject();
                        }
                    },
                    function (error) {
                        logger.error("checkLocationAuthorizationStatus: ", error);
                        q.reject(error);
                    }
                );
                return q.promise;
            },

            setCurrentBgGeoConfiguration: function(conf){
                currentBgGeoConfiguration = conf;
            },

            getCurrentBgGeoConfiguration: function(){
                return currentBgGeoConfiguration;
            },

            configure: function (conf, successCallback, errorCallBack) {
                logger.info("BACKGROUND GEOLOCATION CONFIGURATION");
                this.setCurrentBgGeoConfiguration(conf);
                backgroundGeoLocation.configure(
                    successCallback,
                    errorCallBack,
                    $rootScope.config.BackgroundGeolocation[conf]
                    // function (result) {
                    //     result.status = true;
                    //     q.notify(result);
                    //     backgroundGeoLocation.finish();
                    // },
                    // function (err) {
                    //     var error = {};
                    //     error.status = false;
                    //     error.result = err;
                    //     q.notify(error);
                    // },
                );
            },

            start: function () {
                var q = $q.defer();
                if (started) {
                    q.resolve();
                }
                backgroundGeoLocation.isLocationEnabled(
                    function (enabled) {
                        if (enabled) {
                            backgroundGeoLocation.start(
                                function (result) {
                                    logger.info("BACKGROUND GEOLOCATION STARTED");
                                    started = true;
                                    q.resolve(result);
                                },
                                function (error) {
                                    if (error.code === 2) {
                                        q.reject('Permission denied');
                                    } else {
                                        q.reject('error.message');
                                    }
                                });
                        }
                        else {
                            q.reject('Location Services are disabled');
                        }

                    },
                    function (error) {
                        q.reject('Error detecting status of location settings');
                    }
                );

                return q.promise;
            },

            stop: function () {
                var q = $q.defer();
                if (!started) {
                    q.resolve();
                }
                backgroundGeoLocation.stop(
                    function (result) {
                        logger.info("BACKGROUND GEOLOCATION STOPPED");
                        q.resolve(result);
                    },
                    function (err) {
                        q.reject(err);
                    });
                started = false;
                return q.promise;
            },

            isStarted: function () {
                return started;
            },

            changeConfiguration: function (conf, successCallback, errorCallBack) {
                logger.info("CHANGE BACKGROUND GEOLOCATION CONFIGURATION");
                var _this = this;
                _this.setCurrentBgGeoConfiguration(conf);
                _this.stop()
                    .then(function(){
                        _this.configure(conf, successCallback, errorCallBack);
                        _this.start();
                    });
            }
        };
    }]);
