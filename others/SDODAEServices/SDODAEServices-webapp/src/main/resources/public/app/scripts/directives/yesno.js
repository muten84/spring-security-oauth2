'use strict';

define(["angular"], function (angular) {

    angular.module('directive').directive('appYesno', ['modalService',
     function (modalService) {
        return {
         //   restrict: 'E',
            scope: {
                title: "@",
                message: "@",
                confirmText: "@confirmText",
                cancelText: "@cancelText",
                placement: "@",
                onConfirm : "&onConfirm",
                onCancel : "&onCancel",
                confirmButtonType: "@confirmButtonType",
                cancelButtonType: "@cancelButtonType"
            },

            link: function ($scope, element, attrs) {
                element.bind('click', function () {
                    modalService.openYesNoModal({
                        title: $scope.title,
                        text: $scope.message,
                        ok: function () {
                            $scope.onConfirm();
                        },
                        cancel: function () {
                            $scope.onCancel();
                        }
                    });
                });
            }
        }
    }]);
});