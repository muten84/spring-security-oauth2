import { NgModule, APP_BOOTSTRAP_LISTENER, ComponentRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
/* import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; */
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ServiceModule, StorageService, LocalBusService } from './service/service.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthGuard } from './shared';
import { BlockUIModule } from 'ng-block-ui';
import { MaterialModule } from '@blox/material';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ComponentsModule, SidebarComponent, HeaderComponent, ListItemDialogComponent, ConfirmDialogComponent, ListItemDialogNoSearchComponent, CalendarDialogComponent } from './tablet-layout/components/components.module';


// AoT requires an exported function for factories
export function createTranslateLoader(http: HttpClient) {
    // for development
    // return new TranslateHttpLoader(http, '/start-angular/SB-Admin-BS4-Angular-5/master/dist/assets/i18n/', '.json');
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
    imports: [
        CommonModule,
        MatCardModule,
        ComponentsModule,
        BlockUIModule,
        BrowserModule,
        NoopAnimationsModule,
        HttpClientModule,
        HttpModule,
        MatIconModule,
        ServiceModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: createTranslateLoader,
                deps: [HttpClient]
            }
        }),
        AppRoutingModule
    ],
    declarations: [AppComponent],
    providers: [AuthGuard, {
        provide: APP_BOOTSTRAP_LISTENER, multi: true, useFactory: (ss: StorageService, bus: LocalBusService) => {
            return (component: ComponentRef<any>) => {
                ////console.log("APP_BOOTSTRAP_LISTENER: " + ss.getItem("app_error"));
                if (ss.getItem("app_error")) {
                    bus.notifyAll('dialogs', { type: 'messageDialog', payload: { title: "Errore", content: "Attenzione inizializzazione del dispositivo fallita" } });
                }

            }
        }, deps: [StorageService, LocalBusService]
    }],
    bootstrap: [AppComponent]
})
export class AppModule {
    constructor(matIconRegistry: MatIconRegistry, domSanitizer: DomSanitizer) {
        matIconRegistry.addSvgIconSet(domSanitizer.bypassSecurityTrustResourceUrl('./assets/icons/mdi.svg')); // Or whatever path you placed mdi.svg at
    }
}
