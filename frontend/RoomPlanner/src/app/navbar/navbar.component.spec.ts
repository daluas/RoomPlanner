import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { By } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { AuthService } from '../core/core.module';
import { Observable, Subscriber } from 'rxjs';
import { LoggedUser } from '../core/models/LoggedUser';

describe('Given a NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  let mockAuthService = {
    user: null,
    userSubject: null,
    getCurrentUser: function () {
      return new Observable((observer) => {
        observer.next(this.user);
        this.userSubject = observer;
      });
    },
    setCurrentUser: function (loggedUserModel) {
      this.user = loggedUserModel;
      this.userSubject.next(this.user);
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: AuthService, useValue: mockAuthService }],
      declarations: [NavbarComponent],
      imports: [FormsModule, MaterialDesignModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  function getRigntButtons() {
    return fixture.debugElement.query(By.css('.right-buttons'));
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not display buttons when user is null', () => {
    component.authService.setCurrentUser(null);
    fixture.detectChanges();
    let rightButtons = getRigntButtons();

    expect(rightButtons).toBeFalsy();
  });

  it('should display buttons when user is not null', () => {
    let currentUser = new LoggedUser().create({
      email: "test@test.com",
      type: "user"
    });

    component.authService.setCurrentUser(currentUser);
    fixture.detectChanges();
    let rightButtons = getRigntButtons();

    expect(rightButtons).toBeTruthy();
  });

  it('should display username when user with email is provided', () => {
    let currentUser = new LoggedUser().create({
      email: "testname@test.com",
      type: "user"
    });

    component.authService.setCurrentUser(currentUser);
    fixture.detectChanges();
    let username = fixture.debugElement.query(By.css('.username'));

    expect(username.nativeElement.innerText.includes('testname')).toBeTruthy();
  });

});
