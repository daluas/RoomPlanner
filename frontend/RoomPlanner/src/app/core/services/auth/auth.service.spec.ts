import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { inject } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Interceptor } from '../../interceptor';

fdescribe('AuthService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    declarations: [],
    imports: [HttpClientTestingModule],
    providers: [
      {
        provide: AuthService,
        useValue: AuthService
      },
      {
        provide: HTTP_INTERCEPTORS,
        useClass: Interceptor,
        multi: true
      }]
  }));

  // it('should be created', inject([AuthService], (service: AuthService) => {
  //     expect(service).toBeTruthy();
  //     // const service: AuthService = TestBed.get(AuthService);
  //   }
  // ) 
});
