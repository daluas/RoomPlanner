import { LoginToken } from './LoginToken';

export class LoggedUser {
    email: string;
    type: string;
    token: LoginToken

    constructor() { }

    create(loggedUserModel: any): LoggedUser {
        return Object.assign(new LoggedUser(), loggedUserModel);
    }
}