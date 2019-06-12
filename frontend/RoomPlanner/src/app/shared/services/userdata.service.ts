import { Injectable } from '@angular/core';
import { RoomModel } from '../../core/models/RoomModel';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {

  constructor() { }

  getRoomsByDate(date: Date):Date /*: RoomModel[]*/ {
    console.log(`The date is: ${date}`);
    //return [new RoomModel().create({})];
    return date;
  }
}
