'use strict';

define(['angular', "moment", "constants", "lodash"], function (angular, moment, constants, _) {
    angular.module("controller").controller('DaeInsertCtrl', ["$scope", "$filter", "daeService",
        "anagraficaService", "loggerService", "$rootScope", "alertService", "modalService",
        "currentDae", "$document", "validateService", "toastr", "FileUploader",
        '$window', "utilityService", "$indexedDB", "$uibModal", "uiGridConstants",
        function ($scope, $filter, daeService, anagraficaService,
            loggerService, $rootScope, alertService, modalService,
            currentDae, $document, validateService, toastr, FileUploader,
            $window, utilityService, $indexedDB, $uibModal, uiGridConstants) {

            $rootScope.$broadcast("main.changeTitle", {
                title: "Gestione DAE",
                icon: "fa fa-heartbeat"
            });

            angular.extend($scope, {
                options: {
                    /*  customClass: getDayClass,*/
                    minDate: new Date(0),
                    showWeeks: false
                },
                birthDatePopup: {
                    opened: false
                },
                //configurazioni per il pannello delle disponibilita
                disp: {
                    popupA: {
                        opened: false
                    },
                    popupDA: {
                        opened: false
                    },
                },
                //gestione invio immagine
                // see :https://github.com/nervgh/angular-file-upload
                uploader: new FileUploader(),
                showUpload: true,
                //oggetto da salvare
                dae: {
                    responsabile: {},
                    disponibilita: [],
                    programmiManutenzione: []
                },
                comuni: [],
                strutture: [],
                localita: [],
                strade: [],
                giorniSettimana: ['LUNEDI', 'MARTEDI', 'MERCOLEDI', 'GIOVEDI', 'VENERDI', 'SABATO', 'DOMENICA'],
                mesiAnno: [],
                groups: [],
                manutenzioni: [],
                tipoDisponibilita: [],
                stati: []
            });

            for (var i = 1; i <= 12; i++) {
                $scope.mesiAnno.push(moment(i + "", "MM").format("MMMM").toUpperCase());
            }

            angular.extend($scope.uploader, {

                onAfterAddingFile: function () {
                    $scope.showUpload = false;
                },
                onBeforeUploadItem: function () {
                    //appena parte l'upload tolgo l'immagine
                    $scope.dae.immagine.urlBkp = $scope.dae.immagine.url;
                    $scope.dae.immagine.url = null;
                },
                //errore nell'upload
                onErrorItem: function () {
                    $scope.uploader.clearQueue();
                    $scope.dae.immagine.url = $scope.dae.immagine.urlBkp;
                    toastr.error("Errore nell'invio dell'immagine al server", "Errore");
                },
                //upload completato
                onCompleteAll: function () {
                    toastr.success("Immagine inviata al server", "Salvataggio");
                    $scope.uploader.clearQueue();
                    //dopo l'invio dell'immagine ricarico il dae
                    var filter = {
                        id: $scope.dae.id
                    };
                    daeService.searchDAEByFilter(filter).then(function (result) {
                        $scope.setDae(result[0]);
                    });
                }
            });

            $scope.formatDate = function (date) {
                if (date) {
                    return moment(date).format("DD/MM/YYYY");
                }
                return "";
            };

            $scope.annullaInvioImmagine = function () {
                $scope.showUpload = true;
                $scope.uploader.clearQueue();
            };

            function dayValue(day) {
                var arr = $scope.giorniSettimana;
                for (var i = 0, len = arr.length; i < len; i++) {
                    if (day == arr[i]) {
                        return i;
                    }
                }
            }

            function ordinaDisponibilitaGiornaliera(dispG) {
                return _.sortBy(dispG, [function (o) {
                    return dayValue(o.giornoSettimana);
                }]);
            }

            function cleanDAE(dae) {
                var newDae = JSON.parse(JSON.stringify(dae));

                if (dae.statoValidazioneBoolean) {
                    newDae.statoValidazione = "VALIDATO";
                } else {
                    newDae.statoValidazione = "DA_VALIDARE";
                }

                if ($scope.disponibilitaPermanente(newDae)) {
                    newDae.disponibilita = [];
                }

                if (newDae.disponibilita) {
                    newDae.disponibilita.forEach(function (disp) {
                        if (disp.disponibilitaSpecifica && disp.disponibilitaSpecifica.disponibilitaGiornaliera) {
                            disp.disponibilitaSpecifica.disponibilitaGiornaliera.forEach(function (dispG) {
                                dispG.orarioDa = moment(dispG.orarioDa).format('HH:mm');
                                dispG.orarioA = moment(dispG.orarioA).format('HH:mm');
                            });
                        }
                    });
                }
                return newDae;
            }

            //inserisce il dae nell'interfaccia
            $scope.setDae = function (dae) {
                //creo l'url per l'upload dell'immagine
                $scope.showUpload = true;
                $scope.uploader.url = daeService._getUrlService() + 'uploadImmagine/' + dae.id;

                if (dae.immagine && dae.immagine.url) {
                    //serve per evitare il cache dell'immagine da parte del browser
                    dae.immagine.url = dae.immagine.url + '?t=' + Date.now();
                }

                $scope.uploader.headers = {
                    authorization: "Bearer {{TOKEN}}".replace('{{TOKEN}}', $window.sessionStorage.token)
                };

                dae.statoValidazioneBoolean = dae.statoValidazione === "VALIDATO";

                if (dae.disponibilita) {
                    var toRem = [];
                    dae.disponibilita.forEach(function (disp, index) {
                        if (disp.deleted) {
                            toRem.push(index);
                        }
                        if (disp.da) {
                            disp.daMonth = moment(disp.da).format("MMMM").toUpperCase();
                        }
                        if (disp.a) {
                            disp.aMonth = moment(disp.a).format("MMMM").toUpperCase();
                        }

                        if (disp.disponibilitaSpecifica && disp.disponibilitaSpecifica.disponibilitaGiornaliera) {
                            disp.disponibilitaSpecifica.disponibilitaGiornaliera = ordinaDisponibilitaGiornaliera(disp.disponibilitaSpecifica.disponibilitaGiornaliera);
                            disp.disponibilitaSpecifica.disponibilitaGiornaliera.forEach(function (dispG) {
                                dispG.orarioDa = moment(dispG.orarioDa, 'HH:mm');
                                dispG.orarioA = moment(dispG.orarioA, 'HH:mm');
                            });
                        }
                    });

                    _.pullAt(dae.disponibilita, toRem);
                }

                if (dae.programmiManutenzione) {
                    //segnalo tutti i programmi con data di scadenza prossima nei 2 mesi

                    dae.programmiManutenzione.forEach(function (prog) {
                        prog.invalid = moment().add(2, 'M').isAfter(prog.scadenzaDopo);
                    });
                    dae.programmiManutenzione = _.sortBy(dae.programmiManutenzione, ['scadenzaDopo']);
                }
                $scope.dae = dae;
            };
            //Carico il DAE dal db e riempio la maschera
            if (!_.isUndefined(currentDae)) {
                $scope.setDae(currentDae[0]);
                daeService.setCurrentDaeId(undefined);
            } else {
                //se si è arrivati direttamente senza passare dalla ricerca, controllo se non sia presente il dae nello store
                $indexedDB.openStore(constants.storeNameDAE, function (store) {
                    //cerco nello store la presenza di dati precedentemente caricati
                    store.find("1").then(function (obj) {
                        $scope.setDae(obj.dae);
                    });
                });
            }
            $scope.$watch("dae", function (newValue, oldValue) {
                if (newValue != oldValue) {
                    $indexedDB.openStore(constants.storeNameDAE, function (store) {
                        store.upsert({
                            "id": "1",
                            "dae": $scope.dae
                        });
                    });
                }
            }, true);

            $scope.disponibilitaPermanente = function (dae) {
                return dae.tipologiaDisponibilita !== 'DA PROGRAMMA';
            };

            $scope.aggiungiDisponibilitaGiorno = function (disp, dispGiornDay) {
                if (dispGiornDay) {
                    if (!disp.disponibilitaSpecifica) {
                        disp.disponibilitaSpecifica = {
                            disponibilitaGiornaliera: []
                        };
                    }
                    //aggiungo il giorno selezionato alla disponibilità
                    disp.disponibilitaSpecifica.disponibilitaGiornaliera.push({
                        "giornoSettimana": dispGiornDay,
                        "orarioDa": disp.orarioDaApp,
                        "orarioA": disp.orarioAApp
                    });
                    //ordino le disponibilità giornaliere per giorno
                    disp.disponibilitaSpecifica.disponibilitaGiornaliera = ordinaDisponibilitaGiornaliera(disp.disponibilitaSpecifica.disponibilitaGiornaliera);
                } else {
                    modalService.openModal("warningModal.html", "Attenzione", {
                        message: {
                            title: "Verifica dati inseriti",
                            text: "Attenzione selezionare un giorno dalla combobox"
                        }
                    });
                }
            };
            $scope.rimuoviDisponibilitaGiorno = function (disp, index) {
                _.pullAt(disp.disponibilitaSpecifica.disponibilitaGiornaliera, [index]);
            };

            //Pulizia campi
            $scope.pulisci = function () {

                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Premendo ok tutti i dati verranno persi",
                    ok: function () {
                        $scope.newDAE();
                    },
                    cancel: function () {}
                });
            };

            $scope.duplica = function () {

                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Vuoi duplicare il DAE salvato?",
                    ok: function () {
                        var copy = JSON.parse(JSON.stringify($scope.dae));
                        $scope.newDAE();

                        $scope.dae.disponibilita = $scope.purge(copy.disponibilita, ['id']);
                    },
                    cancel: function () {}
                });
            };

            $scope.purge = function (obj, props) {
                var copy;
                if (_.isArray(obj)) {
                    copy = obj.map(function (arrVal) {
                        return $scope.purge(arrVal, props);
                    });
                } else {
                    copy = {};
                    Object.keys(obj).forEach(function (key) {
                        //se la proprietà non è inclusa nelle proprietà da omettere
                        if (!_.includes(props, key)) {

                            var val = obj[key];
                            if (_.isPlainObject(val)) {
                                //se la proprietà è un oggetto
                                copy[key] = $scope.purge(val, props);
                            } else if (_.isArray(val)) {
                                copy[key] = val.map(function (arrVal) {
                                    return $scope.purge(arrVal, props);
                                });
                            } else {
                                copy[key] = val;
                            }
                        }
                    });
                }
                //var copy = _.omit(obj, props);
                return copy;
            };

            $scope.newDAE = function () {
                $scope.dae = {
                    operativo: false,
                    responsabile: {},
                    disponibilita: [],
                    programmiManutenzione: []
                };
            };

            $scope.saveDAE = function () {
                if (!$scope.daeForm.$invalid) {
                    //creo una copia dell'oggetto nella view e lo pulisco dei campi non validi
                    var dae = cleanDAE($scope.dae);
                    //se non è stata selezionata la disponibilità permanente
                    if (!$scope.disponibilitaPermanente(dae)) {
                        //se non è presente nessuna disponibilità
                        if (_.isEmpty(dae.disponibilita)) {
                            modalService.openYesNoModal({
                                title: "Attenzione",
                                text: "Inserire la disponibilità o indicare la disponibilità permanente",
                                ok: function () {}
                            });
                            return;
                        }
                    }

                    if (dae.tipologiaDisponibilita === "DA PROGRAMMA") {
                        // se è stata selezionata la disponbilità da programma 
                        // controllo che sia stato inserito un programma valido 
                        let notPresent = false;
                        if (dae.disponibilita) {
                            dae.disponibilita.forEach(function (disp) {
                                if (disp.daMonth) {
                                    disp.da = moment(disp.daMonth.toLowerCase() + "/1970", "MMMM/YYYY");
                                }
                                if (disp.aMonth) {
                                    disp.a = moment(disp.aMonth.toLowerCase() + "/2070", "MMMM/YYYY");
                                }

                                if (disp.disponibilitaSpecifica &&
                                    disp.disponibilitaSpecifica.disponibilitaGiornaliera &&
                                    disp.disponibilitaSpecifica.disponibilitaGiornaliera.length > 0) {
                                        notPresent = false;
                                } else {
                                    notPresent = true;
                                }
                            });
                        } else {
                            notPresent = true;
                        }

                        if (notPresent) {
                            modalService.openYesNoModal({
                                title: "Attenzione",
                                text: "È stata scelta la disponibilità programmata ma non è stato inserito un programma valido. Inserire almeno un giorno della settmana",
                                ok: function () {}
                            });
                            return;
                        }
                    }



                    daeService.saveDae(dae).then(function (response) {
                        if (response.duplicate === true) {

                            modalService.openYesNoModal({
                                title: "Attenzione",
                                text: response.message + "<br>Sede: " + response.dae[0].nomeSede +
                                    " <br> Comune: " + response.dae[0].gpsLocation.comune.nomeComune +
                                    "<br> Indirizzo: " + response.dae[0].gpsLocation.indirizzo.name,
                                ok: function () {}
                            });
                        } else {
                            toastr.success('Salvataggio dei dati avvenuto con successo', 'Salvataggio avvenuto');
                            $scope.setDae(response);

                            $scope.requestClean();
                        }
                    });
                } else {
                    /*debugger;*/
                    modalService.openModal("warningModal.html", "Attenzione", {
                        message: {
                            title: "Verifica dati inseriti",
                            text: "Attenzione verificare i dati della form"
                        }
                    });
                }
            };

            $scope.requestClean = function () {
                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Pulire la maschera per inserire un nuovo DAE?",
                    ok: function () {
                        $scope.newDAE();
                    },
                    cancel: function () {}
                });
            };

            var handler = $rootScope.$on('leafletDirectiveMap.click', function (event, c) {
                if (!$scope.dae.gpsLocation) {
                    $scope.dae.gpsLocation = {};
                }

                $scope.app = {
                    latitudine: c.leafletEvent.latlng.lat,
                    longitudine: c.leafletEvent.latlng.lng
                };

                if (!$scope.markers[0]) {
                    $scope.markers[0] = {
                        focus: true,
                        draggable: false,
                        icon: constants.daeMapIcon
                    };
                }
                $scope.markers[0].lat = c.leafletEvent.latlng.lat;
                $scope.markers[0].lng = c.leafletEvent.latlng.lng;
            });

            $scope.$on("$destroy", function (event) {
                //distruggo l'handler
                handler();
            });

            $scope.showMappa = function () {
                $scope.markers = [];
                if (validateService.validateLocation($scope.dae.gpsLocation)) {
                    var gpsLocation = $scope.dae.gpsLocation;
                    $scope.markers.push({
                        lat: Number(gpsLocation.latitudine),
                        lng: Number(gpsLocation.longitudine),
                        focus: true,
                        draggable: false,
                        message: utilityService.daeTooltip($scope.dae),
                        icon: constants.daeMapIcon
                    });
                }
                var modalInstance = modalService.openMapModal({
                    title: "DAE",
                    markers: $scope.markers,
                    events: ['click'],
                    save: true
                });

                modalInstance.result.then(function () {
                    if ($scope.app.latitudine) {
                        $scope.dae.gpsLocation.latitudine = $scope.app.latitudine;
                        $scope.dae.gpsLocation.longitudine = $scope.app.longitudine;
                    }
                });

            };
            /** Gestione Disponibilita*/
            $scope.aggiungiDisponibilita = function () {
                //aggiungo un elemento all'array
                /*Luigi soluzione immediata per bloccare a 1 posto dubbio a Donatella per capire se va bene cosi*/
                if ($scope.dae.disponibilita.length == 0) {
                    $scope.dae.disponibilita.push({
                        "orarioDa": "00:00",
                        "orarioA": "23:59",
                        "daMonth": "GENNAIO",
                        "aMonth": "DICEMBRE",
                        disponibilitaSpecifica: {
                            disponibilitaGiornaliera: []
                        }
                    });
                }
            };
            $scope.aggiungiDisponibilitaPiena = function () {
                //aggiungo un elemento all'array
                if ($scope.dae.disponibilita.length == 0) {
                    var disponibilita = {
                        "orarioDa": "00:00",
                        "orarioA": "23:59",
                        "daMonth": "GENNAIO",
                        "aMonth": "DICEMBRE",
                        disponibilitaSpecifica: {
                            disponibilitaGiornaliera: []
                        }
                    };

                    $scope.giorniSettimana.forEach(function (el) {
                        disponibilita.disponibilitaSpecifica.disponibilitaGiornaliera.push({
                            giornoSettimana: el,
                            orarioDa: moment("00:00", 'HH:mm'),
                            orarioA: moment("23:59", 'HH:mm'),
                        });
                    });

                    $scope.dae.disponibilita.push(disponibilita);
                }
            };
            $scope.removeDisponibilita = function (disp, index) {
                _.pullAt($scope.dae.disponibilita, [index]);
            };
            /* -------------------------------- */

            /** Gestione programma manutenzione*/
            $scope.aggiungiProgramma = function () {
                if (!$scope.dae.tmpTipoManutenzione) {
                    modalService.openYesNoModal({
                        title: "Attenzione",
                        text: "Seleziona il tipo di manutenzione",
                        ok: function () {}
                    });
                    return;
                }
                //controllo se sono presenti altri programmi manutenzione con lo stesso tipo
                var foundTipo = false;
                if ($scope.dae.programmiManutenzione) {
                    $scope.dae.programmiManutenzione.forEach(function (p) {
                        if (p.tipoManutenzione === $scope.dae.tmpTipoManutenzione) {
                            foundTipo = true;
                        }
                    });
                }

                if (foundTipo) {
                    modalService.openYesNoModal({
                        title: "Attenzione",
                        text: "Hai già aggiunto un programma manutenzione di tipo " + $scope.dae.tmpTipoManutenzione,
                        ok: function () {}
                    });
                    return;
                }

                //aggiungo un elemento vuoto all'array
                $scope.dae.programmiManutenzione.push({
                    tipoManutenzione: $scope.dae.tmpTipoManutenzione
                });

                $scope.dae.tmpTipoManutenzione = undefined;
            };

            $scope.removeProgramma = function (prog, index) {
                //aggiungo un elemento vuoto all'array
                _.pullAt($scope.dae.programmiManutenzione, [index]);
            };
            /* -------------------------------- */
            $scope.refreshComune = function (input) {
                if (input === undefined || input === null || !input || input.length < 3) {
                    $scope.comuni = [];
                    return [];
                }

                var filter = {
                    "nomeComune": input
                };
                return anagraficaService.searchComuniByFilter(filter).then(function (response) {
                    $scope.comuni = response;
                });
            };
            /* Combo Tipo Struttura*/
            $scope.refreshStrutture = function (search) {
                var filter = {};
                if (search !== undefined || search !== null) {
                    filter.name = search;
                }
                return anagraficaService.getAllTipoStruttura(filter).then(function (response) {
                    $scope.strutture = response;
                });
            };


            /*Combo INDIRIZZO*/
            $scope.selectStrada = function (selected) {
                if (!$scope.dae.gpsLocation) {
                    $scope.dae.gpsLocation = {};
                }
                if (selected) {
                    $scope.dae.gpsLocation.comune = selected.comune;
                    if ($scope.dae.gpsLocation.localita) {
                        $scope.dae.gpsLocation.localita[0] = selected.locality;
                    }
                }
            };
            $scope.refreshStrade = function (search) {
                if (search === undefined || search === null || !search || search.length < 3) {
                    $scope.strade = [];
                    return [];
                }
                var filter = {
                    "name": search
                };
                if ($scope.dae.gpsLocation && $scope.dae.gpsLocation.comune) {
                    filter.codiceIstatComune = $scope.dae.gpsLocation.comune.codiceIstat;
                }
                return anagraficaService.searchStradeByFilter(filter).then(function (response) {
                    $scope.strade = response;
                });
            };

            /*Combo LOCALITA*/
            $scope.refreshLocalita = function (search) {
                if (search === undefined || search === null || !search || ($scope.dae.gpsLocation.comune == null && search.length < 3)) {
                    $scope.localita = [];
                    return [];
                }
                var filter = {
                    "name": search
                };
                if ($scope.dae.gpsLocation && $scope.dae.gpsLocation.comune) {
                    filter.codiceIstatComune = $scope.dae.gpsLocation.comune.codiceIstat;
                }

                return anagraficaService.searchLocalitaByFilter(filter).then(function (response) {
                    $scope.localita = response;
                });
            };

            /* Combo Tipo Manutenzione*/
            anagraficaService.getAllTipoManutenzione().then(function (response) {
                $scope.manutenzioni = response;
            });

            /* Combo Tipo Disponibilita*/
            anagraficaService.getAllTipoDisponibilita().then(function (response) {
                $scope.tipoDisponibilita = response;
            });

            /* Combo Stato*/
            anagraficaService.getAllStato().then(function (response) {
                $scope.stati = response;
            });

            /* GESTIONE date piker */
            $scope.clear = function () {
                $scope.dae.responsabile.dataNascita = null;
            };

            $scope.checkChildren = function (id) {
                var invalids = $document.find('#' + id + ' .ng-invalid');
                if (invalids && !_.isEmpty(invalids)) {
                    return false;
                } else {
                    return true;
                }
            };

            $scope.open = function (url) {
                $window.open(url + '&size=LARGE');
            };

            $scope.changeDisponibilita = function () {
                if ($scope.disponibilitaPermanente($scope.dae)) {
                    $scope.dae.disponibilita = [];
                }
            };

            $scope.geocodeAddress = function () {
                if ($scope.dae.gpsLocation.comune && $scope.dae.gpsLocation.indirizzo) {
                    var filter = {
                        street: $scope.dae.gpsLocation.indirizzo.name,
                        municipality: $scope.dae.gpsLocation.comune.nomeComune,
                        houseNumber: $scope.dae.gpsLocation.civico
                    };

                    anagraficaService.geocodeAddress(filter).then(function (res) {
                        if (res) {
                            if (res.precision === 'HOUSE_NUMBER') {

                            } else if (res.precision === 'STREET') {
                                modalService.openYesNoModal({
                                    title: "Attenzione",
                                    text: "Coordinate precise sul Centro Strada",
                                    ok: function () {}
                                });
                            } else if (res.precision === 'CITY') {
                                modalService.openYesNoModal({
                                    title: "Attenzione",
                                    text: "Coordinate precise sul Centro Comune",
                                    ok: function () {}
                                });
                            }

                            if (res.position && res.position.x && res.position.y) {
                                $scope.dae.gpsLocation.latitudine = res.position.y;
                                $scope.dae.gpsLocation.longitudine = res.position.x;
                            }
                        }
                    });
                }
            };

            $scope.reverseGeocoding = function () {
                if ($scope.validateCoord($scope.dae.gpsLocation.latitudine) && $scope.validateCoord($scope.dae.gpsLocation.longitudine)) {
                    var filter = {
                        latitudine: $scope.dae.gpsLocation.latitudine,
                        longitudine: $scope.dae.gpsLocation.longitudine
                    };

                    anagraficaService.reverseGeocoding(filter).then(function (res) {
                        if (res && res.strada) {
                            $scope.dae.gpsLocation.indirizzo = res.strada;
                            $scope.dae.gpsLocation.comune = res.strada.comune;
                            $scope.dae.gpsLocation.localita = res.localita;
                        }
                    });
                }
            };

            $scope.elimina = function () {
                modalService.openYesNoModal({
                    title: "Attenzione",
                    text: "Premendo ok il DAE verrà eliminato",
                    ok: function () {
                        var dae = {
                            id: $scope.dae.id
                        };
                        daeService.deleteDAE(dae).then(function (res) {
                            $scope.newDAE();
                        });
                    },
                    cancel: function () {}
                });
            };

            $scope.showHistoryProgramma = function (prog, i) {
                daeService.getProgrammaHistory(prog.id).then(function (res) {
                    $scope.openProgList(res);
                });
            };


            $scope.openProgList = function (res) {
                var parentElem = angular.element($document[0].querySelector('.modal-attach'));
                var parentScope = $scope;
                var modalInstance = $uibModal.open({
                    /* Prevengono l'uscita cliccando fuori oppure esc*/
                    backdrop: 'static',
                    keyboard: false,
                    animation: true,
                    templateUrl: "programmiModal.html",
                    appendTo: parentElem,
                    size: 'lg',
                    controller: function ($scope, $uibModalInstance) {
                        var $ctrl = this;
                        $scope.formatDate = function (date) {
                            if (date) {
                                return moment(date).format("DD/MM/YYYY HH:mm");
                            }
                            return "";
                        };

                        $scope.gridProgrammiOptions = {
                            data: res,
                            enableGridMenu: true,
                            enableHorizontalScrollbar: 0,
                            enableVerticalScrollbar: 1,
                            onRegisterApi: function (gridApi) {
                                $scope.gridProgrammiApi = gridApi;
                            },
                            columnDefs: [{
                                displayName: 'Utente Modifica',
                                field: 'utenteLogon',
                                cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{ row.entity.utenteLogon}}\" tooltip-placement=\"left\" >{{ row.entity.utenteLogon}}</div>",
                            }, {
                                displayName: 'Data Modifica',
                                field: 'operationDate',
                                cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{grid.appScope.formatDate(row.entity.operationDate)}}\" tooltip-placement=\"left\" >{{grid.appScope.formatDate(row.entity.operationDate)}}</div>",
                                sort: {
                                    direction: uiGridConstants.ASC,
                                    priority: 1
                                },
                            }, {
                                displayName: 'Scadenza Dopo',
                                field: 'scadenzaDopo',
                                cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{grid.appScope.formatDate(row.entity.scadenzaDopo)}}\" tooltip-placement=\"left\" >{{grid.appScope.formatDate(row.entity.scadenzaDopo)}}</div>",
                            }, {
                                displayName: 'Nota',
                                field: 'nota',
                                cellTemplate: "<div class=\"ui-grid-cell-contents\" uib-tooltip=\"{{ row.entity.nota}}\" tooltip-placement=\"left\" >{{ row.entity.nota}}</div>",
                            }]
                        }
                    }
                });
            }

            //Funzione di validazione : False se il valore non è valido, True se è valido
            $scope.validate = validateService.validate;
            //valida la singola coordinata
            $scope.validateCoord = validateService.validateCoord;
        }
    ]);
});