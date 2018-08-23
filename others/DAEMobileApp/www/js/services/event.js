appServices.factory("alertService", [
    '$rootScope',
    'loggerService',
    'localStorageService',
    'loggerService',
    '$injector',
    '$q',
    '$timeout',
    function ($rootScope, loggerService, storage, logger, $injector, $q, $timeout) {

        $rootScope.eventCounter = 0;
        var currentEvent = {
            id: null,
            coRiferimento: null,
            civico: null,
            indirizzo: null,
            comune: null,
            provincia: null,
            cartellino: null,
            operation: null,
            message: null,
            managed: false,
            location: {
                latitude: null,
                longitude: null
            }
        };

        var eventList = [];

        return {
            setCurrentEvent: function () {
                var event = eventList[0];
                currentEvent.id = event.id;
                currentEvent.coRiferimento = event.coRiferimento;
                currentEvent.civico = event.civico;
                currentEvent.indirizzo = event.indirizzo;
                currentEvent.comune = event.comune;
                currentEvent.provincia = event.provincia;
                currentEvent.cartellino = event.cartellino;
                currentEvent.location.latitude = event.location.latitude;
                currentEvent.location.longitude = event.location.longitude;
                currentEvent.closed = event.closed;
                currentEvent.timestamp = event.timestamp;
                currentEvent.frAcceptDate = event.frAcceptDate;
                currentEvent.frCloseDate = event.frCloseDate;

            },

            updateEvent: function (notification) {
                if (currentEvent.id === notification.userdata.eventId) {
                    currentEvent.operation = notification.userdata.operation;
                    currentEvent.message = notification.message;
                }
            },

            abortEvent: function (notification) {
                if (currentEvent.id === notification.userdata.eventId) {
                    this.remove();
                }
                else {
                    _.remove(eventList, function (o) {
                        return o.id == notification.userdata.eventId;
                    });
                    $timeout(function () {
                        $rootScope.eventCounter = eventList.length;
                    }, 0);
                }
            },

            resetEvent: function () {
                currentEvent.id = null;
                currentEvent.operation = null;
                currentEvent.message = null;
                currentEvent.managed = false;
                currentEvent.coRiferimento = null;
                currentEvent.civico = null;
                currentEvent.indirizzo = null;
                currentEvent.comune = null;
                currentEvent.provincia = null;
                currentEvent.cartellino = null;
                currentEvent.location.latitude = null;
                currentEvent.location.longitude = null;
            },

            addEvents: function (events) {
                $timeout(function () {
                    $rootScope.eventCounter = null;
                }, 0);
                this.clearEventList();
                this.resetEvent();
                if (events.length > 0) {
                    var obj = _.find(events, function (o) {
                        return o.managementStatus === "SELF_MANAGED" || o.managementStatus === "SELF_MANAGED_CLOSED";
                    });

                    if (obj) {
                        var event = this.createEvent(obj);
                        eventList.push(event);
                        this.setCurrentEvent();
                        currentEvent.managed = true;
                        $timeout(function () {
                            $rootScope.eventCounter = eventList.length;
                        }, 0);
                    }
                    else {
                        for (var i = 0; i < events.length; i++) {
                            var event = this.createEvent(events[i]);
                            eventList.push(event);
                        }
                        this.setCurrentEvent();
                        $timeout(function () {
                            $rootScope.eventCounter = eventList.length;
                        }, 0);
                    }
                }
            },

            createEvent: function (obj) {
                var event = {};
                event.id = obj.id;
                event.coRiferimento = obj.coRiferimento;
                event.civico = obj.civico;
                event.indirizzo = obj.indirizzo;
                event.comune = obj.comune
                event.provincia = obj.provincia;
                event.cartellino = obj.cartellino;
                event.location = {};
                event.location.latitude = obj.coordinate.latitudine;
                event.location.longitude = obj.coordinate.longitudine;
                event.closed = obj.managementStatus === "SELF_MANAGED_CLOSED";
                event.timestamp = obj.timestamp;
                event.frAcceptDate = obj.frAcceptDate;
                event.frCloseDate = obj.frCloseDate;
                return event;
            },

            remove: function () {
                eventList.shift();
                this.resetEvent();
                $timeout(function () {
                    $rootScope.eventCounter = eventList.length;
                }, 0);
            },

            clearEventList: function () {
                eventList = [];
                $timeout(function () {
                    $rootScope.eventCounter = eventList.length;
                }, 0);
            },

            size: function () {
                return eventList.length;
            },

            setLocation: function (lat, long) {
                currentEvent.location.latitude = lat;
                currentEvent.location.longitude = long;
            },

            getMessage: function () {
                return currentEvent.message;
            },

            getLocation: function () {
                return currentEvent.location;
            },

            getEventId: function () {
                return currentEvent.id;
            },

            getCartellino: function () {
                return currentEvent.cartellino;
            },

            getCoRiferimento: function () {
                return currentEvent.coRiferimento;
            },

            manageEvent: function (val) {
                currentEvent.managed = val;
                // mi salvo la data di accettazone in memoria
                currentEvent.frAcceptDate = new Date();
            },

            isManaged: function () {
                return currentEvent.managed;
            },

            isClosed: function () {
                return currentEvent.closed;
            },

            isPlaceArrival: function () {
                return !!currentEvent.frCloseDate;
            },

            getCurrentEvent() {
                return currentEvent;
            }
        }
    }
])
    ;