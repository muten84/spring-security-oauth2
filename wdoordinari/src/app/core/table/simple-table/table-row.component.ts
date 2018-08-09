import {
    ChangeDetectionStrategy, Component, Input, Output, EventEmitter,
    HostListener, ElementRef, OnInit, OnDestroy, AfterViewInit
} from "@angular/core";
import { SafeHtml, DomSanitizer } from "@angular/platform-browser";
import { SimpleTableOption, SimpleTableColumn } from "app/core/table/simple-table/table.component";

@Component({
    selector: '[simpleTableRow]',
    templateUrl: './table-row.component.html'
})
export class SimpleTableRowComponent implements OnInit, OnDestroy {

    @Input('row') row;
    @Input('columns') columns;
    @Input('rowClass') rowClass;

    @Output() public cellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public dblCellClicked: EventEmitter<any> = new EventEmitter();

    constructor(
        private sanitizer: DomSanitizer,
        private elem: ElementRef
    ) {
    }

    ngOnInit() {
        this.setVisible();
    }

    ngOnDestroy() {
    }

    public cellClick(row: any, column: any): void {
        this.cellClicked.emit({ row, column });
    }

    public dblCellClick(row: any, column: any): void {
        this.dblCellClicked.emit({ row, column });
    }

    public setVisible(): boolean {
        this.row.visible = this.isVisible();
        return this.row.visible;
    }

    // restituisce true se la riga è visualizzata 
    public isVisible(): boolean {
        let rectEl = this.elem.nativeElement.getBoundingClientRect();
        let rectParent = this.elem.nativeElement.parentElement.getBoundingClientRect();

        // Only completely visible elements return true:
        var isVisible = (rectEl.bottom >= rectParent.top) && (rectEl.top <= rectParent.bottom);
        // Partially visible elements return true:
        //isVisible = elemTop < window.innerHeight && elemBottom >= 0;
        return isVisible;
    }

    public showButton(row, column): boolean {
        return column.options && row.visible;
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    public enabled(row: any, o: SimpleTableOption): boolean {
        if (o.name != 'SEPARATOR' && o.enabled) {
            return o.enabled(row);
        } else {
            return true;
        }
    }

    public optionItemClick(row: any, column: SimpleTableColumn, o: SimpleTableOption, $event: any) {
        o.clicked(row);
    }

    public getData(row: any, column: SimpleTableColumn): string {
        let value;
        if (column.template) {
            value = column.template;
        } else {
            value = this.getPropertyValue(row, column.name);
        }
        return value || '';
    }

    public getPropertyValue(row: any, propertyName: string): string {
        return propertyName.split('.').reduce((prev: any, curr: string) => prev ? prev[curr] : null, row);
    }

}