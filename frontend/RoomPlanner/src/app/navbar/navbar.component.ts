import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isLoggedIn: boolean = false;
  username: string;
  usertype: string;
  passwordFormOpen: boolean = false;
  roomPassword: string = "";
  roomPasswordInvalid: boolean = false;

  constructor() { }

  ngOnInit() {
    this.updateUserState();
  }

  updateUserState() {
    this.resetState();

    // TO DO

    // get from auth service if user is logged in
    this.isLoggedIn = true;

    // if logged in
    // get user object and set:

    // username
    this.username = "mockusername";

    // usertype
    this.usertype = "room"
  }

  resetState() {
    this.isLoggedIn = false;
    this.username = "";
    this.usertype = "";
    this.roomPassword = "";
    this.passwordFormOpen = false;
    this.roomPasswordInvalid = false;
  }

  isAdmin() {
    return this.usertype === "admin";
  }

  logOut() {
    if (this.usertype === "room") {
      if (this.passwordFormOpen) {
        this.roomPassword = "";
        this.passwordFormOpen = false;
        this.roomPasswordInvalid = false;
      } else {
        this.passwordFormOpen = true;
      }
      return;
    }

    // TO DO
    // call sign out function from service
    console.log('updateUserState');

    this.updateUserState();
  }

  logOutRoom() {

    if (this.roomPassword.length > 0) {
      console.log('logOutRoom');

      this.roomPasswordInvalid = true;

      // TO DO
      // return new Promise(async (res)=>{
      //   if (!scope.passwordFormOpen) {
      //     scope.passwordFormOpen = true;
      //     res(false);
      //   } else {
      //     //
      //     //await for service to validate password, .then((bool) => { res(bool) })

      //     let roomPasswordValid = false;

      //     if(roomPasswordValid){
      //       res(true);
      //     }

      //     this.roomPasswordInvalid = true;
      //     res(false)
      //   }
      // });
    }
  }
}
