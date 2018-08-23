import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { AuthGuard } from './shared';

/*{ path: '', loadChildren: './layout/layout.module#LayoutModule', canActivate: [AuthGuard] },,*/

const routes: Routes = [
    { path: '', loadChildren: './tablet-layout/layout.module#LayoutModule', canActivate: [AuthGuard] },
    { path: 'loader', loadChildren: './loader/loader.module#LoaderModule', canActivate: [AuthGuard] },    
    { path: 'intervention', loadChildren: './tablet-layout/intervention-layout/intervention.module#InterventionModule' },
    { path: 'login', loadChildren: './login/login.module#LoginModule' },
    { path: 'signup', loadChildren: './signup/signup.module#SignupModule' },
    { path: 'error', loadChildren: './server-error/server-error.module#ServerErrorModule' },
    { path: 'access-denied', loadChildren: './access-denied/access-denied.module#AccessDeniedModule' },
    { path: 'not-found', loadChildren: './not-found/not-found.module#NotFoundModule' },
    { path: '**', redirectTo: 'not-found' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
