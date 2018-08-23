appControllers.controller('NewDaeCtrl', [
    '$rootScope',
    '$scope',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    '$cordovaDatePicker',
    'deviceService',
    '$translate',
    '$cordovaCamera',
    '$cordovaGeolocation',
    '$state',
    '$timeout',
    '$window',
    '$cordovaKeyboard',
    '$mdSelect',
    '$mdDialog',
    '$templateCache',
    function ($rootScope, $scope, logger, api, user, $timeout, $cordovaDatePicker, device, $translate, $cordovaCamera, $cordovaGeolocation, $state, $timeout, $window, $cordovaKeyboard, $mdSelect, $mdDialog, $templateCache) {
        logger.info("Caricamento NewDaeCtrl...");

        var vm = this;

        vm.getPicture = getPicture;
        vm.localize = localize;
        vm.addNewDae = addNewDae;
        vm.resetModel = resetModel;

        vm.insertInProgress = false;

        $scope.$on('$ionicView.enter', function () {
            vm.resetModel();
            device.hideLoader();
            vm.photo = false;
            vm.localize = false;
            initializeMonthAvailability();
        });

        function initializeMonthAvailability() {
            var now = new Date();
            var year = now.getFullYear();
            var firstDay = new Date(year, 0, 1);
            var lastDay = new Date(year, 0, 0);
            vm.dae.disponibilita[0].da = new Date(firstDay.getTime() + (-1) * firstDay.getTimezoneOffset() * 60000).getTime();
            vm.dae.disponibilita[0].a = new Date(lastDay.getTime() + (-1) * lastDay.getTimezoneOffset() * 60000).getTime();
        }

        function getPicture() {
            device.showLoader();
            if (device.isCordova()) {
                var sourceType = Camera.PictureSourceType.CAMERA;
                var options = {
                    quality: 50,
                    destinationType: Camera.DestinationType.DATA_URL,
                    sourceType: sourceType,
                    targetHeight: 1024,
                    targetWidth: 768,
                    allowEdit: false,
                    encodingType: Camera.EncodingType.JPEG,
                    popoverOptions: CameraPopoverOptions,
                    saveToPhotoAlbum: false,
                    correctOrientation: true
                };

                $cordovaCamera.getPicture(options).then(
                    function (imageData) {
                        device.hideLoader();
                        vm.photo = true;
                        var image = document.getElementById('photoDae');
                        image.src = "data:image/jpeg;base64," + imageData;
                        vm.dae.immagine.data = imageData;
                    },
                    function (err) {
                        logger.error("newDaeCtrl.getPicture error", err);
                        device.hideLoader();
                    });

            }
            else {
                device.hideLoader();
                device.alert("Datepicker non disponibile", "balanced");
            }
        }

        function localize() {
            cordova.plugins.diagnostic.isLocationAvailable(
                function (status) {
                    if (status == "GRANTED") {
                        getCurrentPosition();
                    }
                    else {
                        cordova.plugins.diagnostic.requestLocationAuthorization(
                            function (status) {
                                if (status == "GRANTED") {
                                    getCurrentPosition();
                                } else {
                                    console.error(status);
                                }
                            },
                            function (error) {
                                console.error(error);
                            }
                        );
                    }
                },
                function (error) {
                    logger.info(error);
                }
            );

        }

        function getCurrentPosition() {
            var posOptions = {
                timeout: 10000,
                enableHighAccuracy: true,
                maximumAge: 0
            };

            $cordovaGeolocation
                .getCurrentPosition(posOptions)
                .then(function (position) {
                    logger.info(position);
                    var lat = position.coords.latitude;
                    var long = position.coords.longitude;
                    vm.dae.gpsLocation.latitudine = lat;
                    vm.dae.gpsLocation.longitudine = long;
                }, function (error) {
                    logger.error(error);
                });
        }

        function addNewDae() {
            device.showLoader();
            logger.info("[newDaeCtrl:addNewDae]");
            if (!vm.insertInProgress) {
                vm.insertInProgress = true;
                $timeout(function () {
                    api.addNewDae(vm.dae).then(
                        function (data) {
                            device.hideLoader();
                            logger.info(data);
                            if (data.esito) {
                                device.alert("Ti ringraziamo per la segnalazione. Attenzione: il DAE sarà visibile solo dopo aver superato la fase di validazione.", "primary", "DAE118ER").then(
                                    function () {
                                        vm.insertInProgress = false;
                                     //   $state.go("app.daeList");
                                        $scope.$ionicGoBack();
                                    }
                                );

                            }
                            else {
                                device.alert(data.messaggio, "accent", "DAE118ER").then(
                                    function () {
                                        vm.insertInProgress = false;
                                    }
                                );
                            }
                        },
                        function (error) {
                            device.hideLoader();
                            logger.error(error);
                            device.alert("Si è verificato un errore", "accent", "DAE118ER");
                        }
                    );
                }, 100);
            }
            // if (this.form.$valid) {
            //     this.$mdToast.show(this.$mdToast.simple().textContent('Submitted').position('left top').hideDelay(3000));
            // }
        }

        function resetModel() {
            vm.options.resetModel();
            vm.dae.disponibilita[0].disponibilitaSpecifica.disponibilitaGiornaliera = [];
            vm.dae.programmiManutenzione = [];
            vm.dae.gpsLocation.comune.provincia.id = null;
            vm.dae.gpsLocation.comune.provincia.nomeProvincia = null;
            vm.dae.gpsLocation.comune.id = null;
            vm.dae.gpsLocation.comune.nomeComune = null;
            vm.dae.gpsLocation.comune.codiceIstat = null;
            vm.dae.gpsLocation.comune.cap = null;
            vm.dae.gpsLocation.indirizzo.id = null;
            vm.dae.gpsLocation.indirizzo.name = null;
            var scope = angular.element(document.getElementsByTagName('md-autocomplete-wrap')).scope();
            if (scope) {
                scope.$mdAutocompleteCtrl.clear();
            }

            vm.photo = false;
            var image = document.getElementById('photoDae');
            image.src = "img/ico_telephone_2.png";
        }

        vm.dae = {
            operativo: true,
            matricola: null,
            modello: null,
            tipo: null,
            alias: null,
            certificatoDae: {
                descrizione: null,
                immagine: {
                    data: null
                },
                dataConseguimento: null,
                rilasciatoDa: null,
                dataScadenza: null,
                emailScadenza: null
            },
            tipologiaStruttura: {
                id: null,
                descrizione: null
            },
            nomeSede: null,
            responsabile: {
                nome: "N.D.",
                cognome: "N.D.",
                dataNascita: null,
                telefono: "N.D.",
                email: "nd@nd.it",
                indirizzoResidenza: null,
                comuneResidenza: null,
                codiceFiscale: null,
                whatsApp: false
            },
            ubicazione: "",
            disponibilitaPermanente: false,
            disponibilita: [
                {
                    id: null,
                    da: null,
                    a: null,
                    orarioDa: "00:00",
                    orarioA: "23:59",
                    disponibilitaSpecifica: {
                        id: null,
                        disponibilitaGiornaliera: [
                            // {
                            //     id: 0,
                            //     orarioDa: "00:00",
                            //     orarioA: "23:59",
                            //     giornoSettimana: "LUNEDI"
                            // }
                        ]
                    },
                    disponibilitaEccezioni: null
                }
            ],
            notediAccessoallaSede: null,
            gpsLocation: {
                comune: {
                    id: null,
                    nomeComune: null,
                    codiceIstat: null,
                    cap: null,
                    provincia: {
                        id: null,
                        nomeProvincia: null
                    }
                },
                indirizzo: {
                    id: null,
                    name: null
                },
                civico: null,
                luogoPubblico: null,
                tipoLuogoPubblico: null,
                latitudine: null,
                longitudine: null,
                altitudine: null,
                error: null,
                timeStamp: null
            },
            immagine: {
                data: null
            },
            scadenzaDae: null,
            programmiManutenzione: [
                // {
                //     id: null,
                //     intervallotraInterventi: 0,
                //     durata: null,
                //     scadenzaDopo: null,
                //     responsabile: {
                //         id: null,
                //         nome: null,
                //         cognome: null,
                //         indirizzo: null,
                //         civico: null,
                //         comune: null,
                //         tel: null,
                //         email: null
                //     },
                //     stato: null,
                //     nota: null,
                //     tipoManutenzione: null
                // }
            ],
            noteGenerali: null
            // currentStato: {
            //     id: null,
            //     nome: null
            // }
        };




        vm.options = {
            formState: {
                disponibilitaPermanente: false,
                da: {
                    id: 1,
                    name: "Gennaio"
                },
                a: {
                    id: 12,
                    name: "Dicembre"
                }
            }
        };

        function resetAddressModel() {
            vm.dae.gpsLocation.comune.provincia.id = null;
            vm.dae.gpsLocation.comune.provincia.nomeProvincia = null;
            vm.dae.gpsLocation.comune.id = null;
            vm.dae.gpsLocation.comune.nomeComune = null;
            vm.dae.gpsLocation.comune.codiceIstat = null;
            vm.dae.gpsLocation.comune.cap = null;
            vm.dae.gpsLocation.indirizzo.id = null;
            vm.dae.gpsLocation.indirizzo.name = null;
        }

        vm.daeFields = [
            {
                className: 'localize',
                type: "dae-localize",
                templateOptions: {
                    onclick: function ($modelValue, $options) {
                        var posOptions = {
                            timeout: 10000,
                            enableHighAccuracy: true,
                            maximumAge: 0
                        };
                        vm.dae.gpsLocation.latitudine = null;
                        vm.dae.gpsLocation.longitudine = null;
                        resetAddressModel();
                        vm.localize = true;
                        if (!device.isCordova()) {
                            var lat = "44.507215";
                            var long = "11.310268";
                            device.showLoader();
                            vm.dae.gpsLocation.latitudine = lat;
                            vm.dae.gpsLocation.longitudine = long;
                            api.reverseGeocoding(lat, long).then(
                                function (data) {
                                    if (data.strada) {
                                        vm.dae.gpsLocation.comune.provincia.id = data.strada.comune.provincia.id;
                                        vm.dae.gpsLocation.comune.provincia.nomeProvincia = data.strada.comune.provincia.nomeProvincia;
                                        vm.dae.gpsLocation.comune.id = data.strada.comune.id;
                                        vm.dae.gpsLocation.comune.nomeComune = data.strada.comune.nomeComune;
                                        vm.dae.gpsLocation.comune.codiceIstat = data.strada.comune.codiceIstat;
                                        vm.dae.gpsLocation.comune.cap = data.strada.comune.cap;
                                        vm.dae.gpsLocation.indirizzo.id = data.strada.id;
                                        vm.dae.gpsLocation.indirizzo.name = data.strada.name;
                                    }

                                    device.hideLoader();

                                },
                                function (error) {
                                    device.hideLoader();
                                    logger.info(error);
                                    device.alert(error.message, "primary", "DAE118ER")
                                }
                            );
                        }
                        else {
                            device.showLoader();
                            device.checkLocationAuthorizationStatus().then(
                                function (status) {
                                    $cordovaGeolocation
                                        .getCurrentPosition(posOptions)
                                        .then(function (position) {
                                            logger.info(position);
                                            var lat = position.coords.latitude;
                                            var long = position.coords.longitude;
                                            vm.dae.gpsLocation.latitudine = lat;
                                            vm.dae.gpsLocation.longitudine = long;
                                            api.reverseGeocoding(lat, long).then(
                                                function (data) {
                                                    if (data.strada) {
                                                        vm.dae.gpsLocation.comune.provincia.id = data.strada.comune.provincia.id;
                                                        vm.dae.gpsLocation.comune.provincia.nomeProvincia = data.strada.comune.provincia.nomeProvincia;
                                                        vm.dae.gpsLocation.comune.id = data.strada.comune.id;
                                                        vm.dae.gpsLocation.comune.nomeComune = data.strada.comune.nomeComune;
                                                        vm.dae.gpsLocation.comune.codiceIstat = data.strada.comune.codiceIstat;
                                                        vm.dae.gpsLocation.comune.cap = data.strada.comune.cap;
                                                        vm.dae.gpsLocation.indirizzo.id = data.strada.id;
                                                        vm.dae.gpsLocation.indirizzo.name = data.strada.name;
                                                    }

                                                    device.hideLoader();

                                                },
                                                function (error) {
                                                    device.hideLoader();
                                                    logger.info(error);
                                                    device.alert(error.message, "primary", "DAE118ER")
                                                }
                                            );
                                        }, function (error) {
                                            logger.error(error);
                                            device.alert("Connessione dati o GPS non attivi", "primary", "DAE118ER")
                                        });
                                },
                                function () {
                                    device.alert("Devi abilitare la localizzazione GPS", "primary", "DAE118ER")
                                });
                        }
                    }
                }
            },
            {
                className: 'layout-row',
                fieldGroup: [
                    {
                        className: "flex-50 input-green",
                        key: "gpsLocation.latitudine",
                        type: "input",
                        templateOptions: {
                            type: "text",
                            label: "Latitudine",
                            placeholder: "",
                            disabled: true,
                            required: true
                        }
                    },
                    {
                        className: "flex-50 input-green",
                        key: "gpsLocation.longitudine",
                        type: "input",
                        templateOptions: {
                            type: "text",
                            label: "Longitudine",
                            placeholder: "",
                            disabled: true,
                            required: true
                        }
                    }
                ]
            },
            {
                className: "input-green",
                type: "select",
                key: "id",
                model: vm.dae.gpsLocation.comune.provincia,
                templateOptions: {
                    label: "Provincia",
                    placeholder: "",
                    labelProp: "nomeProvincia",
                    valueProp: "id",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.getAllProvincie().then(
                            function (data) {
                                scope.to.options = data;
                            },
                            function (error) {
                                logger.info(error);
                                $mdSelect.hide();
                                device.alert(error.message, "balanced");
                            }
                        );
                    }
                },
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            if (field.templateOptions.options.length > 0) {
                                scope.model.gpsLocation.comune.provincia.nomeProvincia = _.find(field.templateOptions.options, function (o) {
                                    return o.id == newValue;
                                }).nomeProvincia;
                            }
                            else {
                                field.templateOptions.options = [
                                    {
                                        id: vm.dae.gpsLocation.comune.provincia.id,
                                        nomeProvincia: vm.dae.gpsLocation.comune.provincia.nomeProvincia
                                    }
                                ]
                            }
                        }
                    }
                }
            },
            {
                className: "input-green",
                type: "select",
                key: "id",
                model: vm.dae.gpsLocation.comune,
                templateOptions: {
                    label: "Comune",
                    placeholder: "",
                    labelProp: "nomeComune",
                    valueProp: "id",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.searchComuniByFilter(scope.model.provincia.nomeProvincia).then(
                            function (data) {
                                scope.to.options = data;
                            },
                            function (error) {
                                logger.info(error);
                                $mdSelect.hide();
                                device.alert(error.message, "balanced");
                            }
                        );
                    }
                },
                hideExpression: '!model.provincia.id',
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            if (field.templateOptions.options.length > 0) {
                                var obj = _.find(field.templateOptions.options, function (o) {
                                    return o.id == newValue;
                                });
                                scope.model.gpsLocation.comune.id = obj.id;
                                scope.model.gpsLocation.comune.nomeComune = obj.nomeComune;
                                scope.model.gpsLocation.comune.codiceIstat = obj.codiceIstat;
                            }
                            else {
                                field.templateOptions.options = [
                                    {
                                        id: vm.dae.gpsLocation.comune.id,
                                        nomeComune: vm.dae.gpsLocation.comune.nomeComune
                                    }
                                ]
                            }
                        }
                    }
                }
            },
            // {
            //     className: "input-green",
            //     key: "nomeProvincia",
            //     model: vm.dae.gpsLocation.comune.provincia,
            //     type: "autocomplete",
            //     templateOptions: {
            //         type: "text",
            //         displayField: "nomeProvincia",
            //         label: "Provincia",
            //         required: true,
            //         notFoundMessage: "Non è stata trovata nessuna corrispondenza",
            //     },
            //     controller: function ($scope, $q, apiService, $mdUtil) {
            //         var streets = [];
            //         var simulateQuery = false;
            //         $scope.autocomplete = {
            //             searchTextChange: searchTextChange,
            //             selectedItemChange: selectedItemChange,
            //             querySearch: querySearch,
            //         };
            //
            //         function init() {
            //             apiService.getAllProvincie().then(
            //                 function (data) {
            //                     streets = data;
            //                     for (var i = 0; i < streets.length; i++) {
            //                         if (streets[i].id === $scope.model.id) {
            //                             $scope.autocomplete.selectedItem = streets[i];
            //                         }
            //                     }
            //                 }
            //             )
            //         }
            //
            //         function selectedItemChange(item) {
            //             logger.info('Item changed to ' + JSON.stringify(item));
            //             if (item) {
            //                 if (angular.isObject(item)) {
            //                     $scope.model.id = item.id;
            //                     $scope.model.nomeProvincia = item.nomeProvincia;
            //                 }
            //                 else {
            //                     for (var i = 0; i < streets.length; i++) {
            //                         if (streets[i].nomeProvincia === item) {
            //                             $scope.model.id = streets[i].id;
            //                             $scope.model.nomeProvincia = streets[i].nomeProvincia;
            //                         }
            //                     }
            //                 }
            //             }
            //             else {
            //                 resetAddressModel();
            //             }
            //         }
            //
            //         function searchTextChange(text) {
            //             logger.info('Text changed to ' + text);
            //             $scope.model.nomeProvincia = text;
            //         }
            //
            //         function querySearch(query) {
            //             var results = query ? streets.filter(createFilterFor(query)) : streets,
            //                 deferred;
            //             if (simulateQuery) {
            //                 deferred = $q.defer();
            //                 $timeout(function () {
            //                     deferred.resolve(results);
            //                 }, Math.random() * 1000, false);
            //                 return deferred.promise;
            //             } else {
            //                 return results;
            //             }
            //         }
            //
            //         function createFilterFor(query) {
            //             var lowercaseQuery = angular.lowercase(query);
            //
            //             return function filterFn(street) {
            //                 return (angular.lowercase(street.nomeProvincia).indexOf(lowercaseQuery) > -1);
            //             };
            //
            //         }
            //
            //         $scope.$watch('model.nomeProvincia', function (newValue, oldValue, theScope) {
            //                 if (vm.localize && newValue) {
            //                     $scope.autocomplete.selectedItem = newValue;
            //                 }
            //
            //             }
            //         );
            //         init();
            //     }
            // },
            // {
            //     className: "input-green",
            //     key: "nomeComune",
            //     model: vm.dae.gpsLocation.comune,
            //     type: "autocomplete",
            //     templateOptions: {
            //         type: "text",
            //         displayField: "nomeComune",
            //         label: "Comune",
            //         required: true,
            //         notFoundMessage: "Non è stata trovata nessuna corrispondenza",
            //         onFocus: function () {
            //
            //         }
            //     },
            //     hideExpression: '!model.provincia.nomeProvincia',
            //     controller: function ($scope, $q, apiService, $mdUtil) {
            //         var streets = [];
            //         var simulateQuery = false;
            //         $scope.autocomplete = {
            //             searchTextChange: searchTextChange,
            //             selectedItemChange: selectedItemChange,
            //             querySearch: querySearch
            //         };
            //
            //         function selectedItemChange(item) {
            //             logger.info('Item changed to ' + JSON.stringify(item));
            //             if (item) {
            //                 if (angular.isObject(item)) {
            //                     $scope.model.id = item.id;
            //                     $scope.model.nomeComune = item.nomeComune;
            //                     $scope.model.codiceIstat = item.codiceIstat;
            //                     $scope.model.cap = item.cap;
            //                 }
            //                 else {
            //                     for (var i = 0; i < streets.length; i++) {
            //                         if (streets[i].nomeComune === item) {
            //                             $scope.model.id = streets[i].id;
            //                             $scope.model.nomeComune = streets[i].nomeComune;
            //                             $scope.model.codiceIstat = streets[i].codiceIstat;
            //                             $scope.model.cap = streets[i].cap;
            //                         }
            //                     }
            //                 }
            //             }
            //             else {
            //                 vm.dae.gpsLocation.comune.id = null;
            //                 vm.dae.gpsLocation.comune.nomeComune = null;
            //                 vm.dae.gpsLocation.comune.codiceIstat = null;
            //                 vm.dae.gpsLocation.comune.cap = null;
            //                 vm.dae.gpsLocation.indirizzo.id = null;
            //                 vm.dae.gpsLocation.indirizzo.name = null;
            //             }
            //         }
            //
            //         function searchTextChange(text) {
            //             logger.info('Text changed to ' + text);
            //             $scope.model.nomeComune = text;
            //         }
            //
            //         function querySearch(query) {
            //             var results = query ? streets.filter(createFilterFor(query)) : streets,
            //                 deferred;
            //             if (simulateQuery) {
            //                 deferred = $q.defer();
            //                 $timeout(function () {
            //                     deferred.resolve(results);
            //                 }, Math.random() * 1000, false);
            //                 return deferred.promise;
            //             } else {
            //                 return results;
            //             }
            //         }
            //
            //         function createFilterFor(query) {
            //             var lowercaseQuery = angular.lowercase(query);
            //
            //             return function filterFn(street) {
            //                 return (angular.lowercase(street.nomeComune).indexOf(lowercaseQuery) > -1);
            //             };
            //
            //         }
            //
            //         $scope.$watch('model.provincia.nomeProvincia', function (newValue, oldValue, theScope) {
            //                 if (newValue) {
            //                     apiService.searchComuniByFilter(newValue).then(
            //                         function (data) {
            //                             streets = data;
            //                             if (vm.localize) {
            //                                 $scope.autocomplete.selectedItem =  vm.dae.gpsLocation.comune.nomeComune;
            //                             }
            //                         }
            //                     )
            //
            //                 }
            //             }
            //         );
            //     }
            // },
            {
                className: "input-green",
                key: "indirizzo",
                model: vm.dae.gpsLocation,
                type: "autocomplete",
                templateOptions: {
                    type: "text",
                    displayField: "name",
                    label: "Indirizzo",
                    required: true,
                    notFoundMessage: "Non è stata trovata nessuna corrispondenza"
                },
                hideExpression: '!model.comune.nomeComune',
                controller: function ($scope, $q, apiService) {
                    var streets = [];
                    var simulateQuery = false;
                    $scope.autocomplete = {
                        searchTextChange: searchTextChange,
                        selectedItemChange: selectedItemChange,
                        querySearch: querySearch
                    };

                    function selectedItemChange(item) {
                        logger.info('Item changed to ' + JSON.stringify(item));
                        if (item) {
                            if (angular.isObject(item)) {
                                $scope.model.indirizzo.id = item.id;
                                $scope.model.indirizzo.name = item.name;
                            }
                            else {
                                for (var i = 0; i < streets.length; i++) {
                                    if (streets[i].name === item) {
                                        $scope.model.indirizzo.id = streets[i].id;
                                        $scope.model.indirizzo.name = streets[i].name;
                                    }
                                }
                            }
                        }
                        else {
                            vm.dae.gpsLocation.indirizzo.id = null;
                            vm.dae.gpsLocation.indirizzo.name = null;
                        }
                    }

                    function searchTextChange(text) {
                        logger.info('Text changed to ' + text);
                        $scope.model.indirizzo.name = text;
                    }

                    function querySearch(query) {
                        var results = query ? streets.filter(createFilterFor(query)) : streets,
                            deferred;
                        if (simulateQuery) {
                            deferred = $q.defer();
                            $timeout(function () {
                                deferred.resolve(results);
                            }, Math.random() * 1000, false);
                            return deferred.promise;
                        } else {
                            return results;
                        }
                    }

                    function createFilterFor(query) {
                        var lowercaseQuery = angular.lowercase(query);

                        return function filterFn(street) {
                            return (angular.lowercase(street.name).indexOf(lowercaseQuery) > -1);
                        };

                    }

                    $scope.$watch('model.comune.codiceIstat', function (newValue, oldValue, theScope) {
                        if (newValue) {
                            logger.info('model.comune.codiceIstat: ' + newValue);
                            apiService.searchStradeByFilter(newValue, null).then(
                                function (data) {
                                    streets = data;
                                    if (vm.localize) {
                                        $scope.autocomplete.selectedItem = vm.dae.gpsLocation.indirizzo.name;
                                        vm.localize = false;
                                        device.hideLoader();
                                    }
                                }
                            )

                        }
                    }
                    );
                }
            },
            {
                className: "input-green",
                key: "civico",
                model: vm.dae.gpsLocation,
                type: "input",
                templateOptions: {
                    type: "text",
                    required: true,
                    label: "Civico"
                },
                hideExpression: '!model.comune.nomeComune'
            },
            {
                className: "input-green",
                key: "matricola",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Matricola"
                }
            },
            {
                className: "input-green",
                key: "modello",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Modello",
                    required: true
                }
            },
            {
                className: "input-green",
                type: "select",
                key: "id",
                model: vm.dae.tipologiaStruttura,
                templateOptions: {
                    label: "Tipologia struttura",
                    placeholder: "",
                    required: true,
                    multiple: false,
                    labelProp: "descrizione",
                    valueProp: "id",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.getAllTipoStruttura().then(
                            function (data) {
                                scope.to.options = data;
                            },
                            function (error) {
                                logger.info(error);
                                $mdSelect.hide();
                                device.alert(error.message, "balanced");
                            }
                        );
                    },
                    onClose: function ($modelValue, $inputValue, scope) {
                        logger.info('select is closed');
                    }
                },
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            scope.model.tipologiaStruttura.descrizione = _.find(field.templateOptions.options, function (o) {
                                return o.id == newValue;
                            }).descrizione;

                        }
                    }
                }
            },
            {
                className: "input-green",
                key: "nomeSede",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Nome sede",
                    required: true
                }
            },
            {
                className: 'section-label',
                template: '<div style="font-size: 20px">Responsabile DAE:</div>'
            }
            ,
            {
                className: 'layout-row',
                fieldGroup: [
                    {
                        className: "flex-50 input-green",
                        key: "responsabile.nome",
                        type: "input",
                        templateOptions: {
                            type: "text",
                            label: "Nome",
                            required: true
                        }
                    },
                    {
                        className: "flex-50 input-green",
                        key: "responsabile.cognome",
                        type: "input",
                        templateOptions: {
                            type: "text",
                            label: "Cognome",
                            required: true
                        }
                    }
                ]
            }
            ,
            {
                className: "input-green",
                key: "responsabile.telefono",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Telefono",
                    required: true
                }
            }
            ,
            {
                className: "input-green",
                key: "responsabile.email",
                type: "input",
                templateOptions: {
                    type: "email",
                    label: "Email",
                    required: true
                }
            }
            ,
            {
                className: "input-green",
                key: "ubicazione",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Ubicazione"
                }
            },
            {
                className: "input-green",
                key: "notediAccessoallaSede",
                type: "textarea",
                templateOptions: {
                    maxlenght: 150,
                    rows: 5,
                    label: "Note di Accesso alla Sede"
                }
            },
            {
                className: 'section-label',
                template: '<div style="font-size: 20px">Disponibilità:</div>'
            },
            {
                key: "disponibilitaPermanente",
                type: "switch",
                model: 'formState',
                templateOptions: {
                    label: "Parziale (vedi calendario)",
                    theme: "default",
                    onChange: function (val, field, scope) {
                        vm.dae.disponibilitaPermanente = val;
                        if (val) {
                            field.templateOptions.label = "Disponibilità H24"
                        }
                        else {
                            field.templateOptions.label = "Parziale (vedi calendario)"
                        }
                    }
                }
            },
            {
                className: 'layout-row',
                fieldGroup: [
                    {
                        className: 'flex-50 input-green',
                        type: "select",
                        key: "id",
                        model: 'formState.da',
                        templateOptions: {
                            label: "Dal mese",
                            required: true,
                            labelProp: "name",
                            valueProp: "id",
                            options: [
                                {
                                    id: 1,
                                    name: "Gennaio"
                                },
                                {
                                    id: 2,
                                    name: "Febbraio"
                                },
                                {
                                    id: 3,
                                    name: "Marzo"
                                },
                                {
                                    id: 4,
                                    name: "Aprile"
                                },
                                {
                                    id: 5,
                                    name: "Maggio"
                                },
                                {
                                    id: 6,
                                    name: "Giugno"
                                },
                                {
                                    id: 7,
                                    name: "Luglio"
                                },
                                {
                                    id: 8,
                                    name: "Agosto"
                                },
                                {
                                    id: 9,
                                    name: "Settembre"
                                },
                                {
                                    id: 10,
                                    name: "Ottobre"
                                },
                                {
                                    id: 11,
                                    name: "Novembre"
                                },
                                {
                                    id: 12,
                                    name: "Dicembre"
                                }
                            ],
                            onChange: function (val, field, scope) {
                                var date = new Date();
                                var year = date.getFullYear();
                                var firstDay = new Date(year, val - 1, 1);
                                vm.dae.disponibilita[0].da = new Date(firstDay.getTime() + (-1) * firstDay.getTimezoneOffset() * 60000).getTime();
                            }
                        },
                        hideExpression: 'formState.disponibilitaPermanente'
                    },
                    {
                        className: 'flex-50 input-green',
                        type: "select",
                        key: "id",
                        model: 'formState.a',
                        templateOptions: {
                            label: "Al mese",
                            required: true,
                            labelProp: "name",
                            valueProp: "id",
                            options: [
                                {
                                    id: 1,
                                    name: "Gennaio"
                                },
                                {
                                    id: 2,
                                    name: "Febbraio"
                                },
                                {
                                    id: 3,
                                    name: "Marzo"
                                },
                                {
                                    id: 4,
                                    name: "Aprile"
                                },
                                {
                                    id: 5,
                                    name: "Maggio"
                                },
                                {
                                    id: 6,
                                    name: "Giugno"
                                },
                                {
                                    id: 7,
                                    name: "Luglio"
                                },
                                {
                                    id: 8,
                                    name: "Agosto"
                                },
                                {
                                    id: 9,
                                    name: "Settembre"
                                },
                                {
                                    id: 10,
                                    name: "Ottobre"
                                },
                                {
                                    id: 11,
                                    name: "Novembre"
                                },
                                {
                                    id: 12,
                                    name: "Dicembre"
                                }
                            ],
                            onChange: function (val, field, scope) {
                                var date = new Date();
                                var year = date.getFullYear();
                                var lastDay = new Date(year, val, 0);
                                vm.dae.disponibilita[0].a = new Date(lastDay.getTime() + (-1) * lastDay.getTimezoneOffset() * 60000).getTime();
                            }
                        },
                        hideExpression: 'formState.disponibilitaPermanente'
                    }
                ]
            },
            {
                className: "input-green",
                type: "dae-availabilityList",
                key: 'disponibilitaGiornaliera',
                model: vm.dae.disponibilita[0].disponibilitaSpecifica,
                templateOptions: {
                    required: true,
                    btnText: "Aggiungi disponibilità giornaliera",
                    list: [],
                    onRemove: function ($options, index) {
                        var day = $options.data.availabilityDays[index];
                        $options.data.availabilityDays.splice(index, 1);
                        _.remove(vm.dae.disponibilita[0].disponibilitaSpecifica.disponibilitaGiornaliera, function (o) {
                            return o.id == day.id;
                        });
                    },
                    onclick: function ($modelValue, $options) {
                        $mdDialog.show({
                            controller: "AddAvailabilityCtrl",
                            controllerAs: 'vm',
                            template: $templateCache.get('dialog/addAvailability.html'),
                            parent: angular.element(document.body),
                            clickOutsideToClose: true
                        }).then(
                            function (data) {
                                logger.info(data);
                                if (data) {
                                    if (!$options.data.availabilityDays) {
                                        $options.data.availabilityDays = [];
                                    }
                                    var dayOfWeek = _.find($options.data.availabilityDays, function (o) {
                                        return o.id == data.id;
                                    });

                                    if (dayOfWeek) {
                                        device.alert("Il giorno è già stato selezionato", "primary");
                                    }
                                    else {
                                        $options.data.availabilityDays.push(data);
                                        for (var i = 0; i < data.intervals.length; i++) {
                                            var obj = {};
                                            obj.id = data.id;
                                            obj.giornoSettimana = data.giornoSettimana;
                                            obj.orarioDa = data.intervals[i].orarioDa;
                                            obj.orarioA = data.intervals[i].orarioA;
                                            vm.dae.disponibilita[0].disponibilitaSpecifica.disponibilitaGiornaliera.push(obj);
                                        }
                                    }
                                }
                            });
                    }
                },
                hideExpression: 'formState.disponibilitaPermanente'
            },

            // {
            //     type: 'repeatAvailabilitySection',
            //     key: 'disponibilitaGiornaliera',
            //     model: vm.dae.disponibilita[0].disponibilitaSpecifica,
            //     templateOptions: {
            //         btnText: 'Aggiungi disponibilità giornaliera',
            //         fields: [
            //             {
            //                 className: 'layout-row',
            //                 fieldGroup: [
            //                     {
            //                         className: 'flex-30 input-green',
            //                         type: "select",
            //                         key: "id",
            //                         templateOptions: {
            //                             label: "Giorno",
            //                             required: true,
            //                             labelProp: "label",
            //                             valueProp: "id",
            //                             options: [
            //                                 {
            //                                     id: 0,
            //                                     name: "LUNEDI",
            //                                     label: "Lunedì"
            //                                 },
            //                                 {
            //                                     id: 1,
            //                                     name: "MARTEDI",
            //                                     label: "Martedì"
            //                                 },
            //                                 {
            //                                     id: 2,
            //                                     name: "MERCOLEDI",
            //                                     label: "Mercoledì"
            //                                 },
            //                                 {
            //                                     id: 3,
            //                                     name: "GIOVEDI",
            //                                     label: "Giovedì"
            //                                 },
            //                                 {
            //                                     id: 4,
            //                                     name: "VENERDI",
            //                                     label: "Venerdì"
            //                                 },
            //                                 {
            //                                     id: 5,
            //                                     name: "SABATO",
            //                                     label: "Sabato"
            //                                 },
            //                                 {
            //                                     id: 6,
            //                                     name: "DOMENICA",
            //                                     label: "Domenica"
            //                                 }
            //                             ],
            //                             onChange: function (val, field, scope) {
            //                                 var obj = _.filter(vm.dae.disponibilita[0].disponibilitaSpecifica.disponibilitaGiornaliera, function (o) {
            //                                     return o.id == val;
            //                                 })
            //                                 if (obj.length > 1) {
            //                                     device.alert("Il giorno è già stato selezionato", "primary").then(
            //                                         function () {
            //                                             vm.dae.disponibilita[0].disponibilitaSpecifica.disponibilitaGiornaliera.pop();
            //                                         }
            //                                     );
            //                                 }
            //                                 else {
            //                                     scope.model.giornoSettimana = _.find(field.templateOptions.options, function (o) {
            //                                         return o.id == val;
            //                                     }).name;
            //                                 }
            //                             }
            //                         }
            //                     },
            //                     {
            //                         className: 'flex-35 input-green',
            //                         key: "orarioDa",
            //                         type: "dae-timepicker",
            //                         templateOptions: {
            //                             label: "Dalle",
            //                             dateFormat: 'HH:mm',
            //                             required: true,
            //                             onFocus: function () {
            //                                 var isVisible = $cordovaKeyboard.isVisible();
            //                                 if (isVisible) {
            //                                     $cordovaKeyboard.close();
            //                                 }
            //                             },
            //                             onclick: function ($modelValue, $options) {
            //                                 var options = {
            //                                     date: new Date(),
            //                                     mode: "time",           // 'date' or 'time'
            //
            //                                     // Configurazione Android
            //                                     //titleText     : $translate.instant("DATEPICKER_TITLE"),
            //                                     okText: $translate.instant("DATEPICKER_DONE"),
            //                                     cancelText: $translate.instant("DATEPICKER_CANCEL"),
            //                                     is24Hour: true,
            //                                     androidTheme: 5,
            //
            //                                     // Configurazione iOS
            //                                     allowOldDates: true,
            //                                     allowFutureDates: true,
            //                                     doneButtonLabel: $translate.instant("DATEPICKER_DONE"),
            //                                     doneButtonColor: "#1D62F0",
            //                                     cancelButtonLabel: $translate.instant("DATEPICKER_CANCEL"),
            //                                     cancelButtonColor: '#000000',
            //                                     locale: $translate.use()
            //                                 };
            //                                 if (device.isCordova()) {
            //                                     $cordovaDatePicker.show(options).then(function (date) {
            //                                         if ($modelValue.orarioA) {
            //                                             var fromTime = moment(date);
            //                                             var toTime = moment($modelValue.orarioA, "HH:mm");
            //                                             var isBefore = fromTime.isBefore(toTime);
            //                                             if (!isBefore) {
            //                                                 device.alert("L'orario di inizio disponibilità deve essere minore dell'orario di fine", "primary").then(
            //                                                     function () {
            //                                                         $modelValue[$options.key] = null;
            //                                                     }
            //                                                 );
            //                                             }
            //                                             else {
            //                                                 $modelValue[$options.key] = moment(date).format("HH:mm");
            //                                             }
            //                                         }
            //                                         else {
            //                                             $modelValue[$options.key] = moment(date).format("HH:mm");
            //                                         }
            //                                     });
            //                                 }
            //                                 else {
            //                                     device.alert("Datepicker non disponibile", "primary");
            //                                 }
            //                             }
            //                         }
            //                     },
            //                     {
            //                         className: 'flex-35 input-green',
            //                         key: "orarioA",
            //                         type: "dae-timepicker",
            //                         templateOptions: {
            //                             label: "Alle",
            //                             dateFormat: 'HH:mm',
            //                             required: true,
            //                             onFocus: function () {
            //                                 var isVisible = $cordovaKeyboard.isVisible();
            //                                 if (isVisible) {
            //                                     $cordovaKeyboard.close();
            //                                 }
            //                             },
            //                             onclick: function ($modelValue, $options) {
            //                                 var options = {
            //                                     date: new Date(),
            //                                     mode: "time",           // 'date' or 'time'
            //
            //                                     // Configurazione Android
            //                                     //titleText     : $translate.instant("DATEPICKER_TITLE"),
            //                                     okText: $translate.instant("DATEPICKER_DONE"),
            //                                     cancelText: $translate.instant("DATEPICKER_CANCEL"),
            //                                     is24Hour: true,
            //                                     androidTheme: 5,
            //
            //                                     // Configurazione iOS
            //                                     allowOldDates: true,
            //                                     allowFutureDates: true,
            //                                     doneButtonLabel: $translate.instant("DATEPICKER_DONE"),
            //                                     doneButtonColor: "#1D62F0",
            //                                     cancelButtonLabel: $translate.instant("DATEPICKER_CANCEL"),
            //                                     cancelButtonColor: '#000000',
            //                                     locale: $translate.use()
            //                                 };
            //                                 if (device.isCordova()) {
            //                                     $cordovaDatePicker.show(options).then(function (date) {
            //                                         if ($modelValue.orarioDa) {
            //                                             var fromTime = moment($modelValue.orarioDa, "HH:mm");
            //                                             var toTime = moment(date);
            //                                             var isAfter = toTime.isAfter(fromTime);
            //                                             if (!isAfter) {
            //                                                 device.alert("L'orario di fine disponibilità deve essere maggiore dell'orario di inizio", "primary").then(
            //                                                     function () {
            //                                                         $modelValue[$options.key] = null;
            //                                                     }
            //                                                 );
            //                                             }
            //                                             else {
            //                                                 $modelValue[$options.key] = moment(date).format("HH:mm");
            //                                             }
            //                                         }
            //                                         else {
            //                                             $modelValue[$options.key] = moment(date).format("HH:mm");
            //                                         }
            //                                     });
            //                                 }
            //                                 else {
            //                                     device.alert("Datepicker non disponibile", "primary");
            //                                 }
            //                             }
            //                         }
            //                     }
            //                 ]
            //             }
            //         ]
            //     }
            // }
            // ,
            {
                className: 'section-label',
                template: '<div style="font-size: 20px; margin-top:20px">Programmi manutenzione:</div>'
            }
            ,
            {
                type: 'repeatSection',
                key: 'programmiManutenzione',
                templateOptions: {
                    btnText: 'Aggiungi programma manutenzione',
                    fields: [
                        {
                            className: 'input-green',
                            type: "select",
                            key: "tipoManutenzione",
                            templateOptions: {
                                label: "Tipo manutenzione",
                                required: false,
                                labelProp: "descrizione",
                                valueProp: "descrizione",
                                options: [],
                                onOpen: function ($modelValue, $inputValue, scope) {
                                    logger.info('select is opened');
                                    return api.getAllTipoManutenzione().then(
                                        function (data) {
                                            scope.to.options = data;
                                        },
                                        function (error) {
                                            logger.info(error);
                                            $mdSelect.hide();
                                            device.alert(error.message, "balanced");
                                        }
                                    );
                                }
                            }
                        },
                        {
                            className: 'input-green',
                            key: "scadenzaDopo",
                            type: "dae-datepicker",
                            templateOptions: {
                                label: "Data prossima scadenza",
                                dateFormat: 'dd MMMM yyyy',
                                required: false,
                                onclick: function ($modelValue, $options) {
                                    var options = {
                                        date: new Date(),
                                        mode: "date",           // 'date' or 'time'
                                        // Configurazione Android
                                        //titleText     : $translate.instant("DATEPICKER_TITLE"),
                                        okText: $translate.instant("DATEPICKER_DONE"),
                                        cancelText: $translate.instant("DATEPICKER_CANCEL"),
                                        is24Hour: true,
                                        androidTheme: 5,
                                        // Configurazione iOS
                                        allowOldDates: true,
                                        allowFutureDates: true,
                                        doneButtonLabel: $translate.instant("DATEPICKER_DONE"),
                                        doneButtonColor: "#1D62F0",
                                        cancelButtonLabel: $translate.instant("DATEPICKER_CANCEL"),
                                        cancelButtonColor: '#000000',
                                        locale: $translate.use()
                                    };
                                    if (device.isCordova()) {
                                        $cordovaDatePicker.show(options).then(function (date) {
                                            $modelValue[$options.key] = date.getTime();
                                        });
                                    }
                                    else {
                                        device.alert("Datepicker non disponibile", "primary");
                                    }
                                }
                            }
                        },
                        {
                            className: 'section-label',
                            template: '<div style="font-size: 18px">Responsabile manutenzione:</div>'
                        },
                        {
                            className: 'layout-row',
                            fieldGroup: [
                                {
                                    className: "flex-50 input-green",
                                    key: "responsabile.nome",
                                    type: "input",
                                    templateOptions: {
                                        type: "text",
                                        label: "Nome",
                                        required: false
                                    }
                                },
                                {
                                    className: "flex-50 input-green",
                                    key: "responsabile.cognome",
                                    type: "input",
                                    templateOptions: {
                                        type: "text",
                                        label: "Cognome",
                                        required: false
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
        ]
    }]);


