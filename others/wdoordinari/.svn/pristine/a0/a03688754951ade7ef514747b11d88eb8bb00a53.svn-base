import { Directive, ElementRef, HostListener, Input, ViewChildren, ContentChildren, ContentChild } from "@angular/core";
import { NgbInputDatepicker } from "@ng-bootstrap/ng-bootstrap";

@Directive({
    selector: '[clickoff]',
})
export class ClickOffDirective {

    @ContentChild(NgbInputDatepicker) datePickerC: NgbInputDatepicker;

    constructor(private el: ElementRef) { }

    @HostListener('document:click', ['$event'])
    decideClosure(event: MouseEvent) {
        if (!this.el.nativeElement.contains(event.srcElement)) {
            if (this.datePickerC) {
                this.datePickerC.close();
            }
        }
    }

}