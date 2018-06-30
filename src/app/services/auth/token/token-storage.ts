import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { TokenModel } from './token-model';
import { determineRole, Role } from '../roles';

const ACCESS_TOKEN = 'access_token';
const REFRESH_TOKEN = 'refresh_token';
const helper = new JwtHelperService();


@Injectable()
export class TokenStorage {

  private accessToken = window.localStorage.getItem(ACCESS_TOKEN);
  private refreshToken = window.localStorage.getItem(REFRESH_TOKEN);
  private decodedToken = this.decodeToken();

  constructor() {
  }

  public signOut() {
    window.localStorage.removeItem(ACCESS_TOKEN);
    // window.localStorage.removeItem(REFRESH_TOKEN);
    window.localStorage.clear();
    this.accessToken = null;
    this.refreshToken = null;
    this.decodedToken = null;
  }

  public saveToken(token: TokenModel) {
    const accessToken = token.accessToken;
    // let refreshToken = token.refreshToken;
    this.signOut();
    window.localStorage.setItem(ACCESS_TOKEN, accessToken);
    this.accessToken = accessToken;
    // window.localStorage.setItem(REFRESH_TOKEN, refreshToken);
    // this.refreshToken = refreshToken;
    this.decodedToken = this.decodeToken();
  }

  public getAccessToken(): string {
    return this.accessToken;
  }

  public getRefreshToken(): string {
    return this.refreshToken;
  }

  public isExpired(): boolean {
    if (this.accessToken == null) {
      return false;
    }
    return helper.isTokenExpired(this.accessToken);
  }

  public getRole(): Role {
    if (!this.hasToken()) {
      return Role.Unauthorized;
    }
    return determineRole(this.decodedToken.authorities[0].authority);
  }

  private decodeToken(): any {
    return helper.decodeToken(this.accessToken);
  }

  public hasToken(): boolean {
    return this.accessToken != null;
  }
}
