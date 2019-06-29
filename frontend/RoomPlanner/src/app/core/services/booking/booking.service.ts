import { Injectable } from '@angular/core';

import { HttpParams, HttpClient } from '@angular/common/http';
import { LoggedUser } from '../../models/LoggedUser';
import { Booking } from '../../models/BookingModel';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private backendUrl: string = 'http://178.22.68.114/RoomPlanner';

  constructor(private httpClient: HttpClient) { }

  prevalidation(booking: Booking): Promise<string> {
    //console.log("prevalidation(booking: Booking) was called!");
    let roomID: number = booking.roomId;
    let startDate = booking.startDate.toUTCString();
    let endDate = booking.endDate.toUTCString();
    let email = booking.personalEmail;

    // return this.httpClient.get(`${this.backendUrl}/api/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${startDate}&email=${email}`).toPromise();

    return new Promise((res) => {
      // caz de succes
      res("You can book");

      // caz de eroare
      //res("Invalid parameters");

      // caz de eroare
      //res("The date is not available");

    });
  }

  createNewBooking(booking: Booking): Promise<string> {
    console.log("createNewBooking(booking: Booking) was called!");
    let roomID: number = booking.roomId;

    let params = {
      email: booking.personalEmail,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,
    }

    // return this.httpClient.post(`${this.backendUrl}/api/reservations/${roomID}`, params).toPromise();

    return new Promise((res) => {
      // caz de succes

      res(`{
        "id": 65,
        "roomId": 2,
        "email": "sghitun@yahoo.com",
        "startDate": "2019-06-24T13:02:41.116+0000",
        "endDate": "2019-06-24T13:02:41.116+0000",
        "description": "ok"
        }`);

      // caz de eroare

      //res(`{}`);

    });
  }


  updateBooking(booking: Booking): Promise<string> {
    console.log("updateBooking(booking: Booking) was called!");
    let roomID: number = booking.roomId;

    let params = {
      email: booking.personalEmail,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,
    }

    // return this.httpClient.post(`${this.backendUrl}/api/update-reservations/${roomID}`, params).toPromise();

    return new Promise((res) => {
      // caz de succes

      res(`{
        "id": 65,
        "roomId": 2,
        "email": "sghitun@yahoo.com",
        "startDate": "2019-06-24T13:02:41.116+0000",
        "endDate": "2019-06-24T13:02:41.116+0000",
        "description": "ok"
        }`);

      // caz de eroare

      //res(`{}`);

    });
  }

  deleteBooking(booking: Booking): Promise<string> {
    console.log("deleteBooking(booking: Booking) was called!");
    let roomID: number = booking.roomId;

    let params = {
      email: booking.personalEmail,
      id: booking.id
    }

    // return this.httpClient.post(`${this.backendUrl}/api/delete-reservations/${roomID}`, params).toPromise();

    return new Promise((res) => {
      // caz de succes

      res(`{
        "status": 200
        }`);

      // caz de eroare

      // res(`{
      //   "status": 400
      //   }`);

    });
  }

}

