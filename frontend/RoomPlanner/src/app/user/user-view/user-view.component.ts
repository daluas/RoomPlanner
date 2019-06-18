import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  bookingPopupOpen: boolean = false;
  newBooking: Booking;
  buildingLayout: any;
  rooms: any[];
  displayedRooms: any[];
  previousFilters: any;

  constructor(public roomDataService: RoomDataService) { }

  ngOnInit(): void {
    this.setDefaultData();

    // set interval for room update ~ each 1 min or 30s
  }

  closeBookingPopup() {
    this.bookingPopupOpen = false;
  }

  setDefaultData() {
    this.roomDataService.getBuildingLayout().then((buildingLayout) => {
      this.buildingLayout = buildingLayout;

      // set default filtes, now that we know the first floor
      this.previousFilters = {}
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
    this.newBooking = booking;
    this.bookingPopupOpen = true;
  }

  addCreatedBooking(booking: any) {
    // add to the list of bookings of the booked room the new booking 
    console.log("Rooms list must be updated with: ", booking);
    // call setDisplayedRooms(this.previousFilters); after setting this.rooms with new booking
  }
}
