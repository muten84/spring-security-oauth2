import { Inject, Injectable, Optional } from '@angular/core';
import * as m from './model/index';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { Subscription } from 'rxjs/Subscription';

import { Http, Headers, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { Response, ResponseContentType } from '@angular/http';
import { InterventionActive } from '../../tablet-layout/intervention-layout/intervention.active.interface';
import { CurrentIntervention } from './model/CurrentIntervention';
import { StatusItem } from './model/StatusItem';
import { SiteInfo } from '../agent/index';
import { Logger } from '../logging/index';
import { MetaMessage, } from '../application/model/index';
import { StorageService } from '../core/index';
import { CheckedStatus } from './model/index';

/*import 'rxjs/Rx';*/

interface ApplicationStatusSubscribers {
    [topic: string]: Subscriber<m.ApplicationStatus>[];
}

export const APP_STATUS: string = "application_status";

@Injectable()
export class ApplicationStatusService {

    private firstload: boolean = true;

    private layout =  "extendedConfirm";

    private applicationStatusObservable: Observable<m.ApplicationStatus>;
    private observers: Subscriber<m.ApplicationStatus>[] = [];
    private currentApplicationStatus: m.ApplicationStatus;

    private map: ApplicationStatusSubscribers = {};

    constructor(private logger: Logger, private storage: StorageService) {
        ////console.log("WARNING NEW INSTANCE OF APPLICATAION STATUS SERVICE CREATED!!!");
        this.createApplicationStatusObservable();
    }

    public updateFirstLoad(load: boolean) {
        this.firstload = load;
    }

    public updateCurrentSiteInfo(siteInfo: SiteInfo) {
        this.currentApplicationStatus.currentSite = siteInfo.operativeArea;
    }

    public isFirstLoad() {
        return this.firstload;
    }

    public getApplicationStatusObservable() {
        return this.applicationStatusObservable;
    }

    public getCurrentStatus(): m.ApplicationStatus {
        let last = this.fetchCurrentStatus();
        if (last) return last;
        return this.currentApplicationStatus;
    }

    public updateEndTurnDate(endTurnDate: Date) {
        this.currentApplicationStatus.currentTurn.endturnDate = endTurnDate;
        this.refreshApplicationStatus();
    }

    public updateCurrentTurn(vehicle: string, duration: number, startTurnDate: Date) {
        let turn: m.CurrentTurn = {
            resource: vehicle,
            duration: duration,
            startTurnDate: startTurnDate,
            endturnDate: undefined
        }
        /* turn.resource = vehicle;
         turn.duration = duration;
         turn.startTurnDate = startTurnDate;*/
        this.currentApplicationStatus.currentTurn = turn;
        this.refreshApplicationStatus();
    }

    public clearCurrentTurn() {
        this.currentApplicationStatus.currentTurn = undefined;
        this.refreshApplicationStatus();
    }


    public requestApplicationStatus() {
        this.notify(this.currentApplicationStatus);
    }

    public initWorkflow() {
        //TODO creare lista degli stati
    }

    public resetInterventionApplicationStatus() {
        this.currentApplicationStatus.currentEvent = undefined;
        this.currentApplicationStatus.status = undefined;
        if (this.currentApplicationStatus.currentIntervention) {
            this.currentApplicationStatus.currentIntervention.checkedStatus = undefined;
            this.currentApplicationStatus.currentIntervention = undefined;
        }
        this.currentApplicationStatus.toBeParked = true;
        sessionStorage.removeItem("hospital");
        this.refreshApplicationStatus();
    }



    public markMessage(metaMsg: MetaMessage) {
        this.currentApplicationStatus.lastProcessedMessage = metaMsg;
        this.refreshApplicationStatus();
    }

    public getLastProcessedMessage(): MetaMessage {
        return this.currentApplicationStatus.lastProcessedMessage;
    }


    public updateCurrentEvent(value: InterventionActive) {
        this.currentApplicationStatus.currentEvent = value;
        this.refreshApplicationStatus();
    }

    public getCurrentEvent(): InterventionActive {
        return this.currentApplicationStatus.currentEvent;
    }

    public updateNota(value: string) {
        this.currentApplicationStatus.nota = value;
    }

    public getNota(): string {
        return this.currentApplicationStatus.nota;
    }


    /*utility methods*/
    public fetchCurrentStatus(): m.ApplicationStatus {
        let lastStatus = this.storage.getItem(APP_STATUS);
        return lastStatus;
    }

    public refreshApplicationStatus() {
        this.storage.saveItem(APP_STATUS, this.currentApplicationStatus);
        this.notify(this.currentApplicationStatus);
    }

    private notify(status: m.ApplicationStatus) {
        this.observers.forEach((o) => o.next(status));
    }

    /*  public subscribe(source: string) {      
        
      }*/

    private createApplicationStatusObservable() {
        let last = this.fetchCurrentStatus();
        if (last) {
            this.currentApplicationStatus = last;
        }
        if (!this.currentApplicationStatus) {
            this.currentApplicationStatus = this.initApplicationStatus();
        }
        if (!this.applicationStatusObservable) {
            this.applicationStatusObservable = new Observable<m.ApplicationStatus>((observer: Subscriber<m.ApplicationStatus>) => {
                this.logger.info("adding observer to application status: " + observer);
                this.observers.push(observer);
            });
        }

    }

    public createNewApplicationStatus() {
        this.currentApplicationStatus = {};
        this.currentApplicationStatus.toBeParked = false;
        this.currentApplicationStatus.currentTurn = undefined;
        //TODO: impostare le altre variabili dello stato iniziale dell'applicativo
        this.refreshApplicationStatus();
    }


    /*  public subscribeToApplicationStatus(f: Function){
          this.applicationStatusObservable.subscribe(f);
      }*/

    private initApplicationStatus(): m.ApplicationStatus {
        let app: m.ApplicationStatus = {};

        return app;
    }

    public clearCheckedStatus() {
        if (this.currentApplicationStatus.currentIntervention) {
            this.currentApplicationStatus.currentIntervention = { checkedStatus: [] };
        }
        this.refreshApplicationStatus();
    }

    public addStatusToIntervention(code: number, hourMinute: string) {
        // console.log("addStatusToIntervention: ", code, hourMinute);
        if (!this.currentApplicationStatus.currentIntervention) {
            this.currentApplicationStatus.currentIntervention = { checkedStatus: [] };
        }
        this.currentApplicationStatus.currentIntervention.checkedStatus[code] = {}
        this.currentApplicationStatus.currentIntervention.checkedStatus[code].code = code
        this.currentApplicationStatus.currentIntervention.checkedStatus[code].sublabel = hourMinute;
        this.refreshApplicationStatus();
    }

    public getInterventionStatus(): CheckedStatus {
        if (!this.currentApplicationStatus.currentIntervention) {
            return undefined;
        }
        if (!this.currentApplicationStatus.currentIntervention.checkedStatus) {
            return undefined;
        }
        let sorted = this.currentApplicationStatus.currentIntervention.checkedStatus.sort((l, r) => {
            if (l.code === r.code) {
                return 0;
            }
            if (l.code < r.code) {
                return -1;
            }
            if (l.code > r.code) {
                return 1;
            }
        });
        if (sorted) {
            return sorted[0];
        }
    }

    public parked() {
        this.currentApplicationStatus.toBeParked = false;
        this.storage.saveItem(APP_STATUS, this.currentApplicationStatus);
    }

    public toBeParked() {
        this.currentApplicationStatus.toBeParked = true;
        this.storage.saveItem(APP_STATUS, this.currentApplicationStatus);
    }

    public initStatusList(): Map<number, StatusItem> {
        let activeStatusMap: Map<number, StatusItem> = new Map<number, StatusItem>();
        let applicationStatus;
        applicationStatus = { label: 'Partenza sede', activeStatus: [1, 6] };
        activeStatusMap.set(0, applicationStatus);
        applicationStatus = { label: 'Arrivo sul Luogo', activeStatus: [2, 6] };
        activeStatusMap.set(1, applicationStatus);
        applicationStatus = { label: 'Parto da Luogo', activeStatus: [3, 6] };
        activeStatusMap.set(2, applicationStatus);
        applicationStatus = { label: 'Arrivo Ospedale', activeStatus: [4, 6] };
        activeStatusMap.set(3, applicationStatus);
        applicationStatus = { label: 'Chiusura', activeStatus: [] };
        activeStatusMap.set(4, applicationStatus);
        this.currentApplicationStatus.status = activeStatusMap;
        this.refreshApplicationStatus();
        return activeStatusMap;
    }

    public getLayout(){
        return this.layout;
    }


}