import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { tokenNotExpired } from '../util/jwt'

@Injectable()
export class DummyAuthGuard implements CanActivate {

    constructor(private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        /*if (sessionStorage.getItem('currentToken')) {
            // logged in so return true
            return true;
        }*/

        let proceed = tokenNotExpired('currentToken');
        //console.log("token not expired? : " + proceed);
        if (proceed) {
            return true;
        }

        // not logged in so redirect to login page with the return url*/
        //console.log("can activate route to: " + route + " - " + state);
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
    }
}