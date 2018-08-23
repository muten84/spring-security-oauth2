import { Component, Input, OnInit, ViewEncapsulation, AfterViewInit, EventEmitter, Output } from "@angular/core";
import { ComponentHolderService, HeaderComponent } from "app/service/shared-service";
import { ActivatedRoute } from "@angular/router";
import { ServiceDTO, OverviewTailReturnFilterDTO } from "services/services.module";

@Component({
    selector: 'coda-ritorni-header',
    templateUrl: './coda-ritorni-header-component.html',
    encapsulation: ViewEncapsulation.None,
})
export class CodaRitorniHeaderComponent implements HeaderComponent, OnInit, AfterViewInit {

    public filter: OverviewTailReturnFilterDTO & {
        selectedFilterAdd?;
        valueFilterAdd?;
    };

    @Output() searchResult = new EventEmitter();

    public addizionaliItems: Array<any> = [
        "TUTTI",
        "CODICE",
        "PARTENZA",
        "DESTINAZIONE",
        "COGNOME PAZIENTE"
    ];

    constructor(
        private componentService: ComponentHolderService) {
    }

    ngOnInit(): void {
        this.resetFilterReturn();        
    }

    resetFilterReturn(){
        this.filter = {
            intraOspFlag : false,
            interOspFlag : false,
            fuoriBologna : false,
            allBookingFlag : true,
            codeList : [],
            startAddressTxtList : [],
            endAddressTxtList : [],
            patientSurnameTxtList : []
        };
    }

    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('currentSearchHeader', this);
  
    }


    // metodi fi HeaderComponent
    fetchHeaderData(id: string) { }

    getHeaderFilter(): any {
        let selectedFilterAdd = (<any>this.filter).selectedFilterAdd;
        let valueFilterAdd = (<any>this.filter).valueFilterAdd;

        this.filter.allBookingFlag = false;
        this.filter.codeList = [];
        this.filter.startAddressTxtList = [];
        this.filter.endAddressTxtList = [];
        this.filter.patientSurnameTxtList = [];

        if(selectedFilterAdd) {
            if(selectedFilterAdd.id === 'TUTTI') {
                this.filter.allBookingFlag = true;
            } else if(selectedFilterAdd.id === 'CODICE') {
                this.filter.codeList.push(valueFilterAdd);
            } else if(selectedFilterAdd.id === 'PARTENZA') {
                this.filter.startAddressTxtList.push(valueFilterAdd);
            } else if(selectedFilterAdd.id === 'DESTINAZIONE') {
                this.filter.endAddressTxtList.push(valueFilterAdd);
            } else if(selectedFilterAdd.id === 'COGNOME PAZIENTE') {
                this.filter.patientSurnameTxtList.push(valueFilterAdd);
            } else {
                this.filter.allBookingFlag = true;
            }
        }

        return this.filter;
    }

    onChangeIntraH(event) {
        if (event) {
            this.filter.interOspFlag = false;
            this.filter.fuoriBologna = false;
        }
    }

    onChangeInterH(event) {
        if (event) {
            this.filter.intraOspFlag = false;
            this.filter.fuoriBologna = false;
        }
    }

    onChangeFuoriBo(event) {

        if (event) {
            this.filter.interOspFlag = false;
            this.filter.intraOspFlag = false;
        }

    }

    resetFilter(): void { 
        this.resetFilterReturn();
    }

    triggerSubmit(): void { }

}