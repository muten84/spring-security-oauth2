import { ChangeDetectionStrategy, ViewChildren, QueryList, Component, EventEmitter, Input, Output } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import * as jQuery from 'jquery';

@Component({
    selector: 'complex-table',
    templateUrl: './table.component.html',
    styleUrls: ['./table.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ComplexTableComponent {

    // Table values
    @Input() public rows: Array<any> = [];

    @Input() public tbodyHeight: string;

    @Input()
    public set config(conf: any) {
        if (!conf.className) {
            conf.className = 'table-striped table-bordered';
        }
        if (conf.className instanceof Array) {
            conf.className = conf.className.join(' ');
        }
        this._config = conf;
    }

    // Outputs (Events)
    @Output() public tableChanged: EventEmitter<any> = new EventEmitter();
    @Output() public cellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public optionItemClicked: EventEmitter<any> = new EventEmitter();
    @Output() public rowCollapseEnd: EventEmitter<any> = new EventEmitter();
    @Output() public showOptionsMenu: EventEmitter<any> = new EventEmitter();
    @Output() public selected: EventEmitter<any> = new EventEmitter();

    @ViewChildren('allRows') things: QueryList<any>;
    @ViewChildren('thisTable') thisTable;

    public showFilterRow: Boolean = false;
    public showOptions: Boolean = false;

    private _columns: Array<any> = [];
    private _config: any = {};

    ngOnInit() {
        //console.log("table ng on init");
    }

    ngAfterViewInit() {
        //console.log("table ngAfterViewInit");
        this.things.changes.subscribe(t => {
            this.ngForRendred();
        })
    }

    ngForRendred() {
        let that = this;
        //this.thisTable.forEach(div => console.log(div));

        this.rows.forEach((r) => {
            let index = this.rows.indexOf(r);

            if (r.rowTableStyle) {
                //console.log("row style is: " + r.rowTableStyle);
                let a: any = r;
                a.fontColor = r.rowTableStyle.color; //red
                a.backgroundStyle = r.rowTableStyle.backgroundColor //antiquewhite
            }

            /*//console.log("aggancio riga rowdetail "+index);*/
            $('#rowdetail-' + index).on('show.bs.collapse', function () {
                /*//console.log("colorare riga "+index);*/
                /*document.getElementById('rowsimple-'+index).*/
                $('#rowsimple-' + index).addClass('td-collapse-in');
                that.rowCollapseEnd.emit('rowdetail-' + index);
            });
            $('#rowdetail-' + index).on('hide.bs.collapse', function () {
                /*//console.log("colorare riga "+index);*/
                /*document.getElementById('rowsimple-'+index).*/
                $('#rowsimple-' + index).removeClass('td-collapse-in');
                that.rowCollapseEnd.emit('rowdetail-' + index);
            });
        });

        /*    this.rows.forEach((r) => {
                let index = this.rows.indexOf(r)
                if (r.rowTableStyle) {
                    //console.log("row style is: " + r.rowTableStyle);
                    let a: any = r;
                    a.fontColor = r.rowTableStyle.color; //red
                    a.backgroundStyle = r.rowTableStyle.backgroundColor //antiquewhite
                }
            })*/

    }

    public showRowMenu(isOpen: any, row: any) {
        //console.log('showRowMenu: ' + isOpen + ' - row:' + row);
        if (isOpen) {
            this.showOptionsMenu.emit(row);
        }
    }

    @Input()
    public set columns(values: Array<any>) {
        values.forEach((value: any) => {
            if (value.filtering) {
                this.showFilterRow = true;
            }
            if (value.options) {
                this.showOptions = true;
            }
            if (value.className && value.className instanceof Array) {
                value.className = value.className.join(' ');
            }
            let column = this._columns.find((col: any) => col.name === value.name);
            if (column) {
                Object.assign(column, value);
            }
            if (!column) {
                this._columns.push(value);
            }
        });
    }


    public constructor(private sanitizer: DomSanitizer) {

    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    public get columns(): Array<any> {
        return this._columns;
    }

    public get config(): any {
        return this._config;
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

    public onChangeTable(column: any): void {
        //console.log('onChangeTable');
        this._columns.forEach((col: any) => {
            if (col.name !== column.name && col.sort !== false) {
                col.sort = '';
            }
        });


        this.tableChanged.emit({ sorting: this.configColumns });
    }

    public getData(row: any, propertyName: string): string {
        if (!row[propertyName.split('.')[0]]) {
            return "";
        }
        return propertyName.split('.').reduce((prev: any, curr: string) => prev[curr], row);
    }

    public getRawCellValue(row: any, propertyName: string): string {
        return propertyName.split('.').reduce((prev: any, curr: string) => prev[curr], row);
    }

    public getRowDetail(row: any): string {
        return row.detail;
    }

    public cellClick(row: any, column: any): void {
        this.cellClicked.emit({ row, column });

        let obj = this.rows[row];
        this.selected.emit(obj);
        /*$('.dropdown-toggle').dropdown();*/
    }

    public optionItemClick(row: any, column: any, source: any, enabled: boolean, event): void {
        if (enabled) {
            this.optionItemClicked.emit({ row, column, source });
        }
        else {
            event.stopPropagation();
        }
    }
}
