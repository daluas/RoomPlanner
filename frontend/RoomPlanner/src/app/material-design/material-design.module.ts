import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import { MatDatepickerModule, MatNativeDateModule  } from '@angular/material';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule 

  ],
  exports:[
    MatToolbarModule,
    MatButtonModule,
    MatDatepickerModule
  ]
})
export class MaterialDesignModule { }
