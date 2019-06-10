import { Injectable } from "@angular/core";
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse,
    HttpErrorResponse,
    HttpHeaders
} from "@angular/common/http";
import { Observable, of } from 'rxjs';
import { LoginModel } from './models/LoginUser';
import { tap, map } from 'rxjs/operators';
import { LoginToken } from './models/LoginToken';
import { LoggedUser } from './models/LoggedUser';


@Injectable()
export class Interceptor implements HttpInterceptor {

    interceptLogout(): boolean {
        return true;
    }
    interceptLogin(): LoggedUser {
        const token = this.refreshToken();

        const loggedUserMock: LoggedUser = new LoggedUser().create({
            "email": "user1@cegeka.ro",
            "token": token,
            "type": "user"
        })
        localStorage.setItem('access-token', JSON.stringify(token));

        return loggedUserMock;
    }
    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!request.headers.has("Content-Type")) {
            request = request.clone({
                headers: request.headers.set("Content-Type", "application/json")
            });
        }

        if (request.url === '/auth') {
            let user : LoggedUser = this.interceptLogin()
            return of(new HttpResponse({
                status: 200,
                body: user
            }));
        }

        // if (request.url === '/logout') {
        //     let user : LoggedUser = this.interceptLogout()
        //     return of(new HttpResponse({
        //         status: 200,
        //         body: user
        //     }));
        // }

        request = this.addAuthenticationToken(request);

        return next.handle(request).pipe(
            tap({
                next: (response: HttpResponse<any>) => {
                    console.log("response");
                    console.log(response)
                },
                error: (error: HttpErrorResponse) => {
                    console.log(error);
                    console.log(error.message);
                },
                complete: () => console.log('on complete')
            })
        );

    }

    addAuthenticationToken(request: HttpRequest<any>): HttpRequest<any> {
        // If you are calling an outside domain then do not add the token.
        // if (!request.url.match(/www.mydomain.com\//)) {
        //     return request;
        // }
        let token: LoginToken = this.refreshToken();
        console.log("adding authorization token", token, "to ", request)
        return request.clone({
            headers: request.headers.set("Authorization", token.value)
        });
    }

    createToken(): LoginToken { // mock function; logic will be passed in refreshToken ?
        console.log("creating token");
        return new LoginToken().create({
            "value": "U-T-O-K-E-N",
            "expirationDate": new Date(new Date(Date.now()).getTime() - 60 * 60 * 24 * 1000)
        })
    }


    //add 24h to token duration
    refreshToken(): LoginToken {
        console.log("Refreshing token");

        if (localStorage.getItem('access-token') == null) {
            console.log("no token available");
            const newToken: LoginToken = this.createToken();
            localStorage.setItem('access-token', JSON.stringify(newToken));
            return newToken;
        }

        let tokenParsed = JSON.parse(localStorage.getItem('access-token'))
        let storedToken: LoginToken = new LoginToken().create(tokenParsed);

        let expirationDate: Date = new Date(storedToken.expirationDate)
        let now = new Date(Date.now());

        if (expirationDate < now) {
            alert('Token is expired');
            storedToken.expirationDate = new Date(new Date(Date.now()).getTime() + 60 * 60 * 24 * 1000)
            localStorage.setItem('access-token', JSON.stringify(storedToken));
        }

        return storedToken;
    }

}

