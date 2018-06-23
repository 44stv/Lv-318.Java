import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, of} from 'rxjs';

import { environment } from '../../environments/environment';
import { Feedback } from '../models/feedback.model';



const httpOptions = {
  headers: new HttpHeaders({
     'Content-Type': 'application/json' })
};


@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private url = environment.serverURL + '/feedback';

  constructor(private http: HttpClient) { 

  }
  addFeedback(feedback:Feedback): Observable<Feedback> {
    return this.http.post<Feedback>(this.url ,feedback, httpOptions);
  }

  addAllFeedback(feedbacks: Feedback[]): Observable<Feedback[]> {
    const feedbackUrl = `${this.url}/add?feedbacks[]=${feedbacks}`;
    return this.http.post<Feedback[]>(feedbackUrl ,feedbacks, httpOptions);
  }
}
