import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserViewComponent } from './user-view/user-view.component';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { FiltersComponent } from './user-view/filters/filters.component';

@NgModule({
  declarations: [UserViewComponent, FiltersComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    MaterialDesignModule
  ]
})
export class UserModule { }
