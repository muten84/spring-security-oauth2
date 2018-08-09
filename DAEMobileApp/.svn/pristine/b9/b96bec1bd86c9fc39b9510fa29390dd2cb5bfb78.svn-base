appControllers.controller('AddDoNotDisturbIntervalCtrl', [
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
    '$cordovaDatePicker',
    '$cordovaKeyboard',
    function ($scope, $rootScope, logger, device, $translate, $state, api, user, $templateCache, $ionicViewSwitcher, $timeout, $mdDialog, $cordovaDatePicker, $cordovaKeyboard) {
        logger.info("Caricamento AddDoNotDisturbIntervalCtrl...");

        var vm = this;
        vm.addDoNotDisturbInterval = addDoNotDisturbInterval;
        vm.closeDialog = closeDialog;

        vm.interval = {
            from: null,
            to: null
        };

        $scope.$on('$ionicView.enter', function (e) {
            device.hideLoader();
            vm.addAvailabilityForm.$setPristine();
            vm.addAvailabilityForm.$setUntouched();
        });

        vm.intervalFields = [
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
                            required: true,
                            onFocus: function () {
                                var isVisible = $cordovaKeyboard.isVisible();
                                if (isVisible) {
                                    $cordovaKeyboard.close();
                                }
                            },
                            onclick: function ($modelValue, $options) {
                                var options = {
                                    date: new Date(),
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
                                        if ($modelValue.orarioA) {
                                            var fromTime = moment(date);
                                            var toTime = moment($modelValue.orarioA, "HH:mm");
                                            var isBefore = fromTime.isBefore(toTime);
                                            if (!isBefore) {
                                                device.alert("L'orario di inizio disponibilità deve essere minore dell'orario di fine", "primary").then(
                                                    function () {
                                                        $modelValue[$options.key] = null;
                                                    }
                                                );
                                            }
                                            else {
                                                $modelValue[$options.key] = moment(date).format("HH:mm");
                                            }
                                        }
                                        else {
                                            $modelValue[$options.key] = moment(date).format("HH:mm");
                                        }
                                    });
                                }
                                else {
                                    $modelValue[$options.key] = "20:00";
                                    // device.alert("Datepicker non disponibile", "primary");
                                }
                            }
                        }
                    },
                    {
                        className: 'flex-50 input-green',
                        key: "to",
                        type: "dae-timepicker",
                        templateOptions: {
                            label: "Fine",
                            dateFormat: 'HH:mm',
                            required: true,
                            onFocus: function () {
                                var isVisible = $cordovaKeyboard.isVisible();
                                if (isVisible) {
                                    $cordovaKeyboard.close();
                                }
                            },
                            onclick: function ($modelValue, $options) {
                                var options = {
                                    date: new Date(),
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
                                        if ($modelValue.orarioDa) {
                                            var fromTime = moment($modelValue.orarioDa, "HH:mm");
                                            var toTime = moment(date);
                                            var isAfter = toTime.isAfter(fromTime);
                                            if (!isAfter) {
                                                device.alert("L'orario di fine disponibilità deve essere maggiore dell'orario di inizio", "primary").then(
                                                    function () {
                                                        $modelValue[$options.key] = null;
                                                    }
                                                );
                                            }
                                            else {
                                                $modelValue[$options.key] = moment(date).format("HH:mm");
                                            }
                                        }
                                        else {
                                            $modelValue[$options.key] = moment(date).format("HH:mm");
                                        }
                                    });
                                }
                                else {
                                    $modelValue[$options.key] = "22:00";
                                    // device.alert("Datepicker non disponibile", "primary");
                                }
                            }
                        }
                    }
                ]
            }
        ];

        $scope.$watch(function () {
            return $translate.use();
        }, function (newLang, oldLang) {
            logger.info("newLang: " + newLang);
            logger.info("oldLang: " + oldLang);
            angular.forEach(vm.intervalFields, function (field) {
                field.runExpressions && field.runExpressions();
            });
        });

        function addDoNotDisturbInterval() {
            logger.info("[AddDoNotDisturbIntervalCtrl:addDoNotDisturbInterval]");
            if (vm.addDoNotDisturbForm.$valid) {
                logger.info(vm.interval);
                $mdDialog.hide(vm.interval);
            }
        }

        function closeDialog() {
            logger.info("[AddDoNotDisturbIntervalCtrl:closeDialog]");
            $mdDialog.cancel();
        }
    }
]);
