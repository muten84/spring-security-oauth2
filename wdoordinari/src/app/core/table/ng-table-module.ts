import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgbModule, NgbDateParserFormatter, NgbDatepickerI18n } from '@ng-bootstrap/ng-bootstrap';
import { ComplexTableComponent } from './complex-table/table.component';
import { SimpleTableComponent } from './simple-table/table.component';
import { NgTableFilteringDirective } from './directive/ng-table-filtering.directive';
import { NgTablePagingDirective } from './directive/ng-table-paging.directive';
import { NgTableSortingDirective } from './directive/ng-table-sorting.directive';
import { ValidationMessageDirective } from './directive/message-tooltip.directive';
import { SimpleTableRowComponent } from 'app/core/table/simple-table/table-row.component';
import { CoreTableComponent } from 'app/core/table/core-table/core-table.component';
import { CoreTableRowComponent } from 'app/core/table/core-table/core-table-row.component';
import { CoreTableColumnComponent } from 'app/core/table/core-table/core-table-column.component';
import { CoreTableInfoComponent } from 'app/core/table/core-table/core-table-info.component';



@NgModule({
  imports: [CommonModule, NgbModule],
  declarations: [
    SimpleTableComponent, SimpleTableRowComponent,
    CoreTableComponent, CoreTableRowComponent, CoreTableColumnComponent,CoreTableInfoComponent,
    ComplexTableComponent, NgTableFilteringDirective,
    NgTablePagingDirective, NgTableSortingDirective,
    ValidationMessageDirective],
  exports: [
    SimpleTableComponent, SimpleTableRowComponent,
    CoreTableComponent, CoreTableRowComponent, CoreTableColumnComponent,CoreTableInfoComponent,
    ComplexTableComponent, NgTableFilteringDirective,
    NgTablePagingDirective, NgTableSortingDirective,
    ValidationMessageDirective]
})
export class TableModule {
}
