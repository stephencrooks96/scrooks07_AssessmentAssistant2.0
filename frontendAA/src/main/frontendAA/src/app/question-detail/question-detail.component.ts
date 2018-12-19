import {Component, Input, OnInit} from '@angular/core';
import {QuestionType, TutorQuestionPojo} from "../modelObjs/objects.model";
import {TestService} from "../services/test.service";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EditTestComponent} from "../edit-test/edit-test.component";

@Component({
  selector: 'app-question-detail',
  templateUrl: './question-detail.component.html',
  styleUrls: ['./question-detail.component.css']
})
export class QuestionDetailComponent implements OnInit {

  @Input() questionDetail : TutorQuestionPojo;
  questionTypesToShow : QuestionType[];
  edit = false;
  closeResult : string;
  testID : number;

  constructor(private testServ: TestService, private modalService: NgbModal, private editTest: EditTestComponent) {
    this.getQuestionTypes();
    this.testID = this.editTest.testID;
  }

  ngOnInit() {
  }

  /**
   *
   */
  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  /**
   *
   * @param questionID
   * @param testID
   */
  removeQuestion(questionID, testID) {
    this.testServ.removeQuestion(questionID, testID).subscribe(
      success => this.editTest.getQuestions(this.editTest.testID)
    );
  }

  /**
   * From ngBootstrap framework
   * @param modal
   */
  open(modal) {
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    });
  }

}
