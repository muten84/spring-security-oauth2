import { AbstractControl, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import * as _ from 'lodash';
import * as moment from 'moment';
import { isArray } from 'util';
import { CustomFormControl } from './custom-form-control';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';


function isEmptyInputValue(value: any): boolean {
    // we don't check for string here so it also works with arrays
    if (_.isString(value)) {
        value = value.trim();
    }
    return value == null || value.length === 0;
}

export class CustomValidators {
    // questo è un finto validatore, serve solo a scatenare la validazione sugli altri FormControl
    // utile per legare più form 
    static updateForms(formsNames: string[]): ValidatorFn {
        return (c: AbstractControl): ValidationErrors | null => {
            formsNames.forEach(fName => {
                //se la validazione non è partita da un altro validatore come questo
                if (!(<CustomFormControl>c).singleValidation) {
                    if (c.parent && c.parent.controls[fName]) {
                        (<CustomFormControl>c.parent.controls[fName]).validateSingle();
                    }
                }
            });
            return null;
        }
    }

    static required(message: string): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let error = isEmptyInputValue(c.value) ? { 'required': true } : null;
            return error ? {
                'message': message,
                'error': error
            } : null;
        }
    }

    static notRequired(message: string): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let value = c.value;
            let error = false;
            if (value) {
                error = true;
            }
            return error ? {
                'message': message,
                'error': error
            } : null;
        }
    }

    static requiredTrue(message: string): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let error = Validators.requiredTrue(c);
            return error ? {
                'message': message,
                'error': error
            } : null;
        }
    }


    static minLength(lenght: number, message: string): ValidatorFn {

        let func = Validators.minLength(lenght);
        return (c: AbstractControl): ValidationErrors | null => {
            let error = func(c);
            return error ? {
                'message': message,
                'error': error
            } : null;
        }
    }

    static maxLength(lenght: number, message: string): ValidatorFn {

        let func = Validators.maxLength(lenght);
        return (c: AbstractControl): ValidationErrors | null => {
            let error = func(c);
            return error ? {
                'message': message,
                'error': error
            } : null;
        }
    }

    static pattern(pattern: string, message: string): ValidatorFn {
        let func = Validators.pattern(pattern);
        return (c: AbstractControl): ValidationErrors | null => {
            let error = func(c);
            return error ? {
                'message': message,
                'error': error
            } : null;
        }
    }

    static notEmpty(message: string): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let err = false;
            if (c.value != null && c.value instanceof Array) {
                if (c.value.length == 0) {
                    err = true;
                }
            } else {
                err = true;
            }
            if (err) {
                return {
                    'message': message,
                    'error': { empty: true }
                };
            }
        }
    }


    static dateValidator(pattern: string, message: string): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let err = false;
            if (c.value) {
                if (c.value instanceof Date) {
                    // se la data è un oggetto Date controllo che sia valido 
                    err = !moment(c.value).isValid();
                } else if (typeof c.value === 'string') {
                    //se è una stringa effettuo il parsing
                    err = !moment(c.value, pattern).isValid();
                } else if (typeof c.value === 'object') {
                    // se è un oggetto molto probabilmente è una NgbDateStruct
                    let clone: NgbDateStruct = _.cloneDeep(c.value);
                    clone.month = clone.month - 1;
                    err = !moment(clone).isValid();
                    if(!err) err=clone.year<1900
                }
            }
            if (err) {
                return {
                    'message': message,
                    'error': { empty: true }
                };
            }
        }
    }


    //valuta un altro campo e restituisce true se l'altro campo ha un valore
    static requiredFields(forms: string[], message: string): ValidatorFn {
        return (c: AbstractControl): ValidationErrors | null => {
            let valid = true;
            forms.forEach(fName => {
                if (c.parent && c.parent.controls[fName]) {
                    valid = valid && c.parent.controls[fName].value;
                }
            });
            if (!valid) {
                return {
                    'message': message,
                    'error': { empty: true }
                };
            }
        }
    }

    //pattern su campo specifico
    static patternFields(forms: string[], pattern: string, message: string): ValidatorFn {
        return (c: AbstractControl): ValidationErrors | null => {
            let valid = true;
            forms.forEach(fName => {
                if (c.parent && c.parent.controls[fName]) {
                    let value = c.parent.controls[fName].value;
                    let patternR = new RegExp(pattern);

                    valid = valid && value && patternR.test(value);
                }
            });
            if (!valid) {
                return {
                    'message': message,
                    'error': { empty: true }
                };
            }
        }
    }
    //mette in and i validatori passati in input, restituisce uno o tutti gli errori dei vari validatori
    static and(fns: ValidatorFn[]): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let errors: ValidationErrors[] = [];
            //se ci sta almeno uno che ha dato errore, lo restituisco
            let resultErr = fns.some(fn => {
                let errBool = false;
                let err = fn(c);
                if (err) {
                    if (isArray(err)) {
                        if (err.length > 0) {
                            errors = errors.concat(err);
                            errBool = true;
                        }
                    } else {
                        errors.push(err);
                        errBool = true;
                    }
                }
                //restituisco true se c'è stato un errore
                return errBool;
            });
            return (errors.length > 0) ? _mergeErrors(errors) : null;
        }
    }

    //mette in or i validatori passati in input, restituisce gli errori solo se tutti i validatori hano avuto un errore
    static or(fns: ValidatorFn[]): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let errors: ValidationErrors[] = [];
            //se tutti i validatori restituiscono un errore
            let resultErr = fns.every(fn => {
                let errBool = false;
                let err = fn(c);
                if (err) {
                    if (isArray(err)) {
                        if (err.length > 0) {
                            errors = errors.concat(err);
                            errBool = true;
                        }
                    } else {
                        errors.push(err);
                        errBool = true;
                    }
                }
                //restituisco true se c'è stato un errore
                return errBool;
            });
            //se tutti sono a true restituisco il merge dell'errore.
            return resultErr ? _mergeErrors(errors) : null;
        }
    }
}

function _mergeErrors(arrayOfErrors: ValidationErrors[]): ValidationErrors | null {
    const res: { [key: string]: any } =
        arrayOfErrors.reduce((res: ValidationErrors | null, errors: ValidationErrors | null) => {
            return errors != null ? { ...res!, ...errors } : res!;
        }, {});
    return Object.keys(res).length === 0 ? null : res;
}

