import { Component, OnInit } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { CoreTableColumn } from "../../core/table/core-table/core-table.component";
import { AcknowledgeResp, StormoModuleServiceService,TransportModuleServiceService, AvailableMobileStationDTO, RequestAddCrewMembersToService } from "services/gen";
import { Observable, BehaviorSubject } from "rxjs";
import { ComponentHolderService } from "../../service/shared-service";

function execute(context: any, funct: { (obj: any): any }): { (obj: any): any } {
    return function (obj: any) {
        return funct.call(context, obj);
    }
}

@Component({
    selector: 'mobile-units-modal',
    templateUrl: './mobile-units-modal-component.html'
})
export class MobileUnitsModalComponent implements OnInit {

    modified = false;

    public title = 'Unità mobili';

    public serviceId: string;

    taskText: string;


    // Conf per la tabella

    columns: Array<CoreTableColumn> = [
        { title: 'Mezzo', name: 'vehicleCode', sort: 'asc', style: { "flex-grow": "20", } },
        { title: 'Persona', name: 'associatedName', style: { "flex-grow": "30", } }
        /*,
        { title: '', name: '', index: 10, optionTitle: 'code', options: [
            {
                name: 'Elimina',
                icon: 'mdi mdi-action-delete',
                clicked: execute(this, this.deleteMS)
            },           
            {
                name: 'Test',
                icon: 'mdi mdi-action-assignment-turned-in', 
                clicked: execute(this, this.testMS)
            }, 
            {
                name: 'Reset',
                icon: 'mdi mdi-table-column-remove',
                clicked: execute(this, this.resetMS)
            }
        
        ], style: { "flex-grow": "3" , "z-index":"100"}
        }*/
    ];
    config = {
        paging: true,
        sorting: { columns: this.columns },
        filtering: { filterString: '' },
    };

    // lista di mobile station disponibili
    mobileStationsList: Array<AvailableMobileStationDTO> = [];

    //mb selezionato dalla griglia
    mobileStationListSelected: AvailableMobileStationDTO;

    constructor(
        protected activeModal: NgbActiveModal,
        protected transportService: TransportModuleServiceService,
        protected comp: ComponentHolderService,
        private stormoService: StormoModuleServiceService
    ) {
    }

    ngOnInit(): void {
        this.comp.getRootComponent().mask();
        this.transportService.selectAllAvailableMS().subscribe(res => {
            if(res.resultList!=null)
            this.mobileStationsList = res.resultList;
            this.comp.getRootComponent().unmask();
        });
    }

    close() {
        this.comp.getRootComponent().mask();
        this.comp.getRootComponent().unmask();       
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    // evento di selezione della griglia
    selected(selected: AvailableMobileStationDTO) {
        this.mobileStationListSelected = selected;
        this.modified = true;

    }

    delete(){
        if(this.mobileStationListSelected){
            this.comp.getRootComponent().showConfirmDialog('Attenzione', "Si desidera confermare l'operazione?").then((result) => {
                this.comp.getRootComponent().mask();
                //rimuovo da tabella l'occorrenza
                this.transportService.removeMobileStation(this.mobileStationListSelected.sessionId).catch((e, o) => {
                            this.comp.getRootComponent().showToastMessage('error', 'Errore rimozione mobile Station!');
                            this.comp.getRootComponent().unmask();
                            return [];
                        }).subscribe(res2 => {
                        this.comp.getRootComponent().showConfirmDialog('Attenzione', "Si desidera notificare alla unità mobile l'operazione?").then((result) => {
                            let req={
                                resourceId: this.mobileStationListSelected.vehicleId,
                                resourceCode: this.mobileStationListSelected.vehicleCode,
                                serviceId: this.serviceId                
                            };                            
                            this.stormoService.cleanActivation(req).catch((e, o) => {
                                this.comp.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
                                this.comp.getRootComponent().unmask();
                                return [];
                            }).subscribe(res2 => {
                                if (AcknowledgeResp.OutEnum.ACK!=res2.out) {
                                    console.warn("Stormo cleanActivation failed for vehicle "+req.resourceCode+" serviceId " + req.serviceId);
                                    this.comp.getRootComponent().showToastMessage('error',"Non e' stato possibile inviare il servizio \nalle unita' mobili in turno sul mezzo " + this.mobileStationListSelected.vehicleCode + ".");
                                }else{
                                    console.log("OK Stormo cleanActivation for vehicle "+req.resourceCode+" serviceId " + req.serviceId);
                                }
                                this.comp.getRootComponent().unmask();                
                            });
                        }, (reason) => {
                            console.log("notifica non confermata.");
                            this.comp.getRootComponent().unmask();
                            return;
                        });

                });
            }, (reason) => {
                console.log("notifica non confermata.");
                this.comp.getRootComponent().unmask();
                return;
            });
        }
    }

    test(){
        if(this.mobileStationListSelected){
            this.stormoService.testMobile(this.mobileStationListSelected.sessionId).catch((e, o) => {
                this.comp.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
                return [];
            }).subscribe(res2 => {
                if (AcknowledgeResp.OutEnum.ACK!=res2.out) {
                    console.warn("Stormo test mobile failed for sessionID " + this.mobileStationListSelected.sessionId);
                    this.comp.getRootComponent().showToastMessage('error',"Non e' stato possibile testare il servizio \nalle unita' mobili in turno sul mezzo " + this.mobileStationListSelected.vehicleCode + ".");
                }else{
                    console.log("OK Stormo test mobile for sessionID " + this.mobileStationListSelected.sessionId);
                }              
            });
        }
    }

    reset(){
        if(this.mobileStationListSelected){
            this.stormoService.resetMobile(this.mobileStationListSelected.sessionId).catch((e, o) => {
                this.comp.getRootComponent().showToastMessage('error', 'Errore servizio stormo');
                return [];
            }).subscribe(res2 => {
                if (AcknowledgeResp.OutEnum.ACK!=res2.out) {
                    console.warn("Stormo reset mobile failed for sessionID " + this.mobileStationListSelected.sessionId);
                    this.comp.getRootComponent().showToastMessage('error',"Non e' stato possibile effettuare il reset delle unita' mobili in turno sul mezzo " + this.mobileStationListSelected.vehicleCode + ".");
                }else{
                    console.log("OK Stormo reset mobile for sessionID " + this.mobileStationListSelected.sessionId);
                }                
            });
        }
    }


}
