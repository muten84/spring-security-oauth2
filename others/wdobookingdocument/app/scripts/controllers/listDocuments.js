'use strict';

/**
 * @ngdoc function
 * @name testAppApp.controller:ListCtrl
 * @description # ListCtrl Controller of the testAppApp
 */
define(['angular', 'moment'], function (angular, moment) {
	angular.module("controller").controller("ListDocumentCtrl",
		ListDocumentCtrl);

	ListDocumentCtrl.$inject = ["$scope", "NgTableParams", "apiService", "$rootScope",
	 "loggerService", "$cookies","$filter","blockUI"];

	function ListDocumentCtrl($scope, ngTableParams, apiService, $rootScope, logger, $cookies,$filter,blockUI) {
		/*var filter = {
			"fromDate" : "2016-09-20T14:27:56.810Z",
			"toDate" : "2016-09-23T14:27:56.810Z",
			"inState" : "CREATED",
			"parking" : "CRI LOIANO",
			"exactParkingMatch" : false
		};*/
		var principal = angular.fromJson($cookies.get("principal"));
		$scope.filter = {
			"fromDate": moment().hour(0).minute(0).second(0),
			"toDate": moment(),
			"inState": null,
			"parking": principal.details.parking,
			/*"excludeInCurrentState": "CLOSED",*/
			"exactParkingMatch": true
		};


		var h = $rootScope.$on("wdobookingdocument.fetchDocuments", function (event, filter) {
			$scope.filter = filter;
			apiService.listDocuments(filter).then(_initGrid);
		});

		$scope.$on("$destroy", function () {
			h();
		});

		$scope.openDocument = function (docId) {			
			logger.debug("opennig document: " + docId);
			apiService.openDocument(docId).then(function (data) {
				$rootScope.$broadcast("wdobookingdocument.changeDocId", docId);
				window.setTimeout(function () {
					apiService.listDocuments($scope.filter).then(_initGrid);
					
				}, 1000);

			}, function (data) {
				$rootScope.$broadcast("wdobookingdocument.changeDocId", docId);
			});
		}

		apiService.listDocuments($scope.filter).then(_initGrid);

		function _initGrid(data) {
			$scope.documents = data;
			$scope.documentsTable = new ngTableParams({
				page: 1,
				count: 10
			}, {
					total: $scope.documents.length,
					getData: function ($defer, params) {
						$scope.data = $scope.documents;
						$defer.resolve($scope.data);
					}
				});
				 
		}

	}

	// angular.module("app").controller('ListCtrl', [ '$scope', function($scope,
	// ngTableParams) {
	//
	//
	// }]);

});
