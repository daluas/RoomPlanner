import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserViewComponent } from './user-view/user-view.component';
import { MaterialDesignModule } from '../material-design/material-design.module';

@NgModule({
  declarations: [UserViewComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    MaterialDesignModule
  ]
})
export class UserModule { }
