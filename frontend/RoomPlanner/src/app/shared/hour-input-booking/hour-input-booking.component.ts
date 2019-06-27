import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder } from '@angular/forms';
import { Time } from '@angular/common';
import { Timeouts } from 'selenium-webdriver';
import { TimeInterval } from 'rxjs/internal/operators/timeInterval';
import { Booking } from '../../core/models/BookingModel';
import { BookingPopupComponent } from '../booking-popup/booking-popup.component';
import { BookingService } from '../../core/services/booking/booking.service';
import { ɵINTERNAL_BROWSER_DYNAMIC_PLATFORM_PROVIDERS } from '@angular/platform-browser-dynamic';


@Component({
  selector: 'app-hour-input-booking',
  templateUrl: './hour-input-booking.component.html',
  styleUrls: ['./hour-input-booking.component.css']
})
export class HourInputBookingComponent implements OnInit {

  @Output() hourEmitter: EventEmitter<any> = new EventEmitter();
  @Output() statusMessage: EventEmitter<string> = new EventEmitter();
  @Output() disableBookButton: EventEmitter<boolean> = new EventEmitter();
  @Input() booking: Booking;

  finalStartHour: Date;
  finalEndHour: Date;
  startHourForm: FormGroup;
  endHourForm: FormGroup;
  invalidHours: boolean;
  status: string;
 
