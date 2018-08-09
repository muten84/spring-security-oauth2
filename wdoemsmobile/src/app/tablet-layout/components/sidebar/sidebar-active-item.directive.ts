import { EventEmitter, HostBinding, Directive, ElementRef, HostListener, Input, Output } from '@angular/core';
import { LocalBusService } from '../../../service/service.module';
import { Subscription } from 'rxjs/Subscription';

@Directive({
    selector: '[sidebar-active-item]'
})
export class SidebarActiveItemDirective {

    /* @HostBinding('class')
    elementClass: string; */

    @Input('itemOrder')
    itemOrder: number;

    @Input("arrowInitalPosition")
    arrowInitalPosition: number;

    arrowStep: number = 50;

    subscrip: Subscription;

    public constructor(
        private element: ElementRef,
        private bus: LocalBusService
    ) {
        //console.log("sibar-directive: " + element.nativeElement.id);
        this.subscrip = this.bus.addObserver("activeItem").subscribe((event) => {
            let arrow = document.getElementById("sidebararrow");
            let offset = this.arrowInitalPosition;
            arrow.style.top = offset + "px";
        })
    }

    @HostListener('focus')
    onFocus() {
        ////console.log("sidebar item focus: " + this.element.nativeElement.id);
        let arrow = document.getElementById("sidebararrow");

        let offset = 0;
        if (this.itemOrder === 1) {
            offset = this.arrowInitalPosition;
        }
        else {
            offset = ((this.arrowStep * this.itemOrder) + this.arrowInitalPosition) - this.arrowStep;
        }
        ////console.log("arrow offset is: " + offset);
        arrow.style.top = offset + "px";
        // this.bus.notifyAll("sidebar-item-active", { type: "focus", payload: { id: this.element.nativeElement.id } });

    }

    @HostListener("blur")
    onBlur() {
        //  this.bus.notifyAll("sidebar-item-active", { type: "blur", payload: { id: this.element.nativeElement.id } });
    }

    ngOnDestroy() {
        this.subscrip.unsubscribe();
    }
}
