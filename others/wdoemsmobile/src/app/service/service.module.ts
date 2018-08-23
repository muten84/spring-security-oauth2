import { NgModule, InjectionToken, APP_INITIALIZER } from '@angular/core';
export * from './core/index';
export * from './application/index';

export * from './application/model/index';
export * from './agent/model/index';
export * from './application/command/index';
export * from './agent/index';
export * from './logging/index';
import { CORE_APIS } from './core/index';
import { APPLICATION_API, ResourceListService } from './application/index';
import { AGENT_API, AgentService } from './agent/index';
import { WebSocketService } from './core/websocket.service'
import { UTIL_APIS, Logger } from './logging/index';
import * as appModels from './application/model/index';
import { REMOTE_LOG_CONFIG, CONSOLE_LOG_CONFIG, LOGGER_CONFIG, NO_LOG_CONFIG } from './logging/logging-api';
import { ConfigurationService } from './core/configuration.service';
import { StorageService } from './core/storage.service';
import { environment } from '../../environments/environment';
import { PatientStatusService } from './application/patient-status.service';



const APIS = [];
APIS.push(ConfigurationService, Logger, CORE_APIS, APPLICATION_API, AGENT_API, UTIL_APIS, PatientStatusService);

/* let logType = 0; */

export function initConfig(logger: Logger, configService: ConfigurationService, agentService: AgentService, storage: StorageService): Function {
    let f = () => {

        const initPromise = new Promise((resolve, reject) => {
            //console.log("APP_INITIALIZER");

            storage.init();
            logger.init({ type: environment.configuration.logging.type });
            let siteInfo = storage.getFromLocal("siteInfo");
            if (!siteInfo) {
                resolve(true);
                return;
            }
            const siteInfoObj = siteInfo;
            siteInfoObj.username = environment.configuration.security.credentials.username;
            siteInfoObj.password = environment.configuration.security.credentials.password;
            let p1 = agentService.initDeviceAgent(siteInfoObj);
            let p2 = p1.then((onresp) => {
                configService.getSettings()
                    .then((succ) => {
                        //const logType = configService.getConfigurationParam("GENERAL", "LOG_TYPE", 1);
                        console.log("settings loaded");

                        resolve(true);
                    });
            });
        }).catch((rejection) => {
            //console.log("rejection intercepted: " + rejection);
            storage.saveItem("app_error", rejection);
        });
        return initPromise;
    }
    return f;

}

/*  */
@NgModule({
    declarations: [
    ],
    imports: [
    ],
    providers: [

        { provide: APP_INITIALIZER, useFactory: initConfig, deps: [Logger, ConfigurationService, AgentService, StorageService], multi: true },
        APIS
    ]
})
export class ServiceModule { }
