export class TimeInterval{
    startDate: number;
    endDate: number;

    create(obj: any): TimeInterval{
        return Object.assign(new TimeInterval(), obj);
    }
}