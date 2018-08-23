appControllers.controller('TestLocationCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'deviceService',
    '$state',
    'userService',
    'BackgroundGeolocationService',
    'utilityService',
    'alertService',
    function ($rootScope, $scope, logger, device, $state, user, bgGeo, utils, alertService) {
        logger.info("Caricamento TestLocationCtrl...");

        var vm = this;
        vm.locations = [];
        vm.geoconf = 'default';
        vm.resetLocations = resetLocations;
        vm.onChange = onChange;

        $scope.$on('$ionicView.loaded', function () {

        });

        $scope.$on('$ionicView.enter', function () {
            if (device.isCordova()) {
                getLocations();
            }
            vm.geoconf = bgGeo.getCurrentBgGeoConfiguration()
        });

        $scope.$on('UpdateFRLocation', function (event, data) {
            if (device.isCordova()) {
                getLocations();
            }
        });

        function onChange(conf) {
            logger.info(conf);
            resetLocations();
            bgGeo.changeConfiguration(conf, bgGeolocationCallback, bgGeolocationErrorCallback);
        }

        function bgGeolocationErrorCallback(error) {
            logger.info("--BGGEOLOCATION ERROR CALLBACK--", error);
        }

        function bgGeolocationCallback(data) {
            logger.info("--BGGEOLOCATION CALLBACK--", location);
            var location = {};
            location.accuracy = data.accuracy.toFixed();
            location.latitude = data.latitude;
            location.longitude = data.longitude;
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

        function getLocations() {
            device.showLoader();
            vm.locations = user.getFRLocations();
            logger.info(vm.locations);
            device.hideLoader();

            // backgroundGeolocation.getValidLocations(function (locations) {
            //     vm.locations = locations;
            //     logger.info(vm.locations);
            //     device.hideLoader();
            // })
        }

        function resetLocations() {
            device.showLoader();
            user.resetFRLocations();
            vm.locations = user.getFRLocations();
            device.hideLoader();
            $state.reload();
            // backgroundGeolocation.deleteAllLocations(function () {
            //     device.hideLoader();
            //     $state.reload();
            // })
        }

    }]);