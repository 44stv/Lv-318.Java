import { Component, OnInit } from '@angular/core';
import { AddQuestionComponent } from '../add-question/add-question.component';
import { FeedbackCriteria } from '../../../models/feedback-criteria.model';
import { FeedbackCriteriaService } from '../../../services/feedback-criteria.service';
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material';
import { Question } from '../../../models/question.model';

@Component({
  selector: 'app-add-feedback-criteria',
  templateUrl: './add-feedback-criteria.component.html',
  styleUrls: ['./add-feedback-criteria.component.css']
})
export class AddFeedbackCriteriaComponent implements OnInit {

  feedbackCriteria = new FeedbackCriteria();
  questions: Question [] = [];
  enumTypes: any;
  isReadOnly = true;

  constructor(private feedbackCriteriaService: FeedbackCriteriaService,
              private location: Location,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.getAllEnumTypes();

  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddQuestionComponent, {
      width: '400px',
      data: this.isReadOnly
    });
    dialogRef.afterClosed().subscribe(result => {
      if (!(result == null)) {
        this.questions.push(result);
      }
    });
  }

  addFeedbackCriteria(): void {
    this.feedbackCriteria.questions = this.questions;
    this.feedbackCriteriaService.addFeedbackCritea(this.feedbackCriteria)
      .subscribe(() => this.gotBack());
  }

  ifRatingType(type: any) {
    if (type === 'RATING') {
      this.isReadOnly = false;
    }
  }

  gotBack(): void {
    this.location.back();
  }

  getAllEnumTypes(): void {
    this.feedbackCriteriaService.getAllEnumTypes()
      .subscribe(enumType => {
        this.enumTypes = enumType;
      });
  }

}
