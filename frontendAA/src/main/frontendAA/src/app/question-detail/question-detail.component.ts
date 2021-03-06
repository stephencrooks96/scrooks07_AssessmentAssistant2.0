import {Component, Input, OnInit} from '@angular/core';
import {
  Alternative,
  CorrectPoint,
  Option,
  QuestionMathLine,
  QuestionType,
  TutorQuestionPojo
} from "../modelObjs/objects.model";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {DomSanitizer} from "@angular/platform-browser";
import {NgForm} from "@angular/forms";
import {KatexOptions} from "ng-katex";

@Component({
  selector: 'app-question-detail',
  templateUrl: './question-detail.component.html',
  styleUrls: ['./question-detail.component.css']
})
export class QuestionDetailComponent implements OnInit {

  @Input() questionDetail = new TutorQuestionPojo();
  questionTypesToShow: QuestionType[];
  editQuestion = false;
  testID: number;
  base64;
  addedImage: any = null;
  imageToSend: any = null;
  fileError = false;
  fileSize: number = 0;
  generalError = false;
  typeError = false;
  contentError = false;
  maxScoreError = false;
  phraseError = -1;
  feedbackError = -1;
  marksWorthError = -1;
  alternativePhraseError = false;
  optionError = false;
  worthMarksError = false;
  insertError = -1;
  optFeedbackError = false;
  fileSuccess: boolean;
  minScoreError: boolean;
  options: KatexOptions = {
    displayMode: true,
  };

  constructor(private testServ: TestService, private modalService: NgbModal, private editTest: EditTestComponent, private sanitizer: DomSanitizer) {
    this.getQuestionTypes();
    this.testID = this.editTest.testID;
  }

  /**
   * Called on initialisation of component
   */
  ngOnInit() {
  }

  /**
   * Called when an image is added to the question
   * @param event
   */
  async imageAdded(event) {
    await this.read(event);
  }

  /**
   * Reads the image file added to the question
   * @param event
   */
  read(event: any): void {
    this.fileSuccess = false;
    let image: File = event.target.files[0];
    this.fileSize = event.target.files[0].size;
    if (this.fileSize > 1048576) {
      this.fileError = true;
      return;
    } else {
      this.fileError = false;
    }
    let fileReader: FileReader = new FileReader();

    fileReader.onloadend = (_) => {
      this.imageToSend = fileReader.result;
    };

    fileReader.readAsDataURL(image);
    this.fileSuccess = true;
  }

  /**
   * Adds an new option to the question
   */
  addOption() {
    const option = new Option();
    option.optionContent = '';
    option.worthMarks = 0;
    this.questionDetail.options.push(option);
    return false;
  }

  /**
   * Removes an option from the question
   * @param i
   */
  removeOption(i) {
    this.testServ.removeOption(this.questionDetail.options[i] as Option)
      .subscribe(success => {
      });
    this.questionDetail.options.splice(i, 1);
    return false;
  }

  /**
   * Adds a new math line to the question
   */
  addMathLine() {
    const mathLine = new QuestionMathLine();
    mathLine.questionMathLineID = 0;
    mathLine.content = '';
    mathLine.indexedAt = this.questionDetail.mathLines.length;
    mathLine.questionID = 0;
    this.questionDetail.mathLines.push(mathLine);
    return false;
  }

  /**
   * Removes a math line from the question
   * @param i
   */
  removeMathLine(i) {
    this.testServ.removeQuestionMathLine(this.questionDetail.mathLines[i].questionMathLineID)
      .subscribe(success => {
      });
    this.questionDetail.mathLines.splice(i, 1);
    return false;
  }

  /**
   * Adds a new alternative to the question
   * @param i
   */
  addMathAlternative(i) {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    alternative.math = 1;
    this.questionDetail.correctPoints[i].alternatives.push(alternative);
    return false;
  }

  /**
   * Adds a new text correct point to the question
   */
  addCorrectPoint() {
    const correctPoint = new CorrectPoint();
    correctPoint.alternatives = [];
    correctPoint.feedback = '';
    correctPoint.marksWorth = 0;
    correctPoint.phrase = '';
    correctPoint.questionID = 0;
    correctPoint.math = 0;
    this.questionDetail.correctPoints.push(correctPoint);
    return false;
  }

  /**
   * Removes a correct point from the question
   * @param i
   */
  removeCorrectPoint(i) {
    if (this.questionDetail.correctPoints[i].correctPointID > 0) {
      this.testServ.removeCorrectPoint(this.questionDetail.correctPoints[i].correctPointID)
        .subscribe(success => {
        });
    }
    this.questionDetail.correctPoints.splice(i, 1);
    return false;
  }

  /**
   * Adds a text alternative to a correct point
   * @param i
   */
  addAlternative(i) {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    alternative.math = 0;
    this.questionDetail.correctPoints[i].alternatives.push(alternative);
    return false;
  }

  /**
   * Removes an alternative from a correct point
   * @param i
   * @param j
   */
  removeAlternative(i, j) {
    if (this.questionDetail.correctPoints[i].alternatives[j].alternativeID > 0) {
      this.testServ.removeAlternative(this.questionDetail.correctPoints[i].alternatives[j].alternativeID)
        .subscribe(success => {
        });
    }
    this.questionDetail.correctPoints[i].alternatives.splice(j, 1);
    return false;
  }

