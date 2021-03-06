import { OnInit, Component } from '@angular/core';
import { NgbActiveModal , NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'bookinghistory-component',
    templateUrl: './bookinghistory-component.html',
    styleUrls: ['./bookinghistory.component.scss']
})
export class BookingHistoryModalComponent implements OnInit {

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
