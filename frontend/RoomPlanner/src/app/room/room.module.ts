import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoomRoutingModule } from './room-routing.module';
import { RoomViewComponent } from './room-view/room-view.component';
import { ClockComponent } from './room-view/clock/clock.component';
import { ClockTwoComponent } from './room-view/clock-two/clock-two.component'

@NgModule({
  declarations: [RoomViewComponent, ClockComponent, ClockTwoComponent],
  imports: [
    CommonModule,
    RoomRoutingModule
  ]
})
export class RoomModule { }
