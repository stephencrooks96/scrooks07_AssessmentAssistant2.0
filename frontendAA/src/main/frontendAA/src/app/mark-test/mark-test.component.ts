import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DomSanitizer} from "@angular/platform-browser";
import {Alternative, Answer, AnswerData, CorrectPoint, Tests, User} from "../modelObjs/objects.model";
import {MarkingService} from "../services/marking.service";
import {ModulesService} from "../services/modules.service";
import {NgForm} from "@angular/forms";
import {KatexOptions} from "ng-katex";

const errorColour: string = "#dc3545";
const normalColour: string = "#202529";

@Component({
  selector: 'app-mark-test',
  templateUrl: './mark-test.component.html',
  styleUrls: ['./mark-test.component.css']
})
export class MarkTestComponent implements OnInit, AfterViewInit {

  TUTOR = 1;
  testID: number;
  test = new Tests();
  chosenOptions;
  showStudent = true;
  showQuestions = false;
  scripts: AnswerData[];
  studentSet: User[];
  moduleAssoc: number;
  studentDetail = new User();
  answerDetail = new AnswerData();
  editScoreShow = false;
  scoreError;
  checkScore;
  altError;
  checkAlt;
  editFeedbackShow = false;
  feedbackError;
  checkFeedback;
  approvalFeedback: string[];
  altToRemove: number = -1;
  correctPointToRemove: number = -1;
  addAlternativeEdit = false;
  addCorrectPointEdit = false;
  alternativeToInsert = new Alternative();
  correctPointToInsert = new CorrectPoint();
  phraseError = false;
  feedbackInsertError = false;
  marksWorthError = false;
  alternativePhraseError = false;
  generalError = false;
  studentCounter = 0;
  answerCounter = -1;
  options: KatexOptions = {
    displayMode: true,
  };

