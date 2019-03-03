import { Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {AuthorizationService} from "../services/authorization.service";

/**
 * Permission checker class
 *
 */
@Injectable()
export class BasicPermission implements CanActivate {

  constructor(private router: Router, private authorizarion : AuthorizationService) {
  }

  /**
   * Method to check if the current user is logged in
   * 
   * @param route
   * @param state
   */
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    /*
    If logged in
     */
    if (localStorage.getItem('principalUser')) {
      return true;
    } else {
      this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
      return false;
    }
  }

}
