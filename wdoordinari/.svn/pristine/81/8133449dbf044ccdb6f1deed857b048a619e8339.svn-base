import { AfterContentChecked, AfterViewInit, Component, HostListener, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { EVENTS } from "app/util/event-type";
import { convertMenuItem } from 'app/util/sinottico-utils';
import { BrowserCommunicationService } from "common/services/browser-communication.service";
import { Message, SelectServiceInSinottico, Type } from 'common/services/messages';
import * as _ from 'lodash';
import * as moment from 'moment';
import { WorkingResourceCompactDTO, WorkingResourceInputDTO, AvailableParkingVehicleTurnDTO,GenericResultDTO } from 'services/gen';
import { MenuItemValue, TransportModuleServiceService } from '../../services/services.module';
import { CoreTableColumn, CoreTableComponent, CoreTableOption, execute } from '../core/table/core-table/core-table.component';
import { MessageService } from '../service/message.service';
import { ComponentHolderService, MiddleComponent } from '../service/shared-service';
import { VehicleDetailComponent } from "app/gestione-servizi/vehicle-detail.component";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'mezzi-attivi',
    templateUrl: './mezzi-attivi.html',
    styleUrls: ['mezzi-attivi.scss'],
    encapsulation: ViewEncapsulation.None

})
export class MezziAttiviComponent implements OnInit, MiddleComponent, OnDestroy, AfterViewInit, AfterContentChecked {

    public synopticModule = false;

    public viewVehicleTurnFlag:boolean = false;

    // Gruppo di check sulla proposta dei mezzi
    vehicleCheck: {
        //check tutti i mezzi
        allVehicles?: boolean;
        // check mezzi disponibili a breve
        toBeFree?: boolean;

        // Assegnazione diretta
      /*  directAssign?: boolean;
        // Accorpato
        merged?: boolean;*/
    };

//configurazioni mezzi in turno 
public listMezziTurno: Array<AvailableParkingVehicleTurnDTO> = [];
public columnsMezziTurno: Array<CoreTableColumn> = [
    { title: 'Codice', name: 'vehicleCode', index: 1, flex: 15 },
    /* { title: 'Note', name: 'note', style: { width: "10%",  } },*/
    { title: 'Orario', name: 'stopLocationDate', index: 2, flex: 5 },
    { title: 'Postazione', name: 'stopLocation', index: 3, flex: 25 },
    { title: 'Destinazione', name: 'nextParkingCode', index: 4, flex: 25 },
    { title: 'In', index: 5, name: 'startTurnDate', flex: 5 },
    { title: 'Out', index: 6, name: 'endTurnDate', flex: 5 },
    /*{
        title: '', name: '', index: 7, optionTitle: 'vehicleCode', options: [
        {
            name: 'Proprietà',
            icon: 'mdi mdi-information-outline',
            clicked: execute(this, this.vehicleProperty),
            enabled: execute(this, this.getPhysicalFlag)
        }], flex: 0,
        style: { "flex-basis": "50px", }
    }*/
];
public configMezziTurno = {
    paging: true,
    sorting: { columns: this.columnsMezziTurno },
    filtering: { filterString: '' },
};

 //  AvailableParkingVehicleTurnDTO | TsServiceResourceDTO | InterventionActivityDTO
 public vehicleProperty(v) {
    //visualizzare la popup col dettaglio del mezzo
    let vehicleCode = v.vehicleCode || v.resourceCode;

    if (vehicleCode) {
        let modal = this.modalService.open(VehicleDetailComponent, { size: 'lg' });
        modal.componentInstance.vehicleCode = vehicleCode;
    }
}
    public getviewVehicleTurnFlag() {
        return this.viewVehicleTurnFlag;
    }

    public getPhysicalFlag(row?: any) {
        return row?row.physicalFlag:false;
    }

    // check tutti i mezzi

    public enableVehicleTurn(e){        
        if(!this.viewVehicleTurnFlag){
            this.refreshVehicleTurn(e, false);
        }
    }

    public refreshVehicleTurn(all: boolean, toBeFree: boolean) {
        if (all === true) {
            this.vehicleCheck.toBeFree = false;
        }
        if (toBeFree === true) {
            this.vehicleCheck.allVehicles = false;
        }

        this.componentService.getRootComponent().mask();
        let filter = {
            ckAllResourceFlag: all
        };
        let listener = (res: GenericResultDTO) => {
            this.componentService.getRootComponent().unmask();
            this.listMezziTurno = res.resultList || [];
            //this.phisicalVehicle = null;
            //this.vehicleTurn = null;
            //    console.log("loaded vehicle in turn", res);
        };
        if (toBeFree) {
            this.transportService.shortlyAvailableVehicles(filter).subscribe(listener);
        } else {
            this.transportService.selectAvailableVehicleTurn(filter).subscribe(listener);
        }
    }

