import { Inject, Injectable, Optional } from '@angular/core';
import * as m from './model/index';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { Subscription } from 'rxjs/Subscription';

import { Http, Headers, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { Response, ResponseContentType } from '@angular/http';
import { Logger } from '../logging/index';
import { BatteryStatus, ConnectionStatus, GpsStatus } from './model/index';
import { StorageService } from '../core/index';

/*import 'rxjs/Rx';*/

interface DeviceStatusSubscribers {
    [topic: string]: Subscriber<m.DeviceStatus>[];
}

export const DEVICE_STATUS: string = "device_status";

@Injectable()
export class DeviceStatusService {

    private firstload: boolean = true;

    private deviceStatusObservable: Observable<m.DeviceStatus>;
    private observers: Subscriber<m.DeviceStatus>[] = [];
    private currentDeviceStatus: m.DeviceStatus;

    private lastPosition;

    private map: DeviceStatusSubscribers = {};

    private mapStatus = {};

    constructor(private logger: Logger, private storage: StorageService) {
        ////console.log("WARNING NEW INSTANCE OF DeviceStatusService STATUS SERVICE CREATED!!!");
        this.createDeviceStatusObservable();
    }

    public updateFirstLoad(load: boolean) {
        this.firstload = load;
    }

    public isFirstLoad() {
        return this.firstload;
    }

    public getDeviceStatusObservable() {
        return this.deviceStatusObservable;
    }

    public getCurrentStatus(): m.DeviceStatus {
        let last = this.fetchCurrentStatus();
        return last;

    }

    public updateGpsStatus(status: GpsStatus) {
        this.currentDeviceStatus.gpsStatus = status;
        this.refreshDeviceStatus();
    }

    public updateBatteryStatus(status: BatteryStatus) {
        this.currentDeviceStatus.batteryStatus = status;
        this.refreshDeviceStatus();
    }

    public updateConnectionStatus(status: ConnectionStatus) {
        this.currentDeviceStatus.connectionStatus = status;
        this.refreshDeviceStatus();
    }

    public requestDeviceStatus() {
        this.notify(this.currentDeviceStatus);
    }

    public initWorkflow() {
        //TODO creare lista degli stati
    }

    public resetInterventionApplicationStatus() {
        //TODO
    }

    /*utility methods*/
    public fetchCurrentStatus() {
        let lastStatus = this.storage.getItem(DEVICE_STATUS);
        return lastStatus;
    }

    public refreshDeviceStatus() {
        this.storage.saveItem(DEVICE_STATUS, this.currentDeviceStatus);
        this.notify(this.currentDeviceStatus);
    }

    private notify(status: m.DeviceStatus) {
        this.observers.forEach((o) => o.next(status));
    }

    /*  public subscribe(source: string) {      
        
      }*/

    private createDeviceStatusObservable() {
        let last = this.fetchCurrentStatus();
        if (last) {
            this.currentDeviceStatus = last;
        }
        if (!this.currentDeviceStatus) {
            this.currentDeviceStatus = this.initDeviceStatus();
        }
        this.deviceStatusObservable = new Observable<m.DeviceStatus>((observer: Subscriber<m.DeviceStatus>) => {
            this.logger.info("adding observer to device status: " + observer);
            this.observers.push(observer);
        });

        this.storage.saveItem(DEVICE_STATUS, this.currentDeviceStatus);

    }

    public getLastPosition() {
        return this.lastPosition;
    }


    public updateGpsPosition(newPosition) {

        if (newPosition.satellite <= 0) {
            console.log("invalid position: " + newPosition.satellite);
            return;
        }
        this.lastPosition = newPosition;
        this.currentDeviceStatus.gpsCoord = newPosition;
        this.notify(this.currentDeviceStatus);

        //this.refreshDeviceStatus();
    }

    public cleanPosition() {
        this.lastPosition = undefined;
        this.currentDeviceStatus.gpsCoord = undefined;
        this.notify(this.currentDeviceStatus);
    }

    public createNewDeviceStatus() {
        this.currentDeviceStatus = {};

        //TODO: impostare le altre variabili dello stato iniziale dell'applicativo
        this.refreshDeviceStatus();
    }


    /*  public subscribeToApplicationStatus(f: Function){
          this.applicationStatusObservable.subscribe(f);
      }*/

    private initDeviceStatus(): m.DeviceStatus {
        let app: m.DeviceStatus = {};
        app.gpsStatus = { gpsStatus: -1 };
        app.connectionStatus = { connected: false };
        app.batteryStatus = { lifePercent: 0, acConnected: false };
        return app;
    }




}