import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RoomDataService {

  constructor() { }

  getRoomsByDate(date: Date):Date /*: RoomModel[]*/ {
    console.log(`The date is: ${date}`);
    //return [new RoomModel().create({})];
    return date;
  }
}
