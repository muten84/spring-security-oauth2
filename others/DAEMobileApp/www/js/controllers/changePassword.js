appControllers.controller('ChangePasswordCtrl', [
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
    '$cordovaDevice',
    '$ionicHistory',
    '$timeout',
    function ($scope, $rootScope, logger, device, $translate, $state, api, user, $templateCache, $ionicViewSwitcher, $cordovaDevice, $ionicHistory, $timeout) {
        logger.info("Caricamento ChangePasswordCtrl...");

        var vm = this;
        vm.changePassword = changePassword;
        vm.goToState = goToState;

        vm.credentials = {
            oldPassword: "",
            newPassword: ""
        };

        vm.changePasswordModel = {
            oldPassword: "",
            newPassword: "",
            confirmPassword: ""
        };

        $scope.$on('$ionicView.enter', function (e) {

            device.hideLoader();
            if ($ionicHistory.backView().stateName != "app.home") {
                $ionicHistory.backView().stateName = "app.home";
            }
            // Resetto le credenziali
            vm.credentials = {
                oldPassword: "",
                newPassword: ""
            };
            vm.changePasswordModel = {
                oldPassword: "",
                newPassword: "",
                confirmPassword: ""
            };

            vm.changePasswordForm.$setPristine();
            vm.changePasswordForm.$setUntouched();


        });

        vm.changePasswordFields = [
            {
                className: "input-white",
                key: 'oldPassword',
                type: 'input',
                templateOptions: {
                    required: true,
                    type: 'password',
                    label: $translate.instant('OLD_PASSWORD')
                },
                expressionProperties: {
                    'templateOptions.label': '"OLD_PASSWORD" | translate'
                }
            },
            {
                className: "input-white",
                key: 'newPassword',
                type: 'input',
                templateOptions: {
                    required: true,
                    type: 'password',
                    label: $translate.instant('NEW_PASSWORD')
                },
                expressionProperties: {
                    'templateOptions.label': '"NEW_PASSWORD" | translate'
                }
            },
            {
                className: "input-white",
                key: 'confirmPassword',
                type: 'input',
                templateOptions: {
                    required: true,
                    type: 'password',
                    label: $translate.instant('NEW_PASSWORD_CONFIRM')
                },
                expressionProperties: {
                    'templateOptions.label': '"NEW_PASSWORD_CONFIRM" | translate'
                }
            }
        ];

        $scope.$watch(function () {
            return $translate.use();
        }, function (newLang, oldLang) {
            logger.info("newLang: " + newLang);
            logger.info("oldLang: " + oldLang);
            //if (newLang && oldLang && newLang !== oldLang) {
            angular.forEach(vm.changePasswordFields, function (field) {
                field.runExpressions && field.runExpressions();
            });
            //}
        });

        /**
         * Effettua una chiamata al server per verificare le credenziali
         * e ottenere il token di autenticazione
         */
        function changePassword() {
            logger.info("[ChangePasswordCtrl:changePassword]", vm.credentials);

            if (vm.changePasswordForm.$valid) {
                device.showLoader();
                if (vm.changePasswordModel.newPassword === vm.changePasswordModel.confirmPassword) {
                    vm.credentials.oldPassword = vm.changePasswordModel.oldPassword;
                    vm.credentials.newPassword = vm.changePasswordModel.newPassword;
                    api.changePassword(vm.credentials).then(
                        function (data) {
                            if (data.response) {
                                device.alert("La password è stata modificata correttamente", "positive").then(
                                    function () {
                                        device.showLoader();
                                        var currentCredentials = user.getUserCredentials();
                                        var newCredentials = {
                                            username: currentCredentials.username,
                                            password: vm.credentials.newPassword,
                                            deviceInfo: currentCredentials.deviceInfo || null
                                        };
                                        api.login(newCredentials).then(
                                            function (loginResponse) {
                                                if (!loginResponse.error) {
                                                    // Imposto la sessione dell'utente
                                                    user.closeSession();
                                                    $timeout(
                                                        function(){
                                                            device.hideLoader();
                                                            user.startSession(loginResponse.token, loginResponse.startSessionTime, loginResponse.available, loginResponse.privacyAccepted);
                                                            user.setUserCredentials(newCredentials, true);
                                                            goToState("app.home");
                                                        }, 500
                                                    );
                                                }
                                                else {
                                                    device.alert("Si è verificato un errore", "balanced");
                                                }
                                            },
                                            function (err) {
                                                device.alert(err.message, "balanced");
                                            });
                                    }
                                );
                            }
                            else {
                                device.alert(data.message, "balanced");
                            }
                        },
                        function (err) {
                            device.alert(err.message, "balanced");
                        });
                }
                else {
                    device.alert("Le password inserite non corrispondono", "balanced");
                }
            }
        }

        function goToState(state) {
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go(state);
        }
    }
]);
