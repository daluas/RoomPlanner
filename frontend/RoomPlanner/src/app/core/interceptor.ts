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
import { tap, map, catchError } from 'rxjs/operators';
import { LoginToken } from './models/LoginToken';
import { LoggedUser } from './models/LoggedUser';
import { MatSnackBar } from '@angular/material';
import { ok } from 'assert';

@Injectable()
export class Interceptor implements HttpInterceptor {
    BASE_URL: string = 'http://178.22.68.114:8081';

    constructor(private _snackBar: MatSnackBar) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (request.url === `${this.BASE_URL}/oauth/token`) {

            if (request.body.grant_type == "password") {
                console.log("requesting token")                
                let newRequest: HttpRequest<any> = this.addGetTokenHeaders(request);
                console.log(newRequest);

                return next.handle(newRequest).pipe(
                    tap((event: HttpEvent<any>) => {
                        if (event instanceof HttpResponse) {
                            console.log('event--->>>', event);
                        }
                        console.log(event);
                        return event;
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

            if (request.body.grant_type == "refresh_token") {
                console.log("requesting refresh token")
                return next.handle(request).pipe(
                    tap({
                        next: (response: HttpResponse<any>) => {
                            console.log(response);
                            // response.body // token
                        },
                        error: (error: HttpErrorResponse) => {
                            console.log(error);
                        }
                    })
                )
            }
        }

        // get the token and request the user 
        //     return of(new HttpResponse<LoggedUser>({
        //         status: 200,
        //         statusText: "Ok",
        //         body: loggedUser
        //     }));
        // }
        // if (request.url === '/rooms/filter') {
        //     console.log(request.body);
        // }
        // return next.handle(request).pipe(
        //     // return next.handle(newRequest).pipe(
        //     tap({
        //         next: (response: HttpResponse<any>) => {
        //             if (response instanceof HttpResponse) {
        //                 console.log("backend responded");
        //                 // response = response.clone({ body: "" })
        //             }
        //             if (response instanceof HttpErrorResponse) {
        //                 console.log("error response (never)");
        //                 console.log(response)
        //             }
        //         },
        //         error: (error: HttpErrorResponse) => {
        //             console.log(error.message);
        //             // status, statusText, name, headers, type, url, ok
        //             this._snackBar.open(
        //                 `Call to ${error.url} failed with ${error.status} - ${error.statusText}`,
        //                 'Close',
        //                 {
        //                     duration: 7000
        //                 }
        //             );

        //         },
        //         complete: () => console.log('on complete')
        //     })
        // )
    }

    addGetTokenHeaders(request: HttpRequest<any>): HttpRequest<any> {
        let newRequest: HttpRequest<any>;

        let client_id = "browser"
        let pass = "pin"
        let encoded = btoa(`${client_id}:${pass}`)

        let getTokenHeaders = new HttpHeaders();
        getTokenHeaders.set("Content-Type", "application/x-www-form-urlencoded")
        getTokenHeaders.set("Authorization", `Basic ${encoded}`)
        getTokenHeaders.set("Access-Control-Request-Headers", "Content-Type");

        newRequest = request.clone({
            headers: getTokenHeaders
        });
        return newRequest;
    }

    addAuthenticationToken(request: HttpRequest<any>, next: HttpHandler): HttpRequest<any> {
        // If you are calling an outside domain then do not add the token.
        // if (!request.url.match(/www.mydomain.com\//)) {
        //     return request;
        // }
        let tokenParsed = JSON.parse(localStorage.getItem('access-token'))
        let storedToken: LoginToken = new LoginToken().create(tokenParsed);

        if (storedToken.isExpired) {
            console.log("token expired in interceptor");
            let newRequest: HttpRequest<any> = this.addGetTokenHeaders(request);
            newRequest.clone({
                body: {
                    grant_type: "refresh_token",
                    refresh_token: storedToken.refresh_token
                }
            })
            next.handle(newRequest).toPromise()
                .then(data => {
                    console.log(data)
                    // localStorage.setItem('access-token', JSON.stringify(data));
                    return newRequest.clone({
                        headers: request.headers.set("Authorization", `Bearer ${storedToken.access_token}`)
                    });
                })
                .catch(error => {
                    console.log(error);

                })

            next.handle(newRequest).pipe(
                // return next.handle(newRequest).pipe(
                tap({
                    next: (response: HttpEvent<any>) => {
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
            )


        }
        else {
            return request.clone({
                headers: request.headers.set("Authorization", `Bearer ${storedToken.access_token}`)
            });
        }

    }

}

