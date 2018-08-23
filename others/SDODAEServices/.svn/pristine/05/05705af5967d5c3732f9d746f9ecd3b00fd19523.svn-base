'use strict';
define(['angular', "moment", "lodash", "constants", "leaflet"], function (angular, moment, _, constants, L) {
    angular.module("controller").controller('EventSearchCtrl', [
        "$scope", "$filter", "emergencyService",
        "loggerService", "$rootScope", "alertService",
        "anagraficaService", "$uibModal", "$document",
        "modalService", "$state", 'uiGridConstants',
        "leafletBoundsHelpers", "validateService", "stateService",
        "daeService", "utilityService", "csvService",
        function ($scope, $filter, emergencyService,
            loggerService, $rootScope, alertService,
            anagraficaService, $uibModal, $document,
            modalService, $state, uiGridConstants,
            leafletService, validateService, stateService,
            daeService, utilityService, csvService) {
            $rootScope.$broadcast("main.changeTitle", {
                title: "Cerca Eventi",
                icon: "fa fa-phone"
            });

            angular.extend($scope, {
                filter: {
                    fetchRule: 'SEARCH',
                    dataDA: new Date(),
                    dataA: new Date(),
                    frStatus: "TUTTI"
                },
                comuni: [],
                strade: [],
                categorie: [],
                managedStatus: ["TUTTI", "ACCETTATI", "ARRIVATI"],

                /* opzioni per i date piker*/
                options: {
                    minDate: new Date(0),
                    showWeeks: false
                },
                formatDate: function (date) {
                    return moment(date).format(constants.exelDate);
                },
                /*Combo Comuni */
                refreshComune: function (input) {
                    if (input === undefined || input === null || !input || input.length < 3) {
                        $scope.comuni = [];
                        return [];
                    }
                    var filter = {
                        "nomeComune": input
                    };
                    return anagraficaService.searchComuniByFilter(filter).then(function (response) {
                        $scope.comuni = response;
                    });
                },
                /*Combo INDIRIZZO*/
                refreshStrade: function (search) {
                    if (search === undefined || search === null || !search || search.length < 3) {
                        $scope.strade = [];
                        return [];
                    }
                    var filter = {
                        "name": search
                    };
                    if ($scope.filter.comune) {
                        filter.nomeComune = $scope.filter.comune;
                    }
                    return anagraficaService.searchStradeByFilter(filter).then(function (response) {
                        $scope.strade = response;
                    });
                },
                pulisci: function () {
                    $scope.filter = {
                        fetchRule: 'SEARCH'
                    };
                    $scope.gridOptions.data = [];

                    stateService.removeState("eventSearchController.filter");
                },
                search: function (filterS) {
                    var filter = angular.copy(filterS);
                    if (filter.dataDA && filter.orarioDA) {
                        filter.dataDA.setHours(filter.orarioDA.getHours(), filter.orarioDA.getMinutes(), 0);
                    } else if (filter.dataDA) {
                        filter.dataDA.setHours(0, 0, 0);
                    }
                    if (filter.dataA && filter.orarioA) {
                        filter.dataA.setHours(filter.orarioA.getHours(), filter.orarioA.getMinutes(), 0);
                    } else if (filter.dataA) {
                        filter.dataA.setHours(23, 59, 59);
                    }

                    if (filter.frStatus == "TUTTI") {
                        filter.managed = undefined;
                        filter.accepted = undefined;
                    } else if (filter.frStatus == "ACCETTATI") {
                        filter.managed = undefined;
                        filter.accepted = true;
                    } else if (filter.frStatus == "ARRIVATI") {
                        filter.managed = true;
                        filter.accepted = undefined;
                    }

                    if (filter.comune && !_.isString(filter.comune)) {
                        filter.comune = filter.comune.nomeComune;
                    }

                    stateService.saveState("eventSearchController.filter", filter);
                    emergencyService.searchEventByFilter(filter).then(function (result) {
                        $scope.gridOptions.data = result;
                    });
                },
                showDetail: function (id) {
                    emergencyService.setCurrentEventId(id);
                    $state.transitionTo('main.eventDetail');
                },
                showMap: function (event) {
                    var markers = [];
                    var points = [];
                    //marker dell'evento:
                    markers.push({
                        lat: event.coordinate.latitudine,
                        lng: event.coordinate.longitudine,
                        draggable: false,
                        zIndexOffset: 1000,
                        message: event.cartellino,
                        icon: constants.eventMapIcon
                    });
                    points.push([event.coordinate.longitudine, event.coordinate.latitudine]);

                    var bounds = null;
                    if (!_.isEmpty(points)) {
                        var b = L.bounds(points);
                        bounds = leafletService.createBoundsFromArray([
                            [b.min.y, b.min.x],
                            [b.max.y, b.max.x]
                        ]);
                    }

                    var modal = modalService.openMapModal({
                        title: "Evento",
                        markers: markers,
                        bounds: bounds,
                        events: ['moveend'],
                        loadCluster: function (bounds, markerClusterLevel) {
                            if ($scope.lastReq) {
                                $scope.lastReq.then(function (data) {
                                    utilityService.loadDAE(bounds, markerClusterLevel, $scope);
                                });
                            } else {
                                utilityService.loadDAE(bounds, markerClusterLevel, $scope);
                            }
                        }
                    });
                }
            });

            //carico le categorie
            anagraficaService.searchCategorie({}).then(function (response) {
                $scope.categorie = response;
            });

            var stFilter = stateService.getState("eventSearchController.filter");
            //se nello stato è presente un filtro
            if (stFilter) {
                if (stFilter.dataDA) {
                    stFilter.dataDA = new Date(stFilter.dataDA);
                }
                if (stFilter.dataA) {
                    stFilter.dataA = new Date(stFilter.dataA);
                }
                if (stFilter.orarioDA) {
                    stFilter.orarioDA = new Date(stFilter.orarioDA);
                }
                if (stFilter.orarioA) {
                    stFilter.orarioA = new Date(stFilter.orarioA);
                }
                stFilter.frStatus = stFilter.accepted ? "ACCETTATI" : (stFilter.managed ? "ARRIVATI" : "TUTTI");

                $scope.search(angular.copy(stFilter));
                if (_.isString(stFilter.comune)) {
                    stFilter.comune = {
                        nomeComune: stFilter.comune
                    };
                }
                if (_.isString(stFilter.indirizzo)) {
                    stFilter.indirizzo = {
                        name: stFilter.indirizzo
                    };
                }
                $scope.filter = stFilter;
            }

            $scope.exportCSV = function () {

                csvService.exportExcel("eventList", $scope.gridOptions.data, [
                        "Data", "Centrale Operativa", "Cartellino",
                        "Categoria", "Provincia", "Comune", "Indirizzo",
                        "Civico", "Latitudine", "Longitudine",
                        "Luogo", "Telefono", "Info", "Riferimento",
                        "Data Chiusura", "Data Annullamento",
                        "Defibrillato", "Note Gestione DAE",
                        "Accettati", "Arrivati sul Posto", "Notifiche"
                    ],
                    function (row, separator) {
                        var result = [
                            $scope.formatDate(row.timestamp),
                            csvService.notNull(row.coRiferimento),
                            csvService.notNull(row.cartellino),
                            csvService.notNull(row.categoria.descrizione),
                            csvService.notNull(row.provincia),
                            csvService.notNull(row.comune),
                            csvService.notNull(row.indirizzo),
                            csvService.notNull(row.civico),
                            csvService.notNull(row.coordinate.latitudine),
                            csvService.notNull(row.coordinate.longitudine),

                            csvService.notNull(row.luogo),
                            csvService.notNull(row.telefono),
                            csvService.notNull(row.info),
                            csvService.notNull(row.riferimento),
                            $scope.formatDate(row.closeDate),
                            $scope.formatDate(row.abortDate),

                            csvService.notNull(row.defibrillato ? "SI" : "NO"),
                            csvService.notNull(row.noteDAE),

                            csvService.notNull(row.accettati),
                            csvService.notNull(row.arrivati),
                            csvService.notNull(row.conteggioNotifiche)
                        ];
                        return result;
                    });

            };

            $scope.gridOptions = {
                enableGridMenu: true,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 1,
                enablePaginationControls: false,
                paginationPageSize: 13,
                virtualizationThreshold: 50,
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                    var resizeHandler = $rootScope.$watch('collapsed', function (old, newVal) {
                        gridApi.core.handleWindowResize();
                    });
                    $scope.$on("$destroy", function (event) {
                        //distruggo l'handler
                        resizeHandler();
                    });
                },
                columnDefs: [{
                    displayName: 'Data',
                    field: 'timestamp',
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{grid.appScope.formatDate(row.entity.timestamp)}}\" tooltip-placement=\"left\" >{{grid.appScope.formatDate(row.entity.timestamp)}}</div>",
                    sort: {
                        direction: uiGridConstants.DESC,
                        priority: 1
                    },
                    sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                        var toRet = 0;
                        if (rowA.entity.timestamp === rowB.entity.timestamp) {
                            return 0;
                        } else if (rowA.entity.timestamp < rowB.entity.timestamp) {
                            toRet = -1;
                        } else {
                            toRet = 1;
                        }
                        return toRet;
                    }
                }, {
                    displayName: 'Evento',
                    field: 'cartellino',
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.coRiferimento}} - {{row.entity.cartellino}}\" tooltip-placement=\"left\" >{{row.entity.coRiferimento}} - {{row.entity.cartellino}}</div>",
                }, {
                    displayName: 'Categoria FR',
                    field: 'categoria',
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.categoria.descrizione}}\" tooltip-placement=\"left\" >{{row.entity.categoria.descrizione}}</div>",
                }, {
                    displayName: 'Provincia',
                    field: 'provincia',
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.provincia}}\" tooltip-placement=\"left\" >{{row.entity.provincia}}</div>",
                }, {
                    displayName: 'Comune',
                    field: 'comune',
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.comune}}\" tooltip-placement=\"left\" >{{row.entity.comune}}</div>",
                }, {
                    displayName: 'Indirizzo',
                    field: 'indirizzo',
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.indirizzo}}\" tooltip-placement=\"left\" >{{row.entity.indirizzo}}</div>",
                }, {
                    displayName: 'A',
                    field: 'accettati',
                    width: "60",
                    sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                        return Number(a) - Number(b);
                    }
                }, {
                    displayName: 'P',
                    field: 'arrivati',
                    width: "60",
                    sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                        return Number(a) - Number(b);
                    }
                }, {
                    displayName: 'N',
                    field: 'conteggioNotifiche',
                    width: "60",
                    sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                        return Number(a) - Number(b);
                    }
                }, {
                    displayName: '',
                    name: 'Visualizza',
                    cellTemplate: "<div class=\"grid-icon\" style=\"padding:0px\" >" +
                        "<a ng-click=\"grid.appScope.showMap(row.entity)\"  uib-tooltip=\"Visualizza L'evento sulla mappa\" tooltip-placement=\"left\" ng-show=\"row.entity.coordinate && row.entity.coordinate.latitudine && row.entity.coordinate.longitudine\"> " +
                        "<i class=\"fa fa-map-o\" aria-hidden=\"true\"></i></a> " +
                        "<a ng-click=\"grid.appScope.showDetail(row.entity.id)\" uib-tooltip=\"Visualizza\" tooltip-placement=\"left\"> " +
                        "<i class=\"fa fa-eye\" aria-hidden=\"true\"></i></a>" +
                        "</div>",
                    width: "60",
                    enableSorting: false,
                    enableFiltering: false
                }]
            };
        }
    ]);
});