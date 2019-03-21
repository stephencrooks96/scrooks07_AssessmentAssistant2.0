import {Component, OnInit} from '@angular/core';
import {QuestionType, TutorQuestionPojo} from "../modelObjs/objects.model";
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {KatexOptions} from "ng-katex";

@Component({
  selector: 'app-old-question',
  templateUrl: './old-question.component.html',
  styleUrls: ['./old-question.component.css']
})
export class OldQuestionComponent implements OnInit {

  oldQuestions: TutorQuestionPojo[];
  questionTypesToShow: QuestionType[];
  testID: number;
  options: KatexOptions = {
    displayMode: true,
  };

  constructor(private testServ: TestService, private editTest: EditTestComponent) {
    this.testID = this.editTest.testID;
  }

  /**
   * Called on initialisation of the component
   */
  ngOnInit() {
    this.getQuestionTypes();
    this.getOldQuestions(this.testID);
  }

  /**
   * Gets all questions made by current user that aren't being used in current test
   * @param testID
   */
  getOldQuestions(testID) {
    return this.testServ.getOldQuestions(testID).subscribe(questions => this.oldQuestions = questions);
  }

  /**
   * Gets question types from database
   */
  getQuestionTypes() {
    return this.testServ.getQuestionTypes().subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  /**
   * Adds one of the old questions to the test
   * @param questionID
   */
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
