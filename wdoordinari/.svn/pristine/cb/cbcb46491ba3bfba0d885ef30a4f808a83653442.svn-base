import { AfterViewInit, Component, OnInit, ViewChild, ViewEncapsulation } from "@angular/core";
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ServiceCommon } from "app/common-servizi/service-common";
import { CoreTableColumn } from "app/core/table/core-table/core-table.component";
import { ClosingModalComponent } from "app/modals/closing-modal/closing-modal-component";
import { ComponentHolderService, MiddleComponent } from "app/service/shared-service";
import { StaticDataService } from "app/static-data/cached-static-data";
import { convertMenuItem, decodePhaseForDate, getBookingImage ,  getBookingTooltipImage} from 'app/util/sinottico-utils';
import { BrowserCommunicationService } from 'common/services/browser-communication.service';
import { SelectBookingInSinottico, Type } from 'common/services/messages';
import * as _ from 'lodash';
import { moment } from 'ngx-bootstrap/chronos/test/chain';
import { CiclicalBookingForVirtualServiceDTO } from 'services/gen/model/models';
import { BookingModuleServiceService, MenuItemValue, OverviewTailReturnFilterDTO, OverviewTailReturnsDTO, SearchBookingResultDTO, TailReturnsServiceService, TransportModuleServiceService } from 'services/services.module';
import { MessageService, Subscription } from '../service/message.service';
import { BookingHistoryModalComponent } from '../sinottico-prenotazioni/bookinghistory.component';
import { EVENTS } from '../util/event-type';

function execute(context: any, funct: { (obj: any): any }): { (obj: any): any } {
    return function (obj: any) {
        return funct.call(context, obj);
    }
}

@Component({
    selector: 'coda-ritorni',
    templateUrl: './coda-ritorni-component.html',
    styleUrls: ['./coda-ritorni-component.scss'],
    encapsulation: ViewEncapsulation.None,
})

export class CodaRitorniComponent implements OnInit, MiddleComponent, AfterViewInit {

    bookingSubscription: Subscription;

    public columnsCodaRitorniTable: Array<CoreTableColumn> =
        [
            { title: 'Data', name: 'transportDateDescr', index: 3, flex: 12 ,style:{'max-width' : '150px'}},
            { title: 'Codice', name: 'code', index: 4, flex: 5 },
            { title: 'Tipo', name: 'transportType', index: 5, flex: 5 },
            { title: 'Deamb', name: 'patientStatusDescr', index: 6, flex: 5 },
            { title: 'Convenzione', name: 'convention', index: 7, flex: 5 },
            { title: 'Indirizzo partenza', name: 'startAddress', index: 8, flex: 15 },
            { title: 'Indirizzo destinazione', name: 'endAddress', index: 9, flex: 15 },
            { title: 'Trasportato', name: 'denominazionePaziente', index: 10, flex: 7 },
            /*{ title: 'Postazione', name: 'postazione',  flex : 7 },*/
            {
                title: '', name: '', index: 11, optionTitle: 'code', options: [
                    {
                        name: 'SEPARATOR',
                        icon: ' '
                    }, {
                        name: 'Trasferisci nella coda prenotazioni ',
                        icon: 'mdi mdi-forward',
                        clicked: execute(this, this.moveToBooking)
                    },
                    {
                        name: 'Dati della Prenotazione',
                        icon: 'mdi mdi-information-outline', /*mdi mdi-lead-pencil',*/
                        clicked: execute(this, this.goToBookingDetail)
                    },
                    {
                        name: 'Elimina il ritorno',
                        icon: 'mdi mdi-table-column-remove',
                        clicked: execute(this, this.removeReturn)
                    },
                    {
                        name: 'SEPARATOR',
                        icon: ' '
                    },
                    {
                        name: 'Crea nuovo servizio',
                        icon: 'fa fa-tasks', /*mdi mdi-lead-pencil',*/
                        clicked: execute(this, this.makeService)
                    },
                    {
                        name: 'SEPARATOR',
                        icon: ' '
                    },
                    {
                        name: 'Mostra lo storico delle modifiche',
                        icon: 'fa fa-list-alt', /*mdi mdi-lead-pencil',*/
                        clicked: execute(this, this.showBookingHistory),
                        enabled: function (row: any): boolean {
                            let enableHistory = false;
                            if (row.popupMenu) {
                                let propertiesFilter = row.popupMenu.filter(i => i.id == 'SHOW_HISTORY');
                                if (propertiesFilter.length > 0 && propertiesFilter[0].enable) {
                                    enableHistory = true;
                                }
                            }
                            return enableHistory;//row.popupMenu && row.popupMenu.filter(i =>i.id='SHOW_HISTORY')[0].enable;
                        }
                    }

                ], flex: 0, style: { "flex-basis": "30px" }
            }
        ];

