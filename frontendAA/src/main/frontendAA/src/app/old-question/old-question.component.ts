import { Component, OnInit } from '@angular/core';
import {QuestionType, TutorQuestionPojo} from "../modelObjs/objects.model";
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";

@Component({
  selector: 'app-old-question',
  templateUrl: './old-question.component.html',
  styleUrls: ['./old-question.component.css']
})
export class OldQuestionComponent implements OnInit {

  oldQuestions : TutorQuestionPojo[];
  questionTypesToShow : QuestionType[];
  constructor(private testServ : TestService, private editTest: EditTestComponent) { }

  ngOnInit() {
    this.getQuestionTypes();
    this.getOldQuestions(this.editTest.testID);
  }

  getOldQuestions(testID) {
    return this.testServ.getOldQuestions(testID).subscribe(questions => this.oldQuestions = questions);
  }

  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

}
