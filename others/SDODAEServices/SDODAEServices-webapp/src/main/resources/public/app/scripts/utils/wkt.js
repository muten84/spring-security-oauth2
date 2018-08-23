'use strict';


define(["leaflet"], function (L) {
    return function (layer) {
        var lng, lat, coords = [];
        if (layer instanceof L.Polygon || layer instanceof L.Polyline) {
            var latlngs = layer.getLatLngs();
            latlngs[0].forEach(function (ll, i) {
                coords.push(ll.lng + " " + ll.lat);
                if (i === 0) {
                    lng = ll.lng;
                    lat = ll.lat;
                }
            });

            if (layer instanceof L.Polygon) {
                return "POLYGON((" + coords.join(",") + "," + lng + " " + lat + "))";
            } else if (layer instanceof L.Polyline) {
                return "LINESTRING(" + coords.join(",") + ")";
            }
        } else if (layer instanceof L.Marker) {
            return "POINT(" + layer.getLatLng().lng + " " + layer.getLatLng().lat + ")";
        }
    };
});
