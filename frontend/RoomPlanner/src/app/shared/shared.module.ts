import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { SpinnerComponent } from './spinner/spinner.component';
import { Filters } from './models/Filters';
import { BookingPopupComponent } from './booking-popup/booking-popup.component';

@NgModule({
  declarations: [
    SpinnerComponent, 
    BookingPopupComponent
  ],
  imports: [
    CommonModule,
    MaterialDesignModule
  ],
  providers:[],
  exports:[
    SpinnerComponent,
    BookingPopupComponent
  ]
})
export class SharedModule { }
