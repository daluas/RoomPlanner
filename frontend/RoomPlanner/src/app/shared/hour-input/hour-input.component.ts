import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder } from '@angular/forms';
import { Time } from '@angular/common';
import { Timeouts } from 'selenium-webdriver';
import { TimeInterval } from 'rxjs/internal/operators/timeInterval';


@Component({
  selector: 'app-hour-input',
  templateUrl: './hour-input.component.html',
  styleUrls: ['./hour-input.component.css']
})
export class HourInputComponent implements OnInit {

  @Output() hourEmitter: EventEmitter<any> = new EventEmitter()

  finalHour: Date;
  hourForm: FormGroup;
  ngOnInit() {

  }


  constructor(private fb: FormBuilder) {
    this.hourForm = this.fb.group({
      hour: ['00', [Validators.pattern('^([0-1][0-9])$|^(2[0-3])$')]],
      minutes: ['00', [Validators.pattern('^([0-5][0-9])$')]]
    });
  }

  updateHours() {
    console.log("--update hours ---")
    console.log(this.hourForm.value.hour);
    console.log(this.hourForm.value.minutes);

    this.finalHour = new Date(new Date().setHours(this.hourForm.value.hour, this.hourForm.value.minutes, 0, 0));
    console.log(this.finalHour);
    this.hourEmitter.emit(this.finalHour);
  }

  updateMinutes() {
    console.log("---update minutes---")
    console.log(this.hourForm.value.hour);
    console.log(this.hourForm.value.minutes);

    this.finalHour = new Date(new Date().setHours(this.hourForm.value.hour, this.hourForm.value.minutes, 0, 0));
    console.log(this.finalHour);
    this.hourEmitter.emit(this.finalHour);
  }

  onKeyUpHours() {

    if (this.hourForm.value.hour == 23) {
      this.hourForm.value.hour = +'00';
    } else {
      if (+this.hourForm.value.hour + 1 < 10) {
        this.hourForm.value.hour = '0' + (+this.hourForm.value.hour + 1);
      } else {
        this.hourForm.value.hour = +this.hourForm.value.hour + 1;
      }
    }

    this.updateHours();

  }

  onKeyUpMinutes() {

    if (this.hourForm.value.minutes == 59) {
      this.hourForm.value.minutes = +'00';
      this.onKeyUpHours();
    } else {
      if (+this.hourForm.value.minutes + 1 < 10) {
        this.hourForm.value.minutes = '0' + (+this.hourForm.value.minutes + 1);
      }
      else {
        this.hourForm.value.minutes = +this.hourForm.value.minutes + 1;
      }
    }
    this.updateMinutes();
  }

  onKeyDownHours() {
    if (this.hourForm.value.hour == undefined) {
      this.hourForm.value.hour = 23;
    }

    if (this.hourForm.value.hour == 0) {
      this.hourForm.value.hour = 23;
    } else
      if (+this.hourForm.value.hour - 1 < 10) {
        this.hourForm.value.hour = '0' + (+this.hourForm.value.hour - 1);
      } else {
        this.hourForm.value.hour = +this.hourForm.value.hour - 1;
      }
    this.updateHours();
  }

  onKeyDownMinutes() {
    if (this.hourForm.value.minutes == undefined) {
      this.hourForm.value.minutes = 30;
    }

    if (this.hourForm.value.minutes == 0) {
      this.hourForm.value.minutes = 59;
      
      this.onKeyDownHours();

    } else
      if (+this.hourForm.value.minutes - 1 < 10) {
        this.hourForm.value.minutes = '0' + (+this.hourForm.value.minutes - 1);
      }
      else {
        this.hourForm.value.minutes = +this.hourForm.value.minutes - 1;
      }

  }


  // onClearClick(){
  //   this.updateHours(' ');
  //   this.updateMinutes(' ');
  // }
}
