import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, CanLoad, Route, UrlSegment, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable, Subscriber } from 'rxjs';
import { AuthService } from '../services/auth/auth.service';
import { UserType } from '../enums/enums';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(public authService: AuthService, private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let currentUser = this.authService.getCurrentUser();
    let linkToRoomPage = '/' + UserType.ROOM.toLowerCase();
    let linkToPersonPage = '/' + UserType.PERSON.toLowerCase();
    let linkToAdminPage = '/' + UserType.ADMIN.toLowerCase();

    if (state.url !== '/login' && !currentUser) {
      this.router.navigate(['login']);
      return false;
    }
    
    if (state.url === '/login' && currentUser) {
      this.router.navigate([currentUser.type.toLowerCase()]);
      return false;
    }

    if (state.url === linkToRoomPage && currentUser.type !== UserType.ROOM){
      this.router.navigate([currentUser.type.toLowerCase()]);
      return false;
    }

    if (state.url === linkToPersonPage && currentUser.type !== UserType.PERSON && currentUser.type !== UserType.ADMIN){
      this.router.navigate([currentUser.type.toLowerCase()]);
      return false;
    }

    if(state.url === linkToAdminPage && currentUser.type !== UserType.ADMIN){
      this.router.navigate([currentUser.type.toLowerCase()]);
      return false;
    }

    return true;
  }
}