  /**
   * Adds new question information to database
   * Performs validation and outputs appropriate error messages
   * @param form
   */
  async addQuestion(form: NgForm) {

    this.fileError = false;
    this.fileSize = 0;
    this.generalError = false;
    this.typeError = false;
    this.contentError = false;
    this.maxScoreError = false;
    this.phraseError = -1;
    this.feedbackError = -1;
    this.marksWorthError = -1;
    this.alternativePhraseError = false;
    this.optionError = false;
    this.insertError = -1;
    this.worthMarksError = false;
    this.optFeedbackError = false;

    if (this.addedImage != null && this.imageToSend == null) {
      await this.read(this.addedImage);
    }

    if (this.fileSize > 1048576) {
      this.fileError = true;
      this.generalError = true;
    } else {
      this.questionDetail.base64 = this.imageToSend;
    }

    this.questionDetail.testID = this.editTest.testID;
    if (!this.questionDetail.question.questionType) {
      this.typeError = true;
      this.generalError = true;
    }

    if (!this.questionDetail.question.questionContent || this.questionDetail.question.questionContent.trim().length <= 0 || this.questionDetail.question.questionContent.length > 65535) {
      this.contentError = true;
      this.generalError = true;
    }

    if (!this.questionDetail.question.maxScore || this.questionDetail.question.maxScore <= 0) {
      this.maxScoreError = true;
      this.generalError = true;
    }

    if (!this.questionDetail.question.minScore) {
      this.questionDetail.question.minScore = 0;
    }

    // Question Type value 2 is Multiple Choice
    if (this.questionDetail.question.questionType == 2) {
      this.questionDetail.correctPoints.splice(0);
      for (let i = 0; i < this.questionDetail.options.length; i++) {

        if (!this.questionDetail.options[i].optionContent || this.questionDetail.options[i].optionContent.trim().length <= 0 || this.questionDetail.options[i].optionContent.length > 65535) {
          this.optionError = true;
          this.generalError = true;
        }

        if (!this.questionDetail.options[i].feedback || this.questionDetail.options[i].feedback.trim().length <= 0 || this.questionDetail.options[i].feedback.length > 65535) {
          this.optFeedbackError = true;
          this.generalError = true;
        }

        if (this.questionDetail.options[i].worthMarks > this.questionDetail.question.maxScore || this.questionDetail.options[i].worthMarks < (-1 * this.questionDetail.question.maxScore)) {
          this.worthMarksError = true;
          this.generalError = true;
        }
      }
    } else {
      this.questionDetail.options = null;
    }

    // Question Type value 3 is Insert the Word
    if (this.questionDetail.question.questionType == 3) {
      for (let i = 0; i < this.questionDetail.correctPoints.length; i++) {
        if (!this.questionDetail.question.questionContent.includes("[[" + this.questionDetail.correctPoints[i].phrase + "]]")) {
          this.insertError = i;
          this.generalError = true;
        }
      }
    }

    for (let i = 0; i < this.questionDetail.correctPoints.length; i++) {
      if (!this.questionDetail.correctPoints[i].phrase || this.questionDetail.correctPoints[i].phrase.trim().length <= 0 || this.questionDetail.correctPoints[i].phrase.length > 65535) {
        this.phraseError = i;
        this.generalError = true;
      }

      if (!this.questionDetail.correctPoints[i].feedback || this.questionDetail.correctPoints[i].feedback.trim().length <= 0 || this.questionDetail.correctPoints[i].feedback.length > 65535) {
        this.feedbackError = i;
        this.generalError = true;
      }

      if (!this.questionDetail.correctPoints[i].marksWorth || this.questionDetail.correctPoints[i].marksWorth > this.questionDetail.question.maxScore || this.questionDetail.correctPoints[i].marksWorth < (-1 * this.questionDetail.question.maxScore)) {
        this.marksWorthError = i;
        this.generalError = true;
      }

      for (let j = 0; j < this.questionDetail.correctPoints[i].alternatives.length; j++) {
        if (!this.questionDetail.correctPoints[i].alternatives[j].alternativePhrase || this.questionDetail.correctPoints[i].alternatives[j].alternativePhrase.trim().length <= 0 || this.questionDetail.correctPoints[i].alternatives[j].alternativePhrase.length > 65535) {
          this.alternativePhraseError = true;
          this.generalError = true;
        }
      }
    }
    if (this.generalError) {
      return;
    }
    this.testServ.editQuestion(this.questionDetail as TutorQuestionPojo)
      .subscribe(success => {
        form.reset();
        this.editQuestion = false;
        this.addedImage = null;
        this.imageToSend = null;
        this.fileError = false;
        this.fileSize = 0;
        this.generalError = false;
        this.typeError = false;
        this.contentError = false;
        this.maxScoreError = false;
        this.phraseError = -1;
        this.feedbackError = -1;
        this.marksWorthError = -1;
        this.alternativePhraseError = false;
        this.optionError = false;
        this.insertError = -1;
        this.worthMarksError = false;
        this.optFeedbackError = false;
        this.editTest.getQuestions(this.editTest.testID);
        this.questionDetail = success;
        return;
      }, error => {
        return;
      });
  }

  /**
   * Duplicates question if need for editing in event the question has been used before
   * to ensure auto-marking isn't affected for old submissions
   * @param questionID
   */
  duplicateQuestion(questionID: number) {
    this.testServ.duplicateQuestion(questionID)
      .subscribe(success => {
        this.editTest.showDetail = false;
        this.editTest.showExist = true;
      }, error => {
        return;
      });
  }

  /**
   * Converts image from base64 to be output as actual image
   */
  readyImage(): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + this.questionDetail.base64);
  }

  /**
   * Retrieves all question types from database
   */
  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  /**
   * Removes the question from the test
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
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
  }
}
