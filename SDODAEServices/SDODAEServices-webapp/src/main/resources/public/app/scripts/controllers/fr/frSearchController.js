'use strict';
define(['angular', "moment", "lodash", "constants"], function (angular, moment, _, constants) {
    angular.module("controller").controller('FrSearchCtrl', [
        "$scope", "$document", "$uibModal",
        "$filter", "frService", "loggerService",
        "$rootScope", "alertService", "anagraficaService",
        "$state", "notificationService", "modalService",
        "uiGridConstants", "stateService", "csvService",
        function ($scope, $document, $uibModal,
            $filter, frService, loggerService,
            $rootScope, alertService, anagraficaService,
            $state, notificationService, modalService,
            uiGridConstants, stateService, csvService) {
            angular.extend($scope, {
                province: [],
                comuni: [],
                categorie: [],
            });

            var profileStatus = [{
                    id: "ATTIVO",
                    descrizione: "ABILITATO"
                },
                {
                    id: "IN_ATTESA_DI_ATTIVAZIONE",
                    descrizione: "IN ATTESA",
                },
                {
                    id: null,
                    descrizione: "TUTTI"
                }
            ];
            /*angular.element().find("#slideit").bxSlider
                 ({
                     captions: true,
                     auto: true,
                     autoControls: true,
                     slideWidth: 110,
                     minSlides: 1,
                     maxSlides: 6,
                     moveSlides: 1,
                     slideMargin: 10,
                     pager: true,
                     autoHover: true
                 });
 */

            /* Combo Province*/
            anagraficaService.getAllProvince().then(function (result) {
                $scope.province = result;
            });

            $rootScope.$broadcast("main.changeTitle", {
                title: "Cerca DAE Responder",
                icon: "fa fa-search"
            });
            $scope.filter = {
                categoria: {
                    id: "FIRST_RESPONDER",
                    descrizione: "First Responder"
                },
                profileStat: {
                    id: "TUTTI",
                    descrizione: "TUTTI"
                }
            };
            $scope.profileStat = {
                id: "TUTTI",
                descrizione: "TUTTI"
            };

            $scope.formatDate = function (date) {
                if (date) {
                    return moment(date).format(constants.exelDate);
                }
                return "";
            };
            /*Combo Comuni */

            $scope.refreshCategorie = function (input) {
                if (_.isUndefined(input)) {
                    return [];
                }
                var categoryFilter = {};
                return anagraficaService.searchCategorie(categoryFilter).then(function (response) {
                    $scope.categorie = response;
                });
            };
            $scope.refreshProfileStatus = function (input) {
                if (_.isUndefined(input)) {
                    $scope.statoProfili = profileStatus;
                    return [];
                }
                var response = _.filter(profileStatus, function (o) {
                    return o.descrizione.indexOf(input) >= 0;
                });
                $scope.statoProfili = response;
            };
            /*  $scope.selectCategoria = function(categoria){
                  $scope.filter.categoria = categoria;
              }*/
            $scope.refreshComune = function (input) {
                if (input === undefined || input === null || !input || input.length < 3) {
                    $scope.comuni = [];
                    return [];
                }
                var munfilter = {
                    "nomeComune": input
                };
                return anagraficaService.searchComuniByFilter(munfilter).then(function (response) {
                    $scope.comuni = response;
                });
            };
            /* GESTIONE date piker */
            $scope.dateFromPopup = {
                opened: false
            };
            $scope.openDateFrom = function () {
                $scope.dateFromPopup.opened = true;
            };
            $scope.today = function () {
                $scope.filter.scadenzaDae = new Date();
            };
            $scope.clear = function () {
                $scope.filter.scadenzaDae = null;
            };
            $scope.options = {
                /*  customClass: getDayClass,*/
                minDate: new Date(),
                showWeeks: false
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
                if (_.isUndefined(filter)) {
                    return;
                }
                if (!_.isUndefined(filter.categoria)) {
                    filter.categoriaFrIDS = [filter.categoria.id];
                } else {
                    filter.categoriaFrIDS = [];
                }
                if (filter.comune && !_.isString(filter.comune)) {
                    filter.comune = filter.comune.nomeComune;
                }
                if (!_.isUndefined(filter.profileStat)) {
                    filter.statoProfilo = filter.profileStat.id;
                    if (filter.statoProfilo === "TUTTI") {
                        filter.statoProfilo = undefined;
                    }
                } else {
                    filter.statoProfilo = undefined;
                }
                //Mauro : aggiunto deleted
                filter.deleted = false;
                stateService.saveState("frSearchController.filter", filter);

                frService.searchFirstResponderByFilter(filter).
                then(function (data) {
                    $scope.testData = data;
                    $scope.gridOptions.data = data;
                    //usato virtualizationThreshold per bug ui-grid https://github.com/angular-ui/ui-grid/issues/860
                    $scope.gridOptions.virtualizationThreshold = data.length;
                    /*$scope.gridOptions.totalItems = data.length;*/
                });
            };
            $scope.openModal = function (htmlTemplate, data, title) {
                var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: htmlTemplate,
                    appendTo: parentElem,
                    controller: function ($scope, $uibModalInstance) {
                        var $ctrl = this;
                        $scope.title = title;
                        if (data.immagine) {
                            $scope.immagine = data.immagine.url + '?size=LARGE';

                            $scope.open = function (url) {
                                window.open(url);
                            };

                        } else if (data.questionario) {
                            $scope.questions = data.questionario;
                        }
                    }
                });
            };

            $scope.exportCSV = function () {

                csvService.exportExcel("frList", $scope.gridOptions.data, [
                        "Nome", "Cognome", "Email", "Categoria", "Stato",
                        "Provincia",
                        "Comune",
                        "Professione",

                        "Codice fiscale",
                        "Numero cellulare",
                        "Indirizzo",
                        "Profilo",

                        "Ente certificatore",
                        "Data conseguimento",
                        "Data registrazione",
                        "Data validazione",
                        "Data accettazione privacy",
                        "Utente approvazione"
                    ],
                    function (row, separator) {
                        var result = [
                            csvService.notNull(row.nome),
                            csvService.notNull(row.cognome),
                            csvService.notNull(row.email),
                            csvService.notNull(row.categoriaFr.descrizione),
                            csvService.notNull(row.statoProfilo),

                            csvService.notNull(row.comuneResidenza != null ? row.comuneResidenza.provincia.nomeProvincia : null),
                            csvService.notNull(row.comuneResidenza != null ? row.comuneResidenza.nomeComune : null),
                            csvService.notNull(row.professione != null ? row.professione.descrizione : null),

                            csvService.notNull(row.codiceFiscale),
                            csvService.notNull(row.numCellulare),
                            csvService.notNull(row.indirizzo != null ? row.indirizzo.name : null),
                            csvService.notNull(row.categoriaFr.descrizione),

                            csvService.notNull(row.certificatoFr != null ? row.certificatoFr.rilasciatoDa : null),
                            csvService.notNull(row.certificatoFr != null ? $scope.formatDate(row.certificatoFr.dataConseguimento) : null),
                            csvService.notNull($scope.formatDate(row.dataIscrizione)),
                            csvService.notNull($scope.formatDate(row.dataValidazione)),
                            csvService.notNull($scope.formatDate(row.privacyAcceptedDate)),
                            csvService.notNull(row.utenteValidazione),
                        ];
                        return result;
                    });

            };

            $scope.setInModifying = function (id) {
                frService.setCurrentFr(id);
                $state.transitionTo('main.frUpdate');
            };
            $scope.openSendMessage = function (frId) {
                modalService.openNotifyModal("Invia Notifiche", frId, notificationService);
            };
            /*GESTIONE UI GRID DI RICERCA*/
            var indirizzoTemplate = "<div>{{row.entity.gpsLocation.indirizzo.name}} {{row.entity.gpsLocation.civico}}</div>";
            var popOverProfilo = "<span class='media-heading'><b>{{row.entity.logon}}</b>, <i></i>";
            /*            var test =
                            "<span class='media-heading'>" +
                            "<a href='donatori-dettaglio.html' title='Napoli Giacomo' tabindex=''>Napoli Giacomo</a></span>";*/
            var profiloTemnplate = "<div class='media' uib-tooltip=\"{{row.entity.logon}}\" tooltip-placement=\"right\">" +
                "<div class=\"media-left\"  popover-trigger=\"'click outsideClick'\" popover-placement=\"right-bottom\" popover-title=\"Dettaglio\"  uib-popover-template=\"'profilePopOver.html'\" >" +
                "<img class=\"media-object img-circle img-avatar\" src='{{row.entity.immagine.url}}?size=SMALL' alt=\"...\" />" +
                "</div>" +
                "<div class=\"media-body\">" +
                "<span class=\"media-title\" style=\"word-break: break-all;\">{{row.entity.logon}}</span>" +
                /*"<span ng-click=\"grid.appScope.openModal('questionarioModal.html', {questionario: row.entity.questionarioFirstResponder},'Questionario')\" class=\"media-content\">Questionario</span>" +*/
                "</div>" +
                "</div>";
            var statoTemplate = "<div class=\"grid-icon\" uib-tooltip=\"{{row.entity.statoProfilo}}\" tooltip-placement=\"right\"><i ng-show=\"row.entity.statoProfilo === 'ATTIVO'\" class=\"fa fa-check-circle-o\"></i>" +
                "<i ng-show=\"row.entity.statoProfilo != 'ATTIVO'\" class=\"fa fa-exclamation\"></i></div>";
            $scope.gridOptions = {
                enableHorizontalScrollbar: 0,
                enableVerticalScrollbar: 1,
                onRegisterApi: function (gridApi) {
                    $scope.gridApi2 = gridApi;
                },
                rowHeight: "50",
                virtualizationThreshold: 50,
                enablePaginationControls: false,
                paginationPageSizes: [8, 10, 20, 50],
                paginationPageSize: 8,
                columnDefs: [
                    /*{
                        displayName: 'Alias',
                        field: 'logon',
                        width: "*"
                    },*/
                    {
                        displayName: 'Profilo',
                        cellTemplate: profiloTemnplate,
                        width: "160",
                        name: 'Profilo',
                        sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                            return rowA.entity.email.localeCompare(rowB.entity.email);
                        },
                        sort: {
                            direction: uiGridConstants.ASC,
                            priority: 2
                        }
                    }, {
                        displayName: 'S',
                        name: 'S',
                        field: 'statoProfilo',
                        width: "30",
                        enableSorting: false,
                        enableFiltering: false,
                        /* cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                             if (grid.getCellValue(row, col) == "Abilitato") {
                                 return 'test';
                             }
                         },*/
                        cellTemplate: statoTemplate
                        /*cellFilter: "userState"*/
                    }, {
                        displayName: 'Nominativo',
                        name: 'Nominativo',
                        cellTemplate: "<div class='ui-grid-cell-contents' uib-tooltip=\"{{row.entity.cognome}} {{row.entity.nome}}\" tooltip-placement=\"right\" >{{row.entity.cognome}} {{row.entity.nome}}</div>",
                        sortingAlgorithm: function (a, b, rowA, rowB, direction) {
                            var toRet = rowA.entity.cognome.localeCompare(rowB.entity.cognome);
                            if (toRet == 0) {
                                return rowA.entity.nome.localeCompare(rowB.entity.nome);
                            }
                            return toRet;
                        },
                    }, {
                        displayName: 'email',
                        field: 'email',
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.email}}\" tooltip-placement=\"right\" >{{row.entity.email}}</div>",
                    }, {
                        displayName: 'Categoria',
                        name: 'categoriaFr',
                        /*cellTemplate: indirizzoTemplate*/
                        field: 'categoriaFr.descrizione',
                        width: "*",
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{row.entity.categoriaFr.descrizione}}\" tooltip-placement=\"right\" >{{row.entity.categoriaFr.descrizione}}</div>",
                        sort: {
                            direction: uiGridConstants.ASC,
                            priority: 1
                        }
                    },
                    /*{
                        displayName: 'Scadenza',
                        field: 'certificatoFr.dataScadenza',
                        width: "*",
                        cellFilter: "date:'dd/MM/yyyy'"
                    },*/
                    {
                        displayName: '',
                        name: 'Immagine',
                        cellTemplate: "<div class='grid-icon' ng-show=\"row.entity.certificatoFr.immagine\" uib-tooltip=\"Visualizza l'immagine\" tooltip-placement=\"left\">" +
                            "<a ng-click=\"grid.appScope.openModal('certificatoModal.html', {immagine: row.entity.certificatoFr.immagine },'Certificato')\"> " +
                            "<i class=\"fa fa-certificate\" aria-hidden=\"true\"></i></a></div>",
                        width: "30",
                        enableSorting: false,
                        enableFiltering: false
                    }, {
                        displayName: '',
                        name: 'Modifica',
                        cellTemplate: "<div class='grid-icon' ng-hide=\"row.entity.readOnly\" uib-tooltip=\"Modifica\" tooltip-placement=\"left\">" +
                            "<a  ng-click=\"grid.appScope.setInModifying(row.entity.id)\"> " +
                            "<i class=\"fa fa-pencil-square\" aria-hidden=\"true\"></i></a></div>",
                        width: "30",
                        enableSorting: false,
                        enableFiltering: false
                    }, {
                        displayName: '',
                        name: 'Messaggio',
                        cellTemplate: "<div class='grid-icon' ng-show=\"row.entity.dispositivo.pushToken\" uib-tooltip=\"Invia un messaggio Push\" tooltip-placement=\"left\">" +
                            "<a ng-click=\"grid.appScope.openSendMessage(row.entity.id)\"> " +
                            "<i class=\"fa fa-telegram\" aria-hidden=\"true\"></i></a></div>",
                        width: "30",
                        enableSorting: false,
                        enableFiltering: false
                    }
                ]
            };
            $rootScope.$watch('collapsed', function (old, newVal) {
                $scope.gridApi2.core.handleWindowResize();
            });
            $scope.pulisci = function () {
                $scope.gridOptions.data = [];
                $scope.filter = {
                    categoria: {
                        id: "FIRST_RESPONDER",
                        descrizione: "First Responder"
                    }
                };

                stateService.removeState("frSearchController.filter");
                //stateService.resetState("frSearchController.filter", $scope.filter);
            };
            var stFilter = stateService.getState("frSearchController.filter");
            if (stFilter) {
                //scateno la ricerca
                $scope.search(angular.copy(stFilter));

                //sostituisco la stringa con l'oggetto per farlo vedere nella combo
                if (_.isString(stFilter.comune)) {
                    stFilter.comune = {
                        nomeComune: stFilter.comune
                    };
                }
                $scope.filter = stFilter;
            }

        }
    ]);
});