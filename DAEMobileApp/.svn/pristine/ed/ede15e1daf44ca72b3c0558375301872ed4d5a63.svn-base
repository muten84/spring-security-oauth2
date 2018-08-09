appServices.factory("utilityService", [
    '$rootScope',
    'loggerService',
    'localStorageService',
    'loggerService',
    'apiService',
    'alertService',
    'deviceService',
    '$state',
    'userService',
    '$q',
    function ($rootScope, loggerService, storage, logger, api, alertService, device, $state, user, $q) {

        var _arrivalConfirmOpened = false;

        var utility = {
            /* HAVERSINE FORMULA */
            calculateDistance: function (destination, currentLocation) {
                var R = 6371; // Radius of the earth in km
                var dLat = utility.deg2rad(destination.latitude - currentLocation.latitude);  // deg2rad below
                var dLon = utility.deg2rad(destination.longitude - currentLocation.longitude);
                var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(utility.deg2rad(currentLocation.latitude)) * Math.cos(utility.deg2rad(currentLocation.longitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                var d = R * c; // Distance in km
                var distance = Math.round(d * 1000); // Distance in m
                return distance;
            },

            deg2rad: function (deg) {
                return deg * (Math.PI / 180)
            },

            manageEmergencyEvent: function (location) {
                var q = $q.defer();
                //se l'evento non è stato già chiuso dal FR
                if (alertService.isManaged() && !alertService.isPlaceArrival()) {
                    var destination = alertService.getLocation();
                    $rootScope.distanceFromEvent = utility.calculateDistance(destination, location);
                    if ($rootScope.distanceFromEvent < $rootScope.config.minDistanceFromEvent && !_arrivalConfirmOpened) {
                        _arrivalConfirmOpened = true;
                        var data = {};
                        data.eventId = alertService.getEventId();
                        data.lat = alertService.getLocation().latitude;
                        data.lng = alertService.getLocation().longitude;
                        device.sendLocalNotification(data);

                        device.alert("LOCAL_NOTIFICATION_TEXT", "balanced").then(
                            function () {
                                _arrivalConfirmOpened = false;
                                device.showLoader();
                                api.placeArrival(data).then(
                                    function (data) {
                                        logger.info(JSON.stringify(data));
                                        device.hideLoader();
                                        // non cancello la lista degli eventi sull'arrivo sul luogo
                                        // alertService.resetEvent();
                                        // alertService.clearEventList();
                                        alertService.getCurrentEvent().frCloseDate = new Date();

                                        q.resolve();
                                    },
                                    function (error) {
                                        logger.info(JSON.stringify(error));
                                        device.hideLoader();
                                        q.reject();
                                    }
                                );
                            });
                    }
                }
                return q.promise;
            },

            updateFRLocation: function (location) {
                user.setCurrentPosition(location.latitude, location.longitude);
                location.distanza = location.distanceFromEvent;
                api.updateFRLocation(location).then(
                    function (data) {
                        var now = new Date();
                        location.time = now.getTime();
                        location.response = data.response;
                        user.updateFRLocations(location);
                        logger.info(data);
                        $rootScope.$broadcast("UpdateFRLocation");
                    },
                    function (error) {
                        var now = new Date();
                        location.time = now.getTime();
                        location.response = false;
                        user.updateFRLocations(location);
                        logger.error(error);
                        $rootScope.$broadcast("UpdateFRLocation");
                    }
                );
            }


        };

        return utility;
    }
]);