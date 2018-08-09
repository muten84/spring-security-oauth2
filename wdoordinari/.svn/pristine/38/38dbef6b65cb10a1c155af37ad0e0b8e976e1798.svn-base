import {
    ChangeDetectionStrategy, ViewChildren, OnInit,
    QueryList, Component, EventEmitter, Input, Output,
    ViewChild, ElementRef, NgZone, AfterViewInit, ChangeDetectorRef
    , ViewEncapsulation
} from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { SimpleTableRowComponent } from 'app/core/table/simple-table/table-row.component';


/*https://github.com/valor-software/ng2-table/blob/development/components/table/ng-table.component.ts*/

export interface SimpleTableOption {
    name: string;
    icon: string;
    clicked?: { (row: any): void };
    enabled?: { (row: any): boolean };
}

export interface SimpleTableColumn {
    title: string;
    name: string;
    style?: {
        [key: string]: string;
    };
    sort?: 'asc' | 'desc' | false;
    template?: string;
    optionTitle?: string;
    options?: [SimpleTableOption];
}


@Component({
    selector: 'simple-table',
    templateUrl: './table.component.html',
    styleUrls: ['./table.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
})
export class SimpleTableComponent implements OnInit, AfterViewInit {

    private _rows: Array<any>;

    public get rows(): Array<any> {
        return this._rows;
    }
    // Table values
    @Input() public set rows(r: Array<any>) {
        this._rows = r;
        if (r && r.length > 0) {
            this.changeSort(null);
        }
    }
    @Input() public config: any = {};
    @Input() public tbodyHeight: string;

    // Outputs (Events)
    @Output() public tableChanged: EventEmitter<any> = new EventEmitter();
    @Output() public cellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public dblCellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public selected: EventEmitter<any> = new EventEmitter();


    protected selectedCell: any;

    @ViewChild('body') body: ElementRef;

    @ViewChildren(SimpleTableRowComponent) rowsDirective: QueryList<SimpleTableRowComponent>;

    public constructor(
        private ref: ChangeDetectorRef,
        private zone: NgZone) {
    }

    ngOnInit() {
        this.zone.runOutsideAngular(() => {
            Observable.fromEvent(this.body.nativeElement, 'scroll')
                .debounceTime(20)
                .subscribe(res => {
                    this.updateVisibility();
                });
        });
    }

    ngAfterViewInit() {
        this.updateVisibility();
    }

    public updateVisibility() {
        this.rowsDirective.forEach(r => {
            r.setVisible();
        });
        //avvio il refresh del dom 
        this.zone.run(() => {
            this.ref.markForCheck();
        });

    }



    @Input()
    public set columns(values: Array<SimpleTableColumn>) {
        values.forEach((value: SimpleTableColumn) => {
            let column = this._columns.find((col: any) => col.name === value.name);
            if (column) {
                Object.assign(column, value);
            }
            if (!column) {
                this._columns.push(value);
            }
        });
    }

    public getTitle(title?: string): string {
        return title || " ";
    }

    public get columns(): Array<SimpleTableColumn> {
        return this._columns;
    }

    public get configColumns(): any {
        let sortColumns: Array<any> = [];

        this.columns.forEach((column: any) => {
            if (column.sort) {
                sortColumns.push(column);
            }
        });

        return { columns: sortColumns };
    }

    private _columns: Array<SimpleTableColumn> = [];

    public onChangeTable(column: any): void {
        this._columns.forEach((col: SimpleTableColumn) => {
            if (col.name !== column.name) {
                col.sort = null;
            }
        });

        this.changeSort(column);
        this.tableChanged.emit({ sorting: this.configColumns });
    }

    public cellClick(event): void {
        this.selectedCell = event.row;
        this.cellClicked.emit({ row: event.row, column: event.column });
        this.selected.emit({ row: event.row, column: event.column });
        /*$('.dropdown-toggle').dropdown();*/
    }

    public dblCellClick(event): void {
        this.selectedCell = event.row;
        this.dblCellClicked.emit({ row: event.row, column: event.column });
    }

    public getActive(row: any): string {
        return this.selectedCell === row ? 'table-active' : '';
    }

    public changeSort(column: any): any {
        let columnName: string;
        let sort: string | boolean;

        this.columns.forEach(c => {
            if (c.sort) {
                columnName = c.name;
                sort = c.sort;
            }
        });

        if (!columnName) {
            return;
        }

        // simple sorting
        this._rows = this.rows.sort((previous: any, current: any) => {
            if (previous[columnName] > current[columnName]) {
                return sort === 'desc' ? -1 : 1;
            } else if (previous[columnName] < current[columnName]) {
                return sort === 'asc' ? -1 : 1;
            }
            return 0;
        });
        setTimeout(() => {
            this.updateVisibility();
        }, 100);

    }

}
