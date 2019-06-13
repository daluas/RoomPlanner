import { Injectable } from '@angular/core';
import { RoomModel } from '../../core/models/RoomModel';
import { Filters } from '../models/Filters';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {

  constructor() { }

  getRoomsByDate(date: Date):Date /*: RoomModel[]*/ {
    //console.log(`Selected date is: ${date}`);
    //return [new RoomModel().create({})];
    return date;
  }

  getRoomsByFilter(filter:Filters){
    
  }
}
