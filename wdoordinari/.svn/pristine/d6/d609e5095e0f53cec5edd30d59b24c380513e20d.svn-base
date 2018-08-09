import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER, ModuleWithProviders } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';  // Reactive Forms
import { HttpModule, JsonpModule, Http } from '@angular/http';

import {
    FormGroupGeneratorService,
    FormGroupGeneratorServiceFactory
} from './service/form-group-generator.service';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ValidateDirective } from './directive/validate.directive';
import { ValidationMessageDirective } from './directive/validation-message.directive';
import { ValidationLabelDirective } from './directive/validation-label.directive';
import { ValidationGroupDirective } from './directive/validation-group.directive';

export {
    FormGroupGeneratorService
}

@NgModule({
    declarations: [
        ValidationMessageDirective,
        ValidationLabelDirective,
        ValidateDirective,
        ValidationGroupDirective
    ],
    imports: [
        NgbModule.forRoot(),
        BrowserModule,
        FormsModule,
        HttpModule,
        JsonpModule,
        ReactiveFormsModule // <-- #2 add to Angular module imports
    ],
    providers: [FormGroupGeneratorService],
    exports: [
        ValidationMessageDirective,
        ValidationLabelDirective,
        ValidationGroupDirective
    ]
})
export class ValidationModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: ValidationModule,

            providers: [FormGroupGeneratorService, {
                provide: APP_INITIALIZER,
                useFactory: FormGroupGeneratorServiceFactory,
                deps: [FormGroupGeneratorService, Http],
                multi: true
            }]
        }
    }
}
