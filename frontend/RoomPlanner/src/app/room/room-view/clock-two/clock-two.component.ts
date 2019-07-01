import { Component, OnInit, ElementRef, ViewChild, AfterViewInit, Renderer2, OnChanges, SimpleChanges } from '@angular/core';
import * as d3 from 'd3';
import { D3DragEvent, PieArcDatum } from 'd3';


@Component({
  selector: 'app-clock-two',
  templateUrl: './clock-two.component.html',
  styleUrls: ['./clock-two.component.css']
})


export class ClockTwoComponent implements OnInit, AfterViewInit, OnChanges {

  r: number;
  coords: { x: number, y: number; };
  offset: { x: number; y: number; };
  selectedElement = null;

  @ViewChild('svgContainer', { read: ElementRef, static: false })
  public svgContainer: ElementRef

  private renderer: Renderer2;

  private draggie: any
  constructor() {

  }

  ngOnInit() {
    let amtPerSec = 3075.64;

    let margin = {
      top: 40,
      right: 40,
      bottom: 40,
      left: 40
    }

    let radians = 0.0174532925;

    let r = 200;

    let secR = r + 16;
    let hourR = r - 40;
    console.log(secR, hourR);
    let hourHandLength = 2 * r / 3;
    let minuteHandLength = r;
    let secondHandLength = r - 12;

    let w = +d3.select('figure').style("width").split("px")[0] - margin.left - margin.right;
    let h = +d3.select('figure').style("height").split("px")[0] - margin.top - margin.bottom;
    console.log(w, h, r);
    this.r = r;
    console.log("this radius", this.r)

    this.draggie = d3.drag()
      .on('start', this.dragstart)
      .on('drag', this.drag)
      .on('end', this.dragend);

    let secondScale = d3.scaleLinear()
      .range([0, 354])
      .domain([0, 59]); // not initialized

    let minuteScale = d3.scaleLinear()
      .range([0, 354])
      .domain([0, 59]);

    let hourScale = d3.scaleLinear()
      .range([0, 330])
      .domain([0, 11]);

    let handData = [
      {
        type: 'hour',
        value: 0,
        length: -hourHandLength,
        scale: hourScale
      },
      {
        type: 'minute',
        value: 30,
        length: -minuteHandLength,
        scale: minuteScale
      },
      {
        type: 'second',
        value: 0,
        length: -secondHandLength,
        scale: secondScale
      }
    ];

    let t = new Date();

    handData[0].value = (t.getHours() % 12) + t.getMinutes() / 60;
    handData[1].value = t.getMinutes();
    handData[2].value = t.getSeconds();

    console.log(handData);


    let svg = d3.select('svg')
      .style('width', `${w + margin.left + margin.right}`)
      .style('height', `${h + margin.top + margin.bottom}`);

    //svg translation
    let g = svg.append('g')
      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

    // circle clock container
    let face = g.append('g')
      .attr('transform', 'translate(' + r + ',' + r + ')');

    //cercul
    face.append('circle')
      .style('stroke', '#673ab7')
      .style('stroke-width', 2)
      .style('fill', 'white') // class outline
      .attr('r', r)
      .attr('cx', 0)
      .attr('cy', 0)

    //gradatiile de secunda
    face.selectAll('.second')
      .data(d3.range(0, 60))
      .enter()
      .append('line')
      .style('class', 'secondLabel')
      .style('stroke', 'gray')
      .style('stroke-width', 2)   //class second
      .attr('x1', 0)
      .attr('x2', 0)
      .attr('y1', r)
      .attr('y2', r - 10)
      .attr('transform', function (d) {
        return `rotate(${minuteScale(d)})`;
      }
      )
      .attr('data-name', "secondsTick")


    //minutes labels
    face.selectAll('.second-label')
      .data(d3.range(5, 61, 5))
      .enter().append('text')
      .style('stroke', "black")
      .style("stroke-width", 1)
      .text(function (d) { return d; })
      .style('text-anchor', 'middle')
      .attr('x', function (d) {
        return secR * Math.sin(secondScale(d) * radians);
      })
      .attr('y', function (d) {
        return -secR * Math.cos(secondScale(d) * radians) + 8;
      })
      .style('fill', 'white')
      .attr('data-name', "minuteLabel")


    // HOUR TICKS
    face.selectAll('.hour')
      .data(d3.range(0, 12))
      .enter().append('line')
      .style('text-color', 'red')
      .style('stroke', "green")
      .style('stroke-width', 4)
      .attr('x1', 0)
      .attr('y1', r - 20)
      .attr('x2', 0)
      .attr('y2', r)
      .style('transform', function (d) {
        return `rotate(${hourScale(d)}deg)`;
      })



    let hands = face.append('g');


    // // cursoare (acele)
    hands.selectAll('line')
      .data(handData)
      .enter().append('line')
      .attr('data-class', function (d) { return d.type + '-hand'; })
      .attr('x1', 0)
      .attr('y1', 0)
      .attr('x2', function (d) {
        return d.length * Math.cos(d.value);
      })
      .attr('y2', function (d) {
        return d.length * Math.sin(d.value);
      })
      .attr('stroke', (d): string => {
        if (d.type == 'hour') {
          return '#673ab7'
        }
        if (d.type == "minute") {
          return "red"
        }
        if (d.type == "second") {
          return "yellow"
        }
      }
      )
      .style('stroke-width', 7)
      .style('stroke-linecap', 'round')
      .call(this.draggie)


    face.selectAll('.hour-label')
      .data(d3.range(3, 13, 3))
      .enter().append('text')
      .text(function (d) { return d; })
      .attr('x', function (d) {
        return hourR * Math.sin(hourScale(d) * radians);
      })
      .attr('y', function (d) {
        return -hourR * Math.cos(hourScale(d) * radians) + 9;
      })
      .style('font-size', 20)
      .style('stroke', 'green')
      .style('text-anchor', 'middle')



    // // WORKS -  small circle in middle to cover hands
    face.append('circle')
      .attr('cx', 0)
      .attr('cy', 0)
      .attr('r', 15)
      .attr('fill', 'white')
      .attr('stroke', '#374140')
      .attr('stroke-width', 3)
  }

  ngOnChanges(changes: SimpleChanges): void {

  }

  ngAfterViewInit() {

  }

  dragstart(event) {
    console.log("nino nino")
  }

  drag(event, d, i) {

    let rr = Math.abs(event.length)
    let rad = Math.atan2(d3.event.y, d3.event.x);
    console.log(rad)
    console.log(rr * Math.cos(rad))

    console.log(event);
    let that :any  = this
    //it s ok with this "error"
    d3.select(that)
      .attr('x2', function (d) {
        return rr * Math.cos(rad);
      })
      .attr('y2', function (d) {
        return rr * Math.sin(rad);
      })
  }

  dragend(event) {
  }


}