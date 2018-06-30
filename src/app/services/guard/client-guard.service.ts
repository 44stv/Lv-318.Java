import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { TokenStorage } from '../auth/token/token-storage';
import { Role } from '../auth/roles';

@Injectable()
export class ClientGuardService {

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
    return this.tokenStorage.getRole() === Role.Client || this.tokenStorage.getRole() === Role.Admin;
  }

}
