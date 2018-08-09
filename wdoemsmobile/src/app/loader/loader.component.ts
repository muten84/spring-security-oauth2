import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { routerTransition } from '../router.animations';
import { creatEmptyPatientObject, CurrentTurn, ResourceListService, LocalBusService, WebSocketService, ApplicationStatusService, AckCommand, PatientStatusService, SharedPatient, PatientItem } from '../service/service.module';
import { InterventionActive, InterventionField, createInterventionField, createInterventionObject } from '../tablet-layout/intervention-layout/intervention.active.interface';
import { AgentService, DeviceStatusService, Logger, StorageService } from '../service/service.module';
import { reject } from 'q';
import { ItemDetail } from '../tablet-layout/intervention-layout/item-detail.interface';
import { environment } from '../../environments/environment';
import { ConfigurationService } from '../service/core/configuration.service';

@Component({
    selector: 'app-loader',
    templateUrl: './loader.component.html',
    styleUrls: ['./loader.component.scss'],
    animations: [routerTransition()]
})
export class LoaderComponent implements OnInit {

    resetted = false;

    /*private resource: ResourceListService*/
    constructor(private deviceStatus: DeviceStatusService, private configService: ConfigurationService, private logger: Logger, private appStatus: ApplicationStatusService, public bus: LocalBusService, public router: Router, private wsService: WebSocketService, private resource: ResourceListService,
        private agentService: AgentService, private storage: StorageService, private patientService: PatientStatusService) {

    }

