import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/core.module';
import { LoggedUser } from '../core/models/LoggedUser';
import { LoginToken } from '../core/models/LoginToken';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  currentUser: any;
  isLoggedIn: boolean = false;
  username: string;
  usertype: string;
  passwordFormOpen: boolean = false;
  roomPassword: string = "";
  roomPasswordInvalid: boolean = false;

  mobileViewMenuOpen: boolean = false;

  constructor(public authService: AuthService) { }

  ngOnInit() {
    this.updateUserState();
  }

  async updateUserState() {
    this.resetState();

    this.currentUser = await this.authService.getCurrentUser();

    if(this.currentUser){
      this.isLoggedIn = true;
      this.usertype = this.currentUser.type;
      this.username = this.getUsernameFromEmail(this.currentUser.email);
    }
  }

  getUsernameFromEmail(email: string): string{
    return email.split('@')[0];
  }

  resetState() {
    this.isLoggedIn = false;
    this.username = "";
    this.usertype = "";
    this.roomPassword = "";
    this.passwordFormOpen = false;
    this.roomPasswordInvalid = false;
    this.currentUser = null;
  }

  isAdmin() {
    return this.usertype === "admin";
  }

  logOut() {
    if (this.usertype === "room") {
      if (this.passwordFormOpen) {
        this.closePasswordForm();
      } else {
        this.passwordFormOpen = true;
      }
      return;
    }

    this.authService.logout();
    this.updateUserState();
  }

  closePasswordForm() {
    this.roomPassword = "";
    this.passwordFormOpen = false;
    this.roomPasswordInvalid = false;
  }

  logOutRoom() {
    if (this.roomPassword.length > 0) {
      let scope = this;
      this.authService.checkRoomPassword(this.roomPassword).then((isValid) => {
        if(isValid){
          scope.authService.logout();
          scope.updateUserState();
        } else {
          scope.roomPasswordInvalid = true;
        }
      });
    }
  }
}
