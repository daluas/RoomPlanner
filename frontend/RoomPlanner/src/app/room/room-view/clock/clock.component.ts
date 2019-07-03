import { Component, OnInit, Input, Output, EventEmitter, OnChanges, OnDestroy } from '@angular/core';
import { Booking } from 'src/app/core/models/BookingModel';
import { TimeInterval } from '../models/timeInterval';
import { IntervalType } from '../enums/enums';

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

  constructor() { }

  ngOnInit() {
    this.svgWidth = document.getElementById('svg-clock').getBoundingClientRect().width;
    this.initClockNumbers();

    this.setClock();

    let scope = this;
    this.updateInterval = setInterval(() => {
      scope.setClock();
    }, 60 * 1000)
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

      this.reservations.forEach((reservation) => {
        if (array[i].from >= reservation.startDate.getTime() &&
          array[i].to <= reservation.endDate.getTime()) {
          array[i].type = IntervalType.occupied;
        }
      });
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
      return;
    }

    if (!this.reservationPins) {
      for (let i = 0; i < this.availabilityIntervals.length; i++) {
        if (this.availabilityIntervals[i].type === IntervalType.available) {
          this.availabilityIntervals[i].type = IntervalType.reserved;

          break;
        }
      }
    }
  }

  getPinForPercent(percent): any {
    let [x1, x2] = this.getCoordinatesForPercent(percent, 67);
    let [cx, cy] = this.getCoordinatesForPercent(percent, 51);

    return {
      x1: x1,
      y1: x2,
      cx: cx,
      cy: cy
    }



    // let width = this.getSvgWidth();

    // let svg = document.getElementById('svg-clock');

    // let [startX, startY] = this.getCoordinatesForPercent(0, 67);

    // let line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    // line.setAttribute('x1', startX.toString() + "px");
    // line.setAttribute('y1', startY.toString() + "px");
    // line.setAttribute('x2', width / 2 + "px");
    // line.setAttribute('y2', width / 2 + "px");
    // line.setAttribute("style", "stroke: #71c422; stroke-width: 3px;");
    // svg.appendChild(line);

    // [startX, startY] = this.getCoordinatesForPercent(0, 51);
    // let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    // circle.setAttributeNS(null, 'cx', startX + "px");
    // circle.setAttributeNS(null, 'cy', startY + "px");
    // circle.setAttributeNS(null, 'r', "15px");
    // circle.setAttributeNS(null, 'style', 'fill: rgb(0,0,0,0); stroke: #71c422; stroke-width: 3px;');
    // svg.appendChild(circle);
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

  /*
  drawClock() {

    // selection pins
    this.drawSelectionPins();

  }

  drawSelectionPins(){
    // pin x, y, ipotenuza, cateta

    this.drawPin();
  }

  drawPin(){
    let width = this.getSvgWidth();

    let svg = document.getElementById('svg-clock');

    let [startX, startY] = this.getCoordinatesForPercent(0, 67);

    let line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    line.setAttribute('x1', startX.toString() + "px");
    line.setAttribute('y1', startY.toString() + "px");
    line.setAttribute('x2', width / 2 + "px");
    line.setAttribute('y2', width / 2 + "px");
    line.setAttribute("style", "stroke: #71c422; stroke-width: 3px;");
    svg.appendChild(line);

    [startX, startY] = this.getCoordinatesForPercent(0, 51);
    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', startX + "px");
    circle.setAttributeNS(null, 'cy', startY + "px");
    circle.setAttributeNS(null, 'r', "15px");
    circle.setAttributeNS(null, 'style', 'fill: rgb(0,0,0,0); stroke: #71c422; stroke-width: 3px;');
    svg.appendChild(circle);
  }
  */
}
