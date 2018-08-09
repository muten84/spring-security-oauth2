//import { GestionePrenotazioniComponent } from './gestione-prenotazioni/gestione-prenotazioni.component';
import { ModificaPrenotazioneComponent } from './gestione-prenotazioni/modifica-prenotazione.component';
import { ModificaPrenotazioneHeaderComponent } from './gestione-prenotazioni/modifica-prenotazione-header.component';
import { SinotticoPrenotazioniComponent } from './sinottico-prenotazioni/sinottico-prenotazioni.component';
import { RicercaAvanzataPrenotazioniComponent } from './ricerca-avanzata-prenotazioni/ricerca-avanzata-prenotazioni.component';
import { RicercaServiziComponent } from './ricerca-servizi/ricerca-servizi.component';

import { Routes, RouterModule } from '@angular/router';
import { RootComponent } from './root/root.component';
import { SinotticoPrenotazioniHeaderComponent } from './sinottico-prenotazioni/sinottico-prenotazioni-header.component';
import { RicercaAvanzataPrenotazioniHeaderComponent } from './ricerca-avanzata-prenotazioni/ricerca-avanzata-prenotazioni-header.component';
import { RicercaServiziHeaderComponent } from './ricerca-servizi/ricerca-servizi-header.component';
import { AppComponent } from './app.component';
import { LoginComponent } from './core/core.module';
import { DummyAuthGuard } from './service/DummyAuthGuard';

import { SinotticoServiziHeaderComponent } from './sinottico-servizi/sinottico-servizi-header.component';
import { SinotticoServiziComponent } from './sinottico-servizi/sinottico-servizi.component';
import { DuplicaMultiplaPrenotazioneComponent } from 'app/duplica/duplica-multipla-prenotazioni-component';
import { IntervalloCiclicheComponent } from 'app/gestione-cicliche/intervallo-cicliche-component';
import { ModificaCiclicheComponent } from 'app/gestione-cicliche/modifica-cicliche-component';

import { GestioneServiziComponent } from 'app/gestione-servizi/gestione-servizi-component';
import { GestioneServiziHeaderComponent } from 'app/gestione-servizi/gestione-servizi-header.component';
import { SinotticoCiclicheComponent } from 'app/sinottico-cicliche/sinottico-cicliche-component';
import { SinotticoCiclicheHeaderComponent } from 'app/sinottico-cicliche/sinottico-cicliche-header.component';
import { GestioneCiclicheHeaderComponent } from 'app/gestione-cicliche/gestione-cicliche-header.component';

import { CodaRitorniComponent } from 'app/coda-ritorni/coda-ritorni-component';
import { CodaRitorniHeaderComponent } from 'app/coda-ritorni/coda-ritorni-header-component';
import { BlankComponent } from './blank/blank.component';
import { StaticDataResolve } from './static-data/cached-static-data-resolver';

