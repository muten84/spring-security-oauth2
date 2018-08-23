'use strict';
/*
questa direttiva può essere utile quando si vuole gestire un comportamento generico su tutte le ui-select*/
define(["angular"], function (angular) {
    angular.module('directive').directive('labelValidated', function ($timeout) {
        var template =
            "  <div ng-class=\"{{class}}\">" +
            "      <div class=\"row\">" +
            "          <div class=\"col-md-12\">" +
            "              <label for=\"nome\">{{label}}</label>" +
            "              <validation-tooltip hovertext=\"'Campo obbligatorio'\" target=\"{{name}}\" label=\"{{label}}\" message=\"{{message}}\">" +
            "                  <ul class=\"list-unstyled \">" +
            "                      <li validation-message ng-if=\"$field.$error.required\">{{message ? message : \"Il campo \" + label + \" e' obbligatorio\"}}</li>" +
            "                  </ul>" +
            "              </validation-tooltip>" +
            "          </div>" +
            "      </div>" +
            "  </div>";

        return {
            restrict: 'E',
            require: ['^validationTooltip'],
            replace: true,
            scope: {
                name: "@",
                label: "@",
                message: "@",
                class: "=",
            },
            template: template
        };
    });
    angular.module('directive').directive('validationTooltip', function ($timeout) {
        return {
            restrict: 'E',
            transclude: true,
            require: '^form',
            scope: {
                hovertext: "=",
                label: "@",
                message: "@",
            },
            template: '<span class="label label-danger-tooltip-lighter span1" ng-show="errorCount > 0"><i class="fa fa-exclamation" aria-hidden="true"></i></span>',
            controller: function ($scope) {
                /*debugger;*/
                var expressions = [];
                $scope.errorCount = 0;
                this.$addExpression = function (expr) {
                    expressions.push(expr);
                };
                $scope.$watch(function () {
                    var count = 0;
                    angular.forEach(expressions, function (expr) {
                        if ($scope.$eval(expr)) {
                            ++count;
                        }
                    });
                    return count;

                }, function (newVal) {
                    $scope.errorCount = newVal;
                });

            },
            link: function (scope, element, attr, formController, transcludeFn) {
                scope.$form = formController;

                transcludeFn(scope, function (clone) {
                    var badge = element.find('.label');

                    var tooltip = angular.element('<div class="validationMessageTemplate tooltip-danger" />');
                    tooltip.append(clone);
                    element.append(tooltip);
                    if (badge && badge.tooltip) {
                        $timeout(function () {
                            scope.$field = formController[attr.target];
                            badge.tooltip({
                                placement: 'right',
                                html: true,
                                title: clone
                            });
                        });
                    }
                });
            }
        };
    });

    angular.module('directive').directive('validationMessage', function () {
        return {
            restrict: 'A',
            priority: 1000,
            require: '^validationTooltip',
            link: function (scope, element, attr, ctrl) {
                ctrl.$addExpression(attr.ngIf || true);
            }
        };
    });
});