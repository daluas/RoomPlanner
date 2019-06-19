import { Time } from '@angular/common';

export class LoginToken {
    access_token: string;
    token_type: string;
    refresh_token: string;
    expiration_timestamp: number;

    constructor() { }

    create(tokenModel: any): LoginToken {
        return Object.assign(new LoginToken(), tokenModel);
    }

    get isExpired() {
        let now = new Date().getTime()
        console.log("expiration :", this.expiration_timestamp);
        console.log("now: ", now);
        if (now > this.expiration_timestamp) {
            return false;
        }
        return false;
    }
}