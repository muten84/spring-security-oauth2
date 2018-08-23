appControllers.controller('SelectMunicipalityCtrl', [
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
    '$mdSelect',
    function ($scope, $rootScope, logger, device, $translate, $state, api, user, $templateCache, $ionicViewSwitcher, $timeout, $mdDialog, $mdSelect) {
        logger.info("Caricamento SelectMunicipalityCtrl...");

        var vm = this;
        vm.addMunicipality = addMunicipality;
        vm.closeResetPasswordPopup = closeResetPasswordPopup;

        vm.selectMunicipalityModel = {
            provincia: {
                id: null,
                nomeProvincia: null
            },
            comune: {
                id: null,
                nomeComune: null
            }
        };

        $scope.$on('$ionicView.enter', function (e) {
            device.hideLoader();
            vm.selectMunicipalityForm.$setPristine();
            vm.selectMunicipalityForm.$setUntouched();
        });

        vm.selectMunicipalityFields = [
            {
                className: "input-green",
                type: "select",
                key: "provincia.id",
                templateOptions: {
                    label: "Provincia",
                    required: true,
                    placeholder: "",
                    labelProp: "nomeProvincia",
                    valueProp: "id",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.getAllProvinceByCompetence().then(
                            function (data) {
                                scope.to.options = data;
                            },
                            function (error) {
                                logger.info(error);
                                $mdSelect.hide();
                                $mdDialog.hide();
                                $timeout(function(){
                                    device.alert(error.message, "balanced");
                                },200);

                            }
                        );
                    }
                },
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            var obj = _.find(field.templateOptions.options, function (o) {
                                return o.id == newValue;
                            });
                            scope.model.provincia = obj;

                        }
                    }
                }
            },
            {
                className: "input-green",
                type: "select",
                key: "comune.id",
                templateOptions: {
                    label: "Comune",
                    required: true,
                    placeholder: "",
                    labelProp: "nomeComune",
                    valueProp: "id",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.searchComuniByFilter(scope.model.provincia.nomeProvincia).then(
                            function (data) {
                                scope.to.options = data;
                            },
                            function (error) {
                                logger.info(error);
                                $mdSelect.hide();
                                $mdDialog.hide();
                                $timeout(function(){
                                    device.alert(error.message, "balanced");
                                },200);
                            }
                        );
                    }
                },
                hideExpression: '!model.provincia.id',
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            var obj = _.find(field.templateOptions.options, function (o) {
                                return o.id == newValue;
                            });
                            scope.model.comune = obj;

                        }
                    }
                }
            }
        ];

        $scope.$watch(function () {
            return $translate.use();
        }, function (newLang, oldLang) {
            logger.info("newLang: " + newLang);
            logger.info("oldLang: " + oldLang);
            angular.forEach(vm.selectMunicipalityFields, function (field) {
                field.runExpressions && field.runExpressions();
            });
        });

        function addMunicipality() {
            logger.info("[SelectMunicipalityCtrl:addMunicipality]");

            if (vm.selectMunicipalityForm.$valid) {
                logger.info(vm.selectMunicipalityModel);
                var obj = {};
                obj.id = vm.selectMunicipalityModel.comune.id;
                obj.nomeComune = vm.selectMunicipalityModel.comune.nomeComune;
                $mdDialog.hide(obj);

            }
        }

        function closeResetPasswordPopup() {
            logger.info("[SelectMunicipalityCtrl:closeResetPasswordPopup]");
            $mdDialog.hide();
        }
    }
]);
