import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';
import { FloorModel } from '../../core/models/FloorModel';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  bookingPopupOpen: boolean = false;
  newBooking: Booking;
  buildingLayout: FloorModel[]=new Array<FloorModel>();
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

  async setDefaultData() {
    // set default filtes, now that we know the first floor
    this.previousFilters = {
      date: new Date()
    }

    await this.roomDataService.getFloors().then((floors) => {
      this.buildingLayout = floors;
    });

    // this.roomDataService.getDefaultRooms().then((defaultRooms) => {
    //   this.rooms = defaultRooms;
    //   this.displayedRooms = defaultRooms;
    // })

    // sort floors
    // this.previousFilters.floor = this.buildingLayout[0].floor;
    // this.roomDataService.getRooms(this.previousFilters).then((defaultRooms) => {
    //   this.rooms = defaultRooms;
    //   this.displayedRooms = defaultRooms;
    // })
  }

  updateRoomsBasedOnFilters(filters: any) {
    console.log("Filters component emited: ", filters);

    if (this.filteredRoomsAlreadyExist(filters)) {
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

  filteredRoomsAlreadyExist(filters: any): boolean {
    //if this.previousFilters date && floor is the same, than return true

    return false;
  }

  setDisplayedRooms(filters: any) {
    // this.displayedRooms = this.rooms.map((room) => { if(filters conditions) return room});
    // DELETE THIS
    this.displayedRooms = this.rooms;
  }

  createBooking(booking: any) {
    this.newBooking = booking;
    this.bookingPopupOpen = true;
  }

  addCreatedBooking(booking: any) {
    // add to the list of bookings of the booked room the new booking 
    console.log("Rooms list must be updated with: ", booking);
    // call setDisplayedRooms(this.previousFilters); after setting this.rooms with new booking

    this.closeBookingPopup();

    this.rooms.forEach(room => {
      if (room.id === booking.roomId) {
        room.bookings.push(booking);
      }
    });
  }
}
