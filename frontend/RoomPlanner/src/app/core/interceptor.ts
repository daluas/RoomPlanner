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

import { Observable, of, throwError } from 'rxjs';
import { LoginModel } from './models/LoginModel';
import { tap, map } from 'rxjs/operators';
import { LoginToken } from './models/LoginToken';
import { LoggedUser } from './models/LoggedUser';
import { MatSnackBar } from '@angular/material';

@Injectable()
export class Interceptor implements HttpInterceptor {
    BASE_URL: string;

    interceptLogout(): boolean {
        return true;
    }

    interceptLogin(loggedUser: LoggedUser): void {
        // const token = this.refreshToken();
        // const loggedUserMock: LoggedUser = new LoggedUser().create({
        //     "email": "room1@cegeka.ro",
        //     "token": token,
        //     "type": "room"
        // })
        let token: LoginToken = this.createToken();

        localStorage.setItem('access-token', JSON.stringify(token));
        localStorage.setItem('user-data', JSON.stringify(loggedUser));
        // return loggedUserMock;
    }
    constructor(private _snackBar: MatSnackBar) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log("interceptor request");
        console.log(request);

        let newRequest: HttpRequest<any>;
        if (!request.headers.has("Content-Type")) {
            newRequest = request.clone({
                headers: request.headers.set("Content-Type", "application/json")
            });
        }

        if (request.url === '/auth/signin') {
            // let user: LoggedUser = this.interceptLogin()

            let reqBody = newRequest.body;
            if (reqBody.email === 'room1@cegeka.ro' && reqBody.password === 'room.1') {
                let loggedUser: LoggedUser = new LoggedUser().create({
                    type: "room",
                    email: "room1@cegeka.ro"
                })
                this.interceptLogin(loggedUser);

                return of(new HttpResponse<LoggedUser>({
                    status: 200,
                    statusText: "Ok",
                    body: loggedUser
                }));
            }

            if (reqBody.email === 'admin1@cegeka.ro' && reqBody.password === 'admin.1') {
                let loggedUser: LoggedUser = new LoggedUser().create({
                    type: "admin",
                    email: "admin1@cegeka.ro"
                });

                this.interceptLogin(loggedUser);

                return of(new HttpResponse<LoggedUser>({
                    status: 200,
                    statusText: "Ok",
                    body: loggedUser
                }));
            }

            if (reqBody.email === 'user1@cegeka.ro' && reqBody.password === 'user.1') {
                let loggedUser: LoggedUser = new LoggedUser().create({
                    type: "user",
                    email: "user1@cegeka.ro"
                })

                this.interceptLogin(loggedUser);

                return of(new HttpResponse<LoggedUser>({
                    status: 200,
                    body: loggedUser
                }));
            }


            // return throwError(new HttpResponse({
            //     status: 404,
            //     statusText: "Not Found"
            // }));
        }

        if (request.url === '/auth/refresh') {
            let reqBody = newRequest.body;
            console.log(reqBody);
            return of(new HttpResponse({
                status: 200,
                body: {
                    token: this.refreshToken() //CHECK
                }
            }));
            // reqBody.refresh_token
                // return of(new HttpResponse<LoggedUser>({
                //     status: 200,
                //     statusText: "Ok",
                //     body: loggedUser
                // }));
        }

        // if (request.url === '/logout') {
        //     let user : LoggedUser = this.interceptLogout()
        //     return of(new HttpResponse({
        //         status: 200,
        //         body: user
        //     }));
        // }

        let newrequest = this.addAuthenticationToken(newRequest);
        return next.handle(newrequest).pipe(
            // return next.handle(newRequest).pipe(
            tap({
                next: (response: HttpResponse<any>) => {
                    if (response instanceof HttpResponse) {
                        response = response.clone({ body: "" })
                    }
                    if (response instanceof HttpErrorResponse) {
                        console.log("error response (never)");
                        console.log(response)
                    }
                },
                error: (error: HttpErrorResponse) => {
                    console.log(error.message);
                    // status, statusText, name, headers, type, url, ok
                    this._snackBar.open(
                        `Call to ${error.url} failed with ${error.status} - ${error.statusText}`,
                        'Close',
                        {
                            duration: 10000
                        }
                    );

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
            headers: request.headers.set("Authorization", `Bearer ${token.value}`)
        });
    }

    createToken(): LoginToken { // mock function; logic will be passed in refreshToken ?
        console.log("creating token");
        return new LoginToken().create({
            "value": "U-T-O-K-E-N",
            "expirationDate": new Date(new Date(Date.now()).getTime() - 60 * 60 * 1 * 1000), // check
            "refreshToken": "REFRESH"
        })
    }


    //add 24h to token duration (token stored in localstorage);
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
            // this.createToken()
            storedToken.expirationDate = new Date(new Date(Date.now()).getTime() + 60 * 60 * 2 * 1000)
            //call to backend to retrieve updated token  ||  go to login again
            localStorage.setItem('access-token', JSON.stringify(storedToken));
            // localStorage.setItem('access-token', JSON.stringify(this.createToken()))
        }

        return storedToken;
    }

}

