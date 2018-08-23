
appServices.factory('languageService', [
    '$rootScope',
    '$translate',
    'localStorageService',
    '$q',
    'loggerService',
    function ($rootScope, $translate, storage, $q, logger) {

        return {

            /*
             * Recupera la lingua corrente
             */
            getLanguage: function () {
                logger.debug("[languageService.getLanguage]");
                return storage.get("LAST_LANGUAGE");
            },

            /*
             * Imposta la lingua da utilizzare effettuando sempre il refresh (che causa un reload delle traduzioni)
             */
            setLanguage: function (newLang) {
                logger.debug("[languageService.setLanguage]", newLang);
                storage.set("LAST_LANGUAGE", newLang);

                var q = $q.defer();
                $translate.refresh().then(
                    function () {
                        logger.debug("[languageService.setLanguage] success");
                        q.resolve();
                    },
                    function (err) {
                        logger.error("[languageService.setLanguage] error", err);
                        q.reject(err);
                    });
                return q.promise;
            },

            /*
             * Imposta l'ultima lingua utilizzata effettuando sempre il refresh (che causa un reload delle traduzioni)
             */
            setLastLanguage: function () {

                var langIsoCode = storage.get("LAST_LANGUAGE");

                // Se non è ancora stata impostata una lingua (primo avvio)
                // Imposto la lingua di default settata nel config
                if(!langIsoCode){
                    langIsoCode = $rootScope.config.i18n.defaultLanguage;
                    storage.set("LAST_LANGUAGE", langIsoCode);
                }

                var q = $q.defer();
                $translate.use(langIsoCode).then(
                    function (data) {
                        logger.debug("[languageService.setLanguage] "+langIsoCode+" success");
                        q.resolve();
                    },
                    function (err) {
                        logger.error("[languageService.setLanguage] "+langIsoCode+" error", err);
                        q.reject(err);
                    });
                return q.promise;
            },

            /*
             * Imposta la lingua corretta per l'utente
             */
            selectUserLanguage: function () {

            },
            /*
             * Recupera l'elenco delle lingue disponibili
             */
            getAvailableLanguages: function () {

            }
        };
    }
]);
