import { Component, OnInit } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { CoreTableColumn } from "../../core/table/core-table/core-table.component";
import { CrewMemberDTO, CrewMemberFilterDTO, TurnModuleServiceService,Value} from "services/gen";
import { ComponentHolderService } from "../../service/shared-service";



@Component({
    selector: 'crew-members-modal',
    templateUrl: './crew-members-modal-component.html'
})
export class CrewMembersModalComponent implements OnInit { 

    public filter: CrewMemberFilterDTO = {};
    listCrewMembers: Array<CrewMemberDTO>;
    selectedCrewMember: CrewMemberDTO;


    constructor(
        protected activeModal: NgbActiveModal,
        protected comp: ComponentHolderService,
        protected componentService: ComponentHolderService,
        protected turniService: TurnModuleServiceService
    ) {
            
    }

    ngOnInit(): void {
        this.clean();
        if (this.filter){
            this.loadCrewMembers();
        }
        
    }

    loadCrewMembers() {  
        this.componentService.getRootComponent().mask();
        this.turniService.loadCrewMembers(this.filter).catch((e, o) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(res => {
            this.listCrewMembers = res;
            this.componentService.getRootComponent().unmask();
        });    
    }


    //griglia dei membri 
    public columnsCrewMembers: Array<CoreTableColumn> = [
        { title: 'Cognome', name: 'surname', style: { "flex-grow": "20", } },
        { title: 'Nome', name: 'name', style: { "flex-grow": "15", } },
        { title: 'Mansione', name: 'task', style: { "flex-grow": "65" } }
    ];

    public configCrewMembersTable = {
        paging: true,
        sorting: { columns: this.columnsCrewMembers }
    };

    // evento di selezione della griglia
    selected(selected: CrewMemberDTO) {
        this.selectedCrewMember = selected;
    }


    close() {
        this.activeModal.close(this.selectedCrewMember); 
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    clean() {
    }
}
