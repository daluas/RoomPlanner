import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { RoomViewComponent } from './room-view/room-view.component';

const roomRoutes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: RoomViewComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomRoutes)],
  exports: [RouterModule]
})
export class RoomRoutingModule { }
