'use strict';

define(['angular',"moment"], function (angular,moment) {
	angular.module("controller").controller(
		"SearchCtrl",
		["$scope", "$rootScope","apiService", "loggerService","$cookies",
		function ($scope, $rootScope,apiService, loggerService,$cookies) {
			$scope.docStateFilter = {
				name: 'TUTTI',
				shade: 'all'
			};
			
			$scope.dateFrom = new Date();
			$scope.dateTo = new Date();
			$scope.bookingCode = "";

			$scope.$on("$destroy", function() {
				//alert("destroyed controller");
			});

			$scope.documentStates = [
				{
				name: 'TUTTI',
				shade: 'all'
			},
			{
				name: 'CREATO',
				shade: 'CREATED'
			}, {
				name: 'APERTO',
				shade: 'OPENED'
			}, {
				name: 'INVIATO',
				shade: 'SENT'
			},
			{
				name: 'CHIUSO',
				shade: 'CLOSED'
			}];


			$scope.dateFromPopup = {
				opened: false
			};

			$scope.dateToPopup = {
				opened: false
			};

			$scope.openDateFrom = function () {
				$scope.dateFromPopup.opened = true;
			};

			$scope.openDateTo = function () {
				$scope.dateToPopup.opened = true;
			};

			$scope.today = function () {
				$scope.dateFrom = new Date();
				$scope.dateTo = new Date();
			};
			$scope.today();

			$scope.clear = function () {
				$scope.dateFrom = null;
				$scope.dateTo = null;
			};

			$scope.options = {
				customClass: getDayClass,
				minDate: new Date(),
				showWeeks: false
			};

			// Disable weekend selection
			function disabled(data) {
				var date = data.date, mode = data.mode;
				return mode === 'day'
					&& (date.getDay() === 0 || date.getDay() === 6);
			}

			$scope.toggleMin = function () {
				$scope.options.minDate = $scope.options.minDate ? null
					: new Date();
			};

			$scope.toggleMin();

			$scope.setDate = function (year, month, day) {
				$scope.dt = new Date(year, month, day);
			};

			var tomorrow = new Date();
			tomorrow.setDate(tomorrow.getDate() + 1);
			var afterTomorrow = new Date(tomorrow);
			afterTomorrow.setDate(tomorrow.getDate() + 1);
			$scope.events = [{
				date: tomorrow,
				status: ''
			}, {
				date: afterTomorrow,
				status: ''
			}];

			function getDayClass(data) {
				var date = data.date, mode = data.mode;
				if (mode === 'day') {
					var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

					for (var i = 0; i < $scope.events.length; i++) {
						var currentDay = new Date($scope.events[i].date)
							.setHours(0, 0, 0, 0);

						if (dayToCheck === currentDay) {
							return $scope.events[i].status;
						}
					}
				}

				return '';
			}

			$scope.searchDocuments = function () {
				loggerService.debug("Invoking search documents: " + $scope.bookingCode + " - " + $scope.dateFrom + " - " + $scope.dateTo + " - " + $scope.docStateFilter.shade);
				var c = $cookies.get("principal");
				var p = angular.fromJson(c);
				
				var filter = {
					"fromDate": moment($scope.dateFrom).hour(0).minute(0).second(0),
					"toDate": moment($scope.dateTo).hour(23).minute(59).second(59),					
					"currentState": $scope.docStateFilter.shade,
					/*"excludeInCurrentState": "CLOSED",*/
					"parking": p.details.parking,
					"exactParkingMatch": true,
					"docReference": $scope.bookingCode
				};

				/*apiService.listDocuments(filter);*/
				$rootScope.$broadcast("wdobookingdocument.fetchDocuments", filter);
			}

		}]);

});