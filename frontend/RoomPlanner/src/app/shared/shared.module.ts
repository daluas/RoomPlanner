import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { UserdataService } from './services/userdata.service';
import { Filters } from './models/Filters';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialDesignModule
    
  ],
  providers:[UserdataService],
  exports:[]
})
export class SharedModule { }
