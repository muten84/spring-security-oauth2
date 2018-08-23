import {Directive, Input, Output, EventEmitter} from '@angular/core';
import {NgModel} from '@angular/forms';

@Directive({ 
  selector: '[ngModel][uppercase]',
  providers: [NgModel],
  host: {
    '(ngModelChange)' : 'onInputChange($event)'
  }
})
export class UppercaseDirective{
  constructor(private model:NgModel){}

  onInputChange(event){
    this.model.valueAccessor.writeValue(event.toUpperCase());
  }
}