import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, CanLoad, Route, UrlSegment, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    // using a service check auth and user type
    // if not auth => redirect to /login if not on it already
    // if auth && user type == 'room' => redirect to /room if not on it already 
    // if auth && user type == 'user' => redirect to /user if not on it already
    // if auth && user type == 'admin' => redirect to /adminPage if not on it already
    
    // EXAMPLE: this.router.navigate(['login']);

    // if redirect, send return false
    // if remain on same page, return true

    return true;
  }
}
