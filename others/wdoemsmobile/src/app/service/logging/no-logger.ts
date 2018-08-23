import { Message } from '../agent/index';
import { Logger, LoggerApi } from './logging-api';
import { Injectable } from '@angular/core';
import { WebSocketService } from '../core/index';

@Injectable()
export class NoLogService implements LoggerApi {

    constructor() {
    }

    info(message: string) {

    }

    warning(message: string, error?: Error) {

    }

    error(message: string, error: Error) {

    }

    debug(message: string, error: Error) {

    }

}