    selected = [];

    currentId;

    // combinazione di tipi
    currentSelected: OverviewTailReturnsDTO & {
        optionItems?: Array<any>;
        charge?: string;
    } = {
            optionItems: [],
            charge: ""
        }

    public filterSet: String;
    public filter: OverviewTailReturnFilterDTO;
    public filterToSave: OverviewTailReturnFilterDTO = {};

    /* public getOptions(): CoreTableOption[]{
         let options= //this.buildRowMenu('optionItems');      
         [
             {
                 name: 'Trasferisci nella coda prenotazioni ',
                 icon: 'mdi mdi-forward',
                 clicked: execute(this, this.moveToBooking)
             }
         ];
         return options;
     }*/

    public configCodaRitorniTable = {
        paging: true,
        sorting: { columns: this.columnsCodaRitorniTable },
        filtering: { filterString: '' },
    };

    public listCodaRitorni: Array<OverviewTailReturnsDTO> = [];
   


    constructor(
        private componentService: ComponentHolderService,
        private route: ActivatedRoute,
        private sdSvc: StaticDataService,
        private staticService: StaticDataService,
        private router: Router,
        private serviceCommon: ServiceCommon,
        private codaRitorniService: TailReturnsServiceService,
        private sanitizer: DomSanitizer,
         private bookingService: BookingModuleServiceService,
        private messageService: MessageService,
        protected bcs: BrowserCommunicationService,
        private modalService: NgbModal,
        private transportService: TransportModuleServiceService,
    ) { }

    public ngOnInit() {
        this.loadCodaRitorni(null);
        this.bookingSubscription = this.messageService.subscribe(EVENTS.BOOKING);
        this.bookingSubscription.observ$.subscribe((msg) => {
            console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
            this.elaboratePushMessage(msg);
        });
    }

    ngAfterViewInit() {
        this.componentService.setMiddleComponent('currentSearchTable', this);
    }

    ngOnDestroy() {
        //console.log('ngOnDestroy');
        this.bookingSubscription.observ$.unsubscribe();
        this.sendMessageToSinottico({});
        ////console.log("sinottico destroyed")
    }

    onSelect(selected: OverviewTailReturnsDTO) {
        //console.log('onSelect');
        // //console.log('Select Event', selected, this.selected);
        this.currentSelected = selected;
        /*Luigi: dichiaro tutto con i tipi così sono sicuro che quello che passo in input
        e' coerente con il backend*/
        let id = this.currentSelected.id;

        this.sendMessageToSinottico(this.currentSelected);
        if (this.currentId == id) {
            return;
        }
        this.currentId = id;
    }


    deleteReturn(row) {
        let modal = this.modalService.open(ClosingModalComponent, { size: 'sm' });
        modal.componentInstance.id = row.id;
        modal.result.then((result) => {
            ////console.log("modal result is: " + result);
            if (result) {
                let idServices: Array<string> = [row.id];// se l'utente ha chiuso la prenotazione, ricarico il sinottico
                this.removeBooking(idServices);
            }
        }, (reason) => {
            console.log("Non accetta la cancellazione del ritorno");
        });
        return;
    }

    makeService(row) {
        let model = {
            bookingId: row.id,
            ciclicalId: "",
            transportDate: (<any>row.transportDate),
        };
        this.transportService.getCiclicalBookingForVirtualService(model).subscribe(res => {
            if (res && res.length > 0) {
                console.log("Devi creare modale");
                //TODO
                // far apparire una popup per selezionare le cicliche da utilizzare nella creazione del servizio virtuale
            } else {
                this.createService(row);
            }
        });
    }


