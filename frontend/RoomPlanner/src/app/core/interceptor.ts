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
import { FloorModel } from './models/FloorModel';
import { RoomModel } from './models/RoomModel';
import { Booking } from './models/BookingModel';

@Injectable()
export class Interceptor implements HttpInterceptor {

    BASE_URL: string = 'http://178.22.68.114:8081';

    constructor(private _snackBar: MatSnackBar) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // console.log(request);

        if (request.url === `${this.BASE_URL}/oauth/token`) {
            //request token
            console.log(request.body)

            if (request.body.get("grant_type") === "password") {
                // console.log("aiciii");

                // let token = new LoginToken().create({
                //     access_token: "access",
                //     token_type: "bearer",
                //     refresh_Token: "refreshh",
                //     expiration_timestamp: 1561102598
                // })
                // localStorage.setItem("access-token", JSON.stringify(token))
                // return of(new HttpResponse({
                //     body: token
                // }))



                console.log("requesting token");
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
        /*
        if (request.url === `${this.BASE_URL}/users`) {

            // console.log(request);
            // let userdata = new LoggedUser().create({
            //     email: request.params.get("email"),
            //     type: "PERSON"
            // })
            // console.log(userdata);

            // return of(new HttpResponse({
            //     body: userdata
            // }))

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
      
        if (request.url === `${this.BASE_URL}/rooms/filters`) {
            console.log(request);
            let rooms: RoomModel[] = new Array<RoomModel>();
            rooms.push(new RoomModel().create({
                id: 4,
                email: "wonderland@yahoo.com",
                type: "ROOM",
                reservations: [new Booking().create(
                    {
                        id: 4,
                        roomId: 2,
                        personalEmail: "sghitun@yahoo.com",
                        startDate: "2019-07-01T09:00:00.000+0000",
                        endDate: "2019-07-01T12:00:00.000+0000",
                        description: "Retro meeting"
                    }), new Booking().create(
                        {
                            id: 5,
                            roomId: 2,
                            personalEmail: "sghitun@yahoo.com",
                            startDate: "2019-07-03T14:00:00.000+0000",
                            endDate: "2019-07-03T15:30:00.000+0000",
                            description: "Retro meeting"
                        })
                ],
                name: "Wonderland",
                floor: 5,
                maxPersons: 14
            }), new RoomModel().create({
                id: 12,
                email: "roomNew@yahoo.com",
                type: "ROOM",
                reservations: [
                    new Booking().create({
                        id: 4,
                        roomId: 12,
                        personalEmail: "sghitun@yahoo.com",
                        startDate: "2019-07-02T09:00:00.000+0000",
                        endDate: "2019-07-02T12:00:00.000+0000",
                        description: "Retro meeting"
                    }),
                    new Booking().create({
                        id: 5,
                        roomId: 12,
                        personalEmail: "sghitun@yahoo.com",
                        startDate: "2019-07-01T12:00:00.000+0000",
                        endDate: "2019-07-01T14:00:00.000+0000",
                        description: "Retro meeting"
                    })
                ],
                name: "roomNew",
                floor: 5,
                maxPersons: 20
            })
            )
            return of(new HttpResponse({
                status: 200,
                body: rooms
            }))


        }

        if (request.url === `${this.BASE_URL}/floors`) {
            let floors: FloorModel[] = new Array<FloorModel>();
            floors.push(new FloorModel().create({
                id: 1,
                floor: 5,
                rooms: [
                    new RoomModel().create(
                        {
                            id: 2,
                            email: "wonderland@yahoo.com",
                            type: "ROOM",
                            reservations: [],
                            name: "Wonderland",
                            floor: 5,
                            maxPersons: 14
                        })
                ]
            }), new FloorModel().create(
                {

                    id: 2,
                    floor: 8,
                    rooms: [new RoomModel().create(
                        {
                            id: 3,
                            email: "westeros@yahoo.com",
                            type: "ROOM",
                            reservations: [],
                            name: "Westeros",
                            floor: 8,
                            maxPersons: 20
                        })
                    ]
                }), new FloorModel().create(
                    {
                        id: 3,
                        floor: 4,
                        rooms: [
                            {
                                id: 4,
                                email: "neverland@yahoo.com",
                                type: "ROOM",
                                reservations: [],
                                name: "Neverland",
                                floor: 4,
                                maxPersons: 5
                            }
                        ]
                    })
            );

            return of(new HttpResponse({
                status: 200,
                body: floors
            }))
            //console.log(request);
        }

        if (request.url.startsWith(`${this.BASE_URL}/floor`)) {
            console.log(request);
            let floor: FloorModel;
            floor = new FloorModel().create({
                id: 1,
                floor: 5,
                rooms: [
                    new RoomModel().create(
                        {
                            id: 2,
                            email: "wonderland@yahoo.com",
                            type: "ROOM",
                            reservations: [new Booking().create(
                                {
                                    id: 4,
                                    roomId: 2,
                                    personalEmail: "sghitun@yahoo.com",
                                    startDate: "2019-07-01T09:00:00.000+0000",
                                    endDate: "2019-07-01T12:00:00.000+0000",
                                    description: "Retro meeting"
                                }), new Booking().create(
                                    {
                                        id: 5,
                                        roomId: 2,
                                        personalEmail: "sghitun@yahoo.com",
                                        startDate: "2019-07-03T14:00:00.000+0000",
                                        endDate: "2019-07-03T15:30:00.000+0000",
                                        description: "Retro meeting"
                                    })
                            ],
                            name: "Wonderland",
                            floor: 5,
                            maxPersons: 14
                        })
                ]
            });
            return of(new HttpResponse({
                status: 200,
                body: floor
            }))
        }
      
        if (request.url === `${this.BASE_URL}/prevalidation`){
            return of(new HttpResponse({
                status: 200
            }))
        }

        if (request.url.startsWith(`${this.BASE_URL}/reservation`)){
            
            let booking = new Booking().create({
                id: 123,
                email: "sghitun@yahoo.com",
                startDate: new Date(),
                endDate: new Date(new Date().getTime() + 200000),
                description: "some description"
            })
            return of(new HttpResponse({
                status: 200,
                body: booking
            }))
        }
        if (request.url.startsWith(`${this.BASE_URL}/reservations`)){
            console.log(request);
            
            return of(new HttpResponse({
                status: 200
            }))
        }
        if (request.url.startsWith(`${this.BASE_URL}/reservations`)){
            console.log(request);
            
            return of(new HttpResponse({
                status: 200
            }))
        }
        */
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
        // return request;
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
                .catch(error => {
                    console.log(error)
                })

            if (refreshedToken) {
                token = refreshedToken
            }
        }

        return request.clone({
            setHeaders: {
                "Authorization": `${token.token_type} ${token.access_token}`
            }
        });
    }

    // // return next.handle(request).pipe(
    // tap({
    // next: (response: HttpEvent<any>) => {
    // if (response instanceof HttpResponse) {
    // console.log("backend responded");
    // // response = response.clone({ body: "" })
    // }
    // if (response instanceof HttpErrorResponse) {
    // console.log("error response (never)");
    // console.log(response)
    // }
    // },
    // error: (error: HttpErrorResponse) => {
    // console.log(error.message);
    // // status, statusText, name, headers, type, url, ok
    // this._snackBar.open(
    // `Call to ${error.url} failed with ${error.status} - ${error.statusText}`,
    // 'Close',
    // {
    // duration: 7000
    // }
    // );

    // },
    // complete: () => console.log('on complete')
    // })
    // )

}
