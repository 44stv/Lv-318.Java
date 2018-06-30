import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders(({
    // 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' ,
    'Accept': 'application/json',
    'Content-Type': 'application/json'
      })),
};

@Injectable()
export class DiagramService {
  constructor(private http: HttpClient) {
  }

  getResults(url: string) {
    return this.http.get(url, httpOptions);
  }


}
