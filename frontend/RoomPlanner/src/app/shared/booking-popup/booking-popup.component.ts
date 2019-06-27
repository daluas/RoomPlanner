import { Component, OnInit, Input, ɵConsole, Output, EventEmitter, Inject } from '@angular/core';
import { BookingService } from 'src/app/core/services/booking/booking.service';
import { RoomsViewComponent } from '../../user/user-view/rooms-view/rooms-view.component';
import { Data } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Booking } from '../../core/models/BookingModel';
import { Time } from '@angular/common';

@Component({
  selector: 'app-booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})
export class BookingPopupComponent implements OnInit {


  @Input() booking: Booking;

  @Output() bookingRes: EventEmitter<Booking> = new EventEmitter();
  @Output() closePopup: EventEmitter<boolean> = new EventEmitter();

  statusMessage: string;
  constructor(public bookingService: BookingService) {

  }

  minDate = new Date(2019, 0, 1);
  status: boolean = false;
  booked: boolean = true;
  bookingStatus: boolean;
  prevalidationStatus: boolean;
  public invalidHours: boolean;
  ngOnInit() {

  }
  
  startDateValidation(val) {
    if (val.value.getDate() > this.booking.endDate.getDate()) {
      this.status = true;
    }
    else {
      this.status = false;
    }
  }
  endDateValidation(val) {
    if (val.value.getDate() < this.booking.startDate.getDate()) {
      this.status = true;
    }
    else {
      this.status = false;
    }
  }
  createBookingTest() {

    this.bookingService.createNewBooking(this.booking).then((bookingRes) => {
     
      let res = JSON.parse(bookingRes);

      if (res.id) {
        this.bookingRes.emit(this.booking);
        console.log("Booking successfully");
        this.bookingStatus = true;
      } else {
        console.log("Booking failed");
        this.bookingStatus = false;
      }

    })

  }
 
  
  getStatusOfBookButton(event) { //true=disabled fals=enabled

    this.invalidHours=event;
    if(this.invalidHours){
      this.booked=false;
    }
    else{
      this.booked=true;
    }
   
  }
 getPrevalidationMessage(event) {
    this.statusMessage = event;
    
    if (this.statusMessage == "You can book" ) {
      this.prevalidationStatus = true;
 
    }
    else  {
      this.prevalidationStatus = false;
      this.booked = false;
    }
  }
  updateBookingTest() {

  }
  deleteBooking() {

  }
  cancelBooking() {
    this.booking.description = "";
    this.closePopup.emit(true);
  }

  onHourEmit(event) {

    this.booking.startDate = event;

    if (this.booking.startDate.getTime() >= this.booking.endDate.getTime()) {
      this.invalidHours = true;
    }
    else {
       this.invalidHours = false;
    }

  }


}
