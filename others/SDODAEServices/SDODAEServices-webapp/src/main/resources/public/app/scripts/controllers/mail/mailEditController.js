'use strict';

define(['angular', "moment", "constants"], function(angular, moment, constants) {
    angular.module("controller").controller('MailEditCtrl', ["$scope", "$filter",
        "loggerService", "$rootScope", "alertService", "modalService",
        "$document", "validateService", "toastr", "mailService",
        function($scope, $filter,
            loggerService, $rootScope, alertService, modalService,
            $document, validateService, toastr, mailService) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Editor Mail",
                icon: "fa fa-envelope-o"
            });


            $scope.modifica = function(index) {
                var data = $scope.templates[index].data;
                var object =  $scope.templates[index].oggetto;
                modalService.openWYSWYGModal('Modifica mail', object, data, function(newData) {
                    var newTemplate = angular.copy($scope.templates[index]);
                    newTemplate.data = newData.data;
                    newTemplate.oggetto = newData.object;
                    return $scope.saveTemplate(newTemplate, index);
                });

                $scope.templates[index].modify = true;
            };

            $scope.pulisci = function(index) {
                $scope.templates[index] = angular.copy($scope.original[index]);
            };

            mailService.getAllMailTemplate().then(function(value) {
                //carico i template dal db e mi salvo nello scope una copia per la pulizia
                $scope.templates = value;
                $scope.original = angular.copy(value);
            });

            $scope.saveTemplate = function(template, index) {
                return mailService.saveMailTemplate(template).then(function(value) {
                    $scope.templates[index] = value;
                });
            };

            $scope.test = function(index) {
                var template = $scope.templates[index];
                var filter = {
                    object : template.oggetto,
                    mail: template.data,
                    mailTemplate: template.id
                };
                modalService.openMailModal("Prova invio mail", filter, mailService);

            };
        }
    ]);
});
