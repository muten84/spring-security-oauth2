import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutTabletComponent } from './layout.component';
/* import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component'; */
import { BlockUIModule } from 'ng-block-ui';
import { MaterialModule } from '@blox/material';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { ComponentsModule, HeaderComponent, ListItemDialogComponent, ConfirmDialogComponent, ListItemDialogNoSearchComponent, } from './components/components.module';
import { InterventionComponent } from './intervention-layout/intervention.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
//TODO provare la keyoboard module

@NgModule({
    imports: [
        CommonModule,
        MaterialModule,
        BlockUIModule,
        LayoutRoutingModule,
        TranslateModule,
        MatDialogModule,
        MatButtonModule,
        ComponentsModule,
        NgbDropdownModule.forRoot()
    ],
    declarations: [LayoutTabletComponent, InterventionComponent]
})
export class LayoutModule { InterventionComponent }
