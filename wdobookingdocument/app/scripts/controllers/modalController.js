"use strict";

define(["angular"], function (angular) {
    angular.module('controller').controller('ModalDemoCtrl', ["$uibModal", "$scope", "$log", "$document", "$rootScope", function ($uibModal, $scope, $log, $document, $rootScope) {
        var $ctrl = this;

        $ctrl.items = ['item1', 'item2', 'item3'];

        $ctrl.animationsEnabled = true;

        $ctrl.open = function (size, data, parentSelector) {
            var parentElem = parentSelector ?
                angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
            var modalInstance = $uibModal.open({
                animation: $ctrl.animationsEnabled,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                controllerAs: '$ctrl',
                size: size,
                appendTo: parentElem,
                resolve: {
                    items: function () {
                        return $ctrl.items;
                    },
                    docId: function () {
                        return data;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $ctrl.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $ctrl.openComponentModal = function () {
            var modalInstance = $uibModal.open({
                animation: $ctrl.animationsEnabled,
                component: 'modalComponent',
                resolve: {
                    items: function () {
                        return $ctrl.items;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $ctrl.selected = selectedItem;
            }, function () {
                $log.info('modal-component dismissed at: ' + new Date());
            });
        };

        $ctrl.openMultipleModals = function () {
            $uibModal.open({
                animation: $ctrl.animationsEnabled,
                ariaLabelledBy: 'modal-title-bottom',
                ariaDescribedBy: 'modal-body-bottom',
                templateUrl: 'stackedModal.html',
                size: 'sm',
                controller: function ($scope) {
                    $scope.name = 'bottom';
                }
            });

            $uibModal.open({
                animation: $ctrl.animationsEnabled,
                ariaLabelledBy: 'modal-title-top',
                ariaDescribedBy: 'modal-body-top',
                templateUrl: 'stackedModal.html',
                size: 'sm',
                controller: function ($scope) {
                    $scope.name = 'top';
                }
            });
        };

        $ctrl.toggleAnimation = function () {
            $ctrl.animationsEnabled = !$ctrl.animationsEnabled;
        };

        var h1 = $rootScope.$on("wdobookingdocument.changeDocId", function (event, data) {

            /*window.setTimeout(function () { $ctrl.open("lg",data); }, 1000);*/

            $ctrl.open("lg", data);
        });

        $scope.$on("$destroy", function () {
            h1();
        });
    }]);

    // Please note that $uibModalInstance represents a modal window (instance) dependency.
    // It is not the same as the $uibModal service used above.

    angular.module('controller').controller('ModalInstanceCtrl', ["$uibModalInstance", "items", "docId", "$scope", "apiService", function ($uibModalInstance, items, docId, $scope, apiService) {
        var $ctrl = this;

        $scope.pdfName = 'Relativity: The Special and General Theory by Albert Einstein';
        $scope.pdfUrl = apiService.getBaseUrl()+contextApp+"/"+restBasePath+"/repository/getPdf/" + docId;
        $scope.scroll = 0;
        $scope.loading = 'loading';
        $scope.pageNum = "1";

        $scope.getNavStyle = function (scroll) {
            if (scroll > 100) return 'pdf-controls fixed';
            else return 'pdf-controls';
        }

        $scope.onError = function (error) {
            console.log(error);
            //  $scope.pageNum="1";
        }

        $scope.onLoad = function () {
            $scope.loading = '';
            // $scope.pageNum="1";         
        }

        $scope.onProgress = function (progress) {
            console.log(progress);
            //$scope.pageNum=1;
        }


        $ctrl.items = items;
        $ctrl.selected = {
            item: $ctrl.items[0]
        };

        $ctrl.ok = function () {
            $uibModalInstance.close($ctrl.selected.item);
        };

        $ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $ctrl.printDocument = function () {
            var canvas = document.getElementById("pdf");
            //var img    = canvas.toDataURL("image/png");

            canvas.toBlob(function (data) {
                var fileURL = URL.createObjectURL(data);
                var wnd = window.open(fileURL);
                wnd.print();
            }, "image/png");
        }
    }]);

    // Please note that the close and dismiss bindings are from $uibModalInstance.

    angular.module('controller').component('modalComponent', {
        templateUrl: 'myModalContent.html',
        bindings: {
            resolve: '<',
            close: '&',
            dismiss: '&'
        },
        controller: function () {
            var $ctrl = this;

            $ctrl.$onInit = function () {
                $ctrl.items = $ctrl.resolve.items;
                $ctrl.selected = {
                    item: $ctrl.items[0]
                };
            };

            $ctrl.ok = function () {
                $ctrl.close({ $value: $ctrl.selected.item });
            };

            $ctrl.cancel = function () {
                $ctrl.dismiss({ $value: 'cancel' });
            };
        }
    });

});