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
  isNewAction: boolean;

  statusMessage: string;  
 
  booked: boolean = true;
  bookingStatus: boolean;
  prevalidationStatus: boolean;
 

  ngOnInit() {
    this.status = false;
    this.isNewAction = false;

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
    this.booking.startDate=val.value;
    console.log('start date :'+this.booking.startDate);
    if (val.value.getDate() > this.booking.endDate.getDate()) {
      this.status = true;
    }
    else {
      this.status = false;
    }
  }
  endDateValidation(val) {
    this.booking.endDate=val.value;
    if (val.value.getDate() < this.booking.startDate.getDate()) {
      this.status = true;
    }
    else {
      this.status = false;
    }
  }
  getDescription(desc){
    this.booking.description=desc.value;
  }
  onStartHourEmit(event){
    this.booking.startDate.setHours(event.getHours(), event.getMinutes());
  }
  onEndHourEmit(event){
    this.booking.endDate.setHours(event.getHours(), event.getMinutes());
  }
  createBookingTest() {
   
    this.bookingService.createNewBooking(this.booking).then((bookingRes) => {     
      let res = JSON.parse(bookingRes);
      if (res.id) {
        this.isNewAction = true;
        this.bookingRes.emit(this.booking);
        console.log("Booking successfully");
        this.bookingStatus = true;      
      }
      else {
        //todo
        console.log("Booking failed");
      }
    })
  }  
  
  updateBookingTest() {
    this.bookingService.updateBooking(this.booking).then((bookingRes) => {     
      let res = JSON.parse(bookingRes);
      if (res.id) {
        this.isNewAction = true;
        this.bookingRes.emit(this.booking);
        console.log("Update successfully");
        this.bookingStatus = true;      
      }
      else {
        //todo
        console.log("Update failed");
      }
    })
  }

  deleteBooking() {
    this.bookingService.deleteBooking(this.booking).then((bookingRes) => {     
      let res = JSON.parse(bookingRes);
      if (res.status == "200") {
        this.isNewAction = true;
        this.bookingRes.emit(this.booking);
        console.log("Delete successfully");
        //this.bookingStatus = true;      
      }
      else if(res.status == "400") {
        //todo
        console.log("Delete failed");
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
