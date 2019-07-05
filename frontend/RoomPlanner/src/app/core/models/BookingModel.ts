import { LoggedUser } from './LoggedUser';

export class Booking {

    id?: number;
    startDate: Date;
    endDate: Date;
    roomId: number;
    description: string;
    email: string;
  
    constructor() {
        this.startDate=new Date();
        this.endDate=new Date();
        this.id=null;
        this.roomId=null;
        this.description=null;
        this.email=null;
     }

    create(bookingModel: any): Booking {
        return Object.assign(new Booking(), bookingModel);
    }
}
