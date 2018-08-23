import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

import { InterventionRoutingModule } from './intervention-routing.module';
//import { InterventionComponent } from './intervention.component';
import { MessageCardComponent } from './message-card.component';
import { SidebarComponent } from '../components/sidebar/sidebar.component';
import { HeaderComponent } from '../components/header/header.component';
import { BlockUIModule } from 'ng-block-ui';
import { MaterialModule } from '@blox/material';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { ComponentsModule } from '../components/components.module';
import { MatCardModule } from '@angular/material/card';
import { InterventionActiveComponent } from './intervention-active.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { NgVirtualKeyboardModule } from '../components/keyboard/virtual-keyboard.module';
import { OspedaliComponent } from './ospedali/ospedali.component';
import { CommonComponentsModule } from '../common/common-components.module';
import { LayoutModule } from '../layout.module';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatDividerModule } from '@angular/material/divider';
import { MatTabsModule } from '@angular/material/tabs';
import { PatientComponent } from './patient/patient.component';
import { FormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
    imports: [
        MatSlideToggleModule,
        CommonModule,
        CommonComponentsModule,
        ComponentsModule,
        MaterialModule,
        MatCardModule,
        BlockUIModule,
        InterventionRoutingModule,
        TranslateModule,
        MatDialogModule,
        MatButtonModule,
        MaterialModule,
        MatIconModule,
        MatInputModule,
        MatGridListModule,
        NgVirtualKeyboardModule,
        LayoutModule,
        MatSelectModule,
        MatRadioModule,
        MatCheckboxModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatDividerModule,
        MatTabsModule,
        FormsModule,
        NgbDropdownModule.forRoot()
    ],
    declarations: [MessageCardComponent, InterventionActiveComponent, OspedaliComponent, PatientComponent]
})
export class InterventionModule { }
