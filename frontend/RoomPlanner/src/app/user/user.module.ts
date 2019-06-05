import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserViewComponent } from './user-view/user-view.component';

@NgModule({
  declarations: [UserViewComponent],
  imports: [
    CommonModule,
    UserRoutingModule
  ]
})
export class UserModule { }
