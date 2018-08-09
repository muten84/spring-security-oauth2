appServices.factory("privacyService", [
    '$rootScope',
    'loggerService',
    'userService',
    'apiService',
    '$q',
    '$ionicModal',
    'deviceService',
    '$timeout',
    '$translate',
    function ($rootScope, loggerService, user, api, $q, $ionicModal, device, $timeout, $translate) {

        var privacy = {

            /**
             * Verifica se la privacy è già stata accettata dall'utente
             * @returns {boolean}
             */
            isAccepted: function () {
                return user.isPrivacyAccepted();
            },

            /**
             * Gestisce tutto il flusso di controllo, presentazione e accettazione della privacy
             * @returns {*}
             */
            checkPrivacy: function () {
                var q = $q.defer();

                // Se l'utente ha già accettato la privacy entro nell'app
                if (privacy.isAccepted()) {
                    q.resolve();
                }
                // Altrimenti chiamo il server e recupero il testo
                else {
                    privacy.getPrivacyAgreement().then(
                        function (data) {
                            device.hideLoader();

                            var scope = $rootScope.$new();
                            scope.privacyText = data.agreementText;

                            $ionicModal.fromTemplateUrl('templates/dialog/privacy.html', {
                                scope: scope,
                                animation: 'animated slideInUp',
                                backdropClickToClose: false,
                                hardwareBackButtonClose: false
                            }).then(function (modal) {

                                modal.show();

                                /**
                                 * Invia al server la notifica di accettazione della privacy
                                 */
                                scope.accept = function () {

                                    device.showLoader();

                                    privacy.acceptPrivacyAgreement().then(
                                        function () {
                                            device.hideLoader();
                                            scope.hidePrivacyModal("OK");
                                            q.resolve();
                                        },
                                        function () {
                                            device.alert("OPERATION_FAILED", "primary");
                                        }
                                    )
                                };

                                /**
                                 * Chiude la modale della privacy
                                 */
                                scope.hidePrivacyModal = function () {
                                    modal.hide();
                                    $timeout(function () {
                                        modal.remove();
                                    }, 500);
                                }
                            });
                        },
                        function () {
                            // Se c'è stato un errore nel recuperare il testo della privacy entro nell'app
                            q.resolve();
                        }
                    )
                }

                return q.promise;
            },

            /**
             * Recupera dal server il testo della privacy
             * @returns {*}
             */
            getPrivacyAgreement: function () {
                return api.getPrivacy();
            },

            /**
             * Invia al server l'accettazione della privacy
             */
            acceptPrivacyAgreement: function () {
                return api.acceptPrivacy();
            }

        };

        return privacy;
    }
]);
