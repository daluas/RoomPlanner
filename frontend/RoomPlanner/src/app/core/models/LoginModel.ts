export class LoginModel {
    email: string;
    password: string;

    constructor() { }

    create(userModel: any): LoginModel {
        return Object.assign(new LoginModel(), userModel);
    }
}