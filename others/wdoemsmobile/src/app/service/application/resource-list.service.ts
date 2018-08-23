import { Inject, Injectable, Optional } from '@angular/core';
import { Http, Headers, Response, URLSearchParams } from '@angular/http';
import * as m from './model/index';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { LocalBusService, Event, StorageService } from '../core/index';
import { Logger } from '../logging/index';
import { AgentService } from '../agent/index';
import { ApplicationStatusService } from './application-status.service';
import { PatientStatusService } from './patient-status.service';



const RESOURCE_VERSION_KEY: string = "RESOURCE_VERSION";
const critEndList = [
    { id: '0', description: 'NON CRITICO', color: 'white', disabled: false },
    { id: '1', description: 'POCO CRITICO', color: 'green', disabled: false },
    { id: '2', description: 'MED. CRITICO', color: 'yellow', disabled: false },
    { id: '3', description: 'MOLTO CRITICO', color: 'red', disabled: false },
    { id: '4', description: 'DECEDUTO', color: 'black', disabled: false }];


const sexList = [
    { id: 'M', description: 'M' },
    { id: 'F', description: 'F' },
    { id: 'N', description: 'N' }];

@Injectable()
export class ResourceListService {



    private cachedConfiguration = [];

    private cachedParkings = [];

    private cachedMobileVehicleResource = [];

    private cachedOspedali = [];

    private cachedOspedaliPatient = [];

    private cachedMobileRepartoResource = [];

    private localResource: boolean;



    constructor(private logger: Logger,
        private localBus: LocalBusService,
        private agentService: AgentService,
        private storage: StorageService,
        private appStatus: ApplicationStatusService,
        private patientStatus: PatientStatusService) {

    }

    /*    public getConfigurationParam(section: string, parameter: string, defaultValue?: any) {
           let value = undefined;
           if (this.cachedConfiguration && this.cachedConfiguration.length > 0) {
               let section = this.cachedConfiguration.find((e) => {
                   return e.name === section;
               });
               let parameters: Array<any> = section.parameter;
               if (parameters && parameters.length > 0) {
                   value = parameters.find((p) => {
                       return p.name === parameter;
                   })
               }
           }
           if (!value && defaultValue) {
               value = defaultValue
           }
           return value;
       } */



