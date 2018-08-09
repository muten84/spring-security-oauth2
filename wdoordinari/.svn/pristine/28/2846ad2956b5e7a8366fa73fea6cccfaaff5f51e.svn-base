import { Component, Input, OnInit, ViewEncapsulation } from "@angular/core";
import { BannerModel } from "app/core/banner/banner-model";
import { ComponentHolderService, HeaderComponent } from "app/service/shared-service";
import { ServiceDTO } from "services/services.module";


@Component({
    selector: 'gestione-equipaggio-mezzo-header',
    templateUrl: './gestione-equipaggio-mezzo-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class GestioneEquipaggioMezzoHeaderComponent implements HeaderComponent, OnInit {

    equipaggioMezzoBanner: BannerModel;

    constructor(
        private componentService: ComponentHolderService,
        ) {
        }


    ngOnInit(): void {
        this.equipaggioMezzoBanner = new BannerModel();
        this.equipaggioMezzoBanner.headerTitle = 'Equipaggio Mezzo';
        this.equipaggioMezzoBanner.subheadings = [];
    }

    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('gestioneEquipaggioTurniHeaderComponent', this);
    }

    public setModel(model: ServiceDTO) {
        this.equipaggioMezzoBanner = new BannerModel();
        this.equipaggioMezzoBanner.headerTitle = 'Equipaggio Mezzo';
        this.equipaggioMezzoBanner.subheadings = [];


    }

    // metodi fi HeaderComponent
    fetchHeaderData(id: string) { }

    getHeaderFilter(): any { }

    resetFilter(): void { }

    triggerSubmit(): void { }

}
