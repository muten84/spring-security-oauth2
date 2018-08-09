import { OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { Event, ResourceListService, LocalBusService, ApplicationStatus, ApplicationStatusService, AgentService, PhaseCommand, PatientStatus, PatientStatusService } from '../../service/service.module';
import { slideToRight } from '../../router.animations';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { ConfigurationService } from '../../service/core/configuration.service';
import { environment } from '../../../environments/environment';

@Component({
    selector: 'app-intervention',
    templateUrl: './intervention.component.html',
    styleUrls: ['./intervention.component.scss'],
    animations: [slideToRight()]
})
export class InterventionComponent implements OnInit, OnDestroy {

    observableApplicationStatus: Observable<ApplicationStatus>;
    appStatusSubscription: Subscription;
    disableAnimation;
    subs: Array<Subscription> = [];

    constructor(
        private configService: ConfigurationService,
        private resourceService: ResourceListService,
        private router: Router,
        public bus: LocalBusService,
        private agent: AgentService,
        private applicationStatus: ApplicationStatusService,
        private patientStatus: PatientStatusService) { }

    ngOnInit() {
        //console.log("INTERVENTION COMPONENT ngOnInit");
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.disableAnimation = !enableAnimation;
        let as = this.applicationStatus.fetchCurrentStatus();
        this.checkAndReset(as);
        this.observableApplicationStatus = this.applicationStatus.getApplicationStatusObservable();
        this.appStatusSubscription = this.observableApplicationStatus.subscribe((val: ApplicationStatus) => {
            this.checkAndReset(val);
        });
        //TODO gestire evento checkPatientSaved

    }

    ngOnDestroy() {
        //console.log("INTERVENTION COMPONENT ngOnDestroy");
        this.subs.forEach(s => { s.unsubscribe() })
    }

    checkAndReset(val: ApplicationStatus) {
        if (this.checkClosedIntervention(val)) {
            this.applicationStatus.resetInterventionApplicationStatus();
            this.patientStatus.clearAllPatient();
            sessionStorage.removeItem("hospital");
            this.router.navigate(["dump118"]);
        }
    }

    newStatus(s: any) {
        ////console.log("new status received from sidebar");
        /*   switch (s.code) {
              case "0":
                  this.sendNewStatus(s.code);
                  break;
              case "1":
                  this.sendNewStatus(s.code);
                  break;
              case "2":
                      this.bus.addObserver("hospitalChoice").subscribe((ev: Event) => {
      
                          this.checkStatus(2, this.currentHourLabel(new Date()));
                          this.enableStatus(3);
                      });
                      this.showOspedali();
                  break;
              case "3":                
                  break;
              case "4":
  
                  this.reset();
                  break;
              default:
                  break;
          }
           */
        //gestisco prima i casi particolari
        if (s.code === "2") {
            this.bus.addObserver("hospitalChoice").subscribe((ev: Event) => {
                //console.log("hospital choice is: " + ev.payload.data);
                const hospValue = ev.payload.data.value;
                this.sendNewStatus(s.code, hospValue);
                this.router.navigate(['/intervention/newIntervention']);

            });
            this.showOspedali();
        }
        else if (s.code === "4") {
            let pats = this.patientStatus.generateListPatientOwner();
            if (pats && pats.length > 0) {
                let confirmAllDataPatient :boolean = environment.configuration.global.confirmAllDataPatient;
                if(confirmAllDataPatient){
                    let proceed: boolean = true;
                    pats.forEach((p) => {
                        if (!p.coId) {
                            return;
                        }
                        let checked = this.patientStatus.ctrlPatientParameter(p);
                        if (checked && checked.length > 0) {
                            proceed = false;
                        }
                    });

                    if (!proceed) {
                        this.showInfoDialog("ATTENZIONE", "NON SONO STATI COMPILATI TUTTI I DATI PAZIENTE. PRIMA DI CHIUDERE L'INTERVENTO COMPILARE TUTTI I DATI OBBLIGATORI.", () => {
                            this.router.navigate(["/intervention/patient/cardValutazSan"]);
                        });
                    }
                    else {
                        if (this.isValidStatus(Number.parseInt(s.code))) {
                            this.sendNewStatus(s.code);
                        }
                    }
                }else if (this.isValidStatus(Number.parseInt(s.code))) {                   
                    this.sendNewStatus(s.code);                   
               }    
            }
            else if (this.isValidStatus(Number.parseInt(s.code))) {
                this.sendNewStatus(s.code);
            }
        }

        //altrimenti vai al caso base
        else if (this.isValidStatus(Number.parseInt(s.code))) {
            this.sendNewStatus(s.code);
        }

    }

    private showInfoDialog(title, text, callback?: Function) {
        let s = this.bus.addObserver('messageDialog').subscribe((event) => {
            if (event.type === 'messageDialogResponse') {
                this.bus.removeAllObservers('messageDialog', [s]);
                if (callback) {
                    callback();
                }
            }
        });

        this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "ATTENZIONE", content: text } });
    }



    showOspedali() {
        this.bus.notifyAll("masks", { type: 'mask' });
        this.resourceService.requestOspedaliList().then(
            (success) => {

                const event: Event = {

                    type: 'ospedaliList', payload: {
                        items: success
                    }
                }

                this.bus.notifyAll('ospedaliList', event);
                this.bus.notifyAll("masks", { type: 'unmask' });
                this.router.navigate(["intervention/ospedali"]);

            },
            (error) => {

            });
    }

    isValidStatus(code: number): boolean {
        return code >= 0 && code <= 5;
    }

    checkClosedIntervention(as: ApplicationStatus) {
        let closeStatus = undefined;
        if (!as.currentEvent) {
            return false;
        }
        if (!as.currentIntervention) {
            return false;
        }
        if (as.currentIntervention.checkedStatus) {
            closeStatus = as.currentIntervention.checkedStatus.find((cs) => {
                if (cs) {
                    return cs.code === 4;
                }
            })
        }
        if (closeStatus) {
            return true;
        }

    }

    sendNewStatus(s: string, hosp?: string) {
        const pc: PhaseCommand = this.createPhaseCommand(s);
        if (hosp) {
            pc.hosp = hosp;
            sessionStorage.setItem("hospital", hosp);
        }
        this.agent.sendAsynchMessage("PHS", pc).then(
            (success) => {
                this.applicationStatus.addStatusToIntervention(Number.parseInt(s), this.currentHourLabel(new Date()));
            },
            (failure) => {
                //console.log("messagge send failure: " + failure);
                //TODO gestire fallimento invio stato all'agent, in teoria dovrebbe riprovare ad inviare lo stato
                //ma bisogna considearare che se fallisce l'invio all'agent molto probabilmente
                //ho perso la connessione.
            }
        )
    }

    private createPhaseCommand(newPhase: string, hosp?: string): PhaseCommand {
        const e = this.applicationStatus.getCurrentEvent();

        const pc: PhaseCommand = {
            evcd: e.codEmergenza.value,
            phase: (Number.parseInt(newPhase) + 1).toString(),

        };
        if (hosp) {
            pc.hosp = hosp;
        }

        return pc;
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
}



