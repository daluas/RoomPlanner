import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoomsViewComponent } from './rooms-view.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { MaterialDesignModule } from 'src/app/material-design/material-design.module';
import { LoggedUser } from 'src/app/core/models/LoggedUser';
import { Booking } from '../../../core/models/BookingModel';
import { RoomModel } from '../../../core/models/RoomModel';

describe('RoomsViewComponent', () => {
  let component: RoomsViewComponent;
  let fixture: ComponentFixture<RoomsViewComponent>;
  let routerMock = { navigate: (path: string) => { } }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RoomsViewComponent],
      imports: [HttpClientTestingModule, MaterialDesignModule],
      providers: [{ provide: Router, useValue: routerMock },]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoomsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should group overlapping bookings for each room when groupeBookings function is called', () => {
    component.rooms = [ new RoomModel().create(
      {
        id: 1,
        name: "401",
        maxPersons: 10,
        reservations: [
          {
            id: 1,
            startDate: new Date(new Date().setHours(10, 0, 0, 0)),
            endDate: new Date(new Date().setHours(10, 30, 0, 0)),
            email: "user1@cegeka.ro",
            description: "this is a test description"
          },
          {
            id: 2,
            startDate: new Date(new Date().setHours(9, 0, 0, 0)),
            endDate: new Date(new Date().setHours(11, 0, 0, 0)),
            email: "user1@cegeka.ro",
            description: "this is a test description"
          },
          {
            id: 3,
            startDate: new Date(new Date().setHours(10, 30, 0, 0)),
            endDate: new Date(new Date().setHours(12, 0, 0, 0)),
            email: "user1@cegeka.ro",
            description: "this is a test description"
          },
          {
            id: 4,
            startDate: new Date(new Date().setHours(12, 0, 0, 0)),
            endDate: new Date(new Date().setHours(13, 0, 0, 0)),
            email: "user1@cegeka.ro",
            description: "this is a test description"
          },
          {
            startDate: new Date(new Date().setHours(14, 30, 0, 0)),
            endDate: new Date(new Date().setHours(15, 0, 0, 0)),
            email: "user10@geceka.ro"
          },
          {
            startDate: new Date(new Date().setHours(14, 30, 0, 0)),
            endDate: new Date(new Date().setHours(16, 0, 0, 0)),
            email: "user10@geceka.ro"
          },
        ]
      }), new RoomModel().create(
      {
        id: 2,
        name: "405",
        reservations: []
      })
    ];
    component.currentUser = new LoggedUser().create({ email: "user1@cegeka.ro" });

    component.groupeBookings();

    expect(component.groupedBookings.length).toEqual(2);
    expect(component.groupedBookings[0].length).toEqual(3);
    expect(component.groupedBookings[0][0].bookings.length).toEqual(3);
    expect(component.groupedBookings[0][1].bookings.length).toEqual(1);
    expect(component.groupedBookings[0][2].bookings.length).toEqual(2);
  });

  it('should return true when bookingsOverlap function is called with overlapping bookings', () => {
    let booking1 = new Booking().create({
      startDate: new Date(new Date().setHours(14, 0, 0, 0)),
      endDate: new Date(new Date().setHours(16, 0, 0, 0)),
      email: "user10@geceka.ro"
    });
    let booking2 = new Booking().create({
      startDate: new Date(new Date().setHours(14, 30, 0, 0)),
      endDate: new Date(new Date().setHours(15, 30, 0, 0)),
      email: "user10@geceka.ro"
    });

    let result = component.bookingsOverlap(booking1, booking2);

    expect(result).toBeTruthy();
  });

  it('should return false when bookingsOverlap function is called with bookings that do not overlap', () => {
    let booking1 = new Booking().create({
      startDate: new Date(new Date().setHours(12, 0, 0, 0)),
      endDate: new Date(new Date().setHours(15, 0, 0, 0)),
      email: "user10@geceka.ro"
    });
    let booking2 = new Booking().create({
      startDate: new Date(new Date().setHours(15, 0, 0, 0)),
      endDate: new Date(new Date().setHours(16, 0, 0, 0)),
      email: "user10@geceka.ro"
    });

    let result = component.bookingsOverlap(booking1, booking2);

    expect(result).toBeFalsy();
  });

  it('should return true when isValueInOpenInterval function is called with value in open interval', () => {
    let value = 1;
    let start = 0;
    let end = 2;

    let result = component.isValueInOpenInterval(value, start, end);

    expect(result).toBeTruthy();
  });

  it('should return true when isValueInOpenInterval function is called with value not in open interval', () => {
    let value = 1;
    let start = 1;
    let end = 2;

    let result = component.isValueInOpenInterval(value, start, end);

    expect(result).toBeFalsy();
  });

  it('should return true when isValueInInterval function is called with value in closed interval', () => {
    let value = 1;
    let start = 1;
    let end = 2;

    let result = component.isValueInInterval(value, start, end);

    expect(result).toBeTruthy();
  });

  it('should return true when isValueInInterval function is called with value not in closed interval', () => {
    let value = 0;
    let start = 1;
    let end = 2;

    let result = component.isValueInInterval(value, start, end);

    expect(result).toBeFalsy();
  });
});