    public synchResource(local: boolean): Promise<any> {
        this.localResource = local;
        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            this.requestConfiguration().then((success) => {
                this.requestParkingList().then((success) => {
                    this.requestOspedaliList().then((s) => {
                        resolve(success);
                    },
                        (e) => {
                            resolve(e);
                        }
                    )

                }, (error) => {
                    resolve(error);
                });
            });
        });
        return p;
    }


    public requestConfiguration(): Promise<any> {
        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            //se ce le ho già notifico semplicemente la lista agli interessati
            if (this.cachedConfiguration.length > 0) {
                const event: Event = {

                    type: 'configurationList', payload: {
                        items: this.cachedConfiguration
                    }
                }

                resolve(this.cachedConfiguration);
                this.localBus.notifyAll('configurationList', event);

                return;
            }
            this.agentService.extractResource("CONFIGURATION", this.localResource).then(
                (success: Response) => {
                    //this.logger.info("configuration list response: " + );
                    this.cachedConfiguration = success.json();
                    resolve(this.cachedConfiguration);
                    this.localBus.notifyAll('configurationList', event);
                },
                (error) => {
                    this.logger.info("configuration list response error: " + error);
                }
            )
        });
        return p;
    }

    public requestParkingList(): Promise<any> {

        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            if (this.cachedParkings.length > 0) {
                const event: Event = {

                    type: 'parkingList', payload: {
                        items: this.cachedParkings
                    }
                }

                resolve(this.cachedParkings);
                this.localBus.notifyAll('parkingList', event);

                return;
            }
            this.agentService.extractResource("VEHICLE", this.localResource).then(
                (success: Response) => {
                    //const res: Response = <Response>success;
                    const allParkings: Array<any> = success.json();
                    this.cachedMobileVehicleResource = allParkings;
                    const temp = []
                    if (!allParkings || allParkings.length <= 0) {
                        resolve([]);
                        return;
                    }
                    let parkings: Array<any> = allParkings.reduce((prev, next) => {
                        prev = temp;
                        prev.push(next.group);
                        return prev;
                    })

                    let uniqueParkings = parkings.map((val) => {
                        let p = parkings.find((v) => {
                            return v === val;
                        });
                        if (p) {
                            parkings = parkings.filter((t) => {
                                return t !== p;
                            })
                        }
                        if (p)
                            return p;
                    });
                    uniqueParkings = uniqueParkings.filter((v) => {
                        if (v) return { 'name': v };
                    })
                    uniqueParkings = uniqueParkings.map((val) => {
                        return { name: val };
                    })
                    this.logger.info("uniqueParkings: " + uniqueParkings);
                    const event: Event = {

                        type: 'parkingList', payload: {
                            items: uniqueParkings
                        }
                    }
                    this.cachedParkings = uniqueParkings;
                    resolve(uniqueParkings);
                    this.localBus.notifyAll('parkingList', event);
                    this.localBus.notifyAll("masks", { type: 'unmask' });
                    this.logger.info("requestParkingList success: " + success);
                }, (error) => {
                    reject(error);
                    this.localBus.notifyAll("masks", { type: 'unmask' });
                    this.logger.info("requestParkingList error: " + error);
                })
        });
        return p;
    }

    public requestVehicleList(parking: string) {


        let filtered = this.cachedMobileVehicleResource.filter((v) => {
            return v.group === parking;
        });
        /*filtered = filtered.map((val) => {
            //return { name: val };
        })*/
        const event: Event = {

            type: 'vehicleList', payload: {
                items: filtered
            }
        }
        this.localBus.notifyAll('vehicleList', event);
        this.logger.info("requestVehicleList success: " + filtered.length);


    }

    public requestVehicleListBySearch(vehicleSearch: string) {
        if (vehicleSearch != undefined && vehicleSearch != null && vehicleSearch.trim().length > 0) {
            let filtered = this.searchObjectArray(this.cachedMobileVehicleResource, vehicleSearch.toUpperCase());

            const event: Event = {
                type: 'vehicleList', payload: {
                    items: filtered
                }
            }

            this.localBus.notifyAll('vehicleList', event);
            this.logger.info("requestVehicleListBySearch success: " + filtered.length);
        }
    }

    public searchObjectArray(arr: any[], str: string): any[] {
        if (typeof arr === 'undefined' || arr.length === 0 || typeof str === 'undefined' || str.length === 0) return [];

        var res = [];

        for (var i = 0; i < arr.length; i++) {
            // //console.log(arr[i].name);   
            if (!arr[i]) {
                continue;
            }
            if (arr[i].name && arr[i].name.indexOf(str) >= 0) {
                res.push(arr[i]);
                continue;
            }
            if (arr[i].description && arr[i].description.indexOf(str) >= 0) {
                res.push(arr[i]);
                continue;
            }
        }

        return res.sort();
    }



    onlyUnique(value, index, self) {
        return self.indexOf(value) === index;
    }

    public getCurrentResourceVersion(): number {
        return Number.parseInt(this.storage.getFromLocal(RESOURCE_VERSION_KEY));
    }

    public setNewResourceVersion(version: number) {
        this.storage.saveToLocal(RESOURCE_VERSION_KEY, version.toString());
    }


    public requestOspedaliList(ospedalePatient?: boolean): Promise<any> {

        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            if (this.cachedOspedali.length > 0) {

                let ret = [];
                let listPatient = this.patientStatus.generateListPatientOwner();
                if (ospedalePatient == undefined && listPatient && listPatient.length > 0) {
                    // ret = [{ "name": listPatient[0].destinazione.description }];
                    /*vado a forzare la ricerca per evitare di passare un ospedale non presente in lista*/
                    let ospedale = listPatient[0].destinazione.description;
                    let filtered = this.searchObjectArray(this.cachedOspedali, ospedale.toUpperCase());
                    if (filtered && filtered.length > 0) {
                        filtered = filtered.filter((o) => {
                            return o.name == ospedale;
                        });
                        if (filtered && filtered.length > 0) {
                            ret = [filtered[0]];
                        }
                        else {
                            ret = this.cachedOspedali;
                        }
                    }
                    else {
                        ret = this.cachedOspedali;
                    }
                } else {
                    ret = this.cachedOspedali;
                }

                const event: Event = {

                    type: 'ospedaliList', payload: {
                        items: ret
                    }

                }

                resolve(ret);


                this.localBus.notifyAll('ospedaliList', event);

                return;
            }
            this.agentService.extractResource("OSPEDALI", this.localResource).then(
                (success: Response) => {
                    //const res: Response = <Response>success;
                    const allOspedali: Array<any> = success.json();
                    this.cachedMobileRepartoResource = allOspedali;
                    if (allOspedali.length <= 0) {
                        reject();
                        this.localBus.notifyAll("masks", { type: 'unmask' });
                        return;
                    }
                    const temp = []
                    let ospedali: Array<any> = allOspedali.reduce((prev, next) => {
                        prev = temp;
                        prev.push(next.group);
                        return prev;
                    })

                    let uniqueOspedali = ospedali.map((val) => {
                        let p = ospedali.find((v) => {
                            return v === val;
                        });
                        if (p) {
                            ospedali = ospedali.filter((t) => {
                                return t !== p;
                            })
                        }
                        if (p)
                            return p;
                    });
                    uniqueOspedali = uniqueOspedali.filter((v) => {
                        if (v) return { 'name': v };
                    })
                    uniqueOspedali = uniqueOspedali.map((val) => {
                        return { name: val };
                    })
                    this.logger.info("uniqueOspedali: " + uniqueOspedali);
                    const event: Event = {

                        type: 'ospedaliList', payload: {
                            items: uniqueOspedali
                        }
                    }
                    this.cachedOspedali = uniqueOspedali;
                    resolve(uniqueOspedali);
                    this.localBus.notifyAll('ospedaliList', event);
                    this.localBus.notifyAll("masks", { type: 'unmask' });
                    this.logger.info("requestOspedaliList success: " + success);
                }, (error) => {
                    reject(error);
                    this.localBus.notifyAll("masks", { type: 'unmask' });
                    this.logger.info("requestOspedaliList error: " + error);
                })
        });
        return p;


    }


    public getElementExist(element, uniqueOspedali): boolean {
        let exist: boolean = false;
        for (var y = 0; y < uniqueOspedali.length; y++) {
            if (uniqueOspedali[y].id === element.id) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public getElement(description, list): any {
        for (var y = 0; y < list.length; y++) {
            if (list[y].description === description) {
                return list[y];
            }
        }
        return {};
    }

    public getElementByCode(list, code, type): any {
        if (type === "ESITO") {
            for (var y = 0; y < list.length; y++) {
                if (list[y].refCode && list[y].refCode == code) {
                    return list[y];
                }
            }
        }
        return {};
    }

    public getElementsByCode(list, code, type): any[] {
        if (type === "ESITO") {
            let listTmp = [];
            for (var y = 0; y < list.length; y++) {
                if (list[y].refCode && list[y].refCode == code) {
                    listTmp.push(list[y]);;
                }
            }
            return listTmp;
        }
        return [];
    }

    public getListVehicle(filter): Promise<any> {
        /* let p: Promise<any> = new Promise<any>((resolve, reject) => {
            resolve([{ id: '0', description: 'MEZZO 1' }, { id: '1', description: 'MEZZO 2' }, { id: '2', description: 'MEZZO 3' }, { id: '3', description: 'MEZZO 4' }]);
        }); */
        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            /* let sVehicles = this.appStatus.getCurrentEvent().codEmergenzaObject.detailArray ? this.appStatus.getCurrentEvent().codEmergenzaObject.detailArray[2].value : undefined;
             let vehicles = [];
             if(sVehicles){
               
                 vehicles = this.getArrayVehicle("M", sVehicles);
             }*/

            //  if ((!filter || (filter && (filter.length == 0 || filter == "EMPTY"))) && vehicles == undefined) {
            if (!filter || (filter && (filter.length == 0 || filter == "EMPTY"))) {
                let result = [{ id: 'empty', description: '' }];
                resolve(result);
                return;
            }

            let filtered = [];
            //if (!vehicles || (vehicles && filter)) {
            // if(!vehicles){
            filtered = this.searchObjectArray(this.cachedMobileVehicleResource, filter.toUpperCase());
            /* } else {
                 filtered = undefined;
             }*/

            let toRet = [{ id: 'empty', description: '' }];

            if (filtered) {
                filtered.forEach(element => {
                    /* if (vehicles && !filter) {
                         if (vehicles.match(element.name) && vehicles.match(element.name).length > 0) {*/
                    toRet.push({ id: element.value, description: element.name });

                    /*   }
                       } else {
                       toRet.push({ id: element.value, description: element.name }
                       );
                   }*/
                });

                resolve(toRet);
            }

            /*  if (vehicles != undefined) {
                  vehicles.forEach(element => {
                      toRet.push({ id: element.value, description: element.name });
                  });
  
                  resolve(toRet);
              }*/

        });
        return p;

    }

    public getListOspedali2(filter: string): Promise<any> {
        /* let p: Promise<any> = new Promise<any>((resolve, reject) => {
            resolve([{ id: '0', description: 'MEZZO 1' }, { id: '1', description: 'MEZZO 2' }, { id: '2', description: 'MEZZO 3' }, { id: '3', description: 'MEZZO 4' }]);
        }); */
        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            this.logger.info("filter ospedali is: " + filter);
            let ospedaleDefault = this.getOspedaleDefault();
            if (!ospedaleDefault || (ospedaleDefault && filter)) {
                if (!filter || (filter && (filter.length == 0 || filter == "EMPTY"))) {
                    let result = [{ id: 'empty', description: '' }];
                    resolve(result);
                    return;
                }
                let filtered = this.searchObjectArray(this.cachedOspedali, filter.toUpperCase());

                let toRet = [{ id: 'empty', description: '' }];
                if (filtered) {

                    filtered.forEach(element => {
                        let group = this.cachedMobileRepartoResource.find((hospRep) => {
                            return hospRep.group == element.name;
                        });
                        toRet.push({ id: group.value, description: element.name });
                    });
                    resolve(toRet);
                }
            } else {
                let toRet = [{ id: 'empty', description: '' }];
                toRet.push(ospedaleDefault);
                resolve(toRet);
            }

        });
        return p;

    }

    public getListOspedali(filter): Promise<any> {
        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            this.agentService.extractResource("OSPEDALI", this.localResource).then(
                (success: Response) => {
                    const allOspedali: Array<any> = success.json();
                    if (allOspedali.length <= 0) {
                        reject();
                        this.localBus.notifyAll("masks", { type: 'unmask' });
                        return;
                    }
                    let uniqueOspedali = [];
                    if (this.cachedOspedaliPatient != undefined && this.cachedOspedaliPatient != null) {
                        allOspedali.forEach((itemPre) => {
                            let object = { 'id': itemPre.value, 'description': itemPre.group };
                            if (uniqueOspedali.length == 0) {
                                uniqueOspedali.push(object);
                            }
                            else {
                                for (var x = 0; x < allOspedali.length; x++) {
                                    let ospedale = allOspedali[x];
                                    if (!this.getElementExist(object, uniqueOspedali)) {
                                        uniqueOspedali.push(object);
                                    }
                                }
                            }
                        });

                        this.cachedOspedaliPatient = uniqueOspedali;
                    } else {
                        uniqueOspedali = this.cachedOspedaliPatient;
                    }

                    this.logger.info("uniqueOspedali-->PreFilter: " + uniqueOspedali);

                    if (filter.length > 0) {
                        let listTmp = this.searchObjectArray(uniqueOspedali, filter.toUpperCase());
                        uniqueOspedali = [];
                        uniqueOspedali = listTmp;
                    }

                    this.logger.info("uniqueOspedali-->PostFilter: " + uniqueOspedali);

                    resolve(uniqueOspedali);

                    this.logger.info("requestOspedaliList success: " + success);
                }, (error) => {
                    reject(error);
                    this.localBus.notifyAll("masks", { type: 'unmask' });
                    this.logger.info("requestOspedaliList error: " + error);
                })
        });

        return p;

    }

    public requestRepartoList(ospedale: string) {


        let filtered = this.cachedMobileRepartoResource.filter((v) => {
            return v.group === ospedale;
        });
        /*filtered = filtered.map((val) => {
            //return { name: val };
        })*/
        const event: Event = {

            type: 'repartoList', payload: {
                items: filtered
            }
        }
        this.localBus.notifyAll('repartoList', event);
        this.logger.info("requestRepartoList success: " + filtered.length);
    }

    public requestShortList(type: string, filter: string): Promise<Array<any>> {
        let p: Promise<any> = new Promise<any>((resolve, reject) => {
            let result = [];

            //TODO: controlla ilt type se la tipologia non è tra quelle previste dall'agent
            //restituire una lista cabalata.
            if (type === 'accompda') {
                let event = this.appStatus.getCurrentEvent();

                if (event.codEmergenzaObject.detailArray) {
                    result.push({ id: "", description: "" });
                    result = this.getArrayVehicle("M", event.codEmergenzaObject.detailArray[2].value);
                    if (!result) {
                        result = this.getArrayVehicle(undefined, event.codEmergenzaObject.detailArray[2].value);
                    }
                }
                resolve(result);
                return;
            }

            this.agentService.dbLookup(type).then(response => {
                let list: Array<any> = response.json();
                list.forEach((item) => {
                    if ((type == "PATHOLOGY" || type == "PATHOLOGYCLASS") && filter != undefined) {
                        if (String(item.id).substring(0, 3) == filter || String(item.id).trim().length == 0) {
                            result.push({ id: item.id, description: item.value });
                        }
                    }
                    else if (type != "serviceresulttype" || (type == "serviceresulttype" && item.value.match("NON REPERITO") == null)) {
                        //result.push({ id: item.id, description: item.value });
                        let refCode = undefined;
                        if (item.value.match("DECESSO")) {
                            refCode = "4";
                        } else if (item.value.match("NON EFFETTUATO") || item.value.match("RIFIUTO TRASPORTO") || item.value.match("TRATTAMENTO SUL POSTO SENZA TRASPORTO")) {
                            refCode = "0";
                        }

                        result.push({ id: item.id, description: item.value, refCode: refCode });
                    }
                });

                if (result != undefined && result != null && result.length == 0) {
                    result = this.getListStaticData(type);
                }

                resolve(result);
            })

        });

        return p;

    }

    public searchByFilter(type: string, filter: string): Promise<Array<any>> {
        let p: Promise<any>;

        if (type === 'ospedali') {
            p = this.getListOspedali2(filter);
        } else if (type == 'vehicles') {
            p = this.getListVehicle(filter);
        } else {
            p = new Promise<any>((resolve, reject) => {
                if (filter && filter == "EMPTY") {
                    let result = [{ id: 'empty', description: '' }];
                    resolve(result);
                    return;
                }
                let result = [{ id: 'empty', description: '' }];

                this.agentService.dbLookup(type, filter).then(response => {
                    let list: Array<any> = response.json();
                    list.forEach((item) => {
                        result.push({ id: item.id, description: item.value });
                    });

                    resolve(result);

                })
            });

        }

        return p;

    }

    public getStaticDataCodeByValue(type, value) {
        if (type == 'CRITICITA_FINE') {
            let found = critEndList.find((v) => {
                return v.description == value;
            });
            if (found) {
                return found.id;
            }
            return undefined;
        }
    }

    public getStaticDataValueByCode(type, value) {
        if (type == 'CRITICITA_FINE') {
            let found = critEndList.find((v) => {
                return v.id == value;
            });
            if (found) {
                return found.description;
            }
            return undefined;
        }
    }

    public getStaticDataByCode(type, code) {
        if (type == 'CRITICITA_FINE') {
            let found = critEndList.find((v) => {
                return v.id == code;
            });
            if (found) {
                return found;
            }
            return undefined;
        }
        else if (type == 'SEX') {
            let found = sexList.find((v) => {
                return v.id == code;
            });
            if (found) {
                return found;
            }
            return undefined;
        }
        else if (type == 'VALUTAZ_SAN') {
            let list = this.getListStaticData("VALUTAZ_SAN");
            let found = list.find((v) => {
                return v.id == code;
            });
            if (found) {
                return found;
            }
            return undefined;
        } else if (type == 'ospedali') {

            let found = this.cachedMobileRepartoResource.find((v) => {
                return v.value == code;
            });
            if (found) {
                return { id: found.value, description: found.group };
            }
            return undefined;
        }

    }

    getStaticDataByDescription(type, description) {
        if (type == 'ospedali') {

            let found = this.cachedMobileRepartoResource.find((v) => {
                return v.group == description;
            });
            if (found) {
                return { id: found.value, description: found.group };
            }
            return undefined;
        }
    }

    public getListStaticData(typeList: string): Array<any> {
        if (typeList == 'TYPE_ETA') {
            return [{ id: '1', description: 'GIORNI' }, { id: '2', description: 'ANNI' }, { id: '3', description: 'MESI' }];
        } else if (typeList == 'VALUTAZ_SAN') {
            return [{ id: '0', description: '0' }, { id: '1', description: '1' }, { id: '2', description: '2' }, { id: '3', description: '3' }, { id: '4', description: '4' }];
        } else if (typeList == 'SEX') {
            return sexList;
        } else if (typeList == 'CRITICITA_FINE') {
            return critEndList;
        }
        else {
            return [];
        }

    }

    getArrayVehicle(type, value): any[] {
        let vehicles = [];
        if (value && value != "-") {
            let array = value.split(",");

            for (let y = 0; y < array.length; y++) {
                if (array[y].match("__")) {
                    let vehicle = array[y].split("__");
                    if (!type || (type && type === vehicle[1])) {
                        let object = { "id": vehicle[0], "type": vehicle[1], "description": vehicle[2] };
                        vehicles.push(object);
                    }
                } else {
                    let object = { "id": y, "type": "", "description": array[y] };
                    vehicles.push(object);
                }
            }
        }
        return vehicles;
    }

    getOspedaleDefault() {
        return this.getStaticDataByCode("ospedali", sessionStorage.getItem("hospital"));
    }



}

