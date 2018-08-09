import { Location } from '@angular/common';
import { AfterViewInit, Component, EventEmitter, OnInit, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'app/service/message.service';
import { ComponentHolderService } from 'app/service/shared-service';
import { cleanMessageList } from 'app/util/error-util';
import { BrowserCommunicationService } from 'common/services/browser-communication.service';
import { Message, NavigateMessage, RequestMessage, StartMessage, Type } from 'common/services/messages';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { ToastrService } from 'ngx-toastr';
import { BookingModuleServiceService } from '../../services/services.module';



@Component({
    moduleId: module.id.toString(),
    selector: 'syn-root',
    templateUrl: './sinottico.root.component.html',
    styleUrls: ['./sinottico.root.component.css']
})
export class SinotticoRootComponent implements OnInit, AfterViewInit {



    @BlockUI() blockUI: NgBlockUI;

    private handle;

    private maskCounter = 0;

    public collapseMenu: EventEmitter<any> = new EventEmitter();

    @ViewChild('rootModal')
    rootModal: TemplateRef<any>


    @ViewChild('rootConfirmModal')
    rootConfirmModal: TemplateRef<any>


    rootModalTitle: string;
    rootModalBody: string;
    rootModalHints: string[];

    /*@ViewChild('bookingSearch') bookingSearch: RicercaPrenotazioniTableComponent;*/
    /*@ContentChild('tabellaPrenotazioni') bookingSearch: SinotticoPrenotazioniTableComponent;*/


    constructor(
        private messageService: MessageService,
        private router: Router,
        private location: Location,
        private bookingService: BookingModuleServiceService,
        private compService: ComponentHolderService,
        public toastr: ToastrService,
        vcr: ViewContainerRef,
        private modalService: NgbModal,
        private bcs: BrowserCommunicationService
    ) {
       // this.toastr.setRootViewContainerRef(vcr);
    }

    public getRootComponent() {
        return SinotticoRootComponent;
    }

    ngAfterViewInit(): void {
        this.compService.setRootComponent(<any>this);
    }

    ngOnInit() {
        this.compService.setRootComponent(<any>this);

        this.blockUI.start();

        this.messageService.start('/user/topic/messages', true);
        this.messageService.subscribe('MessageDTO').observ$.subscribe((msg) => {
        });

        let msg: RequestMessage = {}
        this.bcs.postMessage(Type.startPopup, msg, '*');

        this.bcs.addEventListener(Type.token, (ev: Message) => {
            let msg: StartMessage = <any>ev;
            sessionStorage.setItem('currentToken', msg.token);

            let syn = msg.path;

            if (!syn) {
                syn = 'prenotazioni';
            }
            this.router.navigate([syn]);
        });

        this.bcs.addEventListener(Type.navigate, (ev: Message) => {
            let msg: NavigateMessage = <any>ev;

            this.router.navigate([msg.path]);
        });
    }

    collapse(event: boolean) {
        this.collapseMenu.emit(event);
    }

    public mask() {
        this.blockUI.start();
    }

    public unmask() {
        this.blockUI.stop();
    }


    public goBack(): void {
        this.location.back();
    }

    onSearchAvailable(event: any) {
        // console.log('onSearchAvailable: ' + event);
        // console.log('booking search el is: ' + this.bookingSearch.searchBookings());
    }

    public showConfirmDialog(title: string, message: string) {
        this.rootModalTitle = title;
        this.rootModalBody = message;
        let p = this.modalService.open(this.rootConfirmModal, {
            keyboard: false,
        }).result;
        return p;
    }

    public showModal(title: string, message: any): Promise<any> {
        this.rootModalTitle = title;
        this.rootModalHints = [];
        if (Array.isArray(message)) {
            this.rootModalHints = cleanMessageList(message);
        }
        else {
            this.rootModalBody = message;
        }

        let p = this.modalService.open(this.rootModal, {
            keyboard: false,
        }).result;

        return p;
    }

}
