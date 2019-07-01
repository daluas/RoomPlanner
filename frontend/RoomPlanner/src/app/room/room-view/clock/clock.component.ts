import { Component, OnInit, Input, Output, EventEmitter, OnChanges, OnDestroy } from '@angular/core';
import { Booking } from 'src/app/core/models/BookingModel';
import { TimeInterval } from '../models/timeInterval';

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

  constructor() { }

  ngOnInit() {
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
    this.drawIntervals();
  }

  getStartTimeForIntervals() {
    let now = new Date();
    return now.setHours(now.getHours(), (now.getMinutes() < 30 ? 0 : 30), 0, 0);
  }

  getAvailabilityIntervals(startTimeForIntervals: number) {
    let array = [];
    for (let i = 0; i < 23; i++) {
      array.push({
        from: startTimeForIntervals + i * 30 * 60 * 1000,
        to: startTimeForIntervals + (i + 1) * 30 * 60 * 1000,
        available: true
      })

      this.reservations.forEach((reservation) => {
        if (array[i].available &&
          array[i].from >= reservation.startDate.getTime() &&
          array[i].to <= reservation.endDate.getTime()) {
          array[i].available = false;
        }
      });
    }

    return array;
  }

  drawIntervals() {
    let svg = document.getElementById('svg-clock');
    svg.innerHTML = "";

    let width = this.getSvgWidth();

    this.availabilityIntervals.forEach(interval => {
      const [startX, startY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.from), 1);
      const [endX, endY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.to), 1);

      const pathData = [
        `M ${startX} ${startY}`, // Move
        `A ${width / 2 - 2} ${width / 2 - 2} 0 0 1 ${endX} ${endY}`, // Arc
        `L ${width / 2} ${width / 2}`, // Line
        `L ${startX} ${startY}`, // Line
      ].join(' ');

      let pathEl = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      pathEl.setAttribute('d', pathData);
      pathEl.setAttribute('fill', interval.available ? '#fff' : '#FF5B3E');
      pathEl.setAttribute("style", "stroke: #222; stroke-width: 1px;");
      svg.appendChild(pathEl);
    });

    // end of interval
    const [startX, startY] = this.getCoordinatesForPercent(this.getPercentsForTime(this.availabilityIntervals[this.availabilityIntervals.length - 1].to), 1);
    const [endX, endY] = this.getCoordinatesForPercent(this.getPercentsForTime(this.availabilityIntervals[0].from), 30);

    const pathData = [
      `M ${startX} ${startY}`, // Move
      `A ${width / 4} ${width / 4} 0 0 1 ${endX} ${endY}`, // Arc
      `L ${width / 2} ${width / 2}`, // Line
      `L ${startX} ${startY}`, // Line
    ].join(' ');

    let pathEl = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    pathEl.setAttribute('d', pathData);
    pathEl.setAttribute('fill', 'gray');
    pathEl.setAttribute("style", "stroke: #222; stroke-width: 1px;");
    svg.appendChild(pathEl);

    // top circle
    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', (width / 2) + "px");
    circle.setAttributeNS(null, 'cy', (width / 2) + "px");
    circle.setAttributeNS(null, 'r', (width / 2 - 30) + "px");
    circle.setAttributeNS(null, 'style', 'fill: #fff; stroke: #222; stroke-width: 1px;');
    svg.appendChild(circle);
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

  getCoordinatesForPercent(percent: number, minusLength: number) {
    const x = Math.cos(2 * Math.PI * percent);
    const y = Math.sin(2 * Math.PI * percent);

    let width = this.getSvgWidth();

    return [
      width * 0.5 + (width * 0.5 - minusLength) * x,
      width * 0.5 + (width * 0.5 - minusLength) * y
    ];
  }

  getSvgWidth() {
    return document.getElementById('svg-clock').getBoundingClientRect().width;
  }
}
