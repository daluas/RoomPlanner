import { Time } from '@angular/common';

export class Filters{
    date:Date;
    startHour:Time;
    endHour:Time;
    floor:number;
    numberOfPeople:number;


    constructor() { }

    create(filtersModel: any): Filters {
        return Object.assign(new Filters(), filtersModel);
    }
}