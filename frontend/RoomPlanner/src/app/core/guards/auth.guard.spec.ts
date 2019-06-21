import { TestBed, async, inject, getTestBed } from '@angular/core/testing';

import { AuthGuard } from './auth.guard';
import { AuthService } from '../core.module';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material';
import { UserType } from '../enums/enums';

describe('Given a AuthGuard', () => {
  describe('with canActivate', () => {
    let injector: TestBed;
    let authService: AuthService
    let guard: AuthGuard;
    let routeMock: any = { snapshot: {} };
    let routerMock = { navigate: jasmine.createSpy('navigate') }

    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [AuthGuard, { provide: Router, useValue: routerMock }, MatSnackBar],
        imports: [HttpClientTestingModule, MatSnackBarModule]
      });

      injector = getTestBed();
      authService = injector.get(AuthService);
      guard = injector.get(AuthGuard);
    });

    it('should be created', () => {
      expect(guard).toBeTruthy();
    });

    it('should return true when access /login and current user is null', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/login' };

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return false when access /login and current user is not null', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/login' };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.PERSON
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeFalsy();
    }));

    it('should redirect to /[userType] when access /login and current user is not null', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/login' };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.PERSON
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith([UserType.PERSON.toLowerCase()]);
    }));

    it('should return true when access /room and type of current user is room', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.ROOM.toLowerCase()};
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.ROOM
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return true when access /user and type of current user is user', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.PERSON.toLowerCase() };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.PERSON
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return true when access /user and type of current user is admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.PERSON.toLowerCase() };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.ADMIN
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should return true when access /admin and type of current user is admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.ADMIN.toLowerCase() };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.ADMIN
      });

      expect(guard.canActivate(routeMock, routeStateMock)).toBeTruthy();
    }));

    it('should redirect to /[userType] when access /room and type of current user is not room', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.ROOM.toLowerCase() };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.PERSON
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith([UserType.PERSON.toLowerCase()]);
    }));

    it('should redirect to /[userType] when access /user and type of current user is not user or admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.PERSON.toLowerCase() };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.ROOM
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith([UserType.ROOM.toLowerCase()]);
    }));

    it('should redirect to /[userType] when access /admin and type of current user is not admin', inject([AuthGuard], (guard: AuthGuard) => {
      let routeStateMock: any = { snapshot: {}, url: '/' + UserType.ADMIN.toLowerCase() };
      spyOn(authService, 'getCurrentUser').and.returnValue({
        email: "test@test.com",
        type: UserType.ROOM
      });

      guard.canActivate(routeMock, routeStateMock);

      expect(routerMock.navigate).toHaveBeenCalledWith([UserType.ROOM.toLowerCase()]);
    }));
  });
});
