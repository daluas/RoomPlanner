import { Component, OnInit } from '@angular/core';
import { Booking } from 'src/app/core/models/BookingModel';
import { TimeInterval } from './models/timeInterval';

@Component({
  selector: 'app-room-view',
  templateUrl: './room-view.component.html',
  styleUrls: ['./room-view.component.css']
})
export class RoomViewComponent implements OnInit {

  isClockActive: boolean = false;
  reservations: Booking[];

  bookingPopupOpen: boolean = false;
  booking: Booking;

  displayGoodClock = false;

  date: Date = new Date();

  constructor() { }

  ngOnInit() {

    // DELETE THIS
    this.reservations = [
      new Booking().create({
        "id": 4,
        "roomId": 2,
        "personEmail": "sghitun@yahoo.com",
        "description": "Retro meeting",
        "startDate": new Date(new Date().setHours(7, 0, 0, 0)),
        "endDate": new Date(new Date().setHours(9, 30, 0, 0)),
      }),

      new Booking().create({
        "id": 5,
        "roomId": 2,
        "personEmail": "sghitun@yahoo.com",
        "description": "Retro meeting",
        "startDate": new Date(new Date().setHours(11, 0, 0, 0)),
        "endDate": new Date(new Date().setHours(14, 30, 0, 0)),
      }),

      new Booking().create({
        "id": 6,
        "roomId": 2,
        "personEmail": "sghitun@yahoo.com",
        "description": "Retro meeting",
        "startDate": new Date(new Date().setHours(20, 0, 0, 0)),
        "endDate": new Date(new Date().setHours(23, 30, 0, 0)),
      }),

      // {
      //   "id": 4,
      //   "roomId": 2,
      //   "personEmail": "sghitun@yahoo.com",
      //   "startDate": "2019-06-28T09:00:00.000+0000",
      //   "endDate": "2019-06-28T12:00:00.000+0000",
      //   "description": "Retro meeting"
      // },
      // {
      //   "id": 5,
      //   "roomId": 2,
      //   "personEmail": "sghitun@yahoo.com",
      //   "startDate": "2019-06-28T14:00:00.000+0000",
      //   "endDate": "2019-06-28T15:30:00.000+0000",
      //   "description": "Retro meeting"
      // }
    ]

  }

  from: string;
  to: string;
  interval: TimeInterval

  processNewInterval(interval: TimeInterval){
    this.interval = interval;
    if(interval){
      let from = new Date(interval.startDate);
      let to = new Date(interval.endDate);

      this.from = (from.getHours() < 10 ? '0' + from.getHours() : from.getHours()) + ":" + (from.getMinutes() < 10 ? '0' + from.getMinutes() : from.getMinutes());
      this.to = (to.getHours() < 10 ? '0' + to.getHours() : to.getHours()) + ":" + (to.getMinutes() < 10 ? '0' + to.getMinutes() : to.getMinutes());
    } else {
      this.from = "--:--"
      this.to = "--:--"
    }
  }

  cancelBooking(){
    this.isClockActive = false;
  }

  createBooking(){
    this.bookingPopupOpen = true;
    this.booking = new Booking().create({
      "personEmail": "sghitun@yahoo.com",
      "startDate": new Date(this.interval.startDate),
      "endDate": new Date(this.interval.endDate),
    })
  }

  addCreatedBooking(booking: any) {
    // add to the list of bookings of the booked room the new booking 
    console.log("Rooms list must be updated with: ", booking);
    // call setDisplayedRooms(this.previousFilters); after setting this.rooms with new booking

    this.closeBookingPopup();

    this.reservations.push(booking);

    this.reservations = [...this.reservations];
  }

  closeBookingPopup() {
    this.bookingPopupOpen = false;
  }
}
