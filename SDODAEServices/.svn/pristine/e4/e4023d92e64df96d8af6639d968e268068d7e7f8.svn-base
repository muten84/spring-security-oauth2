'use strict';

/**
 * @ngdoc service
 * @name
 * @description
 */
define(['angular', "lodash", "constants", "zxcvbn"], function(angular, _, constats, zxcvbn) {

    angular.module("service").factory('validateService', ["$http", "loggerService", "$q", "$location", 'abstractSerivce',
        function($http, logger, $q, $location, abstractSerivce) {
            var api = {};
            api.validate = function(value) {
                //se value è undefined restituisco false
                if (_.isUndefined(value)) {
                    return false;
                }
                //se è un numero vuol dire che è validato
                if (_.isNumber(value)) {
                    return true;
                }
                //se è una data
                if (_.isDate(value)) {
                    return true;
                }
                //  se non è un numero vuol dire che è un oggetto o una stringa
                return !_.isEmpty(value);
            };

            api.validateLocation = function(loc) {
                if (_.isUndefined(loc) || loc === null) {
                    return false;
                }
                return this.validateCoord(loc.latitudine) && this.validateCoord(loc.longitudine);
            };

            api.validateCoord = function(coord) {
                if (!_.isNumber(coord)) {
                    if (_.isEmpty(coord)) {
                        return false;
                    } else {
                        coord = Number(coord);
                    }
                }
                if (isNaN(coord) || coord < 0 || coord > 360) {
                    return false;
                }
                return true;
            };

            //  0 # too guessable: risky password. (guesses < 10^3)
            //  1 # very guessable: protection from throttled online attacks. (guesses < 10^6)
            //  2 # somewhat guessable: protection from unthrottled online attacks. (guesses < 10^8)
            //  3 # safely unguessable: moderate protection from offline slow-hash scenario. (guesses < 10^10)
            //  4 # very unguessable: strong protection from offline slow-hash scenario. (guesses >= 10^10)

            api.validatePassword = function(pass) {
                var score = 0;
                var resultLevel = 'danger';

                if (pass && pass.length >= 8) {
                    score = 10;
                    var result = zxcvbn(pass);

                    if (result.score == 4) {
                        score = 100;
                        resultLevel = 'success';
                    } else if (result.score == 3) {
                        score = 80;
                        resultLevel = 'success';
                    } else if (result.score == 2) {
                        score = 60;
                        resultLevel = 'info';
                    } else if (result.score == 1) {
                        score = 30;
                        resultLevel = 'warning';
                    }
                }
                return {
                    score: score,
                    level: resultLevel,
                    valid: score >= 60
                };
            };

            //Restituisce un valore numerico che indica la qualità della password
            api.validatePasswordPrivacy = function(pass, regexpList) {
                var score = 0;
                var resultLevel = 'danger';

                if (pass && pass.length >= 8) {
                    score = 10;
                    if( pass.match(regexpList[0]) && pass.match(regexpList[1]) && pass.match(regexpList[2]) ) {
                        score = 100;
                        resultLevel = 'success';
                    }
                }
                return {
                    score: score,
                    level: resultLevel,
                    valid: score >= 60
                };
            };


            // 0  - 30 password inutile
            // 31 - 60 debole
            // 61 - 80 buona
            // 81 in su ottima
            api.validatePasswordOld = function(pass) {
                var score = 0;
                var resultLevel = 'danger';

                if (!pass || pass.length < 8) {
                    return {
                        score: score,
                        level: resultLevel,
                        valid: score >= 30
                    };
                }
                // award every unique letter until 5 repetitions
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
                    nonWords: /\W/.test(pass),
                };

                var variationCount = 0;
                for (var check in variations) {
                    variationCount += (variations[check]) ? 1 : 0;
                }
                score += (variationCount - 1) * 10;
                score = parseInt(score);

                if (score > 100) {
                    score = 100;
                }
                if (score > 80) {
                    resultLevel = 'success';
                } else if (score > 60) {
                    resultLevel = 'info';
                } else if (score >= 30) {
                    resultLevel = 'warning';
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
