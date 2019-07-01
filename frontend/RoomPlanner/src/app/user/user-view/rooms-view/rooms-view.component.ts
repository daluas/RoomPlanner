import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit, OnChanges, SimpleChanges } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { BookingPopupComponent } from '../../../shared/booking-popup/booking-popup.component';
import { DomSanitizer, SafeStyle } from '@angular/platform-browser';
import { AuthService } from 'src/app/core/core.module';
import { LoggedUser } from 'src/app/core/models/LoggedUser';
import { Booking } from '../../../core/models/BookingModel';

@Component({
  selector: 'app-rooms-view',
  templateUrl: './rooms-view.component.html',
  styleUrls: ['./rooms-view.component.css']
})
export class RoomsViewComponent implements OnInit, AfterViewInit, OnChanges {

  @Input('rooms') rooms: any;
  @Input() forDate: Date;

  @Output() createBooking: EventEmitter<any> = new EventEmitter();

  intervals: string[];
  currentUser: LoggedUser;
  styleForPastTime: SafeStyle;

  newBookingStartDate: Date;
  newBookingEndDate: Date;
  newBookingStyle: SafeStyle;
  newBookingRoomIndex: number;
  personalBookingsDisplayedHalf: boolean;

  newBookingArrowsStartDate: Date;
  newBookingArrowsEndDate: Date;
  newBookingArrowsStyle: SafeStyle;

  groupedBookings: any[][];

  constructor(private sanitizer: DomSanitizer, private authService: AuthService) { }

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();

    this.setCalendarIntervals();

    this.setStyleForPastTime();
    setInterval(() => { this.setStyleForPastTime(); }, 60 * 1000);

