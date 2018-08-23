import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { Configuration } from './configuration';
import { HttpClient } from '@angular/common/http';


import { AAAControllerService } from './api/aAAController.service';
import { AuthorityModuleServiceService } from './api/authorityModuleService.service';
import { BookingModuleServiceService } from './api/bookingModuleService.service';
import { ChargeServiceService } from './api/chargeService.service';
import { CiclicalModuleServiceService } from './api/ciclicalModuleService.service';
import { CoreLayerModuleServiceService } from './api/coreLayerModuleService.service';
import { DefaultService } from './api/default.service';
import { MessageControllerService } from './api/messageController.service';
import { ParkingServiceService } from './api/parkingService.service';
import { PatientArchiveModuleServiceService } from './api/patientArchiveModuleService.service';
import { SecuredMessageControllerService } from './api/securedMessageController.service';
import { StormoModuleServiceService } from './api/stormoModuleService.service';
import { StreetMapServiceService } from './api/streetMapService.service';
import { TailReturnsServiceService } from './api/tailReturnsService.service';
import { TransportModuleServiceService } from './api/transportModuleService.service';
import { TurnModuleServiceService } from './api/turnModuleService.service';
import { VehicleServiceService } from './api/vehicleService.service';

@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers: [
    AAAControllerService,
    AuthorityModuleServiceService,
    BookingModuleServiceService,
    ChargeServiceService,
    CiclicalModuleServiceService,
    CoreLayerModuleServiceService,
    DefaultService,
    MessageControllerService,
    ParkingServiceService,
    PatientArchiveModuleServiceService,
    SecuredMessageControllerService,
    StormoModuleServiceService,
    StreetMapServiceService,
    TailReturnsServiceService,
    TransportModuleServiceService,
    TurnModuleServiceService,
    VehicleServiceService ]
})
export class ApiModule {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders {
        return {
            ngModule: ApiModule,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModule,
                 @Optional() http: HttpClient) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }
    }
}
