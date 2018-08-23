require
	.config({
		shim: {
			bootstrap: {
				deps: ["tether",
					'angular',
					'jquery'
				]
			},
			angular: {
				exports: 'angular'
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
			'angular-bootstrap': '../../webjars/angular-bootstrap/ui-bootstrap-tpls',			
			moment: '../../webjars/moment/2.15.1/moment',
			'angular-block-ui': '../../webjars/angular-block-ui/dist/angular-block-ui',
			"xeditable": "thirdparty/xeditable/xeditable",
			"angular-uiselect": "../../webjars/angular-ui-select/dist/select.min",
			"lodash": "../../webjars/lodash/dist/lodash.min",
			"angular-bootstrap-confirm": "../../webjars/angular-bootstrap-confirm/dist/angular-bootstrap-confirm",
			"tether": "../../webjars/tether/dist/js/tether.min"

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
require(["tether"], function (t) {
	window.Tether = t;
	require(["angular", "tether", "bootstrap", "lodash"], function (angular, tether, bootstrap, _) {
		require(["angular", "../../webjars/angular-i18n/angular-locale_" + locale, "controllers", "services", "filters", "directives"], function (angular, locale, controller,
			services, filters, directives) {
			angular.module("filter", []);
			angular.module("directive", []);
			angular.module("controller", ["filter", "directive"]);
			angular.module("service", []);


			require(['angular', 'app', 'angular-route', 'angular-cookies',
				'angular-sanitize', 'angular-resource', 'angular-animate',
				'angular-touch', 'angular-aria', 'angular-messages', 'ng-table',
				'angular-ui-router', 'angular-bootstrap', "moment", "angular-block-ui", "angular-uiselect", "xeditable", "angular-bootstrap-confirm"], function (angular, app,
					ngRoutes, ngCookies, ngSanitize, ngResource, ngAnimate, ngTouch,
					ngAria, ngMessages, ngTable, uiRouter, uiBootstrap,  moment, blockUI, uiselect, xeditable, uiConfirm) {
					'use strict';
					/* define innser angular modules */

					/* jshint ignore:end */
					angular.element().ready(function () {
						angular.bootstrap(document, ["app"]);
					});
				});
		});
	});
});


