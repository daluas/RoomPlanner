import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth/auth.service';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http'
import { RoomDataService } from './services/room-data/room-data.service';
import { BookingService } from './services/booking/booking.service';
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule,
  ],
  providers: [
    AuthService,
    HttpClient,
    RoomDataService,
    BookingService
  ],
  exports: []
})
export class CoreModule { }
export { AuthService } from './services/auth/auth.service';