  ngOnInit() {

    
    if (this.booking) {
      this.startHourForm.setValue({ hour: this.booking.startDate.getHours(), minutes: this.booking.startDate.getMinutes() });
      this.endHourForm.setValue({ hour: this.booking.endDate.getHours(), minutes: this.booking.endDate.getMinutes() });

      this.startHourForm.markAsDirty();
      this.endHourForm.markAsDirty();
    }

    if (this.startHourForm.value.hour < 10) {
      this.startHourForm.setValue({ hour: '0' + (this.startHourForm.value.hour), minutes: this.startHourForm.value.minutes });
    }
    else if (this.startHourForm.value.minutes < 10) {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '0' + (this.startHourForm.value.minutes) });
    }
    else if (this.endHourForm.value.hour < 10) {
      this.endHourForm.setValue({ hour: '0' + (this.endHourForm.value.hour), minutes: this.endHourForm.value.minutes });
    }
    else if (this.endHourForm.value.minutes < 10) {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '0' + (this.endHourForm.value.minutes) });
    }
   
    this.finalStartHour = new Date();
    this.finalEndHour = new Date();

  }

  constructor(private fb: FormBuilder, public bookingService: BookingService) {
    this.startHourForm = this.fb.group({
      hour: ['00', [Validators.pattern('^([0-1][0-9])$|^(2[0-3])$')]],
      minutes: ['00', [Validators.pattern('^([0-5][0-9])$')]]
    });

    this.endHourForm = this.fb.group({
      hour: ['00', [Validators.pattern('^([0-1][0-9])$|^(2[0-3])$')]],
      minutes: ['00', [Validators.pattern('^([0-5][0-9])$')]]
    });

  }
  validateHours() {

    this.invalidHours = false;

    if(this.startHourForm.value.hour == this.endHourForm.value.hour && this.startHourForm.value.minutes >= this.endHourForm.value.minutes){
      this.invalidHours = true;
    }

    if (this.startHourForm.value.hour > this.endHourForm.value.hour) {
      this.invalidHours = true;
    }
  }
  prevalidation() {
    this.bookingService.prevalidation(this.booking).then((prevalidationStatus) => {
      this.status=prevalidationStatus;

      if(prevalidationStatus=="You can book" && this.invalidHours == false ){
        this.disableBookButton.emit(false);//emit(false)=buton enabled
      }
      else{
        this.disableBookButton.emit(true);//emit(true)=buton disabled
      }
      this.statusMessage.emit(prevalidationStatus);
    })
  }


  updateHours() {
    if ((this.startHourForm.value.hour == this.endHourForm.value.hour) && (this.startHourForm.value.minutes == this.endHourForm.value.minutes)) {
      if (this.endHourForm.value.minutes == '30') {
        this.endHourForm.setValue({ hour: this.endHourForm.value.hour + 1, minutes: '00' });
      }
      else {
        this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '30' });
      }
    }

    this.finalStartHour = new Date(new Date().setHours(this.startHourForm.value.hour, this.startHourForm.value.minutes, 0, 0));
    this.finalEndHour = new Date(new Date().setHours(this.endHourForm.value.hour, this.endHourForm.value.minutes, 0, 0));

    this.hourEmitter.emit(this.finalStartHour);
    this.hourEmitter.emit(this.finalEndHour);
     this.validateHours();
     this.prevalidation();
   
  }

  updateMinutes() {
    if ((this.startHourForm.value.hour == this.endHourForm.value.hour) && (this.startHourForm.value.minutes == this.endHourForm.value.minutes)) {
      if (this.endHourForm.value.minutes == '30') {
        this.endHourForm.setValue({ hour: this.endHourForm.value.hour + 1, minutes: '00' });
      }
      else {
        this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '30' });
      }
    }

    this.finalStartHour = new Date(new Date().setHours(this.startHourForm.value.hour, this.startHourForm.value.minutes, 0, 0));
    this.finalEndHour = new Date(new Date().setHours(this.endHourForm.value.hour, this.endHourForm.value.minutes, 0, 0));

    this.hourEmitter.emit(this.finalStartHour);
    this.hourEmitter.emit(this.finalEndHour);
    this.validateHours();
    this.prevalidation();
    
  }

  onKeyUpHoursStartDate() {
    this.updateHours();
    if (this.startHourForm.value.hour == undefined) {
      this.startHourForm.setValue({ hour: '00', minutes: this.startHourForm.value.minutes });
    }
    if (this.startHourForm.value.hour == 23) {
      this.startHourForm.setValue({ hour: '00', minutes: this.startHourForm.value.minutes });
    } else {
      if (+this.startHourForm.value.hour + 1 < 10) {
        this.startHourForm.setValue({ hour: '0' + (+this.startHourForm.value.hour + 1), minutes: this.startHourForm.value.minutes });
      } else {
        this.startHourForm.setValue({ hour: +this.startHourForm.value.hour + 1, minutes: this.startHourForm.value.minutes });
      }
    }
    this.updateHours();
  }

  onKeyUpHoursEndDate() {
    this.updateHours();
    if (this.endHourForm.value.hour == undefined) {
      this.endHourForm.setValue({ hour: '00', minutes: this.endHourForm.value.minutes });
    }
    else if (this.endHourForm.value.hour == 23) {
      this.endHourForm.setValue({ hour: '00', minutes: this.endHourForm.value.minutes });
    } else {
      if (+this.endHourForm.value.hour + 1 < 10) {
        this.endHourForm.setValue({ hour: '0' + (+this.endHourForm.value.hour + 1), minutes: this.endHourForm.value.minutes });
      } else {
        this.endHourForm.setValue({ hour: +this.endHourForm.value.hour + 1, minutes: this.endHourForm.value.minutes });
      }
    }
    this.updateHours();
  }

  onKeyUpMinutesStartDate() {
    this.updateMinutes();
    if (this.startHourForm.value.hour == undefined) {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '00' });
    }
    if (this.startHourForm.value.minutes == 30) {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '00' });
      this.onKeyUpHoursStartDate();
    } else {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '30' });
    }
    this.updateMinutes();
  }

  onKeyUpMinutesEndDate() {
    this.updateMinutes();
    if (this.endHourForm.value.hour == undefined) {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '00' });
    }
    if (this.endHourForm.value.minutes == 30) {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '00' });
      this.onKeyUpHoursEndDate();
    } else {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '30' });
    }
    this.updateMinutes();
  }

  onKeyDownHoursStartDate() {
    this.updateHours();
    if (this.startHourForm.value.hour == undefined) {
      this.startHourForm.setValue({ hour: 23, minutes: this.startHourForm.value.minutes });
    }
    if (this.startHourForm.value.hour == 0) {
      this.startHourForm.setValue({ hour: 23, minutes: this.startHourForm.value.minutes });
    } else
      if (+this.startHourForm.value.hour - 1 < 10) {
        this.startHourForm.setValue({ hour: '0' + (+this.startHourForm.value.hour - 1), minutes: this.startHourForm.value.minutes });
      } else {
        this.startHourForm.setValue({ hour: +this.startHourForm.value.hour - 1, minutes: this.startHourForm.value.minutes });
      }
    this.updateHours();
  }

  onKeyDownMinutesStartDate() {
    this.updateMinutes();
    if (this.startHourForm.value.minutes == undefined) {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '00' });
    }
    if (this.startHourForm.value.minutes == 0) {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '30' });
      this.onKeyDownHoursStartDate();
    } else {
      this.startHourForm.setValue({ hour: this.startHourForm.value.hour, minutes: '00' });
    }
    this.updateMinutes();
  }

  onKeyDownHoursEndDate() {
    this.updateHours();
    if (this.endHourForm.value.hour == undefined) {
      this.endHourForm.setValue({ hour: 23, minutes: this.endHourForm.value.minutes });
    }
    if (this.endHourForm.value.hour == 0) {
      this.endHourForm.setValue({ hour: 23, minutes: this.endHourForm.value.minutes });
    } else
      if (+this.endHourForm.value.hour - 1 < 10) {
        this.endHourForm.setValue({ hour: '0' + (+this.endHourForm.value.hour - 1), minutes: this.endHourForm.value.minutes });
      } else {
        this.endHourForm.setValue({ hour: +this.endHourForm.value.hour - 1, minutes: this.endHourForm.value.minutes });
      }
    this.updateHours();
  }

  onKeyDownMinutesEndDate() {
    this.updateMinutes();
    if (this.endHourForm.value.minutes == undefined) {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '00' });
    }
    if (this.endHourForm.value.minutes == 0) {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '30' });
      this.onKeyDownHoursStartDate();
    } else {
      this.endHourForm.setValue({ hour: this.endHourForm.value.hour, minutes: '00' });
    }
    this.updateMinutes();
  }



}