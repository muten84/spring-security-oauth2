appControllers.controller('UpdateCtrl', [
    '$rootScope',
    '$scope',
    '$state',
    'loggerService',
    'deviceService',
    '$cordovaDialogs',
    '$cordovaSplashscreen',
    '$window',
    function ($rootScope, $scope, $state, logger, device, $cordovaDialogs, $cordovaSplashscreen, $window) {
        logger.info("Caricamento UpdateCtrl...");

        var vm = this;

        $scope.$on('$ionicView.enter', function () {
            if (device.isCordova()){
                $cordovaSplashscreen.show();
            }
            init();
        });

        function init(){
            if (device.isAndroid()) {
                if (!$rootScope.config.Versioning.android.isLastVersion) {

                    $cordovaDialogs.confirm($rootScope.config.Versioning.android.message, 'Aggiornamento versione', [
                        'OK',
                        'Annulla'
                    ]).then(function (buttonIndex) {
                        logger.info('Premuto tasto: ' + buttonIndex);
                        if (buttonIndex == 1) {
                            $window.open($rootScope.config.Versioning.android.storeLink, '_system');
                        } else {
                            if ($rootScope.config.Versioning.android.isMandatoryUpdate) {
                                if ($window.navigator && $window.navigator.app)
                                    $window.navigator.app.exitApp();
                            }
                            else{
                                $state.go("app.home");
                            }
                        }
                    }, function () {
                    });
                }
            } else {
                if (!$rootScope.config.Versioning.iOS.isLastVersion) {

                    $cordovaDialogs.confirm($rootScope.config.Versioning.iOS.message, 'Aggiornamento versione', [
                        'OK',
                        'Annulla'
                    ]).then(function (buttonIndex) {
                        logger.info('Premuto tasto: ' + buttonIndex);
                        if (buttonIndex == 1) {
                            $window.open($rootScope.config.Versioning.iOS.storeLink, '_system');
                        } else {
                            if ($rootScope.config.Versioning.iOS.isMandatoryUpdate) {

                            }
                            else{
                                $state.go("app.home");
                            }
                        }
                    }, function () {
                    });
                }
            }
        }

    }]);