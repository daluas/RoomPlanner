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
  
  //@Output() booked: EventEmitter<Booking> = new EventEmitter();
  @Output() closePopup: EventEmitter<boolean> = new EventEmitter();

  constructor(public bookingService: BookingService) {
    console.log("on constructor"+this.booking);
   }
  
  minDate = new Date(2019, 0, 1);
 
  status: boolean =false;
  booked : any;
  ngOnInit() {
   this.booked=true;
   
  }
 
  startDateValidation(val){ 
    if(val.value.getDate()> this.booking.endDate.getDate()){
      this.status=true;
    }
    else{
      this.status=false;
    }
  }


  endDateValidation(val){
    if(val.value.getDate() < this.booking.startDate.getDate()){
      this.status=true;
    }
    else{
      this.status=false;
    }
  }

  prevalidation(){

  }

  createBookingTest() {
    
    this.bookingService.createNewBooking(this.booking).then((booked) => {
      console.log(booked);
    
      if (booked) {
        // succes, rezervarea a avut loc
        this.booked.emit(this.booking);
      } else {
        // eroare, inseamna ca nu s-a putut face rezervarea
        //document.getElementById('testButton').innerHTML = "Update";
        //console.log("Booking error!");
      }

    })
    
  }

  updateBookingTest() {

  }
  deleteBooking() {
    
  }
  cancelBooking() { 
    this.booking.description="";
    this.closePopup.emit(true);
  }

  onHourEmit(event) {

    this.booking.startDate = event;

    if (this.booking.startDate.getTime() >= this.booking.endDate.getTime()) {
      //this.invalidHours = true;
    }
    else {
     // this.invalidHours = false;
    }
    
  }
  

}
