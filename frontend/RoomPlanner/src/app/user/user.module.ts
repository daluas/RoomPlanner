import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserViewComponent } from './user-view/user-view.component';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { FiltersComponent } from './user-view/filters/filters.component';
import { SharedModule } from '../shared/shared.module';
import { RoomsViewComponent } from './user-view/rooms-view/rooms-view.component';

@NgModule({
  declarations: [
    UserViewComponent, 
    FiltersComponent,
    RoomsViewComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    MaterialDesignModule,
    SharedModule
  ]
})
export class UserModule { }
