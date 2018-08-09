'use strict';

/**
 * @ngdoc function
 * @name testAppApp.controller:ListCtrl
 * @description # ListCtrl Controller of the testAppApp
 */
define(['angular'], function (angular) {
	angular.module("controller").controller("ListCtrl", ListCtrl);

	ListCtrl.$inject = [ "$scope", "NgTableParams" ];

	function ListCtrl($scope, ngTableParams) {
		$scope.users = [ {
			"id" : 1,
			"first_name" : "Philip",
			"last_name" : "Kim",
			"email" : "pkim0@mediafire.com",
			"country" : "Indonesia",
			"ip_address" : "29.107.35.8"
		}, {
			"id" : 2,
			"first_name" : "Judith",
			"last_name" : "Austin",
			"email" : "jaustin1@mapquest.com",
			"country" : "China",
			"ip_address" : "173.65.94.30"
		}, {
			"id" : 3,
			"first_name" : "Julie",
			"last_name" : "Wells",
			"email" : "jwells2@illinois.edu",
			"country" : "Finland",
			"ip_address" : "9.100.80.145"
		}, {
			"id" : 4,
			"first_name" : "Gloria",
			"last_name" : "Greene",
			"email" : "ggreene3@blogs.com",
			"country" : "Indonesia",
			"ip_address" : "69.115.85.157"
		}, {
			"id" : 50,
			"first_name" : "Andrea",
			"last_name" : "Greene",
			"email" : "agreene4@fda.gov",
			"country" : "Russia",
			"ip_address" : "128.72.13.52"
		} ];

		$scope.usersTable = new ngTableParams({
			page : 1,
			count : 10
		}, {
			total : $scope.users.length,
			getData : function($defer, params) {
				$scope.data = $scope.users.slice((params.page() - 1)
						* params.count(), params.page() * params.count());
				$defer.resolve($scope.data);
			}
		});
	}

	// angular.module("app").controller('ListCtrl', [ '$scope', function($scope,
	// ngTableParams) {
	//
	//
	// }]);

});
