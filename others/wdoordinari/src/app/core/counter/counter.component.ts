import { Component, OnInit, forwardRef, Input, OnChanges } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR, NG_VALIDATORS } from '@angular/forms';


@Component({
    selector: 'counter-input',
    template: `
    <div class="input-group">
        <button class="input-group-addon btn btn-primary" (click)="increase()">+</button><span style="margin-top: 20px;" class="input-group-addon">{{counterValue}}</span><button class="btn btn-primary input-group-addon" (click)="decrease()">-</button>
    </div>
  `,
    providers: [
        { provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => CounterInputComponent), multi: true },
        { provide: NG_VALIDATORS, useExisting: forwardRef(() => CounterInputComponent), multi: true }
    ]
})
export class CounterInputComponent implements ControlValueAccessor, OnChanges {

    propagateChange: any = () => { };
    validateFn: any = () => { };

    @Input('counterValue') _counterValue = 0;
    @Input() counterRangeMax;
    @Input() counterRangeMin;

    get counterValue() {
        return this._counterValue;
    }

    set counterValue(val) {
        this._counterValue = val;
        this.propagateChange(val);
    }

    ngOnChanges(inputs) {
        if (inputs.counterRangeMax || inputs.counterRangeMin) {
            //this.validateFn = createCounterRangeValidator(this.counterRangeMax, this.counterRangeMin);
            this.propagateChange(this.counterValue);
        }
    }

    writeValue(value) {
        if (value) {
            this.counterValue = value;
        }
    }

    registerOnChange(fn) {
        this.propagateChange = fn;
    }

    registerOnTouched() { }

    increase() {
        this.counterValue++;
    }

    decrease() {
        this.counterValue--;
    }

    validate(c: FormControl) {
        return this.validateFn(c);
    }
}