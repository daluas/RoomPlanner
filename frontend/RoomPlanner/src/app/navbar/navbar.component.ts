import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from '../core/core.module';
import { LoggedUser } from '../core/models/LoggedUser';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {

  currentUserObserver: Subscription;
  currentUser: LoggedUser;
  isLoggedIn: boolean = false;
  passwordFormOpen: boolean = false;
  roomPassword: string = "";
  roomPasswordInvalid: boolean = false;
  mobileMenuOpen: boolean = false;

  constructor(public authService: AuthService) { }

  ngOnInit() {
    this.currentUserObserver = this.authService.getCurrentUserObserver().subscribe(currentUser => {
      this.resetState();

      if (currentUser) {
        this.isLoggedIn = true;
        this.currentUser = currentUser;
      }
    });
  }

  resetState() {
    this.currentUser = null;
    this.isLoggedIn = false;
    this.passwordFormOpen = false;
    this.roomPassword = "";
    this.roomPasswordInvalid = false;
    this.mobileMenuOpen = false;
  }

  getUsernameFromEmail(): string {
    let email = this.currentUser.email;
    return email.split('@')[0];
  }

  checkEnterPressed(event: KeyboardEvent) {
    if(event.key === "Enter"){
      this.logOutRoom();
    }
  }

  isAdmin() {
    return this.currentUser.type === "admin";
  }

  logOut() {
    if (this.currentUser.type === "room") {
      if (this.passwordFormOpen) {
        this.closePasswordForm();
      } else {
        this.passwordFormOpen = true;
      }

      return;
    }

    this.authService.logout();
  }

  toggleMobileMenu() {
    if (this.mobileMenuOpen) {
      this.closePasswordForm();
    }

    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  closePasswordForm() {
    this.passwordFormOpen = false;
    this.roomPassword = "";
    this.roomPasswordInvalid = false;
  }

  closeNavPopups(){
    this.mobileMenuOpen = false;
    this.closePasswordForm();
  }

  logOutRoom() {
    if (this.roomPassword.length > 0) {
      this.roomPasswordInvalid = false;

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
    this.currentUserObserver.unsubscribe();
  }
}
