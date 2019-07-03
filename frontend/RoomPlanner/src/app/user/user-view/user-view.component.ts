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

    await this.roomDataService.getFloors().then((floors) => {
      this.buildingLayout = <FloorModel[]>floors;
    });

    if (this.buildingLayout == undefined) {
      return;
    }
    this.buildingLayout.sort(function (a, b) { return a.floor - b.floor });

    let firstFloor: FloorModel;

    firstFloor = this.buildingLayout[0];

    this.previousFilters.floor = firstFloor.floor;

    await this.roomDataService.getSingleFloor(firstFloor.floor).then((floor) => {
      this.rooms = <RoomModel[]>floor.rooms;
      
      this.rooms.forEach(room => {
        let roomAux:RoomModel=new RoomModel().create(room);
        roomAux.reservations=[];
        let reservations = room.reservations;
        for (let i = 0; i < reservations.length; i++) {
          if (reservations[i].startDate.getDate() == new Date().getDate()) {
            roomAux.reservations.push(reservations[i])
          }
        }
        this.displayedRooms = new Array<RoomModel>();
        this.displayedRooms.push(roomAux);
      })
   
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
        
        let reservations = this.singleRoomSelected.reservations;
        let roomAux:RoomModel=new RoomModel().create(this.singleRoomSelected);
        roomAux.reservations=[];    
        for (let i = 0; i < reservations.length; i++) {
          if (reservations[i].startDate.getDate() == filters.startDate.getDate()) {
            roomAux.reservations.push(reservations[i])
           
          }
        }

        this.displayedRooms.push(roomAux);
      }
    } else {
      if (filters.minPersons != null) {
        this.rooms.forEach(room => {
          if (room.maxPersons == filters.minPersons) {
            let roomAux:RoomModel=new RoomModel().create(room);
            roomAux.reservations=[];
            let reservations = room.reservations;
            for (let i = 0; i < reservations.length; i++) {
              if (reservations[i].startDate.getDate() == filters.startDate.getDate()) {
                roomAux.reservations.push(reservations[i])
              }
            }
            this.displayedRooms.push(roomAux);
          }
        })
      } else {
        this.rooms.forEach(room => {
            let roomAux:RoomModel=new RoomModel().create(room);
            roomAux.reservations=[];
            let reservations = room.reservations;
            for (let i = 0; i < reservations.length; i++) {
              if (reservations[i].startDate.getDate() == filters.startDate.getDate()) {
                roomAux.reservations.push(reservations[i])
              }
            }
            this.displayedRooms.push(roomAux);
        })
      }
    }
  }

  createBooking(booking: any) {
    this.booking = booking;
    this.bookingPopupOpen = true;
  }

  addCreatedBooking(booking: Booking) {
    // add to the list of bookings of the booked room the new booking 
    console.log("addCreatedBooking: ", booking);
    // call setDisplayedRooms(this.previousFilters); after setting this.rooms with new booking

    this.closeBookingPopup();

    this.rooms.forEach(room => {
      if (room.id === booking.roomId) {
        room.reservations.push(booking);
      }
    });

    this.displayedRooms = [...this.displayedRooms];
    this.updateRoomsBasedOnFilters(this.previousFilters);
  }

  updateBooking(booking: Booking) {
    console.log("updateBooking: ", booking);
    this.closeBookingPopup();

    let index = 0;
    this.rooms.forEach(room => {
      if (room.id === booking.roomId ) {
        room.reservations.forEach(bookingItem => {
          if(bookingItem.id === booking.id) {
            room.reservations.splice(index,1);
            room.reservations.splice(index,0,booking);
          }
          index++;
        })
      }
    });

    this.displayedRooms = [...this.displayedRooms];
    this.updateRoomsBasedOnFilters(this.previousFilters);
  }

  deleteBooking(booking: Booking) {
    console.log("deleteBooking: ", booking);
    this.closeBookingPopup();

    let index = 0;
    this.rooms.forEach(room => {
      if (room.id === booking.roomId ) {
        room.reservations.forEach(bookingItem => {
          if(bookingItem.id === booking.id) {
            room.reservations.splice(index,1);
          }
          index++;
        })
      }
    });

    this.displayedRooms = [...this.displayedRooms];
    this.updateRoomsBasedOnFilters(this.previousFilters);
  }
}
