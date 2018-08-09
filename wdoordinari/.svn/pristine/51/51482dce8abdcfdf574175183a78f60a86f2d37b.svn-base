import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ServiceCommon } from "app/common-servizi/service-common";
import { CoreTableColumn } from "app/core/table/core-table/core-table.component";
import { ComponentHolderService } from "app/service/shared-service";
import { StaticDataService } from "app/static-data/cached-static-data";
import * as moment from 'moment';
import { BookingDTO, BookingModuleServiceService, CiclicalDTO, CiclicalModuleServiceService, OverviewCiclicalFilterDTO } from "services/services.module";
import { MessageService, Subscription } from '../service/message.service';
import { TokenService } from '../service/token.service';
import { EVENTS } from '../util/event-type';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ngbStructToDate, dateToNgbStruct } from "app/util/convert";
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';

function execute(context: any, funct: { (obj: any): any }): { (obj: any): any } {
    return function (obj: any) {
        return funct.call(context, obj);
    }
}


@Component({
    selector: 'sinottico-cicliche',
    templateUrl: './sinottico-cicliche-component.html',
    styleUrls: ['./sinottico-cicliche-component.scss'],
    encapsulation: ViewEncapsulation.None,
})



export class SinotticoCiclicheComponent implements OnInit {


    ciclicalSubscription: Subscription;
    bookingSubscription: Subscription;

    // ciclica selezionata
    public ciclicalSelected: CiclicalDTO;

    public filterSet: String;
    public filter: OverviewCiclicalFilterDTO;
    public filterToSave: OverviewCiclicalFilterDTO;
 
    public listCicliche = [];
    private currentUser;
    /* FormGroup per la validazione */
    ciclicheFG: FormGroup;


    constructor(
        private componentService: ComponentHolderService,
        private route: ActivatedRoute,
        private sdSvc: StaticDataService,
        private modalService: NgbModal,
        private staticService: StaticDataService,
        private router: Router,
        private serviceCommon: ServiceCommon,
        private ciclicheService: CiclicalModuleServiceService,
        private messageService: MessageService,
        private bookingService: BookingModuleServiceService,
        private tokenService: TokenService,
        private sanitizer: DomSanitizer,
        private fgs: FormGroupGeneratorService

    ) {
        this.ciclicheFG = this.fgs.getFormGroup('sinCicliche');
    }

