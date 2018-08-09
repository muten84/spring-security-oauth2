import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { slideToRight } from '../../../router.animations';
import { Observable } from 'rxjs/Observable';
import { slideToBottom, fadeInOut, fadeOutIn } from '../../../router.animations';
import { AgentService, LocalBusService, Event, ResourceListService, PatientStatusService, ApplicationStatusService, CurrentTurn, CurrentIntervention } from '../../../service/service.module';
import { CheckInCommand, StorageService } from '../../../service/service.module';
import { Subscriber } from 'rxjs/Subscriber';
import { trigger, style, animate, transition } from '@angular/animations';
import { ConfigurationService } from '../../../service/core/configuration.service';
import { Router } from '@angular/router';
import { PatientCommand, PatientItem, createPatientObject, PatientField } from '../../../service/service.module';
import * as moment from 'moment';
import { Subscription } from 'rxjs/Subscription';
import { InterventionActive } from '../../../tablet-layout/intervention-layout/intervention.active.interface';




@Component({
    selector: 'app-patient',
    templateUrl: './patient.component.html',
    styleUrls: ['./patient.component.scss'],
    changeDetection: ChangeDetectionStrategy.Default,
    animations: [slideToRight(), slideToBottom(), fadeInOut(), fadeOutIn()]
})
export class PatientComponent implements OnInit {

    subs: Array<Subscription> = [];
    currentList: Array<any> = [];
    ospedaliObserver: Subscriber<Array<any>>;
    disableAnimation;
    openKeyBoardEvent: CustomEvent;
    patient: PatientItem;
    //typeEta: string;
    pages: number;
    indexPatient: number;
    prevEnabled: boolean = true;
    nextEnabled: boolean = true;
    currentPatient: number;
    activeItem: string;
    listPatient: Array<PatientItem>;//Map<number, PatientItem>;
    itemsPerPage: number = 9;
    pagesOspedali: number;
    currentTurn: CurrentTurn;
    ownerPage: string;

    errorField: Map<String, String> = new Map<String, String>();

    enabledOwner: boolean;

    currentPage: number = 0;
    //intervention: CurrentIntervention;
    intervention: InterventionActive;

    allItems: Array<any>;
    allItemsByFilter: Array<any>;

    ospedaliList: Array<any>;

    repartoList: Observable<Array<any>>;
    valutazSanitList: Array<PatientField>;
    sexList: Array<PatientField>;
    typeEtaList: Array<PatientField>;
    criticitaFineList: Array<PatientField>;

    //removeButton: boolean;


    synchronized: boolean;
    synchronizing: boolean;
    classLabelMezzo: string = "classLabelMezzoG";
    isListDirty: boolean;

    inPatient: string;

    proceed: boolean = true;

    removeEnabled = true;

    layout;

    constructor(
        private patientStatus: PatientStatusService,
        private appStatus: ApplicationStatusService,
        private router: Router,
        private configService: ConfigurationService,
        private agent: AgentService,
        private bus: LocalBusService,
        private resourceService: ResourceListService,
        private storage: StorageService,
        private cd: ChangeDetectorRef,
    ) { }


