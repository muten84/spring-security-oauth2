import { Component, ViewEncapsulation } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { Router } from "@angular/router";
import { ServiceCommon } from "app/common-servizi/service-common";
import { MessageService } from "app/service/message.service";
import { ServiziService } from "app/service/servizi-service";
import { ComponentHolderService } from "app/service/shared-service";
import { SinotticoServiziComponent } from "app/sinottico-servizi/sinottico-servizi.component";
import { BrowserCommunicationService } from "common/services/browser-communication.service";
import { NavigateMessage, Type } from "common/services/messages";
import { MenuItemValue, TransportModuleServiceService, TransportResultDTO } from "services/gen";

@Component({
    selector: 'sinottico-servizi-ex',
    templateUrl: '../../app/sinottico-servizi/sinottico-servizi.html',
    styleUrls: ['../../app/sinottico-servizi/sinottico-servizi.scss'],
    encapsulation: ViewEncapsulation.None

})
export class SinotticoServiziExComponent extends SinotticoServiziComponent {


    constructor(
        sanitizer: DomSanitizer,

        router: Router,
        messageService: MessageService,
        componentService: ComponentHolderService,
        transportService: TransportModuleServiceService,
        serviceCommon: ServiceCommon,
        bcs: BrowserCommunicationService,
        serviziService: ServiziService
    ) {
        super(sanitizer,
            router,
            messageService,
            componentService,
            transportService,
            serviceCommon,
            bcs,
            serviziService);
    }

    triggerSubmit(filterInput: any) {
        filterInput.sinottico = true;
        super.triggerSubmit(filterInput);
    }

    buildRowMenu(menuItems: MenuItemValue[]) {
        menuItems = [, {
            name: 'Vai al servizio',
            id: 'go_to_service',
            position: 1,
            enable: true
        }];
        return super.buildRowMenu(menuItems);
    }


    public onOptionItemClicked(row: any, source: string, enabled: boolean, event): any {
        if (enabled) {
            let src = source.toLowerCase();

            let selected = <TransportResultDTO>this.currentSelected;
            let serviceId = selected.id;

            switch (src) {

                case 'go_to_service':
                    let msg: NavigateMessage = {
                        path: 'gestione-servizi/' + serviceId
                    };
                    this.bcs.postMessage(Type.navigate, msg);
                    return;
            }
        }

    }
}