import { Injectable } from '@angular/core';
import { Filters } from '../../../shared/models/Filters';
import { Observable } from 'rxjs';
import { RoomModel } from '../../models/RoomModel';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FloorModel } from '../../models/FloorModel';
import { Booking } from '../../models/BookingModel';

@Injectable({
  providedIn: 'root'
})
export class RoomDataService {

  private backendUrl: string = 'http://178.22.68.114:8081';

  constructor(private httpClient: HttpClient) { }

  getRoomById(room: RoomModel): Promise<Object> {
    var roomId: number = room.id;
    return this.httpClient.get(`${this.backendUrl}/api/rooms/${roomId}`).toPromise();
  }

  async getRoomsByFilter(filter: Filters): Promise<RoomModel[]> {

    // if (filter.startDate.getHours() == 0 && filter.endDate.getHours() == 0
    //   && filter.startDate.getMinutes() == 0 && filter.endDate.getMinutes() == 0) {
    //   filter.endDate.setHours(23);
    //   filter.endDate.setHours(59);
    // }

    let params = new HttpParams();
    params = params.append("startDate", filter.startDate.toUTCString());
    params = params.append("endDate", filter.endDate.toUTCString());
    if (filter.floor != null) {
      params = params.append("floor", filter.floor.toString());
    }
    if (filter.minPersons != null) {
      params = params.append("minPersons", filter.minPersons.toString());
    }

    let result = await this.httpClient.get<RoomModel[]>(`${this.backendUrl}/api/rooms/filters`, { params: params }).toPromise();


    if (result == undefined) {
      return Promise.reject("No object from backend -- getRoomsByFilter");
    }

    let roomArray: RoomModel[] = new Array<RoomModel>();


    result.forEach(roomFromResult => {
      let reservationArray: Booking[] = new Array<Booking>();
      roomFromResult.reservations.forEach(reservationFromResult => {
        reservationArray.push(new Booking().create({
          id: reservationFromResult.id,
          roomId: reservationFromResult.roomId,
          personalEmail: reservationFromResult.personalEmail,
          startDate: new Date(new Date(reservationFromResult.startDate)),
          endDate: new Date(new Date(reservationFromResult.endDate)),
          description: reservationFromResult.description

        }))
      });
      roomArray.push(new RoomModel().create({
        id: roomFromResult.id,
        email: roomFromResult.email,
        type: roomFromResult.type,
        name: roomFromResult.name,
        floor: roomFromResult.floor,
        maxPersons: roomFromResult.maxPersons,
        reservations: reservationArray
      })
      );
    });

    if (roomArray.length == 0) {
      return Promise.reject("Empty array for getRoomsByFilter()")
    }
    return Promise.resolve(roomArray);
  }


  async getFloors(): Promise<FloorModel[]> {

    var result = await this.httpClient.get<FloorModel[]>(`${this.backendUrl}/api/floors`).toPromise();
    if (result == undefined) {
      return Promise.reject("No object from backend -- getFloors");
    }


    
    let floorArray: FloorModel[] = new Array<FloorModel>();

    
    result.forEach(floorFromResult => {
      let roomArray: RoomModel[] = new Array<RoomModel>();
      floorFromResult.rooms.forEach(roomFromResult => {

        roomArray.push(new RoomModel().create({
          id: roomFromResult.id,
          email: roomFromResult.email,
          type: roomFromResult.type,
          name: roomFromResult.name,
          floor: roomFromResult.floor,
          maxPersons: roomFromResult.maxPersons,
        })
        );
      })

      floorArray.push(new FloorModel().create({
        id: floorFromResult.id,
        floor: floorFromResult.floor,
        rooms: roomArray
      }))
    });

    if (floorArray.length == 0) {
      return Promise.reject("Empty array for getFloors()")
    }
    return Promise.resolve(floorArray);
  }

  async getSingleFloor(floorId: number): Promise<FloorModel> {

    let result = await this.httpClient.get<FloorModel>(`${this.backendUrl}/api/floors/${floorId}`).toPromise()
    if (result == undefined) {
      return Promise.reject("No object from backend -- getSingleFloor");
    }
    let floor: FloorModel = new FloorModel();
    let roomArray: RoomModel[] = new Array<RoomModel>();

    result.rooms.forEach((roomFromResult: RoomModel) => {
      let reservationArray: Booking[] = new Array<Booking>();
      roomFromResult.reservations.forEach(reservationFromResult => {
        reservationArray.push(new Booking().create({
          id: reservationFromResult.id,
          roomId: reservationFromResult.roomId,
          personalEmail: reservationFromResult.personalEmail,
          startDate: new Date(new Date(reservationFromResult.startDate)),
          endDate: new Date(new Date(reservationFromResult.endDate)),
          description: reservationFromResult.description
        }))
      });
      roomArray.push(new RoomModel().create({
        id: roomFromResult.id,
        email: roomFromResult.email,
        type: roomFromResult.type,
        name: roomFromResult.name,
        floor: roomFromResult.floor,
        maxPersons: roomFromResult.maxPersons,
        reservations: reservationArray
      })
      );
    });

    floor = new FloorModel().create({
      id: result.id,
      floor: result.floor,
      rooms: roomArray
    });

    if (floor == undefined) {
      return Promise.reject("Empty array for getSingleFloor()")
    }
    return Promise.resolve(floor);
  }

  verifyRoomAvailabilityByFilters(room: RoomModel, filters: Filters) {

    let status = true;

    room.reservations.forEach(res => {
      console.log(res.startDate, res.endDate);

      if (this.isValueInInterval(res.startDate, filters.startDate, filters.endDate)) {
        status = false;
      }

      if (this.isValueInOpenInterval(res.endDate, filters.startDate, filters.endDate)) {
        status = false;
      }

      if (this.isValueInOpenInterval(filters.startDate, res.startDate, res.endDate)) {
        status = false;
      }
    })
    return status;
  }

  isValueInInterval(value, start, end) {
    if (value >= start && value < end) {
      return true;
    }
    return false;
  }
  isValueInOpenInterval(value, start, end) {
    if (value > start && value < end) {
      return true;
    }
    return false;
  }

}