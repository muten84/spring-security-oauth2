appControllers.controller('DialogController', [
    '$scope',
    '$mdDialog',
    'displayOption',
    function ($scope, $mdDialog, displayOption) {

        $scope.displayOption = displayOption;

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.ok = function () {
            $mdDialog.hide();
        };
    }]);

appControllers.controller('ToastController', [
    '$scope',
    'displayOption',
    function ($scope, displayOption) {
        $scope.displayOption = displayOption;
    }]);
