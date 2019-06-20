import { RoomModel } from '../../core/models/RoomModel';

export class FloorModel{

    name:number; //{4, 5, 8}
    rooms: RoomModel[];

    constructor() { }

    create(floorModel: any): FloorModel {
        return Object.assign(new FloorModel(), floorModel);
    }
}