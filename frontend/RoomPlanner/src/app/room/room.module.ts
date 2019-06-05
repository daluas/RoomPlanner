import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoomRoutingModule } from './room-routing.module';
import { RoomViewComponent } from './room-view/room-view.component';

@NgModule({
  declarations: [RoomViewComponent],
  imports: [
    CommonModule,
    RoomRoutingModule
  ]
})
export class RoomModule { }
