import {
    ViewChild, ElementRef, Directive, Component, OnInit,
    ViewEncapsulation, ViewContainerRef, EventEmitter, Input, Output, AfterViewInit
} from '@angular/core';
import { ComponentHolderService, MiddleComponent, HeaderComponent } from '../service/shared-service';
import { OverviewCiclicalFilterDTO, ParkingServiceService} from '../../services/services.module';
import { Subject } from "rxjs";
import { FormGroupGeneratorService } from '../core/validation/validation.module';
import { FormGroup, Validators } from '@angular/forms';


@Component({
    selector: 'sinottico-cicliche-header',
    templateUrl: './sinottico-cicliche-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class SinotticoCiclicheHeaderComponent implements OnInit, HeaderComponent, AfterViewInit {

    public selectedStatus: any;
    //private selectedDays: Array<any> = [];

    public filter: OverviewCiclicalFilterDTO;
    public parkingItems: Subject<Array<any>>;
    public statusItems: Array<any> = [
        'ATTIVE', 'SOSPESE'
    ];
    public dayItems: Array<any> = [
        'LUNEDI', 'MARTEDI', 'MERCOLEDI', 'GIOVEDI', 'VENERDI', 'SABATO', 'DOMENICA'
    ];

    /* FormGroup per la validazione */
    ciclicheFG: FormGroup;

    constructor(
        private componentService: ComponentHolderService, 
        private parkingService: ParkingServiceService,
        private fgs: FormGroupGeneratorService
    )
        {
            this.parkingItems = new Subject();
            this.ciclicheFG = this.fgs.getFormGroup('sinCicliche');
        }

    ngOnInit(): void {
        this.resetFilter();
    }


    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('currentSearchHeader', this);
        setTimeout(() => {

            let filterS = sessionStorage.getItem("ciclicalFilter");
            if (filterS) {
                this.filter = JSON.parse(filterS);
                //valorizzo selectedStatus leggendoli dal filtro
                if (this.filter.status){
                    this.selectedStatus={"id":this.filter.status,"text":this.filter.status};
                }
                //manipolo filter.dayStruct per settare anche il text
                if (this.filter.daysStruct){
                    var tmpDaysStruct : Array<any> = [];
                    for (var i = 0; i < this.filter.daysStruct.length; i++) {
                        tmpDaysStruct[i]={"id":this.filter.daysStruct[i].id,"text":this.filter.daysStruct[i].id};
                    }
                    this.filter.daysStruct = tmpDaysStruct;
                }
            }
        }, 0)
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.ciclicheFG);
    }

    //carica la lista di parking filtrata per 'name'
    updateParkingList(name?: string) {
        let filter = { name: name ? name.toUpperCase() : '' };
        this.parkingService.getParkingCompact(filter.name).subscribe(list => {
            this.parkingItems.next(list);
        });
    }

    //al click del filtro 'Tutte' vengono annullati tutti gli altri filtri
    onChangeAll(event) {
        if (event) {
            this.filter.todayCiclicalFlag = false;
            this.filter.transportDate = null;
            this.filter.fromDate = null;
            this.filter.toDate = null;
            this.filter.parkingCode = null;
            this.filter.transportedPatient = null;
            this.filter.bookingCode =  null;
            this.filter.status =  null;
            this.filter.days =  [];
            this.filter.daysStruct = [];
            this.selectedStatus= null;
        }
    }

    //al click del filtro 'Oggi' vengono annullati i filtri 'Tutte' , 'Data di trasporto' 
    onChangeToday(event) {
        if (event) {
            this.filter.allCiclicalFlag = false;
            this.filter.transportDate = null;
            //this.filter.days =  [];
            //this.filter.daysStruct = [];
        }
    }

    fetchHeaderData(id: string) { };

    resetFilter(): void {
        this.filter = {
            allCiclicalFlag: true,
            todayCiclicalFlag: false,
            transportDate: null,
            fromDate: null,
            toDate: null,
            transportedPatient: null,
            bookingCode:null,
            parkingId:null,
            parkingCode:null,
            days:[],
            daysStruct:[],
            status:null
        };
        this.selectedStatus= null;
    }

    getHeaderFilter(): any {
        return this.filter
    };

    statusSelected(value: any): void {
        if (value.text) {
            console.log('selectedStatus = '+this.selectedStatus);
            this.filter.status=value.id;
        }
    }

    statusRemoved(value: any): void {
        this.selectedStatus=null;
        this.filter.status=null;
    }

    triggerSubmit(): void { 
    };

}




