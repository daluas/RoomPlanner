import { Injectable } from "@angular/core";
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse,
    HttpErrorResponse,
    HttpHeaders,
    HttpUrlEncodingCodec,
    HttpParams
} from "@angular/common/http";

import { Observable, of, throwError } from 'rxjs';
import { LoginModel } from './models/LoginModel';
import { tap, map, catchError } from 'rxjs/operators';
import { LoginToken } from './models/LoginToken';
import { LoggedUser } from './models/LoggedUser';
import { MatSnackBar } from '@angular/material';
import { ok } from 'assert';
import { EVENT_MANAGER_PLUGINS } from '@angular/platform-browser';

@Injectable()
export class Interceptor implements HttpInterceptor {

    BASE_URL: string = 'http://178.22.68.114/RoomPlanner';

    constructor(private _snackBar: MatSnackBar) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (request.url === `${this.BASE_URL}/oauth/token`) {
            //request token
            if (request.body.get("grant_type") === "password") {
                console.log("requesting token")
                request = this.addGetTokenHeaders(request);

                return next.handle(request)
                    .pipe(
                        tap((event: HttpEvent<any>) => {
                            if (event instanceof HttpResponse) {
                                let token = new LoginToken().create({
                                    access_token: event.body.access_token,
                                    token_type: event.body.token_type,
                                    refresh_Token: event.body.refresh_token,
                                    expiration_timestamp: new Date().getTime() + event.body.expires_in
                                })
                                localStorage.setItem("access-token", JSON.stringify(token))
                                return event;
                            }
                        }),
                        catchError((error: HttpErrorResponse) => {
                            console.log(error);
                            let data = {};
                            data = {
                                reason: error && error.error.reason ? error.error.reason : '',
                                status: error.status
                            };

                            // this.errorDialogService.openDialog(data);
                            return throwError(error);
                        })
                    );
            }

            //refresh token
            if (request.body.get("grant_type") == "refresh_token") {
                console.log("requesting refresh token")
                return next.handle(request).pipe(
                    tap({
                        next: (response: HttpResponse<any>) => {
                            console.log(response);
                        },
                        error: (error: HttpErrorResponse) => {
                            console.log(error);
                        }
                    })
                )
            }
        }

        if (request.url === `${this.BASE_URL}/users`) {
            request = this.addAuthenticationToken(request, next);
            return next.handle(request)
                .pipe(
                    tap((event: HttpEvent<any>) => {
                        if (event instanceof HttpResponse) {
                            console.log(event.body)
                            // goes here
                        }
                    }),
                    catchError((error: HttpErrorResponse) => {
                        console.log(error);
                        return throwError(error);
                    })
                );
        }
    }

    getTokenLS(): LoginToken {
        if (localStorage.getItem("access-token") != undefined) {
            let token = JSON.parse(localStorage.getItem("access-token"));
            let tokenLogin = new LoginToken().create(token);
            return token;
        }
        return null;
    }


    getUserData(): LoggedUser {
        if (localStorage.getItem("user-data") != undefined) {
            let user = JSON.parse(localStorage.getItem("user-data"));
            let userLogged = new LoggedUser().create(user);
            return userLogged;
        }
        return null;
    }

    addGetTokenHeaders(request: HttpRequest<any>): HttpRequest<any> {
        console.log("adding headers for requesting token");
        let client_id = "browser"
        let pass = "pin"
        let encoded = btoa(`${client_id}:${pass}`)
        return request.clone({
            setHeaders: {
                "Content-Type": 'application/x-www-form-urlencoded',
                "Authorization": `Basic ${encoded}`
                // Access-Control-Request-Headers: "Content-Type"
            }
        });
    }

    addAuthenticationToken(request: HttpRequest<any>, next: HttpHandler): HttpRequest<any> {
        // If you are calling an outside domain then do not add the token.
        // if (!request.url.match(/www.mydomain.com\//)) {
        //     return request;
        // }
        let token = this.getTokenLS();
        let userData = this.getUserData();

        if (token.isExpired) { //refresh token
            console.log("token expired in interceptor");
            let params = new HttpParams();
            request = request.clone({
                setParams: {
                    "grant_type": "refresh_token",
                    "refresh_token": token.refresh_token
                },
                setHeaders: {
                    "Authorization": `${token.token_type} ${token.access_token}`
                }
            });

            let refreshedToken = new LoginToken()
            next.handle(request).toPromise()
                .then((response: HttpEvent<any>) => {
                    if (response instanceof HttpResponse) {
                        console.log(response);
                        refreshedToken = refreshedToken.create(response.body)
                        localStorage.setItem("access-token", JSON.stringify(refreshedToken));
                    }
                    
                })
                .catch(error =>{
                    console.log(error)
                })
            
            if(refreshedToken){
                token = refreshedToken
            }
        }

        return request.clone({
            setHeaders: {
                "Authorization": `${token.token_type} ${token.access_token}`
            }
        });
    }
  
    //         // return next.handle(request).pipe(
    //         tap({
    //             next: (response: HttpEvent<any>) => {
    //                 if (response instanceof HttpResponse) {
    //                     console.log("backend responded");
    //                     // response = response.clone({ body: "" })
    //                 }
    //                 if (response instanceof HttpErrorResponse) {
    //                     console.log("error response (never)");
    //                     console.log(response)
    //                 }
    //             },
    //             error: (error: HttpErrorResponse) => {
    //                 console.log(error.message);
    //                 // status, statusText, name, headers, type, url, ok
    //                 this._snackBar.open(
    //                     `Call to ${error.url} failed with ${error.status} - ${error.statusText}`,
    //                     'Close',
    //                     {
    //                         duration: 7000
    //                     }
    //                 );

    //             },
    //             complete: () => console.log('on complete')
    //         })
    //     )

}


