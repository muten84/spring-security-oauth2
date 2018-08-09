'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

define(['angular'], function (angular) {

	angular.module("controller").controller('MainCtrl', ["$rootScope", "$state", "alertService",
		function ($rootScope, $state, alertService) {

			$rootScope.closeAlert = alertService.closeAlert;
			$state.transitionTo('login');


		}]);


});


