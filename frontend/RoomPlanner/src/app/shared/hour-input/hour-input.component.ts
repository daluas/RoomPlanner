import { Component, OnInit, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder } from '@angular/forms';


@Component({
  selector: 'app-hour-input',
  templateUrl: './hour-input.component.html',
  styleUrls: ['./hour-input.component.css']
})
export class HourInputComponent implements OnInit, OnChanges {


  @Output() hourEmitter: EventEmitter<any> = new EventEmitter()

  finalHour: Date;
  hourForm: FormGroup;

  @Input() hoursRecieved;
  @Input() minutesRecieved;

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.hourForm.setValue({ hour: this.hoursRecieved, minutes: this.minutesRecieved });
  }

  constructor(private fb: FormBuilder) {
    this.hourForm = this.fb.group({
      hour: ['00', [Validators.pattern('^([0-1][0-9])$|^(2[0-3])$')]],
      minutes: ['00', [Validators.pattern('^([0-5][0-9])$')]]
    });
  }

  updateHours() {
    this.finalHour = new Date(new Date().setHours(this.hourForm.value.hour, this.hourForm.value.minutes, 0, 0));
    this.hourEmitter.emit(this.finalHour);
  }

  updateMinutes() {
    this.finalHour = new Date(new Date().setHours(this.hourForm.value.hour, this.hourForm.value.minutes, 0, 0));
    this.hourEmitter.emit(this.finalHour);
  }

  onKeyUpHours() {

    if (this.hourForm.value.hour == undefined) {
      this.hourForm.setValue({ hour: '00', minutes: this.hourForm.value.minutes });
    }

    if (this.hourForm.value.hour == 23) {
      this.hourForm.setValue({ hour: '00', minutes: this.hourForm.value.minutes });
    } else {
      if (+this.hourForm.value.hour + 1 < 10) {
        this.hourForm.setValue({ hour: '0' + (+this.hourForm.value.hour + 1), minutes: this.hourForm.value.minutes });
      } else {
        this.hourForm.setValue({ hour: +this.hourForm.value.hour + 1, minutes: this.hourForm.value.minutes });
      }
    }
    this.updateHours();
  }

  onKeyUpMinutes() {

    if (this.hourForm.value.hour == undefined) {
      this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: '00' });
    }

    if (this.hourForm.value.minutes == 59) {
      this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: '00' });
      this.onKeyUpHours();
    } else {
      if (this.hourForm.value.minutes >= 30) {
        this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: '00' });
        this.onKeyUpHours();
      } else {
        this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: +'30' });
      }
    }
    this.updateMinutes();
  }

  onKeyDownHours() {
    if (this.hourForm.value.hour == undefined) {
      this.hourForm.setValue({ hour: 23, minutes: this.hourForm.value.minutes });
    }

    if (this.hourForm.value.hour == 0) {
      this.hourForm.setValue({ hour: 23, minutes: this.hourForm.value.minutes });
    } else
      if (+this.hourForm.value.hour - 1 < 10) {
        this.hourForm.setValue({ hour: '0' + (+this.hourForm.value.hour - 1), minutes: this.hourForm.value.minutes });
      } else {
        this.hourForm.setValue({ hour: +this.hourForm.value.hour - 1, minutes: this.hourForm.value.minutes });
      }
    this.updateHours();
  }

  onKeyDownMinutes() {
    if (this.hourForm.value.minutes == undefined) {
      this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: 30 });
    }

    if (this.hourForm.value.minutes == 0) {
      this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: +'30' });
      this.onKeyDownHours();
    } else {
      if (this.hourForm.value.minutes <= 30) {
        this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: '00' });
      } else {
        this.hourForm.setValue({ hour: this.hourForm.value.hour, minutes: +'30' });
      }
    }
  }
}
