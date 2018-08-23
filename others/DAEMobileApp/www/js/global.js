window.globalVariable = {
    color: {
        appPrimaryColor: "#44A048"
    },
    startPage: {
        url: "/app/home",
        state: "app.home"
    }
};


var _APP_ = 'DAE RespondER';
var _APPNAME_ = 'DAE RespondER';

var _DEV_ = false;
var _VERSION_ = '1.6.3';
var _CONFIG_URL_ = null;

if (_DEV_) {
    _CONFIG_URL_ = 'http://daeeng.118er.it/testSDODAEServices/config/' + _VERSION_ + '/config_app_er_test.json';
    _VERSION_ += '-[DEV]';
}
else {
    _CONFIG_URL_ = 'https://daeeng.118er.it/SDODAEServices/config/' + _VERSION_ + '/config_app_er.json';
}

var _CONTROLLERS_ = _APP_ + '.controllers'
    , _DIRECTIVES_ = _APP_ + '.directives'
    , _FILTERS_ = _APP_ + '.filters'
    , _MODULES_ = _APP_ + '.modules'
    , _SERVICES_ = _APP_ + '.services'
    , _TEMPLATES_ = 'templates';

var appControllers = angular.module(_CONTROLLERS_, []);
var appDirectives = angular.module(_DIRECTIVES_, []);
var appFilters = angular.module(_FILTERS_, []);
var appModules = angular.module(_MODULES_, []);
var appServices = angular.module(_SERVICES_, []);
angular.module(_TEMPLATES_, []);