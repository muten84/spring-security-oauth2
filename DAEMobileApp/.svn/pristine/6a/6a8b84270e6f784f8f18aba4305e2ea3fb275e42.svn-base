appControllers.controller('SettingsCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$state',
    '$stateParams',
    'deviceService',
    '$ionicHistory',
    '$translate',
    '$cordovaDatePicker',
    '$cordovaKeyboard',
    'userService',
    '$mdDialog',
    '$templateCache',
    function ($rootScope, $scope, logger, api, user, $timeout, $state, $stateParams, device, $ionicHistory, $translate, $cordovaDatePicker, $cordovaKeyboard, user, $mdDialog, $templateCache) {
        logger.info("Caricamento SettingsCtrl...");

        var vm = this;
        vm.goToState = goToState;
        vm.settingsModified = false;
        var userDetails = {};
        vm.options = {};
        vm.options.formState = {
            silent: false,
            doNotDisturb: false
        };

        $scope.$on('$ionicView.loaded', function () {
            userDetails = user.getUser();
            vm.options.formState.doNotDisturb = userDetails.doNotDisturb || false;
            vm.options.formState.silent = userDetails.silent || false;
            vm.settings.from = userDetails.doNotDisturbFrom;
            vm.settings.to = userDetails.doNotDisturbTo;
        });

        $scope.$on('$ionicView.enter', function () {
            vm.settingsModified = false;
        });

        vm.settings = {
            from: "21:00",
            to: "22:00"
        };
        vm.settingsFields = [
            {
                key: "silent",
                type: "switch",
                model: 'formState',
                templateOptions: {
                    label: "Silenzioso",
                    theme: "default"
                },
                controller: function ($scope, apiService) {
                    logger.info("Caricamento controller...");
                    $scope.$watch('formState.silent', function (newValue, oldValue, scope) {
                            if (angular.isDefined(newValue) && newValue != oldValue) {
                                logger.info(newValue);
                                logger.info(oldValue);
                                if (newValue) {
                                    device.showLoader();
                                    var settings = {};
                                    var now = new Date();
                                    settings.from = moment(now).format("HH:mm");
                                    settings.to = moment(now).add($rootScope.config.settings.silentHours, 'hours').format("HH:mm");

                                    logger.info("setSilent");
                                    apiService.setSilent(settings).then(
                                        function (data) {
                                            if (data) {
                                                // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                userDetails.silent = vm.options.formState.silent;
                                                userDetails.silentFrom = settings.from;
                                                userDetails.silentTo = settings.to;
                                                user.setUser(userDetails);
                                                device.hideLoader();
                                            }
                                            else {
                                                device.alert(data.message, "balanced").then(
                                                    function () {
                                                        goToState("app.home");
                                                    }
                                                );
                                            }
                                        },
                                        function (err) {
                                            device.alert(err.message, "balanced").then(
                                                function () {
                                                    goToState("app.home");
                                                }
                                            );
                                        }
                                    );
                                    
/*                                     if (userDetails.doNotDisturb) {
                                        logger.info("removeDoNotDisturb");
                                        apiService.removeDoNotDisturb().then(
                                            function (data) {
                                                if (data) {
                                                    // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                    userDetails.doNotDisturb = false;
                                                    scope.formOptions.formState.doNotDisturb = false;
                                                    logger.info("setSilent");
                                                    apiService.setSilent(settings).then(
                                                        function (data) {
                                                            if (data) {
                                                                // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                                userDetails.silent = vm.options.formState.silent;
                                                                userDetails.silentFrom = settings.from;
                                                                userDetails.silentTo = settings.to;
                                                                user.setUser(userDetails);
                                                                device.hideLoader();
                                                            }
                                                            else {
                                                                device.alert(data.message, "balanced").then(
                                                                    function () {
                                                                        goToState("app.home");
                                                                    }
                                                                );
                                                            }
                                                        },
                                                        function (err) {
                                                            device.alert(err.message, "balanced").then(
                                                                function () {
                                                                    goToState("app.home");
                                                                }
                                                            );
                                                        }
                                                    );
                                                }
                                                else {
                                                    device.alert(data.message, "balanced").then(
                                                        function () {
                                                            goToState("app.home");
                                                        }
                                                    );
                                                }
                                            },
                                            function (err) {
                                                device.alert(err.message, "balanced").then(
                                                    function () {
                                                        goToState("app.home");
                                                    }
                                                );
                                            }
                                        );
                                    }
                                    else {
                                        logger.info("setSilent");
                                        apiService.setSilent(settings).then(
                                            function (data) {
                                                if (data) {
                                                    // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                    userDetails.silent = vm.options.formState.silent;
                                                    userDetails.silentFrom = settings.from;
                                                    userDetails.silentTo = settings.to;
                                                    user.setUser(userDetails);
                                                    device.hideLoader();
                                                }
                                                else {
                                                    device.alert(data.message, "balanced").then(
                                                        function () {
                                                            goToState("app.home");
                                                        }
                                                    );
                                                }
                                            },
                                            function (err) {
                                                device.alert(err.message, "balanced").then(
                                                    function () {
                                                        goToState("app.home");
                                                    }
                                                );
                                            }
                                        );
                                    } */
                                }
                                else {
                                    logger.info("removeSilent");
                                    if (userDetails.silent) {
                                        device.showLoader();
                                        apiService.removeSilent().then(
                                            function (data) {
                                                if (data) {
                                                    // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                    userDetails.silent = false;
                                                    user.setUser(userDetails);
                                                    device.hideLoader();
                                                }
                                                else {
                                                    device.alert(data.message, "balanced").then(
                                                        function () {
                                                            goToState("app.home");
                                                        }
                                                    );
                                                }
                                            },
                                            function (err) {
                                                device.alert(err.message, "balanced").then(
                                                    function () {
                                                        goToState("app.home");
                                                    }
                                                );
                                            }
                                        );
                                    }
                                }
                            }
                        }
                    );
                }
            },
            {
                template: '<md-card-content class="settings-card"><p>' + $rootScope.config.settings.silentInfo.replace(/{{N}}/g, $rootScope.config.settings.silentHours) + '</p></md-card-content>'
            },
            {
                key: "doNotDisturb",
                type: "switch",
                model: 'formState',
                templateOptions: {
                    label: "Non disturbare",
                    theme: "default"
                },
                controller: function ($scope, apiService, deviceService) {
                    logger.info("Caricamento controller...");
                    $scope.$watch('formState.doNotDisturb', function (newValue, oldValue, scope) {
                            if (angular.isDefined(newValue) && newValue != oldValue) {
                                logger.info(newValue);
                                logger.info(oldValue);
                                if (newValue) {
                                    $mdDialog.show({
                                        controller: "AddDoNotDisturbIntervalCtrl",
                                        controllerAs: 'vm',
                                        template: $templateCache.get('dialog/addDoNotDisturbInterval.html'),
                                        parent: angular.element(document.body),
                                        clickOutsideToClose: false
                                    }).then(
                                        function (data) {
                                            device.showLoader();
                                            logger.info(data);
                                            if (data) {
                                                vm.settings.from = data.from;
                                                vm.settings.to = data.to;
                                                logger.info("setDoNotDisturb");
                                                apiService.setDoNotDisturb(vm.settings).then(
                                                    function (data) {
                                                        if (data) {
                                                            // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                            userDetails.doNotDisturb = vm.options.formState.doNotDisturb;
                                                            userDetails.doNotDisturbFrom = vm.settings.from;
                                                            userDetails.doNotDisturbTo = vm.settings.to;
                                                            user.setUser(userDetails);
                                                            device.hideLoader();
                                                        }
                                                        else {
                                                            device.alert(data.message, "balanced").then(
                                                                function () {
                                                                    goToState("app.home");
                                                                }
                                                            );
                                                        }
                                                    },
                                                    function (err) {
                                                        device.alert(err.message, "balanced").then(
                                                            function () {
                                                                goToState("app.home");
                                                            }
                                                        );
                                                    }
                                                );

                                                /* if (userDetails.silent){
                                                    logger.info("removeSilent");
                                                    apiService.removeSilent().then(
                                                        function (data) {
                                                            if (data) {
                                                                // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                                userDetails.silent = false;
                                                                //disattivo il silenzioso
                                                                scope.formOptions.formState.silent = false;
                                                                logger.info("setDoNotDisturb");
                                                                apiService.setDoNotDisturb(vm.settings).then(
                                                                    function (data) {
                                                                        if (data) {
                                                                            // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                                            userDetails.doNotDisturb = vm.options.formState.doNotDisturb;
                                                                            userDetails.doNotDisturbFrom = vm.settings.from;
                                                                            userDetails.doNotDisturbTo = vm.settings.to;
                                                                            user.setUser(userDetails);
                                                                            device.hideLoader();
                                                                        }
                                                                        else {
                                                                            device.alert(data.message, "balanced").then(
                                                                                function () {
                                                                                    goToState("app.home");
                                                                                }
                                                                            );
                                                                        }
                                                                    },
                                                                    function (err) {
                                                                        device.alert(err.message, "balanced").then(
                                                                            function () {
                                                                                goToState("app.home");
                                                                            }
                                                                        );
                                                                    }
                                                                );
                                                            }
                                                            else {
                                                                device.alert(data.message, "balanced").then(
                                                                    function () {
                                                                        goToState("app.home");
                                                                    }
                                                                );
                                                            }
                                                        },
                                                        function (err) {
                                                            device.alert(err.message, "balanced").then(
                                                                function () {
                                                                    goToState("app.home");
                                                                }
                                                            );
                                                        }
                                                    );
                                                }
                                                else{
                                                    logger.info("setDoNotDisturb");
                                                    apiService.setDoNotDisturb(vm.settings).then(
                                                        function (data) {
                                                            if (data) {
                                                                // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                                userDetails.doNotDisturb = vm.options.formState.doNotDisturb;
                                                                userDetails.doNotDisturbFrom = vm.settings.from;
                                                                userDetails.doNotDisturbTo = vm.settings.to;
                                                                user.setUser(userDetails);
                                                                device.hideLoader();
                                                            }
                                                            else {
                                                                device.alert(data.message, "balanced").then(
                                                                    function () {
                                                                        goToState("app.home");
                                                                    }
                                                                );
                                                            }
                                                        },
                                                        function (err) {
                                                            device.alert(err.message, "balanced").then(
                                                                function () {
                                                                    goToState("app.home");
                                                                }
                                                            );
                                                        }
                                                    );
                                                } */

                                            }
                                        },
                                        function () {
                                            logger.info("CANCEL");
                                            scope.formOptions.formState.doNotDisturb = false;
                                        }
                                    );

                                }
                                else {
                                    if (userDetails.doNotDisturb) {
                                        device.showLoader();
                                        logger.info("removeDoNotDisturb");
                                        apiService.removeDoNotDisturb().then(
                                            function (data) {
                                                if (data) {
                                                    // device.alert("Le impostazioni sono state salvate correttamente", "positive");
                                                    userDetails.doNotDisturb = false;
                                                    user.setUser(userDetails);
                                                    device.hideLoader();
                                                }
                                                else {
                                                    device.alert(data.message, "balanced").then(
                                                        function () {
                                                            goToState("app.home");
                                                        }
                                                    );
                                                }
                                            },
                                            function (err) {
                                                device.alert(err.message, "balanced").then(
                                                    function () {
                                                        goToState("app.home");
                                                    }
                                                );
                                            }
                                        );
                                    }
                                }
                                vm.settingsModified = true;
                            }
                        }
                    );
                }
            },
            {
                className: 'layout-row',
                fieldGroup: [
                    {
                        className: 'flex-50 input-green',
                        key: "from",
                        type: "dae-timepicker",
                        templateOptions: {
                            label: "Inizio",
                            dateFormat: 'HH:mm',
                            disabled: true,
                            required: false,
                            onFocus: function () {
                                var isVisible = $cordovaKeyboard.isVisible();
                                if (isVisible) {
                                    $cordovaKeyboard.close();
                                }
                            },
                            onclick: function ($modelValue, $options) {
                                var options = {
                                    date: $modelValue.from ? moment(vm.settings.from, 'HH:mm').toDate() : new Date(),
                                    mode: "time",           // 'date' or 'time'

                                    // Configurazione Android
                                    //titleText     : $translate.instant("DATEPICKER_TITLE"),
                                    okText: $translate.instant("DATEPICKER_DONE"),
                                    cancelText: $translate.instant("DATEPICKER_CANCEL"),
                                    is24Hour: true,
                                    androidTheme: 5,

                                    // Configurazione iOS
                                    allowOldDates: true,
                                    allowFutureDates: true,
                                    doneButtonLabel: $translate.instant("DATEPICKER_DONE"),
                                    doneButtonColor: "#1D62F0",
                                    cancelButtonLabel: $translate.instant("DATEPICKER_CANCEL"),
                                    cancelButtonColor: '#000000',
                                    locale: $translate.use()
                                };
                                if (device.isCordova()) {
                                    $cordovaDatePicker.show(options).then(function (date) {
                                        vm.settingsModified = true;
                                        $modelValue[$options.key] = moment(date).format("HH:mm");
                                    });
                                }
                                else {
                                    device.alert("Datepicker non disponibile", "primary");
                                }
                            }
                        },
                        hideExpression: '!formState.doNotDisturb'
                    },
                    {
                        className: 'flex-50 input-green',
                        key: "to",
                        type: "dae-timepicker",
                        templateOptions: {
                            label: "Fine",
                            dateFormat: 'HH:mm',
                            disabled: true,
                            required: false,
                            onFocus: function () {
                                var isVisible = $cordovaKeyboard.isVisible();
                                if (isVisible) {
                                    $cordovaKeyboard.close();
                                }
                            },
                            onclick: function ($modelValue, $options) {
                                var options = {
                                    date: $modelValue.to ? moment(vm.settings.to, 'HH:mm').toDate() : new Date(),
                                    mode: "time",           // 'date' or 'time'

                                    // Configurazione Android
                                    //titleText     : $translate.instant("DATEPICKER_TITLE"),
                                    okText: $translate.instant("DATEPICKER_DONE"),
                                    cancelText: $translate.instant("DATEPICKER_CANCEL"),
                                    is24Hour: true,
                                    androidTheme: 5,

                                    // Configurazione iOS
                                    allowOldDates: true,
                                    allowFutureDates: true,
                                    doneButtonLabel: $translate.instant("DATEPICKER_DONE"),
                                    doneButtonColor: "#1D62F0",
                                    cancelButtonLabel: $translate.instant("DATEPICKER_CANCEL"),
                                    cancelButtonColor: '#000000',
                                    locale: $translate.use()
                                };
                                if (device.isCordova()) {
                                    $cordovaDatePicker.show(options).then(function (date) {
                                        vm.settingsModified = true;
                                        $modelValue[$options.key] = moment(date).format("HH:mm");
                                    });
                                }
                                else {
                                    device.alert("Datepicker non disponibile", "primary");
                                }
                            }
                        },
                        hideExpression: '!formState.doNotDisturb'
                    }
                ]
            },
            {
                template: '<md-card-content class="settings-card"><p>' + $rootScope.config.settings.doNotDisturbInfo + '</md-card-content>'
            }
        ];

        function goToState(state) {
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go(state);
        }


    }]);