import { Inject, Injectable, Optional } from '@angular/core';
import * as m from './model/index';
import * as common from '../common/model/index';

import { Http, Headers, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { Response, ResponseContentType } from '@angular/http';
import { BASE_PATH, Configuration } from './agent.service.configuration';
import { Observable } from 'rxjs/Observable';
import { InterventionActive } from '../../tablet-layout/intervention-layout/intervention.active.interface';

import { getUuidV4String } from './uuid';
import { environment } from '../../../environments/environment';
import { AgentSession } from '../service.module';

interface PositionHolder {
    posi;
    timestamp;

}

@Injectable()
export class AgentService {

    protected basePath = 'http://localhost';
    public defaultHeaders: Headers = new Headers();
    public configuration: Configuration = new Configuration();
    public deviceId: string;
    private lastPosition: PositionHolder;

    constructor(private http: Http) {
        this.basePath = 'http://' + environment.configuration.service.host + ':' + environment.configuration.service.port;
        setTimeout(() => {
            this.getDeviceId().then((success: Response) => {
                this.deviceId = success.json().deviceId;
            });
        }, 2000)
        this.lastPosition = undefined;
        /*  if (configuration) {
              this.configuration = configuration;
          }*/
    }


    private createAsynchMessage(type: string, payload: any, ttl: number): m.Message {
        const message: m.Message = this.createTplMessage(type, ttl, true);
        message.rpcOperation = type;
        message.payload = JSON.stringify(payload);
        return message;
    }

    private createSynchMessage(type: string, payload: any, ttl: number): m.Message {
        const message: m.Message = this.createTplMessage(type, ttl, false);
        message.payload = JSON.stringify(payload);
        return message;
    }

    private createTplMessage(type: string, ttl: number, asynch: boolean): m.Message {
        if (!this.deviceId) {
            throw new Error("device id is null");
        }
        const message: m.Message = { asynch: asynch };
        message.id = getUuidV4String();
        message.from = this.deviceId;
        message.timestamp = new Date().getTime();
        message.type = type;
        message.ttl = ttl;
        return message;
    }

    private createCommand(type: string, payload: any, ttl: number): m.Message {
        const m = this.createSynchMessage(type, payload, ttl);
        m.rpcOperation = type;
        return m;
    }


    public getCurrentLocation(): common.GpsLocation {
        //return current gps location 
        return null;
    }

    public publishMessage(type, payload, relatesTo, to) {
        payload.from = this.deviceId;
        const message = this.createAsynchMessage(type, payload, 30);
        message.relatesTo = relatesTo;
        if (!message.from) {
            return;
        }
        if (!to) {
            message.to = message.from + "_out";
        }
        else {
            message.to = to;
        }
        return this.post(JSON.stringify(message), "publish").toPromise();
    }

    public beepAlarm() {
        return this.get("beepAlarm").toPromise();
    }

    public stopAlarm() {
        return this.get("stopBeepAlarm").toPromise();
    }

    /* public xhrGet() {
        return this.get("xhrGet").toPromise();
    } */

    public dispatch(method, op, body) {
        let request = {
            method: method,
            op: op,
            payload: body
        }
        return this.post(JSON.stringify(request), "dispatch").toPromise();

    }


    public sendAsynchMessage(type: string, payload: any): Promise<any> {

        //this.logger.info('sendSynchMessage: ' + JSON.stringify(message));
        const p = new Promise((resolve, reject) => {
            /*  if (!message.rpcOperation) {
                 reject(new Error("cannot send synch message without specify rpc operation"));
                 return;
             } */
            let message = null;
            try {
                message = this.createAsynchMessage(type, payload, 30);
            } catch (err) {
                reject(err);
                return;

            }

            this.invokeForAsynchMessage(message).toPromise().then(
                (success: Response) => {
                    resolve(success.json());
                },
                (error) => {
                    reject(error);
                });
        });

        return p;
    }


    public sendSynchMessage(type: string, payload: any): Promise<any> {
        const p = new Promise((resolve, reject) => {
            let message = null;
            try {
                message = this.createAsynchMessage(type, payload, 30);
            } catch (err) {
                reject(err);
                return;

            }
            if (!message.rpcOperation) {
                reject(new Error("cannot send synch message without specify rpc operation"));
                return;
            }
            this.invokeForSynchMessage(message).toPromise().then(
                (success: Response) => {
                    resolve(success.json());
                },
                (error) => {
                    reject(error);
                });
        });

        return p;
    }

    public extractResource(type: string, local: boolean): Promise<Response> {
        return this.getResourceByType(type, local).toPromise();
    }

    private invokeForSynchMessage(body?: m.Message) {
        return this.post(JSON.stringify(body));
    }

    private invokeForAsynchMessage(body?: m.Message) {
        return this.post(JSON.stringify(body), "enqueue");
    }

    private getDeviceId(): Promise<Response> {
        return this.get("deviceId").toPromise();
    }

    public shutdownDevice() {
        return this.get("shutdown").toPromise();
    }

    private getResourceByType(param: string, local: boolean, extraHttpRequestParams?: any): Observable<Response> {
        let path = this.basePath + '/' + 'resource' + '/' + param;
        if (local) {
            path = this.basePath + '/' + 'localResource' + '/' + param;
        }
        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Content-Type header
        let consumes: string[] = [

        ];
        /*if (param !== undefined) {
            queryParameters.set('type', <any>param);
        }*/

        // to determine the Accept header
        let produces: string[] = [
            'application/json;charset=UTF-8'
        ];

        headers.set('Content-Type', 'application/json');

        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = (<any>Object).assign(requestOptions, extraHttpRequestParams);
        }

        return this.http.request(path, requestOptions);
    }


    public callback(param?: string, extraHttpRequestParams?: any): Observable<Response> {
        const path = this.basePath + '/' + 'callback';

        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Content-Type header
        let consumes: string[] = [

        ];
        /*if (param !== undefined) {
            queryParameters.set('type', <any>param);
        }*/

        // to determine the Accept header
        let produces: string[] = [
            'application/json;charset=UTF-8'
        ];

        // headers.set('Content-Type', 'application/json');

        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = (<any>Object).assign(requestOptions, extraHttpRequestParams);
        }

        return this.http.request(path, requestOptions);
    }

    public async initDeviceAgent(siteInfo): Promise<m.SiteInfo> {
        /* let keyReset = localStorage.getItem("KEY_RESET");
        let resetted = false; */
        let newSiteInfo: m.SiteInfo = await this.initMessageService(siteInfo);
        let deviceInitialized = await this.callInitDevice(siteInfo.deviceConf);
        siteInfo.deviceInitialized = deviceInitialized;
        return siteInfo;
    }


    public requestDeviceState() {
        this.get("requestDeviceState").toPromise().then((res) => { }, (fail) => { });
    }

    public dbLookup(type: string, filter?: string): Promise<Response> {
        //console.log("dbLookup", type, filter);
        if (!filter) {
            return this.get("/db/v1/listAll/" + type).toPromise();
        }
        else {
            let request = { filter: filter };
            return this.post(JSON.stringify(request), "/db/v1/lookup/" + type).toPromise();
        }
    }


    public getSiteInfo(siteId: string): Promise<any> {

        const p = new Promise((resolve, reject) => {
            if (!siteId) {
                reject(new Error("cannot get site with any site info in the request"));
                return;
            }
            let obj = {
                siteId: siteId,
                username: "emsmobile",
                password: "3m5m0b1l3"
            }
            this.post(JSON.stringify(obj), "getSiteInfo").toPromise().then(
                (success: Response) => {
                    const res: m.SiteInfo = success.json();
                    //this.logger.info("site info response: " + res);
                    //const cooperativeUrl = res.messageServiceUrl
                    //res.messageReceiverUrl
                    res.operativeArea = siteId;
                    resolve(res);
                },
                (error) => {
                    reject(error);
                });
        })
        /* .then((siteInfo: m.SiteInfo) => {
            return this.initMessageService(siteInfo);
        }); */
        //this.initMessageReceiver().then(this.initMessageService)
        return p;
    }

    private callInitDevice(configuration): Promise<any> {
        return this.post(JSON.stringify(configuration), "initDevice").toPromise();
    }

    private reinitMessageService(res: m.SiteInfo): Promise<any> {
        const p = new Promise((resolve, reject) => {
            this.post(JSON.stringify(res), "reInitMessageService").toPromise().then(success => {
                resolve(res);
            }, failure => {
                reject(failure);
            });
        });
        return p;
    }

    private initMessageService(res: m.SiteInfo): Promise<any> {
        const p = new Promise((resolve, reject) => {
            this.post(JSON.stringify(res), "initMessageService").toPromise().then(success => {
                resolve(res);
            }, failure => {
                reject(failure);
            });
        });
        return p;
    }

    private initMessageReceiver(res: m.SiteInfo) {
        const p = new Promise((resolve, reject) => {
            this.post(JSON.stringify(res), "initMessageReceiver").toPromise().then(success => {
                resolve(res);
            }, failure => {
                reject(failure);
            });
        });
        return p;
    }

    private get(opParam, extraHttpRequestParams?: any): Observable<Response> {
        let path = this.basePath + '/' + opParam;

        let queryParameters = new URLSearchParams();
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Content-Type header
        let consumes: string[] = [

        ];
        /*if (param !== undefined) {
            queryParameters.set('type', <any>param);
        }*/

        // to determine the Accept header
        let produces: string[] = [
            'application/json;charset=UTF-8'
        ];

        headers.set('Content-Type', 'application/json');

        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = (<any>Object).assign(requestOptions, extraHttpRequestParams);
        }

        return this.http.request(path, requestOptions);
    }



    private post(body?: string, op?: string, extraHttpRequestParams?: any): Observable<Response> {

        let path = this.basePath + '/' + 'rpc';
        if (op) {
            path = this.basePath + '/' + op;
        }

        let queryParameters = new URLSearchParams();
        //TODO
        let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json;charset=UTF-8'
        ];

        // to determine the Accept header
        let produces: string[] = [
            'application/json;charset=UTF-8'
        ];

        headers.set('Content-Type', 'application/json');

        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            body: body == null ? '' : body, // https://github.com/angular/angular/issues/10612
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = (<any>Object).assign(requestOptions, extraHttpRequestParams);
        }

        return this.http.request(path, requestOptions);
    }

    /*Prima di inviare verifica se l'ultima posizione è più vecchia di T secondi
    Se si allora verifica che sia stata percorsa una distanza minima e poi procedi */
    public sendNewPosition(posi, checkDiff: number, checkDist: number) {
        let proceed: boolean = false;
        if (!this.lastPosition) {
            proceed = true;
        }
        if (!proceed) {
            proceed = this.checkForNewPosition(posi, checkDiff, checkDist);
        }
        else {
            this.lastPosition = { posi: posi, timestamp: Date.now() };
        }
        //console.log("sendNewPosition proceed: " + proceed);
        if (proceed) {
            this.publishMessage("GPS", posi, undefined, "mobile_queue").then(
                fulfilled => {
                    this.lastPosition = { posi: posi, timestamp: Date.now() };
                },
                rejected => {
                    this.lastPosition = undefined;
                });
        }
    }

    /*  verifica se l'ultima posizione è più vecchia di T secondi
     Se si allora verifica che sia stata percorsa una distanza minima */
    private checkForNewPosition(posi, checkDiff: number, checkDist: number): boolean {
        if (!this.lastPosition) {
            return true;
        }
        let isNew: boolean = false;
        let now = Date.now();
        let diff = now - this.lastPosition.timestamp;
        //se la coordinata è fresca verifica distanza
        //console.log("checkForNewPosition time: " + diff + " - " + checkDiff);
        if (diff > checkDiff) {
            let dist = this.calculateDistance(posi.latitude, posi.longitude, this.lastPosition.posi.latitude, this.lastPosition.posi.longitude);
            //console.log("checkForNewPosition distance: " + dist + " - " + checkDist);
            if (dist > checkDist) {
                return true;
            }
        }

        return false;
    }

    calculateDistance(lat1, lon1, lat2, lon2) {

        let R = 6371; // km 

        let x1 = lat2 - lat1;
        let dLat = this.toRad(x1);
        let x2 = lon2 - lon1;
        let dLon = this.toRad(x2);
        let a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(this.toRad(lat1)) * Math.cos(this.toRad(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        let d = R * c;
        return d;

    }

    toRad(n: number) {
        return n * Math.PI / 180;
    }


    /*utils function*/


}