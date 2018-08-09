appControllers.controller('InterventionConfirmedCtrl', [
    '$rootScope',
    '$scope',
    '$state',
    'loggerService',
    '$timeout',
    '$ionicHistory',
    '$cordovaInAppBrowser',
    'utilityService',
    'BackgroundGeolocationService',
    'alertService',
    function ($rootScope, $scope, $state, logger, $timeout, $ionicHistory, $cordovaInAppBrowser, utils, bgGeo, alertService) {
        logger.info("Caricamento InterventionConfirmedCtrl...");

        var vm = this;
        vm.navigateTo = navigateTo;
        vm.openExternalLink = openExternalLink;

        $scope.$on('$ionicView.enter', function () {
            // if ($ionicHistory.backView().stateName != "app.home") {
            //     $ionicHistory.backView().stateName = "app.home";
            // }
            if ($ionicHistory.backView().stateName == "app.interventionDetail"){
                $ionicHistory.removeBackView();
            }
            vm.event = alertService.getCurrentEvent();

            bgGeo.changeConfiguration(bgGeo.getCurrentBgGeoConfiguration(), bgGeolocationCallback, bgGeolocationErrorCallback);
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

        function goToState(stateName) {
            $state.go(stateName);
        }

        function navigateTo(stateName) {
            $ionicHistory.nextViewOptions({
               disableAnimate: true, 
                disableBack: false
            });
            $state.go(stateName);
        }

        function openExternalLink(link) {
            var options = {
                location: 'no',
                clearcache: 'yes',
                toolbar: 'yes',
                closebuttoncaption: "Chiudi"
            };
            $cordovaInAppBrowser.open(link, '_blank', options)
                .then(function (event) {
                    // success
                })
                .catch(function (event) {
                    // error
                });
        }
    }]);