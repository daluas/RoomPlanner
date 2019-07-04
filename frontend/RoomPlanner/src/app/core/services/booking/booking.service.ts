import { Injectable } from '@angular/core';

import { HttpParams, HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { LoggedUser } from '../../models/LoggedUser';
import { Booking } from '../../models/BookingModel';
import { Observable, Subscription } from 'rxjs';
import { Config } from 'protractor';
import { AuthService } from 'src/app/core/services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private backendUrl: string = 'http://178.22.68.114:8081'; //  /RoomPlanner/ ...
  //private options = { headers: new HttpHeaders().set('Content-Type', 'application/json') };

  constructor(private httpClient: HttpClient, private authService: AuthService) { }

  prevalidation(booking: Booking): Observable<HttpResponse<Config>> {//Observable<HttpResponse<Config>>
    console.log("prevalidation was called!");
    let roomID: number = booking.roomId;
    let startDate = booking.startDate.toUTCString();
    let endDate = booking.endDate.toUTCString();
    let email = this.authService.getCurrentUser().email;
    
    // let params = new HttpParams()
    // params = params.append("roomId", `${roomID}`);
    // params = params.append("startDate", `${startDate}`);
    // params = params.append("endDate", `${endDate}`);
    // params = params.append("email", `${email}`);

    // let params = {
    //   roomID:  booking.roomId,
    //   startDate : booking.startDate.toUTCString(),
    //   endDate : booking.endDate.toUTCString(),
    //   email : this.authService.getCurrentUser().email
    

    // }
                      
    return this.httpClient.get(`${this.backendUrl}/api/prevalidation?roomId=${roomID}&startDate=${startDate}&endDate=${endDate}&email=${email}`, {
      observe: 'response'
    });
  }

  createNewBooking(booking: Booking): Promise<any> {
    console.log("createNewBooking was called!");
    let roomID: number = booking.roomId;
    let currentUser: string = this.authService.getCurrentUser().email;
    let params = {
      email: currentUser,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,

    }
    
    // let params = new HttpParams()
    // params.append('email', currentUser)
    // params.append('startDate', booking.startDate.toJSON())
    // params.append('endDate', booking.endDate.toJSON())
    // params.append('description', booking.description)
  console.log(params);

    // return this.httpClient.post(`${this.backendUrl}/api/reservations/${roomID}`, params , this.options).toPromise();
    return this.httpClient.post(`${this.backendUrl}/api/reservations/${roomID}`, params).toPromise();
  }


  updateBooking(booking: Booking): Observable<HttpResponse<Config>> {
    console.log("updateBooking was called!");
    let roomID: number = booking.roomId;
    let id: number = booking.id;
    let currentUser: string = this.authService.getCurrentUser().email;
    let params = {
      email: currentUser,
      roomId: booking.roomId,
      startDate: booking.startDate.toJSON(),
      endDate: booking.endDate.toJSON(),
      description: booking.description,

    }
    return this.httpClient.patch(`${this.backendUrl}/api/reservations/${id}`, params, { observe: 'response' });
    //status code: 200 OK or 204 NO CONTENT 
  }

  deleteBooking(booking: Booking): Observable<HttpResponse<Config>> {//
    console.log("deleteBooking(booking: Booking) was called!");
     let bookingId: number = booking.id;
    return this.httpClient.delete(`${this.backendUrl}/api/reservations?reservation=${bookingId}`, { observe: 'response' });
    //return 400;
  }

}

