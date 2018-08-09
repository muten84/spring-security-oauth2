import { Message } from '../agent/index';
import { Logger, LoggerApi } from './logging-api';
import { Injectable } from '@angular/core';
import { WebSocketService } from '../core/index';

@Injectable()
export class RemoteLogService implements LoggerApi {

    constructor(public ws: WebSocketService) {
    }

    info(message: string) {
        let m: Message = this.createLogMessage("info", message, null);
        this.ws.sendMessage(m);
    }
    warning(message: string, error?: Error) {
        let m: Message = this.createLogMessage("warning", message, error);
        this.ws.sendMessage(m);
    }
    error(message: string, error: Error) {
        let m: Message = this.createLogMessage("error", message, error);
        this.ws.sendMessage(m);
    }
    debug(message: string, error: Error) {
        let m: Message = this.createLogMessage("error", message, error);
        this.ws.sendMessage(m);
    }

    private createLogMessage(verbosity: string, message: string, error?: Error): Message {
        const m: Message = { asynch: false };
        m.from = "localapp";
        m.id = "1";
        m.timestamp = new Date().getTime();
        m.type = "LOG"
        /* if (caller) {
            message = "[" + caller.name + "]" + " - " + message;
        } */
        m.payload = JSON.stringify({
            verbosity: verbosity,
            message: message,
            error: error ? error.message : ''
        })
        return m;
    }


}