import {
    ChangeDetectionStrategy, Component,
    Input, Output, EventEmitter,
    HostListener, ElementRef, OnInit,
    OnDestroy, AfterViewInit, ContentChild, TemplateRef, ViewChild
} from "@angular/core";
import { SafeHtml, DomSanitizer } from "@angular/platform-browser";
import { CoreTableOption, CoreTableColumn } from "./core-table.component";
import { NgbTooltip } from "@ng-bootstrap/ng-bootstrap";


@Component({
    selector: '[coreTableRow]',
    templateUrl: './core-table-row.component.html'
})
export class CoreTableRowComponent implements OnInit, OnDestroy {

    @Input('i') i: number;
    @Input('row') row;
    @Input('columns') columns;
    @Input('rowClass') rowClass;
    @Input('showInfo') showInfo: boolean;
    @Input('showTooltip') showTooltip: boolean;
    @Input('autoExpand') autoExpand: boolean;

    @ContentChild('rowTemplate') rowTemplate: TemplateRef<any>;
    @ViewChild('dropDownbutton') dropDownbutton: ElementRef;

    @Output() public cellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public dblCellClicked: EventEmitter<any> = new EventEmitter();
    @Output() public expandRow: EventEmitter<any> = new EventEmitter();

    cellClass: string[];
    showDropdown = false;
    tooltipDisabled = false;
    menuPlacement = 'bottom-right';
    tooltipOpened: NgbTooltip;
    tooltipTimeout;

    constructor(
        private sanitizer: DomSanitizer,
        private elem: ElementRef
    ) {
    }

    ngOnInit() {
        if (!this.row.tooltips) {
            this.row.tooltips = [];
        }
    }

    ngOnDestroy() {
    }

    @HostListener('mouseenter')
    public mouseenter() {
        this.showDropdown = true
    }

    @HostListener('mouseleave')
    public mouseleave() {
        this.showDropdown = false
    }

    public showHideDetail() {
        this.row.showDetail = !this.row.showDetail;

        if (this.row.showDetail) {
            this.expandRow.emit({ row: this.row, expanded: true });
        }
    }

    public cellClick(row: any, column: CoreTableColumn): void {
        if (column && !column.options && this.autoExpand) {
            this.showHideDetail();
        }
        this.cellClicked.emit({ row, column: column.name });
    }

    public dblCellClick(row: any, column: CoreTableColumn): void {
        this.dblCellClicked.emit({ row, column: column.name });
    }

    public setVisible(parent: ElementRef): boolean {
        this.row.visible = this.isVisible(parent);
        return this.row.visible;
    }

    // restituisce true se la riga è visualizzata 
    public isVisible(parent: ElementRef): boolean {
        let rectEl = this.elem.nativeElement.getBoundingClientRect();
        let rectParent = parent.nativeElement.getBoundingClientRect();

        // Only completely visible elements return true:
        var isVisible = (rectEl.bottom >= rectParent.top) && (rectEl.top <= rectParent.bottom);
        // Partially visible elements return true:
        //isVisible = elemTop < window.innerHeight && elemBottom >= 0;
        if (!isVisible) {
            this.showDropdown = false;
            this.row.showDetail = false;
            if (this.tooltipOpened) {
                this.tooltipOpened.close();
                this.tooltipOpened = null;
            }
        }
        return isVisible;
    }

    public showButton(row, column: CoreTableColumn): boolean {
        return column.options && row.visible;
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    public enabled(row: any, o: CoreTableOption): boolean {
        if (o.name != 'SEPARATOR' && o.enabled != undefined && o.enabled != null) {
            if (typeof o.enabled === 'function') {
                return o.enabled(row);
            } else {
                return o.enabled
            }
        } else {
            return true;
        }
    }

    public hasOptions(column: CoreTableColumn): boolean {
        let options = this.getOptions(column);
        return options && options.length > 0;
    }

    public getOptions(column: CoreTableColumn): CoreTableOption[] {
        let ctMenuOptions
        if (typeof column.options === 'function') {
            ctMenuOptions = column.options(this.row);
        } else {
            ctMenuOptions = column.options;
        }

        return ctMenuOptions;
    }

    public getClass(column: CoreTableColumn): string {
        let additionalClass = '';
        let styleClass = '';
        if (typeof column.cellClass === 'function') {
            styleClass = column.cellClass(this.row);
        } else {
            styleClass = column.cellClass
        }

        if (this.hasOptions(column)) {
            additionalClass += ' option-cell';
        }
        return styleClass + ' ' + additionalClass;
    }

    public optionItemClick(row: any, column: CoreTableColumn, o: CoreTableOption, $event: any) {
        o.clicked(row);
    }

    public getTooltipData(row: any, column: CoreTableColumn): string {
        if (this.showTooltip) {
            return this.getData(row, column);
        } else {
            return '';
        }
    }

    public getData(row: any, column: CoreTableColumn): string {
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

    public getTooltip(column: CoreTableColumn, i: number) {
        let tooltip = this.row.tooltips[i];
        if (!tooltip) {
            tooltip = column.tooltip || {};

            if (tooltip.placement === undefined) {
                tooltip.placement = 'left-top';
            }
            if (tooltip.disabled === undefined) {
                tooltip.disabled = false;
            }


            this.row.tooltips[i] = tooltip;
        }
        let toRet = { ...tooltip };

        if (!toRet.text) {
            toRet.text = this.getData(this.row, column)
        } else if (typeof toRet.text === 'function') {
            toRet.text = toRet.text(this.row);
        }

        return toRet;
    }

    public openTooltip(column: CoreTableColumn, tooltip: NgbTooltip) {
        if (this.tooltipOpened) {
            this.tooltipOpened.close();
        }
        if (tooltip) {
            tooltip.open({ row: this.row });
            this.tooltipOpened = tooltip;

            this.tooltipTimeout = setTimeout(() => {
                if (tooltip) {
                    tooltip.close();
                }
            }, 3000);
        }
    }

    public closeTooltip(column: CoreTableColumn, tooltip: NgbTooltip) {
        if (tooltip) {
            tooltip.close();
        }
        if (this.tooltipOpened) {
            if (this.tooltipOpened !== tooltip) {
                this.tooltipOpened.close();
            }
            this.tooltipOpened = null;
        }
    }

    public dropdownOpened(opened: boolean) {
        if (opened) {
            let rectBody = document.body.getBoundingClientRect();
            let buttonRect = this.dropDownbutton.nativeElement.getBoundingClientRect();

            console.log(buttonRect);
            console.log();
            if ((rectBody.bottom - buttonRect.bottom) < 300) {
                this.menuPlacement = 'top-right';
            } else {
                this.menuPlacement = 'bottom-right';
            }
        }
    }

}