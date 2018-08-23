appControllers.controller('AddAvailabilityCtrl', [
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
        logger.info("Caricamento AddAvailabilityCtrl...");

        var vm = this;
        vm.addAvailability = addAvailability;
        vm.closeDialog = closeDialog;

        vm.addAvailabilityModel = {
            id: null,
            giornoSettimana: null,
            intervals: [
                {
                    orarioDa: "00:00",
                    orarioA: "23:59"
                }
            ]
        };

        $scope.$on('$ionicView.enter', function (e) {
            device.hideLoader();
            vm.addAvailabilityForm.$setPristine();
            vm.addAvailabilityForm.$setUntouched();
        });

        vm.addAvailabilityFields = [
            {
                className: 'input-green',
                type: "select",
                key: "id",
                templateOptions: {
                    label: "Giorno",
                    required: true,
                    labelProp: "label",
                    valueProp: "id",
                    options: [
                        {
                            id: 0,
                            name: "LUNEDI",
                            label: "Lunedì"
                        },
                        {
                            id: 1,
                            name: "MARTEDI",
                            label: "Martedì"
                        },
                        {
                            id: 2,
                            name: "MERCOLEDI",
                            label: "Mercoledì"
                        },
                        {
                            id: 3,
                            name: "GIOVEDI",
                            label: "Giovedì"
                        },
                        {
                            id: 4,
                            name: "VENERDI",
                            label: "Venerdì"
                        },
                        {
                            id: 5,
                            name: "SABATO",
                            label: "Sabato"
                        },
                        {
                            id: 6,
                            name: "DOMENICA",
                            label: "Domenica"
                        }
                    ],
                    onChange: function (val, field, scope) {
                        scope.model.giornoSettimana = _.find(field.templateOptions.options, function (o) {
                            return o.id == val;
                        }).name;
                    }
                }
            },
            {
                type: 'repeatAvailabilitySection',
                key: 'intervals',
                templateOptions: {
                    btnText: 'Aggiungi intervallo',
                    fields: [
                        {
                            className: 'layout-row',
                            fieldGroup: [
                                {
                                    className: 'flex-50 input-green',
                                    key: "orarioDa",
                                    type: "dae-timepicker",
                                    templateOptions: {
                                        label: "Dalle",
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
                                                device.alert("Datepicker non disponibile", "primary");
                                            }
                                        }
                                    }
                                },
                                {
                                    className: 'flex-50 input-green',
                                    key: "orarioA",
                                    type: "dae-timepicker",
                                    templateOptions: {
                                        label: "Alle",
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
                                                device.alert("Datepicker non disponibile", "primary");
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
        ];

        $scope.$watch(function () {
            return $translate.use();
        }, function (newLang, oldLang) {
            logger.info("newLang: " + newLang);
            logger.info("oldLang: " + oldLang);
            angular.forEach(vm.addAvailabilityFields, function (field) {
                field.runExpressions && field.runExpressions();
            });
        });

        $scope.$watchCollection('vm.addAvailabilityModel.intervals.length', function (newvalue, oldvalue) {
            logger.info("newvalue: " + newvalue);
            logger.info("oldvalue: " + oldvalue);
            if (vm.errorMessages) vm.errorMessages = '';
        });

        function addAvailability() {
            logger.info("[AddAvailabilityCtrl:addAvailability]");
            if (vm.addAvailabilityForm.$valid) {
                logger.info(vm.addAvailabilityModel);
                if (vm.addAvailabilityModel.intervals.length > 1) {
                    if (!checkIntervals(vm.addAvailabilityModel.intervals)) {
                        vm.errorMessages = 'Gli intervalli inseriti si sovrappongono';
                    }
                    else {
                        $mdDialog.hide(vm.addAvailabilityModel);
                    }
                }
                else{
                    if (vm.addAvailabilityModel.intervals.length === 0) {
                        vm.errorMessages = 'Devi aggiungere almeno un intervallo';
                    }
                    else {
                        $mdDialog.hide(vm.addAvailabilityModel);
                    }
                }
            }
        }

        function checkIntervals(intervals) {
            var firstIntervalFrom = moment(intervals[0].orarioDa, "HH:mm");
            var firstIntervalTo = moment(intervals[0].orarioA, "HH:mm");
            var secondIntervalFrom = moment(intervals[1].orarioDa, "HH:mm");
            var secondIntervalTo = moment(intervals[1].orarioA, "HH:mm");
            if (intervals[0].orarioDa === intervals[1].orarioDa || intervals[0].orarioA === intervals[1].orarioA) {
                return false;
            }
            if (secondIntervalFrom.isBetween(firstIntervalFrom, firstIntervalTo)) {
                return false;
            }
            else if (secondIntervalTo.isBetween(firstIntervalFrom, firstIntervalTo)) {
                return false;
            }
            return true;
        }

        function closeDialog() {
            logger.info("[AddAvailabilityCtrl:closeDialog]");
            $mdDialog.hide();
        }
    }
]);
