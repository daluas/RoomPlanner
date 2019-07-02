import { Component, OnInit, ViewChild } from '@angular/core';

import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';
import { FloorModel } from '../../core/models/FloorModel';
import { Filters } from '../../shared/models/Filters';
import { RoomModel } from '../../core/models/RoomModel';
import { Booking } from '../../core/models/BookingModel';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  bookingPopupOpen: boolean = false;
  booking: Booking;
  newBooking: Booking;
  buildingLayout: FloorModel[] = new Array<FloorModel>();
  rooms: RoomModel[] = new Array<RoomModel>();
  displayedRooms: RoomModel[];
  singleRoomSelected: RoomModel;
  previousFilters: Filters;


  constructor(public roomDataService: RoomDataService) { }

  ngOnInit(): void {
    this.setDefaultData();
    // set interval for room update ~ each 1 min
  }

  closeBookingPopup() {
    this.bookingPopupOpen = false;
  }


  async setDefaultData() {

    this.previousFilters = new Filters().create({
      startDate: new Date(new Date().setHours(0, 0, 0, 0)),
      endDate: new Date(new Date().setHours(0, 0, 0, 0)),
      minPersons: null,
      floor: null
    });

    await this.roomDataService.getFloors().then((floors)=>{
      this.buildingLayout = <FloorModel[]>floors;
    });
      
    if(this.buildingLayout == undefined){
      return;
    }
    this.buildingLayout.sort(function (a, b) { return a.floor - b.floor });

    let firstFloor: FloorModel;

    firstFloor = this.buildingLayout[0];

    this.previousFilters.floor = firstFloor.floor;

    await this.roomDataService.getSingleFloor(firstFloor.floor).then((floor) => {
      this.rooms = <RoomModel[]>floor.rooms;
      this.displayedRooms = <RoomModel[]>floor.rooms;
      console.log("Rooms by default", this.rooms)
    })

  }

  async updateRoomsBasedOnFilters(filters: Filters) {

    console.log("Filters component emited: ", filters);

    if (filters.roomSelected != null || filters.roomSelected != undefined) {
      await this.roomDataService.getSingleFloor(filters.roomSelected.floor).then((floor) => {
        floor.rooms.forEach(r => {
          if (r.name == filters.roomSelected.name) {
            this.singleRoomSelected = r;
            this.checkRoomsBasedOnFilters(filters);
          }
        })
      })
    } else {
      this.singleRoomSelected = null;
      this.checkRoomsBasedOnFilters(filters);
    }
  }

  async checkRoomsBasedOnFilters(filters: Filters) {

    console.log("single room", this.singleRoomSelected)
    this.displayedRooms = [];

    if (this.filteredRoomsAlreadyExist(filters)) {
      this.setDisplayedRooms(filters);
    } else {
      this.rooms = [];
      await this.roomDataService.getRoomsByFilter(filters).then((rooms) => {
        this.rooms = <RoomModel[]>rooms;
        this.setDisplayedRooms(filters)
      })
    }
    this.previousFilters = new Filters().create(filters);
  }

  filteredRoomsAlreadyExist(filters: Filters): boolean {
    if (this.previousFilters.startDate.getTime() == filters.startDate.getTime() &&
      this.previousFilters.endDate.getTime() == filters.endDate.getTime() &&
      this.previousFilters.floor == filters.floor) {
      return true;
    }
    return false;
  }

  setDisplayedRooms(filters: Filters) {

    this.displayedRooms = [];
    if (this.singleRoomSelected != null) {
      if (this.roomDataService.verifyRoomAvailabilityByFilters(this.singleRoomSelected, filters)) {
        console.log('blabla')
        this.displayedRooms.push(this.singleRoomSelected);
      }
    } else {
      if (filters.minPersons != null) {
        this.rooms.forEach(room=>{
          if(room.maxPersons==filters.minPersons){
            this.displayedRooms.push(room);
          }
        })
      }else{
        this.displayedRooms=<RoomModel[]>this.rooms;
      }
    }
  }

  createBooking(booking: any) {
    this.booking = booking;
    this.bookingPopupOpen = true;
  }

  addCreatedBooking(booking: any) {
    // add to the list of bookings of the booked room the new booking 
    console.log("Rooms list must be updated with: ", booking);
    // call setDisplayedRooms(this.previousFilters); after setting this.rooms with new booking

    this.closeBookingPopup();

    this.rooms.forEach(room => {
      if (room.id === booking.roomId) {
        room.reservations.push(booking);
      }
    });

    this.rooms = [...this.rooms];
  }
}
