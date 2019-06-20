import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BookingPopupComponent } from './booking-popup.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Component, OnInit, Input, ɵConsole, Output, EventEmitter, Inject, DebugElement } from '@angular/core';
import { Booking } from '../models/Booking';
import { BookingService } from 'src/app/core/services/booking/booking.service';
import { MatDatepickerModule, MatFormFieldModule, MatNativeDateModule, MatInputModule } from '@angular/material';
import { RoomsViewComponent } from '../../user/user-view/rooms-view/rooms-view.component';
import { Data } from '@angular/router';
import { FormControl } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('BookingPopupComponent', () => {
  let component: BookingPopupComponent;
  let fixture: ComponentFixture<BookingPopupComponent>;
  let de: DebugElement;
  let elem: HTMLElement;
  let routerMock = { navigate: (path: string) => { } }
  let submitButton: HTMLButtonElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookingPopupComponent ],
      imports: [
        MatDatepickerModule,
        MatFormFieldModule,
        MatNativeDateModule,
        MatInputModule,
        BrowserAnimationsModule
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
