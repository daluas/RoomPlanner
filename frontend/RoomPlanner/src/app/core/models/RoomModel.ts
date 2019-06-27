import { Booking } from './BookingModel';

export class RoomModel{
    id?: number;
    email:string;
    type:string;
    name: string;
    floor: number;
    maxPersons: number;
    bookings: Booking[];

    constructor(){}

    create(roomModel: any): RoomModel {
        return Object.assign(new RoomModel(), roomModel);
    }
}

