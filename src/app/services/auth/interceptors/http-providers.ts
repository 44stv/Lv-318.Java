import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { InterceptorService } from './interceptor.service';
import { ExpirationService } from './expiration.service';


export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: ExpirationService, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true}
];