    ngOnInit() {
        this.synchronizing = this.patientStatus.isSynchronizing();
        //console.log("sono nel componente paziente!!!");
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.layout = this.appStatus.getLayout();
        this.disableAnimation = !enableAnimation;
        //gestione e aggiornamento della sincronizzazione dati paziente
        this.synchronized = this.patientStatus.isAllSynchronized();
        this.subs.push(this.bus.addObserver("checkPatientSaved").subscribe((dest) => {
            if (this.validateForm()) {
                if (this.patient.dirty && !this.patientStatus.isSynchronizing()) {
                    let inPatient = sessionStorage.getItem("inPatient");
                    let msg = "Le modifiche apportate al paziente non sono state salvate. Si vuole procedere al salvataggio? ";
                    if (inPatient && inPatient == "false") {
                        msg += " Attenzione rispondendo NO i dati non salvati verranno persi";
                    }
                    this.confirmDialog(msg, true);
                } else {
                    this.activeItem = this.router.url.includes('cardDatiAnagrafici') ? "cardDatiAnagrafici" : (this.router.url.includes('cardDatiClinici') ? "cardDatiClinici" : "cardValutazSan");
                    this.patientStatus.updateCurrentPatient(this.patient);
                    this.bus.notifyAll('navigateTo', {});
                }

                this.refresh();
            }
        }))
        this.subs.push(this.bus.addObserver("patientSynchronizing").subscribe((e) => {
            this.synchronized = false;
            this.patientStatus.setSynchronizing(true);
            this.synchronizing = this.patientStatus.isSynchronizing();
            this.refresh();
        }));
        this.subs.push(this.bus.addObserver("patientSynchronized").subscribe((e) => {
            this.synchronized = this.patientStatus.isAllSynchronized();
            this.patientStatus.setSynchronizing(!this.synchronized);
            this.synchronizing = this.patientStatus.isSynchronizing();
            this.indexPatient = this.patientStatus.getCurrentIndex();
            this.patient = this.patientStatus.getCurrentPatient();
            let listPatientNoDeleted = this.patientStatus.generateListPatient();

            if (!this.patient && listPatientNoDeleted.length <= 0) {
                this.createPatient(0);
            } else if (!this.patient && listPatientNoDeleted.length > 0) {
                this.patient = listPatientNoDeleted[0];
                this.patientStatus.updateCurrentPatient(this.patient);
            }

            this.setOwnerStatus();

            this.updateCountPatient();
            this.checkPatientRadioItem();
            this.refresh();

        }));
        this.subs.push(this.bus.addObserver("connectionStatus").subscribe((e: any) => {
            if (e && e.connected) {
                //qui probabilmente si dovrebbe ricontattare la centrale per avere l'ultimo messaggio fresco
                //non necessario se i pazienti arrivano con retained message
                //  this.synchronized = this.patientStatus.isAllSynchronized();
            }
            else {
                //sono disconnesso e' il caso di gestire almeno il fatto che l'utente e' potenzialmente non sincronizzato
                // this.synchronized = this.patientStatus.isAllSynchronized();
            }
        }));

        this.addStaticData();

        this.currentTurn = this.appStatus.getCurrentStatus().currentTurn;
        this.intervention = this.appStatus.getCurrentEvent();
        if (this.router != undefined) {
            this.activeItem = this.router.url.includes('cardDatiAnagrafici') ? "cardDatiAnagrafici" : (this.router.url.includes('cardDatiClinici') ? "cardDatiClinici" : "cardValutazSan");
        }
        let listPatientNoDeleted = this.patientStatus.generateListPatient();
        this.listPatient = this.storage.getItem("patientList");

        /* this.patientStatus.setListPatient(this.listPatient); */
        /* if (!this.patientStatus.getCurrentIndex()) {
            this.patientStatus.setCurrentIndex(1);
        } */

        if (!this.indexPatient) {
            this.indexPatient = this.patientStatus.getCurrentIndex();
        }

        this.patient = this.patientStatus.getCurrentPatient();
        /*   let patIn = this.patientStatus.getPatientInModifyIndex();
          if(patIn>=0){
              this.
              this.patient = 
          } */

        //        if (!this.patientStatus.ctrlObject(this.listPatient) || this.listPatient.length == 0) {
        if (!this.patient && listPatientNoDeleted.length <= 0) {
            this.createPatient(0);
        } else if (!this.patient && listPatientNoDeleted.length > 0) {
            this.patient = listPatientNoDeleted[0];
            this.patientStatus.updateCurrentPatient(this.patient);
        }

        this.refreshOspedaleDefault(this.storage.getItem("patientList"));

        this.setOwnerStatus();

        this.updateCountPatient();


        /*evento per far comparire la tastiera senza un input element visibile */
        this.openKeyBoardEvent = document.createEvent("CustomEvent");
        this.openKeyBoardEvent.initCustomEvent('OpenKeyboardEvent', true, true,
            { 'origin': 'hiddenItem', 'keyboardType': 'numeric', 'target': "turnDuration" });


        this.createPage();
        this.checkPatientRadioItem();
        this.checkListDirty();
        this.checkProceed();
        this.checkRemoveEnabled();
        /*  if (this.activeItem == "cardValutazSan") {
             //se sono in lista pazienti richiedi una sincronizzazione dei dati alla centrale
             //TODO: ottimizzare invio delle richieste inutili
             this.requestPatientSynch();
         } */
    }

    checkListDirty() {
        this.isListDirty = this.patientStatus.checkListDirty();
    }

    checkPatientRadioItem() {
        if (this.patient) {
            if (this.patient.criticitaFineId != undefined) {
                this.patient.criticitaFine = this.resourceService.getStaticDataValueByCode("CRITICITA_FINE", this.patient.criticitaFineId);
            }
            if (this.patient.sex && this.patient.sex.length > 1) {
                switch (this.patient.sex) {
                    case "MASCHIO":
                        this.patient.sex = "M";
                        break;
                    case "FEMMINA":
                        this.patient.sex = "F";
                        break;
                    case "SCONOSCIUTO":
                        this.patient.sex = "N";
                        break;
                }
            }
        }
    }

    addStaticData(): void {
        this.valutazSanitList = this.resourceService.getListStaticData("VALUTAZ_SAN");
        this.sexList = this.resourceService.getListStaticData("SEX");
        this.typeEtaList = this.resourceService.getListStaticData("TYPE_ETA");
        this.criticitaFineList = this.resourceService.getListStaticData("CRITICITA_FINE");

    }

    createPage() {
        if (this.pages > 1 && this.indexPatient < this.pages && this.indexPatient != 1) {
            this.prevEnabled = false;
            this.nextEnabled = false;
        } else if (this.pages <= 1) {
            this.prevEnabled = true;
            this.nextEnabled = true;
        }
        else if (this.indexPatient == 1 && this.pages > 1) {
            this.prevEnabled = true;
            this.nextEnabled = false;
        }
        else {
            this.prevEnabled = false;
            this.nextEnabled = true;
        }
    }

    ngOnDestroy() {
        //console.log("ngOnDestroy patient component")
        this.subs.forEach(s => { s.unsubscribe() })
        // this.clean();
    }

    reset() {

    }

    back() {
        window.history.back();
    }



    public add() {
        let len = this.patientStatus.getListPatient().length;
        //let step = len - this.indexPatient + 1;

        let step = this.patientStatus.generateListPatient().length - this.indexPatient + 1;

        this.createPatient(len);
        this.updateCountPatient();
        this.toMovePatientAdd(step);
    }



