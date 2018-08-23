appControllers.controller('UpdateProfileCtrl', [
    '$rootScope',
    '$scope',
    '$state',
    'loggerService',
    'apiService',
    'userService',
    'deviceService',
    '$mdBottomSheet',
    '$mdDialog',
    '$cordovaImagePicker',
    '$cordovaCamera',
    '$q',
    '$timeout',
    '$templateCache',
    '$ionicHistory',
    '$mdSelect',
    '$translate',
    '$cordovaDatePicker',
    function ($rootScope, $scope, $state, logger, api, userService, device, $mdBottomSheet,
        $mdDialog, $cordovaImagePicker, $cordovaCamera, $q, $timeout, $templateCache,
        $ionicHistory, $mdSelect, $translate, $cordovaDatePicker) {
        logger.info("Caricamento UpdateProfileCtrl...");

        var vm = this;
        vm.photo = false;
        vm.updateImageProfiloFailed = false;
        vm.showUploadBottomSheet = showUploadBottomSheet;
        vm.selectMedia = selectMedia;
        vm.updateFirstResponder = updateFirstResponder;
        vm.resetModel = resetModel;


        $scope.$on('$ionicView.loaded', function () {
            //initialize();
        });
        $scope.$on('$ionicView.enter', function () {
            initialize();
        });

        function initialize() {
            device.showLoader();
            api.getLoggedFirstResponderDetails().then(
                function (profile) {
                    logger.info(profile);

                    vm.profile.nome = profile.nome;
                    vm.profile.cognome = profile.cognome;
                    vm.profile.codiceFiscale = profile.codiceFiscale;
                    vm.profile.certificatoFr.rilasciatoDa = profile.certificatoFr.rilasciatoDa;
                    vm.profile.certificatoFr.dataConseguimento =  profile.certificatoFr.dataConseguimento ;

                    vm.profile.civico = profile.civico || "ND";
                    vm.profile.indirizzo.id = profile.indirizzo.id;
                    vm.profile.indirizzo.name = profile.indirizzo.name;
                    vm.profile.comuneResidenza.provincia.id = profile.comuneResidenza.provincia.id;
                    vm.profile.comuneResidenza.provincia.nomeProvincia = profile.comuneResidenza.provincia.nomeProvincia;
                    vm.profile.comuneResidenza.id = profile.comuneResidenza.id;
                    vm.profile.comuneResidenza.nomeComune = profile.comuneResidenza.nomeComune;
                    vm.profile.comuneResidenza.codiceIstat = profile.comuneResidenza.codiceIstat;
                    vm.profile.comuneResidenza.cap = profile.comuneResidenza.cap;
                    if (profile.professione) {
                        vm.profile.professione.id = profile.professione.id;
                        vm.profile.professione.descrizione = profile.professione.descrizione;
                    }
                    vm.profile.immagine.id = profile.immagine.id;
                    vm.profile.immagine.data = profile.immagine.data;
                    vm.profile.immagine.url = profile.immagine.url;
                    vm.profile.numCellulare = profile.numCellulare;

                    profile.comuniCompetenza.sort(
                        function (a, b) {
                            return a.nomeComune.localeCompare(b.nomeComune);
                        }
                    );

                    vm.profile.comuniCompetenza = profile.comuniCompetenza;

                    if (vm.profile.immagine.url) {
                        vm.photo = true;
                        $timeout(function () {
                            var image = document.getElementById('update-photo');
                            image.src = $rootScope.config.imgUrl + vm.profile.immagine.url;
                        }, 500);

                    }
                    device.hideLoader();
                },
                function (error) {
                    device.hideLoader();
                    device.alert(error.message, "balanced").then(
                        function () {
                            $ionicHistory.goBack();
                        }
                    );
                }
            );
            // $timeout(function () {
            //     vm.user = userService.getUser();
            //     vm.profile.civico = vm.user.civico || "ND";
            //     vm.profile.indirizzo.id = vm.user.indirizzo.id;
            //     vm.profile.indirizzo.name = vm.user.indirizzo.name;
            //     vm.profile.comuneResidenza.provincia.id = vm.user.comuneResidenza.provincia.id;
            //     vm.profile.comuneResidenza.provincia.nomeProvincia = vm.user.comuneResidenza.provincia.nomeProvincia;
            //     vm.profile.comuneResidenza.id = vm.user.comuneResidenza.id;
            //     vm.profile.comuneResidenza.nomeComune = vm.user.comuneResidenza.nomeComune;
            //     vm.profile.comuneResidenza.codiceIstat = vm.user.comuneResidenza.codiceIstat;
            //     vm.profile.comuneResidenza.cap = vm.user.comuneResidenza.cap;
            //     vm.profile.professione.id = vm.user.professione.id;
            //     vm.profile.professione.descrizione = vm.user.professione.descrizione;
            //     vm.profile.immagine.id = vm.user.immagine.id;
            //     vm.profile.immagine.data = vm.user.immagine.data;
            //     vm.profile.immagine.url = vm.user.immagine.url;
            //     vm.profile.numCellulare = vm.user.numCellulare;
            //
            //     vm.profile.comuniCompetenza = vm.user.comuniCompetenza;
            //
            //     if (vm.profile.immagine.url) {
            //         vm.photo = true;
            //         $timeout(function () {
            //             var image = document.getElementById('update-photo');
            //             image.src = $rootScope.config.imgUrl + vm.profile.immagine.url;
            //             device.hideLoader();
            //         }, 500);
            //
            //     }
            // }, 1000);
        }

        function selectMedia(media) {
            $mdBottomSheet.hide(media);
        }

        function selectImage() {
            var q = $q.defer();
            if (device.isCordova()) {
                var options = {
                    maximumImagesCount: 1,
                    width: 1024,
                    height: 768,
                    quality: 50
                };
                $cordovaImagePicker.getPictures(options).then(
                    function (results) {
                        logger.info(JSON.stringify(results));
                        getFileContentAsBase64(results[0], function (imageData) {
                            q.resolve(imageData);
                        });
                    },
                    function (error) {
                        logger.info(error);
                        q.reject(error);
                    });
            }
            else {
                device.hideLoader();
                device.alert("PhotoGallery non disponibile", "balanced");
            }
            return q.promise;
        }

        function getPicture(type) {
            var q = $q.defer();
            if (device.isCordova()) {
                var sourceType = Camera.PictureSourceType.CAMERA;
                var options = {
                    quality: 50,
                    destinationType: Camera.DestinationType.DATA_URL,
                    sourceType: sourceType,
                    targetHeight: 768,
                    targetWidth: 1024,
                    allowEdit: false,
                    encodingType: Camera.EncodingType.JPEG,
                    popoverOptions: CameraPopoverOptions,
                    saveToPhotoAlbum: false,
                    correctOrientation: true
                };

                $cordovaCamera.getPicture(options).then(
                    function (base64Url) {
                        q.resolve(base64Url);
                    },
                    function (err) {
                        logger.error("RegistrationCtrl.getPicture error", err);
                        q.reject(err);
                        device.hideLoader();
                    });

            }
            else {
                device.hideLoader();
                device.alert("Camera non disponibile", "balanced");
            }
            return q.promise;
        }

        /**
         * Convert an image
         * to a base64 url
         * @param  {String}   url
         * @param  {Function} callback
         * @param  {String}   [outputFormat=image/png]
         */
        function convertImgToBase64URL(url, callback, outputFormat) {
            var img = new Image();
            img.crossOrigin = 'Anonymous';
            img.onload = function () {
                var canvas = document.createElement('CANVAS'),
                    ctx = canvas.getContext('2d'), dataURL;
                canvas.height = this.height;
                canvas.width = this.width;
                ctx.drawImage(this, 0, 0);
                dataURL = canvas.toDataURL(outputFormat);
                callback(dataURL);
                canvas = null;
            };
            img.src = url;
        }

        /**
         * Convert an image
         * to a base64 url
         * @param  {String}   path
         * @param  {Function} callback
         */
        function getFileContentAsBase64(path, callback) {
            window.resolveLocalFileSystemURL(path, gotFile, fail);

            function fail(e) {
                alert('Cannot found requested file');
            }

            function gotFile(fileEntry) {
                fileEntry.file(function (file) {
                    var reader = new FileReader();
                    reader.onloadend = function (e) {
                        var content = this.result;
                        callback(content);
                    };
                    reader.readAsDataURL(file);
                });
            }
        }

        function checkPermission() {
            var q = $q.defer();
            if (device.isCordova()) {
                if (device.isAndroid()) {
                    cordova.plugins.diagnostic.getPermissionAuthorizationStatus(
                        function (status) {
                            if (status === cordova.plugins.diagnostic.permissionStatus.GRANTED) {
                                q.resolve();
                            }
                            else {
                                cordova.plugins.diagnostic.requestRuntimePermission(
                                    function (status) {
                                        if (status === cordova.plugins.diagnostic.permissionStatus.GRANTED) {
                                            q.resolve();
                                        }
                                    },
                                    function (error) {
                                        console.error("The following error occurred: " + error);
                                        q.reject();
                                    }, cordova.plugins.diagnostic.permission.READ_EXTERNAL_STORAGE);
                            }
                        },
                        function (error) {
                            console.error("The following error occurred: " + error);
                            q.reject();
                        }, cordova.plugins.diagnostic.permission.READ_EXTERNAL_STORAGE);
                }
                else {
                    q.resolve();
                }
            }
            else {
                q.resolve();
            }
            return q.promise;
        }

        function updateFirstResponder() {
            device.showLoader();
            logger.info("[RegistrationCtrl:addNewFirstResponder]");
            logger.info(JSON.stringify(vm.profile));
            if (vm.updateFRForm.$valid) {
                if (!vm.profile.comuniCompetenza) {
                    device.alert("Devi inserire almeno un comune", "balanced");
                }
                else if (vm.profile.comuniCompetenza.length == 0) {
                    device.alert("Devi inserire almeno un comune di competenza", "balanced");
                }
                else {
                    api.updateFirstResponder(vm.profile).then(
                        function (data) {
                            logger.info(data);
                            device.hideLoader();
                            if (typeof data.response == 'undefined') {
                                device.alert("Aggiornamento profilo avvenuto con successo", "positive").then(
                                    function () {
                                        userService.updateUser(vm.profile);
                                        $ionicHistory.goBack();
                                    }
                                )
                            }
                            else {
                                device.alert(data.message, "balanced");
                            }
                        },
                        function (error) {
                            device.hideLoader();
                            logger.info(error);
                            device.alert("Si è verificato un errore", "balanced");
                        }
                    );
                }
            }
        }

        function resetModel() {
            $ionicHistory.goBack();

        }

        function showUploadBottomSheet($event) {
            $mdBottomSheet.show({
                templateUrl: 'templates/bottomSheet/uploadPhoto.html',
                targetEvent: $event,
                scope: $scope.$new(false)
            }).then(function (media) {
                logger.info("<<<<" + media + ">>>>");
                checkPermission().then(
                    function () {
                        if (media === "CAMERA") {
                            getPicture().then(
                                function (data) {
                                    vm.photo = true;
                                    var image = document.getElementById('update-photo');
                                    var imageData = "data:image/jpeg;base64," + data;
                                    image.src = imageData;
                                    vm.profile.immagine.data = data;
                                },
                                function (error) {

                                }
                            );
                        }
                        else {
                            selectImage().then(
                                function (data) {
                                    vm.photo = true;
                                    var image = document.getElementById('update-photo');
                                    image.src = data;
                                    var base64 = data.split(',')[1];
                                    vm.profile.immagine.data = base64;
                                },
                                function (error) {

                                }
                            );
                        }
                    },
                    function () {
                        device.alert("Si è verificato un errore", "balanced");
                    }
                );
            });
        }

        vm.profile = {
            civico: null,
            nome: null,
            cognome: null,
            codiceFiscale: null,
            indirizzo: {
                id: null,
                name: null
            },
            comuneResidenza: {
                id: null,
                nomeComune: null,
                codiceIstat: null,
                cap: null,
                provincia: {
                    id: null,
                    nomeProvincia: null
                }
            },
            immagine: {
                id: null,
                data: null,
                url: null
            },
            numCellulare: null,
            professione: {
                id: null,
                descrizione: null
            },
            comuniCompetenza: null,
            certificatoFr: {
                dataConseguimento: new Date(),
                rilasciatoDa: null
            }
        };
        vm.options = {};
        vm.profileFields = [
            {
                className: "input-green",
                key: "nome",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Nome",
                    required: true
                }
            },
            {
                className: "input-green",
                key: "cognome",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Cognome",
                    required: true
                }
            },
            {
                className: "input-green",
                key: "numCellulare",
                type: "input",
                templateOptions: {
                    type: "tel",
                    label: "Numero di cellulare",
                    required: true
                }
            },
            {
                className: "input-green",
                key: "codiceFiscale",
                type: "input",
                templateOptions: {
                    type: "text",
                    label: "Codice fiscale",
                    required: true
                },
                watcher: {
                    listener: function (field, newValue, oldValue, formScope, stopWatching) {
                        if (formScope.model.codiceFiscale) {
                            formScope.model.codiceFiscale = formScope.model.codiceFiscale.toUpperCase();
                        }
                    }
                }
            },
            {
                className: "input-green",
                type: "select",
                key: "id",
                model: vm.profile.comuneResidenza.provincia,
                templateOptions: {
                    label: "Provincia",
                    required: true,
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
                                var obj = _.find(field.templateOptions.options, function (o) {
                                    return o.id == newValue;
                                });
                                scope.model.comuneResidenza.provincia.nomeProvincia = obj.nomeProvincia;
                            }
                            else {
                                field.templateOptions.options = [
                                    {
                                        id: vm.profile.comuneResidenza.provincia.id,
                                        nomeProvincia: vm.profile.comuneResidenza.provincia.nomeProvincia
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
                model: vm.profile.comuneResidenza,
                templateOptions: {
                    label: "Comune",
                    required: true,
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
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            if (field.templateOptions.options.length > 0) {
                                var obj = _.find(field.templateOptions.options, function (o) {
                                    return o.id == newValue;
                                });
                                scope.model.comuneResidenza.id = obj.id;
                                scope.model.comuneResidenza.nomeComune = obj.nomeComune;
                                scope.model.comuneResidenza.cap = obj.cap;
                                scope.model.comuneResidenza.codiceIstat = obj.codiceIstat;
                            }
                            else {
                                field.templateOptions.options = [
                                    {
                                        id: vm.profile.comuneResidenza.id,
                                        nomeComune: vm.profile.comuneResidenza.nomeComune
                                    }
                                ]
                            }
                        }
                    }
                },
                hideExpression: '!model.provincia.id'
            },
            // {
            //     className: "input-green",
            //     key: "nomeProvincia",
            //     model: vm.profile.comuneResidenza.provincia,
            //     type: "autocomplete",
            //     templateOptions: {
            //         type: "text",
            //         displayField: "nomeProvincia",
            //         label: "Provincia",
            //         required: true,
            //         notFoundMessage: "Non è stata trovata nessuna corrispondenza",
            //         onFocus: function () {
            //
            //         }
            //     },
            //     controller: function ($scope, $q, apiService, $mdUtil) {
            //         var streets = [];
            //         var simulateQuery = false;
            //         $scope.autocomplete = {
            //             searchTextChange: searchTextChange,
            //             selectedItemChange: selectedItemChange,
            //             querySearch: querySearch
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
            //                 $scope.model.id = item.id;
            //                 $scope.model.nomeProvincia = item.nomeProvincia;
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
            //         init();
            //     }
            // },
            // {
            //     className: "input-green",
            //     key: "comuneResidenza.nomeComune",
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
            //     hideExpression: '!model.comuneResidenza.provincia.id',
            //     controller: function ($scope, $q, apiService, $mdUtil) {
            //         var streets = [];
            //         var simulateQuery = false;
            //         $scope.autocomplete = {
            //             searchTextChange: searchTextChange,
            //             selectedItemChange: selectedItemChange,
            //             querySearch: querySearch
            //         };
            //
            //         function init() {
            //             if ($scope.model.comuneResidenza.provincia.nomeProvincia) {
            //                 apiService.searchComuniByFilter($scope.model.comuneResidenza.provincia.nomeProvincia).then(
            //                     function (data) {
            //                         streets = data;
            //                         for (var i = 0; i < streets.length; i++) {
            //                             if (streets[i].id === $scope.model.comuneResidenza.id) {
            //                                 $scope.autocomplete.selectedItem = streets[i];
            //                             }
            //                         }
            //                     }
            //                 )
            //             }
            //         }
            //
            //         function selectedItemChange(item) {
            //             logger.info('Item changed to ' + JSON.stringify(item));
            //             if (item) {
            //                 $scope.model.comuneResidenza.id = item.id;
            //                 $scope.model.comuneResidenza.nomeComune = item.nomeComune;
            //                 $scope.model.comuneResidenza.codiceIstat = item.codiceIstat;
            //                 $scope.model.comuneResidenza.cap = item.cap;
            //             }
            //         }
            //
            //         function searchTextChange(text) {
            //             logger.info('Text changed to ' + text);
            //             $scope.model.comuneResidenza.nomeComune = text;
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
            //         $scope.$watch('model.comuneResidenza.provincia.nomeProvincia', function (newValue, oldValue, theScope) {
            //                 if (newValue !== oldValue) {
            //                     $scope.autocomplete.selectedItem = "";
            //                     apiService.searchComuniByFilter(newValue).then(
            //                         function (data) {
            //                             streets = data;
            //                             for (var i = 0; i < streets.length; i++) {
            //                                 if (streets[i].id === $scope.model.comuneResidenza.id) {
            //                                     $scope.autocomplete.selectedItem = streets[i];
            //                                 }
            //                             }
            //                         }
            //                     )
            //
            //                 }
            //             }
            //         );
            //
            //         init();
            //     }
            // },
            {
                className: "input-green",
                key: "indirizzo",
                type: "autocomplete",
                templateOptions: {
                    type: "text",
                    displayField: "name",
                    label: "Indirizzo",
                    required: false,
                    notFoundMessage: "Non è stata trovata nessuna corrispondenza"
                },
                hideExpression: '!model.comuneResidenza.codiceIstat',
                controller: function ($scope, $q, apiService) {
                    var streets = [];
                    var simulateQuery = true;
                    $scope.autocomplete = {
                        searchTextChange: searchTextChange,
                        selectedItemChange: selectedItemChange,
                        querySearch: querySearch,
                        clear: clear
                    };

                    function clear() {
                        $scope.autocomplete.searchText = '';
                    }

                    function init() {
                        apiService.searchStradeByFilter($scope.model.comuneResidenza.codiceIstat).then(
                            function (data) {
                                streets = data;
                            }
                        )
                    }

                    function loadAll(query) {
                        apiService.searchStradeByFilter($scope.model.comuneResidenza.codiceIstat, query).then(
                            function (data) {
                                streets = data;
                            }
                        )
                    }

                    function selectedItemChange(item) {
                        logger.info('Item changed to ' + JSON.stringify(item));
                        if (item) {
                            // $scope.model.comuneResidenza.id = item.comune.id;
                            // $scope.model.comuneResidenza.nomeComune = item.comune.nomeComune;
                            // $scope.model.comuneResidenza.codiceIstat = item.comune.codiceIstat;
                            // $scope.model.comuneResidenza.cap = item.comune.cap;
                            // $scope.model.comuneResidenza.provincia.id = item.comune.provincia.id;
                            // $scope.model.comuneResidenza.provincia.nomeProvincia = item.comune.provincia.nomeProvincia;
                            $scope.model.indirizzo.id = item.id;
                            $scope.model.indirizzo.name = item.name;
                        }
                    }

                    function searchTextChange(text) {
                        logger.info('Text changed to ' + text);
                        $scope.model.indirizzo.name = text;
                    }

                    function querySearch(query) {
                        loadAll(query);
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

                    // init();

                    $scope.$watch('model.comuneResidenza.codiceIstat', function (newValue, oldValue, theScope) {
                        if (newValue) {
                            logger.info('new value is different from old value');
                            apiService.searchStradeByFilter(newValue).then(
                                function (data) {
                                    streets = data;
                                    for (var i = 0; i < streets.length; i++) {
                                        if (streets[i].id === $scope.model.indirizzo.id) {
                                            $scope.autocomplete.selectedItem = streets[i];
                                        }
                                    }
                                }
                            )

                        }
                    });

                }
            },
            {
                className: "input-green",
                key: "civico",
                type: "input",
                templateOptions: {
                    type: "text",
                    required: true,
                    label: "Civico"
                },
                hideExpression: '!model.comuneResidenza.codiceIstat'
            },
            {
                className: 'input-green',
                key: "dataConseguimento",
                model: vm.profile.certificatoFr,
                type: "dae-datepicker",
                templateOptions: {
                    label: "Data Certificazione / Diploma",
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
                        } else {
                            device.alert("Datepicker non disponibile", "balanced");
                        }
                    }
                }
            },
            {
                className: "input-green",
                type: "select",
                key: "certificatoFr.rilasciatoDa",
                templateOptions: {
                    label: "Certificato BLSD / Diploma rilasciato da:",
                    placeholder: "",
                    required: false,
                    multiple: false,
                    labelProp: "descrizione",
                    valueProp: "descrizione",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.getAllEnteCertificatore().then(
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
            /* {
                 className: "input-green",
                 type: "select",
                 key: "id",
                 model: vm.profile.professione,
                 templateOptions: {
                     label: "Professione",
                     placeholder: "",
                     multiple: false,
                     labelProp: "descrizione",
                     valueProp: "id",
                     options: [],
                     onOpen: function ($modelValue, $inputValue, scope) {
                         logger.info('select is opened');
                         return api.getAllProfessione().then(
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
                                 var obj = _.find(field.templateOptions.options, function (o) {
                                     return o.id == newValue;
                                 });
                                 scope.model.professione.descrizione = obj.descrizione;
                             }
                             else {
                                 field.templateOptions.options = [
                                     {
                                         id: vm.profile.professione.id,
                                         descrizione: vm.profile.professione.descrizione
                                     }
                                 ]
                             }
                         }
                     }
                 }
             },*/
            {
                className: "input-green",
                type: "dae-municipalityList",
                key: "comuniCompetenza",
                templateOptions: {
                    label: "Comuni su cui essere allertato se la localizzazione non è disponibile",
                    required: true,
                    btnText: "Aggiungi comune",
                    list: [],
                    onclick: function ($modelValue, $options) {
                        $mdDialog.show({
                            controller: "SelectMunicipalityCtrl",
                            controllerAs: 'vm',
                            template: $templateCache.get('dialog/selectMunicipality.html'),
                            parent: angular.element(document.body),
                            clickOutsideToClose: true
                        }).then(
                            function (data) {
                                logger.info(data);
                                if (data) {
                                    if (!angular.isArray($modelValue[$options.key])) {
                                        $modelValue[$options.key] = [];
                                    }
                                    var obj = _.find($modelValue[$options.key], function (o) {
                                        return o.id == data.id;
                                    });
                                    if (obj) {
                                        device.alert("Il comune è già stato inserito", "balanced");
                                    }
                                    else {
                                        $modelValue[$options.key].push(data);
                                    }
                                }
                            });
                    }
                }
            }
        ];
    }]);


