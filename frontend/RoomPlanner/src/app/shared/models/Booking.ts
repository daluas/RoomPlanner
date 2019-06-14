export class Booking{
    startDate: Date;
    endDate: Date;
    roomId: number;
    
    ownerEmail: string; 
    description: Date;

    create(obj: any): Booking{
        return Object.assign(new Booking(), obj);
    }
}