'use strict';

/**
 * @ngdoc service
 * @name testAppApp.api
 * @description # api Factory in the testAppApp.
 */
define(['angular', "lodash"], function (angular, _) {

	angular.module("service").factory('apiService', ["$http", "loggerService", "$q", "$location", function ($http, logger, $q, $location) {

		var timeout = 10000;

		function* idGenerator() {
			var index = 0;
			while (index < Number.MAX_SAFE_INTEGER)
				yield index++;
		}
		var autoId = idGenerator();

		/**
		 * Restituisce solo il data dell'oggetto HTTP
		 * 
		 * @param {Object}
		 *                response
		 * @returns {Object}
		 */
		var _handleSuccess = function (response) {
			logger.debug("success : " + response.data);
			return $q.resolve(response.data);
		};

		/**
		 * Restituisce l'oggetto d'errore così come me lo ha parsato
		 * l'interceptor
		 * 
		 * @param {Object}
		 *                response
		 * @returns {Object}
		 */
		var _handleError = function (response) {
			logger.debug("error : " + response);
			return $q.reject(response);
		};

		var _handleEvents = function (response) {
			var events = [];
			var cnt = 0;
			response.data.forEach(function (e) {
				events[cnt] = angular.fromJson(e.eventData);
				cnt++;
			})
			return $q.resolve(events);
		}

		var _handleMunicipalities = function (response) {
			if (!Array.isArray(response.data)) {
				var arr = [];
				if (_.isEmpty(response.data)) {
					$q.reject(response);
					return;
				}
				arr[0] = response.data;
				return $q.resolve(arr);
			}
			else {
				return $q.resolve(response.data);
			}
		};

		var api = {};

		var _getUrlBase = function () {
			var protocol = $location.protocol();
			var host = $location.host();
			var port = $location.port();
			if (devMode) {
				port = extPort;
			}
			if (port == "80" || port == "443") {
				return protocol + "://" + host;
			}
			else {
				return protocol + "://" + host + ":" + port + "/";
			}
		}

		var _prepareRequest = function (method, op) {
			var options = {
				method: method,
				url: _getUrlBase() + contextApp + "/" + restBasePath + "/" + op,
				headers: {
					"Content-Type": "application/json"
				},
				timeout: timeout
			};
			return options;
		};

		var _createEventDR = function (ev) {
			/*{
				"creationDate": "2016-10-25T08:26:57.302Z",
					"emergencyId": "string",
						"eventData": "string",
							"id": "string",
								"terminalId": "string",
									"userId": "string"
			}*/
			if (_.isEmpty(ev.emergencyId)) {
				ev.emergencyId = ev.id;
			}
			var evJson = angular.toJson(ev);
			var newDR = {
				emergencyId: ev.emergencyId,
				eventData: evJson,
				terminalId: "127.0.0.1",
				userId: "USER"
			};

			return newDR;
		}

		api.getBaseUrl = function () {
			return _getUrlBase();
		}

		api.loadEvents = function (callback) {
			var options = _prepareRequest("GET", "/allEventsDr");
			var request = $http(options);
			return request.then(_handleEvents, _handleError);
		}

		api.searchStreet = function (municipality, locality, name) {
			/*if (_.isEmpty(name)) {
				return $q.reject();
			}
			var options = _prepareRequest("GET", "/StreetByName?name=" + name + "&municipalityName=" + municipality);
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);*/
			if (_.isEmpty(name) && (_.isEmpty(municipality) || _.isEmpty(locality))) {
				return $q.reject();
			}
			if (_.isEmpty(name) || name.length <= 3){
				return $q.reject();
			}
			var filter = {
				"localityName": locality,
				"municipalityName": municipality,
				"name": name
			}
			var options = _prepareRequest("POST", "/StreetByNameOrMunicipalityOrLocality");
			options.data = filter;
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.searchMunicipalities = function (name) {
			if (_.isEmpty(name)) {
				return $q.reject();
			}
			var options = _prepareRequest("GET", "/MunicipalityByName?name=" + name);
			var request = $http(options);
			return request.then(_handleMunicipalities, _handleError);
		}

		api.searchLocalities = function (name, municipalityName) {
			if (_.isEmpty(name)) {
				return $q.reject();
			}
			/*var options = _prepareRequest("GET", "/LocalityByName?name=" + name);
			var request = $http(options);
			return request.then(_handleMunicipalities, _handleError);*/
			var filter = {
				"municipalityName": municipalityName,
				"name": name
			}
			var options = _prepareRequest("POST", "/LocalityByNameOrMunicipality");
			options.data = filter;
			var request = $http(options);
			return request.then(_handleMunicipalities, _handleError);
		}

		api.getEventByEmergencyId = function (emergencyId) {
			var options = _prepareRequest("GET", "/eventDrByEmergencyId?emergencyId=" + emergencyId);
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.saveEvent = function (data) {
			var newEvent = _createEventDR(data);
			var options = _prepareRequest("POST", "/saveEventDr");
			options.data = newEvent;
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.searchVehicle = function (vehicle) {
			if (_.isEmpty(vehicle)) {
				return $q.reject();
			}
			var options = _prepareRequest("GET", "/VehicleTurnByName?name=" + vehicle);
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.listStaticData = function (type) {
			var options = _prepareRequest("GET", "/StaticDataByType?type=" + type);
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.searchHospital = function (hosp) {
			var options = _prepareRequest("GET", "/hospitalByName?name=" + hosp);
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.searchPhones = function(desc){			
			if (_.isEmpty(desc) || desc.length < 3) {
				return $q.reject();
			}
			var options = _prepareRequest("GET", "/AddressBookByName?name=" + desc);
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}

		api.countEmergencies = function(){
			var options = _prepareRequest("GET", "/countEmergencies");
			var request = $http(options);
			return request.then(_handleSuccess, _handleError);
		}
		
		return api;
	}]);

});
