import { AfterContentChecked, AfterViewInit, Component, HostListener, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ServiceCommon } from "app/common-servizi/service-common";
import { EVENTS } from "app/util/event-type";
import { convertMenuItem } from 'app/util/sinottico-utils';
import { BrowserCommunicationService } from "common/services/browser-communication.service";
import { Message, SelectServiceInSinottico, Type } from 'common/services/messages';
import { environment } from 'environments/environment';
import * as _ from 'lodash';
import * as moment from 'moment';
import { FiltroSinotticoAvanzato } from 'services/gen';
import { MenuItemValue, SearchTransportResultDTO, TransportModuleServiceService, TransportResultDTO } from '../../services/services.module';
import { CoreTableColumn, CoreTableComponent, CoreTableOption, execute } from '../core/table/core-table/core-table.component';
import { MessageService } from '../service/message.service';
import { ServiziService } from '../service/servizi-service';
import { ComponentHolderService, MiddleComponent } from '../service/shared-service';
import { FiltroCodaServizi } from './sinottico-servizi-model';
import { convertToStruct } from 'app/util/convert';

@Component({
    selector: 'sinottico-servizi',
    templateUrl: './sinottico-servizi.html',
    styleUrls: ['sinottico-servizi.scss'],
    encapsulation: ViewEncapsulation.None

})
export class SinotticoServiziComponent implements OnInit, MiddleComponent, OnDestroy, AfterViewInit, AfterContentChecked {

