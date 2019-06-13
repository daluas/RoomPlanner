import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { By } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { LoggedUser } from '../core/models/LoggedUser';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthGuard } from '../core/guards/auth.guard';
import { Router } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('Given a NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let routerMock = {navigate: (path: string) => {}}


  beforeEach((() => {
    TestBed.configureTestingModule({
      declarations: [NavbarComponent],
      providers: [AuthGuard, { provide: Router, useValue: routerMock},],
      imports: [FormsModule, MaterialDesignModule, HttpClientTestingModule, BrowserAnimationsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  function getRightButtons() {
    return fixture.debugElement.query(By.css('.right-buttons'));
  }

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should not display right buttons when current user is null', () => {
    component.authService.OnCurrentUserChanged(null);
    fixture.detectChanges();

    let rightButtons = getRightButtons();

    expect(rightButtons).toBeFalsy();
  });

  it('should display right buttons when current user is not null', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "user"
    }));
    fixture.detectChanges();

    let rightButtons = getRightButtons();

    expect(rightButtons).toBeTruthy();
  });

  it('should display username when current user is not null', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "testname@test.com",
      type: "user"
    }));
    fixture.detectChanges();

    let username = fixture.debugElement.query(By.css('.username'));

    expect(username.nativeElement.innerText.includes('testname')).toBeTruthy();
  });

  it('should display admin buttons when current user is of type admin', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "admin"
    }));
    fixture.detectChanges();

    let adminButtons = fixture.debugElement.query(By.css('.adminbuttons'));

    expect(adminButtons).toBeTruthy();
  });

  it('should not display admin buttons when current user is not of type admin', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "user"
    }));
    fixture.detectChanges();

    let adminButtons = fixture.debugElement.query(By.css('.adminbuttons'));

    expect(adminButtons).toBeFalsy();
  });

  it('should open room password form when logout button is pressed and current user is of type room', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "room"
    }));

    component.logOut();
    fixture.detectChanges();
    let roomPasswordForm = fixture.debugElement.query(By.css('.room-password-popup'));

    expect(roomPasswordForm).toBeTruthy();
  });

  it('should call logout function from auth service when current user is of type room, logoutroom button is pressed and passwoed is valid', async () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "room"
    }));
    component.authService.logout = jasmine.createSpy("logout");
    spyOn(component.authService, 'checkRoomPassword').and.returnValue(new Promise((res) => { res(true); }));
    component.roomPassword = "test";

    await component.logOutRoom();

    expect(component.authService.logout).toHaveBeenCalled();
  });

  it('should not call logout function from auth service when current user is of type room, logoutroom button is pressed and passwoed is invalid', async () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "room"
    }));
    component.authService.logout = jasmine.createSpy("logout");
    spyOn(component.authService, 'checkRoomPassword').and.returnValue(new Promise((res) => { res(false); }));
    component.roomPassword = "test";

    await component.logOutRoom();

    expect(component.authService.logout).toHaveBeenCalledTimes(0);
  });

  it('should call logout function from auth service when logout button is pressed and current user is of type user', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "user"
    }));
    component.authService.logout = jasmine.createSpy("logout");

    component.logOut();

    expect(component.authService.logout).toHaveBeenCalled();
  });

  it('should call logout function from auth service when logout button is pressed and current user is of type admin', () => {
    component.authService.OnCurrentUserChanged(new LoggedUser().create({
      email: "test@test.com",
      type: "admin"
    }));
    component.authService.logout = jasmine.createSpy("logout");

    component.logOut();

    expect(component.authService.logout).toHaveBeenCalled();
  });
});
