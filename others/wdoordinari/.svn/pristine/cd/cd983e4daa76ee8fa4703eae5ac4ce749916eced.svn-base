import { Input, Directive, ElementRef, HostListener } from "@angular/core";
import { NgControl } from "@angular/forms";
/*import { createTextMaskInputElement } from 'text-mask-core/dist/textMaskCore'*/
/*import * as Inputmask  from 'inputmask  ';
*/
import * as jQuery from "jquery";
import * as Inputmask from 'inputmask';


//https://github.com/RobinHerbots/Inputmask
@Directive({
  selector: '[inputMask]',
  /*  host: {
      '(input)': 'onInput($event.target.value)',
      '(blur)': '_onTouched()'
    },*/
})
export class InputMaskDirective {

  @Input('inputMask')
  private mask: string;

  private element: ElementRef

  private m;

  constructor(private el: ElementRef, private control: NgControl) {
    /*//console.log("constructor im directive");*/

    this.element = el;
    Inputmask.extendDefinitions({
      '+': {
        casing: "upper"
      }
    });

  }

  ngOnInit() {
    ////console.log("input mask is: " + this.mask)
    this.m = new Inputmask(this.mask);
    this.m.mask(this.element);
  }

  ngOnDestroy() {
    this.m = null;
  }


  @HostListener('input', ['$event']) onEvent($event) {
    /* let upper = this.el.nativeElement.value.toUpperCase();
     this.control.control.setValue(upper);
     //console.log("updating value: "+this.el.nativeElement.value);*/
    /* if (this.textMaskInputElement !== undefined) {
         //console.log("updating value: "+this.el.nativeElement.value);
      this.textMaskInputElement.update(this.el.nativeElement.value);      
      this.onInput(this.el.nativeElement.value);
    this.control.control.setValue(this.el.nativeElement.value);
      
    }*/
  }



}