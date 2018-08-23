import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ComponentHolderService } from "app/service/shared-service";
import { TransportModuleServiceService, Value } from "services/services.module";
import { StaticDataService } from "app/static-data/cached-static-data";
import { valueToSelect } from "app/util/convert";

@Component({
    selector: 'chiusura-mezzo-modal',
    templateUrl: './chiusura-mezzo.modal.html'
})
export class ChiusuraMezzoModal implements OnInit {

    public title = 'Motivo della cancellazione';

    public reason: Value;
    public reasonText: string;

    reasonList: Observable<Array<any>>;

    constructor(
        public activeModal: NgbActiveModal,
        private sdSvc: StaticDataService
    ) { }

    ngOnInit(): void {
        this.reasonList = this.sdSvc.getStaticDataByType('DEALLOCATE_REASON').map(valueToSelect);
    }

    public close() {
         this.activeModal.close(this.reason);
    }

    public dismiss() {
        // l'utente ha annullato, chiudo la window senza fare nulla
        this.activeModal.dismiss('close');
    }


}