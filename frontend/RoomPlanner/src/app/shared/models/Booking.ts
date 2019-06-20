export class Booking{
    id : number;
    startDate: Date;
    endDate: Date;
    roomId: number;
    ownerEmail: string; 
    description: string;

    create(obj: any): Booking{
        return Object.assign(new Booking(), obj);
    }
}