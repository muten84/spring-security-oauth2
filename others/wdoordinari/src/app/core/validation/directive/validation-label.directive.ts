import { Directive, OnInit, DoCheck, Input, ElementRef } from '@angular/core';
import { ControlContainer, FormGroup, AbstractControl } from '@angular/forms';

@Directive({
  selector: '[validationLabel]'
})
export class ValidationLabelDirective implements OnInit, DoCheck {

  @Input("validationLabel") name: string;

  control: AbstractControl;

  constructor(
    private el: ElementRef,
    private controlContainer: ControlContainer) {
  }
  //serve quando i campi sono nascosti in un accordion
  ngDoCheck() {
    if (this.control) {
      this.updateClass();
    }
  }

  ngOnInit() {
    let c: FormGroup;
    c = <FormGroup>this.controlContainer.control;
    this.control = c.controls[this.name];
    if (this.control) {
      this.control.statusChanges.subscribe((state) => {
        this.updateClass();
      });
    }
  }

  updateClass() {
    if (this.control.valid) {
      this.el.nativeElement.classList.remove("invalid");
      this.el.nativeElement.classList.add("valid");
    } else {
      this.el.nativeElement.classList.remove("valid");
      this.el.nativeElement.classList.add("invalid");
    }
  }

}
