import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Observer } from "rxjs/Observer";
import { Message } from '../agent/index';
import { Logger, LoggerApi } from './logging-api';

@Injectable()
export class ConsoleLogService implements LoggerApi {

    info(message: string) {
        console.info(message);
    }
    warning(message: string, error?: Error) {
        console.warn(message, error);
    }

    error(message: string, error?: Error) {
        console.error(message, error);
    }
    debug(message: string, error?: Error) {
        console.debug(message, error);
    }
}