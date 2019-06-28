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
  rooms: RoomModel[]=new Array<RoomModel>();
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

    this.buildingLayout = await this.roomDataService.getFloors();

    this.buildingLayout.sort(function (a, b) { return a.floor - b.floor });

    let firstFloor: FloorModel;

    firstFloor = this.buildingLayout[0];

    this.previousFilters.floor = firstFloor.floor;

    this.roomDataService.getSingleFloor(firstFloor.floor).then((floor) => {
      this.rooms = <RoomModel[]>floor.rooms;
      this.displayedRooms = <RoomModel[]>floor.rooms;
    })

  }

  roomIsSelectedOnFilters(room: RoomModel) {
    console.log("Room emited: ", room);

    this.roomDataService.getSingleFloor(room.floor).then((floor)=>{
      floor.rooms.forEach(r=>{
        if(r.name==room.name){
          this.rooms.push(r);
        }
      })
      
    })
    this.singleRoomSelected = this.rooms.pop();
  }

  updateRoomsBasedOnFilters(filters: Filters) {
    console.log("Filters component emited: ", filters);

    if (this.filteredRoomsAlreadyExist(filters)) {
      console.log("aceleasi filtre!");
      this.setDisplayedRooms(filters);
    } else {
      this.roomDataService.getRoomsByFilter(filters).then((rooms) => {
        console.log("filtre diferite!");
        this.rooms = <RoomModel[]>rooms;
        this.setDisplayedRooms(filters);
      })
    }
    this.previousFilters = new Filters().create(filters);
  }

  filteredRoomsAlreadyExist(filters: Filters): boolean {
    if (this.verifyDate(this.previousFilters.startDate, filters.startDate) &&
      this.previousFilters.floor == filters.floor) {
      return true;
    }
    return false;
  }

  verifyDate(date1: Date, date2: Date): boolean {
    if (date1.getFullYear() == date2.getFullYear()) {
      if (date1.getMonth() == date2.getMonth()) {
        if (date1.getDate() == date2.getDate()) {
          return true;
        }
      }
    }
    return false;
  }

  setDisplayedRooms(filters: Filters) {

    console.log(`camera selectata de user:`);
    console.log(this.singleRoomSelected);

    this.displayedRooms = [];
  
    if (this.singleRoomSelected != null) {
      if (this.roomDataService.verifyRoomAvailabilityByFilters(this.singleRoomSelected, filters)) {
        this.displayedRooms.push(this.singleRoomSelected);
      }
    } else {
      this.displayedRooms = this.rooms.map((room) => {
        if (filters.minPersons != null && filters.minPersons == room.maxPersons) {
          if (this.roomDataService.verifyRoomAvailabilityByFilters(room, filters)) {
            return room;
          }
        } else {
          console.log(room);
          console.log(this.roomDataService.verifyRoomAvailabilityByFilters(room, filters))
          if (this.roomDataService.verifyRoomAvailabilityByFilters(room, filters)) {
            return room;
          }
        }
      }
      );
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