    createService(row: OverviewTailReturnsDTO, cicliche?: [CiclicalBookingForVirtualServiceDTO]) {

        let listId;

        if (cicliche) {
            listId = cicliche.map(el => {
                return el.id;
            });
        } else {
            listId = [];
        }

        listId.push(row.id);

        let model = {
            bookingIdList: listId,
            bookingFlag: false,
            closedStatus: false,
        };
        this.componentService.getRootComponent().mask();
        this.transportService.createService(model).subscribe(res => {
            // this.router.navigate(['/sinottico-servizi']);
            // ricarico la lista dei bookings
            this.loadCodaRitorni(null);
            this.componentService.getRootComponent().showToastMessage('success', "Il servizo '" + res.result.code + "' è stato creato!");
            this.componentService.getRootComponent().unmask();
        });
    }

    showBookingHistory(res) {
        //console.log('showBookingHistory');
        if (res && res.id) {
            this.componentService.getRootComponent().mask();
            this.bookingService.getBookingHistoryById(res.id).subscribe(res => {
                //console.log("history result: " + JSON.stringify(res));
                this.componentService.getRootComponent().unmask();
                let modal = this.modalService.open(BookingHistoryModalComponent, { size: 'lg' });

                modal.componentInstance.list = res;
                modal.componentInstance.title = 'Storico modifiche prenotazione';
                modal.componentInstance.columns = [
                    { title: 'Data', name: 'modificationDate' },
                    { title: 'Operatore', name: 'operator' },
                    { title: 'Modifica', name: 'modificationSynthesis' },
                ];
                return modal;
            })
        } else {
            this.componentService.getRootComponent().showToastMessage('warning', "L'elemento selezionato ha identificativo non valido!");
        }
    }


    sendMessageToSinottico(currentBooking: OverviewTailReturnsDTO) {
        // invio l'id al sinottico staccato se è aperto
        let param: SelectBookingInSinottico = {
            bookingCode: currentBooking.code,
            bookingId: currentBooking.id
        }
        this.bcs.postMessage(Type.APP2_SET_CURRENT_BOOKING, param, "*");
    }

    private elaboratePushMessage(msg) {
        if (msg.data.payload && msg.data.action) {
            let idServices: Array<string> = null;
            //lista di id dei servizi da allarmare
            idServices = msg.data.payload.split(",");
            switch (msg.data.action) {
                case "SWITCH_TO_BOOKING":
                    //remove dalla coda ritorno nel caso di passaggio a booking
                    this.removeBooking(idServices);
                    break;
                case "ARCHIVE":
                    //se è un archiviazione lo tolgo
                    this.removeBooking(idServices);
                    break;
                case "UPDATE":
                // questo evento arriva sia su un update che su una creazione, 
                // faccio le stesse cose sull'alert
                case "ALERT":
                    // in caso di update o alert aggiorno i singoli servizi
                    this.reloadBookings(idServices);
                    break;
                case "SWITCH_TO_RETURN":
                    // in caso di update o alert aggiorno i singoli servizi
                    this.reloadBookings(idServices);
                    break;
                default:
                    this.loadCodaRitorni(null);
                    break;
            }
        }
    }

    private loadCodaRitorni(filter: OverviewTailReturnFilterDTO) {
        if (!filter) {
            filter = {};
        }
        this.componentService.getRootComponent().mask();
        this.codaRitorniService.searchTailReturnsByFilter(filter).catch((err, c) => {
            this.componentService.getRootComponent().unmask();
            return err;
        }).subscribe((result) => {
            let res: SearchBookingResultDTO = result;
            if (res && res.resultList) {
                let resultList: Array<OverviewTailReturnsDTO> = res.resultList;
                resultList.forEach(element => {
                    this.generateTableElement(element);
                    /*(<any>element).transportDateDescr = decodePhaseForDate(element.phase, element.transportDate.toString());
                    (<any>element).patientStatusDescr = 
                    (element.patientStatus ? element.patientStatus : '') +  (element.patientCompare  ? ' + ' + element.patientCompare  : '');
                    (<any>element).optionItems = this.buildRowMenu(element.popupMenu);*/
                });
                this.listCodaRitorni = resultList;
            } else {
                this.listCodaRitorni = [];
            }
            this.componentService.getRootComponent().unmask();
        });
    }

