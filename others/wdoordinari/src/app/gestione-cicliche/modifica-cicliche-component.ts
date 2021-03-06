import { Component, OnInit, ViewEncapsulation, HostListener } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ComponentHolderService } from "app/service/shared-service";
import { CiclicalDaysInfoDTO, CiclicalDaysInfoHistDTO, CiclicalRequestDTO,CiclicalFullDTO,AddressBookingDTO } from "services/services.module";
import { CalendarEvent } from "calendar-utils";
import { CalendarEventTimesChangedEvent, CalendarMonthViewDay, DAYS_OF_WEEK } from "angular-calendar";
import { Subject } from "rxjs/Subject";
import { convertToDate, sameDay,valueToSelect, ngbStructToDate, dateToNgbStruct } from "app/util/convert";
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import { Router } from "@angular/router";
import * as moment from 'moment';
import { CiclicalModuleServiceService } from "services/services.module";
import { Observable } from 'rxjs/Observable';
import { StaticDataService } from '../static-data/cached-static-data';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';
import { CoreTableColumn, CoreTableComponent } from "app/core/table/core-table/core-table.component";
import { MessageService, Subscription } from '../service/message.service';
import { EVENTS } from '../util/event-type';
import { TokenService } from '../service/token.service';
import { RouterComponent } from '../core/routing/router-component';
import * as _ from 'lodash';



