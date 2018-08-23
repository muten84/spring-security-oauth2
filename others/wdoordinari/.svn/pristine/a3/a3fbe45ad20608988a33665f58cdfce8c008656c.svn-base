import { SinotticoPrenotazioniHeaderComponent } from "app/sinottico-prenotazioni/sinottico-prenotazioni-header.component";
import { ViewEncapsulation, Component } from "@angular/core";
import { ComponentHolderService } from "app/service/shared-service";
import { BookingModuleServiceService } from "services/gen";

@Component({
    selector: 'sinottico-prenotazioni-header-ex',
    templateUrl: '../../app/sinottico-prenotazioni/sinottico-prenotazioni-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class SinotticoPrenotazioniHeaderExComponent extends SinotticoPrenotazioniHeaderComponent {


    constructor(componentService: ComponentHolderService, bookingService: BookingModuleServiceService) {

        super(componentService, bookingService);
    }

    getHeaderFilter(): any {
        let filter = super.getHeaderFilter();
        filter.sinottico = true;
        return filter;
    }

    hiddenFilterReturn(){
        return false;
    }

}