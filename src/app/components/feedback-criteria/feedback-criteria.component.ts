import {Component, OnInit, ViewChild} from '@angular/core';

import {FeedbackCriteria} from '../../models/feedback-criteria.model';
import {FeedbackCriteriaService} from '../../services/feedback-criteria.service';
import {MatPaginator, MatTableDataSource} from '@angular/material';
import {filter} from 'rxjs/operators';
import {Question} from '../../models/question.model';

@Component({
  selector: 'app-feedback-criteria',
  templateUrl: './feedback-criteria.component.html',
  styleUrls: ['./feedback-criteria.component.css']
})
export class FeedbackCriteriaComponent implements OnInit {

  feedbackCriterias: FeedbackCriteria[];
  displayedColumns = ['type', 'questions'];
  dataSource = new MatTableDataSource<FeedbackCriteria>();
  data: FeedbackCriteria[];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private feedbackCriteriaService: FeedbackCriteriaService) {
  }

  ngOnInit() {
    this.getAllFeedbackCriteria();
  }

  getAllFeedbackCriteria(): void {
    this.feedbackCriteriaService.getAllFeedbackCriteria()
      .subscribe(feedbackCriterias => {
        this.dataSource.data = feedbackCriterias;
        this.data = feedbackCriterias;
      });
    this.dataSource.paginator = this.paginator;

  }

  // applyFilter(searchTerm: string) {
  //   this.dataSource.filterPredicate = (criteria, searchTerm) => {
  //     if (searchTerm) {
  //       return this.containsIgnoringCase(criteria.id, searchTerm)
  //         || this.containsIgnoringCase(criteria.type, searchTerm)
  //         // || this.containsIgnoringCase(criteria.weight, searchTerm)
  //         || criteria.questions.reduce((accumulatedResult, question) => accumulatedResult
  //         || this.containsIgnoringCase(question.name, searchTerm), false);
  //     }
  //   };
  // }

  search(searchTerm: string) {
    if (searchTerm) {
      this.dataSource.data = this.data.filter(criteria => this.containsIgnoringCase(criteria.type, searchTerm)
        // || this.containsIgnoringCase(criteria.weight, searchTerm)
        || criteria.questions.reduce((accumulatedResult, question) => accumulatedResult
        || this.containsIgnoringCase(question.name, searchTerm), false)
        || criteria.questions.reduce((accumulatedResult, question) => accumulatedResult
        || this.containsIgnoringCase(question.weight, searchTerm), false)
      );
    } else {
      this.dataSource.data = this.data;
    }
  }

  private containsIgnoringCase(first: any, second: any): boolean {
    return first && second && first.toString().trim().toLowerCase().indexOf(second.toString().trim().toLowerCase()) >= 0;
  }
}
