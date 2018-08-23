import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler } from "@angular/common/http";
import { ComponentHolderService } from "../../service/shared-service";
import { Observable } from "rxjs";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(private componentService: ComponentHolderService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        let root = this.componentService.getRootComponent();

        // send cloned request with header to the next handler.
        return next.handle(req).catch((error: any) => {
            /*Luigi: gestione dei codici http diversi da 200ok al momento abbiamo mappato
            il 422, 417, 403, 401 e 500, l'oggetto err resituito al controller
            dovrebbe essere gestito solamente se si deve aggiungere qualche pecuialirtà alla gestione degli errori
            generica, grazie per la collaborazione*/
            //console.log(error);
            if (root) {
                console.log("error: " + error);
                root.unmask();
            }
            let err = error.error;
            if (error.status === 401 || error.status === 403) {
                console.log('errore 401 o 403');
                console.log('utente sloggato');
                sessionStorage.removeItem('currentToken');
                if (root) {
                    root.redirectToLogin("Attenzione", "Sessione scaduta, per motivi di sicurezza sarà necessario effettuare nuovamente la login");
                    // root.showToastMessage("error", "Sessione scaduta, eseguire un refresh della pagina");
                }
            } else if (error.status <= 0) {
                if (root) {
                    root.showModal("Errore invio richiesta", "Non è stato possibile inviare la richiesta al server. Se il problema persiste contattare l'assistenza");
                }
            } else if (error.status === 422) {
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
                } else if (err.msgList) {
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
            } else if (error.status === 420) {
                err.msgList.forEach(m => {
                    let type: string = m.messageType || 'error';
                    //root.showToastMessage(type.toLowerCase(), error.message);
                    root.showModal("Errore elaborazione richiesta", m.message);
                });
            } else if (error.status === 500) {
                //console.log('errore 500');
                let exception: string = err.exception;
                if (exception.includes('BadCredentials') || err.message.includes('token')) {
                    //console.log('utente sloggato');
                    sessionStorage.removeItem('currentToken');
                    if (root)
                        //root.showToastMessage("error", "Token di autenticazione scaduto uscire eseguire un refresh della pagina");
                        root.showModal("Errore autenticazione", "Token di autenticazione scaduto uscire eseguire un refresh della pagina");
                } else {
                    if (root)
                        //root.showToastMessage("error", "Errore durante l'elaborazione della richiesta. Contattare l'assistenza");
                        root.showModal("Errore richiesta", "Errore durante l'elaborazione della richiesta. Contattare l'assistenza");
                }
            }
            return [];
        }).share(); // ...errors if any;
    }
}