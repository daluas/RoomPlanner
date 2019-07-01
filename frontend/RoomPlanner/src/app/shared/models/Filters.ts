import { Time } from '@angular/common';
import { RoomModel } from '../../core/models/RoomModel';

export class Filters{
   
    startDate:Date;
    endDate:Date;
    floor:number;
    minPersons:number;

    roomSelected:RoomModel;

    constructor() { }

    create(filtersModel: any): Filters {
        return Object.assign({}, filtersModel);
    }
}