import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

import { slideToBottom, fadeInOut, fadeOutIn } from '../../router.animations';
import { ApplicationStatus, CurrentTurn, AgentService, LocalBusService, Event, ResourceListService, ApplicationStatusService } from '../../service/service.module';
import { CheckInCommand, Logger, Message } from '../../service/service.module';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { Subscription } from 'rxjs/Subscription';

import { trigger, style, animate, transition } from '@angular/animations';
import { ListItemComponent } from '../components/components.module';
import { ConfigurationService } from '../../service/core/configuration.service';


@Component({
    selector: 'app-checkin',
    templateUrl: './checkin.component.html',
    styleUrls: ['./checkin.component.scss'],
    changeDetection: ChangeDetectionStrategy.Default,
    animations: [slideToBottom(), fadeInOut(), fadeOutIn()]
})
export class CheckInComponent implements OnInit {
    parkListSubscription: Subscription;

    vehicleListSubscription: Subscription;

    comeBack = false;

    selectedParking: any;

    selectedVehicle: any;

    parkingList: Observable<Array<any>>;

    vehicleList: Observable<Array<any>>;

    parkingConfirmed: boolean;

    openKeyboard: boolean;

    vkeyboardValue: string;

    searchFilter: string;

    turnDuration: string;

    openKeyBoardEvent: CustomEvent;

    pages: number;

    currentPage: number = 0;

    parkingsObserver: Subscriber<Array<any>>;

    vehicleObserver: Subscriber<Array<any>>;

    allItems: Array<any>;

    allVehicles: Array<any>;

    private observableApplicationStatus: Observable<ApplicationStatus>;

    appStatusSubscription: Subscription;

    currentTurn: CurrentTurn;

    itemsPerPage: number = 20;

    prevEnabled: boolean = false;

    nextEnabled: boolean = false;

    disableAnimation;

    activeTurn: boolean;

    @ViewChild('parkingListContainer')
    parkingListComponent: ListItemComponent;

    constructor(
        private logger: Logger,
        private agent: AgentService,
        private bus: LocalBusService,
        private resourceService: ResourceListService,
        private appStatus: ApplicationStatusService,
        private router: Router,
        private configService: ConfigurationService) { }

    ngOnInit() {
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.activeTurn = this.configService.getConfigurationParam("GENERAL", "ACTIVE_TURN", true);
        this.parkingConfirmed = false;
        this.disableAnimation = !enableAnimation;

        this.currentPage = 0;
        this.observableApplicationStatus = this.appStatus.getApplicationStatusObservable();
        this.appStatusSubscription = this.observableApplicationStatus.subscribe((val: ApplicationStatus) => {
            this.logger.info("in check in component manually subscribed to application status received: " + JSON.stringify(val));
            //this.connected = val.connectionStatus.connected;
            this.logger.info("check in done on: " + val.currentTurn.resource);
            this.currentTurn = val.currentTurn;

        });
        this.bus.notifyAll("masks", { type: 'mask' });
        this.addParkingListObserver();
        //this.resourceService.requestParkingList();
        this.createVehicleParkingObserver();

        /*evento per far comparire la tastiera senza un input element visibile */
        this.openKeyBoardEvent = document.createEvent("CustomEvent");
        this.openKeyBoardEvent.initCustomEvent('OpenKeyboardEvent', true, true,
            { 'origin': 'hiddenItem', 'keyboardType': 'numeric', 'target': "turnDuration" });
        const currApp = this.appStatus.getCurrentStatus();
        if (currApp.currentTurn && currApp.currentEvent) {
            this.bus.notifyAll('newIntervention', {});
        }
        this.bus.notifyAll('checkinui', { type: 'init' });

    }

    ngOnDestroy() {
        this.bus.notifyAll('checkinui', { type: 'destroy' });
        this.currentPage = 0;
        this.appStatusSubscription.unsubscribe();
        this.currentTurn = undefined;
        this.turnDuration = undefined;
        this.parkingConfirmed = false;
        this.removeParkingListObserver();
        this.removeVehicleListObserver();
    }

    itemSelected(type: string, data: any) {
        this.logger.info("parking selected: " + data);
        if (type === 'parking') {
            this.selectedParking = data;
        }
        else if (type === 'vehicle') {
            this.selectedVehicle = data;
        }
    }



    onInputChange(data) {
        this.logger.info("on input change data: " + data);
    }

    reset() {
        this.parkingConfirmed = false;
        this.comeBack = true;
        this.selectedParking = null;
        this.selectedVehicle = null;
    }