    public getTooltipMezziTurno(row: AvailableParkingVehicleTurnDTO): string {
        let ret = '';
        if (row.note) {
            ret += (row.note?"NOTA: "+row.note:"");
        }
        if (row.nextParkingCode) {
            ret += 'Mezzo in transito verso ' + row.nextParkingCode;
        }
        if(row.pauseFlag){
            if(!row.endPauseDate){
                ret += ret.length>0?' - ':'';
                ret +="FINE PAUSA INDEFINITA";
            }
            else if(moment(moment(),"HH:mm:ss")<=(moment(row.endPauseDate,"HH:mm:ss"))){
                ret += ret.length>0?' - ':'';
                ret +=(row.startPauseDate?"FINE PAUSA TRA "+moment.utc(moment(row.endPauseDate,"HH:mm:ss").diff(moment(moment(),"HH:mm:ss"))).format("HH:mm:ss"):
                "");
            }
        }
        return ret;
    }

    public getVehicleStatus(row: AvailableParkingVehicleTurnDTO): string {
        
        if (row.nextParkingCode) {
            //mezzo in viaggio 
            return "fa fa-arrow-circle-right";
        }else if(row.pauseFlag){
            return "fa fa-pause-circle";
        }else{
            return "";
        }
    }

    public columnsMezziTable: Array<CoreTableColumn> = [
        {
            flex: 0,
            style: { 'flex-basis': '60px' },
            index: 10,
            title: '',
            name: '',
            optionTitle: 'resourceCode',
            options: (row) => {
                let menu: CoreTableOption[] = [];
                if (row.optionItems) {
                    row.optionItems.forEach(o => {
                        menu.push({
                            name: o.name,
                            icon: 'fa ' + o.icon,
                            clicked: execute(this, (row) => {
                                this.onOptionItemClicked(row, o.eventSource, o.enabled, null);
                            })
                        });
                    });
                }
                return menu;
            }
        }
    ];

    public configMezziTable = {
        paging: true,
        sorting: { columns: this.columnsMezziTable },
        filtering: { filterString: '' },
    };


    @ViewChild(CoreTableComponent) sinottico: CoreTableComponent;

    selected = [];
    public addBookingToServiceLs: (ev: Message) => void;

    currentSelected = {
        optionItems: []
    }
    //Stringa riassuntiva dei filtri di ricerca impostati    
    public filterSet: String;

    public filter: WorkingResourceInputDTO;
    public filterToSave: WorkingResourceInputDTO;

    // sottoscrizione agli eventi pushing
    private serviceSubscription;
    private vehicleSubscription;
    private interventionActivitySubscription;

    rows = [];

    constructor(
        private sanitizer: DomSanitizer,
        private router: Router,
        private messageService: MessageService,
        private componentService: ComponentHolderService,
        private transportService: TransportModuleServiceService,
        protected bcs: BrowserCommunicationService,
        private modalService: NgbModal
    ) {
    }

    @HostListener("resize")
    public resize() {
        console.log('resize div');
    }

    public loadVehicles(filter: WorkingResourceInputDTO) {
        //this.viewVehicleTurnFlag = filter.inTurn;
        if(filter.inTurn){            
            this.enableVehicleTurn(true);
            this.viewVehicleTurnFlag = true;
        }else{
            this.viewVehicleTurnFlag = false;
            this.componentService.getRootComponent().mask();

            this.transportService.getWorkingResourceList(filter).subscribe(result => {

                if (result.resultList) {
                    let risultato: WorkingResourceCompactDTO[] = result.resultList;

                    this.rows = risultato;
                    //      
                    if (this.rows) {
                        this.rows.forEach(e => {
                            let row: WorkingResourceCompactDTO = e;
                            //
                            //   e.optionItems = this.buildRowMenu(row.popupMenu);
                            this.generateDetail(row);

                            e.optionItems = this.buildRowMenu(e.popupMenu);
                        });
                    }
                } else {
                    this.rows = [];
                }
                this.componentService.getRootComponent().unmask();
            });
        }
    }



