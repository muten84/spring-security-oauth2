define(["angular"], function (angular) {

    angular.module("controller").controller('DocCtrl', ["$scope", "$rootScope", "loggerService", "blockUI", "apiService", function ($scope, $rootScope, loggerService, blockUI, apiService) {

        $scope.pdfName = 'Relativity: The Special and General Theory by Albert Einstein';
        $scope.pdfUrl = "";
        $scope.scroll = 0;
        $scope.loading = 'loading';
        $scope.pageNum = "1";

        var h3 = $rootScope.$on("wdobookingdocument.changeDocId", function (event, data) {
            loggerService.debug("retrieving pdf from repository: " + data);
            $scope.pdfName = 'Prenotazione ' + data;
            // $scope.pdfUrl = 'http://172.30.50.45:9292/sdoto-docservice-war/rest/repository/getPdf/' + data;
            $scope.pdfUrl = apiService.getBaseUrl() + "repository/getPdf/" + data;
            $rootScope.$broadcast("wdobookingdocument.openDocument", data);
        });

        $scope.$on("$destroy", function () {
            h3();
        });

        $scope.getNavStyle = function (scroll) {
            if (scroll > 100) return 'pdf-controls fixed';
            else return 'pdf-controls';
        }

        $scope.onError = function (error) {
            blockUI.stop();
            console.log(error);
            $scope.pageNum = "1";
        }

        $scope.onLoad = function () {
            blockUI.start();
            $scope.loading = 'Loading...';
            $scope.pageNum = "1";
        }

        $scope.onProgress = function (progress) {
            loggerService.info(progress);
            $scope.pageNum = 1;
        }

    }]);

});