import { SinotticoTurniComponent } from 'app/sinottico-turni/sinottico-turni-component';
import { SinotticoTurniHeaderComponent } from 'app/sinottico-turni/sinottico-turni-header.component';
import { GestioneEquipaggioMezzoComponent } from 'app/gestione-equipaggio-mezzo/gestione-equipaggio-mezzo-component';
import { GestioneEquipaggioMezzoHeaderComponent } from 'app/gestione-equipaggio-mezzo/gestione-equipaggio-mezzo-header.component';
import { GestioneTurnoComponent } from 'app/gestione-turno/gestione-turno-component';
import { GestioneTurnoHeaderComponent } from 'app/gestione-turno/gestione-turno-header.component';
import { CanDeactivateBookingModify } from './gestione-prenotazioni/deactivate-BookingModify';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: RootComponent,
    canActivate: [DummyAuthGuard],
    runGuardsAndResolvers : 'always' ,
    data: {
      breadcrumb: 'Trasporti Ordinari'
    }, resolve: {
      staticData: StaticDataResolve
    },
    children: [
      {
        path: "blank",
        component: BlankComponent
      },
      {
        path: 'gestione-prenotazioni',
        component: ModificaPrenotazioneComponent,
        runGuardsAndResolvers : 'always' ,
        canDeactivate: [CanDeactivateBookingModify],
        data: {
          breadcrumb: 'Nuova Prenotazione',
          headerComponent: ModificaPrenotazioneHeaderComponent,
          headerTitle: 'Gestione Prenotazioni',
          headerIcon: 'fa-plus-square-o',
          showHeaderHook: true,
          isForSearch: false
        }
        },
      { //chiamata con unico parametro id non permette il salvataggio in modifica
        path: 'modifica-prenotazione/:id',
        component: ModificaPrenotazioneComponent,
        runGuardsAndResolvers : 'always' ,
        canDeactivate: [CanDeactivateBookingModify],
        data: {
          breadcrumb: 'Modifica Prenotazioni',
          headerComponent: ModificaPrenotazioneHeaderComponent,
          headerTitle: 'Gestione Prenotazioni',
          headerIcon: 'fa-pencil',
          showHeaderHook: true,
          isForSearch: false,
        }
      },
      { //con parametro sorgente chiamata per gestione chiamata verso archivio e abiltazione modifiche
        path: 'modifica-prenotazione/:source/:id',
        component: ModificaPrenotazioneComponent,
        runGuardsAndResolvers : 'always' ,
        canDeactivate: [CanDeactivateBookingModify],
        data: {
          breadcrumb: 'Modifica Prenotazioni',
          headerComponent: ModificaPrenotazioneHeaderComponent,
          headerTitle: 'Gestione Prenotazioni',
          headerIcon: 'fa-pencil',
          showHeaderHook: true,
          isForSearch: false,
        }
      },
      {
        path: 'sinottico-prenotazioni',
        component: SinotticoPrenotazioniComponent,
        data: {
          breadcrumb: 'Sinottico Prenotazioni',
          headerComponent: SinotticoPrenotazioniHeaderComponent,
          headerTitle: 'Sinottico Prenotazioni',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true
        }
      },
      {
        path: 'sinottico-prenotazioni/:id',
        component: SinotticoPrenotazioniComponent,
        data: {
          breadcrumb: 'Sinottico Prenotazioni',
          headerComponent: SinotticoPrenotazioniHeaderComponent,
          headerTitle: 'Sinottico Prenotazioni',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true
        }
      },
      {
        path: 'ricerca-avanzata-prenotazioni',
        component: RicercaAvanzataPrenotazioniComponent,
        data: {
          breadcrumb: 'Ricerca Avanzata Prenotazioni',
          headerComponent: RicercaAvanzataPrenotazioniHeaderComponent,
          headerTitle: 'Ricerca Avanzata Prenotazioni',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true
        }
      },
      {
        path: 'ricerca-servizi',
        component: RicercaServiziComponent,
        data: {
          breadcrumb: 'Consulta Servizi',
          headerComponent: RicercaServiziHeaderComponent,
          headerTitle: 'Gestione Servizi',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true,
        }
      },
      {
        path: 'sinottico-servizi',
        component: SinotticoServiziComponent,
        data: {
          breadcrumb: 'Sinottico Servizi',
          headerComponent: SinotticoServiziHeaderComponent,
          headerTitle: 'Sinottico Servizi',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true,
        }
      },
      {
        path: '',
        redirectTo: '../sinottico-prenotazioni',
        pathMatch: 'full'
      }, 
      {
        path: 'duplica-multipla-prenotazioni/:id',
        component: DuplicaMultiplaPrenotazioneComponent,
        data: {
          breadcrumb: 'Duplica Multipla Prenotazione',
          headerComponent: ModificaPrenotazioneHeaderComponent,
          headerTitle: 'Duplica Prenotazioni',
          headerIcon: 'fa-pencil',
          showHeaderHook: true,
          isForSearch: false,
        }
      },
      {
        path: 'gestione-servizi/:id',
        component: GestioneServiziComponent,
        runGuardsAndResolvers : 'always' ,
        data: {
          breadcrumb: 'Associazione o Gestione',
          headerComponent: GestioneServiziHeaderComponent,
          headerTitle: 'Associazione o Gestione',
          headerIcon: 'fa-pencil',
          showHeaderHook: false,
          isForSearch: false,
          }
        },
        { //con parametro sorgente chiamata per gestione chiamata verso archivio e abiltazione modifiche
          path: 'gestione-servizi/:source/:id',
          component: GestioneServiziComponent,
          runGuardsAndResolvers : 'always' ,
          data: {
            breadcrumb: 'Dettaglio servizio',
            headerComponent: GestioneServiziHeaderComponent,
            headerTitle: 'Dettaglio servizio',
            headerIcon: 'fa-pencil',
            showHeaderHook: false,
            isForSearch: false,
          }
        },      
      {
        path: 'sinottico-cicliche',
        component: SinotticoCiclicheComponent,
        data: {
          breadcrumb: 'Sinottico Cicliche',
          headerComponent: SinotticoCiclicheHeaderComponent,
          headerTitle: 'Sinottico Cicliche',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true
        }
      },
      {
        path: 'coda-ritorni',
        component: CodaRitorniComponent,
        data: {
          breadcrumb: 'Coda Ritorni',
          headerComponent: CodaRitorniHeaderComponent,
          headerTitle: 'Coda Ritorni',
          headerIcon: 'fa-arrow-left',
          showHeaderHook: true,
          isForSearch: true,
        }
      },
      {
        path: 'intervallo-cicliche/:id',
        component: IntervalloCiclicheComponent,
        data: {
          breadcrumb: 'Prenotazione Ciclica',
          headerComponent: GestioneCiclicheHeaderComponent,
          headerTitle: 'Prenotazione Ciclica',
          headerIcon: 'fa-pencil',
          showHeaderHook: true,
          isForSearch: false,
        }
      },
      {
        path: 'modifica-cicliche/:id',
        component: ModificaCiclicheComponent,
        data: {
          breadcrumb: 'Modifica Ciclica',
          headerComponent: GestioneCiclicheHeaderComponent,
          headerTitle: 'Modifica Ciclica',
          headerIcon: 'fa-pencil',
          showHeaderHook: true,
          isForSearch: false,
        }
      },
      {
        path: 'sinottico-turni',
        component: SinotticoTurniComponent,
        data: {
          breadcrumb: 'Sinottico Turni',
          headerComponent: SinotticoTurniHeaderComponent,
          headerTitle: 'Sinottico Turni',
          headerIcon: 'fa-search',
          showHeaderHook: true,
          isForSearch: true
        }
      },
      {
        path: 'gestione-equipaggio-mezzo/:id',
        component: GestioneEquipaggioMezzoComponent,
        data: {
          breadcrumb: 'Equipaggio Mezzo',
          headerComponent: GestioneEquipaggioMezzoHeaderComponent,
          headerTitle: 'Equipaggio Mezzo',
          headerIcon: 'fa-pencil',
          showHeaderHook: true,
          isForSearch: false,
        }
      },
      {
        path: 'modifica-turno/:id',
        component: GestioneTurnoComponent,
        data: {
          breadcrumb: 'Gestione Turno',
          headerComponent: GestioneTurnoHeaderComponent,
          headerTitle: 'Gestione Turno',
          headerIcon: 'fa-plus-square-o',
          showHeaderHook: true,
          isForSearch: false
        }
      },
      {
        path: 'gestione-turno',
        component: GestioneTurnoComponent,
        runGuardsAndResolvers : 'always' ,
        data: {
          breadcrumb: 'Gestione Turno',
          headerComponent: GestioneTurnoHeaderComponent,
          headerTitle: 'Gestione Turno',
          headerIcon: 'fa-plus-square-o',
          showHeaderHook: true,
          isForSearch: false
        }
      },
    ]
  },
  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(routes, { useHash: true, onSameUrlNavigation : 'reload' });
