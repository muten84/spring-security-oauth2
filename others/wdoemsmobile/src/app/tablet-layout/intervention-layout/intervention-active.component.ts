import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ChangeDetectorRef, ChangeDetectionStrategy, Component, OnInit, Inject, TemplateRef, OnDestroy } from '@angular/core';
import { slideToRight, slideToBottom, fadeInOut, fadeOutIn } from '../../router.animations';
import { InterventionActive } from './intervention.active.interface';
import { ItemDetail, DetailMap } from "./item-detail.interface";
import { AgentService, LocalBusService, Event, ResourceListService, ApplicationStatusService, ApplicationStatus, DeviceStatusService, DeviceStatus, PatientStatusService, StorageService } from '../../service/service.module';
import { CheckInCommand } from '../../service/service.module';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { trigger, style, animate, transition } from '@angular/animations';
import { Router } from '@angular/router';
import { StatusItem } from '../../service/application/model/StatusItem';
import { Subscription } from 'rxjs/Subscription';
import { ConfigurationService } from '../../service/core/configuration.service';

@Component({
    selector: 'app-intervention',
    templateUrl: './intervention-active.component.html',
    styleUrls: ['./intervention.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    animations: [slideToRight(), slideToBottom(), fadeInOut(), fadeOutIn()]
})
export class InterventionActiveComponent implements OnInit, OnDestroy {

    subs: Array<Subscription> = [];
    valueTextColor: string = "value-text-DEFAULT";
    interventionActiveCard: InterventionActive;
    vkeyboardValue: string;

    allStatus = [];
    currentIntervention;
    appStatusSubscription: Subscription;
    activeStatusMap: Map<number, StatusItem> = new Map<number, StatusItem>();
    observableApplicationStatus: Observable<ApplicationStatus>;
    disableAnimation;
    deviceStatusSubscription: Subscription;
    observableDeviceStatus: Observable<DeviceStatus>;
    eventPosition: any;
    hiddenCkPatient: boolean = true;

    constructor(public deviceStatusService: DeviceStatusService, private cd: ChangeDetectorRef, private agent: AgentService, private configService: ConfigurationService, public bus: LocalBusService,
        private appStatus: ApplicationStatusService, public router: Router, private patientStatus: PatientStatusService, private resourceService: ResourceListService,
        private storage: StorageService) { }

    ngOnInit() {
        //console.log("activation ngOnInit");
        this.subs.push(this.bus.addObserver("checkPatientSaved").subscribe((dest) => {
            let inPatient = this.storage.getItem("inPatient");
            //console.log("INTERVENTION COMPONENT NAVIGATE TO: " + dest);
            this.bus.notifyAll('navigateTo', {});

        }));
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.disableAnimation = !enableAnimation;

        //stop alarm and ack activation
        this.agent.stopAlarm().then((success) => {
            ////console.log("alarm stopped sending ack to activation");
        },
            (fail) => {
                ////console.log("alarm not stopped sending ack to activation");
            });

        this.interventionActiveCard = this.appStatus.getCurrentEvent();

        this.interventionActiveCard.interviewList =  this.orderArray(); 

        this.valueTextColor = this.ctrlEmptyNullObject(this.interventionActiveCard.criticita.value) ? this.valueTextColor : this.interventionActiveCard.criticita.value == "G" ? "value-text-G" : (this.interventionActiveCard.criticita.value == "R" ? "value-text-R" : (this.interventionActiveCard.criticita.value == "V" ? "value-text-V" : "value-text-B"));
        this.vkeyboardValue = this.appStatus.getNota();

        this.observableApplicationStatus = this.appStatus.getApplicationStatusObservable();
        this.appStatusSubscription = this.observableApplicationStatus.subscribe((val: ApplicationStatus) => {
            //  this.logger.info("manually subscribed to application status received: " + JSON.stringify(val));
            //this.connected = val.connectionStatus.connected;
            //this.logger.info("check in done on: " + val.currentTurn.resource);
            //  this.currentTurn = val.currentTurn;

            //se il turno è vuoto inizializza la bottoniera degli
            // this.activeStatusMap = this.appStatus.getCurrentStatus().status;
            //this.activeStatusMap = this.turnNotStarted();
            //  this.currentIntervention = val.currentEvent;
            /*  let currentIntervention = [];
              currentIntervention = [{ code: 0, sublabel: "12:30" }];
              currentIntervention = [{ code: 0, sublabel: "12:30" }, { code: 1, sublabel: "13:30" }];
              currentIntervention = [{ code: 0, sublabel: "12:30" }, { code: 1, sublabel: "13:30" }, { code: 2, sublabel: "14:30" }];
  */
            this.activeStatusMap = this.interventionStarted();
            //   this.currentIntervention.status
            this.updateStatusList();

            this.hiddenCkPatient = this.setHiddenCkPatient()?this.ctrlOnePatientExist(this.patientStatus.generateListPatient()):true;
            this.refresh();
            //this.appStatus.requestApplicationStatus();

        });
        this.appStatus.requestApplicationStatus();
        this.subs.push(this.bus.addObserver('updateIntervention').subscribe((event) => {
            //console.log("updating intervention properties...");
            //this.router.navigate(['intervention/alertMessage']);
            this.interventionActiveCard = null;
            this.interventionActiveCard = this.appStatus.getCurrentEvent();
            this.valueTextColor = this.ctrlEmptyNullObject(this.interventionActiveCard.criticita.value) ? this.valueTextColor : this.interventionActiveCard.criticita.value == "G" ? "value-text-G" : (this.interventionActiveCard.criticita.value == "R" ? "value-text-R" : (this.interventionActiveCard.criticita.value == "V" ? "value-text-V" : "value-text-B"));
           
            this.hiddenCkPatient = this.setHiddenCkPatient()?this.ctrlOnePatientExist(this.patientStatus.generateListPatient()):true;
      
            let nonRperito = this.storage.getItem("bPatientNnReperito");
            if (nonRperito == undefined) {
                this.storage.saveItem("bPatientNnReperito", false);
                this.interventionActiveCard.patientNnReperito.value = false;
            }
            else {
                if (nonRperito && !this.hiddenCkPatient) {
                    this.interventionActiveCard.patientNnReperito.value = true;
                } else {
                    this.interventionActiveCard.patientNnReperito.value = false;
                }
            }

            this.interventionActiveCard.interviewList =  this.orderArray(); 
            this.refresh();
        }));

        this.observableDeviceStatus = this.deviceStatusService.getDeviceStatusObservable();
        this.deviceStatusSubscription = this.observableDeviceStatus.subscribe((deviceStatus: DeviceStatus) => {


            this.updateCurrentPosition(deviceStatus.gpsCoord)
        });
        
        this.hiddenCkPatient = this.setHiddenCkPatient()?this.ctrlOnePatientExist(this.patientStatus.generateListPatient()):true;
        

        let nonRperito = this.storage.getItem("bPatientNnReperito");
        if (nonRperito == undefined) {
            this.storage.saveItem("bPatientNnReperito", false);
            this.interventionActiveCard.patientNnReperito.value = false;
        }
        else {
            if (nonRperito && !this.hiddenCkPatient) {
                this.interventionActiveCard.patientNnReperito.value = true;
            } else {
                this.interventionActiveCard.patientNnReperito.value = false;
            }
        }
    }

    public refresh() {
        this.cd.detectChanges();
    }

    public interventionStarted(): Map<number, StatusItem> {
        let activeStatusMap: Map<number, StatusItem> = new Map<number, StatusItem>();
        let applicationStatus;
        applicationStatus = { label: 'Partenza sede', activeStatus: [1, 4] };
        activeStatusMap.set(0, applicationStatus);
        applicationStatus = { label: 'Arrivo luogo', activeStatus: [2, 4] };
        activeStatusMap.set(1, applicationStatus);
        applicationStatus = { label: 'Partenza luogo', activeStatus: [3, 4] };
        activeStatusMap.set(2, applicationStatus);
        applicationStatus = { label: 'Arrivo', activeStatus: [4] };
        activeStatusMap.set(3, applicationStatus);
        applicationStatus = { label: 'Chiusura', activeStatus: [null] };
        activeStatusMap.set(4, applicationStatus);
        return activeStatusMap;
    }

    public getLabel(key: string) {
        ////console.log('getLabel: ' + key);
        return this.interventionActiveCard[key].label;
    }

    public getValue(key: string) {
        ////console.log('getValue: ' + key);
        return this.interventionActiveCard[key].value;
    }

    public getExtra(key: string, subkey: string) {
        ////console.log('getExtra: ' + key);
        return this.interventionActiveCard[key].extra[subkey];
    }


    private updateStatusList() {

        //  let currentIntervention = [];
        //      currentIntervention = [{code: 0, sublabel: "12:30"}];
        //       currentIntervention = [{code: 0, sublabel: "12:30"}, {code: 1, sublabel: "13:30"}];
        //    currentIntervention = [{code: 0, sublabel: "12:30"}, {code: 1, sublabel: "13:30"}, {code:2, sublabel: "14:30"}];

        this.allStatus = [];
        let activeStatus = [];
        for (let i = 0; i < this.activeStatusMap.size; i++) {
            let type = "active";

            let statusItem = {
                "type": type,
                "status": [{
                    label: this.activeStatusMap.get(i).label,
                    sublabel: null,
                    code: String(i)
                }]
            }

            this.allStatus.push(statusItem);

            if (this.currentIntervention != undefined) {
                if (activeStatus.length == 0) {
                    if (i == this.currentIntervention.length - 1) {
                        activeStatus = this.activeStatusMap.get(this.currentIntervention[i].code).activeStatus;
                    }
                    this.checkStatus(i, this.currentIntervention[i].sublabel);
                } else {
                    this.cheakActiveButton(i, activeStatus);
                }
            } else if (i == 0) {
                this.enableStatus(i);
                activeStatus = [4];
            } else {
                this.cheakActiveButton(i, activeStatus);
            }

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

    enableStatus(index) {
        let label = this.allStatus[index].status[0].label;
        let code = this.allStatus[index].status[0].code;
        this.allStatus[index] = {
            "type": "active",
            "status": [{
                label: label,
                code: code
            }]
        }
    }

    disableStatus(index) {
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

    checkStatus(index, sublabel) {
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

    ngOnDestroy() {
        //console.log("activation ngOnDestroy");
        this.subs.forEach(s => { s.unsubscribe() })
    }


    public viewGridDetail(title: string, items, widthModal) {

        let s = this.bus.addObserver('gridDialog').subscribe((event) => {
            if (event.type === 'gridDialogResponse') {

                this.bus.removeAllObservers('gridDialog', [s]);
                if (event.payload) {

                }

            }
        });
        this.bus.notifyAll('dialogs', { type: 'gridDialog', payload: { width: widthModal, title: title, items: items, content: items, justifyContent: true } });
    }


    public viewDetail(title: string, items, widthModal) {

        let s = this.bus.addObserver('infoDialog').subscribe((event) => {
            if (event.type === 'infoDialogResponse') {
                //this.appStatus.getCurrentEvent().codEmergenzaObject.detailArray[2].value = "1__A__RADIO TEST 1, 2__M__RADIO TEST 2";

                // this.appStatus.getCurrentEvent().codEmergenzaObject.detailArray[2].value = "RADIO TEST 1, RADIO TEST 2";

                this.bus.removeAllObservers('infoDialog', [s]);
                if (event.payload) {
                }

            }
        });
        if (!Array.isArray(items)) {
            let itemsArray = [{
                label: title,
                value: items
            }];
            this.bus.notifyAll('dialogs', { type: 'infoDialog', payload: { width: widthModal, title: title, content: itemsArray, justifyContent: true } });
        }
        else {

            /*   items[2].value = "1__A__RADIO TEST 1, 2__M__RADIO TEST 2";
                this.appStatus.getCurrentEvent().codEmergenzaObject.detailArray[2].value = "1__A__RADIO TEST 1, 2__M__RADIO TEST 2";
            */

            /*     items[2].value = "RADIO TEST 1, RADIO TEST 2";
                 this.appStatus.getCurrentEvent().codEmergenzaObject.detailArray[2].value = "RADIO TEST 1, RADIO TEST 2";
               */

            if (title === "EMERGENZA" && items && items[2] && items[2].value.match("__")) {
                let itemsCodEmerg: any[] = [];
                items.forEach(v => {
                    let object = { "label": v.label, "value": v.value };
                    itemsCodEmerg.push(object);
                });
                itemsCodEmerg[2].value = this.getStringVehicle(undefined, itemsCodEmerg[2].value);
                this.bus.notifyAll('dialogs', { type: 'infoDialog', payload: { width: widthModal, title: title, content: itemsCodEmerg, justifyContent: true } });
            } else {
                this.bus.notifyAll('dialogs', { type: 'infoDialog', payload: { width: widthModal, title: title, content: items, justifyContent: true } });
            }
        }
    }

    private ctrlEmptyNullObject(valueIni: string): boolean {
        if (valueIni == undefined || valueIni == null || String(valueIni).trim().length <= 0 || valueIni == "*") {
            return true;
        } else {
            return false;
        }
    }

    public isLabelUpdated(key: any) {
        ////console.log("isLabelUpdated");
        if (!this.interventionActiveCard.listLabelUpdate) {
            return false;
        }
        if (Array.isArray(key)) {
            let a = this.interventionActiveCard.listLabelUpdate.split(',');
            let found = key.filter((val) => {
                return a.includes(this.getLabel(val));
            });
            return found && found.length > 0
        }
        else {
            let a = this.interventionActiveCard.listLabelUpdate.split(',');
            return a.includes(this.getLabel(key));
        }
    }

    public checkDetailUpdated(key: any) {
        let found = undefined;
        if (!this.interventionActiveCard.listLabelUpdate) {
            return false;
        }
        let detail: ItemDetail = this.interventionActiveCard[key];
        if (detail) {
            found = detail.detailArray.find((t) => {
                let a = this.interventionActiveCard.listLabelUpdate.split(',');
                return a.includes(t.label);
            })


        }
        return found != undefined;
    }


    updateCurrentPosition(posi: any) {
        if (posi) {
            let eventPosition = this.appStatus.getCurrentEvent().position;
            if (!eventPosition) {
                return;
            }
            let deviceLat = posi.latitude;
            let deviceLon = posi.longitude;
            let eventLat = eventPosition.lat;
            let eventLon = eventPosition.lon;
            let d = this.calculateDistance(deviceLat, deviceLon, eventLat, eventLon);
            let tmp = d.toString().split('.');
            tmp[1] = tmp[1].substring(0, 2);

            this.appStatus.getCurrentEvent().position.dist = tmp[0] + "," + tmp[1];
            this.eventPosition = this.appStatus.getCurrentEvent().position;
            this.cd.detectChanges();
            // console.log("updating current position distance is: " + dist);
        }
    }

    toRad(n: number) {
        return n * Math.PI / 180;
    }

    calculateDistance(lat1, lon1, lat2, lon2) {

        let R = 6371; // km 

        let x1 = lat2 - lat1;
        let dLat = this.toRad(x1);
        let x2 = lon2 - lon1;
        let dLon = this.toRad(x2);
        let a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(this.toRad(lat1)) * Math.cos(this.toRad(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        let d = R * c;
        return d;

    }

    onChangeCheck(event) {
        if (event.checked) {
            this.showConfirmDialog("Si è sicuri di non dover inserire nessun paziente?", (response) => {
                if (response) {
                    this.interventionActiveCard.patientNnReperito.value = true;
                    this.storage.saveItem("bPatientNnReperito", true);
                    this.refresh();
                }
                else {
                    this.storage.saveItem("bPatientNnReperito", false);
                    this.refresh();
                }
            });
        } else {
            this.storage.saveItem("bPatientNnReperito", false);
            this.refresh();
        }
    }

    showConfirmDialog(valueMsg: string, cb: Function) {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.bus.removeAllObservers('confirmDialog', [s]);
                let response = event.payload;
                cb(response);
                /* if (!event.payload) {
                    this.interventionActiveCard.patientNnReperito.value = false;
                    sessionStorage.setItem("bPatientNnReperito", String(false));
                    this.refresh();
                    
                } else {
                    sessionStorage.setItem("bPatientNnReperito", String(true));
                    this.refresh();
                } */

            }

        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "ATTENZIONE", content: valueMsg } });
    }


    ctrlOnePatientExist(list) {
        if (list != undefined && list.length > 0) {
            let exist = false;
            for (var x = 0; x < list.length; x++) {
                if (list[x].coId != undefined) {
                    exist = true;
                    break;
                }
            }
            return exist;
        } else {
            return false;
        }
    }

    getStringVehicle(type, value): string {

        let array = this.resourceService.getArrayVehicle(type, value);

        let valueEnd = "";
        for (let y = 0; y < array.length; y++) {
            valueEnd += array[y].description + ", ";
        }

        return valueEnd.length == 0 ? "-" : valueEnd.slice(0, valueEnd.length - 2);
    }

    setHiddenCkPatient() :any{
        let current: ApplicationStatus = this.appStatus.fetchCurrentStatus();
        return current.currentIntervention     
        && current.currentIntervention.checkedStatus.length > 0 
        && current.currentIntervention.checkedStatus[1];
    }

    orderArray() :any[]{
        let array = this.interventionActiveCard.interviewList.sort((a: any, b: any) => a.group < b.group? -1 : 1); 
        let arrayGroup = [];     
        for(let x=0; x<array.length; x++){
            if(x==0){
                arrayGroup.push(array[x].group);
            }else{
                for(let r=0;r<arrayGroup.length;r++){
                    if(arrayGroup.includes(array[x].group)){
                        break;                      
                    }else{
                        arrayGroup.push(array[x].group);
                    }
                }
            }           
        }

        let newArray = [];      
        for(let y=0; y<arrayGroup.length; y++){
           let foundArray = array.filter((val) => {
                return val.group === arrayGroup[y];
            });
            let arr = foundArray.sort((a: any, b: any) => a.subgroup < b.subgroup? -1 : 1);
            let arraySubGroup = [];
            for(let t=0; t<arr.length; t++){
                if(t==0){
                    arraySubGroup.push(arr[t].subgroup);
                }else{
                    for(let r=0;r<arraySubGroup.length;r++){
                        if(arraySubGroup.includes(arr[t].subgroup)){
                            break;                      
                        }else{
                            arraySubGroup.push(arr[t].subgroup);
                        }
                    }
                }           
            }
            for(let z=0; z<arraySubGroup.length; z++){
                let found = array.filter((val) => {
                     return val.subgroup === arraySubGroup[z];
                 });
                 let arr = found.sort((a: any, b: any) => a.name < b.name? -1 : 1);
                 for(let d=0; d<arr.length; d++){
                    newArray.push(arr[d]);
                }   
           }            
        }

        return newArray;
    }

   
}



