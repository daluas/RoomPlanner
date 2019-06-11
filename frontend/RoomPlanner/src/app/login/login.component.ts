import { Component, OnInit, Input, Output } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { LoginModel } from '../core/models/LoginUser';
import { AuthService } from '../core/services/auth/auth.service'
import { ThemePalette, ProgressSpinnerMode } from '@angular/material';
import { LoggedUser } from '../core/models/LoggedUser';
import { Router } from '@angular/router';
import { HttpResponse, HttpHandler } from '@angular/common/http';
import { Interceptor } from '../core/interceptor';
import { ReactiveFormsModule }  from '@angular/forms';

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
    this.statusMessage = "";
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

  loginSuccessfully(){
    this.status = true;
    this.isLoading = false;
    this.statusMessage = "Login successfully";
  }

  loginFailed(){
    this.status = false;
    this.isLoading = false;
    this.statusMessage = "Invalid credentials";
    this.loginForm.reset();
  }

  authenticate() {
    var user: LoginModel = new LoginModel().create({
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    });

    this.authService.authenticateUser(user)
      .then((user: LoggedUser) => {
        console.log(user);
        this.authService.setCurrentUser(user)
        switch (user.type) {
          
          case "user":
            this.loginSuccessfully();
            setTimeout(() => {
              this.router.navigate(["/user"]);
            }, 1000);
            break;

          case "room":
          this.loginSuccessfully();
            setTimeout(() => {
              this.router.navigate(["/room"]);
            }, 1000);
            break;

          case "admin":
          this.loginSuccessfully();
            setTimeout(() => {
              this.router.navigate(["/admin"]);
            }, 1000);
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
    this.mode = 'indeterminate';
    this.isLoading = true;
    this.statusMessage = "";

    setTimeout(() => {
      this.authenticate();
    },
      1000);
  }
}
