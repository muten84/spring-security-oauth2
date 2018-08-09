
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { DuplicaMultiplaPrenotazioneComponent } from 'app/duplica/duplica-multipla-prenotazioni-component';
import { GestioneServiziComponent } from 'app/gestione-servizi/gestione-servizi-component';
import { GestioneServiziHeaderComponent } from 'app/gestione-servizi/gestione-servizi-header.component';
import { SinotticoCiclicheComponent } from 'app/sinottico-cicliche/sinottico-cicliche-component';
import { SinotticoCiclicheHeaderComponent } from 'app/sinottico-cicliche/sinottico-cicliche-header.component';
import { SinotticoRootComponent } from './root/sinottico.root.component';
import { SinotticoPrenotazioniHeaderExComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-header-ex-component';
import { SinotticoPrenotazioniExComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-ex-component';
import { SinotticoServiziExComponent } from './sinottico-servizi/sinottico-servizi-ex-component';
import { SinotticoServiziHeaderExComponent } from './sinottico-servizi/sinottico-servizi-header-ex-component';
import { MezziAttiviHeaderComponent } from 'app/mezzi-attivi/mezzi-attivi-header.component';
import { MezziAttiviExComponent } from './mezzi-attivi/mezzi-attivi-ex.component';
import { StaticDataResolve } from 'app/static-data/cached-static-data-resolver';


export const routes: Routes = [
    {
        path: '',
        component: SinotticoRootComponent,
        data: {
            breadcrumb: 'Trasporti Ordinari'
        },
        resolve: {
            staticData: StaticDataResolve
        },
        children: [{
            path: 'prenotazioni',
            component: SinotticoPrenotazioniExComponent,
            data: {
                breadcrumb: 'Lista Prenotazioni',
                headerComponent: SinotticoPrenotazioniHeaderExComponent,
                headerTitle: 'Lista Prenotazioni',
                headerIcon: 'fa-search',
                showHeaderHook: true,
                isForSearch: true
            }
        }, {
            path: 'servizi',
            component: SinotticoServiziExComponent,
            data: {
                breadcrumb: 'Lista Servizi',
                headerComponent: SinotticoServiziHeaderExComponent,
                headerTitle: 'Lista Servizi',
                headerIcon: 'fa-search',
                showHeaderHook: true,
                isForSearch: true
            }
        }, { //con parametro sorgente chiamata per gestione abilitazione salvataggio
            path: 'mezzi',
            component: MezziAttiviExComponent,
            data: {
                breadcrumb: 'Lista mezzi',
                headerComponent: MezziAttiviHeaderComponent,
                headerTitle: 'Lista Mezzi',
                headerIcon: 'fa-ambulance',
                showHeaderHook: true,
                isForSearch: true,
            }
        }]
    },
    // otherwise redirect to home
    { path: '**', redirectTo: '/prenotazioni' }
];

export const routing = RouterModule.forRoot(routes, { useHash: true });