    addParkingListObserver() {
        let parkList = this.bus.addObserver('parkingList');
        this.parkListSubscription = parkList.subscribe((event: Event) => {
            this.parkingList = new Observable<Array<any>>((observer: Subscriber<Array<any>>) => {
                this.logger.info("parkingList received: " + event.payload.items);
                this.parkingsObserver = observer;
                this.bus.notifyAll("masks", { type: 'unmask' });
                this.allItems = event.payload.items;
                this.pages = Math.trunc(Math.round((this.allItems.length / this.itemsPerPage) * 100) / 100) + 1;
                if (this.pages > 1) {
                    this.prevEnabled = true;
                    this.nextEnabled = true;
                } else if (this.pages <= 1) {
                    this.prevEnabled = false;
                    this.nextEnabled = false;
                }
                this.toCurrentPage('parking');
                //observer.next(items);
            });
        });
        this.resourceService.requestParkingList();
    }


    toCurrentPage(type: string) {
        let start = Math.trunc(this.currentPage * this.itemsPerPage);
        if (this.allItems.length <= this.itemsPerPage) {
            if (type === 'parking') {
                this.parkingsObserver.next(this.allItems);
            } else if (type === 'vehicle') {
                this.vehicleObserver.next(this.allItems);
            }
            return;
        }
        let end = Math.trunc(this.currentPage + this.itemsPerPage);


        let items = this.allItems.slice(start, end);
        if (type === 'parking') {
            this.parkingsObserver.next(items);
        } else if (type === 'vehicle') {
            this.vehicleObserver.next(items);
        }
    }

    toPrevPage(type: string) {

        if (this.allItems.length <= this.itemsPerPage || this.currentPage <= 0) {
            return;
        }
        this.currentPage = Math.trunc((this.currentPage - 1) % this.pages);

        let start = Math.trunc(this.currentPage * this.itemsPerPage);
        let end = start + this.itemsPerPage;

        let items = this.allItems.slice(start, end);

        if (type === 'parking') {
            this.parkingsObserver.next(items);
        } else if (type === 'vehicle') {
            this.vehicleObserver.next(items);
        }
    }

    toNextPage(type: string) {
        if (this.allItems.length <= this.itemsPerPage) {
            return;
        }
        this.currentPage = (this.currentPage + 1) % this.pages;
        let start = this.currentPage * this.itemsPerPage;

        let end = start + this.itemsPerPage;

        let items = this.allItems.slice(start, end);

        if (type === 'parking') {
            this.parkingsObserver.next(items);
        } else if (type === 'vehicle') {
            this.vehicleObserver.next(items);
        }
    }

    createVehicleParkingObserver() {
        let vList = this.bus.addObserver('vehicleList');
        this.vehicleListSubscription = vList.subscribe((event: Event) => {
            this.vehicleList = new Observable<Array<any>>((observer: Subscriber<Array<any>>) => {
                this.logger.info("vehicleList received: " + event.payload.items);
                this.vehicleObserver = observer;
                this.allItems = event.payload.items;
                this.pages = Math.trunc(Math.round((this.allItems.length / this.itemsPerPage) * 100) / 100) + 1;
                //observer.next(event.payload.items);
                if (this.pages > 1) {
                    this.prevEnabled = true;
                    this.nextEnabled = true;
                } else if (this.pages <= 1) {
                    this.prevEnabled = false;
                    this.nextEnabled = false;
                }
                this.toCurrentPage('vehicle');
            });
        });
    }



    removeParkingListObserver() {
        this.parkingList = null;
        this.bus.removeAllObservers('parkingList', [this.parkListSubscription]);
    }

    removeVehicleListObserver() {
        this.vehicleList = null;
        this.bus.removeAllObservers('vehicleList', [this.vehicleListSubscription]);
    }

    public turnDurationChoice() {
        this.logger.info("OpenKeyboardEvent");
        if (this.activeTurn) {
            if (!this.selectedVehicle) {
                return;
            }
            if (!this.selectedVehicle.name) {
                return;
            }
            setTimeout(() => {

                document.dispatchEvent(this.openKeyBoardEvent)
            }, 100)
        } else {
            this.confirm('identity');
        }
    }

    public resetCurrentChoice(itemType: string) {
        if (itemType === 'parking') {
            this.parkingListComponent.doReset(true);
            this.selectedVehicle = undefined;
            this.parkingConfirmed = false;
            this.comeBack = false;
            this.selectedParking = null;
        } else if (itemType === 'vehicle') {
            this.selectedVehicle = undefined;
            this.parkingConfirmed = false;
            this.comeBack = true;
            this.selectedParking = null;
            this.searchFilter = null;
        }
        if (!this.comeBack) {
            window.history.back();
        }
    }

