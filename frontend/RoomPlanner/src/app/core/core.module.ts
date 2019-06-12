import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth/auth.service';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http'
import { Interceptor } from './interceptor';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RoomDataService } from './services/room-data/room-data.service';
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule,
  ],
  providers: [
    AuthService,
    HttpClient,
    RoomDataService
  ],
  exports: []
})
export class CoreModule { }
export { AuthService } from './services/auth/auth.service';