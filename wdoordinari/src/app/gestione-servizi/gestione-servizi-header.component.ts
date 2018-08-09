import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { BannerModel } from "app/core/banner/banner-model";
import { ComponentHolderService, HeaderComponent } from "app/service/shared-service";
import { BookingModuleServiceService, ServiceDTO } from "services/services.module";



@Component({
    selector: 'gestione-servizi-header',
    templateUrl: './gestione-servizi-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class GestioneServiziHeaderComponent implements HeaderComponent, OnInit {

    serviceBanner: BannerModel;

    constructor(
        private componentService: ComponentHolderService,
        private route: ActivatedRoute,
        private bookingService: BookingModuleServiceService) {
    }


    ngOnInit(): void {
        this.serviceBanner = new BannerModel();
        this.serviceBanner.headerTitle = '';
        this.serviceBanner.subheadings = [];
    }

    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('gestioneServiziHeaderComponent', this);
    }

    public setModel(model: ServiceDTO) {
        this.serviceBanner = new BannerModel();
        this.serviceBanner.headerTitle = 'Servizio ' + model.code;
        this.serviceBanner.subheadings = [];

        this.serviceBanner.subheadings = [];
        //Prenotazioni Accorpate
        if (model.interventionList && model.interventionList.length > 0) {
            //evito di doppiare ogni volta i cartellini delle tappe
            let value = Array.from(new Set(model.interventionList.map(el => {
                return el.bookingCode;
            }))).join(" , ");

            this.serviceBanner.subheadings[0] =
                '<p style="color: #b4bcc2;margin-bottom: 1px"><i class="fa fa-arrow-circle-right" aria-hidden="true"> </i> Prenotazioni</p><p style="margin-bottom: 1px">' +
                value + "</p>";
        }

        // Mezzi assegnati
        if (model.serviceResourceList && model.serviceResourceList.length > 0) {
            this.serviceBanner.subheadings[1] =
                '<p style="color: #b4bcc2;margin-bottom: 1px"><i class="fa fa-ambulance" aria-hidden="true"> </i> Mezzi</p><p style="margin-bottom: 1px">' +
                model.serviceResourceList.map(el => {
                    return el.resourceCode
                }).join(" , "); + "</p>";
        }

    }

    // metodi fi HeaderComponent
    fetchHeaderData(id: string) { }

    getHeaderFilter(): any { }

    resetFilter(): void { }

    triggerSubmit(): void { }

}
