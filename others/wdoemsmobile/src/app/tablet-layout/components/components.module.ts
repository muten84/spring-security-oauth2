import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

import { SidebarActiveItemDirective } from './sidebar/sidebar-active-item.directive';
import { SidebarComponent } from './sidebar/sidebar.component';
import { WorkflowComponent } from './sidebar/workflow/workflow.component';
import { WorkflowTurnoComponent } from './sidebar/workflow/workflow-turno.component';
import { WorkflowStatusComponent } from './sidebar/workflow/workflow-status.component';
import { HeaderComponent } from './header/header.component';
import { ListItemDialogComponent } from './dialogs/list-item-dialog/list-item-dialog.component';
import { ListItemDialogNoSearchComponent } from './dialogs/list-item-dialog-no-search/list-item-dialog-no-search.component';
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { InfoDialogComponent } from './dialogs/info-dialog/info-dialog.component';
import { MessageDialogComponent } from './dialogs/message-dialog/message-dialog.component';
import { ListItemComponent } from './list-item/list-item.component';
import { RadioButtonComponent } from './radiobutton/radio-button.component';
import { MatSelectComponent } from './matselect/mat-select.component';
import { CalendarDialogComponent } from './dialogs/calendar-dialog/calendar-dialog.component';

import { GridDialogComponent } from './dialogs/grid-dialog/grid-dialog.component';
import { ConfirmGridDialogComponent } from './dialogs/confirm-grid-dialog/confirm-grid-dialog.component';
import { BlockUIModule } from 'ng-block-ui';
import { MaterialModule } from '@blox/material';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { Routes, RouterModule } from '@angular/router';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { NgVirtualKeyboardModule } from './keyboard/virtual-keyboard.module';

import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';



@NgModule({
    imports: [
        CommonModule,
        MaterialModule,
        NgVirtualKeyboardModule,
        BlockUIModule,
        TranslateModule,
        MatDialogModule,
        MatButtonModule,
        MatGridListModule,
        MatInputModule,
        MatIconModule,
        RouterModule,
        MatMenuModule,
        MatSelectModule,
        MatRadioModule,
        MatCheckboxModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatCardModule,
        MatTableModule,
        MatSortModule,
        MatExpansionModule,
        NgbDropdownModule.forRoot(),
    ],
    exports: [SidebarActiveItemDirective, SidebarComponent, WorkflowStatusComponent, WorkflowComponent, WorkflowTurnoComponent, HeaderComponent, InfoDialogComponent, ListItemDialogComponent, ConfirmDialogComponent, ListItemComponent, RadioButtonComponent, MatSelectComponent],
    declarations: [SidebarActiveItemDirective, SidebarComponent, WorkflowStatusComponent, WorkflowComponent, WorkflowTurnoComponent, HeaderComponent, InfoDialogComponent, MessageDialogComponent, ListItemDialogComponent, ConfirmDialogComponent, ListItemComponent, GridDialogComponent, RadioButtonComponent, MatSelectComponent,
        ListItemDialogNoSearchComponent, CalendarDialogComponent, ConfirmGridDialogComponent],
    entryComponents: [GridDialogComponent, ListItemDialogComponent, ConfirmGridDialogComponent, ConfirmDialogComponent, InfoDialogComponent, MessageDialogComponent, ListItemDialogNoSearchComponent, CalendarDialogComponent]
})
export class ComponentsModule { }
export * from './sidebar/sidebar.component';
export * from './header/header.component';
export * from './list-item/list-item.component';
export * from './dialogs/list-item-dialog/list-item-dialog.component';
export * from './dialogs/confirm-dialog/confirm-dialog.component';
export * from './dialogs/info-dialog/info-dialog.component';
export * from './dialogs/message-dialog/message-dialog.component';
export * from './radiobutton/radio-button.component';
export * from './matselect/mat-select.component';
export * from './dialogs/list-item-dialog-no-search/list-item-dialog-no-search.component';
export * from './dialogs/calendar-dialog/calendar-dialog.component';
export * from './dialogs/grid-dialog/grid-dialog.component';
export * from './dialogs/confirm-grid-dialog/confirm-grid-dialog.component';

