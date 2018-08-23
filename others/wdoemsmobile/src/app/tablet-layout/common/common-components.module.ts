import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { Dump118RoutingModule } from './dump118/dump118-routing.module';
import { Dump118Component } from './dump118/dump118.component';
import { MaterialModule } from '@blox/material';
import { MatTableModule } from '@angular/material/table';
import { BlockUIModule } from 'ng-block-ui';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';


@NgModule({
    imports: [
        CommonModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        Dump118RoutingModule,
        MaterialModule,
        MatTableModule,
        BlockUIModule,
        MatTooltipModule,
        MatSlideToggleModule
    ],
    declarations: [
        Dump118Component
    ]
})
export class CommonComponentsModule { }
