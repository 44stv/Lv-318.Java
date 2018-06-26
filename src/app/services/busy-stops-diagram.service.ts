import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Stop} from '../models/stop.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BusyStopsDiagramService {

  private serviceUrl = environment.serverURL + '/stop/';

  constructor(private http: HttpClient) {
  }

  getStopsByTransitId(transitId: number): Observable<Stop[]> {
    return this.http.get<Stop[]>(this.serviceUrl + '?transit-id=' + transitId);
  }

  addStop(stop: Stop, id: number): Observable<Stop> {
    return this.http.post<Stop>(this.serviceUrl + '/' + id, stop);
  }


}
