import { Component, OnInit,Input, Output} from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import {AddressDetail} from 'services/services.module';


@Component({
    selector: 'address-booking-detail-modal',
    templateUrl: './address-booking-detail.modal.html'
})
export class AddressBookingDetailModal implements OnInit {

    public title = 'Dettaglio indirizzi';
    public startAddress: AddressDetail;
    public endAddress: AddressDetail;   

    constructor(
        public activeModal: NgbActiveModal
    ) { }

    ngOnInit(): void {
        
    }

    public close() {
        this.activeModal.dismiss('close');
    }

    public dismiss() {
        // l'utente ha annullato, chiudo la window senza fare nulla
        this.activeModal.dismiss('close');
    }


}