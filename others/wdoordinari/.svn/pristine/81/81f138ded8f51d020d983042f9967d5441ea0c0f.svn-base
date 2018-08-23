
import {
    ViewChild, ElementRef, Directive, Component, OnInit,
    ViewEncapsulation, ViewContainerRef, EventEmitter, Input, Output, AfterViewInit
} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { OrdinariCombo } from '../core/core.module'
import { NgbModal, ModalDismissReasons, NgbDropdown } from '@ng-bootstrap/ng-bootstrap';

import { RicercaBookingFilter, BookingSearchItem, Item } from './ricerca-avanzata-prenotazioni-model';
import { ComponentHolderService, MiddleComponent, HeaderComponent } from '../service/shared-service';
import {
    BookingModuleServiceService, OverviewBookingDTO,
    OverviewBookingFilterDTO
} from '../../services/services.module';

@Component({
    selector: 'ricerca-avanzata-prenotazioni-header',
    templateUrl: './ricerca-avanzata-prenotazioni-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class RicercaAvanzataPrenotazioniHeaderComponent implements OnInit, HeaderComponent, AfterViewInit {

    public filter: OverviewBookingFilterDTO;
    //public filtroAggiuntivo: any;
    public valueFilterAdd: string;
    public selectedFilterAdd: any;
    public isEditDisabled: boolean = true;

    public addizionaliItems: Array<any> = [
        'PART.INTRA-H', 'PART.NON-INTRA-H', 'DEST.INTRA-H', 'DEST.NON-INTRA-H',
        'MATTINA', "POMERIGGIO", "CODICE", "PARTENZA", "DESTINAZIONE", "CONVENZIONE",
        "TIPO DI TRASPORTO", "COGNOME TRASPORTATO", "PRENOTAZIONI CICLICHE", "ORARIO INDEFINITO"
    ];


    @Output() searchResult = new EventEmitter();

    listAddFilterItems = [];
    selectedIndex: number;

    select(index: number) {
        this.selectedIndex = index;
    }

    constructor(private componentService: ComponentHolderService, private bookingService: BookingModuleServiceService) {

    }

    ngOnInit(): void {
        this.filter = {
            allBookingFlag: false,
            todayBookingFlag: true,
            tomorrowBookingFlag: false,
            codeList: [],
            timeSlotList: [],
            patientSurnameTxtList: [],
            typeTxtList: [],
            conventionTxtList: [],
            startAddressTxtList: [],
            endAddressTxtList: [],
            transportDate: null,
            intraOspFlag: false,
            interOspFlag: false,
            startIntraOspFlag: false,
            endIntraOspFlag: false,
            startExtraOspFlag: false,
            endExtraOspFlag: false,
            cyclicalBookingFlag: false
        };
    }

    ngAfterViewInit(): void {
        //console.log('ngAfterViewInit: RicercaPrenotazioniTableComponent: ')
        this.componentService.setHeaderComponent('currentSearchHeader', this);
    }

    filtroAggSelected(value: any): void {
        if (value.text == "CODICE" || value.text == "PARTENZA" || value.text == "DESTINAZIONE"
            || value.text == "CONVENZIONE" || value.text == "TIPO DI TRASPORTO" || value.text == "COGNOME TRASPORTATO") {
            this.isEditDisabled = false;
        } else {
            this.isEditDisabled = true;
        }

    }

    addFilter(): void {
        if (this.selectedFilterAdd != null) {
            if (this.isEditDisabled || (!this.isEditDisabled && this.valueFilterAdd != null)) {
                if (this.isEditDisabled && this.checkItemContain()) {
                } else {
                    this.listAddFilterItems.push(new Item(this.selectedFilterAdd.text, this.valueFilterAdd));
                    this.selectedFilterAdd = null;
                    this.valueFilterAdd = null;
                    this.isEditDisabled = true;
                }
            }
        }
    }

    checkItemContain(): boolean {
        let returnValue = false;
        this.listAddFilterItems.forEach(element => {
            if (element.name == this.selectedFilterAdd.text) {
                returnValue = true;
            }
        });

        return returnValue;
    }

    removeFilter(): void {
        if (this.selectedIndex != null) {
            this.listAddFilterItems.splice(this.selectedIndex, 1);
            this.selectedIndex = null;
        }
    }

    onChangeAll(event) {
        if (event) {
            this.filter.todayBookingFlag = false;
            this.filter.tomorrowBookingFlag = false;
            this.filter.transportDate = null;
        }
    }

    onChangeToday(event) {
        if (event) {
            this.filter.allBookingFlag = false;
            this.filter.tomorrowBookingFlag = false;
            this.filter.transportDate = null;
        }
    }

    onChangeTomorrow(event) {
        if (event) {
            this.filter.todayBookingFlag = false;
            this.filter.allBookingFlag = false;
            this.filter.transportDate = null;
        }
    }

    onChangeIntraH(event) {
        if (event) {
            this.filter.interOspFlag = false;
        }
    }

    onChangeInterH(event) {
        if (event) {
            this.filter.intraOspFlag = false;
        }
    }

    fetchHeaderData(id: string) { };

    resetFilter(): void {
        
    }

    getHeaderFilter(): any {
        this.filter.patientSurnameTxtList = [];
        this.filter.timeSlotList = [];
        this.filter.codeList = [];
        this.filter.typeTxtList = [];
        this.filter.conventionTxtList = [];
        this.filter.startAddressTxtList = [];
        this.filter.endAddressTxtList = [];
        this.filter.startIntraOspFlag = false;
        this.filter.endIntraOspFlag = false;
        this.filter.startExtraOspFlag = false;
        this.filter.endExtraOspFlag = false;
        this.filter.cyclicalBookingFlag = false;
        if (this.listAddFilterItems.length > 0) {
            //console.log('Lista Filtri aggiuntivi: ' + this.listAddFilterItems);
            this.listAddFilterItems.forEach(element => {
                if (element.name == 'CODICE') {
                    if (element.value != undefined) {
                        this.filter.codeList.push(element.value);
                    }
                }
                if (element.name == 'COGNOME TRASPORTATO') {
                    if (element.value != undefined) {
                        this.filter.patientSurnameTxtList.push(element.value);
                    }
                }
                if (element.name == 'MATTINA') {
                    this.filter.timeSlotList.push(OverviewBookingFilterDTO.TimeSlotListEnum.AM);
                }
                if (element.name == 'POMERIGGIO') {
                    this.filter.timeSlotList.push(OverviewBookingFilterDTO.TimeSlotListEnum.PM);
                }
                if (element.name == 'ORARIO INDEFINITO') {
                    this.filter.timeSlotList.push(OverviewBookingFilterDTO.TimeSlotListEnum.ND);
                }
                if (element.name == 'PART.INTRA-H') {
                    this.filter.startIntraOspFlag = true;
                }
                if (element.name == 'PART.NON-INTRA-H') {
                    this.filter.startExtraOspFlag = true;
                }
                if (element.name == 'DEST.INTRA-H') {
                    this.filter.endIntraOspFlag = true;
                }
                if (element.name == 'DEST.NON-INTRA-H') {
                    this.filter.endExtraOspFlag = true;
                }
                if (element.name == 'PRENOTAZIONI CICLICHE') {
                    this.filter.cyclicalBookingFlag = true;
                }
                if (element.name == 'TIPO DI TRASPORTO') {
                    if (element.value != undefined) {
                        this.filter.typeTxtList.push(element.value);
                    }
                }
                if (element.name == 'CONVENZIONE') {
                    if (element.value != undefined) {
                        this.filter.conventionTxtList.push(element.value);
                    }
                }
                if (element.name == 'PARTENZA') {
                    if (element.value != undefined) {
                        this.filter.startAddressTxtList.push(element.value);
                    }
                }
                if (element.name == 'DESTINAZIONE') {
                    if (element.value != undefined) {
                        this.filter.endAddressTxtList.push(element.value);
                    }
                }
            });
        }

        return this.filter
    };

    triggerSubmit(): void { };

}
