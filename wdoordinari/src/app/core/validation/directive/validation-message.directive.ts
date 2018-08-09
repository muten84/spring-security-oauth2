import { ComponentFactoryResolver, Directive, ElementRef, Injector, NgZone, OnDestroy, OnInit, Renderer2, ViewContainerRef } from '@angular/core';
import { NgControl } from '@angular/forms';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap';
import { NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap/tooltip/tooltip-config';
import { Config } from '../model/model';


@Directive({
  selector: '[validationMessage]'
})
export class ValidationMessageDirective implements OnInit, OnDestroy {

  ngbTooltip: NgbTooltip;

  conf: Config;

  constructor(
    private el: ElementRef,
    private control: NgControl,
    _renderer: Renderer2,
    injector: Injector,
    componentFactoryResolver: ComponentFactoryResolver,
    viewContainerRef: ViewContainerRef,
    config: NgbTooltipConfig,
    ngZone: NgZone
  ) {
    this.ngbTooltip = new NgbTooltip(el, _renderer, injector, componentFactoryResolver, viewContainerRef, config, ngZone);
  }

  ngOnDestroy() {
    if (this.ngbTooltip) {
      this.ngbTooltip.ngOnDestroy();
    }
  }

  ngOnInit() {
    this.ngbTooltip.ngOnInit();
    //cambio di stato del form control
    this.control.statusChanges.subscribe((state) => {
      this.changeStatus();
      // if (this.control.invalid) {
      //    this.ngbTooltip.open();
      //  }
    });
    if (this.control.invalid) {
      this.changeStatus();
    }
  }

  changeStatus() {
    if (this.ngbTooltip.isOpen) {
      this.ngbTooltip.close();
    }

    let message = "";
    if (this.control.errors && this.control.errors["message"]) {
      message += this.control.errors["message"];
    }
    this.ngbTooltip.ngbTooltip = message;

  }
}
