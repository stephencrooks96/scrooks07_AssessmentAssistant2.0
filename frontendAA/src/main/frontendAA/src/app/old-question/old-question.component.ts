import { Component, OnInit } from '@angular/core';
import {QuestionType, TutorQuestionPojo} from "../modelObjs/objects.model";
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {NgForm} from "@angular/forms";
import {KatexOptions} from "ng-katex";

@Component({
  selector: 'app-old-question',
  templateUrl: './old-question.component.html',
  styleUrls: ['./old-question.component.css']
})
export class OldQuestionComponent implements OnInit {

  oldQuestions : TutorQuestionPojo[];
  questionTypesToShow : QuestionType[];
  testID : number;
  options: KatexOptions = {
    displayMode: true,
  };
  constructor(private testServ : TestService, private editTest: EditTestComponent) {
    this.testID = this.editTest.testID;
  }

  ngOnInit() {
    this.getQuestionTypes();
    this.getOldQuestions(this.testID);
  }

  getOldQuestions(testID) {
    return this.testServ.getOldQuestions(testID).subscribe(questions => this.oldQuestions = questions);
  }

  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  addExistingQuestion(questionID) {
    this.testServ.addExistingQuestion(questionID, this.testID)
      .subscribe(success => {
        this.editTest.getQuestions(this.testID);
        this.getOldQuestions(this.testID);
      }, error => {
        return;
      });
  }

}
