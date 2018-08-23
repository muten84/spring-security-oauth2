import { MezziAttiviComponent } from "app/mezzi-attivi/mezzi-attivi.component";
import { Component, ViewEncapsulation } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
 
import { Router } from "@angular/router";
import { MessageService } from "app/service/message.service";
import { ComponentHolderService } from "app/service/shared-service";
import { TransportModuleServiceService, MenuItemValue, WorkingResourceInputDTO, WorkingResourceDTO, WorkingResourceCompactDTO } from "services/gen";
import { BrowserCommunicationService } from "common/services/browser-communication.service";
import { Message, SelectBookingInSinottico, Type, AddBookingToServiceMessage, NavigateMessage } from "common/services/messages";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'mezzi-attivi-ex',
    templateUrl: '../../app/mezzi-attivi/mezzi-attivi.html',
    styleUrls: ['../../app/mezzi-attivi/mezzi-attivi.scss'],
    encapsulation: ViewEncapsulation.None

})
export class MezziAttiviExComponent extends MezziAttiviComponent {



    currentBookingCode: string;
    currentBookingId: string;
    lsSetCurr: (ev: Message) => void;
    lsRemBook: (ev: Message) => void;

    constructor(sanitizer: DomSanitizer, 
        router: Router,
        messageService: MessageService,
        componentService: ComponentHolderService,
        transportService: TransportModuleServiceService,
        bcs: BrowserCommunicationService,
        modalService: NgbModal,
        private componentService_ex: ComponentHolderService) {

        super(sanitizer, 
            router,
            messageService,
            componentService,
            transportService,
            bcs,
            modalService);
    }

    ngOnInit() {
        super.ngOnInit();
        
        this.synopticModule = true;

        this.lsSetCurr = (ev: Message) => {
            let data: SelectBookingInSinottico = <any>ev;
            this.currentBookingCode = data.bookingCode;
            this.currentBookingId = data.bookingId;

            if (this.rows && this.rows.length > 0) {
                this.rows.forEach(e => {
                    e.optionItems = this.buildRowMenu(e.popupMenu);
                });
            }

        }

        this.lsRemBook = (ev: Message) => {
            this.componentService_ex.getRootComponent().unmask();
        };

        this.bcs.addEventListener(Type.APP2_SET_CURRENT_BOOKING, this.lsSetCurr);
        this.bcs.addEventListener(Type.APP2_REMOVE_BOOKING_AND_UNMASK, this.lsRemBook);

    }

    ngOnDestroy() {
        super.ngOnDestroy();

        this.bcs.removeEventListener(Type.APP2_SET_CURRENT_BOOKING, this.lsSetCurr);
    }



    buildRowMenu(menuItems: MenuItemValue[]) {
        menuItems = [];
        if(this.currentBookingCode!=undefined)
        {
            menuItems.push(
                {
                    name: 'Accorpa Prenotazione' + this.currentBookingCode ,
                    id: 'add_booking_to_service',
                    position: 0,
                    enable: true
                }
            );
        }
        menuItems.push({
            name: 'Vai al servizio associato',
            id: 'go_to_service',
            position: 1,
            enable:true
        });
        return super.buildRowMenu(menuItems);
    }

    public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any {
        if (enabled) {
            let src = source.toLowerCase();

            let selected = <WorkingResourceCompactDTO>this.currentSelected;
            let serviceId = selected.nextStop ? selected.nextStop.serviceId : selected.lastStop.serviceId;


            switch (src) {
                case 'add_booking_to_service':
                    this.componentService_ex.getRootComponent().mask();


                    let param: AddBookingToServiceMessage = {
                        bookingId: this.currentBookingId,
                        serviceId: serviceId,
                    }
                    this.bcs.postMessage(Type.ADD_BOOKING_TO_SERVICE, param, "*");
                    this.componentService_ex.getRootComponent().unmask();
                    return;
                case 'go_to_service':
                    let msg: NavigateMessage = {
                        path: 'gestione-servizi/' + serviceId
                    };
                    this.bcs.postMessage(Type.navigate, msg);
                    return;
            }
        }

    }
}