import { AfterContentChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ClosingModalComponent } from "app/modals/closing-modal/closing-modal-component";
import { convertMenuItem, decodeCompact2Sd, decodePhaseForDate, getBookingImage, getBookingTooltipImage } from 'app/util/sinottico-utils';
import { BrowserCommunicationService } from 'common/services/browser-communication.service';
import { SelectBookingInSinottico, Type } from 'common/services/messages';
import * as _ from 'lodash';
import { moment } from 'ngx-bootstrap/chronos/test/chain';
import { CiclicalBookingForVirtualServiceDTO } from 'services/gen/model/models';
import { BookingModuleServiceService, ChargeRequestFilter, ChargeServiceService, MenuItemValue, OverviewBookingDTO, OverviewBookingFilterDTO, TransportModuleServiceService, Value } from '../../services/services.module';
import { CoreTableColumn, CoreTableComponent, CoreTableOption, execute } from '../core/table/core-table/core-table.component';
import { MessageService, Subscription } from '../service/message.service';
import { ComponentHolderService, MiddleComponent } from '../service/shared-service';
import { StaticDataService } from '../static-data/cached-static-data';
import { defIfEmpty, formatDate } from "../util/convert";
import { EVENTS } from '../util/event-type';
import { BookingHistoryModalComponent } from './bookinghistory.component';





@Component({
    selector: 'sinottico-prenotazioni',
    templateUrl: './sinottico-prenotazioni.html',
    styleUrls: ['./sinottico-prenotazioni.scss']

})
export class SinotticoPrenotazioniComponent implements OnInit, MiddleComponent, OnDestroy, AfterViewInit, AfterContentChecked {

    public synopticModule = false;

