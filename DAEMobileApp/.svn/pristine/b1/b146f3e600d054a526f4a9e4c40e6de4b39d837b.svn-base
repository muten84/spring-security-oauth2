appServices.factory('deviceService', [
    'loggerService',
    '$ionicLoading',
    '$ionicPopup',
    '$translate',
    '$cordovaNetwork',
    '$ionicPlatform',
    '$ionicHistory',
    '$q',
    '$timeout',
    '$cordovaLocalNotification',
    function (logger, $ionicLoading, $ionicPopup, $translate,
        $cordovaNetwork, $ionicPlatform, $ionicHistory, $q, $timeout, $cordovaLocalNotification) {

        logger.info("Inizializzazione deviceService...");



        var device = {
            /*
            Variabile per capire se l'app è in background o foreground
            */
            inBackground: false,

            /**
             * Verifica se si sta girando su Cordova
             * @returns {Boolean}
             */
            isCordova: function () {
                return ionic.Platform.isWebView();
            },

            /**
             * Verifica se si sta girando su iOS
             * @returns {Boolean}
             */
            isIos: function () {
                return ionic.Platform.isIOS();
            },

            /**
             * Verifica se si sta girando su Android
             * @returns {Boolean}
             */
            isAndroid: function () {
                return ionic.Platform.isAndroid();
            },

            checkLocationAuthorizationStatus: function () {
                var q = $q.defer();
                if (device.isCordova() && cordova.plugins.diagnostic) {
                    cordova.plugins.diagnostic.getLocationAuthorizationStatus(
                        function (status) {
                            if (status == cordova.plugins.diagnostic.permissionStatus.GRANTED) {
                                q.resolve(true);
                            }
                            else if (status == cordova.plugins.diagnostic.permissionStatus.GRANTED_WHEN_IN_USE) {
                                q.resolve(true);
                            }
                            else {
                                cordova.plugins.diagnostic.requestLocationAuthorization(
                                    function (status) {
                                        if (status == cordova.plugins.diagnostic.permissionStatus.GRANTED) {
                                            q.resolve(true);
                                        }
                                        else if (status == cordova.plugins.diagnostic.permissionStatus.GRANTED_WHEN_IN_USE) {
                                            q.resolve(true);
                                        }
                                        else {
                                            q.resolve(false);
                                        }
                                    },
                                    function (error) {
                                        q.reject(error);
                                    },
                                    cordova.plugins.diagnostic.locationAuthorizationMode.ALWAYS
                                );
                            }
                        }
                    );
                }
                else {
                    q.reject("Plugin cordova.plugins.diagnostic not installed");
                }
                return q.promise;
            },

            checkIsLocationAvailable: function () {
                var q = $q.defer();
                if (device.isCordova() && cordova.plugins.diagnostic) {
                    cordova.plugins.diagnostic.isLocationAvailable(
                        function (status) {
                            q.resolve(status);
                        },
                        function (error) {
                            q.reject(error);
                        }
                    );
                }
                else {
                    q.reject("plugin cordova.plugins.diagnostic not installed");
                }
                return q.promise;
            },

            /**
             * Inizializza il comportamento del back button di Android
             * sulla base dello stato corrente
             */
            initBackButton: function () {
                $ionicPlatform.registerBackButtonAction(function () {
                    // Lista degli stati in cui è concesso il back di Android
                    var backAvailable = [];
                    // Se lo stato non è tra quelli autorizzati blocco il back
                    if (backAvailable.indexOf($ionicHistory.currentStateName) == -1) {

                    }
                    // Altrimenti effettuo il back
                    else {
                        $ionicHistory.goBack();
                    }
                }, 100);
            },

            /**
             * Mostra un avviso all'utente
             * @param {String} msg - Corpo dell'avviso
             * @param {String/null} css - Se esiste reppresenta la classe CSS
             * @param {String/null} title - Se esiste è il titolo dell'avviso
             * @returns {promise}
             */
            alert: function (msg, css, title) {
                msg = (msg) ? $translate.instant(msg) : "...";
                title = title || _APPNAME_;

                device.hideLoader();

                return $ionicPopup.alert({
                    title: title,
                    template: msg,
                    cssClass: css
                });
            },

            /**
             * Mostra una modale di conferma
             * @param {String} msg - Corpo dell'avviso
             * @param {Object} options - Configurazione del popup ionic
             * @returns {promise}
             */
            confirm: function (msg, options) {
                msg = msg || "...";

                device.hideLoader();

                var opt = {
                    title: _APPNAME_,
                    template: "...",
                    cancelText: $translate.instant("CANCEL"),
                    cancelType: "button-default",
                    okText: $translate.instant("OK"),
                    okType: "button-positive"
                };

                // Se è stato richiesto un titolo particolare lo sostituisco al default
                if (options.title) {
                    opt.title = $translate.instant(options.title);
                }

                // Aggiungo il testo tradotto
                if (msg) {
                    opt.template = $translate.instant(msg);
                }

                // Aggiungo la classe se richiesta
                if (options.cssClass) {
                    opt.cssClass = options.cssClass;
                }

                // Se ci sono configurazioni per i bottoni le aggiungo
                if (options.cancelText)
                    opt.cancelText = $translate.instant(options.cancelText);
                if (options.cancelType)
                    opt.cancelType = options.cancelType;
                if (options.okText)
                    opt.okText = $translate.instant(options.okText);
                if (options.okType)
                    opt.okType = options.okType;

                return $ionicPopup.confirm(opt);
            },

            /**
             * Mostra un popup utilizzando Ionic
             * @param {String} options.title - Titolo
             * @param {String} options.cssClass - Classe CSS
             * @param {String} options.subTitle - Sottotitolo
             * @param {String} options.template - Template HTML
             * @param {String} options.templateUrl - Url del template HTML
             * @param {String} options.scope - Scope da linkare
             * @param {[Object]} options.buttons - Lista dei bottoni
             * @param {String} options.buttons.text - Testo del bottone
             * @param {String} options.buttons.type - Classe del bottone
             * @param {String} options.buttons.onTap - Funzione del bottone
             * @returns {promise}
             */
            showPopup: function (options) {

                if (!options.title) {
                    options.title = _APPNAME_;
                }

                return $ionicPopup.show(options);
            },

            /**
             * Mostra il loader
             * @param {String} text - Testo da visualizzare
             * @returns {promise}
             */
            showLoader: function (text) {
                device.hideKeyboard();

                text = text || "";
                if (text)
                    text += "<br/><br/>";

                return $ionicLoading.show({
                    template: text + '<ion-spinner></ion-spinner>'
                });
            },

            /**
             * Chiude il loader
             * @returns {promise}
             */
            hideLoader: function () {
                return $ionicLoading.hide();
            },

            /**
             * Mostra il loader
             * @param {String} text - Testo da visualizzare
             * @returns {promise}
             */
            showCustomLoader: function () {
                if (device.isAndroid()) {
                    jQuery('#custom-loading-progress').show();
                }
                else {
                    jQuery('#custom-loading-progress').fadeIn(700);
                }
            },

            /**
             * Chiude il loader
             * @returns {promise}
             */
            hideCustomLoader: function () {
                $timeout(function () {
                    jQuery('#custom-loading-progress').hide();
                }, 800);
            },

            /**
             * Nasconde lo splashscreen nativo dell'app
             */
            hideSplashscreen: function () {
                if (navigator.splashscreen)
                    navigator.splashscreen.hide();
                return device;
            },

            /**
             * Toglie il focus da tutti gli input chiudendo la tastiera
             */
            hideKeyboard: function () {
                if (device.isCordova()) {
                    cordova.plugins.Keyboard.close();
                }
                else {
                    document.activeElement.blur();
                    $("input").blur();
                }
                return device;
            },

            /**
             * Verifica se il device è online
             */
            isOnline: function () {
                if (device.isCordova())
                    return $cordovaNetwork.isOnline();
                else
                    return (window.navigator && window.navigator.onLine);
            },

            /**
             * Verifica se il device è offline
             */
            isOffline: function () {
                return !device.isOnline();
            },

            sendLocalNotification: function (data) {
                var title = _APPNAME_;
                var text = $translate.instant("LOCAL_NOTIFICATION_TEXT");
                if (device.inBackground) {
                    $cordovaLocalNotification.hasPermission().then(
                        function (result) {
                            logger.info("$cordovaLocalNotification hasPermission success :" + result);
                        },
                        function (error) {
                            logger.info("$cordovaLocalNotification hasPermission error :" + error);
                        }
                    );
                    $cordovaLocalNotification.schedule({
                        id: 1,
                        title: title,
                        text: text,
                        data: data
                    }).then(function (result) {
                        logger.info("$cordovaLocalNotification schedule success :" + result);
                    });
                }
            },

            sendTestLocalNotification: function (data) {
                var title = _APPNAME_;
                var text = $translate.instant("LOCAL_NOTIFICATION_TEXT");
                var now = new Date().getTime()
                var _5_sec_from_now = new Date(now + 5 * 1000);
                $cordovaLocalNotification.hasPermission().then(
                    function (result) {
                        logger.info("$cordovaLocalNotification hasPermission success :" + result);
                    },
                    function (error) {
                        logger.info("$cordovaLocalNotification hasPermission error :" + error);
                    }
                );

                $cordovaLocalNotification.schedule({
                    id: 1,
                    title: title,
                    text: text,
                    at: _5_sec_from_now,
                    data: data
                }).then(function (result) {
                    logger.info("$cordovaLocalNotification schedule success :" + result);
                });
            }

        };



        $ionicPlatform.ready(function () {
            document.addEventListener("resume", function () { device.inBackground = false; }, false);
            document.addEventListener("pause", function () { device.inBackground = true; }, false);
        });

        return device;
    }]);