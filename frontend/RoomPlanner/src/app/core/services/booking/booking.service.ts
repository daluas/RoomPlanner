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

    // fara returnare de erori, doar true in caz de succes, ori false in caz de eroare
    return new Promise((res)=>{
      //use booking fields for http request
      
      res(true);
    });
  }
}

