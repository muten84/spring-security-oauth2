'use strict';
define(
    ['angular', 'moment', 'lodash', 'constants'],
    function (angular, moment, _, constants) {
        angular.module('controller').controller('UserProfileCtrl', [
            '$scope', 'loggerService', '$rootScope',
            'anagraficaService', '$state', 'notificationService',
            'userService', 'validateService', '$document',
            '$uibModal', 'modalService', 'toastr', 'configurationService',
            function ($scope, loggerService, $rootScope,
                anagraficaService, $state, notificationService,
                userService, validateService, $document,
                $uibModal, modalService, toastr, configurationService) {
                $rootScope.$broadcast('main.changeTitle', {
                    title: 'Gestione Profilo',
                    icon: 'fa fa-user'
                });

                angular.extend($scope, {
                    comuni: [],
                    strade: [],
                    validate: validateService.validate,
                    cambioPassword: function () {
                        // visualizzo la popup per il cambio password
                        var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                        var modalInstance = $uibModal.open({
                            animation: true,
                            templateUrl: 'passwordChangeModal.html',
                            appendTo: parentElem,
                            controller: function ($scope, $uibModalInstance) {
                                var $ctrl = this;
                                $scope.validatePassword = function (value) {
                                	if( $scope.configurations === undefined ) {
	                                	configurationService.getAllConfiguration().then(function(conf) {
		          							$scope.configurations = conf;
		          							for (var i = 0; i < $scope.configurations.length; i++) {
		          								if($scope.configurations[i].parametro === 'ENABLE_PRIVACY') {
		          									$scope.enable_privacy = $scope.configurations[i].valore.toLowerCase() === 'true';
		          								}
		          								if($scope.configurations[i].parametro === 'REGEXP_PWD') {
		          									$scope.regexp_pwd = $scope.configurations[i].valore;
		          								}
		          								if($scope.configurations[i].parametro === 'REGEXP_PWD_FOUR_CHAR') {
		          									$scope.regexp_pwd_four_char = $scope.configurations[i].valore;
		          								}
		          								if($scope.configurations[i].parametro === 'REGEXP_PWD_DOUBLE_CHAR_MAX') {
		          									$scope.regexp_pwd_double_char_max = $scope.configurations[i].valore;
		          								}
		          							}
	                                    });
									}                                    
          							if( $scope.enable_privacy ) {
          								var regexpList = [$scope.regexp_pwd, $scope.regexp_pwd_four_char, $scope.regexp_pwd_double_char_max];
          								
                                    	var result = validateService.validatePasswordPrivacy(value, regexpList);
          							
          							} else {
                                    	var result = validateService.validatePassword(value);
                                	}
                                    $scope.passwordStreght = result.score;
                                    $scope.passwordStreghtType = result.level;
                                    return result.valid;
                                };
                                $scope.validateRepeatPassword = function (value) {
                                    return value === $scope.password;
                                };
                                $scope.salvaPassword = function () {
                                    if ($scope.oldPassword && $scope.password) {
                                        userService.changePassword($scope.oldPassword, $scope.password).then(function (result) {
                                            if (result.response) {
                                                $scope.$dismiss('cancel');
                                                toastr.success("Cambio Password", "Cambio password avvenuto con successo. Ricorda di non comunicare a nessuno la tua password!");
                                            } else {
                                                modalService.openModal("warningModal.html", "Attenzione", {
                                                    message: {
                                                        title: "Verifica dati inseriti",
                                                        text: "Attenzione cambio password non avvenuto"
                                                    }
                                                });
                                            }
                                        });
                                    }
                                };
                            }
                        });
                    },
                    saveUser: function () {
                        if ($scope.userForm.$invalid) {
                            /*debugger;*/
                            modalService.openModal("warningModal.html", "Attenzione", {
                                message: {
                                    title: "Verifica dati inseriti",
                                    text: "Attenzione verificare i dati della form"
                                }
                            });
                            return;
                        }
                        var user = $scope.user;
                        userService.saveUtenteProfilo(user).then(function (response) {
                            toastr.success('Salvataggio dei dati avvenuto con successo', 'Salvataggio avvenuto');
                            $scope.user = response;
                            $rootScope.$broadcast("user.changeProfile", {
                                data: response
                            });
                        });
                    },
                    refreshComune: function (input) {
                        if (_.isUndefined(input)) return [];
                        if (input.length < 3) return [];
                        var filter = {
                            "nomeComune": input
                        };
                        return anagraficaService.searchComuniByFilter(filter).then(function (response) {
                            $scope.comuni = response;
                            return $scope.comuni;
                        });
                    },
                    refreshStrade: function (search) {
                        if (_.isUndefined(search)) return [];
                        if (search.length < 3) return [];
                        var filter = {
                            "name": search
                        };
                        if ($scope.model.comuneResidenza) {
                            filter.nomeComune = $scope.model.comuneResidenza.nomeComune;
                        }
                        return anagraficaService.searchStradeByFilter(filter).then(function (response) {
                            /*debugger;*/
                            $scope.strade = response;
                        });
                    },
                });
                $scope.selectMunicipality = function (mun) {

                    //if (!mun) {
                    $scope.user.indirizzo = undefined;
                    //}
                };
                userService.getLoggedUtenteDetails().then(function (user) {
                    $scope.user = user;
                });
            }
        ]);
    });