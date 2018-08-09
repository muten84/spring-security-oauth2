import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PatientComponent } from './patient.component';

/*const routes: Routes = [
    {
        path: 'card', component: PatientComponent,
        children: [
            //   { path: 'ospedali', loadChildren: './intervention-layout/ospedali/ospedali.module#OspedaliModule' },
        ],
    }
];*/

const routes: Routes = [
    {
        path: '',
       
        children: [
        /*    { path: 'card', component: PatientComponent },
            { path: 'cardValutazSan', component: PatientComponent },
            { path: 'cardDatiAnagrafici', component: PatientDatianagraficiComponent },
            { path: 'cardDatiClinici', component: PatientDaticliniciComponent}
        */
        { path: 'card', component: PatientComponent },
        { path: 'cardValutazSan', component: PatientComponent },
        { path: 'cardDatiAnagrafici', component: PatientComponent },
        { path: 'cardDatiClinici', component: PatientComponent}
        ]
    }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PatientRoutingModule {
}
