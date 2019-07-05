import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoomRoutingModule } from './room-routing.module';
import { RoomViewComponent } from './room-view/room-view.component';
import { ClockComponent } from './room-view/clock/clock.component';
import { ClockTwoComponent } from './room-view/clock-two/clock-two.component';
import { ClockThreeComponent } from './room-view/clock-three/clock-three.component'
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [RoomViewComponent, ClockComponent, ClockTwoComponent, ClockThreeComponent],
  imports: [
    CommonModule,
    RoomRoutingModule,
    SharedModule
  ]
})
export class RoomModule { }
