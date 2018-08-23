﻿import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, RequestOptionsArgs, RequestMethod } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { BackendConfigService } from '../service/config.service';
import 'rxjs/Rx'

@Injectable()
export class AuthService {
    public defaultHeaders: Headers = new Headers();

    constructor(private http: Http, private config: BackendConfigService) { }

    login(username: string, password: string) {
        /*let headers = new Headers(this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845*/
        let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' }); // ... Set content type to JSON
        let queryParameters = new URLSearchParams();
        let requestOptions: RequestOptionsArgs = new RequestOptions({
            method: RequestMethod.Post,
            headers: headers,
            search: queryParameters,
            withCredentials: false
        });



        let bodyString = 'username=' + username + '&password=' + password;
        let url = this.config.getUrlLogin('login');

        return this.http.post(url, bodyString, requestOptions)
            .map((response: Response) => {
                // login successful if there's a jwt token in the response
                let token = response.json();
                if (token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    sessionStorage.setItem('currentToken', token.token);
                    // console.log('Response from login: ' + token.token);
                }
            });
    }

    logout() {
        // remove user from local storage to log user out
        let currentToken = sessionStorage.getItem('currentToken');
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');
        headers.append('Access-Control-Allow-Origin', '*');

        let requestOptions: RequestOptionsArgs = new RequestOptions({ headers });

        let url = this.config.getUrlControlAccess('logout');
        return this.http.post(url, {}, requestOptions);
    }
}
