var boot = {
    showAlert: function (msg, title, button) {
        title = title || _APPNAME_;
        button = button || "OK";
        if (navigator && navigator.notification) {
            navigator.notification.alert(
                msg,
                function () {
                },
                title,
                button
            );
        }
        else {
            alert(msg);
        }
    },
    initialize: function () {
        this.bindEvents();
    },
    bindEvents: function () {
        if (!!window.cordova) {
            // bind deviceready callback for phonegap devices
            document.addEventListener('deviceready', this.onDeviceReady, true);
        }
        else {
            // running on desktop, just call device ready immediately
            this.onDeviceReady();
        }
    },
    onDeviceReady: function () {
        console.log("onDeviceReady");

        // Definisco le configurazioni dell'app
        var mainConfig = {
            url: _CONFIG_URL_,
            statusBarColor: "#F8B330"
        };

        // Scarico il file di configurazione aggiornato
        $.ajax({
            url: mainConfig.url,
            method: "GET",
            cache: false,
            timeout: 30000,
            dataType: "json",
            success: function (config) {
                console.log("File di configurazione scaricato correttamente %s", JSON.stringify(config));

                mainConfig = _.merge(mainConfig, config);

                localStorage.setItem("CONFIG", JSON.stringify(mainConfig));

                angular.element(document).ready(function () {
                    console.log("Starting %s", _APP_);
                    angular.bootstrap(document, [_APP_]);
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Impossibile recuperare file di configurazione");

                console.log("jqXHR:" + JSON.stringify(jqXHR));
                console.log("textStatus:" + JSON.stringify(textStatus));
                console.log("errorThrown:" + JSON.stringify(errorThrown));

                console.log("Cerco nello Storage...");
                var config = localStorage.getItem("CONFIG");
                if (config) {
                    mainConfig = _.merge(mainConfig, JSON.parse(config));

                    angular.element(document).ready(function () {
                        console.log("Starting %s", _APP_);
                        angular.bootstrap(document, [_APP_]);
                    });
                }
                else {
                    boot.showAlert("Verifica la copertura di rete e riavvia l'app", "Donatore");
                }
            }
        });
    }
};
