import {
  Input, Directive, OnInit,
  ElementRef, ViewChild, HostBinding,
  forwardRef, Renderer2, Injector,
  OnDestroy, ComponentFactoryResolver,
  ViewContainerRef, NgZone, HostListener
} from '@angular/core';

import {
  NgControl, AbstractControl
} from '@angular/forms';


import { NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap/tooltip/tooltip-config';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap';

@Directive({
  selector: '[messageTooltip]',
  providers: [NgbTooltipConfig]
})
export class ValidationMessageDirective implements OnInit, OnDestroy {

  ngbTooltip: NgbTooltip;

  constructor(
    private el: ElementRef,
    _renderer: Renderer2,
    injector: Injector,
    componentFactoryResolver: ComponentFactoryResolver,
    viewContainerRef: ViewContainerRef,
    config: NgbTooltipConfig,
    ngZone: NgZone
  ) {
    config.placement = "left";
    this.ngbTooltip = new NgbTooltip(el, _renderer, injector, componentFactoryResolver, viewContainerRef, config, ngZone);
  }

  ngOnDestroy() {
    if (this.ngbTooltip) {
      this.ngbTooltip.ngOnDestroy();
    }
  }

  ngOnInit() {
    this.ngbTooltip.ngOnInit();

    //this.changeStatus();

  }

  @HostListener('mouseover', ['$event.target']) onMessage(e: HTMLElement) {
    ////console.log("mouse over for tooltip: " + e);
    //this.ngbTooltip.ngbTooltip = e.innerText;
    this.changeStatus(e.innerText);
  }

  changeStatus(s: string) {
    if (this.ngbTooltip.isOpen) {
      this.ngbTooltip.close();
    }
    ////console.log(this.el);
    //let message = "EMPTY";

    this.ngbTooltip.ngbTooltip = s;

  }
}