    this.groupeBookings();
  }

  ngAfterViewInit() {
    this.scrollCalendarToEightOClock();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.setStyleForPastTime();

    this.resetNewBooking();

    this.groupeBookings();
  }

  setCalendarIntervals() {
    this.intervals = [];
    let time: string;
    for (let i = 0; i < 24; i++) {
      time = i < 10 ? '0' + i : i.toString();
      this.intervals.push(time + ".00");
      this.intervals.push(time + ".30");
    }

    this.intervals.push("00.00");
  }

  groupeBookings() {
    console.log(this.rooms);
    if (this.rooms) {
      this.groupedBookings = [];

      this.rooms.forEach(room => {

        let group = []
        room.reservations.forEach(booking => {
          booking.startDate = new Date(booking.startDate);
          booking.endDate = new Date(booking.endDate);

          group.push({
            startDate: booking.startDate,
            endDate: booking.endDate,
            personal: this.isBookingCreatedByCurrentUser(booking),
            focus: 0,
            bookings: [booking]
          });
        });

        for (let i = 0; i < group.length - 1; i++) {
          for (let x = 0; x < group[i].bookings.length; x++) {
            for (let j = i + 1; j < group.length; j++) {
              if (this.bookingsOverlap(group[i].bookings[x], group[j].bookings[0])) {
                group[i].bookings.push(group[j].bookings[0]);
                group[i].focus = null;

                if (group[i].startDate.getTime() > group[j].bookings[0].startDate.getTime()) {
                  group[i].startDate = group[j].bookings[0].startDate;
                }

                if (group[i].endDate.getTime() < group[j].bookings[0].endDate.getTime()) {
                  group[i].endDate = group[j].bookings[0].endDate;
                }

                group.splice(j, 1);
                j--;
              }
            }
          }
        }

        this.groupedBookings.push(group);

      });
    }
  }

  bookRoom() {
    this.createBooking.emit(new Booking().create({
      startDate: this.newBookingStartDate,
      endDate: this.newBookingEndDate,
      roomId: this.rooms[this.newBookingRoomIndex].id
    }));
  }

  openBooking(canOpen, roomIndex, groupIndex, bookingIndex) {
    if (canOpen) {
      let booking = new Booking().create(this.groupedBookings[roomIndex][groupIndex].bookings[bookingIndex]);
      booking.roomId = this.rooms[roomIndex].id;
      this.createBooking.emit(booking);
    }
  }

  scrollCalendarToEightOClock() {
    if (window.innerWidth > 1024) {
      document.getElementById('room-panel').scrollBy(0, 800);
    }
  }

  getStyleForBooking(booking: any): SafeStyle {
    let startHour = booking.startDate.getHours();
    let startMinute = booking.startDate.getMinutes();

    let endHour = booking.endDate.getHours() === 0 ? 24 : booking.endDate.getHours();
    let endMinute = booking.endDate.getMinutes();

    let top = startHour * 100 + startMinute / 30 * 50 + 10;
    let height = (endHour - startHour) * 100 + (endMinute - startMinute) / 30 * 50;

    let style = `top: ${top}px; height: ${height}px`;

    return this.sanitizer.bypassSecurityTrustStyle(style);
  }

  getStyleForGroup(group): SafeStyle {
    if (group) {
      let startHour = group.startDate.getHours();
      let startMinute = group.startDate.getMinutes();

      let endHour = group.endDate.getHours() === 0 ? 24 : group.endDate.getHours();
      let endMinute = group.endDate.getMinutes();

      let top = startHour * 100 + startMinute / 30 * 50 + 10;
      let height = (endHour - startHour) * 100 + (endMinute - startMinute) / 30 * 50;

      let style = `top: ${top}px; height: ${height}px`;

      return this.sanitizer.bypassSecurityTrustStyle(style);
    }

    return null;
  }

  getBookingColorClass(booking: any) {
    return {
      "booking-other": !this.isBookingCreatedByCurrentUser(booking),
      "booking-personal": this.isBookingCreatedByCurrentUser(booking),
    }
  }

  isBookingCreatedByCurrentUser(booking) {
    return booking.personEmail === this.currentUser.email;
  }

  getUsernameFromEmail(email): string {
    return email.split('@')[0];
  }

  setStyleForPastTime() {
    if (this.forDate) {
      // let forDate = new Date(this.forDate.setHours(0, 0, 0, 0)).getTime();
      let today = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
      let style = "height: 0px";

      if (this.forDate.getTime() < today) {
        style = "height: 2400px";
      }

      if (this.forDate.getTime() === today) {
        let now = new Date();
        style = `height: ${now.getHours() * 100 + now.getMinutes() * 10 / 6}px`
      }

      this.styleForPastTime = this.sanitizer.bypassSecurityTrustStyle(style);
    }
  }

  newBooking(roomIndex, intervalIndex) {
    //let forDate = new Date(this.forDate.setHours(0, 0, 0, 0)).getTime();
    let today = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
    let nowIntervals = new Date().getHours() * 2 + (new Date().getMinutes() < 30 ? 0 : 1);

    if (intervalIndex < 48 && (this.forDate.getTime() > today || this.forDate.getTime()  === today && intervalIndex >= nowIntervals)) {
      this.newBookingRoomIndex = roomIndex;
      this.personalBookingsDisplayedHalf = this.shouldBeDisplayedHalf();

      let newDate = new Date(this.forDate.getTime());

      this.newBookingStartDate = new Date(newDate.setHours(Math.floor(intervalIndex / 2), Math.floor(30 * (intervalIndex % 2)), 0, 0));
      this.newBookingEndDate = new Date(newDate.setHours(Math.floor((intervalIndex + 1) / 2), Math.floor(30 * ((intervalIndex + 1) % 2)), 0, 0));
      this.newBookingStyle = this.getStyleForBooking({
        startDate: this.newBookingStartDate,
        endDate: this.newBookingEndDate
      });

      this.newBookingArrowsStartDate = new Date(newDate.setHours(Math.floor(intervalIndex / 2), Math.floor(30 * (intervalIndex % 2)), 0, 0));
      this.newBookingArrowsEndDate = new Date(newDate.setHours(Math.floor((intervalIndex + 1) / 2), Math.floor(30 * ((intervalIndex + 1) % 2)), 0, 0));
      this.newBookingArrowsStyle = this.getStyleForBooking({
        startDate: this.newBookingArrowsStartDate,
        endDate: this.newBookingArrowsEndDate
      });
    }
  }

  shouldBeDisplayedHalf() {
    let status = false;
    this.rooms[this.newBookingRoomIndex].reservations.forEach(booking => {
      if (this.isBookingCreatedByCurrentUser(booking)) {
        status = true;
        return;
      }
    });

    return status;
  }

  arrow: string;
  mouseMove(event) {
    if (!this.arrow) {
      return;
    }

    let arrowsStartTime, arrowsEndTime;

    //let forDate = new Date(this.forDate.setHours(0, 0, 0, 0)).getTime();
    let today = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
    let now = new Date().setHours(new Date().getHours(), (new Date().getMinutes() < 30 ? 0 : 30), 0, 0);

    if (this.arrow === "up") {
      arrowsStartTime = this.newBookingArrowsStartDate.getTime() + event.movementY * 60 * 60 * 10;
      arrowsEndTime = this.newBookingArrowsEndDate.getTime();

      if (arrowsStartTime < this.forDate.getTime() ) {
        arrowsStartTime = this.forDate.getTime() ;
      }

      if (arrowsStartTime < now) {
        arrowsStartTime = now;
      }

      if (arrowsStartTime > this.forDate.getTime()  + 23 * 60 * 60 * 1000 + 1000 * 60 * 30) {
        arrowsStartTime = this.forDate.getTime()  + 23 * 60 * 60 * 1000 + 1000 * 60 * 30;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
      }

      if (arrowsEndTime > this.forDate.getTime()  + 24 * 60 * 60 * 1000) {
        arrowsEndTime = this.forDate.getTime()  + 24 * 60 * 60 * 1000;
      }
    }

    if (this.arrow === "down") {
      arrowsStartTime = this.newBookingArrowsStartDate.getTime();
      arrowsEndTime = this.newBookingArrowsEndDate.getTime() + event.movementY * 60 * 60 * 10;

      if (arrowsEndTime > this.forDate.getTime()  + 24 * 60 * 60 * 1000) {
        arrowsEndTime = this.forDate.getTime()  + 24 * 60 * 60 * 1000;
      }

      if (arrowsEndTime < this.forDate.getTime()  + 30 * 60 * 1000) {
        arrowsEndTime = this.forDate.getTime()  + 30 * 60 * 1000;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsStartTime = arrowsEndTime - 1000 * 60 * 30;
      }

      if (arrowsStartTime < this.forDate.getTime() ) {
        arrowsStartTime = this.forDate.getTime() ;
      }

      if (arrowsStartTime < now) {
        arrowsStartTime = now;
      }

      if (arrowsStartTime > this.forDate.getTime()  + 23 * 60 * 60 * 1000 + 1000 * 60 * 30) {
        arrowsStartTime = this.forDate.getTime()  + 23 * 60 * 60 * 1000 + 1000 * 60 * 30;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
      }

      if (arrowsEndTime > this.forDate.getTime()  + 24 * 60 * 60 * 1000) {
        arrowsEndTime = this.forDate.getTime()  + 24 * 60 * 60 * 1000;
      }
    }

    if (this.arrow === "both") {
      arrowsStartTime = this.newBookingArrowsStartDate.getTime() + event.movementY * 60 * 60 * 10;
      arrowsEndTime = this.newBookingArrowsEndDate.getTime() + event.movementY * 60 * 60 * 10;

      if (arrowsStartTime < this.forDate.getTime() ) {
        arrowsStartTime = this.forDate.getTime() ;
      }

      if (arrowsStartTime < now) {
        arrowsStartTime = now;
      }

      if (arrowsStartTime > this.forDate.getTime()  + 23 * 60 * 60 * 1000 + 1000 * 60 * 30) {
        arrowsStartTime = this.forDate.getTime()  + 23 * 60 * 60 * 1000 + 1000 * 60 * 30;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
      }

      if (arrowsEndTime > this.forDate.getTime()  + 24 * 60 * 60 * 1000) {
        arrowsEndTime = this.forDate.getTime()  + 24 * 60 * 60 * 1000;
      }
    }

    if (this.arrow) {
      this.newBookingArrowsStartDate = new Date(arrowsStartTime);
      this.newBookingArrowsEndDate = new Date(arrowsEndTime);

      this.newBookingArrowsStyle = this.getStyleForBooking({
        startDate: this.newBookingArrowsStartDate,
        endDate: this.newBookingArrowsEndDate
      });

      this.updateNewBookingTime();
    }
  }

  finishBookingDragEvents() {
    if (this.arrow) {
      this.newBookingArrowsStartDate = new Date(this.newBookingStartDate.getTime());
      this.newBookingArrowsEndDate = new Date(this.newBookingEndDate.getTime());
      this.newBookingArrowsStyle = this.getStyleForBooking({
        startDate: this.newBookingArrowsStartDate,
        endDate: this.newBookingArrowsEndDate
      });
    }

    this.arrow = null;
  }

  followMouse(arrow) {
    this.arrow = arrow;
  }

  updateNewBookingTime() {
    let roundedHoursStart = this.newBookingArrowsStartDate.getHours();
    let roundedMinutesStart = this.newBookingArrowsStartDate.getMinutes();
    if (this.isValueInInterval(roundedMinutesStart, 15, 45)) {
      roundedMinutesStart = 30;
    } else {
      if (roundedMinutesStart > 45) {
        roundedHoursStart++;
      }
      roundedMinutesStart = 0;
    }

    let roundedHoursEnd = this.newBookingArrowsEndDate.getHours();
    let roundedMinutesEnd = this.newBookingArrowsEndDate.getMinutes();
    if (this.isValueInInterval(roundedMinutesEnd, 15, 45)) {
      roundedMinutesEnd = 30;
    } else {
      if (roundedMinutesEnd > 45) {
        roundedHoursEnd++;
      }
      roundedMinutesEnd = 0;
    }

    let arrowsStartDateCopy = new Date(this.newBookingArrowsStartDate.getTime());
    let arrowsStartTime = arrowsStartDateCopy.setHours(roundedHoursStart, roundedMinutesStart, 0, 0);
    let arrowsEndDateCopy = new Date(this.newBookingArrowsEndDate.getTime());
    let arrowsEndTime = arrowsEndDateCopy.setHours(roundedHoursEnd, roundedMinutesEnd, 0, 0);

    if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
      arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
    }

    let bookings = this.rooms[this.newBookingRoomIndex].reservations;

    let bookingStartTime, bookingEndTime;
    let bookingCanBePerformed = true;
    if (bookings) {
      bookings.forEach(booking => {
        bookingStartTime = booking.startDate.getTime();
        bookingEndTime = booking.endDate.getTime();

        if (this.isBookingCreatedByCurrentUser(booking)) {
          return;
        }

        if (this.isValueInInterval(arrowsStartTime, bookingStartTime, bookingEndTime) &&
          this.isValueInInterval(arrowsEndTime, bookingStartTime, bookingEndTime)) {
          bookingCanBePerformed = false;
          return;
        }

        if (!this.isValueInInterval(arrowsStartTime, bookingStartTime, bookingEndTime) &&
          !this.isValueInInterval(arrowsEndTime, bookingStartTime, bookingEndTime) &&
          this.isValueInInterval(bookingStartTime, arrowsStartTime, arrowsEndTime)) {
          bookingCanBePerformed = false;
          return;
        }

        if (this.isValueInInterval(arrowsStartTime, bookingStartTime, bookingEndTime) &&
          !this.isValueInInterval(arrowsEndTime, bookingStartTime, bookingEndTime)) {
          arrowsStartTime = bookingEndTime;
        }

        if (!this.isValueInInterval(arrowsStartTime, bookingStartTime, bookingEndTime) &&
          this.isValueInInterval(arrowsEndTime, bookingStartTime, bookingEndTime)) {
          arrowsEndTime = bookingStartTime;
        }
      });
    }

    if (bookingCanBePerformed) {
      this.newBookingStartDate = new Date(arrowsStartTime);
      this.newBookingEndDate = new Date(arrowsEndTime);

      this.newBookingStyle = this.getStyleForBooking({
        startDate: this.newBookingStartDate,
        endDate: this.newBookingEndDate
      });
    }
  }

  bookingsOverlap(booking1: Booking, booking2: Booking) {
    if (booking1.startDate.getTime() === booking2.startDate.getTime()) {
      return true;
    }

    if (this.isValueInOpenInterval(booking1.startDate, booking2.startDate, booking2.endDate)) {
      return true;
    }

    if (this.isValueInOpenInterval(booking1.endDate, booking2.startDate, booking2.endDate)) {
      return true;
    }

    if (this.isValueInOpenInterval(booking2.startDate, booking1.startDate, booking1.endDate)) {
      return true;
    }

    if (this.isValueInOpenInterval(booking2.endDate, booking1.startDate, booking1.endDate)) {
      return true;
    }

    return false;
  }

  isValueInOpenInterval(value, start, end) {
    if (value > start && value < end) {
      return true;
    }

    return false;
  }

  isValueInInterval(value, start, end) {
    if (value >= start && value <= end) {
      return true;
    }

    return false;
  }

  resetNewBooking() {
    this.newBookingStartDate = null;
    this.newBookingEndDate = null;
    this.newBookingStyle = null;
    this.newBookingRoomIndex = null;

    this.newBookingArrowsStartDate = null;
    this.newBookingArrowsEndDate = null;
    this.newBookingArrowsStyle = null;

    this.personalBookingsDisplayedHalf = false;
  }
}
