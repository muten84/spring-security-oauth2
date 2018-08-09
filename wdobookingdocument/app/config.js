/*configurazione del caricamento asincrono dei moduli qui dichiaro dove sono presenti  i moduli 
 * e che dipendenze hanno tra di loro*/
require.config({
	shim : {
		boot : {
			exports : "boot"
		},
		loginCtrl : {
			deps : [ "app" ]
		},
		apiService : {
			deps : [ "app" ]
		}
	},
	paths : {
		"domReady" : 'bower_components/domReady/domReady',
		"boot" : "boot",
		app : "scripts/app",
		loginCtrl : "scripts/controllers/login",
		apiService : "scripts/services/api",
		aboutCtrl : "scripts/controllers/about",
		listCtrl : "scripts/controllers/list"
	// aboutCtrl : "scripts/controllers/about",
	// mainCtrl : "scripts/controllers/main",
	// loginCtrl : "scripts/controllers/login",
	// listCtrl : "scripts/controllers/list",
	// testCtrl : "scripts/controllers/test",
	// apiService : "scripts/services/api"
	},
	packages : [

	]
});

/*
 * qui vanno richieste tutte le dipendenze del progetto se non faccio il require
 * è il modulo non verrà mai scaricato
 */
require(
		[ "domReady", "boot", "app", "loginCtrl", "apiService", "aboutCtrl",
				"listCtrl" ],
		function(domReady, boot, app, loginCtrl) {

			'use strict';
			domReady(function() {
				angular
						.element(document)
						.ready(
								function() {
									console
											.info("Il DOM è pronto passo al bootstrap di angular: "
													+ angular);

									setTimeout(
											function() {
												var err = false;
												try {
													angular.bootstrap(document,
															[ "app" ]);
												} catch (e) {
													err = true;
												}
												if (!err) {
													console
															.log("Application caricata correttamente.... ");
												}
											}, 500);

								});

			});

		});