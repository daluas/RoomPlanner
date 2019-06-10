import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { By } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { MaterialDesignModule } from '../material-design/material-design.module';
import { AuthService } from '../core/core.module';

describe('Given a NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  let mockAuthService = {
    getCurrentUser: () => { }
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

  it('should not display buttons when user is null', async() => {
    spyOn(component.authService, 'getCurrentUser').and.returnValue(null);

    await component.updateUserState();
    fixture.detectChanges();
    let rightButtons = getRigntButtons();

    expect(rightButtons).toBeFalsy();
  });

  it('should display buttons when user is not null', async() => {
    spyOn(component.authService, 'getCurrentUser').and.returnValue({
      email: "test@test.com",
      type: "user"
    });

    await component.updateUserState();
    fixture.detectChanges();
    let rightButtons = getRigntButtons();

    expect(rightButtons).toBeTruthy();
  });

  it('should display username when user with email is provided', async() => {
    spyOn(component.authService, 'getCurrentUser').and.returnValue({
      email: "testid@test.com",
      type: "user"
    });

    await component.updateUserState();
    fixture.detectChanges();
    let username = fixture.debugElement.query(By.css('.username'));

    expect(username.nativeElement.innerText.includes('testid')).toBeTruthy();
  });

});
