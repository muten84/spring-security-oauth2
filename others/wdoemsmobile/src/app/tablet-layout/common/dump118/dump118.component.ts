import { Component, OnInit } from '@angular/core';
import { slideToRight } from '../../../router.animations';
import { LocalBusService, AgentService, StorageService } from '../../../service/service.module';
import { DumpEvent } from './model/DumpEvent';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'app-dump118',
    templateUrl: './dump118.component.html',
    styleUrls: ['./dump118.component.scss'],
    animations: [slideToRight()]
})
export class Dump118Component implements OnInit {

    @BlockUI('dump-section') blockUI: NgBlockUI;

    dumpEvents: Array<DumpEvent> = [];

    columnsToDisplay = ['emergencyId', 'codex', 'prov', 'address', 'typeVehicle', 'vehicles', 'phase'];

    coName: string;

    redCriticity: boolean;

    subs: Array<Subscription> = [];

    constructor(private bus: LocalBusService, private agent: AgentService, private storage: StorageService) { }

    ngOnInit() {
        //console.log("dump 118 component init!!");
        /*codice di test per provare l'evento di un nuovo intervento*/
        /*  setTimeout(() => {
              this.bus.notifyAll('newIntervention', {});
          }, 5000);*/
        const siteinfo = this.storage.getFromLocal("siteInfo");
        this.subs.push(this.bus.addObserver("checkPatientSaved").subscribe((dest) => {
            let inPatient = sessionStorage.getItem("inPatient");
            //console.log("INTERVENTION COMPONENT NAVIGATE TO: " + dest);
            this.bus.notifyAll('navigateTo', {});

        }));

        if (siteinfo) {
            this.coName = siteinfo.operativeArea;
        }

        this.requestEvents();

    }

    public onCriticityChange(e) {
        this.redCriticity = e.checked;
        this.requestEvents();
    }

    public requestEvents() {
        this.blockUI.start();

        let payload = {
            province: this.coName,
            redOnly: this.redCriticity
        }

        this.agent.dispatch("POST", "dump118", payload).then(
            (success) => {
                //console.log("success: " + success);
                this.dumpEvents = success.json();
                this.blockUI.stop();
            },
            (fail) => {
                this.blockUI.stop();
            })
    }



    public action1() {
        let s = this.bus.addObserver('action1Dialog').subscribe((event) => {
            if (event.type === 'action1DialogResponse') {

                this.bus.removeAllObservers('action1Dialog', [s]);
            }
        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { title: "Sei sicuro?", content: "Posso procedere con questa azione?" } });
    }

    public action2() {
        let s = this.bus.addObserver('action2Dialog').subscribe((event) => {
            if (event.type === 'action2DialogResponse') {

                this.bus.removeAllObservers('action2Dialog', [s]);
            }
        });
        const items = [
            { description: "POSTAZIONE 1" },
            { description: "POSTAZIONE 2" },
            { description: "POSTAZIONE 3" },
            { description: "POSTAZIONE 4" }
        ]
        this.bus.notifyAll('dialogs', { type: 'listItemDialog', payload: { items: items, width: '500px', title: "Scegliere un item dalla lista" } });
    }

    changeClassCriticy(codice: string): string {
        return codice.substring(codice.length - 1, codice.length);
    }


    addFilter(typeFilter: string): void {
        /* this.agent.xhrGet(typeFilter).then(
             (success) => {
                 //console.log("success: " + success);
                 this.dumpEvents = success.json();
                 //this.dumpEvents.forEach(e => //console.log(e.manageVehicleForSynoptics));
                 this.blockUI.stop();
             },
             (fail) => {
                 this.blockUI.stop();
             })
         }*/
    }

    ngOnDestroy() {
        console.log("activation ngOnDestroy");
        this.subs.forEach(s => { s.unsubscribe() })
    }

}
