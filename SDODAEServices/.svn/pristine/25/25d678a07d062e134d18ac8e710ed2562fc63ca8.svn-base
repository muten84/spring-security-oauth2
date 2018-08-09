'use strict';

// @ts-ignore
define(['angular', "moment", "lodash", "constants", "leaflet"],
    function (angular, moment, _, constants, L) {
        angular.module("controller").controller('EventDetailCtrl', [
            "$scope", "$filter", "emergencyService",
            "loggerService", "$rootScope", "alertService",
            "$document", "$state", "modalService",
            'uiGridConstants', "validateService", "currentEvent",
            "$timeout", "leafletBoundsHelpers", "daeService",
            "utilityService", "$uibModal", "toastr", "csvService",
            function ($scope, $filter, emergencyService,
                loggerService, $rootScope, alertService,
                $document, $state, modalService,

                uiGridConstants, validateService, currentEvent,
                $timeout, leafletService, daeService,
                utilityService, $uibModal, toastr, csvService) {

                $rootScope.$broadcast("main.changeTitle", {
                    title: "Dettaglio Evento",
                    icon: "fa fa-phone"
                });

                angular.extend($scope, {
                    formatDate: function (d) {
                        if (d) {
                            return moment(d).format(constants.exelDate);
                        } else {
                            return "";
                        }
                    },
                    resizeGrid: function (type) {
                        if (type === 'interventi' && $scope.gridInterventiApi) {
                            $timeout(function () {
                                $scope.gridInterventiApi.core.handleWindowResize();
                            }, 500);
                        }
                        if (type === 'notifiche' && $scope.gridNotificheApi) {
                            $timeout(function () {
                                $scope.gridNotificheApi.core.handleWindowResize();
                            }, 500);

                        }
                    },
                    setEvent: function (event) {

                        event.interventi = _.orderBy(event.interventi, ["dataCreazione"], ['asc']);

                        $scope.event = event;

                        $scope.gridInterventiOptions.data = event.interventi;


                    },
                    showNotify: function () {
                        var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                        var parentScope = $scope;
                        var modalInstance = $uibModal.open({
                            /* Prevengono l'uscita cliccando fuori oppure esc*/
                            backdrop: 'static',
                            keyboard: false,
                            animation: true,
                            templateUrl: "notifyModal.html",
                            appendTo: parentElem,
                            size: 'lg',
                            controller: function ($scope, $uibModalInstance) {
                                var $ctrl = this;
                                $scope.formatDate = parentScope.formatDate;

                                $scope.singleFilter = function (renderableRows) {
                                    var matcher = new RegExp($scope.filterValue);
                                    renderableRows.forEach(function (row) {
                                        var match = false;
                                        if (row.entity.firstResponder.email.match(matcher)) {
                                            match = true;
                                        }
                                        if (!match) {
                                            row.visible = false;
                                        }
                                    });
                                    return renderableRows;
                                };

                                $scope.refresh = function () {
                                    $scope.gridNotificheApi.grid.refresh();
                                }

                                $scope.gridNotificcheOptions = parentScope.gridNotificcheOptions;
                                $scope.gridNotificcheOptions.onRegisterApi = function (gridApi) {
                                    $scope.gridNotificheApi = gridApi;
                                    gridApi.grid.registerRowsProcessor($scope.singleFilter, 200);
                                };
                                //carico le notifiche
                                emergencyService.searchNotifiche(parentScope.event.id).then(function (result) {
                                    result = _.orderBy(result, ["timestamp"], ['asc']);
                                    if (result) {
                                        result.forEach(function (el) {
                                            if (el.tipoSelezione === 'COORD') {
                                                el.tipoSelezioneText = 'Coordinate Valide';
                                            } else if (el.tipoSelezione === 'COMUNE') {
                                                el.tipoSelezioneText = 'Comune Competenza';
                                            } else if (el.tipoSelezione === 'SUPER') {
                                                el.tipoSelezioneText = 'Categoria';
                                            }
                                        });
                                    }

                                    $scope.gridNotificcheOptions.data = result;
                                });

                                $scope.exportCSV = function () {

                                    csvService.exportExcel("notifyList", $scope.gridNotificcheOptions.data, [
                                            "Data", "Email", "Nome",
                                            "Cognome", "Categoria", "Notifica",
                                            "Criterio Attivazione",
                                            "Data ultima coordinata valida",
                                            "Distanza ultima coordinata valida"
                                        ],
                                        function (row, separator) {
                                            var result = [
                                                $scope.formatDate(row.timestamp),
                                                csvService.notNull(row.firstResponder.email),
                                                csvService.notNull(row.firstResponder.nome),
                                                csvService.notNull(row.firstResponder.cognome),
                                                csvService.notNull(row.firstResponder.categoriaFr.descrizione),
                                                csvService.notNull(row.tipoNotifica),
                                                csvService.notNull(row.tipoSelezioneText),
                                                $scope.formatDate(row.coordTimestamp),
                                                row.longitudine && row.latitudine &&
                                                parentScope.event.coordinate.longitudine &&
                                                parentScope.event.coordinate.latitudine ?
                                                L.latLng(
                                                    row.longitudine,
                                                    row.latitudine)
                                                .distanceTo(L.latLng(
                                                    parentScope.event.coordinate.longitudine,
                                                    parentScope.event.coordinate.latitudine)) : 0
                                            ];
                                            return result;
                                        });

                                };

                            }

                        });
                    },
                    refreshInterventi: function () {
                        $scope.gridInterventiApi.grid.refresh();
                    },

                    filterFnInterventi: function (renderableRows) {
                        var matcher = new RegExp($scope.filterInterventi);
                        renderableRows.forEach(function (row) {
                            var match = false;
                            var value = row.entity.eseguitoDa.email + " " +
                                row.entity.eseguitoDa.nome + " " +
                                row.entity.eseguitoDa.cognome;
                            if (value.match(matcher)) {
                                match = true;
                            }
                            if (!match) {
                                row.visible = false;
                            }
                        });
                        return renderableRows;
                    },

                    exportInterventiCSV: function () {
                        csvService.exportExcel("interventionList", $scope.gridInterventiOptions.data, [
                                "Email", "Nome", "Cognome", "Categoria",
                                "Accettato",
                                "Data Accettazione",
                                "Data Rifiuto", "Data Arrivo sul posto",
                            ],
                            function (row, separator) {
                                var result = [
                                    csvService.notNull(row.eseguitoDa.email),
                                    csvService.notNull(row.eseguitoDa.nome),
                                    csvService.notNull(row.eseguitoDa.cognome),
                                    csvService.notNull(row.eseguitoDa.categoriaFr.descrizione),
                                    row.dataAccettazione ? "Si" : "NO",
                                    $scope.formatDate(row.dataAccettazione),
                                    $scope.formatDate(row.dataRifiuto),
                                    $scope.formatDate(row.dataChiusura)
                                ];
                                return result;
                            });

                    },
                    saveEvent: function () {
                        emergencyService.saveEvent($scope.event).then(function (result) {
                            toastr.success('Salvataggio dei dati avvenuto con successo', 'Salvataggio avvenuto');
                            $scope.evet = result;
                        });
                    },
                    showMap: function (el) {
                        var markers = [];
                        var points = [];

                        //marker dell'evento:
                        markers.push({
                            lat: $scope.event.coordinate.latitudine,
                            lng: $scope.event.coordinate.longitudine,
                            draggable: false,
                            zIndexOffset: 1000,
                            message: $scope.event.cartellino,
                            icon: constants.eventMapIcon
                        });
                        points.push([$scope.event.coordinate.longitudine, $scope.event.coordinate.latitudine]);
                        //marker degli interventi
                        if ($scope.event.interventi) {
                            $scope.event.interventi.forEach(function (el) {
                                if (el.coordinate) {
                                    el.coordinate.forEach(function (coord) {
                                        if (validateService.validateCoord(coord.longitudine) && validateService.validateCoord(coord.latitudine)) {
                                            points.push([coord.longitudine, coord.latitudine]);
                                            markers.push({
                                                lat: coord.latitudine,
                                                lng: coord.longitudine,
                                                draggable: false,
                                                zIndexOffset: 1000,
                                                message: el.eseguitoDa.nome + ' ' + el.eseguitoDa.cognome,
                                                icon: constants.frMapIcon
                                            });
                                        }
                                    });
                                }
                            });
                        }

                        if (el) {
                            if (validateService.validateCoord(el.longitudine) &&
                                validateService.validateCoord(el.latitudine)) {
                                points.push([el.longitudine, el.latitudine]);
                                markers.push({
                                    lat: el.latitudine,
                                    lng: el.longitudine,
                                    draggable: false,
                                    zIndexOffset: 1000,
                                    message: el.firstResponder.nome + ' ' + el.firstResponder.cognome,
                                    icon: constants.frMapIcon
                                });
                            }
                        }

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
                this.$onInit = function () {
                    //Carico il DAE dal db e riempio la maschera
                    if (!_.isUndefined(currentEvent)) {
                        $scope.setEvent(currentEvent[0]);
                    }
                };

                $rootScope.$watch('collapsed', function (old, newVal) {
                    $scope.resizeGrid('interventi');
                    $scope.resizeGrid('notifiche');
                });

                $scope.gridNotificcheOptions = {
                    enableGridMenu: true,
                    enableHorizontalScrollbar: 0,
                    enableVerticalScrollbar: 1,
                    //abilitazione all'expand
                    expandableRowHeight: 45,
                    enablePaginationControls: false,
                    paginationPageSize: 8,
                    rowHeight: "50",
                    virtualizationThreshold: 50,
                    useExternalFiltering: true,
                    onRegisterApi: function (gridApi) {
                        $scope.gridNotificheApi = gridApi;
                    },
                    columnDefs: [{
                        displayName: 'First Responder',
                        field: 'firstResponder.nome',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.firstResponder.nome + ' ' + row.entity.firstResponder.cognome + '(' + row.entity.firstResponder.email+ ')'}}\" tooltip-placement=\"right\" >{{row.entity.firstResponder.nome + ' ' + row.entity.firstResponder.cognome+ '(' + row.entity.firstResponder.email+ ')'}}</div>",
                    }, {
                        displayName: 'Categoria',
                        field: 'firstResponder.categoriaFr.descrizione',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.firstResponder.categoriaFr.descrizione}}\" tooltip-placement=\"left\" >{{row.entity.firstResponder.categoriaFr.descrizione}}</div>",
                        width: "120",
                    }, {
                        displayName: 'Data Invio',
                        field: 'dataCreazione',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{grid.appScope.formatDate(row.entity.timestamp)}}\" tooltip-placement=\"left\" >{{grid.appScope.formatDate(row.entity.timestamp)}}</div>",
                        width: "150",
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
                        displayName: 'Tipo',
                        field: 'tipoNotifica',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.tipoNotifica}}\" tooltip-placement=\"left\" >{{row.entity.tipoNotifica}}</div>",
                        width: "50",
                    }, {
                        displayName: 'Criterio Attivazione',
                        field: 'tipoSelezioneText',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.tipoSelezioneText}}\" tooltip-placement=\"left\" >{{row.entity.tipoSelezioneText}}</div>",
                        width: "120",
                    }, {
                        displayName: '',
                        name: 'Visualizza',
                        cellTemplate: "<div class=\"grid-icon\"  >" +
                            "<a  uib-tooltip=\"Coordinate aggiornate al: {{grid.appScope.formatDate(row.entity.coordTimestamp)}}\" tooltip-placement=\"left\" ng-show=\" row.entity.latitudine && row.entity.longitudine\"> " +
                            "<i class=\"fa fa-map-marker\" aria-hidden=\"true\"></i></a> " +
                            "</div>",
                        width: "50",
                        enableSorting: false,
                        enableFiltering: false
                    }]
                }; /* Tolto dall'icona delle coordinate ng-click=\"grid.appScope.showMap(row.entity)\" */

                $scope.gridInterventiOptions = {
                    enableGridMenu: true,
                    enableHorizontalScrollbar: 0,
                    enableVerticalScrollbar: 1,
                    //abilitazione all'expand
                    expandableRowTemplate: 'interventoRowTemplate.html',
                    expandableRowHeight: 45,
                    //subGridVariable will be available in subGrid scope
                    enableExpandableRowHeader: false,
                    enablePaginationControls: false,
                    paginationPageSize: 8,
                    rowHeight: "50",
                    virtualizationThreshold: 50,
                    onRegisterApi: function (gridApi) {
                        $scope.gridInterventiApi = gridApi;
                        gridApi.grid.registerRowsProcessor($scope.filterFnInterventi, 200);
                    },
                    columnDefs: [{
                        // Add a new column with your icon
                        name: 'Expandable',
                        displayName: '',
                        enableSorting: false,
                        headerCellClass: 'header-cell',
                        cellClass: 'center-align',
                        enableCellEdit: false,
                        enableFiltering: false,
                        width: '35',
                        cellTemplate: "<div class=\"ui-grid-row-header-cell ui-grid-expandable-buttons-cell\" ng-click=\"grid.api.expandable.toggleRowExpansion(row.entity)\"><div class=\"ui-grid-cell-contents\"><i ng-class=\"{ 'ui-grid-icon-plus-squared' : !row.isExpanded, 'ui-grid-icon-minus-squared' : row.isExpanded }\"></i></div></div>"

                    }, {
                        displayName: 'First Responder',
                        field: 'eseguitoDa.nome',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.eseguitoDa.nome + ' ' + row.entity.eseguitoDa.cognome + ' (' +row.entity.eseguitoDa.email+ ')'}}\" tooltip-placement=\"right\" >{{row.entity.eseguitoDa.nome + ' ' + row.entity.eseguitoDa.cognome + ' (' +row.entity.eseguitoDa.email+ ')'}}</div>",
                    }, {
                        displayName: 'Data Accettazione/Rifiuto',
                        field: 'dataCreazione',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{grid.appScope.formatDate(row.entity.dataCreazione)}}\" tooltip-placement=\"left\" >{{grid.appScope.formatDate(row.entity.dataCreazione)}}</div>",
                        width: "220",
                        sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                            var toRet = 0;
                            if (rowA.entity.dataCreazione === rowB.entity.dataCreazione) {
                                return 0;
                            } else if (rowA.entity.dataCreazione < rowB.entity.dataCreazione) {
                                toRet = -1;
                            } else {
                                toRet = 1;
                            }
                            return toRet;
                        }
                    }, {
                        displayName: 'Accettato',
                        field: 'dataCreazione',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\"  >{{row.entity.dataAccettazione ? 'SI': 'NO'}}</div>",
                        width: "80",
                        sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                            var toRet = 0;
                            if (rowA.entity.dataAccettazione == null) {
                                return 1;
                            } else if (rowB.entity.dataAccettazione == null) {
                                return -1;
                            } else if (rowA.entity.dataAccettazione === rowB.entity.dataAccettazione) {
                                return 0;
                            } else if (rowA.entity.dataAccettazione < rowB.entity.dataAccettazione) {
                                toRet = -1;
                            } else {
                                toRet = 1;
                            }
                            return toRet;
                        }
                    }]
                };

            }
        ]);
    });