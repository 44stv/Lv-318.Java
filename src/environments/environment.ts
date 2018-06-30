// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,

  // serverURL: 'http://localhost:8080',
  serverURL: 'http://ec2-18-184-79-174.eu-central-1.compute.amazonaws.com',

  accessTokenHeader: 'Authorization',
  refreshTokenHeader: 'Refresh-token',

  auth: {
    clientID: 'fMvXBLfJoy8yuoUoTGnTa8kI-3gEePfQ',
    domain: 'uatransport.eu.auth0.com',
    audience: 'http://localhost:4200',
    redirect: 'http://localhost:4200/callback',
    scope: 'openid profile email'
  }
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
