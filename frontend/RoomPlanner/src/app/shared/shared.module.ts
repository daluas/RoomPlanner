import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { UserdataService } from './services/userdata.service';
import { SpinnerComponent } from './spinner/spinner.component';
import { Filters } from './models/Filters';

@NgModule({
  declarations: [SpinnerComponent],
  imports: [
    CommonModule,
    MaterialDesignModule
    
  ],
  providers:[UserdataService],
  exports:[
    SpinnerComponent
  ]
})
export class SharedModule { }
