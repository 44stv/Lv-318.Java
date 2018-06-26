import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class DiagramService {
  constructor(private http: HttpClient) {
  }

  getResults(url: string) {
    return this.http.get(url);
  }


}
