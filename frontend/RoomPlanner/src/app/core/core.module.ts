import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth/auth.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [AuthService],
  exports: []
})
export class CoreModule { }
export { AuthService } from './services/auth/auth.service';