import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//import { fakeBackendProvider } from './_helpers/index';
//import { MockBackend, MockConnection } from '@angular/http/testing';
//import { BaseRequestOptions } from '@angular/http';

import { AlertComponent } from './_directives/index';
//import { AuthGuard } from './_guards/index';
/*import { AlertService, AuthenticationService, UserService } from './_services/index';*/
import { LoginComponent } from './index';
//import { RegisterComponent } from './register/index';
import { AlertService, UserService } from './_services/index';
import { AuthService } from '../../service/auth.service';

import { InputMaskDirective } from '../directives/inputmask.directive';

@NgModule({
  exports: [AlertComponent, LoginComponent, InputMaskDirective],
  imports: [CommonModule, FormsModule],
  declarations: [AlertComponent, LoginComponent, InputMaskDirective],
  providers: [AlertService, AuthService, UserService]
})
export class LoginModule {
}

