import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { CheckInRoutingModule } from './checkin-routing.module';
import { CheckInComponent } from './checkin.component';
import { MaterialModule } from '@blox/material';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { NgVirtualKeyboardModule } from '../components/keyboard/virtual-keyboard.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ComponentsModule } from '../components/components.module';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
    imports: [
        CommonModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        CheckInRoutingModule,
        ComponentsModule,
        FormsModule,
        MaterialModule,
        MatInputModule,
        MatGridListModule,
        NgVirtualKeyboardModule,
        FlexLayoutModule,
        MatIconModule
    ],
    declarations: [
        CheckInComponent
    ]
})
export class CheckInModule { }
