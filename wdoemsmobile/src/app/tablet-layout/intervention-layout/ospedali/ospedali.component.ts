import { Component, OnInit } from '@angular/core';
import { slideToRight } from '../../../router.animations';
import { Observable } from 'rxjs/Observable';
import { slideToBottom, fadeInOut, fadeOutIn } from '../../../router.animations';
import { AgentService, LocalBusService, Event, ResourceListService } from '../../../service/service.module';
import { CheckInCommand } from '../../../service/service.module';
import { Subscriber } from 'rxjs/Subscriber';
import { trigger, style, animate, transition } from '@angular/animations';
import { ConfigurationService } from '../../../service/core/configuration.service';


@Component({
    selector: 'app-ospedali',
    templateUrl: './ospedali.component.html',
    styleUrls: ['./ospedali.component.scss'],
    animations: [slideToRight(), slideToBottom(), fadeInOut(), fadeOutIn()]
})
export class OspedaliComponent implements OnInit {

    comeBack = false;

    selectedOspedale: any;

    selectedReparto: any;

    ospedaliList: Observable<Array<any>>;

    repartoList: Observable<Array<any>>;

    ospedaleConfirmed: boolean;

    openKeyboard: boolean;

    vkeyboardValue: string;

    turnDuration: string;

    openKeyBoardEvent: CustomEvent;

    pages: number;

    currentPage: number = 0;

    ospedaliObserver: Subscriber<Array<any>>;

    repartoObserver: Subscriber<Array<any>>;

    allItems: Array<any>;

    allReparti: Array<any>;

    itemsPerPage: number = 9;

    prevEnabled: boolean = false;

    nextEnabled: boolean = false;

    disableAnimation;

    allItemsByFilter: Array<any>;

    searchFilterRep: string;

    searchFilter: string;

    constructor(private configService: ConfigurationService, private agent: AgentService, private bus: LocalBusService, private resourceService: ResourceListService) { }

    ngOnInit() {
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.disableAnimation = !enableAnimation;
        this.currentPage = 0;
        this.bus.notifyAll("masks", { type: 'mask' });
        this.addOspedaliListObserver();
        this.createRepartoOspedaleObserver();
        /*evento per far comparire la tastiera senza un input element visibile */
        this.openKeyBoardEvent = document.createEvent("CustomEvent");
        this.openKeyBoardEvent.initCustomEvent('OpenKeyboardEvent', true, true,
            { 'origin': 'hiddenItem', 'keyboardType': 'numeric', 'target': "turnDuration" });
    }

    ngOnDestroy() {
        this.currentPage = 0;
        /* this.appStatusSubscription.unsubscribe();
        this.currentTurn = undefined; */
        this.turnDuration = undefined;

        /* this.parkingConfirmed = false;
        this.removeParkingListObserver();
        this.removeVehicleListObserver(); */
    }

    itemSelected(type: string, data: any) {

        if (type === 'ospedale') {
            this.selectedOspedale = data;
        }
        else if (type === 'reparto') {
            this.selectedReparto = data;
        }
    }

    createRepartoOspedaleObserver() {
        let vList = this.bus.addObserver('repartoList');
        vList.subscribe((event: Event) => {
            this.repartoList = new Observable<Array<any>>((observer: Subscriber<Array<any>>) => {

                this.repartoObserver = observer;
                this.allItems = event.payload.items;
                this.pages = Math.trunc(Math.round((this.allItems.length / this.itemsPerPage) * 100) / 100) + 1;
                if (this.pages > 1) {
                    this.prevEnabled = true;
                    this.nextEnabled = true;
                } else if (this.pages <= 1) {
                    this.prevEnabled = false;
                    this.nextEnabled = false;
                }
                //observer.next(event.payload.items);
                this.toCurrentPage('reparto');
            });
        });
    }