    generateTableElement(element: OverviewTailReturnsDTO) {
        (<any>element).transportDateDescr = decodePhaseForDate(element.phase, element.transportDate.toString());
        (<any>element).patientStatusDescr =
            (element.patientStatus ? element.patientStatus : '') + (element.patientCompare ? ' + ' + element.patientCompare : '');
        (<any>element).optionItems = this.buildRowMenu(element.popupMenu);
    }

    private reloadBookings(idServices: Array<string>) {
        //copio il filter
        let filter = JSON.parse(JSON.stringify(this.filterToSave));
        //
        filter.ids = idServices;

        this.codaRitorniService.searchTailReturnsByFilter(filter).subscribe(result => {
            let cnt = 0;

            let list: OverviewTailReturnsDTO[] = result ? result.resultList : [];

            if (list && list.length > 0) {
                list.forEach(e => {
                    this.generateTableElement(e);
                    //sostituisco gli elementi o li aggiungo alla fine 
                    let index = this.listCodaRitorni.findIndex(r => {
                        return r.id === e.id;
                    });

                    if (index >= 0) {
                        this.listCodaRitorni[index] = e;
                    } else {
                        this.listCodaRitorni.push(e);
                    }
                    //svuoto la lista di id;
                    idServices = idServices.filter(id => {
                        return id !== e.id;
                    });
                });
            }
            if (idServices.length > 0) {
                // se è rimasto qualche id nella lista vuol dire che il booking non è stato caricato
                // provo a rimuoverlo dalla griglia
                this.removeBooking(idServices);
            }
            this.listCodaRitorni =  _.sortBy(this.listCodaRitorni, [function (o) {
                let pad = '0000000000';
                let codeFilled_10 = (pad + o.code).slice(-pad.length);
                let dateString = moment(o.transportDate, 'DD/MM/YYYY HH:mm:ss').format('YYYYMMDDHHmmss');
                return dateString + codeFilled_10;
            }]);//_.orderBy(this.listCodaRitorni, ['transportDate', 'code'], ['asc', 'asc']);
        });
    }

    public goToBookingDetail(res) {
        this.router.navigate(['/modifica-prenotazione', '4', res.id]);//4 indica il codice della pagina di provenienza
    }
    public removeReturn(res) {
        let modal = this.modalService.open(ClosingModalComponent, { size: 'sm' });
        modal.componentInstance.id = res.id;
        modal.result.then((result) => {
            ////console.log("modal result is: " + result);
            if (result) {
                // se l'utente ha chiuso il ritorno, ricarico il sinottico
                this.loadCodaRitorni(null);
            }
        }, (reason) => {
        });
        return;
        //this.router.navigate(['/modifica-prenotazione', '3', res.id]);
    }
    public moveToBooking(r) {
        if (r != null) {
            let body = { bookingId: r.id };
            this.componentService.getRootComponent().showConfirmDialog('Attenzione', "Si desidera confermare l'operazione?").then((result) => {
                this.componentService.getRootComponent().mask();
                this.bookingService.switchBookingReturn(body).subscribe(res => {
                    if (res.removedReturnId) {
                        this.componentService.getRootComponent().showToastMessage('success', 'Nuova Prenotazione creata con codice ' + res.bookingCode);
                        let idBookings: Array<string> = [res.removedReturnId];
                        this.removeBooking(idBookings);
                        this.componentService.getRootComponent().unmask();
                    }
                });
            }, (reason) => {
                console.log("L'elemento resta come ritorno");
                this.componentService.getRootComponent().unmask();
            });
        }
    }

    getRowClasses(row: OverviewTailReturnsDTO): string {
        switch (row.rowStyle.foreground.toLowerCase()) {
            case 'green':
                return 'row-foreground-green';
            case 'yellow':
                return 'row-foreground-yellow';
            case 'red':
                return 'row-foreground-red';
            case 'pink':
                return 'row-background-pink';
            default:
                return '';
        }
    }

