import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { SpinnerComponent } from './spinner/spinner.component';
import { Filters } from './models/Filters';
import { BookingPopupComponent } from './booking-popup/booking-popup.component';
import { HourInputComponent } from './hour-input/hour-input.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [
    SpinnerComponent, 
    BookingPopupComponent, 
    HourInputComponent
  ],
  imports: [
    CommonModule,
    MaterialDesignModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers:[],
  exports:[
    SpinnerComponent,
    BookingPopupComponent,
    HourInputComponent
  ]
})
export class SharedModule { }