    addOspedaliListObserver(ospedalePatient? :boolean) {
        let ospList = this.bus.addObserver('ospedaliList');
        ospList.subscribe((event: Event) => {
            this.ospedaliList = new Observable<Array<any>>((observer: Subscriber<Array<any>>) => {

                this.ospedaliObserver = observer;
                this.bus.notifyAll("masks", { type: 'unmask' });
                this.allItems = event.payload.items;
                if(this.allItems !=undefined && this.allItems != null){
                    this.allItemsByFilter = [];
                    for(var i=0; i<this.allItems.length; i++){
                        this.allItemsByFilter.push(this.allItems[i]);
                    }
                }
                this.pages = Math.trunc(Math.round((this.allItems.length / this.itemsPerPage) * 100) / 100) + 1;
                if (this.pages > 1) {
                    this.prevEnabled = true;
                    this.nextEnabled = true;
                } else if (this.pages <= 1) {
                    this.prevEnabled = false;
                    this.nextEnabled = false;
                }
                this.toCurrentPage('ospedale');
            });
        });
        this.resourceService.requestOspedaliList(ospedalePatient);
    }

    reset() {
        /* this.parkingConfirmed = false; */
        this.comeBack = true;
        /* this.selectedParking = null;
        this.selectedVehicle = null; */
    }


    createRepartoOspedaliObserver() {
        let vList = this.bus.addObserver('repartoList');
        vList.subscribe((event: Event) => {
            this.repartoList = new Observable<Array<any>>((observer: Subscriber<Array<any>>) => {

                this.repartoObserver = observer;
                this.allItems = event.payload.items;
                this.pages = Math.round((this.allItems.length / 15) * 100) / 100;
                //observer.next(event.payload.items);
                this.toCurrentPage('reparto');
            });
        });
    }

    back() {
        window.history.back();
    }

    /*  toCurrentPage(type: string) {
         let start = this.currentPage * 15;
         let end = this.currentPage + 15;
 
         let items = this.allItems.slice(start, end);
         if (type === 'ospedale') {
             this.ospedaliObserver.next(items);
         } else if (type === 'reparto') {
             this.repartoObserver.next(items);
         }
     } */

    toCurrentPage(type: string) {
        let start = Math.trunc(this.currentPage * this.itemsPerPage);
        if (this.allItems.length <= this.itemsPerPage) {
            if (type === 'ospedale') {
                this.ospedaliObserver.next(this.allItems);
            } else if (type === 'reparto') {
                this.repartoObserver.next(this.allItems);
            }
            return;
        }
        let end = Math.trunc(this.currentPage + this.itemsPerPage);


        let items = this.allItems.slice(start, end);
        if (type === 'ospedale') {
            this.ospedaliObserver.next(items);
        } else if (type === 'reparto') {
            this.repartoObserver.next(items);
        }
    }

    public confirm(itemType: string) {
        if (itemType === 'ospedale') {
            this.ospedaleConfirmed = true;
            setTimeout(() => { this.resourceService.requestRepartoList(this.selectedOspedale.name); }, 1);
        }
        else{
            if(this.selectedReparto == undefined || this.selectedReparto == null){
                let hosp = this.getOspedaleDefault(this.selectedOspedale.name);
                this.selectedReparto = {'group': hosp.description, 'name': "", 'value':hosp.id};
                this.bus.notifyAll("hospitalChoice", { type: 'hospital', payload: { data: this.selectedReparto } });
            }else{
                this.bus.notifyAll("hospitalChoice", { type: 'hospital', payload: { data: this.selectedReparto } });
            }
        /*TODO -> disabilitare button chiusura*/
        }
    }

