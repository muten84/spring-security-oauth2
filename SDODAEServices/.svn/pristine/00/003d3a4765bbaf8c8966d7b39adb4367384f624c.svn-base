'use strict';

define(['angular', "moment", "constants",  "leaflet"], function (angular, moment, constants, L) {
    angular.module("controller").controller('DaeMapCtrl', [
        "$rootScope", "$scope", "daeService",
        "leafletBoundsHelpers", "validateService", "$window",
        "leafletData", "utilityService",
        function ($rootScope, $scope, daeService,
            leafletService, validateService, $window,
            leafletData, utilityService) {
            $rootScope.$broadcast("main.changeTitle", {
                title: "Mappa DAE",
                icon: "fa fa-map-marker"
            });

            $scope.daeBounds = [];
            $scope.center = {
                //lat: 44.491607329696045,
                //  lng: 11.419944763183594,
                //    zoom: 7
            };
            $scope.daeMarkers = [];

            $scope.mapHeight = ($window.innerHeight * 70) / 100;

            $scope.daeControls = {
                custom: [L.Control.boxzoom({
                    position: 'topleft'
                })]
            };

            var markerClusterLevel = L.markerClusterGroup();
            leafletData.getMap().then(function (map) {
                map.addLayer(markerClusterLevel);
            });

            $scope.layers = {

                baselayers: {
                    osm: {
                        name: 'OpenStreetMap',
                        url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                        type: 'xyz',
                        layerParams: {
                            showOnSelector: false
                        }
                    }
                }
            };

            var handlerMove = $rootScope.$on('leafletDirectiveMap.moveend', function (e, c) {
                utilityService.loadDAE(c.leafletEvent.target.getBounds(), markerClusterLevel, $scope);
            });

            $scope.$on("$destroy", function () {
                handlerMove();
            });

            $scope.center = {
                /*44,491607329696045
                11,419944763183594*/
                lat: 44.491607329696045,
                lng: 11.419944763183594,
                zoom: 7
            };
        }
    ]);
});
