import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { SpinnerComponent } from './spinner/spinner.component';
import { Filters } from './models/Filters';

@NgModule({
  declarations: [SpinnerComponent],
  imports: [
    CommonModule,
    MaterialDesignModule
  ],
  providers:[],
  exports:[
    SpinnerComponent
  ]
})
export class SharedModule { }
