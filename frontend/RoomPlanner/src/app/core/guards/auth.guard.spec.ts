import { TestBed, async, inject, getTestBed } from '@angular/core/testing';

import { AuthGuard } from './auth.guard';
import { AuthService } from '../core.module';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

fdescribe('AuthGuard', () => {
  describe('canActivate', () => {
    let injector: TestBed;
    let authService: AuthService
    let guard: AuthGuard;
    let routeMock: any = { snapshot: {}};
    let routerMock = {navigate: jasmine.createSpy('navigate')}

    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [AuthGuard, { provide: Router, useValue: routerMock },],
        imports: [HttpClientTestingModule]
      });
      
      injector = getTestBed();
      authService = injector.get(AuthService);
      guard = injector.get(AuthGuard);
    });

    it('should be created', () => {
      expect(guard).toBeTruthy();
    });

    it('should return true when access /login and current user is null', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/login'};

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return false when access /login and current user is not null', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/login'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'user'
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeFalsy();
    }));

    it('should redirect to /[userType] when access /login and current user is not null', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/login'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'user'
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith(['user']);
    }));

    it('should return true when access /room and type of current user is room', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/room'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'room'
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return true when access /user and type of current user is user', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/user'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'user'
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return true when access /user and type of current user is admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/user'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'admin'
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return true when access /admin and type of current user is admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/admin'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'admin'
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should redirect to /[userType] when access /room and type of current user is not room', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/room'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'user'
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith(['user']);
    }));

    it('should redirect to /[userType] when access /user and type of current user is not user or admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/user'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'room'
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith(['room']);
    }));

    it('should redirect to /[userType] when access /admin and type of current user is not admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/admin'};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: 'room'
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith(['room']);
    }));
  });
});
