import {Component} from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-back-to-previous-page-btn',
  templateUrl: './back-to-previous-page-btn.component.html',
  styleUrls: ['./back-to-previous-page-btn.component.css']
})
export class BackToPreviousPageBtnComponent {

  constructor(private location: Location) {
  }

  cancel() {
    this.location.back();
  }
}
