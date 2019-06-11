import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, CanLoad, Route, UrlSegment, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable, Subscriber } from 'rxjs';
import { AuthService } from '../services/auth/auth.service';
import { LoggedUser } from '../models/LoggedUser';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) { }

  canActivate(
    router: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let currentUser = this.authService.getCurrentUser();

    switch (state.url) {
      case '/login':
        if (currentUser) {
          this.router.navigate([currentUser.type]);
          return false;
        }
        break;
      case '/room':
        if (!currentUser) {
          this.router.navigate(['login']);
          return false;
        }
        if (currentUser.type !== 'room') {
          this.router.navigate([currentUser.type]);
          return false;
        }
        break;
      case '/user':
        if (!currentUser) {
          this.router.navigate(['login']);
          return false;
        }
        if (currentUser.type !== 'user' && currentUser.type !== 'admin') {
          this.router.navigate([currentUser.type]);
          return false;
        }
        break;
      case '/admin':
        if (!currentUser) {
          this.router.navigate(['login']);
          return false;
        }
        if (currentUser.type !== 'admin') {
          this.router.navigate([currentUser.type]);
          return false;
        }
        break;
    }

    return true;
  }
}