    private generateDetail(e: WorkingResourceCompactDTO) {
        let groups = [];
        if (e.lastStop) {
            groups.push({
                "name": "Ultima Tappa servita",
                "icon": "fa fa-book",
                values: [{
                    "key": "Codice Servizio",
                    "value": e.lastStop.serviceCode,
                }, {
                    "key": "Indirizzo",
                    "value": e.lastStop.address,
                }, {
                    "key": "Deambulazione",
                    "value": e.lastStop.companion,
                },/*{
                    "key": "Data Attivazione",
                    "value": e.lastStop.activationDate,
                }, */{
                    "key": "Ora Arrivo",
                    "value": e.lastStop.arrivalDate,
                }, {
                    "key": "Ora partenza",
                    "value": e.lastStop.departureDate,
                }]
            });

        }
        if (e.nextStop) {
            groups.push({
                "name": "Prossima Tappa",
                "icon": "fa-book",
                values: [{
                    "key": "Codice Servizio",
                    "value": e.nextStop.serviceCode,
                }, {
                    "key": "Indirizzo",
                    "value": e.nextStop.address,
                }, {
                    "key": "Deambulazione",
                    "value": e.nextStop.companion,
                }/*, {
                    "key": "Data Attivazione",
                    "value": e.nextStop.activationDate,
                }*/]
            });
        }

        (<any>e).detail = {
            groups: groups
        };
    }

    buildRowMenu(menuItems: MenuItemValue[]) {
        //console.log('buildRowMenu');
        let optionItems = [];

        if (menuItems && menuItems.length > 0) {
            menuItems.forEach((mi) => {
                optionItems.push(convertMenuItem(mi));
            });
        }
        return _.sortBy(optionItems, "position");
    }


    public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any { }

    private ordinaAsc: boolean = true;
    private colonnaPrecedente: string = '';

    //Viene chiamato dopo  DatatableComponent.prototype.onColumnSort 
    onSort($event) {
        //    console.log('onSort');
        if ($event && $event.column) {
            let direzione: string = $event.newValue;
            let proprieta = $event.column.prop;

            if (proprieta && this.sinottico) {

                if (proprieta === "dataTasporto") {

                    if (this.colonnaPrecedente && this.colonnaPrecedente === 'dataTasporto') {
                        this.ordinaAsc = !this.ordinaAsc;
                    } else {
                        this.ordinaAsc = false;
                        this.colonnaPrecedente = 'dataTasporto';
                    }

                    let direzioneOrdinamentoData = this.ordinaAsc ? 'asc' : 'desc';
                    //this.sinottico._rows = _.orderBy(this.sinottico._rows, ['transportDate'], [direzioneOrdinamentoData]);

                    this.sinottico.rows = _.orderBy(
                        this.sinottico.rows,
                        function (e) {

                            return e.transportDate;
                        },
                        [direzioneOrdinamentoData]);
                } else {
                    this.ordinaAsc = true;
                    this.colonnaPrecedente = undefined;
                }
            }
        }
    }


    ngOnInit() {
        let filter = { all: true };
        let filterS = sessionStorage.getItem("resourceFilter");
        if (filterS) filter = JSON.parse(filterS);
        this.loadVehicles(filter);

        this.serviceSubscription = this.messageService.subscribe(EVENTS.SERVICE);
        this.serviceSubscription.observ$.subscribe((msg) => {
            if (!this.filter) {
                this.filter = { all: true };
            }
            if (msg.data.action && msg.data.action != "ALERT") {
                this.loadVehicles(this.filter);
            }
            /* else {
                //refresh solo se presente il servizio dell'ALERT nella lista risultato della ricerca
                let trovato = false;
                if (msg.data.payload) {
                    //lista di id dei servizi da allarmare
                    let idServices: Array<string> = msg.data.payload.split(",");
                    if (this.rows) {
                        this.rows.forEach(e => {
                            //Cerco se devo allarmare servizi per risorse non avviate
                            if (e.rowStyle && e.rowStyle.background && e.rowStyle.background != "pink")
                                trovato = this.checkPresentElementIntoArray(idServices, e.id);
                        });
                    }
                    if (trovato) {
                        this.loadVehicles(this.filter);
                    }
                }
            }*/
        });

        this.vehicleSubscription = this.messageService.subscribe(EVENTS.VEHICLE);
        this.vehicleSubscription.observ$.subscribe((msg) => {
            console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
            //disaccoppia contesto subscribe da gestione evento 
            //- in caso di errore lascia sottoscrizione e individua riga errore
            setTimeout(() => {
                this.evaluatePushingEventVehicle(msg);
            }, 0);//Chiudi timeout
        });

        this.interventionActivitySubscription = this.messageService.subscribe(EVENTS.INTERVENTION_ACTIVITY);
        this.interventionActivitySubscription.observ$.subscribe((msg) => {
            if (!this.filter) {
                this.filter = { all: true };
            }
            if (msg.data.action && msg.data.action != "ALERT") {
                this.loadVehicles(this.filter);
            }
        });


        // carico i check dalla cache //TODO
        this.vehicleCheck = {
            //check tutti i mezzi
            allVehicles: false,
            // check mezzi disponibili a breve
            toBeFree: false,
            // Assegnazione diretta
          /*  directAssign: false,
            // Accorpato
            merged: false,*/
        };

        this.viewVehicleTurnFlag = false;
    }

