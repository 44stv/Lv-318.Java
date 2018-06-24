import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MapsService {

  private serviceUrl = environment.serverURL + '/stop';

  constructor(private http: HttpClient) {
  }

  getForwardDirection(id, direction) {
    return this.http.get(this.serviceUrl + '?id=' + id + '&dir=' + direction);
  }
}
