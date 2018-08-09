'use strict';

define(['angular', "moment", "lodash", "constants"], function(angular, moment, _, constants) {
    angular.module("controller").controller('NotificationSearchCtrl', ["$scope",
        "loggerService", "$rootScope", "anagraficaService", "$state", 'notificationService',
        function($scope, loggerService, $rootScope,
            anagraficaService, $state, notificationService) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Cerca Notifiche",
                icon: "fa fa-paper-plane-o"
            });

            angular.extend($scope, {
                filter: {},
                options: {
                    showWeeks: false
                }
            });

            /* */
            $scope.search = function(filter) {

                if (filter.da) {
                    filter.da.setHours(0, 0, 0);
                }
                if (filter.a) {
                    filter.a.setHours(23, 59, 59);
                }

                var promise = notificationService.searchNotificationByFilter(filter);
                promise.then(function(data) {
                    $scope.gridOptions.data = data;
                });
            };

            $scope.format = function(timestamp) {
                return moment(timestamp).format('DD/MM/YYYY HH:mm');
            };

            $scope.pulisci = function() {
                $scope.filter = {};
            };
            $scope.delete = function(id) {
                var promise = notificationService.deleteNotification(id);
                promise.then(function(data) {
                    //ricarico la lista
                    $scope.search($scope.filter);
                });
            };

            $scope.gridOptions = {
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 1,
                enableGridMenu: true,
                expandableRowTemplate: 'partials/notification/responderGrid.html',
                expandableRowHeight: 150,
                enableExpandableRowHeader: false,
                onRegisterApi: function(gridApi) {
                    $scope.gridApi = gridApi;
                },
                virtualizationThreshold: 50,
                enablePaginationControls: false,
                paginationPageSizes: [8, 10, 20, 50],
                paginationPageSize: 15,
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

                    },
                    {
                        displayName: 'Testo',
                        field: 'testo'
                    }, {
                        displayName: 'Data Invio',
                        field: 'invio',
                        width: "150",
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" >{{grid.appScope.format(grid.getCellValue(row, col))}}</div> ",
                    }, {
                        displayName: 'Destinatari',
                        field: 'destinatari',
                        width: "40"
                    }, {
                        displayName: '',
                        name: 'Elimina',
                        cellTemplate: "<div style=\"font-size:20px;\"  mwl-confirm " +
                            "title=\"Attenzione\" " +
                            "message=\"Sei sicuro?\" " +
                            "confirm-text=\"Si\" " +
                            "cancel-text=\"No\" " +
                            "placement=\"left\" " +
                            "on-confirm=\"grid.appScope.delete(row.entity.id)\" " +
                            "on-cancel=\"cancelClicked = true\" " +
                            "confirm-button-type=\"danger\" " +
                            "cancel-button-type=\"primary\" > " +
                            "<i class=\"fa fa-trash-o\" aria-hidden=\"true\" ></i></div> ",
                        width: "30",
                        enableSorting: false,
                        enableFiltering: false
                    }
                ]
            };

            $rootScope.$watch('collapsed', function(old, newVal) {
                $scope.gridApi.core.handleWindowResize();
            });

            $scope.gridOptions.onRegisterApi = function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.expandable.on.rowExpandedStateChanged($scope, function(row) {
                    if (row.isExpanded) {
                        row.entity.subGridOptions = {
                            columnDefs: [{
                                displayName: 'Nominativo',
                                name: 'Nominativo',
                                width: "*",
                                cellTemplate: "<div class='ui-grid-cell-contents'>{{row.entity.nome}} {{row.entity.cognome}}</div>"
                            }, {
                                displayName: 'email',
                                field: 'email',
                                width: "20%"
                            }]
                        };

                        notificationService.getNotificationFR(row.entity.id).then(function(data) {
                            row.entity.subGridOptions.data = data;
                        });
                    }
                });
            };

            $scope.expandAllRows = function() {
                $scope.gridApi.expandable.expandAllRows();
            };

            $scope.collapseAllRows = function() {
                $scope.gridApi.expandable.collapseAllRows();
            };
        }
    ]);
});
