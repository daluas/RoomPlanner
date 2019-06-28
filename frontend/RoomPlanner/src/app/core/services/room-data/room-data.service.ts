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
        "id": 4,
        "email": "neverland@yahoo.com",
        "type": "ROOM",
        "reservations":[
          {
            "id": 4,
            "roomId": 4,
            "personEmail": "sghitun@yahoo.com",
            "startDate": "2019-06-28T09:00:00.000+0000",
            "endDate": "2019-06-28T12:00:00.000+0000",
            "description": "Retro meeting"
          },
          {
            "id": 5,
            "roomId": 4,
            "personEmail": "sghitun@yahoo.com",
            "startDate": "2019-06-27T14:00:00.000+0000",
            "endDate": "2019-06-27T15:30:00.000+0000",
            "description": "Retro meeting"
          }
        ] ,
        "name": "Neverland",
        "floor": 4,
        "maxPersons": 14
      },
      {
        "id": 12,
        "email": "roomNew@yahoo.com",
        "type": "ROOM",
        "reservations":[
          {
            "id": 4,
            "roomId":12,
            "personEmail": "sghitun@yahoo.com",
            "startDate": "2019-06-28T09:00:00.000+0000",
            "endDate": "2019-06-28T12:00:00.000+0000",
            "description": "Retro meeting"
          },
          {
            "id": 5,
            "roomId": 12,
            "personEmail": "sghitun@yahoo.com",
            "startDate": "2019-06-27T12:00:00.000+0000",
            "endDate": "2019-06-27T14:00:00.000+0000",
            "description": "Retro meeting"
          }
        ] ,
        "name": "roomNew",
        "floor": 4,
        "maxPersons":20
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
          personEmail: resJson.personEmail,
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
            "reservations":[],
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
                "reservations": [],
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
                "reservations": [],
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
        r.push(new RoomModel().create({
          id: roomJson.id,
          email: roomJson.email,
          type: roomJson.type,
          name: roomJson.name,
          floor: roomJson.floor,
          maxPersons: roomJson.maxPersons,
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

  getSingleFloor(floorId: number): Promise<FloorModel> {
    console.log("getSingleFloor() was called!");

    var rez =
      `{
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
              "personEmail": "sghitun@yahoo.com",
              "startDate": "2019-06-28T09:00:00.000+0000",
              "endDate": "2019-06-28T12:00:00.000+0000",
              "description": "Retro meeting"
            },
            {
              "id": 5,
              "roomId": 2,
              "personEmail": "sghitun@yahoo.com",
              "startDate": "2019-06-27T14:00:00.000+0000",
              "endDate": "2019-06-27T15:30:00.000+0000",
              "description": "Retro meeting"
            }
          ],
            "name": "Wonderland",
            "floor": 5,
            "maxPersons": 14
          }
        ]
      }
    `;

    // return this.httpClient.get(`${this.backendUrl}/floor/${floorId}`).toPromise().toPromise().then(data=>{
    //   return data;
    // }).catch(error=>{
    //   console.log("Erorr--getRoomsByFilter")
    //   return Promise.reject(error);
    // });

    let j = JSON.parse(rez);
    let floor: FloorModel = new FloorModel();
    let r: RoomModel[] = new Array<RoomModel>();

    j.rooms.forEach(roomJson => {

      let reserv: Booking[] = new Array<Booking>();

      roomJson.reservations.forEach(resJson => {
        reserv.push(new Booking().create({
          id: resJson.id,
          roomId: resJson.roomId,
          personEmail: resJson.personEmail,
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
    });

    floor = new FloorModel().create({
      id: j.id,
      floor: j.floor,
      rooms: r
    });

    return new Promise(res => { return res(floor); })

  }


  verifyRoomAvailabilityByFilters(room: RoomModel, filters: Filters) {

    let status = true;

    room.reservations.forEach(res => {
      console.log(res.startDate, res.endDate);
      if (this.isValueInOpenInterval(res.startDate, filters.startDate, filters.endDate)) {
        status = false;
      }

      if (this.isValueInOpenInterval(res.endDate, filters.startDate, filters.endDate)) {
        status = false;
      }

      if (this.isValueInOpenInterval(filters.startDate, res.startDate, res.endDate)) {
        status = false;
      }

      // if (res.startDate.getTime() != filters.startDate.getTime() && res.endDate.getTime() != filters.endDate.getTime()) {
      //   return true;
      // }
    })
    return status;
  }


  isValueInOpenInterval(value, start, end) {
    if (value > start && value < end) {
      return true;
    }
    return false;
  }

}


