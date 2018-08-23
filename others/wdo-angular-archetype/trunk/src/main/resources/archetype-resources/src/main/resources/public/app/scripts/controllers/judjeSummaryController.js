define(["angular"], function (angular) {
    angular.module('controller').controller("JudgeSummaryController", ["$uibModal", "$scope", "$log", "$document", "$rootScope",
        function ($uibModal, $scope, $log, $document, $rootScope) {
            var $ctrl = this;

            $ctrl.open = function (size, data, dataEvent, parentSelector) {
                var parentElem = parentSelector ?
                    angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
                var modalInstance = $uibModal.open({
                    animation: $ctrl.animationsEnabled,
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: 'modalSummary.html',
                    controller: 'InnerCtrl',
                    controllerAs: '$ctrl',
                    windowClass: 'app-modal-window',
                    size: "sm",
                    backdrop: "static",
                    appendTo: parentElem,
                    resolve: {
                        summaryList: function () {
                            return data;
                        },
                        currentEvent: function () {
                            return dataEvent;
                        }
                    }
                });

                modalInstance.result.then(function (summary) {
                    //debugger;
                    $ctrl.summary = summary;
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $ctrl.toggleAnimation = function () {
                $ctrl.animationsEnabled = !$ctrl.animationsEnabled;
            };

            var h1 = $rootScope.$on("wdodisrec.openSummaryJudge", function (event, list, currentEvent) {

                /*window.setTimeout(function () { $ctrl.open("lg",data); }, 1000);*/

                $ctrl.open("sm", list, currentEvent);
            });

            $scope.$on("$destroy", function () {
                h1();
            });

        }]);

    angular.module('controller').controller('InnerCtrl',
        ["$uibModalInstance", "summaryList", "currentEvent", "$scope", "apiService", "$rootScope",
            function ($uibModalInstance, summaryList, currentEvent, $scope, apiService, $rootScope) {
                var $ctrl = this;

                $ctrl.currentform = {};

                $ctrl.setCurrentForm = function (form) {                    
                    $ctrl.currentform = form;
                }


                $ctrl.pathologies = summaryList.pathologies;
                $ctrl.places = summaryList.places;
                $ctrl.criticities = summaryList.criticities;

                $ctrl.ev = currentEvent;

                $ctrl.ok = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $ctrl.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $ctrl.saveSummary = function (data){
                    //debugger;
                    $ctrl.ev.pathology = data.pathology;
                    $uibModalInstance.close(data);                    
                }
            }]);
});