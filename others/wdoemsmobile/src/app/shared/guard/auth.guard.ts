import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Router } from '@angular/router';
import { StorageService } from '../../service/service.module';

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private router: Router, private storage: StorageService) { }

    canActivate() {
        if (this.storage.getFromLocal('siteInfo')) {
            return true;
        }

        this.router.navigate(['/login']);
        return false;
    }
}
