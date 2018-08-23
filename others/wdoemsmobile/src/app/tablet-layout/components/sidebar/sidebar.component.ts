import { EventEmitter, Output, OnChanges, Component, OnInit, OnDestroy, Input } from '@angular/core';
import { LocalBusService, Event, ApplicationStatusService, CheckOutCommand, AgentService, Logger } from '../../../service/service.module';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { Router, RouterEvent } from '@angular/router';
import { environment } from '../../../../environments/environment';


@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit, OnDestroy {
    isActive: boolean = false;
    @Input('activeElement')
    activeElement: string;
    /* @Input('openPatientElement')
     openPatientElement: boolean = false;*/
    hideArrow: boolean = true;
    showMenu: string = '';
    subscriptions: Array<Subscription> = [];
    @Input()
    public intervention = false;

    inPatient = "false";

    @Output('onNewStatus')
    onNewStatus: EventEmitter<any> = new EventEmitter();

    appVersion: string = "1.0.8";

    refreshEnabled = false;

    constructor(public router: Router,
        public appStatusService: ApplicationStatusService,
        public bus: LocalBusService,
        private agent: AgentService,
        private logger: Logger) {
    }

    ngAfterViewInit() {
        setTimeout(() => {
            if (this.activeElement) {
                this.hideArrow = false;
                let tmp = this.activeElement;
                this.activeElement = "none";
                this.activeElement = tmp;
                this.bus.notifyAll("activeItem", {});
            }

        }, 500);
    }

    ngOnInit() {
        /*  if (this.activeElement) {
             this.hideArrow = false;
             let tmp = this.activeElement;
             this.activeElement = "none";
             this.activeElement = tmp;
 
         } */
        this.refreshEnabled = !environment.production;
        this.subscriptions.push(this.router.events
            .subscribe((event: RouterEvent) => {
                // example: NavigationStart, RoutesRecognized, NavigationEnd
                //console.log("router event: " + event.id + " - " + event.url);
                if (event && event.url) {
                    //console.log("router event for " + event.url);
                    if (event.url.match("patient") != null && event.url.match("patient").length > 0) {
                        //sono nel contesto paziente
                        sessionStorage.setItem("inPatient", "true");
                        this.inPatient = "true";
                    }
                    else {
                        sessionStorage.setItem("inPatient", "false");
                        this.inPatient = "false";
                    }
                    let splitted = event.url.split('/');
                    let id = splitted[splitted.length - 1];
                    if (id) {
                        this.bus.notifyAll("sidebar-item-active", { type: "focus", payload: { id: id } });
                        this.bus.notifyAll("activeItem", {});
                    }
                    /*   if (event.url.endsWith(this.activeElement)) {
                          //console.log("router navigated to active element!!!!!");
                          this.hideArrow = false;
                          
                      }
                      else {
                          this.hideArrow = true;
                          this.activeElement = "none";
                      } */
                }
            }));
        this.subscriptions.push(this.bus.addObserver('newIntervention').subscribe((event) => {
            //console.log("sidebar checkintervention: " + event);
            this.intervention = true;
        }));
        this.subscriptions.push(this.bus.addObserver('updateIntervention').subscribe((event) => {
            //console.log("sidebar checkintervention: " + event);
            this.intervention = true;
        }));
        this.subscriptions.push(this.bus.addObserver("ARTIFACT_UPDATE").subscribe((event) => {
            let state = "IDLE";
            if (event && event.payload) {
                state = event.payload.state;
            }
            switch (state) {
                case "IDLE": {
                    this.logger.info("agent is in IDLE;")
                    break;
                }
                case "CHECKING": {
                    this.logger.info("agent is in CHECKING: " + event.payload.artifact);

                    break;
                }
                case "DOWNLOAD_SUCCESS": {
                    this.logger.info("agent is in DOWNLOAD_SUCCESS;")
                    break;
                }
                case "DOWNLOAD_ERROR": {
                    this.logger.info("agent is in DOWNLOAD_ERROR;")
                    break;
                }
                case "DOWNLOADING": {
                    this.logger.info("agent is in DOWNLOADING;")
                    break;
                }
                case "INSTALLING": {
                    this.logger.info("agent is in INSTALLING;")
                    break;
                }
                case "INSTALLED": {
                    let artifact: string = event.payload.artifact;
                    this.logger.info("agent is in INSTALLED: " + artifact);
                    if (artifact && artifact.includes("emsmobile_webapp")) {
                        this.showConfirmDialog(
                            "E' disponibile una nuova versione dell'applicazione web. Se si vuole usufruire della nuova versione premere il tasto SI. Altrimenti premere NO. Attenzione premendo NO la nuova versione sarà disponibile al primo riavvio utile.".toUpperCase(),
                            (response) => {
                                if (response) {
                                    this.refreshGUI();
                                }
                                else {
                                    this.logger.info("webapp installed but user declined refresh");
                                }
                            });
                    }

                    break;
                }
                case "NOT_INSTALLED": {
                    this.logger.info("agent is in NOT_INSTALLED;")
                    //TODO: this.showInfoDialog("Nuova versione disponibile", "E' disponibile una nuova versione dell'applicazione web. Se si vuole usufruire della nuova versione premere il tasto CONFERMA. Altrimenti premere ANNULLA, in questo caso la nuova versione sarà disponibile al primo riavvio utile.");
                    break;
                }
                case "ERROR": {
                    this.logger.info("agent is in ERROR;")
                    break;
                }
            }
        }));
        let stat = this.appStatusService.fetchCurrentStatus();
        if (stat.currentEvent && stat.currentTurn) {
            //console.log("sidebar fetchCurrentStatus");
            this.intervention = true;
        }
        this.subscriptions.push(this.bus.addObserver("sidebar-item-active").subscribe((event) => {

            switch (event.type) {
                case "focus":
                    //  //console.log("focus event received: " + event.payload.id);
                    this.activeElement = "none";
                    this.hideArrow = false;
                    this.activeElement = event.payload.id;
                    break;
                case "blur":
                    // //console.log("blur event received: " + event.payload.id);
                    // //console.log("current url is: " + this.router.url);
                    break;
            }

        }));
    }



    ngOnDestroy() {
        this.bus.removeAllObservers("sidebar-item-active");
        this.subscriptions.forEach(s => {
            s.unsubscribe();
        })
        /* if(this.activeElement === 'card'){
             this.openPatientElement = true;
 
         }*/
    }

    eventCalled() {
        this.isActive = !this.isActive;
    }

    addExpandClass(element: any) {
        if (element === this.showMenu) {
            this.showMenu = '0';
        } else {
            this.showMenu = element;
        }
    }

    public newStatus(status: any) {
        this.onNewStatus.emit(status);
    }

    public toPatient() {
        this.router.navigate(["intervention/patient/card"]);
    }

    public refreshGUI() {
        window.location.replace(environment.cdn.url);
    }

    public shutdownDevice() {
        if (environment.production) {
            let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
                if (event.type === 'confirmDialogResponse') {
                    this.bus.removeAllObservers('confirmDialog', [s]);
                    if (event.payload) {
                        this.doCheckOut();
                    }
                }
            });
            this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "Sei sicuro?", content: "Procedere con la fine del turno?" } });
        }
        else {
            window.close();
        }
    }

    /*  endTurn() {
         let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
             if (event.type === 'confirmDialogResponse') {
                 this.bus.removeAllObservers('confirmDialog', [s]);
                 if (event.payload) {
                     this.doCheckOut();
                 }
             }
         });
         this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "Sei sicuro?", content: "Procedere con la fine del turno?" } });
 
     } */

    private doCheckOut() {
        let resource = this.appStatusService.getCurrentStatus().currentTurn.resource;
        const checkout: CheckOutCommand = { rscd: resource };
        this.agent.sendSynchMessage("USB", checkout).then(
            success => {
                this.appStatusService.clearCurrentTurn();
                this.agent.shutdownDevice().then((resolved) => {
                    console.log("device shutdown processed");
                })
            },
            (failed) => {
                this.appStatusService.clearCurrentTurn();

            });

    }


    public navigateTo(dest) {
        if (dest && !dest.match("patient")) {
            sessionStorage.setItem("inPatient", "false");
        }
        if (this.intervention) {
            if (dest.match("patientCard")) {
                if (this.appStatusService.getCurrentEvent().patientNnReperito != undefined && this.appStatusService.getCurrentEvent().patientNnReperito.value) {
                    this.showInfoDialog("", "Per poter inserire i pazienti deselezionare il check 'Paziente Non Reperito' nella sezione 'INTERVENTO'");
                    dest = "/intervention/newIntervention";
                    this.router.navigate([dest]);
                } else {
                    dest = "/intervention/patient/cardValutazSan";
                    //gestire check dati paziente non salvati
                    this.subscriptions.push(this.bus.addObserver("navigateTo").subscribe((e) => {
                        //procedi a navigare verso la destinazione 
                        this.router.navigate([dest]);
                    }));
                    this.bus.notifyAll("checkPatientSaved", dest);
                }
            } else {
                //gestire check dati paziente non salvati
                this.subscriptions.push(this.bus.addObserver("navigateTo").subscribe((e) => {
                    //procedi a navigare verso la destinazione 
                    this.router.navigate([dest]);
                }));
                this.bus.notifyAll("checkPatientSaved", dest);
            }
        }
        else {
            this.router.navigate([dest]);
        }

    }

    private showInfoDialog(title, text) {
        let s = this.bus.addObserver('messageDialog').subscribe((event) => {
            if (event.type === 'messageDialogResponse') {
                this.bus.removeAllObservers('messageDialog', [s]);
            }
        });

        this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "Informazione", content: text } });
    }

    showConfirmDialog(valueMsg: string, callback: Function) {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.bus.removeAllObservers('confirmDialog', [s]);
                let response = event.payload;
                if (event.payload) {
                    callback(true);
                } else {
                    callback(false);
                }
            }

        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "ATTENZIONE", content: valueMsg } });
    }
}
