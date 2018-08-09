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
				$rootScope.isSearchCollapsed = true;
				$rootScope.isSearchEventCollapsed  =true;

				$scope.createNewEvent = function(){
					$rootScope.$broadcast("wdodisrec.newevent",{});
				};

				$scope.myFun = function () {
					$rootScope.isSearchCollapsed = !$rootScope.isSearchCollapsed;
				};

				$scope.myFun2 = function () {
					$rootScope.isSearchEventCollapsed = !$rootScope.isSearchEventCollapsed;
				};

			}]);

	});
