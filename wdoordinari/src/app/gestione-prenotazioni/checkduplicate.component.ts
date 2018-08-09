import { OnInit, Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'checkduplicate-component',
    templateUrl: './checkduplicate-component.html'
})
export class CheckDuplicateModalComponent implements OnInit {

    public columns: Array<any>;

    public config: any = {
        paging: true,
        sorting: { columns: this.columns },
        filtering: { filterString: '' },
        /**/
        /*className: ['table-responsive', 'table-striped', 'table-bordered', 'table search tablesaw togglable-table tablesaw-columntoggle']*/
    };

    public list: Array<any>;

    public title: string;

    public selected: any;

    constructor(public activeModal: NgbActiveModal) {
    }

    ngOnInit(): void {

    }

    selectedElement(el) {
        //console.log("modal grid selected element: " + el);
        this.selected = el.row;
    }

    close() {
        this.activeModal.close(true);
    }

    dismiss() {
        this.activeModal.dismiss(false);
    }

}
