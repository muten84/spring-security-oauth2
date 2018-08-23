define(["angular"], function (angular) {
    angular.module("controller").controller('SearchPhonesCtrl', ["$scope", "$filter", "apiService", "loggerService", "$rootScope",
        function ($scope, $filter, apiService, loggerService, $rootScope) {
            $scope.phones = [];

            $scope.searchPhones = function (desc) {
                apiService.searchPhones(desc).then(function (response) {
                    $scope.phones = response;
                });
            }
        }]);
})