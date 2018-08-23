'use strict';

define(['angular', "moment", "constants"], function (angular, moment, constants) {
    angular.module("controller").controller('MailListCtrl', ["$scope", "$filter",
        "loggerService", "$rootScope", "alertService", "modalService",
        "$document", "validateService", "toastr", "mailService", "daeService", "$state", "frService",
        function ($scope, $filter,
            loggerService, $rootScope, alertService, modalService,
            $document, validateService, toastr, mailService, daeService, $state, frService) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Lista Mail",
                icon: "fa fa-envelope-o"
            });

            angular.extend($scope, {
                filter: {
                    dataDa: new Date(),
                    dataA: new Date(),
                },
                options: {
                    showWeeks: false
                }
            });

            $scope.format = function (timestamp) {
                return moment(timestamp).format('DD/MM/YYYY HH:mm');
            };
            /* */
            $scope.search = function (filter) {
                if (filter.dataDa) {
                    filter.dataDa.setHours(0, 0, 0);
                }

                if (filter.dataA) {
                    filter.dataA.setHours(23, 59, 59);
                }

                var promise = mailService.searchMailByFilter(filter);
                promise.then(function (data) {
                    $scope.gridOptions.data = data;
                });
            };

            $scope.pulisci = function () {
                $scope.filter = {
                    dataDa: new Date(),
                    dataA: new Date(),
                };
            };

            //serve per passare il dae alla modifica
            $scope.setInModifying = function (template) {
                if (template.entityType == "DAE") {
                    daeService.setCurrentDaeId(template.entityId);
                    $state.transitionTo('main.daeInsert');
                } else if (template.entityType == "USER") {
                    frService.setCurrentFr(template.entityId);
                    $state.transitionTo('main.frUpdate');
                }
            };

            $scope.gridOptions = {
                enableGridMenu: true,
                minRowToShow: 15,
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 1,
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                },
                columnDefs: [{
                    displayName: 'Destinatario',
                    field: 'destinatario',
                    width: "250"
                }, {
                    displayName: 'Data Invio',
                    field: 'dataInvio',
                    width: "150",
                    cellTemplate: "<div class=\"ui-grid-cell-contents\" >{{grid.appScope.format(grid.getCellValue(row, col))}}</div> ",
                }, {
                    displayName: 'Mail',
                    field: 'mailTemplate.descrizione',
                }, {
                    displayName: '',
                    name: 'Dae',
                    cellTemplate: "<div style=\"font-size:20px;\" ng-show=\"row.entity.entityType && row.entity.entityId\">" +
                        "<a ng-click=\"grid.appScope.setInModifying(row.entity)\"> " +
                        "<i class=\"fa fa-pencil-square\" aria-hidden=\"true\"></i></a></div>",
                    width: "30",
                    enableSorting: false,
                    enableFiltering: false
                }]
            };

            $rootScope.$watch('collapsed', function (old, newVal) {
                $scope.gridApi.core.handleWindowResize();
            });
        }
    ]);
});