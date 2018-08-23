appControllers.controller('LoginCtrl', [
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
    '$cordovaDevice',
    'localStorageService',
    '$ionicHistory',
    'privacyService',
    '$q',
    'PushWoosh',
    '$cordovaNetwork',
    function ($scope, $rootScope, logger, device, $translate, $state, api, user, $templateCache, $ionicViewSwitcher, $timeout, $mdDialog, $cordovaDevice, storage, $ionicHistory, privacy, $q, pushwoosh, $cordovaNetwork) {
        logger.info("Caricamento LoginCtrl...");

        var vm = this;
        vm.login = login;
        vm.openPromptForPasswordRecover = openPromptForPasswordRecover;
        vm.navigateTo = navigateTo;
        vm.goToState = goToState;

        $scope.$on('$ionicView.enter', function (e) {

            device.hideLoader();
            if ($ionicHistory.backView() && $ionicHistory.backView().stateName != "app.home") {
                $ionicHistory.backView().stateName = "app.home";
            }
            // Resetto le credenziali
            vm.credentials = {
                username: "",
                password: "",
                rememberMe: true,
                deviceInfo: {
                    marca: "",
                    modello: "",
                    os: "",
                    pushToken: ""
                }
            };

            if (device.isCordova()) {
                vm.credentials.deviceInfo.modello = $cordovaDevice.getModel();
                vm.credentials.deviceInfo.os = $cordovaDevice.getPlatform();
            }
            vm.loginForm.$setPristine();
            vm.loginForm.$setUntouched();
        });

        // JSON di configurazione per la form di login 
        vm.loginFields = [{
                className: "input-white",
                key: 'username',
                type: 'input',
                templateOptions: {
                    required: true,
                    type: 'text',
                    label: $translate.instant('USERNAME')
                },
                expressionProperties: {
                    'templateOptions.label': '"USERNAME" | translate'
                }
            },
            {
                className: "input-white",
                key: 'password',
                type: 'input',
                templateOptions: {
                    required: true,
                    type: 'password',
                    label: $translate.instant('PASSWORD')
                },
                expressionProperties: {
                    'templateOptions.label': '"PASSWORD" | translate'
                }
            }

        ];

        $scope.$watch(function () {
            return $translate.use();
        }, function (newLang, oldLang) {
            angular.forEach(vm.loginFields, function (field) {
                field.runExpressions && field.runExpressions();
            });
        });

        /**
         * PUSHWOOSH REGISTRATION
         */
        function pushwooshRegistration() {
            var q = $q.defer();
            if (ionic.Platform.isWebView()) {
                if ($cordovaNetwork.isOnline()) {
                    pushwoosh.register(function (result) {
                        logger.info('PushWoosh register ');
                        logger.info('PushWoosh result: ' + result.type);
                        if (result.type == 'registration') {
                            logger.info('token [ ' + JSON.stringify(result) + ' ]');
                            logger.info('device [ ' + result.device + ' ]');
                            if (result.id) {
                                vm.credentials.deviceInfo.pushToken = result.id;
                                storage.add('registrationId', result.id);
                                q.resolve();
                            } else {
                                q.reject("Servizio momentaneamente non disponibile, riprova");
                            }
                        }
                    });
                } else {
                    q.reject("OFFLINE_ACTION_NOT_COMPLETED");
                }
            } else {
                vm.credentials.deviceInfo.pushToken = "111111111111111";
                q.reject();
            }
            return q.promise;
        }

        /**
         * Effettua una chiamata al server per verificare le credenziali
         * e ottenere il token di autenticazione
         */
        function login() {
            logger.info("[LoginCtrl:login]", vm.credentials);

            if (vm.loginForm.$valid) {

                device.showLoader();
                pushwooshRegistration().then(
                    function () {
                        api.login(vm.credentials).then(
                            function (loginResponse) {
                                if (!loginResponse.error) {
                                    // Imposto la sessione dell'utente
                                    user.startSession(loginResponse.token, loginResponse.startSessionTime, loginResponse.available, loginResponse.privacyAccepted);
                                    privacy.checkPrivacy().then(
                                        function () {
                                            //Setto l'autologin per default
                                            user.setUserCredentials(vm.credentials, true);
                                            api.getLoggedFirstResponderDetails().then(
                                                function (profile) {
                                                    logger.info(profile);
                                                    user.setUser(profile);
                                                    navigateTo("app.home");
                                                },
                                                function (error) {
                                                    device.alert(error.message, "balanced");
                                                }
                                            );
                                        },
                                        function (error) {
                                            logger.error(error);
                                        }
                                    );
                                } else {
                                    device.alert(loginResponse.message, "balanced");
                                }
                            },
                            function (error) {
                                device.alert(error.message, "balanced");
                            }
                        );
                    },
                    function (error) {
                        if (ionic.Platform.isWebView()) {
                            device.alert(error, "balanced");
                        } else {
                            api.login(vm.credentials).then(
                                function (loginResponse) {
                                    if (!loginResponse.error) {
                                        // Imposto la sessione dell'utente
                                        user.startSession(loginResponse.token, loginResponse.startSessionTime, loginResponse.available, loginResponse.privacyAccepted);
                                        privacy.checkPrivacy().then(
                                            function () {
                                                //Setto l'autologin per default
                                                user.setUserCredentials(vm.credentials, true);
                                                api.getLoggedFirstResponderDetails().then(
                                                    function (profile) {
                                                        logger.info(profile);
                                                        user.setUser(profile);
                                                        navigateTo("app.home");
                                                    },
                                                    function (error) {
                                                        device.alert(error.message, "balanced");
                                                    }
                                                );
                                            },
                                            function (error) {
                                                logger.error(error);
                                            }
                                        );
                                    } else {
                                        device.alert(loginResponse.message, "balanced");
                                    }
                                },
                                function (error) {
                                    device.alert(error.message, "balanced");
                                }
                            );
                        }
                    }
                );
            }
        }

        /**
         * Apre la modale per gestire il cambio password
         */
        function openPromptForPasswordRecover(ev) {
            logger.info("[LoginCtrl:openPromptForPasswordRecover]", vm.credentials.email);

            vm.credentials.emailToRecover = vm.credentials.email;
            $mdDialog.show({
                    controller: "ResetPasswordCtrl",
                    controllerAs: 'vm',
                    template: $templateCache.get('dialog/resetPassword.html'),
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: true
                })
                .then(function (answer) {
                    logger.info(answer);

                }, function (error) {

                });
        }
        
        function navigateTo(stateName) {
            $ionicHistory.nextViewOptions({
                disableAnimate: false,
                disableBack: true
            });
            $state.go(stateName);
        }

        function goToState(state) {
            $state.go(state);
        }

    }
]);