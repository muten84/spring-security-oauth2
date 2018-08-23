appControllers.controller('ProfileCtrl', [
    '$rootScope',
    '$scope',
    '$state',
    'loggerService',
    'apiService',
    'userService',
    'deviceService',
    '$ionicHistory',
    function ($rootScope, $scope, $state, logger, api, user, device, $ionicHistory) {
        logger.info("Caricamento ProfileCtrl...");

        var vm = this;
        vm.goToModify = goToModify;
        vm.photo = false;

        $scope.$on('$ionicView.loaded', function () {
            // initialize();
        });
        $scope.$on('$ionicView.enter', function () {
            initialize();
            logger.info(JSON.stringify(vm.profile));
        });
        function initialize() {
            device.showLoader();
            vm.photo = false;
            api.getLoggedFirstResponderDetails().then(
                function (profile) {
                    logger.info(profile);
                    user.setUser(profile);
                    vm.profile = profile;
                    // device.hideLoader();
                    if (vm.profile.comuniCompetenza) {
                        vm.profile.comuniCompetenza.sort(
                            function (a, b) {
                                return a.nomeComune.localeCompare(b.nomeComune);
                            }
                        );
                    }

                    if (vm.profile.immagine.url) {
                        vm.photo = true;
                        var image = document.getElementById('photo');
                        image.onload = function () {
                            device.hideLoader();
                        };
                        image.src = $rootScope.config.imgUrl + vm.profile.immagine.url + "?t=" + new Date().getTime();
                    }
                },
                function (error) {
                    device.hideLoader();
                    device.alert(error.message, "balanced").then(
                        function () {
                            $ionicHistory.goBack();
                        }
                    );
                }
            );
        }
        function goToModify() {
            $state.go("app.updateProfile");
        }

    }]);