    public ctrlSavePatient() {
        return this.patient.dirty;
    }

    public remove() {
        this.showConfirmDialog("Si è sicuri di voler eliminare il paziente?", (response: boolean) => {
            if (response) {
                this.synchronizing = true;
                this.synchronized = false;
                this.patientStatus.removePatient(this.patient).then(typeRemove => {
                    if (typeRemove == "error") {
                        /* this.showInfoDialog("", "ATTENZIONE!!!Il paziente " + (this.patient.idArray + 1) + " NON e' stato rimosso."); */
                    } else {
                        /* this.showInfoDialog("", "Il paziente " + (this.patient.idArray + 1) + " e' stato rimosso."); */
                        this.patient = this.patientStatus.getCurrentPatient();
                        if (this.patient == undefined) {
                            this.createPatient(this.patientStatus.getCurrentIndex());
                        } else {
                            this.patient = this.patientStatus.getCurrentPatient();
                        }
                        this.listPatient = this.patientStatus.getListPatient();
                        this.indexPatient = this.patientStatus.getCurrentIndex();

                        this.updateCountPatient();

                    }
                    this.synchronizing = this.patientStatus.isSynchronizing();
                    // this.synchronized = false;
                    this.synchronized = this.patientStatus.isAllSynchronized();
                });
            }
            else {
                //niente il nulla
            }
        });


    }

    /*public getListPatientStorage()  {
       
        this.storage.getListPatientStorage("patientList").then(list => {     
            this.listPatient = list;  
        });
    
    }*/




