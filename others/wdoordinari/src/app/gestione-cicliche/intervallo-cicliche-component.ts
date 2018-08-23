import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ComponentHolderService } from "app/service/shared-service";
import { CiclicalModuleServiceService } from "services/services.module";
import { CalendarEvent } from "calendar-utils";
import { CalendarEventTimesChangedEvent, CalendarMonthViewDay, DAYS_OF_WEEK } from "angular-calendar";
import { Subject } from "rxjs/Subject";
import { convertToDate, convertMomentDateToStruct, sameDay, ngbStructToDate } from "app/util/convert";
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import { Router } from "@angular/router";
import * as moment from 'moment';
import { MessageService, Subscription } from '../service/message.service';
import { EVENTS } from '../util/event-type';
import { TokenService } from '../service/token.service';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';


@Component({
    selector: 'intervallo-cicliche-component',
    templateUrl: './intervallo-cicliche-component.html'
})
export class IntervalloCiclicheComponent implements OnInit {

    bookingId: string;
    ciclicalId: string;
    bookingCode: string; //è il trasportato
    viewDate: Date = new Date();
    events: CalendarEvent[] = [];
    days: Array<Date> = [];
    intervalFrom: NgbDateStruct;
    intervalTo: NgbDateStruct;
    selectedDays: boolean[] = [];
    daysOfWeek = DAYS_OF_WEEK;
    allDay: boolean;
    ciclicalSubscription: Subscription;
    private currentUser;
    /* FormGroup per la validazione */
    ciclicheFG: FormGroup;
    


    constructor(
        private route: ActivatedRoute,
        private componentService: ComponentHolderService,
        private ciclicalService: CiclicalModuleServiceService,
        private router: Router,
        private messageService: MessageService,
        private tokenService: TokenService,
        private fgs: FormGroupGeneratorService
    ) {
        this.ciclicheFG = this.fgs.getFormGroup('intCiclica');
    }

