import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth/auth.service';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http'
import { Interceptor } from './interceptor';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    AuthService,
    HttpClient
  ],
  exports: []
})
export class CoreModule { }
export { AuthService } from './services/auth/auth.service';