    ngOnInit() {
        /*ws://127.0.0.1:8989/event*/
        this.logger.info("ng on init loader");
        this.deviceStatus.cleanPosition();
        /*    let conn = sessionStorage.getItem("ws_conn");
           if (conn && conn === "1") {
   
               this.router.navigate(["/dump118"]);
               return;
           } */
        //TODO: caricare configurazioni dell'applicativo

        let siteInfo = this.storage.getFromLocal("siteInfo");
        if (siteInfo) {
            const siteInfoObj = siteInfo;
            siteInfoObj.username = environment.configuration.security.credentials.username;
            siteInfoObj.password = environment.configuration.security.credentials.password;
            //console.log("credentials: " + siteInfoObj.username + " - " + siteInfoObj.password);
            this.agentService.initDeviceAgent(siteInfoObj).then(
                success => { this.initWsHandler() },
                failure => {
                    //TODO: mostrare dialog di errore
                    this.logger.info("message service not initialized could not proceed");
                    let s = this.bus.addObserver('messageDialog').subscribe((event) => {
                        if (event.type === 'messageDialogResponse') {
                            this.bus.removeAllObservers('messageDialog', [s]);
                            this.logger.info("error visualized");
                            //window.location.replace("http://localhost:4200");
                            window.location.replace(environment.cdn.url);
                        }
                    });
                    this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "Errore", content: "Attenzione: non e' stato possibile inizializzare il dispositivo premere OK per tentare un ripristino. Se il problema persiste contattare l'assistenza." } });
                }
            );
        }

        else {
            //TODO: mostrare dialog di warning e ripetere la login
        }

        //creare un nuovo application status quando opportuno
        let oldStatus = this.appStatus.fetchCurrentStatus();
        if (!oldStatus) {
            this.appStatus.createNewApplicationStatus();
        }
        else {
            this.appStatus.refreshApplicationStatus();
        }


    }

    private initWsHandler() {
        this.logger.info("connecting to local websocket");
        this.wsService.connect('ws://' + environment.configuration.service.host + ':' + environment.configuration.service.port + '/event').subscribe(
            (event: MessageEvent) => {
                //this.logger.info("websocket event: " + event.data);
                //sessionStorage.setItem("ws_conn", "1");
                try {
                    let object = undefined;
                    try {
                        object = JSON.parse(event.data);
                    } catch (err) {
                        console.log("error while parsing object: " + event.data);
                    }

                    if (Array.isArray(object) && object[0].type == "SETUP") {
                        let local: boolean = false;
                        let obj = JSON.parse(object[0].resourceVersion);
                        let newVersion: number = -1;
                        let currVersion: number = this.resource.getCurrentResourceVersion();
                        if (!obj.version) {
                            local = true;
                        }
                        else {
                            newVersion = obj.version;
                        }

                        if (!currVersion && obj.version) {
                            this.resource.setNewResourceVersion(newVersion);
                            local = false;
                        }
                        else if (!obj.version || newVersion <= currVersion) {
                            local = true;
                        }
                        this.logger.info("starting synch of resources local: " + local);
                        this.resource.synchResource(local).then((success) => {
                            this.logger.info("routinG to dump 118");
                            if (!local && newVersion) {
                                this.resource.setNewResourceVersion(newVersion);
                            }
                            this.router.navigate(["/dump118"]);

                        });
                    }
                    if (Array.isArray(object) && object[0].type == "RESET") {
                        //FIXME: resettare lo storage passando per l'agent ovvero per lo storageservice
                        //localStorage.clear();
                        //sessionStorage.clear();
                        //let keyReset = { resetted: true };
                        //localStorage.setItem("KEY_RESET", JSON.stringify(keyReset));
                        //window.location.replace(environment.cdn.url);
                        this.bus.notifyAll("masks", { type: 'unmask' });
                        this.bus.notifyAll("masks", { type: 'unmask' });
                        this.bus.notifyAll("masks", { type: 'unmask' });
                        this.resetted = true;
                        this.storage.removeAll();
                    }
                    if (Array.isArray(object) && object[0].type == "ARTIFACT_UPDATE") {
                        //console.log("Agent ARTIFACT_UPDATE event: " + event.data);
                        let data = object[0];
                        if (data) {
                            let reason = data.result;
                            if (reason) {
                                let splitted = reason.split("-");
                                let state = splitted[0].trim();
                                let artifact = splitted[1];
                                if (state) {
                                    /* switch (state) {
                                        case "IDLE": {
                                            console.log("agent is in IDLE;")
                                            break;
                                        }
                                        case "CHECKING": {
                                            console.log("agent is in CHECKING;")
                                            break;
                                        }
                                        case "DOWNLOADING": {
                                            console.log("agent is in DOWNLOADING;")
                                            break;
                                        }
                                        case "INSTALLING": {
                                            console.log("agent is in INSTALLING;")
                                            break;
                                        }
                                        case "ERROR": {
                                            console.log("agent is in ERROR;")
                                            break;
                                        }
                                    } */
                                    this.bus.notifyAll("ARTIFACT_UPDATE", { type: "ARTIFACT_UPDATE", payload: { state: state, artifact: artifact } });
                                }
                            }
                        }
                    }
                    else if (object.msg) {
                        let msg = object.msg;
                        this.logger.info("received message from central: " + msg.hd.type + " - " + msg.hd.uid + " - " + msg.hd.beep + " - " + msg.hd.error);
                        let dateTime = msg.hd.time;
                        if (dateTime) {
                            let splitted = dateTime.split(" ");
                            let isoString = this.fromDateToISOString(splitted[0], splitted[1]);
                            let diff = this.diffInMinutes(new Date().toISOString(), isoString);
                            this.logger.info("new message received with timesatmp diff: " + diff);
                            if (diff >= 10) {
                                this.logger.info("WARNING MESSAGE IS TOO OLD AND WILL BE DISCARDED: " + msg.hd.uid);
                                return;
                            }
                        }
                        if (msg.hd.error) {
                            let s = this.bus.addObserver('messageDialog').subscribe((event) => {
                                if (event.type === 'messageDialogResponse') {
                                    this.bus.removeAllObservers('messageDialog', [s]);
                                }
                            });
                            this.synchronizePatients(msg);
                            this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "ATTENZIONE", content: "SI E' VERIFICATO UN ERRORE DURANTE LA SINCRONIZZAZIONE DEI DATI PAZIENTE. I DATI DEL PAZIENTE NON SONO STATI INVIATI IN CENTRALE." } });


                            //this.bus.notifyAll('patientSynchronizing', {});
                            //this.bus.notifyAll('patientSynchronized', {});
                            return;

                        }


                        const last = this.appStatus.getLastProcessedMessage();
                        let type = msg.hd.type;
                        if (last && last.id === msg.hd.uid && last.type === msg.hd.type) {
                            this.logger.info("msg just processed nothing to do");
                            return;
                        }
                        /**LISTA LABEL MODIFICATE */
                        let listFieldChange = null;
                        switch (type) {
                            case "ACT": {
                                this.notifyActivationMessage(msg);
                                break;
                            }
                            case "MOD": {
                                this.notifyUpdateMessage(msg);
                                break;
                            }
                            case "CLR": {
                                this.notifyClearIntervention(msg);
                                break;
                            }
                            case "RQP": {
                                this.sendLastPosition(msg);
                                break;
                            }
                            case "TXT_MSG": {
                                this.showInfoMessage(msg);
                                break;
                            }
                            default: {
                                this.logger.info("cannot process message of type: " + JSON.stringify(msg));
                                break;
                            }
                        }

                        //MARCA IL MESSAGGIO COME PROCESSATO SOLO PER QUELLI CON FLAG RETAIN A TRUE
                        if (type == "ACT" || type == "MOD") {
                            this.appStatus.markMessage({
                                id: msg.hd.uid,
                                type: msg.hd.type
                            })
                        }
                    }
                    else if (object.type && object.type.startsWith("DEVICE")) {
                        switch (object.type) {
                            case "DEVICE_GPS_STATUS": {
                                let payload = JSON.parse(object.payload);
                                this.deviceStatus.updateGpsStatus(payload);
                                break;
                            }
                            case "DEVICE_GPS_COORD": {
                                let payload = JSON.parse(object.payload);
                                this.deviceStatus.updateGpsPosition(payload);
                                this.deviceStatus.updateGpsStatus({
                                    gpsStatus: 1
                                });
                                //invia le coordinate ogni 3 secondi
                                this.agentService.sendNewPosition(payload, 3000, 0);
                                break;
                            }
                            case "DEVICE_BATTERY": {
                                let payload = JSON.parse(object.payload);
                                this.deviceStatus.updateBatteryStatus(payload);
                                break;
                            }
                            case "DEVICE_CONNECTION_STATUS": {
                                let payload = JSON.parse(object.payload);
                                this.deviceStatus.updateConnectionStatus(payload);
                                this.bus.notifyAll("connectionStatus", payload)
                                break;
                            }
                        }
                    }
                    else if (object.type && object.type == 'h') {
                        //TODO gestire heartbeat della websocket se non arrivano h da più di tot secondi e 
                        //non è stato gestito nessun evento di error o di complete dell'observable della stessa
                        //reinizianilizzare il ws handler dell'applicazione 

                    }
                    /*      else if (object.type && object.type == "ACT") {
     
                             let interventionActive: InterventionActive = JSON.parse(object.payload);
                             this.appStatus.updateCurrentEvent(interventionActive);
                             let turn = this.appStatus.getCurrentStatus().currentTurn;
                             if (turn && turn.resource) {
                                 this.bus.notifyAll('newIntervention', {});
                             }
     
                         
                         } */
                }
                catch (err) {
                    //caccio qualsiasi errore in modo da non perdere gli eventi
                    //della websocket
                    this.logger.error("websocket handler error: " + err);
                    //console.log("websocket handler error: " + err);
                }
            },
            (error) => {
                sessionStorage.setItem("ws_conn", "0");
                this.logger.info("websocket error: " + error);
                let s = this.bus.addObserver('messageDialog').subscribe((event) => {
                    if (event.type === 'messageDialogResponse') {
                        this.bus.removeAllObservers('messageDialog', [s]);
                        this.logger.info("error visualized");
                        window.location.replace(environment.cdn.url);

                    }
                });
                this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "Errore", content: "Attenzione: non e' stato possibile inizializzare la comunicazione locale premere OK per tentare un ripristino. Se il problema persiste contattare l'assistenza." } });
            },
            () => {
                this.logger.info("websocket complete");
                //   sessionStorage.setItem("ws_conn", "0");
                let s = this.bus.addObserver('messageDialog').subscribe((event) => {
                    if (event.type === 'messageDialogResponse') {
                        this.bus.removeAllObservers('messageDialog', [s]);
                        this.logger.info("error visualized");
                        window.location.replace(environment.cdn.url);

                    }
                });
                setTimeout(() => {
                    let disclaimerMessage = "Attenzione: e' stata persa la comunicazione locale con il dispositivo premere OK per tentare un ripristino. Se il problema persiste contattare l'assistenza.";
                    if (this.resetted) {
                        this.resetted = false;
                        disclaimerMessage = "Attenzione: e' stato comandato un reset del dispositivo da remoto. Premere OK per avviare la procedura di ripristino";
                    }
                    this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '1000px', title: "Errore", content: disclaimerMessage } });
                }, 2000);

            }
        );
    }

    private checkBeepAlarm(msg: any) {
        console.log("check beep alarm: " + msg.hd.beep);
        if (msg.hd.beep) {
            this.agentService.beepAlarm().then(
                (resolved) => {

                },
                (failed) => {

                })
        }
        else {
            this.logger.info("no need to beep: " + msg.hd.uid + " - " + msg.hd.type);
        }
    }

    private showInfoMessage(msg: any) {
        if (msg.bd.textmsg) {
            let s = this.bus.addObserver('messageDialog').subscribe((event) => {
                if (event.type === 'messageDialogResponse') {
                    this.bus.removeAllObservers('messageDialog', [s]);
                }
            });
            this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "Informazione dalla centrale operativa", content: msg.bd.textmsg } });
        }
    }

    private notifyClearIntervention(msg: any) {
        //console.log("clearing activation: " + msg);
        let e = this.appStatus.getCurrentEvent();
        let evcd = msg.bd.evcd;
        if (e && e.codEmergenza.value === evcd) {
            this.appStatus.resetInterventionApplicationStatus();
            this.checkBeepAlarm(msg);
            this.bus.notifyAll('clearIntervention', {});
        }
    }

    private notifyActivationMessage(msg: any) {
        if (msg.hd.rel && msg.hd.rel === 2) {
            let e = this.appStatus.getCurrentEvent();
            if (e && e.codEmergenza.value === this.getValue(msg, "evcd")) {
                this.notifyUpdateMessage(msg);
                return;
            }
            this.patientService.clearAllPatient();
            let interventionActive: InterventionActive = this.createInterventionFromMessageV2(msg);
            this.appStatus.updateCurrentEvent(interventionActive);
            let turn = this.appStatus.getCurrentStatus().currentTurn;
            if (turn && turn.resource) {
                this.checkBeepAlarm(msg);
                this.appStatus.clearCheckedStatus();
                this.bus.notifyAll('newIntervention', {});
            }
        }
    }

    private notifyUpdateMessage(msg: any) {
        if (msg.hd.rel && msg.hd.rel === 2) {
            let e = this.appStatus.getCurrentEvent();
            if (!e) {
                this.notifyActivationMessage(msg);
                return;
            }
            if (e && e.codEmergenza.value !== this.getValue(msg, "evcd")) {
                this.logger.info("update message discarded: " + this.getValue(msg, "evcd"));
                return;
            }
            let interventionActive: InterventionActive = this.createInterventionFromMessageV2(msg);

            this.appStatus.updateCurrentEvent(interventionActive);
            let turn = this.appStatus.getCurrentStatus().currentTurn;

            if (turn && turn.resource && (interventionActive.notifyUpdate || msg.hd.type === "ACT")) {
                this.checkBeepAlarm(msg);
                this.bus.notifyAll('updateNotifyIntervention', {});
            } else if (turn && turn.resource) {
                this.checkBeepAlarm(msg);
                this.sendAckMessage();
                this.bus.notifyAll('updateIntervention', {});

            }

            //tenta di sincornizzare la lista paziente inviata da co altrimenti non fa nulla
            this.synchronizePatients(msg);
        }
    }

    synchronizePatients(msg: any) {
        this.bus.notifyAll('patientSynchronizing', {});
        if (!msg.bd.patients || !msg.bd.patients.pat) {
            //console.log("no patients from co to be synchronized");
            this.bus.notifyAll('patientSynchronized', {});
            this.patientService.setCurrentPatient(undefined);
            return;
        }
        let patients: Array<SharedPatient> = msg.bd.patients.pat;


        if (!Array.isArray(patients)) {
            patients = [];
            patients.push(msg.bd.patients.pat);

        }
        //console.log("synchronizing patients: " + patients.length);

        //notifico che sta partendo la sincronizzazione pazienti


        let pats = this.patientService.getListPatientFromStorage();
        if (pats && pats.length > 0) {
            //ho almeno un paziente in lista
            let localSize = pats.length;
            let coSize = patients.length;
            if (localSize === coSize) {
                //se non si tratta dello stesso paziente devo prendere la lista da co e aggiungere alla fine il paziente che ho in locale
                this.doPatientSynch(patients);
            }
            else if (localSize < coSize) {
                //ci sono piu pazienti da co e li devo aggiungere alla lista locale i pazienti senzo coId vengono messi alla fine della lista
                this.doPatientSynch(patients);
            }
            else if (localSize > coSize) {
                //ho più pazienti in locale, questo caso non dovrebbe mai verificarsi perché non posso aggiungere nuovi pazienti finché non sono allineato con la centrale.
                //console.log("WARNING LOCAL PATIENT SIZE IS GREATER THAN CO SIZE")
                patients.forEach((coPatient) => {
                    /*   let updatedPatient = this.isPatientMatch(coPatient);
                      if (updatedPatient) {
                          //console.log("patient match updating current list");
                          let index = this.findPatientIndex(coPatient);
                          this.patientService.synchronizePatientListForUpdate(updatedPatient, index);
  
                      } */
                    this.updatePatient(coPatient);
                });
            }
        }
        else if (pats && pats.length == 0) {
            this.patientService.clearAllPatient();
        }
        else {

            //prenditi la lista da centrale così com'è per buona
            patients.forEach((coPatient) => {
                //console.log("adding patient: " + coPatient.coid);
                let newPatient: PatientItem = this.convertSharePatientToPatientItem(coPatient);
                newPatient.synchronized = true;
                newPatient.dirty = false;
                newPatient.sent = false;
                this.patientService.saveOrUpdate(newPatient);
            })

        }

        //notifico che la sincronizzazione paziente e' terminata
        this.bus.notifyAll('patientSynchronized', {});
        this.patientService.setCurrentPatient(undefined);
        this.patientService.setSynchronizing(false);
    }

    updatePatient(coPatient: SharedPatient) {
        let updatedPatient = this.isPatientMatchById(coPatient);
        let index = -1;
        if (updatedPatient) {
            index = this.findPatientIndex(coPatient, (remote, local) => {
                if (!local.coId) {
                    return false;
                }
                return local.coId == remote.coid;
            });
        }
        /* else if (!updatedPatient) {
            updatedPatient = this.isPatientMatchByFields(coPatient);
            if (updatedPatient) {
                index = this.findPatientIndex(coPatient, (remote, local) => {
                    if ((remote.nome && local.nome == remote.nome) && (remote.cognome && remote.cognome == local.cognome)) {
                        return true;
                    }
                    else if (remote.sanEval == local.valutazSanitaria && remote.owner == local.owner) {
                        return true;
                    }
                    return false;
                });
            }
        } */

        if (updatedPatient && index >= 0) {
            //console.log("patient match updating current list");

            this.patientService.synchronizePatientListForUpdate(updatedPatient, index);
            return true;
        }
        return false;
    }

    doPatientSynch(patients: Array<SharedPatient>) {
        patients.forEach((coPatient) => {
            if (!this.updatePatient(coPatient)) {
                //aggiungi
                //console.log("patient not match adding patient in current list");
                let toAdd = [];
                toAdd.push(this.convertSharePatientToPatientItem(coPatient));
                this.patientService.synchronizePatientListForAdd(toAdd);
            }
        })
    }

    convertSharePatientToPatientItem(p: SharedPatient): PatientItem {
        let pat: PatientItem = creatEmptyPatientObject();
        //TODO: pat.accompDa = p.ac
        pat.accompDa = p.accompDa;
        pat.cittadinanza = p.cittadinanza;
        pat.classePatologia = p.classePatologia;
        pat.cognome = p.cognome;
        pat.comune = p.comune;
        pat.criticitaFine = p.criticitaFine
        pat.dataNascita = p.dataNascita;
        pat.deleted = p.deleted;
        pat.destinazione = p.destinazione;
        pat.dirty = false;
        pat.esito = p.esito;
        pat.eta = p.eta;
        pat.id = p.id;//non metto l'id -1 mi arriva normalizzato dal backend
        pat.nome = p.nome;
        pat.owner = p.owner;
        pat.patologia = p.patologia;
        pat.prestazPrinc = p.prestazPrinc;
        pat.prestazSec1 = p.prestazSec1;
        pat.prestazSec2 = p.prestazSec2;
        pat.prestazSec3 = p.prestazSec3;
        pat.prestazSec4 = p.prestazSec4;
        pat.sent = false;
        pat.sex = p.sex;
        pat.synchronized = undefined;
        pat.typeEta = p.typeEta;
        pat.valutazSanitaria = p.sanEval;
        pat.coId = p.coid;
        pat.criticitaFine = p.criticitaFine;
        pat.criticitaFineId = p.criticitaFineId;
        pat.ownerVehicle = p.ownerVehicle.description;
        if (p.pcards && p.pcards.saneval) {
            pat.pcards = p.pcards.saneval;
        }
        return pat;
    }

    /*restituisce il PatientItem aggioranto i nuovi dati inviati dalla co e che matcha il coid con lo SharedPatient
    undefined altrimenti */
    isPatientMatchById(coPatient: SharedPatient): PatientItem {
        let pats = this.patientService.getListPatientFromStorage();
        if (pats.find((val, index, obj) => {
            if (!val.coId) {
                return coPatient.id == val.id;
            }
            return val.coId === coPatient.coid;
        })) {
            return this.convertSharePatientToPatientItem(coPatient);
        }
    }

    isPatientMatchByFields(coPatient: SharedPatient): PatientItem {
        let pats = this.patientService.getListPatientFromStorage();
        //trova se il paziente combacia per nome e cognome o valutazione sanitaria
        if (pats.find((val, index, obj) => {

            if ((coPatient.nome && val.nome == coPatient.nome) && (coPatient.cognome && coPatient.cognome == val.cognome)) {
                return true;
            }
            /* else if (coPatient.sanEval == val.valutazSanitaria && coPatient.owner == val.owner) {
                return true;
            } */
            return false;
        })) {
            return this.convertSharePatientToPatientItem(coPatient);
        };

    }

    findPatientIndex(coPatient: SharedPatient, predicate): number {
        let pats = this.patientService.getListPatientFromStorage();
        return pats.findIndex((val, index, obj) => {
            /*  if (!val.coId) {
                 return false;
             }
             return val.coId === coPatient.coid; */
            return predicate(coPatient, val);
        })
    }

    sendAckMessage() {
        let last = this.appStatus.getLastProcessedMessage();
        const ackCommand: AckCommand = {
            relatesTo: last.id,
            relatesToType: last.type,
            result: true
        }
        this.agentService.sendAsynchMessage("ACK", ackCommand).then(
            (success) => {
                //console.log("act messagge send success: " + success);
            },
            (failure) => {
                //console.log("act messagge send failure: " + failure);
            }
        )
    }

    public getLabel(msg, field): string {
        let fields: Array<string> = Object.keys(msg.bd);
        let found = fields.find((f) => {
            return f === field;
        })
        if (found) {
            return this.ctrlEmptyNullObject(msg.bd[field].label) ? "" : msg.bd[field].label;
        }
        return "-";
    }

    public getValue(msg, field): string {
        let fields: Array<string> = Object.keys(msg.bd);
        let found = fields.find((f) => {
            return f === field;
        })
        if (found) {
            return this.ctrlEmptyNullObject(msg.bd[field].value) ? "" : msg.bd[field].value;
        }
        return "-";
    }


    public createInterventionField(msg, field) {
        return createInterventionField(this.getLabel(msg, field), this.getValue(msg, field));
    }


    private createInterventionFromMessageV2(msg: any): InterventionActive {
        ////console.log(msg);

        let typeDetailArray = [];
        let intervention: InterventionActive = createInterventionObject();
        if (msg.bd.upd) {
            intervention.listLabelUpdate = msg.bd.upd.fields;
            intervention.notifyUpdate = msg.bd.upd.notify;
        }
        intervention.luogo = this.createInterventionField(msg, "sntsL");
        intervention.patologia = this.createInterventionField(msg, "sntsP");
        intervention.criticita = this.createInterventionField(msg, "sntsC");
        /*   typeDetailArray = [];
          if (this.ctrlEmptyNullArrayIndex(msg.bd.itvlist.value, 1)) {
               typeDetailArray.push({ label: '', value: "-" });
           } else {
               let objectArray: any[] = msg.bd.itvlist.value;
               for (var i = 1; i < objectArray.length; i++) {
                   typeDetailArray.push({ label: objectArray[i].label, value: objectArray[i].value });
               }
   
           } */

        intervention.criticitaObject = new ItemDetail();
        //intervention.criticitaObject.detailArray = typeDetailArray;

        intervention.modAttivazione = this.createInterventionField(msg, "infs");
        /* intervention.sirena = "-";
        intervention.sirenaLabel = ""; */
        intervention.comune = this.createInterventionField(msg, "mnct");
        intervention.localita = this.createInterventionField(msg, "lclt");
        typeDetailArray = [];
        typeDetailArray.push({ label: this.getLabel(msg, "lcnt"), value: this.getValue(msg, "lcnt") });
        intervention.localitaObject = new ItemDetail();
        intervention.localitaObject.detailArray = typeDetailArray;

        /* intervention.prov = this.ctrlStringValue(msg.bd.prov.value); */
        intervention.indirizzo = this.createInterventionField(msg, "strf");

        typeDetailArray = [];
        typeDetailArray.push({ label: this.getLabel(msg, "stnt"), value: this.getValue(msg, "stnt") });
        intervention.indirizzoObject = new ItemDetail();
        intervention.indirizzoObject.detailArray = typeDetailArray;

        intervention.luogoPubblico = {
            label: this.getLabel(msg, "plnm"),
            value: this.getValue(msg, "pltp") + " - " + this.getValue(msg, "plnm")
        };

        typeDetailArray = [];
        typeDetailArray.push({ label: this.getLabel(msg, "plnt"), value: this.getValue(msg, "plnt") });
        intervention.luogoPubblicoObject = new ItemDetail();
        intervention.luogoPubblicoObject.detailArray = typeDetailArray;

        intervention.civico = this.createInterventionField(msg, "hsnb");
        intervention.piano = this.createInterventionField(msg, "floo");

        intervention.codEmergenza = this.createInterventionField(msg, "evcd");

        typeDetailArray = [];
        intervention.codEmergenzaObject = new ItemDetail();
        typeDetailArray.push({ label: msg.bd.auths.label, value: this.ctrlStringValue(msg.bd.auths.value) });
        typeDetailArray.push({ label: msg.bd.rscplist.label, value: this.ctrlStringValue(msg.bd.rscplist.value) });
        typeDetailArray.push({ label: msg.bd.rescilist.label, value: this.ctrlStringValue(msg.bd.rescilist.value) });
        intervention.codEmergenzaObject.detailArray = typeDetailArray;

        let time = this.getValue(msg, "evtm").substring(10);

        intervention.dataOraSegnalazione = {
            label: this.getLabel(msg, "evtm"),
            value: time
        }

        typeDetailArray = [];
        intervention.dataOraSegnalazioneObject = new ItemDetail();
        typeDetailArray.push({ label: '', value: this.ctrlStringValue(msg.bd.evtm.value) });
        intervention.dataOraSegnalazioneObject.detailArray = typeDetailArray;

        intervention.personaRif = this.createInterventionField(msg, "refr");

        typeDetailArray = [];
        intervention.personaRifObject = new ItemDetail();
        typeDetailArray.push({ label: '', value: this.getValue(msg, "refr") });
        intervention.personaRifObject.detailArray = typeDetailArray;

        intervention.telefono = this.createInterventionField(msg, "phon");

        /* intervention.nPazienti = this.ctrlEmptyNullArrayIndex(msg.bd.itvlist.value, 0) ? "-" : msg.bd.value.itvlist[0].patn;
        intervention.sex = this.ctrlEmptyNullArrayIndex(msg.bd.itvlist.value, 0) ? "-" : msg.bd.value.value.itvlist[0].sex;
        intervention.eta = this.ctrlEmptyNullArrayIndex(msg.bd.itvlist.value, 0) ? "-" : msg.bd.value.value.itvlist[0].age; */

        typeDetailArray = [];
        intervention.noteObject = new ItemDetail();

        if (!this.ctrlEmptyNullObject(msg.bd.note)) {
            let length: number = this.configService.getConfigurationParam("GENERAL", "LENGHT_NOTE", '58');

            //  let length = 58;//TODO prelevare il parametro dal file di configurazione resource.
            let note = this.getValue(msg, "note").length <= length ? msg.bd.note.value.substring(0, msg.bd.note.value.length) : msg.bd.note.value.substring(0, length);

            let note1 = msg.bd.note.value.length <= length ? msg.bd.note.value.substring(0, msg.bd.note.value.length) : msg.bd.note.value.substring(0, length);
            let note2 = "";
            if (this.getValue(msg, "note").length > length) {
                note2 = this.ctrlStringValue(msg.bd.note.value).substring(length, msg.bd.note.value.length);
            }
            intervention.note = {
                label: this.getLabel(msg, "note"),
                value: this.getValue(msg, "note")
            }
            intervention.note.extra = new Map();
            intervention.note.extra["note1"] = note1;
            intervention.note.extra["note2"] = note2;
            typeDetailArray.push({ label: intervention.note.label, value: intervention.note.value });
        } else {
            //intervention.noteIniBreak = "-";
            typeDetailArray.push({ label: '-', value: '-' });
        }

        intervention.interviewList = msg.bd.itrvw.sym;
        intervention.sex.label = "SESSO";
        intervention.sex.value = this.getBaseInterview(msg.bd.itrvw, "SESSO");
        intervention.eta.label = "ETA'";
        intervention.eta.value = this.getBaseInterview(msg.bd.itrvw, "ETA'");
        intervention.nPazienti.label = "N° PAZIENTI";
        intervention.nPazienti.value = this.getBaseInterview(msg.bd.itrvw, "PAZIENT");

        if (msg.bd.posi) {
            intervention.position = msg.bd.posi;
            intervention.position.precisionZone = this.mapPrecisionZone(msg.bd.posi);
            intervention.position.precisionLevel = this.mapPrecisionLevel(msg.bd.posi) || "N.D";

        }

        intervention.patientNnReperito.label = "Paziente Non Reperito";
        intervention.patientNnReperito.value = false;

        return intervention;
    }

    mapPrecisionZone(posi) {
        let posiEval: number = posi.eval;
        if (posiEval == undefined) {
            return "N.D.";
        }
        return this.configService.getConfigurationParam("TARGET_LEVEL_PRECISION", posiEval.toString(), "N.D.");
    }

    mapPrecisionLevel(posi) {
        let posiEval = posi.eval;
        if (posiEval == undefined) {
            return "N.D.";
        }
        let target = this.configService.getConfigurationParamObject("TARGET_LEVEL_PRECISION", posiEval.toString());
        if (target) {
            return target.alias;
        }
    }

    private sendLastPosition(msg) {
        //this.agentService.publishMessage
        let payload = this.deviceStatus.getLastPosition();
        this.agentService.publishMessage("GPS", payload, msg.hd.uid, "mobile_queue").then(
            fulfilled => {

            },
            rejected => {

            });
    }

    private splitBaseValue(val: string) {
        let arr = val.split(":");
        if (arr) {
            return arr[arr.length - 1];
        }
        else {
            arr = val.split(" ");
            if (arr) {
                return arr[arr.length - 1];
            }
        }

    }

    private getBaseInterview(itrvw, match) {
        for (let i = 0; i < 4; i++) {
            if (itrvw["base" + i]) {
                let s: string = itrvw["base" + i];
                if (s.indexOf(match) > -1) return this.splitBaseValue(s);
            }
        }
        return "-";
    }


    private ctrlStringValue(valueIni: any): string {
        let value: string = "-";
        if (!this.ctrlEmptyNullObject(valueIni)) {
            value = valueIni;
        }
        return value;
    }

    private ctrlEmptyNullObject(valueIni: string): boolean {
        if (valueIni == undefined || valueIni == null || String(valueIni).trim().length <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private ctrlEmptyNullArray(valueIni: any): boolean {
        if (valueIni == undefined || valueIni == null || valueIni.length <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private ctrlEmptyNullArrayIndex(valueIni: any, index: number): boolean {
        return this.ctrlEmptyNullArray(valueIni) ? true : (this.ctrlEmptyNullArray(valueIni[index]) ? true : false);
    }

    private diffInMinutes(isoStringB, isoStringA) {
        let b = Date.parse(isoStringB);
        let a = Date.parse(isoStringA);
        return ((b - a) / 1000) / 60;

    }

    /*convert dd/mm/yyyy HH:mi:ss date to an iso string*/
    private fromDateToISOString(dateString: string, timeString: string) {
        let dateParts = dateString.split("/");
        var dateString = dateParts[2].toString() + "/" + (Number.parseInt(dateParts[1])).toString() + "/" + dateParts[0].toString();
        dateString = dateString + " " + timeString;
        let dateObject = new Date(dateString);
        return dateObject.toISOString();
    }


}
