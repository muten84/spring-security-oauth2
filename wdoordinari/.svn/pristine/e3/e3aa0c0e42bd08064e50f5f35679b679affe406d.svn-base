import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap/dropdown/dropdown.module';
import { TextMaskModule } from 'angular2-text-mask';
import { HourMenu } from 'app/core/hour-menu/hour-menu';
import { OffClickDirective } from "app/core/select/off-click";
import { SelectComponent } from "app/core/select/select";
import { HighlightPipe } from "app/core/select/select-pipes";
import { SelectModule } from 'app/core/select/select.module';
import { Spinner } from 'app/core/spinner/spinner';
import { CoreTableColumnComponent } from 'app/core/table/core-table/core-table-column.component';
import { CoreTableInfoComponent } from 'app/core/table/core-table/core-table-info.component';
import * as Inputmask from 'inputmask';
import { AccordionModule } from './accordion/accordion.module';
import { CommonBannerComponent } from './banner/banner.component';
import { CalendarComponent } from './calendar/calendar.component';
import { OrdinariCombo } from './combo/combo.component';
import { CounterInputComponent } from './counter/counter.component';
import { LoginComponent } from './login/index';
import { LoginModule } from './login/login.module';
import { MenuComponent } from './menu/menu.component';
import { MenuModule } from './menu/menu.module';
import { PanelModule } from './panel/panel.module';
import { DateStructPipe } from './pipe/date-struct-pipe';
/*import { TopNavModule } from './top-nav/top-nav.module';*/
import { ComplexTableComponent } from './table/complex-table/table.component';
import { CoreTableRowComponent } from './table/core-table/core-table-row.component';
import { CoreTableComponent } from './table/core-table/core-table.component';
import { NgTableFilteringDirective } from './table/directive/ng-table-filtering.directive';
import { NgTablePagingDirective } from './table/directive/ng-table-paging.directive';
import { NgTableSortingDirective } from './table/directive/ng-table-sorting.directive';
import { TableModule } from './table/ng-table-module';
import { SimpleTableRowComponent } from './table/simple-table/table-row.component';
import { SimpleTableComponent } from './table/simple-table/table.component';
/*import {BreadcrumbComponent} from './breadcrumb/breadcrumb.component'*/
import { TopNavComponent } from './top-nav/top-nav.component';
import { UiSwitchComponent } from './ui-switch/ui-switch.component';


@NgModule({
    exports: [
        NgbDropdownModule,
        TextMaskModule,
        CalendarComponent,
        CounterInputComponent,
        AccordionModule,
        LoginModule,
        TopNavComponent, LoginComponent, OrdinariCombo, UiSwitchComponent,
        CommonBannerComponent, MenuComponent,
        SimpleTableComponent, SimpleTableRowComponent,
        CoreTableComponent, CoreTableRowComponent, CoreTableColumnComponent, CoreTableInfoComponent,
        ComplexTableComponent, NgTableFilteringDirective,
        NgTablePagingDirective, NgTableSortingDirective, DateStructPipe,
        SelectComponent, HighlightPipe, OffClickDirective,
        Spinner, HourMenu],
    imports: [TextMaskModule,
        AccordionModule, LoginModule, FormsModule,
        ReactiveFormsModule, PanelModule, CommonModule,
        NgbModule.forRoot(), NgbDropdownModule.forRoot(),
        TableModule, MenuModule, SelectModule],
    declarations: [CalendarComponent, CounterInputComponent,
        TopNavComponent, OrdinariCombo, CommonBannerComponent,
        UiSwitchComponent, DateStructPipe, Spinner, HourMenu],
})
export class CoreModule {

    constructor() {
        Inputmask.extendDefinitions({
            'A': {
                validator: "[A-Za-z\'\,\u0410-\u044F\u0401\u0451\u00C0-\u00FF\u00B5\u0020]",
                casing: "upper" //auto uppercasing
            },
            '*': {
                validator: "[0-9A-Za-z\'\,\u0410-\u044F\u0401\u0451\u00C0-\u00FF\u00B5\u0020\u002D]",
                casing: "upper" //auto uppercasing
            },
            'a': {
                validator: "[A-Za-z]",
                casing: "upper" //auto uppercasing
            }
        });
    }
}

export { CommonBannerComponent } from './banner/banner.component';
export { OrdinariCombo } from './combo/combo.component';
export { BannerModel, BannerDataGroup } from './banner/banner-model';
export * from './model/menu-item';
export * from './model/topnav-item';
export { LoginComponent } from './login/login.component';
export { LoginModule } from './login/login.module';
