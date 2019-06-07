export class LoginToken {
    value: string;
    expirationDate: Date;

    constructor() { }

    create(tokenModel: any): LoginToken {
        return Object.assign(new LoginToken(), tokenModel);
    }
}