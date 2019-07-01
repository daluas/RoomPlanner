import { Injectable } from '@angular/core';

import { HttpParams, HttpClient, HttpResponse } from '@angular/common/http';
import { LoggedUser } from '../../models/LoggedUser';
import { Booking } from '../../models/BookingModel';
import { Observable, Subscription } from 'rxjs';
import { Config } from 'protractor';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private backendUrl: string = 'http://178.22.68.114/RoomPlanner';

  constructor(private httpClient: HttpClient) { }

  prevalidation(booking: Booking): number {//Observable<HttpResponse<Config>>
    console.log("prevalidation was called!");
    let roomID: number = booking.roomId;
    let startDate = booking.startDate.toUTCString();
    let endDate = booking.endDate.toUTCString();
    let email = booking.personalEmail;
    
   // return this.httpClient.get(`${this.backendUrl}/api/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${startDate}&email=${email}`).toPromise();
   
    //ok!
   // return this.httpClient.get(`${this.backendUrl}/api/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${startDate}&email=${email}`, {observe: 'response'});                  
    return 200;
   
   
  }

  createNewBooking(booking: Booking): Promise<string> {
    console.log("createNewBooking was called!");
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

      // res(`{
      //   "id": 65,
      //   "roomId": 2,
      //   "email": "sghitun@yahoo.com",
      //   "startDate": "2019-06-24T13:02:41.116+0000",
      //   "endDate": "2019-06-24T13:02:41.116+0000",
      //   "description": "ok"
      //   }`);

      // caz de eroare

      res(`{}`);

    });
  }


  updateBooking(booking: Booking): Promise<string> {
    console.log("updateBooking was called!");
    let roomID: number = booking.roomId;
    let id : number = booking.id;
    let params = {
      email: booking.personalEmail,
      roomId :booking.roomId,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,
     
    }

    //return this.httpClient.patch(`${this.backendUrl}/api/update-reservations/${id}`, params).toPromise();
    //status code: 200 OK or 204 NO CONTENT 

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

  deleteBooking(booking: Booking): number {//Observable<HttpResponse<Config>>
    console.log("deleteBooking(booking: Booking) was called!");
    let roomID: number = booking.roomId;

    let params = {
      email: booking.personalEmail,
      id: booking.id
    }

    //return this.httpClient.delete(`${this.backendUrl}/api/reservations?/reservations=${roomID}`, params).toPromise();

    return 200;
  }

}

