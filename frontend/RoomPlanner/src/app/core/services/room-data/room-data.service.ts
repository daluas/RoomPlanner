import { Injectable } from '@angular/core';
import { Filters } from '../../../shared/models/Filters';
import { Observable } from 'rxjs';
import { RoomModel } from '../../models/RoomModel';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoomDataService {

  backendUrl: "";
  constructor(private http: HttpClient) { }

  getRoomsByDate(date: Date): Date /*: RoomModel[]*/ {
    console.log(`Selected date is: ${date}`);
    //return [new RoomModel().create({})];
    return date;
  }

  getRoomsByFilter(filter: Filters) {
    return this.http.post(`${this.backendUrl}/rooms/filter`, {body: filter}).toPromise();
  }

  getAllRooms(): Promise<Object> {
    return this.http.get(`${this.backendUrl}/rooms`).toPromise();
    // return new Observable();
  }


}
