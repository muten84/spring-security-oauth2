appControllers.controller('ErrorPageCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'userService',
    '$timeout',
    '$state',
    'deviceService',
    '$ionicHistory', '$stateParams',
    function ($rootScope, $scope, logger,
        user, $timeout, $state, device, $ionicHistory, $stateParams) {
        logger.info("Caricamento ErrorPageCtrl...");

        var vm = this;
        vm.goToState = goToState;

        $scope.$on('$ionicView.enter', function () {

            vm.error = $stateParams.error;

            vm.message = "";
            if (vm.error) {
                if (vm.error.status) {
                    switch (vm.error.status) {
                        case 0:
                        case -1:
                            if (vm.error.xhrStatus === 'timeout') {
                                // richiesta abortita a causa del timeout
                                vm.message = $rootScope.config.settings.errors.timeout;
                            } else {
                                // errore di connessione sconosciuto
                                vm.message = $rootScope.config.settings.errors.connection;
                            }
                            break;
                        case 404:
                        case 500:
                            //errore sul server
                            vm.message = $rootScope.config.settings.errors.server;
                            break;
                    }
                } else {
                    //errore non causato dalla chiamata ai nostri server, dovuto ad esempio a pushwosh
                    //se il dispositivo è offline
                    vm.message = $rootScope.config.settings.errors.connection;
                }
            }

        });


        vm.retry = function () {
            goToState("app.home");
        }


        function goToState(state) {
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go(state);
        }


    }]);