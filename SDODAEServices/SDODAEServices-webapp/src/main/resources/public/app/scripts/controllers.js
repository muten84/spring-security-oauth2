/*definizione dei controllers*/
'use strict';

define(["angular"], function(angular) {
    var controllerPath = "controllers";

    return [
        controllerPath + "/mainCtrl",
        controllerPath + "/login",
        controllerPath + "/updatePasswordCtrl",
        controllerPath + "/dae/daeSearchController",
        controllerPath + "/dae/daeInsertController",
        controllerPath + "/dae/daeMapController",
        controllerPath + "/dae/daeFaultController",
        controllerPath + "/fr/frSearchController",
        controllerPath + "/fr/frUpdateController",
        controllerPath + "/navBarController",
        controllerPath + "/dashboardController",
        controllerPath + "/notification/notificationSendController",
        controllerPath + "/notification/notificationSearchController",
        controllerPath + "/configuration/configurationController",
        controllerPath + "/mail/mailEditController",
        controllerPath + "/mail/mailListController",
        controllerPath + "/user/userSearchController",
        controllerPath + "/user/userInsertController",
        controllerPath + "/user/userProfileController",
        controllerPath + "/user/groupInsertController",
        controllerPath + "/event/eventSearchController",
        controllerPath + "/event/eventDetailController",
        controllerPath + "/report/graphController",
    ];
});
