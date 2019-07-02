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
    this.drawClock();
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

  drawClock() {
    let width = this.getSvgWidth();

    let svg = document.getElementById('svg-clock');
    svg.innerHTML = "";

    this.availabilityIntervals.forEach(interval => {
      let [startX, startY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.from), 1);
      let [endX, endY] = this.getCoordinatesForPercent(this.getPercentsForTime(interval.to), 1);

      let pathData = [
        `M ${startX} ${startY}`, // Move
        `A ${width / 2 - 2} ${width / 2 - 2} 0 0 1 ${endX} ${endY}`, // Arc
        `L ${width / 2} ${width / 2}`, // Line
        `L ${startX} ${startY}`, // Line
      ].join(' ');

      let pathEl = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      pathEl.setAttribute('d', pathData);
      pathEl.setAttribute('fill', interval.available ? '#fff' : '#FF5B3E');
      pathEl.setAttribute("style", "stroke: #222; stroke-width: 2px;");
      svg.appendChild(pathEl);
    });

    // top circle
    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', (width / 2) + "px");
    circle.setAttributeNS(null, 'cy', (width / 2) + "px");
    circle.setAttributeNS(null, 'r', (width / 2 - 30) + "px");
    circle.setAttributeNS(null, 'style', 'fill: #fff; stroke: #222; stroke-width: 2px;');
    svg.appendChild(circle);

    //beginning line
    let [lineX, lineY] = this.getCoordinatesForPercent(this.getPercentsForTime(this.availabilityIntervals[0].from), 1);
    let line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    line.setAttribute('x1', lineX.toString());
    line.setAttribute('y1', lineY.toString());
    line.setAttribute('x2', width / 2 + "");
    line.setAttribute('y2', width / 2 + "");
    line.setAttribute("style", "stroke: #ffaa21; stroke-width: 3px;");
    svg.appendChild(line);

    // clock numbers
    for (let i = 1; i <= 24; i++) {
      let date = new Date().setHours(Math.floor(i / 2), (i % 2) * 30, 0, 0);
      let [x, y] = this.getCoordinatesForPercent(this.getPercentsForTime(date), 50);

      if (i % 2 === 1) {
        // point
        let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
        circle.setAttributeNS(null, 'cx', x + "px");
        circle.setAttributeNS(null, 'cy', y + "px");
        circle.setAttributeNS(null, 'r', "3px");
        circle.setAttributeNS(null, 'style', 'fill: #222;');
        svg.appendChild(circle);
      } else {
        // text
        let text = document.createElementNS("http://www.w3.org/2000/svg", 'text');
        text.setAttributeNS(null, 'x', x + "px");
        text.setAttributeNS(null, 'y', y + "px");
        text.setAttributeNS(null, 'dy', 2 + "px");
        text.setAttributeNS(null, 'alignment-baseline', "middle");
        text.setAttributeNS(null, 'text-anchor', "middle");
        text.setAttributeNS(null, "style", "font-size: 14px; color: #222; font-weight: bold;");
        text.innerHTML = Math.floor(i / 2).toString();
        svg.appendChild(text);
      }
    }

    // minutes line
    let [minutesPercent, hoursPercent] = this.getPercentsForNow();
    [lineX, lineY] = this.getCoordinatesForPercent(minutesPercent, 70);
    line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    line.setAttribute('x1', lineX.toString() + "px");
    line.setAttribute('y1', lineY.toString() + "px");
    line.setAttribute('x2', width / 2 + "px");
    line.setAttribute('y2', width / 2 + "px");
    line.setAttribute("style", "stroke: #222; stroke-width: 3px;");
    svg.appendChild(line);

    // hours line
    [lineX, lineY] = this.getCoordinatesForPercent(hoursPercent, 100);
    line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    line.setAttribute('x1', lineX.toString() + "px");
    line.setAttribute('y1', lineY.toString() + "px");
    line.setAttribute('x2', width / 2 + "px");
    line.setAttribute('y2', width / 2 + "px");
    line.setAttribute("style", "stroke: #222; stroke-width: 5px;");
    svg.appendChild(line);

    // selection pins
    this.drawSelectionPins();

    // time circle
    circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', (width / 2) + "px");
    circle.setAttributeNS(null, 'cy', (width / 2) + "px");
    circle.setAttributeNS(null, 'r', "50px");
    circle.setAttributeNS(null, 'style', 'fill: #222; stroke: #222; stroke-width: 2px;');
    svg.appendChild(circle);

    // hour
    let text = document.createElementNS("http://www.w3.org/2000/svg", 'text');
    text.setAttributeNS(null, 'x', (width / 2) + "px");
    text.setAttributeNS(null, 'y', (width / 2) + "px");
    text.setAttributeNS(null, 'dy', 3 + "px");
    text.setAttributeNS(null, 'alignment-baseline', "middle");
    text.setAttributeNS(null, 'text-anchor', "middle");
    text.setAttributeNS(null, "style", "font-size: 24px; fill: #fff; font-weight: bold;");
    let now = new Date();
    text.innerHTML = (now.getHours() < 10 ? '0' + now.getHours() : now.getHours().toString()) + " : " + (now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes().toString());
    svg.appendChild(text);
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
