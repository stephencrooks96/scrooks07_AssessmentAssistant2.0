import {Component, Input, OnInit} from '@angular/core';
import {QuestionType, TutorQuestionPojo} from "../modelObjs/objects.model";
import {TestService} from "../services/test.service";

@Component({
  selector: 'app-question-detail',
  templateUrl: './question-detail.component.html',
  styleUrls: ['./question-detail.component.css']
})
export class QuestionDetailComponent implements OnInit {

  @Input() questionDetail : TutorQuestionPojo;
  questionTypesToShow : QuestionType[];
  edit = false;

  constructor(private testServ: TestService) {
    this.getQuestionTypes();
  }

  ngOnInit() {
  }

  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

}
