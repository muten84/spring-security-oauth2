appControllers.controller('AlertDaeListCtrl', [
    '$rootScope',
    '$scope',
    '$state',
    'loggerService',
    'NgMap',
    'apiService',
    'userService',
    '$timeout',
    'deviceService',
    '$stateParams',
    '$cordovaGeolocation',
    function ($rootScope, $scope, $state, logger, NgMap, api, user, $timeout, device, $stateParams, $cordovaGeolocation) {
        logger.info("Caricamento AlertDaeListCtrl...");

        var vm = this;
        vm.showDae = showDae;
        vm.showEvent = showEvent;
        vm.goToState = goToState;
        vm.openMap = openMap;
        vm.getDAEForList = getDAEForList;

        vm.map = null;
        vm.gmUrl = $rootScope.config.GoogleMapsUrl;
        vm.localizedDae = [];
        vm.daeList = [];

        var firstIndex = 0;
        var location = {};

        $scope.$on('$ionicView.enter', function () {
            vm.noMoreDaeAvailable = false;
            vm.selectedIndex = 0;
            vm.localizedDae = [];
            vm.daeList = [];
            vm.event = $stateParams.event;
            location.lat = vm.event.coordinate.latitudine;
            location.lng = vm.event.coordinate.longitudine;
            vm.currentPosition = location;
            vm.pauseLoading=false;

            checkCurrentLocation().then(
                function () {
                    if (vm.selectedIndex === 0){
                        getDAEForList();
                    }
                    else{
                        getDAEForMap();
                    }
                },
                function (error) {
                    device.hideLoader();
                    device.alert("Connessione dati o GPS non attivi", "balanced");
                }
            );

            // NgMap.getMap('customMap').then(
            //     function (map) {
            //
            //
            //         vm.map = map;
            //         if (vm.selectedIndex === 0){
            //             getDAEForList();
            //         }
            //         else{
            //             getDAEForMap();
            //         }
            //     },
            //     function (error) {
            //         device.hideLoader();
            //         logger.info(error);
            //     }
            // );
        });

        $scope.$watch('vm.selectedIndex', function (current, old) {
            logger.info("current: " + current);
            logger.info("old: " + old);
            if (current === 1) {
                if (vm.localizedDae.length === 0){
                    getDAEForMap();
                }
            }
            if (current === 0) {
                if (vm.daeList.length === 0){

                }

            }
        });

        function getDAEForList(){
            device.showLoader();
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
                            if (dae.disponibilita && dae.disponibilita.length > 0) {
                                dae.disponibilita.isAvailable = checkAvailability(dae.disponibilita);
                            }
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
                    device.alert("Servizio momentaneamente non disponibile", "balanced").then(
                        function(){
                            $state.go("app.home");
                        }
                    );
                    logger.info(error);
                }
            );
        }

        function getDAEForMap(){
            device.showLoader();
            api.searchDAEByFilter(vm.currentPosition).then(
                function (data) {
                    device.hideLoader();
                    var daeMap = data;
                    var n = 0;
                    for (var i = 0; i < daeMap.length; i++) {
                        var dae = daeMap[i];
                        if (dae.gpsLocation) {
                            dae.icon = {};
                            if (dae.disponibilita && dae.disponibilita.length > 0) {
                                dae.disponibilita.isAvailable = checkAvailability(dae.disponibilita);
                                if (dae.disponibilita.isAvailable) {
                                    dae.icon.url = "img/dae_active.png";
                                    dae.icon.size = [60, 60];
                                    dae.icon.scaledSize = [50, 50];
                                    dae.icon.origin = [0, 0];
                                    dae.icon.anchor = [25, 50];
                                }
                                else {
                                    dae.icon.url = "img/dae_inactive.png";
                                    dae.icon.size = [60, 60];
                                    dae.icon.scaledSize = [50, 50];
                                    dae.icon.origin = [0, 0];
                                    dae.icon.anchor = [25, 50];
                                }
                            }
                            else {
                                dae.icon.url = "img/dae_inactive.png";
                                dae.icon.size = [60, 60];
                                dae.icon.scaledSize = [50, 50];
                                dae.icon.origin = [0, 0];
                                dae.icon.anchor = [25, 50];
                            }
                            vm.localizedDae.push(dae);
                            n++;
                        }
                    }
                },
                function (error) {
                    device.hideLoader();
                    device.alert("Servizio momentaneamente non disponibile", "balanced").then(
                        function(){
                            $state.go("app.home");
                        }
                    );
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
                logger.info("Dal", moment(data[i].da).format("DD/MM/YYYY"));
                logger.info("Al", moment(data[i].a).format("DD/MM/YYYY"));
                var fromDate = moment(data[i].da);
                var toDate = moment(data[i].a);
                var isInRange = moment().isBetween(fromDate, toDate);
                if (isInRange) {
                    logger.info("Dalle", data[i].orarioDa);
                    logger.info("Alle", data[i].orarioA);
                    var fromTime = moment(data[i].orarioDa, "HH:mm");
                    var toTime = moment(data[i].orarioA, "HH:mm");
                    isAvailable = moment().isBetween(fromTime, toTime);
                    logger.info("Attivo", isAvailable);
                }
                if (isAvailable) {
                    return isAvailable;
                }
            }
        }

        function checkCurrentLocation() {
            var q = $q.defer();
            var posOptions = {
                timeout: 20000,
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
                                    NgMap.getMap('customMap').then(
                                        function (map) {
                                            var bounds = new google.maps.LatLngBounds();
                                            var eventLatLng = new google.maps.LatLng(vm.event.coordinate.latitudine, vm.event.coordinate.longitudine);
                                            bounds.extend(eventLatLng);
                                            var currentLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                                            bounds.extend(currentLatLng);
                                            map.setCenter(bounds.getCenter());
                                            map.fitBounds(bounds);
                                            vm.map = map;
                                            q.resolve();
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
                        $timeout(function() {
                            vm.pauseLoading=false;
                            NgMap.getMap('customMap').then(
                                function (map) {
                                    var bounds = new google.maps.LatLngBounds();
                                    var eventLatLng = new google.maps.LatLng(vm.event.coordinate.latitudine, vm.event.coordinate.longitudine);
                                    bounds.extend(eventLatLng);
                                    var currentLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                                    bounds.extend(currentLatLng);
                                    map.setCenter(bounds.getCenter());
                                    map.fitBounds(bounds);
                                    vm.map = map;
                                    q.resolve();
                                },
                                function (error) {
                                    logger.info(error);
                                    q.reject(error);
                                }
                            );
                        }, 2000);

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

        function goToState(state, dae) {
            $state.go(state, {dae: dae});
        }

        function showDae(event, id) {
            vm.dae = vm.localizedDae[id];
            vm.map.showInfoWindow('marker-info', this);
        }

        function showEvent(event, info) {
            vm.event = info;
            vm.map.showInfoWindow('event-info', this);
        }

        function openMap(lat, long) {
            var targetDestinationLocation = lat+","+long;
            if (device.isAndroid()) {
                window.open('geo:?q=' + targetDestinationLocation + '&z=15', '_system');
            }
            else{
                window.open('maps://?q=' + targetDestinationLocation, '_system');
            }
        }

    }]);