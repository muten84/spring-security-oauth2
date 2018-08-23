import { Injectable } from "@angular/core";
import { Configuration } from "../agent/agent.service.configuration";
import { Http, Headers, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { Response, ResponseContentType } from '@angular/http';
import { Observable } from "rxjs/Observable";
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface CurrentSession {
    sessionId: string;
    entries: Array<any>;
}

@Injectable()
export class StorageService {
    protected basePath = 'http://' + environment.configuration.service.host + ':' + environment.configuration.service.port;

    deviceId;

    private TO_FLUSH_KEYS = ['application_status', 'RESOURCE_VERSION', 'patientList'];

    constructor(private httpClient: HttpClient) {
        /* this.init(); */
    }

    public init() {
        //console.log("init session StorageService");
        const promise = this.httpClient.get(this.basePath + '/' + 'deviceId').toPromise();
        promise.then((success) => {
            let res = <any>success;
            this.deviceId = res.deviceId;
            let p = this.httpClient.get(this.basePath + '/' + 'getCurrentSession/' + res.deviceId).toPromise();
            p.then((response) => {
                if (!response && sessionStorage.length > 0) {
                    //console.log("session is empty saving on agent");
                    this.saveCurrentSession().then((session) => {
                        //console.log("session was empty and it hase been saved from application: " + session);
                    });
                }
                else if (sessionStorage.length <= 0) {
                    //console.log("current loaded session is present and need to be saved in localstorage: " + response);
                    //this.fillCurrentSessionInSessionStorage(<CurrentSession>response);
                }
            });
        })
    }

    public saveToLocal(key: string, value: any) {
        if (value) {
            localStorage.setItem(key, JSON.stringify(value));
        }
    }

    public getFromLocal(key: string) {
        return JSON.parse(localStorage.getItem(key));
    }

    private fillCurrentSessionInSessionStorage(session: CurrentSession) {
        //console.log("saving current session in session storage");
        sessionStorage.clear();
        session.entries.forEach(e => {
            sessionStorage.setItem(e.key, e.value);
        });

    }

    public saveItem(key: string, value: any) {
        try {
            if (value != undefined) {
                sessionStorage.setItem(key, JSON.stringify(value));
            }
        } catch (err) {
            console.log("error on saving item");
        }

        /*   let found = this.TO_FLUSH_KEYS.find((e) => { return e === key });
          if (found) {            
              this.saveCurrentSession().then((success: CurrentSession) => {                
              });
          } */


    }

    public getItem(key: string): any {
        let tmp = sessionStorage.getItem(key);
        if (tmp == undefined) {
            return undefined;
        }
        return JSON.parse(tmp);
    }

    public removeItem(key: string) {
        sessionStorage.removeItem(key)
    }

    public removeAll() {
        sessionStorage.clear();
        localStorage.clear();
    }

    public getCurrentSession(): Promise<CurrentSession> {
        let p = new Promise<any>((resolve, reject) => {
            /*  this.httpClient.get(this.basePath + '/' + "getCurrentSession/" + this.deviceId).toPromise().then(
                 (success) => {
                     resolve(success);
                 }),
                 (failure) => {
                     reject(failure);
                 } */

        });

        return p;
    }

    public saveCurrentSession(): Promise<any> {
        // let session = this.buildCurrentSession();
        // return this.httpClient.post(this.basePath + '/' + 'saveSession', session).toPromise();
        let p = new Promise<any>((resolve, reject) => {
            /*  this.httpClient.get(this.basePath + '/' + "getCurrentSession/" + this.deviceId).toPromise().then(
                 (success) => {
                     resolve(success);
                 }),
                 (failure) => {
                     reject(failure);
                 } */
            resolve({});

        });

        return p;
    }

    private buildCurrentSession(): CurrentSession {
        let c: CurrentSession = {
            sessionId: this.deviceId,
            entries: []
        };

        for (let i: number = 0; i < sessionStorage.length; i++) {
            const k = sessionStorage.key(i);
            const v = sessionStorage.getItem(k);
            c.entries.push({
                key: k,
                value: v
            });
        }
        return c;

    }
}