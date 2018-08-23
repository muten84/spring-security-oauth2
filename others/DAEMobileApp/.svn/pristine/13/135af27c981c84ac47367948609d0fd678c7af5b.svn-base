appServices.factory('googleMapsFactory', [
    '$rootScope',
    '$q',
    function ($rootScope, $q) {
        var deferred = $q.defer();
        if (typeof window.google !== 'undefined' && typeof window.google.maps !== 'undefined') {
            logger.info('yes, google is undefined, creating promise');
            deferred.resolve(window.google.maps);
            return deferred.promise;
        }
        var randomizedFunctionName = 'onGoogleMapsReady' + Math.round(Math.random() * 1000);
        window[randomizedFunctionName] = function () {
            window[randomizedFunctionName] = null;
            deferred.resolve(window.google.maps);
        };
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = $rootScope.config.GoogleMapsUrl + '&callback=' + randomizedFunctionName;
        document.body.appendChild(script);
        return deferred.promise;
    }
]);