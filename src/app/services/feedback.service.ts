import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {environment} from '../../environments/environment';


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json; charset=UTF-8'})
};


@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private url = environment.serverURL + '/feedback';

  constructor(private http: HttpClient) {
  }

  saveAllFeedback(feedbacks) {
    return this.http.post(this.url + '/add', feedbacks, httpOptions);
  }
}
