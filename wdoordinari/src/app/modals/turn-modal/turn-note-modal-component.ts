import { Component, OnInit } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { TurnModuleServiceService, StaticData118,  TurnRequestDTO} from "services/gen";
import { Observable, BehaviorSubject } from "rxjs";
import { ComponentHolderService } from "../../service/shared-service";
import { Subject } from "rxjs";
import { Router } from "@angular/router";
import { TokenService } from '../../service/token.service';
import { MessageService, Subscription } from '../../service/message.service';
import { EVENTS } from '../../util/event-type';



@Component({
    selector: 'turn-note-modal',
    templateUrl: './turn-note-modal-component.html'
})
export class TurnNoteModalComponent implements OnInit {

    public parkingVehicleTurnId: string;

    taskText: string;

    public pause:boolean;
    public standby:boolean;
    public title:string;
    public timeLabel:string;

    //lista codici note pausa/standby
    public noteCodes: Subject<Array<any>>;
    //lista tempi pausa/standby
    public times: Subject<Array<any>>;
    //codice selezionato dalla combo
    public selectedCode: StaticData118;
    //tempo selezionato dalla combo
    public selectedTime: StaticData118;

     
     public timeDescr: string; //id del tempo selezionato
     public codeDescr: string; //id del codice selezionato
     public pauseNote: string; //note della messa in pausa/standby

     turnSubscription: Subscription;
     private currentUser;

    constructor(
        protected activeModal: NgbActiveModal,
        protected comp: ComponentHolderService,
        protected turnService: TurnModuleServiceService,
        protected componentService: ComponentHolderService,
        private router: Router,
        private tokenService: TokenService,
        private messageService: MessageService,
    ) {
        this.noteCodes = new Subject<Array<any>>();
        this.times = new Subject<Array<any>>();
    }

    ngOnInit(): void {
        if (this.pause){
            this.title='Note di Pausa del Mezzo';
            this.timeLabel='Tempo Pausa Previsto';
        }
        else if (this.standby){
            this.title='Note di Standby del Mezzo';
            this.timeLabel='Tempo Standby Previsto';
        }
        this.selectNoteCodes();
        this.selectTimes();
        //recupero l'utente loggato
        if (!this.getCurrentUser()) {
            console.log("ATTENZIONE! Utente non recuperato da token");
        }

        this.turnSubscription = this.messageService.subscribe(EVENTS.TURN);
        this.turnSubscription.observ$.subscribe((msg) => {
            setTimeout(() => { //Gestione messaggio inserita in timeout per preservare la sottoscrizione al servizo in caso di errore
                console.log('received message from topic ' + msg.topic + " - " + JSON.stringify(msg));
                this.elaboratePushMessage(msg);
            }, 0);//Chiudi timeout per preservare la sottoscrizione al servizo in caso di errore 
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

    //carica la lista dei codici delle note di pausa/standby
    selectNoteCodes() {
        let type ='';
        if (this.pause){
            type = 'COD NOTE PAUSA'
        }else if (this.standby){
            type = 'COD NOTE STANDBY'
        }
        let filter = {
            type: type
        };
        this.turnService.searchStaticDataByFilter(filter).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento dei codici delle note di pausa/standby');
            return [];
        }).subscribe(res => {
            this.noteCodes.next(res.resultList);
        });

    }


    //carica la lista dei tempi di pausa/standby
    selectTimes() {
        let filter = {
            type: 'MINUTI PAUSA'
        };
        this.turnService.searchStaticDataByFilter(filter).catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento dei tempi di pausa/standby');
            return [];
        }).subscribe(res => {
            this.times.next(res.resultList);
        });

    }

    codeSelected(value: StaticData118): void {
        if (value) {
            console.log('code selected = '+value);
            this.codeDescr=value.description;
        }else {
            this.codeDescr=null;
        }
    }

    timeSelected(value: StaticData118): void {
        if (value) {
            console.log('time selected = '+value);
            this.timeDescr=value.description;
        }else {
            this.timeDescr=null;
        }
    }

    close() {
        this.comp.getRootComponent().mask();
        // invio la lista al server
        let body: TurnRequestDTO = {
            parkingVehicleTurnId: this.parkingVehicleTurnId,
            pauseNote: this.pauseNote,
            timeDescr: this.timeDescr,
            codeDescr: this.codeDescr
        };
        if (this.pause){
            this.turnService.changePause(body).subscribe(res => {
                this.comp.getRootComponent().unmask();
                this.activeModal.close();
            });
        }else  if (this.standby){
            this.turnService.changeAvailability(body).subscribe(res => {
                this.comp.getRootComponent().unmask();
                this.activeModal.close();
            });
        }
    }

    dismiss() {
        this.activeModal.dismiss();
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
                if (parkingVehicleTurnId==this.parkingVehicleTurnId){
                    this.parkingVehicleTurnId = parkingVehicleTurnId;
                    this.componentService.getRootComponent().showModal('Attenzione!', "Il turno è stato '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                    switch (msg.data.action) {
                        case "DELETE":
                            //ritorno al sinottico dei turni
                            this.activeModal.close();
                            this.router.navigate(['/sinottico-turni']);
                            break;
                        case "UPDATE":
                            //ricarico dal server l'equipaggio del turno
                            //this.clean();
                            //this.getTurnCrewMembersByTurnId(parkingVehicleTurnId);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    ngOnDestroy() {
        this.turnSubscription.observ$.unsubscribe();
    }
}
