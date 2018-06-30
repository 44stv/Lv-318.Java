import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { FeedbackCriteria } from '../models/feedback-criteria.model';
import { catchError, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { Question } from '../models/question.model';
import { environment } from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class FeedbackCriteriaService {
  private feedbackCriteriaUrl = environment.serverURL + '/feedback-criteria';


  constructor(private  http: HttpClient,
              private messageService: MessageService) {
  }

  getAllFeedbackCriteria(): Observable<FeedbackCriteria[]> {
    return this.http.get<FeedbackCriteria[]>(this.feedbackCriteriaUrl)
      .pipe(
        tap(feedbackCriterias => this.log(`fetched feedbackCriterias`)),
        catchError(this.handleError('getAllFeedbackCriterias', []))
      );
  }
  getAllEnumTypes(): Observable<String> {
    const feedbackCriteriaUrl = `${this.feedbackCriteriaUrl}/enums`;
    return this.http.get<String>(feedbackCriteriaUrl);
  }

  addFeedbackCritea(feedbackCriteria: FeedbackCriteria): Observable<FeedbackCriteria> {
    return this.http.post<FeedbackCriteria>(this.feedbackCriteriaUrl, feedbackCriteria);
  }


  getAllFeedbackCriteriaByTypeAndCategoryId(categoryId: number, type: String): Observable<FeedbackCriteria[]> {
    const feedbackCriteriaUrl = `${this.feedbackCriteriaUrl}/categoryId/${categoryId}/type/${type}`;
    return this.http.get<FeedbackCriteria[]>(feedbackCriteriaUrl)
      .pipe(
        tap(feedbackCriterias => this.log(`fetched feedbackCriterias`)),
        catchError(this.handleError('getAllFeedbackCriterias', []))
      );
  }

  getAllFeedbackCriteriaByCategoryId(categoryId: number): Observable<FeedbackCriteria[]> {
    const feedbackCriteriaUrl = `${this.feedbackCriteriaUrl}/categoryId/${categoryId}`;
    return this.http.get<FeedbackCriteria[]>(feedbackCriteriaUrl)
      .pipe(
        tap(feedbackCriterias => this.log(`fetched feedbackCriterias`)),
        catchError(this.handleError('getAllFeedbackCriterias', []))
      );
  }
  deleteFeedbackCriteria(number): Observable<FeedbackCriteria> {
    const id = number;
    const url = `${this.feedbackCriteriaUrl}/${id}`;
    return this.http.delete<FeedbackCriteria>(url, httpOptions)
      .pipe(
        tap(_ => this.log(`deleted feedbackCriteria id=${id}`)),
        catchError(this.handleError<FeedbackCriteria>('deletefeedbackCriteria'))
      );
  }

  getFeedbackCriteria(id: number): Observable<FeedbackCriteria> {
    const feedbackCriteriaUrl = `${this.feedbackCriteriaUrl}/${id}`;
    return this.http.get<FeedbackCriteria>(feedbackCriteriaUrl).pipe(
      tap(_ => this.log(`fetched feedbackCriteria id=${id}`)),
      catchError(this.handleError<FeedbackCriteria>(`get feedbackCriteria id=${id}`))
    );
  }

  updateFeedbackCriteria(feedbackCriteria: FeedbackCriteria): Observable<FeedbackCriteria> {
    return this.http.put(this.feedbackCriteriaUrl + '/' + feedbackCriteria.id, feedbackCriteria, httpOptions)
    .pipe(tap(_ => this.log(`update feedbackCriteria id=${feedbackCriteria.id}`)),
  catchError(this.handleError<any>('update Feedback Criteria'))
);
  }

  addQuestion(question: Question): Observable<Question> {
    return this.http.post<Question>(this.feedbackCriteriaUrl, question);
}

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);

      this.log(`${operation} failed: ${error.message}`);

      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add('HeroService: ' + message);
  }
}
