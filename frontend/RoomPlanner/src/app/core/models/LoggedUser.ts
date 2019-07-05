import { LoginToken } from './LoginToken';

export class LoggedUser {
    id: number;
    email: string;
    type?: string;
    firstName?: string;
    lastName?: string;


    constructor() { }

    create(loggedUserModel: any): LoggedUser {
        return Object.assign(new LoggedUser(), loggedUserModel);
    }
}