    private evaluatePushingEventVehicle(msg) {
        let action = msg.data.action;
        if (msg.data.payload) {
            //lista di id delle resource da allarmare
            let idResources: Array<string> = msg.data.payload.split(",");
            let trovato = false;
            //cerco nella lista dei mezzi assegnati
            if (true){
                //nel caso è stato rimosso un veicolo da un servizio va fatto refresh lista disponibili
                if (action == "REMOVE") {
                    // se ho trovato un mezzo tra quelli disponibili, effettuo il refresh della stessa lista
                    this.refreshVehicleTurn(this.vehicleCheck.allVehicles, this.vehicleCheck.toBeFree);
                } else {
                    //Cerco nella lista dei mezzi disponibili
                    let trovato = false;
                    if (this.listMezziTurno) {
                        this.listMezziTurno.forEach(sr => {
                            if (!trovato) {
                                trovato = this.checkPresentElementIntoArray(idResources, sr.vehicleId);
                            }
                        });
                    }
                    if (trovato) {
                        // se ho trovato un mezzo tra quelli disponibili, effettuo il refresh della stessa lista
                        this.refreshVehicleTurn(this.vehicleCheck.allVehicles, this.vehicleCheck.toBeFree);
                    }
                }
            }
        }
    }

    private checkPresentElementIntoArray(idList: Array<string>, idSearch: string): boolean {
        let esito = false;
        if (idList && idList.length > 0 && idSearch && idSearch.length > 0) {
            idList.forEach(element => {
                if (element == idSearch) {
                    esito = true;
                }
            });
        }
        return esito;
    }

    ngAfterViewInit() {
        this.componentService.setMiddleComponent('currentSearchTable', this);
        if(this.filter) this.viewVehicleTurnFlag =this.filter.inTurn;
    }

    ngOnDestroy() {
        this.serviceSubscription.observ$.unsubscribe();
        this.interventionActivitySubscription.observ$.unsubscribe();
    }

    ngAfterContentChecked() {
    }

    //Riceve l'evento 'Cerca' dal filtro dove filterInput è il filtro proveniente dall'header
    triggerSubmit(filterInput: any) {

        if (filterInput != null) {
            this.filter = filterInput;
            /*aggiorno qui il filtro perché è l'ultima ricerca che ha avviato*/
            sessionStorage.setItem("resourceFilter", JSON.stringify(this.filter));
            this.filterToSave = JSON.parse(JSON.stringify(this.filter));

            this.loadVehicles(this.filter);
        }
    }


    onPage(event) {

    }

    onSelect(selected) {

        this.currentSelected = selected;

        let param: SelectServiceInSinottico = {
            serviceCode: selected.code,
            serviceId: selected.id
        }
        this.bcs.postMessage(Type.APP2_SET_CURRENT_SERVICE, param, "*");

    }

    onActivate(event) {

        this.currentSelected = event.row;
    }

    getRowClass(row: WorkingResourceCompactDTO) {
    }

    getCellClass(cell) {
    }

    contains(pattern, val): boolean {
        console.log("contains() da sinittico-mezzi");
        let matchResult = pattern.toLowerCase().match(val);
        if (matchResult) {
            return matchResult.length > 0
        }
        return false;
    }

    public formatNgbDateStructToDate(date): Date {
        return new Date(date.year, (date.month - 1), (date.day + 1));
    }

    public formatNgbDateStructToString(date): string {
        return moment(date).format('DD/MM/YYYY HH:mm:ss');
    }

    getColumnSIcon(pattern: boolean) {
        if (pattern === true)
            return '<i class="fa fa-circle"  style="font-size:1.3rem; padding-left:5px; padding-top:5px; color:red"></i>';
        return '<i></i>';
    }

    public sanitize(html: string): SafeHtml {
        //      console.log("sanitize() da sinittico-servizi");
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }


    //Deve implementare fetchComponentData perche' implementa l'intefaccia MiddleComponent
    fetchComponentData() {
        // console.log("fetchComponentData() da sinittico-servizi : TODO");
    }
}