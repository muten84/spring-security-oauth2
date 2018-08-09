import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InterventionComponent } from './intervention.component';
import { InterventionActiveComponent } from './intervention-active.component';
import { MessageCardComponent } from './message-card.component';
import { OspedaliComponent } from './ospedali/ospedali.component';
import { Dump118Component } from '../common/dump118/dump118.component';
import { PatientComponent } from './patient/patient.component';

const routes: Routes = [
    {
        path: 'alertMessage',
        component: MessageCardComponent,
        /*  children: [
              { path: 'newIntervention', component: InterventionComponent }
          ] */
    },
    {
        path: '',
        component: InterventionComponent,

        children: [
            { path: 'newIntervention', component: InterventionActiveComponent },
            { path: 'updateIntervention', component: InterventionActiveComponent },
            { path: 'dump118', component: Dump118Component },
            { path: 'ospedali', loadChildren: './ospedali/ospedali.module#OspedaliModule' },
            { path: 'patient', loadChildren: './patient/patient.module#PatientModule' },
            /* { path: 'patient', component: PatientComponent }, */
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class InterventionRoutingModule { }
