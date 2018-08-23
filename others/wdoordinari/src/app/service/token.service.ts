import { Injectable } from '@angular/core';
//import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { getUserName } from '../util/jwt'

@Injectable()
export class TokenService {

    constructor() { }

    public getUserName(): string {
        return getUserName('currentToken');
    }
}