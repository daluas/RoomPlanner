import { Injectable } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  constructor() { }

  createNewBooking(booking: Booking): Promise<boolean>{
    console.log("createNewBooking(booking: Booking) was called!");

    // fara returnare de erori, doar true in caz de succes, ori false in caz de eroare
    return new Promise((res)=>{
      //use booking fields for http request
      
      res(true);
    });
  }
}
