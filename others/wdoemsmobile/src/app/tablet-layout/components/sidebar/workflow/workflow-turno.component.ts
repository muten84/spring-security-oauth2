import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationStatus, CurrentTurn, AgentService, ApplicationStatusService, LocalBusService, Event, ResourceListService } from '../../../../service/service.module';
import { StatusItem } from '../../../../service/application/model/StatusItem';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { Subscription } from 'rxjs/Subscription';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { PhaseCommand, CheckInCommand, CheckOutCommand, Logger } from '../../../../service/service.module';

@Component({
    selector: 'app-sidebar-workflow-turno',
    templateUrl: './workflow.component.html',
    //  templateUrl: '../../../checkin/checkin.component.html',
    styleUrls: ['./workflow.component.scss']
})
export class WorkflowTurnoComponent implements OnInit, OnDestroy {

    @BlockUI('header-section') blockUI: NgBlockUI;

    isActive: boolean = false;
    showMenu: string = '';
    allStatus = [];
    currentTurn;
    activeStatusMap: Map<number, StatusItem> = new Map<number, StatusItem>();
    appStatusSubscription: Subscription;
    observableApplicationStatus: Observable<ApplicationStatus>;

    constructor(
        private agent: AgentService,
        private logger: Logger,
        public bus: LocalBusService,
        private router: Router,
        private resourceService: ResourceListService,
        private appStatus: ApplicationStatusService,
    ) {

    }


    public changeStatusList(status: Array<any>) {
        this.allStatus = status;
    }

    private initInTurnStatusList() {
        this.allStatus = [

        ]
    }

    private updateStatusList() {


        this.allStatus = [];
        let activeStatus = [];
        for (let i = 0; i < this.activeStatusMap.size; i++) {
            let type;

            let statusItem = {
                "type": type,
                "status": [{
                    label: this.activeStatusMap.get(i).label,
                    sublabel: null,
                    code: String(i)
                }]
            }

            this.allStatus.push(statusItem);

            if (this.currentTurn != undefined && this.currentTurn.length > 0) {
                if (activeStatus.length == 0) {
                    if (i == this.currentTurn.length - 1) {
                        activeStatus = this.activeStatusMap.get(this.currentTurn[i].code).activeStatus;
                    }
                    this.checkStatus(i, this.currentTurn[i].sublabel);
                } else {
                    this.cheakActiveButton(i, activeStatus);
                }
            } else if (i == 0) {
                this.enableStatus(i);
                activeStatus = [4];
            } else {
                this.cheakActiveButton(i, activeStatus);
            }
        }

    }

    cheakActiveButton(index, listActiveCode) {
        for (let d = 0; d < listActiveCode.length; d++) {
            if (index == Number(listActiveCode[d])) {
                this.enableStatus(index);
                break;
            }
            else
                this.disableStatus(index);
        }
    }

    checkStatus(index, sublabel) {
        let label = this.allStatus[index].status[0].label;
        let code = this.allStatus[index].status[0].code;
        this.allStatus[index] = {
            "type": "readonly",
            "status": [{
                label: label,
                sublabel: sublabel,
                code: code
            }]
        }
    }

    disableStatus(index) {
        if (!this.allStatus) {
            return;
        }
        if (!this.allStatus[index]) {
            return;
        }
        let label = this.allStatus[index].status[0].label;
        let code = this.allStatus[index].status[0].code;
        let sublabel = this.allStatus[index].status[0].sublabel;
        this.allStatus[index] = {
            "type": "disabled",
            "status": [{
                label: label,
                sublabel: sublabel,
                code: code
            }]
        }
    }

    enableStatus(index) {
        if (!this.allStatus) {
            return;
        }
        if (!this.allStatus[index]) {
            return;
        }
        let label = this.allStatus[index].status[0].label;
        let code = this.allStatus[index].status[0].code;
        this.allStatus[index] = {
            "type": "active",
            "status": [{
                label: label,
                code: code
            }]
        }
    }

    newStatus(s) {
        this.logger.info("new status clicked: " + s);
        switch (s.code) {
            case "0":
                this.disableCheckInButton();
                this.router.navigate(['/checkin']);
                break;
            case "1":
                this.endTurn();
                break;
            case "2":
                this.parkVehicle();
                break;
            default:
                this.logger.info("unknown status");
        }
    }


    disableCheckInButton() {
        this.disableStatus(0);
    }

    enableCheckInButton() {
        const currentStatus = this.appStatus.getCurrentStatus()
        if (currentStatus && currentStatus.currentTurn && currentStatus.currentTurn.startTurnDate) {
            return;
        }
        if (!this.allStatus || !this.allStatus[0]) {
            this.allStatus[0] = {
                "type": "active",
                "status": [{
                    label: "Inizio Turno",
                }]
            }
        }
        this.enableStatus(0);
    }



    currentHourLabel(date: Date): string {
        let h: string = "" + date.getHours();
        if (h.length == 1) {
            h = "0" + h;
        }
        let m = "" + date.getMinutes();
        if (m.length == 1) {
            m = "0" + m;
        }
        return h + ":" + m;
    }



