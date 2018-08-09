import { registerLocaleData } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import localeIT from '@angular/common/locales/it';
import { InjectionToken, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Http, HttpModule, RequestOptions, XHRBackend } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonHeaderComponent, ROUTES } from 'app/common-header/common-header.component';
import { ServiceCommon } from 'app/common-servizi/service-common';
import { BreadcrumbComponent } from 'app/core/breadcrumb/breadcrumb.component';
import { CoreModule } from 'app/core/core.module';
import { AuthInterceptor } from 'app/core/http/auth-interceptor';
import { ErrorInterceptor } from 'app/core/http/error-interceptor';
import { MezziAttiviHeaderComponent } from 'app/mezzi-attivi/mezzi-attivi-header.component';
import { BackendConfigService } from 'app/service/config.service';
import { ExtendedHttpService } from 'app/service/extended-http.service';
import { MessageService } from 'app/service/message.service';
import { ServiziService } from 'app/service/servizi-service';
import { ComponentHolderService, EventService } from 'app/service/shared-service';
import { StaticDataService } from 'app/static-data/cached-static-data';
import { StaticDataResolve } from 'app/static-data/cached-static-data-resolver';
import { BrowserCommunicationService } from 'common/services/browser-communication.service';
import { BlockUIModule } from 'ng-block-ui';
import { DynamicModule } from 'ng-dynamic-component';
import { ToastrModule } from 'ngx-toastr';
import { BasePathGenerator, ServicesModule } from 'services/services.module';
import { routes, routing } from 'sinottico/app.routes';
import { AppComponent } from './app.component';
import { MezziAttiviExComponent } from './mezzi-attivi/mezzi-attivi-ex.component';
import { SinotticoRootComponent } from './root/sinottico.root.component';
import { SinotticoPrenotazioniExComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-ex-component';
import { SinotticoPrenotazioniHeaderExComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-header-ex-component';
import { SinotticoServiziExComponent } from './sinottico-servizi/sinottico-servizi-ex-component';
import { SinotticoServiziHeaderExComponent } from './sinottico-servizi/sinottico-servizi-header-ex-component';

export const GLOBAL_BASE_PATH = new InjectionToken<string>('globalBasePath');

@NgModule({
  declarations: [
    AppComponent,
    SinotticoServiziExComponent,
    SinotticoServiziHeaderExComponent,
    SinotticoPrenotazioniExComponent,
    SinotticoPrenotazioniHeaderExComponent,
    MezziAttiviExComponent,
    MezziAttiviHeaderComponent,
    SinotticoRootComponent,
    CommonHeaderComponent,
    BreadcrumbComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    HttpClientModule,
    ToastrModule.forRoot(), // ToastrModule added
    NgbModule.forRoot(),
    CoreModule,
    ServicesModule,
    routing,
    BlockUIModule.forRoot(),
    FormsModule,
    DynamicModule.withComponents([
      SinotticoServiziHeaderExComponent,
      SinotticoPrenotazioniHeaderExComponent,
      MezziAttiviHeaderComponent,
    ]),
  ],
  providers: [
    BrowserCommunicationService,
    StaticDataService,
    StaticDataResolve,
    ComponentHolderService,
    MessageService,
    BackendConfigService,
    EventService,
    ServiceCommon,
    ServiziService,
    {
      provide: GLOBAL_BASE_PATH, useFactory: BasePathGenerator
    }, {
      provide: ROUTES, useFactory: getRoutes
    },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    {
      provide: Http,
      useClass: ExtendedHttpService,
      deps: [ComponentHolderService, XHRBackend, RequestOptions]
    },
  ],
  entryComponents: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function getRoutes() {
  return routes;
}

//registro la lingua italiana nel sistema
registerLocaleData(localeIT);