'use strict';

define(
    ['angular', "moment", "lodash", "constants"],
    function (angular, moment, _, constants) {
        angular.module("controller").controller('UserInsertCtrl', [
            "$scope", "loggerService", "$rootScope",
            "anagraficaService", "$state", 'notificationService',
            "validateService", "currentUser", "userService",
            "modalService", "$document", "authenticationService",
            "toastr", "FileUploader", "$window",
            function ($scope, loggerService, $rootScope,
                anagraficaService, $state, notificationService,
                validateService, currentUser, userService,
                modalService, $document, authenticationService,
                toastr, FileUploader, $window) {

                $rootScope.$broadcast("main.changeTitle", {
                    title: "Gestione Utente",
                    icon: "fa fa-user"
                });

                angular.extend($scope, {
                    user: {
                        gruppi: []
                    },
                    comuni: [],
                    strade: [],
                    ruoli: [],
                    gruppi: [],
                    passwordStreght: 0,
                    passwordStreghtLabel: "Debole",
                    passwordStreghtType: "danger",
                    //gestione invio immagine
                    // see :https://github.com/nervgh/angular-file-upload
                    uploader: new FileUploader(),
                    showUpload: true,
                    //Funzione di validazione : False se il valore non è valido, True se è valido
                    pulisci: function () {
                        modalService.openYesNoModal({
                            title: "Attenzione",
                            text: "Tutti i dati sulla maschera verranno persi",
                            ok: function () {
                                $scope.user = {
                                    gruppi: []
                                };
                            },
                            cancel: function () {}
                        });
                    },
                    validate: validateService.validate,
                    validatePassword: function (value) {
                        if ($scope.user.id) {
                            //se si sta modificando un utente esistente non valido la password
                            return true;
                        }

                        var result = validateService.validatePassword(value);
                        $scope.passwordStreght = result.score;
                        $scope.passwordStreghtType = result.level;
                        return result.valid;
                    },
                    validateRepeatPassword: function (value) {
                        if ($scope.user.id) {
                            //se si sta modificando un utente esistente non valido la password
                            return true;
                        }
                        return value === $scope.user.password;
                    },
                    checkChildren: function (id) {
                        var invalids = $document.find('#' + id + ' .ng-invalid');
                        if (invalids && !_.isEmpty(invalids)) {
                            return false;
                        } else {
                            return true;
                        }
                    },
                    refreshComune: function (input) {
                        if (_.isUndefined(input) || input.length < 3) {
                            $scope.comuni = [];
                            return [];
                        }

                        var filter = {
                            "nomeComune": input
                        };
                        return anagraficaService.searchComuniByFilter(filter).then(function (response) {
                            $scope.comuni = response;
                            return $scope.comuni;
                        });
                    },
                    refreshStrade: function (search) {
                        if (_.isUndefined(search) || search.length < 3) {
                            $scope.strade = [];
                            return [];
                        }
                        var filter = {
                            "name": search
                        };
                        if ($scope.user.comuneResidenza) {
                            filter.nomeComune = $scope.user.comuneResidenza.nomeComune;
                        }
                        return anagraficaService.searchStradeByFilter(filter).then(function (response) {
                            /*debugger;*/
                            $scope.strade = response;
                        });
                    },
                    aggiungiGruppo: function () {
                        if ($scope.user.gruppo) {
                            //cerco nell'array la presenza del gruppo
                            if (!_.find($scope.user.gruppi, {
                                    id: $scope.user.gruppo.id
                                })) {
                                $scope.user.gruppi.push($scope.user.gruppo);
                                $scope.user.gruppo = null;
                            }
                        }
                    },
                    rimuoviGruppo: function (gruppo, index) {
                        _.pullAt($scope.user.gruppi, [index]);
                    },
                    resetPassword: function () {
                        authenticationService.resetPassword($scope.user.email).then(function () {
                            modalService.openModal("warningModal.html", "Attenzione", {
                                message: {
                                    title: "Invio mail Reset Password",
                                    text: "è stata inviata la mail del reset della password all'utente"
                                }
                            });
                        });
                    },
                    setUser: function (user) {
                        //creo l'url per l'upload dell'immagine
                        $scope.showUpload = true;
                        $scope.uploader.url = userService._getUrlService() + 'uploadImmagine/' + user.id;

                        if (user.immagine.url) {
                            //serve per evitare il cache dell'immagine da parte del browser
                            user.immagine.url = user.immagine.url + '?t=' + Date.now();
                        }

                        $scope.uploader.headers = {
                            authorization: "Bearer {{TOKEN}}".replace('{{TOKEN}}', $window.sessionStorage.token)
                        };

                        if (user.ruoli) {
                            user.ruolo = user.ruoli[0];
                        }
                        $scope.user = user;
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

                        if ($scope.user.gruppi.length == 0) {
                            modalService.openModal("warningModal.html", "Attenzione", {
                                message: {
                                    title: "Verifica dati inseriti",
                                    text: "Aggiungere almeno un gruppo all'utente"
                                }
                            });
                            return;
                        }

                        var user = $scope.user;

                        if (user.ruolo) {
                            user.ruoli = [user.ruolo];
                        }

                        userService.saveUtente(user).then(function (response) {
                            toastr.success('Salvataggio dei dati avvenuto con successo. Accertarsi che il profilo appena configurato corrisponda alle specifiche richieste.', 'Salvataggio avvenuto');
                            $scope.setUser(response);
                        });
                    },
                    annullaInvioImmagine: function () {
                        $scope.showUpload = true;
                        $scope.uploader.clearQueue();
                    }
                });

                angular.extend($scope.uploader, {

                    onAfterAddingFile: function () {
                        $scope.showUpload = false;
                    },
                    onBeforeUploadItem: function () {
                        //appena parte l'upload tolgo l'immagine
                        $scope.user.immagine.urlBkp = $scope.user.immagine.url;
                        $scope.user.immagine.url = null;
                    },
                    //errore nell'upload
                    onErrorItem: function () {
                        $scope.uploader.clearQueue();
                        $scope.user.immagine.url = $scope.user.immagine.urlBkp;
                        toastr.error("Errore nell'invio dell'immagine al server", "Errore");
                    },
                    //upload completato
                    onCompleteAll: function () {
                        toastr.success("Immagine inviata al server", "Salvataggio");
                        $scope.uploader.clearQueue();
                        //dopo l'invio dell'immagine ricarico il dae
                        var filter = {
                            id: $scope.user.id
                        };

                        userService.searchUtenteByFilter(filter).then(function (result) {
                            $scope.setUser(result[0]);
                        });
                    }
                });

                /* Combo Ruoli*/
                userService.getAllRuoli().then(function (response) {
                    $scope.ruoli = response;
                });

                /* Combo Gruppi*/
                userService.getAllGruppi().then(function (response) {
                    $scope.gruppi = response;
                });

                //Carico L'utente dal db e riempio la maschera
                if (!_.isUndefined(currentUser)) {
                    $scope.setUser(currentUser[0]);
                    userService.setCurrentUserId(undefined);
                }
            }
        ]);
    }
);