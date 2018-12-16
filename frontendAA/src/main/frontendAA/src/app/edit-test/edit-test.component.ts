import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {
  Alternative,
  CorrectPoint,
  ModuleWithTutorFE,
  Question,
  QuestionType,
  Tests,
  TutorQuestionPojo
} from "../modelObjs/objects.model";
import {ModulesService} from "../services/modules.service";

@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.css']
})
export class EditTestComponent implements OnInit {

  testID: number;
  test = new Tests();
  moduleTutor = new ModuleWithTutorFE();
  questionTypesToShow: QuestionType[];
  questionInsert = new TutorQuestionPojo();
  showAdd = true;
  showExist = false;
  showDetail = false;
  questions : TutorQuestionPojo[];

  constructor(private router: Router, private route: ActivatedRoute, private testServ: TestService) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.getByTestID(this.testID);
    this.getQuestionTypes();
    this.questionInsert.correctPoints = [];
    this.addCorrectPoint();
    this.getQuestions(this.testID);
  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => this.test = test);
  }

  getQuestions(testID) {
    return this.testServ.getQuestions(testID).subscribe(questions => this.questions = questions);
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
