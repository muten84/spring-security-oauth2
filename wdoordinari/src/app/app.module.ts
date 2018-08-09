import { registerLocaleData } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import localeIT from '@angular/common/locales/it';
import { InjectionToken, NgModule, forwardRef } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BaseRequestOptions, Http, HttpModule, RequestOptions, XHRBackend } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbDateParserFormatter, NgbDatepickerI18n, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CalendarModule } from 'angular-calendar';
import { routes } from 'app/app.routes';
import { CodaRitorniComponent } from 'app/coda-ritorni/coda-ritorni-component';
import { CodaRitorniHeaderComponent } from 'app/coda-ritorni/coda-ritorni-header-component';
import { ArchiviaServizioConfirmModal } from 'app/common-servizi/archivia-servizio.modal';
import { DuplicaMultiplaPrenotazioneComponent } from 'app/duplica/duplica-multipla-prenotazioni-component';
import { GestioneCiclicheHeaderComponent } from 'app/gestione-cicliche/gestione-cicliche-header.component';
import { IntervalloCiclicheComponent } from 'app/gestione-cicliche/intervallo-cicliche-component';
import { ModificaCiclicheComponent } from 'app/gestione-cicliche/modifica-cicliche-component';
import { AddressBookingDetailModal } from 'app/gestione-prenotazioni/address-booking-detail.modal';
import { AddressDetailComponent } from 'app/gestione-prenotazioni/address-detail-component';
import { AddressModifyComponent } from 'app/gestione-prenotazioni/address-modify-component';
import { CheckDuplicateModalComponent } from 'app/gestione-prenotazioni/checkduplicate.component';
import { ResultGridModalComponent } from 'app/gestione-prenotazioni/result-grid-modal-component';
import { RichiedenteComponent } from 'app/gestione-prenotazioni/richiedente.component';
import { ChiusuraMezzoModal } from 'app/gestione-servizi/chiusura-mezzo.modal';
import { GestioneServiziComponent } from 'app/gestione-servizi/gestione-servizi-component';
import { GestioneServiziHeaderComponent } from 'app/gestione-servizi/gestione-servizi-header.component';
import { VehicleDetailComponent } from 'app/gestione-servizi/vehicle-detail.component';
import { ClosingModalComponent } from 'app/modals/closing-modal/closing-modal-component';
import { RicercaPazienteModalComponent } from 'app/ricerca-paziente/ricerca-paziente-component';
import { SinotticoCiclicheComponent } from 'app/sinottico-cicliche/sinottico-cicliche-component';
import { SinotticoCiclicheHeaderComponent } from 'app/sinottico-cicliche/sinottico-cicliche-header.component';
import { SinotticoTurniComponent } from 'app/sinottico-turni/sinottico-turni-component';
import { SinotticoTurniHeaderComponent } from 'app/sinottico-turni/sinottico-turni-header.component';
import { GestioneEquipaggioMezzoComponent } from 'app/gestione-equipaggio-mezzo/gestione-equipaggio-mezzo-component';
import { GestioneEquipaggioMezzoHeaderComponent } from 'app/gestione-equipaggio-mezzo/gestione-equipaggio-mezzo-header.component';
import { BrowserCommunicationService } from 'common/services/browser-communication.service';
import { BlockUIModule } from 'ng-block-ui';
import { DynamicModule } from 'ng-dynamic-component';
import { PaginationModule } from 'ngx-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import { BasePathGenerator, ServicesModule } from '../services/services.module';
import { AppComponent } from './app.component';
import { routing } from './app.routes';
import { BlankComponent } from './blank/blank.component';
import { CommonHeaderComponent, ROUTES } from './common-header/common-header.component';
import { ServiceCommon } from './common-servizi/service-common';
/*import { TopNavModule } from './top-nav/top-nav.module';*/
import { BreadcrumbComponent } from './core/breadcrumb/breadcrumb.component';
import { CommonBannerComponent, CoreModule, LoginModule } from './core/core.module'; /*modulo core di componenti*/
import { ClickOffDirective } from './core/directives/click-off.directive';
import { AuthInterceptor } from './core/http/auth-interceptor';
import { ErrorInterceptor } from './core/http/error-interceptor';
import { ValidationModule } from './core/validation/validation.module';
import { ModificaPrenotazioneHeaderComponent } from './gestione-prenotazioni/modifica-prenotazione-header.component';
//import { GestionePrenotazioniComponent } from './gestione-prenotazioni/gestione-prenotazioni.component';
import { ModificaPrenotazioneComponent } from './gestione-prenotazioni/modifica-prenotazione.component';
import { MezziAttiviHeaderComponent } from './mezzi-attivi/mezzi-attivi-header.component';
import { MezziAttiviComponent } from './mezzi-attivi/mezzi-attivi.component';
import { CrewMemberModalComponent } from './modals/crew-member-modal/crew-member-modal-component';
import { MobileUnitsModalComponent } from './modals/mobile-units-modal/mobile-units-modal-component';
import { TextModalComponent } from './modals/text-modal/text-modal-component';
import { TurnModalComponent } from './modals/turn-modal/turn-modal-component';
import { TurnNoteModalComponent } from './modals/turn-modal/turn-note-modal-component';
import { CrewMembersModalComponent } from './modals/turn-modal/crew-members-modal-component';
import { RicercaAvanzataPrenotazioniHeaderComponent } from './ricerca-avanzata-prenotazioni/ricerca-avanzata-prenotazioni-header.component';
import { RicercaAvanzataPrenotazioniTableComponent } from './ricerca-avanzata-prenotazioni/ricerca-avanzata-prenotazioni-table.component';
import { RicercaAvanzataPrenotazioniComponent } from './ricerca-avanzata-prenotazioni/ricerca-avanzata-prenotazioni.component';
/*import { SinotticoPrenotazioniTableComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-table.component';*/
import { RicercaServiziHeaderComponent } from './ricerca-servizi/ricerca-servizi-header.component';
import { RicercaServiziComponent } from './ricerca-servizi/ricerca-servizi.component';
import { RootComponent } from './root/root.component';
/*import per fake auth*/
import { DummyAuthGuard } from './service/DummyAuthGuard';
import { BackendConfigService } from './service/config.service';
import { ExtendedHttpService } from './service/extended-http.service';
import { MessageService } from './service/message.service';
import { ServiziService } from './service/servizi-service';
import { ComponentHolderService, EventService } from './service/shared-service';
import { TokenService } from './service/token.service';
import { BookingHistoryModalComponent } from './sinottico-prenotazioni/bookinghistory.component';
import { SinotticoPrenotazioniHeaderComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-header.component';
import { SinotticoPrenotazioniComponent } from './sinottico-prenotazioni/sinottico-prenotazioni.component';
import { SinotticoServiziHeaderComponent } from './sinottico-servizi/sinottico-servizi-header.component';
import { SinotticoServiziComponent } from './sinottico-servizi/sinottico-servizi.component';
import { StaticDataService } from './static-data/cached-static-data';
import { StaticDataResolve } from './static-data/cached-static-data-resolver';
import { NgbDateCustomParserFormatter } from './util/date-formatter';
import { CustomDatepickerI18n, I18n } from './util/date-language';
import { UppercaseDirective } from './util/uppercase.directive';
import { DynamicDisable } from './util/validator-dynamic-disable.directive';
import { LocalDataService } from './service/local-data-service';
import { GestioneTurnoComponent } from 'app/gestione-turno/gestione-turno-component';
import { GestioneTurnoHeaderComponent } from 'app/gestione-turno/gestione-turno-header.component';
import { FullTextComponent } from './gestione-prenotazioni/fulltext-component';
import { CanDeactivateBookingModify } from './gestione-prenotazioni/deactivate-BookingModify';


export const GLOBAL_BASE_PATH = new InjectionToken<string>('globalBasePath');

@NgModule({
    declarations: [
        /*LoginComponent,*/
        AppComponent,
        RootComponent,
        BreadcrumbComponent,
        //GestionePrenotazioniComponent,
        SinotticoPrenotazioniComponent,
        SinotticoServiziHeaderComponent,
        RicercaAvanzataPrenotazioniComponent,
        RicercaServiziComponent,
        UppercaseDirective, DynamicDisable,
        CommonHeaderComponent,
        SinotticoPrenotazioniHeaderComponent,
        RicercaAvanzataPrenotazioniHeaderComponent,
        RicercaServiziHeaderComponent,
        RicercaAvanzataPrenotazioniTableComponent,
        ModificaPrenotazioneHeaderComponent,
        ModificaPrenotazioneComponent,
        AddressModifyComponent,
        ResultGridModalComponent,
        CheckDuplicateModalComponent,
        AddressDetailComponent,
        RichiedenteComponent,
        SinotticoServiziComponent,
        BookingHistoryModalComponent,
        ClosingModalComponent,
        DuplicaMultiplaPrenotazioneComponent,
        GestioneServiziComponent,
        GestioneServiziHeaderComponent,
        SinotticoCiclicheComponent,
        SinotticoCiclicheHeaderComponent,
        GestioneCiclicheHeaderComponent,
        ChiusuraMezzoModal,
        VehicleDetailComponent,
        ArchiviaServizioConfirmModal,
        AddressBookingDetailModal,
        CodaRitorniComponent,
        CodaRitorniHeaderComponent,
        IntervalloCiclicheComponent,
        ModificaCiclicheComponent,
        RicercaPazienteModalComponent,
        BlankComponent,
        TextModalComponent,
        CrewMemberModalComponent,
        TurnModalComponent,
        TurnNoteModalComponent,
        SinotticoTurniComponent,
        SinotticoTurniHeaderComponent,
        MobileUnitsModalComponent,
        ClickOffDirective,
        MezziAttiviComponent,
        MezziAttiviHeaderComponent,
        GestioneEquipaggioMezzoComponent,
        GestioneEquipaggioMezzoHeaderComponent,
        CrewMembersModalComponent,
        GestioneTurnoComponent,
        GestioneTurnoHeaderComponent,
        FullTextComponent
    ],
    imports: [
        LoginModule,
        BlockUIModule.forRoot(),

        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        NgbModule.forRoot(),
        ToastrModule.forRoot(), // ToastrModule added
        PaginationModule.forRoot(),
        CoreModule,
        routing,
        CalendarModule.forRoot(),
        HttpClientModule,
        /*TopNavModule,*/
        DynamicModule.withComponents([
            SinotticoServiziHeaderComponent,
            SinotticoPrenotazioniHeaderComponent,
            RicercaAvanzataPrenotazioniHeaderComponent,
            RicercaServiziHeaderComponent,
            CodaRitorniHeaderComponent]),

        /*DataTableModule*/
        ValidationModule.forRoot(),
        ServicesModule,

    ],
    providers: [
        // providers used to create fake backend
        DummyAuthGuard,
        /*fakeBackendProvider,*/
        /*MockBackend,*/
        BaseRequestOptions,
        /*fine fake login*/
        ComponentHolderService,
        EventService,
        BrowserCommunicationService,
        BackendConfigService,
        ServiziService,
        LocalDataService,
        {
            provide: GLOBAL_BASE_PATH, useFactory: BasePathGenerator
        },
        {
            provide: ROUTES, useFactory: getRoutes
        },
        StaticDataService,
        StaticDataResolve,
        {
            provide: NgbDateParserFormatter,
            useClass: forwardRef(() => NgbDateCustomParserFormatter),

        },
        I18n,
        {
            provide: NgbDatepickerI18n, useClass: forwardRef(() => CustomDatepickerI18n)
        },
        {
            provide: Http,
            useClass: ExtendedHttpService,
            deps: [ComponentHolderService, XHRBackend, RequestOptions]
        },
        /*    {
                provide: Http,
                useFactory: (xhrBackend: XHRBackend, requestOptions: RequestOptions, router: Router) => new ExtendedHttpService(xhrBackend, requestOptions, router),
                deps: [XHRBackend, RequestOptions, Router]
            },*/
        MessageService,
        TokenService,
        ServiceCommon,
        CanDeactivateBookingModify,

        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
        /*StompService,
        {
          provide: StompConfig,
          useValue: stompConfig
        }*/
    ],
    entryComponents: [CommonBannerComponent, ModificaPrenotazioneHeaderComponent,
        SinotticoPrenotazioniHeaderComponent, RicercaAvanzataPrenotazioniHeaderComponent,
        RicercaServiziHeaderComponent, ResultGridModalComponent, RicercaPazienteModalComponent,
        CheckDuplicateModalComponent, BookingHistoryModalComponent,
        ClosingModalComponent, GestioneServiziHeaderComponent, ChiusuraMezzoModal,
        VehicleDetailComponent, ArchiviaServizioConfirmModal, AddressBookingDetailModal,
        TextModalComponent, CrewMemberModalComponent, TurnModalComponent, TurnNoteModalComponent, MobileUnitsModalComponent,CrewMembersModalComponent],
    bootstrap: [AppComponent]
})
export class AppModule { }

export function getRoutes() {
    return routes;
}
//registro la lingua italiana nel sistema
registerLocaleData(localeIT);