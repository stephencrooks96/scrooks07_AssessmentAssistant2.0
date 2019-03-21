import {Component, OnInit} from '@angular/core';
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {
  Alternative,
  CorrectPoint,
  Option,
  QuestionMathLine,
  QuestionType,
  TutorQuestionPojo
} from "../modelObjs/objects.model";
import {NgForm} from "@angular/forms";
import {KatexOptions} from "ng-katex";

@Component({
  selector: 'app-add-question',
  templateUrl: './add-question.component.html',
  styleUrls: ['./add-question.component.css']
})
export class AddQuestionComponent implements OnInit {

  questionInsert = new TutorQuestionPojo();
  questionTypesToShow: QuestionType[];
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
  questionTypeChecker: number = 1;
  optFeedbackError = false;
  fileSuccess: boolean;
  minScoreError: boolean;
  options: KatexOptions = {
    displayMode: true,
  };

  constructor(private editTest: EditTestComponent, private testServ: TestService) {
  }

  /**
   * Called on initialization of component
   */
  ngOnInit() {
    this.getQuestionTypes();
    this.questionInsert.correctPoints = [];
    this.addCorrectPoint();
    this.questionInsert.options = [];
    this.addOption();
    this.questionInsert.question.minScore = 0;
  }

  /**
   * Called when a question type is selected
   * @param questionType
   */
  onTypeClick(questionType: any) {
    this.questionTypeChecker = questionType;
  }

  /**
   * Called when an image is added
   * @param event
   */
  async imageAdded(event) {
    await this.read(event);
  }

  /**
   * Reads an image file in and converts to base64 upon entry
   * @param event
   */
  read(event: any): void {
    this.fileSuccess = false;
    let image: File = event.target.files[0];
    this.fileSize = event.target.files[0].size;
    this.fileError = this.fileSize > 1048576;
    let fileReader: FileReader = new FileReader();

    fileReader.onloadend = (_) => {
      this.imageToSend = fileReader.result;
    };

    fileReader.readAsDataURL(image);
    this.fileSuccess = true;
  }

  /**
   * Gets the various types of question from database
   */
  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  /**
   * Adds a new initialized option to the question
   */
  addOption() {
    const option = new Option();
    option.optionContent = '';
    option.worthMarks = 0;
    this.questionInsert.options.push(option);
    return false;
  }

  /**
   * Removes an option from the question
   * @param i
   */
  removeOption(i) {
    this.questionInsert.options.splice(i, 1);
    return false;
  }

  /**
   * Adds an empty correct point to the question
   */
  addCorrectPoint() {
    const correctPoint = new CorrectPoint();
    correctPoint.alternatives = [];
    correctPoint.feedback = '';
    correctPoint.marksWorth = 0;
    correctPoint.phrase = '';
    correctPoint.questionID = 0;
    correctPoint.math = 0;
    this.questionInsert.correctPoints.push(correctPoint);

    return false;
  }

  /**
   * Removes the chosen correct point from the question
   * @param i
   */
  removeCorrectPoint(i) {
    this.questionInsert.correctPoints.splice(i, 1);
    return false;
  }

  /**
   * Adds an empty alternative non-math to a correct point
   * @param i
   */
  addAlternative(i) {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    alternative.math = 0;
    this.questionInsert.correctPoints[i].alternatives.push(alternative);
    return false;
  }

  /**
   * Adds an empty math line to the question
   */
  addMathLine() {
    const mathLine = new QuestionMathLine();
    mathLine.questionMathLineID = 0;
    mathLine.content = '';
    mathLine.indexedAt = this.questionInsert.mathLines.length;
    mathLine.questionID = 0;
    this.questionInsert.mathLines.push(mathLine);
    return false;
  }

  /**
   * Removes a chosen math line from the question
   * @param i
   */
  removeMathLine(i) {
    this.questionInsert.mathLines.splice(i, 1);
    return false;
  }

  /**
   * Adds a mathematical alternative to the question
   * @param i
   */
  addMathAlternative(i) {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    alternative.math = 1;
    this.questionInsert.correctPoints[i].alternatives.push(alternative);
    return false;
  }

