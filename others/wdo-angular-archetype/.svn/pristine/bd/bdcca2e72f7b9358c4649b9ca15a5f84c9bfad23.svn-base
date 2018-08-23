define(["angular"], function(angular) {
    /*sarebbe più corretto usare templateUrl ma 
    non ho be capito come generalizzare il path del template che si chiama la 
    direttiva quando poi deve essere importata da un modulo esterno */
    /**/
    let templateMenu = "<div ng-class=\"[style]\">" +
        "<div class=\"layout-app-nav\" id=\"menu\">" +
        "<div class=\"slimScrollDiv\" style=\"position: relative; overflow: hidden; width: auto; height: 906px;\">" +
        "<nav class=\"menu-content\" role=\"navigation\" id=\"menu-nav\" aria-multiselectable=\"false\" style=\"overflow: hidden; width: auto; height: 906px;\">" +
        "<ul id=\"areas-entry-container\"class=\"nav nav-pills nav-stacked\">" +
        /*"<div ng-transclude>"+*/
        "<li ng-repeat=\"m in menuItems\">" +
        "<areas-sidebar-entry childs=\"m.childs\" target=\"{{m.target}}\" entrytext=\"{{m.title}}\" iconclass=\"{{m.iconClass}}\"/>" +
        "</li>" +
        /*"</div>"+*/
        "</ul>" +
        "</nav>" +
        "<div class=\"slimScrollBar\" style=\"background: rgb(0, 0, 0); width: 7px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 906px;\"></div>" +
        "<div class=\"slimScrollRail\" style=\"width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(51, 51, 51); opacity: 0.2; z-index: 90; right: 1px;\"></div>" +
        "</div>" +
        "<a ng-click=\"collapseSideBar()\" href=\"#\" class=\"js-collapse-menu\" title=\"\">" +
        "<span class=\"nav-icon\">" +
        "<i ng-show=\"collapsed\" class=\"label-out fa fa-chevron-right\"></i>" +
        "<i  class=\"label-in fa fa-chevron-left\"></i>" +
        "</span>" +
        "<span class=\"nav-label label-in\">Compatta menu</span>" +
        "<span class=\"nav-label label-out\">Nacondi menu</span>" +
        "</a>" +
        "</div>" +
        "</div>";



    let templateSingleEntry =
        "<a ng-click=\"goTo(target)\" class=\"current\" href=\"#\" title=\"{{entrytext}}\">" +
        "<span class=\"nav-icon\"><i class=\"{{iconclass}}\"></i></span>" +
        "<span class=\"nav-label\">{{entrytext}}</span>" +
        "</a>" +
        "<a ng-if=\"childs.length>0\" href=\"#{{entrytext}}-collapse\" data-toggle=\"collapse\" data-parent=\"#menu-nav\" aria-expanded=\"false\"" +
        " aria-controls=\"{{entrytext}}-collapse\" title=\"Espandi {{entrytext}}\"><i class=\"fa fa-chevron-down\"></i></a>" +
        "<ul id=\"{{entrytext}}-collapse\" class=\"panel-collapse collapse in\" role=\"menu\" aria-labelledby=\"menu-{{entrytext}}\" style=\"height: auto;\">"+
        "<li ng-repeat=\"c in childs\"><a ng-click=\"goTo(c.target)\" href=\"#\">{{c.text}}</a></li>" +        
        "</ul>";

    angular.module("directive").directive("areasSidebar", function($http, $compile) {
        return {
            restrict: "E",
            template: templateMenu,
            /*   transclude: true,
               replace: true,*/
            transclude: true,
            replace: true,
            link: function(scope, element, attrs, ctrl, transclude) {
                //debugger;
                /*element.find('span').replaceWith(transclude());*/
            },
            /*scope: true,*/
            scope: { config: '@' },
            controller: function($scope, $http) {
                $scope.collapsed = false;

                $scope.collapseSideBar = function() {
                    if ($scope.style === "collapsed-menu") {
                        $scope.style = "menu";
                    }
                    else {
                        $scope.style = "collapsed-menu"
                    }
                    $scope.collapsed = !$scope.collapsed;
                }

                $http({ url: $scope["config"] }).then(function(data) {
                    $scope.menuItems = data.data;
                }).catch(function(error) {
                    console.log(error);
                });



                /* $scope.test = function(val) {
                     window.alert("Test ok: " + val);
                 }*/
            },
            /*   scope: {
                   collapsed: true
               },*/
            /*  compile: function() {
                  return {
                      pre: function(scope, elem, attrs, ctrl) {
                          //debugger;
                      },
                      post: function(scope, elem, attrs, ctrl) {
                          //debugger;
                          scope.collapsed = false;
  
                          scope.collapseSideBar = function() {
                              if (scope.style === "collapsed-menu") {
                                  scope.style = "menu";
                              }
                              else {
                                  scope.style = "collapsed-menu"
                              }
                              scope.collapsed = !scope.collapsed;
                          }
                      }
                  }
              }*/
        };
    });

    angular.module("directive").directive('areasSidebarEntry', function($compile) {
        return {
            //restrict to elements
            restrict: 'E',
            replace: true,
            transclude: true,
            //take data parameter
            scope: {
                data: '=',
                entrytext: '@',
                iconclass: '@',
                target: '@',
                childs: '=',
                onmenuclick: '&'
            },

            controller: function($scope, $state, $http) {
                //debugger;
                $scope.goTo = function(dest) {
                   // debugger;
                    $state.transitionTo(dest);
                }
            },
            //link function
            link: function(scope, element, attribute, ngModel) {
                scope.clicked = function(value) {
                    //debugger;
                    // scope.onmenuclick();
                }

                /* scope.clicked = function(value) {
                     scope.onvaluechange({ val: value });
                 };
                 
                 scope.valueChanged = function(value) {
                     scope.onvaluechange({ val: value });
                 }*/

                scope.$watch('data', function(newVal, oldVal) {

                    var el = angular.element(templateSingleEntry);
                    $compile(el)(scope);
                    element.parent().append(el);
                });
            },
            /* compile: function() {
                 return {
                     pre: function(scope, elem, attrs, ctrl) {
                         debugger;
                     },
                     post: function(scope, elem, attrs, ctrl) {
                         debugger;
 
                     }
                 }
             }*/
        };
    })

    angular.module("directive").directive("myEntry", function($compile) {
        return {
            restrict: "E",
            template: templateSingleEntry,
            /*link: function(scope, element, attribute, ngModel) {
                //one of the links is clicked, set the value
                scope.clicked = function(value) {
                    scope.onmenuclick();
                };
                $compile(el)(scope);
                element.parent().append(el);
            },*/
            controller: function($scope) {
                debugger;
            },
            scope: {
                title: '=',
                iconClass: '='
            },
            compile: function() {
                return {
                    pre: function(scope, elem, attrs, ctrl) {
                        //debugger;
                        /*  if (attrs.title) {
                              scope.entryText = attrs.title;
                          }
                          if (attrs.icon) {
                              scope.iconClass = attrs.icon;
                          }*/
                    },
                    post: function(scope, elem, attrs, ctrl) {
                        //TODO: append current element on areas entry container
                        //debugger;
                    }
                }
            }
        };
    });
});