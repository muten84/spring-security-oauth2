appControllers.controller('DamageReportingCtrl', [
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
    '$mdSelect',
    function ($rootScope, $scope, logger, api, user, $timeout, $state, $stateParams, device, $ionicHistory, $mdSelect) {
        logger.info("Caricamento DamageReportingCtrl...");

        var vm = this;
        vm.goToState = goToState;
        vm.newDamageReporting = newDamageReporting;

        $scope.$on('$ionicView.enter', function () {
            vm.dae = $stateParams.dae;
            logger.info(JSON.stringify(vm.dae));
        });

        vm.options = {};
        vm.damageReporting = {
            id: null,
            dae: {
                id: null
            },
            tipologia: null,
            note: null,
            dataSegnalazione: "2017-03-16T08:17:24.920Z",
            statoAttuale: "APERTA"
        };
        vm.damageReportingFieds = [
            {
                type: "select",
                key: "tipologia",
                templateOptions: {
                    label: "Tipologia",
                    required: true,
                    labelProp: "value",
                    valueProp: "value",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.getTipologiaSegnalazioni().then(
                            function (data) {
                                scope.to.options = data;
                            },
                            function (error) {
                                logger.info(error);
                                $mdSelect.hide();
                                device.alert(error.message, "balanced");
                            }
                        );
                    }
                }
                /* templateOptions: {
                    label: "Tipologia",
                    placeholder: "",
                    required: true,
                    labelProp: "type",
                    valueProp: "id",
                    options: [
                        {
                            id: "Coordinate errate",
                            type: "Coordinate errate"
                        },
                        {
                            id: "Atto vandalico",
                            type: "Atto vandalico"
                        },
                        {
                            id: "Led lampeggiante",
                            type: "Led lampeggiante"
                        }
                    ]
                } */
            },
            {
                className: "input-green",
                key: "note",
                type: "textarea",
                templateOptions: {
                    maxlength: 1024,
                    rows: 6,
                    required: true,
                    label: "Note"
                }
            }
        ];

        function newDamageReporting() {
            logger.info("[DamageReportingCtrl:newDamageReporting]", vm.damageReporting);

            if (vm.damageReportingForm.$valid) {
                device.showLoader();
                vm.damageReporting.dae.id = vm.dae.id;
                vm.damageReporting.dataSegnalazione = moment().format();
                api.reportFault(vm.damageReporting).then(
                    function (data) {
                        if (data) {
                            device.alert("La segnalazione è stata inserita correttamente", "positive").then(
                                function () {
                                    $ionicHistory.goBack();
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
        }

        function goToState(state) {
            $ionicHistory.nextViewOptions({
                disableBack: true
            });
            $state.go(state);
        }


    }]);