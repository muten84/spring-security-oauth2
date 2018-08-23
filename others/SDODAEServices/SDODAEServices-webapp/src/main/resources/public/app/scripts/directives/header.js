'use strict';

define(["angular"], function(angular) {
    angular.module("directive").directive('areasHeader', function($timeout, $parse) {
        var template =
            "<header class=\"page-header js-sticky-header\">" +
            "  <div class=\"header header-section\">" +
            "      <div class=\"row\">" +
            "          <div class=\"title col-xs-12 col-sm-6\">" +
            "                <span class=\"section-icon\"><i class=\"{{icon}}\"></i></span>" +
            "              <h1 class=\"section-title\">{{title}}</h1>" +
            "            </div>" +
            "        </div>" +
            "    </div>" +
            "  </header>";

        return {
            restrict: "EAC",
            transclude: true,
            replace: true,
            scope: {
                title: "=",
                icon: "="
            },
            template: template
        };
    });
    
});
