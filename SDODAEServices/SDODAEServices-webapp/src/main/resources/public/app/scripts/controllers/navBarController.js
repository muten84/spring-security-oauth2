'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

/*
 * Definisco un modluo di requirejs che si carica angular
 */

define(
    ['angular', 'constants'],
    function (angular, constants) {
        angular.module("controller").controller("NavBarCtrl", ['$rootScope',
            '$scope', '$animate', '$sanitize',
            "$cookies", "daeService", "$state",
            "authenticationService", "$window", "userService", "$indexedDB",
            function ($rootScope, $scope, $animate,
                $sanitize, $cookies, daeService,
                $state, authenticationService, $window,
                userService, $indexedDB) {
                $scope.user = {};

                var handler = $rootScope.$on('user.changeProfile', function (event, c) {
                    userService.getLoggedUtenteDetails().then(function (data) {
                        $scope.user = data;
                    });
                });
                $scope.$on("$destroy", function (event) {
                    //distruggo l'handler
                    handler();
                });

                userService.getLoggedUtenteDetails().then(function (data) {
                    $scope.user = data;
                });

                $scope.back = function () {
                    $state.transitionTo($rootScope.prevState);
                };

                $scope.logout = function () {
                    authenticationService.logout().then(function () {
                        $window.sessionStorage.setItem('token', undefined);
                        $state.transitionTo('login');
                    });
                };

                $scope.refresh = function () {
                    try {
                        let stores = [constants.storeNameService, constants.storeNameDAE, constants.storeNameFR, constants.storeNameUser];
                        stores.forEach(function (s) {
                            $indexedDB.openStore(s, function (store) {
                                store.clear().then(function () {
                                    $window.location.reload();
                                });
                            });
                        });
                    } catch (e) {
                        $window.location.reload();
                    }
                };

                $scope.profilo = function () {
                    //debugger;
                    if ($state.current.name === 'main.userProfile') {
                        return;
                    }
                    $state.transitionTo('main.userProfile');
                };


            }
        ]);
    });