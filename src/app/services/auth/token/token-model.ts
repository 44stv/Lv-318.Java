export class TokenModel {
  accessToken: string;

  // refreshToken: string;

  constructor(accessToken: string) {
    this.accessToken = accessToken;
    // this.refreshToken = refreshToken;
  }
}
