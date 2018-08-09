import { Component, OnInit,Input, Output} from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'archivia-servizio-modal',
    templateUrl: './archivia-servizio.modal.html'
})
export class ArchiviaServizioConfirmModal implements OnInit {

    public title = 'Conferma Archiviazione Servizio';
    //public serviceCode = 'XXXX';

    public paidValue: boolean;
    public paidEnabled: boolean;
    public serviceCode: string;

    constructor(
        public activeModal: NgbActiveModal
    ) { }

    ngOnInit(): void {
        this.paidValue=false;
    }

    public close() {
         this.activeModal.close(this.paidValue);
    }

    public dismiss() {
        // l'utente ha annullato, chiudo la window senza fare nulla
        this.activeModal.dismiss('close');
    }

    onChangeBlood(event) {
        this.paidValue=!this.paidValue;
        //this.checkPersonOrOther();
    }


}