    private showInfoDialog(title, text) {
        this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: title, content: text } });
    }
    /* 
        toNextPage() {
            this.currentPage = (this.currentPage + 1) % this.pages;
            let start = this.currentPage * 15;
            let end = start + 15;
    
            let items = this.allItems.slice(start, end);
    
            this.ospedaliObserver.next(items);
        }
    
        toPrevPage(type: string) {
            this.currentPage = (this.currentPage - 1) % this.pages;
            let start = this.currentPage * 15;
            let end = start + 15;
    
            let items = this.allItems.slice(start, end);
    
            if (type === 'ospedale') {
                this.ospedaliObserver.next(items);
            } else if (type === 'reparto') {
                this.repartoObserver.next(items);
            }
        } */

    toPrevPage(type: string) {

        if (this.allItems.length <= this.itemsPerPage || this.currentPage <= 0) {
            return;
        }
        this.currentPage = Math.trunc((this.currentPage - 1) % this.pages);

        let start = Math.trunc(this.currentPage * this.itemsPerPage);
        let end = start + this.itemsPerPage;

        let items = this.allItems.slice(start, end);

        if (type === 'ospedale') {
            this.ospedaliObserver.next(items);
        } else if (type === 'reparto') {
            this.repartoObserver.next(items);
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
        if (type === 'ospedale') {
            this.ospedaliObserver.next(items);
        } else if (type === 'reparto') {
            this.repartoObserver.next(items);
        }
    }

    public requestOspedaliBySearch(ospedaleSearch: string) {
        if (ospedaleSearch != undefined && ospedaleSearch != null && ospedaleSearch.trim().length > 0) {
            this.addOspedaliListObserver(true);
            setTimeout(() =>{
                let filtered = this.searchObjectArray(this.allItemsByFilter, ospedaleSearch.toUpperCase());

                if(this.allItems !=undefined && this.allItems != null){
                    this.allItemsByFilter = [];
                    for(var i=0; i<this.allItems.length; i++){
                        this.allItemsByFilter.push(this.allItems[i]);
                    }
                }

                const event: Event = {
                    type: 'ospedaliList', payload: {
                        items: filtered
                    }
                }

                this.pages = Math.trunc(Math.round((this.allItemsByFilter.length / this.itemsPerPage) * 100) / 100) + 1;
                if (this.pages > 1) {
                    this.prevEnabled = true;
                    this.nextEnabled = true;
                } else if (this.pages <= 1) {
                    this.prevEnabled = false;
                    this.nextEnabled = false;
                }

                this.bus.notifyAll('ospedaliList', event);
            ;}, 20);
        }
    }

    public requestRepartoBySearch(repartoSearch: string) {

        if (repartoSearch != undefined && repartoSearch != null && repartoSearch.trim().length > 0) {
          
                 this.resourceService.requestRepartoList(this.selectedOspedale.name);
                 setTimeout(() =>{
                    if(this.allItems !=undefined && this.allItems != null){
                        this.allItemsByFilter = [];
                        for(var i=0; i<this.allItems.length; i++){
                            this.allItemsByFilter.push(this.allItems[i]);
                        }
                    }
                    let filtered = this.searchObjectArray(this.allItemsByFilter, repartoSearch.toUpperCase());

                    const event: Event = {
                        type: 'repartoList', payload: {
                            items: filtered
                        }
                    }

                    this.pages = Math.trunc(Math.round((this.allItemsByFilter.length / this.itemsPerPage) * 100) / 100) + 1;
                    if (this.pages > 1) {
                        this.prevEnabled = true;
                        this.nextEnabled = true;
                    } else if (this.pages <= 1) {
                        this.prevEnabled = false;
                        this.nextEnabled = false;
                    }
                    this.toCurrentPage('reparto');

                    this.bus.notifyAll('repartoList', event);
                    ;}, 20);
            }
              
    }
 

    public searchObjectArray(arr: any[], str: string): any {
        if (typeof arr === 'undefined' || arr.length === 0 || typeof str === 'undefined' || str.length === 0) return [];

        var res = [];

        for (var i = 0; i < arr.length; i++) {
            // //console.log(arr[i].name);     
            if (arr[i].name.indexOf(str) >= 0) {
                res.push(arr[i]);
            }
        }

        return res.sort();
    }

    onVKeyboardValueRep(origin: any) {      
        if(origin.value != undefined &&  origin.value != null && origin.value.trim().length > 0){
            setTimeout(() => { this.requestRepartoBySearch(origin.value);             
            }, 90);  
        }else{
            this.confirm('ospedale');
        }
           
    }

    onVKeyboardValue(origin: any) {      
        if(origin.value != undefined &&  origin.value != null && origin.value.trim().length > 0){
            setTimeout(() => { this.requestOspedaliBySearch(origin.value); }, 90);  
        }else{
            this.addOspedaliListObserver();
        }          
    }

    onInputChange(data) {
       // this.logger.info("on input change data: " + data);
    }

    public clean(origin: any) {
        this.searchFilter = null;
        origin.value = null;
        this.addOspedaliListObserver();        
    }

    public cleanRep(origin: any) {
         this.searchFilterRep = null;
         origin.value = null;
         this.resourceService.requestRepartoList(this.selectedOspedale.name);   
         setTimeout(() =>{this.createRepartoOspedaleObserver;}, 20);
    }

    getOspedaleDefault(description){
        return this.resourceService.getStaticDataByDescription("ospedali", description);
    }
 
 

}
