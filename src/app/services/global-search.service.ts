import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { Transit } from '../models/transit.model';
import { environment } from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class GlobalSearchService {
  private globalSearchUrl = environment.serverURL + '/search';

  searchValue = '';

  constructor(private  http: HttpClient) {
  }

  setSearchValue(value: string) {
    this.searchValue = value;
  }

  getResults(): Observable<Transit []> {
    const globalSearchUrl = `${this.globalSearchUrl}/?search=${this.searchValue}`;
    return this.http.get<Transit []>(globalSearchUrl);
  }
}