    public ngOnInit() {
        //recupero l'utente loggato
        if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }
        //se ricevo un messaggio di un evento delle cicliche aggiorno la griglia
        this.ciclicalSubscription = this.messageService.subscribe(EVENTS.CICLICAL);
        this.ciclicalSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });

        //se ricevo un messaggio di un evento delle prenotazioni aggiorno la griglia se la prenotazione è ciclica
        this.bookingSubscription = this.messageService.subscribe(EVENTS.BOOKING);
        this.bookingSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                //carico la prenotazione e se è ciclica, ricarico le cicliche
                if (msg.data.payload && msg.data.action) {
                    switch (msg.data.action) {
                        case "UPDATE":
                            //carico la prenotazione e se è una ciclica, ricarico le cicliche
                            let bookingId: string = null;
                            bookingId = msg.data.payload;
                            this.bookingService.getBookingById(bookingId).subscribe(res => {
                                let result: BookingDTO = res.result;
                                if (result.ciclicalId) {
                                    this.loadCicliche();
                                }
                            });
                            break;

                        default:
                            break;
                    }
                }
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });
    }

    ngOnDestroy() {
        this.ciclicalSubscription.observ$.unsubscribe();
        this.bookingSubscription.observ$.unsubscribe();
       
    }

    fetchComponentData() {
        //console.log('fetchComponentData');
    }

    public getCurrentUser() {
        this.currentUser = this.tokenService.getUserName();
        if (this.currentUser) {
            console.log("################## CURRENT_USER:" + this.currentUser);
            return true;
        } else {
            return false;
        }
    }

    //elaboro il messaggio che ho ricevuto della topic al quale mi sono sottoscritta
    private elaboratePushMessage(msg) {
        if (msg.data.payload && msg.data.action) {
            //se l'operazione è stata effettuata da un altro utente
            if (!this.currentUser || !msg.data.from || (msg.data.from != this.currentUser)) {
                //ricarico il sinottico
                this.loadCicliche();
            }
        }
    }

    public sortRowByFromDate(prev: CiclicalExtDTO, next: CiclicalExtDTO) {
        var fromDateP = moment(prev.fromDateString, 'DD/MM/YYYY');
        var fromDateN = moment(next.fromDateString, 'DD/MM/YYYY');

        if (fromDateP.isSame(fromDateN)) {
            return 0;
        }
        return fromDateP.isBefore(fromDateN) ? -1 : 1;
    }

    public sortRowByToDate(prev: CiclicalExtDTO, next: CiclicalExtDTO) {
        var toDateP = moment(prev.toDateString, 'DD/MM/YYYY');
        var toDateN = moment(next.toDateString, 'DD/MM/YYYY');

        if (toDateP.isSame(toDateN)) {
            return 0;
        }
        return toDateP.isBefore(toDateN) ? -1 : 1;
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }



    //griglia del sinottico
    public columnsCiclicheTable: Array<CoreTableColumn> = [
        { title: 'Trasportato', name: 'bookingCode', index: 1, sort: 'asc', flex: 17},
        { title: 'Postazione', name: 'parking', index: 2, flex: 14},
       // { title: 'Dal giorno', name: 'fromDateString', index: 3, flex: 11},
        //{ title: 'Al giorno', name: 'toDateString', index: 4, flex: 11},
        { title: 'Ultima operazione effettuata da', index: 5, name: 'modificationUser', flex: 19},
        { title: 'Ultima operazione effettuata il', index: 6, name: 'modificationDateString', flex: 23},
        {
            title: '', name: '', index: 7, optionTitle: 'bookingCode', options: [
                {
                    name: 'Elimina la prenotazione ciclica',
                    icon: 'fa fa-trash',
                    clicked: execute(this, this.removeCiclical)
                }
                /*, {
                    name: 'Rendi ciclica la prenotazione',
                    icon: 'fa fa-gg',
                    //clicked: execute(this, this.moveStageUp)
                },*/
                /*{
                    name: 'Pulisce la coda delle prenotazioni cicliche',
                    icon: 'fa fa-eraser',
                    clicked: execute(this, this.clearSelected)
                }*/
                , {
                    name: 'Modifica i dati strutturali della prenotazione ciclica',
                    icon: 'fa fa-calendar',
                    clicked: execute(this, this.modifyCiclicalPeriod)
                }, {
                    name: 'Modifica i dati della prenotazione ciclica',
                    icon: 'fa fa-pencil-square-o',
                    clicked: execute(this, this.modifyCiclical)
                }, {
                    name: 'Sospende/Ripristina la prenotazione ciclica',
                    icon: 'fa fa-clock-o',
                    clicked: execute(this, this.suspendResumeCiclical)
                }, {
                    name: 'Dati della prenotazione',
                    icon: 'fa fa-info-circle',
                    clicked: execute(this, this.goToPrenotazioneInfo)
                }, {
                    name: 'Duplicazione della prenotazione associata alla ciclica',
                    icon: 'fa fa-plus-circle',
                    clicked: execute(this, this.duplicateSingleBooking)
                }
            ], flex: 0, style: { "flex-basis": "30px", }
        }
    ];

    public configCiclicheTable = {
        paging: true,
        sorting: { columns: this.columnsCiclicheTable },
        filtering: { filterString: '' },
    };

    ngAfterViewInit(): void {
        this.componentService.setMiddleComponent('currentSearchTable', this);

        // Partenza automatica della ricerca sinottico con i parametri di dafault
        let currentFilter = sessionStorage.getItem("ciclicalFilter");
        let filterDefault: OverviewCiclicalFilterDTO;
        if (currentFilter) {
            filterDefault = JSON.parse(currentFilter);

        } else {
            filterDefault = { allCiclicalFlag: true };
        }
        setTimeout(() => this.triggerSubmit(filterDefault), 100);
    }

    //metodo che costruisce la stringa che mostra i filtri impostati
    updateFilterSet(): void {
        this.filterSet = "";
        if (this.filterToSave.allCiclicalFlag == true) {
            this.filterSet += "TUTTE" + "; "
        }
        if (this.filterToSave.todayCiclicalFlag == true) {
            this.filterSet += "OGGI" + "; "
        }

        if (this.filterToSave.transportDate != null) {
            let m = moment(this.filterToSave.transportDate);
            let formatDate = m.format('DD-MM-YYYY');
            this.filterSet += "DATA TRASPORTO:" + formatDate + "; ";
        }
        if (this.filterToSave.fromDate != null) {
            let m = moment(this.filterToSave.fromDate);
            let formatDate = m.format('DD-MM-YYYY');
            this.filterSet += "DAL GIORNO:" + formatDate + "; ";
        }
        if (this.filterToSave.toDate != null) {
            let m = moment(this.filterToSave.toDate);
            let formatDate = m.format('DD-MM-YYYY');
            this.filterSet += "AL GIORNO:" + formatDate + "; ";
        }
        if (this.filterToSave.transportedPatient) {
            this.filterSet += "TRASPORTATO:" + this.filterToSave.transportedPatient + "; ";
        }
        if (this.filterToSave.bookingCode) {
            this.filterSet += "CODICE PRENOTAZIONE:" + this.filterToSave.bookingCode + "; ";
        }
        if (this.filterToSave.parkingCode) {
            this.filterSet += "POSTAZIONE:" + this.filterToSave.parkingCode + "; ";
        }
        if (this.filterToSave.status) {
            this.filterSet += "STATO:" + this.filterToSave.status + "; ";
        }
        if (this.filterToSave.days && this.filterToSave.days.length > 0) {
            this.filterSet += "GIORNI:" + this.filterToSave.days + "; ";
        }

        this.filterSet.trim;
    }

    ngAfterViewChecked() {
        //console.log('ngAfterViewChecked');
    }

    ngAfterContentChecked() {
        //console.log('ngAfterContentChecked');
    }

    //vado alla gui per modificare i dati della ciclica
    public async modifyCiclical(ciclical: CiclicalDTO) {
        //se la ciclica è stata sospesa non posso modificare i suoi dati
        if (ciclical.status && ciclical.status == 'UN') {
            var message = 'Questa prenotazione ciclica è stata sospesa. \n E\' possibile apportare modifiche solo riattivandola.';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        } else {
            this.router.navigate(['/modifica-cicliche', ciclical.id]);
        }
    }

    //vado alla gui per la duplicazione della prenotazione
    public async duplicateSingleBooking(ciclical: CiclicalDTO) {
        //carico il booking relativo alla ciclica selezionata
        this.bookingService.getBookingByCode(ciclical.ciclicalBooking.code).catch((e, o) => {
            return [];
        }).subscribe(res => {
            //passare il parametro per indicare che sto facendo una duplicazione
            this.router.navigate(['/modifica-prenotazione', 'C', ciclical.id], { queryParams: { isDuplicate: true } });
            /*if (res){
                this.router.navigate(['/modifica-prenotazione', 'C', res.id], { queryParams: { isDuplicate: true } });
            }else {
                //la prenotazione non esiste quindi ricavo i dati dalla ciclica
                this.router.navigate(['/modifica-prenotazione', 'C', ciclical.id], { queryParams: { isDuplicate: true} });
            }*/
        });
    }

    //vado alla gui per visualizzare/modificare i dati della prenotazione
    public async goToPrenotazioneInfo(ciclical: CiclicalDTO) {
        //i dati della prenotazione vengono recuperati a partire dalla ciclical (ciclical_booking, ecc.)
        this.router.navigate(['/modifica-prenotazione', 'C', ciclical.id]);
        //carico il booking relativo alla ciclia selezionata
        /*this.bookingService.getBookingByCode(ciclical.ciclicalBooking.code).catch((e, o) => {
            return [];
        }).subscribe(res => {
            if (res){
                this.router.navigate(['/modifica-prenotazione', 'C', res.id]);
            }else {
                //la prenotazione non esiste quindi ricavo i dati dalla ciclica
                this.router.navigate(['/modifica-prenotazione', 'C', ciclical.id]);
            }
            
        });*/

    }

    public selectCiclical(ev: CiclicalDTO) {
        this.ciclicalSelected = ev;
    }

    public clearSelected() {
        this.ciclicalSelected = null;
    }

    //rimozione della ciclica
    public async removeCiclical(ciclical: CiclicalDTO) {
        let ciclicalToDelete = {
            ciclicalId: this.ciclicalSelected.id
        };

        this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
            "Eliminare la prenotazione ciclica selezionata?").then((result) => {
                this.componentService.getRootComponent().mask();
                this.ciclicheService.removeCiclical(ciclicalToDelete).catch((e, o) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(res => {
                    this.componentService.getRootComponent().unmask();
                    this.componentService.getRootComponent().showToastMessage('success', "Eliminazione della prenotazione ciclica avvenuta con successo");
                    this.loadCicliche();
                });
            }, (reason) => {
                console.log("Eliminazione della prenotazione ciclica non effettuata ");
                this.componentService.getRootComponent().unmask();
                return;
            });

    }

    //sospensione/attivazione della ciclica
    public async suspendResumeCiclical(ciclical: CiclicalDTO) {
        let ciclicalToCheck = {
            ciclicalId: this.ciclicalSelected.id
        };
        this.componentService.getRootComponent().mask();
        //controllo se la ciclica è sospendibile/attivabile
        this.ciclicheService.checkSuspendCiclical(ciclicalToCheck).catch((o, t) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            this.componentService.getRootComponent().unmask();
            if (!res.checkSuspendCiclicalResult) {
                if (res.checkSuspendCiclicalMessage) {
                    this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                        res.checkSuspendCiclicalMessage).then((result) => {
                            this.componentService.getRootComponent().mask();
                            this.ciclicheService.suspendResumeCiclical(ciclicalToCheck).catch((e, o) => {
                                this.componentService.getRootComponent().unmask();
                                return [];
                            }).subscribe(res => {
                                this.componentService.getRootComponent().unmask();
                                this.componentService.getRootComponent().showToastMessage('success', "Sospensione/Attivazione della prenotazione ciclica avvenuta con successo");
                                this.loadCicliche();
                            });
                        }, (reason) => {
                            console.log("Sospensione/Attivazione della prenotazione ciclica non effettuata");
                            this.componentService.getRootComponent().unmask();
                            return;
                        });
                }
            } else {
                this.componentService.getRootComponent().mask();
                this.ciclicheService.suspendResumeCiclical(ciclicalToCheck).catch((e, o) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(res => {
                    this.componentService.getRootComponent().unmask();
                    this.componentService.getRootComponent().showToastMessage('success', "Sospensione/Attivazione della prenotazione ciclica avvenuta con successo");
                    this.loadCicliche();
                });
            }
        });

    }

    //vado alla gui per modificare i dati strutturali della ciclica
    public async modifyCiclicalPeriod(ciclical: CiclicalDTO) {
        this.router.navigate(['/intervallo-cicliche', this.ciclicalSelected.bookingId], { queryParams: { ciclicalId: this.ciclicalSelected.id,bookingCode: this.ciclicalSelected.bookingCode } });
        return;

    }

    //trigger che scatta prima della ricerca
    triggerSubmit(filterInput: any) {
        if (filterInput != null) {
            if (!this.checkValid()) {
                this.componentService.getRootComponent().unmask();
                return
            };
            this.filter = filterInput;

            sessionStorage.setItem("ciclicalFilter", JSON.stringify(this.filter));
            this.filterToSave = JSON.parse(JSON.stringify(this.filter));

            //console.log(ngbStructToDate(filterInput.fromDate,'DD/MM/YYYY'));
            //console.log(ngbStructToDate(filterInput.toDate,'DD/MM/YYYY'));

            if (filterInput.fromDate && filterInput.toDate && ngbStructToDate(filterInput.fromDate,'DD/MM/YYYY').isAfter(ngbStructToDate(filterInput.toDate,'DD/MM/YYYY'),'day')){
                var message = 'Intervallo \'Dal giorno\' \'Al giorno\'  non corretto';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }else {
                if (filterInput.transportDate != null) {
                    this.filterToSave.transportDate = new Date(filterInput.transportDate['year'], filterInput.transportDate['month'] - 1, filterInput.transportDate['day']);
                }
                if (filterInput.fromDate != null) {
                    this.filterToSave.fromDate = new Date(filterInput.fromDate['year'], filterInput.fromDate['month'] - 1, filterInput.fromDate['day']);
                }
                if (filterInput.toDate != null) {
                    this.filterToSave.toDate = new Date(filterInput.toDate['year'], filterInput.toDate['month'] - 1, filterInput.toDate['day']);
                }
                if (!filterInput.days || filterInput.days.length == 0) {
                    //converto la lista daysStruct(oggetti) in days(stringhe), da passare al server
                    if (filterInput.daysStruct && filterInput.daysStruct.length > 0) {
                        var listTemp = [];
                        for (var i = 0; i < filterInput.daysStruct.length; i++) {
                            listTemp[i] = filterInput.daysStruct[i].id;
                        }
                        this.filterToSave.days = listTemp;
                    }
                }
                //caricamento delle cicliche
                this.loadCicliche();
            }
        }    
    }


    //ricerca delle cicliche in base ai filtri impostati
    public loadCicliche() {
        this.updateFilterSet();
        this.componentService.getRootComponent().mask();
        this.ciclicheService.ciclicals(this.filterToSave).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        })
            .subscribe(res => {
                let tempListCicliche: Array<CiclicalExtDTO> = res.resultList;
                let index = 0;
                for (index = 0; index < tempListCicliche.length; index++) {
                    tempListCicliche[index].modificationDateString = this.getOperation(tempListCicliche[index]);
                    tempListCicliche[index].fromDateString = this.formatDate(tempListCicliche[index].fromDate, 'DD/MM/YY');
                    tempListCicliche[index].toDateString = this.formatDate(tempListCicliche[index].toDate, 'DD/MM/YY');
                    if (!tempListCicliche[index].modificationUser){
                        tempListCicliche[index].modificationUser = tempListCicliche[index].createdBy;
                    }
                    
                }
                this.listCicliche = tempListCicliche;
                this.componentService.getRootComponent().unmask();
            });
    }

    public formatDate(date: Date, format: string) {
        if (date == null) {
            return '';
        }
        return moment(date).format(format);
    }

    //metodo che calcola se sto facendo una sospensione o un'attivazione
    public getOperation(row: CiclicalDTO) {
        let modificationDate: string = '';
        if (row.modificationDate == null) {
            modificationDate = this.formatDate(new Date(), 'DD/MM/YY HH:mm:SS');
        } else {
            modificationDate = this.formatDate(row.modificationDate, 'DD/MM/YY HH:mm:SS');
        }
        let operation: string = '';
        if (row.status == 'PN') {
            operation = ' - Attivazione';
        } else {
            operation = ' - Sospensione';
        }
        return modificationDate + operation;
    }

    //calcolo dell'icona della ciclica (prima colonna a sx)
    public getStatusIcon(row: CiclicalDTO) {
        let today = new Date();
        let toDate: Date = moment(row.toDate).toDate();

        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);

        let image: string;
        if (today.getTime() > toDate.getTime()) {
            image = 'fa fa-clock-o';
        }else {
            if (row.status == 'UN') {
                image = 'fa fa-ban cicliche-red';//sospesa
            }else {
                image = 'fa fa-check-circle-o cicliche-green';
            }    
        }
        return image;
    }

    checkValid() {
        if (!this.ciclicheFG.valid) {
            let message = '';

            for (const key in this.ciclicheFG.controls) {
                if (this.ciclicheFG.controls.hasOwnProperty(key)) {
                    const value = this.ciclicheFG.controls[key];
                    if (value.errors && value.errors['message']) {
                        message += value.errors['message'] + '#$# ';
                    }
                }
            }
            if (message.length > 0)
                message = message.slice(0, -4);
           
            this.componentService.getRootComponent().showModal("Attenzione verificare dati inseriti", cleanMessageList(message.split('#$#')));
            this.componentService.getRootComponent().unmask();
        }
        return this.ciclicheFG.valid;
    }

}

interface CiclicalExtDTO extends CiclicalDTO {
    modificationDateString: string;
    fromDateString: string;
    toDateString: string;
}

export function cleanMessageList(actual) {
    var newArray = new Array();
    for (var i = 0; i < actual.length; i++) {
        if (newArray.indexOf(actual[i].trim())<0) {
            newArray.push(actual[i].trim());
        }
    }
    return newArray;
}