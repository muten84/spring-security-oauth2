/**
 * Swagger Maven Plugin Sample
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: v2
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs/Observable';

import { ErrorMessage } from '../model/errorMessage';
import { OverviewTailReturnFilterDTO } from '../model/overviewTailReturnFilterDTO';
import { SearchTailReturnResultDTO } from '../model/searchTailReturnResultDTO';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class TailReturnsServiceService {

    protected basePath = 'http://localhost';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (const consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * searchTailReturnsByFilter
     * Permette di cercare i ritorni delle prenotazioni dei trasporti con i filtri passati in input
     * @param overviewTailReturnFilterDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchTailReturnsByFilter(overviewTailReturnFilterDTO?: OverviewTailReturnFilterDTO, observe?: 'body', reportProgress?: boolean): Observable<SearchTailReturnResultDTO>;
    public searchTailReturnsByFilter(overviewTailReturnFilterDTO?: OverviewTailReturnFilterDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<SearchTailReturnResultDTO>>;
    public searchTailReturnsByFilter(overviewTailReturnFilterDTO?: OverviewTailReturnFilterDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<SearchTailReturnResultDTO>>;
    public searchTailReturnsByFilter(overviewTailReturnFilterDTO?: OverviewTailReturnFilterDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json;charset=UTF-8'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json;charset=UTF-8'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.post<SearchTailReturnResultDTO>(`${this.basePath}/api/secure/rest/tailReturns/searchTailReturns`,
            overviewTailReturnFilterDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
