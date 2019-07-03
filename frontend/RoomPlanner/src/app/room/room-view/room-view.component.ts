import { Component, OnInit } from '@angular/core';
import { Booking } from 'src/app/core/models/BookingModel';

@Component({
  selector: 'app-room-view',
  templateUrl: './room-view.component.html',
  styleUrls: ['./room-view.component.css']
})
export class RoomViewComponent implements OnInit {

  isClockActive: boolean = true;
  reservations: Booking[];

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

}
