import { Component, OnInit, Input, Inject } from '@angular/core';
import { Question } from '../../../models/question.model';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-question',
  templateUrl: './add-question.component.html',
  styleUrls: ['./add-question.component.css']
})
export class AddQuestionComponent implements OnInit {
  @Input() question: Question = new Question();
  isReadOnly: boolean;

  constructor(private dialogRef: MatDialogRef<AddQuestionComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.isReadOnly = data;
  }

  ngOnInit() {
  }

  close(name: string, weight: number) {
    this.dialogRef.close();
  }

  add(name: string, weight: number) {
    if (!(name == null)) {
      this.dialogRef.close({name, weight} as Question);
    }
  }

}
