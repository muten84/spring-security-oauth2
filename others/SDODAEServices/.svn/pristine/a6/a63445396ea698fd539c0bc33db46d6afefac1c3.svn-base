'use strict';
// @ts-ignore
require.config({
    waitSeconds: 300,
    shim: {
        bootstrap: {
            deps: ["tether",
                'angular',
                'jquery'
            ]
        },
        'angular-ui-grid': {
            deps: [
                'angular',
                'jquery'
            ]
        },
        'angular-uiselect': {
            deps: [
                'angular',
                'jquery'
            ]
        },
        'areas-ui': {
            deps: [
                'angular',
                'jquery'
            ]
        },
        "angular-simple-logger": {
            deps: ['angular']
        },
        'ui-leaflet': {
            deps: [
                'angular',
                'angular-sanitize',
                "angular-simple-logger",
                'leaflet'
            ]
        },
        'leaflet': {
            exports: 'leaflet'
        },
        "leaflet-control-boxzoom": {
            deps: ['leaflet']
        },
        "leaflet-draw": {
            deps: ['leaflet']
        },
        "leaflet.markercluster": {
            deps: ['leaflet']
        },
        "ui-leaflet-draw": {
            deps: ['ui-leaflet', "leaflet-draw"]
        },
        'angular-indexed-db': {
            deps: ['angular']
        },
        'angular-ui-validate': {
            deps: ['angular']
        },
        angular: {
            exports: 'angular'
        },
        "angular-trix": {
            deps: ['angular', 'trix']
        },
        "tether": {
            exports: 'tether'
        },
        lodash: {
            exports: '_'
        },
        moment: {
            exports: 'moment'
        },
        'angular-route': [
            'angular'
        ],

        'angular-cookies': [
            'angular'
        ],
        'angular-sanitize': [
            'angular'
        ],
        'angular-resource': [
            'angular'
        ],
        'angular-animate': [
            'angular'
        ],
        'angular-touch': [
            'angular'
        ],
        'angular-aria': [
            'angular'
        ],
        'angular-messages': [
            'angular'
        ],
        'angular-help-overlay': {
            deps: [
                'angular',
                'jquery',
                'chardin'
            ]
        },
        'angular-bootstrap': [
            'angular',
            "tether",
            'bootstrap',
            'angular-animate',
            'angular-touch'
        ],
        'ng-table': [
            'angular'
        ],
        'angular-block-ui': [
            'angular'
        ],
        'angular-mocks': {
            deps: [
                'angular'
            ],
            exports: 'angular.mock'
        },
        "xeditable": [
            'angular'
        ],
        "angular-uiselect2": {
            deps: [
                'angular',
                "select2"

            ]
        },
        "angular-bootstrap-confirm": {
            deps: [
                'angular',
                "angular-bootstrap",
                "tether"
            ]
        },
        "angular-chartjs": {
            deps: [
                "angular",
                "chart"
            ]
        },
        "angular-toastr": {
            deps: [
                'angular',
                'bootstrap',
                'angular-animate',
                'angular-touch'
            ]
        },
        "bxslider": {
            deps: [
                "jquery",
                "angular"
            ]
        },
        "angular-file-upload": {
            deps: ["angular"]
        },
        "file-saver": {
            deps: []
        },
        "zxcvbn": {
            deps: []
        }
    },
    paths: {
        angular: '../../webjars/angular/angular',
        'angular-animate': '../../webjars/angular-animate/angular-animate',
        'angular-aria': '../../webjars/angular-aria/angular-aria',
        'angular-cookies': '../../webjars/angular-cookies/angular-cookies',
        'angular-messages': '../../webjars/angular-messages/angular-messages',
        'angular-mocks': '../../webjars/angular-mocks/angular-mocks',
        'angular-resource': '../../webjars/angular-resource/angular-resource',
        'angular-route': '../../webjars/angular-route/angular-route',
        'angular-sanitize': '../../webjars/angular-sanitize/angular-sanitize',
        'angular-touch': '../../webjars/angular-touch/angular-touch',
        bootstrap: '../../webjars/bootstrap/dist/js/bootstrap',
        domReady: '../../webjars/domReady/domReady',
        jquery: '../../webjars/jquery/dist/jquery',
        'ng-table': '../../webjars/ng-table/dist/ng-table.min',
        'angular-ui-router': '../../webjars/angular-ui-router/release/angular-ui-router',
        'angular-ui-grid': '../../webjars/angular-ui-grid/ui-grid',
        'angular-ui-validate': '../../webjars/angular-ui-validate/dist/validate',
        'angular-bootstrap': '../../webjars/angular-bootstrap/ui-bootstrap-tpls',
        moment: '../../webjars/moment/2.16.0/moment',
        'angular-block-ui': '../../webjars/angular-block-ui/dist/angular-block-ui',
        "xeditable": "thirdparty/xeditable/xeditable",
        "angular-uiselect": "../../webjars/angular-ui-select/dist/select.min",
        "lodash": "../../webjars/lodash/dist/lodash.min",
        "angular-bootstrap-confirm": "../../webjars/angular-bootstrap-confirm/dist/angular-bootstrap-confirm.min",
        "tether": "../../webjars/tether/dist/js/tether.min",
        "areas-ui": "../../webjars/areas-ui/areas-ui",
        /*"areas-charts": "../../webjars/areas-charts/areas-charts",*/
        "chart": "thirdparty/charts/chart",
        "angular-chartjs": "thirdparty/charts/angular-chart",
        "ui-leaflet": "../../webjars/github-com-angular-ui-ui-leaflet/dist/ui-leaflet.min",
        "leaflet": "../../webjars/leaflet/leaflet",
        "leaflet-draw": "../../webjars/leaflet-draw/dist/leaflet.draw",
        "leaflet.markercluster": "../../webjars/leaflet-markercluster/leaflet.markercluster",
        "angular-help-overlay": "../../webjars/github-com-jordanburke-angular-help-overlay/lib/angular-help-overlay",
        "chardin": "../../webjars/chardin.js/chardinjs",
        "packery": "../../webjars/packery/dist/packery.pkgd",
        "draggabilly": "../../webjars/draggabilly/dist/draggabilly.pkgd",
        "angular-toastr": "../../webjars/angular-toastr/dist/angular-toastr.tpls",
        "bxslider": "../../webjars/bxslider-4/dist/jquery.bxslider",
        "trix": "../../webjars/trix/dist/trix",
        "angular-trix": "../../webjars/angular-trix/dist/angular-trix",
        "angular-indexed-db": "../../webjars/angular-indexed-db/angular-indexed-db.min",
        "leaflet-control-boxzoom": "thirdparty/leaflet-boxzoom/leaflet-control-boxzoom",
        "ui-leaflet-draw": "thirdparty/ui-leaflet-draw",
        "angular-simple-logger": "../../webjars/angular-simple-logger/angular-simple-logger.min",
        "angular-file-upload": "thirdparty/angular-file-upload/angular-file-upload.min",
        "file-saver": "thirdparty/file-saver/FileSaver.min",
        "zxcvbn": "../../webjars/zxcvbn/dist/zxcvbn"
    },
    priority: [
        'angular'
    ],
    packages: [

    ]
});

