'use strict';
/*
questa direttiva può essere utile quando si vuole gestire un comportamento generico su tutte le ui-select*/
define(["angular"], function(angular) {
    angular.module('directive').directive('labelValidated', function($timeout) {
        var template =
            "  <div ng-class=\"{{class}}\">" +
            "      <div class=\"row\">" +
            "          <div class=\"col-md-12\">" +
            "              <label for=\"nome\">{{label}}</label>" +
            "              <validation-tooltip hovertext=\"'Campo obbligatorio'\" target=\"{{name}}\">" +
            "                  <ul class=\"list - unstyled \">" +
            "                      <li validation-message ng-if=\"$field.$error.required\">Il campo {{label}} e' obbligatorio</li>" +
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
                class: "=",
            },
            template: template
        };
    });
});
