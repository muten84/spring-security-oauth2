'use strict';
/**
 * @ngdoc overview
 * @name testAppApp
 * @description # testAppApp
 * 
 * Main module of the application.
 */

require(["angular", "controllers", "services", "filters", "directives"], function (angular,
	controllers, services, filters, directives) {
	var deps = ["angular"];
	deps = deps.concat(directives).concat(filters).concat(controllers).concat(services);
	define(deps, function (angular, LoginController, MainCtrl, apiService) {

		var myApp = angular.module(
			"app",
			['ngAnimate', 'ngAria', 'ngCookies', 'ngMessages',
				'ngResource', 'ngRoute', 'ngSanitize', 'ngTouch',
				'ngTable', 'ui.router', 'ui.bootstrap', "blockUI", "xeditable", "ui.select", "mwl.confirm", "controller",
				"service"]).config(
			function ($stateProvider, $urlRouterProvider, $httpProvider, blockUIConfig) {
				// Change the default overlay message
				blockUIConfig.message = 'Attendere...';

				blockUIConfig.blockBrowserNavigation = true;
				blockUIConfig.resetOnException = true;

				// Change the default delay to 100ms before the blocking is visible
				blockUIConfig.delay = 1000;

				//$httpProvider.interceptors.push('apiInterceptor');

				//$urlRouterProvider.otherwise('/dashboard');

				$stateProvider.state('dashboard', {
					url: "/dashboard",
					views: {
						"": {
							templateUrl: 'views/dashboard.html'
						},
						"navbar@dashboard": {
							templateUrl: 'partials/navbar.html',
							controller: 'NavBarCtrl'
						},
						"grid@dashboard": {
							templateUrl: 'partials/eventsGrid.html',
							controller: "EventGridCtrl"
						},
						"modals@dashboard": {
							templateUrl: 'partials/vehicleModal.html',
							controller: "VehicleModalCtrl",
							controllerAs: "$ctrl"
						},
						"modalSummary@dashboard": {
							templateUrl: 'partials/judgeSummary.html',
							controller: "JudgeSummaryController",
							controllerAs: "$ctrl"
						},
						"search@dashboard": {
							templateUrl: 'partials/searchPhones.html',
							controller: "SearchPhonesCtrl"
						},
						/*"grid@dashboard": {
							templateUrl: 'partials/testUiSelect.html',
							controller: 'TestUICtrl'
						}*/
					}
				}).state("login", {
					url: "/login",
					templateUrl: 'views/login.html',
					controller: "LoginController"
				});

			});

		myApp.run(function (editableOptions) {
			editableOptions.theme = 'bs3';
		});

	});
});

