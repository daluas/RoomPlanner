import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, CanLoad, Route, UrlSegment, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable, Subscriber } from 'rxjs';
import { AuthService } from '../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(public authService: AuthService, private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let currentUser = this.authService.getCurrentUser();
    console.log(currentUser);
    if (state.url !== '/login' && !currentUser) {
      this.router.navigate(['login']);
      return false;
    }
    
    if (state.url === '/login' && currentUser) {
      this.router.navigate([currentUser.type]);
      return false;
    }

    if (state.url === '/ROOM' && currentUser.type !== 'ROOM'){
      this.router.navigate([currentUser.type]);
      return false;
    }

    if (state.url === '/PERSON' && ['PERSON', 'ADMIN'].indexOf(currentUser.type) === -1){
      this.router.navigate([currentUser.type]);
      return false;
    }

    if(state.url === '/ADMIN' && currentUser.type !== 'ADMIN'){
      this.router.navigate([currentUser.type]);
      return false;
    }

    return true;
  }
}
