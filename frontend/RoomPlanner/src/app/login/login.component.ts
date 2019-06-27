import { Component, OnInit, Input, Output } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { LoginModel } from '../core/models/LoginModel';
import { AuthService } from '../core/services/auth/auth.service'
import { LoggedUser } from '../core/models/LoggedUser';
import { Router } from '@angular/router';
import { HttpResponse, HttpHandler } from '@angular/common/http';
import { Interceptor } from '../core/interceptor';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  statusMessage: string;
  status: boolean;
  isLoading: boolean;

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router) {
    this.loginForm.value.email = "";
    this.loginForm.value.password = "";
    this.statusMessage = "";
    this.status = true;
    this.isLoading = false;
  }

  ngOnInit() {
    this.statusMessage = "";

  }

  getErrorMessage() {
    return this.loginForm.controls.email.hasError('invalid') ? 'You must enter a value' :
      this.loginForm.controls.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  loginSuccessfully() {
    this.status = true;
    this.isLoading = false;
    this.statusMessage = "Login successfully";
  }

  loginFailed() {
    this.status = false;
    this.isLoading = false;
    this.statusMessage = "Invalid credentials";
    this.loginForm.reset();
    this.loginForm.markAllAsTouched();
  }

  authenticate() {
    var user: LoginModel = new LoginModel().create({
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    });

    this.authService.authenticateUser(user)
      .then((user: LoggedUser) => {
        console.log("got user: ", user);
        this.authService.OnCurrentUserChanged(user)
        switch (user.type) {

          case "PERSON":
            this.loginSuccessfully();
            this.router.navigate(["/PERSON"]);
            break;

          case "ROOM":
            this.loginSuccessfully();
            this.router.navigate(["/ROOM"]);
            break;

          case "ADMIN":
            this.loginSuccessfully();
            this.router.navigate(["/ADMIN"]);
            break;

          default:
            this.loginFailed();
            break;
        }
      })
      .catch((error) => {
        this.loginFailed();
        console.log(error);
      });

  }

  onSubmit() {
    
    this.isLoading = true;
    this.statusMessage = "";
    this.authenticate();

  }
}
