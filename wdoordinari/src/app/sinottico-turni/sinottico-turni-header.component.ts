import {
    ViewChild, ElementRef, Directive, Component, OnInit,
    ViewEncapsulation, ViewContainerRef, EventEmitter, Input, Output, AfterViewInit
} from '@angular/core';
import { ComponentHolderService, MiddleComponent, HeaderComponent } from '../service/shared-service';
import { OverviewTurnFilterDTO, ParkingServiceService, TurnModuleServiceService, SanitaryCategory118, Convention118, Vehicle118,
    Value,TransportModuleServiceService,VehicleDirectAssignmentDTO} from '../../services/services.module';
import { Subject } from "rxjs";
import { convertToDate, convertMomentDateToStruct } from "app/util/convert";
import * as moment from 'moment';
import { Observable } from 'rxjs/Observable';
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';



@Component({
    selector: 'sinottico-turni-header',
    templateUrl: './sinottico-turni-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class SinotticoTurniHeaderComponent implements OnInit, HeaderComponent, AfterViewInit {

    private selectedStatus: any;

    public filter: OverviewTurnFilterDTO;
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
    
    public initDateToSet: NgbDateStruct;

    /* FormGroup per la validazione */
    turniFG: FormGroup;

    constructor(
        private componentService: ComponentHolderService, 
        private parkingService: ParkingServiceService,
        private transportService: TransportModuleServiceService,
        public turnService: TurnModuleServiceService,
        private fgs: FormGroupGeneratorService,
    )
        {
            this.parkingItems = new Subject();
            this.sanitaryCategories = new Subject<Array<any>>();
            this.conventions = new Subject<Array<any>>();
            this.vehicles = new Subject();
            this.turniFG = this.fgs.getFormGroup('sinTurni');
        }

    ngOnInit(): void {
        this.initDateToSet = convertMomentDateToStruct(moment(new Date()));
        this.resetFilter();
    }


    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('currentSearchHeader', this);
        setTimeout(() => {

            let filterS = sessionStorage.getItem("turnFilter");
            if (filterS) {
                this.filter = JSON.parse(filterS);
                //valorizzo initDateToSet recuperandola dal filtro
                if (this.filter.initTurnDate){
                    this.initDateToSet = convertMomentDateToStruct(moment(this.filter.initTurnDate));
                }
                //valorizzo i valori eventualmente selezionati dalle combo, recuperandoli dal filtro
                if (this.filter.vehicleId && this.filter.vehicleCode){
                    this.selectedVehicle={"id":this.filter.vehicleId,"code":this.filter.vehicleCode};
                }
                if (this.filter.sanitaryCategoryId && this.filter.sanitaryCategoryDescr){
                    this.selectedSanitaryCategory={"id":this.filter.sanitaryCategoryId,"description":this.filter.sanitaryCategoryDescr,"code":this.filter.sanitaryCategoryCode};
                }
                if (this.filter.conventionId && this.filter.conventionDescr){
                    this.selectedConvention={"id":this.filter.conventionId,"description":this.filter.conventionDescr,"code":this.filter.conventionCode};
                }
            }
            this.selectSanitaryCategory118Web();
            this.selectConvention118();
        }, 0)
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.turniFG);
    }

    //carica la lista di parking filtrata per 'name'
    updateParkingList(name?: string) {
        let filter = { name: name ? name.toUpperCase() : '%' };
        this.parkingService.getParkingCompact(filter.name).subscribe(list => {
            this.parkingItems.next(list);
        });
    }

    //carica la lista di mezzi filtrata per nome
    retrieveVehicle118ByCode(code?: string) {
        console.log('code= '+code)
        this.transportService.retrieveVehicle118ByCode(code ? code.toUpperCase() : '').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento dei mezzi del 118');
            return [];
        }).subscribe(res => {
            this.vehicles.next(res.resultList);
        });

    }


    //carica la lista delle categorie sanitarie del 118
    selectSanitaryCategory118Web() {
        
        this.turnService.selectSanitaryCategory118Web().catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle categorie sanitarie del 118');
            return [];
        }).subscribe(res => {
            this.sanitaryCategories.next(res.resultList);
        });

    }


    //carica la lista delle convention del 118
    public selectConvention118() {
        this.turnService.selectConvention118('').catch((e, o) => {
            this.componentService.getRootComponent().showToastMessage('error', 'Errore nel caricamento delle convention del 118');
            return [];
        }).subscribe(res => {
            this.conventions.next(res.resultList);
        });

    }

    fetchHeaderData(id: string) { };

    resetFilter(): void {
        this.filter = {
            parkingId:null,
	        parkingCode:null,
	        vehicleId:null,
	        vehicleCode:null,
	        conventionId:null,
            conventionCode:null,
            conventionDescr:null,
	        sanitaryCategoryId:null,
            sanitaryCategoryCode:null,
            sanitaryCategoryDescr:null,
            initTurnTime:null,
            initTurnDate:convertToDate(this.initDateToSet, "")
        };
        this.selectedVehicle=null;
        this.selectedSanitaryCategory=null;
        this.selectedConvention=null;
    }

    getHeaderFilter(): any {
        return this.filter
    };

    vehicleSelected(value: Vehicle118): void {
        if (value) {
            console.log('selectedVehicle = '+value);
            this.filter.vehicleId=value.id;
            this.filter.vehicleCode=value.code;
        }else {
            this.filter.vehicleId=null;
            this.filter.vehicleCode=null;
        }
    }

    sanitaryCategorySelected(value: SanitaryCategory118): void {
        if (value) {
            this.filter.sanitaryCategoryId=value.id;
            this.filter.sanitaryCategoryCode=value.code;
            this.filter.sanitaryCategoryDescr=value.description;
        }else {
            this.filter.sanitaryCategoryId=null;
            this.filter.sanitaryCategoryCode=null;
            this.filter.sanitaryCategoryDescr=null;
        }
    }

    conventionSelected(value: Convention118): void {
        if (value) {
            this.filter.conventionId=value.id;
            this.filter.conventionCode=value.code;
            this.filter.conventionDescr=value.description;
        }else {
            this.filter.conventionId=null;
            this.filter.conventionCode=null;
            this.filter.conventionDescr=null;
        }
    }

    dateSelected(value: NgbDateStruct): void {
        if (value) {
            console.log('selectedDate = '+value);
            this.filter.initTurnDate=convertToDate(value, "");
        }else {
            this.filter.initTurnDate = null;
        }
    }




    triggerSubmit(): void { 

    };
}






