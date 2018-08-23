import { Component, Input, OnInit, ViewEncapsulation } from "@angular/core";
import { BannerModel } from "app/core/banner/banner-model";
import { ComponentHolderService, HeaderComponent } from "app/service/shared-service";
import { ServiceDTO,TurnDTO } from "services/services.module";


@Component({
    selector: 'gestione-turno-header',
    templateUrl: './gestione-turno-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class GestioneTurnoHeaderComponent implements HeaderComponent, OnInit {

    turnoBanner: BannerModel;

    constructor(
        private componentService: ComponentHolderService,
        ) {

        }


    ngOnInit(): void {
        //this.turnoBanner = new BannerModel();
        //this.turnoBanner.headerTitle = 'Nuovo Turno';
        this.fromItemToBannerModel();
        this.turnoBanner.subheadings = [];
    }

    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('gestioneTurnoHeaderComponent', this);
    }

    /*public setModel(model: ServiceDTO) {
        this.turnoBanner = new BannerModel();
        this.turnoBanner.headerTitle = 'Gestione Turno';
        this.turnoBanner.subheadings = [];
    }*/

    // metodi fi HeaderComponent
    fetchHeaderData(id: string) { }

    getHeaderFilter(): any { }

    resetFilter(): void { }

    triggerSubmit(): void { }


    fromItemToBannerModel(item?: TurnDTO): void {

        this.turnoBanner = new BannerModel();
        if (item) {
            if (item.vehicleCode) {
                this.turnoBanner.headerTitle = '<span style="color: #b4bcc2;margin-bottom: 1px">Turno </span>' + '<span>' + item.vehicleCode + '</span>';
            }       
        } else {
            this.turnoBanner.headerTitle = 'Nuovo Turno';
        }
        this.turnoBanner.subheadings = [];
    }

}