// è stato utilizzato il calendario:
// https://mattlewis92.github.io/angular-calendar/
@Component({
    selector: 'modifica-cicliche-component',
    templateUrl: './modifica-cicliche-component.html',
    styleUrls: ['./modifica-cicliche-component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class ModificaCiclicheComponent extends RouterComponent implements OnInit {

    ciclicalId: string;
    bookingCode: string; //è il trasportato
    fromDateStr: string;
    toDateStr: string;
    fromDate: Date;
    toDate: Date;
    fromDateEffStr: string;
    toDateEffStr: string;
    //Configurazioni per il calendario
    viewDate: Date = new Date();
    events: CalendarEvent[] = [];
    refresh: Subject<any> = new Subject();
    activeDayIsOpen: boolean = false;
    days: Array<Date> = [];
    view = 'month';
    locale = 'it';
    weekStartsOn: number = DAYS_OF_WEEK.MONDAY;
    weekendDays: number[] = [DAYS_OF_WEEK.SUNDAY, DAYS_OF_WEEK.SATURDAY];
    faseItems: Observable<Array<any>>;

    //intervallo temporale per le modifiche ai giorni della ciclica
    intervalFrom: NgbDateStruct;
    intervalTo: NgbDateStruct;

    //dati del giorno selezionato
    phase: string;
    noteModifica: string;
    orario: string;
    destinazione: string;
    //campi della destinazione
    endCompoundAddress:string;
    endStreetName:string;
    endHouseNumber:string;
    endMunicipality:string;
    endLocality:string;
    endProvince:string;
    endType:string;
    endConvention:string;
    endDescription:string;
    endDetail:string;
    endReference:string;
    endVat:string;
    endCc:string;
    endAuthorityId:string;
    endDepartmentId:string;
    endPavilionNumber:string;
    endPavilionName:string;
    //fine campi della destinazione

    //array di ciclicalDays della ciclica
    ciclicalDays: Array<any> = [];

    currentDate = new Date();
    //giorno del calendario cliccato
    clickedDay: Date;

    //min e max ciclicalDaysInfo della ciclica
    minCiclicalDay: CiclicalDaysInfoDTO;
    maxCiclicalDay: CiclicalDaysInfoDTO;
    

    ciclical: CiclicalFullDTO;

    //booleani vari
    endAddressModify = false;
    multipleDays = false;
    enableBtn = true;
    disableBtn = true;
    addBtn = true;
    modifyBtn = true;
    endAddressBtn = false;

    data;

    //lista delle infoHistory del giorno selezionato della ciclica
    listDaysInfoHistory: Array<CiclicalDaysInfoHistDTO>;

    //addressBookingDTO del giorno selezionato della ciclica (inizializzato appena entro nella pagina)
    value: AddressBookingDTO={municipality:{},province:{}};
    clickedCiclicalDaysInfo:CiclicalDaysInfoDTO = {
        endAddress: this.value
    };

    /* FormGroup per la validazione */
    ciclicheFG: FormGroup;

    ciclicalSubscription: Subscription;
    private currentUser;

    constructor(
        private route: ActivatedRoute,
        private componentService: ComponentHolderService,
        router: Router,
        private ciclicalService: CiclicalModuleServiceService,
        private sdSvc: StaticDataService,
        private fgs: FormGroupGeneratorService,
        private messageService: MessageService,
        private tokenService: TokenService,

    ) {
        super(router);
        this.ciclicheFG = this.fgs.getFormGroup('ciclica');
    }

   /* @HostListener("keypress", ["$event"])
    public onClick(event: KeyboardEvent): void {
        if (event.keyCode === 13) {
            event.preventDefault();
        }
    }*/
    
    ngOnInit(): void {
        super.ngOnInit(); 
        this.ciclicalSubscription = this.messageService.subscribe(EVENTS.CICLICAL);
        this.ciclicalSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });    
    }

    loadData():void{
        //carico la combo delle fasi
        this.faseItems = this.sdSvc.getStaticDataByType('PHASE').map(valueToSelect);
        this.ciclicalId = this.route.snapshot.params['id'];//è il ciclicalId
        if (this.ciclicalId){
            let ciclicalToGet = {
                ciclicalId: this.ciclicalId,
                dayDate:this.currentDate
            };
            this.getCiclical(ciclicalToGet);
           
        }
        //recupero l'utente loggato
        if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }
       
    }

    //recupero la ciclica in modifica
    getCiclical(ciclicalToGet: any){
        this.componentService.getRootComponent().mask();
        this.ciclicalService.getCiclical(ciclicalToGet).catch((e, o) => {
             this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            this.ciclical = res;
            this.bookingCode = res.bookingCode; //è il trasportato
            this.fromDateStr = this.formatDate(res.fromDate, 'DD/MM/YY');   
            this.toDateStr = this.formatDate(res.toDate, 'DD/MM/YY');   
            this.fromDate = res.fromDate;
            this.toDate = res.toDate;
            //carico in ciclicalDays i giorni della ciclica
            var i = 0;
            res.ciclicalDaysInfo.forEach(d => {
                var elem = {date:(<CiclicalDaysInfoDTO>d).dayDate, status:d.status, id:d.id};
                this.ciclicalDays[i] = elem;
                i++;
            });
            //recupero la data min e max dell'intervallo della ciclica
            this.minCiclicalDay = res.minCiclicalDayInfo; 
            this.maxCiclicalDay = res.maxCiclicalDayInfo;
            this.fromDateEffStr = this.formatDate(this.minCiclicalDay.dayDate, 'DD/MM/YY');
            this.toDateEffStr = this.formatDate(this.maxCiclicalDay.dayDate, 'DD/MM/YY');
           //effettuo il refresh del calendario
            this.refresh.next();

            if (this.componentService.getHeaderComponent('gestioneCiclicheHeaderComponent')) {
                // passo oggetto vuoto all'header
                (<any>this.componentService.getHeaderComponent('gestioneCiclicheHeaderComponent')).fromItemToBannerModel(this.ciclical);
            }
           
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
                    actionDec = "CANCELLATA";
                    break;
                case "UPDATE":
                    actionDec = "AGGIORNATA";
                    break;
                default:
                    actionDec = "AGGIORNATA";
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
                let ciclicalId: string = null;
                //recupero l'id della ciclica
                ciclicalId = msg.data.payload;
                if (ciclicalId==this.ciclicalId){
                    this.ciclicalId = ciclicalId;
                    let ciclicalToGet = {
                        ciclicalId: this.ciclicalId
                    };
                    this.componentService.getRootComponent().showModal('Attenzione!', "La ciclica "+this.bookingCode+" è stata '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                    switch (msg.data.action) {
                        case "DELETE":
                            //ritorno al sinottico delle cicliche
                            this.router.navigate(['/sinottico-cicliche']);
                            break;
                        case "UPDATE":
                            //ricarico dal server la ciclica
                            this.cleanInfo();
                            this.ciclicalDays = [];
                            this.getCiclical(ciclicalToGet);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
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

     

    ngOnDestroy() {
        super.ngOnDestroy();
        this.ciclicalSubscription.observ$.unsubscribe();
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.ciclicheFG);
    }

    public formatDate(date: Date, format: string) {
        if (date == null) {
            return '';
        }
        return moment(date).format(format);
    }

    //metodo per aggiungere un nuovo giorno alla ciclica, selezionato dal calendario
    aggiungi() {
        if (!this.clickedDay){
            var message = 'Selezionare almeno un giorno dal calendario';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }else {
            //se è già un giorno della ciclica, erroe
            //if (this.isCiclicalDay(this.clickedDay)){
               // var message = 'Operazione non consentita per il giorno selezionato';
               // this.componentService.getRootComponent().showToastMessage('error', message);
            //}else {
                if (moment(this.clickedDay).isAfter(moment(this.currentDate),'day')){
                    if (!moment(this.clickedDay).isSame(moment(this.currentDate),'day')){
                        this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                            "Si è sicuri di aggiungere il giorno alla ciclica? ").then((result) => {
                                var toAdd = {
                                    ciclicalId: this.ciclicalId,
                                    dayDate: this.clickedDay ,
                                    note:this.noteModifica,
                                    status:"MOD"
                                };
                                this.componentService.getRootComponent().mask();
                                this.ciclicalService.addCiclicalDaysInfo(toAdd).catch((o, t) => {
                                    this.componentService.getRootComponent().unmask();
                                    return [];
                                }).subscribe(res => {
                                    this.ciclical = res;
                                    this.ciclicalDays = [];
                                    //RIcarico in ciclicalDays i giorni della ciclica
                                    var i = 0;
                                    res.ciclicalDaysInfo.forEach(d => {
                                        var elem = {date:(<CiclicalDaysInfoDTO>d).dayDate, status:d.status, id:d.id};
                                        this.ciclicalDays[i] = elem;
                                        i++;
                                    });
                                    //effettuo il refresh del calendario
                                    this.refresh.next();
                                    //setto le info del giorno
                                    this.setDaysInfo();

                                    this.componentService.getRootComponent().unmask();
                                });
                        }, (reason) => {
                            console.log("Giorno non aggiunto alla ciclica");
                            this.componentService.getRootComponent().unmask();
                            return;
                        });
                    }else {
                        var message = 'Non è possibile creare un\'occorenza per il giorno stesso del trasporto.';
                        this.componentService.getRootComponent().showModal('Attenzione', message);
                    }
                }else {
                    var message = 'Non é possibile creare un\'occorenza per giorni antecedenti.';
                    this.componentService.getRootComponent().showModal('Attenzione', message);
                }
            //}
            
        }
    }

    //metodo per modificare il giorno della ciclica selezionato o i giorni compresi nell'intervallo selezionato
    modifica() {
        //fase, orario e destinazione sono obbligatori
        if (!this.phase){
            var message = 'Fase non valida';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        else if (!this.destinazione){
            var message = 'Destinazione non valida';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        else if (!this.orario){
            var message = 'Orario non valido';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        else if (this.intervalFrom && this.intervalTo && 
            (
            ngbStructToDate(this.intervalFrom,'DD/MM/YYYY').isBefore(moment(this.fromDate),'day')
            ||
            ngbStructToDate(this.intervalFrom,'DD/MM/YYYY').isAfter(moment(this.toDate),'day')
            ||
            ngbStructToDate(this.intervalTo,'DD/MM/YYYY').isBefore(moment(this.fromDate),'day')
            ||
            ngbStructToDate(this.intervalTo,'DD/MM/YYYY').isAfter(moment(this.toDate),'day')
            )
            ){
            var message = 'L\'intervallo temporale inserito non è compreso in quello di creazione della ciclica';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        else {
            if (!this.checkValid()) {
                this.componentService.getRootComponent().unmask();
                return
            };
        
            this.multipleDays = false;
            var toModify : CiclicalRequestDTO={};
            //se non ho selezionato alcun giorno dal calendario, nè impostato l'intervallo
            if (!this.clickedDay && !this.intervalFrom && !this.intervalTo){
                var message = 'Questa operazione agisce su un intervallo temporale o su un giorno specifico. \n Valorizzare data inizio e fine intervallo o selezionare un giorno dal calendario.';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
            //intervallo non valido
            else if (this.intervalFrom && !this.intervalTo){
                var message = 'Data Fine Intervallo non valida';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
            //intervallo non valido
            else if (!this.intervalFrom && this.intervalTo){
                var message = 'Data Inizio Intervallo non valida';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            } 
            //intervallo non valido
            else if (this.intervalFrom && this.intervalTo){
                if (ngbStructToDate(this.intervalFrom,'DD/MM/YYYY').isAfter(ngbStructToDate(this.intervalTo,'DD/MM/YYYY'),'day')){
                    var message = 'Intervallo non corretto';
                    this.componentService.getRootComponent().showModal('Attenzione', message);
                }
                else {
                    //intervallo valido
                    toModify.fromDate = convertToDate(this.intervalFrom, "");
                    toModify.toDate = convertToDate(this.intervalTo, "");
                    toModify.ciclicalId =this.ciclicalId;
                    this.multipleDays = true;
                }
            }
            //giorno seleionato dal calendario
            else if (this.clickedDay){
                //se non è già un giorno della ciclica, erroe
                //if (!this.isCiclicalDay(this.clickedDay)){
                    //var message = 'Operazione non consentita per il giorno selezionato';
                    //this.componentService.getRootComponent().showToastMessage('error', message);
                //}
                //primo e ultimo giorno dell'intervallo della ciclica non può essere modificato
                //else if (this.isFirstOrLastDay(this.clickedDay)){
                    //var message = 'Operazione non consentita per il giorno selezionato';
                    //this.componentService.getRootComponent().showToastMessage('error', message);
                //}
                //else {
                    toModify.fromDate = this.clickedDay;
                    toModify.toDate = this.clickedDay;
                    toModify.ciclicalId =this.ciclicalId;
                //}
            }
            //se ho creato toModify
            if (toModify && toModify.ciclicalId){
                this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                "Si è sicuri di modificare il giorno/i della ciclica? ").then((result) => {
                    //setto gli altri parametri a toModify
                    toModify.phase= parseInt(this.phase);
                    toModify.time= this.orario;
                    toModify.note=this.noteModifica;
                    toModify.status="MOD";
                    toModify.endCompoundAddress=this.endCompoundAddress;
                    toModify.endStreetName=this.endStreetName;
                    toModify.endHouseNumber=this.endHouseNumber;
                    toModify.endMunicipality=this.endMunicipality;
                    toModify.endLocality=this.endLocality;
                    toModify.endProvince=this.endProvince;
                    toModify.endType=this.endType;
                    toModify.endConvention=this.endConvention;
                    toModify.endDescription=this.endDescription;
                    toModify.endDetail=this.endDetail;
                    toModify.endReference=this.endReference;
                    toModify.endVat=this.endVat;
                    toModify.endCc=this.endCc;
                    toModify.endAuthorityId=this.endAuthorityId;
                    toModify.endDepartmentId=this.endDepartmentId;
                    toModify.endPavilionNumber=this.endPavilionNumber;
                    toModify.endPavilionName=this.endPavilionName;
                    
                    this.modifyCiclicalDaysInfo(toModify);
                }, (reason) => {
                    console.log("Giorno/i della ciclica non modificati");
                    this.componentService.getRootComponent().unmask();
                    return;
                });
            }
        }
    } 

    //metodo per abilitare il giorno della ciclica selezionato o i giorni compresi nell'intervallo selezionato
    abilita() {
        this.multipleDays = false;
        var toEnable : CiclicalRequestDTO={};
        //se non ho selezionato alcun giorno dal calendario, nè impostato l'intervallo
        if (!this.clickedDay && !this.intervalFrom && !this.intervalTo){
            var message = 'Questa operazione agisce su un intervallo temporale o su un giorno specifico. \n Valorizzare data inizio e fine intervallo o selezionare un giorno dal calendario.';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        //intervallo non valido
        else if (this.intervalFrom && !this.intervalTo){
            var message = 'Data Fine Intervallo non valida';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        //intervallo non valido
        else if (!this.intervalFrom && this.intervalTo){
            var message = 'Data Inizio Intervallo non valida';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        } 
        //intervallo non valido
        else if (this.intervalFrom && this.intervalTo){
            if (ngbStructToDate(this.intervalFrom,'DD/MM/YYYY').isAfter(ngbStructToDate(this.intervalTo,'DD/MM/YYYY'),'day')){
                var message = 'Intervallo non corretto';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
            else {
                //intervallo valido
                toEnable.fromDate = convertToDate(this.intervalFrom, "");
                toEnable.toDate = convertToDate(this.intervalTo, "");
                toEnable.ciclicalId =this.ciclicalId;
                this.multipleDays = true;
            }
        }
        //giorno seleionato dal calendario
        else if (this.clickedDay){
            //se non è già un giorno della ciclica, erroe
            /*if (!this.isCiclicalDay(this.clickedDay)){
                var message = 'Operazione non consentita per il giorno selezionato';
                this.componentService.getRootComponent().showToastMessage('error', message);
            }
            //primo e ultimo giorno dell'intervallo della ciclica non può essere modificato
            else if (this.isFirstOrLastDay(this.clickedDay)){
                var message = 'Operazione non consentita per il giorno selezionato';
                this.componentService.getRootComponent().showToastMessage('error', message);
            }
            else {*/
                toEnable.fromDate = this.clickedDay;
                toEnable.toDate = this.clickedDay;
                toEnable.ciclicalId =this.ciclicalId;
            //}
        }
        //se ho creato toEnable
        if (toEnable && toEnable.ciclicalId){
            var msg = null;
            if (this.multipleDays){
                msg = 'Si è sicuri di abilitare i giorni della ciclica contenuti nell\'intervallo specificato? ';
            }else {
                msg = 'Si è sicuri di abilitare il giorno della ciclica selezionato?';
            }
            this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
            msg).then((result) => {
                //setto gli altri parametri a toEnable
                toEnable.note=this.noteModifica;
                toEnable.status="ACT";
                this.modifyCiclicalDaysInfo(toEnable);
            }, (reason) => {
                console.log("Abilitazione del giorno/i della ciclica non effettuata");
                this.componentService.getRootComponent().unmask();
                return;
            });
        }
    }

    //metodo per disabilitare il giorno della ciclica selezionato o i giorni compresi nell'intervallo selezionato
    disabilita() {
        this.multipleDays = false;
        var toDisable : CiclicalRequestDTO={};
        //se non ho selezionato alcun giorno dal calendario, nè impostato l'intervallo
        if (!this.clickedDay && !this.intervalFrom && !this.intervalTo){
            var message = 'Questa operazione agisce su un intervallo temporale o su un giorno specifico. \n Valorizzare data inizio e fine intervallo o selezionare un giorno dal calendario.';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        //intervallo non valido
        else if (this.intervalFrom && !this.intervalTo){
            var message = 'Data Fine Intervallo non valida';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        //intervallo non valido
        else if (!this.intervalFrom && this.intervalTo){
            var message = 'Data Inizio Intervallo non valida';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        } 
        //intervallo non valido
        else if (this.intervalFrom && this.intervalTo){
            if (ngbStructToDate(this.intervalFrom,'DD/MM/YYYY').isAfter(ngbStructToDate(this.intervalTo,'DD/MM/YYYY'),'day')){
                var message = 'Intervallo non corretto';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
            else {
                //intervallo valido
                toDisable.fromDate = convertToDate(this.intervalFrom, "");
                toDisable.toDate = convertToDate(this.intervalTo, "");
                toDisable.ciclicalId =this.ciclicalId;
                this.multipleDays = true;
            }
        }
        //giorno seleionato dal calendario
        else if (this.clickedDay){
            //se non è già un giorno della ciclica, erroe
            /*if (!this.isCiclicalDay(this.clickedDay)){
                var message = 'Operazione non consentita per il giorno selezionato';
                this.componentService.getRootComponent().showToastMessage('error', message);
            }
            //primo e ultimo giorno dell'intervallo della ciclica non può essere modificato
            else if (this.isFirstOrLastDay(this.clickedDay)){
                var message = 'Operazione non consentita per il giorno selezionato';
                this.componentService.getRootComponent().showToastMessage('error', message);
            }
            else {*/
                toDisable.fromDate = this.clickedDay;
                toDisable.toDate = this.clickedDay;
                toDisable.ciclicalId =this.ciclicalId;
            //}
        }
        //se ho creato toDisable
        if (toDisable && toDisable.ciclicalId){
            var msg = null;
            if (this.multipleDays){
                msg = 'Si è sicuri di disabilitare i giorni della ciclica contenuti nell\'intervallo specificato? ';
            }else {
                msg = 'Si è sicuri di disabilitare il giorno della ciclica selezionato?';
            }
            this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
            msg).then((result) => {
                //setto gli altri parametri a toDisable
                toDisable.note=this.noteModifica;
                toDisable.status="DEL";
                this.modifyCiclicalDaysInfo(toDisable);
            }, (reason) => {
                console.log("Disabilitazione del giorno/i della ciclica non effettuata");
                this.componentService.getRootComponent().unmask();
                return;
            });
        }
    }

    //metodo per modificare (abiliytare, disabilitare, modificare) un giorno della ciclica o i giorni compresi in un intervallo specificato
    modifyCiclicalDaysInfo(toModify: CiclicalRequestDTO) {
        this.componentService.getRootComponent().mask();
        this.ciclicalService.modifyCiclicalDaysInfo(toModify).catch((o, t) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            this.ciclical = res;
            this.ciclicalDays = [];
            //Ricarico in ciclicalDays i giorni della ciclica
            var i = 0;
            res.ciclicalDaysInfo.forEach(d => {
                var elem = {date:(<CiclicalDaysInfoDTO>d).dayDate, status:d.status, id:d.id};
                this.ciclicalDays[i] = elem;
                i++;
            });
           //effettuo il refresh del calendario
            this.refresh.next();
            //se l'operazione ha riguardato più giorni, pulisco la maschera
            if (this.multipleDays){
                this.cleanInfo();
                this.multipleDays = false;
            }else {
                //aggiorno la tabella dell'history
                this.setDaysInfoHistory();
            }
            this.componentService.getRootComponent().unmask();
        });
    }

    //metodo per il rendering delle celle
    beforeMonthViewRender({ body }: { body: CalendarMonthViewDay[] }): void {
        var color = null;
        body.forEach(day => {
            //al giorno selezionato non gli setto il colore
            if (this.clickedDay && moment(this.clickedDay).isSame(day.date,'day')){
                color = 'cal-day-selected';
            }else {
                color = this.getColorDay(day.date);   
            }
            if (color){
                day.cssClass = color; 
            }
        });
    }

    //metodo che controlla se il giorno passato è il primo o l'ultimo dell'intervallo della ciclica
    isFirstOrLastDay(date: Date): boolean {
        var firstOrLast = false;
        if ((this.minCiclicalDay && moment(date).isSame(this.minCiclicalDay.dayDate, 'day'))
        || (this.maxCiclicalDay && moment(date).isSame(this.maxCiclicalDay.dayDate, 'day'))){ 
            firstOrLast = true;
        }
        return firstOrLast;
    }

    //metodo che controlla se il giorno selezionato è precedente il primo o successivo l'ultimo dell'intervallo della ciclica
    isBeforeFirstOrAfterLastDay(date: Date): boolean {
        var firstOrLast = false;
        if ((this.minCiclicalDay && moment(date).isBefore(this.minCiclicalDay.dayDate, 'day'))
        || (this.maxCiclicalDay && moment(date).isAfter(this.maxCiclicalDay.dayDate, 'day'))){ 
            firstOrLast = true;
        }
        return firstOrLast;
    }

    //metodo che controlla se il giorno selezionato è un giorno che appartiene all'intervallo della ciclica
    isCiclicalDay(date: Date): boolean {
        var isCiclicalDay = false;
        this.ciclicalDays.forEach(d => {
            if (moment(d.date).isSame(date, 'day')){
                isCiclicalDay = true;
            }
        });
        return isCiclicalDay;
    }

    /*isActivedDay(date: Date): boolean {
        var isActivedDay = false;
        this.ciclicalDays.forEach(d => {
            if (moment(d.date).isSame(date, 'day')){
                if (d.status=='ACT'){ 
                    isActivedDay = true;
                }
            }
        });
        return isActivedDay;
    }

    isDisabledDay(date: Date): boolean {
        var isDisabledDay = false;
        this.ciclicalDays.forEach(d => {
            if (moment(d.date).isSame(date, 'day')){
                if (d.status=='DEL'){ 
                    isDisabledDay = true;
                }
            }
        });
        return isDisabledDay;
    }

    isModifiedDay(date: Date): boolean {
        var isModifiedDay = false;
        this.ciclicalDays.forEach(d => {
            if (moment(d.date).isSame(date, 'day')){
                if (d.status=='MOD'){ 
                    isModifiedDay = true;
                }
            }
        });
        return isModifiedDay;
    }*/

    //restituisce il ciclicalDaysInfoDTO corrispondente alla data passata come input
    getCiclicalDaysInfoDTO(date: Date): CiclicalDaysInfoDTO {
        var dayInfo :CiclicalDaysInfoDTO = null;
        this.ciclicalDays.forEach(d => {
            if (moment(d.date).isSame(date, 'day')){
                dayInfo = d;
            }
        });
        return dayInfo;
    }

    //restituisce il colore del giorno del calendario
    getColorDay(date: Date): string {
        var color = null;
        
        for (var i = 0; i < this.ciclicalDays.length; i++) {
            var d = this.ciclicalDays[i];
            if (moment(d.date).isSame(date, 'day')){
                //se è attivo allora verde
                //se è sospeso allora rosso
                // se è un giorno passato allora grigio
                if (d.status=='ACT')
                    color = 'cal-day-green'; 
                else if (d.status=='DEL')
                    color = 'cal-day-red'; 
                else if (d.status=='MOD')
                    color = 'cal-day-yellow'; 
                if (moment(d.date).isBefore(moment(this.currentDate),'day')){
                    color = 'cal-day-grey'; 
                }
                break;
            }
        }
        return color
    }


    // ------------ Eventi 
    dayClicked(day: CalendarMonthViewDay): void {
        console.log("dayClicked  " + day.date);
        
        if (day.cssClass == 'cal-day-selected') {//deselezione
            this.clickedDay = null;
            this.cleanInfo();
            //refresh di tutti i giorni
            this.refresh.next();
            //abilito tutti i bottoni
            this.enableActionButton();
            this.endAddressBtn = false;
        } else {//selezione
            this.clickedDay = day.date;
            //refresh degli altri
            this.refresh.next();
            //resetto le date dell'intervallo
            this.intervalFrom = null;
            this.intervalTo = null;
            //ottengo le daysInfo del giorno selezionato
            var dayInfo = this.getCiclicalDaysInfoDTO(day.date);
            //se il giorno selezionato è precedente il primo giorno della ciclica o successivo l'ultimo giorno della ciclica
            if (this.isBeforeFirstOrAfterLastDay(this.clickedDay)){
                //disabilito tutti i bottoni
                this.disableActionButton();
            }else {
                //se il giorno selezionato è della ciclica
                if (dayInfo){
                    this.addBtn = false;//disabilito il bottone "Aggiungi"
                    //se è il primo o ultimo giorno della ciclica, disabilito anche tutti gli altri bottoni
                    //non posso eseguire alcuna azione
                    if (this.isFirstOrLastDay(this.clickedDay)){
                        this.enableBtn = false;
                        this.disableBtn = false;
                        this.modifyBtn = false;
                        this.endAddressBtn = false;
                    }else {
                        this.modifyBtn = true;
                        this.endAddressBtn = true;
                        //se è attivo
                        if (dayInfo.status=='ACT'){
                            //posso modificare e disabilitare
                            //disabilito il bottone "Abilita"
                            this.enableBtn = false;
                            this.disableBtn = true;
                        //se è disabilitato
                        }else if (dayInfo.status=='DEL'){
                            //posso modificare e abilitare
                            //disabilito il bottone "Disabilita"
                            this.enableBtn = true;
                            this.disableBtn = false;  
                        }else if (dayInfo.status=='MOD'){
                            //posso modificare e disabilitare
                            //disabilito il bottone "Disabilita"
                            this.enableBtn = false;
                            this.disableBtn = true;  
                        }
                    }
                }
                else {
                    //abilito solo il bottone per aggiungere il giorno
                    this.addBtn = true;
                    this.enableBtn = false;
                    this.disableBtn = false;
                    this.modifyBtn = false;
                    this.endAddressBtn = false;
                }
            }
            //setto le info del giorno
            this.setDaysInfo();
        }
    }


    //disabilita tutti i bottoni delle azioni possibili
    disableActionButton(){
        this.addBtn = false;
        this.enableBtn = false;
        this.disableBtn = false;
        this.modifyBtn = false;
        this.endAddressBtn = false;
    }

    //abilita tutti i bottoni delle azioni possibili
    enableActionButton(){
        this.addBtn = true;
        this.enableBtn = true;
        this.disableBtn = true;
        this.modifyBtn = true;
    }

    //setto le info e l'history del giorno seleionato
    setDaysInfo(){
        if (this.ciclical){
            var ciclicalDaysInfoList = this.ciclical.ciclicalDaysInfo;
            var found: boolean = false;
            for (var i = 0; i < ciclicalDaysInfoList.length; i++) {
                var ciclicalDaysInfo = ciclicalDaysInfoList[i];
                //se ho trovato il giorno selezionato
                if (moment(ciclicalDaysInfo.dayDate).isSame(this.clickedDay, 'day')){
                    this.clickedCiclicalDaysInfo.endAddress = ciclicalDaysInfo.endAddress;
                    found = true;
                    //parametri della destinazione
                    this.destinazione=ciclicalDaysInfo.endCompoundAddress;
                    this.phase=ciclicalDaysInfo.dateFlag.toString();
                    this.orario = this.formatDate(ciclicalDaysInfo.dayDate, 'HH:mm');
                    this.endCompoundAddress=ciclicalDaysInfo.endCompoundAddress;
                    this.endStreetName=ciclicalDaysInfo.endStreetName;
                    this.endHouseNumber=ciclicalDaysInfo.endHouseNumber;
                    this.endMunicipality=ciclicalDaysInfo.endMunicipality;
                    this.endLocality=ciclicalDaysInfo.endLocality;
                    this.endProvince=ciclicalDaysInfo.endProvince;
                    this.endType=ciclicalDaysInfo.endType;
                    this.endConvention=ciclicalDaysInfo.endConvention;
                    this.endDescription=ciclicalDaysInfo.endDescription;
                    this.endDetail=ciclicalDaysInfo.endDetail;
                    this.endReference=ciclicalDaysInfo.endReference;
                    this.endVat=ciclicalDaysInfo.endVat;
                    this.endCc=ciclicalDaysInfo.endCc;
                    this.endAuthorityId=ciclicalDaysInfo.endAuthorityId;
                    this.endDepartmentId=ciclicalDaysInfo.endDepartmentId;
                    this.endPavilionNumber=ciclicalDaysInfo.endPavilionNumber;
                    this.endPavilionName=ciclicalDaysInfo.endPavilionNumber;
                    //recupero l'history
                    let tempListDaysInfoHistory: Array<CiclicalDaysInfoHistDTO> = ciclicalDaysInfo.daysInfoHist;
                    _.sortBy(tempListDaysInfoHistory,"modificationDate");
                    let index = 0;
                    for (index = 0; index < tempListDaysInfoHistory.length; index++) {
                        tempListDaysInfoHistory[index].modificationDateStr = this.formatDate(tempListDaysInfoHistory[index].modificationDate, 'DD/MM/YY HH:mm:SS');
                    }
                    if (tempListDaysInfoHistory && tempListDaysInfoHistory.length>0){
                        this.listDaysInfoHistory = tempListDaysInfoHistory;
                    }else {
                        this.listDaysInfoHistory = null;
                    }
                    break;
                }
            }
            if (!found){
                this.cleanInfo();
            }
        }
    }

    //setto l'history del giorno selezionato nell'apposita griglia
    setDaysInfoHistory(){
        if (this.ciclical){
            var ciclicalDaysInfoList = this.ciclical.ciclicalDaysInfo;
            var found: boolean = false;
            for (var i = 0; i < ciclicalDaysInfoList.length; i++) {
                var ciclicalDaysInfo = ciclicalDaysInfoList[i];
                //se ho trovato il giorno selezionato
                if (moment(ciclicalDaysInfo.dayDate).isSame(this.clickedDay, 'day')){
                    this.clickedCiclicalDaysInfo = ciclicalDaysInfo;
                    found = true;
                    //recupero l'history
                    let tempListDaysInfoHistory: Array<CiclicalDaysInfoHistDTO> = ciclicalDaysInfo.daysInfoHist;
                    let index = 0;
                    for (index = 0; index < tempListDaysInfoHistory.length; index++) {
                        tempListDaysInfoHistory[index].modificationDateStr = this.formatDate(tempListDaysInfoHistory[index].modificationDate, 'DD/MM/YY HH:mm:SS');
                    }
                    if (tempListDaysInfoHistory && tempListDaysInfoHistory.length>0){
                        this.listDaysInfoHistory = tempListDaysInfoHistory;
                    }
                    break;
                }
            }
        }
    }

    //svuota le info del giorno
    cleanInfo() {
        this.noteModifica=null;
        this.destinazione=null;
        this.phase=null;
        this.listDaysInfoHistory = null;
        this.orario = null;
        this.intervalFrom = null;
        this.intervalTo = null;
        this.endCompoundAddress= null;
        this.endStreetName= null;
        this.endHouseNumber= null;
        this.endMunicipality= null;
        this.endLocality= null;
        this.endProvince= null;
        this.endType= null;
        this.endConvention= null;
        this.endDescription= null;
        this.endDetail= null;
        this.endReference= null;
        this.endVat= null;
        this.endCc= null;
        this.endAuthorityId= null;
        this.endDepartmentId= null;
        this.endPavilionNumber= null;
        this.endPavilionName= null;
    }

    handleEvent(action: string, event: CalendarEvent): void {
        console.log("handleEvent  " + action + " " + event);
    }

    eventTimesChanged({
        event,
        newStart,
        newEnd
      }: CalendarEventTimesChangedEvent): void {
        console.log("eventTimesChanged  " + event + " " + newStart + " " + newEnd);
    }

   
    //griglia dell'history del giorno selezionato
    public columnsDaysInfoHistory: Array<CoreTableColumn> = [
        { title: 'Data', name: 'modificationDateStr', style: { "flex-grow": "20", } },
        { title: 'Operatore', name: 'modificationUser', style: { "flex-grow": "15", } },
        { title: 'Modifiche', name: 'modificationSynthesis', style: { "flex-grow": "65" } }
    ];

    public configDaysInfoHistoryTable = {
        paging: true,
        sorting: { columns: this.columnsDaysInfoHistory }
    };

    //salva il nuovo indirizzo della destinazione
    saveAddress(address: AddressBookingDTO, type:  'end') {
        if (type === 'end') {
            // altrimenti è il ritorno
            this.clickedCiclicalDaysInfo.endAddress = address;
            this.endAddressModify = false;
            this.destinazione = address.compoundAddress;
            this.endCompoundAddress= address.compoundAddress;
            if (address.street){
                this.endStreetName= address.street.name;
            }
            this.endHouseNumber= address.civicNumber;
            if (address.municipality){
                this.endMunicipality= address.municipality.name;
            }
            if (address.locality){
                this.endLocality= address.locality.name;
            }
            if (address.province){
                this.endProvince= address.province.name;
            }
            if (address.authority){
                this.endAuthorityId= address.authority.id;
                this.endType= address.authority.type;
                this.endConvention= address.authority.convention;
                this.endDescription= address.authority.description;
                this.endVat= address.authority.vat;
                this.endReference= address.authority.reference;
                this.endCc= address.authority.costCenter;
            }
            if (address.department){
                this.endDepartmentId= address.department.id;
                this.endDetail= address.department.description;
                this.endPavilionNumber= address.department.pavilionNumber;
                this.endPavilionName= address.department.pavilionName;
                this.endReference= address.department.reference;
                this.endCc= address.department.costCenter;
            } 
        } 
    }

    searchAuthority(type:  'end') {
        // passo i dati alla finestra
        if (type === 'end') {
            // altrimenti è il ritorno
            this.endAddressModify = !this.endAddressModify;
        } 
    }

    //quando seleziono una data dell'intervallo annullo il giorno selezionato dal calendario
    dataChanged(data: any) {
        if (data){
            this.clickedDay = null;
            //abilito i bottoni delle azioni
            this.enableActionButton();
        }
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