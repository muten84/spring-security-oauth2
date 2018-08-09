import { Inject, Injectable, Optional } from '@angular/core';
import * as m from './model/index';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { Subscription } from 'rxjs/Subscription';

import { Http, Headers, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { Response, ResponseContentType } from '@angular/http';
import { Logger } from '../logging/index'
import { StorageService } from '../core/storage.service';
import { PatientCommand } from './command/PatientCommand';
import { AgentService } from '../agent/index';
import { ApplicationStatusService } from './application-status.service';
import { PatientItem } from '../service.module';

interface PatientStatusSubscribers {
    [topic: string]: Subscriber<m.PatientStatus>[];
}

export const PATIENT_STATUS: string = "patient_status";

@Injectable()
export class PatientStatusService {

    private patientStatusObservable: Observable<m.PatientStatus>;
    private observers: Subscriber<m.PatientStatus>[] = [];
    private patient: m.PatientStatus;
    private map: PatientStatusSubscribers = {};
    private currentIndex;
    private synchronizing;



    constructor(private logger: Logger, private storage: StorageService, private agent: AgentService, private appStatus: ApplicationStatusService) {
        //console.log("WARNING NEW INSTANCE OF APPLICATAION STATUS SERVICE CREATED!!!");
        this.createPatientObservable();
    }

    public getPatientStatusObservable() {
        return this.patientStatusObservable;
    }

    private notify(status: m.PatientStatus) {
        this.observers.forEach((o) => o.next(status));
    }

    private createPatientObservable() {
        if (!this.patient) {
            this.patient = this.initPatientStatus();
        }
        if (!this.patientStatusObservable) {
            this.patientStatusObservable = new Observable<m.PatientStatus>((observer: Subscriber<m.PatientStatus>) => {
                this.logger.info("adding observer to patient status: " + observer);
                this.observers.push(observer);
            });
        }

    }

    private initPatientStatus(): m.PatientStatus {
        let app: m.PatientStatus = {
            listPatient: this.generateListPatient(),
            currentPatient: undefined,
            staticDataStatus: false

        };

        return app;
    }

    public updateCurrentPatient(patient: m.PatientItem) {
        this.patient.currentPatient = patient;
    }


    public getCurrentPatient(): m.PatientItem {
        let pats = this.generateListPatient();
        let index = this.getCurrentIndex();
        let curPat: m.PatientItem = undefined;
        if (!pats || pats.length == 0) {
            return undefined;
        }
        if (index >= 0) {
            //return this.patient.listPatient[index];
            curPat = pats[index - 1];
        }
        //se ho trovato il paziente verifica se la valutazione sanitaria visualizzata è effettivamente la 
        //mia quando non sono owner del paziente. Diversamente la valutazione sanitaria presente sarà sicuramente
        // la mia.
        curPat = this.checkPatientCard(curPat);
        return curPat;
    }

    public checkPatientCard(curPat: m.PatientItem): m.PatientItem {
        if (curPat && curPat.owner != this.agent.deviceId && curPat.pcards && curPat.pcards.length > 0) {
            let myPcard = curPat.pcards.find((pcard) => {
                return pcard.mobileCode === this.agent.deviceId;
            })
            if (myPcard) {
                curPat.valutazSanitaria = myPcard.value;
            }
        }
        return curPat;
    }

    public updateStaticDataStatus(value: boolean) {
        this.patient.staticDataStatus = value;
    }

    public getStaticDataStatus() {
        return this.patient.staticDataStatus;
    }

    public getListPatient(): Array<m.PatientItem> {
        //return this.patient.listPatient;
        return this.getListPatientFromStorage();
    }

    public getListPatientFromStorage(): Array<m.PatientItem> {
        return this.storage.getItem('patientList');
    }

    /*  public setListPatient(listPatient: Array<m.PatientItem>) {
         this.patient.listPatient = listPatient;
     } */

    public saveOrUpdate(patient: m.PatientItem) {
        /*   if (this.patient.listPatient == null || this.patient.listPatient == undefined) {
              this.patient.listPatient = [];
          } */
        this.patient.listPatient = this.getListPatient();
        if (!this.patient.listPatient) {
            this.patient.listPatient = [];
        }
        /*  if (!patient.coId && patient.dirty) {
             patient.id = this.patient.listPatient.length;
         } */
        this.patient.listPatient[patient.id] = patient;

        let goodArray = [];

        let cnt = 0;
        this.patient.listPatient.forEach((e) => {
            if (e) {
                e.id = cnt;
                e.idArray = cnt;
                goodArray[cnt] = e;
                cnt++;
            }
        })
        this.patient.listPatient = goodArray;
        this.storage.saveItem('patientList', this.patient.listPatient);


    }







    public savePatient(patient: m.PatientItem): Promise<boolean> {

        const p: Promise<boolean> = new Promise<boolean>((resolve, reject) => {
            let evcd = this.appStatus.getCurrentEvent().codEmergenza;

            patient.synchronized = false;
            let patCmd: PatientCommand = {
                evcd: evcd.value,
                patient: patient,
                phase: -1
            };
            this.agent.sendAsynchMessage("PAT_DATA", patCmd).then((success) => {
                this.saveOrUpdate(patient);


                this.updateCurrentPatient(patient);
                //this.saveOrUpdate(patient);
                this.storage.saveItem('patientList', this.getListPatient());
                this.setSynchronizing(false);

                resolve(true);
            },
                (failure) => {
                    reject(false);
                })
        });

        return p;

    }


    public requestPatientSynch(): Promise<boolean> {
        let evcd = this.appStatus.getCurrentEvent().codEmergenza;
        let patCmd: PatientCommand = {
            evcd: evcd.value,
            patient: undefined,
            phase: -1
        };
        const p: Promise<boolean> = new Promise<boolean>((resolve, reject) => {
            this.agent.sendAsynchMessage("PAT_DATA_SYNCH", patCmd).then((success) => {

            });
        });
        return p;
    }

    public removePatient(patient: m.PatientItem): Promise<any> {

        const p: Promise<any> = new Promise<any>((resolve, reject) => {
            //trova il paziente da rimuovere con l'id
            let toBeDeleted = this.getListPatientFromStorage().find((pat) => {
                return pat.coId == patient.coId
            });
            //let toBeDeleted = this.patient.listPatient[this.getCurrentIndex() - 1]
            toBeDeleted.deleted = true;
            toBeDeleted.synchronized = false;


            let evcd = this.appStatus.getCurrentEvent().codEmergenza;
            let patCmd: PatientCommand = {
                evcd: evcd.value,
                patient: toBeDeleted,
                phase: -1
            };

            if (toBeDeleted.coId) {
                this.saveOrUpdate(toBeDeleted);
                this.agent.sendAsynchMessage("PAT_DATA", patCmd).then(
                    (success) => {
                        this.storage.saveItem('patientList', this.getListPatient());
                        let listPatientTmp = this.generateListPatient();
                        this.updateCurrentPatient(listPatientTmp.length > 0 ? listPatientTmp[0] : undefined);
                        this.currentIndex = undefined;
                        //this.currentIndex = listPatientTmp.length > 0 ? listPatientTmp[0].id : 0;
                        resolve(true);
                    },
                    (failure) => {
                        reject(false);
                    });
            }
            else {
                this.removePatientOrphans();
                let listPatientTmp = this.generateListPatient();
                this.updateCurrentPatient(listPatientTmp.length > 0 ? listPatientTmp[0] : undefined);
                this.currentIndex = undefined;
                this.setSynchronizing(false);
                resolve(true);
            }
        });

        return p;


    }

    /*quando ha finito di sincronizzare rimuovi tutti i pazienti che 
    non hanno il coId. */
    public removePatientOrphans(): Array<m.PatientItem> {
        let cleanArr = [];
        this.getListPatientFromStorage().forEach((e) => {
            if (e.coId) {
                cleanArr.push(e);
            }
        });
        this.patient.listPatient = cleanArr;
        this.storage.saveItem("patientList", this.patient.listPatient);
        return cleanArr;
    }

    public generateListPatient(): Array<m.PatientItem> {
        let currentPatientList = this.getListPatientFromStorage();
        if (!currentPatientList) {
            currentPatientList = [];
        }
        let newArrayPatient: Array<m.PatientItem> = [];

        let indexNewArray = 0;
        for (var y = 0; y < currentPatientList.length; y++) {
            if (currentPatientList[y] && !currentPatientList[y].deleted) {
                newArrayPatient[indexNewArray] = currentPatientList[y];
                newArrayPatient[indexNewArray].idArray = indexNewArray;
                indexNewArray++;
            }
        }

        return newArrayPatient;
    }

    public generateListPatientOwner(): Array<m.PatientItem> {
        let currentPatientList = this.getListPatientFromStorage();
        if (!currentPatientList) {
            currentPatientList = [];
        }
        let newArrayPatient: Array<m.PatientItem> = [];

        let indexNewArray = 0;
        for (var y = 0; y < currentPatientList.length; y++) {
            if (currentPatientList[y] && !currentPatientList[y].deleted && currentPatientList[y].destinazione && currentPatientList[y].destinazione.id && currentPatientList[y].owner == this.agent.deviceId) {
                newArrayPatient[indexNewArray] = currentPatientList[y];
                newArrayPatient[indexNewArray].idArray = indexNewArray;
                indexNewArray++;
            }
        }

        return newArrayPatient;
    }

    public getMovePatient(value, action, patientIndex?): m.PatientItem {
        let index = this.getCurrentIndex() + value;
        if (patientIndex) {
            index = patientIndex;
        }
        this.currentIndex = index - 1;
       
        let realPatients =  this.generateListPatient();
        let patient = realPatients[this.currentIndex]; //this.getElementById(this.currentIndex,  realPatients);    
        if(!patient){
            patient = realPatients[0];
        }
        this.updateCurrentPatient(patient);
        patient = this.checkPatientCard(patient);
        return patient;
    }

    public getMovePatientAdd(value): m.PatientItem {
        let index = this.getCurrentIndex() + value; 
        this.currentIndex = index - 1;
        let realPatients =  this.generateListPatient();
        let patient =  this.getElementById(this.currentIndex,  realPatients);    
        if(!patient){
            patient = realPatients[0];
        }
        this.updateCurrentPatient(patient);
        patient = this.checkPatientCard(patient);
        return patient;
    }

    ctrlObject(object) {
        return object != null && object != undefined ? true : false;
    }

    ctrlValue(s: string) {
        if (!s) {
            return false;
        }
        return s.length > 0;

    }

    public getPatientById(id, list): any {
        if (list == undefined || list == null) {
            return undefined;
        }
        for (var y = 0; y < list.length; y++) {
            if (list[y].id === id) {
                return list[y];
            }
        }
        return undefined;
    }

    public isAllSynchronized(): boolean {
        let pats: Array<m.PatientItem> = this.storage.getItem('patientList');
        if (!pats) {
            return true;
        }
        let notSynchronized = pats.find((val, index, obj) => {
            if (!val.synchronized || val.dirty) {
                return true;
            }
        });
        if (notSynchronized) {
            return false;
        }
        else {
            return true;
        }
    }

    public clearAllPatient() {
        this.storage.removeItem('patientList');
        this.currentIndex = undefined;
    }

    public ctrlPatientParameter(patient: m.PatientItem): Array<any> {
        let listParameter: Array<any> = [];
        if ((Number(patient.valutazSanitaria) >= 1 && Number(patient.valutazSanitaria) <= 3) && !this.ctrlObject(patient.destinazione)) {
            listParameter.push({ group: "1", subgroup: "Valutazione Sanitaria", name: "DESTINAZIONE" });
        }
        if (!this.ctrlObject(patient.esito) || patient.esito.id.toString().trim().length == 0) {
            listParameter.push({ group: "1", subgroup: "Valutazione Sanitaria", name: "ESITO" });
        }
        if (!this.ctrlObject(patient.accompDa) || Object.keys(patient.accompDa).length == 0) {
            listParameter.push({ group: "1", subgroup: "Valutazione Sanitaria", name: "ACCOMPAGNATO DA" });
        }
        if (!this.ctrlObject(patient.criticitaFine)) {
            listParameter.push({ group: "1", subgroup: "Valutazione Sanitaria", name: "CRITICITA' FINE" });
        }
        if (!this.ctrlValue(patient.cognome)) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "COGNOME" });
        }
        if (!this.ctrlValue(patient.nome)) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "NOME" });
        }
        if (!this.ctrlObject(patient.sex)) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "SESSO" });
        }
        if (!this.ctrlObject(patient.dataNascita)) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "DATA NASCITA" });
        }
        if (!this.ctrlObject(patient.eta)) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "ETA'" });
        }
        if (!this.ctrlObject(patient.cittadinanza) || Object.keys(patient.cittadinanza).length == 0 || patient.cittadinanza.id.trim().length == 0) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "CITTADINANZA" });
        }
        if (!this.ctrlObject(patient.comune) || Object.keys(patient.comune).length == 0 || patient.comune.id.trim().length == 0) {
            listParameter.push({ group: "2", subgroup: "Dati Anagrafici", name: "COMUNE" });
        }
        if (!this.ctrlObject(patient.classePatologia) || patient.classePatologia.id.trim().length == 0) {
            listParameter.push({ group: "3", subgroup: "Dati Clinici", name: "CLASSE PATOLOGIA" });
        }
        if (!this.ctrlObject(patient.patologia) || patient.patologia.id.trim().length == 0) {
            listParameter.push({ group: "3", subgroup: "Dati Clinici", name: "DETTAGLIO PATOLOGIA" });
        }
        if (!this.ctrlObject(patient.prestazPrinc) || Object.keys(patient.prestazPrinc).length == 0 || patient.prestazPrinc.id.trim().length == 0) {
            listParameter.push({ group: "3", subgroup: "Dati Clinici", name: "PRESTAZIONE PRINCIPALE" });
        }

        return listParameter;
    }

    /*aggiorna i dati del paziente su un paziente gia' esistente*/
    public synchronizePatientListForUpdate(remotePatient: m.PatientItem, index: number) {
        let localList: Array<m.PatientItem> = this.getListPatientFromStorage();
        remotePatient.synchronized = true;
        remotePatient.dirty = false;
        localList[index] = remotePatient;
        this.patient.listPatient = localList;
        this.storage.saveItem("patientList", localList);
        this.removePatientOrphans();

    }

    /*aggiunge la lista pazienti da centrale e normalizza la lista mettendo in coda
    i pazienti locali al terminale*/
    public synchronizePatientListForAdd(remotePats: Array<m.PatientItem>): Array<m.PatientItem> {

        let newList: Array<m.PatientItem> = []
        let localList = this.getListPatientFromStorage();
        if (!newList) {
            newList = [];
        }
        /*      newList = remotePats.sort((l, r) => {
                 if (!l.id) {
                     return 1;
                 }
                 if (!r.id) {
                     -1;
                 }
                 if (l.id < r.id) {
                     return -1;
                 }
                 else if (l.id > r.id) {
                     return 1;
                 }
                 else if (l.id === r.id) {
                     return 0;
                 }
             }); */
        /* let newSize = newList.length; */
        localList.forEach((localPatient) => {
            newList[localPatient.id] = localPatient;
        });
        remotePats.forEach((remotePatient) => {
            newList[remotePatient.id] = remotePatient;
        });

        this.patient.listPatient = newList;
        this.patient.listPatient.forEach((p) => {
            //se c'è l'id di centrale e il paziente non e' stato modificato posso ritenerlo sincronizzato
            //lo riporto col dirty a false
            if (p.coId) {
                p.synchronized = true;
                p.dirty = false;
            }
            else {
                p.synchronized = false;
                p.dirty = true;
            }
        });
        this.storage.saveItem("patientList", this.patient.listPatient);
        this.removePatientOrphans();
        this.currentIndex = undefined;
        return this.storage.getItem("patientList");

    }

    /*   public setCurrentIndex(index) {
          this.currentIndex = index - 1;
      } */

    public getCurrentIndex() {
        //if (this.isAllSynchronized()) {
        if (!this.currentIndex) {
            this.currentIndex = 0;
        }
        if (!this.getListPatientFromStorage()) {
            return this.currentIndex + 1;
        }
        let len = this.generateListPatient().length;
        if (len == 0) {
            return this.currentIndex + 1;
        }
        return (this.currentIndex % len) + 1;
        //return (this.currentIndex % this.getListPatientFromStorage().length) + 1;
        //} 
        /* else {
            this.currentIndex = this.getPatientInModifyIndex();
            this.updateCurrentPatient(this.generateListPatient()[this.currentIndex]);
        } */
    }

    public isSynchronizing() {
        return this.synchronizing;
    }

    public setSynchronizing(sync: boolean) {
        this.synchronizing = sync;
    }


    public deleteLastElement(array) {
        let newArray = [];
        for (var y = 0; y < array.length - 1; y++) {
            newArray.push(array[y]);
        }
        return newArray;
    }


    checkListDirty() {
        let pats = this.getListPatientFromStorage();
        if (!pats) {
            return false;
        }
        pats.find((p) => {
            if (p.dirty) {
                return true;
            }
            else {
                return false;
            }
        })
    }

    removeUnusedPatient(id) {

        let pats = this.getListPatientFromStorage();
        this.patient.listPatient = this.deleteAt(pats, id);
        this.storage.saveItem("patientList", this.patient.listPatient);
    }

    public deleteAt(array, index) {
        let newArray = [];
        if (!array || array.length <= 0) {
            return newArray;
        }
        let c = 0;
        for (var y = 0; y < array.length - 1; y++) {
            if (y == index) {
                continue;
            }
            newArray[c] = array[y];
            c++;
        }
        return newArray;
    }

    public saveDirtyOnStorage(p: PatientItem){
        this.getPatientById(p.id, this.storage.getItem("patientList")).dirty = true;
    }


    public getElementById(id, list): PatientItem {
        for (var y = 0; y < list.length; y++) {
            if (list[y].idArray === id) {
                return list[y];              
            }
        }
        return undefined;
    }

    public setCurrentPatient(index){
        this.currentIndex = index;
    }


}