export class LoginToken {
    value: string;
    expirationDate: Date;
    refresh_token: string;

    constructor() { }

    create(tokenModel: any): LoginToken {
        return Object.assign(new LoginToken(), tokenModel);
    }
}