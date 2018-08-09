'use strict';
/**
 * @ngdoc overview
 * @name testAppApp
 * @description # testAppApp
 *
 * Main module of the application.
 */
// @ts-ignore
require(["angular", "lodash", "controllers", "services", "filters", "directives"], function (angular, _,
    controllers, services, filters, directives) {
    var deps = ["angular", "constants"];
    deps = deps.concat(directives).concat(filters).concat(controllers).concat(services);
    define(deps, function (angular, constants) {

        var myApp = angular.module(
            "app", ['ngAnimate', 'ngAria', 'ngCookies', 'ngMessages',
                'ngResource', 'ngRoute', 'ngSanitize', 'ngTouch',
                'ngTable', 'ui.router', 'ui.grid', 'ui.grid.expandable',
                'ui.bootstrap', 'ui.bootstrap.carousel', "blockUI", "xeditable",
                "ui.select", "mwl.confirm", "angularHelpOverlay", "areas-ui",
                "controller", "service", "ui-leaflet", 'ui.validate',
                "chart.js", "toastr", 'ui.grid.pagination', "angularTrix",
                'ui.grid.selection', 'indexedDB', 'nemLogging', 'angularFileUpload',
            ]).config(
            function (
                $compileProvider, $stateProvider, $urlRouterProvider,
                $httpProvider, blockUIConfig, uiSelectConfig, ChartJsProvider,
                toastrConfig, $indexedDBProvider) {
                $compileProvider.debugInfoEnabled(false);
                //configurazioni per l'indexedDB
                try {
                    $indexedDBProvider
                        .connection(constants.dbName)
                        .upgradeDatabase(2, function (event, db, tx) {
                            var objStore = db.createObjectStore(constants.storeNameService, {
                                keyPath: 'url'
                            });
                            var objStore = db.createObjectStore(constants.storeNameDAE, {
                                keyPath: 'id'
                            });
                            var objStore = db.createObjectStore(constants.storeNameFR, {
                                keyPath: 'id'
                            });
                            var objStore = db.createObjectStore(constants.storeNameUser, {
                                keyPath: 'id'
                            });
                        }).upgradeDatabase(3, function (event, db, tx) {
                            tx.objectStore(constants.storeNameService).clear();
                            tx.objectStore(constants.storeNameDAE).clear();
                            tx.objectStore(constants.storeNameFR).clear();
                            tx.objectStore(constants.storeNameUser).clear();
                        });
                } catch (e) {}
                angular.extend(toastrConfig, {
                    autoDismiss: false,
                    /*containerId: 'toast-container',*/
                    maxOpened: 5,
                    newestOnTop: true,
                    positionClass: 'toast-bottom-right',
                    preventDuplicates: false,
                    preventOpenDuplicates: true,
                    timeOut: 2000,
                    /*target: 'body'*/
                });

                /* ChartJsProvider.setOptions({
                     chartColors: ['#00809d', '#5bd6de', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360']
                 });*/

                uiSelectConfig.theme = 'bootstrap';
                uiSelectConfig.resetSearchInput = true;
                // Change the default overlay message
                blockUIConfig.message = 'Attendere...';

                blockUIConfig.blockBrowserNavigation = true;
                blockUIConfig.resetOnException = true;

                // Change the default delay to 100ms before the blocking is visible
                blockUIConfig.delay = 1000;

                $httpProvider.defaults.useXDomain = true;
                $httpProvider.interceptors.push('apiInterceptor');
                //router di default
                $urlRouterProvider.otherwise('/main');

                // pulisco i filtri eventualmente salvati
                window.sessionStorage.removeItem("daeSearchController.filter");
                window.sessionStorage.removeItem("frSearchController.filter");
                window.sessionStorage.removeItem('userSearchController.filter');
                window.sessionStorage.removeItem("eventSearchController.filter");

                $stateProvider
                    //Login
                    .state("login", {
                        url: "/login",
                        templateUrl: 'views/login.html',
                        controller: "LoginController"
                    })
                    .state("updatePassword", {
                        url: "/updatePassword",
                        templateUrl: 'views/updatePassword.html',
                        controller: "UpdatePasswordController"
                    })
                    // Main con tutte le sotto view
                    .state("main", {
                        url: "/main",
                        views: {
                            "": {
                                templateUrl: 'views/main.html'
                            },
                            "navbar@main": {
                                templateUrl: 'partials/new-navbar.html',
                                controller: 'NavBarCtrl'
                            },
                            "modals@main": {
                                templateUrl: 'partials/modal/modals.html'
                            }
                        },
                        onEnter: function ($window, $state) {
                            if (!($window.sessionStorage.getItem('token'))) {
                                $state.transitionTo("login");
                                return;
                            }
                        }
                    })
                    .state("main.dashboard", {
                        url: "/dashboard",
                        templateUrl: 'partials/dashboard.html',
                        controller: 'DashboardCtrl',
                        onEnter: function ($window, $state) {
                            if (!($window.sessionStorage.getItem('token'))) {
                                $state.transitionTo("login");
                                return;
                            }
                        }
                    })
                    // pannello ricerca dae
                    .state("main.daeSearch", {
                        url: "/daeSearch",
                        templateUrl: 'partials/dae/daeSearch.html',
                        controller: "DaeSearchCtrl"
                    })
                    // pannello ricerca dae
                    .state("main.daeMap", {
                        url: "/daeMap",
                        templateUrl: 'partials/dae/daeMap.html',
                        controller: "DaeMapCtrl"
                    })
                    // pannello inserimento dae
                    .state("main.daeInsert", {
                        url: "/daeInsert",
                        templateUrl: 'partials/dae/daeInsert.html',
                        controller: "DaeInsertCtrl",
                        resolve: {
                            currentDae: function (daeService) {
                                var daeId = daeService.getCurrentDaeId();
                                if (!_.isUndefined(daeId)) {
                                    var filter = {
                                        id: daeId
                                    };
                                    return daeService.searchDAEByFilter(filter);
                                } else {
                                    return undefined;
                                }
                            }
                        }
                    })
                    //Pannello gestione Fault
                    .state("main.daeFault", {
                        url: "/daeFault",
                        templateUrl: 'partials/dae/daeFault.html',
                        controller: "DaeFaultCtrl",
                        resolve: {
                            currentDae: function (daeService) {
                                var daeId = daeService.getCurrentDaeId();
                                if (!_.isUndefined(daeId)) {
                                    var filter = {
                                        id: daeId,
                                        fetchRule: 'DAE_FAULT'
                                    };
                                    return daeService.searchDAEByFilter(filter);
                                } else {
                                    return undefined;
                                }
                            }
                        }
                    })
                    // pannello ricerca fr
                    .state("main.frSearch", {
                        url: "/frSearch",
                        templateUrl: 'partials/fr/frSearch.html',
                        controller: "FrSearchCtrl"
                    })
                    .state("main.frUpdate", {
                        url: "/frUpdate",
                        templateUrl: 'partials/fr/frUpdate.html',
                        controller: "FrUpdateCtrl",
                        resolve: {
                            currentFr: function (frService) {
                                var frId = frService.getCurrentFr();
                                if (!_.isUndefined(frId)) {
                                    /*var filter = { id: frId };*/
                                    return frService.reloadFirstReponder(frId);
                                } else {
                                    return undefined;
                                }
                            }
                        }
                    })
                    //Ricerca notifiche
                    .state("main.notificationSearch", {
                        url: "/notificationSearch",
                        templateUrl: 'partials/notification/notificationSearch.html',
                        controller: "NotificationSearchCtrl"
                    })
                    //Invio notifiche
                    .state("main.notificationSend", {
                        url: "/notificationSend",
                        templateUrl: 'partials/notification/notificationSend.html',
                        controller: "NotificationSendCtrl"
                    }) //Gestione configurazioni
                    .state("main.configuration", {
                        url: "/configuration",
                        templateUrl: 'partials/configuration/configuration.html',
                        controller: "ConfigurationCtrl"
                    })
                    //Gestione Mail
                    .state("main.mailEdit", {
                        url: "/mailEdit",
                        templateUrl: 'partials/mail/mailEdit.html',
                        controller: "MailEditCtrl"
                    }).state("main.mailList", {
                        url: "/mailList",
                        templateUrl: 'partials/mail/mailList.html',
                        controller: "MailListCtrl"
                    })
                    //Gestione Utenti
                    .state("main.userSearch", {
                        url: "/userSearch",
                        templateUrl: 'partials/user/userSearch.html',
                        controller: "UserSearchCtrl"
                    })
                    .state("main.userInsert", {
                        url: "/userInsert",
                        templateUrl: 'partials/user/userInsert.html',
                        controller: "UserInsertCtrl",
                        resolve: {
                            currentUser: function (userService) {
                                var userId = userService.getCurrentUserId();
                                if (!_.isUndefined(userId)) {
                                    var filter = {
                                        id: userId
                                    };
                                    return userService.searchUtenteByFilter(filter);
                                } else {
                                    return undefined;
                                }
                            }
                        }
                    })
                    .state("main.userProfile", {
                        url: "/userProfile",
                        templateUrl: 'partials/user/userProfile.html',
                        controller: "UserProfileCtrl"
                    })
                    .state("main.groupInsert", {
                        url: "/groupInsert",
                        templateUrl: 'partials/user/groupInsert.html',
                        controller: "GroupInsertCtrl"
                    })
                    //gestione Eventi
                    .state("main.eventSearch", {
                        url: "/eventSearch",
                        templateUrl: 'partials/event/eventSearch.html',
                        controller: "EventSearchCtrl"
                    }).state("main.eventDetail", {
                        url: "/eventDetail",
                        templateUrl: 'partials/event/eventDetail.html',
                        controller: "EventDetailCtrl",
                        resolve: {
                            currentEvent: function (emergencyService) {
                                var eventId = emergencyService.getCurrentEventId();
                                if (!_.isUndefined(eventId)) {
                                    var filter = {
                                        fetchRule: 'DETAIL',
                                        id: eventId
                                    };
                                    return emergencyService.searchEventByFilter(filter);
                                } else {
                                    return undefined;
                                }
                            }
                        }
                    })
                    .state("main.graph", {
                        url: "/graph",
                        templateUrl: 'partials/report/graph.html',
                        controller: "GraphCtrl"
                    });
            });

        myApp.run(function (editableOptions) {
            editableOptions.theme = 'bs3';


            var currentClass = document.body.className;
            document.body.className = currentClass + " loaded";
        });
    });
}, function (error) {
    console.log("Errore caricamento moduli dell'applicazione qui sarebbe utile sbattere l'utente su una pagina html statica che lo guida al reload dell'applicazione");
});