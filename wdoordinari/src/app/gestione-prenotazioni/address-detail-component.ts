import { Component, Input } from '@angular/core';
import { AddressBookingDTO } from 'services/services.module';

@Component( {
    selector: 'address-detail',
    templateUrl: './address-detail-component.html'
} )
export class AddressDetailComponent {

    @Input() address: AddressBookingDTO;



}
