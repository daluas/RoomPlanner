import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginBookingComponent } from './login-booking.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MatSnackBarModule } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';

describe('LoginBookingComponent', () => {
  let component: LoginBookingComponent;
  let fixture: ComponentFixture<LoginBookingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginBookingComponent ],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        MatFormFieldModule,
        HttpClientTestingModule,
        RouterTestingModule,
        MatSnackBarModule
      ]
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
