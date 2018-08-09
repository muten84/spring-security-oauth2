import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

import { OspedaliRoutingModule } from './ospedali-routing.module';
//import { OspedaliComponent } from './ospedali.component';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '@blox/material';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
//import { MatIconModule } from '@angular/material/icon';
import { NgVirtualKeyboardModule } from '../../components/keyboard/virtual-keyboard.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ComponentsModule, ListItemComponent } from '../../components/components.module';
import { InterventionModule } from '../intervention.module';


@NgModule({
    imports: [
        CommonModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        OspedaliRoutingModule,
        MaterialModule,
        ComponentsModule,
        FormsModule,
        MaterialModule,
        MatInputModule,
        MatGridListModule,
        NgVirtualKeyboardModule,
        FlexLayoutModule,
        InterventionModule
    ],
    declarations: [

    ]
})
export class OspedaliModule { }
