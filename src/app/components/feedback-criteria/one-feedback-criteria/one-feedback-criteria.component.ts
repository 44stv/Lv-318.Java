import { Component, OnInit } from '@angular/core';
import { FeedbackCriteria } from '../../../models/feedback-criteria.model';
import { FeedbackCriteriaService } from '../../../services/feedback-criteria.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material';
import { AddQuestionComponent } from '../add-question/add-question.component';

@Component({
  selector: 'app-one-feedback-criteria',
  templateUrl: './one-feedback-criteria.component.html',
  styleUrls: ['./one-feedback-criteria.component.css']
})
export class OneFeedbackCriteriaComponent implements OnInit {

  feedbackCriteria: FeedbackCriteria;
  isReadOnly = true;

  constructor(private feedbackCriteriaService: FeedbackCriteriaService,
              private route: ActivatedRoute,
              private location: Location,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.getFeedbackCriteria();
  }

  getFeedbackCriteria(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.feedbackCriteriaService.getFeedbackCriteria(id)
      .subscribe(feedbackCriteria => {
        this.feedbackCriteria = feedbackCriteria;
        if (this.feedbackCriteria.type === 'RATING') {
          this.isReadOnly = false;
        }
      });
  }

  deleteFeedbackCriteria(id: number): void {
    this.feedbackCriteriaService.deleteFeedbackCriteria(id)
      .subscribe(() => this.gotBack());
  }

  updateFeedbackCriteria(): void {
    this.feedbackCriteriaService.updateFeedbackCriteria(this.feedbackCriteria)
      .subscribe(() => this.gotBack());
  }

  gotBack(): void {
    this.location.back();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddQuestionComponent, {
      width: '600px',
      data: this.isReadOnly
    });
    dialogRef.afterClosed().subscribe(result => {
      this.feedbackCriteria.questions.push(result);
    });
  }
}
