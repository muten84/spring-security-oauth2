import { Component, Input, OnInit, ViewEncapsulation } from "@angular/core";
import { BannerModel } from "app/core/banner/banner-model";
import { ComponentHolderService, HeaderComponent } from "app/service/shared-service";
import { BookingModuleServiceService, ServiceDTO, CiclicalFullDTO } from "services/services.module";



@Component({
    selector: 'gestione-cicliche-header',
    templateUrl: './gestione-cicliche-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class GestioneCiclicheHeaderComponent implements HeaderComponent, OnInit {

    ciclicheBanner: BannerModel;

    constructor(
        private componentService: ComponentHolderService,
        ) {
        }


    ngOnInit(): void {
        this.fromItemToBannerModel();
        this.ciclicheBanner.subheadings = [];
    }

    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('gestioneCiclicheHeaderComponent', this);
    }

    public setModel(model: ServiceDTO) {
        this.ciclicheBanner = new BannerModel();
        this.ciclicheBanner.headerTitle = 'Modifica Ciclica';
        this.ciclicheBanner.subheadings = [];


    }

    fromItemToBannerModel(item?: CiclicalFullDTO): void {

        this.ciclicheBanner = new BannerModel();
        if (item) {
            if (item.bookingCode) {
                this.ciclicheBanner.headerTitle = '<span style="color: #b4bcc2;margin-bottom: 1px">Modifica Ciclica - Trasportato </span>' + '<span>' + item.bookingCode + '</span>';
            }       
        } else {
            this.ciclicheBanner.headerTitle = 'Nuova Ciclica';
        }
        this.ciclicheBanner.subheadings = [];
    }

    // metodi fi HeaderComponent
    fetchHeaderData(id: string) { }

    getHeaderFilter(): any { }

    resetFilter(): void { }

    triggerSubmit(): void { }

}
