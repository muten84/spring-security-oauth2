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

		angular.module(
			"app",
			['ngAnimate', 'ngAria', 'ngCookies', 'ngMessages',
				'ngResource', 'ngRoute', 'ngSanitize', 'ngTouch',
				'ngTable', 'ui.router', 'ui.bootstrap', "pdf", "blockUI", "controller",
				"service"]).config(
			function ($stateProvider, $urlRouterProvider, $httpProvider, blockUIConfig) {
				// Change the default overlay message
				blockUIConfig.message = 'Attendere...';

				blockUIConfig.blockBrowserNavigation = true;
				blockUIConfig.resetOnException = true;

				// Change the default delay to 100ms before the blocking is visible
				blockUIConfig.delay = 2000;

				$httpProvider.interceptors.push('apiInterceptor');

				$urlRouterProvider.otherwise('/dashboard');

				$stateProvider.state('documents', {

					url: "/documents",
					views: {
						"navbar@documents": {
							templateUrl: 'partials/navbar.html',
						},
						"list@documents": {
							templateUrl: 'partials/grid.html',
							controller: "ListCtrl"
						},
						"": {
							templateUrl: 'views/documents.html'
						}
					}
				}).state('dashboard', {
					url: "/dashboard",
					/*resolve: {
						authorize: ['authorization',
							function (apiService) {
								return apiService.checkAuthenticated();
							}
						]
					},*/
					views: {
						"": {
							templateUrl: 'views/dashboard.html'
						},
						"navbar@dashboard": {
							templateUrl: 'partials/navbar.html',
							controller: 'NavBarCtrl'
						},
						"grid@dashboard": {
							templateUrl: 'partials/documentGrid.html',
							controller: "ListDocumentCtrl"
						},
						"search@dashboard": {
							templateUrl: 'partials/search.html',
							controller: "SearchCtrl"
						},
						"modals@dashboard": {
							templateUrl: 'partials/modal.html',
							controller: "ModalDemoCtrl",
							controllerAs: "$ctrl"
						}
					},
					onEnter: function ($cookies, $state) {
						var c = $cookies.get("principal");
						var p = angular.fromJson(c);
						if (!p) {
							$state.transitionTo("login");
							return;
						}
						if (!p.authenticated) {
							$state.transitionTo("login");
							return;
						}
					}
				}).state("dashboard.documents", {
					url: "/documents",
					templateUrl: 'partials/grid.html',
					controller: "ListCtrl"
				}).state("login", {
					url: "/login",
					templateUrl: 'views/login.html',
					controller: "LoginController"
				});

			});

	});
});

// function($routeProvider) {
// $routeProvider.when('/', {
// templateUrl : 'views/login.html',
// controller : 'LoginController',
// controllerAs : 'main'
// }).when('/styleguides', {
// templateUrl : 'views/styleguides.html',
// controller : 'AboutCtrl',
// controllerAs : 'about'
// }).when('/list', {
// templateUrl : 'views/list.html',
// controller : 'ListCtrl',
// controllerAs : 'list'
// }).otherwise({
// redirectTo : '/'
// });
// }

