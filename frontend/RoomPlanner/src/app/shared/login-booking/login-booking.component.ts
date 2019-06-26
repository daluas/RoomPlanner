import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { BookingService } from '../../core/services/booking/booking.service';
import { AuthService } from '../../core/services/auth/auth.service';
import { LoginModel } from '../../core/models/LoginModel';
import { LoggedUser } from '../../core/models/LoggedUser';
import { BookingPopupComponent } from '../booking-popup/booking-popup.component'

@Component({
  selector: 'app-login-booking',
  templateUrl: './login-booking.component.html',
  styleUrls: ['./login-booking.component.css']
})

export class LoginBookingComponent implements OnInit {

  @Input() isLogged:boolean;
  @Output() logged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() closeLogin: EventEmitter<boolean> = new EventEmitter();
  
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
  
  ngOnInit() {
    this.status = false;
    this.statusMessage = "";
  }

  getErrorMessage() {
    return this.loginBookingForm.controls.email.hasError('invalid') ? 'You must enter a value' :
      this.loginBookingForm.controls.email.hasError('email') ? 'Not a valid email' :
        '';
  }
  
  loginBookingSuccessfully() {
    this.status = true;
    this.logged.emit(true);
    this.countdownTimer();
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

          case "PERSON":
            this.loginBookingSuccessfully();
            break;

          case "ROOM":
            this.loginBookingFailed();
            break;

          case "ADMIN":
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
    this.logged.emit(false);
    this.loginBookingForm.reset();
  }

  logoutAutomatticaly() {
    console.log("logout")
    if(this.isLogged == true) {
      this.logged.emit(false); 
      this.loginBookingForm.reset();
    }  
  }

  countdownTimer() {
    let scope = this;
    setTimeout(() => {
      scope.logoutAutomatticaly()
    }, 60*1000);    
  }  

  cancelLogin() {
    this.closeLogin.emit(true);
  }
}
