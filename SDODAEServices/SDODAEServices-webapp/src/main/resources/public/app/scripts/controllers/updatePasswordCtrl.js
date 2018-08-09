'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

/*
 * Definisco un modluo di requirejs che si carica angular*/
define(['angular', "constants", "lodash"], function(angular, constants, _) {
    angular.module("controller").controller("UpdatePasswordController", [
        "$scope", "authenticationService", "$location",
        "$state", "loggerService", "$window",
        "$uibModal", "$document", "userService",
        "toastr", "modalService", "validateService", 'configurationService',
        function($scope, authenticationService, $location,
            $state, loggerService, $window,
            $uibModal, $document, userService,
            toastr, modalService, validateService, configurationService) {

            angular.extend($scope, {
                changePassword: function() {
                    if ($scope.oldPassword && $scope.password) {
                        userService.changePassword($scope.oldPassword, $scope.password).then(function(result) {
                            if (result.response) {
                                toastr.success("Cambio Password", "Cambio password avvenuto con successo. Ricorda di non comunicare a nessuno la tua password!");
                                $window.sessionStorage.removeItem('updatePassword');
                                $state.transitionTo("main.dashboard");
                            } else {
                                toastr.error("Verifica dati inseriti", "Attenzione cambio password non avvenuto");
                            }
                        });
                    }
                },
                cancel: function() {
                    $window.sessionStorage.removeItem('updatePassword');
                    $window.sessionStorage.removeItem('token');
                    $state.transitionTo("login");
                },
                validatePassword: function(value) {
                	if( $scope.configurations === undefined ) {
                    	configurationService.getAllConfiguration().then(function(conf) {
  							$scope.configurations = conf;
  							for (var i = 0; i < $scope.configurations.length; i++) {
  								if($scope.configurations[i].parametro === 'ENABLE_PRIVACY') {
  									$scope.enable_privacy = $scope.configurations[i].valore.toLowerCase() === 'true';
  								}
  								if($scope.configurations[i].parametro === 'REGEXP_PWD') {
  									$scope.regexp_pwd = $scope.configurations[i].valore;
  								}
  								if($scope.configurations[i].parametro === 'REGEXP_PWD_FOUR_CHAR') {
  									$scope.regexp_pwd_four_char = $scope.configurations[i].valore;
  								}
  								if($scope.configurations[i].parametro === 'REGEXP_PWD_DOUBLE_CHAR_MAX') {
  									$scope.regexp_pwd_double_char_max = $scope.configurations[i].valore;
  								}
  							}
                        });
					}                                    
					if( $scope.enable_privacy ) {
						var regexpList = [$scope.regexp_pwd, $scope.regexp_pwd_four_char, $scope.regexp_pwd_double_char_max];
						
                    	var result = validateService.validatePasswordPrivacy(value, regexpList);
					
					} else {
                    	var result = validateService.validatePassword(value);
                	}
                    $scope.passwordStreght = result.score;
                    $scope.passwordStreghtType = result.level;
                    return result.valid;
                },
                validateRepeatPassword: function(value) {
                    return value === $scope.password;
                }
            });
        }
    ]);
});