/*Il doppio require serve per importare angular prima di tutti gli altri componenti perché ad esempio con l'i18n
non è conveniente inserirlo nella configurazione inziale di requirejs dato che dovrebbe essere modificato il task
bowerRequirejs*/
// @ts-ignore
require(["tether", "leaflet", "leaflet-draw", "packery"], function (t, L, leafletDraw, p) {
    // @ts-ignore
    window.Tether = t;
    //  window.L = L;
    // @ts-ignore
    require(["angular", "tether", "bootstrap", "lodash", "constants"], function (angular, tether, bootstrap, _, constants) {
        // @ts-ignore
        require(["angular", "controllers", "services",
            "filters", "directives", "areas-ui",
            "../../webjars/angular-i18n/angular-locale_" + constants.locale.toLowerCase(),
            "i18n/moment." + constants.locale.toLowerCase(),
            "i18n/draw." + constants.locale.toLowerCase()
        ], function (angular, controller, services,
            filters, directives, areasUi,
            locale, momentLocale, drawLocale) {
            angular.module("filter", []);
            angular.module("service", []);
            angular.module("directive", ["service"]);
            angular.module("controller", ["filter", "directive"]);
            // @ts-ignore
            require(['angular', "angular-help-overlay", "areas-ui", 'app', 'angular-route', 'angular-cookies',
                'angular-sanitize', 'angular-resource', 'angular-animate',
                'angular-touch', 'angular-aria', 'angular-messages', 'ng-table',
                'angular-ui-router', 'angular-ui-grid', 'angular-bootstrap', "moment",
                "angular-block-ui", "angular-uiselect", "xeditable", "angular-bootstrap-confirm",
                "ui-leaflet", 'angular-ui-validate', "angular-chartjs", "angular-toastr",
                "bxslider", "angular-trix", "leaflet-control-boxzoom", 'angular-indexed-db',
                "ui-leaflet-draw", "angular-simple-logger", "angular-file-upload",
                "leaflet.markercluster", "file-saver"
            ], function (angular, angularHelpOverlay, areasUi, app,
                ngRoutes, ngCookies, ngSanitize, ngResource, ngAnimate, ngTouch,
                ngAria, ngMessages, ngTable, uiRouter, uiGrid, uiBootstrap, moment,
                blockUI, uiselect, xeditable, uiConfirm, uiLeaflet, uiValidate,
                chartjs, toastr, bxslider, angulartrix, leafletControlBoxzoom, angularIndexedDB,
                uiLeafletDraw, nemLogging, angularFileUpload, leafletKarkercluster,
                fileSaver) {

                //Se tlocale
                moment.locale(constants.locale);
                /* define innser angular modules */

                /* jshint ignore:end */
                angular.element().ready(function () {
                    angular.bootstrap(document, ["app"]);

                });
            }, function (error) {
                console.log("Errore caricamento moduli dell'applicazione qui sarebbe utile sbattere l'utente su una pagina html statica che lo guida al reload dell'applicazione");
            });
        });
    }, function (error) {
        console.log("Errore caricamento moduli dell'applicazione qui sarebbe utile sbattere l'utente su una pagina html statica che lo guida al reload dell'applicazione");
    });
}, function (error) {
    console.log("Errore caricamento moduli dell'applicazione qui sarebbe utile sbattere l'utente su una pagina html statica che lo guida al reload dell'applicazione");
});