//
//Welcome to app.js
//This is main application config of project. You can change a setting of :
//  - Global Variable
//  - Theme setting
//  - Icon setting
//  - Register View
//  - Spinner setting
//  - Custom style
//
angular.module(_APP_, [
    _CONTROLLERS_,
    _DIRECTIVES_,
    _FILTERS_,
    _MODULES_,
    _SERVICES_,
    _TEMPLATES_,
    'ionic',
    'LocalStorageModule',
    'pascalprecht.translate',
    'ngCordova',
    'tmh.dynamicLocale',
    'formly',
    'ngIOS9UIWebViewPatch',
    'ngMaterial',
    'ngMessages',
    'ngMap'
]);
angular.module(_APP_).run([
    '$ionicPlatform',
    '$cordovaSQLite',
    '$rootScope',
    '$ionicHistory',
    '$state',
    '$mdDialog',
    '$mdBottomSheet',
    'deviceService',
    'localStorageService',
    'loggerService',
    'languageService',
    'formlyConfig',
    'formlyValidationMessages',
    'PushWoosh',
    '$translate',
    'alertService',
    'userService',
    'apiService',
    '$cordovaDevice',
    function ($ionicPlatform, $cordovaSQLite, $rootScope, $ionicHistory, $state, $mdDialog, $mdBottomSheet, device, storage, logger, language, formlyConfig, formlyValidationMessages, pushwoosh, $translate, alertService, user, api, $cordovaDevice) {

        formlyConfig.extras.errorExistsAndShouldBeVisibleExpression = 'fc.$touched || form.$submitted';
        formlyValidationMessages.addStringMessage('required', 'Campo obbligatorio');

        // Create custom defaultStyle.
        function getDefaultStyle() {
            return "" +
                ".material-background-nav-bar { " +
                "   background-color        : " + appPrimaryColor + " !important; " +
                "   border-style            : none;" +
                "}" +
                ".md-primary-color {" +
                "   color                     : " + appPrimaryColor + " !important;" +
                "}";
        }// End create custom defaultStyle


        function initialRootScope() {
            $rootScope.appPrimaryColor = appPrimaryColor;
            $rootScope.eventInProgress = false;
        }

        function hideActionControl() {
            //For android if user tap hardware back button, Action and Dialog should be hide.
            $mdBottomSheet.cancel();
            $mdDialog.cancel();
        }


        // createCustomStyle will change a style of view while view changing.
        // Parameter :
        // stateName = name of state that going to change for add style of that page.
        function createCustomStyle(stateName) {
            var customStyle =
                ".material-background {" +
                "   background-color          : " + appPrimaryColor + " !important;" +
                "   border-style              : none;" +
                "}" +
                ".spinner-android {" +
                "   stroke                    : " + appPrimaryColor + " !important;" +
                "}";
            customStyle += getDefaultStyle();
            return customStyle;
        }// End createCustomStyle

        // Add custom style while initial application.
        $rootScope.customStyle = createCustomStyle(window.globalVariable.startPage.state);

        $ionicPlatform.ready(function () {

            ionic.Platform.isFullScreen = true;

            // Imposto il flag per le scorciatoie di sviluppo
            $rootScope.DEV = _DEV_;

            // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
            // for form inputs)
            if (window.cordova && window.cordova.plugins.Keyboard) {
                cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                cordova.plugins.Keyboard.disableScroll(true);
            }

            if (window.StatusBar) {
                // org.apache.cordova.statusbar required
                StatusBar.hide();
                StatusBar.styleDefault();
                StatusBar.overlaysWebView(false);
            }

            if (device.isCordova() && device.isAndroid()) {
                // Fullscreen per android
                // AndroidFullScreen.immersiveMode();
            }

            // Leggo e imposto il file di configurazione dal localStorage
            $rootScope.config = storage.get("CONFIG");

            // Inizializzo il servizio di logger al livello richiesto
            logger.init($rootScope.config.LogLevel);

            // Carico la lingua utilizzata nell'ultima sessione
            language.setLastLanguage();

            device.initBackButton();

            if (window.cordova && window.plugins.pushNotification) {
                var pushNotification = window.plugins.pushNotification;
                if ($cordovaDevice.getPlatform().toLowerCase() == 'android') {
                    pushNotification.onDeviceReady({
                        projectid: $rootScope.config.PushWoosh.GoogleProjectNumber,
                        appid: $rootScope.config.PushWoosh.AppId
                    });

                }
                else {
                    pushNotification.onDeviceReady({ pw_appid: $rootScope.config.PushWoosh.AppId });
                }
            }

            initialRootScope();

            /**********************************************************************
             * Intercetto l'evento scatenato dall'arrivo di una notifica push
             **********************************************************************
             * iOS:
             {
                 "notification": {
                     "message": "test1",
                     "foreground": true,
                     "userdata": {
                         "eventId": "1"
                     },
                     "ios": {
                         "aps": {
                             "alert": "test1",
                             "sound": "default"
                         },
                         "u": "{\"eventId\": \"1\" }",
                         "p": "EO"
                     },
                     "onStart": false
                 },
                 "isTrusted": false
             }
             * Android:
             {
                "isTrusted": false,
                "notification": {
                    "android": {
                        "message_id": 1001,
                        "google.sent_time": 1481020057193,
                        "onStart": false,
                        "pw_msg": "1",
                        "p": "EL",
                        "userdata": {
                            "eventId": "1"
                        },
                        "u": "{\"eventId\":\"1\"}",
                        "pri": "",
                        "vib": "0",
                        "title": "test android",
                        "google.message_id": "0:1481020057202298399a4fef9fd7ecd",
                        "collapse_key": "do_not_collapse",
                        "foreground": true
                    },
                    "message": "test android",
                    "foreground": true,
                    "onStart": false,
                    "userdata": {
                        "eventId": "1"
                    }
                }
            }
             **********************************************************************/

            document.addEventListener('push-notification', function (event) {
                logger.info("[APP RUN] Ricevuta notifica push");
                logger.info("[APP RUN] Notifica push" + JSON.stringify(event));
                var notification = event.notification;
                if (!notification.userdata.operation) {
                    if (notification.foreground) {
                        device.alert(notification.message, "balanced", "DAE118ER");
                    }
                }
                else if (notification.userdata.operation === "NEW") {
                    $rootScope.$broadcast("NewAlertNotification");
                }
                else if (notification.userdata.operation === "UPDATE") {
                    alertService.updateEvent(notification);
                    $rootScope.$broadcast("UpdateAlertNotification");
                }
                else if (notification.userdata.operation === "ABORT") {
                    alertService.abortEvent(notification);
                    $rootScope.$broadcast("AbortAlertNotification", notification);
                    $state.transitionTo("app.home");
                }
            });

            document.addEventListener("resume", function () {
                logger.info("[APP RUN] Invio evento ResumeApp");
                $rootScope.$broadcast("ResumeApp");
            }, false);

            $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
                logger.info("toState: " + toState.name);
                logger.info("fromState: " + fromState.name);
                if (fromState.name == "" && toState.name == "app.home") {
                    if (device.isCordova()) {
                        if (device.isAndroid()) {
                            if (!$rootScope.config.Versioning.android.isLastVersion) {
                                $state.transitionTo("app.update");
                                event.preventDefault();
                                return;
                            }
                        } else {
                            if (!$rootScope.config.Versioning.iOS.isLastVersion) {
                                $state.transitionTo("app.update");
                                event.preventDefault();
                                return;
                            }
                        }
                    }
                }
            });

            $rootScope.$on('$ionicView.beforeEnter', function () {
                hideActionControl();
                $rootScope.customStyle = createCustomStyle($ionicHistory.currentStateName());
            });
        });
    }]);


