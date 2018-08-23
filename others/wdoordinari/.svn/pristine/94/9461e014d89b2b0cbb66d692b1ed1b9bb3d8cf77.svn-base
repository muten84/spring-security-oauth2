import { Directive, OnInit , OnChanges, Input, Optional, Host,SkipSelf} from '@angular/core';
import {ControlContainer, AbstractControl,FormGroup} from '@angular/forms';

@Directive({
  selector: '[formControlName][dynamicDisable]'
})
export class DynamicDisable implements OnInit  {
  constructor(
    @Optional() @Host() @SkipSelf() private parent: ControlContainer,
  ) { 

  }

  @Input() formControlName: string;  
  @Input() dynamicDisable: boolean;

  private ctrl: AbstractControl;

  ngOnInit() { 
    if(this.parent && this.parent["form"]) {
      this.ctrl = (<FormGroup>this.parent["form"]).get(this.formControlName);
    }
    if (!this.ctrl) return;

    if (this.dynamicDisable) {
      this.ctrl.disable();
    }
    else {
      this.ctrl.enable();
    }
  }

 /* ngOnChanges() {
    if (!this.ctrl) return;

    if (this.dynamicDisable) {
      this.ctrl.disable();
    }
    else {
      this.ctrl.enable();
    }
  }*/
}