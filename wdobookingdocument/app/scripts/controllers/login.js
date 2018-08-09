'use strict';

/**
 * @ngdoc function
 * @name APP_NAME.controller:MainCtrl, AnotherCtrl
 * @description # MainCtrl Controller of the APP_NAME
 */

/*
 * Definisco un modluo di requirejs che si carica angular*/

define(['angular'], function (angular) {
  angular.module("controller").controller("LoginController", ["$scope", "apiService", "$location", "$state", "loggerService", "$cookies",
    function ($scope, apiService, $location, $state, loggerService, $cookies) {
      $scope.loginStep = 1;
      $scope.passwordReset = false;

      var msg = {
        errorMessage: {
          text: ""
        },
        detailMessage: {
          text: ""
        }

      };
      $scope.msg = msg;

      $scope.logout = function () {
        //					apiService.logout().then(function(data){
        //						$cookies.remove("principal");
        //						$state.transitionTo("login");						
        //					})
      };

      $scope.login = function (credentials) {
        msg.errorMessage.text = '';
        if (!credentials) {
          msg.errorMessage.text = "Inserire username e password";
          $scope.logout();
          return;
        }
        if (!credentials.password || !credentials.username) {
          msg.errorMessage.text = "Inserire username e password";
          $scope.logout();
          return;
        }
        if ($scope.passwordReset) {
          if (!credentials.newPassword || !credentials.newPassword2) {
            msg.errorMessage.text = "Inserire la password";
            $scope.logout();
            return;
          }
          if (credentials.password == credentials.newPassword2) {
            msg.errorMessage.text = "La nuova password non può essere uguale alla password precedente";
            $scope.logout();
            return;
          }
          if (credentials.newPassword != credentials.newPassword2) {
            msg.errorMessage.text = "Le password non coincidono";
            $scope.logout();
            return;
          }
          var score = apiService.validateNewPassword(credentials);

          if (score.valid) {
            apiService.changePassword(credentials).then(function (response) {
              $cookies.remove("principal");
              $scope.passwordReset = false;
              $scope.loginStep = 1;
            })
          } else {
            msg.errorMessage.text = "Password debole. Inserire almeno 8 caratteri. Almeno un carattere numerico. Almeno un carattere minuscolo. Almeno un carattere maiuscolo.";
            $scope.logout();
            return;
          }

        } else {
          apiService.authenticate(credentials).then(function (data) {
              loggerService.debug("User authenticated: " + data.token);
              var expireDate = new Date();
              expireDate.setDate(expireDate.getDate() + expiresInDays);
              //$cookies.put('principal', angular.toJson(data),{'expires': expireDate});
              $cookies.put('principal', angular.toJson(data));
              var principal = angular.fromJson($cookies.get("principal"));
              apiService.checkRenew(credentials).then(function (data) {
                if (data) {
                  /* apiService.checkRenew().then(function(resp) {
                       $cookies.remove("principal");
                       $state.transitionTo("login");
                   })*/
                  $scope.passwordReset = true;
                  $scope.loginStep = -1;
                } else {
                  loggerService.debug("User to be validated: " + principal.token);
                  $state.transitionTo("dashboard");
                }

              });
            },
            function (err) {
              debugger;
              loggerService.error("Authentication failed", err);
              if (err.status == 401) {
                msg.errorMessage.text = "Utente e/o password errati, oppure l'utente esiste ma non ha una postazione associata";
              }


            })
          /*loggerService.debug(result)*/
        }
      };

      function _changeLocation(response) {
        loggerService.debug(response);
        //$location.path( "/styleguides" );
        $state.transitionTo('dashboard');
      }
    }
  ]);
});
