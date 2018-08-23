import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Observer } from "rxjs/Observer";
import { Message } from '../service.module';

@Injectable()
export class WebSocketService {
    private subject: Subject<MessageEvent>;

    private ws: WebSocket;

    public connect(url: string): Subject<MessageEvent> {
        if (!this.subject) {
            this.subject = this.create(url);
        }
        return this.subject;
    }

    public sendMessage(message: Message) {
        if (this.ws) {
            // //console.log("sending remote logging message: " + message.payload);
            this.ws.send(JSON.stringify(message));
        }
    }

    private create(url: string): Subject<MessageEvent> {
        let ws = new WebSocket(url);
        this.ws = ws;
        let observable = Observable.create(
            (obs: Observer<MessageEvent>) => {
                ws.onmessage = obs.next.bind(obs);
                ws.onerror = obs.error.bind(obs);
                ws.onclose = obs.complete.bind(obs);

                return ws.close.bind(ws);
            });

        let observer = {
            next: (data: Object) => {
                if (ws.readyState === WebSocket.OPEN) {
                    // ws.send(JSON.stringify(data));
                    // //console.log("websocket opened!!!!!");

                }
            }
        };

        return Subject.create(observer, observable);
    }

    // For random numbers
    /* public connectData(url: string): Subject<number> {
         if (!this.subjectData) {
             this.subjectData = this.createData(url);
         }
         return this.subjectData;
     }*/

} // end class WebSocketService