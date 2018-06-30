import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transit } from '../models/transit.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransitService {

  private serviceUrl = `${environment.serverURL}/transit/`;

  constructor(private http: HttpClient) {
  }

  getTransits(): Observable<Transit[]> {
    return this.http.get<Transit[]>(this.serviceUrl);
  }

  addTransit(transit: Transit): Observable<Transit> {
    return this.http.post<Transit>(this.serviceUrl, transit);
  }

  getTransitById(id: number): Observable<Transit> {
    return this.http.get<Transit>(`${this.serviceUrl}/${id}`);
  }

  getTransitsByCategoryId(id: number): Observable<Transit[]> {
    return this.http.get<Transit[]>(`${this.serviceUrl}?categoryId=${id}`);
  }

  getTransitsByNextLevelCategoryName(categoryName: string): Observable<Transit[]> {
    return this.http.get<Transit[]>(`${this.serviceUrl}?nextLevelCategoryName=${categoryName}`);
  }
}
