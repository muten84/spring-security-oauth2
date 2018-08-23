import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Dump118Component } from './dump118.component';

const routes: Routes = [
    {
        path: '', component: Dump118Component
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class Dump118RoutingModule {
}
