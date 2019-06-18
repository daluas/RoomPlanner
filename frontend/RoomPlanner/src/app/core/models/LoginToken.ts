export class LoginToken {
    access_token: string;
    token_type: string;
    refresh_token: string;
    expires_in: number;
    expirationDate: Date;
    constructor() { }

    create(tokenModel: any): LoginToken {
        return Object.assign(new LoginToken(), tokenModel);
    }

    get isExpired(){
        let expirationDate: Date = new Date(this.expirationDate)
        let now = new Date(Date.now());
        console.log("expiration :", expirationDate);
        console.log("now: ", now);

        return false;
    }
}