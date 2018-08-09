'use strict';
define(['angular', "moment", "lodash", "constants"], function(angular, moment, _, constants) {
    function setStepSize(el, data) {
        var max = 0;
        if (data) {
            data.forEach(function(d) {
                var num = Number(d);
                max = num > max ? num : max;
            });
        }
        if (max <= 5) {
            el.options.scales = {
                yAxes: [{
                    ticks: {
                        min: 0,
                        stepSize: 1
                    }
                }]
            };
        } else {
            el.options.scales = {
                yAxes: [{
                    ticks: {
                        min: 0
                    }
                }]
            };
        }
    }
    angular.module("controller").controller('GraphCtrl', [
		"$scope", "loggerService", "$rootScope", "chartService",
		function($scope, loggerService, $rootScope, chartService) {
            $rootScope.$broadcast("main.changeTitle", {
                title: "Grafici",
                icon: "fa fa-bar-chart"
            });
            angular.extend($scope, {

                activationProv: {
                    series: ['Attivazioni FR per provincia', 'Attivazioni LB per provincia'],
                    options: {
                        legend: {
                            display: false
                        }
                    }
                },
                activation: {
                    series: ['Eventi FR Giornalieri', 'Eventi Luci Blu  Giornalieri'],
                    options: {
                        legend: {
                            display: false
                        }
                    }
                },
                activationByType: {
                    series: ['First Responder Accettati', 'Luci Blu Accettati', 'SuperUser Accettati'],
                    options: {
                        legend: {
                            display: false
                        }
                    }
                },
                frRegistration: {
                    series: ['Registrazioni First Responder'],
                    options: {
                        legend: {
                            display: false
                        }
                    }
                },
                frDaeSubscriptions: {
                    series: ['Registrazioni DAE'],
                    options: {
                        legend: {
                            display: false
                        }
                    }
                },
                daeValidation: {
                    series: ['Validazioni DAE'],
                    options: {
                        legend: {
                            display: false
                        }
                    }
                },

                filter: {
                    from: new Date(),
                    to: new Date(),
                },

                reload: function() {
                    if ($scope.filter.from) {
                        $scope.filter.from.setHours(0, 0, 0);
                    }
                    if ($scope.filter.to) {
                        $scope.filter.to.setHours(23, 59, 59);
                    }
                    chartService.fetchDaeActivations($scope.filter).then(function(response) {
                        $scope.activationProv.data = [response.data.dataSet.daeFrEvents,
                          response.data.dataSet.daeLbEvents];
                        $scope.activationProv.labels = response.data.labels;
                        setStepSize($scope.activationProv, _.concat(response.data.dataSet.daeFrEvents,
                            response.data.dataSet.daeLbEvents));
                    });

                    chartService.fetchDaeActivationsByDay($scope.filter).then(function(response) {
                        $scope.activation.data = [
                          response.data.dataSet.daeEvents,
                          response.data.dataSet.daeFrEvents
                        ];
                        $scope.activation.labels = response.data.labels;

                        setStepSize($scope.activation,
                            _.concat(response.data.dataSet.daeEvents,
                                response.data.dataSet.daeFrEvents
                            ));

                    });

                    chartService.fetchFrActivationsByType($scope.filter).then(function(response) {
                        $scope.activationByType.data = [
                            response.data.dataSet.frAccepted,
                            response.data.dataSet.lbAccepted,
                            response.data.dataSet.suAccepted];

                        $scope.activationByType.labels = response.data.labels;
                        setStepSize($scope.activationByType,
                            _.concat(response.data.dataSet.frAccepted,
                                response.data.dataSet.lbAccepted,
                                response.data.dataSet.suAccepted));

                    });

                    chartService.fetchFrSubscriptions($scope.filter).then(function(response) {
                        $scope.frRegistration.data = [response.data.data];
                        $scope.frRegistration.labels = response.data.labels;
                        setStepSize($scope.frRegistration, response.data.data);
                    });

                    chartService.fetchDaeSubscriptions($scope.filter).then(function(response) {
                        $scope.frDaeSubscriptions.data = [response.data.data];
                        $scope.frDaeSubscriptions.labels = response.data.labels;
                        setStepSize($scope.frDaeSubscriptions, response.data.data);
                    });

                    chartService.fetchDaeValidation($scope.filter).then(function(response) {
                        $scope.daeValidation.data = [response.data.data];
                        $scope.daeValidation.labels = response.data.labels;
                        setStepSize($scope.daeValidation, response.data.data);
                    });

                }
            });

            this.$onInit = function() {
                $scope.reload();
            };
		}
	]);
});
