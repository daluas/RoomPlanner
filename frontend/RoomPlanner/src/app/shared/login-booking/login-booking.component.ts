import { Component, OnInit, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { BookingService } from '../../core/services/booking/booking.service';
import { AuthService } from '../../core/services/auth/auth.service';
import { LoginModel } from '../../core/models/LoginModel';
import { LoggedUser } from '../../core/models/LoggedUser';
import { BookingPopupComponent } from '../booking-popup/booking-popup.component'
import { UserType } from '../../core/enums/enums';

@Component({
  selector: 'app-login-booking',
  templateUrl: './login-booking.component.html',
  styleUrls: ['./login-booking.component.css']
})

export class LoginBookingComponent implements OnInit {

  @Input() isLogged: boolean;
  @Input() isNewBooking: boolean;

  @Output() logged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() closeLogin: EventEmitter<boolean> = new EventEmitter<boolean>();

  loginBookingForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  constructor(public bookingService: BookingService, private fb: FormBuilder, private authService: AuthService) {
    this.loginBookingForm.value.email = "";
    this.loginBookingForm.value.password = "";
  }

  status: boolean;
  statusMessage: string;
  isLogout: boolean;
  counter: number;

  ngOnInit() {
    this.status = false;
    this.statusMessage = "";
    this.isNewBooking = false;
    this.isLogout = false;
    this.counter = 60;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.logoutTimer();
  }

  getErrorMessage() {
    return this.loginBookingForm.controls.email.hasError('invalid') ? 'You must enter a value' :
      this.loginBookingForm.controls.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  loginBookingSuccessfully() {
    this.status = true;
    this.logged.emit(true);
  }

  loginBookingFailed() {
    this.status = false;
    this.statusMessage = "Invalid credentials";
    this.logged.emit(false);
    this.loginBookingForm.reset();
    this.loginBookingForm.markAllAsTouched();
  }

  authenticate() {
    var user: LoginModel = new LoginModel().create({
      email: this.loginBookingForm.value.email,
      password: this.loginBookingForm.value.password
    });

    this.authService.authenticateUser(user)
      .then((user: LoggedUser) => {
        console.log("got user: ", user);
        this.authService.OnCurrentUserChanged(user)

        switch (user.type) {

          case UserType.PERSON:
            this.loginBookingSuccessfully();
            break;

          case UserType.ROOM:
            this.loginBookingFailed();
            break;

          case UserType.ADMIN:
            this.loginBookingSuccessfully();
            break;

          default:
            this.loginBookingFailed();
            break;
        }
      })
      .catch((error) => {
        this.loginBookingFailed();
        console.log(error);
      });
  }

  logoutBooking() {
    console.log("logout");
    this.isLogout = true;
    this.logged.emit(false);
    this.loginBookingForm.reset();
  }

  countdownTimer() {
    var countdownInterval = setInterval(() => {
      if (this.isLogout == false) {
        this.counter = this.counter - 1;
        //console.log(this.counter)
        if (this.counter === 0) {
          this.counter = 60;
          clearInterval(countdownInterval);
        }
      }
      else {
        this.counter = 60;
        clearInterval(countdownInterval);
      }
    }, 1000)

  }

  logoutTimer() {
    let scope = this;
    if (this.isLogout == false) {

      if (this.isNewBooking == false) {
        this.countdownTimer();
        var startTimeout = setTimeout(() => {
          scope.logoutBooking()
        }, 60 * 1000);
      }

      if (this.isNewBooking == true) {
        clearTimeout(startTimeout);
        this.countdownTimer();
        var startTimeout = setTimeout(() => {
          scope.logoutBooking()
        }, 60 * 1000);
      }

    }
  }

  cancelLogin() {
    this.closeLogin.emit(true);
  }
}
