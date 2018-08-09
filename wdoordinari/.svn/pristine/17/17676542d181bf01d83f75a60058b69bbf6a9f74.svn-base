
import { NgModule } from '@angular/core';
import { Http, XHRBackend, RequestOptions } from '@angular/http';

import { ExtendedHttpService } from '../app/service/extended-http.service'
import { BASE_PATH } from './gen/variables';
import * as services from './gen/api/api';

export * from './gen/index';

import { environment } from '../environments/environment';

@NgModule({
    declarations: [
    ],
    imports: [
    ],
    providers: [
        services.AAAControllerService,
        services.AuthorityModuleServiceService,
        services.BookingModuleServiceService,
        services.ParkingServiceService,
        services.ChargeServiceService,
        services.CoreLayerModuleServiceService,
        services.MessageControllerService,
        services.PatientArchiveModuleServiceService,
        services.SecuredMessageControllerService,
        services.StreetMapServiceService,
        services.TransportModuleServiceService,
        services.VehicleServiceService,
        services.CiclicalModuleServiceService,
        services.TailReturnsServiceService,
        services.StormoModuleServiceService,
        services.TurnModuleServiceService,
        {
            provide: Http,
            useClass: ExtendedHttpService,
            deps: [XHRBackend, RequestOptions]
        },
        {
            provide: BASE_PATH, useFactory: BasePathGenerator
        }],
})




export class ServicesModule {

}
export function BasePathGenerator(): String {

    let a = window.location;
    let ret = a.protocol + "//" + a.host + "";
    if (environment.serviceURL) {
        ret = a.protocol + "//" + environment.serviceURL;
    }
    /*ret += a.pathname + "/";*/
    ret += "/ordinari/"
    //ret += "/ordinari-web/"
    //ret += "api/secure/rest";
    //console.log("injecting base path: " + ret);
    return ret;

}
