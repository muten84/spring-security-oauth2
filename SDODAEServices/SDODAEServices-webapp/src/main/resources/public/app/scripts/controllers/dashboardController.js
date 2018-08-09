'use strict';

define(['angular', "lodash", "constants", "leaflet", "moment"], function (angular, _, constants, L, moment) {

    function setStepSize($scope, data) {
        var max = 0;
        if (data) {
            data.forEach(function (d) {
                var num = Number(d);
                max = num > max ? num : max;
            });
        }
        if (max <= 5) {
            $scope.options.scales = {
                yAxes: [{
                    ticks: {
                        min: 0,
                        stepSize: 1
                    }
                }]
            };
        } else {
            $scope.options.scales = {
                yAxes: [{
                    ticks: {
                        min: 0
                    }
                }]
            };
        }
    }

    angular.module("controller").controller("UserActivationProfileCtrl2", [
        "$scope", "frService", "$timeout", "$state", 'uiGridConstants',
        function ($scope, frService, $timeout, $state, uiGridConstants) {

            var profiloTemnplate = "<a style=\"margin: -9px;\" class=\"list-group-item\" ng-click=\"grid.appScope.setInModifying(row.entity.id)\" title=\"{{row.entity.nome}} {{row.entity.cognome}}\">" +
                "<div class=\"media\">" +
                "<div class=\"media-left\">" +
                "<img class=\"media-object img-circle img-avatar\" ng-src=\"{{row.entity.immagine.url}}?size=SMALL\" alt=\"...\">" +
                "</div>" +
                "<div class=\"media-body\">" +
                "<span class=\"media-title\">{{row.entity.nome}}" +
                "{{row.entity.cognome}}</span>" +
                "<span class=\"media-subheading\">{{row.entity.categoriaFr.descrizione}}</span>" +
                '</div>' +
                '</div>' +
                '</a>';

            $scope.gridOptions = {
                onRegisterApi: function (gridApi) {
                    $scope.gridApi2 = gridApi;
                },
                enableGridMenu: false,
                rowHeight: "50",
                virtualizationThreshold: 50,
                enablePaginationControls: false,
                paginationPageSize: 4,
                showHeader: false,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 0,
                columnDefs: [{
                    displayName: 'Profilo',
                    cellTemplate: profiloTemnplate,
                    width: "*",
                    name: 'Profilo',
                }, {
                    displayName: 'Data Iscrizione',
                    field: 'dataIscrizione',
                    width: "0",
                    sort: {
                        direction: uiGridConstants.DESC,
                    }
                }]
            };

            $scope.setInModifying = function (id) {
                frService.setCurrentFr(id);
                $state.transitionTo('main.frUpdate');
            };

            frService.searchFirstResponderByFilter({
                daAttivare: true,
                fetchRule: 'DASHBOARD'
            }).then(function (response) {
                $scope.gridOptions.data = response;
                $scope.users = response;
            });
        }
    ]);

    angular.module("controller").controller("DaeFaultProfileCtrl", [
        "$scope", "daeService", "$timeout", "$state",
        function ($scope, daeService, $timeout, $state) {

            var modelloTemnplate = "<a style=\"margin: -9px;\" class=\"list-group-item\" ng-click=\"grid.appScope.setInModifying(row.entity.id)\" > " +
                "<div   class='media'>" +
                "<div class=\"media-left\"  >" +
                "<img class=\"media-object img-circle img-avatar\" ng-src=\"{{row.entity.immagine.url }}\"  ng-show=\"row.entity.immagine\" alt=\"...\" />" +
                "</div>" +
                "<div class=\"media-body \">" +
                "<span class=\"media-title\">{{row.entity.nomeSede}} - {{row.entity.tipologiaStruttura.descrizione}}</span>" +
                "<span class=\"media-subheading\">Responsabile: {{row.entity.responsabile.cognome}}</span>" +
                "<span ng-repeat=\"guasto in row.entity.guasti|limitTo:1\" class=\"media-content\">Segnalazione {{guasto.tipologia}}: {{guasto.dataSegnalazione |  date:'dd/MM/yyyy'}} </span>" +
                "</div>" +
                "</div></a>";

            $scope.gridOptions = {
                onRegisterApi: function (gridApi) {
                    $scope.gridApi2 = gridApi;
                },
                enableGridMenu: false,
                rowHeight: "69",
                virtualizationThreshold: 69,
                enablePaginationControls: false,
                paginationPageSize: 3,
                showHeader: false,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 0,
                columnDefs: [{
                    displayName: 'Modello',
                    cellTemplate: modelloTemnplate,
                    width: "*",
                    name: 'Modello',
                }]
            };

            $scope.setInModifying = function (id) {
                daeService.setCurrentDaeId(id);
                $state.transitionTo('main.daeFault');
            };
            this.$onInit = function () {
                daeService.searchDAEByFilter({
                    fetchRule: 'DASHBOARD',
                    isInFault: true,
                    orderBy: {
                        property: "guasti.dataSegnalazione",
                        ascending: false
                    }
                }).then(function (response) {

                    response.forEach(function (dae) {
                        if (dae.immagine.url) {
                            //serve per evitare il cache dell'immagine da parte del browser
                            dae.immagine.url = dae.immagine.url + '?t=' + Date.now() + "&size=SMALL";
                        }

                        dae.guasti =  _.orderBy(dae.guasti, ["dataSegnalazione"], ['desc']);
                    });

                    $scope.gridOptions.data = response;
                    $scope.daeList = response;
                });
            };

        }
    ]);

    angular.module("controller").controller("DaeActivationProfileCtrl", ["$scope", "daeService", "$timeout", "$state",
        function ($scope, daeService, $timeout, $state) {

            /*<div class="media-body">
		  	<span class="media-heading"><a href="donatori-dettaglio.html" title="Rossetti Giacomo" tabindex="">Rossetti Giacomo</a></span>
		  	<span class="media-subheading">Nato a Napoli il 2 Novembre 1989</span>
	  	</div>*/

            var modelloTemnplate = "<a style=\"margin: -9px;\" class=\"list-group-item\" ng-click=\"grid.appScope.setInModifying(row.entity.id)\" > " +
                "<div  class='media'>" +
                "<div class=\"media-left\"  >" +
                "<img class=\"media-object img-circle img-avatar\"  ng-src=\"{{row.entity.immagine.url }} \"  ng-show=\"row.entity.immagine\" alt=\"...\" />" +
                "</div>" +
                "<div class=\"media-body \">" +
                "<span class=\"media-title\">{{row.entity.nomeSede}} - {{row.entity.tipologiaStruttura.descrizione}}</span>" +
                "<span class=\"media-subheading\">Responsabile:  {{row.entity.responsabile.cognome}}</span>" +
                "<span class=\"media-content\">Data Creazione : {{row.entity.dataInserimento |  date:'dd/MM/yyyy'}} </span>" +
                 "</div>" +
                "</div></a>";

            $scope.formatDate = function (date) {
                return moment(date).format(constants.exelDate);
            }

            $scope.gridOptions = {
                onRegisterApi: function (gridApi) {
                    $scope.gridApi2 = gridApi;
                },
                enableGridMenu: false,
                rowHeight: "69",
                virtualizationThreshold: 69,
                enablePaginationControls: false,
                paginationPageSize: 3,
                showHeader: false,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 0,
                columnDefs: [{
                    displayName: 'Modello',
                    cellTemplate: modelloTemnplate,
                    width: "*",
                    name: 'Modello',
                } ]
            };

            $scope.setInModifying = function (id) {
                daeService.setCurrentDaeId(id);
                $state.transitionTo('main.daeInsert');
            };
            this.$onInit = function () {
                daeService.searchDAEByFilter({
                    fetchRule: 'DASHBOARD',
                    statoValidazione: "DA_VALIDARE",
                    orderBy: {
                        property: "dataInserimento",
                        ascending: false
                    }
                }).then(function (response) {

                    response.forEach(function (dae) {
                        if (dae.immagine.url) {
                            //serve per evitare il cache dell'immagine da parte del browser
                            dae.immagine.url = dae.immagine.url + '?t=' + Date.now() + "&size=SMALL";
                        }
                    });

                    $scope.gridOptions.data = response;
                    $scope.daeList = response;
                });
            };

        }
    ]);

    angular.module("controller").controller('DaeExpirationProfileCtrl', ["$scope", "$controller", "daeService", "$timeout", "$state",
        function ($scope, $controller, daeService, $timeout, $state) {
            //Estendo il controller di sopra, tanto sono uguali
            /* angular.extend(this, $controller('DaeActivationProfileCtrl', {
                 $scope: $scope,
             }));*/

            var modelloTemnplate = "<a style=\"margin: -9px;\" class=\"list-group-item\" ng-click=\"grid.appScope.setInModifying(row.entity.id)\" >" +
                " <div   class='media'>" +
                "<div class=\"media-left\"  >" +
                "<img class=\"media-object img-circle img-avatar\"  ng-src=\"{{row.entity.immagine.url }} \"  ng-show=\"row.entity.immagine\" alt=\"...\" />" +
                "</div>" +
                "<div class=\"media-body \">" +
                "<span class=\"media-title\">{{row.entity.nomeSede}}</span>" +
                "<span class=\"media-subheading\">Responsabile: {{row.entity.responsabile.cognome}}</span>" +
                "<span ng-repeat=\"prog in row.entity.programmiManutenzione\" class=\"media-content\">Scadenza {{prog.tipoManutenzione}}: {{prog.scadenzaDopo |  date:'dd/MM/yyyy'}} </span>" +
                "</div>" +
                "</div></a>";

            $scope.gridOptions = {
                onRegisterApi: function (gridApi) {
                    $scope.gridApi2 = gridApi;
                },
                enableGridMenu: false,
                rowHeight: "69",
                virtualizationThreshold: 69,
                enablePaginationControls: false,
                paginationPageSize: 3,
                showHeader: false,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 0,
                columnDefs: [{
                    displayName: 'Modello',
                    cellTemplate: modelloTemnplate,
                    width: "*",
                    name: 'Modello',
                }]
            };

            $scope.setInModifying = function (id) {
                daeService.setCurrentDaeId(id);
                $state.transitionTo('main.daeInsert');
            };

            this.$onInit = function () {
                //cerco i dae in scadenza a 2 mesi
                var dataScadenzaDA = moment();
                dataScadenzaDA.set({
                    'hour': 0,
                    'minute': 0,
                    'second': 0
                });
                var dataScadenzaA = moment();
                dataScadenzaA.set({
                    'hour': 23,
                    'minute': 59,
                    'second': 59
                });
                dataScadenzaA.add(2, 'M');

                daeService.searchDAEByFilter({
                    fetchRule: 'DASHBOARD',
                    operativo: true,
                    scadenzaManutenzioneDa: dataScadenzaDA.toDate(),
                    scadenzaManutenzioneA: dataScadenzaA.toDate(),
                    orderBy: {
                        property: "programmiManutenzione.scadenzaDopo",
                        ascending: false
                    }

                }).then(function (response) {

                    response.forEach(function (dae) {
                        if (dae.immagine.url) {
                            //serve per evitare il cache dell'immagine da parte del browser
                            dae.immagine.url = dae.immagine.url + '?t=' + Date.now() + "&size=SMALL";
                        }
                    });

                    $scope.gridOptions.data = response;
                    $scope.daeList = response;
                });
            };
        }
    ]);

    angular.module("controller").controller('DaeExpiratedProfileCtrl', ["$scope", "$controller", "daeService", "$timeout", "$state",
        function ($scope, $controller, daeService, $timeout, $state) {
            //Estendo il controller di sopra, tanto sono uguali
            /* angular.extend(this, $controller('DaeActivationProfileCtrl', {
                 $scope: $scope,
             }));*/

            var modelloTemnplate = "<a style=\"margin: -9px;\" class=\"list-group-item\" ng-click=\"grid.appScope.setInModifying(row.entity.id)\" >" +
                " <div   class='media'>" +
                "<div class=\"media-left\"  >" +
                "<img class=\"media-object img-circle img-avatar\"  ng-src=\"{{row.entity.immagine.url }}\"  ng-show=\"row.entity.immagine\" alt=\"...\" />" +
                "</div>" +
                "<div class=\"media-body \">" +
                "<span class=\"media-title\">{{row.entity.nomeSede}}</span>" +
                "<span class=\"media-subheading\">Responsabile: {{row.entity.responsabile.cognome}}</span>" +
                "<span ng-repeat=\"prog in row.entity.programmiManutenzione\" class=\"media-content\">Scadenza {{prog.tipoManutenzione}}: {{prog.scadenzaDopo |  date:'dd/MM/yyyy'}} </span>" +
                "</div>" +
                "</div></a>";

            $scope.gridOptions = {
                onRegisterApi: function (gridApi) {
                    $scope.gridApi2 = gridApi;
                },
                enableGridMenu: false,
                rowHeight: "69",
                virtualizationThreshold: 69,
                enablePaginationControls: false,
                paginationPageSize: 3,
                showHeader: false,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 0,
                columnDefs: [{
                    displayName: 'Modello',
                    cellTemplate: modelloTemnplate,
                    width: "*",
                    name: 'Modello',
                }]
            };

            $scope.setInModifying = function (id) {
                daeService.setCurrentDaeId(id);
                $state.transitionTo('main.daeInsert');
            };

            this.$onInit = function () {
                //cerco i dae  scaduti oggi
                var dataScadenzaDA = moment(0);
                //dae scaduti oggi
                var dataScadenzaA = moment();
                dataScadenzaA.set({
                    'hour': 23,
                    'minute': 59,
                    'second': 59
                });

                daeService.searchDAEByFilter({
                    fetchRule: 'DASHBOARD',
                    operativo: false,
                    scadenzaManutenzioneDa: dataScadenzaDA.toDate(),
                    scadenzaManutenzioneA: dataScadenzaA.toDate(),
                    orderBy: {
                        property: "programmiManutenzione.scadenzaDopo",
                        ascending: false
                    }
                }).then(function (response) {

                    response.forEach(function (dae) {
                        if (dae.immagine.url) {
                            //serve per evitare il cache dell'immagine da parte del browser
                            dae.immagine.url = dae.immagine.url + '?t=' + Date.now() + "&size=SMALL";
                        }
                    });

                    $scope.gridOptions.data = response;
                    $scope.daeList = response;
                });
            };
        }
    ]);

    angular.module("controller").controller("UserActivationProfileCtrl", ["$scope", "frService", "$timeout", "$state",
        function ($scope, frService, $timeout, $state) {
            $scope.myInterval = 5000;
            $scope.noWrapSlides = false;
            $scope.active = 0;
            $scope.users = [];
            var slides = $scope.slides = [];
            var currIndex = 0;
            var el = angular.element().find("#frSlider");
            /*    if(el){
                    el.bxSlider
                        ({
                            captions: true,
                            auto: true,
                            autoControls: true,
                            slideWidth: 110,
                            minSlides: 1,
                            maxSlides: 6,
                            moveSlides: 1,
                            slideMargin: 10,
                            pager: false,
                            autoHover: true
                        });
                }*/

            $scope.setInModifying = function (id) {
                frService.setCurrentFr(id);
                $state.transitionTo('main.frUpdate');
            };

            $timeout(function () {
                frService.searchFirstResponderByFilter({
                    daAttivare: true
                }).then(function (response) {
                    $scope.users = response;

                    response.forEach(function (o) {
                        slides.push({
                            id: currIndex++,
                            user: o,
                        });
                    });

                });
            }, 1000);
        }
    ]);

    angular.module("controller").controller("LineChartCtrl", ["$scope", "chartService", function ($scope, charts) {
        $scope.options = {
            legend: {
                display: false
            }
        };
        $scope.series = ['Registrazioni First Responder'];
        var response = {
            "data": ["0"],
            "labels": ["0"]
        };
        $scope.data2 = response.data;
        $scope.labels2 = response.labels;
        charts.fetchFrSubscriptions().then(function (response) {
            $scope.data2 = [response.data.data];
            $scope.labels2 = response.data.labels;
            setStepSize($scope, response.data.data);
        });
    }]);

    angular.module("controller").controller("LineChartDAECtrl", ["$scope", "chartService", function ($scope, charts) {
        $scope.options = {
            legend: {
                display: false
            }
        };
        $scope.series = ['Registrazioni DAE'];
        charts.fetchDaeSubscriptions().then(function (response) {
            $scope.data = [response.data];
            $scope.labels = response.labels;
            setStepSize($scope, response.data);
        });
    }]);

    angular.module("controller").controller("DaeEventActivationCtrl", ["$scope", "chartService", function ($scope, charts) {
        $scope.options = {
            legend: {
                display: false
            }

        };
        $scope.series = ['Attivazioni DAE FR per provincia', 'Attivazioni DAE LB per provincia'];
        charts.fetchDaeActivations().then(function (response) {
            $scope.data = [response.dataSet.daeFrEvents,
                response.dataSet.daeLbEvents
            ];
            $scope.labels = response.labels;
            setStepSize($scope, _.concat(response.dataSet.daeFrEvents,
                response.dataSet.daeLbEvents));
        });
    }]);

    angular.module("controller").controller("DaeEventActivationCtrlByDay", ["$scope", "chartService", function ($scope, charts) {
        $scope.options = {
            legend: {
                display: false
            }
        };
        $scope.series = ['Eventi FR Giornalieri', 'Eventi Luci Blu Giornalieri'];
        charts.fetchDaeActivationsByDay().then(function (response) {
            $scope.data = [
                response.dataSet.daeEvents,
                response.dataSet.daeFrEvents
            ];
            $scope.labels = response.labels;
            setStepSize($scope, _.concat(
                response.dataSet.daeEvents,
                response.dataSet.daeFrEvents));
        });
    }]);

    angular.module("controller").controller("BarChartCtrl", ["$scope", function ($scope) {
        $scope.options = {
            legend: {
                display: true
            },
            scales: {
                yAxes: [{
                    ticks: {
                        stepSize: 1
                    }
                }]
            }
        };
        $scope.labels = ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Dec'];
        $scope.series = ['Inviati', 'Accettati'];
        $scope.data = [
            [65, 59, 69, 81, 56, 55, 40, 34, 80, 61, 72],
            [28, 48, 40, 19, 35, 27, 35, 25, 11, 40, 30]
        ];
    }]);

    angular.module("controller").controller("RadarChartCtrl", ["$scope", function ($scope) {
        var radarScope = this;
        $scope.labels = ['Emilia-Est', 'Emilia-Ovest', 'Romagna'];
        $scope.options = {
            legend: {
                display: false
            }
        };

        $scope.data = [
            [65, 59, 90],
            [28, 48, 40]
        ];

        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };
    }]);

    angular.module("controller").controller('DashboardCtrl', ["$rootScope", "$scope",
        "daeService", "leafletBoundsHelpers", "validateService", "leafletData", "utilityService",
        function ($rootScope, $scope, daeService, leafletService, validateService, leafletData, utilityService) {
            $rootScope.$broadcast("main.changeTitle", {
                title: "Dashboard DAE",
                icon: "glyphicon glyphicon-th"
            });

            $scope.daeBounds = [];
            $scope.center = {
                //lat: 44.491607329696045,
                //  lng: 11.419944763183594,
                //    zoom: 7
            };
            $scope.daeMarkers = [];
            $scope.daeControls = {
                custom: [L.Control.boxzoom({
                    position: 'topleft'
                })]
            };

            var markerClusterLevel = L.markerClusterGroup();
            leafletData.getMap().then(function (map) {
                map.addLayer(markerClusterLevel);
            });

            $scope.layers = {

                baselayers: {
                    osm: {
                        name: 'OpenStreetMap',
                        url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                        type: 'xyz',
                        layerParams: {
                            showOnSelector: false
                        }
                    }
                }
            };

            var handlerMove = $rootScope.$on('leafletDirectiveMap.moveend', function (e, c) {
                utilityService.loadDAE(c.leafletEvent.target.getBounds(), markerClusterLevel, $scope);
            });

            $scope.$on("$destroy", function () {
                handlerMove();
            });

            $scope.center = {
                /*44,491607329696045
                11,419944763183594*/
                lat: 44.491607329696045,
                lng: 11.419944763183594,
                zoom: 7
            };
        }
    ]);

    angular.module("controller").controller("NmberDAEandFRCtrl", ["$scope", "chartService", function ($scope, charts) {
        $scope.val = {};
        charts.fetchDaeNumbers().then(function (response) {
            $scope.val = response;
        });
    }]);

});