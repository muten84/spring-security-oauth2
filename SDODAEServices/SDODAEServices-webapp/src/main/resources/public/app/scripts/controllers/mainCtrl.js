'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

define(['angular'], function (angular) {

    angular.module("controller").controller('MainCtrl', ["$document",
        "$uibModal",
        "$rootScope",
        "$scope",
        "$state",
        "alertService",
        "modalService",
        "authenticationService",
        "$window",
        "i18nService",
        function ($document, $uibModal, $rootScope, $scope, $state, alertService, modalService, authenticationService, $window, i18nService) {
            $rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
                //assign the "from" parameter to something
                $rootScope.prevState = from;
            });

            i18nService.setCurrentLang('it');
            /*angular.element(document).find("body").addClass("loaded");*/
            var hanlder = $rootScope.$on("main.changeTitle", function (ev, date) {
                $scope.title = date.title;
                $scope.icon = date.icon;
            });



            //questo serve a distrggere l'handler sopra creato
            $scope.$on('$destroy', function () {
                hanlder();
            });

            /*        var openModal = function (htmlTemplate, data, title) {
                       var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                       var modalInstance = $uibModal.open({
                           animation: true,
                           templateUrl: htmlTemplate,
                           appendTo: parentElem,
                           controller: function ($scope, $uibModalInstance) {
                               var $ctrl = this;
                               $scope.title = title;
                               if(data.error) {
                                   $scope.error = data.error;
                               }
                           }
                       });
                   };*/

            $scope.showHelp = false;


            if ($window.sessionStorage.getItem('token')) {
                $state.transitionTo("main.dashboard");
            }

            $scope.onStart = function (event) {
                console.log('STARTING');
                return '1';
            };

            $scope.onStop = function (event) {
                console.log('ENDING');
                return '0';
            };

            $scope.toggleHelp = function () {
                $scope.showHelp = !$scope.showHelp;
                /*var picture = $('.jumbotron img');
                if (picture.is(':visible')) {
                  return ($('body').data('chardinJs')).toggle();
                } else {
                  return picture.animate({
                    height: 250
                  }, 600, function() {
                    return ($('body').data('chardinJs')).toggle();
                  });
                }*/
            };
        }
    ]);
});