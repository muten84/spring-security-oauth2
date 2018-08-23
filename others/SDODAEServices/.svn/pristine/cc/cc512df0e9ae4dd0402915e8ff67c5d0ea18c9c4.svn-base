'use strict';

define(
    ['angular', 'moment', 'lodash', 'constants'],
    function (angular, moment, _, constants) {
        angular.module('controller').controller('UserSearchCtrl', [
            '$scope', 'loggerService', '$rootScope',
            'anagraficaService', '$state', 'notificationService',
            'uiGridConstants', 'stateService', 'userService',
            "csvService",
            function ($scope, loggerService, $rootScope,
                anagraficaService, $state, notificationService,
                uiGridConstants, stateService, userService,
                csvService) {
                $rootScope.$broadcast('main.changeTitle', {
                    title: 'Cerca Utenti',
                    icon: 'fa fa-search'
                });

                angular.extend($scope, {
                    filter: {},
                    ruoli: [],
                    gruppi: [],
                    pulisci: function () {
                        $scope.gridOptions.data = [];
                        $scope.filter = {};

                        stateService.removeState("userSearchController.filter");
                    },
                    search: function (filter) {
                        if (_.isUndefined(filter)) {
                            return;
                        }
                        // Mauro : aggiunto deleted
                        filter.deleted = false;

                        if (filter.ruolo) {
                            filter.ruoloNome = filter.ruolo.nome;
                        }
                        if (filter.gruppo) {
                            filter.gruppoNome = filter.gruppo.nome;
                        }

                        stateService.saveState('userSearchController.filter', filter);
                        userService.searchUtenteByFilter(filter).then(function (data) {
                            $scope.gridOptions.data = data;
                            // usato virtualizationThreshold per bug ui-grid https://github.com/angular-ui/ui-grid/issues/860
                            $scope.gridOptions.virtualizationThreshold = data.length;
                        });
                    },
                    setInModifying: function (id) {
                        userService.setCurrentUserId(id);
                        $state.transitionTo('main.userInsert');
                    },
                    exportCSV: function () {

                        csvService.exportExcel("userList", $scope.gridOptions.data, ["User", "Nome", "Cognome", "Email", "Ruolo"],
                            function (row, separator) {
                                var result = [
                                    csvService.notNull(row.logon),
                                    csvService.notNull(row.nome),
                                    csvService.notNull(row.cognome),
                                    csvService.notNull(row.email),
                                    csvService.notNull(row.ruoli[0].descrizione),
                                ];
                                return result;
                            });
                    }

                });

                /* Combo Ruoli*/
                userService.getAllRuoli().then(function (response) {
                    $scope.ruoli = response;
                });

                /* Combo Gruppi*/
                userService.getAllGruppi().then(function (response) {
                    $scope.gruppi = response;
                });

                var stFilter = stateService.getState('userSearchController.filter');
                if (stFilter) {
                    $scope.filter = stFilter;
                    $scope.search(stFilter);
                }

                var profiloTemnplate = "<div class='media'  uib-tooltip=\"{{ row.entity.logon }}\" tooltip-placement=\"right\">" +
                    '<div class="media-left"  popover-trigger="\'click outsideClick\'" popover-placement="right-bottom" popover-title="Dettaglio"  uib-popover-template="\'profileUserPopOver.html\'"  >' +
                    '<img class="media-object img-circle img-avatar" src=\'{{row.entity.immagine.url}}?size=SMALL\' alt="..." />' +
                    '</div>' +
                    '<div class="media-body">' +
                    '<span class="media-title" style="word-break: break-all;">{{row.entity.logon}}</span>' +
                    '</div>' +
                    '</div>';

                $scope.gridOptions = {
                    onRegisterApi: function (gridApi) {
                        $scope.gridApi2 = gridApi;
                    },
                    enableHorizontalScrollbar: 0,
                    enableVerticalScrollbar: 1,
                    enableGridMenu: true,
                    rowHeight: '50',
                    virtualizationThreshold: 50,
                    enablePaginationControls: false,
                    paginationPageSizes: [8, 10, 20, 50],
                    paginationPageSize: 8,
                    columnDefs: [{
                        displayName: 'Utente',
                        cellTemplate: profiloTemnplate,
                        width: '160',
                        name: 'Profilo',
                        sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                            return rowA.entity.email.localeCompare(rowB.entity.email);
                        }
                    }, {
                        displayName: 'Nominativo',
                        name: 'Nominativo',
                        cellTemplate: "<div class='ui-grid-cell-contents' uib-tooltip=\"{{row.entity.cognome}} {{row.entity.nome}}\" tooltip-placement=\"right\" >{{row.entity.cognome}} {{row.entity.nome}} </div>",
                        sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                            var toRet = rowA.entity.cognome.localeCompare(rowB.entity.cognome);
                            if (toRet == 0) {
                                return rowA.entity.nome.localeCompare(rowB.entity.nome);
                            }
                            return toRet;
                        },
                        sort: {
                            direction: uiGridConstants.ASC,
                            priority: 2
                        }
                    }, {
                        displayName: 'email',
                        field: 'email',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.email}}\" tooltip-placement=\"left\" >{{row.entity.email}}</div>",
                    }, {
                        displayName: 'Profilo',
                        name: 'profilo',
                        /*cellTemplate: indirizzoTemplate*/
                        field: 'ruoli[0].descrizione',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.ruoli[0].descrizione}}\" tooltip-placement=\"left\" >{{row.entity.ruoli[0].descrizione}}</div>",
                        sort: {
                            direction: uiGridConstants.ASC,
                            priority: 1
                        }
                    }, {
                        displayName: '',
                        name: 'Modifica',
                        cellTemplate: '<div class="grid-icon">' +
                            '<a ng-show="row.entity.certificatoFr.immagine" uib-tooltip="Visualizza l\'immagine" tooltip-placement="left" ' +
                            'ng-click="grid.appScope.openModal(\'certificatoModal.html\', {immagine: row.entity.certificatoFr.immagine},\'Certificato\')"> ' +
                            '<i class="fa fa-certificate" aria-hidden="true"></i></a> ' +
                            '<a ng-hide=\"row.entity.readOnly\" ng-click="grid.appScope.setInModifying(row.entity.id)" uib-tooltip="Modifica" tooltip-placement="left"> ' +
                            '<i class="fa fa-pencil-square" aria-hidden="true"></i></a></div>',
                        width: '40',
                        enableSorting: false,
                        enableFiltering: false
                    }]
                };

                $rootScope.$watch('collapsed', function (old, newVal) {
                    $scope.gridApi2.core.handleWindowResize();
                });
            }
        ]);
    }
);