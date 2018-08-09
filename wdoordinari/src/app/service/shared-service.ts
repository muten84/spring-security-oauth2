import { Injectable, EventEmitter } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { RootComponent } from '../root/root.component'

export interface MiddleComponent {

    /**
     * utile per prelevare i dati appena si apre il componente
     */
    fetchComponentData(id: string): void;

    /**
     * utile per esporre operazioni di interazione verso il backend
     */
    triggerSubmit(any): void
}

export interface HeaderComponent {
    fetchHeaderData(id: string);

    getHeaderFilter(): any;

    resetFilter(): void;

    triggerSubmit(): void;
}

/**
 * Si preoccupa di mantenere i riferimenti dei componenti visibili nell'applicativo
 */
@Injectable()
export class ComponentHolderService {

    _componentRegistry: Map<string, MiddleComponent> = new Map<string, MiddleComponent>();
    _headerComponentRegistry: Map<string, HeaderComponent> = new Map<string, HeaderComponent>();
    _rootComponent: RootComponent;
    constructor() {

    }

    public setRootComponent(rootComp: RootComponent) {
        console.log("setRootComponent");
        this._rootComponent = rootComp;
    }

    public getRootComponent(): RootComponent {
        return this._rootComponent;
    }

    public setMiddleComponent(key: string, comp: MiddleComponent) {
        //this._tableComponent = comp;
        console.log("setMiddleComponent: " + key);
        this._componentRegistry.set(key, comp);
    }

    public getMiddleComponent(key: string): MiddleComponent {
        //return this._tableComponent;
        return this._componentRegistry.get(key);
    }

    public setHeaderComponent(key: string, comp: HeaderComponent) {
        //this._tableComponent = comp;
        console.log("setHeaderComponent: " + key);
        this._headerComponentRegistry.set(key, comp);
    }

    public getHeaderComponent(key: string): HeaderComponent {
        //return this._tableComponent;
        return this._headerComponentRegistry.get(key);
    }
}


@Injectable()
export class EventService {

    eventEmitter: Subject<EventData> = new Subject();

    sendEvent(event: EventData): void {
        //console.log("sending event: " + event.from);
        this.eventEmitter.next(event);
    }

}

export class EventData {
    private _from: string;
    private _data: any;



    get from(): string {
        return this._from;
    }

    set from(_f: string) {
        this._from = _f;
    }

    get data(): any {
        return this._data;
    }

    set data(_d: any) {
        this._data = _d;
    }


}