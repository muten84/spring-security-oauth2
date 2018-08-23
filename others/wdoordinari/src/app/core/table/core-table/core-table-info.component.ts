import { ContentChild, TemplateRef, Input, Component } from "@angular/core";



@Component({
    selector: 'core-table-info',
    template: '<td></td>'
})
export class CoreTableInfoComponent {
    @ContentChild(TemplateRef) template: TemplateRef<any>;

}