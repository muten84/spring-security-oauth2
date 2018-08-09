import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AlertService } from './_services/index';
import { AuthService } from '../../service/auth.service';

@Component({
    moduleId: module.id.toString(),
    templateUrl: 'login.component.html',
    styleUrls: ['login.component.css']
})
export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;
    returnUrl: string;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authService: AuthService,
        private alertService: AlertService) { }

    ngOnInit() {
        // console.log('sono in ngOnInit di login component');
        // reset login status

        this.authService.logout().toPromise().then(
            (response)=>{
                sessionStorage.removeItem('currentToken');
            }, 
            (failure)=>{
                sessionStorage.removeItem('currentToken');
            }
        );

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    login() {
        sessionStorage.removeItem('currentToken');
        this.loading = true;
        this.authService.login(this.model.username, this.model.password)
            .subscribe(
                data => {
                    this.router.navigate([this.returnUrl]);
                },
                error => {
                    let message = 'Autenticazione fallita';
                    if (error && error.message) {
                        message = error.message;
                    }
                    this.alertService.error(message);
                    this.loading = false;
                });
    }
}
