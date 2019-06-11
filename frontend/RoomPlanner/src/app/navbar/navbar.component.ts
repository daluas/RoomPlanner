import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/core.module';
import { LoggedUser } from '../core/models/LoggedUser';
import { LoginToken } from '../core/models/LoginToken';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  currentUserSub: Subscription;
  currentUser: LoggedUser;
  isLoggedIn: boolean = false;
  username: string;
  usertype: string;
  passwordFormOpen: boolean = false;
  roomPassword: string = "";
  roomPasswordInvalid: boolean = false;

  mobileViewMenuOpen: boolean = false;

  constructor(public authService: AuthService) {

  }

  ngOnInit() {
    this.currentUserSub = this.authService.getCurrentUser().subscribe(currentUser => {
      this.resetState();

      if (currentUser) {
        this.isLoggedIn = true;
        this.usertype = currentUser.type;
        this.username = this.getUsernameFromEmail(currentUser.email);
      }
    })


  }

  getUsernameFromEmail(email: string): string {
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
        if (isValid) {
          scope.authService.logout();
        } else {
          scope.roomPasswordInvalid = true;
        }
      });
    }
  }


  ngOnDestroy() {
    this.currentUserSub.unsubscribe();
  }
}
