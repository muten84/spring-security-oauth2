appControllers.controller('ResetPasswordCtrl', [
    '$scope',
    '$rootScope',
    'loggerService',
    'deviceService',
    '$translate',
    '$state',
    'apiService',
    'userService',
    '$templateCache',
    '$ionicViewSwitcher',
    '$timeout',
    '$mdDialog',
    function ($scope, $rootScope, logger, device, $translate, $state, api, user, $templateCache, $ionicViewSwitcher, $timeout, $mdDialog) {
        logger.info("Caricamento ResetPasswordCtrl...");

        var vm = this;
        vm.resetPassword = resetPassword;
        vm.closeResetPasswordPopup = closeResetPasswordPopup;

        vm.resetPasswordModel = {
            emailToRecover: ""
        };

        $scope.$on('$ionicView.enter', function (e) {

            device.hideLoader();


            vm.resetPasswordForm.$setPristine();
            vm.resetPasswordForm.$setUntouched();


        });

        vm.resetPasswordFields = [
            {
                className: "input-green",
                key: 'emailToRecover',
                type: 'input',
                templateOptions: {
                    required: true,
                    type: 'email',
                    label: $translate.instant('EMAIL')
                },
                expressionProperties: {
                    'templateOptions.label': '"EMAIL" | translate'
                }
            }
        ];

        $scope.$watch(function () {
            return $translate.use();
        }, function (newLang, oldLang) {
            logger.info("newLang: " + newLang);
            logger.info("oldLang: " + oldLang);
            //if (newLang && oldLang && newLang !== oldLang) {
            angular.forEach(vm.resetPasswordFields, function (field) {
                field.runExpressions && field.runExpressions();
            });
            //}
        });


        /**
         * Chiama il server inviando l'email di cui effettuare il reset password
         * @param {Object} form - Form di reset password
         */
        function resetPassword() {
            logger.info("[ResetPasswordCtrl:resetPassword]");

            // Se l'email è valida chiamo l'API per il reset password
            if (vm.resetPasswordForm.$valid) {
                device.showLoader();
                api.resetPassword(vm.resetPasswordModel.emailToRecover).then(
                    function (result) {
                        device.hideLoader();
                        logger.info(result);
                        $mdDialog.hide(result);
                        if (result.response){
                            device.alert(result.message, "primary");
                        }
                        else {
                            device.alert(result.message, "balanced");
                        }


                    },
                    function (err) {
                        device.hideLoader();
                        $mdDialog.hide();
                        device.alert("Sistemi momentaneamente non disponibili", "balanced");
                    }
                )
            }
        }

        /**
         * Chiude il popup di reset password e ritorna la risposta
         * @param {String|null} response - Risposta da ritornare alla login
         */
        function closeResetPasswordPopup() {
            logger.info("[ResetPasswordCtrl:closeResetPasswordPopup]");
            $mdDialog.hide();
        }
    }
]);
