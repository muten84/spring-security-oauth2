import { ContentChild, TemplateRef, Input, Component } from "@angular/core";



@Component({
    selector: 'core-table-column',
    template: '<td></td>'
})
export class CoreTableColumnComponent {

    @Input() title: string;
    @Input() name: string;
    @Input() tooltip: {
        text?: string | ((any) => string),
        placement?: string,
        disabled?: boolean
    };

    @Input() style: {
        [key: string]: string;
    };
    @Input() sort: 'asc' | 'desc' | false;
    @Input() sortFn: (x: any, y: any) => number;
    @Input() index = 0;
    @Input() flex: number;
    @Input() cellClass?: string | ((any) => string);

    @ContentChild(TemplateRef) template: TemplateRef<any>;


}