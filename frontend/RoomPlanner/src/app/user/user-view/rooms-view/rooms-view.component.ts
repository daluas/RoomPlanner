import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit, OnChanges, SimpleChanges } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';
import { DomSanitizer, SafeStyle } from '@angular/platform-browser';
import { AuthService } from 'src/app/core/core.module';
import { LoggedUser } from 'src/app/core/models/LoggedUser';

@Component({
  selector: 'app-rooms-view',
  templateUrl: './rooms-view.component.html',
  styleUrls: ['./rooms-view.component.css']
})
export class RoomsViewComponent implements OnInit, AfterViewInit, OnChanges {

  @Input() rooms: any;
  @Input() forDate: Date;
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

  bookRoomTest() {
    this.createBooking.emit(new Booking().create({
      startDate: new Date(new Date().setHours(12, 30, 0, 0)),
      endDate: new Date(new Date().setHours(15, 0, 0, 0)),
      roomId: 1
    }));
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
    let forDate = new Date(this.forDate.setHours(0,0,0,0)).getTime();
    let today = new Date(new Date().setHours(0,0,0,0)).getTime();
    let style = "height: 0px";

    if(forDate < today){
      style = "height: 2400px";
    }

    if(forDate === today){
      let now = new Date();
      style = `height: ${now.getHours() * 100 + now.getMinutes() * 10 / 6}px`
    }

    this.styleForPastTime = this.sanitizer.bypassSecurityTrustStyle(style);
  }

  newBooking(roomIndex, intervalIndex){
    if(intervalIndex < 48){
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
  mouseMove(event){
    if(this.arrow === "up"){
      console.log("up: " + event.movementY);

      let newDate = this.newBookingArrowsStartDate.getTime() + event.movementY * 60 * 60 * 10;

      if(this.newBookingArrowsEndDate.getTime() - newDate < 1000 * 60 * 30){
        this.newBookingArrowsEndDate = new Date(newDate + 1000 * 60 * 30);
      }

      this.newBookingArrowsStartDate = new Date(newDate);

      this.newBookingArrowsStyle = this.getStyleForBooking({
        startDate: this.newBookingArrowsStartDate,
        endDate: this.newBookingArrowsEndDate
      });
    }

    if(this.arrow === "down"){
      console.log("down: " + event.movementY);

      let newDate = this.newBookingArrowsEndDate.getTime() + event.movementY * 60 * 60 * 10;

      if(newDate - this.newBookingStartDate.getTime() < 1000 * 60 * 30){
        this.newBookingArrowsStartDate = new Date(newDate - 1000 * 60 * 30);
      } 

      this.newBookingArrowsEndDate = new Date(newDate);

      this.newBookingArrowsStyle = this.getStyleForBooking({
        startDate: this.newBookingArrowsStartDate,
        endDate: this.newBookingArrowsEndDate
      });
    }

    if(this.arrow === "both"){
      console.log("both: " + event.movementY);

      let newDate = this.newBookingArrowsStartDate.getTime() + event.movementY * 60 * 60 * 10;
      this.newBookingArrowsStartDate = new Date(newDate);

      newDate = this.newBookingArrowsEndDate.getTime() + event.movementY * 60 * 60 * 10;
      this.newBookingArrowsEndDate = new Date(newDate);

      this.newBookingArrowsStyle = this.getStyleForBooking({
        startDate: this.newBookingArrowsStartDate,
        endDate: this.newBookingArrowsEndDate
      });
    }
  }

  resetBookingDragEvents(){
    this.arrow = null;
  }

  followMouse(arrow){
    this.arrow = arrow;
  }
}
