appControllers.controller('RegistrationCtrl', [
    '$rootScope',
    '$q',
    '$scope',
    '$state',
    'loggerService',
    'apiService',
    'userService',
    '$timeout',
    'deviceService',
    '$mdBottomSheet',
    '$cordovaImagePicker',
    '$cordovaCamera',
    '$translate',
    '$cordovaDatePicker',
    '$mdSelect',
    '$mdDialog',
    '$templateCache',
    function ($rootScope, $q, $scope, $state, logger, api, user, $timeout, device, $mdBottomSheet, $cordovaImagePicker, $cordovaCamera, $translate, $cordovaDatePicker, $mdSelect, $mdDialog, $templateCache) {
        logger.info("Caricamento RegistrationCtrl...");

        var vm = this;
        vm.photo = false;
        vm.certificato = false;
        vm.updateImageProfiloFailed = false;
        vm.showUploadBottomSheet = showUploadBottomSheet;
        vm.selectMedia = selectMedia;
        vm.addNewFirstResponder = addNewFirstResponder;
        vm.resetModel = resetModel;

        $scope.$on('$ionicView.enter', function () {
            init();
        });

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

        function showUploadBottomSheet($event, uploadType) {
            logger.info(uploadType);
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
                                    var image = document.getElementById('photo');
                                    var imageData = "data:image/jpeg;base64," + data;
                                    image.src = imageData;
                                    vm.registration.immagine.data = data;
                                },
                                function (error) {

                                }
                            );
                        }
                        else {
                            selectImage().then(
                                function (data) {
                                    vm.photo = true;
                                    var image = document.getElementById('photo');
                                    image.src = data;
                                    var base64 = data.split(',')[1];
                                    vm.registration.immagine.data = base64;
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

        function init() {
            api.getAllCategorie().then(
                function (data) {
                    vm.registration.categoriaFr = data[0];
                },
                function (error) {
                    logger.info(error);
                }
            );
        }

        function addNewFirstResponder() {
            device.showLoader();
            logger.info("[RegistrationCtrl:addNewFirstResponder]");
            logger.info(JSON.stringify(vm.registration));
            if (vm.newFRForm.$valid) {
                if (!vm.registration.comuniCompetenza) {
                    device.alert("Devi inserire almeno un comune", "balanced");
                }
                else if (vm.registration.comuniCompetenza.length == 0) {
                    device.alert("Devi inserire almeno un comune", "balanced");
                }
                else {
                    api.addNewFirstResponder(vm.registration).then(
                        function (data) {
                            logger.info(data);
                            device.hideLoader();
                            if (typeof data.response == 'undefined') {
                                device.alert("Conferma la registrazione tramite la mail che ti è stata inviata", "positive").then(
                                    function () {
                                        $state.go("app.login");
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
            vm.options.resetModel();
            // var autocompleteArray = document.getElementsByTagName('md-autocomplete-wrap');
            // if (autocompleteArray.length != 0){
            //     for (var i = 0; i < autocompleteArray.length; i++){
            //         var scope = angular.element(autocompleteArray[i]).scope();
            //         scope.$mdAutocompleteCtrl.clear();
            //     }
            // }
            vm.registration.indirizzo.id = null;
            vm.registration.indirizzo.name = null;
            vm.registration.comuneResidenza.id = null;
            vm.registration.comuneResidenza.cap = null;
            vm.registration.comuneResidenza.nomeComune = null;
            vm.registration.comuneResidenza.codiceIstat = null;
            vm.registration.comuneResidenza.provincia.id = null;
            vm.registration.comuneResidenza.provincia.nomeProvincia = null;

            var scope = angular.element(document.getElementsByTagName('md-autocomplete-wrap')).scope();
            if (scope) {
                scope.$mdAutocompleteCtrl.clear();
            }

            vm.photo = false;
            var image = document.getElementById('photo');
            image.src = "img/omino.png";
        }

        vm.registration = {
            id: null,
            nome: null,
            cognome: null,
            email: null,
            civico: null,
            indirizzo: {
                id: null,
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
                name: null
            },
            comuneResidenza: {
                id: null,
                nomeComune: null,
                cap: null,
                provincia: {
                    id: "",
                    nomeProvincia: ""
                }
            },
            comuniCompetenza: null,
            codiceFiscale: null,
            numCellulare: null,
            categoriaFr: {},
            certificatoFr: {
                id: null,
                descrizione: null,
                immagine: {
                    id: null,
                    data: null,
                    url: null
                },
                dataConseguimento: null,
                rilasciatoDa: null,
                dataScadenza: null,
                emailScadenza: null
            },
            immagine: {
                data: null
            },
            medicoFr: {
                id: null,
                dataLaurea: null,
                numIscrizioneOrdine: null
            }
        };
        vm.options = {
            formState: {
                medico: false,
                comuniCompetenza: {
                    id: null,
                    nomeComune: null
                }
            }
        };
        vm.registrationFields = [
            {
                className: "input-green",
                key: "logon",
                type: "input",
                templateOptions: {
                    type: "email",
                    label: "Email",
                    required: true
                },
                watcher: {
                    listener: function (field, newValue, oldValue, scope, stopWatching) {
                        if (newValue) {
                            scope.model.email = newValue;
                        }
                    }
                }
            },
            {
                className: "section-label",
                template: "<div style=\" font-size:20px; margin-top:0px;\">Verrà inviata una mail di verifica al tuo indirizzo, controlla di aver inserito un indirizzo valido</div>"
            },
            {
                className: "input-green",
                key: "password",
                type: "input",
                templateOptions: {
                    type: "password",
                    label: "Password",
                    required: true
                }
            },
            /*   {
                  className: "input-green",
                  type: "select",
                  key: "categoriaFr.id",
                  templateOptions: {
                      label: "Categoria First Responder",
                      placeholder: "",
                      required: true,
                      multiple: false,
                      labelProp: "descrizione",
                      valueProp: "id",
                      options: []
                  },
                  watcher: {
                      listener: function (field, newValue, oldValue, scope) {
                          api.getAllCategorie().then(
                              function (data) {
                                  scope.model.categoriaFr = data[0];
                                  field.templateOptions.options = [
                                      {
                                          id: data[0].id,
                                          descrizione: data[0].descrizione
                                      }
                                  ]
                              },
                              function (error) {
                                  logger.info(error);
                                  $mdSelect.hide();
                                  device.alert(error.message, "balanced");
                              }
                          );
                      }
                  }
              },  */
            {
                className: 'layout-row',
                fieldGroup: [
                    {
                        className: "flex-50 input-green",
                        key: "nome",
                        type: "input",
                        templateOptions: {
                            type: "text",
                            label: "Nome",
                            required: true
                        }
                    },
                    {
                        className: "flex-50 input-green",
                        key: "cognome",
                        type: "input",
                        templateOptions: {
                            type: "text",
                            label: "Cognome",
                            required: true
                        }
                    }
                ]
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
                type: "select",
                key: "id",
                model: vm.registration.comuneResidenza.provincia,
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
                            var obj = _.find(field.templateOptions.options, function (o) {
                                return o.id == newValue;
                            });
                            scope.model.comuneResidenza.provincia = obj;

                        }
                    }
                }
            },
            {
                className: "input-green",
                type: "select",
                key: "comuneResidenza.id",
                templateOptions: {
                    label: "Comune",
                    placeholder: "",
                    labelProp: "nomeComune",
                    valueProp: "id",
                    options: [],
                    onOpen: function ($modelValue, $inputValue, scope) {
                        logger.info('select is opened');
                        return api.searchComuniByFilter(scope.model.comuneResidenza.provincia.nomeProvincia).then(
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
                hideExpression: '!model.comuneResidenza.provincia.id',
                watcher: {
                    listener: function (field, newValue, oldValue, scope) {
                        if (newValue) {
                            var obj = _.find(field.templateOptions.options, function (o) {
                                return o.id == newValue;
                            });
                            scope.model.comuneResidenza = obj;

                        }
                    }
                }
            },
            // {
            //     className: "input-green",
            //     key: "nomeProvincia",
            //     model: vm.registration.comuneResidenza.provincia,
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
            //         // $scope.$watch('model.provincia.nomeProvincia', function (newValue, oldValue, theScope) {
            //         //         if (newValue !== oldValue) {
            //         //             $scope.autocomplete.selectedItem = "";
            //         //             apiService.searchComuniByFilter(newValue).then(
            //         //                 function (data) {
            //         //                     streets = data;
            //         //                     for (var i = 0; i < streets.length; i++) {
            //         //                         if (streets[i].id === $scope.model.id) {
            //         //                             $scope.autocomplete.selectedItem = streets[i];
            //         //                         }
            //         //                     }
            //         //                 }
            //         //             )
            //         //
            //         //         }
            //         //     }
            //         // );
            //
            //         init();
            //     }
            // },
            // {
            //     className: "input-green",
            //     key: "indirizzo.comune.nomeComune",
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
            //                             if (streets[i].id === $scope.model.id) {
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
            //                                 if (streets[i].id === $scope.model.id) {
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
                            $scope.model.comuneResidenza.id = item.comune.id;
                            $scope.model.comuneResidenza.nomeComune = item.comune.nomeComune;
                            $scope.model.comuneResidenza.codiceIstat = item.comune.codiceIstat;
                            $scope.model.comuneResidenza.cap = item.comune.cap;
                            $scope.model.comuneResidenza.provincia.id = item.comune.provincia.id;
                            $scope.model.comuneResidenza.provincia.nomeProvincia = item.comune.provincia.nomeProvincia;
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
            /*    {
                   className: "input-green",
                   type: "select",
                   key: "professione.id",
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
                               var obj = _.find(field.templateOptions.options, function (o) {
                                   return o.id == newValue;
                               });
                               scope.model.professione = obj;
   
                           }
                       }
                   }
               }, */
            {
                className: 'input-green',
                key: "dataConseguimento",
                model: vm.registration.certificatoFr,
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
                        }
                        else {
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
            /*  {
                 className: "input-green",
                 type: "dae-upload",
                 key: "data",
                 model: vm.registration.certificatoFr.immagine,
                 templateOptions: {
                     label: "Upload Certificato BLSD / Diploma",
                     required: true,
                     btnText: "Upload del diploma",
                     onclick: function ($modelValue, $options) {
                         $mdBottomSheet.show({
                             templateUrl: 'templates/bottomSheet/uploadPhoto.html',
                             scope: $scope.$new(false)
                         }).then(function (media) {
                             logger.info("<<<<" + media + ">>>>");
                             checkPermission().then(
                                 function () {
                                     if (media === "CAMERA") {
                                         getPicture().then(
                                             function (data) {
                                                 $modelValue[$options.key] = data;
                                                 $options.templateOptions.message = "Upload completato";
                                             },
                                             function (error) {
                                                 $options.templateOptions.message = "Si è verificato un errore";
                                             }
                                         );
                                     }
                                     else {
                                         selectImage().then(
                                             function (data) {
                                                 var base64 = data.split(',')[1];
                                                 $modelValue[$options.key] = base64;
                                                 $options.templateOptions.message = "Upload completato";
                                             },
                                             function (error) {
                                                 $options.templateOptions.message = "Si è verificato un errore";
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
                 }
             },  */
            {
                className: "input-green",
                type: "dae-municipalityList",
                key: "comuniCompetenza",
                templateOptions: {
                    label: "Comuni sui quali essere allertato",
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