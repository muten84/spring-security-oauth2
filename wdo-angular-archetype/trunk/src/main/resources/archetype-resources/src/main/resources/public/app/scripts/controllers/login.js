'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

/*
 * Definisco un modluo di requirejs che si carica angular*/

define(['angular'], function (angular) {	
	angular.module("controller").controller("LoginController",
			["$scope","apiService","$location","$state","loggerService","$cookies",
			function($scope,apiService,$location,$state,loggerService,$cookies) {
				$scope.loginStep = 1;

				var msg = {
					errorMessage : {
						text : ""
					},
					detailMessage : {
						text : ""
					}

				};
				$scope.msg = msg;
				
				$scope.login = function(credentials) {
					$state.transitionTo("dashboard");		
					
			      }; 
			      
			    function _changeLocation(response){
			    	loggerService.debug(response);
			    	//$location.path( "/styleguides" );
			    	$state.transitionTo('dashboard');
			    }  
			}]);
});
