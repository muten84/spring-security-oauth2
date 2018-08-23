import { Component, ViewEncapsulation, Input, forwardRef } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { DISABLED } from "@angular/forms/src/model";


@Component({
    selector: "spinner",
    templateUrl: "./spinner.html",
    styleUrls: ["./spinner.scss"],
    encapsulation: ViewEncapsulation.None,
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => Spinner),
            multi: true
        }
    ]
})
export class Spinner implements ControlValueAccessor {

    @Input("increment") incrementFn: (_: any) => {};
    @Input("decrement") decrementFn: (_: any) => {};

    @Input() minVal = 0;
    @Input() maxVal = 100;

    @Input() disabled = false;


    private data: any;
    // the method set in registerOnChange, it is just 
    // a placeholder for a method that takes one parameter, 
    // we use it to emit changes back to the form
    private propagateChange = (_: any) => { };


    public spinnerUp() {
        // se presente la funzione di increment
        if (this.incrementFn) {
            this.data = this.incrementFn(this.data);
        } else {
            this.data = Math.min(this.data + 1, this.maxVal);
        }

        // update the form
        this.propagateChange(this.data);
    }

    public spinnerDown() {
        // se presente la funzione di decrement
        if (this.decrementFn) {
            this.data = this.decrementFn(this.data);
        } else {
            this.data = Math.max(this.data - 1, this.minVal);
        }
        // update the form
        this.propagateChange(this.data);
    }


    writeValue(obj: any): void {
        if (obj) {
            this.data = obj;
        }
    }


    registerOnChange(fn: any): void {
        this.propagateChange = fn;
    }


    registerOnTouched(fn: any): void { }

}