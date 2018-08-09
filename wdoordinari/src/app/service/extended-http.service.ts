import { Injectable } from '@angular/core';
import { Request, XHRBackend, RequestOptions, Response, Http, RequestOptionsArgs, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { ComponentHolderService } from './shared-service';
import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';



/*
export function provideHttpService(backend: XHRBackend, options: RequestOptions) {
    return { provide: ExtendedHttpService, useFactory: () => new ExtendedHttpService(backend, options) }
}

@Injectable()
export class HttpDependencies {
   
    constructor(backend: XHRBackend, defaultOptions: RequestOptions) {

    }
}
*/
@Injectable()
export class ExtendedHttpService extends Http {


    constructor(private componentService: ComponentHolderService,
         backend: XHRBackend, 
         defaultOptions: RequestOptions) {
        super(backend, defaultOptions);
    }

    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
        let root = this.componentService.getRootComponent();
        /*if (root) {
            root.mask();
        }*/
        /*if (root) {
            root.showToastMessage("warning", "test");
        }*/
        let headers = new Headers()
        /* headers.append("Content-Type", 'application/json');
         headers.append('Accept', 'application/json');
         headers.append('Access-Control-Allow-Origin', '*');*/
        //console.log("request: " + url);
        if (typeof url === 'string') {
            if (url.match('secure')) {
                if (!options.headers) {
                    options = { headers: headers };
                }
                this.setHeaders(options);
            }
        } else {
            // if (url.url.match('secure')) {
            this.setHeaders(url);
            //}
        }


        let o: Observable<Response> = super.request(url, options).catch((error: any) => {
            /*Luigi: gestione dei codici http diversi da 200ok al momento abbiamo mappato
            il 422, 417, 403, 401 e 500, l'oggetto err resituito al controller
            dovrebbe essere gestito solamente se si deve aggiungere qualche pecuialirtà alla gestione degli errori
            generica, grazie per la collaborazione*/
            //console.log(error);
            if (root) {
                console.log("error: " + error);
                root.unmask();
            }
            let err = error.json();
            if (error.status === 401 || error.status === 403) {
                console.log('errore 401 o 403');
                console.log('utente sloggato');
                sessionStorage.removeItem('currentToken');
                if (root) {
                    root.redirectToLogin("Attenzione", "Sessione scaduta, per motivi di sicurezza sarà necessario effettuare nuovamente la login");
                    // root.showToastMessage("error", "Sessione scaduta, eseguire un refresh della pagina");
                }
                return Observable.throw(new Error("managed"));
            }
            else if (error.status <= 0) {
                if (root) {
                    root.showModal("Errore invio richiesta", "Non è stato possibile inviare la richiesta al server. Se il problema persiste contattare l'assistenza");
                    return Observable.throw(error);
                }
            }
            else if (error.status === 422) {
                //console.log("errore 422 di validazione input: " + err);
                let msg = ""
                if (err.message) {
                    msg = ""
                    //msg += err.message; //rimosso la prima parte del messaggio
                    err.cause.split(",").forEach((m) => {
                        let s = m.split(":::")[1];
                        //console.log(s);
                        //msg += ': ' + s;
                        msg += s;
                        msg += ","
                    });
                    if (msg.length > 0)
                        msg = msg.slice(0, -2);
                }
                else if (err.msgList) {
                    msg = "";
                    err.msgList.forEach((m) => {
                        msg += ", " + m.message;
                    });
                    if (msg.length > 0)
                        msg = msg.slice(0, -1);
                }

                if (root) {
                    //root.showToastMessage("warning", "" + msg);
                    root.showModal("Attenzione verificare dati", msg.split(','));
                }
                return Observable.throw(new Error("managed"));
            }
            else if (error.status === 420) {
                err.msgList.forEach(m => {
                    let type: string = m.messageType || 'error';
                    //root.showToastMessage(type.toLowerCase(), error.message);
                    root.showModal("Errore elaborazione richiesta", m.message);
                });
                return Observable.throw(new Error("managed"));
            }
            else if (error.status === 500) {
                //console.log('errore 500');
                let exception: string = err.exception;
                if (exception.includes('BadCredentials') || err.message.includes('token')) {
                    //console.log('utente sloggato');
                    sessionStorage.removeItem('currentToken');
                    if (root)
                        //root.showToastMessage("error", "Token di autenticazione scaduto uscire eseguire un refresh della pagina");
                        root.showModal("Errore autenticazione", "Token di autenticazione scaduto uscire eseguire un refresh della pagina");
                }
                else {
                    if (root)
                        //root.showToastMessage("error", "Errore durante l'elaborazione della richiesta. Contattare l'assistenza");
                        root.showModal("Errore richiesta", "Errore durante l'elaborazione della richiesta. Contattare l'assistenza");
                }
                return Observable.throw(err);
            }
            //   return Observable.throw(err.error + ' ' + err.message || 'Server error');
            // (Mauro) restituisco tutto l'oggetto in modo da poterlo gestire nelle varie maschere  
            return Observable.throw(err);
        }).share(); // ...errors if any
        // il metodo share evita che ad ogni subscribe venga invocata la chiamata http
        /*o.subscribe(res => {
            if (root) {
                root.unmask();
            }
        });*/

        return o;
    }

    private catchErrors(res: any) {
        return () => {
            if (res.status === 401 || res.status === 403) {
                /*this.router.navigate(['logout']);*/
                //console.log('errore 401 o 403');
            }
            return Observable.throw(res);
        };
    }

    private setHeaders(objectToSetHeadersTo: Request | RequestOptionsArgs) {
        let token = sessionStorage.getItem('currentToken');

        if (!token || token == null || token === 'undefined') {
            // TODO: SLOGGARE L'UTENTE!!!!
            //console.log('utente senza token!!')
            return;
        }
        objectToSetHeadersTo.headers.set('Authorization', 'Bearer ' + token);
        objectToSetHeadersTo.headers.set('Content-Type', 'application/json');
        objectToSetHeadersTo.headers.set('Accept', 'application/json');
        objectToSetHeadersTo.headers.set('Access-Control-Allow-Origin', '*');
    }
}
