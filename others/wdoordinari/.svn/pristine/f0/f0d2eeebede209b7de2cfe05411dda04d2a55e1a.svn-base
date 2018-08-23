import {
    ViewChild, ElementRef, Directive,
    Component, OnInit, ViewEncapsulation,
    ViewContainerRef, EventEmitter, Input, Output,
    AfterViewInit
} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { BannerModel, BannerDataGroup } from '../core/core.module';
import { BookingModuleServiceService, BookingDTO, PatientBookingDTO, Value, WorkingResourceInputDTO, TransportModuleServiceService } from '../../services/services.module';
import { ComponentHolderService, HeaderComponent } from '../service/shared-service';
import { defIfEmpty, formatDate } from '../util/convert';
import { StaticDataService } from '../static-data/cached-static-data';
import * as _ from 'lodash';
import { calcolaTrasportato, decodeCompact2Sd } from 'app/util/sinottico-utils';

@Component({
    selector: 'mezzi-attivi-header',
    templateUrl: './mezzi-attivi-header.html',
    encapsulation: ViewEncapsulation.None,
})
export class MezziAttiviHeaderComponent implements OnInit, HeaderComponent, AfterViewInit {


    @Output() searchResult = new EventEmitter();

    //Oggetto filtro proviente dal front-end
    public filtroMezzi: WorkingResourceInputDTO;

    // Valori della combo "Filtro Aggiuntivo"
    public addizionaliItems: Array<any> = [
        "PART.INTRA-H",
        "PART.NON-INTRA-H",
        "DEST.INTRA-H",
        "DEST.NON-INTRA-H",
        "CODICE",
        "PARTENZA",
        "DESTINAZIONE",
        "PAZIENTE",
        "MEZZI_ATTIVI"
    ];

    // Valore selezionato nella combo "Filtro Aggiuntivo"
    private selectedFilterAdd: any;

    // Abilita/disabilita l'inserimento del Valore 
    // testuale corripondente al filtro aggiuntivo   
    private isEditDisabled: boolean = true;

    // Valore testuale corripondente al filtro aggiuntivo    
    private newItemValue: string;

    private selectedItemIndex: number;
    //Array di filtri aggiunti
    //   private listAddFilterItems: ItemFiltroServizi[] = [];

    select(index: any): void {
        this.selectedItemIndex = index;
    }

    //Funzioni per la gestione del filtro aggiuntivo
    filtroAggSelected(value: any): void {
        if (value.text == "CODICE" || value.text == "PARTENZA" || value.text == "DESTINAZIONE" || value.text == "PAZIENTE") {
            this.isEditDisabled = false;
        } else {
            this.isEditDisabled = true;
        }
    }
    /*
        addFilter(): void {
            console.log("addFilter");
            if (this.selectedFilterAdd != null) {
                if (this.isEditDisabled || (!this.isEditDisabled && this.newItemValue != null)) {
                    if (this.isEditDisabled && this.checkItemContain()) {
    
                    } else {
                        if (!this.checkItemContain()) {
                            this.listAddFilterItems.push(new ItemFiltroServizi(this.selectedFilterAdd.text, this.newItemValue));
                        }
                        this.selectedFilterAdd = null;
                        this.newItemValue = null;
                        this.isEditDisabled = true;
                    }
                }
            }
        }
    
        removeFilter(): void {
            if (this.selectedItemIndex != null) {
                this.listAddFilterItems.splice(this.selectedItemIndex, 1);
                this.selectedItemIndex = null;
            }
    
        }
    
        checkItemContain(): boolean {
            let returnValue = false;
    
            this.listAddFilterItems.forEach(element => {
                if (element.name == this.selectedFilterAdd.text && element.value == this.newItemValue) {
                    returnValue = true;
                }
            });
    
            return returnValue;
        }
    */

    constructor(
        private componentService: ComponentHolderService,
        private transportService: TransportModuleServiceService
    ) {

    }

    ngOnInit(): void {
        this.initItemsList();
        this.resetFilter();
    }

    getHeaderFilter(): any {
        return this.filtroMezzi;
    }

    private initItemsList() {
        this.selectedFilterAdd = undefined;
        this.newItemValue = undefined;
        this.isEditDisabled = true;
        this.selectedItemIndex = -1;
        //     this.listAddFilterItems = [];
    }

    ngAfterViewInit(): void {
        //inietto l'header nel componente padre
        this.componentService.setHeaderComponent('currentSearchHeader', this);
        setTimeout(() => {

            let filterS = sessionStorage.getItem("resourceFilter");
            if (filterS) {
                this.filtroMezzi = JSON.parse(filterS);
                this.processFilter();
            }
        }, 0)
    }

    processFilter() {
        /* Aggiorna la lista dei filtri aggiuntivi */

    }


    onChangeMezziAttivi(event) {
        if (event) {
            this.filtroMezzi.all = true;
            this.filtroMezzi.inTurn = false;
        }else{
            this.filtroMezzi.all = false;
            this.filtroMezzi.inTurn = true;
        }
    }

    onChangeMezziTurno(event) {
        if (event) {
            this.filtroMezzi.all = false;
            this.filtroMezzi.inTurn = true;
        }else{
            this.filtroMezzi.all = true;
            this.filtroMezzi.inTurn = false;
        }
    }


    onChangeIntraH(event) {
        if (event) {
            this.filtroMezzi.interFlag = false;
            this.filtroMezzi.fuoriBOFlag = false;
        }
    }

    onChangeInterH(event) {
        if (event) {
            this.filtroMezzi.intraFlag = false;
            this.filtroMezzi.fuoriBOFlag = false;
        }
    }

    onChangeFuoriBo(event) {

        if (event) {
            this.filtroMezzi.interFlag = false;
            this.filtroMezzi.intraFlag = false;
        }

    }

    fetchHeaderData(id: string) {
    };

    resetFilter(): void {
        var displayDate = new Date();
        displayDate.setUTCHours(0, 0, 0);
        let transportDateToSet = { day: displayDate.getUTCDate(), month: displayDate.getUTCMonth() + 1, year: displayDate.getUTCFullYear() };

        this.filtroMezzi = {
            all: true,
            dispABreve: false,
            intraFlag: false,
            interFlag: false,
            fuoriBOFlag: false,
            inTurn:false
        };

    }

    /* VALORIZZA IL FILTRO DA PASSARE AL COMPOMENTE PER LA RICERCA */
    /*   getHeaderFilter(): any {
           this.filtroMezzi.listaElementiFiltro = this.listAddFilterItems;
   
           return this.filtroMezzi;
       }
   */
    triggerSubmit(): void {

    };
}