    public removeBooking(idServices: Array<string>) {
        // filtro tutti gli elemendi del sinottico 
        this.listCodaRitorni = this.listCodaRitorni.filter(el => {
            //se l'elemento è presente nella lista degli id lo escludo
            let val = idServices.find(value => {
                return value == el.id;
            });
            return !val;
        });
    }

    public decodePhaseForDate(phase, date) {
        return decodePhaseForDate(phase, date);
    }

    public getBookingImage(element) {
        return getBookingImage(element, null, null);
    }

    public getTooltipT(row: OverviewTailReturnsDTO) {
        return getBookingTooltipImage(row.columnT.name, row.ciclicalId, row.source);
    }

    public getTooltipA(row: OverviewTailReturnsDTO) {
        return (row.columnA ? row.columnA.description : '').replace('\n', ' ');
    }


    getForeground(row: OverviewTailReturnsDTO) {
        row.convention
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    fetchHeaderData(id: string) { }

    getHeaderFilter(): any { }

    resetFilter(): void { }

    triggerSubmit(filterInput: OverviewTailReturnFilterDTO): void {
        this.loadCodaRitorni(filterInput);
    }

    fetchComponentData(id: string): void { }

    createDefaultMenu(): any[] {
        //console.log('createDefaultMenu');
        let optionItems = [];

        // optionItems.push(convertMenuItem(mi));
        return optionItems;

        /*let mi: MenuItemValue = {
            enable: true,
            id: "return2booking",
            name: "Trasferisci nella coda prenotazioni",
            position: 0

        }
        let miBookingDet: MenuItemValue = {
            enable: true,
            id: "bookingDetail",
            name: "Dati prenotazione",
            position: 1

        }
        let deleteRet: MenuItemValue = {
            enable: true,
            id: "returnDelete",
            name: "Elimina ritorno",
            position: 2

        }
        let createService: MenuItemValue = {
            enable: true,
            id: "createService",
            name: "Crea il servizio",
            position: 3

        }
        let add2Resource: MenuItemValue = {
            enable: true,
            id: "add2Resource",
            name: "Aggiungi al mezzo",
            position: 4

        }
        let synoptic: MenuItemValue = {
            enable: true,
            id: "synoptic",
            name: "Finestra Prenotazioni",
            position: 1

        }

        optionItems.push(convertMenuItem(mi));
        optionItems.push(convertMenuItem(miBookingDet));
        optionItems.push(convertMenuItem(deleteRet));
        optionItems.push(convertMenuItem(createService));
        optionItems.push(convertMenuItem(add2Resource));
        optionItems.push(convertMenuItem(synoptic));
        return optionItems;*/
    }

    /*public onContextMenu($event: MouseEvent, item: any): void {
        //console.log('onContextMenu');
        this.contextMenuService.show.next({
            // Optional - if unspecified, all context menu components will open
            contextMenu: this.rowMenu,
            event: $event,
            item: item,
        });
        $event.preventDefault();
        $event.stopPropagation();
    }
*/
    buildRowMenu(menuItems: MenuItemValue[]) {
        //console.log('buildRowMenu');
        let optionItems = [];

        if (menuItems && menuItems.length > 0) {
            menuItems.forEach((mi) => {
                optionItems.push(convertMenuItem(mi));
            });
        }
        /*  else {
             optionItems.push(this.createDefaultMenu());
         }*/
        return _.sortBy(optionItems, "position");
    }

    /* public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any {
         if (enabled) {
             let src = source.toLowerCase();
             switch (src) {
                 case "return2booking":
                     this.createBookingFromReturn(row.id);
                     return;
                 case "bookingdetail":
                     this.router.navigate(['/modifica-prenotazione', row.id]);
                     return;
                 case "bookingdetail":
                     this.router.navigate(['/modifica-prenotazione', row.id]);
                     return;                
                 case "bookingdetail":
                     this.router.navigate(['/modifica-prenotazione', row.id]);
                     return;
             }
         }
     }*/

    createBookingFromReturn(retId: string) {

    }
}