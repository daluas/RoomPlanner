import { Time } from '@angular/common';

export class Filters{
   
    startDate:Date;
    endDate:Date;
    floor:number;
    numberOfPeople:number;


    constructor() { }

    create(filtersModel: any): Filters {
        return Object.assign(new Filters(), filtersModel);
    }
}