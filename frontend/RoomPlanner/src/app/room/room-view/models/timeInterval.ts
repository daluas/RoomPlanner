export class TimeInterval{
    startHour: number;
    startMinute: number;
    endHour: number;
    endMinute: number;

    create(obj: any): TimeInterval{
        return Object.assign(new TimeInterval(), obj);
    }
}