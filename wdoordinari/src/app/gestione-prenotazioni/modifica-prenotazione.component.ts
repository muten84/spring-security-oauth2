import { AfterViewInit, Component, EventEmitter, Input, Output, ViewChild, ViewContainerRef, ViewEncapsulation, HostListener } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDismissReasons, NgbDateStruct, NgbModal, NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { AddressBookingDetailModal } from 'app/gestione-prenotazioni/address-booking-detail.modal';
import { ResultGridModalComponent } from 'app/gestione-prenotazioni/result-grid-modal-component';
import { RicercaPazienteModalComponent } from 'app/ricerca-paziente/ricerca-paziente-component';
import { convertMomentDateToStruct, convertToDate, convertToString, valueToSelect, valueToSelectMap } from 'app/util/convert';
import { isEmpty } from 'app/util/string-util';
import { BrowserCommunicationService } from 'common/services/browser-communication.service';
import { SelectBookingInSinottico, Type } from 'common/services/messages';
import * as _ from 'lodash';
import * as moment from 'moment';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Rx';
import 'rxjs/add/operator/switchMap';
import { AcknowledgeResp, AddressBookingDTO, AddressDetail, AddressDetailMapRequest, AddressDetailResponse, BookingDTO, BookingDuplicateSearchDTO, BookingModuleServiceService, CoreLayerModuleServiceService, GenericResultDTOBookingDTO, ParkingServiceService, PatientArchiveDTO, PatientBookingDTO, StormoModuleServiceService, StreetMapServiceService, UpdateBookingServiceDTO, Value } from 'services/services.module';
import { OverviewBookingDTO, TransportModuleServiceService } from '../../services/services.module';
import { RouterComponent } from '../core/routing/router-component';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { MessageService } from '../service/message.service';
import { ComponentHolderService, EventData, EventService } from '../service/shared-service';
import { TokenService } from '../service/token.service';
import { StaticDataService } from '../static-data/cached-static-data';
import { cleanMessageList, isManagedError } from '../util/error-util';
import { EVENTS } from '../util/event-type';
import { CheckDuplicateModalComponent } from './checkduplicate.component';
import { promise } from 'protractor';
import { AddressModifyComponent } from './address-modify-component';
import { FullTextComponent } from './fulltext-component';



const today = new Date();

