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
        let newRequest: HttpRequest<any>;
        if (!request.headers.has("Content-Type")) {
            newRequest = request.clone({
                headers: request.headers.set("Content-Type", "application/json")
            });
        }

        if (newRequest.url === '/auth/signin') {
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
                    statusText: "Ok",
                    body: loggedUser
                }));
            }


            // return throwError(new HttpResponse({
            //     status: 404,
            //     statusText: "Not Found"
            // }));
        }

        if (newRequest.url === '/auth/refresh') {
            let reqBody = newRequest.body;

            return of(new HttpResponse({
                status: 200,
                statusText: "Ok",
                body: {
                    token: this.refreshToken() //CHECK
                }
            }));
        }

        // if (request.url === '/logout') {
        //     let user : LoggedUser = this.interceptLogout()
        //     return of(new HttpResponse({
        //         status: 200,
        //         body: user
        //     }));
        // }

        // let newrequest = this.addAuthenticationToken(newRequest);
        return next.handle(newRequest).pipe(
            // return next.handle(newRequest).pipe(
            tap({
                next: (response: HttpResponse<any>) => {
                    if (response instanceof HttpResponse) {
                        console.log("backend responded");
                        // response = response.clone({ body: "" })
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
                            duration: 7000
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
        let expDate = new Date(new Date(Date.now()).getTime() + 15 * 1000)
        console.log(expDate);
        return new LoginToken().create({
            "value": "U-T-O-K-E-N",
            "expirationDate": expDate, // check
            "refreshToken": "REFRESH"
        })
    }


    refreshToken(): LoginToken {
        this._snackBar.open(
            `Refreshing token (intercepted request)`,
            'Close',
            {
                duration: 2000
            }
        );

        let tokenParsed = JSON.parse(localStorage.getItem('access-token'))
        let storedToken: LoginToken = new LoginToken().create(tokenParsed);

        let expirationDate: Date = new Date(storedToken.expirationDate)
        let now = new Date(Date.now());
        console.log("expiration :", expirationDate);
        console.log("now: ", now);

        if (expirationDate < now) {
            this._snackBar.open(
                `Token expired (interceptor)`,
                'Close',
                {
                    duration: 2000
                }
            );
            // this.createToken()
            let newExpirationDate = new Date(new Date(Date.now()).getTime() + 60 * 1000) // 10 seconds
            console.log("new expiration date: ", newExpirationDate);
            storedToken.expirationDate = newExpirationDate;
            //call to backend to retrieve updated token  ||  go to login again
            localStorage.setItem('access-token', JSON.stringify(storedToken));
            // localStorage.setItem('access-token', JSON.stringify(this.createToken()))
        }
        else {
            this._snackBar.open(
                `Token alive`,
                'Close',
                {
                    duration: 2000
                }
            );
        }

        return storedToken;
    }

}

