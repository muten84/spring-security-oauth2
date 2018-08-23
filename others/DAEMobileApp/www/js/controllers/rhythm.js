appControllers.controller('RhythmCtrl', [
    '$rootScope',
    '$scope',
    '$state',
    'loggerService',
    '$timeout', '$ionicHistory',
    function ($rootScope, $scope, $state, logger, $timeout, $ionicHistory) {
        logger.info("Caricamento RhythmCtrl...");

        var vm = this;
        vm.play = play;
        vm.stop = stop;
        vm.started = false;
        vm.volume = 3;
        var manualStop = false

        var media = null;

        $scope.$on('$ionicView.enter', function () {
            manualStop = false;
        });

        $scope.$on('$ionicView.leave', function () {
            stop();
        });

        function play() {
            var mp3URL = getMediaURL("res/rate_sound.mp3");
            media = new Media(mp3URL, mediaSuccess, mediaError, mediaStatus);
            media.setVolume(vm.volume / 10);
            media.play({ playAudioWhenScreenIsLocked: true, numberOfLoops: 9999 });
            vm.started = true;
        }

        function stop() {
            media.stop();
            media.release();
            manualStop = true;
            vm.started = false;
        }

        $scope.$watch('vm.volume', function (current, old) {
            logger.info("current: " + current);
            logger.info("old: " + old);
            setVolume(current);
        });

        function getMediaURL(s) {
            if (device.platform.toLowerCase() === "android") return "/android_asset/www/" + s;
            return s;
        }

        function setVolume(value) {
            var val = (value) ? value / 10 : 0.0;
            logger.info("val: " + val);
            if (media) {
                media.setVolume(val);
            }
        }

        function mediaSuccess() {
            logger.info("playAudio():Audio Success");
        }

        function mediaError(err) {
            logger.info("playAudio():Audio Error: " + JSON.stringify(err));
        }

        function mediaStatus(status) {
            if (status === Media.MEDIA_STOPPED) {
                if (!manualStop) {
                    media.play();
                }
                else {
                    manualStop = false;
                }
            }
            logger.info('status', JSON.stringify(arguments));
        }

        $scope.goHome = function () {
            //   $scope.  $ionicGoBack();
            //  $ionicHistory.goBack(-1);
            //  window.history.back();  
        }

    }]);