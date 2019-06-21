import { LoggedUser } from './LoggedUser';

export class Booking {
    id?: number;
    startDate: Date;
    endDate: Date;
    roomId: number;
    description: string;
    personalEmail: string;
  
    constructor() { }

    create(bookingModel: any): Booking {
        return Object.assign(new Booking(), bookingModel);
    }
}
