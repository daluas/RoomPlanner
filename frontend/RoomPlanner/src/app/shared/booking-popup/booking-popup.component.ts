import { Component, OnInit, Input, ɵConsole, Output, EventEmitter, Inject, ViewChild, ElementRef } from '@angular/core';
import { BookingService } from 'src/app/core/services/booking/booking.service';
import { RoomsViewComponent } from '../../user/user-view/rooms-view/rooms-view.component';
import { Data } from '@angular/router';
import { FormControl, Validators, FormBuilder } from '@angular/forms';
import { Time } from '@angular/common';
import { LoginModel } from '../../core/models/LoginModel';
import { AuthService } from '../../core/services/auth/auth.service';
import { LoggedUser } from '../../core/models/LoggedUser';
import { LoginBookingComponent } from '../login-booking/login-booking.component'
import { Booking } from '../../core/models/BookingModel';
import { UserType } from '../../core/enums/enums';

@Component({
  selector: 'app-booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})

export class BookingPopupComponent implements OnInit {

  @Input() booking: Booking;

  @Output() bookingRes: EventEmitter<Booking> = new EventEmitter();
  @Output() closePopup: EventEmitter<boolean> = new EventEmitter();


  constructor(public bookingService: BookingService, private fb: FormBuilder, private authService: AuthService) {
    console.log("on constructor" + this.booking);
  }

  minDate = new Date(2019, 0, 1);
  invalidHours: boolean = false;

  status: boolean = false;
  usertype: string;

  isLogged: boolean;
  isNewBooking: boolean;

  statusMessage: string;  
 
  booked: boolean = true;
  bookingStatus: boolean;
  prevalidationStatus: boolean;
 

  ngOnInit() {
    this.status = false;
    this.isNewBooking = false;

    let userData = JSON.parse(localStorage.getItem("user-data"));

    this.usertype = userData.type;
    //this.usertype = "ROOM";

    if (this.usertype == UserType.ROOM) {
      this.isLogged = false;
    }

    if (this.usertype == UserType.PERSON || this.usertype == UserType.ADMIN) {
      this.isLogged = true;
    }
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
        this.isNewBooking = true;
        this.bookingRes.emit(this.booking);
        console.log("Booking successfully");
        this.bookingStatus = true;
      
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

  getErrorForDate() {
    if (this.booking.startDate > this.booking.endDate) {
      this.status = true;
    }
  }

  loggedChange(event) {
    this.isLogged = event;
  }

  closeLoginPopup(event) {
    if(event) {
      this.closePopup.emit(true);
    }
  }

}
