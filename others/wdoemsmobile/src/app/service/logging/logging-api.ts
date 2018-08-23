import { Inject, NgModule, InjectionToken, Injectable } from '@angular/core';
import { ConsoleLogService } from './console-logging.service';
import { NoLogService } from './no-logger';
import { RemoteLogService } from './remote-logging.service';
import { WebSocketService } from '../core/websocket.service';
import { environment } from '../../../environments/environment';

export const LOGGER_CONFIG = new InjectionToken<LoggerConfig>('logger.config');

export interface LoggerConfig {
    type: number;
}

export const NO_LOG_CONFIG: LoggerConfig = {
    type: 0
};

export const CONSOLE_LOG_CONFIG: LoggerConfig = {
    type: 1
};

export const REMOTE_LOG_CONFIG: LoggerConfig = {
    type: 2
};

export interface LoggerApi {
    info(message: string);
    warning(message: string, erro?: Error);
    error(message: string, error?: Error);
    debug(message: string, error?: Error);
}

@Injectable()
export class Logger {

    logger: LoggerApi;

    constructor(public ws: WebSocketService) {
        this.init({ type: environment.configuration.logging.type });
    }

    init(config: LoggerConfig) {
        console.log("configuring log service type is: " + config.type);
        switch (config.type) {
            case 0: {
                this.logger = new NoLogService();
                break;
            }
            case 1: {
                this.logger = new ConsoleLogService();
                break;
            }
            case 2: {
                this.logger = new RemoteLogService(this.ws);
                break;
            }
        }
    }

    info(message: string) {
        if (this.logger)
            this.logger.info(message);
    }

    warning(message: string, erro?: Error) {
        if (this.logger)
            this.logger.warning(message, erro);
    }

    error(message: string, error?: Error) {
        if (this.logger)
            this.logger.error(message, error);
    }

    debug(message: string, error?: Error) {
        if (this.logger)
            this.logger.debug(message, error);
    }
}