import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Inject, Injectable, Optional } from '@angular/core';
import 'rxjs/Rx';
import { BasePathGenerator } from '../../services/services.module';

export class ConfigItem {
    protocol: string
    host: string;
    port: string;
    context: string;
    basePath: string;
    accessPath: string;
    enableWs: boolean;
}

const config = new ConfigItem();
config.protocol = "http";
config.host = "localhost";
config.port = "8080"
config.context = "ordinari"
config.basePath = "api/secure/rest"
config.accessPath = "api/secure"
config.enableWs = true;

@Injectable()
export class BackendConfigService {

    protected basePath: String;

    constructor() {
        this.basePath = BasePathGenerator();

    }

    /*  getURLBase(): String {
  
          let a = window.location;
          let ret = a.protocol + "//" + a.host + "";
          if (a.port === '4200') {            
              ret = a.protocol + "//" + "qalitax.eng.it" + ":9125";
          }        
          ret += "/ordinari-web/"
          return ret;
  
      }*/

    getURLBase(): String {
        return this.basePath;
    }

    /*getURLBase(): string {
        
        return config.protocol + "://" + config.host + ":" + config.port + "/" + config.context + "/"
    }*/

    getUrlOperation(op: string): string {
        //return config.protocol + "://" + config.host + ":" + config.port + "/" + config.context + "/" + config.basePath + "/" + op;
        return this.getURLBase() + config.basePath + "/" + op;
    }

    getUrlControlAccess(op: string): string {
        //return config.protocol + "://" + config.host + ":" + config.port + "/" + config.context + "/" + config.accessPath + "/" + op;
        return this.getURLBase() + config.accessPath + "/" + op;
    }

    getUrlLogin(op: string): string {
        /*return config.protocol + "://" + config.host + ":" + config.port + "/" + config.context + "/" + op;*/
        return this.getURLBase() + op;
    }

    isWsEnabled(): boolean {
        return config.enableWs;
    }
}

