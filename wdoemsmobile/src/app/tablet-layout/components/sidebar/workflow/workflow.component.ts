import { Component, Output, OnInit, OnDestroy, Input } from '@angular/core';
import { Router } from '@angular/router';
import { CheckedStatus, ApplicationStatus, ApplicationStatusService, LocalBusService, Event, ResourceListService } from '../../../../service/service.module';
import { StatusItem } from '../../../../service/application/model/StatusItem';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { EventEmitter } from '@angular/core';

@Component({
    selector: 'app-sidebar-workflow',
    templateUrl: './workflow.component.html',
    styleUrls: ['./workflow.component.scss']
})
export class WorkflowComponent implements OnInit, OnDestroy {
    isActive: boolean = false;
    showMenu: string = '';
    observableApplicationStatus: Observable<ApplicationStatus>;
    appStatusSubscription: Subscription;

    @Output('onNewStatus')
    onNewStatus: EventEmitter<any> = new EventEmitter();

    //   readOnlyStatus = [];
    //  disabledStatus = [];
    allStatus = [];

    currentStatus: number = -1;
    activeStatusMap: Map<number, StatusItem> = new Map<number, StatusItem>();


    constructor(
        public bus: LocalBusService,
        private router: Router,
        private resourceService: ResourceListService,
        private applicationStatus: ApplicationStatusService) {

    }


    public changeStatusList(status: Array<any>) {
        this.allStatus = status;
    }

    private refreshStatusList(checkedStatus?: Array<CheckedStatus>) {

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

            if (i == 0) {
                this.enableStatus(i);
                activeStatus = [4];
            } else {
                this.cheakActiveButton(i, activeStatus);
            }
        }

        if (checkedStatus) {
            //console.log("refreshing status list with checked status: " + checkedStatus.length);
            checkedStatus.forEach((cs) => {
                if (cs) {
                    // console.log("cs is ", cs.code, cs.sublabel);
                    this.checkStatus(cs.code, cs.sublabel);
                }
            });
            this.enableStatus(checkedStatus.length);
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
        //console.log("checkStatus", index, sublabel);
        if (!this.allStatus) {
            return;
        }
        if (!this.allStatus[index]) {
            return;
        }
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
        // console.log("disableStatus", index);
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
        // console.log("enableStatus", index);
        let theStatus = this.allStatus[index];
        if (!theStatus || !theStatus.status) {
            return
        }
        if (theStatus.type === 'readonly') {
            return;
        }
        let label = theStatus.status[0].label;
        let code = this.allStatus[index].status[0].code;
        this.allStatus[index] = {
            "type": "active",
            "status": [{
                label: label,
                code: code
            }]
        }
    }


    advanceStatus(prev: number, next: number, date: string) {
        this.checkStatus(prev, date);
        this.enableStatus(next);
    }



    newStatus(s) {
        this.currentStatus = Number.parseInt(s.code) - 1;
        this.onNewStatus.emit(s);
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

    ngOnInit() {
        this.activeStatusMap = this.applicationStatus.initStatusList();
        let current: ApplicationStatus = this.applicationStatus.fetchCurrentStatus();
        if (current && current.currentIntervention) {
            this.refreshStatusList(current.currentIntervention.checkedStatus);
        }
        else {
            this.refreshStatusList();
        }

        /*al momento l'observable lo metto qui e tengo legato la gestione degli stati
        dell'intervento con la gestione generica degli stati del componente grafico
        in realtà bisognerebbe spostare la subscribe sul componente dell'intervento
        e gestire l'avanzamento degli stati sull'observer di newInterventionStatus*/
        this.observableApplicationStatus = this.applicationStatus.getApplicationStatusObservable();
        this.appStatusSubscription = this.observableApplicationStatus.subscribe((val: ApplicationStatus) => {
            ////console.log("new status arrived");
            this.activeStatusMap = val.status;
            if (val.currentIntervention && this.activeStatusMap) {
                this.refreshStatusList(val.currentIntervention.checkedStatus);
                //TODO gestire intervento chiuso
                if (val.currentIntervention.checkedStatus) {
                    let newStatus = val.currentIntervention.checkedStatus[this.currentStatus + 1];
                    if (newStatus) {
                        this.advanceStatus(this.currentStatus + 1, newStatus.code + 1, newStatus.sublabel);
                    }
                }
            }
        });
        this.bus.addObserver("newInterventionStatus").subscribe((event) => {
            //TODO predisposizione per gestire avanzamento degli stati da un evento esterno
            //nel caso in cui la gestione dall'application status non dovesse bastare.
        });

    }

    ngOnDestroy() {
        if (this.appStatusSubscription)
            this.appStatusSubscription.unsubscribe();
    }





}
