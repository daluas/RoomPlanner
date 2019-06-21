import { Component, OnInit, Input, ɵConsole, Output, EventEmitter, Inject } from '@angular/core';
import { Booking } from '../models/Booking';
import { BookingService } from 'src/app/core/services/booking/booking.service';
import { RoomsViewComponent } from '../../user/user-view/rooms-view/rooms-view.component';
import { Data } from '@angular/router';
import { FormControl } from '@angular/forms';
import { UserViewComponent } from 'use'
@Component({
  selector: 'app-booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css'] 
})
export class BookingPopupComponent implements OnInit {

  @Input() booking: Booking;
  @Output() booked: EventEmitter<Booking> = new EventEmitter();
 // @Input() closeBookingPopup: closeBookingPopup = new this.closeBookingPopup();
  constructor(public bookingService: BookingService) { }

  ngOnInit() {
    
    this.booking = new Booking().create({
       startDate: new Date() ,
       endDate: new Date()
    });
   
  }

  createBookingTest() {
    this.bookingService.createNewBooking(this.booking).then((booked) => {

      if (booked) {
        // succes, rezervarea a avut loc
        //document.getElementById('testButton').innerHTML = "Book now";

        // IMPORTANT!
        this.booked.emit(this.booking);
      } else {
        // eroare, inseamna ca nu s-a putut face rezervarea
        //document.getElementById('testButton').innerHTML = "Update";
      }

    })
    console.log(this.booking);
  }
  // getDate(mydate: Data): string {
  //   let day = mydate.getDay();
  //   let month = mydate.getMonth();
  //   let year = mydate.getFullYear();
  //   let resultDate = day + "/" + month + "/" + year;
  //   return resultDate;
  // }
  getTime( mytime: Data):string{
    let hour=mytime.getHours();
    let minutes=mytime.getMinutes();
    if(minutes>=0 && minutes<=9){
      minutes="0"+minutes;
    }
    let resultTime=hour+":"+minutes;
    return resultTime;
  }
  updateBookingTest(){

  }
  deleteBooking(){
    
  }
  closePopup(){

  }
}
