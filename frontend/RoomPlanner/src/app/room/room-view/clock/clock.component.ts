import { Component, OnInit, Input, Output, EventEmitter, OnChanges, OnDestroy } from '@angular/core';
import { Booking } from 'src/app/core/models/BookingModel';
import { TimeInterval } from '../models/timeInterval';
import { IntervalType } from '../enums/enums';
import { DomSanitizer } from '@angular/platform-browser';
import { max } from 'rxjs/operators';

@Component({
  selector: 'app-clock',
  templateUrl: './clock.component.html',
  styleUrls: ['./clock.component.css']
})
export class ClockComponent implements OnInit, OnChanges, OnDestroy {

  @Input() isActive: boolean;
  @Input() reservations: Booking[];

  @Output() interval: EventEmitter<TimeInterval> = new EventEmitter();

  updateInterval: any;
  availabilityIntervals: any[]
  beginningLine: any;
  clockNumbers: any[];
  clockTime: string;
  clockPins: any;
  reservationPins: any[] = null;

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

    this.setReservationPins();
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
      let [x, y] = this.getCoordinatesForPercent(this.getPercentsForTime(date), 50);

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
    let [lineMinX, lineMinY] = this.getCoordinatesForPercent(minutesPercent, 70);
    let [lineHourX, lineHourY] = this.getCoordinatesForPercent(hoursPercent, 100);

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

  setReservationPins() {
    if (!this.isActive) {
      this.reservationPins = null;
      this.availabilityIntervals.forEach((interval) => {
        if (interval.type === IntervalType.reserved) {
          interval.type = IntervalType.available;
        }
      });
      return;
    }

    if (!this.reservationPins) {
      for (let i = 0; i < this.availabilityIntervals.length; i++) {
        if (this.availabilityIntervals[i].type === IntervalType.available) {
          this.availabilityIntervals[i].type = IntervalType.reserved;

          let pin1Percent = this.getPercentsForTime(this.availabilityIntervals[i].from);
          let pin2Percent = this.getPercentsForTime(this.availabilityIntervals[i].to);

          let pin1 = this.getPinForPercent(pin1Percent + 0.001);
          let pin2 = this.getPinForPercent(pin2Percent - 0.001);

          let scope = this;
          setTimeout(() => {
            scope.reservationPins = [pin1, pin2];
          }, 0);

          this.interval.emit(new TimeInterval().create({
            startDate: this.availabilityIntervals[i].from,
            endDate: this.availabilityIntervals[i].to
          }))

          break;
        }
      }
    } else {
      let availabilityIntervalForPin1 = this.getAvailabilityIntervalForPin(0);
      let availabilityIntervalForPin2 = this.getAvailabilityIntervalForPin(1);

      if (availabilityIntervalForPin1 === -1) {
        availabilityIntervalForPin1 = availabilityIntervalForPin2;
      }

      if (availabilityIntervalForPin2 === -1) {
        availabilityIntervalForPin2 = availabilityIntervalForPin1;
      }

      if (availabilityIntervalForPin1 > availabilityIntervalForPin2) {
        let aux = availabilityIntervalForPin2;
        availabilityIntervalForPin2 = availabilityIntervalForPin1;
        availabilityIntervalForPin1 = aux;
      }

      for (let i = 0; i < 24; i++) {
        if (this.availabilityIntervals[i].type === IntervalType.reserved) {
          this.availabilityIntervals[i].type = IntervalType.available;
        }
      }

      let reservationUpdate = true;
      let reservationStartIndex = availabilityIntervalForPin1;
      let reservationEndIndex = -1;
      for (let i = availabilityIntervalForPin1; i <= availabilityIntervalForPin2; i++) {
        if (this.availabilityIntervals[i].type === IntervalType.occupied) {
          reservationUpdate = false;
          break;
        }

        this.availabilityIntervals[i].type = IntervalType.reserved;
        reservationEndIndex = i;
      }

      if (!this.trackPin && reservationUpdate) {
        let pin1Percent = this.getPercentsForTime(this.availabilityIntervals[reservationStartIndex].from);
        let pin2Percent = this.getPercentsForTime(this.availabilityIntervals[reservationEndIndex].to);

        let pin1 = this.getPinForPercent(pin1Percent + 0.001);
        let pin2 = this.getPinForPercent(pin2Percent - 0.001);

        let scope = this;
        setTimeout(() => {
          scope.reservationPins = [pin1, pin2];
        }, 0);
      }

      if (reservationEndIndex === -1) {
        this.interval.emit(null);
      } else {
        this.interval.emit(new TimeInterval().create({
          startDate: this.availabilityIntervals[reservationStartIndex].from,
          endDate: this.availabilityIntervals[reservationEndIndex].to
        }))
      }
    }
  }

