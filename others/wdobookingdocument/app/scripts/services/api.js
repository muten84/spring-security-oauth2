'use strict';

/**
 * @ngdoc service
 * @name testAppApp.api
 * @description # api Factory in the testAppApp.
 */
define(
  ['angular'],
  function (angular) {

    angular
      .module("service")
      .factory(
        'apiService', [
          "$http",
          "loggerService",
          "$q",
          "$location",
          function ($http, logger, $q, $location) {
            // Service logic
            // ...
            var baseUrl = "http://172.30.50.45:9292/sdoto-docservice-war/rest/"
            var timeout = 10000;

            /**
             * Restituisce solo il data dell'oggetto
             * HTTP
             * 
             * @param {Object}
             *            response
             * @returns {Object}
             */
            var _handleSuccess = function (response) {
              logger.debug("success : " +
                response.data);
              return $q.resolve(response.data);
            };

            /**
             * Restituisce l'oggetto d'errore così
             * come me lo ha parsato l'interceptor
             * 
             * @param {Object}
             *            response
             * @returns {Object}
             */
            var _handleError = function (response) {
              logger.debug("error : " + response);
              return $q.reject(response);
            };

            var api = {};

            var _getUrlBase = function () {
              var protocol = $location.protocol();
              var host = $location.host();
              var port = $location.port();
              if (devMode) {
                port = extPort;
                host = extHost;
              }
              if (port == "80" || port == "443") {
                return protocol + "://" + host + "/";
              } else {
                return protocol + "://" + host +
                  ":" + port + "/";
              }
            }

            var _prepareRequest = function (method,
              op) {
              var options = {
                method: method,
                url: _getUrlBase() +
                  contextApp + "/" +
                  restBasePath + "/" +
                  op,
                headers: {
                  "Content-Type": "application/json"
                },
                timeout: timeout
              };
              return options;
            };

            api.getBaseUrl = function () {
              return _getUrlBase();
            }

            api.logout = function () {
              var options = _prepareRequest(
                "GET",
                "documentService/logout");
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
            }

            api.checkRenew = function () {
              var options = _prepareRequest(
                "GET",
                "documentService/needToRenew/40");
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
            }

            api.changePassword = function (
              credentials) {
              var options = _prepareRequest(
                "POST",
                "documentService/changePassword");
              options.data = {
                username: credentials.username,
                password: credentials.newPassword
              };
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
            }

            api.authenticate = function (credentials) {
              var options = _prepareRequest(
                "POST",
                "documentService/authenticateUser");
              logger.info(
                "Invoking listDocuments: ",
                options);
              options.data = {
                username: credentials.username,
                password: credentials.password
              };
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
            }

            api.checkAuthenticated = function () {
              var options = _prepareRequest(
                "GET",
                "documentService/test");
              logger.info("Invoking test: ",
                options);
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
            }

            // Public API here
            api.test = function () {
              // return
              // $resource('/rest/documentService/test');
              // // Note
              // the full
              // endpoint address
              var options = _prepareRequest(
                "GET",
                "documentService/test");
              logger.info("Invoking test: ",
                options);
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
              // return
              // $http.get(baseUrl+"documentService/test");
            };

            /*
             * { "fromDate":
             * "2016-10-10T08:54:36.692Z", "toDate":
             * "2016-10-10T08:54:36.692Z",
             * "inState": "CREATED", "currentState":
             * "CREATED", "excludeInCurrentState":
             * "CREATED", "parking": "string",
             * "exactParkingMatch": false,
             * "docReference": "string" }
             */
            api.listDocuments = function (data) {
              var options = _prepareRequest(
                "POST",
                "documentService/listDocuments");
              logger.info(
                "Invoking listDocuments: ",
                options);
              if (data.currentState == "all" ||
                data.inState == "all") {
                data.currentState = null;
                data.inState = null;
              } else {
                /*
                 * data.excludeInCurrentState =
                 * "CLOSED";
                 */
              }
              options.data = data;
              var request = $http(options);
              return request.then(_handleSuccess,
                _handleError);
            };

            api.openDocument = function (docId) {
              /*
               * var options =
               * _prepareRequest("GET",
               * "documentService/openDocument/"+docId);
               * var request = $http(options);
               * return
               * request.then(_handleSuccess,
               * _handleError);
               */
              var url = _getUrlBase() +
                contextApp + "/" +
                restBasePath + "/";
              return $http
                .get(
                  url +
                  "documentService/openDocument/" +
                  docId, {
                    responseType: 'arraybuffer'
                  });

            }

            api.validateNewPassword = function (
              credentials) {
              // Restituisce un valore numerico
              // che indica la qualità della
              // password
              // 0 - 30 password inutile
              // 31 - 60 debole
              // 61 - 80 buona
              // 81 in su ottima
              debugger;
              var pass = credentials.newPassword;

              var score = 0;
              if (!pass)
                return score;

              if (pass.length < 8) {
                return score;
              }

              // award every unique letter until 5
              // repetitions
              var letters = {};
              for (var i = 0; i < pass.length; i++) {
                letters[pass[i]] = (letters[pass[i]] || 0) + 1;
                score += 5.0 / letters[pass[i]];
              }

              // bonus points for mixing it up
              var variations = {
                digits: /\d/.test(pass),
                lower: /[a-z]/.test(pass),
                upper: /[A-Z]/.test(pass),
                //nonWords: /\W/.test(pass),
              };

              var variationCount = 0;
              for (var check in variations) {
                variationCount += (variations[check]) ? 1 :
                  0;
              }
              score += (variationCount - 1) * 10;
              score = parseInt(score);

              var resultLevel = "";

              if (score > 100) {
                score = 100;
              }
              if (score > 80) {
                resultLevel = 'success';
              } else if (score > 60) {
                resultLevel = 'info';
              } else if (score >= 30) {
                resultLevel = 'warning';
              } else {
                resultLevel = 'danger';
              }

              return {
                score: score,
                level: resultLevel,
                valid: score >= 60
              };
            };

            return api;
          }
        ]);

  });
