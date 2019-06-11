import { TestBed, inject, async } from '@angular/core/testing';
import {
    HttpClientTestingModule,
    HttpTestingController,

} from '@angular/common/http/testing';

import { AuthService } from './services/auth/auth.service';
import { Interceptor } from './interceptor';
import { HTTP_INTERCEPTORS, HttpClient, HttpInterceptor } from '@angular/common/http';
import { LoginModel } from './models/LoginUser';
import { AppRoutingModule } from '../app-routing.module';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

// getInterceptorInstance<T extends HttpInterceptor>(interceptors: HttpInterceptor[], type: any): HttpInterceptor {
//     let searchedInterceptor: HttpInterceptor = null;
//     interceptors.forEach((interceptor: HttpInterceptor) => {
//         if (interceptor instanceof type) {
//             searchedInterceptor = interceptor;
//         }
//     });
//     return searchedInterceptor;
// }
// interceptorInstance = getInterceptorInstance<MyInterceptor>(
//     TestBed.get(HTTP_INTERCEPTORS),
//     MyInterceptor
// );

describe(`Interceptor`, () => {

    let authService: AuthService;
    let httpMock: HttpTestingController;
    let loginModel : LoginModel = new LoginModel().create({
        email: "room1@cegeka.ro",
        password: "room.1"
    });
    let routerStub: Router;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                // .withRoutes([
                // {
                //     path: '',
                //     component: BlankCmp
                // },
                // {
                //     path: 'simple',
                //     component: SimpleCmp
                // }
                // ])
            ],
            providers: [
                {
                    provide: Router,
                    useValue: routerStub
                },
                {
                    provide: AuthService,
                    useClass: AuthService
                },
                {
                    provide: HTTP_INTERCEPTORS,
                    useClass: Interceptor,
                    multi: true,
                },
            ],
        }).compileComponents();

        authService = TestBed.get(AuthService);
        httpMock = TestBed.get(HttpTestingController);
    });

    // it(`Fake Login with should set isAuthenticated Flag and finally call route: ects`,
    //     async(inject([AuthService, HttpTestingController],
    //         (service: AuthService, backend: HttpTestingController) => {
    //             // spyOn(service, 'authenticateUser').and.returnValue(true);

               

    //             service.authenticateUser(loginModel);
    //             // Fake a HTTP response with a Bearer Token
    //             // Ask the HTTP mock to return some fake data using the flush method:
    //             expect(service.authenticateUser(loginModel)).toBe(loginModel)
    //         })
    //     )
    // );


    // it('should add Accept-Language to Headers', inject([HttpClient, HttpTestingController],
    //     (http: HttpClient, mock: HttpTestingController) => {

    //         http.get('/api').subscribe(response => expect(response).toBeTruthy());
    //         const request = mock.expectOne(req => (req.headers.has('Accept-Language') && req.headers.get('Accept-Language') === 'ar'));

    //         request.flush({ data: 'test' });
    //         mock.verify();
    //     }
    // ));

    // afterEach(inject([HttpTestingController], (mock: HttpTestingController) => {
    //     mock.verify();
    // }));

    // it('should add an Authorization header', () => {
    //     let user = new LoginModel().create({
    //         email: "room1@cegeka.ro",
    //         password: "room.1"
    //     });
    //     // authService.authenticateUser(user).then(
    //     //     response => {
    //     //         expect(response).toBeTruthy();
    //     //     }
    //     // )

    //     // const httpRequest = httpMock.expectOne(`${authService.BASE_URL}/`);
    //     const httpRequest = httpMock.expectOne(`localhost:4200/auth/signin`);

    //     expect(httpRequest.request.headers.has('Authorization')).toEqual(true);
    //     expect(httpRequest.request.headers.get('Authorization')).toBe('U-T-O-K-E-N');
    // });
});