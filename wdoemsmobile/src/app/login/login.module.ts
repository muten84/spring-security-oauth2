import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginRoutingModule } from './login-routing.module';
import { LoginComponent } from './login.component';
import { MaterialModule } from '@blox/material';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { NgVirtualKeyboardModule } from '../tablet-layout/components/keyboard/virtual-keyboard.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ComponentsModule } from '../tablet-layout/components/components.module';
import { BlockUIModule } from 'ng-block-ui';

@NgModule({
    imports: [BlockUIModule, ComponentsModule, NgVirtualKeyboardModule, FlexLayoutModule, MatGridListModule, MatInputModule, MatIconModule, MaterialModule, CommonModule, LoginRoutingModule],
    declarations: [LoginComponent]
})
export class LoginModule { }
