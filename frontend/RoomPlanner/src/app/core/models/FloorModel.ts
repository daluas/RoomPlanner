import { RoomModel } from '../../core/models/RoomModel';

export class FloorModel{

    id?: number;
    floor:number; //{4, 5, 8}
    rooms: RoomModel[]; 

    constructor() { }

    create(floorModel: any): FloorModel {
        return Object.assign(new FloorModel(), floorModel);
    }
}