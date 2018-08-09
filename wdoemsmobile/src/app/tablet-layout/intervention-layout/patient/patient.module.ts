import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonComponentsModule } from '../../common/common-components.module';
import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { PatientRoutingModule } from './patient-routing.module';
import { FormsModule} from '@angular/forms';
import { MaterialModule } from '@blox/material';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

import { NgVirtualKeyboardModule } from '../../components/keyboard/virtual-keyboard.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ComponentsModule, ListItemComponent } from '../../components/components.module';

import { MatOptionModule } from '@angular/material/';

import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatDividerModule } from '@angular/material/divider';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { InterventionModule } from '../intervention.module';
import { PatientComponent } from './patient.component';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MAT_MOMENT_DATE_FORMATS, MomentDateAdapter } from '@angular/material-moment-adapter';

@NgModule({
    imports: [
        CommonModule,
        InterventionModule,
        CommonComponentsModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        PatientRoutingModule,
        MaterialModule,
        MatCardModule,
        ComponentsModule,
        FormsModule,
        MaterialModule,
        MatInputModule,
        MatDatepickerModule,
        MatGridListModule,
        MatFormFieldModule,
        NgVirtualKeyboardModule,
        FlexLayoutModule,
        MatSelectModule,
        MatCheckboxModule,
        MatRadioModule,
        MatNativeDateModule,
        MatDividerModule,
        MatTabsModule,
        MatIconModule,
        CommonModule, 
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),

    ],
    declarations: [

    ],
    providers: [
        { provide: MAT_DATE_LOCALE, useValue: 'it-IT' },
        { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },
        { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS },
    ]
})
export class PatientModule { }
