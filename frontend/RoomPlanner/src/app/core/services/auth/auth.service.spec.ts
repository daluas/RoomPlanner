import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { inject } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Interceptor } from '../../interceptor';
import { Router } from '@angular/router';

fdescribe('AuthService', () => {

  let routerMock = { navigate: (path: string) => { } }
  let authService: AuthService

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [],
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        // {
        //   provide: AuthService,
        //   useValue: AuthService
        // },
        {
          provide: Router,
          useValue: routerMock
        }
        // {
        //   provide: HTTP_INTERCEPTORS,
        //   useClass: Interceptor,
        //   multi: true
        // }
      ]
    });
    authService = TestBed.get(AuthService);

  });



  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  // it('should return false if not logged in', ()=> {
  //   spyOn(authService, 'checkIfLoggedIn').and.returnValue()
  // });
  it('should return authenticated user when authenticating', () => {
    spyOn(authService.httpClient, 'post').and.returnValue(new Promise((res, rej) => {

    }));
  });

  // it('should return authenticated user when authenticating', ()=>{
  //   spyOn(authService.httpClient, 'post').and.returnValue(new Promise((res, rej)=>{

  //   }));
  // });

  // it('should return error when authenticating', ()=>{
  //   spyOn(authService.httpClient, 'post').and.returnValue(new Promise((res, rej)=>{

  //   }));
  // });

  // });
  it('should return authenticated user when authenticating', () => {
    spyOn(authService.httpClient, 'post').and.returnValue(new Promise((res, rej) => {

    }));
  });
});
