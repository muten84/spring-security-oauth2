import { Component, OnInit, ViewEncapsulation, Optional, Inject } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import { ServiceCommon } from "app/common-servizi/service-common";
import { CoreTableColumn } from "app/core/table/core-table/core-table.component";
import { ComponentHolderService } from "app/service/shared-service";
import { StaticDataService } from "app/static-data/cached-static-data";
import { convertMomentDateToStruct, convertToDate } from "app/util/convert";
import * as moment from 'moment';
import { OverviewTurnFilterDTO, TurnDTO, TurnModuleServiceService, TurnRequestDTO, BASE_PATH } from "services/services.module";
import { TurnModalComponent } from "../modals/turn-modal/turn-modal-component";
import { TurnNoteModalComponent } from "../modals/turn-modal/turn-note-modal-component";
import { MessageService, Subscription } from '../service/message.service';
import { TokenService } from '../service/token.service';
import { EVENTS } from '../util/event-type';
import { saveAs } from 'file-saver';
import { ResponseContentType } from "@angular/http";
import { Http } from "@angular/http";
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';


function execute(context: any, funct: { (obj: any): any }): { (obj: any): any } {
    return function (obj: any) {
        return funct.call(context, obj);
    }
}


@Component({
    selector: 'sinottico-turni',
    templateUrl: './sinottico-turni-component.html',
    styleUrls: ['./sinottico-turni-component.scss'],
    encapsulation: ViewEncapsulation.None,
})



export class SinotticoTurniComponent implements OnInit {


    turnSubscription: Subscription;

    // turno selezionata
    public turnSelected: TurnDTO;

    public filterSet: String;
    public filter: OverviewTurnFilterDTO;
    public filterToSave: OverviewTurnFilterDTO;
    //public initDateToSet : any;
    private today: NgbDateStruct;

    public listTurni = [];

    private vehicleTurnPause: boolean;
    private integration118web: boolean;
    private currentUser;
    /* FormGroup per la validazione */
    turniFG: FormGroup;



    constructor(
        private componentService: ComponentHolderService,
        private route: ActivatedRoute,
        private sdSvc: StaticDataService,
        private modalService: NgbModal,
        private staticService: StaticDataService,
        private router: Router,
        private serviceCommon: ServiceCommon,
        public turniService: TurnModuleServiceService,
        private messageService: MessageService,
        private tokenService: TokenService,
        protected http: Http,
        private sanitizer: DomSanitizer,
        private fgs: FormGroupGeneratorService,
        @Optional() @Inject(BASE_PATH) protected basePath: string
    ) {
        this.turniFG = this.fgs.getFormGroup('sinTurni');
    }

