import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { BookingPopupComponent } from '../../../shared/booking-popup/booking-popup.component';

@Component({
  selector: 'app-rooms-view',
  templateUrl: './rooms-view.component.html',
  styleUrls: ['./rooms-view.component.css']
})
export class RoomsViewComponent implements OnInit {

  @Input() rooms: any;
  @Output() createBooking: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  bookRoomTest(){
    this.createBooking.emit(new Booking().create({
      startDate: new Date(new Date(2019,6,9).setHours(12, 30, 0, 0)),
      endDate: new Date(new Date(2019,6,10).setHours(15, 0, 0, 0)),
      roomId: 1,
       id:1,
      description: "This is a test description"
    }));
  }

}
