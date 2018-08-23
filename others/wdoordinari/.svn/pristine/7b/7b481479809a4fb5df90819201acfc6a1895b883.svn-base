
import { ViewChild, ElementRef, Directive, Component, OnInit, ViewEncapsulation, ViewContainerRef, EventEmitter, Input, Output } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { OrdinariCombo } from '../core/core.module';
import { NgbModal, ModalDismissReasons, NgbDropdown } from '@ng-bootstrap/ng-bootstrap';

import {BookingModuleServiceService, OverviewBookingFilterDTO } from '../../services/services.module';

@Component({
    selector: 'ricerca-servizi-header',
    templateUrl: './ricerca-servizi-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class RicercaServiziHeaderComponent implements OnInit {

    public filter: OverviewBookingFilterDTO;

    public modelDataA; 
    public modelDataDa; 

    @Output() searchResult = new EventEmitter();

    constructor(private bookingService: BookingModuleServiceService) {

    }

    ngOnInit(): void {
        this.filter = {};
    }

    public searchServices(): void {
        //console.log('Search bookings: ' + this.filter.toString());
//        this.bookingService.searhcBookings(this.filter).subscribe(result => {
//            //console.log('Search bookings result: ' + result.length);
//            this.bookingService.setSearchResult(result);
//            this.searchResult.emit({ from: 'ricerca-servizi', data: result });
//
//
//        });

    }
}
