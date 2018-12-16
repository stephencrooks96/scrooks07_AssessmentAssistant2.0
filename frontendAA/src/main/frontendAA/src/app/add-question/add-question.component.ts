import { Component, OnInit } from '@angular/core';
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {Alternative, CorrectPoint, QuestionType, TutorQuestionPojo} from "../modelObjs/objects.model";

@Component({
  selector: 'app-add-question',
  templateUrl: './add-question.component.html',
  styleUrls: ['./add-question.component.css']
})
export class AddQuestionComponent implements OnInit {


  questionInsert = new TutorQuestionPojo();
  questionTypesToShow: QuestionType[];
  constructor(private editTest: EditTestComponent, private testServ: TestService) { }

  ngOnInit() {
    this.getQuestionTypes();
    this.questionInsert.correctPoints = [];
    this.addCorrectPoint();
  }

  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  addCorrectPoint() {
    const correctPoint = new CorrectPoint();
    correctPoint.alternatives = [];
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    correctPoint.alternatives.push(alternative);
    correctPoint.feedback = '';
    correctPoint.marksWorth = 0;
    correctPoint.phrase = '';
    correctPoint.questionID = 0;
    this.questionInsert.correctPoints.push(correctPoint);

    return false;
  }

  addAlternative(i) {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    this.questionInsert.correctPoints[i].alternatives.push(alternative);
    return false;
  }

  trackById(index: number, obj: any): any {
    return index;
  }

}
