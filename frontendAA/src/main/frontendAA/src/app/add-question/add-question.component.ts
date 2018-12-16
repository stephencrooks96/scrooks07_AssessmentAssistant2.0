import {Component, OnInit} from '@angular/core';
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {Alternative, CorrectPoint, QuestionType, Tests, TutorQuestionPojo} from "../modelObjs/objects.model";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-add-question',
  templateUrl: './add-question.component.html',
  styleUrls: ['./add-question.component.css']
})
export class AddQuestionComponent implements OnInit {


  questionInsert = new TutorQuestionPojo();
  questionTypesToShow: QuestionType[];

  constructor(private editTest: EditTestComponent, private testServ: TestService) {
  }

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

  addQuestion(form: NgForm) {
    this.questionInsert.testID = this.editTest.testID;
    if (!this.questionInsert.question.questionType || !this.questionInsert.question.questionContent || this.questionInsert.question.questionContent.trim().length <= 0 || !this.questionInsert.question.maxScore || this.questionInsert.question.maxScore <= 0 || !this.questionInsert.correctPoints[0].phrase || this.questionInsert.correctPoints[0].phrase.trim().length <= 0 || !this.questionInsert.correctPoints[0].marksWorth) {
      return;
    }
    this.testServ.addQuestion(this.questionInsert as TutorQuestionPojo)
      .subscribe(success => {
        form.reset();
        this.editTest.getQuestions(this.editTest.testID);
      }, error => {
        return;
      });
  }

}
