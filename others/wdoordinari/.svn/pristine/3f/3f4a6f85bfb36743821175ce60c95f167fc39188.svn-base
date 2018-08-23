import { AfterViewInit, Directive, DoCheck, ElementRef } from "@angular/core";

@Directive({
    selector: '[validationGroup]'
})
export class ValidationGroupDirective implements AfterViewInit, DoCheck {

    headers: HTMLElement[] = [];

    bodies: HTMLElement[] = [];

    constructor(
        private el: ElementRef) {
    }

    ngAfterViewInit() {

    }

    ngDoCheck() {
        // spostato dall'after view ininit per risolvere 
        // il problema che dava con gli accordion dentro l'ngIf
        let elements = this.el.nativeElement.querySelectorAll('div.card>div');
        //resetto la lista 
        this.headers = [];
        this.bodies = [];

        let i = 0;
        if (elements) {
            elements.forEach(el => {
                if (el.classList.contains("card-header")) {
                    this.headers[i] = el;
                } else if (el.classList.contains("card-block")) {
                    this.bodies[i] = el;
                    i++;
                }
            });
        }

        this.bodies.forEach((el, i) => {
            let list = el.querySelectorAll('.ng-invalid');
            if (list.length > 0) {
                this.headers[i].classList.add("invalid")
            } else {
                this.headers[i].classList.remove("invalid");
            }
        });
    }
}
