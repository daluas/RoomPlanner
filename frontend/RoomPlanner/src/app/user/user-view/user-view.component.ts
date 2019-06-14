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
  rooms: any[]

  constructor(public roomDataService: RoomDataService){}

  ngOnInit(): void { 
    this.setDefaultData();
  }

  closeBookingPopup(){
    this.bookingPopupOpen = false;
  }

  setDefaultData(){
    this.roomDataService.getBuildingLayout().then((buildingLayout)=>{ 
      this.buildingLayout = buildingLayout
    });

    this.roomDataService.getDefaultRooms().then((defaultRooms) => {
      this.rooms = defaultRooms;
    })
  }

  updateRoomsBasedOnFilters(filters: any){
    console.log("Filters component emited: ", filters);

    this.roomDataService.getRooms(filters).then((rooms) => {
      this.rooms = rooms;
    })
  }

  createBooking(booking: any){
    this.newBooking = booking;
    this.bookingPopupOpen = true;
  }

  addCreatedBooking(booking: any){
    // add to the list of bookings of the booked room the new booking 
    console.log("Rooms list must be updated with: ", booking);
  }
}
