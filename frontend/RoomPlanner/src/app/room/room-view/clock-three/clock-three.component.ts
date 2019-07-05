import { Component, OnInit, Input, Output, EventEmitter, OnChanges, OnDestroy } from '@angular/core';
import { Booking } from 'src/app/core/models/BookingModel';
import { TimeInterval } from '../models/timeInterval';
import { IntervalType } from '../enums/enums';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-clock-three',
  templateUrl: './clock-three.component.html',
  styleUrls: ['./clock-three.component.css']
})
export class ClockThreeComponent implements OnInit {


  @Input() isActive: boolean;
  @Input() reservations: Booking[];

  @Output() interval: EventEmitter<TimeInterval> = new EventEmitter();

  updateInterval: any;
  availabilityIntervals: any[]
  beginningLine: any;
  clockNumbers: any[];
  clockTime: string;
  clockPins: any;

  booking: any = null;

  intervalsType = {
    available: IntervalType.available,
    occupied: IntervalType.occupied,
    reserved: IntervalType.reserved
  }

  svgWidth: number = 0;

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.svgWidth = document.getElementById('svg-clock').getBoundingClientRect().width;
    this.initClockNumbers();

    this.setClock();

    let scope = this;
    this.updateInterval = setInterval(() => {
      scope.setClock();
    }, 10 * 1000)
  }

  ngOnChanges() {
    this.setClock();
  }

  ngOnDestroy() {
    clearInterval(this.updateInterval);
  }

  setClock() {
    let startTimeForIntervals = this.getStartTimeForIntervals();
    this.availabilityIntervals = this.getAvailabilityIntervals(startTimeForIntervals);
    this.updateClock();
  }

  getStartTimeForIntervals() {
    let now = new Date();
    return now.setHours(now.getHours(), (now.getMinutes() < 30 ? 0 : 30), 0, 0);
  }

  getAvailabilityIntervals(startTimeForIntervals: number) {

    let array = [];
    for (let i = 0; i < 24; i++) {
      array.push({
        from: startTimeForIntervals + i * 30 * 60 * 1000,
        to: startTimeForIntervals + (i + 1) * 30 * 60 * 1000,
        type: IntervalType.available
      })

      if (this.reservations) {
        this.reservations.forEach((reservation) => {
          if (array[i].from >= reservation.startDate.getTime() &&
            array[i].to <= reservation.endDate.getTime()) {
            array[i].type = IntervalType.occupied;
          }
        });
      }
    }

    return array;
  }

  updateClock() {
    this.setBeginningLine();

    let now = new Date();
    this.clockTime = (now.getHours() < 10 ? '0' + now.getHours() : now.getHours().toString()) + " : " + (now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes().toString());

    this.setClockPins();

    if (this.booking) {
      this.availabilityIntervals.forEach(interval => {
        if (interval.from >= this.booking.from && interval.to <= this.booking.to) {
          interval.type = IntervalType.reserved;
        }
      });
    }
  }

  getIntervalPath(intervalIndex: number) {
    let interval = this.availabilityIntervals[intervalIndex];

    let [startX, startY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.from), 1);
    let [endX, endY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.to), 1);

    let pathData = [
      `M ${startX} ${startY}`, // Move
      `A ${this.svgWidth / 2 - 2} ${this.svgWidth / 2 - 2} 0 0 1 ${endX} ${endY}`, // Arc
      `L ${this.svgWidth / 2} ${this.svgWidth / 2}`, // Line
      `L ${startX} ${startY}`, // Line
    ].join(' ');

    return pathData;
  }

  setBeginningLine() {
    let [lineX, lineY] = this.getCoordinatesForPercent(this.getPercentsForTime(this.availabilityIntervals[0].from), 0);

    this.beginningLine = {
      x1: lineX.toString(),
      y1: lineY.toString(),
      x2: this.svgWidth / 2,
      y2: this.svgWidth / 2
    }
  }

  initClockNumbers() {
    this.clockNumbers = [];

    for (let i = 1; i <= 24; i++) {
      let date = new Date().setHours(Math.floor(i / 2), (i % 2) * 30, 0, 0);
      let [x, y] = this.getCoordinatesForPercent(this.getPercentsForTime(date), 70);

      let obj: any;

      if (i % 2 === 1) {
        obj = {
          type: "point",
          cx: x,
          cy: y,
          r: 3
        }
      } else {
        obj = {
          type: "text",
          x: x,
          y: y,
          dy: 2,
          value: Math.floor(i / 2).toString()
        }
      }

      this.clockNumbers.push(obj);
    }
  }

  setClockPins() {
    let [minutesPercent, hoursPercent] = this.getPercentsForNow();
    let [lineMinX, lineMinY] = this.getCoordinatesForPercent(minutesPercent, 90);
    let [lineHourX, lineHourY] = this.getCoordinatesForPercent(hoursPercent, 120);

    this.clockPins = {
      minutes: {
        x1: lineMinX,
        y1: lineMinY,
        x2: this.svgWidth / 2,
        y2: this.svgWidth / 2
      },
      hours: {
        x1: lineHourX,
        y1: lineHourY,
        x2: this.svgWidth / 2,
        y2: this.svgWidth / 2
      }
    }
  }

  selectInterval(index){
    let interval = this.availabilityIntervals[index];

    if(interval.type === IntervalType.occupied){
      return;
    }

    if(this.booking){

      if(index === this.booking.indexFrom || index === this.booking.indexTo){
        interval.type = IntervalType.available;
        if(this.booking.indexFrom === this.booking.indexTo){
          this.booking = null;
        } else {
          if(index === this.booking.indexFrom){
            this.availabilityIntervals[index].type = IntervalType.available;
            this.booking.from = this.availabilityIntervals[index + 1].from;
            this.booking.indexFrom = index + 1;
          } else {
            this.availabilityIntervals[index].type = IntervalType.available;
            this.booking.to = this.availabilityIntervals[index - 1].to;
            this.booking.indexTo = index - 1;
          }
        }


        return
      }

      if(index > this.booking.indexFrom && index < this.booking.indexTo){
        for(let i = index; i <= this.booking.indexTo; i++){
          this.availabilityIntervals[i].type = IntervalType.available;
        }

        this.booking.to = this.availabilityIntervals[index-1].to;
        this.booking.indexTo = index - 1;


        return
      }

      if(index < this.booking.indexFrom){
        let ok = true;
        for(let i = index; i < this.booking.indexFrom; i++){
          if(this.availabilityIntervals[i].type === IntervalType.occupied){
            ok = false;
            break;
          }
        }

        if(!ok){
          for(let i = this.booking.indexFrom; i <= this.booking.indexTo; i++){
            this.availabilityIntervals[i].type = IntervalType.available;
          }
          this.availabilityIntervals[index].type = IntervalType.reserved;
          this.booking = {
            from: interval.from,
            to: interval.to,
            indexFrom: index,
            indexTo: index
          }
        } else {
          for(let i = index; i <= this.booking.indexFrom; i++){
            this.availabilityIntervals[i].type = IntervalType.reserved;
          }
          this.booking.from = interval.from;
          this.booking.indexFrom = index;
        }
        

        return
      }

      if(index > this.booking.indexTo){
        let ok = true;
        for(let i = this.booking.indexTo + 1; i <= index; i++){
          if(this.availabilityIntervals[i].type === IntervalType.occupied){
            ok = false;
            break;
          }
        }

        if(!ok){
          for(let i = this.booking.indexFrom; i <= this.booking.indexTo; i++){
            this.availabilityIntervals[i].type = IntervalType.available;
          }
          this.availabilityIntervals[index].type = IntervalType.reserved;
          this.booking = {
            from: interval.from,
            to: interval.to,
            indexFrom: index,
            indexTo: index
          }
        } else {
          for(let i = this.booking.indexTo + 1; i <= index; i++){
            this.availabilityIntervals[i].type = IntervalType.reserved;
          }
          this.booking.to = interval.to;
          this.booking.indexTo = index;
        }
        

        return
      }

    } else {
      this.booking = {
        from: interval.from,
        to: interval.to,
        indexFrom: index,
        indexTo: index
      }
      interval.type = IntervalType.reserved;

      console.log(this.booking);
    }
  }

  getPercentsForTime(time: number) {
    let date = new Date(time);
    let hour = date.getHours();
    let minutes = date.getMinutes();

    if (hour > 12) { hour = hour - 12; }

    let unit = 1 / 24;
    let units = (hour * 2 + (minutes == 30 ? 1 : 0));

    return unit * (units + 24 - 6);
  }

  getPercentsForNow() {
    let now = new Date();

    let minuteUnit = 1 / 60;
    let hourUnit = 1 / (60 * 12);

    let minute = now.getMinutes();
    let hour = now.getHours();
    if (hour > 12) { hour = hour - 12; }

    return [minuteUnit * (minute + 60 * 3 / 4), hourUnit * (hour * 60 + minute + 60 * 12 * 3 / 4)]
  }

  getCoordinatesForPercent(percent: number, minusLength: number) {
    const x = Math.cos(2 * Math.PI * percent);
    const y = Math.sin(2 * Math.PI * percent);

    let width = this.svgWidth;

    return [
      width * 0.5 + (width * 0.5 - minusLength) * x,
      width * 0.5 + (width * 0.5 - minusLength) * y
    ];
  }

}
