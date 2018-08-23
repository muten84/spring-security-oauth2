'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

define(['angular'], function (angular) {

  angular.module("controller").controller('MainCtrl', ["$rootScope", "$state", "apiService", "$cookies",
    function ($rootScope, $state, apiService, $cookies) {
      var principal = angular.fromJson($cookies.get("principal"));
      if (!principal) {
        $state.transitionTo('login');
        return;
      }
      apiService.checkRenew(principal.username).then(function (data) {
        if (data) {

          apiService.logout().then(function (data) {
            $cookies.remove("principal");
            $state.transitionTo("login");
          })

        } else {
          //loggerService.debug("User to be validated: " + principal.token);
          $state.transitionTo("dashboard");
        }
      });



    }
  ]);


});
