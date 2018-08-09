import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OspedaliComponent } from './ospedali.component';

const routes: Routes = [
    {
        path: '', component: OspedaliComponent,
          children: [
        //   { path: 'ospedali', loadChildren: './intervention-layout/ospedali/ospedali.module#OspedaliModule' },
        ],
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OspedaliRoutingModule {
}