  /**
   * Removes a chosen alternative from a chosen correct point
   * @param i
   * @param j
   */
  removeAlternative(i, j) {
    this.questionInsert.correctPoints[i].alternatives.splice(j, 1);
    return false;
  }

  /**
   * Adds a new question to the database from the form data
   * Performs validation and outputs error messages where required
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
      this.questionInsert.base64 = this.imageToSend;
    }

    this.questionInsert.testID = this.editTest.testID;
    if (!this.questionInsert.question.questionType) {
      this.typeError = true;
      this.generalError = true;
    }

    if (!this.questionInsert.question.questionContent || this.questionInsert.question.questionContent.trim().length <= 0 || this.questionInsert.question.questionContent.length > 65535) {
      this.contentError = true;
      this.generalError = true;
    }

    if (!this.questionInsert.question.maxScore || this.questionInsert.question.maxScore <= 0) {
      this.maxScoreError = true;
      this.generalError = true;
    }

    // Question Type value 2 is Multiple Choice
    if (this.questionInsert.question.questionType == 2) {
      this.questionInsert.correctPoints.splice(0);
      for (let i = 0; i < this.questionInsert.options.length; i++) {

        if (!this.questionInsert.options[i].optionContent || this.questionInsert.options[i].optionContent.trim().length <= 0 || this.questionInsert.options[i].optionContent.length > 65535) {
          this.optionError = true;
          this.generalError = true;
        }

        if (!this.questionInsert.options[i].feedback || this.questionInsert.options[i].feedback.trim().length <= 0 || this.questionInsert.options[i].feedback.length > 65535) {
          this.optFeedbackError = true;
          this.generalError = true;
        }

        if (this.questionInsert.options[i].worthMarks > this.questionInsert.question.maxScore || this.questionInsert.options[i].worthMarks < (-1 * this.questionInsert.question.maxScore)) {
          this.worthMarksError = true;
          this.generalError = true;
        }
      }

    } else {
      this.questionInsert.options = null;
    }

    // Question Type value 3 is Insert the Word
    if (this.questionInsert.question.questionType == 3) {
      for (let i = 0; i < this.questionInsert.correctPoints.length; i++) {
        if (!this.questionInsert.question.questionContent.includes("[[" + this.questionInsert.correctPoints[i].phrase + "]]")) {
          this.insertError = i;
          this.generalError = true;
        }
      }
    }

    for (let i = 0; i < this.questionInsert.correctPoints.length; i++) {

      if (!this.questionInsert.correctPoints[i].phrase || this.questionInsert.correctPoints[i].phrase.trim().length <= 0 || this.questionInsert.correctPoints[i].phrase.length > 65535) {
        this.phraseError = i;
        this.generalError = true;
      }

      if (!this.questionInsert.correctPoints[i].feedback || this.questionInsert.correctPoints[i].feedback.trim().length <= 0 || this.questionInsert.correctPoints[i].feedback.length > 65535) {
        this.feedbackError = i;
        this.generalError = true;
      }

      if (this.questionInsert.correctPoints[i].marksWorth > this.questionInsert.question.maxScore || this.questionInsert.correctPoints[i].marksWorth < this.questionInsert.question.minScore) {
        this.marksWorthError = i;
        this.generalError = true;
      }

      for (let j = 0; j < this.questionInsert.correctPoints[i].alternatives.length; j++) {
        if (!this.questionInsert.correctPoints[i].alternatives[j].alternativePhrase || this.questionInsert.correctPoints[i].alternatives[j].alternativePhrase.trim().length <= 0 || this.questionInsert.correctPoints[i].alternatives[j].alternativePhrase.length > 65535) {
          this.alternativePhraseError = true;
          this.generalError = true;
        }
      }
    }
    if (this.generalError) {
      return;
    }
    this.testServ.addQuestion(this.questionInsert as TutorQuestionPojo)
      .subscribe(success => {
        form.reset();
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
        this.ngOnInit();
      }, error => {
        return;
      });
  }
}
