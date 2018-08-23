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
	['angular'],
	function (angular) {
		angular.module("controller").controller("NavBarCtrl", ['$rootScope',
			'$scope',
			'$animate',
			'$sanitize',
			"$cookies",
			"apiService",
			"$state",
			
			function ($rootScope, $scope, $animate, $sanitize, $cookies, apiService, $state) {
				$scope.parking = ""
				$scope.parkingDesc = ""
				var c = $cookies.get("principal");
				var p = angular.fromJson(c);
				if (p) {
					$scope.parking = p.details.parking;
					$scope.parkingDesc = "";
				}



				$rootScope.isSearchCollapsed = true;

				$scope.myFun = function () {
					$rootScope.isSearchCollapsed = !$rootScope.isSearchCollapsed;
				};

				$scope.logout = function(){
					apiService.logout().then(function(data){
						$cookies.remove("principal");
						$state.transitionTo("login");						
					})
				};

			}]);

	});
