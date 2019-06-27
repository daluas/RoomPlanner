import { LoggedUser } from './LoggedUser';

export class Booking {
    id?: number;
    startDate: Date;
    endDate: Date;
    roomId: number;
    description: string;
    personalEmail: string;
  
    constructor() {
        this.startDate=new Date();
        this.endDate=new Date();
        this.id=null;
        this.roomId=null;
        this.description=null;
        this.personalEmail=null;
     }

    create(bookingModel: any): Booking {
        return Object.assign(new Booking(), bookingModel);
    }
}
