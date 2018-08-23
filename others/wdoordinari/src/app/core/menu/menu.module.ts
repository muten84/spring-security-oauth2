import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import {MenuComponent} from './menu.component';
import {MenuItem} from '../model/menu-item';

@NgModule({
  imports: [
    CommonModule,
    RouterModule  
  ],
  declarations: [MenuComponent],
  exports: [MenuComponent]
})
export class MenuModule { }
