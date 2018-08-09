import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { MaterialModule } from '@blox/material';


@NgModule({
    imports: [
        CommonModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        MapRoutingModule,
        MaterialModule
    ],
    declarations: [
        MapComponent
    ]
})
export class MapModule {}
