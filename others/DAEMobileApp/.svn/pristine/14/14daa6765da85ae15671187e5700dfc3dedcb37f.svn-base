
appDirectives.directive('qyToggleOverflowScroll', [
    '$timeout',
    '$window',
    '$ionicPlatform',
    function ($timeout, $window, $ionicPlatform) {

    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var domElement = element[0];

            $ionicPlatform.ready(function () {
                $window.addEventListener('native.keyboardshow', handleKeyboardShow);
                $window.addEventListener('native.keyboardhide', handleKeyboardHide);

                // remove event listener on destroy
                scope.$on('$destroy', removeKeyboardHandlerListener);

                function handleKeyboardShow() {
                    //logger.info('qyOverflowScrollToggle keyboard show: ELEMENT CLASS LIST: '
                    //    + domElement.classList.toString());
                    // iOS or Android full screen
                    var isIosOrAndroidFullScreen
                        = ionic.Platform.isIOS() || (ionic.Platform.isAndroid() && ionic.Platform.isFullScreen);

                    if (isIosOrAndroidFullScreen) {
                        //logger.info('qyOverflowScrollToggle: '
                        //    + 'keyboard is shown, set overflow-y to: scroll');
                        domElement.style.overflowY = 'hidden';
                        // set -webkit-overflow-scrolling to auto for having non-momentum scrolling if
                        // keyboard is up. Setting it to touch causes screen flicker when closing keyboard
                        domElement.style.webkitOverflowScrolling = 'auto';

                        $timeout(function setOverflowYToScrollIfNeeded() {
                            try {
                                var scrollerHeight = element.height();
                                var scrollerContentHeight = domElement.scrollHeight;

                                // if scroller contains enough content to enable scrolling
                                if (scrollerContentHeight > scrollerHeight + 1) {
                                    //logger.info('qyOverflowScrollToggle keyboard show: '
                                    //    + 'scroller height / scroller content height: '
                                    //    + scrollerHeight
                                    //    + ' / '
                                    //    + scrollerContentHeight);

                                    //logger.info('qyOverflowScrollToggle keyboard show: '
                                    //    + 'content larger than scroller, set overflow-y to: scroll');
                                    domElement.style.overflowY = 'scroll';
                                    // no need to set -webkit-overflow-scrolling as it should remain with value auto
                                    // whenever keyboard is up. We disable momentum scrolling when keyboard is up.
                                }
                            }catch(e){}
                        }, 400);
                    }
                }

                function handleKeyboardHide() {
                    //logger.info('qyOverflowScrollToggle keyboard hide: ELEMENT CLASS LIST: '
                    //    + domElement.classList.toString());
                    //// iOS or Android full screen
                    var isIosOrAndroidFullScreen
                        = ionic.Platform.isIOS() || (ionic.Platform.isAndroid && ionic.Platform.isFullScreen);

                    if (isIosOrAndroidFullScreen) {
                        domElement.style.overflowY = 'hidden';
                        // set -webkit-overflow-scrolling to auto for keyboard transition
                        domElement.style.webkitOverflowScrolling = 'auto';

                        $timeout(function setOverflowYToScrollIfNeeded() {
                            var scrollerHeight = domElement.clientHeight;
                            var scrollerContentHeight = domElement.scrollHeight;

                            //logger.info('qyOverflowScrollToggle keyboard hide: '
                            //    + 'scroller height / scroller content height: '
                            //    + scrollerHeight
                            //    + ' / '
                            //    + scrollerContentHeight);

                            // if scroller contains enough content to enable scrolling
                            if (scrollerContentHeight > scrollerHeight + 1) {
                                //logger.info('qyOverflowScrollToggle keyboard hide: '
                                //    + 'content larger than scroller, set overflow-y to: scroll');
                                domElement.style.overflowY = 'scroll';
                                // set -webkit-overflow-scrolling to touch for default momentum scrolling if
                                // keyboard is not up
                                domElement.style.webkitOverflowScrolling = 'touch';
                            }
                        }, 400);
                    }
                }

                function removeKeyboardHandlerListener() {
                    $window.removeEventListener('native.keyboardshow', handleKeyboardShow);
                    $window.removeEventListener('native.keyboardhide', handleKeyboardHide);
                }
            });
        }
    };
}]);