@Component({
    selector: 'modifica-prenotazione',
    templateUrl: './gestione-prenotazioni.component.html',
    styleUrls: ['./gestione-prenotazioni.component.scss',
        '../ricerca-paziente/ricerca-paziente-component.scss',
        '../app.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ModificaPrenotazioneComponent extends RouterComponent implements AfterViewInit {

    minimal: boolean=false;
    /* Oggetto da riempire*/
    model: BookingDTO;
    modelBck: BookingDTO;

    minDate: NgbDateStruct;
    maxDate: NgbDateStruct;
    startDate: NgbDateStruct;
    toggleClass: string;
    currSubscription;
    servSubscription;

    startAddressModify = false;
    endAddressModify = false;

    @ViewChild('headerDiv') headerDiv;

    @ViewChild('startAddress') startAddressComponent: AddressModifyComponent;
    @ViewChild('endAddress') endAddressComponent: AddressModifyComponent;

    @ViewChild('startFullText') startFullText: FullTextComponent;

    @Output() onSearching = new EventEmitter<boolean>();
    @Output() currentBookingCode = new EventEmitter<string>();

    /* Liste statiche */
    attrezzatureItems: Observable<Array<any>>;
    tipoItems: Observable<Array<any>>;
    deambulationItems: Observable<Array<any>>;
    accompaniedItems: Observable<Array<any>>;
    faseItems: Observable<Array<any>>;
    convenzioneItems: Observable<Array<any>>;
    priorityItems: Array<any>;
    parkingItems: Subject<Array<any>>;

    /* FormGroup per la validazione */
    prenotazioniFG: FormGroup;

    singleSlide: any;

    closeResult: string;

    source: string;//Pagina da cui viene fatta la richiesta / archivio di appartenenza 

    @Input() currentId: string;

    selectedTipo: Value;

    private maxBirthDate: NgbDateStruct = { day: today.getDate(), month: today.getMonth() + 1, year: today.getFullYear() };
    private minBirthDate: NgbDateStruct = { day: 1, month: 1, year: 1900 };

    protected _modelOverviewBooking: OverviewBookingDTO;

    private cognomeNomeFlag: boolean = true;
    private sangueOrganiFlag: boolean = false;
    //private personOrOther: any = null;
    private bloodValue: boolean = false;

    private currentUser;
    private serviceCode: string;
    private serviceId: string;
    private returnBookingId: string;
    private returnBookingCode: string;

    dataRitornoFlag;
    dataRitornoLabel;
    dataRitornoSwitchLocked: boolean = false;
    dataRitornoSwitch: boolean;

    public disableSave: boolean = true;

    showSangue;
    showOrganiVarie;
    showCharge;

    private queryParams;
    private pageSourceParam;
    private enableModifyCiclicalMatrix: boolean;

    endAddressEnable = true; //per abilitare/disabilitare il dettaglio dell'indirizzo di destinazione
    transportDateEnable = true; //per abilitare/disabilitare la data del trasporto quando provengo dalla ciclica
    transportHourEnable = true; //per abilitare/disabilitare l'orario del trasporto quando provengo dalla ciclica
    phaseEnable = true; //per abilitare/disabilitare la fase del trasporto quando provengo dalla ciclica

    constructor(
        private sanitizer: DomSanitizer,
        private coreService: CoreLayerModuleServiceService,
        private componentService: ComponentHolderService,
        private msgService: MessageService,
        private sdSvc: StaticDataService,
        private fgs: FormGroupGeneratorService,
        private eventService: EventService,
        private modalService: NgbModal,
        vcr: ViewContainerRef,
        private route: ActivatedRoute,
        router: Router,
        private bookingService: BookingModuleServiceService,
        private parkingService: ParkingServiceService,
        private tokenService: TokenService,
        private transportService: TransportModuleServiceService,

        private streetService: StreetMapServiceService,
        protected bcs: BrowserCommunicationService,

        private stormoService: StormoModuleServiceService,
        private staticService: StaticDataService,

    ) {
        super(router)
        this.prenotazioniFG = this.fgs.getFormGroup('prenotazioni');
        this.parkingItems = new Subject();
    }

    @HostListener("keypress", ["$event"])
    public onClick(event: KeyboardEvent): void {
        if (event.keyCode === 13) {
            event.preventDefault();
        }
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.prenotazioniFG);
    }

    ngOnInit(): void {
        super.ngOnInit();
        //console.log('subscribe to messagedto from modifica prenotazione');
        this.currSubscription = this.msgService.subscribe(EVENTS.BOOKING);
        this.currSubscription.observ$.catch((err, c) => {
            let message = 'Errore nella sottoscrizione alla topic ' + EVENTS.BOOKING;
            console.log("Errore sottoscrizione topic:" + err);
            this.componentService.getRootComponent().showToastMessage('error', message);
            return Observable.throw(err);
        }).subscribe((msg) => {

            console.log('received message from topic ' + msg.topic + ' and payload ' + msg.data.payload);
            //disaccoppia contesto subscribe da gestione evento 
            //- in caso di errore lascia sottoscrizione e individua riga errore
            setTimeout(() => {
                this.evaluatePushingEvent(msg);
            }, 0);
        });

        this.servSubscription = this.msgService.subscribe(EVENTS.SERVICE);
        this.servSubscription.observ$.catch((err, c) => {
            let message = 'Errore nella sottoscrizione alla topic ' + EVENTS.SERVICE;
            console.log("Errore sottoscrizione topic:" + err);
            this.componentService.getRootComponent().showToastMessage('error', message);
            return Observable.throw(err);
        }).subscribe((msg) => {
            //this.currSubscription = this.msgService.subscribe(EVENTS.BOOKING).observ$.subscribe((msg) => {
            console.log('received message from topic ' + msg.topic + ' and payload ' + msg.data.payload);
            //disaccoppia contesto subscribe da gestione evento 
            //- in caso di errore lascia sottoscrizione e individua riga errore
            setTimeout(() => {
                this.evaluatePushingEventService(msg);
            }, 0);
        });
    }


    loadData(): void {
        // Inizializzazione interfaccia
        this.minDate = { year: 1900, month: 1, day: 1 };

        let now = moment();
        this.maxDate = { day: now.date(), month: now.month() + 1, year: now.year() };

        this.startDate = this.maxDate;//{ day: 1, month: 1, year: now.year() - 1 };

        //Disabilito salvataggio di default
        this.disableSave = false;

        this.toggleClass = 'fa-chevron-down';
        this.currentId = this.route.snapshot.params['id'];

        this.source = this.route.snapshot.params['source']

        if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }

        //Query Param
        this.queryParams = this.route.snapshot.queryParams;
        console.debug('QueryParam:' + this.queryParams);

        this.loadStaticData();

        this.copyToModel(this.newModel());

        if (this.currentId) {
            this.refreshBooking();
        } else {
            this.minimal=true;
            this.initDefaultValueSetting();
            this.componentService.getRootComponent().unmask();
        }


        if (this.queryParams) {
            if (this.queryParams.serviceCode) {
                this.serviceCode = this.queryParams.serviceCode;
            } else {
                console.warn('Valore del serviceCode non presente con QueryParam non nullo');
            }
            if (this.queryParams.serviceId) {
                this.serviceId = this.queryParams.serviceId;
            } else {
                console.warn('Valore del serviceId non presente con QueryParam non nullo');
            }

            if (this.queryParams.returnBookingCode) {
                this.returnBookingCode = this.queryParams.returnBookingCode;
            }
            if (this.queryParams.returnBookingId) {
                this.returnBookingId = this.queryParams.returnBookingId;
            }
            /* if (queryParam.isDuplicate){
                 this.model.code = null;
                 this.model.id = null;
                 this.model.duplicable = true;
                 this.model.creationDate = null;
                 this.model.creationUser = null;
                 this.model.assignedParkingCode = null;
                 (<any>this.componentService.getHeaderComponent('headerComponent')).fromItemToBannerModel(this.model);
                 this.componentService.getRootComponent().showToastMessage('success', 'Prenotazione duplicata in locale. \nProcedere con le eventuali modifiche ed al salvataggio sul server.');
                 //this.disableSave = false;
             } */
        }
        //disable/enable save booking param        
        //this.disableSaveButtonBySourceRequestParam();
    }
 

    sendMessageToSinottico(currentBooking: BookingDTO) {
        // invio l'id al sinottico staccato se è aperto
        let param: SelectBookingInSinottico = {
            bookingCode: currentBooking ? currentBooking.code : null,
            bookingId: currentBooking ? currentBooking.id : null
        }
        this.bcs.postMessage(Type.APP2_SET_CURRENT_BOOKING, param, "*");
    }

    public disableSaveButtonBySourceRequestParam() {

        let source = this.source;
        let disabled = false;

        if (!this.currentId) {
            disabled = false;
        } else if (!source || source.trim().length == 0) {
            disabled = true;
        } else {
            this.pageSourceParam = source;
            switch (source) {
                case "1"://from sinottico booking
                    disabled = false;
                    break;

                case "2"://from service detail
                    disabled = false;
                    break;

                case "C"://from cyclical
                    disabled = false;
                    //disabilito il dettaglio dell'indirizzo di destinazione
                    this.endAddressEnable = false;
                    break;

                case "4"://from return tail
                    disabled = true;
                    break;

                case "5"://from booking detail
                    disabled = true;
                    break;

                case "6"://from booking detail return
                    if (this.model.returnReady)
                        disabled = true; //vengo al ritorno ma non ancora risvegliato
                    else
                        disabled = false;
                    break;

                case "S"://STORICO disabled
                    disabled = true;
                    break;

                case ""://default disabled
                    disabled = true;
                    break;
            }
        }

        this.disableSave = disabled;
    }


    public refreshBooking() {
        if (this.currentId) {
            this.componentService.getRootComponent().mask();
            this.bookingService.getBookingById(this.currentId, this.source).catch((err, c) => {
                let message = 'Errore nel recupero della prenotazione.';
                console.log("Errore sottoscrizione topic:" + err);
                this.componentService.getRootComponent().showToastMessage('error', message);
                return Observable.throw(err);
            }).subscribe(val => {
                if (val.result == null) {
                    let message = 'Errore nel recupero della prenotazione.\nLa prenotazione potrebbe essere archiviata o cancellata.';
                    this.componentService.getRootComponent().showToastMessage('warning', message);
                    this.componentService.getRootComponent().unmask();
                } else {
                    this.copyToModel(val.result);
                    this.componentService.getRootComponent().unmask();
                    if (val.result.blood)
                        this.bloodValue = val.result.blood;

                    this.sendMessageToSinottico(this.model);
                    this.disableSaveButtonBySourceRequestParam();

                    //salvo modello di bck per eventuali modifiche    
                    this.modelBck = _.cloneDeep(this.model);
                }
            });
        } else {
            console.log("refresh booking problem: currentId not setting")
            this.initDefaultValueSetting();
            this.componentService.getRootComponent().unmask();
        }
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

    hasReturnBooking(): boolean {
        if (!this.model.bookingId && this.returnBookingId) {
            return true;
        } else {
            return false
        }
    }

    getReturnBooking() {
        if (this.returnBookingId) {
            this.router.navigate(['/modifica-prenotazione', this.source == 'S' ? 'S' : '6', this.returnBookingId]);
        }
    }

    getForwardBooking() {
        if (this.model.bookingId) {
            /* let mySource = '5';
            if (this.source == 'S')
                mySource = 'S'; */
            this.router.navigate(['/modifica-prenotazione', this.source == 'S' ? 'S' : '5', this.model.bookingId], { queryParams: { returnBookingId: this.model.id, returnBookingCode: this.model.code } });//5=bookingDetail indica il codice della pagina di provenienza     
        }
    }

    private evaluatePushingEvent(msg) {
        if (msg.data.action && msg.data.action != "ALERT") {
            let ids: string[] = msg.data.payload.split(",");
            if (ids && ids.length > 0 && this.model.id) {
                for (var i = 0; i < ids.length; i++) {
                    if (ids[i] === this.model.id) {
                        if (msg.data.action && (msg.data.action == "DELETE" ||
                            msg.data.action == "ARCHIVE" ||
                            msg.data.action == "REMOVE" ||
                            msg.data.action == "SWITCH_TO_RETURN" ||
                            msg.data.action == "SWITCH_TO_BOOKING")) {
                            this.componentService.getRootComponent().showModal('Attenzione!', "La prenotazione '" + this.model.code + "' è stata '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                            if (this.pageSourceParam == '4') {//stiamo chiamando il dettaglio dalla coda dei ritorni
                                this.router.navigate(['/coda-ritorni']);
                            }
                            else {
                                if (msg.data.action == "SWITCH_TO_BOOKING") {
                                    this.serviceId = undefined;
                                    this.serviceCode = undefined;
                                    this.router.navigate(['/modifica-prenotazione', this.model.id]);
                                }
                                else {
                                    this.router.navigate(['/sinottico-prenotazioni']);
                                }
                            }
                        } else {
                            if (msg.data.from != this.currentUser) {
                                //prenotazione aggiornata --- segue messaggio se modifica non è effettuata da utente corrente
                                this.componentService.getRootComponent().showModal('Attenzione!', "La prenotazione '" + this.model.code + "' è stata '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                                this.refreshBooking();
                            }
                        }
                        /*this.componentService.getRootComponent().unmask();
                        this.componentService.getRootComponent().showConfirmDialog('Attenzione!', "Sono state effettuate delle modifiche dall'utente '" + msg.data.from + "'. \nE' necessario ricaricare dati. Procedere?").then((result) => {
                            console.log("dati modificati lato server: forzo refresh");
                            this.refreshBooking();
                        }, (reason) => {
                            console.log("dati modificati lato server: non forzo refresh");
                        });*/
                    }
                }
            }
        }
    }

    private evaluatePushingEventService(msg) {
        if (this.serviceId != null && this.serviceId && msg.data.action) {
            if (msg.data.action == "UPDATE") {
                console.log("dati modificati lato server: non forzo refresh");
            } else if (msg.data.action == "ARCHIVE" || msg.data.action == "DELETE") {
                let serviceAction = (msg.data.action == "ARCHIVE" ? "ARCHIVIATO" : "CANCELLATO");
                this.componentService.getRootComponent().showModal('Attenzione!', "La prenotazione '" + this.model.code + "' è associata al servizio " + this.serviceCode + ".\nIl servizio è stato '" + serviceAction + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                this.router.navigate(['/sinottico-prenotazioni']);
            }
        }
    }

    initDefaultValueSetting() {
        let m = moment();
        //console.log("la data corrent :" + m);
        (<any>this.model).transportDateObj = convertMomentDateToStruct(m);
        //(<any>this.model).transportTime = convertToString(m.toDate());
        //(<any>this.model).returnDateObj = convertMomentDateToStruct(m);        
        this.model.priority = "NO";
        this.disableSave = false;
        this.source = "O";
        //returnDate prop
        this.dataRitornoSwitch = false;
        this.dataRitornoSwitchLocked = false;
    }

    newModel(): BookingDTO {
        return <BookingDTO>{
            patientBooking: {},
            req: {},
            startAddress: {},
            endAddress: {},
        };
    }

    /*
     * Caricamento dati statici dal DB
     */
    loadStaticData() {

        //this.loadPriorityComboSyncRequest();  
        this.priorityItems = this.sdSvc.getStaticData('PRIORITY').map(valueToSelectMap);
        this.attrezzatureItems = this.sdSvc.getStaticDataByType('ATTREZZATURA').map(valueToSelect);

        this.tipoItems = this.sdSvc.getStaticDataByType('TIPO').map(valueToSelect);
        this.faseItems = this.sdSvc.getStaticDataByType('PHASE').map(valueToSelect);
        this.convenzioneItems = this.sdSvc.getStaticDataByType('CONVENTION').map(valueToSelect);

        this.deambulationItems = this.sdSvc.getStaticDataByType('DEAMBULAZIONE');//.map(valueToSelect);
        this.accompaniedItems = this.sdSvc.getStaticDataByType('ACCOMPAGNATO').map(valueToSelect);

        let chargePresentConfigValue = this.sdSvc.getConfigValueByKey('CHARGE_VIEW', '0');
        if (chargePresentConfigValue && chargePresentConfigValue == '1') {
            this.showCharge = 1;
        } else {
            this.showCharge = 0;
        }

        this.dataRitornoFlag = this.sdSvc.getConfigValueByKey('BOOKING_DATA_RITORNO', '0');
        this.dataRitornoFlag = this.dataRitornoFlag == '1';

        this.dataRitornoLabel = 'Ritorno';
        if (this.dataRitornoFlag) {
            this.dataRitornoLabel = 'Data e Ora Ritorno';
        }

        this.showSangue = this.sdSvc.getConfigValueByKey('BOOKING_SHOW_SANGUE', '0');
        this.showSangue = this.showSangue == '1';

        this.showOrganiVarie = this.sdSvc.getConfigValueByKey('BOOKING_SHOW_ORGANI_VARIE', '0');
        this.showOrganiVarie = this.showOrganiVarie == '1';


        //se provengo da una ciclica e non sto duplicando la prenotazione leggo configurazione per disabilitare alcuni campi della prenotazione
        if (this.source && this.source == 'C' && (this.queryParams && !this.queryParams.isDuplicate)) {
            this.enableModifyCiclicalMatrix = this.staticService.getTSConfig('EnableModifyCiclicalMatrix', 'FALSE') === 'TRUE';
            if (!this.enableModifyCiclicalMatrix) {
                this.phaseEnable = false;
                this.transportDateEnable = false;
                this.transportHourEnable = false;
            }
        }

    }

    // public async loadPriorityComboSyncRequest() {
    //     this.priorityItems = await this.sdSvc.getStaticDataByType('PRIORITY').map(valueToSelect);
    // }

    setValue(value: any, object: any, property: string) {
        object[property] = value.text;
    }

    setValueCompact(value: any, object: any, property: string) {
        object[property] = value.compact;
    }


    offClickHandler(event: any) {
        // console.log('offClickHandler: ' + event);
        /*  if (!this.container.nativeElement.contains(event.target)) { // check click origin
              this.dropdown.nativeElement.style.display = "none";
          }*/
    }

    ngAfterViewInit() {

        let event = new EventData();
        event.from = 'modifica-prenotazione';
        event.data = this.currentId;
        this.eventService.sendEvent(event);

        if (this.queryParams && this.queryParams.isDuplicate) {
            this.duplicateSingleBooking();
        }
        if(!this.currentId)
            this.startFullText.focus();
    }

    /* dateChange(date) {
         this.date = date;
     }
 
     dateClick() {
         //console.log('click click!')
     }
     */

    headerClick(source: any) {
        // console.log('header clicked');
    }

    onToggleClick(source: any) {
        console.log('toggle  clicked: ' + source);
    }


    onPanelChange(event: NgbPanelChangeEvent) {
        if (event.panelId != 'PartenzaDestPrenotazione' || !event.nextState) {
            this.searchAuthority();
        }
        //  console.log('onPanelChange: ' + event.panelId + ' - ' + event.nextState);
    }

    beforeToggle(event: any) {
        // console.log('beforeToggle: ' + event);
    }

    afterToggle(event: any) {
        // console.log('afterToggle: ' + event);
        (<any>this.model).selectDate(moment());
    }

    copyAndClean(model): BookingDTO {
        let modelToSave: BookingDTO = JSON.parse(JSON.stringify(model));
        // pulisco l'oggetto dai campi di appoggio
        (<any>modelToSave).transportDateObj = undefined;
        (<any>modelToSave).returnDateObj = undefined;
        modelToSave.transportDate = convertToDate((<any>this.model).transportDateObj, (<any>this.model).transportTime);
        modelToSave.returnDate = convertToDate((<any>this.model).returnDateObj, (<any>this.model).returnTime);
        this.dataRitornoSwitch = false;
        //        modelToSave.assignedParkingCode = model.assignedParkingCode ? model.assignedParkingCode.name : undefined;
        if (!isEmpty(modelToSave.variousDetail)) {
            modelToSave.organsFlag = true;
        }
        if (!isEmpty(modelToSave.code)) {
            modelToSave.duplicable = true;
        }

        if ((<any>modelToSave).patientBooking != null) {
            if ((<any>modelToSave).patientBooking.birthDateObj != null) {
                modelToSave.patientBooking.birthDate = convertToDate((<any>this.model).patientBooking.birthDateObj, null);
                (<any>modelToSave).patientBooking.birthDateObj = undefined;
            } else {
                modelToSave.patientBooking.birthDate = null;
            }
        }

        return modelToSave;
    }

    copyToModel(model) {
        this.model = JSON.parse(JSON.stringify(model));
        // pulisco l'oggetto dai campi di appoggio
        if (model.transportDate) {
            (<any>this.model).transportDateObj = convertMomentDateToStruct(moment(model.transportDate));
        }
        if (model.returnDate) {
            (<any>this.model).returnDateObj = convertMomentDateToStruct(moment(model.returnDate));
            this.dataRitornoSwitch = true;
        } else {
            this.dataRitornoSwitch = false;
        }
        (<any>this.model).transportTime = convertToString(model.transportDate);
        (<any>this.model).returnTime = convertToString(model.returnDate);

        if (model.patientBooking != null && model.patientBooking.birthDate != null) {
            (<any>this.model).patientBooking.birthDateObj = convertMomentDateToStruct(moment(model.patientBooking.birthDate));
        }

        //        ( <any>this.model ).assignedParking = { description: model.assignedParkingCode };

        if (this.componentService.getHeaderComponent('headerComponent')) {
            // passo l'oggetto all'header
            (<any>this.componentService.getHeaderComponent('headerComponent')).fromItemToBannerModel(model);
        }

        //lockswitchReturnDateSetting
        this.settingSelectedTipo();
        this.settingLockSwitchReturnDate();
    }

    showDuplicates(value) {

        let modal = this.modalService.open(CheckDuplicateModalComponent, { size: 'lg' });

        modal.componentInstance.list = value;
        modal.componentInstance.title = 'Prenotazioni duplicate';
        modal.componentInstance.columns = [
            { title: 'Codice', name: 'code' },
            { title: 'Data', name: 'transportDate' },
            { title: 'Trasportato', name: 'trasportato' },
            { title: 'Indirizzo Partenza', name: 'startAddress' },
            { title: 'Stato', name: 'status' }
        ];
        return modal;


    }

    saveBooking(content) {
        this.componentService.getRootComponent().mask();

        this.calculateReturnDate(false, true);
        if (this.model.source && this.model.source == 'C') {
            //se provengo da una ciclica non controllo la validità dei campi
        } else {
            if (!this.checkValid()) {
                this.componentService.getRootComponent().unmask();
                return
            };
        }

        let modelToSave = this.copyAndClean(this.model)
        /*if (modelToSave.code && modelToSave.code.length > 0) {
            this.proceedWithSaveBooking(modelToSave);
            return;
        }*/

        //Source decode for save bookingDTO
        if (modelToSave.source && modelToSave.source != 'S' && modelToSave.source != 'C') {
            modelToSave.source = 'O';
        }

        let checkDuplRequest: BookingDuplicateSearchDTO = { bookingDto: <BookingDTO>modelToSave, visualizeAll: false };
        checkDuplRequest.visualizeAll = false;

        this.bookingService.getDuplicateBooking(checkDuplRequest).catch((err, c) => {
            if (isManagedError(err)) {
                return;
            }

            let message = 'Errore nella verifica della presenza di prenotazioni duplicate';
            //            if ( err && err.message ) {
            //                message = err.message;
            //            }
            this.componentService.getRootComponent().unmask();
            this.componentService.getRootComponent().showToastMessage('error', message);
            return Observable.throw(err);
        }).subscribe(res => {
            // console.log("prenotazioni duplicate individuate: " + result);
            this.componentService.getRootComponent().unmask();
            if (res && res.resultList && res.resultList.length > 0) {
                let modal = this.showDuplicates(res.resultList);
                modal.result.then((r) => {
                    if (r) {
                        // console.log("risultato modale: " + r);
                        modelToSave.duplicable = true;
                        this.proceedWithSaveBooking(modelToSave);
                    }
                }, (reason) => {
                    if (reason) {
                        // console.log("reason modale: " + reason);
                    }
                });
                // return per delegare tutto a quello che restituisce la modale
                return;
            } else {
                this.proceedWithSaveBooking(modelToSave);
            }
        })

    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    checkValid() {

        /*   if (!this.checkTipo(this.selectedTipo)) {
               return;
           }*/
        //faccio trim per nome e cognome paziente
        if (this.model.patientBooking) {
            this.model.patientBooking.name = this.model.patientBooking.name ? this.model.patientBooking.name.trim() : null;
            this.model.patientBooking.surname = this.model.patientBooking.surname ? this.model.patientBooking.surname.trim() : null;
        }

        if (!this.prenotazioniFG.valid) {
            let message = '';

            for (const key in this.prenotazioniFG.controls) {
                if (this.prenotazioniFG.controls.hasOwnProperty(key)) {
                    const value = this.prenotazioniFG.controls[key];
                    if (!(!this.dataRitornoFlag && (key == "returnDate" || key == "hourReturn"))) {
                        if (value.errors && value.errors['message']) {
                            message += value.errors['message'] + '#$# ';
                        }
                    } else {
                        console.log("Check return date e hour quando non serve");
                    }
                }
            }

            // check data di nascita
            if (this.model && this.model.patientBooking && (<any>this.model).patientBooking.birthDateObj != null) {
                let now = moment();
                now.milliseconds(0);
                now.seconds(0);
                now.minutes(0);
                now.hours(0);
                let birthDate = moment(convertToDate((<any>this.model).patientBooking.birthDateObj, null));
                birthDate.milliseconds(0);
                birthDate.seconds(0);
                birthDate.minutes(0);
                birthDate.hours(0);
                if (birthDate.isAfter(now)) {
                    message += 'La data di nascita non può essere superiore alla data odierna' + '#$# ';
                } else if (!birthDate.isValid()) {
                    message += 'Data di nascita non valida' + '#$# ';
                }
            }

            if (message.length > 0)
                message = message.slice(0, -4);
            //this.componentService.getRootComponent().showToastMessage( 'warning', message );
            //this.sanitize(message);
            this.componentService.getRootComponent().showModal("Attenzione verificare dati inseriti", cleanMessageList(message.split('#$#')));
            this.componentService.getRootComponent().unmask();
        }
        return this.prenotazioniFG.valid;
    }

    //////////////////////
    // Inizio metodi funzionali
    proceedWithSaveBooking(content) {
        /*if (this.prenotazioniFG.valid) {*/

        this.componentService.getRootComponent().mask();
        // effettuo il clone del model, pulendo gli eventuali campi di appoggio
        // let modelToSave = this.copyAndClean(this.model)
        if (this.serviceId && this.serviceId.length > 0) {
            //Salvataggio della prenotazione e del servizio relativo
            let bookingService: UpdateBookingServiceDTO = {
                booking: content,
                serviceId: this.serviceId
            }
            this.transportService.updateBookingService(bookingService).catch((err, c) => {
                if (isManagedError(err)) {
                    return;
                }
                console.log("Errore nel salvataggio prenotazione e del servizio connesso: " + err + " - " + c);
                let message = 'Errore nel salvataggio prenotazione e del servizio connesso';

                //Send stromo update activation


                this.componentService.getRootComponent().unmask();
                this.componentService.getRootComponent().showToastMessage('error', message);
                return Observable.throw(err);
            }).subscribe((result: UpdateBookingServiceDTO) => {
                //Aggiorno modello bck
                this.modelBck = _.cloneDeep(result.booking);

                this.copyToModel(result.booking);
                this.componentService.getRootComponent().unmask();
                this.componentService.getRootComponent().showToastMessage('success', "Salvataggio prenotazione '" + result.booking.code + "' avvenuto con successo");
                this.componentService.getRootComponent().showConfirmDialog('Avviso', "Aggiornata la prenotazione '" + result.booking.code + "' ed il servizio '" + this.serviceCode + "' connesso!\n Spostarsi nella gestione del servizio?").then((result) => {
                    this.router.navigate(['/gestione-servizi', this.serviceId]);
                }, (reason) => {
                    console.log("Resta alla modifica della prenotazione");
                });
            });
        } else {
            //Salvo la sola prenotazione
            this.bookingService.saveBooking(content, null).catch((err, c) => {
                if (isManagedError(err)) {
                    return;
                }
                console.log("Errore nel salvataggio prenotazione: " + err + " - " + c);
                let message = 'Errore nel salvataggio prenotazione';
                if (err && err.message) {
                    message = err.message;
                }
                this.componentService.getRootComponent().unmask();
                this.componentService.getRootComponent().showToastMessage('error', message);
                return Observable.throw(err);
            }).subscribe((result: GenericResultDTOBookingDTO) => {
                // se il server ha restituito una lista di messaggi, vuol dire che è avvenuto un errore, di conseguenza visualizzo i toast con i messaggi restituiti
                this.componentService.getRootComponent().unmask();
                if (result.msgList && result.msgList.length > 0) {
                    result.msgList.forEach(m => {
                        if (m.messageType === 'ERROR') {
                            //   this.toastr.error(m.message, '', { showCloseButton: true, positionClass: 'toast-bottom-right' });
                        } else {
                            //  this.toastr.warning(m.message, '', { showCloseButton: true, positionClass: 'toast-bottom-right' });
                        }
                    });
                } else {
                    //Aggiorno modello bck
                    this.modelBck = _.cloneDeep(result.result);
                    // se tutto è andato a buon fine, inserisco l'oggetto restituito sulla maschera                    
                    this.copyToModel(result.result);
                    this.currentId = this.model.id;
                    this.componentService.getRootComponent().showToastMessage('success', "Salvataggio prenotazione '" + result.result.code + "' avvenuto con successo");
                    //Richiesta Paolo redirect to sinottico quando salvo nuovo booking
                    //if(content.id==null){
                    //    this.router.navigate(['/sinottico-prenotazioni', this.model.id]);
                    //}
                }
            });
        }
    }

    public sendStormoUpdateActivation(servId: string) {
        //invia stormo activation
        let req = { serviceId: servId };
        this.stormoService.updateActivation(req).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
            return [];
        }).subscribe(res2 => {
            if (AcknowledgeResp.OutEnum.ACK != res2.out) {
                this.componentService.getRootComponent().showToastMessage('error', "Non e' stato possibile aggiornare il servizio \na tutte le unita' mobili impegnate su di esso.");
            }
        });
    }

    /*private async rebuiltServiceBooking(serviceIdParam: string, currentIdParam:string){
        let model={serviceId:serviceIdParam , bookingId:currentIdParam};
        //TODO LUCA --- richiama servizio rebuilt
        let ret = await this.rebuiltServiceForBookingModify(model);
        if (ret) {
            this.componentService.getRootComponent().showToastMessage('success', "Aggiornato il servizio'"+ret.code+"' connesso!" );            
        } else {
            console.warn("Rebuilt service problem");
        }
    }

    public async rebuiltServiceForBookingModify(model: any): Promise<any> {
        return this.transportService.rebuiltServiceForBookingModify(model).toPromise();
    }*/

    public search(): void {
        this.onSearching.emit(true);
        //   this.voted = true;
    }

    
    async reloadBooking(){
        let rsp: boolean = await this.hasModificationOnDestroy();
        if (rsp) {
            try {
                await this.componentService.getRootComponent().showConfirmDialog("Attenzione!", "Sono state effettuate delle modifiche alla prenotazione.\nSi desidera continuare con l'operazione perdendo tali dati?");
                this.refreshBooking();
            } catch (err) {
                return;
            }
        } else {
            this.refreshBooking();
        }
    }
    
    async cleanBooking() {       
        this.newFunction();
        this.model = this.newModel();
        this.selectedTipo = {};
        // chiamo il metodo sull'header senza passare nulla, in questo modo pulisco i dati caricati in alto
        (<any>this.componentService.getHeaderComponent('headerComponent')).fromItemToBannerModel();
        this.initDefaultValueSetting();
    }

    saveAddress(address: AddressBookingDTO, type: 'start' | 'end') {
        if (type === 'start') {
            // se è stata aperta per l'andata:
            this.model.startAddress = address;
            this.startAddressModify = false;
        } else {
            // altrimenti è il ritorno
            this.model.endAddress = address;
            this.endAddressModify = false;
        }
        //remove conv value setted
        //if(address.authority==null)
        if (this.model.req.authority && this.model.req.authority.description && this.model.req.authority.description == this.model.startAddress.authority.description)
            this.model.req.convention = this.model.startAddress.authority.convention;
        else if (this.model.req.authority && this.model.req.authority.description && this.model.req.authority.description == this.model.endAddress.authority.description)
            this.model.req.convention = this.model.endAddress.authority.convention;
        else
            this.model.req.convention = undefined;

    }

    searchAuthority(type?: 'start' | 'end') {
        // passo i dati alla finestra
        if (!type) {
            this.toggleStartAddress(false);
            this.toggleEndAddress(false);
        } else if (type === 'start') {
            // se è stata aperta per l'andata:
            if (!this.toggleEndAddress(false)) {
                this.toggleStartAddress(!this.startAddressModify);
            }
        } else {
            // altrimenti è il ritorno
            if (!this.toggleStartAddress(false)) {
                this.toggleEndAddress(!this.endAddressModify);
            }
        }
    }

    private toggleStartAddress(toggle: boolean): boolean {
        if (!toggle && this.startAddressModify) {
            this.startAddressModify = !this.startAddressComponent.close();
        } else {
            this.startAddressModify = toggle;
        }
        return this.startAddressModify;
    }

    private toggleEndAddress(toggle: boolean): boolean {
        if (!toggle && this.endAddressModify) {
            this.endAddressModify = !this.endAddressComponent.close();
        } else {
            this.endAddressModify = toggle;
        }
        return this.endAddressModify;
    }


    public async viewAddressDetail(typeAdd: 'start' | 'end' | '') {
        // passo i dati alla finestra
        let requestAddressDetail: AddressDetailMapRequest;
        let add: AddressBookingDTO;

        let addrDetails: AddressDetailResponse[] = new Array<AddressDetailResponse>();
        let index = 0;
        this.componentService.getRootComponent().mask();
        if (typeAdd === 'start' || typeAdd == '') {
            // se è stata aperta per l'andata:
            // Passo i dati dell'indirizzo di partenza per recuperate eventuali note
            if (this.model.startAddress && this.model.startAddress.street && this.model.startAddress.street.name && this.model.startAddress.street.name.trim().length > 0) {
                let req = this.createRequestAddressDetail(this.model.startAddress, 'start');
                let startAddrDetail = await this.searchBookingAddressDetail(req);
                if (startAddrDetail) {
                    addrDetails[index] = startAddrDetail;
                    index++;
                }
            }
        }
        if (typeAdd === 'end' || typeAdd == '') {
            if (this.model.endAddress && this.model.endAddress.street && this.model.endAddress.street.name && this.model.endAddress.street.name.trim().length > 0) {
                // se è stata aperta per l'andata:
                // Passo i dati dell'indirizzo di ritorno per recuperate eventuali note
                let req = this.createRequestAddressDetail(this.model.endAddress, 'end');
                let endAddrDetail = await this.searchBookingAddressDetail(req);
                if (endAddrDetail) {
                    addrDetails[index] = endAddrDetail;
                }
            }
        }
        this.showAddressBookingDetail(addrDetails);
    }

    public createRequestAddressDetail(add: AddressBookingDTO, typeAdd: string): AddressDetailMapRequest {
        let requestAddressDetail: AddressDetailMapRequest = {
            map: {
                [typeAdd]: {
                    municipalityName: add.municipality ? add.municipality.name : null,
                    provinceName: add.province ? add.province.name : null,
                    localityName: add.locality ? add.locality.name : null,
                    streetName: add.street ? add.street.name : null
                }
            }
        }
        return requestAddressDetail;
    }

    protected async searchBookingAddressDetail(req: AddressDetailMapRequest): Promise<AddressDetailResponse> {
        this.componentService.getRootComponent().mask();
        // La risposta del servizio rest e' una lista di OverviewBookingDTO
        try {
            let result = this.streetService.showBookingAddressDetail(req).toPromise();
            return result;
        } catch (err) {
            return null;
        }
    }

    protected showAddressBookingDetail(resp: AddressDetailResponse[]) {
        //console.log(value);
        //let valueElement:AddressDetail;

        let value: AddressDetail[] = new Array<AddressDetail>();
        resp.forEach(element => {
            if (element.responseMap && element.responseMap["start"]) {
                value[0] = element.responseMap["start"];
                //valueElement = element.responseMap["start"]; 
            } else if (element.responseMap && element.responseMap["end"]) {
                value[1] = element.responseMap["end"];
                //valueElement = element.responseMap["end"];
            }
        });


        let modal = this.modalService.open(AddressBookingDetailModal, { size: 'lg' });
        modal.componentInstance.startAddress = modal.componentInstance.endAddress = {
            streetName: '', localityName: '', municipalityName: '', provinceName: '',
            municipalityId: '', localityId: '', streetId: '', localityNote: '', streetNote: ''
        };

        if (value && value.length > 0) {
            let elNum = value.length;
            modal.componentInstance.startAddress = value[0];
            if (elNum = 2) {
                modal.componentInstance.endAddress = value[1];
            }
        }
        this.componentService.getRootComponent().unmask();

        /*let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });

        modal.componentInstance.list = valueElement;
        modal.componentInstance.title = 'Dettaglio indirizzo';
        modal.componentInstance.columns = [

            { title: 'Indirizzo', name: 'streetName', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Note indirizzo', name: 'streetNote', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Località', name: 'localityName', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Note Localita', name: 'localityNote', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Comune', name: 'municipalityName', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } }
        ];  */
    }

    sendAuthorityToReq(type: 'start' | 'end') {
        // passo i dati alla finestra        
        if (type === 'start') {
            // se è stata aperta per l'andata:
            // Passo i dati dell'indirizzo per il richiedente
            this.model.req.telephone = this.model.startAddress.phoneNumber;
            if (this.model.startAddress.authority != null) {
                this.model.req.authority = { description: this.model.startAddress.authority.description };
                this.model.req.description = this.model.startAddress.authority.description;
                this.model.req.cc = this.model.startAddress.authority.costCenter;
                this.model.req.convention = this.model.startAddress.authority.convention;
                this.model.req.type = this.model.startAddress.authority.type;
                this.model.req.vat = this.model.startAddress.authority.vat;
            } else {
                this.model.req = { convention: null };
            }

            if (this.model.startAddress.department != null) {
                this.model.req.department = { description: this.model.startAddress.department.description };
                //this.model.req.detail = this.model.startAddress.department.description;
            }
        } else {
            // altrimenti è il ritorno
            // Passo i dati dell'indirizzo per il richiedente
            this.model.req.telephone = this.model.endAddress.phoneNumber;
            if (this.model.endAddress.authority != null) {
                this.model.req.authority = { description: this.model.endAddress.authority.description };
                this.model.req.description = this.model.endAddress.authority.description;
                this.model.req.cc = this.model.endAddress.authority.costCenter;
                this.model.req.convention = this.model.endAddress.authority.convention;
                this.model.req.type = this.model.endAddress.authority.type;
                this.model.req.vat = this.model.endAddress.authority.vat;
            } else {
                this.model.req = { convention: null };
            }

            if (this.model.endAddress.department != null) {
                this.model.req.department = { description: this.model.endAddress.department.description };
                //this.model.req.detail = this.model.endAddress.department.description;
            }
        }
        this.componentService.getRootComponent().showToastMessage('success', 'Dati trasferiti sul richiedente');

    }

    private getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }

    getRootComponent() {
        return this.componentService.getRootComponent();
    }

    public async hasModificationOnDestroy(): Promise<boolean> {
        let modelBckCompleteExt = this.modelBck ? JSON.parse(JSON.stringify(this.modelBck)) : this.modelBck;
        if (modelBckCompleteExt) {
            /*modelDepurateText.equipmentList.map(
                el => {
                    let newEl: any = Object.assign({}, el);
                    newEl.text = el.name;
                    return newEl;
                }
            );*/
            if (modelBckCompleteExt.equipmentList) {
                let array = modelBckCompleteExt.equipmentList;
                for (var i in array) {
                    array[i].text = array[i].name;
                }
            }

            if (modelBckCompleteExt.req && modelBckCompleteExt.req && modelBckCompleteExt.req.authority) { //&& modelBckCompleteExt.req.authority.description
                modelBckCompleteExt.req.authority.text = modelBckCompleteExt.req.authority.id = modelBckCompleteExt.req.authority.description;
            }
            if (modelBckCompleteExt.req && modelBckCompleteExt.req && modelBckCompleteExt.req.department) { //&& modelBckCompleteExt.req.department.description
                modelBckCompleteExt.req.department.text = modelBckCompleteExt.req.department.id = modelBckCompleteExt.req.department.description;
            }

        }

        if (!modelBckCompleteExt && this.currentId) {
            //refresh
            modelBckCompleteExt = this.model;
        } else if (!modelBckCompleteExt) {
            //new booking
            modelBckCompleteExt = this.newModel();
            (<any>modelBckCompleteExt).transportDateObj = convertMomentDateToStruct(moment());
            modelBckCompleteExt.priority = "NO";
        }

        if (modelBckCompleteExt && this.currentId) {
            //after modify -- mantenere la sequenza delle valorizzazioni per il confronto come stringa
            (<any>modelBckCompleteExt).transportDateObj = convertMomentDateToStruct(moment(modelBckCompleteExt.transportDate));
            if (modelBckCompleteExt.returnDate) {
                (<any>modelBckCompleteExt).returnDateObj = convertMomentDateToStruct(moment(modelBckCompleteExt.returnDate));
            }
            (<any>modelBckCompleteExt).transportTime = convertToString(modelBckCompleteExt.transportDate);
            if (modelBckCompleteExt.returnDate) {
                (<any>modelBckCompleteExt).returnTime = convertToString(modelBckCompleteExt.returnDate);
            }
        }

        if (modelBckCompleteExt && JSON.stringify(modelBckCompleteExt) != JSON.stringify(this.model)) {
            return true;
        } else {
            return false;
        }
    }

    ngOnDestroy() {
        super.ngOnDestroy();
        //console.log('unsubscribed: ' + this.currSubscription.observ$.unsubscribe());
        this.currSubscription.observ$.unsubscribe();
        this.servSubscription.observ$.unsubscribe();
        this.sendMessageToSinottico(null);
    }

    duplicateSingleBooking() {
        this.componentService.getRootComponent().showConfirmDialog('Attenzione!', 'Duplicare la prenotazione corrente?').then((result) => {
            if (this.model.source && this.model.source == 'C') {//se provengo dalla ciclica annullo il source in modo da far duplicare la prenotazione
                this.model.source = null;
            }

            this.model.code = null;
            this.model.id = null;
            this.model.bookingId = null;
            this.model.ciclicalId = null;
            this.model.duplicable = true;
            this.model.creationDate = null;
            this.model.creationUser = null;
            //this.model.assignedParking = null;
            this.model.assignedParkingCode = null;
            //this.model.optionedParking = null;
            //this.model.optionedParkingCode = null;
            this.disableSave = false;
            this.serviceId = null;
            (<any>this.componentService.getHeaderComponent('headerComponent')).fromItemToBannerModel(this.model);
            this.componentService.getRootComponent().showToastMessage('success', 'Prenotazione duplicata in locale. \nProcedere con le eventuali modifiche ed al salvataggio sul server.');
        }, (reason) => {
            console.log("Duplicazione non effettuata " + this.getDismissReason(reason));
        });
    }

    updateParkingList(name?: string) {
        let filter = { name: name ? name.toUpperCase() : '' };
        if (filter.name === '') {
            filter.name = '%';
        }
        //this.componentService.getRootComponent().mask();
        this.parkingService.getParkingCompact(filter.name).subscribe(list => {
            this.parkingItems.next(list);
            //this.componentService.getRootComponent().unmask();
        });
    }

    protected decodeName2Sd(type: string, name: string) {
        if (!localStorage.getItem('staticDataMap')) {
            return {};
        }
        let values: Value[] = JSON.parse(localStorage.getItem('staticDataMap'))[type];
        return _.find(values, (o: Value) => {
            return o.name === name;
        });
    }

    protected decodeActionSynchForMessage(action: string) {
        let actionDec = "";
        if (action) {
            switch (action) {
                case "DELETE":
                    actionDec = "cancellata";
                    break;
                case "REMOVE":
                    actionDec = "associata a servizio";
                    break;
                case "SWITCH_TO_BOOKING":
                    actionDec = "riportata in coda prenotazione";
                    break;
                case "SWITCH_TO_RETURN":
                    actionDec = "riportata in cdoda ritorni";
                    break;
                case "ARCHIVE":
                    //se non è presente sul sinottico non faccio nulla
                    actionDec = "archiviata";
                    break;
                case "UPDATE":
                // questo evento arriva sia su un update che su una creazione, 
                // faccio le stesse cose sull'alert
                case "ALERT":
                    actionDec = "aggiornata";
                    break;
                default:
                    actionDec = "AGGIORNATA";
                    break;
            }
        }
        return actionDec;
    }

    protected checkTipo(selectedType: Value) {
        /*console.log("Selected type is: " + JSON.stringify(selectedType));*/
        this.selectedTipo = selectedType;
        if (this.selectedTipo && this.selectedTipo.extraInfo.isGenerateReturnTransportType) {
            this.dataRitornoSwitch = true;
        } else {
            this.dataRitornoSwitch = false;
        }
        return this.calculateReturnDate(true, false);

    }

    protected checkFase(selectedPhase: Value) {
        /*console.log("Selected type is: " + JSON.stringify(selectedType));*/
        if (selectedPhase && selectedPhase.name == '2') {
            this.model.hourTransport = null;
        }
    }

    protected calculateReturnDate(showMessage: boolean, notRitornoFlag: boolean) {
        if (this.dataRitornoFlag && notRitornoFlag) {
            return;
        }

        let _innerModel: any = this.model;

        this.settingSelectedTipo();

        this.settingLockSwitchReturnDate();
        if (this.selectedTipo && this.selectedTipo.extraInfo && _innerModel.transportDateObj) {
            if (this.selectedTipo.extraInfo.isGenerateReturnTransportType || (this.dataRitornoSwitch && !this.dataRitornoFlag)) {
                this.settingReturnDateAndHour(_innerModel, showMessage);
                this.dataRitornoSwitch = true;
                return true;
            } else {
                //remove date time from return date
                _innerModel.returnDateObj = undefined;
                _innerModel.hourReturn = undefined;
                this.dataRitornoSwitch = false;
            }
        }
        return true;
    }

    private settingSelectedTipo() {
        if (!this.selectedTipo &&
            this.model.transportType &&
            this.tipoItems &&
            (<any>this.tipoItems).source &&
            (<any>this.tipoItems).source.value) {

            (<any>this.tipoItems).source.value.forEach(element => {
                if (element.name === this.model.transportType) {
                    this.selectedTipo = element;
                }
            });

        }
    }

    private settingLockSwitchReturnDate() {
        if (this.selectedTipo && this.selectedTipo.extraInfo && this.selectedTipo.extraInfo.isGenerateReturnTransportType) {
            this.dataRitornoSwitchLocked = true;
        } else {
            this.dataRitornoSwitchLocked = false;
        }
    }

    private settingReturnDateAndHour(_innerModel: any, showMessage: boolean) {
        let d: Date = convertToDate(_innerModel.transportDateObj, _innerModel.hourTransport);
        let m: moment.Moment = moment(d);

        if (_innerModel.hourTransport) {
            m.add(1, 'm');
        }

        _innerModel.returnDateObj = convertMomentDateToStruct(m);

        if (!_innerModel.hourTransport) {
            m = moment();
        }
        _innerModel.hourReturn = convertToString(m.toDate()); //model.returnTime
        if (showMessage) {
            if (this.dataRitornoFlag) {
                this.componentService.getRootComponent().showToastMessage('success', 'Data di ritorno compilata in automatico. Verificare campo.');
            } else {
                this.componentService.getRootComponent().showToastMessage('success', 'Ritorno attivato.');
            }
        }
    }

    public searchLastReservations(val: any) {
        this.componentService.getRootComponent().mask();
        // La risposta del servizio rest e' una lista di OverviewBookingDTO
        this.bookingService.getUltimePrenotazioni().subscribe(list => {
            console.log(list);
            this.componentService.getRootComponent().unmask();
            this.setListaUltimePrenotazioni(list);

        });
    }


    get modelOverviewBooking(): OverviewBookingDTO {
        if (!this._modelOverviewBooking) {
            this._modelOverviewBooking = <OverviewBookingDTO>{};
        }
        return this._modelOverviewBooking;
    }
    /*

    // TODO implementare get e set
    get model(): AddressBookingDTO {
        if (!this._model) {
            this._model = <AddressBookingDTO>{};
        }
        return this._model;
    }
    @Input('address') set model(model: AddressBookingDTO) {
        this._model = JSON.parse(JSON.stringify(model));
    }


    */


    protected setListaUltimePrenotazioni(value: Array<OverviewBookingDTO>) {

        //console.log(value);
        /*if (value && value.length === 1) {
            this.setAuthority(value[0]);
            return;
        }*/

        let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });

        modal.componentInstance.list = value;
        modal.componentInstance.title = 'Prenotazioni gestite nelle ultime ore';
        modal.componentInstance.columns = [

            { title: 'Codice', name: 'code', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Data', name: 'transportDate', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Tipo', name: 'transportType', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Deambulazione', name: 'patientStatus', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Convenzione', name: 'convention', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Indirizzo partenza', name: 'startAddress', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Indirizzo destinazione', name: 'endAddress', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Trasportato', name: 'denominazionePaziente', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Postazione', name: 'postazione', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Doc', name: 'doctorName', style: { width: "10%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } }
        ];


        modal.result.then((result) => {

            if (result) {

                this.copyToModel(this.newModel());

                //this.router.navigate(['/modifica-prenotazione', result.id]);
                this.currentId = result.id;
                if (this.currentId) {
                    this.componentService.getRootComponent().mask();
                    this.bookingService.getBookingById(this.currentId).subscribe(val => {

                        val.result.startAddress = {};
                        val.result.endAddress = {};
                        val.result.code = null;
                        val.result.id = null;
                        val.result.creationUser = null;
                        val.result.creationDate = null;
                        val.result.returnDate = null;
                        this.copyToModel(val.result);
                        this.componentService.getRootComponent().unmask();
                    });
                }





                // this.componentService.getRootComponent().mask();

                // this.model.patientStatus=result.patientStatus;
                // this.model.transportDate=result.transportDate;
                // this.model.transportType=result.transportType;

                // this.model.phase=result.phase;
                // this.model.priority=result.priority;

                // this.model.returnDate=result.returnDate;
                // this.model.endAddress=result.endAddress;

                // this.model.patientBooking.name=result.patientName;
                // this.model.patientBooking.surname=result.patientSurname;
                // this.model.patientBooking.patientTel=result.patientTel;
                // this.model.patientBooking.sanitaryCode=result.sanitaryCode;

                // this.componentService.getRootComponent().unmask();
            }
        }, (reason) => {
        });


    }

    onChangeTransp(event) {
        //this.checkPersonOrOther();
    }

    /*checkPersonOrOther() {
        this.personOrOther = null;
        let nameControl = this.prenotazioniFG.get('name');
        let surnameControl = this.prenotazioniFG.get('surname');
        let bloodControl = this.prenotazioniFG.get('blood');
        let variousDetailControl = this.prenotazioniFG.get('variousDetail');

        let name = this.model.patientBooking.name;
        let surname = this.model.patientBooking.surname;//this.prenotazioniFG.get('surname');
        let blood = this.prenotazioniFG.get('blood');
        let variousDetail = this.prenotazioniFG.get('variousDetail');
        if ((!this.bloodValue ) && (variousDetail.value == undefined ||variousDetail.value.length==0) ) {
            if ((name== undefined || name.length==0) && (surname == undefined || surname.length==0) ) {
                surnameControl.setValidators([Validators.required]);
                nameControl.setValidators([Validators.required]);
                bloodControl.setValidators([Validators.required]);
                variousDetailControl.setValidators([Validators.required]);
                return;
            } else {
                blood.setValidators(null);
                variousDetail.setValidators(null);
                if ((name!=null && name.length>0) && (surname == undefined || surname.length==0) ) {
                    surnameControl.setValidators([Validators.required]);
                    nameControl.setValidators(null);
                    bloodControl.setValidators(null);
                    variousDetailControl.setValidators(null);
                    //this.prenotazioniFG.setErrors({'surname':true});
                    return;
                }
                if ((surname != null && surname.length>0) && (name == undefined || name.length==0) ) {
                    nameControl.setValidators([Validators.required]);
                    surnameControl.setValidators(null);
                    bloodControl.setValidators(null);
                    variousDetailControl.setValidators(null);
                    //this.prenotazioniFG.setErrors({'name':true});
                    return;
                }
            }
        } else {
            if ((name!=null && name.length>0) && (surname == undefined || surname.length==0)) {
                surnameControl.setValidators([Validators.required]);
                nameControl.setValidators(null);
                bloodControl.setValidators(null);
                variousDetailControl.setValidators(null);
                //this.prenotazioniFG.setErrors({'surname':true});
                return;
            }
            if ((surname != null && surname.length>0) && (name == undefined || name.length==0)) {
                nameControl.setValidators([Validators.required]);
                surnameControl.setValidators(null);
                bloodControl.setValidators(null);
                variousDetailControl.setValidators(null);
                //this.prenotazioniFG.setErrors({'name':true});
                return;
            }            
        }
        nameControl.setValidators(null);
        surnameControl.setValidators(null);
        bloodControl.setValidators(null);
        variousDetailControl.setValidators(null);
        this.personOrOther = 'ok';
    } */

    onChangeBlood(event) {
        this.bloodValue = !this.bloodValue;
        //this.checkPersonOrOther();
    }

    onChangeReturnSwitch(event) {
        this.dataRitornoSwitch = !this.dataRitornoSwitch;
        let _innerModel: any = this.model;
        if (this.dataRitornoSwitch) {
            this.calculateReturnDate(true, false);
        } else {
            _innerModel.returnDateObj = undefined;
            _innerModel.hourReturn = undefined;
        }
    }

    private newFunction() {
        this.model = this.newModel();
    }
    /*
        searchLastReservations(val: any) {
            
            console.log("searchLastBooking");
            let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });
            
            // modal.componentInstance.list = value;
            modal.componentInstance.title = 'Prenotazioni gestite nelle ultime ore';
            
            modal.componentInstance.columns = [
                { title: 'Tipo Ente', name: 'type', style: { width: "50%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
                { title: 'Descrizione', name: 'compact', style: { width: "50%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
                
            ];
            
            // La risposta del servizio rest e' una lista di OverviewBookingDTO
            this.bookingService.getUltimePrenotazioni().subscribe(list => {
                console.log(list);
                modal.result.then((result) => {
                                //console.log("modal result is: " + result);
                    
                                if (result) {
                                    this.componentService.getRootComponent().mask();             
                                }
                            }, (reason) => {});
                    
            });
    
            
                    this.componentService.getRootComponent().unmask();
            
        }
    */


    public incrementHour(hour: string): string {
        let h = moment(hour, "HH:mm");
        if (!h.isValid()) {
            h = moment();
        } else {
            h.add(15, 'm');
        }
        return h.format("HH:mm");
    }

    public decrementHour(hour: string): string {
        let h = moment(hour, "HH:mm");
        if (!h.isValid()) {
            h = moment();
        } else {
            h.add(-15, 'm');
        }
        return h.format("HH:mm");
    }

    public openPatientArchiveSearch() {
        let modal = this.modalService.open(RicercaPazienteModalComponent, { size: 'lg', windowClass: 'ricerca-paziente-modal' });
        modal.componentInstance.title = 'Pazienti Trasportati';
        let patientBooking = Object.assign({}, this.model.patientBooking); // clona oggetto
        modal.componentInstance.model = this.fromPatientBookingToPatientArchive(patientBooking);
        modal.result.then((patient: PatientArchiveDTO) => {
            if (patient) {
                let patientBooking: PatientBookingDTO = {};
                patientBooking.name = patient.name;
                patientBooking.surname = patient.surname;
                patientBooking.patientTel = patient.telephone;
                if (patient.taxCode) {
                    patientBooking.sanitaryCode = patient.taxCode;
                } else {
                    patientBooking.sanitaryCode = patient.sanitaryCode;
                }
                patientBooking.additionalPatientId = patient.additionalPatientId;
                patientBooking.taxCode = patient.taxCode;
                patientBooking.sex = patient.sex;
                patientBooking.birthDate = patient.birthDate;

                if (patient.birthDate != null) {
                    (<any>patientBooking).birthDateObj = convertMomentDateToStruct(moment(patient.birthDate));
                }

                patientBooking.municipality = patient.municipality;
                patientBooking.locality = patient.locality;
                patientBooking.streetName = patient.streetName;
                patientBooking.houseNumber = patient.houseNumber;
                patientBooking.province = patient.province;
                patientBooking.ticket = patient.ticket;

                this.model.patientBooking = patientBooking;
            }
        });
    }

    private fromPatientArchiveToPatientBooking(p: PatientArchiveDTO): PatientBookingDTO {
        let pb: PatientBookingDTO = p;
        if (p) {
            pb.patientTel = p.telephone;
        }

        return pb;
    }

    private fromPatientBookingToPatientArchive(pb: PatientBookingDTO): PatientArchiveDTO {
        let p: PatientArchiveDTO = pb;
        if (pb) {
            p.telephone = pb.patientTel;
        }
        return p;
    }


    public sendPatientAddress(type: 'start' | 'end') {

        let patient = this.model.patientBooking;
        let address: AddressBookingDTO = {
            municipality: { name: patient.municipality },
            province: { name: patient.province },
            locality: { name: patient.locality },
            street: { name: patient.streetName },
            civicNumber: patient.houseNumber
        };

        if (type === 'start') {
            this.model.startAddress = address;
            this.componentService.getRootComponent().showToastMessage('success', "Assegnato a 'Partenza' l'indirizzo del paziente");
        } else {
            this.model.endAddress = address;
            this.componentService.getRootComponent().showToastMessage('success', "Assegnato a 'Destinazione' l'indirizzo del paziente");
        }
    }

    public hasPatientAddress(): boolean {
        return (this.model &&
            this.model.patientBooking &&
            !!this.model.patientBooking.municipality &&
            !!this.model.patientBooking.province);
    }

    public printBookingPdf() {
        this.bookingService.getPdf(this.model.code).subscribe( resp => {
            let fileURL = URL.createObjectURL(resp);
            window.open(fileURL);
        });
    }

}
