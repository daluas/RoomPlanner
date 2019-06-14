import { Component, OnInit, Input, ɵConsole, Output, EventEmitter } from '@angular/core';
import { Booking } from '../models/Booking';
import { BookingService } from 'src/app/core/services/booking/booking.service';

@Component({
  selector: 'app-booking-popup',
  templateUrl: './booking-popup.component.html',
  styleUrls: ['./booking-popup.component.css']
})
export class BookingPopupComponent implements OnInit {

  @Input() booking: Booking;
  @Output() booked: EventEmitter<Booking> = new EventEmitter();

  constructor(public bookingService: BookingService) { }

  ngOnInit() { }

  createBookingTest() {
    let booking = new Booking().create({
      // complete all fields with valid data
    });

    this.bookingService.createNewBooking(booking).then((booked) => {

      if (booked) {
        // succes, rezervarea a avut loc
        document.getElementById('testButton').innerHTML = "SUCCESS!";

        // IMPORTANT!
        this.booked.emit(booking);
      } else {
        // eroare, inseamna ca nu s-a putut face rezervarea
        document.getElementById('testButton').innerHTML = "ERROR!";
      }

    })
  }
}