    ngOnInit() {
        this.bus.addObserver('checkinui').subscribe((e) => {
            //console.log("on checkin: " + e.type);
            switch (e.type) {
                case 'init':
                    this.disableCheckInButton();
                    break;
                case 'destroy':
                    this.enableCheckInButton();
                    break;
            }
        })
        const as = this.appStatus.fetchCurrentStatus()
        if (as.toBeParked) {
            const parked = this.checkParked(as);
            if (parked) {
                this.disableParking();
            }
            else {
                this.enableParking();
            }
        }
        this.observableApplicationStatus = this.appStatus.getApplicationStatusObservable();
        this.appStatusSubscription = this.observableApplicationStatus.subscribe((val: ApplicationStatus) => {
            this.logger.info("in workflow turno manually subscribed to application status received: " + JSON.stringify(val));
            //this.connected = val.connectionStatus.connected;
            //this.logger.info("check in done on: " + val.currentTurn.resource);
            this.currentTurn = val.currentTurn;

            //se il turno è vuoto inizializza la bottoniera degli stati 
            this.activeStatusMap = this.turnNotStarted();
            this.updateStatusList();

            if (val.currentTurn) {
                //TODO ho il turno 
                this.turnStarted(val.currentTurn);
                //this.updateStatusList();
            }
            if (val.toBeParked) {
                const parked = this.checkParked(as);
                if (parked) {
                    this.disableParking();
                }
                else {
                    this.enableParking();
                }
            }

        });

        this.appStatus.requestApplicationStatus();
    }

    public checkParked(as: ApplicationStatus): boolean {
        let stat = undefined;
        if (!as.currentIntervention) {
            return false;
        }
        if (as.currentIntervention.checkedStatus) {
            stat = as.currentIntervention.checkedStatus.find((cs) => {
                if (cs) {
                    return cs.code === 5;
                }
            })
        }
        if (stat) {
            return true;
        }
    }

    public disableParking() {
        this.checkStatus(2, this.currentHourLabel(new Date()));

    }

    public enableParking() {
        this.enableStatus(2);
    }

    public turnStarted(turn: CurrentTurn) {
        //fixme: passare la string dell'ora di inizio turno this.currentHourLabel(turn.startTurnDate)
        this.checkStatus(0, this.currentHourLabel(new Date(turn.startTurnDate)));
        this.enableStatus(1);
    }

    public turnEnded(turn: CurrentTurn) {
        let d = new Date();
        this.checkStatus(1, this.currentHourLabel(new Date()));
        this.enableStatus(0);
    }

    public turnNotStarted(): Map<number, StatusItem> {
        let activeStatusMap: Map<number, StatusItem> = new Map<number, StatusItem>();
        let applicationStatus;
        applicationStatus = { label: 'Inizio Turno', activeStatus: [1] };
        activeStatusMap.set(0, applicationStatus);
        applicationStatus = { label: 'Fine Turno', activeStatus: [2] };
        activeStatusMap.set(1, applicationStatus);
        applicationStatus = { label: 'Parcheggio', activeStatus: null };
        activeStatusMap.set(2, applicationStatus);
        return activeStatusMap;
    }

    ngOnDestroy() {
        this.appStatusSubscription.unsubscribe();
    }

    endTurn() {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.logger.info('response dialog is: ' + JSON.stringify(event.payload));
                this.bus.removeAllObservers('confirmDialog', [s]);
                if (event.payload) {
                    this.doCheckOut();
                }
            }
        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "Sei sicuro?", content: "Procedere con la fine del turno?" } });

    }

    parkVehicle() {
        this.sendParkingStatus("5");
    }





    sendParkingStatus(s: string) {
        this.agent.sendAsynchMessage("PHS", this.createParkingCommand(s)).then(
            (success) => {
                this.appStatus.addStatusToIntervention(Number.parseInt(s), this.currentHourLabel(new Date()));
                this.appStatus.parked();
                this.disableParking();
            },
            (failure) => {
                //console.log("messagge send failure: " + failure);
                //TODO gestire fallimento invio stato all'agent, in teoria dovrebbe riprovare ad inviare lo stato
                //ma bisogna considearare che se fallisce l'invio all'agent molto probabilmente
                //ho perso la connessione.
            }
        )
    }


    private createParkingCommand(newPhase: string): PhaseCommand {
        const e = this.appStatus.getCurrentEvent();

        const pc: PhaseCommand = {
            phase: (Number.parseInt(newPhase) + 1).toString()
        };

        return pc;
    }

    private showInfoDialog(title, text) {
        this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: title, content: text } });
    }


    private doCheckOut() {
        let resource = this.appStatus.getCurrentStatus().currentTurn.resource;
        const checkout: CheckOutCommand = { rscd: resource };
        this.agent.sendSynchMessage("USB", checkout).then(
            success => {
                this.appStatus.clearCurrentTurn();
                this.router.navigate(["dump118"]);
            },
            (failed) => {
                this.appStatus.clearCurrentTurn();
                this.router.navigate(["dump118"]);
            });

    }



}
