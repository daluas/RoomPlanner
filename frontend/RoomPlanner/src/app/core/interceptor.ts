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
    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (request.url === '/auth') {
            const token = new LoginToken().create({
                "value": "U-T-O-K-E-N",
                "expirationDate": new Date(new Date(Date.now()).getTime() + 60 * 60 * 24 * 1000)
            })
            
            const loggedUserMock: LoggedUser = new LoggedUser().create({
                "email": "user1@cegeka.ro",
                "token": token,
                "type": "user"
            })

            return of(new HttpResponse({
                status: 200,
                body: loggedUserMock
            }));
        }


        if (!request.headers.has("Content-Type")) {
            request = request.clone({
                headers: request.headers.set("Content-Type", "application/json")
            });
        }

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
        // If we do not have a token yet then we should not set the header.
        // Here we could first retrieve the token from where we store it.
        if (localStorage.getItem('access-token') == null) {
            console.log("no token available");
            return request;
        }
        // If you are calling an outside domain then do not add the token.
        // if (!request.url.match(/www.mydomain.com\//)) {
        //     return request;
        // }
        const token = localStorage.getItem('access-token');
        return request.clone({
            headers: request.headers.set("Authorization", token)
        });
    }

    createToken() { // mock function

    }
    //add 24h to token duration
    refreshToken(token: string = ''): string {
        console.log('refreshing token');
        console.log(token);
        if (token === undefined) {
            console.log("UNDEF");
        }

        let storedToken: LoginToken = new LoginToken().create(localStorage.getItem('access-token'));

        let expirationDate = storedToken.expirationDate
        console.log(expirationDate);

        let newExpirationDate = new Date(new Date(expirationDate).getTime() + 60 * 60 * 24 * 1000);
        let newExpirationDateStr = newExpirationDate.toString();

        console.log(newExpirationDate, newExpirationDateStr);

        localStorage.setItem(token, newExpirationDateStr);
        return '';
    }

}

