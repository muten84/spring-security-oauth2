import {
    ViewChild, ElementRef, Directive, Component, OnInit, AfterContentChecked, AfterViewInit,
    ViewEncapsulation, ViewContainerRef, EventEmitter, Input, Output
} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { OrdinariCombo } from '../core/core.module';
import { NgbModal, ModalDismissReasons, NgbDropdown } from '@ng-bootstrap/ng-bootstrap';

import { RicercaBookingFilter, BookingSearchItem } from './ricerca-avanzata-prenotazioni-model';


/*import * as _ from "lodash";*/
/*import * as jQuery from "jquery";*/
import { PaginationModule, PaginationConfig } from 'ngx-bootstrap';
import { RicercaAvanzataPrenotazioniTableComponent } from './ricerca-avanzata-prenotazioni-table.component'
/*import isUndefined from 'lodash/isUndefined'*/
import * as _ from 'lodash'

import { BookingModuleServiceService, OverviewBookingFilterDTO, OverviewBookingDTO } from '../../services/services.module';


declare var $: any;

/*@Directive( {
    selector: '[cell-dropdown]'
} )
export class CellDropdownDirective {
    constructor( el: ElementRef ) {
        //console.log( 'CellDropdown' )
        $( el.nativeElement ).dropdown();
    }
}*/



@Component({
    selector: 'ricerca-avanzata-prenotazioni',
    templateUrl: './ricerca-avanzata-prenotazioni.component.html',
    encapsulation: ViewEncapsulation.None,
    providers: [PaginationConfig],
})
export class RicercaAvanzataPrenotazioniComponent implements OnInit, AfterContentChecked, AfterViewInit {




    public filter: OverviewBookingFilterDTO;
    private name: string;
    private searchResult: OverviewBookingDTO[];
    private data: Array<any> = this.searchResult;

    items = [];
    itemCount = 0;


    @ViewChild('myDrop') myDrop: NgbDropdown;
    @ViewChild('bookingSearch') bookingSearch: RicercaAvanzataPrenotazioniTableComponent;

    public rows: Array<any> = [];
    public columns: Array<any> = [
        { title: 'Codice', name: 'bookingCode', filtering: { filterString: '', placeholder: 'Filtra per codice' } },
        { title: 'Data', name: 'dataDaA' },
        { title: 'Tipo', name: 'type' },
        { title: 'Deamb.', name: 'deamb' },
        { title: 'Convenzione', name: 'convention' },
        { title: 'Partenza', name: 'startAddress' },
        { title: 'Destinazione', name: 'endAddress' },
        { title: 'Trasportato', name: 'transported' },
        {
            title: 'M', name: 'dropd', options: true, optionItems: [{
                name: 'Apri dettaglio',
                eventSource: 'openDetail',
            },
            {
                name: 'Modifica prenotazione',
                eventSource: 'modifybooking',
            }]
        },

        /* {
             title: 'Tipo',
             name: 'type',
             sort: false,
             filtering: { filterString: '', placeholder: 'Cerca per nome' }
         },*/
        /*{ title: 'Nome', className: ['office-header', 'text-success'], name: 'office', sort: 'asc' },*/
        /*{ title: 'Descrizione.', name: 'desc', sort: '', filtering: { filterString: '', placeholder: 'Filtra per descrizione' } },*/

    ];
    public page = 1;
    public itemsPerPage = 5;
    public maxSize = 5;
    public numPages = 1;
    public length = 0;


    public config: any = {
        paging: true,
        sorting: { columns: this.columns },
        filtering: { filterString: '' },
        /**/
        /*className: ['table-responsive', 'table-striped', 'table-bordered', 'table search tablesaw togglable-table tablesaw-columntoggle']*/
    };

    constructor(
        private bookingService: BookingModuleServiceService,
        paginationConfig: PaginationConfig,
        private router: Router
    ) {
        if (!_.isUndefined(this.data)) {
            this.length = this.data.length;
        }
        paginationConfig.main.nextText = '>';
        paginationConfig.main.previousText = '<';
        paginationConfig.main.lastText = '>>';
        paginationConfig.main.firstText = '<<';
        paginationConfig.main.pageBtnClass = '';
    }


    ngOnInit(): void {
        this.filter = {

        };
    }

