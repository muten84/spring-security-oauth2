appControllers.controller('InterventionDetailCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$stateParams',
    '$state',
    'deviceService',
    '$cordovaGeolocation',
    'googleMapsFactory',
    '$q',
    '$ionicHistory',
    'eventData',
    function ($rootScope ,$scope, logger, api, user, $timeout, $stateParams, $state, device, $cordovaGeolocation, googleMapsFactory, $q, $ionicHistory, eventData) {
        logger.info("Caricamento InterventionDetailCtrl...");

        var vm = this;
        vm.openMap = openMap;
        vm.openGoogleMap = openGoogleMap;
        vm.goToDae = goToDae;

        $scope.$on('$ionicView.loaded', function () {
            vm.event = eventData;
        });

        $scope.$on('$ionicView.enter', function () {
            logger.info($stateParams);
            logger.info($ionicHistory.backView());
            if ($ionicHistory.backView().stateName == "app.interventions"){
                $ionicHistory.removeBackView();
            }
            vm.event = $state.$current.locals["menuContent@app"].eventData;
            // vm.event.arrivalTime = null;
            // if (vm.event.dataArrivoAmbulanza && vm.event.tempoArrivoAmbulanza){
            //     vm.event.arrivalTime = moment(vm.event.dataArrivoAmbulanza).add(vm.event.tempoArrivoAmbulanza, 'minutes').format('DD/MM/YYYY HH:mm');
            // }
        });

        function openMap() {
            var targetDestinationLocation = vm.event.coordinate.latitudine+","+vm.event.coordinate.longitudine;
            if (device.isAndroid()) {
                window.open('geo:?q=' + targetDestinationLocation + '&z=15', '_system');
            }
            else{
                window.open('maps://?q=' + targetDestinationLocation, '_system');
            }
        }

        function openGoogleMap() {
            var targetDestinationLocation = vm.event.latitude+","+vm.event.longitude;
            window.open('comgooglemaps://?q=' + targetDestinationLocation + '&zoom=15', '_system');
        }

        function goToDae(state) {
            $state.go(state, {event: vm.event});
        }

    }]);