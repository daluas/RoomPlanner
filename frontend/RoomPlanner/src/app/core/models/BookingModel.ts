import { LoggedUser } from './LoggedUser';

export class BookingModel {
    id?: number;
    startDate: Date;
    endDate: Date;
    description: string;
    owner: LoggedUser;

    constructor() { }

    create(bookingModel: any): BookingModel {
        return Object.assign(new BookingModel(), bookingModel);
    }
}