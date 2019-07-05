import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BookingPopupComponent } from './booking-popup.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Component, OnInit, Input, ɵConsole, Output, EventEmitter, Inject, DebugElement } from '@angular/core';
import { BookingService } from 'src/app/core/services/booking/booking.service';
import { MatDatepickerModule, MatFormFieldModule, MatNativeDateModule, MatInputModule, MatSnackBarModule } from '@angular/material';
import { RoomsViewComponent } from '../../user/user-view/rooms-view/rooms-view.component';
import { Data } from '@angular/router';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { LoginBookingComponent } from '../login-booking/login-booking.component';
import { HourInputBookingComponent } from '../hour-input-booking/hour-input-booking.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SpinnerComponent } from '../spinner/spinner.component';
import { MaterialDesignModule } from '../../material-design/material-design.module';

describe('BookingPopupComponent', () => {
  let component: BookingPopupComponent;
  let fixture: ComponentFixture<BookingPopupComponent>;
  let de: DebugElement;
  let elem: HTMLElement;
  let routerMock = { navigate: (path: string) => { } }
  let submitButton: HTMLButtonElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookingPopupComponent, LoginBookingComponent, HourInputBookingComponent, SpinnerComponent ],
      imports: [
        MatDatepickerModule,
        MatFormFieldModule,
        MatNativeDateModule,
        MatInputModule,
        BrowserAnimationsModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        FormsModule,
        HttpClientTestingModule,
        RouterTestingModule, 
        MaterialDesignModule
      ]
    })
    .compileComponents().then(() => {
      fixture = TestBed.createComponent(BookingPopupComponent);
      component = fixture.componentInstance;;
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookingPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
