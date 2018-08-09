import { FormControl } from "@angular/forms";

export class CustomFormControl extends FormControl {

    public singleValidation = false;


    /** 
     * 
     */
    public validateSingle(opts?: {
        onlySelf?: boolean;
        emitEvent?: boolean;
    }) {
        this.singleValidation = true;
        this.updateValueAndValidity(opts);
        this.singleValidation = false;
    }

}