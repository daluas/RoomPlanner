import { Injectable } from '@angular/core';

import { HttpParams, HttpClient } from '@angular/common/http';
import { LoggedUser } from '../../models/LoggedUser';
import { Booking } from '../../models/BookingModel';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private backendUrl: string ='http://178.22.68.114/RoomPlanner';

  constructor(private httpClient: HttpClient) { }

  createNewBooking(booking: Booking): Promise<Object> {
    console.log("createNewBooking(booking: Booking) was called!");
    let roomID: number = booking.roomId;

    let params = {
      email: booking.personalEmail,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,
    }

    return this.httpClient.post(`${this.backendUrl}/api/reservations/${roomID}`, params).toPromise();
  }

  prevalidation(booking: Booking): Promise<Object> {
    console.log("prevalidation(booking: Booking) was called!");
    let roomID: number = booking.roomId;
    let startDate= booking.startDate.toUTCString();
    let endDate= booking.endDate.toUTCString();
    let email= booking.personalEmail;
    
    return this.httpClient.get(`${this.backendUrl}/api/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${startDate}&email=${email}`).toPromise();
  }
}

