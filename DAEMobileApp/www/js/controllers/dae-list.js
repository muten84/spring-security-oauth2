appControllers.controller('DaeListCtrl', [
    '$rootScope',
    '$scope',
    '$q',
    '$state',
    'loggerService',
    'NgMap',
    'apiService',
    'userService',
    '$timeout',
    'deviceService',
    '$cordovaGeolocation',
    '$stateParams',
    '$ionicHistory',
    'localStorageService',
    function ($rootScope, $scope, $q, $state, logger, NgMap, api, user, $timeout, device, $cordovaGeolocation, $stateParams, $ionicHistory, storage) {
        logger.info("Caricamento DaeListCtrl...");

        var vm = this;
        vm.showInfoWindowFromMarker = showInfoWindowFromMarker;
        vm.showEvent = showEvent;
        vm.goToState = goToState;
        vm.goBack = goBack;
        vm.openMap = openMap;
        vm.getDAEForList = getDAEForList;
        vm.centerMap = centerMap;
        vm.dragEnd = dragEnd;
        vm.zoomChanged = zoomChanged;
        vm.onIdle = onIdle;

        vm.map = null;
        vm.gmUrl = $rootScope.config.GoogleMapsUrl;
        vm.localizedDae = [];
        vm.daeList = [];
        vm.tabId = null;

        var zoomChanged = false;
        var showInfoWindow = false;
        var firstIndex = 0;
        var location = {};

        $scope.$on('$ionicView.loaded', function () {
            vm.mapState = storage.get('mapState');
            if (vm.mapState) {
                storage.remove('mapState');
                var tabId = vm.mapState.tabId | 0;
                vm.selectedIndex = tabId;
            }
            else {
                vm.selectedIndex = 0;
            }
            vm.event = $stateParams.event;
        });

        $scope.$on('$ionicView.enter', function () {
            logger.info('$ionicView.enter');
        });

        function initializeMap() {
            if (vm.event) {
                var bounds = new google.maps.LatLngBounds();
                var eventLatLng = new google.maps.LatLng(vm.event.coordinate.latitudine, vm.event.coordinate.longitudine);
                bounds.extend(eventLatLng);
                var currentLatLng = new google.maps.LatLng(location.lat, location.lng);
                bounds.extend(currentLatLng);
                vm.map.setCenter(bounds.getCenter());
                vm.map.fitBounds(bounds);
            }
            else {
                if (vm.mapState) {
                    vm.map.setZoom(vm.mapState.zoomLevel);
                    vm.map.setCenter(vm.mapState.center);
                }
                else {
                    vm.map.setZoom(14);
                }
                // vm.map.setZoom(14);
            }
            getDAEForMap();
        }

        $scope.$watch('vm.selectedIndex', function (current, old) {
            device.showLoader();
            logger.info('$scope.$watch - vm.selectedIndex');
            logger.info("current: " + current);
            logger.info("old: " + old);

            if (angular.isDefined(current)) {
                vm.noMoreDaeAvailable = false;
                if (current === 1) {
                    vm.daeList = [];
                    checkCurrentLocation().then(
                        function (location) {
                            if (location) {
                                currentPositionMarker(location);
                            }
                            initializeMap();
                        },
                        function (error) {
                            device.hideLoader();
                            if (vm.event.currentPosition && vm.map) {
                                initializeMap();
                            }
                            else {
                                device.alert("Connessione dati o GPS non attivi", "balanced").then(
                                    function () {
                                        $ionicHistory.nextViewOptions({
                                            disableBack: true
                                        });
                                        $state.go("app.home");
                                    }
                                );
                            }
                        }
                    );
                }
                if (current === 0) {
                    firstIndex = 0;
                    checkCurrentLocation().then(
                        function (location) {
                            vm.currentPosition = location;
                            getDAEForList();
                        },
                        function (error) {
                            device.hideLoader();
                            if (vm.event && vm.event.currentPosition) {
                                vm.currentPosition = vm.event.currentPosition;
                                getDAEForList();
                            }
                            else {
                                device.alert("Connessione dati o GPS non attivi", "balanced").then(
                                    function () {
                                        $ionicHistory.nextViewOptions({
                                            disableBack: true
                                        });
                                        $state.go("app.home");
                                    }
                                );
                            }
                        }
                    );
                }
            }
        });

        function checkCurrentLocation() {
            var q = $q.defer();
            var posOptions = {
                timeout: 10000,
                enableHighAccuracy: true,
                maximumAge: 0
            };
            if (device.isCordova()) {
                device.checkIsLocationAvailable().then(
                    function (status) {
                        if (status) {
                            $cordovaGeolocation
                                .getCurrentPosition(posOptions)
                                .then(function (position) {
                                    logger.info(position);
                                    location.lat = position.coords.latitude;
                                    location.lng = position.coords.longitude;
                                    //vm.currentPosition = location;
                                    // $timeout(function () {
                                    vm.pauseLoading = false;
                                    NgMap.getMap('customMap').then(
                                        function (map) {
                                            vm.map = map;
                                            q.resolve(location);
                                        },
                                        function (error) {
                                            logger.info(error);
                                            q.reject(error);
                                        }
                                    );
                                    // }, 2000);
                                }, function (error) {
                                    NgMap.getMap('customMap').then(
                                        function (map) {
                                            vm.map = map;
                                            q.resolve();
                                        },
                                        function (error) {
                                            logger.info(error);
                                            q.reject(error);
                                        }
                                    );
                                    logger.error(error);
                                });
                        }
                        else {
                            q.reject("Connessione dati o GPS non attivi");
                        }
                    },
                    function (error) {
                        q.reject(error);
                    }
                );
            }
            else {
                $cordovaGeolocation
                    .getCurrentPosition(posOptions)
                    .then(function (position) {
                        logger.info(position);
                        location.lat = position.coords.latitude;
                        location.lng = position.coords.longitude;
                        // vm.currentPosition = location;
                        vm.pauseLoading = false;
                        NgMap.getMap('customMap').then(
                            function (map) {
                                vm.map = map;
                                q.resolve(location);
                            },
                            function (error) {
                                logger.info(error);
                                q.reject(error);
                            }
                        );
                    }, function (error) {
                        NgMap.getMap('customMap').then(
                            function (map) {
                                vm.map = map;
                                q.resolve();
                            },
                            function (error) {
                                logger.info(error);
                                q.reject(error);
                            }
                        );
                        logger.error(error);
                    });
            }
            return q.promise;
        }

        function getDAEForMap() {
            device.showLoader();
            vm.localizedDae = [];
            var bounds = vm.map.getBounds();
            var geoJSON = getGeoJSONString(bounds);
            logger.info("--getDAEForMap--geoJSON--", geoJSON);
            api.searchDAEByGeoJSON(geoJSON).then(
                function (data) {
                    if (data.length > 0) {
                        var daeMap = [];
                        for (var i = 0; i < data.length; i++) {

                            var dae = data[i];
                            if (dae.gpsLocation) {
                                dae.icon = {};
                                if (dae.disponibilitaIndefinita) {
                                    dae.icon.url = "img/dae_map_undefined.png";
                                    dae.icon.size = [60, 60];
                                    dae.icon.scaledSize = [45, 50];
                                    dae.icon.origin = [0, 0];
                                    dae.icon.anchor = [25, 50];

                                } else {
                                    if (dae.disponibile) {
                                        dae.icon.url = "img/dae_active.png";
                                        dae.icon.size = [60, 60];
                                        dae.icon.scaledSize = [50, 50];
                                        dae.icon.origin = [0, 0];
                                        dae.icon.anchor = [25, 50];
                                    }
                                    else {
                                        dae.icon.url = "img/dae_inactive.png";
                                        dae.icon.size = [60, 60];
                                        dae.icon.scaledSize = [45, 50];
                                        dae.icon.origin = [0, 0];
                                        dae.icon.anchor = [25, 50];
                                    }
                                }
                                // }
                                // else {
                                //     dae.icon.url = "img/dae_inactive.png";
                                //     dae.icon.size = [60, 60];
                                //     dae.icon.scaledSize = [45, 50];
                                //     dae.icon.origin = [0, 0];
                                //     dae.icon.anchor = [25, 50];
                                // }
                                daeMap.push(dae);
                            }
                        }
                        $timeout(function () {
                            device.hideLoader();
                            vm.localizedDae = checkMarkerWithSamePosition(daeMap);
                            initMarkerClusterer();
                        }, 500);
                    }
                    else {
                        device.hideLoader();
                    }

                },
                function (error) {
                    device.hideLoader();
                    if (error.status != 401) {
                        device.alert("Servizio momentaneamente non disponibile", "balanced").then(
                            function () {
                                $state.go("app.home");
                            }
                        );
                    }
                    logger.info(error);
                }
            );
        }

        function checkMarkerWithSamePosition(markers) {
            for (var i = 0; i < markers.length; i++) {
                var dae = markers[i];
                var lat = dae.gpsLocation.latitudine;
                var lng = dae.gpsLocation.longitudine;
                for (var n = i + 1; n < markers.length; n++) {
                    if (lat === markers[n].gpsLocation.latitudine && lng === markers[n].gpsLocation.longitudine) {
                        var a = 360.0 / markers.length;
                        markers[n].gpsLocation.latitudine = markers[n].gpsLocation.latitudine + -.00004 * Math.cos((+a * n) / 180 * Math.PI);
                        markers[n].gpsLocation.longitudine = markers[n].gpsLocation.longitudine + -.00004 * Math.sin((+a * n) / 180 * Math.PI);
                    }
                }
            }
            return markers;
        }

        function initMarkerClusterer() {
            if (vm.markerClusterer) {
                vm.markerClusterer.clearMarkers();
            }
            vm.markers = vm.localizedDae.map(function (dae) {
                return createMarker(dae);
            });
            var mcOptions = {
                imagePath: 'img/map/map-cluster',
                imageExtension: 'svg',
                maxZoom: 15,
                zoomOnClick: true,
                gridSize: 80
            };
            vm.markerClusterer = new MarkerClusterer(vm.map, vm.markers, mcOptions);
            return vm.markerClusterer;
        }

        function createMarker(dae) {
            var image = {
                url: dae.icon.url,
                // This marker is 20 pixels wide by 32 pixels high.
                size: new google.maps.Size(dae.icon.size[0], dae.icon.size[1]),
                scaledSize: new google.maps.Size(dae.icon.scaledSize[0], dae.icon.scaledSize[1]),
                // The origin for this image is (0, 0).
                origin: new google.maps.Point(dae.icon.origin[0], dae.icon.origin[1]),
                // The anchor for this image is the base of the flagpole at (0, 32).
                anchor: new google.maps.Point(dae.icon.anchor[0], dae.icon.anchor[1])
            };
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(dae.gpsLocation.latitudine, dae.gpsLocation.longitudine),
                icon: image
            });
            google.maps.event.addListener(marker, 'click', function () {
                vm.selectedDae = dae;
                showInfoWindow = true;
                vm.map.showInfoWindow('marker-info', this);
            });
            return marker;
        }

        function currentPositionMarker(location) {
            if (vm.cpMarker) {
                vm.cpMarker.setMap(null);
            }
            var image = {
                url: 'img/localize.png',
                scaledSize: new google.maps.Size(30, 30)
            };
            vm.cpMarker = new google.maps.Marker({
                position: new google.maps.LatLng(location.lat, location.lng),
                icon: image,
                map: vm.map,
                animation: google.maps.Animation.DROP
            });
        }

        function getDAEForList() {
            var pageSize = 6;
            api.searchDAEByFilter(vm.currentPosition, pageSize, firstIndex).then(
                function (data) {
                    device.hideLoader();
                    var allDae = data;
                    var n = 0;
                    for (var i = 0; i < allDae.length; i++) {
                        var dae = allDae[i];
                        if (dae.gpsLocation) {
                            dae.icon = {};
                            vm.daeList.push(dae);
                            n++;
                        }
                    }
                    if (data.length < pageSize) {
                        vm.noMoreDaeAvailable = true;
                    }
                    else {
                        firstIndex = firstIndex + data.length;
                    }
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                },
                function (error) {
                    device.hideLoader();
                    if (error.status != 401) {
                        device.alert("Servizio momentaneamente non disponibile", "balanced").then(
                            function () {
                                $state.go("app.home");
                            }
                        );
                    }
                    logger.info(error);
                }
            );
        }

        /**
         * Controlla la disponibilità del DAE per visualizzare icona o marker differenti
         **/
        function checkAvailability(data) {
            var isAvailable = false;
            for (var i = 0; i < data.length; i++) {
                var fromDate = moment(data[i].da);
                var toDate = moment(data[i].a);
                var isInRange = moment().isBetween(fromDate, toDate);
                if (isInRange) {
                    if (data[i].disponibilitaSpecifica) {
                        var today = moment().format('ddd').toUpperCase();
                        var day = _.find(data[i].disponibilitaSpecifica.disponibilitaGiornaliera, function (o) {
                            return o.giornoSettimana.indexOf(today) != -1;
                        });
                        if (day) {
                            var fromTime = moment(day.orarioDa, "HH:mm");
                            var toTime = moment(day.orarioA, "HH:mm");
                            isAvailable = moment().isBetween(fromTime, toTime);
                        }
                    }
                    else {
                        isAvailable = isInRange;
                    }

                }
                if (isAvailable) {
                    return isAvailable;
                }
            }
        }

        function goToState(state, dae) {
            if (dae) {
                var mapState = {};
                mapState.tabId = vm.selectedIndex;
                mapState.center = vm.map.getCenter();
                mapState.zoomLevel = vm.map.getZoom();
                storage.set('mapState', mapState);
                $state.go(state, { dae: dae });
            }
            else {
                $state.go(state);
            }
        }

        function goBack() {
            $state.go("app.home");
        }

        function centerMap() {
            device.showLoader();
            checkCurrentLocation().then(
                function (location) {
                    logger.info(JSON.stringify(location));
                    vm.lat = location.lat;
                    vm.lng = location.lng;
                    var currentPosition = new google.maps.LatLng(location.lat, location.lng);
                    device.hideLoader();
                    vm.map.setCenter(currentPosition);
                    currentPositionMarker(location);
                },
                function (error) {
                    device.hideLoader();
                    device.alert("Connessione dati o GPS non attivi", "balanced").then(
                        function () {
                            $ionicHistory.nextViewOptions({
                                disableBack: true
                            });
                            $state.go("app.home");
                        }
                    );
                }
            );
        }

        function dragEnd() {
            logger.info("--dragEnd--");
            // getDAEForMap()
        }

        function zoomChanged() {
            // zoomChanged = true;
            logger.info("--zoomChanged--", vm.map.getZoom());
            // if (vm.map) {
            //     if (!showInfoWindow) {
            //         getDAEForMap();
            //     }
            zoomChanged = false;
            // showInfoWindow = false;
            // }
            //getDAEForMap()
        }

        function onIdle() {
            logger.info("--onIdle--");
            if (vm.map) {
                if (!showInfoWindow) {
                    getDAEForMap();
                }
                zoomChanged = false;
                showInfoWindow = false;
            }
        }

        function getGeoJSONString(bounds) {
            vm.neLat = bounds.getNorthEast().lat();
            vm.neLng = bounds.getNorthEast().lng();
            vm.swLat = bounds.getSouthWest().lat();
            vm.swLng = bounds.getSouthWest().lng();
            vm.nwLat = vm.neLat;
            vm.nwLng = vm.swLng;
            vm.seLat = vm.swLat;
            vm.seLng = vm.neLng;
            var geoJSONString = {
                type: "Polygon",
                coordinates: [
                    [
                        [
                            vm.nwLng,
                            vm.nwLat
                        ],
                        [
                            vm.neLng,
                            vm.neLat
                        ],
                        [
                            vm.seLng,
                            vm.seLat
                        ],
                        [
                            vm.swLng,
                            vm.swLat
                        ],
                        [
                            vm.nwLng,
                            vm.nwLat
                        ]
                    ]
                ]
            };
            logger.info("geoJSONString: ", geoJSONString);
            return JSON.stringify(geoJSONString);
        }

        function showInfoWindow() {
            showInfoWindow = true;
            vm.map.showInfoWindow('marker-info', this);
        }

        function showInfoWindowFromMarker(event, id) {
            showInfoWindow = true;
            if (id != null) {
                vm.dae = vm.localizedDae[id];
                vm.map.showInfoWindow('marker-info', this);
            }
            else {
                vm.map.showInfoWindow('event-info', this);
            }
        }

        function showEvent(event, info) {
            showInfoWindow = true;
            //vm.event = info;
            vm.map.showInfoWindow('event-info', this);
        }

        function openMap(lat, long) {
            var targetDestinationLocation = lat + "," + long;
            if (device.isAndroid()) {
                window.open('geo:?q=' + targetDestinationLocation + '&z=15', '_system');
            }
            else {
                window.open('maps://?q=' + targetDestinationLocation, '_system');
            }
        }

        // if (allMarkers.length != 0) {
        //     for (i=0; i < allMarkers.length; i++) {
        //         var existingMarker = allMarkers[i];
        //         var pos = existingMarker.getPosition();
        //
        //if a marker already exists in the same position as this marker
        // if (latlng.equals(pos)) {
        //update the position of the coincident marker by applying a small multipler to its coordinates
        // var newLat = latlng.lat() + (Math.random() -.5) / 1500;// * (Math.random() * (max - min) + min);
        // var newLng = latlng.lng() + (Math.random() -.5) / 1500;// * (Math.random() * (max - min) + min);
        // finalLatLng = new google.maps.LatLng(newLat,newLng);
        // }
        // }
        // }
    }]);