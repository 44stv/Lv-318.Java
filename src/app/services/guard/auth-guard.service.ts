import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot } from '@angular/router';
import { TokenStorage } from '../auth/token/token-storage';

@Injectable()
export class AuthGuardService implements CanActivate, CanActivateChild {

  constructor(private tokenStorage: TokenStorage,
              private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.checkRights();
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.canActivate(childRoute, state);
  }

  checkRights(): boolean {
    if (this.tokenStorage.hasToken()) {
      this.router.navigate(['/error/']);
      return false;
    } else {
      return true;
    }
  }
}

