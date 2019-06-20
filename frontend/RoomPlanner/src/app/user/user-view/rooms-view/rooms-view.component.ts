import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit, OnChanges, SimpleChanges } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { BookingPopupComponent } from '../../../shared/booking-popup/booking-popup.component';
import { DomSanitizer, SafeStyle } from '@angular/platform-browser';
import { AuthService } from 'src/app/core/core.module';
import { LoggedUser } from 'src/app/core/models/LoggedUser';

@Component({
  selector: 'app-rooms-view',
  templateUrl: './rooms-view.component.html',
  styleUrls: ['./rooms-view.component.css']
})
export class RoomsViewComponent implements OnInit, AfterViewInit, OnChanges {

  rooms: any;
  @Input('rooms') set newRooms(value: any) {
    this.rooms = value;
    this.resetNewBooking();
  }

  forDate: Date;
  @Input('forDate') set newForDate(value: any) {
    this.forDate = value;
    this.resetNewBooking();
  }

  @Output() createBooking: EventEmitter<any> = new EventEmitter();

  intervals: string[];
  currentUser: LoggedUser;
  styleForPastTime: SafeStyle;

  newBookingStartDate: Date;
  newBookingEndDate: Date;
  newBookingStyle: SafeStyle;
  newBookingRoomIndex: number;

  newBookingArrowsStartDate: Date;
  newBookingArrowsEndDate: Date;
  newBookingArrowsStyle: SafeStyle;

  constructor(private sanitizer: DomSanitizer, private authService: AuthService) { }

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();

    this.setCalendarIntervals();

    this.setStyleForPastTime();
    setInterval(() => { this.setStyleForPastTime(); }, 60 * 1000);
  }

  ngAfterViewInit() {
    this.scrollCalendarToEightOClock();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.setStyleForPastTime();
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

  bookRoom() {
    this.createBooking.emit(new Booking().create({
      startDate: this.newBookingStartDate,
      endDate: this.newBookingEndDate,
      roomId: this.rooms[this.newBookingRoomIndex].id
    }));
  }

  openBooking(roomIndex, bookingIndex) {
    if (this.rooms[roomIndex].bookings[bookingIndex].id) {
      let booking = new Booking().create(this.rooms[roomIndex].bookings[bookingIndex]);
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

  getBookingColorClass(booking: any) {
    return {
      "booking-other": !this.isBookingCreatedByCurrentUser(booking),
      "booking-personal": this.isBookingCreatedByCurrentUser(booking),
    }
  }

  isBookingCreatedByCurrentUser(booking) {
    return booking.ownerEmail === this.currentUser.email;
  }

  getUsernameFromEmail(email): string {
    return email.split('@')[0];
  }

  setStyleForPastTime() {
    if (this.forDate) {
      let forDate = new Date(this.forDate.setHours(0, 0, 0, 0)).getTime();
      let today = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
      let style = "height: 0px";

      if (forDate < today) {
        style = "height: 2400px";
      }

      if (forDate === today) {
        let now = new Date();
        style = `height: ${now.getHours() * 100 + now.getMinutes() * 10 / 6}px`
      }

      this.styleForPastTime = this.sanitizer.bypassSecurityTrustStyle(style);
    }
  }

  newBooking(roomIndex, intervalIndex) {
    let forDate = new Date(this.forDate.setHours(0, 0, 0, 0)).getTime();
    let today = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
    let nowIntervals = new Date().getHours() * 2 + (new Date().getMinutes() < 30 ? 0 : 1);

    if (intervalIndex < 48 && (forDate > today || forDate === today && intervalIndex >= nowIntervals)) {
      this.newBookingRoomIndex = roomIndex;

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

  arrow: string;
  mouseMove(event) {
    if (!this.arrow) {
      return;
    }

    let arrowsStartTime, arrowsEndTime;

    let forDate = new Date(this.forDate.setHours(0, 0, 0, 0)).getTime();
    let today = new Date(new Date().setHours(0, 0, 0, 0)).getTime();
    let now = new Date().setHours(new Date().getHours(), (new Date().getMinutes() < 30 ? 0 : 30), 0, 0);

    if (this.arrow === "up") {
      arrowsStartTime = this.newBookingArrowsStartDate.getTime() + event.movementY * 60 * 60 * 10;
      arrowsEndTime = this.newBookingArrowsEndDate.getTime();

      if (arrowsStartTime < forDate) {
        arrowsStartTime = forDate;
      }

      if (arrowsStartTime < now) {
        arrowsStartTime = now;
      }

      if (arrowsStartTime > forDate + 23 * 60 * 60 * 1000 + 1000 * 60 * 30) {
        arrowsStartTime = forDate + 23 * 60 * 60 * 1000 + 1000 * 60 * 30;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
      }

      if (arrowsEndTime > forDate + 24 * 60 * 60 * 1000) {
        arrowsEndTime = forDate + 24 * 60 * 60 * 1000;
      }
    }

    if (this.arrow === "down") {
      arrowsStartTime = this.newBookingArrowsStartDate.getTime();
      arrowsEndTime = this.newBookingArrowsEndDate.getTime() + event.movementY * 60 * 60 * 10;

      if (arrowsEndTime > forDate + 24 * 60 * 60 * 1000) {
        arrowsEndTime = forDate + 24 * 60 * 60 * 1000;
      }

      if (arrowsEndTime < forDate + 30 * 60 * 1000) {
        arrowsEndTime = forDate + 30 * 60 * 1000;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsStartTime = arrowsEndTime - 1000 * 60 * 30;
      }

      if (arrowsStartTime < forDate) {
        arrowsStartTime = forDate;
      }

      if (arrowsStartTime < now) {
        arrowsStartTime = now;
      }

      if (arrowsStartTime > forDate + 23 * 60 * 60 * 1000 + 1000 * 60 * 30) {
        arrowsStartTime = forDate + 23 * 60 * 60 * 1000 + 1000 * 60 * 30;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
      }

      if (arrowsEndTime > forDate + 24 * 60 * 60 * 1000) {
        arrowsEndTime = forDate + 24 * 60 * 60 * 1000;
      }
    }

    if (this.arrow === "both") {
      arrowsStartTime = this.newBookingArrowsStartDate.getTime() + event.movementY * 60 * 60 * 10;
      arrowsEndTime = this.newBookingArrowsEndDate.getTime() + event.movementY * 60 * 60 * 10;

      if (arrowsStartTime < forDate) {
        arrowsStartTime = forDate;
      }

      if (arrowsStartTime < now) {
        arrowsStartTime = now;
      }

      if (arrowsStartTime > forDate + 23 * 60 * 60 * 1000 + 1000 * 60 * 30) {
        arrowsStartTime = forDate + 23 * 60 * 60 * 1000 + 1000 * 60 * 30;
      }

      if (arrowsEndTime - arrowsStartTime < 1000 * 60 * 30) {
        arrowsEndTime = arrowsStartTime + 1000 * 60 * 30;
      }

      if (arrowsEndTime > forDate + 24 * 60 * 60 * 1000) {
        arrowsEndTime = forDate + 24 * 60 * 60 * 1000;
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

    let bookings = this.rooms[this.newBookingRoomIndex].bookings;

    let bookingStartTime, bookingEndTime;
    let bookingCanBePerformed = true;
    if (bookings) {
      bookings.forEach(booking => {
        bookingStartTime = booking.startDate.getTime();
        bookingEndTime = booking.endDate.getTime();

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
  }
}
