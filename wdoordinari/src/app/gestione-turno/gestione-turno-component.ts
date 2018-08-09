import { Component, OnInit, ViewEncapsulation, AfterViewInit} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ComponentHolderService,EventService, EventData } from "app/service/shared-service";
import {TurnRequestDTO, TurnDTO, ParkingServiceService, TurnModuleServiceService, SanitaryCategory118, Convention118, Vehicle118,
    Value,TransportModuleServiceService} from "services/services.module";
import { Subject } from "rxjs/Subject";
import { Router } from "@angular/router";
import * as moment from 'moment';
import { Observable } from 'rxjs/Observable';
import { MessageService, Subscription } from '../service/message.service';
import { EVENTS } from '../util/event-type';
import { TokenService } from '../service/token.service';
import { convertToDate, convertMomentDateToStruct, sameDay } from "app/util/convert";
import { RouterComponent } from '../core/routing/router-component';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";



@Component({
    selector: 'gestione-turno-component',
    templateUrl: './gestione-turno-component.html',
    //styleUrls: ['./gestione-turno-component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class GestioneTurnoComponent extends RouterComponent implements AfterViewInit {

    public currentVehicleCode: string;
    public parkingVehicleTurnId: string;
    public update: boolean;

    startDateStruct: NgbDateStruct;
    endDateStruct: NgbDateStruct;

    public turn:TurnDTO;

    //lista postazioni
    public parkingItems: Subject<Array<any>>;
    //lista mezzi
    public vehicles : Subject<Array<any>>;
    //lista catgorie sanitarie
    public sanitaryCategories: Subject<Array<any>>;
    //lista convention
    public conventions: Subject<Array<any>>;
    //mezzo selezionato
    public selectedVehicle: Vehicle118;
    //categoria sanitaria selezionata
    public selectedSanitaryCategory: SanitaryCategory118;
    //convention selezionata
    public selectedConvention: Convention118;

    turnSubscription: Subscription;
    /* FormGroup per la validazione */
    turniFG: FormGroup;
    private currentUser;

    constructor(
        private route: ActivatedRoute,
        private comp: ComponentHolderService,
        private parkingService: ParkingServiceService,
        private transportService: TransportModuleServiceService,
        private turnService: TurnModuleServiceService,
        private componentService: ComponentHolderService,
        private messageService: MessageService,
        private tokenService: TokenService,
        router: Router,
        private eventService: EventService,
        private fgs: FormGroupGeneratorService,
    ) {
        super(router)
        this.parkingItems = new Subject();
        this.sanitaryCategories = new Subject<Array<any>>();
        this.conventions = new Subject<Array<any>>();
        this.vehicles = new Subject();
        this.turniFG = this.fgs.getFormGroup('turno');
    }

    ngAfterViewInit() {

        let event = new EventData();
        event.from = 'gestione-turno-component';
        //event.data = this.currentId;
        this.eventService.sendEvent(event);
        this.componentService.getRootComponent().unmask();  
    }

    ngOnInit(): void {
        super.ngOnInit();  
        this.turnSubscription = this.messageService.subscribe(EVENTS.TURN);
        this.turnSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });             
    }

    loadData():void{
        this.clean();
        this.parkingVehicleTurnId = this.route.snapshot.params['id']; 
        if (this.parkingVehicleTurnId){
            this.update = true;
            this.getTurn(this.parkingVehicleTurnId);
        }else {
            //nuovo turno
            //this.update = false;
            //sto in insert, valorizzo la convenzione di default, ovvero secondario
            //this.setSecConvention118();
            /*if (this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')) {
                // passo oggetto vuoto all'header
                (<any>this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')).fromItemToBannerModel();
            }*/
        }
        this.selectSanitaryCategory118Web();
        this.selectConvention118();

         //recupero l'utente loggato
         if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }       
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.turniFG);
    }

    getTurn(parkingVehicleTurnId: string){
        let req: TurnRequestDTO = {
            parkingVehicleTurnId: this.parkingVehicleTurnId
        };
        //sto in update, ricarico il turno dal server
        this.componentService.getRootComponent().mask();
        this.turnService.getTurn(req).catch((e, o) => {
        this.componentService.getRootComponent().unmask();
        return [];
        }).subscribe(res => {
            this.turn = res;
            if (this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')) {
                // passo l'oggetto all'header
                (<any>this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')).fromItemToBannerModel(this.turn);
            }
            this.startDateStruct = convertMomentDateToStruct(moment(res.startTurnDate));
            this.endDateStruct = convertMomentDateToStruct(moment(res.endTurnDate));
            
            this.selectedVehicle={"id":res.vehicleId,"code":res.vehicleCode};
            this.selectedConvention={"id":res.conventionId,"description":res.conventionDescription};
            this.selectedSanitaryCategory={"id":res.sanitaryCategoryId,"description":res.sanitaryCategoryDescription};
            
            this.currentVehicleCode = res.vehicleCode;

            this.componentService.getRootComponent().unmask();
        });
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

    protected decodeActionSynchForMessage(action: string) {
        let actionDec = "";
        if (action) {
            switch (action) {
                case "DELETE":
                    actionDec = "CANCELLATO";
                    break;
                case "UPDATE":
                    actionDec = "AGGIORNATO";
                    break;
                default:
                    actionDec = "AGGIORNATO";
                    break;
            }
        }
        return actionDec;
    }

    //elaboro il messaggio che ho ricevuto della topic al quale mi sono sottoscritta
    private elaboratePushMessage(msg) {
        if (msg.data.payload && msg.data.action) {
            //se l'operazione è stata effettuata da un altro utente
            if (!this.currentUser || !msg.data.from || (msg.data.from != this.currentUser)){
                let parkingVehicleTurnId: string = null;
                //recupero l'id del turno
                parkingVehicleTurnId = msg.data.payload;
                if (this.parkingVehicleTurnId && parkingVehicleTurnId==this.parkingVehicleTurnId){
                    this.parkingVehicleTurnId = parkingVehicleTurnId;
                    this.componentService.getRootComponent().showModal('Attenzione!', "Il turno "+this.currentVehicleCode+" è stato '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                    switch (msg.data.action) {
                        case "DELETE":
                            //ritorno al sinottico dei turni
                            this.router.navigate(['/sinottico-turni']);
                            break;
                        case "UPDATE":
                            //ricarico dal server il turno
                            this.getTurn(parkingVehicleTurnId);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    ngOnDestroy() {
        super.ngOnDestroy();
        this.turnSubscription.observ$.unsubscribe();
    }

    //carica la lista di parking filtrata per 'name'
    updateParkingList(name?: string) {
        let filter = { name: name ? name.toUpperCase() : '' };
        if (filter.name === '') {
            filter.name = '%';
        }
        this.parkingService.getParkingCompact(filter.name).subscribe(list => {
            this.parkingItems.next(list);
        });
    }

    //carica la lista di mezzi del 118 filtrata per nome
    retrieveVehicle118ByCode(code?: string) {
        this.transportService.retrieveVehicle118ByCode(code ? code.toUpperCase() : '').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento dei mezzi del 118');
            return [];
        }).subscribe(res => {
            this.vehicles.next(res.resultList);
        });

    }


    //carica la lista delle categorie sanitarie
    selectSanitaryCategory118Web() {
        this.turnService.selectSanitaryCategory118Web().catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle categorie sanitarie');
            return [];
        }).subscribe(res => {
            this.sanitaryCategories.next(res.resultList);
        });

    }


    //carica la convenzione di default (code = SEC)
    public setSecConvention118() {
        this.turnService.selectConvention118('SEC').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento della convention SECONDARIO');
            return [];
        }).subscribe(res => {
            if (res && res.resultList && res.resultList.length==1){
                this.selectedConvention={"id":res.resultList[0].id,"description":res.resultList[0].description,"code":res.resultList[0].code};
                this.turn.conventionId = this.selectedConvention.id;
            }
        });

    }

    //carica la lista delle convention del 118
    public selectConvention118() {
        this.turnService.selectConvention118('').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle convention');
            return [];
        }).subscribe(res => {
            this.conventions.next(res.resultList);
        });

    }

    vehicleSelected(value: Vehicle118): void {
        if (value) {
            console.log('selectedVehicle = '+value);
            this.turn.vehicleId=value.id;
            this.turn.vehicleCode=value.code;
        }else {
            this.turn.vehicleId=null;
            this.turn.vehicleCode=null;
        }
    }

    sanitaryCategorySelected(value: SanitaryCategory118): void {
        if (value) {
            console.log('selectedSanitaryCategory = '+value);
            this.turn.sanitaryCategoryId=value.id;
            //this.turn.sanitaryCategoryCode=value.code;
        }else {
            this.turn.sanitaryCategoryId=null;
            //this.turn.sanitaryCategoryCode=null;
        }
    }

    conventionSelected(value: Convention118): void {
        if (value) {
            console.log('selectedConvention = '+value);
            this.turn.conventionId=value.id;
            //this.turn.conventionCode=value.code;
        }else {
            this.turn.conventionId=null;
            //this.turn.conventionCode=null;
        }
    }

    startDateSelected(value: NgbDateStruct): void {
        if (value) {
            this.turn.startTurnDate=convertToDate(value, "");
        }else {
            this.turn.startTurnDate = null;
        }
    }

    endDateSelected(value: NgbDateStruct): void {
        if (value) {
            this.turn.endTurnDate=convertToDate(value, "");
        }else {
            this.turn.endTurnDate = null;
        }
    }

    clean() {
        this.setSecConvention118();
        this.update = false;
        this.selectedSanitaryCategory=null;
        this.selectedConvention=null;
        this.selectedVehicle=null;
        this.startDateStruct=null;
        this.endDateStruct=null;
        this.parkingVehicleTurnId=null;
        this.currentVehicleCode=null;
        this.turn = {
            //startTurnTime:null,
            //endTurnTime:null,
            //startTurnDate:null,
            //endTurnDate:null,
            //vehicleId:null,
            //vehicleCode:null,
            //sanitaryCategoryId:null,
            //conventionId:null,
            //parkingCode:null,
            //note:null,
            //id: null
        }; 
        //valorizzo gli orari di default
        this.turn.startTurnTime ='00:00:00';
        this.turn.endTurnTime ='23:59:59';
        //indico all'header che è un nuovo turno
        if (this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')) {
            // passo l'oggetto all'header
            (<any>this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')).fromItemToBannerModel();
        }
    }


    manageTurn() {
        //controlli sui valori passati in input
        if (this.turn.startTurnDate && this.turn.startTurnTime){
            if (this.turn.endTurnDate && this.turn.endTurnTime){    
                if (!this.checkValid()) {
                    this.componentService.getRootComponent().unmask();
                    return
                };
                if (moment(this.turn.startTurnDate).isBefore(moment(this.turn.endTurnDate))){
                    if (this.turn.vehicleId){
                        if (this.turn.parkingCode){
                            if (this.turn.conventionId){
                                this.comp.getRootComponent().mask();
                                // invio la richiesta al server   
                                if (this.update){
                                    this.turn.id = this.parkingVehicleTurnId;
                                    this.turn.parkingId = null; //lo svuoto così da prendere l'id a partire dal code
                                    this.turnService.updateTurn(this.turn).subscribe(res => {
                                    this.comp.getRootComponent().unmask();
                                    if (res.checkCollidedTurnResult){
                                        if (res.checkCollidedTurnMessage){
                                            this.componentService.getRootComponent().showModal('Attenzione', res.checkCollidedTurnMessage);
                                        }
                                    }else {
                                        this.turn = res.turn;
                                        if (this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')) {
                                            // passo l'oggetto all'header
                                            (<any>this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')).fromItemToBannerModel(this.turn);
                                        }
                                    }
                                    });
                                }else {
                                    this.turnService.insertTurn(this.turn).subscribe(res => {
                                    this.comp.getRootComponent().unmask();
                                    if (res.checkCollidedTurnResult){
                                        if (res.checkCollidedTurnMessage){
                                            this.componentService.getRootComponent().showModal('Attenzione', res.checkCollidedTurnMessage);
                                        }
                                    }else {
                                        //setto al turno l'id creato dal server
                                        this.turn = res.turn;
                                        this.parkingVehicleTurnId = res.parkingVehicleTurnId;
                                        this.update = true;
                                        if (this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')) {
                                            // passo l'oggetto all'header
                                            (<any>this.componentService.getHeaderComponent('gestioneTurnoHeaderComponent')).fromItemToBannerModel(this.turn);
                                        }
                                        this.componentService.getRootComponent().showToastMessage('success', "Salvataggio del turno avvenuto con successo");
                                    }
                                    });
                                }   
                            }else {
                                var message = 'Selezionare una convenzione.';
                                this.componentService.getRootComponent().showModal('Attenzione', message);
                            }
                        }else {
                            var message = 'Selezionare una postazione dalla lista postazioni.';
                            this.componentService.getRootComponent().showModal('Attenzione', message);
                        }
                    }else {
                        var message = 'Selezionare un mezzo dalla lista mezzi.';
                        this.componentService.getRootComponent().showModal('Attenzione', message);
                    }
                }else {
                    var message = 'Definire un intervallo di tempo valido per il turno.';
                    this.componentService.getRootComponent().showModal('Attenzione', message);
                }
            }else {
                var message = 'Definire data/ora di inizio turno validi.';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
        }else {
            var message = 'Definire data/ora di inizio turno validi.';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }         
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

export function cleanMessageList(actual) {
    var newArray = new Array();
    for (var i = 0; i < actual.length; i++) {
        if (newArray.indexOf(actual[i].trim())<0) {
            newArray.push(actual[i].trim());
        }
    }
    return newArray;
}