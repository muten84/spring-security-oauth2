import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
/*import { MdButtonModule, MdDialogModule, MdIconModule, MdInputModule } from '@angular/material';*/
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatMenuModule } from '@angular/material/menu';
import { FlexLayoutModule } from '@angular/flex-layout';

import { NgVirtualKeyboardDirective } from './virtual-keyboard.directive';
import { VirtualKeyboardComponent } from './virtual-keyboard.component';
import { VirtualKeyboardKeyComponent } from './virtual-keyboard-key.component';
import { VirtualKeyboardService } from './virtual-keyboard.service';

@NgModule({
  declarations: [
    NgVirtualKeyboardDirective,
    VirtualKeyboardComponent,
    VirtualKeyboardKeyComponent,
  ],
  providers: [
    VirtualKeyboardService,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    MatGridListModule
  ],
  entryComponents: [
    VirtualKeyboardComponent,
  ],
  exports: [
    NgVirtualKeyboardDirective,
  ]
})

export class NgVirtualKeyboardModule { }
