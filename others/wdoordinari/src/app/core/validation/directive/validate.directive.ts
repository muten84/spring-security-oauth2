import { Directive, forwardRef, Inject, Input, Optional, Output, Provider, Self } from '@angular/core';
import { AsyncValidator, AsyncValidatorFn, ControlContainer, ControlValueAccessor, FormControlDirective, FormGroup, NgControl, NG_ASYNC_VALIDATORS, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validator, ValidatorFn } from '@angular/forms';
import { FormGroupGeneratorService } from '../service/form-group-generator.service';

export const controlNameBinding: Provider = {
    provide: NgControl,
    multi: true,
    useExisting: forwardRef(() => ValidateDirective

    )
};

@Directive({
    selector: '[validate]',
    providers: [controlNameBinding],
    exportAs: 'ngForm'
})
export class ValidateDirective extends FormControlDirective {

    @Input("validate") formName: string;

    @Input('ngModel')
    set model(model: any) {
        this.model = model;
    }
    @Output('ngModelChange') update = this.update;

    @Input('disabled')
    set isDisabled(isDisabled: boolean) {
        this.isDisabled = isDisabled;
    }

    constructor(
        @Optional() @Self() @Inject(NG_VALIDATORS) validators: Array<Validator | ValidatorFn>,
        @Optional() @Self() @Inject(NG_ASYNC_VALIDATORS) asyncValidators: Array<AsyncValidator | AsyncValidatorFn>,
        @Optional() @Self() @Inject(NG_VALUE_ACCESSOR) valueAccessors: ControlValueAccessor[],
        private fgs: FormGroupGeneratorService,
        private controlContainer: ControlContainer
    ) {
        super(validators, asyncValidators, valueAccessors, "");
        let c = <FormGroup>this.controlContainer.control;
        this.form = this.fgs.getControl(this.formName, c);
    }

}
