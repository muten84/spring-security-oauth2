import { Component, OnInit } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { CoreTableColumn } from "../../core/table/core-table/core-table.component";
import { TurnRequestDTO, TurnDTO, ParkingServiceService, TurnModuleServiceService, SanitaryCategory118, Convention118, Vehicle118,
    Value,TransportModuleServiceService} from "services/gen";
import { Observable, BehaviorSubject } from "rxjs";
import { ComponentHolderService } from "../../service/shared-service";
import { Subject } from "rxjs";
import { convertToDate,convertMomentDateToStruct } from "app/util/convert";
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import * as moment from 'moment';


@Component({
    selector: 'turn-modal',
    templateUrl: './turn-modal-component.html'
})
export class TurnModalComponent implements OnInit { 

    public parkingVehicleTurnId: string;
    public update: boolean;

    startDateStruct: NgbDateStruct;
    endDateStruct: NgbDateStruct;

    public turn:TurnDTO;

    //lista postazioni
    public parkingItems: Subject<Array<any>>;
    //lista mezzi
    public vehicles : Subject<Array<any>>;
    //lista catgorie sanitarie
    public sanitaryCategories: Subject<Array<any>>;
    //lista convention
    public conventions: Subject<Array<any>>;
    //mezzo selezionato
    public selectedVehicle: Vehicle118;
    //categoria sanitaria selezionata
    public selectedSanitaryCategory: SanitaryCategory118;
    //convention selezionata
    public selectedConvention: Convention118;
    
    


    constructor(
        protected activeModal: NgbActiveModal,
        protected comp: ComponentHolderService,
        protected parkingService: ParkingServiceService,
        protected transportService: TransportModuleServiceService,
        protected turnService: TurnModuleServiceService,
        protected componentService: ComponentHolderService,
    ) {
            this.parkingItems = new Subject();
            this.sanitaryCategories = new Subject<Array<any>>();
            this.conventions = new Subject<Array<any>>();
            this.vehicles = new Subject();
    }