    public columnsPrenotazioniTable: Array<CoreTableColumn> = [
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
                            enabled: o.enabled,
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

    public configPrenotazioniTable = {
        paging: true,
        sorting: { columns: this.columnsPrenotazioniTable },
        filtering: { filterString: '' },
    };

    @ViewChild(CoreTableComponent) sinottico: CoreTableComponent;

    /*    public items = [
          { name: 'John', otherProperty: 'Foo' },
          { name: 'Joe', otherProperty: 'Bar' }
      ];*/
    bookingSubscription: Subscription;



    selected = [];

    currentId;

    currentSelected = {
        optionItems: [],
        charge: ""
    }

    public filterSet: String;
    public filter: OverviewBookingFilterDTO;
    public filterToSave: OverviewBookingFilterDTO;
    private calculateChargeFlag = false;
    private defaultChargeValue = "n.d.";//"Calcolo preventivo...";

    private idPrenotazioneFocus;

    numAlarm: number = 0;
    numDelay: number = 0;
    numElement: number = 0;

    timeout = undefined;
    rows = [];

    constructor(private sdSvc: StaticDataService,
        private modalService: NgbModal,
        private chargeService: ChargeServiceService,
        private sanitizer: DomSanitizer,
        private route: ActivatedRoute,
        private router: Router,
        protected messageService: MessageService,
        protected componentService: ComponentHolderService,
        protected transportService: TransportModuleServiceService,
        private bookingService: BookingModuleServiceService,
        protected bcs: BrowserCommunicationService) {
        /*   this.fetch((data) => {
             this.rows = data;
           });*/

    }

    /* fetch(cb) {
       const req = new XMLHttpRequest();
       req.open('GET', `assets/data/100k.json`);
   
       req.onload = () => {
         cb(JSON.parse(req.response));
       };
   
       req.send();
     }*/

    public searchBookings(): void {
        //check controllo formato data        
        if(this.filterToSave.transportDate && !moment(this.filterToSave.transportDate).isValid()){
            this.componentService.getRootComponent().showModal("Attenzione!", "Formato data trasporto non valido");
            return;
        }
        //console.log('Search bookings in ricerca prenotazioni-table: ' + this.filterToSave.toString());
        this.updateFilterSet();
        this.componentService.getRootComponent().mask();

        //sul refresh dei dati del sinottico invio l'evento al sinottico staccato
        this.sendMessageToSinottico({});

        this.bookingService.searchBookingByFilter(this.filterToSave).subscribe(result => {
            let cnt = 0;

            let list: OverviewBookingDTO[] = result ? result.resultList : [];
            //Luigi: esempio per colorazione sinottico da rimuovere quando arrierà dal backend...
            this.rows = list;

            if (list && list.length >= 0) {
                this.numElement = this.rows.length;
                this.numAlarm = result.numAlarm;
                this.numDelay = result.numDelay;
            }

            let data = this.rows;
            if (data) {
                data.forEach(e => {
                    this.generateTableMenu(e);
                });
                this.rows = data;
            }

            //remove  value with n.d. value
            let emptyValue = "n.d.";
            if (data) {
                data.forEach(e => {
                    if (e.detail && e.detail.groups) {
                        e.detail.groups.forEach(d => {
                            if (d.values && d.values.length > 0) {
                                let newValues = [];
                                d.values.forEach(p => {
                                    if (emptyValue != p.value)
                                        newValues.push(p);
                                });
                                d.values = newValues;
                            }
                        });
                    }
                    if (e.id == this.idPrenotazioneFocus) {
                        this.selected = e;
                    }
                });
            }
            if (this.selected) {
                this.sinottico.expandRow(this.selected);
            }

            this.componentService.getRootComponent().unmask();

        });
    }

    private generateTableMenu(e) {
        e.optionItems = this.buildRowMenu(e.popupMenu, e.source, e.bookingId);
        e.detail = {
            groups: [{
                "name": "Trasporto",
                "icon": "fa fa-ambulance",
                values: [{
                    "key": "Codice",
                    "value": e.code,
                }/*, {
                "key": "Tipo",
                "value": e.transportType,
            }*/, {
                    "key": "Fase",
                    "value": decodeCompact2Sd("PHASE", e.phase)
                }, {
                    "key": "Postazione",
                    "value": defIfEmpty(e.postazione)
                },
                {
                    "key": "Priorità",
                    "value": decodeCompact2Sd("PRIORITY", e.priority)
                }, {
                    "key": "Preventivo",
                    "value": this.defaultChargeValue //"Calcolo Preventivo...."
                }, {
                    "key": "Riferimento",
                    "value": defIfEmpty(e.riferimento)//"Calcolo Preventivo...."
                }, {
                    "key": "Convenzione",
                    "value": defIfEmpty(e.convention)//"Calcolo Preventivo...."
                },
                {
                    "key": "Attrezzature",
                    "value": defIfEmpty(e.attrezzature)
                }]
            },
            {
                "name": "Paziente",
                "icon": "fa-user-circle",
                values: [
                    {
                        "key": "Trasportato",
                        "value": defIfEmpty(e.denominazionePaziente)
                    },

                    /*{
                        "key": "Cognome",
                        "value": defIfEmpty( e.patientSurname )
                    },
                    {
                        "key": "Nome",
                        "value": defIfEmpty( e.patientName )
                    },*/
                    {
                        "key": "Accompagnato",
                        "value": defIfEmpty(e.patientCompare)
                    },
                    {
                        "key": "Deambulazione",
                        "value": e.patientStatus
                    }
                ]
            },
            {
                "name": "Itinerario",
                "icon": "fa-hospital-o",
                values: [{
                    "key": "Partenza",
                    "value": e.startAddress
                },
                {
                    "key": "Destinazione",
                    "value": e.endAddress
                },
                {
                    "key": "Prevede ritorno",
                    "value": this.formatReturnDateField(e.returnDate)
                }
                ]
            },
            {
                "name": "Informazioni Prenotazione",
                "icon": "fa-info",
                values: [
                    {
                        "key": "Archivio",
                        "value": e.source == 'S' ? 'STORICO' : 'OnLine'
                    },
                    {
                        "key": "Prenotazione ciclica",
                        "value": e.ciclicalId ? 'SI' : 'NO'
                    }, {
                        "key": "Prenotazione di ritorno",
                        "value": e.bookingId ? e.returnReady ? 'SI (non risvegliata)' : 'SI' : 'NO'
                    }, {
                        "key": "Note",
                        "value": defIfEmpty(e.note) //"Calcolo Preventivo...."
                    }, {
                        "key": "Indicazioni",
                        "value": defIfEmpty(e.indicazioni) //"Calcolo Preventivo...."
                    }, {
                        "key": "Creata il",
                        "value": e.creationDate
                    }]
            }]
        };
    }


    updateFilterSet(): void {
        this.filterSet = "";
        if (this.filterToSave.historicalArchiveFlag == true)
            this.filterSet += "ARCHIVIO STORICO incluso" + "; "

        if (this.filterToSave.allBookingFlag == true) {
            this.filterSet += "TUTTE" + "; "
        }
        if (this.filterToSave.todayBookingFlag == true) {
            this.filterSet += "OGGI" + "; "
        }
        if (this.filterToSave.tomorrowBookingFlag == true) {
            this.filterSet += "DOMANI" + "; "
        }
        if (this.filterToSave.fuoriBologna == true) {
            this.filterSet += "FUORI BOLOGNA" + "; "
        }
        if (this.filterToSave.intraOspFlag == true) {
            this.filterSet += "INTRA-H" + "; "
        }
        if (this.filterToSave.interOspFlag == true) {
            this.filterSet += "INTER-H" + "; "
        }
        if (this.filterToSave.fuoriBologna == true) {
            this.filterSet += "FUORI BO" + "; "
        }
        if (this.filterToSave.returnReady == true) {
            this.filterSet += "RITORNI" + "; "
        }
        if (this.filterToSave.transportDate != null) {
            let m = moment(this.filterToSave.transportDate);
            let formatDate = m.format('DD-MM-YYYY');
            this.filterSet += "DATA TRASPORTO:" + formatDate + "; ";
        }
        if (this.filterToSave.cyclicalBookingFlag == true) {
            this.filterSet += "PREN. CICLICHE" + "; "
        }
        if (this.filterToSave.startIntraOspFlag == true) {
            this.filterSet += "PART.INTRA-H" + "; "
        }
        if (this.filterToSave.startExtraOspFlag == true) {
            this.filterSet += "PART.NON-INTRA-H" + "; "
        }
        if (this.filterToSave.endIntraOspFlag == true) {
            this.filterSet += "DEST.INTRA-H" + "; "
        }
        if (this.filterToSave.endExtraOspFlag == true) {
            this.filterSet += "DEST.NON-INTRA-H" + "; "
        }
        if (this.filterToSave.codeList != null && this.filterToSave.codeList.length > 0) {
            this.filterSet += "CODICE:";
            this.filterToSave.codeList.forEach(element => {
                this.filterSet += element + ", ";
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        if (this.filterToSave.typeTxtList != null && this.filterToSave.typeTxtList.length > 0) {
            this.filterSet += "TIPO TRASP.:";
            this.filterToSave.typeTxtList.forEach(element => {
                this.filterSet += element + ", ";
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        if (this.filterToSave.conventionTxtList != null && this.filterToSave.conventionTxtList.length > 0) {
            this.filterSet += "CONV.:";
            this.filterToSave.conventionTxtList.forEach(element => {
                this.filterSet += element + ", ";
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        if (this.filterToSave.startAddressTxtList != null && this.filterToSave.startAddressTxtList.length > 0) {
            this.filterSet += "PART.:";
            this.filterToSave.startAddressTxtList.forEach(element => {
                this.filterSet += element + ", ";
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        if (this.filterToSave.endAddressTxtList != null && this.filterToSave.endAddressTxtList.length > 0) {
            this.filterSet += "DEST.:";
            this.filterToSave.endAddressTxtList.forEach(element => {
                this.filterSet += element + ", ";
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        if (this.filterToSave.patientSurnameTxtList != null && this.filterToSave.patientSurnameTxtList.length > 0) {
            this.filterSet += "COGN.TRASP.:";
            this.filterToSave.patientSurnameTxtList.forEach(element => {
                this.filterSet += element + ", ";
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        if (this.filterToSave.timeSlotList != null && this.filterToSave.timeSlotList.length > 0) {
            this.filterToSave.timeSlotList.forEach(element => {
                if (element == OverviewBookingFilterDTO.TimeSlotListEnum.AM) {
                    this.filterSet += "MATTINA" + ", ";
                }
                if (element == OverviewBookingFilterDTO.TimeSlotListEnum.PM) {
                    this.filterSet += "POMERIGGIO" + ", ";
                }
                if (element == OverviewBookingFilterDTO.TimeSlotListEnum.ND) {
                    this.filterSet += "ORARIO N.D." + ", ";
                }
            });
            this.filterSet = this.filterSet.slice(0, length - 2);
            this.filterSet += "; ";
        }
        this.filterSet.trim;
    }

    private formatReturnDateField(returnDate): string {
        let dataRitornoFlag = this.sdSvc.getConfigValueByKey('BOOKING_DATA_RITORNO', '0');
        dataRitornoFlag = dataRitornoFlag == '1';

        if (!dataRitornoFlag) {
            if (returnDate) {
                return 'SI';
            }
            return 'NO';
        }

        return defIfEmpty(formatDate(returnDate, 'DD-MM-YYYY HH:mm'))
    }

    private formatEquipmentList(eqList: Array<Value>): string {
        let list: string = '';
        if (eqList && eqList.length > 0) {
            eqList.forEach(element => {
                list += element.description + " - ";
            });
            if (list != '') {
                list = list.substring(0, list.length - 3);
            }
        }
        return defIfEmpty(list);
    }

    ngOnInit() {
        this.loadStaticData();
        this.bookingSubscription = this.messageService.subscribe(EVENTS.BOOKING);
        this.bookingSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });
        this.idPrenotazioneFocus = this.route.snapshot.params['id'];
    }

    loadStaticData() {

        let configValues = this.sdSvc.getConfigValueByKey('CHARGE_CALC', '0');
        if (configValues && configValues == '1') {
            this.calculateChargeFlag = true;
            this.defaultChargeValue = 'Calcolo Preventivo....';
        }


    }


    private elaboratePushMessage(msg) {
        if (msg.data.payload && msg.data.action) {
            let idServices: Array<string> = null;
            //lista di id dei servizi da allarmare
            idServices = msg.data.payload.split(",");
            switch (msg.data.action) {
                case "SWITCH_TO_RETURN":
                    //remove dalla coda booking nel caso di passaggio a coda ritorno
                    this.removeBooking(idServices);
                    break;
                case "ARCHIVE":
                    //se è un archiviazione lo tolgo
                    this.removeBooking(idServices);
                    break;
                case "REMOVE":
                    this.removeBooking(idServices);
                    break;
                case "UPDATE":
                // questo evento arriva sia su un update che su una creazione, 
                // faccio le stesse cose sull'alert
                case "ALERT":
                    // in caso di update o alert aggiorno i singoli servizi
                    this.reloadBookings(idServices);
                    break;
                case "SWITCH_TO_BOOKING":
                    // in caso di update o alert aggiorno i singoli servizi
                    this.reloadBookings(idServices);
                    break;
                default:
                    this.searchBookings();
                    break;
            }
        }
    }


    private removeBooking(idServices: Array<string>) {
        // filtro tutti gli elemendi del sinottico
        let deleted = false;
        this.rows = this.rows.filter(el => {
            //se l'elemento è presente nella lista degli id lo escludo
            let val = idServices.find(value => {
                return value == el.id;
            });
            if (val) {
                deleted = true;
                console.log('Rimossa dalla lista la prenotazione con codice:' + el.code + ' e id:' + el.id);
            }
            return !val;
        });
        if (deleted)
            this.aggiornaNumElementAlertDelay();
    }

    private aggiornaNumElementAlertDelay() {
        this.numElement = this.rows.length;
        this.numAlarm = this.rows.filter(x => x.statusOverview == 0).length; //0=ALARM
        this.numDelay = this.rows.filter(x => x.statusOverview == 1).length; //1=TO_LATE e 2=NO_ALARM
    }

    private reloadBookings(idServices: Array<string>) {
        //copio il filter
        let filter = JSON.parse(JSON.stringify(this.filterToSave));
        //
        filter.ids = idServices;

        this.bookingService.searchBookingByFilter(filter).subscribe(result => {
            let cnt = 0;

            let list: OverviewBookingDTO[] = result ? result.resultList : [];

            if (list && list.length > 0) {
                list.forEach(e => {
                    this.generateTableMenu(e);
                    //sostituisco gli elementi o li aggiungo alla fine 
                    let index = this.rows.findIndex(r => {
                        return r.id === e.id;
                    });

                    if (index >= 0) {
                        //let prevStatus = this.rows[index].statusOverview;
                        //this.rows[index] = e;
                        //this.aggiornaNumElementAlertDelay(prevStatus, e.statusOverview, 'UPDATE');
                        let deleted = this.rows.splice(index, 1, e);
                        //this.aggiornaNumElementAlertDelay(deleted[0].statusOverview, e.statusOverview, 'UPDATE');
                        console.log('Sostituita nella lista la prenotazione con codice:' + e.code + ' e id:' + e.id);
                    } else {
                        //this.rows.push(e);
                        this.rows.splice(1, 0, e);//add in first positon
                        //this.aggiornaNumElementAlertDelay(null, e.statusOverview, 'NEW');
                        console.log('Aggiunta alla lista la prenotazione con codice:' + e.code + ' e id:' + e.id);
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
                // LUCA - Potrebbe essere anche un booking accorpato??
                this.removeBooking(idServices);
            }
            //this.rows = _.orderBy(this.rows, ['transportDate', 'code'], ['asc', 'asc']);
            this.rows = _.sortBy(this.rows, [function (o) {
                let pad = '0000000000';
                let codeFilled_10 = (pad + o.code).slice(-pad.length);
                let dateString = moment(o.transportDate, 'DD/MM/YYYY HH:mm:ss').format('YYYYMMDDHHmmss');
                return dateString + codeFilled_10;
            }]);

            this.aggiornaNumElementAlertDelay();
        });
    }



    private ordinaAsc: boolean = true;
    private colonnaPrecedente: string = undefined;


    public sortRowByData(prev: OverviewBookingDTO, next: OverviewBookingDTO) {
        var dataTrasportoP = moment(prev.transportDate, 'DD/MM/YYYY HH:mm:ss');
        var dataTrasportoN = moment(next.transportDate, 'DD/MM/YYYY HH:mm:ss');

        if (dataTrasportoP.isSame(dataTrasportoN)) {
            return 0;
        }
        return dataTrasportoP.isBefore(dataTrasportoN) ? -1 : 1;
    }

    public sortRowByTrasportato(prev: OverviewBookingDTO, next: OverviewBookingDTO) {
        var trasportoP = prev.denominazionePaziente;
        var trasportoN = next.denominazionePaziente;

        return trasportoP.localeCompare(trasportoN);
    }
    //Viene chiamato dopo  DatatableComponent.prototype.onColumnSort 



    ngAfterViewInit(): void {
        //console.log('ngAfterViewInit');
        ////console.log('ngAfterViewInit: RicercaPrenotazioniTableComponent: ')
        this.componentService.setMiddleComponent('currentSearchTable', this);

        // Partenza automatica della ricerca sinottico con i parametri di dafault
        let currentFilter = sessionStorage.getItem("bookingFilter");
        let filterDefault: OverviewBookingFilterDTO;
        if (currentFilter) {
            filterDefault = JSON.parse(currentFilter);
        } else {
            filterDefault = { todayBookingFlag: true };
        }
        setTimeout(() => this.triggerSubmit(filterDefault), 100);
    }


    ngAfterViewChecked() {
        //console.log('ngAfterViewChecked');
    }

    ngOnDestroy() {
        //console.log('ngOnDestroy');
        this.bookingSubscription.observ$.unsubscribe();
        //svuoto il servizio selezionato
        this.sendMessageToSinottico({});
        ////console.log("sinottico destroyed")
    }

    ngAfterContentChecked() {
        //console.log('ngAfterContentChecked');
    }

    triggerSubmit(filterInput: any) {
        //console.log('triggerSubmit');
        if (filterInput != null) {
            this.filter = filterInput;
            /*aggiorno qui il filtro perché è l'ultima ricerca che ha avviato*/
            sessionStorage.setItem("bookingFilter", JSON.stringify(this.filter));
            this.filterToSave = JSON.parse(JSON.stringify(this.filter));
            if (filterInput.transportDate != null) {
                this.filterToSave.transportDate = new Date(filterInput.transportDate['year'], filterInput.transportDate['month'] - 1, filterInput.transportDate['day']);
            }
        }
        //this.updateFilterSet();
        this.searchBookings();

    }

    fetchComponentData() {
        //console.log('fetchComponentData');
    }


    buildRowMenu(menuItems: MenuItemValue[], source, bookingIdRef) {
        //console.log('buildRowMenu');
        let optionItems = this.createDefaultMenu(source,bookingIdRef);
        if (menuItems && menuItems.length > 0) {
            menuItems.forEach((mi) => {
                optionItems.push(convertMenuItem(mi));

            });
        }
        return _.sortBy(optionItems, "position");
    }

    createDefaultMenu(source,bookingIdRef) {
        //console.log('createDefaultMenu');
        let optionItems = [];
        let mi: MenuItemValue = {
            enable: true,
            id: "modifybooking",
            name: "Modifica Prenotazione",
            position: 0

        }
        let viewBook: MenuItemValue = {
            enable: true,
            id: "viewbooking",
            name: "Dati Prenotazione",
            position: 0

        }

        

        if (source && source == 'S') {
            optionItems.push(convertMenuItem(viewBook));
        } else {
            optionItems.push(convertMenuItem(mi));
        }

        if(bookingIdRef){
            let viewBookRef: MenuItemValue = {
                enable: true,
                id: "viewbookingRef",
                name: "Dati Prenotazione andata",
                position: -2
            }
            let separator: MenuItemValue = {
                enable: true,
                id: "SEPARATOR",
                name: "SEPARATOR",
                position: -1
            }
            optionItems.push(convertMenuItem(viewBookRef));
        }

        return optionItems;
    }

    onPage(event) {
        //console.log('onPage');
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            // //console.log('paged!', event);
        }, 100);
    }

    onDetailToggle(event) {
        //console.log('onDetailToggle');
        //   //console.log('Detail Toggled', event);
        let detRow: any = this.currentSelected;
        this.changeCurrentCharge(detRow, this.defaultChargeValue);
    }

    onSelect(selected) {
        //console.log('onSelect');
        // //console.log('Select Event', selected, this.selected);

        this.currentSelected = selected;
        /*Luigi: dichiaro tutto con i tipi così sono sicuro che quello che passo in input
        e' coerente con il backend*/
        let currentBooking: OverviewBookingDTO = <OverviewBookingDTO>this.currentSelected;
        let id = currentBooking.id;

        this.sendMessageToSinottico(currentBooking);

        if (this.currentId == id) {
            return;
        }
        this.currentId = id;
        //Il calcolo del costo deve essere regolato da un parametro di configurazione
        if (this.calculateChargeFlag && this.calculateChargeFlag == true) {
            this.calculateCharge(id);
        }
    }

    sendMessageToSinottico(currentBooking: OverviewBookingDTO) {
        // invio l'id al sinottico staccato se è aperto
        let param: SelectBookingInSinottico = {
            bookingCode: currentBooking.code,
            bookingId: currentBooking.id
        }
        this.bcs.postMessage(Type.APP2_SET_CURRENT_BOOKING, param, "*");
    }

    calculateCharge(id) {
        //console.log('calculateCharge');
        let detRow: any = this.currentSelected;
        this.changeCurrentCharge(detRow, this.defaultChargeValue);
        this.bookingService.getBookingById(id).subscribe(val => {

            let result: any = val.result;
            let request: ChargeRequestFilter = {

                origin: {
                    municipality: result.startAddress.municipality.name,
                    province: result.startAddress.province.name
                },
                destination: {
                    municipality: result.endAddress.municipality.name,
                    province: result.endAddress.province.name
                }

            }
            this.chargeService.computeCharge(request).subscribe(response => {
                let detRow: any = this.currentSelected;
                this.changeCurrentCharge(detRow, this.defaultChargeValue);
                if (this.currentId == result.id) {
                    if (!response.result) {
                        this.changeCurrentCharge(detRow, "Calcolo preventivo non possibile");
                        return;
                    }
                    this.currentSelected.charge = "" + response.result.cost;

                    let found = false;

                    found = this.changeCurrentCharge(detRow, this.currentSelected.charge);

                } else {
                    let detRow: any = this.currentSelected;
                    this.currentSelected.charge = this.defaultChargeValue;
                    this.changeCurrentCharge(detRow, this.currentSelected.charge);
                }
            })
        });
    }

    changeCurrentCharge(row, charge) {
        //console.log('changeCurrentCharge');
        let found = false;
        if (row.detail) {
            row.detail.groups.forEach(e => {
                if (e.name === "Trasporto") {
                    e.values.forEach(v => {
                        if (v.key === "Preventivo") {
                            v.value = charge;
                        }
                    });
                    found = true;
                    return;
                }
            });
        }
        return found;
    }

    onActivate(event) {
        //console.log('onActivate');
        // //console.log('Activate Event', event);
        this.currentSelected = event.row;
    }


    public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any {
        //console.log('onOptionItemClicked');
        if (enabled) {
            /*this.optionItemClicked.emit({ row, column, source });*/
            //console.log('onOptionItemClicked: ' + source + " - " + row.id);
            let src = source.toLowerCase();
            switch (src) {
                case "viewbookingref":
                    this.router.navigate(['/modifica-prenotazione', '5', row.bookingId]);
                    return;
                case "viewbooking":
                //in questo caso apri cmq il dettaglio prenotazione ... in base a valore 'source' si gestisce la possibilità di modifica dei dati 
                case "modifybooking":
                    //let param = { id: row.id, serviceCode: null, serviceId: null };
                    let source = '1';
                    if (row.source && row.source == "S") source = 'S'; // 0 = storico
                    this.router.navigate(['/modifica-prenotazione', source, row.id]);//,{queryParams:{ serviceCode: '1', serviceId:'2'}}
                    return;
                case "aggiungi_ai_mezzi":
                    this.router.navigate(['/mezzi-attivi', row.id]);//,{queryParams:{ serviceCode: '1', serviceId:'2'}}
                    return;
                case "show_history":
                    this.showBookingHistory(row.id);
                    return;
                case "crea_servizio":
                    this.makeService(row);
                    return;
                case 'popup_elimina_prenotazione':
                    let modal = this.modalService.open(ClosingModalComponent, { size: 'sm' });
                    modal.componentInstance.id = row.id;
                    modal.result.then((result) => {
                        ////console.log("modal result is: " + result);
                        if (result) {
                            // se l'utente ha chiuso la prenotazione, ricarico il sinottico
                            this.searchBookings()
                        }
                    }, (reason) => {
                    });
                    return;
                case "archive_booking":
                    this.archiveBooking(row.id);
                    return;
                case 'multiple_duplicate':
                    this.router.navigate(['/duplica-multipla-prenotazioni', row.id]);
                    return;
                case 'popup_rendi_ciclica':
                    this.router.navigate(['/intervallo-cicliche', row.id], { queryParams: { transportDate: row.transportDate } });
                    return;

                case 'move_to_returns':
                    this.moveToReturn(row.id);
                    return;
            }
        }
        /*else {
            event.stopPropagation();
        }*/

    }

    makeService(row: OverviewBookingDTO) {
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

    moveToReturn(id: string) {
        if (id != null) {
            let body = { bookingId: id };
            this.componentService.getRootComponent().showConfirmDialog('Attenzione', "Si desidera confermare l'operazione?").then((result) => {
                this.componentService.getRootComponent().mask();
                this.bookingService.switchBookingReturn(body).subscribe(res => {
                    if (res.removedBookingId) {
                        this.componentService.getRootComponent().showToastMessage('success', 'Prenotazione con codice ' + res.bookingCode + ' ri-trasferito nella coda di ritorno.');
                        let idBookings: Array<string> = [res.removedBookingId];
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

    createService(row: OverviewBookingDTO, cicliche?: [CiclicalBookingForVirtualServiceDTO]) {

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
            this.searchBookings();
            this.componentService.getRootComponent().showToastMessage('success', "Il servizo '" + res.result.code + "' è stato creato!");
            this.componentService.getRootComponent().unmask();
        });
    }

    showBookingHistory(id) {
        //console.log('showBookingHistory');
        this.componentService.getRootComponent().mask();
        this.bookingService.getBookingHistoryById(id).subscribe(res => {
            //console.log("history result: " + JSON.stringify(res));
            this.componentService.getRootComponent().unmask();
            let modal = this.modalService.open(BookingHistoryModalComponent, { size: 'lg', windowClass: 'storico-prenotazione-modal' });

            modal.componentInstance.list = res;
            modal.componentInstance.title = 'Storico modifiche prenotazione';
            modal.componentInstance.columns = [
                { title: 'Data', name: 'modificationDate', flex: 2 },
                { title: 'Operatore', name: 'operator', flex: 1 },
                { title: 'Modifica', name: 'modificationSynthesis', flex: 6 },
            ];
            return modal;
        })
    }

    getRowClasses(row: OverviewBookingDTO): string {
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

    getRowStyle(row: OverviewBookingDTO): { [key: string]: string; } {
        let style = {
            'color': row.rowStyle.foreground.toLowerCase()
        }
        return style;
    }

    getCellClass(cell) {
    }

    contains(pattern, val): boolean {
        ////console.log('contains');
        let matchResult = pattern.toLowerCase().match(val);
        if (matchResult) {
            return matchResult.length > 0
        }
        return false;
    }

    getBookingImage(pattern: string, ciclicalId: string, source: string) {
        return getBookingImage(pattern, ciclicalId, source);
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }


    public decodePhaseForDate(phase: string, date: string) {
        return decodePhaseForDate(phase, date);
    }

    protected truncHour(transportDate: string) {
        return transportDate.substr(0, 10);
    }

    protected archiveBooking(idBooking: string) {
        if (!idBooking || idBooking.length == 0)
            this.componentService.getRootComponent().showToastMessage('error', 'Identificativo prenotazione selezioonata non valido!');
        // verifica id e richiedi la conferma per l'archiviazione       
        this.componentService.getRootComponent().showConfirmDialog('Attenzione!', "Archiviare la prenotazione selezionare?").then((result) => {
            this.componentService.getRootComponent().mask();
            let model = {
                bookingId: idBooking
            };
            this.transportService.archiveBooking(model).subscribe(res => {
                this.componentService.getRootComponent().showToastMessage('success', 'Archiviato con servizio ' + res.service.code);
                this.searchBookings();
            })
        }, (reason) => {
            console.log("Archiviazione non effettuata " + reason);
            this.componentService.getRootComponent().unmask();
        });
    }
    /* public optionItemClick(row: any, column: any, source: any, enabled: boolean, event): void {
            if (enabled) {
                this.optionItemClicked.emit({ row, column, source });
            else {
                event.stopPropagation();
            }
        }*/


    public getTooltipA(row: OverviewBookingDTO) {
        return (row.columnA ? row.columnA.description : '').replace('\n', ' ');
    }

    public getTooltipT(row: OverviewBookingDTO) {
        return getBookingTooltipImage(row.columnT.name, row.ciclicalId, row.source);
    }
}
