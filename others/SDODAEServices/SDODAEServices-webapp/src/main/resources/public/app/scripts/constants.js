//Configurazioni utilizzate dal client
'use strict';

define([], function () {
	return {
		locale: "it-IT",
		contextApp: ".",
		devMode: false,
		devContextApp: "SDODAEServices-webapp",
		host: "127.0.0.1",
		port: "8080",
		restBasePath: "rest/portal",
		expiresInDays: 1,
		centralName: "BO",
		bypassLogin: false,
		// nome del database indexedDB
		dbName: "daeDB",
		// nome dello store indexedDB dove vengono caricati i grafici
		storeNameService: "services",
		storeNameDAE: "dae",
		storeNameFR: "fr",
		storeNameUser: "user",
		// formato delle date su excel
		exelDate: "DD/MM/YYYY HH:mm",
		// numero massimo di minuti di validità dei dati in cache
		maxMinutesCache: 120,
		daeMapIcon: {
			iconUrl: 'images/dae_pin.png',
			iconSize: [40, 37], // size of the icon
			iconAnchor: [20, 38], // point of the icon which will
			// correspond to
			// marker's location
			popupAnchor: [-3, -40]
			// point from which the popup should open
			// relative to the iconAnchor
		},
		daeMapIconNotAvailable: {
			iconUrl: 'images/dae_pin_n.png',
			iconSize: [40, 37], // size of the icon
			iconAnchor: [20, 38], // point of the icon which will
			// correspond to
			// marker's location
			popupAnchor: [-3, -40]
			// point from which the popup should open
			// relative to the iconAnchor
		},
		daeMapIconUndefined: {
			iconUrl: 'images/dae_pin_i.png',
			iconSize: [40, 37], // size of the icon
			iconAnchor: [20, 38], // point of the icon which will
			// correspond to
			// marker's location
			popupAnchor: [-3, -40]
			// point from which the popup should open
			// relative to the iconAnchor
		},
		frMapIcon: {
			iconUrl: 'images/fr.png',
			iconSize: [45, 45], // size of the icon
			iconAnchor: [22, 45], // point of the icon which will
			// correspond to
			// marker's location
			popupAnchor: [-3, -40]
			// point from which the popup should open
			// relative to the iconAnchor
		},
		eventMapIcon: {
			iconUrl: 'images/event.png',
			iconSize: [32, 32], // size of the icon
			iconAnchor: [0, 32], // point of the icon which will correspond
			// to
			// marker's location
			popupAnchor: [-3, -40]
			// point from which the popup should open
			// relative to the iconAnchor
		},
		responseTimeoutinMillis: 60000,
	};
});