    public ngOnInit() {
        //recupero l'utente loggato
        if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }
        this.vehicleTurnPause = this.staticService.get118Config('VehicleTurnPauseNote', 'FALSE') === 'TRUE';
        this.integration118web = this.staticService.getTSConfig('Integration118Web', 'FALSE') === 'TRUE';
        this.today = convertMomentDateToStruct(moment(new Date()));
        //se ricevo un messaggio di un evento dei turni aggiorno la griglia
        this.turnSubscription = this.messageService.subscribe(EVENTS.TURN);
        this.turnSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });
    }

    ngOnDestroy() {
        this.turnSubscription.observ$.unsubscribe();
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

    public sortRowByStartDate(prev: TurnExtDTO, next: TurnExtDTO) {
        var fromDateP = moment(prev.startTurnDateString, 'DD/MM/YYYY');
        var fromDateN = moment(next.startTurnDateString, 'DD/MM/YYYY');

        if (fromDateP.isSame(fromDateN)) {
            return 0;
        }
        return fromDateP.isBefore(fromDateN) ? -1 : 1;
    }

    public sortRowByEndDate(prev: TurnExtDTO, next: TurnExtDTO) {
        var toDateP = moment(prev.endTurnDateString, 'DD/MM/YYYY');
        var toDateN = moment(next.endTurnDateString, 'DD/MM/YYYY');

        if (toDateP.isSame(toDateN)) {
            return 0;
        }
        return toDateP.isBefore(toDateN) ? -1 : 1;
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }



    //griglia del sinottico
    public columnsTurniTable: Array<CoreTableColumn> = [
        { title: 'Mezzo', name: 'vehicleCode', sort: 'asc', flex: 10 },
        { title: 'Postazione', name: 'parkingCode', flex: 10 },
        //{ title: 'Inizio Turno', name: 'startTurnDateString', flex: 8 },
        //{ title: 'Fine turno', name: 'endTurnDateString', flex: 8 },
        {
            title: '', name: '', index: 6, optionTitle: 'vehicleCode', options: [
                {
                    name: 'Aggiorna il turno selezionato',
                    //icon: 'fa fa-pencil',
                    icon: 'fa fa-pencil-square-o',
                    clicked: execute(this, this.updateTurn)
                }, {
                    name: 'Sospende/Riattiva il turno selezionato',
                    icon: 'fa fa-clock-o',
                    clicked: execute(this, this.changeAvailabilityTurn)
                }, {
                    name: 'Pausa del turno selezionato',
                    icon: 'fa fa-product-hunt',
                    clicked: execute(this, this.changePauseTurn)
                }, {
                    name: 'Cancella il turno selezionato',
                    icon: 'fa fa-trash',
                    clicked: execute(this, this.removeTurn)
                }, {
                    name: 'Imposta il turno dell\'equipaggio sul mezzo in turno selezionato',
                    icon: 'fa fa-ambulance',
                    clicked: execute(this, this.selectTurnCrewMembers)
                }
            ], flex: 0, style: { "flex-basis": "30px" }
        }
    ];

    public configTurniTable = {
        paging: true,
        sorting: { columns: this.columnsTurniTable },
        filtering: { filterString: '' },
    };

    ngAfterViewInit(): void {
        this.componentService.setMiddleComponent('currentSearchTable', this);

        // Partenza automatica della ricerca sinottico con i parametri di dafault
        let currentFilter = sessionStorage.getItem("turnFilter");
        let filterDefault: OverviewTurnFilterDTO;
        if (currentFilter) {
            filterDefault = JSON.parse(currentFilter);
            this.filterToSave = filterDefault;
            this.updateFilterSet();
        } else {
            filterDefault = { initTurnDate: convertToDate(this.today, "") }
        }
        setTimeout(() => this.triggerSubmit(filterDefault), 100);

    }

    //metodo che costruisce la stringa che mostra i filtri impostati
    updateFilterSet(): void {
        this.filterSet = "";

        if (this.filterToSave.initTurnDate != null) {
            let m = moment(this.filterToSave.initTurnDate);
            let formatDate = m.format('DD-MM-YYYY');
            this.filterSet += "DATA INIZIO TURNO:" + formatDate + "; ";
        }
        if (this.filterToSave.initTurnTime) {
            this.filterSet += "ORARIO:" + this.filterToSave.initTurnTime + "; ";
        }
        if (this.filterToSave.vehicleCode) {
            this.filterSet += "MEZZO:" + this.filterToSave.vehicleCode + "; ";
        }
        if (this.filterToSave.parkingCode) {
            this.filterSet += "POSTAZIONE:" + this.filterToSave.parkingCode + "; ";
        }
        if (this.filterToSave.conventionDescr) {
            this.filterSet += "CONVENZIONE:" + this.filterToSave.conventionDescr + "; ";
        }
        if (this.filterToSave.sanitaryCategoryDescr) {
            this.filterSet += "CATEGORIA SANITARIA:" + this.filterToSave.sanitaryCategoryDescr + "; ";
        }

        this.filterSet.trim;
    }

    ngAfterViewChecked() {
        //console.log('ngAfterViewChecked');
    }

    ngAfterContentChecked() {
        //console.log('ngAfterContentChecked');
    }


    public selectTurn(ev: TurnDTO) {
        this.turnSelected = ev;
    }

    public clearSelected() {
        this.turnSelected = null;
    }

    //rimozione del turno
    public async removeTurn() {
        let turnToDelete = {
            parkingVehicleTurnId: this.turnSelected.id
        };

        this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
            "Sei sicuro di voler eliminare il turno selezionato?").then((result) => {
                this.componentService.getRootComponent().mask();
                this.turniService.removeTurn(turnToDelete).catch((e, o) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(res => {
                    this.componentService.getRootComponent().unmask();
                    this.componentService.getRootComponent().showToastMessage('success', "Eliminazione del turno avvenuta con successo");
                    this.loadTurni();
                });
            }, (reason) => {
                console.log("Eliminazione del turno non effettuata ");
                this.componentService.getRootComponent().unmask();
                return;
            });
    }

    //mette/toglie  del turno
    public async changePauseTurn() {
        let turnToPause = {
            parkingVehicleTurnId: this.turnSelected.id
        };
        //se ho l'integrazone attiva con il 118 e la configurazione della pausa del turno attiva
        if (this.vehicleTurnPause && this.integration118web) {
            //se il turno non è in pausa, apro la maschera
            if (!this.turnSelected.pause) {
                //apro la maschera per inserire i dati per mettere/togliere in pausa del turno (codice, tempo e note)
                let modal = this.modalService.open(TurnNoteModalComponent, { size: 'lg' });
                modal.componentInstance.pause = true;
                modal.componentInstance.standby = false;
                modal.componentInstance.parkingVehicleTurnId = this.turnSelected.id;
                modal.result.then((result) => {
                    this.loadTurni();
                }, (reason) => {
                });
            } else {
                //invoco il servizio per togliere la pausa
                let body: TurnRequestDTO = {
                    parkingVehicleTurnId: this.turnSelected.id
                };
                this.componentService.getRootComponent().mask();
                this.turniService.changePause(body).catch((e, o) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(res => {
                    this.componentService.getRootComponent().unmask();
                    this.loadTurni();
                });
            }
        }
    }

    //sospensione/riattivazione del turno
    public async changeAvailabilityTurn() {
        let turnToPause = {
            parkingVehicleTurnId: this.turnSelected.id
        };
        //se ho l'integrazone attiva con il 118 e la configurazione della pausa del turno attiva
        if (this.vehicleTurnPause && this.integration118web) {
            //se il turno è disponibile, apro la maschera
            if (this.turnSelected.availability) {
                //apro la maschera per inserire i dati per mettere/togliere in pausa del turno (codice, tempo e note)
                let modal = this.modalService.open(TurnNoteModalComponent, { size: 'lg' });
                modal.componentInstance.pause = false;
                modal.componentInstance.standby = true;
                modal.componentInstance.parkingVehicleTurnId = this.turnSelected.id;
                modal.result.then((result) => {
                    this.loadTurni();
                }, (reason) => {
                });
            } else {
                //invoco il servizio per riattivarlo
                let body: TurnRequestDTO = {
                    parkingVehicleTurnId: this.turnSelected.id
                };
                this.componentService.getRootComponent().mask();
                this.turniService.changeAvailability(body).catch((e, o) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(res => {
                    this.componentService.getRootComponent().unmask();
                    this.loadTurni();
                });
            }
        }
    }

    //update del turno selezionato
    public async updateTurn() {
        //apro la maschera per modificare i dati del turno
        /*let modal = this.modalService.open(TurnModalComponent, { size: 'lg', windowClass : 'turn-modal' });
        modal.componentInstance.parkingVehicleTurnId = this.turnSelected.id;
        modal.componentInstance.update = true;
        modal.result.then((result) => {
            this.loadTurni();
        }, (reason) => {
        });*/
        this.router.navigate(['/modifica-turno', this.turnSelected.id]);
    }

    //vado alla gui per gestire l'equipaggio del mezzo
    public async selectTurnCrewMembers() {
        this.router.navigate(['/gestione-equipaggio-mezzo', this.turnSelected.id], { queryParams: { vehicleCode: this.turnSelected.vehicleCode, startTurnDateStr: this.formatDate(this.turnSelected.startTurnDate, 'DD/MM/YY HH:mm:ss'), endTurnDateStr: this.formatDate(this.turnSelected.endTurnDate, 'DD/MM/YY HH:mm:ss')} });
    }


    public async exportTurns() {
        //esporto i turni in excel, filtrati per il filtro impostato
        if (!this.isEmptyFilter(this.filterToSave)) {
            this.componentService.getRootComponent().mask();
            this.turniService.exportTurnByFilter()
            this.http.post(`${this.basePath}/api/secure/rest/turn/exportTurnByFilter`,
            this.filterToSave, { responseType: ResponseContentType.Blob }
        ).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        })
            .subscribe(res => {
                this.componentService.getRootComponent().unmask();
                var file = new Blob([res._body], {
                    type: 'application/vnd.ms-excel'
                });
                //uso la libreria FileSaver 
                saveAs(file, 'Turni' + ".xlsx");
            });
        }else {
            var message = 'Impostare almeno un criterio di ricerca';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }


       
    }

    //elaboro il messaggio che ho ricevuto della topic al quale mi sono sottoscritta
    private elaboratePushMessage(msg) {
        if (msg.data.payload && msg.data.action) {
            //se l'operazione è stata effettuata da un altro utente
            if (!this.currentUser || !msg.data.from || (msg.data.from != this.currentUser)) {
                //ricarico il sinottico
                this.loadTurni();
            }
        }
    }

    isEmptyFilter(filter: OverviewTurnFilterDTO): boolean {
        var empty = true;
        //la data di inizio turno, il mezzo o il parcheggio devono essere valorizzati
        if (filter.initTurnDate || filter.parkingCode || filter.vehicleCode) {
            return false;
        }
        return empty;
    }

    //trigger che scatta prima della ricerca
    triggerSubmit(filterInput: any) {
        if (filterInput != null) {
            //se non ho impostato criteri di ricerca, message
            if (!this.isEmptyFilter(filterInput)) {
                if (!this.checkValid()) {
                    this.componentService.getRootComponent().unmask();
                    return
                };
                this.filter = filterInput;
                sessionStorage.setItem("turnFilter", JSON.stringify(this.filter));
                this.filterToSave = JSON.parse(JSON.stringify(this.filter));
                //caricamento dei turni
                this.loadTurni();
            } else {
                var message = 'Impostare almeno un criterio di ricerca';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
        } else {
            var message = 'Impostare almeno un criterio di ricerca';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
    }


    //ricerca delle cicliche in base ai filtri impostati
    public loadTurni() {
        this.updateFilterSet();
        this.componentService.getRootComponent().mask();
        this.turniService.searchTurnByFilter(this.filterToSave).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        })
            .subscribe(res => {
                let tempListTurni: Array<TurnExtDTO> = res.resultList;
                let index = 0;
                for (index = 0; index < tempListTurni.length; index++) {
                    // tempListTurni[index].availabilityString = this.getAvailabilityIcon(tempListTurni[index].availability);
                    tempListTurni[index].startTurnDateString = this.formatDate(tempListTurni[index].startTurnDate, 'DD/MM/YY HH:mm:SS');
                    tempListTurni[index].endTurnDateString = this.formatDate(tempListTurni[index].endTurnDate, 'DD/MM/YY HH:mm:SS');
                }
                this.listTurni = tempListTurni;
                this.componentService.getRootComponent().unmask();
            });
    }

    public formatDate(date: Date, format: string) {
        if (date == null) {
            return '';
        }
        return moment(date).format(format);
    }

    //metodo che calcola l'icona per il campo availability della griglia
    public getAvailabilityIcon(availability: boolean) {
        let image: string;
        if (availability && availability == true) {
            image = 'fa fa-check icon-green';
        } else {
            image = 'fa fa-ban icon-red';
        }

        return image;
    }

    //metodo che calcola l'icona per il campo pause della griglia
    public getPauseIcon(pause: boolean) {
        let image: string;
        if (pause && pause == true) {
            image = 'fa fa-ban icon-red';
        } else {
            image = 'fa fa-check icon-green';
        }

        return image;
    }

    
    checkValid() {
        if (!this.turniFG.valid) {
            let message = '';

            for (const key in this.turniFG.controls) {
                if (this.turniFG.controls.hasOwnProperty(key)) {
                    const value = this.turniFG.controls[key];
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
        return this.turniFG.valid;
    }

}

interface TurnExtDTO extends TurnDTO {
    startTurnDateString: string;
    endTurnDateString: string;
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

