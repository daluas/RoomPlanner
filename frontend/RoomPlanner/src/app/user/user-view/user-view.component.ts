import { Component, OnInit, ViewChild } from '@angular/core';

import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';
import { Booking } from '../../core/models/BookingModel';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  bookingPopupOpen: boolean = false;
  booking: Booking;
  buildingLayout: any;
  rooms: any[];
  displayedRooms: any[];
  previousFilters: any;

 
  constructor(public roomDataService: RoomDataService) { }

  ngOnInit(): void {
    this.setDefaultData();
    // set interval for room update ~ each 1 min
  }

  closeBookingPopup() {
    this.bookingPopupOpen = false;
  }

  setDefaultData() {
    // set default filtes, now that we know the first floor
    this.previousFilters = {
      date: new Date()
    }

    this.roomDataService.getBuildingLayout().then((buildingLayout) => {
      this.buildingLayout = buildingLayout;
    });

    this.roomDataService.getDefaultRooms().then((defaultRooms) => {
      this.rooms = defaultRooms;
      this.displayedRooms = defaultRooms;
    })
  }

  updateRoomsBasedOnFilters(filters: any) {
    console.log("Filters component emited: ", filters);

    if (this.filteredRoomsAlreadyExist(filters)){
      this.setDisplayedRooms(filters);
    } else {
      this.roomDataService.getRooms(filters).then((rooms) => {
        this.rooms = rooms;
        this.setDisplayedRooms(filters);
      })
    }

    this.previousFilters = filters;

    // !!! IMPORTANT - make this work with filters date field;
    this.previousFilters = {
      date: new Date()
    }
  }

  filteredRoomsAlreadyExist(filters: any): boolean{
    //if this.previousFilters date && floor is the same, than return true

    return false;
  }

  setDisplayedRooms(filters: any){
    // this.displayedRooms = this.rooms.map((room) => { if(filters conditions) return room});
    // DELETE THIS
    this.displayedRooms = this.rooms;
  }

  createBooking(booking: any) {
    this.booking = booking;
    this.bookingPopupOpen = true;
  }

  addCreatedBooking(booking: any) {
    // add to the list of bookings of the booked room the new booking 
    console.log("Rooms list must be updated with: ", booking);
    // call setDisplayedRooms(this.previousFilters); after setting this.rooms with new booking
  }
}
