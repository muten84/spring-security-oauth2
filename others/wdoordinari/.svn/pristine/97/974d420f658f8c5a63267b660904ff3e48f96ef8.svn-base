import {
    Component, ViewEncapsulation,
    OnInit, ViewChild, OnDestroy
} from "@angular/core";
import { ComponentHolderService } from "app/service/shared-service";
import {
    StormoModuleServiceService, TransportModuleServiceService, VehicleDirectAssignmentFilter, AcknowledgeResp,
    AvailableParkingVehicleTurnDTO, VehicleDirectAssignmentDTO,
    ServiceDTO, ParkingServiceService, Value, TsServiceResourceDTO,
    InterventionDTO, RequestInterventionUnifiedService, InterventionActivityDTO,
    MoveInterventionPositionRequestDTO, MoveInterventionRequestDTO, CheckResultDTO, GenericResultDTO, VehiclePositionToChangeRequest
} from "services/services.module";
import { Router, ActivatedRoute } from "@angular/router";
import { GestioneServiziHeaderComponent } from "app/gestione-servizi/gestione-servizi-header.component";
import { Observable, Subject } from "rxjs";
import { StaticDataService } from "app/static-data/cached-static-data";
import { valueToSelect } from "app/util/convert";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ChiusuraMezzoModal } from "app/gestione-servizi/chiusura-mezzo.modal";
import * as moment from 'moment';
import { CoreTableColumn, CoreTableComponent, execute } from "app/core/table/core-table/core-table.component";
import { VehicleDetailComponent } from "app/gestione-servizi/vehicle-detail.component";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { BookingModuleServiceService } from 'services/services.module';
import { ServiceCommon } from "app/common-servizi/service-common";
import { MessageService } from "app/service/message.service";
import { TokenService } from '../service/token.service';
import { EVENTS } from "app/util/event-type";
import { ServiziService } from "../service/servizi-service";
import { BrowserCommunicationService } from "common/services/browser-communication.service";
import { Message, SelectServiceInSinottico, Type } from "common/services/messages";
import { environment } from "environments/environment";
import { TextModalComponent } from "../modals/text-modal/text-modal-component";
import { CrewMemberModalComponent } from "../modals/crew-member-modal/crew-member-modal-component";
import { MobileUnitsModalComponent } from "../modals/mobile-units-modal/mobile-units-modal-component";
import { LocalDataService } from "../service/local-data-service";
import { RouterComponent } from "../core/routing/router-component";



