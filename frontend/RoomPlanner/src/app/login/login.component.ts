import { Component, OnInit, Input } from '@angular/core';
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
  
  hide=true;
  statusMessage: string;

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
  }


  ngOnInit() {
    this.statusMessage="";

    this.mode='determinate';
    this.value=0;
    this.diameter=30;
  }

  
  getErrorMessage() {
    return this.loginForm.controls.email.hasError('invalid') ? 'You must enter a value' :
      this.loginForm.controls.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  onSubmit() {

    //change this mock with actual form data
    console.log(this.loginForm.value.email);
    console.log(this.loginForm.value.password);
    let user: LoginModel = new LoginModel().create({
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    });

    // let scope = this;
    this.authService.authenticateUser(user)
      .then((user: LoggedUser) => {
        console.log(user);

        this.authService.setCurrentUser(user);

        
        switch(user.type){
          case "user":
            this.router.navigate(["/user"])
            break;
          case "room":
            break;
          case "admin":
            break;
          default:
            break;
        }
      })
      .catch((error) => {
        console.log(error);
      });
      
      this.mode='indeterminate';
  }

  navigateToUser(): any {
    this.router.navigate(['/user']);
  }
}
