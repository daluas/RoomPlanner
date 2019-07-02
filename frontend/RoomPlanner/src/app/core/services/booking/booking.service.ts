import { Injectable } from '@angular/core';

import { HttpParams, HttpClient, HttpResponse } from '@angular/common/http';
import { LoggedUser } from '../../models/LoggedUser';
import { Booking } from '../../models/BookingModel';
import { Observable, Subscription } from 'rxjs';
import { Config } from 'protractor';
import { AuthService } from 'src/app/core/services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private backendUrl: string = 'http://178.22.68.114/RoomPlanner/api';

  constructor(private httpClient: HttpClient, private authService : AuthService) { }

  prevalidation(booking: Booking): Observable<HttpResponse<Config>> {//Observable<HttpResponse<Config>>
    console.log("prevalidation was called!");
    let roomID: number = booking.roomId;
    let startDate = booking.startDate.toUTCString();
    let endDate = booking.endDate.toUTCString();
    let email = booking.personalEmail;
    
   // return this.httpClient.get(`${this.backendUrl}/api/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${startDate}&email=${email}`).toPromise();
   
    //ok!
    return this.httpClient.get(`${this.backendUrl}/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${startDate}&email=${email}`, {observe: 'response'});                  
   // return 200;
   
   
  }

  createNewBooking(booking: Booking): Promise<any> {
    console.log("createNewBooking was called!");
    let roomID: number = booking.roomId;
    let currentUser : string=this.authService.getCurrentUser().email;
   
    let params = {
      email: currentUser,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,
    }
  
     return this.httpClient.post(`${this.backendUrl}/reservations/${roomID}`, params).toPromise();

    //mock
    // return new Promise((res) => {
    //   // caz de succes

    //   res(`{
    //     "id": 65,
    //     "roomId": 2,
    //     "email": "sghitun@yahoo.com",
    //     "startDate": "2019-06-24T13:02:41.116+0000",
    //     "endDate": "2019-06-24T13:02:41.116+0000",
    //     "description": "ok"
    //     }`);

    //   // caz de eroare

    //   //res(`{}`);

    // });
  }


  updateBooking(booking: Booking): Promise<any> {
    console.log("updateBooking was called!");
    let roomID: number = booking.roomId;
    let id : number = booking.id;
    let currentUser : string=this.authService.getCurrentUser().email;
    let params = {
      email: currentUser,
      roomId :booking.roomId,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,
     
    }
    console.log(params.startDate);
    return this.httpClient.patch(`${this.backendUrl}/update-reservations/${id}`, params).toPromise();
    //status code: 200 OK or 204 NO CONTENT 

    // return new Promise((res) => {
    //   // caz de succes

    //   // res(`{
    //   //   "id": 65,
    //   //   "roomId": 2,
    //   //   "email": "sghitun@yahoo.com",
    //   //   "startDate": "2019-06-24T13:02:41.116+0000",
    //   //   "endDate": "2019-06-24T13:02:41.116+0000",
    //   //   "description": "ok"
    //   //   }`);

    //   // caz de eroare

    //   res(`{}`);

   // });
  }

  deleteBooking(booking: Booking): Observable<HttpResponse<Config>> {//
    console.log("deleteBooking(booking: Booking) was called!");
    let bookingId: number = booking.id;
    return this.httpClient.delete(`${this.backendUrl}/reservations?/reservations=${bookingId}`, {observe: 'response'});
    //return 400;
  }

}