    public clean() {
        this.searchFilter = null;
        this.reset();
    }

    onVKeyboardValue(origin: any) {
        let time_turn: number = this.configService.getConfigurationParam("GENERAL", "TIME_TURN", '24');

        if (origin.target === 'turnDuration') {
            if (this.vkeyboardValue != undefined && this.vkeyboardValue != null && (Number(this.vkeyboardValue) > time_turn || Number(this.vkeyboardValue) == 0)) {
                let msgType = "Attenzione";
                let msgText = Number(this.vkeyboardValue) == 0 ? "Il turno deve essere almeno di 1 ora" : "Il turno non deve superare " + time_turn + " ore";
                this.showInfoDialog(msgType, msgText);
            }
            else {
                this.turnDuration = this.vkeyboardValue;
                this.logger.info("turn duration confirmed proceed with confirm turn");
                this.confirm('identity');
            }
        } else if (origin.target === "searchFilter") {
            //console.log("searching vehicles with: " + this.searchFilter + " - " + origin.value);
            if (origin.value != undefined && origin.value != null && origin.value.trim().length > 0) {
                this.parkingConfirmed = true;
                setTimeout(() => {
                    this.resourceService.requestVehicleListBySearch(origin.value);
                }, 90);
            } else {
                this.clean();
            }
        }
    }

    private searchVehicles() {
        this.searchFilter;
        // this.resourceService.searchVehicleByfilter();
    }


    public confirm(itemType: string) {
        if (itemType === 'parking') {
            if (!this.selectedParking) {
                return;
            }
            this.parkingConfirmed = true;
            setTimeout(() => { this.resourceService.requestVehicleList(this.selectedParking.name); }, 1);

        } else if (itemType === 'identity') {
            if (!this.selectedVehicle) {
                return;
            }
            if (!this.selectedVehicle.name) {
                return;
            }
            if (!this.turnDuration && this.activeTurn) {
                return;
            }
            let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
                if (event.type === 'confirmDialogResponse') {
                    this.logger.info('identity response dialog is: ' + JSON.stringify(event.payload));
                    this.bus.removeAllObservers('confirmDialog', [s]);
                    if (event.payload) {
                        this.doCheckin();
                    }

                }
            });

            this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: '800px', title: "Sei sicuro?", content: "Confermi inizio del turno " + (this.activeTurn ? (" di " + this.turnDuration + " ore ") : "") + " con il mezzo: " + this.selectedVehicle.name } });
        }
    }

    private showInfoDialog(title, text) {
        this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '800px', title: title, content: text } });
    }

    public doCheckin() {
        //TODO RECUPERARE ID DEL DEVICE???
        //TODO aggiungere campo id all'item        
        this.bus.notifyAll("masks", { type: 'mask' });
        const checkin: CheckInCommand = { rscd: this.selectedVehicle.value, startTurnDate: new Date(), duration: Number.parseInt(this.turnDuration) }
        this.agent.sendSynchMessage("SBS", checkin).then(
            (success: Message) => {
                //TODO fare un refresh dello stato dell'applicativo con l'ApplicationStatusService                
                let checkinResponse = JSON.parse(success.payload);
                if (!checkinResponse.response) {
                    this.checkinFailed();
                    return;
                }
                this.logger.info("checkin done: " + success);
                this.bus.notifyAll("masks", { type: 'unmask' });
                this.router.navigate(["dump118"]);
                this.appStatus.updateCurrentTurn(this.selectedVehicle.name, Number.parseInt(this.turnDuration), checkin.startTurnDate);
            },
            (error) => {
                //TODO avvisare l'utente che non è stato possibile iniziare il turno e discriminare il messaggio in base
                // al codice di errore
                this.checkinFailed(error);
            });
    }

    private checkinFailed(error?: any) {
        let msgType = "Errore";
        let msgText = "Errore durante l'elaborazione della richiesta di inizio turno";
        if (!error) {
            msgType = "Attenzione";
            msgText = "Attenzione il turno e' stato negato dalla centrale. Contattare la centrale per l'eventuale pulizia del turno";
        }
        this.logger.info("checkin failed: " + error);
        this.bus.notifyAll("masks", { type: 'unmask' });
        this.showInfoDialog(msgType, msgText);
    }



}



