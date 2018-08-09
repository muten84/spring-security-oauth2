import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoaderRoutingModule } from './loader-routing.module';
import { LoaderComponent } from './loader.component';
import { MaterialModule } from '@blox/material';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
    imports: [MaterialModule, CommonModule, LoaderRoutingModule],
    declarations: [LoaderComponent]
})
export class LoaderModule { }