    private showInfoDialog(title, text, callback?: Function) {
        let s = this.bus.addObserver('messageDialog').subscribe((event) => {
            if (event.type === 'messageDialogResponse') {
                this.bus.removeAllObservers('messageDialog', [s]);
                if (callback) {
                    callback();
                }
            }
        });

        this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '500px', title: "ATTENZIONE", content: text } });
    }

    onVKeyboardValue(origin: any) {
        this.patient.dirty = true;
        this.savePatientDirtyOnStorage();
        let object;
        if(this.currentList && this.currentList.length > 0){
            object = this.resourceService.getElement(origin.value, this.currentList);
            if(object != {}){
                origin.value = "";
            };
        }
        if (origin.target == 'destinazione') {
            this.patient.destinazione = object;
        } else if (origin.target == 'cittadinanza') {
            this.patient.cittadinanza = object;
        } else if (origin.target == 'comune') {
            this.patient.comune = object;
        } else if (origin.target == 'prestPrinc') {
            this.patient.prestazPrinc = object;
        } else if (origin.target == 'prestSec1') {
            this.patient.prestazSec1 = object;
        } else if (origin.target == 'prestSec2') {
            this.patient.prestazSec2 = object;
        } else if (origin.target == 'prestSec3') {
            this.patient.prestazSec3 = object;
        } else if (origin.target == 'prestSec4') {
            this.patient.prestazSec4 = object;
        } else if (origin.target == 'cognome') {
            this.patient.cognome = origin.value.toUpperCase();
        } else if (origin.target == 'nome') {
            this.patient.nome = origin.value.toUpperCase();
        } else if (origin.target == 'eta') {
            if (origin.target) {
                this.patient.dataNascita = undefined;
                this.patient.typeEta = this.resourceService.getElement("ANNI", this.typeEtaList);
            }
        } else if (origin.target == 'accompDa') {
            this.patient.accompDa = object;
        } else if (origin.target == 'dataNascita') {
            if (this.ctrlDate(origin.value)) {
                this.errorField.set("DataNascita", "0");
                this.cleanFieldEta_TypeEta();
            } else {
                if (this.calcEta_TypeEta(origin.value, true)) {
                    this.errorField.delete("DataNascita");
                }
            }
        }

        this.setMsgErr();

        this.currentList = [];
    }

    ctrlDate(value: any) {
        //let RegExPattern = /^((((0?[1-9]|[12]\d|3[01])[\.\-\/](0?[13578]|1[02])      [\.\-\/]((1[6-9]|[2-9]\d)?\d{2}))|((0?[1-9]|[12]\d|30)[\.\-\/](0?[13456789]|1[012])[\.\-\/]((1[6-9]|[2-9]\d)?\d{2}))|((0?[1-9]|1\d|2[0-8])[\.\-\/]0?2[\.\-\/]((1[6-9]|[2-9]\d)?\d{2}))|(29[\.\-\/]0?2[\.\-\/]((1[6-9]|[2-9]\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00)))|(((0[1-9]|[12]\d|3[01])(0[13578]|1[02])((1[6-9]|[2-9]\d)?\d{2}))|((0[1-9]|[12]\d|30)(0[13456789]|1[012])((1[6-9]|[2-9]\d)?\d{2}))|((0[1-9]|1\d|2[0-8])02((1[6-9]|[2-9]\d)?\d{2}))|(2902((1[6-9]|[2-9]\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00))))$/;
        let RegExPattern = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
        return value && value.length != 0 && !value.match(RegExPattern);
    }

    onInputChange(data) {
    }

    onChangeCheck(checked) {
        this.patient.dirty = true; this.savePatientDirtyOnStorage()
        this.patient.checkSconosc = checked.checked;
        if (this.patient.checkSconosc) {
            this.patient.nome = "SCONOSCIUTO";
            this.patient.cognome = "SCONOSCIUTO";
        } else {
            this.patient.nome = "";
            this.patient.cognome = "";
        }
    }

    public valueRadioButtonSelected(event) {
        this.patient.valutazSanitaria = event;
    }

    openDialogList(typeDialog, listType, value, nCols, width) {
        this.patient.dirty = true; this.savePatientDirtyOnStorage()
        let s = this.bus.addObserver(typeDialog).subscribe((event) => {
            if (event) {
                if (event.payload) {
                    let returnValue = event.payload;
                    //console.log("openDialogList", typeDialog, value);
                    if (value == 'ESITO') {
                        this.patient.esito = returnValue;
                        if (this.patient.esito.description.match("DECESSO") && this.patient.valutazSanitaria && this.patient.valutazSanitaria != "4") {
                            this.enabledCriticitaFine(true);
                            this.criticitaFineList[4].disabled = false;
                            this.patient.criticitaFine = this.resourceService.getStaticDataValueByCode("CRITICITA_FINE", 4);
                            this.patient.criticitaFineId = "4";
                        }
                        /*  if(this.patient.esito.refCode && this.patient.esito.refCode == "4"){
                              this.patient.criticitaFine =  this.patient.criticitaFine = this.resourceService.getStaticDataValueByCode("CRITICITA_FINE", this.patient.esito.refCode);
                              this.patient.criticitaFineId = this.patient.esito.refCode;
                              this.enabledCriticitaFine(true); 
                              this.criticitaFineList[4].disabled = false;  
                          }else{
                              this.enabledCriticitaFine(false);
                              if(this.patient.valutazSanitaria){                                                             
                                  this.patient.criticitaFine = this.resourceService.getStaticDataValueByCode("CRITICITA_FINE", this.patient.valutazSanitaria);
                                  this.patient.criticitaFineId = this.patient.valutazSanitaria;
                                  this.criticitaFineList[4].disabled = this.patient.valutazSanitaria == "4"?false:true;  
                              }
                          }*/
                    }
                    else if (value == 'accompda') {
                        this.patient.accompDa = returnValue;
                    }
                    else if (value == 'DESTINAZIONE') {
                        this.patient.destinazione = returnValue;
                    } else if (value == 'TYPE_ETA') {
                        this.patient.typeEta = returnValue;
                    } else if (value == 'PATHOLOGYCLASS') {
                        this.patient.classePatologia = returnValue;
                        if (this.patient.classePatologia.description.length != 0) {
                            this.resourceService.requestShortList('PATHOLOGY', this.patient.classePatologia.id.substring(0, 3)).then(listPathology => {
                                if (this.patient.patologia == undefined && this.patientStatus.ctrlObject(listPathology) && listPathology.length == 1) {
                                    this.patient.patologia = listPathology[0];
                                } else if (this.patient.patologia != undefined && this.patient.classePatologia.id.substring(0, 3) != this.patient.patologia.id.substring(0, 3)) {
                                    this.patient.patologia = this.patientStatus.ctrlObject(listPathology) && listPathology.length == 1 ? listPathology[0] : undefined;
                                }
                                this.refreshOneValue();
                            });
                        } else {
                            this.patient.patologia = undefined;
                            this.patient.classePatologia = undefined;
                        }
                    } else if (value == 'PATHOLOGY') {
                        this.patient.patologia = returnValue;
                        if (this.patient.patologia.description.length != 0) {
                            this.resourceService.requestShortList('PATHOLOGYCLASS', this.patient.patologia.id.substring(0, 3)).then(listPathologyClass => {
                                this.patient.classePatologia = this.patientStatus.ctrlObject(listPathologyClass) && listPathologyClass.length == 1 ? listPathologyClass[0] : undefined;
                                this.refreshOneValue();
                            });
                        } else {
                            this.patient.patologia = undefined;
                        }
                    }

                }

                this.bus.removeAllObservers(typeDialog, [s]);
            }

        });

        let filter = undefined;

        if (value == 'PATHOLOGY' && this.patient.classePatologia != undefined && this.patient.classePatologia.description.length != 0) {
            filter = this.patient.classePatologia.id.substring(0, 3);
        }

        if (value == 'ESITO' && this.patient.valutazSanitaria != undefined) {
            if (this.patient.valutazSanitaria === "4" || this.patient.valutazSanitaria === "0") {
                this.resourceService.requestShortList("serviceresulttype", undefined).then(list => {
                    list = this.resourceService.getElementsByCode(list, this.patient.valutazSanitaria, "ESITO");
                    list.push({ id: "", description: "" });
                    this.bus.notifyAll('dialogs', { type: typeDialog, payload: { items: list, width: width, title: "Scegliere un elemento dalla lista".toUpperCase(), nCols: nCols } });
                });
            } else {
                this.resourceService.requestShortList(listType, undefined).then(list => {
                    list = list.filter(item => item.description !== "N1 - DECESSO SUL POSTO")
                    list.push({ id: "", description: "" });
                    this.bus.notifyAll('dialogs', { type: typeDialog, payload: { items: list, width: width, title: "Scegliere un elemento dalla lista".toUpperCase(), nCols: nCols } });
                });
            }
        }
        else {
            this.resourceService.requestShortList(listType, filter).then(list => {
                list.push({ id: "", description: "" });
                this.bus.notifyAll('dialogs', { type: typeDialog, payload: { items: list, width: width, title: "Scegliere un elemento dalla lista".toUpperCase(), nCols: nCols } });
            });
        }
    }


    toMovePatient(action: string, step?: number) {
        /* if (this.ctrlSavePatient()) {
            this.showInfoDialog("", "Prima di visualizzare un altro paziente, salvare il paziente " + (this.patient.idArray + 1));
        } */
        if (action == 'prev') {
            this.patient = this.patientStatus.getMovePatient(-1, action);
            //this.patientStatus.setCurrentIndex(this.indexPatient)
            this.indexPatient = this.patientStatus.getCurrentIndex();
            this.updateCountPatient();
        } else {
            let t = 1;
            if (step) {
                t = step;
            }
            this.patient = this.patientStatus.getMovePatient(t, action);
            // this.patientStatus.setCurrentIndex(this.indexPatient)
            //this.patient.id + 1;
            this.indexPatient = this.patientStatus.getCurrentIndex();
            this.updateCountPatient();
        }
        this.synchronized = this.patientStatus.isAllSynchronized();
        this.setOwnerStatus();
        //this.checkRemoveEnabled();
        this.checkPatientRadioItem();
        this.refresh();
    }

    toMovePatientAdd(step: number) {
        
        this.patient = this.patientStatus.getMovePatientAdd(step);
        this.indexPatient = this.patientStatus.getCurrentIndex();
        this.updateCountPatient();
      
        this.synchronized = this.patientStatus.isAllSynchronized();
        this.setOwnerStatus();
        //this.checkRemoveEnabled();
        this.checkPatientRadioItem();
        this.refresh();
    }

    openDialogCalendar(typeDialog) {
        /*   let s = this.bus.addObserver(typeDialog).subscribe((event) => {
              if(event != undefined && event != null){
             
                  if(event.payload != undefined && event.payload != null){
                      
                      this.patient.dataNascita =  event.payload.description;                                   
                    
                      this.bus.removeAllObservers(typeDialog, [s]);
                  }
              }
  
          });   
         
          this.bus.notifyAll('dialogs', { type: typeDialog, payload: { items: null, width: '500px', title: "Scegliere un item dalla lista" } }); */
    }

    updateCountPatient() {
        this.pages = this.patientStatus.generateListPatient().length;
        if (this.pages == 0) {
            this.pages = 1;
        }
        this.createPage();
    }

    private createPatientCommand(): PatientCommand {
        let event = this.appStatus.getCurrentEvent();
        let interventionStatus = this.appStatus.getInterventionStatus();
        if (!interventionStatus) {
            interventionStatus = { code: -1 };
        }
        let cmd: PatientCommand = {
            evcd: event.codEmergenza.value,
            phase: interventionStatus.code,
            patient: this.patient
        }
        return cmd;
    }


    save() {
        if (this.validateForm()) {
            let listPatameterNull = this.patientStatus.ctrlPatientParameter(this.patient);
            if (listPatameterNull.length == 0) {
                this.savePatient(this.patient);
            } else {
                // this.confirmDialog("NON SONO STATI INSERITI I SEGUENTI DATI: " + listPatameterNull + ". CONTINUARE IL SALVATAGGIO?", false);
                this.viewGridDetail('NON SONO STATI INSERITI I SEGUENTI DATI. PROCEDERE CON IL SALVATAGGIO?', listPatameterNull, 800);
            }
        }
    }

    private onPatientSaved() {
        this.synchronizing = true;
        /*this.listPatient = this.patientStatus.getListPatientFromStorage();
        this.patient = this.patientStatus.getCurrentPatient();
        this.indexPatient = this.patientStatus.getCurrentIndex();*/
        this.enabledOwner = false;
        this.classLabelMezzo = "classLabelMezzoG";
        this.updateCountPatient();
        this.bus.notifyAll('patientSynchronized', {});
        this.errorField = new Map<String, String>();
        this.synchronizing = this.patientStatus.isSynchronizing();
        this.patient.dirty = false;
        this.savePatientDirtyOnStorage();
        
    }

    public savePatient(patient: PatientItem) {
        this.checkOwner(patient);

        this.patientStatus.savePatient(this.patient).then(
            (success) => {
                this.listPatient = this.patientStatus.getListPatientFromStorage();
                this.patient = this.patientStatus.getCurrentPatient();
                this.indexPatient = this.patientStatus.getCurrentIndex();
                this.requestPatientSynch();
                //this.onPatientSaved();
            },
            (failure) => {
                this.synchronized = false;
                this.showInfoDialog("", "ATTENZIONE! FALLITO Salvataggio del paziente " + (this.patient.idArray + 1));
            }
        );
    }

    checkOwner(patient: PatientItem) {
        if (!patient.owner) {
            this.patient.owner = this.agent.deviceId;
        }
    }



    /*save() {
        let isOwner = true;//TODO gestire l'isOwner con il cambio dalla valutazione sanitaria


        if (this.patient.valutazSanitaria == undefined) {
            this.showInfoDialog("", "Inserire la valutazione sanitaria");
        }
        else {
            this.patientStatus.savePatient(this.patient, isOwner).then(
                (success) => {

                    this.listPatient = this.patientStatus.getListPatient();
                    this.patient = this.patientStatus.getCurrentPatient();
                    this.updateCountPatient(0, "");
                    this.removeButton = false;
                    //this.showInfoDialog("", "Salvataggio del paziente " + (this.patient.idArray + 1) + " effettuato con successo ");
                    this.bus.notifyAll('patientSynchronized', {});
                },
                (failure) => {
                    this.synchronized = false;
                    this.showInfoDialog("", "ATTENZIONE! FALLITO Salvataggio del paziente " + (this.patient.idArray + 1));
                }

            );
        }

    }*/

    clean() {
       // this.patient.dirty = false;
        //this.savePatientDirtyOnStorage();
        let patientOld = this.patientStatus.getPatientById(this.patient.id, this.storage.getItem("patientList"));
        patientOld.dirty = false;
       
        /*     if (patientOld.valutazSanitaria != undefined) {
                this.patient = patientOld;
                this.patientStatus.updateCurrentPatient(patientOld);
            } else {
                this.patientStatus.cleanPatient(this.patient).then(typeclean => {
                    if (typeclean == "error") {
                        this.showInfoDialog("", "Annullamento del paziente " + (this.patient.idArray + 1) + " NON effettuato.");
                    } else {
             
                        this.patient = this.patientStatus.getCurrentPatient();
                        if (!this.patient) {
                            let pats = this.patientStatus.getListPatientFromStorage();
                            let id = 0;
                            if (pats) {
                                id = pats.length;
                            }
                            this.createPatient(id);
                            this.patientStatus.updateCurrentPatient(this.patient);
                        }
                        else {
                            this.toMovePatient('prev', 1);
                        }
    
                        this.listPatient = this.patientStatus.getListPatient();
                        this.updateCountPatient();
                    }
                });
            } */

        if (patientOld.coId) {
            this.patient = patientOld;
            this.patientStatus.updateCurrentPatient(patientOld);
            this.savePatientDirtyOnStorage();
        } else {
            this.patientStatus.removeUnusedPatient(patientOld.id);
            let pats = this.patientStatus.getListPatientFromStorage();
            let id = 0;
            if (pats) {
                id = pats.length;
            }
            this.createPatient(id);

            this.patientStatus.updateCurrentPatient(this.patient);
        }

        this.errorField = new Map<String, String>();

        this.setOwnerStatus();

        this.enabledCriticitaFine(false);
    }

    showConfirmDialog(valueMsg: string, callback: Function) {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.bus.removeAllObservers('confirmDialog', [s]);
                let response = event.payload;
                if (event.payload) {
                    callback(true);
                } else {
                    callback(false);
                }
            }

        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "ATTENZIONE", content: valueMsg } });
    }


    //FIXME rinominare per quello che fa... conferma il salvataggio dei dati nel caso in cuoi il patient è dirty 
    confirmDialog(valueMsg: string, check: boolean) {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.bus.removeAllObservers('confirmDialog', [s]);
                let response = event.payload;
                if (event.payload) {
                    /* this.activeItem = String(dest).includes('cardDatiAnagrafici') ? "cardDatiAnagrafici" : (String(dest).includes('cardDatiClinici') ? "cardDatiClinici" : "cardValutazSan"); */
                    if (this.errorField.size == 0) {
                        if (this.enabledOwner) {
                            this.patient.owner = this.agent.deviceId;
                            this.patientStatus.saveOrUpdate(this.patient);
                            this.setOwnerStatus();
                            this.patient.dirty = true; this.savePatientDirtyOnStorage()
                            //????? TODO this.savePatient(this.patient);                    
                        } else {
                            if (check) {
                                this.save();
                            }
                            else {
                                this.savePatient(this.patient);
                            }
                        }
                    } else {
                        this.setMsgErr();
                    }
                } else {
                    this.inPatient = sessionStorage.getItem("inPatient");

                    if (!this.patient.coId && !(this.inPatient && this.inPatient == "true")) {
                        this.patient.dirty = false;
                    }
                    /*this.clean(); */

                }
                this.inPatient = sessionStorage.getItem("inPatient");
                if (this.inPatient && this.inPatient == "true") {
                    if (response) {
                        if (this.errorField.size == 0) {
                            this.bus.notifyAll('navigateTo', {});
                        } else {
                            this.setMsgErr();
                        }
                    }
                }
                else {
                    this.bus.notifyAll('navigateTo', {});
                }

            }
        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "ATTENZIONE", content: valueMsg } });

    }

    confirmDialogOwner(valueMsg: string, check: boolean) {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.bus.removeAllObservers('confirmDialog', [s]);
                if (event.payload) {
                    this.ownerPage = this.currentTurn.resource;
                    this.enabledOwner = false;
                    this.patient.dirty = true; this.savePatientDirtyOnStorage()
                    //se ho risposto si posso forzare l'owner a me stesso
                    this.patient.owner = this.agent.deviceId;
                    this.patientStatus.updateCurrentPatient(this.patient);
                    this.refresh();
                } else {
                    this.enabledOwner = true;
                }
            }
        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "", content: valueMsg } });
    }



    public listResource(type, filter?: string) {
        this.resourceService.searchByFilter(type, filter || '').then(list => {
            //console.log("LIST length==>" + list.length);
            this.currentList = list.slice(0, 6)
        });
    }

    public refreshList(event, type: string) {
        //console.log("refreshing list", event, type);
        let val: string = event;
        this.listResource(type, val.toUpperCase());
    }

    public addEvent(type: string, event) {
        this.patient.dirty = true; this.savePatientDirtyOnStorage()
        let sDate = event.value._i.date + "/" + event.value._i.month + "/" + event.value._i.year;
        if (!this.ctrlDate(sDate)) {
            this.calcEta_TypeEta(event.value._i, false);
            if(this.errorField.get("DataNascita") != "1"){
                this.errorField.delete("DataNascita");
            }
        }
        else {
            this.errorField.set("DataNascita", "0");           
            this.cleanFieldEta_TypeEta();
        }

    }

    public calcEta_TypeEta(data, convOK): boolean {
        this.errorField.delete("DataNascita");
        if (data) {
            let bdate = true;
            let birthday: Date;
            if (!convOK) {
                birthday = new Date(data.year, data.month, data.date);
            } else {
                let newData: any[] = String(data).split("/");
                birthday = new Date(newData[2], newData[1] - 1, newData[0]);
            }
            let today: Date = new Date();
            var _MS_PER_DAY = 1000 * 60 * 60 * 24;
            var diffDays = Math.floor((today.getTime() - birthday.getTime()) / _MS_PER_DAY);

            if (diffDays < 0) {
                this.patient.dataNascita = undefined;
                this.cleanFieldEta_TypeEta();
                this.showInfoDialog("", "Data Nascita successiva alla data odierna");
                this.errorField.set("DataNascita", "1");
                bdate = false;
            }
            else {
                if (diffDays <= 31) {
                    this.patient.eta = diffDays;
                    this.patient.typeEta = this.resourceService.getElement("GIORNI", this.typeEtaList);
                }
                else if (diffDays > 31 && diffDays <= 366) {
                    this.patient.eta = Math.floor(diffDays / 30);
                    this.patient.typeEta = this.resourceService.getElement("MESI", this.typeEtaList);
                } else {
                    var years = today.getFullYear() - birthday.getFullYear();
                    this.patient.eta = today.getMonth() >= birthday.getMonth() ? years : (years - 1);
                    this.patient.typeEta = this.resourceService.getElement("ANNI", this.typeEtaList);
                }

            }
            return bdate;
        } else {
            this.cleanFieldEta_TypeEta();
            return false;
        }
    }

    itemSelected(type: string, data: any) {
        let enableCtrlValutazSan: boolean = !this.configService.getConfigurationParam("GENERAL", "EVALUATION_CRITICITY_DECOUPLING", true);
        if (type === 'valSan') {
            this.enabledCriticitaFine(false);

            this.errorField.delete("ValutazioneSanitaria");
            this.patient.dirty = true; this.savePatientDirtyOnStorage()
            if (enableCtrlValutazSan) {
                this.patient.esito = undefined;
                let object = this.resourceService.getStaticDataByCode("CRITICITA_FINE", data.id);
                this.patient.criticitaFine = object.description;
                this.patient.criticitaFineId = object.id;
                if (data.id === '0') {
                    this.patient.destinazione = undefined;
                }
                if (data.id === '4') {
                    this.enabledCriticitaFine(true);
                    this.criticitaFineList[4].disabled = false;
                } else {
                    this.criticitaFineList[4].disabled = true;
                }
            }

            this.patient.valutazSanitaria = data.id;
            if (this.enabledOwner) {
                this.confirmDialogOwner("Si desidera prendere in carico il paziente?", false);
            }
        } else if (type === 'sex') {
            /*  this.patient.dirty = this.enabledOwner?false:true;
              this.patient.sex = data.id;*/

            if (!this.enabledOwner) {
                this.patient.sex = data.id;
                this.patient.dirty = true; this.savePatientDirtyOnStorage()
            }
        } else if (type === 'criticitaFine') {
            if (enableCtrlValutazSan) {
                this.patient.dirty = true; this.savePatientDirtyOnStorage()
                if (this.criticitaFineList[data.id].disabled && !this.patient.esito) {
                    this.patient.criticitaFineId = this.resourceService.getStaticDataByCode("VALUTAZ_SAN", this.patient.valutazSanitaria).id;
                    this.patient.criticitaFine = this.resourceService.getStaticDataValueByCode("CRITICITA_FINE", this.patient.criticitaFineId);
                }
            }
            else if (!this.enabledOwner) {
                this.patient.criticitaFine = data.description;
                this.patient.criticitaFineId = this.resourceService.getStaticDataCodeByValue("CRITICITA_FINE", data.description);
                this.patient.dirty = true; this.savePatientDirtyOnStorage()
            }


        }
    }

    createPatient(idArray: number) {
        this.patient = createPatientObject();
        this.patient.idArray = idArray;
        this.patient.id = this.patientStatus.ctrlObject(this.storage.getItem("patientList")) ? this.storage.getItem("patientList").length : 0;
        this.patient.typeEta = this.resourceService.getElement("ANNI", this.typeEtaList);
        
        this.resourceService.searchByFilter("country", "ITALIA").then(list => {
            //console.log("LIST length==>" + list.length);
            this.currentList = list.slice(0, 6);
            this.patient.cittadinanza = this.currentList.length > 1?this.currentList[1]:this.currentList[0];
            this.patientStatus.updateCurrentPatient(this.patient);
            this.patientStatus.saveOrUpdate(this.patient);
            //this.removeButton = idArray == 0 ? true : false;
            this.refresh();
        }); 

        this.patient.owner = this.agent.deviceId;
        this.patient.destinazione = this.resourceService.getOspedaleDefault();
        this.patient.deleted = false;
        this.patient.synchronized = false;
        this.patient.dirty = false;
        this.patientStatus.updateCurrentPatient(this.patient);
        this.patientStatus.saveOrUpdate(this.patient);
        //this.removeButton = idArray == 0 ? true : false;
        this.refresh();

    }

    public viewGridDetail(title: string, items, widthModal) {

        let s = this.bus.addObserver('confirmGridDialog').subscribe((event) => {
            // if (event.type === 'gridDialogResponse') {

            this.bus.removeAllObservers('confirmGridDialog', [s]);
            if (event.payload) {
                this.savePatient(this.patient);
            }


            // }
        });
        this.bus.notifyAll('dialogs', { type: 'confirmGridDialog', payload: { width: widthModal, title: title, items: items, content: items, justifyContent: true } });
    }

    public setOwnerStatus() {
        if (!this.patient) {
            this.ownerPage = "";
        } else {
            this.ownerPage = this.patientStatus.ctrlObject(this.patient.ownerVehicle) ? this.patient.ownerVehicle : this.appStatus.getCurrentStatus().currentTurn.resource;
            if (this.patientStatus.ctrlObject(this.patient.owner) && this.patient.owner != this.agent.deviceId) {
                this.enabledOwner = true;
                this.classLabelMezzo = "classLabelMezzoR";
            } else {
                this.enabledOwner = false;
                this.classLabelMezzo = "classLabelMezzoG";
            }
        }
    }

    public refresh() {
        this.checkListDirty();
        this.synchronized = this.patientStatus.isAllSynchronized();
        /* this.setOwnerStatus(); */
        this.checkRemoveEnabled();
        //this.cd.detectChanges();

    }

    checkRemoveEnabled() {
        let len = this.patientStatus.generateListPatient().length;
        if (!this.patient.coId && len > 1) {
            this.removeEnabled = true;
        }
        else if (this.enabledOwner) {
            this.removeEnabled = false;
        }
        else {
            this.removeEnabled = !(this.patient.dirty || this.isListDirty || !this.synchronized)
        }




    }

    public refreshOneValue() {
        // this.cd.detectChanges();
    }

    setMsgErr() {
        if (this.errorField.get("ValutazioneSanitaria") === "0") {
            this.showInfoDialog("Valutazione Sanitaria", "Inserire la valutazione sanitaria.");
        } else if (this.errorField.get("DataNascita") === "0") {
            this.showInfoDialog("Data Nascita", "La data di nascita non risulta corretta. Utilizzare il formato gg/MM/aaaa.");
            this.cleanFieldEta_TypeEta();
        } else if (this.errorField.get("DataNascita") === "1") {
            this.showInfoDialog("Data Nascita", "Data Nascita successiva alla data odierna");
        }
    }

    validateForm(): boolean {
        let validate: boolean = true;
        // && this.patient.valutazSanitaria != undefined
        if (!this.patient.dirty) {
            return true;
        }
        if (this.patient.valutazSanitaria === undefined) {
            this.errorField.set("ValutazioneSanitaria", "0");
        }

        if (this.errorField.size > 0) {
            this.setMsgErr();
            validate = false;
        }

        return validate;
    }

    checkProceed() {
        if (this.activeItem != "cardValutazSan") {
            if (this.patient.valutazSanitaria === undefined) {
                this.showInfoDialog(
                    "Valutazione Sanitaria",
                    "Prima di procedere alla compilazione delle altre sezioni è necessario inserire la valutazione sanitaria.",
                    () => {
                        //this.activeItem = "cardValutazSan";
                        this.refreshOneValue();
                        this.router.navigate(["/intervention/patient/cardValutazSan"])

                    });
                this.proceed = false;
            }
            //this.proceed = true;
            return this.proceed;

        }
    }

    private requestPatientSynch() {
        this.patientStatus.requestPatientSynch().then(fulfilled => {
            this.onPatientSaved();
        });
    }

    enabledCriticitaFine(value: boolean) {
        this.criticitaFineList.forEach((item) => {
            this.criticitaFineList[item.id].disabled = value;
        });
    }

    refreshOspedaleDefault(list) {
        if (!this.patient.destinazione && sessionStorage.getItem("hospital")) {
            list.forEach((item) => {
                if (!item.deleted && item.destinazione == undefined && this.patientStatus.ctrlObject(this.patient.owner) && this.patient.owner == this.agent.deviceId) {
                    item.destinazione = this.resourceService.getOspedaleDefault();
                    this.patient.destinazione = item.destinazione;
                }
            });
            this.storage.saveItem("patientList", list);
            this.patientStatus.updateCurrentPatient(this.patient);

            // this.patient.dirty = true; this.savePatientDirtyOnStorage()
        }
    }

    cleanFieldEta_TypeEta() {
        this.patient.eta = undefined;
        this.patient.typeEta = this.resourceService.getElement("ANNI", this.typeEtaList);
    }

    savePatientDirtyOnStorage() {
        this.patientStatus.saveDirtyOnStorage(this.patient);   //saveOrUpdate(this.patient);
    }

}