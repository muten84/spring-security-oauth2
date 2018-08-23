import { Component } from "@angular/core";
import { OnDestroy, OnInit } from "@angular/core/src/metadata/lifecycle_hooks";
import { DomSanitizer } from "@angular/platform-browser";
import { Router, ActivatedRoute } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { MessageService } from "app/service/message.service";
import { ComponentHolderService } from "app/service/shared-service";
import { SinotticoPrenotazioniComponent } from "app/sinottico-prenotazioni/sinottico-prenotazioni.component";
import { StaticDataService } from "app/static-data/cached-static-data";
import { BrowserCommunicationService } from "common/services/browser-communication.service";
import { AddBookingToServiceMessage, Message, NavigateMessage, SelectServiceInSinottico, Type } from "common/services/messages";
import { BookingModuleServiceService, ChargeServiceService, TransportModuleServiceService } from "services/gen/api/api";
import { MenuItemValue, OverviewBookingDTO } from "services/services.module";
import { EVENTS } from "app/util/event-type";


@Component({
    selector: 'sinottico-prenotazioni',
    templateUrl: '../../app/sinottico-prenotazioni/sinottico-prenotazioni.html',
    styleUrls: ['../../app/sinottico-prenotazioni/sinottico-prenotazioni.scss']

})
export class SinotticoPrenotazioniExComponent extends SinotticoPrenotazioniComponent implements OnInit, OnDestroy {

    currentServiceCode: string;
    currentServiceId: string;
    vehicleService: string;
    lsSetCurr: (ev: Message) => void;
    lsRemBook: (ev: Message) => void;

    // sottoscrizione agli eventi pushing
    private serviceSubscription;

    constructor(sdSvc: StaticDataService,
        modalService: NgbModal,
        chargeService: ChargeServiceService,
        sanitizer: DomSanitizer,

        route: ActivatedRoute,
        router: Router,
        messageService: MessageService,
        componentService: ComponentHolderService,
        private componentService_ex: ComponentHolderService,
        transportService: TransportModuleServiceService,
        bookingService: BookingModuleServiceService,
        bcs: BrowserCommunicationService) {

        super(sdSvc,
            modalService,
            chargeService,
            sanitizer,
            route,
            router,
            messageService,
            componentService,
            transportService,
            bookingService,
            bcs);
    }

    ngOnInit() {
        super.ngOnInit();
        this.synopticModule = true;
        this.lsSetCurr = async (ev: Message) => {
            let data: SelectServiceInSinottico = <any>ev;
            this.currentServiceCode = data.serviceCode;
            this.currentServiceId = data.serviceId;

            if (this.currentServiceId){
                let result = await this.recoveryActiveServiceResource(this.currentServiceId);
                if (result && result.length>0) {
                    let mezzi = result.map(o => o.resourceCode).join(', ');
                    this.currentServiceCode = this.currentServiceCode + ' (Mezzi associati: ' + mezzi + ')';
                    this.vehicleService = ' (Mezzi associati: ' + mezzi + ')';
                }
            }
                

            if (this.rows && this.rows.length > 0) {
                this.rows.forEach(e => {
                    e.optionItems = this.buildRowMenu(e.popupMenu, e.source);
                });
            }
        }

        this.lsRemBook = (ev: Message) => {
            let data: AddBookingToServiceMessage = <any>ev;

            if (data && data.bookingId) {
                let list: OverviewBookingDTO[] = [];
                for (let idx = 0; idx < this.rows.length; idx++) {
                    let row: OverviewBookingDTO = this.rows[idx];
                    if (row.id !== data.bookingId) {
                        list.push(row);
                    }
                }
                this.rows = list;
            }
            this.componentService_ex.getRootComponent().unmask();
        };

        this.bcs.addEventListener(Type.APP2_SET_CURRENT_SERVICE, this.lsSetCurr);
        this.bcs.addEventListener(Type.APP2_REMOVE_BOOKING_AND_UNMASK, this.lsRemBook);

        this.bcs.postMessage(Type.APP2_GET_SELECTED_BOOKING, {}, "*");

         //mi sottoscrivo a service per capire se cambia qualcosa mentre ho intenzione di associare una prenotazione
         this.serviceSubscription = this.messageService.subscribe(EVENTS.SERVICE);
         this.serviceSubscription.observ$.subscribe((msg) => {
             console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
             if (msg.data.action && msg.data.action != "ALERT") {
                 //disaccoppia contesto subscribe da gestione evento 
                 //- in caso di errore lascia sottoscrizione e individua riga errore
                 setTimeout(() => {
                     this.evaluatePushingEvent(msg);
                 }, 0);//Chiudi timeout
             }
         });


    }

    private evaluatePushingEvent(msg) {
        if (msg.data.action && msg.data.action != "ALERT") {
            let ids: string[] = msg.data.payload.split(",");

            if (ids && ids.length > 0 && this.currentServiceId ) { //&& msg.data.from != this.currentUser
                for (var i = 0; i < ids.length; i++) {
                    if (ids[i] === this.currentServiceId) {
                        if (msg.data.action == "ARCHIVE" || msg.data.action == "DELETE") {
                            this.componentService.getRootComponent().showModal('Attenzione!', "Il servizio è stato rimosso dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                            this.bcs.addEventListener(Type.APP2_REMOVE_BOOKING_AND_UNMASK, this.lsRemBook);
                        } else {
                            this.searchBookings();
                        }                        
                    }
                }
            }
        }
    }

    async recoveryActiveServiceResource(serviceId) : Promise<any>{
        return await this.transportService.getAllocatedServiceResources(serviceId).toPromise();        
    }

    ngOnDestroy() {
        super.ngOnDestroy();
        this.serviceSubscription.observ$.unsubscribe();
        this.bcs.removeEventListener(Type.APP2_SET_CURRENT_SERVICE, this.lsSetCurr);
        this.bcs.removeEventListener(Type.APP2_REMOVE_BOOKING_AND_UNMASK, this.lsRemBook);
    }

    buildRowMenu(menuItems: MenuItemValue[], source) {
        menuItems = [{
            name: 'Aggiungi al servizio ' + (this.currentServiceCode ? this.currentServiceCode : ""),
            id: 'add_booking_to_service',
            position: 0,
            enable: this.currentServiceId ? true : false
        }, {
            name: 'Vai alla Prenotazione',
            id: 'go_to_booking',
            position: 1,
            enable: true
        }];
        return super.buildRowMenu(menuItems, source, null);
    }

    createDefaultMenu() {
        let optionItems = [];
        return optionItems;
    }

    public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any {
        if (enabled) {
            let src = source.toLowerCase();
            switch (src) {
                case 'add_booking_to_service':

                    this.componentService_ex.getRootComponent().mask();

                    let param: AddBookingToServiceMessage = {
                        bookingId: (<OverviewBookingDTO>this.currentSelected).id,
                        serviceId: this.currentServiceId
                    }

                    this.bcs.postMessage(Type.ADD_BOOKING_TO_SERVICE, param, "*");
                    this.componentService_ex.getRootComponent().unmask();
                    return;
                case 'go_to_booking':
                    let archiveBooking = 1;
                    if (row.source && row.source == 'S') archiveBooking = row.source;
                    let msg: NavigateMessage = {
                        path: 'modifica-prenotazione/' + archiveBooking + '/' + (<OverviewBookingDTO>this.currentSelected).id,
                    };
                    this.bcs.postMessage(Type.navigate, msg);
                    return;
            }
        }

    }

    triggerSubmit(filterInput: any) {
        filterInput.sinottico = true;
        super.triggerSubmit(filterInput);
    }


}