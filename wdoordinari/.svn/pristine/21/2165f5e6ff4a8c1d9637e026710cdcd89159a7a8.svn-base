import { AfterContentInit, AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ContentChild, ContentChildren, ElementRef, EventEmitter, Input, NgZone, OnInit, Output, QueryList, TemplateRef, ViewChild, ViewChildren, ViewEncapsulation, HostListener } from '@angular/core';
import { CoreTableColumnComponent } from 'app/core/table/core-table/core-table-column.component';
import { CoreTableInfoComponent } from 'app/core/table/core-table/core-table-info.component';
import * as _ from 'lodash';
import { Observable } from 'rxjs';
import { CoreTableRowComponent } from './core-table-row.component';


/*https://github.com/valor-software/ng2-table/blob/development/components/table/ng-table.component.ts*/

export function execute(context: any, funct: { (obj: any): any }): { (obj: any): any } {
    return function (obj: any) {
        return funct.call(context, obj);
    }
}

export interface CoreTableOption {
    name: string;
    icon: string;
    clicked?: ((any) => void);
    enabled?: boolean | ((any) => boolean);
}

export interface CoreTableColumn {
    title: string;
    name: string;
    tooltip?: {
        text?: string | ((any) => string),
        placement?: string,
        disabled?: boolean
    };
    index?: number;
    style?: {
        [key: string]: string;
    };
    flex?: number;
    sort?: 'asc' | 'desc' | false;
    sortFn?: ((x: any, y: any) => number);
    optionTitle?: string;
    template?: TemplateRef<any>;
    options?: CoreTableOption[] | ((any) => CoreTableOption[]);
    cellClass?: string | ((any) => string);
}


@Component({
    selector: 'core-table',
    templateUrl: './core-table.component.html',
    styleUrls: ['./core-table.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
})
export class CoreTableComponent implements OnInit, AfterViewInit, AfterContentInit {

    private _rows: Array<any>;

    public get rows(): Array<any> {
        return this._rows;
    }
    // Table values
    @Input() public set rows(r: Array<any>) {
        this._rows = r;
        if (r && r.length > 0) {
            this.changeSort(null, null);
        }

        setTimeout(() => {
            this.updateVisibility();
        }, 10);
    }
    @Input() public config: any = {};
    @Input() public tbodyHeight: string;
    @Input() public rowClass: string | ((any) => string);
    @Input() public rowStyle: { [key: string]: string; } | ((any) => { [key: string]: string; });
    @Input() public showTooltip: boolean;
    @Input() public autoExpand: boolean;
 


    // Outputs (Events)
    @Output() public tableChanged: EventEmitter<any> = new EventEmitter();
    @Output() public cellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public dblCellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public selected: EventEmitter<any> = new EventEmitter();


    protected selectedCell: any;
    //template del dettaglio 
    @ContentChild(CoreTableInfoComponent) infoComponent: CoreTableInfoComponent;
    //colonne definite nell'html
    @ContentChildren(CoreTableColumnComponent) columnsComponent: QueryList<CoreTableColumnComponent>;

    @ViewChild('body') body: ElementRef;
    @ViewChild('thead') thead: ElementRef;
    @ViewChild('tbody') tbody: ElementRef;


    @ViewChildren(CoreTableRowComponent) rowsDirective: QueryList<CoreTableRowComponent>;

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


    ngAfterContentInit() {
        if (this.columnsComponent) {
            this.columnsComponent.forEach(el => {
                this.columns.push({
                    title: el.title,
                    name: el.name,
                    style: el.style,
                    sort: el.sort,
                    sortFn: el.sortFn,
                    index: el.index,
                    template: el.template,
                    flex: el.flex,
                    cellClass: el.cellClass,
                    tooltip: el.tooltip
                });
            });
        }

        this.columns.sort((c1, c2) => {
            if (!c1.index) {
                c1.index = 0;
            }
            if (!c2.index) {
                c2.index = 0;
            }
            return c1.index - c2.index;
        })
    }

    ngAfterViewInit() {
        this.updateVisibility();
    }

    @HostListener('window:resize')
    public updateVisibility() {
        if (this.rowsDirective) {
            this.rowsDirective.forEach(r => {
                r.setVisible(this.body);
            });
        }

        //avvio il refresh del dom 
        this.zone.run(() => {
            this.ref.markForCheck();
        });

        if (this.body.nativeElement.offsetHeight < this.tbody.nativeElement.offsetHeight) {
            this.thead.nativeElement.style.width = 'calc( 100% - 17px)';
        } else {
            this.thead.nativeElement.style.width = '100%';
        }

    }



    @Input()
    public set columns(values: Array<CoreTableColumn>) {
        values.forEach((value: CoreTableColumn) => {
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

    public get columns(): Array<CoreTableColumn> {
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

    private _columns: Array<CoreTableColumn> = [];

    public onChangeTable(event: any, column: CoreTableColumn): void {
        this._columns.forEach((col: CoreTableColumn) => {
            if (col.name !== column.name) {
                col.sort = null;
            }
        });

        this.changeSort(event, column);
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

    public changeSort(event, column: CoreTableColumn): any {
        if (column) {
            let columnName = column.name;
            let sort = event.sort;

            if (!columnName && !column.sortFn && _.isEmpty(sort)) {
                return;
            }

            // Core sorting
            this._rows = this.rows.sort((previous: any, current: any) => {
                if (column.sortFn) {
                    return column.sortFn(previous, current) * (sort === 'desc' ? -1 : 1);
                } else {
                    if (previous[columnName] > current[columnName]) {
                        return sort === 'desc' ? -1 : 1;
                    } else if (previous[columnName] < current[columnName]) {
                        return sort === 'asc' ? -1 : 1;
                    }
                }
                return 0;
            });
        }
        setTimeout(() => {
            this.updateVisibility();
        }, 100);

    }


    public haveDetail(row): boolean {
        return row && row.detail && row.detail.length > 0;
    }


    public selectByproperty(property: string, value: any) {
        let row = this.rows.find(r => {
            return r[property] == value;
        });
        this.selected = row;
    }


    public scrollbarVisible() {
        return this.body.nativeElement.scrollHeight > this.body.nativeElement.clientHeight;
    }

    public getRowClass(row: any): string {
        let clss;
        if (this.rowClass) {
            if (typeof this.rowClass === 'function') {
                clss = this.rowClass(row);
            } else {
                clss = this.rowClass;
            }
        }
        if (!clss) {
            clss = '';
        }
        return clss;
    }


    public getRowStyle(row): { [key: string]: string; } {
        let style;
        if (this.rowStyle) {
            if (typeof this.rowStyle === 'function') {
                style = this.rowStyle(row);
            } else {
                style = this.rowStyle;
            }
        }
        if (!style) {
            style = {};
        }
        return style;
    }

    public expandRow(row) {
        row.showDetail = true;
        this.expandedRow({ row, expanded: true });
    }

    public expandedRow({ row, expanded }) {
        this._rows.forEach(r => {
            if (r !== row) {
                r.showDetail = false;
            }
        });
    }
}