    ngOnInit(): void {
        this.bookingId = this.route.snapshot.params['id']; //è il bookingId
        let queryParam = this.route.snapshot.queryParams;
        if(queryParam){
            if(queryParam.ciclicalId){
                let bookingCode;
                if(queryParam.bookingCode){
                    this.bookingCode = queryParam.bookingCode;
                }
                //se sto in modifica di una ciclica, la ricarico
                this.ciclicalId = queryParam.ciclicalId;
                let ciclicalToGet = {
                    ciclicalId: this.ciclicalId
                };
                //ricarico dal server la ciclica
                this.getCiclicalPeriod(ciclicalToGet);
            }  
            else if(queryParam.transportDate){
                //se ho la data di trasporto allora provendo dalla prenotazione e la setto come data da
                this.intervalFrom = convertMomentDateToStruct(moment(queryParam.transportDate,'DD/MM/YYYY'));
            }             
        }

        if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }
        this.ciclicalSubscription = this.messageService.subscribe(EVENTS.CICLICAL);
        this.ciclicalSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
        });
       
    }

    getCiclicalPeriod(ciclicalToGet: any){
        this.componentService.getRootComponent().mask();
        this.ciclicalService.getCiclicalPeriod(ciclicalToGet).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            this.intervalFrom = convertMomentDateToStruct(moment(res.fromDate));
            this.intervalTo = convertMomentDateToStruct(moment(res.toDate));
            if (res.days){
                for (let d in res.days) {
                    this.selectedDays[res.days[d]] = true;
                }
            }
            this.setCheckAll();
            if (this.componentService.getHeaderComponent('gestioneCiclicheHeaderComponent')) {
                let ciclicalModel = {
                    bookingCode: this.bookingCode
                };
                // passo oggetto all'header
                (<any>this.componentService.getHeaderComponent('gestioneCiclicheHeaderComponent')).fromItemToBannerModel(ciclicalModel);
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

    //elaboro il messaggio ricevuto
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
                            this.clean();
                            this.getCiclicalPeriod(ciclicalToGet);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    ngOnDestroy() {
        this.ciclicalSubscription.observ$.unsubscribe();
    }

    //creo un array con i giorni selezionati
    setCodeDays(selectedDays: boolean[]) {
        var daySelectedCode = [];
        var i=0;
        for (let d in DAYS_OF_WEEK) {
            if (this.selectedDays[d] == true){
                daySelectedCode[i] = d;
                i=i+1;
            }
        }
        return daySelectedCode;
    }

    //controllo se è stato selezionato almeno un giorno della settimana
    checkSelectedDays(): boolean{
        for (var i = 0; i < this.selectedDays.length; i++) {
            if (this.selectedDays[i]==true){
                //trovato almeno un giorno selezionato
                return true;
            }
        }
        return false;
    }

    salva() {
        //controllo che sia stato selezionato almeno un giorno della settimana
        //ovvero che ci siano elementi in selectedDays a true
        if (this.checkSelectedDays()){

            //controlllo che sia stato valorizzato l'intervallo di date della ciclica
            if (this.intervalFrom &&  this.intervalTo) {
                if (!this.checkValid()) {
                    this.componentService.getRootComponent().unmask();
                    return
                };   
                if (ngbStructToDate(this.intervalFrom,'DD/MM/YYYY').isAfter(ngbStructToDate(this.intervalTo,'DD/MM/YYYY'),'day')){
                    var message = 'Intervallo \'Dal giorno\' \'Al giorno\'  non corretto';
                    this.componentService.getRootComponent().showModal('Attenzione', message);
                }else {
                    //ottengo l'array dei giorni selezionati
                    let daySelectedCode = this.setCodeDays(this.selectedDays);
                    //se ho il ciclicalId vuol dire che sto in update dell'intervallo della prenotazione ciclica
                    if (this.ciclicalId){
                        var toCheck = {
                            ciclicalId: this.ciclicalId,
                            days: daySelectedCode,
                            fromDate: convertToDate(this.intervalFrom, "") ,
                            toDate:convertToDate(this.intervalTo, "")
                        };
                        this.componentService.getRootComponent().mask();
                        //controllo il periodo selezionato 
                        this.ciclicalService.checkNewCiclicalInterval(toCheck).catch((o, t) => {
                            this.componentService.getRootComponent().unmask();
                            return [];
                        }).subscribe(res => {
                            this.componentService.getRootComponent().unmask();
                            if (!res.checkNewCiclicalIntervalResult){
                                if (res.checkNewCiclicalIntervalMessage){
                                    //mostro il messaggio di conferma
                                    this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                                    res.checkNewCiclicalIntervalMessage).then((result) => {
                                        this.componentService.getRootComponent().mask();
                                        this.ciclicalService.modifyCiclicalPeriod(toCheck).catch((e, o) => {
                                            this.componentService.getRootComponent().unmask();
                                            return [];
                                        }).subscribe(res => {
                                            this.componentService.getRootComponent().unmask();
                                            this.componentService.getRootComponent().showToastMessage('success', "Modifica del periodo della prenotazione ciclica avvenuta con successo");
                                        });
                                    }, (reason) => {
                                        console.log("Modifica del periodo della prenotazione ciclica non effettuata");
                                        this.componentService.getRootComponent().unmask();
                                        return;
                                    });
                                }
                            }else {
                                this.componentService.getRootComponent().mask();
                                this.ciclicalService.modifyCiclicalPeriod(toCheck).catch((e, o) => {
                                    this.componentService.getRootComponent().unmask();
                                    return [];
                                }).subscribe(res => {
                                    this.componentService.getRootComponent().unmask();
                                    this.componentService.getRootComponent().showToastMessage('success', "Modifica del periodo della prenotazione ciclica avvenuta con successo");
                                });
                            }
                        });
                    }else {
                        //nuova ciclica
                        var toSave = {
                            bookingId: this.bookingId,
                            days: daySelectedCode,
                            fromDate: convertToDate(this.intervalFrom, "") ,
                            toDate:convertToDate(this.intervalTo, "")
                        };
                        this.componentService.getRootComponent().mask();
                        this.ciclicalService.makeBookingCiclical(toSave).catch((o, t) => {
                            this.componentService.getRootComponent().unmask();
                            return [];
                        }).subscribe(res => {
                            this.componentService.getRootComponent().unmask();
                            this.router.navigate(['/sinottico-prenotazioni']);
                        });
                    }
                }
            }else {
                var message = 'Selezionare l\'intervallo delle date';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
        } else {
          var message = 'Selezionare almeno un giorno della settimana';
          this.componentService.getRootComponent().showModal('Attenzione', message);
       }
    }

    clean() {
        this.selectedDays = [];
        this.intervalFrom = null;
        this.intervalTo = null;
        this.allDay = false;
        this.days = [];
    }


    setDate(day: Date) {
        this.viewDate = day
    }

    //metodo chiamato al click di 'Tutti i giorni'
    checkAll(event) {
        this.allDay = true;
        for (let d in DAYS_OF_WEEK) {
            this.selectedDays[d] = event;
        }
    }

    setCheckAll() {
        var isCheckAll = true;
        for (let d in DAYS_OF_WEEK) {
            if (!isNaN(Number(d))){
                if (!this.selectedDays[d] || this.selectedDays[d] == false){
                    isCheckAll = false;
                    break;
                }
            }
        }
        if (isCheckAll){
            this.allDay = true;
        }else {
            this.allDay = false;
        }
    }

    //metodo chiamato al click di un giorno della settimana
    checkDay(event, daysOfWeek: DAYS_OF_WEEK) {
        this.selectedDays[daysOfWeek] = event;
        console.log('giorni selezionati= '+this.selectedDays)
        //se ho selezionato tutti i giorni allora checkAll
       this.setCheckAll();
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

    getControl(name: string) {
        return this.fgs.getControl(name, this.ciclicheFG);
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

export function cleanMessageList(actual) {
    var newArray = new Array();
    for (var i = 0; i < actual.length; i++) {
        if (newArray.indexOf(actual[i].trim())<0) {
            newArray.push(actual[i].trim());
        }
    }
    return newArray;
}