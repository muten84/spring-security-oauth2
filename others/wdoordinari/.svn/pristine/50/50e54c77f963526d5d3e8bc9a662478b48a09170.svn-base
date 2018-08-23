import { Component, ViewEncapsulation } from "@angular/core";
import { SinotticoServiziHeaderComponent } from "app/sinottico-servizi/sinottico-servizi-header.component";
import { ComponentHolderService } from "app/service/shared-service";
import { TransportModuleServiceService } from "services/gen";

@Component({
    selector: 'sinottico-servizi-header-ex',
    templateUrl: '../../app/sinottico-servizi/sinottico-servizi-header.component.html',
    encapsulation: ViewEncapsulation.None
})
export class SinotticoServiziHeaderExComponent extends SinotticoServiziHeaderComponent {

    constructor(
        componentService: ComponentHolderService,
        transportService: TransportModuleServiceService
    ) {
        super(componentService,
            transportService);
    }


    getHeaderFilter(): any {
        let filter = super.getHeaderFilter();
        filter.sinottico = true;
        return filter;
    }

}