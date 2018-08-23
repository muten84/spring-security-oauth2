'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(
    ['angular', "lodash", "constants", "leaflet"],
    function (angular, _, constants, L) {
        angular.module("service").factory('utilityService', [
            "$http", "loggerService", "$q",
            "$location", 'abstractSerivce', 'toastr',
            "daeService",
            function ($http, logger, $q,
                $location, abstractSerivce, toastr,
                daeService) {
                var api = {
                    //funzione per generare i tooltip
                    daeTooltip: function (dae) {
                        var msg = "<b>" + (dae.nomeSede ? dae.nomeSede : "") + "</b>";
                        msg += "<br/><b>Indirizzo</b><br/>";
                        msg += " " + (dae.gpsLocation.indirizzo ? dae.gpsLocation.indirizzo.name : " ");
                        msg += "<br/>";
                        msg += " " + (dae.gpsLocation.comune ? dae.gpsLocation.comune.nomeComune : " ");
                        msg += "<br/><b>Ubicazione</b><br/>";
                        msg += " " + (dae.ubicazione ? dae.ubicazione : "");
                        msg += "<br/>";

                        return msg;
                    },
                    loadDAE: function (bounds, markerClusterLevel, scope) {
                        var geoJSON = L.rectangle(bounds).toGeoJSON();
                        var self = this;
                        if (scope.lastBounds && scope.lastBounds.contains(bounds)) {
                            //se l'area passata è contenuta nell'ultima area caricata non faccio nulla
                            return;
                        }

                        scope.lastReq = daeService.searchDAEByFilter({
                            fetchRule: 'MINIMAL',
                            location: {
                                srid: 4326,
                                geoJSON: JSON.stringify(geoJSON.geometry)
                            }
                        });

                        scope.lastReq.then(function (data) {
                            //altrimenti carico il tutto
                            scope.lastBounds = bounds;
                            //azzero l'ultima richiesta
                            scope.lastReq = null;
                            //tolgo i markers già presenti
                            markerClusterLevel.clearLayers();
                            // markers.concat(markersBK);
                            data.forEach(function (dae) {
                                var msg = self.daeTooltip(dae);
                                var icon ;
                                if(dae.disponibile){
                                	icon = constants.daeMapIcon;
                                } else if (dae.disponibilitaIndefinita){
                                	icon = constants.daeMapIconUndefined;
                                } else {
                                	icon = constants.daeMapIconNotAvailable;
                                }
                                var marker = L.marker(new L.LatLng(dae.gpsLocation.latitudine, dae.gpsLocation.longitudine), {
                                    icon: L.icon(icon),
                                    zIndexOffset: 1,
                              /*      title: msg*/
                                });
                                //aggiungo i nuovi markerss
                                markerClusterLevel.addLayer(marker);
                                marker.bindPopup(msg);
                            });
                        });
                    }
                };
                return api;
            }
        ]);
    });
