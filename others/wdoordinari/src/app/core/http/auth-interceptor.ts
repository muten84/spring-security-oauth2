import { HttpInterceptor, HttpRequest, HttpHandler } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor() { }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        if (req.url.match('secure')) {
            req = this.setHeaders(req);
        }

        // send cloned request with header to the next handler.
        return next.handle(req);
    }



    private setHeaders(req: HttpRequest<any>): HttpRequest<any> {
        let token = sessionStorage.getItem('currentToken');

        if (!token || token == null || token === 'undefined') {
            // TODO: SLOGGARE L'UTENTE!!!!
            //console.log('utente senza token!!')
            return req;
        }

        return req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + token)
                .set('Content-Type', 'application/json')
                .set('Access-Control-Allow-Origin', '*'),
        });
    }
}