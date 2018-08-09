import { Injectable } from "@angular/core";
import { Message, Type, StartMessage, NavigateMessage } from "./messages";

@Injectable()
export class BrowserCommunicationService {

    private child: Window;
    private parent: Window;

    private listeners: { [key: string]: ((ev: Message) => any)[] } = {};

    private listener = (ev: MessageEvent) => {
        const data: Message = ev.data;
        const type = data.type;
        const listeners = this.listeners[type];
        if (listeners) {
            listeners.forEach(l => {
                l(data);
            });
        }

    };

    constructor() {
        this.parent = window.opener;
        const self = this;

        window.addEventListener("message", this.listener, false);

        window.addEventListener("beforeunload", function (e) {
            // Do something
            self.closeChildren('', '');

        }, false);
    }

    public addEventListener(type: Type, listener: (ev: Message) => any) {
        let arr = this.listeners[type];
        if (!arr) {
            arr = [];
        }
        arr.push(listener);

        this.listeners[type] = arr;
    }


    public removeEventListener(type: Type, listener: (ev: Message) => any) {
        let arr = this.listeners[type];
        if (arr) {
            this.listeners[type] = arr.filter(l => {
                return l !== listener;
            });
        }
    }

    public openChildren(url: string, type: 'servizi' | 'prenotazioni' | 'mezzi', width: number, height: number): Window {
        if (this.isChildOpen()) {
            this.focusChild();
            let msg: NavigateMessage = {
                path: type
            }
            this.postMessage(Type.navigate, msg);
        } else {

            this.child = window.open(url, "sinottico",
                'channelmode=no,directories=no,fullscreen=yes,height='
                + height
                + ', location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=no,toolbar=no,width='
                + width + '', false);
            // dopo aver aperto la finestra mi metto in ascolto sullo startup
            const ls1 = (ev: Message) => {
                //se arriva una richiesta del token invio il token alla finestra figlia
                let token = sessionStorage.getItem('currentToken');

                let tkMsg: StartMessage = {
                    token: token,
                    path: type
                }
                this.postMessage(Type.token, tkMsg, '*');
            };

            this.addEventListener(Type.startPopup, ls1);
        }
        return this.child;
    }

    public closeChildren(url: string, name: string) {
        if (this.child) {
            this.child.close();
        }
    }

    public postMessage(type: Type, message: Message, origin?: string) {
        message.type = type;
        let o = origin ? origin : "*";
        if (this.child) {
            this.child.postMessage(message, o);
        } else if (this.parent) {
            this.parent.postMessage(message, o);
        }
    }

    public isChildOpen(): boolean {
        return this.child && !this.child.closed;
    }

    public focusChild() {
        if (this.isChildOpen()) {
            this.child.focus();
        }
    }

}