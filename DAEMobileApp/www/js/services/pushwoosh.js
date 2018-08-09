appServices.factory('PushWoosh', ['$rootScope', '$cordovaDevice', 'loggerService', function ($rootScope, $cordovaDevice, logger) {
    return {
        register: function (fn) {
            var pushNotification = window.plugins.pushNotification;
            var androidSuccessHandler = function (status) {
                logger.info('pushwoosh service - SUCCESS ' + JSON.stringify(status));
                var deviceToken = status['pushToken'];
                logger.info('registerDevice: ' + deviceToken);
                return fn({
                    'type': 'registration',
                    'id': deviceToken,
                    'device': 'android'
                });
            };
            var iosSuccessHandler = function (status) {
                logger.info('pushwoosh service - SUCCESS');
                logger.info(status);
                var deviceToken = status['pushToken'];
                logger.info('registerDevice: ' + deviceToken);
                return fn({
                    'type': 'registration',
                    'id': deviceToken,
                    'device': 'ios'
                });
            };
            var errorHandler = function (status) {
                logger.info('pushwoosh service - ERROR: ' + status);
                return fn({'type': 'error'});
            };
            if ($cordovaDevice.getPlatform().toLowerCase() == 'android') {
                // pushNotification.onDeviceReady( {
                // 	projectid: $rootScope.config.PushWoosh.GoogleProjectNumber,
                // 	appid: $rootScope.config.PushWoosh.AppId
                // } );
                pushNotification.registerDevice(androidSuccessHandler, errorHandler);
            }
            else {
                logger.info('register ios');
                // pushNotification.onDeviceReady( {pw_appid: $rootScope.config.PushWoosh.AppId} );
                pushNotification.registerDevice(iosSuccessHandler, errorHandler);
            }
        },
        createLocalNotification: function (pn) {
            var pushNotification = window.plugins.pushNotification, success = function (data) {
                logger.info('pushwoosh createLocalNotification - SUCCESS ' + data);
            }, fail = function (error) {
                logger.info('pushwoosh createLocalNotification - ERROR: ' + error);
            };
            var text;
            if (cordovaDeviceService.platform() == 'android') {
                text = pn.title;
            }
            else {
                text = pn.aps.alert;
            }
            var config = {
                msg: text,
                seconds: 0,
                userData: ''
            };
            pushNotification.createLocalNotification(config, success, fail);
        },
        getRemoteNotificationStatus: function () {
            var pushNotification = window.plugins.pushNotification, success = function (data) {
                logger.info('pushwoosh getRemoteNotificationStatus - SUCCESS ' + JSON.stringify(data));
            };
            pushNotification.getRemoteNotificationStatus(success);
        },
        cancelAllLocalNotifications: function (pn) {
            var pushNotification = window.plugins.pushNotification, success = function (data) {
                logger.info('pushwoosh cancelAllLocalNotifications - SUCCESS ' + JSON.stringify(data));
            };
            pushNotification.cancelAllLocalNotifications(success);
        },
        setApplicationIconBadgeNumber: function (number) {
            var pushNotification = window.plugins.pushNotification, success = function (data) {
                logger.info('pushwoosh setApplicationIconBadgeNumber - SUCCESS ' + JSON.stringify(data));
            };
            pushNotification.setApplicationIconBadgeNumber(number, success);
        }
    };
}]);
