'use strict';
/*
questa direttiva può essere utile quando si vuole gestire un comportamento generico su tutte le ui-select*/ 
define(["angular"], function (angular) {
    angular.module('directive').directive('validateOnBlur', function () {
        return {
            require: 'uiSelect',
            link: function ($scope, $element, attrs, $select) {
                var searchInput = $element.querySelectorAll('input.ui-select-search');
                if (searchInput.length !== 1) throw Error("bla");

                searchInput.on('blur', function () {
                    debugger;
                    $scope.$apply(function () {
                        var item = $select.items[$select.activeIndex];
                        /*$select.select(item);*/
                    });
                });

            }
        }
    });
});
