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

  private backendUrl: string = 'http://178.22.68.114/RoomPlanner/api';

  constructor(private httpClient: HttpClient) { }

  getRoomById(room: RoomModel) {
    var roomId: number = room.id;
    return this.httpClient.get(`${this.backendUrl}/rooms/${roomId}`).toPromise();
  }


  getRoomsByFilter(filter: Filters) {

    // let params = new HttpParams();
    // params = params.append("startDate", filter.startDate.toUTCString());
    // params = params.append("endDate", filter.endDate.toUTCString());
    // if (filter.floor != null) {
    //   params = params.append("floor", filter.floor.toString());
    // }
    // if (filter.minPersons != null) {
    //   params = params.append("minPersons", filter.minPersons.toString());
    // }


    // var res = this.httpClient.get(`${this.backendUrl}/rooms/filters`, { params: params });


    var res =
      `[
      {
        "id": 2,
        "email": "wonderland@yahoo.com",
        "type": "ROOM",
        "reservations":[
          {
            "id": 4,
            "roomId": 2,
            "personalEmail": "sghitun@yahoo.com",
            "startDate": "2019-06-28T09:00:00.000+0000",
            "endDate": "2019-06-28T12:00:00.000+0000",
            "description": "Retro meeting"
          },
          {
            "id": 5,
            "roomId": 2,
            "personalEmail": "sghitun@yahoo.com",
            "startDate": "2019-06-28T14:00:00.000+0000",
            "endDate": "2019-06-28T15:30:00.000+0000",
            "description": "Retro meeting"
          }
        ] ,
        "name": "Wonderland",
        "floor": 5,
        "maxPersons": 14
      }
    ]`

    let j = JSON.parse(res);
    let roomArray: RoomModel[] = new Array<RoomModel>();

    j.forEach(roomJson => {
      let reserv: Booking[] = new Array<Booking>();

      roomJson.reservations.forEach(resJson => {
        reserv.push(new Booking().create({
          id: resJson.id,
          roomId: resJson.roomId,
          personalEmail: resJson.personalEmail,
          startDate: new Date(new Date(resJson.startDate)),
          endDate: new Date(new Date(resJson.endDate)),
          description: resJson.description
        }))
      });


      roomArray.push(new RoomModel().create({
        id: roomJson.id,
        email: roomJson.email,
        type: roomJson.type,
        name: roomJson.name,
        floor: roomJson.floor,
        maxPersons: roomJson.maxPersons,
        reservations: reserv
      })
      );

    });

    return new Promise(res => { return res(roomArray); })
  }

  //getFloors():FloorModel

  getFloors(): FloorModel[] {
    console.log("getFloors() was called!");

    var rez =
      `[
        {
        "id": 1,
        "floor": 5,
        "rooms": [
          {
            "id": 2,
            "email": "wonderland@yahoo.com",
            "type": "ROOM",
            "reservations":[
              {
                "id": 4,
                "roomId": 2,
                "personalEmail": "sghitun@yahoo.com",
                "startDate": "2019-06-28T09:00:00.000+0000",
                "endDate": "2019-06-28T12:00:00.000+0000",
                "description": "Retro meeting"
              },
              {
                "id": 5,
                "roomId": 2,
                "personalEmail": "sghitun@yahoo.com",
                "startDate": "2019-06-27T14:00:00.000+0000",
                "endDate": "2019-06-27T15:30:00.000+0000",
                "description": "Retro meeting"
              }
            ] ,
            "name": "Wonderland",
            "floor": 5,
            "maxPersons": 14
          }
        ]
      },
      {
        "id": 2,
        "floor": 8,
        "rooms": [
            {
                "id": 3,
                "email": "westeros@yahoo.com",
                "type": "ROOM",
                "reservations": [
                  {
                    "id": 4,
                    "roomId": 3,
                    "personalEmail": "sghitun@yahoo.com",
                    "startDate": "2019-06-29T11:30:00.000+0000",
                    "endDate": "2019-06-28T12:00:00.000+0000",
                    "description": "Retro meeting"
                  },
                  {
                    "id": 5,
                    "roomId": 3,
                    "personalEmail": "sghitun@yahoo.com",
                    "startDate": "2019-06-27T14:00:00.000+0000",
                    "endDate": "2019-06-29T12:30:00.000+0000",
                    "description": "Retro meeting"
                  }
                ],
                "name": "Westeros",
                "floor": 8,
                "maxPersons": 20
            }
        ]
    },
    {
        "id": 3,
        "floor": 4,
        "rooms": [
            {
                "id": 4,
                "email": "neverland@yahoo.com",
                "type": "ROOM",
                "reservations": [
                  {
                    "id": 4,
                    "roomId": 4,
                    "personalEmail": "sghitun@yahoo.com",
                    "startDate": "2019-06-28T13:00:00.000+0000",
                    "endDate": "2019-06-28T14:00:00.000+0000",
                    "description": "Retro meeting"
                  },
                  {
                    "id": 5,
                    "roomId": 4,
                    "personalEmail": "sghitun@yahoo.com",
                    "startDate": "2019-06-26T14:00:00.000+0000",
                    "endDate": "2019-06-26T15:30:00.000+0000",
                    "description": "Retro meeting"
                  }
                ],
                "name": "Neverland",
                "floor": 4,
                "maxPersons": 5
            }
        ]
    },
    {
        "id": 4,
        "floor": 10,
        "rooms": []
    }
    ]`;

    // return this.httpClient.get(`${this.backendUrl}/floors`).toPromise().toPromise().then(data=>{
    //   return data;
    // }).catch(error=>{
    //   console.log("Erorr--getRoomsByFilter")
    //   return Promise.reject(error);
    // });

    let j = JSON.parse(rez);
    let floorArray: FloorModel[] = new Array<FloorModel>();

    j.forEach(floorJson => {

      let r: RoomModel[] = new Array<RoomModel>();

      floorJson.rooms.forEach(roomJson => {
        let reserv: Booking[] = new Array<Booking>();

        roomJson.reservations.forEach(resJson => {
          reserv.push(new Booking().create({
            id: resJson.id,
            roomId: resJson.roomId,
            personalEmail: resJson.personalEmail,
            startDate: new Date(new Date(resJson.startDate)),
            endDate: new Date(new Date(resJson.endDate)),
            description: resJson.description
          }))
        });


        r.push(new RoomModel().create({
          id: roomJson.id,
          email: roomJson.email,
          type: roomJson.type,
          name: roomJson.name,
          floor: roomJson.floor,
          maxPersons: roomJson.maxPersons,
          reservations: reserv
        })
        );
      })

      floorArray.push(new FloorModel().create({
        id: floorJson.id,
        floor: floorJson.floor,
        rooms: r
      }))
    });

    return floorArray;
  }


  verifyRoomAvailabilityByFilters(room: RoomModel, filters: Filters) {

    room.reservations.forEach(res => {
      if (res.startDate.getTime() != filters.startDate.getTime() && res.endDate.getTime() != filters.endDate.getTime()) {
        return true;
      }
    })
    return false;


  }

  // getDefaultRooms() nu poate fi combinat cu getRooms(filters)
  // pentru ca getRooms cu filtre goale trebuie sa returneze toate camerele
  // si getDefaultRooms() toate camere de la primul etaj, dar noi nu stim inca care
  // e acel prim etaj ca sa apelam getRooms() cu filtru pe acel prim etaj
  // getDefaultRooms(): Promise<any[]> {
  //   console.log("getDefaultRooms() was called!");

  //   // fara returnare de erori, doar rooms, ori null
  //   return new Promise((res) => {
  //     // http request fara filtre, decat pe data curenta la nivel de zi,
  //     // ca nu stim inca care este primul etaj al cladirii

  //     res([
  //       {
  //         id: 1,
  //         name: "401",
  //         capacity: 10,
  //         bookings: [
  //           // this is a booking made by current user
  //           {
  //             id: 1,
  //             startDate: new Date(new Date().setHours(10, 0, 0, 0)),
  //             endDate: new Date(new Date().setHours(10, 30, 0, 0)),
  //             ownerEmail: "user1@cegeka.ro",
  //             description: "this is a test description"
  //           },
  //           // this is a booking made by another user
  //           // this one shouldn't have ID or Description
  //           {
  //             startDate: new Date(new Date().setHours(12, 30, 0, 0)),
  //             endDate: new Date(new Date().setHours(15, 0, 0, 0)),
  //             ownerEmail: "user10@geceka.ro"
  //           }
  //         ]
  //       },
  //       {
  //         id: 2,
  //         name: "405",
  //         capacity: 5,
  //         bookings: []
  //       }
  //     ])

  //     // if error, res(null)
  //   });
  // }

  // apelata doar din user view
  // getRooms(filter: Filters): Promise<any[]> {
  //   console.log("getRooms(filters: any) was called!");

  //   // fara returnare de erori, doar rooms, ori null
  //   return new Promise((res) => {
  //     // You can filter only on these values:
  //     // current date -> which will get all rooms
  //     // current date, floor value -> which will get all rooms for a specific floor
  //     // current date, room id -> which will get a single room

  //     res([
  //       {
  //         id: 3,
  //         name: "501",
  //         capacity: 10,
  //         bookings: [
  //           // this is a booking made by current user
  //           {
  //             id: 3,
  //             startDate: new Date(new Date().setHours(13, 0, 0, 0)),
  //             endDate: new Date(new Date().setHours(14, 30, 0, 0)),
  //             ownerEmail: "user1@cegeka.ro",
  //             description: "this is a test description"
  //           },
  //         ]
  //       }
  //     ])

  //     // if error, res(null)
  //   });
  // }

  // apelata doar din room view
  // getRoom(date: Date): Promise<any> {
  //   console.log("getRoom(date: Date) was called!");
  //   // fara returnare de erori, doar room, ori null

  //   return new Promise((res) => {
  //     // vezi cu backend-ul cum iei programul camerei utilizatorului room logat
  //     // si va fi apelata la anumite intervale pentru a face refresh

  //     res([
  //       {
  //         id: 5,
  //         name: "813",
  //         capacity: 14,
  //         bookings: [
  //           {
  //             id: 3,
  //             startDate: new Date(new Date().setHours(11, 0, 0, 0)),
  //             endDate: new Date(new Date().setHours(13, 30, 0, 0)),
  //             ownerEmail: "user1@cegeka.ro",
  //           },
  //         ]
  //       }
  //     ])
  //   });
  // }




}
