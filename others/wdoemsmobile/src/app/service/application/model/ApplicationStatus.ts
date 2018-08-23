import { CurrentTurn } from './CurrentTurn';

import { InterventionActive } from '../../../tablet-layout/intervention-layout/intervention.active.interface';
import { Inject, Injectable, Optional } from '@angular/core';
import { CurrentIntervention } from './CurrentIntervention';
import { StatusItem } from './StatusItem';

export interface MetaMessage {
    id: string,
    type: string
}

export interface ApplicationStatus {

    lastProcessedMessage?: MetaMessage

    currentTurn?: CurrentTurn;

    currentEvent?: InterventionActive;

    nota?: string;

    currentIntervention?: CurrentIntervention; //qui ci mettiamo la lista degli stati passati con gli orari

    status?: Map<number, StatusItem>;//questa sarebbe lo stato iniziale

    currentSite?: string;

    toBeParked?: boolean;

    //inTurn: boolean;




    /* constructor() {
 
    }
 
    public get currentTurn(): CurrentTurn {
        return this._currentTurn;
    }
    public set currentTurn(value: CurrentTurn) {
        this._currentTurn = value;
    }
 
    public get gpsStatus(): GpsStatus {
        return this._gpsStatus;
    }
    public set gpsStatus(value: GpsStatus) {
        this._gpsStatus = value;
    }
 
    public get connectionStatus(): ConnectionStatus {
        return this._connectionStatus;
    }
    public set connectionStatus(value: ConnectionStatus) {
        this._connectionStatus = value;
    }
 
    public get batteryStatus(): BatteryStatus {
        return this._batteryStatus;
    }
    public set batteryStatus(value: BatteryStatus) {
        this._batteryStatus = value;
    }
    public set currentEvent(value: InterventionActive) {
        this._currentEvent = value;
    }
    public get currentEvent(): InterventionActive {
        return this._currentEvent;
    }
    public get nota(): string {
        return this._nota;
    }
    public set nota(value: string) {
        this._nota = value;
    }*/
}