import { Injectable } from "@angular/core";
import { Configuration } from "../agent/agent.service.configuration";
import { Http, Headers, URLSearchParams } from '@angular/http';
import { RequestMethod, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { Response, ResponseContentType } from '@angular/http';
import { Observable } from "rxjs/Observable";
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class ConfigurationService {
    private cachedConfiguration: Array<any> = [];

    protected basePath = 'http://' + environment.configuration.service.host + ':' + environment.configuration.service.port;
    public defaultHeaders: Headers = new Headers();
    public configuration: Configuration = new Configuration();

    //"CONFIGURATION", true

    constructor(private httpClient: HttpClient) {
        /*    let f = async () => {
               //console.log("requesting local configuration....");
               this.cachedConfiguration = await this.requestLocalConfiguration();
               //console.log("requesting local configuratiom done");
           }
           f(); */

    }


    getSettings(): Promise<any> {
        console.log(`getSettings:: before http.get call`);

        const promise = this.httpClient.get(this.basePath + '/' + 'localResource' + '/' + "CONFIGURATION")
            .toPromise()
            .then(settings => {
                console.log(`Settings from API: `, settings);
                this.cachedConfiguration = <Array<any>>settings;
                return settings;
            });

        return promise;
    }

    /*   public async requestLocalConfiguration(): Promise<Array<any>> {
          //console.log("app initilizer requesting local configuration");
          let response = await this.getResourceByType("CONFIGURATION", true);
          this.cachedConfiguration = response.json();
          return this.cachedConfiguration;
      } */

    public getConfigurationParam(section: string, parameter: string, defaultValue?: any) {
        //console.log("get configuration param2", section, parameter, defaultValue);
        let value = undefined;
        if (this.cachedConfiguration && this.cachedConfiguration.length > 0) {
            let s = this.cachedConfiguration.find((e) => {
                return e.name === section;
            });
            let parameters: Array<any> = s.parameter;
            if (parameters && parameters.length > 0) {
                value = parameters.find((p) => {
                    return p.name === parameter;
                })
            }
        }
        if (!value && defaultValue) {
            //console.log("warning returning default value.");
            return defaultValue;
        }
        return value.value;
    }

    public getConfigurationParamObject(section: string, parameter: string) {
        //console.log("get configuration param2", section, parameter, defaultValue);
        let value = undefined;
        if (this.cachedConfiguration && this.cachedConfiguration.length > 0) {
            let s = this.cachedConfiguration.find((e) => {
                return e.name === section;
            });
            let parameters: Array<any> = s.parameter;
            if (parameters && parameters.length > 0) {
                value = parameters.find((p) => {
                    return p.name === parameter;
                })
            }
        }

        return value;
    }

    /*   private getResourceByType(param: string, local: boolean, extraHttpRequestParams?: any): Promise<Response> {
          let path = this.basePath + '/' + 'resource' + '/' + param;
          if (local) {
              path = this.basePath + '/' + 'localResource' + '/' + param;
          }
          let queryParameters = new URLSearchParams();
          let headers = new Headers(this.defaultHeaders.toJSON());
  
          let consumes: string[] = [
  
          ];
  
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
  
          if (extraHttpRequestParams) {
              requestOptions = (<any>Object).assign(requestOptions, extraHttpRequestParams);
          }
  
          return this.http.request(path, requestOptions).toPromise();
      } */
}
