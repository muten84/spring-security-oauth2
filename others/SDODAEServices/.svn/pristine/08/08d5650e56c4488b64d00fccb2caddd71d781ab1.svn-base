'use strict';

define(["angular", "packery", "draggabilly", "lodash"], function (angular, Packery, Draggabilly, _) {

    //aggiungo dei metodi di utilità per recuperare la posizione dal blocco
    Packery.prototype.getShiftPositions = function (attrName) {
        attrName = attrName || 'id';
        var _this = this;
        return this.items.map(function (item) {
            return {
                id: item.element.getAttribute(attrName),
                x: item.rect.x / _this.packer.width
            }
        });
    };
    //e per impostare la posizione al blocco
    Packery.prototype.initShiftLayout = function (positions, attr) {
        if (!positions) {
            // if no initial positions, run packery layout
            this.layout();
            return;
        }
        // parse string to JSON
        if (typeof positions == 'string') {
            try {
                positions = JSON.parse(positions);
            } catch (error) {
                console.error('JSON parse error: ' + error);
                this.layout();
                return;
            }
        }

        attr = attr || 'id'; // default to id attribute
        this._resetLayout();
        // set item order and horizontal position from saved positions
        this.items = positions.map(function (itemPosition) {
            var selector = '[' + attr + '="' + itemPosition.id + '"]'
            var itemElem = this.element.querySelector(selector);
            var item = this.getItem(itemElem);
            item.rect.x = itemPosition.x * this.packer.width;
            return item;
        }, this);
        this.shiftLayout();
    };

    /*test slider*/
    angular.module('directive').directive('slideit', function () {
        return {
            restrict: 'A',
            replace: true,
            scope: {
                slideit: '=',
            },
            controller: function ($scope) {
                $scope.users = $scope.slideit;

            },
            template: '<ul id="frSlider" class="lista-donatori list-group bxslider">' +
                '<li ng-repeat="user in users">' +
                '<a class="list-group-item" ng-click="setInModifying(user.id)" title="{{user.nome}} {{user.cognome}}">' +
                '<div class="media">' +
                '<div class="media-left">' +
                '<img class="media-object img-circle img-avatar" ng-src="{{user.immagine.url}}" alt="...">' +
                '</div>' +
                '<div class="media-body">' +
                '<span class="media-title">{{user.nome}}' +
                '{{user.cognome}}</span>' +
                '<span class="media-content">{{user.categoriaFr.descrizione}}</span>' +
                '</div>' +
                '</div>' +
                '</a>' +
                '</li>' +
                '</ul>',
            link: function (scope, elm, attrs) {
                elm.bxSlider({
                    captions: true,
                    auto: true,
                    autoControls: true,
                    slideWidth: 110,
                    minSlides: 1,
                    maxSlides: 6,
                    moveSlides: 1,
                    slideMargin: 10,
                    pager: false,
                    autoHover: true
                });
            }
        };
    });


    angular.module("directive").directive('areasDashboard', function ($rootScope, $timeout) {
        var template = "<div  aweid=\"dashboard-container\" class=\"page-content\" style=\"width:100% ;height: 800px;\">";
        return {
            restrict: 'A',
            scope: {
                entryCount: "=areasDashboard"
            },
            link: function (scope, element, attrs) {

                $rootScope.areasDashboard = scope.entryCount;
                $rootScope.packeryEnd = false;
                if ($rootScope.packery.items.length == scope.entryCount) {
                    $rootScope.packeryEnd = true;
                }

                var resizeHandler = $rootScope.$watch('collapsed', function (old, newVal) {
                    if ($rootScope.packery) {
                        $timeout(function () {
                            if ($rootScope.packery) {
                                $rootScope.packery.resize();
                            }
                        }, 200);
                    }
                });
                scope.$on("$destroy", function (event) {
                    //distruggo l'handler
                    resizeHandler();

                    if ($rootScope.packery) {
                        console.log("packery destroy");
                        $rootScope.packery.destroy();
                    }
                    $rootScope.packery = undefined;

                    $timeout.cancel($rootScope.timer1);
                    $timeout.cancel($rootScope.timer2);
                    $timeout.cancel($rootScope.timer3);
                });
            }
        };
    });

    angular.module("directive").directive('areasDashboardList', [
        '$rootScope', '$timeout', 'configurationService',
        function ($rootScope, $timeout, configurationService) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var initPositions = localStorage.getItem('dashboardPositions');
                    if (!initPositions) {
                        configurationService.getDashboardPosition().then(function (res) {
                            if (res && res.length > 0) {
                                localStorage.setItem('dashboardPositions', JSON.stringify(res));
                                $rootScope.packery.initShiftLayout(res, 'id');
                            }
                        });
                    }
                }
            }
        }
    ]);


    angular.module("directive").directive('areasDashboardEntry', [
        '$rootScope', '$timeout', 'configurationService',
        function ($rootScope, $timeout, configurationService) {
            return {
                restrict: 'A',
                /*require: ['^areasDashboard'],*/

                link: function (scope, element, attrs) {
                    console.log("link called on", element[0]);


                    scope.$on("$destroy", function (event) {
                        //
                    });

                    if ($rootScope.packery === undefined || $rootScope.packery === null) {
                        scope.element = element;
                        $rootScope.packery = new Packery(element[0].parentElement, {
                            isResizeBound: true,
                            itemSelector: '.dashboard-entry',
                            initLayout: false // disable initial layout
                        });
                        $rootScope.packery.bindResize();
                        var dragEl = angular.element(element[0]).find(".draggable")[0] || element[0];

                        var draggable1 = new Draggabilly(dragEl, {
                            handle: '.panel-heading'
                        });
                        $rootScope.packery.bindDraggabillyEvents(draggable1);

                        draggable1.on('dragEnd', function (instance, event, pointer) {
                            $rootScope.timer1 = $timeout(function () {
                                if ($rootScope.packery) {
                                    $rootScope.packery.layout();
                                    // save drag positions
                                    var positions = $rootScope.packery.getShiftPositions('id');
                                    localStorage.setItem('dashboardPositions', JSON.stringify(positions));
                                    configurationService.saveDashboardPosition(positions).then(function (res) {});
                                }
                            }, 200);
                        });
                    } else {
                        // console.log("else", element[0]);
                        var dragEl = angular.element(element[0]).find(".draggable")[0] || element[0];
                        var draggable2 = new Draggabilly(dragEl, {
                            handle: '.panel-heading'
                        });
                        $rootScope.packery.bindDraggabillyEvents(draggable2);


                        draggable2.on('dragEnd', function (instance, event, pointer) {
                            $rootScope.timer2 = $timeout(function () {
                                if ($rootScope.packery) {
                                    $rootScope.packery.layout();
                                    // save drag positions
                                    var positions = $rootScope.packery.getShiftPositions('id');
                                    localStorage.setItem('dashboardPositions', JSON.stringify(positions));
                                    configurationService.saveDashboardPosition(positions).then(function (res) {});
                                }
                            }, 200);
                        });

                    }
                    // get saved dragged positions
                    var initPositions = localStorage.getItem('dashboardPositions');
                    // init layout with saved positions
                    $rootScope.packery.initShiftLayout(initPositions, 'id');
                    $rootScope.timer3 = $timeout(function () {
                        if ($rootScope.packery) {
                            //  $rootScope.packery.reloadItems();
                            $rootScope.packery.layout();
                        }
                    }, 100);
                }
            };
        }
    ]);
});