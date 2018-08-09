import { Component, ViewEncapsulation, OnInit, Input } from '@angular/core';
import { FormGroupGeneratorService } from '../validation/validation.module';
import { FormGroup } from '@angular/forms';


@Component({
    selector: 'calendar',
    templateUrl: './calendar.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class CalendarComponent implements OnInit {
    @Input() calendarID;
    @Input() calendarName;
    @Input() formControlName;
    @Input() calendarPlaceholder;
    @Input() formGroup: string;

    form: FormGroup;

    public value;
    public calendarId;

    constructor(private fgs: FormGroupGeneratorService) {

    }

    ngOnInit(): void {
        //console.log("formGroup: " + this.formGroup)

/*        this.form = this.fgs.getFormGroup(this.formGroup);
*/    }

    getControl() {
        //console.log("formControlName: " + this.formControlName)
/*        return this.fgs.getControl(this.formControlName, this.form);
*/    }

}