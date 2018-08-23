appControllers.controller('DaeMapDetailCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$stateParams',
    'NgMap',
    'deviceService',
    function ($rootScope ,$scope, logger, api, user, $timeout, $stateParams, NgMap, device) {
        logger.info("Caricamento DaeMapDetailCtrl...");

        var vm = this;
        vm.showInfo = showInfo;
        vm.openMap = openMap;

        vm.map = null;
        vm.gmUrl = $rootScope.config.GoogleMapsUrl;

        $scope.$on('$ionicView.enter', function () {
            vm.dae = $stateParams.dae;
            logger.info(JSON.stringify(vm.dae));

            NgMap.getMap('customMap').then(
                function (map) {
                    vm.map = map;
                },
                function (error) {
                    device.hideLoader();
                    logger.info(error);
                }
            );
        });

        function showInfo(event, id) {
            vm.map.showInfoWindow('marker-info', this);
        }

        function openMap(lat, long) {
            var targetDestinationLocation = lat+","+long;
            if (device.isAndroid()) {
                window.open('geo:?q=' + targetDestinationLocation + '&z=15', '_system');
            }
            else{
                window.open('maps://?q=' + targetDestinationLocation, '_system');
            }
        }

    }]);