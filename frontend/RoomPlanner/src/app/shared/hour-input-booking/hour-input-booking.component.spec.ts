import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MaterialDesignModule } from '../../material-design/material-design.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HourInputBookingComponent } from 'src/app/shared/hour-input-booking/hour-input-booking.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';

describe('HourInputComponentBooking', () => {
  let component: HourInputBookingComponent;
  let fixture: ComponentFixture<HourInputBookingComponent>;
  let routerMock = { navigate: (path: string) => { } }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HourInputBookingComponent ]
   ,
    imports: [
      MaterialDesignModule,
      BrowserAnimationsModule,
      ReactiveFormsModule,
      FormsModule,
      HttpClientTestingModule
    ],
    providers: [{ provide: Router, useValue: routerMock}]
  })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HourInputBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});