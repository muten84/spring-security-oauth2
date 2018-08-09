appControllers.controller('EventListCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$state',
    'deviceService',
    '$ionicHistory',
    function ($rootScope, $scope, logger, api, user, $timeout, $state, device, $ionicHistory) {
        logger.info("Caricamento EventListCtrl...");

        var vm = this;
        vm.goToState = goToState;

        $scope.$on('$ionicView.enter', function () {
            device.showLoader();
            getEventList();
        });



        function getEventList() {
            logger.info("[EventListCtrl:getEventList]", vm.damageReporting);
            api.getEventList().then(
                function(data){
                    device.hideLoader();
                    logger.info(JSON.stringify(data));
                    vm.events = data;
                },
                function(error){
                    device.hideLoader();
                    logger.error(JSON.stringify(error));
                    device.alert(error.message, "balanced").then(
                        function(){
                            $state.go("app.home");
                        }
                    );

                }
            );

        }

        function goToState(state) {
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go(state);
        }


    }]);