  constructor(private router: Router, private route: ActivatedRoute, private testServ: TestService, private markServ: MarkingService, private modServ: ModulesService, private modalService: NgbModal, private sanitizer: DomSanitizer) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
    this.approvalFeedback = [];
    this.approvalFeedback.push("");
    this.approvalFeedback.push("#B2E1B8");
    this.getByTestID(this.testID);
    this.getScriptsByTestID(this.testID);
  }

  /**
   * Called after initialization of component
   */
  ngAfterViewInit() {
  }

  /**
   * Converts image from base64 to actual image output
   * @param base64
   */
  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
  }

  /**
   * Gets the test data
   * @param testID
   */
  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => {
          this.test = test;
          this.getModuleAssociation(test.moduleID);
        }
      );
  }

  /**
   * Gets the users association to the module
   * @param moduleID
   */
  getModuleAssociation(moduleID) {
    return this.modServ.getModuleAssociation(moduleID)
      .subscribe(moduleAssoc => this.moduleAssoc = moduleAssoc);
  }

  /**
   * Gets the correct points for a given question
   * @param questionID
   */
  getCorrectPoints(questionID) {
    return this.markServ.getCorrectPoints(questionID, this.testID)
      .subscribe(correctPoints => this.answerDetail.questionAndAnswer.correctPoints = correctPoints);
  }

  /**
   * Approves an answer to a question
   * @param answerID
   */
  approve(answerID) {
    return this.markServ.approve(answerID)
      .subscribe(ans => {
        this.getScriptsByTestID(this.testID);
      });
  }

  /**
   * Removes an alternative from a correct point
   * @param altID
   */
  removeAlt(altID) {
    this.markServ.removeAlternative(altID, this.testID)
      .subscribe(success => {
        this.getScriptsByTestID(this.testID);
        this.getCorrectPoints(this.answerDetail.questionAndAnswer.question.question.questionID);
      });
  }

  /**
   * Removes a correct point from a question
   * @param correctPointID
   */
  removeCorrectPoint(correctPointID) {
    this.markServ.removeCorrectPoint(correctPointID, this.testID)
      .subscribe(success => {
        this.getScriptsByTestID(this.testID);
        this.getCorrectPoints(this.answerDetail.questionAndAnswer.question.question.questionID);
      });
  }

  /**
   * Sends user back home if unauthorized to be here
   */
  backHome() {
    this.router.navigate(['/moduleHome', this.test.moduleID]);
  }

  /**
   * Gets the scripts that are to be marked by the user from the database
   * @param testID
   */
  getScriptsByTestID(testID) {
    return this.markServ.getScriptsByTestIDMarker(testID)
      .subscribe(scripts => {
          if (!scripts || scripts.length < 1) {
            this.backHome();
          }
          this.scripts = scripts;
          this.studentSet = [];
          this.chosenOptions = [];
          for (let x = 0; x < this.scripts.length; x++) {
            let add = true;
            for (let y = 0; y < this.studentSet.length; y++) {
              if (this.studentSet[y].userID == this.scripts[x].student.userID) {
                add = false;
              }
            }
            if (add) {
              this.studentSet.push(this.scripts[x].student);
            }
          }
          this.studentDetail = this.studentSet[this.studentCounter];
          for (let x = 0; x < this.scripts.length; x++) {
            if (this.scripts[x].student.userID == this.studentDetail.userID) {
              if (this.answerCounter == -1) {
                this.answerDetail = this.scripts[x];
                break;
              } else {
                this.answerDetail = this.scripts[this.answerCounter];
                break;
              }
            }
          }
        }, error => {
          this.backHome();
        }
      );
  }

  /**
   * Chooses a new answer to show in detail
   * @param studentID
   */
  newAnswerDetail(studentID) {
    for (let x = 0; x < this.scripts.length; x++) {
      if (this.scripts[x].student.userID == studentID) {
        this.answerDetail = this.scripts[x];
        break;
      }
    }
  }

  /**
   * From ngBootstrap framework
   * @param modal
   */
  open(modal) {
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Allows user to manually enter the score for a question
   * @param form
   */
  editScore(form: NgForm) {
    if (this.answerDetail.questionAndAnswer.answer.score > this.answerDetail.questionAndAnswer.question.question.maxScore || this.answerDetail.questionAndAnswer.answer.score < -1 * this.answerDetail.questionAndAnswer.question.question.maxScore) {
      this.checkScore = errorColour;
      this.scoreError = true;
      return;
    }
    this.markServ.editScore(this.answerDetail.questionAndAnswer.answer as Answer)
      .subscribe(answer => {
        form.reset();
        this.editScoreShow = false;
        this.answerDetail.questionAndAnswer.answer = answer;
      }, error => {
        return;
      });
  }

  /**
   * Initializes a new correct point to be added to a question
   */
  initCorrectPoint() {
    const correctPoint = new CorrectPoint();
    correctPoint.alternatives = [];
    correctPoint.feedback = '';
    correctPoint.marksWorth = 0;
    correctPoint.phrase = '';
    correctPoint.questionID = 0;
    this.correctPointToInsert = correctPoint;

    return false;
  }

  /**
   * Initialises a new text based alternative to be added to a correct point
   */
  initTextAlternative() {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    alternative.math = 0;
    this.correctPointToInsert.alternatives.push(alternative);
    return false;
  }

  /**
   * Initialises a new math based alternative to be added to a correct point
   */
  initMathAlternative() {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    alternative.math = 1;
    this.correctPointToInsert.alternatives.push(alternative);
    return false;
  }

  /**
   * Removes an alternative from a correct point
   * @param j
   */
  removeAlternative(j) {
    this.correctPointToInsert.alternatives.splice(j, 1);
    return false;
  }

  /**
   * Adds a new correct point to a question
   * @param form
   */
  addCorrectPoint(form: NgForm) {
    this.generalError = false;
    this.correctPointToInsert.questionID = this.answerDetail.questionAndAnswer.question.question.questionID;
    if (!this.correctPointToInsert.phrase || this.correctPointToInsert.phrase.trim().length <= 0 || this.correctPointToInsert.phrase.length > 65535) {
      this.phraseError = true;
      this.generalError = true;
    }

    if (!this.correctPointToInsert.feedback || this.correctPointToInsert.feedback.trim().length <= 0 || this.correctPointToInsert.feedback.length > 65535) {
      this.feedbackInsertError = true;
      this.generalError = true;
    }

    if (!this.correctPointToInsert.marksWorth || this.correctPointToInsert.marksWorth > this.answerDetail.questionAndAnswer.question.question.maxScore || this.correctPointToInsert.marksWorth < (-1 * this.answerDetail.questionAndAnswer.question.question.maxScore)) {
      this.marksWorthError = true;
      this.generalError = true;
    }

    for (let j = 0; j < this.correctPointToInsert.alternatives.length; j++) {
      if (!this.correctPointToInsert.alternatives[j].alternativePhrase || this.correctPointToInsert.alternatives[j].alternativePhrase.trim().length <= 0 || this.correctPointToInsert.alternatives[j].alternativePhrase.length > 65535) {
        this.alternativePhraseError = true;
        this.generalError = true;
      }
      if (this.correctPointToInsert.alternatives[j].math != 1) {
        this.correctPointToInsert.alternatives[j].math = 0;
      }
    }

    if (this.correctPointToInsert.math != 1) {
      this.correctPointToInsert.math = 0;
    }

    if (this.generalError) {
      return;
    }


    this.markServ.addCorrectPoint(this.correctPointToInsert as CorrectPoint, this.testID)
      .subscribe(answer => {
        form.reset();
        this.addCorrectPointEdit = false;
        this.generalError = false;
        this.alternativePhraseError = false;
        this.marksWorthError = false;
        this.feedbackInsertError = false;
        this.phraseError = false;
        this.getCorrectPoints(this.answerDetail.questionAndAnswer.question.question.questionID);
        this.getScriptsByTestID(this.testID);
      }, error => {
        return;
      });
  }

  /**
   * Adds a new alternative to a correct point
   * @param form
   * @param correctPointID
   */
  addAlternative(form: NgForm, correctPointID) {
    this.alternativeToInsert.correctPointID = correctPointID;
    if (this.alternativeToInsert.alternativePhrase.length > 56535 || this.alternativeToInsert.alternativePhrase.length < 0) {
      this.checkAlt = errorColour;
      this.altError = true;
      return;
    }
    this.markServ.addAlternative(this.alternativeToInsert as Alternative, this.testID)
      .subscribe(answer => {
        form.reset();
        this.addAlternativeEdit = false;
        this.checkAlt = normalColour;
        this.altError = false;
        this.getCorrectPoints(this.answerDetail.questionAndAnswer.question.question.questionID);
        this.getScriptsByTestID(this.testID);
      }, error => {
        return;
      });
  }

  /**
   * Manually enter feedback for a given answer
   * @param form
   */
  editFeedback(form: NgForm) {
    if (this.answerDetail.questionAndAnswer.answer.feedback.length > 56535 || this.answerDetail.questionAndAnswer.answer.feedback.length < 0) {
      this.checkFeedback = errorColour;
      this.feedbackError = true;
      return;
    }
    this.markServ.editFeedback(this.answerDetail.questionAndAnswer.answer as Answer)
      .subscribe(answer => {
        form.reset();
        this.editFeedbackShow = false;
        this.answerDetail.questionAndAnswer.answer = answer;
      }, error => {
        return;
      });
  }
}
