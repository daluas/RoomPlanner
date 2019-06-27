import { Component, OnInit, ViewChild } from '@angular/core';
import { Booking } from 'src/app/shared/models/Booking';
import { RoomDataService } from 'src/app/core/services/room-data/room-data.service';
import { FloorModel } from '../../core/models/FloorModel';
import { Filters } from '../../shared/models/Filters';
import { RoomModel } from '../../core/models/RoomModel';


@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {

  bookingPopupOpen: boolean = false;
  newBooking: Booking;
  buildingLayout: FloorModel[] = new Array<FloorModel>();
  rooms: RoomModel[];
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

    this.buildingLayout = await this.roomDataService.getFloors();

    this.buildingLayout.sort(function (a, b) { return a.floor - b.floor });

    let firstFloor: FloorModel;

    firstFloor = this.buildingLayout[0];

    this.previousFilters = new Filters().create({
      startDate: new Date(new Date().setHours(0, 0, 0, 0)),
      endDate: new Date(new Date().setHours(23, 59, 0, 0)),
      minPersons: null,
      floor: firstFloor.floor
    });

  

    this.roomDataService.getRoomsByFilter(this.previousFilters).then((defaultRooms) => {
      this.rooms = <RoomModel[]>defaultRooms;
      this.displayedRooms = <RoomModel[]>defaultRooms;
    })

  }

  roomIsSelectedOnFilters(room: RoomModel) {
    console.log("Room emited: ", room);
    this.singleRoomSelected = room;
  }

  updateRoomsBasedOnFilters(filters: Filters) {
    console.log("Filters component emited: ", filters);

    if (this.filteredRoomsAlreadyExist(filters)) {
      this.setDisplayedRooms(filters);
    } else {
      this.roomDataService.getRoomsByFilter(filters).then((rooms) => {
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

    console.log(this.singleRoomSelected);

    if (this.singleRoomSelected != null) {

      console.log(this.roomDataService.verifyRoomAvailabilityByFilters(this.singleRoomSelected, filters))
      if (this.roomDataService.verifyRoomAvailabilityByFilters(this.singleRoomSelected, filters)) {
        this.displayedRooms = new Array<RoomModel>();
        this.displayedRooms.push(this.singleRoomSelected);
      }
    } else {
      this.displayedRooms = this.rooms.map((room) => {
        if (filters.minPersons != null) {
          if (filters.minPersons == room.maxPersons) {
            if (this.roomDataService.verifyRoomAvailabilityByFilters(room, filters)) {
              return room;
            }
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
        room.reservations.push(booking);
      }
    });
  }
}
