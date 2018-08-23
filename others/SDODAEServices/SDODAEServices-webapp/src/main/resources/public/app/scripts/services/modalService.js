'use strict';

define(['angular', "lodash", "leaflet"], function(angular, _, L) {

    angular.module("service").factory('modalService', ["$rootScope", "$document", "$uibModal", "$timeout", "toastr", function($rootScope, $document, $uibModal, $timeout, toastr) {
        var modalService = {
            errorModalOpen: false
        };

        var errorHandler = $rootScope.$on("application.error", function(ev, error) {
            /* if (!modalService.errorModalOpen) {
                 modalService.errorModalOpen = true;
                 modalService.openModal("errorModal.html", "Errore", {
                     message: {
                         title: "Errore imprevisto ",
                         text: "Errore imprevisto mentre si processava la richiesta lato server. Verificare i log lato server per capire la causa dell'errore"
                     }
                 }).closed.then(function() {
                     modalService.errorModalOpen = false;
                 });
             }*/
            toastr.error('Errore imprevisto mentre si processava la richiesta lato server.', 'Errore imprevisto');
        });

        modalService.openYesNoModal = function(data) {
            if (!_.isUndefined(data)) {
                var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                var modalInstance = $uibModal.open({
                    /* Prevengono l'uscita cliccando fuori oppure esc*/
                    backdrop: 'static',
                    keyboard: false,
                    animation: true,
                    templateUrl: "yesNoModal.html",
                    appendTo: parentElem,
                    controller: function($scope, $uibModalInstance) {
                        var $ctrl = this;
                        $scope.title = data.title;
                        $scope.text = data.text;
                        $scope.showOK = !!data.ok;
                        $scope.showCancel = !!data.cancel;
                    }

                });
                if (data.ok) {
                    modalInstance.result.then(data.ok);
                }
                if (data.cancel) {
                    modalInstance.result.catch(data.cancel);
                }
                return modalInstance;
            }

        };

        modalService.openModal = function(htmlTemplate, title, data) {
            if (htmlTemplate == "warningModal.html") {
                toastr.warning(data.message.text, data.message.title);
                return;
            } else if (htmlTemplate == "errorModal.html") {
                toastr.error(data.message.text, data.message.title);
                return;
            }

            var parentElem = angular.element($document[0].querySelector('.modal-attach'));
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: htmlTemplate,
                appendTo: parentElem,
                controller: function($scope, $uibModalInstance) {
                    var $ctrl = this;
                    $scope.title = title;
                    if (!_.isUndefined(data)) {
                        if (data.message) {
                            $scope.message = data.message;
                        }
                    }
                }
            });
            return modalInstance;
        };

        /*
        options: {
          title: "String",
          markers: [array],
          events[array],
          bounds: object,
          controls: object,
          layers: object,
          loadCluster : function(bounds, markerClusterLevel)
        }
        */
        modalService.openMapModal = function(options) {
            var parentElem = angular.element($document[0].querySelector('.modal-attach'));
            var modalScope;

            if (!options.layers) {
                options.layers = {};
            }

            options.layers.baselayers = {
                osm: {
                    name: 'OpenStreetMap',
                    url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                    type: 'xyz',
                    layerParams: {
                        showOnSelector: false
                    }
                }
            };

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "mapDaeModal.html",
                //  template: modalTemplateMap,
                appendTo: parentElem,
                controller: function($scope, $uibModalInstance, leafletData) {
                    modalScope = $scope;
                    $scope.save = options.save;
                    $scope.title = options.title;
                    $scope.markers = options.markers;
                    $scope.layers = options.layers;
                    $scope.enableEdit = options.enableEdit;
                    if (!options.controls) {
                        options.controls = [];
                    }

                    options.controls.push(
                        L.Control.boxzoom({
                            position: 'topleft'
                        })
                    );

                    if (options.bounds) {
                        //se ho i bounds non metto il center e viceversa
                        $scope.daeBounds = options.bounds;
                    }

                    if ($scope.markers && $scope.markers.length > 0) {
                        $scope.center = {
                            lat: $scope.markers[0].lat,
                            lng: $scope.markers[0].lng,
                            zoom: 12
                        };
                    } else {
                        $scope.center = {
                            lat: 44,
                            lng: 11,
                            zoom: 5
                        };
                    }

                    $scope.events = {
                        map: {
                            enable: options.events,
                            logic: 'broadcast'
                        }
                    };

                    //livello per il clustering dei marker
                    var markerClusterLevel = L.markerClusterGroup();
                    leafletData.getMap().then(function(map) {
                        map.addLayer(markerClusterLevel);

                        options.controls.forEach(function(c) {
                            map.addControl(c);
                        });

                        if (options.editLayer) {
                            map.addLayer(options.editLayer);
                        }
                    });

                    var handlerMove = $rootScope.$on('leafletDirectiveMap.moveend', function(e, c) {
                        options.loadCluster(c.leafletEvent.target.getBounds(), markerClusterLevel);
                    });

                    var handlerCreate = $rootScope.$on('leafletDirectiveMap.' + L.Draw.Event.CREATED, function(event, c) {
                        $scope.drawLayer = c.leafletEvent.layer;
                        $scope.drawLayers = c.leafletEvent.layers;
                        options.editLayer.addLayer(c.leafletEvent.layer);
                    });
                    var handlerEdited = $rootScope.$on('leafletDirectiveMap.' + L.Draw.Event.EDITED, function(event, c) {
                        $scope.drawLayer = c.leafletEvent.layer;
                        $scope.drawLayers = c.leafletEvent.layers;
                    });

                    $scope.close = function() {
                        $uibModalInstance.close();
                    };

                    $scope.find = function() {
                        $uibModalInstance.close({
                            layer: $scope.drawLayer,
                            layers: $scope.drawLayers
                        });
                    };

                    $scope.clean = function() {
                        if (options.editLayer) {
                            options.editLayer.clearLayers();
                        }
                        $scope.drawLayer = null;
                        $scope.drawLayers = null;
                    };

                    $uibModalInstance.closed.then(function() {
                        //distruggo l'handler
                        handlerCreate();
                        handlerEdited();
                        handlerMove();
                    });
                }
            });

            modalInstance.rendered.then(function() {
                if (options.bounds) {
                    //imposto i bounds dopo il rendering della mappa,
                    //perchè impostarli prima che la mappa sia visibile non da nessun effetto
                    modalScope.daeBounds = options.bounds;
                }
            });
            return modalInstance;
        };

        modalService.openNotifyModal = function(title, frId, notificationService) {
            var parentElem = angular.element($document[0].querySelector('.modal-attach'));
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "notifyModal.html",
                appendTo: parentElem,
                controller: function($scope, $uibModalInstance) {
                    var $ctrl = this;
                    $scope.title = title;
                    $scope.frId = frId;
                    $scope.send = function() {
                        var filter = {
                            id: $scope.frId
                        };
                        notificationService.sendNotificationByFilter($scope.filter.messaggio, filter).then(function(result) {
                            $scope.$dismiss('cancel');
                            console.log('Messaggio inviato ' + result);
                        });
                    };
                }
            });
            return modalInstance;
        };

        modalService.openMailModal = function(title, filter, anagService) {
            var parentElem = angular.element($document[0].querySelector('.modal-attach'));
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "mailModal.html",
                appendTo: parentElem,
                controller: function($scope, $uibModalInstance) {
                    var $ctrl = this;
                    $scope.title = title;
                    $scope.entities = ["DAE", "USER"];
                    $scope.filter = filter;
                    $scope.send = function() {
                        anagService.sendMail($scope.filter).then(function(result) {
                            $scope.$dismiss('cancel');
                            console.log('Mail inviato ' + result);
                        });
                    };
                }
            });
            return modalInstance;
        };

        //prende in input il titolo della modale, il testo da editare e una callback che restituisce una promise
        modalService.openWYSWYGModal = function(title, object, text, callback) {
            var parentElem = angular.element($document[0].querySelector('.modal-attach'));
            var modalScope;
            var modalInstance = $uibModal.open({
                animation: true,
                /* Prevengono l'uscita cliccando fuori oppure esc*/
                backdrop: 'static',
                keyboard: false,
                /**/
                templateUrl: "wyswyfgModal.html",
                appendTo: parentElem,
                controller: function($scope, $uibModalInstance) {
                    var $ctrl = this;
                    modalScope = $scope;
                    $scope.title = title;
                    $scope.object = object;
                    $scope.template = {
                        data: text
                    };
                    $scope.save = function() {
                        var promise = callback({
                            data: $scope.template.data,
                            object: $scope.object
                        });
                        if (promise) {
                            promise.then(function() {
                                $scope.$dismiss('cancel');
                            });
                        }
                    };
                }
            });
            return modalInstance;
        };

        return modalService;
    }]);
});