    ngOnInit(): void {
        this.clean();
        if (this.parkingVehicleTurnId){
            let req: TurnRequestDTO = {
                parkingVehicleTurnId: this.parkingVehicleTurnId
            };
            //sto in update, ricarico il turno dal server
            this.componentService.getRootComponent().mask();
            this.turnService.getTurn(req).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
            }).subscribe(res => {
                this.turn = res;
                this.startDateStruct = convertMomentDateToStruct(moment(res.startTurnDate));
                this.endDateStruct = convertMomentDateToStruct(moment(res.endTurnDate));
                
                this.selectedVehicle={"id":res.vehicleId,"code":res.vehicleCode};
                this.selectedConvention={"id":res.conventionId,"description":res.conventionDescription};
                this.selectedSanitaryCategory={"id":res.sanitaryCategoryId,"description":res.sanitaryCategoryDescription};
                
                this.componentService.getRootComponent().unmask();
            });
        }else {
            //sto in insert, valorizzo la convenzione di default, ovvero secondario
            this.setSecConvention118();
        }
        this.selectSanitaryCategory118Web();
        this.selectConvention118();
        
    }

    //carica la lista di parking filtrata per 'name'
    updateParkingList(name?: string) {
        let filter = { name: name ? name.toUpperCase() : '' };
        this.parkingService.getParkingCompact(filter.name).subscribe(list => {
            this.parkingItems.next(list);
        });
    }

    //carica la lista di mezzi del 118 filtrata per nome
    retrieveVehicle118ByCode(code?: string) {
        this.transportService.retrieveVehicle118ByCode(code ? code.toUpperCase() : '').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento dei mezzi del 118');
            return [];
        }).subscribe(res => {
            this.vehicles.next(res.resultList);
        });

    }


    //carica la lista delle categorie sanitarie
    selectSanitaryCategory118Web() {
        this.turnService.selectSanitaryCategory118Web().catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle categorie sanitarie');
            return [];
        }).subscribe(res => {
            this.sanitaryCategories.next(res.resultList);
        });

    }


    //carica la convenzione di default (code = SEC)
    public setSecConvention118() {
        this.turnService.selectConvention118('SEC').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento della convention SECONDARIO');
            return [];
        }).subscribe(res => {
            if (res && res.resultList && res.resultList.length==1){
                this.selectedConvention={"id":res.resultList[0].id,"description":res.resultList[0].description,"code":res.resultList[0].code};
                this.turn.conventionId = this.selectedConvention.id;
            }
        });

    }

    //carica la lista delle convention del 118
    public selectConvention118() {
        this.turnService.selectConvention118('').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle convention');
            return [];
        }).subscribe(res => {
            this.conventions.next(res.resultList);
        });

    }

    close() {
        //controlli sui valori passati in input
        if (this.turn.startTurnDate && this.turn.startTurnTime){
            if (this.turn.endTurnDate && this.turn.endTurnTime){
                if (moment(this.turn.startTurnDate).isBefore(moment(this.turn.endTurnDate),'day')){
                    if (this.turn.vehicleId){
                        if (this.turn.parkingCode){
                            if (this.turn.conventionId){
                                this.comp.getRootComponent().mask();
                                // invio la richiesta al server   
                                if (this.update){
                                    this.turn.id = this.parkingVehicleTurnId;
                                    this.turnService.updateTurn(this.turn).subscribe(res => {
                                    this.comp.getRootComponent().unmask();
                                    if (res.checkCollidedTurnResult){
                                        if (res.checkCollidedTurnMessage){
                                            this.componentService.getRootComponent().showModal('Attenzione', res.checkCollidedTurnMessage);
                                        }
                                    }else {
                                        this.activeModal.close();
                                    }
                                    });
                                }else {
                                    this.turnService.insertTurn(this.turn).subscribe(res => {
                                    this.comp.getRootComponent().unmask();
                                    if (res.checkCollidedTurnResult){
                                        if (res.checkCollidedTurnMessage){
                                            this.componentService.getRootComponent().showModal('Attenzione', res.checkCollidedTurnMessage);
                                        }
                                    }else {
                                        this.activeModal.close();
                                    }
                                    });
                                }   
                            }else {
                                var message = 'Selezionare una convenzione.';
                                this.componentService.getRootComponent().showModal('Attenzione', message);
                            }
                        }else {
                            var message = 'Selezionare una postazione dalla lista postazioni.';
                            this.componentService.getRootComponent().showModal('Attenzione', message);
                        }
                    }else {
                        var message = 'Selezionare un mezzo dalla lista mezzi.';
                        this.componentService.getRootComponent().showModal('Attenzione', message);
                    }
                }else {
                    var message = 'Definire un intervallo di tempo valido per il turno.';
                    this.componentService.getRootComponent().showModal('Attenzione', message);
                }
            }else {
                var message = 'Definire data/ora di inizio turno validi.';
                this.componentService.getRootComponent().showModal('Attenzione', message);
            }
        }else {
            var message = 'Definire data/ora di inizio turno validi.';
            this.componentService.getRootComponent().showModal('Attenzione', message);
        }
        
        
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    vehicleSelected(value: Vehicle118): void {
        if (value) {
            console.log('selectedVehicle = '+value);
            this.turn.vehicleId=value.id;
            this.turn.vehicleCode=value.code;
        }else {
            this.turn.vehicleId=null;
            this.turn.vehicleCode=null;
        }
    }

    sanitaryCategorySelected(value: SanitaryCategory118): void {
        if (value) {
            console.log('selectedSanitaryCategory = '+value);
            this.turn.sanitaryCategoryId=value.id;
            //this.turn.sanitaryCategoryCode=value.code;
        }else {
            this.turn.sanitaryCategoryId=null;
            //this.turn.sanitaryCategoryCode=null;
        }
    }

    conventionSelected(value: Convention118): void {
        if (value) {
            console.log('selectedConvention = '+value);
            this.turn.conventionId=value.id;
            //this.turn.conventionCode=value.code;
        }else {
            this.turn.conventionId=null;
            //this.turn.conventionCode=null;
        }
    }

    startDateSelected(value: NgbDateStruct): void {
        if (value) {
            this.turn.startTurnDate=convertToDate(value, "");
        }else {
            this.turn.startTurnDate = null;
        }
    }

    endDateSelected(value: NgbDateStruct): void {
        if (value) {
            this.turn.endTurnDate=convertToDate(value, "");
        }else {
            this.turn.endTurnDate = null;
        }
    }

    clean() {
    
        this.selectedSanitaryCategory=null;
        this.selectedConvention=null;
        this.selectedVehicle=null;
        this.startDateStruct=null;
        this.endDateStruct=null;
        this.turn = {
            startTurnTime:null,
            endTurnTime:null,
            startTurnDate:null,
            endTurnDate:null,
            vehicleId:null,
            vehicleCode:null,
            sanitaryCategoryId:null,
            conventionId:null,
            parkingCode:null,
            note:null
        };
        
    }
}
