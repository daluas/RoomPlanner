import { Component, OnInit, Input, Output } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { LoginModel } from '../core/models/LoginUser';
import { AuthService } from '../core/services/auth/auth.service'
import { ThemePalette, ProgressSpinnerMode } from '@angular/material';
import { LoggedUser } from '../core/models/LoggedUser';
import { Router } from '@angular/router';


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


  @Input() color: ThemePalette;
  @Input() diameter: number;
  @Input() mode: ProgressSpinnerMode;
  @Input() strokeWidth: number;
  @Input() value: number;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router) {
    this.loginForm.value.email = "";
    this.loginForm.value.password = "";
    this.statusMessage = "hello";
    this.status = true;
    this.isLoading = true;
  }


  ngOnInit() {
    this.statusMessage = "";

    this.mode = 'determinate';
    this.value = 0;
    this.diameter = 30;
  }


  getErrorMessage() {
    return this.loginForm.controls.email.hasError('invalid') ? 'You must enter a value' :
      this.loginForm.controls.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  authenticate() {
    let user: LoginModel = new LoginModel().create({
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    });
    let scope = this;

    this.authService.authenticateUser(user)
      .then((user: LoggedUser) => {
        // new LoggedUser().create(body)
        // this.authService.setCurrentUser(up)
        switch (user.type) {
          case "user":
            this.router.navigate(["/user"])
            break;
          case "room":
            this.router.navigate(["/room"])
            break;
          case "admin":
            this.router.navigate(["/admin"])
            break;
          default:
            this.status = false;
            break;
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }
  getStatus(): boolean {
    return this.status = true;
  }
  onSubmit() {
    this.mode = 'indeterminate';

    setTimeout(() => {
      this.status = this.getStatus();
      this.isLoading = false;
      if (this.status == false) {
        this.statusMessage = "Invalid credentials";
        //to do
        //var line = document.getElementsByClassName(".mat-form-field-underline");
        return;
      }
      this.statusMessage = "Login successfully";
      setTimeout(() => {

        this.authenticate()
      }, 1000);
    },
      1000);
  }
}