    ngAfterContentChecked(): void {
        /*//console.log("ngAfterContentChecked");*/
        /* let section = <HTMLElement><any>document.getElementById('ricercaPrenotazioni');
         let innerSections = <HTMLElement[]><any>section.getElementsByClassName("to-check");
         for (let s of innerSections) {
             if (!s.classList.contains("show")) {
               
             }
             else {
               
             }
         }*/

    }

    ngAfterViewInit(): void {
        //console.log('ngAfterViewInit: RicercaPrenotazioniComponent: ' + this.bookingSearch)

    }

    public searchBookings(): void {
        //console.log('Search bookings: ' + this.filter.toString());
        //        this.bookingService.searhcBookings( this.filter ).subscribe( result => {
        //            this.searchResult = result;
        //            this.data = this.searchResult;
        //            this.length = this.data.length;
        //            this.onChangeTable( this.config );
        //
        //        } );

    }


    public changePage(page: any, data: Array<any> = this.data): Array<any> {
        let start = (page.page - 1) * page.itemsPerPage;
        let end = page.itemsPerPage > -1 ? (start + page.itemsPerPage) : data.length;
        return data.slice(start, end);
    }

    public changeSort(data: any, config: any): any {
        if (!config.sorting) {
            return data;
        }

        let columns = this.config.sorting.columns || [];
        let columnName: string = void 0;
        let sort: string = void 0;

        for (let i = 0; i < columns.length; i++) {
            if (columns[i].sort !== '' && columns[i].sort !== false) {
                columnName = columns[i].name;
                sort = columns[i].sort;
            }
        }

        if (!columnName) {
            return data;
        }

        // simple sorting
        return data.sort((previous: any, current: any) => {
            if (previous[columnName] > current[columnName]) {
                return sort === 'desc' ? -1 : 1;
            } else if (previous[columnName] < current[columnName]) {
                return sort === 'asc' ? -1 : 1;
            }
            return 0;
        });
    }

    public changeFilter(data: any, config: any): any {
        let filteredData: Array<any> = data;
        this.columns.forEach((column: any) => {
            if (column.filtering) {
                filteredData = filteredData.filter((item: any) => {
                    //console.log(column.name);
                    return item[column.name].match(column.filtering.filterString);
                });
            }
        });

        if (!config.filtering) {
            return filteredData;
        }

        if (config.filtering.columnName) {
            return filteredData.filter((item: any) =>
                item[config.filtering.columnName].match(this.config.filtering.filterString));
        }

        let tempArray: Array<any> = [];
        filteredData.forEach((item: any) => {
            let flag = false;
            this.columns.forEach((column: any) => {
                if (item[column.name].toString().match(this.config.filtering.filterString)) {
                    flag = true;
                }
            });
            if (flag) {
                tempArray.push(item);
            }
        });
        filteredData = tempArray;

        return filteredData;
    }

    public onChangeTable(config: any, page: any = { page: this.page, itemsPerPage: this.itemsPerPage }): any {
        //console.log('onChangeTable');
        if (config.filtering) {
            Object.assign(this.config.filtering, config.filtering);
        }

        if (config.sorting) {
            Object.assign(this.config.sorting, config.sorting);
        }

        let filteredData = this.changeFilter(this.data, this.config);
        let sortedData = this.changeSort(filteredData, this.config);
        this.rows = page && config.paging ? this.changePage(page, sortedData) : sortedData;
        this.length = sortedData.length;
    }

    public onCellClick(data: any): any {
        //console.log('onCellClick');
        /*this.myDrop.open();*/
        /*jQuery(el.nativeElement).dropdown();*/

    }

    public onOptionItemClicked(data: any): any {
        //console.log('onOptionItemClicked: ' + data);
        this.router.navigate(['/modifica-prenotazione', data.row.bookingCode]);
    }

    reloadItems(params) {
        //        this.bookingService.searhcBookings( this.filter ).subscribe( result => {
        //            this.searchResult = result;
        //            this.data = this.searchResult;
        //            this.length = this.data.length;
        //            this.onChangeTable( this.config );
        //            this.itemResource = new DataTableResource( this.searchResult );
        //            this.itemResource.query( params ).then( items => this.items = items );
        //        } );

    }

    // special properties:

    rowClick(rowEvent) {
        //console.log('rowClick');
    }

    rowDoubleClick(rowEvent) {
        //console.log('rowDoubleClick');
    }

    rowTooltip(item) { return item.jobTitle; }
}
