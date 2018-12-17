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
  showAdd = true;
  showExist = false;
  showDetail = false;
  questions : TutorQuestionPojo[];
  questionDetail = new TutorQuestionPojo();

  constructor(private router: Router, private route: ActivatedRoute, private testServ: TestService) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.getByTestID(this.testID);
    this.getQuestions(this.testID);
  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => this.test = test);
  }

  getQuestions(testID) {
    return this.testServ.getQuestions(testID).subscribe(questions => this.questions = questions);
  }

}