    public columnsServiziTable: Array<CoreTableColumn> = [
        {
            flex: 0,
            style: { 'flex-basis': '60px' },
            index: 10,
            title: '',
            name: '',
            optionTitle: 'code',
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

    public configServiziTable = {
        paging: true,
        sorting: { columns: this.columnsServiziTable },
        filtering: { filterString: '' },
    };

    @ViewChild(CoreTableComponent) sinottico: CoreTableComponent;

    selected = [];
    private addBookingToServiceLs: (ev: Message) => void;

    currentSelected = {
        optionItems: []
    }

    //Stringa riassuntiva dei filtri di ricerca impostati    
    public filterSet: String;
    // public filterSinotticoToSave: FiltroSinottico;

    //Filtro front-end
    public filtroCodaServizi: FiltroCodaServizi;
    //Filtro da passere al servizio rest
    // public filterRest: FiltroSinottico;
    public filterRest: FiltroSinotticoAvanzato;

    public filter: FiltroSinotticoAvanzato;
    public filterToSave: FiltroSinotticoAvanzato;

    // sottoscrizione agli eventi pushing
    private serviceSubscription;


    numAlarm: number = 0;
    numDelay: number = 0;
    numElement: number = 0;

    timeout = undefined;
    rows = [];

    constructor(
        private sanitizer: DomSanitizer,
        private router: Router,
        private messageService: MessageService,
        private componentService: ComponentHolderService,
        private transportService: TransportModuleServiceService,
        private serviceCommon: ServiceCommon,
        protected bcs: BrowserCommunicationService,
        private serviziService: ServiziService
    ) {
        /*   
        this.fetch((data) => {
             this.rows = data;
           });
        */
    }

    private updateFiltro(filterInput: any) {

        console.log("updateFiltro() da sinittico-servizi");
        this.ordinaAsc = true;
        this.colonnaPrecedente = '';
        this.filterRest = {};

        let filtroFrontEnd: FiltroCodaServizi = filterInput;
        this.printFilter(filtroFrontEnd);

        if (filtroFrontEnd.transportDate)
            this.filterRest.data = this.formatNgbDateStructToDate(filtroFrontEnd.transportDate);

        this.filterRest.fuoriBo = filtroFrontEnd.fuoriBO;
        this.filterRest.interH = filtroFrontEnd.interH;
        this.filterRest.intraH = filtroFrontEnd.intraH;
        this.filterRest.sinottico = filtroFrontEnd.sinottico ? filtroFrontEnd.sinottico : false;
        this.filterRest.historicalArchiveFlag = filtroFrontEnd.historicalArchiveFlag ? filtroFrontEnd.historicalArchiveFlag : false;


        if (!this.filterRest.valori)
            this.filterRest.valori = {};

        for (let item of filtroFrontEnd.listaElementiFiltro) {

            let listaValori: Array<string> = this.filterRest.valori[item.name];

            if (listaValori) {
                listaValori.push(item.value);
            } else {
                let listaValori: Array<string> = [];
                /* if(item.value==undefined){
                     listaValori.push("true");
                 }else{*/
                listaValori.push(item.value);
                // }
                this.filterRest.valori[this.convertiNomeFiltro(item.name)] = listaValori;
            }
        }

    }

    private convertiNomeFiltro(keyIn) {
        switch (keyIn) {
            case "PART.INTRA-H":
                return "INTRA_H_PART";
            case "DEST.INTRA-H":
                return "INTRA_H_DEST";
            case "PART.NON-INTRA-H":
                return "NON_INTRA_H_PART";
            case "DEST.NON-INTRA-H":
                return "NON_INTRA_H_DEST";
        }
        return keyIn;
    }

    private updateRiepilogoFiltro(filterInput: any) {

        console.log("updateRiepilogoFiltro() da sinittico-servizi");

        let filtroFrontEnd: FiltroCodaServizi = filterInput;
        //this.printFilter(filtroFrontEnd);
        var now = new Date();
        let transportDateToSet = { day: now.getUTCDate(), month: now.getUTCMonth(), year: now.getUTCFullYear() };//oggi

        this.filterSet = "";

        if (filtroFrontEnd.historicalArchiveFlag == true)
            this.filterSet += "ARCHIVIO STORICO incluso" + "; "

        this.filterSet += "DATA TRASPORTO: "
        if (filtroFrontEnd.allFlag == true)
            this.filterSet += "TUTTI" + "; "
        else if (filtroFrontEnd.todayFlag == true)
            this.filterSet += "OGGI" + "; "
        else if (filtroFrontEnd.tomorrowFlag == true) {
            now.setDate(now.getDate() + 1);
            transportDateToSet = { day: now.getUTCDate(), month: now.getUTCMonth(), year: now.getUTCFullYear() };//domani
            this.filterSet += "DOMANI" + "; "
        }
        else if (filtroFrontEnd.transportDate != null) {
            if (!filtroFrontEnd.todayFlag && !filtroFrontEnd.tomorrowFlag) {
                transportDateToSet = { day: filtroFrontEnd.transportDate.day, month: filtroFrontEnd.transportDate.month - 1, year: filtroFrontEnd.transportDate.year };
                let m = moment(transportDateToSet);
                let formatDate = m.format('DD-MM-YYYY').toLocaleString();
                // let parts = moment(filtroFrontEnd.transportDate).format("DD-MM-YYYY").split("-");
                this.filterSet += formatDate + "; ";
            }
        }

        if (filtroFrontEnd.fuoriBO == true)
            this.filterSet += "FUORI BOLOGNA" + "; "

        if (filtroFrontEnd.intraH == true)
            this.filterSet += "INTRA-H" + "; "

        if (filtroFrontEnd.interH == true)
            this.filterSet += "INTER-H" + "; "

        for (let item of filtroFrontEnd.listaElementiFiltro) {
            this.filterSet += item.name + ((item.value === undefined || item.value == null) ? " ; " : ":" + item.value + "; ");
        }

        this.filterSet.trim;
        console.log("DATI DEL FILTRO DI RICERCA: " + this.filterSet);
    }


    public searchTransports(): void {
        //    console.log("searchTransports() da sinittico-servizi ");
        this.componentService.getRootComponent().mask();
        if (this.filterRest.data) {
            this.filterRest.data.setUTCHours(0);
            this.filterRest.data.setUTCMinutes(0);
            this.filterRest.data.setUTCSeconds(0);
        }
        console.log("FILTRO DI RICERCA --> " + JSON.stringify(this.filterRest));

        //sul refresh dei dati del sinottico invio l'evento al sinottico staccato
        this.sendMessageToSinottico({});

        this.transportService.filterAdvancedTransports(this.filterRest)
            .subscribe(result => {
                let risultato: SearchTransportResultDTO = result;

                let list: TransportResultDTO[] = risultato ? risultato.resultList : [];
                this.rows = list;

                if (list && list.length >= 0) {
                    this.numElement = this.rows.length;
                    this.numAlarm = result.numAlarm;
                    this.numDelay = result.numDelay;
                }

                if (this.rows) {
                    this.rows.forEach(e => {
                        this.elaborateRow(e);
                    });
                }

                this.componentService.getRootComponent().unmask();
            }, error => {
                console.error("Errore" + error);
                this.componentService.getRootComponent().unmask();
            });
    }

    private elaborateRow(r) {
        let row: TransportResultDTO = r;
        r.optionItems = this.buildRowMenu(row.popupMenu);
        this.generateDetail(row);
    }

    private generateDetail(e: TransportResultDTO) {
        (<any>e).optionItems = this.buildRowMenu(e.popupMenu);
        (<any>e).detail = {
            groups: [{
                "name": "Trasporto",
                "icon": "fa fa-ambulance",
                values: [{
                    "key": "Codice",
                    "value": e.code,
                }, {
                    "key": "Data Trasporto",
                    "value": e.transportDate,
                }, {
                    "key": "Trasportati",
                    "value": this.listToString(e.patients),
                }, {
                    "key": "Archivio",
                    "value": e.source && e.source == 'S' ? 'ARCHIVIO STORICO' : 'OnLine'
                }]
            }, {
                "name": "Itinerario",
                "icon": "fa-hospital-o",
                values: [{
                    "key": "Partenza",
                    "value": e.startAddress
                }, {
                    "key": "Destinazione",
                    "value": e.endAddress
                }]
            }, {
                "name": "Mezzi",
                "icon": "fa-ambulance",
                values: [{
                    "key": "Mezzi",
                    "value": e.resources,
                }]
            }]
        };
    }

    listToString(list, separator?: string) {
        if (list) {
            let defaultSeaprator = ", ";
            if (separator)
                defaultSeaprator = separator;
            return list.map(v => {
                return v || defaultSeaprator;
            });
        } else {
            return '';
        }
    }

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



    private ordinaAsc: boolean = true;
    private colonnaPrecedente: string = '';

    //Viene chiamato dopo  DatatableComponent.prototype.onColumnSort 
    onSort($event) {
        //    console.log('onSort');

        if ($event && $event.column) {

            let direzione: string = $event.newValue; //        "asc"       
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

                            return e.transportDateValue;
                        },
                        [direzioneOrdinamentoData]);

                }

                else {
                    this.ordinaAsc = true;
                    this.colonnaPrecedente = undefined;
                }
            }
        }
    }


    public sortRowByData(prev: TransportResultDTO, next: TransportResultDTO) {
        var dataTrasportoP = moment(prev.transportDate, 'DD/MM/YYYY HH:mm:ss');
        var dataTrasportoN = moment(next.transportDate, 'DD/MM/YYYY HH:mm:ss');

        if (dataTrasportoP.isSame(dataTrasportoN)) {
            return 0;
        }
        return dataTrasportoP.isBefore(dataTrasportoN) ? -1 : 1;
    }


    createDefaultMenu(): any[] {
        //console.log('createDefaultMenu');
        let optionItems = [];
        let mi: MenuItemValue = {
            enable: true,
            id: "assignVehicle",
            name: "Associazione e Gestione",
            position: 0

        }
        let serviceDetail: MenuItemValue = {
            enable: true,
            id: "serviceDetail",
            name: "Dettaglio Servizio",
            position: 1

        }

        let miArchive: MenuItemValue = {
            enable: true,
            id: "archiveService",
            name: "Archivia Servizio",
            position: 2

        }
        let synoptic: MenuItemValue = {
            enable: true,
            id: "synoptic",
            name: "Finestra Prenotazioni",
            position: 3
        }

        optionItems.push(convertMenuItem(mi));
        optionItems.push(convertMenuItem(serviceDetail));
        optionItems.push(convertMenuItem(miArchive));
        optionItems.push(convertMenuItem(synoptic));
        return optionItems;
    }



    public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any {
        if (enabled) {
            let src = source.toLowerCase();
            switch (src) {
                case "assignvehicle":
                    this.router.navigate(['/gestione-servizi', row.id]);
                    return;
                case "servicedetail":
                    this.router.navigate(['/gestione-servizi', row.source, row.id]);
                    return;
                case "archiveservice":
                    this.archiveService(row.id, row.code);
                    return;
                case 'synoptic':
                    if (this.bcs.isChildOpen()) {
                        this.bcs.focusChild();
                    } else {
                        let sendRowIdLs = (ev: Message) => {
                            let param: SelectServiceInSinottico = {
                                serviceCode: row.code,
                                serviceId: row.id
                            }
                            this.bcs.postMessage(Type.APP2_SET_CURRENT_SERVICE, param, "*");
                            this.bcs.removeEventListener(Type.APP2_GET_SELECTED_BOOKING, sendRowIdLs);
                        };
                        this.bcs.addEventListener(Type.APP2_GET_SELECTED_BOOKING, sendRowIdLs);

                        this.bcs.openChildren(environment.synURL, 'prenotazioni', 600, 600);
                    }
                    return;

            }
        }
    }

    private async archiveService(id: string, serviceCode: string) {
        //controlla se isManaged per 
        this.componentService.getRootComponent().mask();
        let checkAllocatedResources = await this.serviceCommon.checkIsManaged(id);
        if (!checkAllocatedResources.ok) {
            this.componentService.getRootComponent().unmask();
            this.componentService.getRootComponent().showModal('warning', 'Archiviazione impossibile! Nessun mezzo è stato associato al servizio.');
        } else {
            this.serviceCommon.checkAndArchiveService(id, serviceCode, true);
        }
    }

    /* private updateFiltriImpostati(): void {
         //   console.log("updateFiltriImpostati() da sinittico-servizi ");
         let filtroFrontEnd: FiltroCodaServizi = this.componentService.getHeaderComponent('currentSearchHeader').getHeaderFilter();
         this.updateFiltro(filtroFrontEnd);
         this.updateRiepilogoFiltro(filtroFrontEnd);
     }*/

    ngOnInit() {
        this.serviceSubscription = this.messageService.subscribe(EVENTS.SERVICE);
        this.serviceSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore 
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                // Action possibili:  "ALERT" , "UPDATE", "ARCHIVE"
                let trovato = false;
                let idServices: Array<string> = null;
                if (msg.data.payload) {
                    //lista di id dei servizi da allarmare
                    idServices = msg.data.payload.split(",");
                    if (this.rows) {
                        this.rows.forEach(e => {
                            //Cerco se devo allarmare servizi per risorse non avviate
                            if (this.checkPresentElementIntoArray(idServices, e.id)) {
                                trovato = true;
                            }
                        });
                    }

                }

                switch (msg.data.action) {
                    case "DELETE":
                        //se non è presente sul sinottico non faccio nulla
                        if (trovato) {
                            this.removeTransport(idServices);
                        }
                        break;
                    case "ARCHIVE":
                        //se non è presente sul sinottico non faccio nulla
                        if (trovato) {
                            this.removeTransport(idServices);
                        }
                        break;
                    case "UPDATE":
                    // questo evento arriva sia su un update che su una creazione, 
                    // faccio le stesse cose sull'alert
                    case "ALERT":
                        // in caso di update o alert aggiorno i singoli servizi
                        this.reloadTransport(idServices, !trovato);
                        break;
                    default:
                        this.searchTransports();
                        break;
                }
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 

        });
    }


    private aggiornaNumElementAlertDelay() {
        this.numElement = this.rows.length;
        this.numAlarm = this.rows.filter(x => x.alarm == TransportResultDTO.AlarmEnum.ALARM).length; //0=ALARM
        this.numDelay = this.rows.filter(x => x.alarm == TransportResultDTO.AlarmEnum.TOOLATE).length; //1=TO_LATE e 2=NO_ALARM
    }

    private removeTransport(idServices: Array<string>) {
        // filtro tutti gli elemendi del sinottico
        let deleted = false;
        this.rows = this.rows.filter(el => {
            //se l'elemento è presente nella lista degli id lo escludo
            let val = idServices.find(value => {
                return value == el.id;
            });
            if (val && val.length > 0) {
                deleted = true;
                console.log('Rimossa dalla lista la prenotazione con codice:' + el.code + ' e id:' + el.id);
            }
            return !val;
        });
        if (deleted)
            this.aggiornaNumElementAlertDelay();
    }

    private reloadTransport(idServices: Array<string>, allFilter: boolean) {
        let filter = null;

        if (allFilter) {
            filter = JSON.parse(JSON.stringify(this.filterRest));
        } else {
            filter = {};
        }


        idServices.forEach(id => {
            filter.id = id;
            this.transportService.filterAdvancedTransports(filter).subscribe(res => {
                let risultato: SearchTransportResultDTO = res;
                let list: TransportResultDTO[] = risultato ? risultato.resultList : [];
                if (list) {
                    list.forEach(e => {
                        this.elaborateRow(e);
                        this.replaceElement(e);
                    });
                }
            });
        });
    }

    private replaceElement(el: TransportResultDTO) {
        // rimpiazzo la riga presente con quella appena caricata
        let index = this.rows.findIndex(r => {
            return r.id === el.id;
        });

        if (index >= 0) {
            //this.rows[index] = el;
            let deleted = this.rows.splice(index, 1, el);
            console.log('Sostituita nella lista la prenotazione con codice:' + el.code + ' e id:' + el.id);
        } else {
            //this.rows.push(el);
            this.rows.splice(1, 0, el);//add in first positon
            console.log('Aggiunta nella lista la prenotazione con codice:' + el.code + ' e id:' + el.id);
        }
        // questo serve a forzare il refresh della grafica, 
        // dato che modificare l'array non scatena il ridisegno 
        this.rows = [...this.rows];
        this.aggiornaNumElementAlertDelay();
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
        //       console.log("ngAfterViewInit() da sinittico-servizi ");
        //      console.log("... inietta il componente come MiddleComponent ");

        this.componentService.setMiddleComponent('currentSearchTable', this);

        // Partenza automatica della ricerca sinottico con i parametri di dafault
        let currentFilter = sessionStorage.getItem("sinotticoFilter");


        let filterDefault: FiltroCodaServizi;
        if (currentFilter) {
            filterDefault = JSON.parse(currentFilter);
        } else {

            let date = new Date();
            let transportDateToSet = { day: date.getUTCDate(), month: date.getUTCMonth() + 1, year: date.getUTCFullYear() };

            //let parts = moment(new Date()).format("DD-MM-YYYY").split("-");
            //let transportDateToSet ={ day: Number(parts[0]), month: Number(parts[1]),  year: Number(parts[2]) };

            filterDefault = {
                tomorrowFlag: false,
                todayFlag: true,
                allFlag: false,
                // firstValue:undefined,
                fuoriBO: false,
                interH: false,
                intraH: false,
                sinottico: false,
                listaElementiFiltro: [],
                historicalArchiveFlag: false,
                // primoElementoFiltro:undefined,
                // secondoElementoFiltro:undefined,
                // secondValue:undefined,
                // selectedPrimoFilterAdd:undefined,
                // selectedSecondoFilterAdd:undefined,
                transportDate: transportDateToSet
            };
        }
        setTimeout(() => this.triggerSubmit(filterDefault), 100);
    }

    ngOnDestroy() {
        //      console.log("ngOnDestroy() da sinittico-servizi : TODO");
        this.serviceSubscription.observ$.unsubscribe();
        this.bcs.removeEventListener(Type.ADD_BOOKING_TO_SERVICE, this.addBookingToServiceLs);

        this.sendMessageToSinottico({});
        //this.bcs.postMessage({type: 'APP2_SET_CURRENT_SERVICE'}, "*");
        //     this.bcs.closeChildren(environment.synURL, 'synoptic');
    }

    ngAfterContentChecked() {
        // console.log("ngAfterContentChecked() da sinittico-servizi : TODO");
    }

    //Riceve l'evento 'Cerca' dal filtro dove filterInput è il filtro proveniente dall'header
    triggerSubmit(filterInput: any) {
        //     console.log("triggerSubmit() da sinittico-servizi ");

        if (filterInput != null) {
            this.filter = filterInput;
            /*aggiorno qui il filtro perché è l'ultima ricerca che ha avviato*/
            sessionStorage.setItem("sinotticoFilter", JSON.stringify(this.filter));
            this.filterToSave = JSON.parse(JSON.stringify(this.filter));
            if (filterInput.transportDate != null) {
                this.filterToSave.data = new Date(filterInput.transportDate['year'], filterInput.transportDate['month'] - 1, filterInput.transportDate['day']);
            }
        }


        if (filterInput != null) {
            this.updateFiltro(filterInput);
            this.updateRiepilogoFiltro(filterInput);
        }
        this.searchTransports();
    }


    onPage(event) {

    }

    onSelect(selected) {
        this.currentSelected = selected;
        this.sendMessageToSinottico(selected);
    }


    sendMessageToSinottico(currentTransport: TransportResultDTO) {
        let param: SelectServiceInSinottico = {
            serviceCode: currentTransport.code,
            serviceId: currentTransport.id
        }
        this.bcs.postMessage(Type.APP2_SET_CURRENT_SERVICE, param, "*");
    }


    onActivate(event) {
        // console.log("onActivate() da sinittico-servizi");
        // console.log('Activate Event', event);
        this.currentSelected = event.row;
    }



    // getACellClass(patientsNumber){

    //     // let valore =row.patientsNumber;
    //     // let value =cell.row;
    //     console.log("getACellClass: " + JSON.stringify(patientsNumber));

    //     console.log("valore: " + patientsNumber);

    //     //<span title="1" style="color:red;">1</span>
    //     let ret = '<span  style="display: block;    height: 60px;    width: 60px;    line-height: 60px; '
    //        + '-moz-border-radius: 30px!important;     border-radius: 30px!important;   background-color: red!important;  '
    //        +' color: white!important;    text-align: center!important;    font-size: 2em;">1</span>';
    //     return ret;

    // }


    getRowClass(row: TransportResultDTO) {
        //console.log("rowclass: " + row.rowStyle.foreground);
        let rt = {
            'row-foreground-green': row.rowStyle.foreground.toLowerCase() === 'green',
            'row-foreground-yellow': row.rowStyle.foreground.toLowerCase() === 'yellow',
            'row-foreground-red': row.rowStyle.foreground.toLowerCase() === 'red',
            'row-background-pink': row.rowStyle.background.toLowerCase() === 'pink',
        }
        // console.log(JSON.stringify(rt));
        return rt;
    }


    getRowClasses(row: TransportResultDTO): string {
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


    getCellClass(cell) {
        return { 'text-center': true };
    }

    contains(pattern, val): boolean {
        console.log("contains() da sinittico-servizi");
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
        //      console.log(pattern);
        if (pattern === true)
            return '<i class="fa fa-circle"  style="font-size:1.3rem; padding-left:5px; padding-top:5px; color:red"></i>';
        return '<i></i>';
    }

    getHistoricalIcon(source: string) {
        //      console.log(pattern);
        if (source && source == 'S')
            return '<i style="padding: 0px; color: #eb1b4c; font-size: 0.8rem; border-radius: 27px" alt="Archivio storico" title="Archivio storico"> S</i>';
        return '<i></i>';
    }

    public sanitize(html: string): SafeHtml {
        //      console.log("sanitize() da sinittico-servizi");
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    private printFilter(filtroFrontEnd: FiltroCodaServizi): void {
        filtroFrontEnd.listaElementiFiltro.forEach(
            e => {
                console.log(" name:  [" + e.name + "] value: [" + e.value + "]");
            }

        );

    }

    //Deve implementare fetchComponentData perche' implementa l'intefaccia MiddleComponent
    fetchComponentData() {
        // console.log("fetchComponentData() da sinittico-servizi : TODO");
    }


    public getTooltipS(row: TransportResultDTO) {
        let text = '';
        if (row.redBall) {
            text += 'Servizio non gestito';
        } else {
            text += 'Servizio gestito';
        }

        if (row.source === 'S') {
            text += ' STORICO';
        }
        return text;
    }


}