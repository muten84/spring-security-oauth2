import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutTabletComponent } from './layout.component';

const routes: Routes = [
    {
        path: '',
        component: LayoutTabletComponent,
        children: [
            { path: '', redirectTo: 'dump118' },
            { path: 'dump118', loadChildren: './common/common-components.module#CommonComponentsModule' },
            { path: 'map', loadChildren: './mappa/map.module#MapModule' },
            { path: 'checkin', loadChildren: './checkin/checkin.module#CheckInModule' },
            //    { path: 'ospedali', loadChildren: './intervention-layout/ospedali/ospedali.module#OspedaliModule' },
        ],
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule { }
