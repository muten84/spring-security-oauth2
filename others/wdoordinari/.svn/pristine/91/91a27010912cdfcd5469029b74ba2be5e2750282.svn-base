import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ComponentHolderService } from "app/service/shared-service";
import {TaskDTO, TurnCrewMembersDTO, TurnRequestDTO, CrewMemberFilterDTO, CrewMemberDTO } from "services/services.module";
import { Subject } from "rxjs/Subject";
import { Router } from "@angular/router";
import * as moment from 'moment';
import { TurnModuleServiceService } from "services/services.module";
import { Observable } from 'rxjs/Observable';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';
import { CoreTableColumn, CoreTableComponent } from "app/core/table/core-table/core-table.component";
import { MessageService, Subscription } from '../service/message.service';
import { EVENTS } from '../util/event-type';
import { TokenService } from '../service/token.service';
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { CrewMembersModalComponent } from "../modals/turn-modal/crew-members-modal-component";


@Component({
    selector: 'gestione-equipaggio-mezzo-component',
    templateUrl: './gestione-equipaggio-mezzo-component.html',
    styleUrls: ['./gestione-equipaggio-mezzo-component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class GestioneEquipaggioMezzoComponent implements OnInit {

    parkingVehicleTurnId: string;
    public currentVehicleCode: string;
    note: string;
    filter: CrewMemberFilterDTO = {};
    public tasks: Subject<Array<any>>;
    selectedCrewMember: CrewMemberDTO;
    selectedTurnCrewMember: TurnCrewMembersDTO = {};
    startTurnDateStr: string;
    endTurnDateStr: string;

    listTurnCrewMembers: Array<TurnCrewMembersDTO>;

    turnSubscription: Subscription;
    private currentUser;

    constructor(
        private route: ActivatedRoute,
        private componentService: ComponentHolderService,
        private router: Router,
        private turniService: TurnModuleServiceService,
        private fgs: FormGroupGeneratorService,
        private messageService: MessageService,
        private tokenService: TokenService,
        private modalService: NgbModal,

    ) {
        this.tasks = new Subject<Array<any>>();
    }

    ngOnInit(): void {
        //carico la combo dei task
        this.selectTask();
        this.parkingVehicleTurnId = this.route.snapshot.params['id'];
        if (this.parkingVehicleTurnId){
            this.getTurnCrewMembersByTurnId(this.parkingVehicleTurnId);
            this.selectedTurnCrewMember.turn = {}; 
            this.selectedTurnCrewMember.turn.id = this.parkingVehicleTurnId;
            let queryParam = this.route.snapshot.queryParams;
            if(queryParam){
                this.currentVehicleCode = queryParam.vehicleCode;
                this.startTurnDateStr = queryParam.startTurnDateStr;
                this.endTurnDateStr = queryParam.endTurnDateStr;
            }
        }
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


    getTurnCrewMembersByTurnId(parkingVehicleTurnId:string){
        this.componentService.getRootComponent().mask();
        let request = {
            parkingVehicleTurnId: parkingVehicleTurnId
        };
        this.turniService.getTurnCrewMembersByTurnId(request).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            //elaboro la lista dei membri dell'equipaggio
            this.listTurnCrewMembers = res;
            this.componentService.getRootComponent().unmask();
        });
    }

    selectTask() {
        this.turniService.selectTask().catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle mansioni');
            return [];
        }).subscribe(res => {
            this.tasks.next(res);
        });
    }

    taskSelected(value: TaskDTO): void {
        if (value) {
            this.filter.task= value.description;
        }else {
            this.filter.task= null;
        }
    }

    loadCrewMembers() {
        //apro la maschera che ricerca i membri secondo il filtro impostato
        let modal = this.modalService.open(CrewMembersModalComponent, { size: 'lg' });
        modal.componentInstance.filter = this.filter;
        modal.result.then((result) => {
            if (result){
                this.selectedCrewMember = result;
                this.selectedTurnCrewMember.crewMember = result;
                //carico il membro nei filtri
                this.filter.name = result.name;
                this.filter.surname = result.surname;
                this.filter.task = result.task;
            }
        }, (reason) => {
        });    
    }

    // evento di selezione della griglia
    selected(selected: TurnCrewMembersDTO) {
        this.selectedTurnCrewMember = selected;
        this.filter.name = selected.name;
        this.filter.surname = selected.surname;
        this.filter.task = selected.task;
    }

    insertTurnCrewMember() {
        if (this.selectedTurnCrewMember && (this.filter.name || this.filter.surname)){
            this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                "Confermi l'associazione di questo componente all'equipaggio del mezzo? ").then((result) => {     
                    this.selectedTurnCrewMember.name = this.filter.name;
                    this.selectedTurnCrewMember.surname = this.filter.surname;
                    this.selectedTurnCrewMember.task = this.filter.task;
                    this.componentService.getRootComponent().mask();
                    this.turniService.insertTurnCrewMember(this.selectedTurnCrewMember).catch((e, o) => {
                        this.componentService.getRootComponent().unmask();
                        return [];
                    }).subscribe(res => {
                      //ricarico l'equipaggio
                        this.getTurnCrewMembersByTurnId(this.selectedTurnCrewMember.turn.id);
                        this.clean();
                        //this.componentService.getRootComponent().unmask();
                    });
                    
                }, (reason) => {
                    this.componentService.getRootComponent().unmask();
                    return;
                });
        }else {
            var message = 'Inserire un componente di equipaggio valido!';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
    }

    removeTurnCrewMember() {
        if (this.selectedTurnCrewMember && this.selectedTurnCrewMember.id){
            this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                 "Sei sicuro di voler cancellare il componente dell'equipaggio del mezzo? ").then((result) => {
                    this.componentService.getRootComponent().mask();
                    let request = {
                        turnCrewMemberId: this.selectedTurnCrewMember.id,
                        parkingVehicleTurnId: this.parkingVehicleTurnId
                    };
                    this.turniService.removeTurnCrewMember(request).catch((e, o) => {
                        this.componentService.getRootComponent().unmask();
                        return [];
                    }).subscribe(res => {
                        //ricarico l'equipaggio
                        this.getTurnCrewMembersByTurnId(this.selectedTurnCrewMember.turn.id);
                        this.clean();
                        //this.componentService.getRootComponent().unmask();
                    });               
            }, (reason) => {
                 this.componentService.getRootComponent().unmask();
                 return;
            });
        }else {
            var message = 'Selezionare un componente dell\'equipaggio';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
    }

    updateTurnCrewMember() {
        if (this.selectedTurnCrewMember && this.selectedTurnCrewMember.id){
            this.componentService.getRootComponent().showConfirmDialog('Attenzione!',
                "Confermi l'aggiornamento del componente dell'equipaggio del mezzo? ").then((result) => {
                    let name = this.filter.name;
                    let surname = this.filter.surname;
                    if ((!name || name.length==0) && (!surname || surname.length==0) )  {
                        var message = 'Inserire un componente di equipaggio valido!';
                        this.componentService.getRootComponent().showModal('Attenzione', message);
                    }else {
                        this.componentService.getRootComponent().mask();
                        this.selectedTurnCrewMember.name = name;
                        this.selectedTurnCrewMember.surname = surname;
                        this.selectedTurnCrewMember.task = this.filter.task;
                        this.turniService.insertTurnCrewMember(this.selectedTurnCrewMember).catch((e, o) => {
                            this.componentService.getRootComponent().unmask();
                            return [];
                        }).subscribe(res => {
                            //ricarico l'equipaggio
                            this.getTurnCrewMembersByTurnId(this.selectedTurnCrewMember.turn.id);
                            this.clean();
                            //this.componentService.getRootComponent().unmask();
                        });
                    }               
                }, (reason) => {
                    this.componentService.getRootComponent().unmask();
                    return;
                });
        }else {
            var message = 'Selezionare un componente dell\'equipaggio';
            this.componentService.getRootComponent().showModal('Attenzione', message);
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
                    this.componentService.getRootComponent().showModal('Attenzione!', "Il turno "+this.currentVehicleCode+" è stato '" + this.decodeActionSynchForMessage(msg.data.action) + "' dall'utente " + msg.data.from + ". \nIl sistema viene aggiornato.");
                    switch (msg.data.action) {
                        case "DELETE":
                            //ritorno al sinottico dei turni
                            this.router.navigate(['/sinottico-turni']);
                            break;
                        case "UPDATE":
                            //ricarico dal server l'equipaggio del turno
                            //this.clean();
                            this.getTurnCrewMembersByTurnId(parkingVehicleTurnId);
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

    
    clean() {
        this.note=null;
        this.filter={};
        this.selectedCrewMember=null;
        this.selectedTurnCrewMember={};
        this.selectedTurnCrewMember.turn={};
        this.selectedTurnCrewMember.turn.id=this.parkingVehicleTurnId;
        this.selectTask=null;

    }



   
    //griglia dei membri dell'equipaggio
    public columnsTurnCrewMembers: Array<CoreTableColumn> = [
        { title: 'Cognome', name: 'surname', style: { "flex-grow": "20", } },
        { title: 'Nome', name: 'name', style: { "flex-grow": "15", } },
        { title: 'Mansione', name: 'task', style: { "flex-grow": "65" } }
    ];

    public configTurnCrewMembersTable = {
        paging: true,
        sorting: { columns: this.columnsTurnCrewMembers }
    };

    
}