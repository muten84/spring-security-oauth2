appControllers.controller('DaeDetailCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$state',
    '$stateParams',
    'deviceService',
    function ($rootScope ,$scope, logger, api, user, $timeout, $state, $stateParams, device) {
        logger.info("Caricamento DaeDetailCtrl...");

        var vm = this;
        vm.openMap = openMap;
        vm.goToState = goToState;
        vm.isOpen = false;

        $scope.$on('$ionicView.enter', function () {
            vm.dae = $stateParams.dae;
            // vm.dae.disponibilitaPermanente = true;
            vm.tabId = $stateParams.tabId;
            logger.info(JSON.stringify(vm.dae));
        });

        $scope.$watch('vm.isOpen', function(isOpen) {
            if (isOpen) {
                $timeout(function() {
                    vm.tooltipVisible = vm.isOpen;
                }, 600);
            } else {
                vm.tooltipVisible = vm.isOpen;
            }
        });

        function openMap() {
            var targetDestinationLocation = vm.dae.gpsLocation.latitudine+","+vm.dae.gpsLocation.longitudine;
            if (device.isAndroid()) {
                window.open('geo:?q=' + targetDestinationLocation + '&z=15', '_system');
            }
            else{
                window.open('maps://?q=' + targetDestinationLocation, '_system');
            }
        }

        function goToState(state) {
            $state.go(state, {dae: vm.dae, tabId: vm.tabId});
        }
    }]);