import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup, FormBuilder } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginModel } from '../core/models/LoginUser';
import { AuthService } from '../core/core.module';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  password: FormControl;
  email: FormControl;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {
    // this.email = new FormControl('', [Validators.required, Validators.email]);
    // this.password = new FormControl('', [Validators.required]);
    // this.loginForm = this.formBuilder.group({ email: this.email, password: this.password });

    this.loginForm = this.formBuilder.group({
      email: ['', {
        validators: [Validators.required, Validators.email],
        updateOn: 'blur'
      }],
      password: ['', {
        validators: [Validators.required],
        updateOn: 'blur'
      }],
    })
  }

  ngOnInit() {

  }



  getErrorMessage() {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('email') ? 'Not a valid email' : '';

  }

  onSubmit() {

    //change this mock with actual form data
    let user: LoginModel = new LoginModel().create({
      email: "admin1@cegeka.ro",
      password: "admin1"
    });

    this.authService.authenticateUser(user)
      .then((user) => {
        console.log(user);
      })
      .catch((error) => {
        console.log(error);
      });
  }
}
