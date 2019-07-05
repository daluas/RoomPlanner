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
import { SpinnerComponent } from '../spinner/spinner.component'
import { Booking } from '../../core/models/BookingModel';
import { UserType } from '../../core/enums/enums';

@Component({
  selector: 'app-booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})

export class BookingPopupComponent implements OnInit {

  @Input() booking: Booking;
  @Output() newBooking: EventEmitter<Booking> = new EventEmitter();
  @Output() updatedBooking: EventEmitter<Booking> = new EventEmitter();
  @Output() deletedBooking: EventEmitter<Booking> = new EventEmitter();
  @Output() closePopup: EventEmitter<boolean> = new EventEmitter();

  constructor(
    public bookingService: BookingService,
    private fb: FormBuilder,
    private authService: AuthService
  ) {  }

  minDate = new Date(2019, 0, 1);
  invalidHours: boolean = false;

  status: boolean = false;
  usertype: string;

  isLogged: boolean;
  isNewAction: boolean;

  statusMessage: number;

  booked: boolean = true;
  bookingStatus: boolean;
  prevalidationStatus: boolean;
  updateStatus: boolean;
  deleteStatus: boolean = true;

  statusDelete: number;
  openMessageContainter: boolean;

  isLoading: boolean;

  ngOnInit() {
    this.status = false;
    this.isNewAction = false;
    this.isLoading = false;

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
    this.booking.startDate = val.value;
    if (val.value.getDate() > this.booking.endDate.getDate()) {
      this.status = true;
      this.booked = false;
    }
    else {
      this.status = false;
      this.booked = true;
    }
  }
  endDateValidation(val) {
    this.booking.endDate = val.value;
    if (val.value.getDate() < this.booking.startDate.getDate()) {
      this.status = true;
      this.booked = false;
    }
    else {
      this.status = false;
      this.booked = true;
    }
  }
  getDescription(desc) {
    this.booking.description = desc.value;
  }
  onStartHourEmit(event) {
    this.booking.startDate.setHours(event.getHours(), event.getMinutes());
  }
  onEndHourEmit(event) {
    this.booking.endDate.setHours(event.getHours(), event.getMinutes());
  }
  createBookingTest() {
    this.isLoading = true;

    this.bookingService.createNewBooking(this.booking).then((bookingRes: Booking) => {
     
      this.booking.email=this.authService.getCurrentUser().email;
      if (bookingRes.id) { 
        this.isNewAction = true;
        this.booking.id=bookingRes.id;
        this.newBooking.emit(this.booking);
       // console.log("Booking successfully");
        this.bookingStatus = true;
        this.closeLoginPopup(true);
      }
      else {
        //console.log("Booking failed");
        this.bookingStatus = false;

      }
    }).catch(error => {
      console.log(error)
    })
  }

  updateBookingTest() {
    this.isLoading = true;

    this.bookingService.updateBooking(this.booking).subscribe(res => {
      if (res.status==200) { // la fel, verificare ca la create NEW booking
        this.isNewAction = true;
        this.updatedBooking.emit(this.booking);
        // console.log("Update successfully");
        this.updateStatus = true;
        this.closeLoginPopup(true);
      }
      else {
        
        // console.log("Update failed");
        this.updateStatus = false;

      }
    })
      // .catch(error => {
      //   console.log(error)
      // })
  }

  deleteBooking() {
    this.isLoading = true;
    
    this.bookingService.deleteBooking(this.booking).subscribe((res) => {
      this.statusDelete = res.status;
      if (this.statusDelete == 200) {
        this.isNewAction = true;
        this.deletedBooking.emit(this.booking);
        this.deleteStatus = true;
        this.closeLoginPopup(true);
        // console.log("Delete successfully");

      }
      else if (this.statusDelete == 400) {
        // console.log("Delete failed");
        this.deleteStatus = false;
      }
      // this.statusMessage.emit(this.prevalidationStatus);
    })

  }

  getStatusOfBookButton(event) { 

    this.invalidHours = event;
    if (this.invalidHours) {
      this.booked = false;
    }
    else {
      this.booked = true;
    }
  }

  getPrevalidationMessage(event) {
    this.statusMessage = event;

    if (this.statusMessage == 200) {
      this.prevalidationStatus = true;

    }
    else {
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
    if (event) {
      this.closePopup.emit(true);
    }
  }

}
