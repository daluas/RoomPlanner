import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginBookingComponent } from './login-booking.component';

describe('LoginBookingComponent', () => {
  let component: LoginBookingComponent;
  let fixture: ComponentFixture<LoginBookingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginBookingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
