export class RoomModel{


    constructor(){}

    create(roomModel: any): RoomModel {
        return Object.assign(new RoomModel(), roomModel);
    }
}

