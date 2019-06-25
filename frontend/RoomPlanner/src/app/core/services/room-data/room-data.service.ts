import { Injectable } from '@angular/core';
import { Filters } from '../../../shared/models/Filters';
import { Observable } from 'rxjs';
import { RoomModel } from '../../models/RoomModel';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FloorModel } from '../../models/FloorModel';

@Injectable({
  providedIn: 'root'
})
export class RoomDataService {

  private backendUrl: string = 'http://178.22.68.114/RoomPlanner/api';

  constructor(private httpClient: HttpClient) { }

  // getRoomsByDate(date: Date): Date /*: RoomModel[]*/ {
  //   console.log(`Selected date is: ${date}`);
  //   //return [new RoomModel().create({})];
  //   return date;
  // }

  getRoomById(room:RoomModel){

    var roomId:number=room.id;
    return this.httpClient.get(`${this.backendUrl}/rooms/${roomId}`).toPromise();
  }


  getRoomsByFilter(filter: Filters) {

    let params = new HttpParams()
    params = params.append("startDate", filter.startDate.toUTCString());
    params = params.append("endDate", filter.endDate.toUTCString());
    params = params.append("floor", filter.floor.toString());
    params = params.append("minPersons", filter.minPersons.toString());

    return this.httpClient.get(`${this.backendUrl}/rooms/filters`, {params: params }).toPromise();
  }

  //getFloors():FloorModel

  getFloors(): Promise<FloorModel[]> {
    console.log("getFloors() was called!");

    var r1 = new RoomModel().create({ name: '101' });
    var r2 = new RoomModel().create({ name: '102' });
    var r3 = new RoomModel().create({ name: '201' });
    var r4 = new RoomModel().create({ name: '202' });

    var f1 = new FloorModel().create({ floor: 1, rooms: new Array<RoomModel>(r1, r2) });
    var f2 = new FloorModel().create({ floor: 2, rooms: new Array<RoomModel>(r3, r4) });
    //return this.httpClient.get(`${this.backendUrl}/floors`).toPromise();

    // fara returnare de erori, doar building layout, ori null
    return new Promise((res) => {
      //http request to backend
      res([f1, f2]);

      // if error, res(null)
    });
  }

  // getDefaultRooms() nu poate fi combinat cu getRooms(filters)
  // pentru ca getRooms cu filtre goale trebuie sa returneze toate camerele
  // si getDefaultRooms() toate camere de la primul etaj, dar noi nu stim inca care
  // e acel prim etaj ca sa apelam getRooms() cu filtru pe acel prim etaj
  getDefaultRooms(): Promise<any[]> {
    console.log("getDefaultRooms() was called!");

    // fara returnare de erori, doar rooms, ori null
    return new Promise((res) => {
      // http request fara filtre, decat pe data curenta la nivel de zi,
      // ca nu stim inca care este primul etaj al cladirii

      res([
        {
          id: 1,
          name: "401",
          capacity: 10,
          bookings: [
            // this is a booking made by current user
            {
              id: 1,
              startDate: new Date(new Date().setHours(10, 0, 0, 0)),
              endDate: new Date(new Date().setHours(10, 30, 0, 0)),
              ownerEmail: "user1@cegeka.ro",
              description: "this is a test description"
            },
            // this is a booking made by another user
            // this one shouldn't have ID or Description
            {
              startDate: new Date(new Date().setHours(12, 30, 0, 0)),
              endDate: new Date(new Date().setHours(15, 0, 0, 0)),
              ownerEmail: "user10@geceka.ro"
            }
          ]
        },
        {
          id: 2,
          name: "405",
          capacity: 5,
          bookings: []
        }
      ])

      // if error, res(null)
    });
  }

  // apelata doar din user view
  getRooms(filter: Filters): Promise<any[]> {
    console.log("getRooms(filters: any) was called!");

    // fara returnare de erori, doar rooms, ori null
    return new Promise((res) => {
      // You can filter only on these values:
      // current date -> which will get all rooms
      // current date, floor value -> which will get all rooms for a specific floor
      // current date, room id -> which will get a single room

      res([
        {
          id: 3,
          name: "501",
          capacity: 10,
          bookings: [
            // this is a booking made by current user
            {
              id: 3,
              startDate: new Date(new Date().setHours(13, 0, 0, 0)),
              endDate: new Date(new Date().setHours(14, 30, 0, 0)),
              ownerEmail: "user1@cegeka.ro",
              description: "this is a test description"
            },
          ]
        }
      ])

      // if error, res(null)
    });
  }

  // apelata doar din room view
  getRoom(date: Date): Promise<any> {
    console.log("getRoom(date: Date) was called!");
    // fara returnare de erori, doar room, ori null

    return new Promise((res) => {
      // vezi cu backend-ul cum iei programul camerei utilizatorului room logat
      // si va fi apelata la anumite intervale pentru a face refresh

      res([
        {
          id: 5,
          name: "813",
          capacity: 14,
          bookings: [
            {
              id: 3,
              startDate: new Date(new Date().setHours(11, 0, 0, 0)),
              endDate: new Date(new Date().setHours(13, 30, 0, 0)),
              ownerEmail: "user1@cegeka.ro",
            },
          ]
        }
      ])
    });
  }

  // getAllRooms(): Observable<Object> {
  //   return this.http.get(`${this.backendUrl}/rooms`)
  //   // .toPromise();
  //   // return new Observable();
  // }


}
