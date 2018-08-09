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

import { AuthorityDTO } from '../model/authorityDTO';
import { AuthorityFilterDTO } from '../model/authorityFilterDTO';
import { AuthorityInfoDTO } from '../model/authorityInfoDTO';
import { DepartmentInfoDTO } from '../model/departmentInfoDTO';
import { ErrorMessage } from '../model/errorMessage';
import { SearchCostCenterDTO } from '../model/searchCostCenterDTO';
import { SearchIdDTO } from '../model/searchIdDTO';
import { SearchTelephoneDTO } from '../model/searchTelephoneDTO';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class AuthorityModuleServiceService {

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
     * getAuthorityTelephone
     * Permette di ottenere la lista dei numeri di telefono dell&#39;ente
     * @param authorityId 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getAuthorityTelephone(authorityId: string, observe?: 'body', reportProgress?: boolean): Observable<Array<AuthorityDTO>>;
    public getAuthorityTelephone(authorityId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<AuthorityDTO>>>;
    public getAuthorityTelephone(authorityId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<AuthorityDTO>>>;
    public getAuthorityTelephone(authorityId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (authorityId === null || authorityId === undefined) {
            throw new Error('Required parameter authorityId was null or undefined when calling getAuthorityTelephone.');
        }

        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (authorityId !== undefined && authorityId !== null) {
            queryParameters = queryParameters.set('authorityId', <any>authorityId);
        }

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
        ];

        return this.httpClient.get<Array<AuthorityDTO>>(`${this.basePath}/api/secure/rest/authority/getAuthorityTelephone`,
            {
                params: queryParameters,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchAuthorityByCostCenter
     * Permette di cercare gli enti per centro di costo
     * @param searchCostCenterDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchAuthorityByCostCenter(searchCostCenterDTO?: SearchCostCenterDTO, observe?: 'body', reportProgress?: boolean): Observable<Array<AuthorityInfoDTO>>;
    public searchAuthorityByCostCenter(searchCostCenterDTO?: SearchCostCenterDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<AuthorityInfoDTO>>>;
    public searchAuthorityByCostCenter(searchCostCenterDTO?: SearchCostCenterDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<AuthorityInfoDTO>>>;
    public searchAuthorityByCostCenter(searchCostCenterDTO?: SearchCostCenterDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<Array<AuthorityInfoDTO>>(`${this.basePath}/api/secure/rest/authority/searchAuthorityByCostCenter`,
            searchCostCenterDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchAuthorityByDepId
     * Permette di cercare gli enti che posseggono un determinato department Id
     * @param searchIdDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchAuthorityByDepId(searchIdDTO?: SearchIdDTO, observe?: 'body', reportProgress?: boolean): Observable<AuthorityDTO>;
    public searchAuthorityByDepId(searchIdDTO?: SearchIdDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<AuthorityDTO>>;
    public searchAuthorityByDepId(searchIdDTO?: SearchIdDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<AuthorityDTO>>;
    public searchAuthorityByDepId(searchIdDTO?: SearchIdDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<AuthorityDTO>(`${this.basePath}/api/secure/rest/authority/searchAuthorityByDepId`,
            searchIdDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchAuthorityByFilter
     * Permette di cercare gli enti con i filtri passati in input
     * @param authorityFilterDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchAuthorityByFilter(authorityFilterDTO?: AuthorityFilterDTO, observe?: 'body', reportProgress?: boolean): Observable<Array<AuthorityDTO>>;
    public searchAuthorityByFilter(authorityFilterDTO?: AuthorityFilterDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<AuthorityDTO>>>;
    public searchAuthorityByFilter(authorityFilterDTO?: AuthorityFilterDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<AuthorityDTO>>>;
    public searchAuthorityByFilter(authorityFilterDTO?: AuthorityFilterDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<Array<AuthorityDTO>>(`${this.basePath}/api/secure/rest/authority/searchAuthority`,
            authorityFilterDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchAuthorityById
     * Permette di cercare gli enti per id
     * @param searchIdDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchAuthorityById(searchIdDTO?: SearchIdDTO, observe?: 'body', reportProgress?: boolean): Observable<AuthorityDTO>;
    public searchAuthorityById(searchIdDTO?: SearchIdDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<AuthorityDTO>>;
    public searchAuthorityById(searchIdDTO?: SearchIdDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<AuthorityDTO>>;
    public searchAuthorityById(searchIdDTO?: SearchIdDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<AuthorityDTO>(`${this.basePath}/api/secure/rest/authority/searchAuthorityById`,
            searchIdDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchAuthorityByTelephone
     * Permette di cercare gli enti per telefono.
     * @param searchTelephoneDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchAuthorityByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe?: 'body', reportProgress?: boolean): Observable<Array<AuthorityInfoDTO>>;
    public searchAuthorityByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<AuthorityInfoDTO>>>;
    public searchAuthorityByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<AuthorityInfoDTO>>>;
    public searchAuthorityByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<Array<AuthorityInfoDTO>>(`${this.basePath}/api/secure/rest/authority/searchAuthorityByTelephone`,
            searchTelephoneDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchDepartmentByTelephone
     * Permette di cercare i reparti per telefono
     * @param searchTelephoneDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchDepartmentByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe?: 'body', reportProgress?: boolean): Observable<Array<DepartmentInfoDTO>>;
    public searchDepartmentByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<DepartmentInfoDTO>>>;
    public searchDepartmentByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<DepartmentInfoDTO>>>;
    public searchDepartmentByTelephone(searchTelephoneDTO?: SearchTelephoneDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<Array<DepartmentInfoDTO>>(`${this.basePath}/api/secure/rest/authority/searchDepartmentByTelephone`,
            searchTelephoneDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * searchDepartmentsByCenterCost
     * Permette di cercare i reparti per centro di costo
     * @param searchCostCenterDTO 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public searchDepartmentsByCenterCost(searchCostCenterDTO?: SearchCostCenterDTO, observe?: 'body', reportProgress?: boolean): Observable<Array<DepartmentInfoDTO>>;
    public searchDepartmentsByCenterCost(searchCostCenterDTO?: SearchCostCenterDTO, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<DepartmentInfoDTO>>>;
    public searchDepartmentsByCenterCost(searchCostCenterDTO?: SearchCostCenterDTO, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<DepartmentInfoDTO>>>;
    public searchDepartmentsByCenterCost(searchCostCenterDTO?: SearchCostCenterDTO, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

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

        return this.httpClient.post<Array<DepartmentInfoDTO>>(`${this.basePath}/api/secure/rest/authority/searchDepartmentsByCenterCost`,
            searchCostCenterDTO,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
