import {
    AfterContentChecked,
    Component,
    ContentChild,
    ContentChildren,
    Directive,
    EventEmitter,
    Input,
    Output,
    QueryList,
    TemplateRef
} from '@angular/core';


@Component({
    selector: 'ngboot-accordion-header',
    template: ` <div>
          <span class="pull-left">Dati Trasporto       
        </span>        
          <a (click)="onToggleClick()" class="pull-right">
            <i class="fa" [ngClass]="toggleClass"></i>
          </a>
        </div>
`
})
export class AccordionHeader {
    @Input() textTitle: string;
    @Input() currToggle: string;
    @Input() toggleClass: string;

    @Output() toggle = new EventEmitter<string>();

    private toggled: boolean;

    /*  get currToggle() {
          return this._currToggle;
      }*/

    onToggleClick() {
        this.toggle.emit(this.currToggle);
    }

    changeToggleClass() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            this.toggleClass = "fa-chevron-up";
        }
        else {
            this.toggleClass = "fa-chevron-down";
        }
    }
}