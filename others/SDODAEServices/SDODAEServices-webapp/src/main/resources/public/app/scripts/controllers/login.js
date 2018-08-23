'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

/*
 * Definisco un modluo di requirejs che si carica angular*/
define(['angular', "constants", "lodash"], function(angular, constants, _) {
    angular.module("controller").controller("LoginController", [
        "$scope", "authenticationService", "$location",
        "$state", "loggerService", "$window",
        "$uibModal", "$document",
        function($scope, authenticationService, $location,
            $state, loggerService, $window, $uibModal, $document) {
            $scope.loginStep = 1;
            var msg = {
                errorMessage: {
                    text: ""
                },
                detailMessage: {
                    text: ""
                }
            };
            $scope.msg = msg;

            $scope.login = function(credentials) {
                var promise = authenticationService.authenticateUser(credentials);
                promise.then(function(data) {
                    loggerService.info(data.token);
                    $window.sessionStorage.setItem('token', data.token);

                    //se in fase di login ho ricevuto la segnalazione di password scaduta
                    if (data.updatePassword) {
                        $window.sessionStorage.setItem('updatePassword', data.updatePassword);
                        $state.transitionTo("updatePassword");
                    } else if (data.token) {
                        $state.transitionTo("main.dashboard");
                    }
                });
            };
            //fa comparire la maschera di recupero password tramite la mail
            $scope.openPasswordReset = function(credentials) {
                var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: "passwordResetModal.html",
                    appendTo: parentElem,
                    controller: function($scope, $uibModalInstance) {
                        var $ctrl = this;
                        $scope.reset = function() {
                            if ($scope.email) {
                                authenticationService.resetPassword($scope.email).then(function(result) {
                                    $scope.$dismiss('cancel');
                                    console.log('Messaggio inviato ' + result);
                                });
                            }
                        };
                    }
                });
            };

            function _changeLocation(response) {
                loggerService.debug(response);
                //$location.path( "/styleguides" );
                $state.transitionTo('main.dashboard');
            }
        }
    ]);

    angular.module("controller").controller("NormativaCookieCtrl", ["$window", "$scope", "abstractSerivce",
        function($window, $scope, abstractSerivce) {
            var accept = $window.sessionStorage.getItem('cookieAccept');
            $scope.showMsgCookies = _.isUndefined(accept) || _.isNull(accept);
            $scope.userAcceptCookies = function() {
                $window.sessionStorage.setItem('cookieAccept', true);
                $scope.showMsgCookies = false;
            };
            $scope.openWindow = function() {
                var baseUrl = abstractSerivce._getUrlBase();
                $window.open(baseUrl + "/app/disclaimer/cookie.html", 'C-Sharpcorner', 'width=500,height=400');
            };
        }
    ]);


    /*TODO: APPLICARE QEUSTA TECNICA  https://ui-router.github.io/docs/latest/classes/transition.transitionservice.html
    // ng1
    $transitions.onBefore( { to: 'requireauth.**' }, function(trans) {
      var myAuthService = trans.injector().get('MyAuthService');
      // If isAuthenticated returns false, the transition is cancelled.
      return myAuthService.isAuthenticated();
    });*/
    angular.module('controller')
        .run(function($rootScope, $urlRouter, authenticationService, $window, $state) {
            $rootScope.$on('$locationChangeSuccess', function(evt, newUrl) {
                //Se lo stato non è quello di login controllo che ci sia il token in memoria e che il token sia valido
                if (!constants.bypassLogin &&  (newUrl.indexOf('login') === -1 && newUrl.indexOf('updatePassword') === -1 ) ) {
                    // Halt state change from even starting
                    evt.preventDefault();
                    // Perform custom logic
                    if ($window.sessionStorage.getItem('updatePassword') ) {
                        $state.transitionTo("updatePassword");
                    } else if ($window.sessionStorage.getItem('token')) {
                        authenticationService.isAuthenticated($window.sessionStorage.getItem('token')).then(function(data) {
                            //passo allo stato successivo
                            $urlRouter.sync();
                        }, function(error) {
                            $window.sessionStorage.removeItem('token');
                            $state.transitionTo('login');
                        });
                    } else {
                        $state.transitionTo('login');
                    }
                }
            });
        });
});
