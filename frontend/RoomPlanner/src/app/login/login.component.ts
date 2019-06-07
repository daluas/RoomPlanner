import { Component, OnInit } from '@angular/core';
import { Validators, FormGroup, Form, NgForm, FormBuilder} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { LoginModel } from '../core/models/LoginUser';
import { AuthService } from '../core/services/auth/auth.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hide = true;
  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });


  constructor(private fb: FormBuilder) { 
    this.loginForm.value.email="";
   this.loginForm.value.password="";
  }

  ngOnInit() {
   
  }

  onSubmit() { 
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


  getErrorMessage() {
    return this.loginForm.controls.email.hasError('invalid') ? 'You must enter a value' :
        this.loginForm.controls.email.hasError('email') ? 'Not a valid email' :
            '';
  }

}