  getPinForPercent(percent): any {
    let [x1, x2] = this.getCoordinatesForPercent(percent, 67);
    let [cx, cy] = this.getCoordinatesForPercent(percent, 51);

    let style = `top: ${cy}px; left: ${cx}px`;

    return {
      x1: x1,
      y1: x2,
      cx: cx,
      cy: cy,
      circleStyle: this.sanitizer.bypassSecurityTrustStyle(style)
    }
  }

  getAvailabilityIntervalForPin(pinIndex) {

    let pin = this.reservationPins[pinIndex];

    let pinCadran = this.getCadranForCoordinates(pin.cx, pin.cy);
    let pinM = this.getMedianForCoordinates(pin.x1, pin.y1);

    let index = 0;

    for (let interval of this.availabilityIntervals) {
      let [startX, startY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.from), 1);
      let [endX, endY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.to), 1);

      if (pinCadran === this.getCadranForCoordinates(endX, endY)) {
        let startM = this.getMedianForCoordinates(startX, startY);
        let endM = this.getMedianForCoordinates(endX, endY);

        if (pinM > Math.min(startM, endM) && pinM < Math.max(startM, endM)) {
          return index;
        }
      }

      index++;
    }

    return -1;
  }

  getCadranForCoordinates(x, y) {
    if (y <= this.svgWidth / 2) {
      if (x >= this.svgWidth / 2) {
        return 1;
      }
      return 2
    } else {
      if (x >= this.svgWidth / 2) {
        return 4;
      }
      return 3
    }
  }

  getMedianForCoordinates(x, y) {
    if (y <= this.svgWidth / 2) {
      if (x >= this.svgWidth / 2) {
        return (this.svgWidth / 2 - y) / (x - this.svgWidth / 2);
      }
      return (this.svgWidth / 2 - y) / (this.svgWidth / 2 - x);
    } else {
      if (x >= this.svgWidth / 2) {
        return (y - this.svgWidth / 2) / (x - this.svgWidth / 2);
      }
      return (y - this.svgWidth / 2) / (this.svgWidth / 2 - x);
    }
  }

  trackPin = false;
  pinIndex = 0;

  mouseMove(event, outerDiv) {
    if (!this.trackPin) {
      return;
    }

    const bounds = outerDiv.getBoundingClientRect();
    let x = event.clientX - bounds.left;
    let y = event.clientY - bounds.top;

    let angle = Math.atan2(y - (this.svgWidth / 2), x - (this.svgWidth / 2));
    let newMoveX = this.svgWidth / 2 + (this.svgWidth / 2 - 51) * Math.cos(angle);
    let newMoveY = this.svgWidth / 2 + (this.svgWidth / 2 - 51) * Math.sin(angle);

    let newMoveXLine = this.svgWidth / 2 + (this.svgWidth / 2 - 67) * Math.cos(angle);
    let newMoveYLine = this.svgWidth / 2 + (this.svgWidth / 2 - 67) * Math.sin(angle);

    this.reservationPins[this.pinIndex].cx = newMoveX;
    this.reservationPins[this.pinIndex].cy = newMoveY;

    let style = `top: ${this.reservationPins[this.pinIndex].cy}px; left: ${this.reservationPins[this.pinIndex].cx}px`;
    this.reservationPins[this.pinIndex].circleStyle = this.sanitizer.bypassSecurityTrustStyle(style);
    this.reservationPins[this.pinIndex].x1 = newMoveXLine;
    this.reservationPins[this.pinIndex].y1 = newMoveYLine;

    this.setReservationPins();
  }

  trackPinMove(pinIndex) {
    this.trackPin = true;
    this.pinIndex = pinIndex;
  }

  finishMove() {
    this.trackPin = false;

    this.setClock()
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
