'use strict';

define(['angular', "moment", "lodash", "constants", "leaflet"], function (angular, moment, _, constants, L) {
    angular.module("controller").controller('DaeSearchCtrl', ["$scope", "$filter", "daeService",
        "loggerService", "$rootScope", "alertService", "anagraficaService", "$uibModal",
        "$document", "modalService", "$state", 'uiGridConstants', "leafletBoundsHelpers", "validateService",
        "stateService", "utilityService", "csvService",
        function ($scope, $filter, daeService, loggerService, $rootScope, alertService,
            anagraficaService, $uibModal, $document, modalService, $state, uiGridConstants,
            leafletService, validateService, stateService, utilityService, csvService) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Cerca DAE",
                icon: "fa fa-heartbeat"
            });

            angular.extend($scope, {
                filter: {
                    operativo: null,
                    statoValidazioneBoolean: null,
                    isInFault: null
                },
                province: [],
                comuni: [],
                strutture: [],
                strade: [],
                manutenzioni: [],
                stati: [],
                dateFromPopupFrom: false,
                dateFromPopupTo: false,
                /* opzioni per i date piker*/
                options: {
                    minDate: new Date(0),
                    showWeeks: false
                },
                statiGuasto: ['APERTA', 'DA_VERIFICARE', 'VERIFICATA', 'ERRATA', 'AZIONI_INTRAPRESE', 'RISOLTA', 'CHIUSA'],
                scelte: [{
                    name: 'TUTTI',
                    value: null
                }, {
                    name: 'SI',
                    value: true
                }, {
                    name: 'NO',
                    value: false
                }]
            });

            /* Combo Tipo Manutenzione*/
            anagraficaService.getAllTipoManutenzione().then(function (response) {
                $scope.manutenzioni = response;
            });

            /* Combo Stato*/
            anagraficaService.getAllStato().then(function (response) {
                $scope.stati = response;
            });

            /* Combo Province*/
            anagraficaService.getAllProvince().then(function (result) {
                $scope.province = result;
            });

            $scope.formatDate = function (date) {
                if (date) {
                    return moment(date).format(constants.exelDate);
                }
                return "";
            };
            //serve per passare il dae alla modifica
            $scope.setInModifying = function (id) {
                daeService.setCurrentDaeId(id);
                $state.transitionTo('main.daeInsert');
            };
            //serve per passare il dae alla gestione guasti
            $scope.setInWarning = function (id) {
                daeService.setCurrentDaeId(id);
                $state.transitionTo('main.daeFault');
            };

            /*Combo Comuni */
            $scope.refreshComune = function (input) {
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
            };
            /* Combo Tipo Struttura*/
            $scope.refreshStrutture = function (search) {
                var filter = {};
                if (search !== undefined || search !== null) {
                    filter.name = search;
                }
                return anagraficaService.getAllTipoStruttura(filter).then(function (response) {
                    $scope.strutture = response;
                });
            };

            /*Combo INDIRIZZO*/
            $scope.refreshStrade = function (search) {
                if (search === undefined || search === null || !search || search.length < 3) {
                    $scope.strade = [];
                    return [];
                }

                var filter = {
                    "name": search
                };
                if ($scope.filter.comune) {
                    if (_.isString($scope.filter.comune)) {
                        filter.nomeComune = $scope.filter.comune;
                    } else {
                        filter.nomeComune = $scope.filter.comune.nomeComune;
                    }
                }
                return anagraficaService.searchStradeByFilter(filter).then(function (response) {
                    $scope.strade = response;
                });
            };

            function getDayClass(data) {
                var date = data.date,
                    mode = data.mode;
                if (mode === 'day') {
                    var dayToCheck = new Date(date).setHours(0, 0, 0, 0);
                    for (var i = 0; i < $scope.events.length; i++) {
                        var currentDay = new Date($scope.events[i].date)
                            .setHours(0, 0, 0, 0);
                        if (dayToCheck === currentDay) {
                            return $scope.events[i].status;
                        }
                    }
                }
                return '';
            }
            /* */
            $scope.search = function (filter) {
                if (filter.statoValidazioneBoolean === null) {
                    filter.statoValidazione = null;
                } else if (filter.statoValidazioneBoolean) {
                    filter.statoValidazione = "VALIDATO";
                } else {
                    filter.statoValidazione = "DA_VALIDARE";
                }

                if (_.isDate(filter.scadenzaManutenzioneDa)) {
                    filter.scadenzaManutenzioneDa.setHours(0, 0, 0, 0);
                }
                if (_.isDate(filter.scadenzaManutenzioneA)) {
                    filter.scadenzaManutenzioneA.setHours(23, 59, 59, 999);
                }

                if (filter.comune && !_.isString(filter.comune)) {
                    filter.comune = filter.comune.nomeComune;
                }
                if (filter.indirizzo && !_.isString(filter.indirizzo)) {
                    filter.indirizzo = filter.indirizzo.name;
                }
                if (filter.tipoManutenzioneObj) {
                    filter.tipoManutenzione = filter.tipoManutenzioneObj.id;
                } else {
                    filter.tipoManutenzione = undefined;
                }

                stateService.saveState("daeSearchController.filter", filter);

                var promise = daeService.searchDAEByFilter(filter);
                promise.then(function (data) {
                    //controllo se tra i dae ci sta qualcuno guasto
                    data.forEach(function (dae) {
                        if (dae.guasti) {
                            dae.guasti.forEach(function (guasto) {
                                if (guasto.statoAttuale !== 'CHIUSA' && guasto.statoAttuale !== 'RISOLTA') {
                                    //se il dae ha un guasto segnalato
                                    dae.guasto = true;
                                }
                            });
                        }
                        if (dae.immagine.url) {
                            //serve per evitare il cache dell'immagine da parte del browser
                            dae.immagine.url = dae.immagine.url + '?t=' + Date.now();
                        }
                    });

                    $scope.gridOptions.data = data;
                    $scope.gridOptions.virtualizationThreshold = data.length;
                });
            };

            $scope.pulisci = function () {
                $scope.filter = {
                    operativo: null,
                    statoValidazioneBoolean: null,
                    isInFault: null
                };
                $scope.layer = null;

                $scope.gridOptions.data = [];

                stateService.removeState("daeSearchController.filter");
            };

            $scope.exportCSV = function () {

                csvService.exportExcel("daeList", $scope.gridOptions.data, [
                        "Modello", "Matricola", "Tipologia Struttura", "Nome Sede",
                        "Note Accesso Sede", "Ubicazione",

                        "Provincia", "Comune", "Località", "Indirizzo", "Civico",
                        "Latitudine", "Longitudine",

                        "E-mail Responsabile", "Nome responsabile", "Cognome responsabile",
                        "Telefono responsabile", "CF responsabile",



                        "Stato", "Operativo", "Validato",
                        "Guasto", "Data inserimento", "Data ultima validazione",
                        "Utente ultima validazione", "Disponibilità",
                        "Programma Manutenzione",
                    ],
                    function (row, separator) {
                        var result = [
                            csvService.notNull(row.modello),
                            csvService.notNull(row.matricola),
                            csvService.notNull(row.tipologiaStruttura.descrizione),
                            csvService.notNull(row.nomeSede),
                            csvService.notNull(row.notediAccessoallaSede),
                            csvService.notNull(row.ubicazione),

                            csvService.notNull(row.gpsLocation.comune.provincia.nomeProvincia),
                            csvService.notNull(row.gpsLocation.comune.nomeComune),
                            csvService.notNull(row.gpsLocation.localita ? row.gpsLocation.localita.name : null),
                            csvService.notNull(row.gpsLocation.indirizzo.name),
                            csvService.notNull(row.gpsLocation.civico),

                            csvService.notNull(row.gpsLocation.latitudine),
                            csvService.notNull(row.gpsLocation.longitudine),

                            csvService.notNull(row.responsabile.email),
                            csvService.notNull(row.responsabile.nome),
                            csvService.notNull(row.responsabile.cognome),
                            csvService.notNull(row.responsabile.telefono),
                            csvService.notNull(row.responsabile.codiceFiscale),

                            csvService.notNull(row.currentStato != null ? row.currentStato.nome : null),
                            row.operativo ? "SI" : "NO",
                            csvService.notNull(row.statoValidazione),

                            row.guasto ? "SI" : "NO",

                            csvService.notNull($scope.formatDate(row.dataInserimento)),
                            csvService.notNull($scope.formatDate(row.dataValidazione)),
                            csvService.notNull(row.utenteValidazione),
                            csvService.notNull(row.tipologiaDisponibilita),

                            row.programmiManutenzione &&
                            row.programmiManutenzione.length > 0 ? "SI" : "NO",

                        ];
                        return result;
                    });

            };

            var indirizzoTemplate = "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.gpsLocation.indirizzo.name}} {{row.entity.gpsLocation.civico}}\" tooltip-placement=\"left\" >{{row.entity.gpsLocation.indirizzo.name}} {{row.entity.gpsLocation.civico}}</div>";

            var modelloTemnplate = "<div class='media' uib-tooltip=\"{{row.entity.modello}}\" tooltip-placement=\"right\"  >" +
                "<div class=\"media-left\"  >" +
                "<img class=\"media-object img-circle img-avatar\" src='{{row.entity.immagine.url}}&size=SMALL'  ng-show=\"row.entity.immagine\" alt=\"...\" />" +
                "</div>" +
                "<div class=\"media-body\">" +
                "<span class=\"media-title\">{{row.entity.modello}}</span>" +
                "</div>" +
                "</div>";

            $scope.gridOptions = {
                enableGridMenu: true,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 1,
                //abilitazione all'expand
                expandableRowTemplate: 'expandableRowTemplate.html',
                expandableRowHeight: 45,
                //subGridVariable will be available in subGrid scope
                enableExpandableRowHeader: false,
                enablePaginationControls: false,
                paginationPageSize: 8,
                rowHeight: "50",
                virtualizationThreshold: 50,
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;

                    var resizeHandler = $rootScope.$watch('collapsed', function (old, newVal) {
                        $scope.gridApi.core.handleWindowResize();
                    });
                    $scope.$on("$destroy", function (event) {
                        //distruggo l'handler
                        resizeHandler();
                    });

                    gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                        if (row.isExpanded) {
                            if (row.entity.programmiManutenzione) {
                                //ordino i programmi manutenzione
                                row.entity.programmiManutenzione = _.sortBy(row.entity.programmiManutenzione, [function (p) {
                                    var time = 0;
                                    if (_.isDate(p.scadenzaDopo)) {
                                        time = p.scadenzaDopo.getTime();
                                    }
                                    if (_.isNumber(p.scadenzaDopo)) {
                                        time = p.scadenzaDopo;
                                    }
                                    return -1 * time;
                                }]);
                            }
                        }
                    });
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
                    displayName: 'Modello',
                    field: 'modello',
                    cellTemplate: modelloTemnplate,

                }, {
                    displayName: 'Tipologia Struttura',
                    field: 'tipologiaStruttura.descrizione',

                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.tipologiaStruttura.descrizione}}\" tooltip-placement=\"left\" >{{row.entity.tipologiaStruttura.descrizione}}</div>",
                }, {
                    displayName: 'Nome Sede',
                    field: 'nomeSede',

                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.nomeSede}}\" tooltip-placement=\"left\" >{{row.entity.nomeSede}}</div>",

                }, {
                    displayName: 'Comune',
                    field: 'gpsLocation.comune.nomeComune',

                    cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.gpsLocation.comune.nomeComune}}\" tooltip-placement=\"left\" >{{row.entity.gpsLocation.comune.nomeComune}}</div>",
                    sort: {
                        direction: uiGridConstants.ASC,
                        priority: 1
                    }
                }, {
                    displayName: 'Indirizzo',
                    name: 'Indirizzo',
                    cellTemplate: indirizzoTemplate,
                    sort: {
                        direction: uiGridConstants.ASC,
                        priority: 2
                    },
                    sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                        var toRet = 1;
                        if (rowA.entity.gpsLocation.indirizzo) {
                            if (rowB.entity.gpsLocation.indirizzo) {
                                toRet = rowA.entity.gpsLocation.indirizzo.name.localeCompare(rowB.entity.gpsLocation.indirizzo.name);
                            } else {
                                toRet = -1;
                            }
                        }
                        if (toRet == 0 && rowA.entity.gpsLocation.civico && rowB.entity.gpsLocation.civico) {
                            toRet = Number(rowA.entity.gpsLocation.civico) - Number(rowB.entity.gpsLocation.civico);
                        }
                        return toRet;
                    }
                }, {
                    displayName: '',
                    name: 'Modifica',
                    cellTemplate: "<div class=\"grid-icon\"  >" +
                        "<a ng-click=\"grid.appScope.openMapModal(row.entity)\"  uib-tooltip=\"Visualizza il DAE sulla mappa\" tooltip-placement=\"left\" ng-show=\"row.entity.gpsLocation && row.entity.gpsLocation.latitudine && row.entity.gpsLocation.longitudine\"> " +
                        "<i class=\"fa fa-map-o\" aria-hidden=\"true\"></i></a> " +
                        "<a ng-click=\"grid.appScope.openImageModal(row.entity.immagine,'Foto')\" ng-show=\"row.entity.immagine\"  uib-tooltip=\"Visualizza l'immagine\" tooltip-placement=\"left\"> " +
                        "<i class=\"fa fa-picture-o\" aria-hidden=\"true\"></i></a> " +
                        "<a ng-hide=\"row.entity.readOnly\"  ng-click=\"grid.appScope.setInModifying(row.entity.id)\" uib-tooltip=\"Modifica\" tooltip-placement=\"left\"> " +
                        "<i class=\"fa fa-pencil-square\" aria-hidden=\"true\"></i></a>" +
                        "<a  ng-show=\"row.entity.guasto\"  ng-click=\"grid.appScope.setInWarning(row.entity.id)\" uib-tooltip=\"Segnalazione Problemi\" tooltip-placement=\"left\"> " +
                        "  <i style=\"color:red;\"  class=\"fa fa-exclamation-circle\" aria-hidden=\"true\"></i> </a>" +
                        "<a ng-show=\"!row.entity.guasto\"  ng-click=\"grid.appScope.setInWarning(row.entity.id)\" uib-tooltip=\"Segnalazione Problemi\" tooltip-placement=\"left\"> " +
                        "  <i class=\"fa  fa-exclamation-triangle\" aria-hidden=\"true\"></i> </a>" +
                        "</div>",
                    width: "150",
                    enableSorting: false,
                    enableFiltering: false
                }]
            };

            $scope.openImageModal = function (immagine, title) {
                var parentElem = angular.element($document[0].querySelector('.modal-component'));
                var modalInstance = $uibModal.open({
                    animation: true,
                    //    template: modalTemplateImage,
                    templateUrl: "immagineDaeModal.html",
                    appendTo: parentElem,
                    controller: function ($scope, $uibModalInstance) {
                        var $ctrl = this;
                        $scope.title = title;
                        $scope.immagine = immagine.url + "&size=LARGE";
                    }
                });
            };

            $scope.openMapModal = function (dae) {
                var points = [];
                var mark = [];

                mark = getmarkers(dae, points);

                var bounds = null;

                var editableLayers = new L.FeatureGroup();

                if ($scope.layer) {
                    editableLayers.addLayer($scope.layer);
                    bounds = $scope.layer.getBounds();
                    bounds = leafletService.createBoundsFromArray([
                        [bounds.getNorth(), bounds.getEast()],
                        [bounds.getSouth(), bounds.getWest()]
                    ]);
                } else if (!_.isEmpty(points)) {
                    var b = L.bounds(points);
                    bounds = leafletService.createBoundsFromArray([
                        [b.min.y, b.min.x],
                        [b.max.y, b.max.x]
                    ]);
                }
                $scope.modal = modalService.openMapModal({
                    enableEdit: true,
                    title: "DAE",
                    markers: mark,
                    bounds: bounds,
                    controls: getControls(editableLayers),
                    editLayer: editableLayers,
                    events: [L.Draw.Event.CREATED, L.Draw.Event.EDITED],
                });

                $scope.modal.result.then(function (obj) {
                    //distruggo l'handler
                    if (obj) {
                        startmapSearch(obj.layer, obj.layers);
                    }
                });
            };

            function startmapSearch(layer, layers) {

                if (!layer && layers) {
                    layers.getLayers().forEach(function (l) {
                        layer = l;
                    });
                }
                if (layer) {
                    $scope.filter.location = {
                        srid: 4326,
                        geoJSON: JSON.stringify(layer.toGeoJSON().geometry)
                    };
                    $scope.filter.searchArea = true;
                } else {
                    $scope.filter.location = null;
                    $scope.filter.searchArea = false;
                }
                $scope.layer = layer;
                $scope.search($scope.filter);
            }

            $scope.reimpostaRicercaMappa = function () {
                if ($scope.filter.searchArea) {
                    $scope.filter.location = $scope.locationApp;
                    $scope.locationApp = null;
                    //visualizzo la mappa
                    $scope.openMapModal();
                } else {
                    $scope.locationApp = $scope.filter.location;
                    $scope.filter.location = null;
                }
            };

            function getControls(editableLayers) {
                var val = [
                    new L.Control.Draw({
                        draw: {
                            polyline: false,
                            marker: false,
                            circle: false
                        },
                        /*    edit: {
                                featureGroup: editableLayers, //REQUIRED!!
                                remove: false
                            }*/
                    })
                ];
                return val;
            }

            function getmarkers(dae, points) {
                var markers = [];
                if (dae && dae.gpsLocation) {
                    var m = createmarker(dae);
                    m.focus = true;
                    markers.push(m);
                } else {
                    if ($scope.gridOptions.data) {
                        $scope.gridOptions.data.forEach(function (el) {
                            if (validateService.validateLocation(el.gpsLocation)) {
                                points.push([el.gpsLocation.longitudine, el.gpsLocation.latitudine]);
                                markers.push(createmarker(el));
                            }
                        });
                        //    markers[0].focus = true;
                    }
                }
                return markers;
            }

            function createmarker(dae) {
                if (!dae.gpsLocation) {
                    return undefined;
                }
                var msg = utilityService.daeTooltip(dae);

                return {
                    lat: dae.gpsLocation.latitudine,
                    lng: dae.gpsLocation.longitudine,
                    draggable: false,
                    message: msg,
                    icon: constants.daeMapIcon
                };
            }

            var stFilter = stateService.getState("daeSearchController.filter");
            if (stFilter) {
                if (stFilter.scadenzaManutenzioneDa) {
                    stFilter.scadenzaManutenzioneDa = new Date(stFilter.scadenzaManutenzioneDa);
                }
                if (stFilter.scadenzaManutenzioneA) {
                    stFilter.scadenzaManutenzioneA = new Date(stFilter.scadenzaManutenzioneA);
                }

                $scope.search(angular.copy(stFilter));

                //se nello stato è presente un filtro
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
        }
    ]);
});