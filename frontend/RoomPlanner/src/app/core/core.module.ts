import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth/auth.service';
import { HttpClientModule, HttpClient } from '@angular/common/http'
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [AuthService, HttpClient],
  exports: []
})
export class CoreModule { }
export { AuthService } from './services/auth/auth.service';