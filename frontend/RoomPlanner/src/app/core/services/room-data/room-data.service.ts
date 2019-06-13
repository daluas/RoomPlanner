import { Injectable } from '@angular/core';
import { Filters } from '../../../shared/models/Filters';

@Injectable({
  providedIn: 'root'
})
export class RoomDataService {

  constructor() { }

  getRoomsByDate(date: Date):Date /*: RoomModel[]*/ {
    //console.log(`Selected date is: ${date}`);
    //return [new RoomModel().create({})];
    return date;
  }

  getRoomsByFilter(filter:Filters){
    
  }
}
