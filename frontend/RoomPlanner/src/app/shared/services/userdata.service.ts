import { Injectable } from '@angular/core';
import { RoomModel } from '../../core/models/RoomModel';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {

  constructor() { }

  getRoomsByDate(date: Date) : RoomModel[] {
    return [new RoomModel().create({})];
  }
}
