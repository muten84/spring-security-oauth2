
appServices.factory('translateService', [
    '$rootScope',
    'loggerService',
    'localStorageService',
    '$q',
    'deviceService',
    'apiService',
    'tmhDynamicLocale',
    function ($rootScope, logger, storage, $q, device, api, tmhDynamicLocale) {

        var langMap = {
            "it_IT": {
                "moment" : "it",
                "angular": "it"
            },
            "en_GB": {
                "moment" : "en_GB",
                "angular": "en-gb"
            }
        };

        /**
         * Se esiste carica il file di lingua dal localStorage
         * @param {promise} deferred - Promise da risolvere
         * @param {string} langIsoCode - Chiave ISO della la lingua
         */
        var retrieveFromCache = function (deferred, langIsoCode) {

            var translationMap = storage.get("LANG_" + langIsoCode);

            if (translationMap != null) {
                logger.debug("[translateService:retrieveFromCache] "+langIsoCode+" success");
                setMomentAndAngularLocale(langIsoCode).then(
                    function(){
                        deferred.resolve(translationMap);
                    }
                );
            }
            else {
                logger.error("[translateService:retrieveFromCache] "+langIsoCode+" not found");
                deferred.reject();
            }
        };

        /**
         * Se esistono carica i file di lingua per Moment e per AngularJS
         * @param {string} langKey - Chiave per la lingua
         */
        var setMomentAndAngularLocale = function(langKey){

            var deferred = $q.defer();

            // Imposto la localizzazione per Moment se presente
            if(langMap[langKey] && langMap[langKey].moment){
                moment.locale(langMap[langKey].moment);
            }

            // Imposto la localizzazione per AngularJS se presente
            if(langMap[langKey] && langMap[langKey].angular){
                tmhDynamicLocale.set(langMap[langKey].angular).then(
                    function(){
                        deferred.resolve();
                    },
                    function(err){
                        logger.error("[translateService] tmhDynamicLocale per "+langKey, err);
                        deferred.resolve();
                    }
                );
            }

            return deferred.promise;
        };

        return function (options) {

            var deferred = $q.defer();

            // Codice di lingua da recuperare sul server
            var langIsoCode = storage.get("LAST_LANGUAGE");

            // Se sono online tento il recupero del file di lingua
            if(device.isOnline()){
                api.getTranslation(langIsoCode).then(
                    function(translation){

                        // Memorizzo la lingua sullo storage per usarla offline
                        storage.set("LANG_" + langIsoCode, translation);

                        // Imposto il locale di AngularJS e Moment
                        setMomentAndAngularLocale(langIsoCode).then(
                            function(){
                                deferred.resolve(translation);
                            }
                        );
                    },
                    function(err){
                        logger.debug("[translateService:getTranslations] error", err);
                        retrieveFromCache(deferred, langIsoCode);
                    }
                );
            }
            // Altrimenti tento di recuperare quello in cache
            else {
                retrieveFromCache(deferred, langIsoCode);
            }

            return deferred.promise;
        };
    }
]);
