import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RoomDataService {

  constructor() { }

  getBuildingLayout(): Promise<any[]> {
    console.log("getBuildingLayout() was called!");

    // fara returnare de erori, doar building layout, ori null
    return new Promise((res) => {
      //http request to backend

      res([
        {
          floor: 4,
          rooms: [
            {
              id: 1,
              name: "401"
            },
            {
              id: 2,
              name: "405"
            }
          ]
        },
        {
          floor: 5,
          rooms: [
            {
              id: 3,
              name: "501"
            },
            {
              id: 4,
              name: "506"
            }
          ]
        },
        {
          floor: 8,
          rooms: [
            {
              id: 5,
              name: "813"
            }
          ]
        }
      ])

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
              ownerEmail: "user10@cegeka.ro"
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
  getRooms(filters: any): Promise<any[]> {
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
}
