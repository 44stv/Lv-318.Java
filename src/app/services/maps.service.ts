import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {PointModel} from '../models/point.model';

@Injectable({
  providedIn: 'root'
})
export class MapsService {

  private serviceUrl = environment.serverURL + '/stop';

  constructor(private http: HttpClient) {
  }

  getForwardDirection(id, direction): Observable<PointModel[]> {
    return this.http.get<PointModel[]>(this.serviceUrl + '?id=' + id + '&dir=' + direction);
  }
}