@Component({
    selector: 'gestione-servizi',
    templateUrl: './gestione-servizi-component.html',
    styleUrls: ['./gestione-servizi-component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class GestioneServiziComponent extends RouterComponent {

    @ViewChild('mezziDisponibiliTable') mezziDisponibiliTable: CoreTableComponent;

    public model: ServiceDTO = {};

    public unifiedService: RequestInterventionUnifiedService & {
        orarioTappaObj?
    } = {
            trasportoCorrente: null,
            prenotazioneCorrente: null,
            risorsaDelServizio: null
        };

    public currentId: string;

    private currentUser;

    public rilievoTappa = false;

    public rilievoSostaTappa = false;

    numTappe: number = 0;

    //sorgente dati : O = onLine , S= storico
    private source = 'O';
    public historicalArchiveFlag = false;

    //configurazioni mezzi assegnati
    public columnsMezziAssegnati: Array<CoreTableColumn> = [
        { title: 'Codice Mezzo', name: 'resourceCode', index: 1, sort: 'asc', flex: 30 },
        { title: 'Attivazione', name: 'activationDate', index: 2, flex: 30 },
        { title: 'Partenza', name: 'startDate', index: 3, flex: 30 },
        {
            title: '', name: '', optionTitle: 'resourceCode', index: 4,
            flex: 0,
            style: { "flex-basis": "30px", },
            options: [
                {
                    name: 'Rilascia il mezzo dal servizio',
                    icon: 'mdi mdi-ambulance',
                    clicked: execute(this, this.removeVehicle),
                    enabled: execute(this, this.evalEnableRilasciaMezzo)
                },
                {
                    name: 'Partenza del mezzo',
                    icon: 'mdi mdi-arrow-right-box',
                    clicked: execute(this, this.startVehicle),
                    enabled: execute(this, this.evalEnableRiattivazioneMezzo)
                },
                {
                    name: 'Proprietà',
                    icon: 'mdi mdi-information-outline',
                    clicked: execute(this, this.vehicleProperty)
                }, {
                    name: 'SEPARATOR',
                    icon: ' '
                },
                {
                    name: 'Riattivazione del mezzo',
                    icon: 'mdi mdi-repeat ',
                    clicked: execute(this, this.vehicleRiactivation),
                    enabled: execute(this, this.evalEnableVehicleRiactivation)
                }
            ]
        }
    ];
    public configMezziAssegnati = {
        paging: true,
        sorting: { columns: this.columnsMezziAssegnati },
        filtering: { filterString: '' },
    };

    //    @ViewChild(MezziAssegnatiComponent) mezziAssegnatiTable: MezziAssegnatiComponent;
    //    @ViewChild(MezziTurnoComponent) mezziTurnoTable: MezziTurnoComponent;
    //    @ViewChild(PrenotazioniAccorpateComponent) prenotazioniAccorpateTable: PrenotazioniAccorpateComponent;

    private evalEnableRilasciaMezzo() {
        return !this.historicalArchiveFlag;
    }

    private evalEnableRiattivazioneMezzo(row: TsServiceResourceDTO) {
        return !row.startDate && !this.historicalArchiveFlag;
    }
    private evalEnableVehicleRiactivation() {
        return !this.historicalArchiveFlag && this.canReactivateMobileStation;
    }

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
        {
            title: '', name: '', index: 7, optionTitle: 'vehicleCode', options: [{
                name: 'Assegna al servizio',
                icon: 'mdi mdi-ambulance',
                clicked: execute(this, this.assignVehicle),
                enabled: execute(this, this.isAssignableVehicle)
            }, {
                name: 'SEPARATOR',
                icon: ' '
            },
            {
                name: 'Proprietà',
                icon: 'mdi mdi-information-outline',
                clicked: execute(this, this.vehicleProperty),
                enabled: execute(this, this.getPhysicalFlag)
            },
            {
                name: 'Modifica Posizione',
                icon: 'mdi mdi-lead-pencil',
                clicked: execute(this, this.modifyPosition),
                enabled: execute(this, this.getPhysicalFlag)
            }], flex: 0,
            style: { "flex-basis": "50px", }
        }
    ];
    public configMezziTurno = {
        paging: true,
        sorting: { columns: this.columnsMezziTurno },
        filtering: { filterString: '' },
    };

    //configurazioni telefoni
    public listTelefoni: Array<any> = [];
    public columnsTelefoni: Array<CoreTableColumn> = [
        { title: 'Telefoni', name: 'telefono', style: {} },
    ];
    public configTelefoni = {
        paging: true,
        sorting: { columns: this.columnsTelefoni },
        filtering: { filterString: '' },
    };


    //configurazioni prenotazioni accorpate
    public columnsPrenotazioni: Array<CoreTableColumn> = [
        /*    { title: 'Indice', name: 'idx', index: 1, sort: 'asc', style: { "flex-grow": "0"  } },*/
        { title: 'Codice', name: 'bookingCode', index: 2, flex: 2 },
        { title: 'A', name: 'columnA.name', index: 3, flex: 1 },
        { title: 'Tappa', name: 'addressType', index: 4, flex: 3 },
        { title: 'Deambulazione', name: 'statusCompanion', index: 5, flex: 1 },
        { title: 'Trasporto', name: 'transportDate', index: 6, flex: 2 },
        { title: 'Indirizzo', name: 'address', index: 7, flex: 6 },
        { title: 'Trasportato', name: 'transported', index: 8, flex: 4 },
        { title: 'Tipo Pausa', name: 'pauseType', index: 9, flex: 4 },
        {
            title: '', name: '', index: 10, optionTitle: 'bookingCode', options: [
                /* {
                     name: 'Sposta la tappa in alto',
                     icon: 'mdi mdi-arrow-collapse-up',
                     clicked: execute(this, this.moveStageFirst)
                 }, {
                     name: 'Sposta la tappa su',
                     icon: 'mdi mdi-arrow-up',
                     clicked: execute(this, this.moveStageUp)
                 }, {
                     name: 'Sposta la tappa giù',
                     icon: 'mdi mdi-arrow-down',
                     clicked: execute(this, this.moveStageDown)
                 }, {
                     name: 'Sposta la tappa in fondo',
                     icon: 'mdi mdi-arrow-collapse-down',
                     clicked: execute(this, this.moveStageLast)
                 }, {
                     name: 'SEPARATOR',
                     icon: ' '
                 }, */
                {
                    name: 'Rileva dati sulla tappa',
                    icon: 'mdi mdi-ambulance',
                    clicked: execute(this, this.retriveStageData),
                    enabled: execute(this, this.getHistoricalFlag)
                }, {
                    name: 'Rileva dati di sosta sulla tappa',
                    icon: 'mdi mdi-ambulance',
                    clicked: execute(this, this.retriveStagePauseData),
                    enabled: execute(this, this.getHistoricalFlag)
                }, {
                    name: 'SEPARATOR',
                    icon: ' '
                }, {
                    name: 'Rimuovi dal servizio',
                    icon: 'mdi mdi-table-column-remove',
                    clicked: execute(this, this.removeStage),
                    enabled: execute(this, this.getHistoricalFlag)
                }, {
                    name: 'SEPARATOR',
                    icon: ' '
                }, /*{
                    name: 'Proprietà della Prenotazione',
                    icon: 'mdi mdi-information-outline',
                    clicked: execute(this, this.stageProperty)
                }, {
                    name: 'Dettaglio indirizzi',
                    icon: 'mdi mdi-information-outline',
                    clicked: execute(this, this.addressDetail)
                },*/ {
                    name: 'Dati della Prenotazione',
                    icon: 'mdi mdi-information-outline', /*mdi mdi-lead-pencil',*/
                    clicked: execute(this, this.stageModify)
                }
            ], flex: 0,
            style: { "flex-basis": "50px", }
        }
    ];
    public configPrenotazioni = {
        paging: true,
        sorting: { columns: this.columnsPrenotazioni },
        filtering: { filterString: '' },
    };

    public moveBooking(moveAction: string) {
        if (!this.penotazioneSelected) {
            this.componentService.getRootComponent().showToastMessage('warning', 'Selezionare una prenotazione dalla lista!');
        } else {
            switch (moveAction) {
                case "moveFirst":
                    this.moveStageFirst(this.penotazioneSelected);
                    break;
                case "moveUp":
                    this.moveStageUp(this.penotazioneSelected);
                    break;
                case "moveDown":
                    this.moveStageDown(this.penotazioneSelected);
                    break;
                case "moveLast":
                    this.moveStageLast(this.penotazioneSelected);
            }
        }
    }

    //configurazioni mezzi intervenuti
    public listMezziIntervenuti: Array<InterventionActivityDTO> = [];
    public columnsMezziIntervenuti: Array<CoreTableColumn> = [
        { title: 'Mezzo', name: 'resourceCode', style: {} },
        { title: 'Ora Arrivo', name: 'timestampArrival', style: {} },
        { title: 'Ora Partenza', name: 'timestampDeparture', style: {} },
        { title: 'Pausa', name: 'pause', style: {} },
        { title: 'Unità Pausa', name: 'pauseMu', style: {} },
        {
            title: '', name: '', options: [
                {
                    name: 'Proprietà',
                    icon: 'fa fa-info-circle',
                    clicked: execute(this, this.vehicleProperty)
                }
            ], style: { width: "50px" }
        }
    ];
    public configMezziIntervenuti = {
        paging: true,
        sorting: { columns: this.columnsMezziIntervenuti },
        filtering: { filterString: '' },
    };

    @ViewChild("prenotazioniTable") prenotazioniTable: CoreTableComponent;

    // Variabili di appoggio
    //Mezzo selezionato da quelli in turno
    public selectedVehicle;
    // mezzo fisico
    public phisicalVehicle: VehicleDirectAssignmentDTO;
    // mezzo in turno selezionato
    public vehicleTurn: AvailableParkingVehicleTurnDTO;
    // Mezzo assegnato  selezionato
    public vehicleAssigned: TsServiceResourceDTO;
    // prenotazione selezionata
    public penotazioneSelected: InterventionDTO;


    // Gruppo di check sulla proposta dei mezzi
    vehicleCheck: {
        //check tutti i mezzi
        allVehicles?: boolean;
        // check mezzi disponibili a breve
        toBeFree?: boolean;

        // Assegnazione diretta
        directAssign?: boolean;
        // Accorpato
        merged?: boolean;
    };

    //  destinazione
    public parking: Value;

    // lista mezzi fisici
    public vehicles = new Subject();
    // lista postazioni
    public parkings: Observable<Array<any>>;
    // lista tipi di sosta
    public pauseTypes: BehaviorSubject<Array<any>>;

    // tipo pausa
    public pauseTypeTemp: string;

    // sottoscrizione agli eventi pushing
    private serviceSubscription;

    private vehicleSubscription;

    private turnSubscription;

    public enableListMobileStation: boolean = false;
    private canReactivateMobileStation: boolean = false;


    private sendRowIdLs: (ev: Message) => void;

    constructor(
        private componentService: ComponentHolderService,
        private transportService: TransportModuleServiceService,
        private stormoService: StormoModuleServiceService,
        private route: ActivatedRoute,
        private parkingService: ParkingServiceService,
        private modalService: NgbModal,
        private bookingService: BookingModuleServiceService,
        private staticService: StaticDataService,
        router: Router,
        private messageService: MessageService,
        private tokenService: TokenService,
        private serviceCommon: ServiceCommon,
        private serviziService: ServiziService,
        private bcs: BrowserCommunicationService,
        private localData: LocalDataService
    ) {
        super(router);

    }

    public ngOnInit() {
        super.ngOnInit();
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

        this.vehicleSubscription = this.messageService.subscribe(EVENTS.VEHICLE);
        this.vehicleSubscription.observ$.subscribe((msg) => {
            console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
            //disaccoppia contesto subscribe da gestione evento 
            //- in caso di errore lascia sottoscrizione e individua riga errore
            setTimeout(() => {
                this.evaluatePushingEventVehicle(msg);
            }, 0);//Chiudi timeout
        });

        this.turnSubscription = this.messageService.subscribe(EVENTS.TURN);
        this.turnSubscription.observ$.subscribe((msg) => {
            console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
            //disaccoppia contesto subscribe da gestione evento 
            //- in caso di errore lascia sottoscrizione e individua riga errore
            setTimeout(() => {
                this.evaluatePushingEventTurn(msg);
            }, 0);//Chiudi timeout
        });

        this.initListener();
    }

    public loadData() {

        //TODO aggiornare i dati sull'header
        this.getCurrentUser();

        this.currentId = this.route.snapshot.params['id'];

        let sourceParam = this.route.snapshot.params['source'];
        if (sourceParam && sourceParam.length > 0 && sourceParam == 'S') {
            this.historicalArchiveFlag = true;
            this.source = 'S';
        } else {
            this.source = 'O';
        }


        this.parkings = this.staticService.getStaticDataByType('POSTAZIONE_ARRIVO').map(valueToSelect);

        this.enableListMobileStation = this.staticService.getTSConfig('CanListMobileStations', 'FALSE') === 'TRUE';
        this.canReactivateMobileStation = this.staticService.getTSConfig('CanReactivateMobile', 'FALSE') === 'TRUE';
        //lista dei tipi di pausa
        this.pauseTypes = new BehaviorSubject<Array<any>>([]);

        this.serviziService.setGestioneServiziComponent(this);

        this.transportService.loadPauseType().subscribe(res => {
            this.pauseTypes.next(res.resultList);
        });

        //all'init carico servizio
        this.refreshService();

        // carico i check dalla cache
        this.vehicleCheck = this.localData.getBean('GESTONE_SERVIZI_CHECK', {
            //check tutti i mezzi
            allVehicles: false,
            // check mezzi disponibili a breve
            toBeFree: false,
            // Assegnazione diretta
            directAssign: false,
            // Accorpato
            merged: false,
        });
    }

    private initListener() {
        this.sendRowIdLs = (ev: Message) => {
            let param: SelectServiceInSinottico = {
                serviceCode: this.model.code,
                serviceId: this.model.id
            }
            this.bcs.postMessage(Type.APP2_SET_CURRENT_SERVICE, param, "*");
        };
        this.bcs.addEventListener(Type.APP2_GET_SELECTED_BOOKING, this.sendRowIdLs);
    }

    private evaluatePushingEvent(msg) {
        if (msg.data.action && msg.data.action != "ALERT") {
            let ids: string[] = msg.data.payload.split(",");

            if (ids && ids.length > 0 && this.model.id && msg.data.from != this.currentUser) {
                for (var i = 0; i < ids.length; i++) {
                    if (ids[i] === this.model.id) {
                        if (msg.data.action == "ARCHIVE" || msg.data.action == "DELETE") {
                            this.componentService.getRootComponent().showModal('Attenzione!', "Il servizio è stato '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                            this.router.navigate(['/sinottico-servizi']);
                        } else {
                            this.refreshService();
                        }
                        /*this.componentService.getRootComponent().unmask();
                        this.componentService.getRootComponent().showConfirmDialog('Attenzione!', "Sono state effettuate delle modifiche dall'utente '" + msg.data.from + "'. \nE' necessario ricaricare dati. Procedere?").then((result) => {
                            console.log("dati modificati lato server: forzo refresh");
                            this.refreshService();
                        }, (reason) => {
                            console.log("dati modificati lato server: non forzo refresh");
                        });*/
                    }
                }
            }
        }
    }

    protected decodeActionSynchForMessage(action: string) {
        let actionDec = "";
        if (action) {
            switch (action) {
                case "DELETE":
                    actionDec = "cancellato";
                    break;
                case "ARCHIVE":
                    //se non è presente sul sinottico non faccio nulla
                    actionDec = "archiviato";
                    break;
                case "UPDATE":
                // questo evento arriva sia su un update che su una creazione, 
                // faccio le stesse cose sull'alert
                case "ALERT":
                    actionDec = "aggiornato";
                    break;
                default:
                    actionDec = "AGGIORNATO";
                    break;
            }
        }
        return actionDec;
    }

    private evaluatePushingEventVehicle(msg) {
        let action = msg.data.action;
        if (msg.data.payload) {
            //lista di id delle resource da allarmare
            let idResources: Array<string> = msg.data.payload.split(",");
            let trovato = false;
            //cerco nella lista dei mezzi assegnati
            if (this.model.serviceResourceList) {
                this.model.serviceResourceList.forEach(sr => {
                    if (!trovato) {
                        trovato = this.checkPresentElementIntoArray(idResources, sr.resourceId);
                    }
                });
            }
            if (trovato) {
                //se ho trovato un mezzo tra quelli assegnati, effettuo il refresh del servizio
                this.refreshService();
            } else {
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

    private evaluatePushingEventTurn(msg) {
        let action = msg.data.action;
        if (msg.data.payload) {
            //lista di parkingVehicleTurnId da gestire
            let idResources: Array<string> = msg.data.payload.split(",");
            this.refreshVehicleTurn(this.vehicleCheck.allVehicles, this.vehicleCheck.toBeFree);
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

    public getCurrentUser() {
        this.currentUser = this.tokenService.getUserName();
        if (this.currentUser) {
            console.log("################## CURRENT_USER:" + this.currentUser);
            return true;
        } else {
            return false;
        }
    }

    ngOnDestroy() {
        super.ngOnDestroy();
        this.serviceSubscription.observ$.unsubscribe();
        this.vehicleSubscription.observ$.unsubscribe();
        this.turnSubscription.observ$.unsubscribe();

        this.serviziService.setGestioneServiziComponent(null);
        if (this.sendRowIdLs) {
            this.bcs.removeEventListener(Type.APP2_GET_SELECTED_BOOKING, this.sendRowIdLs);
        }

        this.sendMessageToSinottico({});

    }

    //Mezzi assegnati
    public selectedMezziAssegnati(ev) {
        this.vehicleAssigned = ev;
    }

    public getVehicleAlarm(row: TsServiceResourceDTO): string {
        let toRet = '';
        if (row.alarm) {
            //allarme 
            toRet += '<i class="fa fa-exclamation" aria-hidden="true"></i>';
        }

        if (row.support) {
            toRet += '<i class="mdi mdi-plus-box" aria-hidden="true"></i>';
        }

        return toRet;
    }

    // mezzi in turno
    public selectedMezziTurno(selected: AvailableParkingVehicleTurnDTO) {
        if (selected) {
            this.vehicleCheck.directAssign = false;

            this.vehicleTurn = selected;
            console.log("selected" + this.vehicleTurn.vehicleCode);

            this.phisicalVehicle = {
                id: this.vehicleTurn.vehicleId,
                vehicleCode: this.vehicleTurn.vehicleCode
            };

            if (selected.telephoneList) {
                this.listTelefoni = selected.telephoneList;
            }

        } else {
            this.vehicleTurn = null;
            this.phisicalVehicle = null;
        }
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

    public getHistoricalFlag(row?: any) {
        return !this.historicalArchiveFlag;
    }
    
    public getPhysicalFlag(row?: any) {
        return row?row.physicalFlag:false;
    }
    
     public isAssignableVehicle(row?:any){
       return row?row.physicalFlag && !row.pauseFlag:false;
    }

    public assignVehicle(el?: AvailableParkingVehicleTurnDTO) {
        if(el && el.pauseFlag){ //in questo modo l'assegnazione diretta passa come nella versione C/S
            this.componentService.getRootComponent().showModal("Attenzione","Mezzo fisico non disponibile");
            return[];
        }
        if (this.phisicalVehicle) {
            if(!this.vehicleCheck.directAssign){
                let mezziSelected  = this.listMezziTurno.filter((r) => r.vehicleId ===this.phisicalVehicle.id);
                if(!mezziSelected || mezziSelected.length==0 || mezziSelected[0].pauseFlag){
                    this.componentService.getRootComponent().showModal("Attenzione","Mezzo fisico non disponibile");
                    return[];
                }
            }

            this.componentService.getRootComponent().mask();
            //console.log("merged:"+this.merged);
            let model = {
                vehicleId: this.phisicalVehicle.id,
                serviceId: this.model.id,
                inAppoggio: this.vehicleCheck.merged
            };

            this.transportService.addVehicleToService(model).catch((e, o) => {
                this.componentService.getRootComponent().showToastMessage('error', 'Errore nell\'assegnamento del mezzo');
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(res => {
                this.componentService.getRootComponent().showToastMessage('success', 'Mezzo assegnato');
                this.refreshService();
                //invia stormo activation
                this.sendStormoActivation(model.vehicleId, this.phisicalVehicle.vehicleCode, model.serviceId);
            });
        } else {
            this.componentService.getRootComponent().unmask();
            this.componentService.getRootComponent().showToastMessage('error', 'Selezionare un mezzo da assegnare');
        }
    }

    public sendStormoActivation(resId: string, resCode: string, servId: string) {
        //invia stormo activation
        let req = { resourceId: resId, resourceCode: resCode, serviceId: servId };
        this.stormoService.sendActivation(req).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
            return [];
        }).subscribe(res2 => {
            if (AcknowledgeResp.OutEnum.ACK != res2.out) {
                console.warn("Stormo Activation failed for vehicle " + resCode + " and serviceId " + servId);
                this.componentService.getRootComponent().showToastMessage('error', "Non e' stato possibile inviare il servizio \nalle unita' mobili in turno sul mezzo " + resCode + ".");
            } else {
                console.log("OK Stormo Activation for vehicle " + resCode + " and serviceId " + servId);
            }
        });
    }

    public sendStormoCleanActivation(resId: string, resCode: string, servId: string) {
        //invia stormo activation
        let req = { resourceId: resId, resourceCode: resCode, serviceId: servId };
        this.stormoService.cleanActivation(req).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
            return [];
        }).subscribe(res2 => {
            if (AcknowledgeResp.OutEnum.ACK != res2.out) {
                console.warn("Stormo CleanActivation failed for vehicle " + resCode + " and serviceId " + servId);
                this.componentService.getRootComponent().showToastMessage('error', "Non e' stato possibile annullare il servizio \nnelle unita' mobili in turno sul mezzo " + resCode + ".");
            } else {
                console.log("OK Stormo CleanActivation for vehicle " + resCode + " and serviceId " + servId);
            }
        });
    }

    public sendStormoUpdateActivation(servId: string) {
        //invia stormo activation
        let req = { serviceId: servId };
        this.stormoService.updateActivation(req).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
            return [];
        }).subscribe(res2 => {
            if (AcknowledgeResp.OutEnum.ACK != res2.out) {
                console.warn("Stormo UpdateActivation failed for serviceId " + servId);
                this.componentService.getRootComponent().showToastMessage('error', "Non e' stato possibile aggiornare il servizio \na tutte le unita' mobili impegnate su di esso.");
            } else {
                console.log("OK Stormo UpdateActivation for serviceId " + servId);
            }
        });
    }

    public removeVehicle(resource?: TsServiceResourceDTO) {
        let model;
        if (resource) {
            this.vehicleAssigned = resource;
        }
        if (this.vehicleAssigned) {

            let modal = this.modalService.open(ChiusuraMezzoModal, { size: 'sm' });
            modal.result.then((result) => {
                let serviceIdSelected = this.vehicleAssigned.serviceId;
                if (result) {
                    model = {
                        vehicleId: this.vehicleAssigned.resourceId,
                        serviceId: this.vehicleAssigned.serviceId,
                        parkingId: this.vehicleAssigned.parkingId,
                        reason: result.id
                    };
                    this.componentService.getRootComponent().mask();
                    this.transportService.releaseVehicleFromService(model).catch((e, o) => {
                        this.componentService.getRootComponent().showToastMessage('error', 'Errore nel rilascio del mezzo');
                        this.componentService.getRootComponent().unmask();
                        return [];
                    }).subscribe(res => {
                        this.componentService.getRootComponent().showToastMessage('success', 'Mezzo rimosso');
                        //Stormo clean activation
                        this.sendStormoCleanActivation(model.vehicleId, this.vehicleAssigned.resourceCode, model.serviceId);
                        if (!(res && res.list && res.list.length > 0)) {
                            this.archiveService(serviceIdSelected, this.model.code, true);
                        } else {
                            this.refreshService();
                        }
                    });
                }
            }, (reason) => {
            });

        } else {
            this.componentService.getRootComponent().showToastMessage('error', 'Selezionare un mezzo da rimuovere');
        }
    }

    public async callArchiveService() {
        this.archiveService(this.model.id, this.model.code, false);
    }

    public async archiveService(id: string, serviceCode: string, fromRemove: boolean) {
        this.componentService.getRootComponent().mask();
        let checkIsManaged = await this.serviceCommon.checkIsManaged(id);
        if (checkIsManaged.ok) {
            let ret = await this.serviceCommon.checkAndArchiveService(id, serviceCode, checkIsManaged.ok);
            if (ret) {
                //vai al sinottico servizi
                this.router.navigate(['/sinottico-servizi']);
            } else {
                if (fromRemove) { this.refreshService(); }
            }
        } else {
            this.componentService.getRootComponent().unmask();
            if (!fromRemove) {
                this.componentService.getRootComponent().showModal('Attenzione', 'Archiviazione impossibile! Nessun mezzo è stato associato al servizio.');
            } else {
                this.refreshService();
            }
        }
        this.componentService.getRootComponent().unmask();
    }


    /*public checkArchivableService(serviceId: string){
        if(serviceId==null || serviceId.length==0)
            return false;
        this.transportService.checkArchivableService(serviceId).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore check archiviazione del servizio');
            return [];
        }).subscribe(resCheck => {
            let esito = resCheck; 
            if(esito && esito.isOk())                    
           return resCheck.isOk();
        });
    }*/

    public refreshService() {
        this.componentService.getRootComponent().mask();

        this.transportService.getServiceById(this.currentId, this.source).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento del servizio');
            return [];
        }).subscribe(service => {
            this.componentService.getRootComponent().mask();
            let header = <GestioneServiziHeaderComponent>this.componentService.getHeaderComponent('gestioneServiziHeaderComponent');
            if (header) {
                header.setModel(service);
            }
            this.model = service;
            this.vehicleAssigned = null;
            this.refreshVehicleTurn(this.vehicleCheck.allVehicles, this.vehicleCheck.toBeFree);

            this.cleanInterventionUnified();
            this.listMezziIntervenuti = [];

            this.reselectTables();

            this.numTappe = service.interventionList.length;
            this.componentService.getRootComponent().unmask();

            // invio il messaggio alla popup se è aperta
            this.sendMessageToSinottico(this.model);
        });
    }

    sendMessageToSinottico(currentTransport: ServiceDTO) {
        let param: SelectServiceInSinottico = {
            serviceCode: this.model.code,
            serviceId: this.model.id
        }
        this.bcs.postMessage(Type.APP2_SET_CURRENT_SERVICE, param);
    }

    public reselectTables() {
        if (this.penotazioneSelected && this.prenotazioniTable) {
            this.prenotazioniTable.selectByproperty("id", this.penotazioneSelected.id);
        }
    }

    public refreshPhisicalVehicles(code: string) {
        let model: VehicleDirectAssignmentFilter = {};
        if (code) {
            model.vehicleCode = code.toUpperCase();
        }

        this.transportService.searchDirectAssignmentVehicle(model).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento dei mezzi');
            return [];
        }).subscribe(res => {
            this.vehicles.next(res.resultList);
        });

    }

    //salvataggio dati servizio
    public saveService() {
        let data = {
            serviceId: this.model.id,
            note: this.model.note,
            km: this.model.km
        };
        this.transportService.saveService(data).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel salvataggio dei dati del servizio');
            return [];
        }).subscribe(res => {
            this.componentService.getRootComponent().showToastMessage('success', 'Salvataggio dati avvenuto con successo');
            this.model = res;
        });
    }

    // check assegnazione diretta
    public setDirectAssign(check: boolean) {
        if (check) {
            this.phisicalVehicle = null;
            this.vehicleTurn = null;

            //     this.mezziTurnoTable.clean();
        } else {
        }
    }


    // check tutti i mezzi
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
            this.phisicalVehicle = null;
            this.vehicleTurn = null;
            //    console.log("loaded vehicle in turn", res);
        };
        if (toBeFree) {
            this.transportService.shortlyAvailableVehicles(filter).subscribe(listener);
        } else {
            this.transportService.selectAvailableVehicleTurn(filter).subscribe(listener);
        }
    }

    // invia mezzo a destinazione
    public moveInterventionOnDestination() {

        if (!this.vehicleTurn) {
            this.componentService.getRootComponent().showToastMessage('warning', 'Selezionare un mezzo disponibile dalla lista!');
        } else if (this.parking == null) {
            this.componentService.getRootComponent().showToastMessage('warning', 'Selezionare una postazione di Destinazione!');
        } else {
            this.componentService.getRootComponent().mask();
            let model: MoveInterventionPositionRequestDTO = {
                destinationId: this.parking.id,
                vehicleTurnId: this.vehicleTurn.vehicleTurnId
            }
            this.transportService.moveInterventionOnDestination(model).subscribe(res => {
                let parkingVehicleTurn: AvailableParkingVehicleTurnDTO = res.result;
                this.replaceVehicleTurn(parkingVehicleTurn);
                this.componentService.getRootComponent().unmask();
            });
        }
    };

    // rientro del mezzo
    public moveInterventionOnArrival() {

        if (!this.vehicleTurn) {
            this.componentService.getRootComponent().showToastMessage('warning', 'Selezionare un mezzo disponibile dalla lista!');
        } else {
            this.componentService.getRootComponent().mask();
            let model: MoveInterventionPositionRequestDTO = {
                vehicleTurnId: this.vehicleTurn.vehicleTurnId
            }
            this.transportService.moveInterventionOnArrival(model).subscribe(res => {
                let parkingVehicleTurn: AvailableParkingVehicleTurnDTO = res.result;
                this.replaceVehicleTurn(parkingVehicleTurn);
                this.componentService.getRootComponent().unmask();
            });
        }
    };

    private replaceVehicleTurn(resTurno: AvailableParkingVehicleTurnDTO) {
        let appList: Array<AvailableParkingVehicleTurnDTO> = [];
        appList = this.listMezziTurno.slice();
        let index: number = 0;
        for (index = 0; index < appList.length; index++) {
            let currTurno = appList[index];
            if (resTurno.vehicleTurnId == currTurno.vehicleTurnId) {
                appList[index] = resTurno;
                break;
            }
        }
        this.listMezziTurno = appList.slice();
        appList = undefined;
    };

    // partenza mezzo
    public startVehicle(row: TsServiceResourceDTO) {
        let model = {
            serviceResourceId: row.id
        }
        this.transportService.vehicleStart(model).catch((e, o) => {
            return [];
        }).subscribe(res => {
            this.refreshService();
        });

    }

    // modifica la posizione
    public modifyPosition(row: AvailableParkingVehicleTurnDTO) {
        //faccio apparire la popup per chiedere quale postazione inserire

        let modal = this.modalService.open(TextModalComponent, { size: 'sm' });

        modal.componentInstance.title = "Posizione del mezzo";
        modal.componentInstance.label = "Nuova posizione";

        modal.result.then((result) => {
            let newPos = result ? result : "";
            this.componentService.getRootComponent().mask();

            let body: VehiclePositionToChangeRequest = {
                vehicleId: row.vehicleId,
                positionName: newPos
            };
            this.transportService.modifyInterventionPosition(body).subscribe(res => {
                this.componentService.getRootComponent().unmask();
                this.refreshVehicleTurn(this.vehicleCheck.allVehicles, this.vehicleCheck.toBeFree);
            });

        }, (reason) => {
        });

    }

    //  AvailableParkingVehicleTurnDTO | TsServiceResourceDTO | InterventionActivityDTO
    public vehicleProperty(v) {
        //visualizzare la popup col dettaglio del mezzo
        let vehicleCode = v.vehicleCode || v.resourceCode;

        if (vehicleCode) {
            let modal = this.modalService.open(VehicleDetailComponent, { size: 'lg' });
            modal.componentInstance.vehicleCode = vehicleCode;
        }
    }

    // riattivazione mezzo sul servizio
    public vehicleRiactivation(row: TsServiceResourceDTO) {
        let body = { idServiceResource: row.id };
        this.transportService.activationVehicleService(body).subscribe(res => {
            this.componentService.getRootComponent().showToastMessage('success', 'Mezzo ' + res.resourceCode + ' riattivato');
            //invia stormo activation
            this.sendStormoActivation(row.resourceId, row.resourceCode, row.serviceId);
        });
    }

    // ------------------- 
    // metodi contestuali alle prenotazioni accorpate

    public getTappaIcon(row: InterventionDTO): string {
        if (!row.arrived && !row.departed) {
            //nero
            return "";
        } else if (row.arrived && row.departed) {
            //bianco
            return "fa fa-check-square";
        } else {
            //grigio
            return "fa fa-check";
        }
    }

    //prenotazione selezionata
    public selectedPrenotazioni(ev: InterventionDTO) {
        this.penotazioneSelected = ev;
        //carico i mezzi intervenuti 
        this.listMezziIntervenuti = ev.interventionActivities;

        if (this.unifiedService &&
            this.unifiedService.prenotazioneCorrente &&
            ev.id !== this.unifiedService.prenotazioneCorrente.id) {
            this.cleanInterventionUnified();
        }
    }
    // salva la timbratura
    public setInterventionUnified() {

        let modelToSave = JSON.parse(JSON.stringify(this.unifiedService));

        
        // pulisco l'oggetto dai campi di appoggio
        modelToSave.orarioTappaObj = undefined;       
        modelToSave.orarioTappa = moment((<any>this.unifiedService).orarioTappaObj, 'HH:mm');

        if(!modelToSave.orarioTappa._isValid){
            modelToSave.orarioTappa = moment(moment(), 'HH:mm');
        }

        this.componentService.getRootComponent().mask();
        this.transportService.setInterventionUnified(modelToSave).subscribe(res => {
            this.componentService.getRootComponent().showToastMessage('success', 'Salvataggio dati avvenuto con successo');
            this.refreshService();
            this.rilievoTappa = false;
            this.componentService.getRootComponent().unmask();
        });

    }

    public cleanInterventionUnified() {
        this.rilievoSostaTappa = false;
        this.rilievoTappa = false;
        this.unifiedService = {
            trasportoCorrente: null,
            risorsaDelServizio: null,
            prenotazioneCorrente: null,
        };
    }

    // salva dati rilievo pausa tappa
    public setInterventionPause() {
        this.componentService.getRootComponent().mask();

        let model = {
            serviceId: this.model.id,
            interventionId: this.penotazioneSelected.id,
            pauseType: this.pauseTypeTemp
        };

        this.transportService.setInterventionStopType(model).subscribe(ret => {
            this.componentService.getRootComponent().unmask();
            this.componentService.getRootComponent().showToastMessage('success', 'Salvataggio dati avvenuto con successo');
            this.refreshService();
        });
    }

    public moveStageFirst(stage: InterventionDTO) {
        this.sendMoveStageRequest(stage, MoveInterventionRequestDTO.ShiftEnum.FIRST);
    }

    public moveStageUp(stage: InterventionDTO) {
        this.sendMoveStageRequest(stage, MoveInterventionRequestDTO.ShiftEnum.UP);
    }

    public moveStageDown(stage: InterventionDTO) {
        this.sendMoveStageRequest(stage, MoveInterventionRequestDTO.ShiftEnum.DOWN);
    }

    public moveStageLast(stage: InterventionDTO) {
        this.sendMoveStageRequest(stage, MoveInterventionRequestDTO.ShiftEnum.LAST);
    }

    private sendMoveStageRequest(stage: InterventionDTO, shiftRequest: MoveInterventionRequestDTO.ShiftEnum) {
        if (this.model == null) {
            this.componentService.getRootComponent().showToastMessage('warning', 'Selezionare un servizio');
        } else if (stage == null) {
            this.componentService.getRootComponent().showToastMessage('warning', 'Selezionare una tappa');
        } else {
            this.componentService.getRootComponent().mask();

            let request: MoveInterventionRequestDTO = {
                serviceCode: this.model.code,
                indexFrom: stage.idx,
                shift: shiftRequest
            };

            this.transportService.moveInterventionPosition(request).catch((e, o) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(res => {
                if (res != null) {
                    let service: ServiceDTO = res.result;
                    if (service != null) {

                        if (service.interventionList) {
                            service.interventionList = service.interventionList.sort((n1, n2) => {
                                if (n1.idx > n2.idx) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            });
                        }

                        this.model.interventionList = service.interventionList;
                        // TODO INTEGRAZIONE STORMO
                        // TrasportiOrdinari:TSMain.moveFirst() -> stormoUpdateActivation(tmpSvc.getId());
                    }
                    this.sendStormoUpdateActivation(stage.serviceId);
                }
                this.componentService.getRootComponent().unmask();
            });

        }
    }

    public retriveStageData(stage: InterventionDTO) {
        if (this.model.serviceResourceList && this.model.serviceResourceList.length > 0) {
            this.cleanInterventionUnified();
            this.rilievoTappa = true;

            let resourceSel = null;
            if (this.model.serviceResourceList && this.model.serviceResourceList.length == 1) {
                resourceSel = this.model.serviceResourceList[0];
            }

            //creo un nuovo oggetto per fare la timbratura della tappa
            this.unifiedService = {
                trasportoCorrente: this.model,
                modificaTappePrecedenti: true,
                risorsaDelServizio: resourceSel,
                prenotazioneCorrente: stage,
                orarioTappa: new Date(),
                pauseType: this.staticService.getConfigValueByKey('LABEL_TEMPO_SOSTA', "1/2H")
            };
            //serve per riempire il campo sulla maschera
            (<any>this.unifiedService).orarioTappaObj = moment().format('HH:mm');
        } else {
            this.componentService.getRootComponent().showToastMessage('warning', 'Non ci sono mezzi assegnati sul servizio');
        }
    }

    public retriveStagePauseData(stage: InterventionDTO) {
        this.cleanInterventionUnified();
        //visualizzo il pannello per la pausa
        this.rilievoSostaTappa = true;

    }

    public async removeStage(stage: InterventionDTO) {
        this.componentService.getRootComponent().mask();

        let modelRequestRemove = {
            serviceId: this.model.id,
            interventionId: this.penotazioneSelected.id,
            bookingId: this.penotazioneSelected.bookingId,
            acceptRemoveBookingWithTimestamp: false
        };

        let hasTimestamp = await this.hasTimestampBooking(this.model.id, this.penotazioneSelected.id);
        let lastBooking: boolean = this.model.interventionList.length > 2 ? false : true;
        if (hasTimestamp.ok) {
            this.componentService.getRootComponent().unmask();
            this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                "Questa prenotazione è già stata servita da un mezzo. Continuare comunque?").then((result) => {
                    modelRequestRemove.acceptRemoveBookingWithTimestamp = true;
                    this.removeBookingFromService(modelRequestRemove, stage.bookingCode, lastBooking);
                }, (reason) => {
                    console.log("Rimozione prenotazione da servizio rifiutata e non effettuata ");
                    this.componentService.getRootComponent().unmask();
                    return;
                });
        } else {
            this.removeBookingFromService(modelRequestRemove, stage.bookingCode, lastBooking);
        }
    }

    public removeBookingFromService(model: any, bookingCode: string, lastBooking: boolean) {
        this.componentService.getRootComponent().mask();
        this.transportService.removeBookingFromService(model).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            this.componentService.getRootComponent().unmask();
            this.componentService.getRootComponent().showToastMessage('success', "Rimozione prenotazione '" + bookingCode + "' dal servizio avvenuta con successo");
            if (lastBooking) {
                //update stormo
                this.sendStormoUpdateActivation(model.serviceId);
                setTimeout(() => {
                    this.router.navigate(['/sinottico-servizi']);
                }, 700);//Chiudi timeout
            } else {
                this.refreshService();
            }
        });
    }
    public async hasTimestampBooking(serviceId: string, interventionId: string): Promise<CheckResultDTO> {
        let model = {
            serviceId: this.model.id,
            interventionId: this.penotazioneSelected.id,
            bookingId: this.penotazioneSelected.bookingId
            //acceptRemoveBookingWithTimestamp: false
        };
        return this.transportService.hasTimestampBookingService(model).toPromise();
    }

    public stageProperty(stage: InterventionDTO) {
    }

    public addressDetail(stage: InterventionDTO) {
    }

    public stageModify(stage: InterventionDTO) {
        this.router.navigate(['/modifica-prenotazione', this.historicalArchiveFlag ? 'S' : '2', stage.bookingId], { queryParams: { serviceCode: this.model.code, serviceId: this.model.id } });
    }

    // mezzi intervenuti
    public selectedMezziIntervenuti(el) {

    }


    public openWindow(type: 'prenotazioni' | 'servizi') {
        this.bcs.openChildren(environment.synURL, type, 600, 600);
    }


    public openCrewPopup() {
        let modal = this.modalService.open(CrewMemberModalComponent, { size: 'lg' });
        modal.componentInstance.serviceId = this.model.id;
    }

    public openMobilePopup() {
        let modal = this.modalService.open(MobileUnitsModalComponent, { size: 'lg' });
        modal.componentInstance.serviceId = this.model.id;
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
    
    public getTooltipMezziAssegnati(row: TsServiceResourceDTO): string {
        let ret = '';
        if (row.alarm) {
            ret += 'Mezzo non partito';
        }

        if(row.support){
            ret += ' Mezzo di supporto';
        }
        return ret;
    }

    public getTooltippreontazioniAccorpate(row: InterventionDTO): string {

        if (!row.arrived && !row.departed) {
            //nero
            return "";
        } else {
            return "Tappa rilevata";
        }

        /*if (row.arrived && row.departed) {
            //bianco
            return "Tappa rilevata, mezzo partito";
        } else {
            //grigio
            return "Tappa rilevata, mezzo arrivato";
        }*/
    }
    
      public printServicePdf() {
        this.bookingService.getServicePdf(this.model.code).subscribe( resp => {
            let fileURL = URL.createObjectURL(resp);
            window.open(fileURL);
        });
    }
    
} 