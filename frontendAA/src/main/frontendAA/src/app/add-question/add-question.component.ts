import {Component, OnInit} from '@angular/core';
import {TestService} from "../services/test.service";
import {EditTestComponent} from "../edit-test/edit-test.component";
import {Alternative, CorrectPoint, QuestionType, TutorQuestionPojo, Option} from "../modelObjs/objects.model";
import {NgForm} from "@angular/forms";

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

  constructor(private editTest: EditTestComponent, private testServ: TestService) {
  }

  ngOnInit() {
    this.getQuestionTypes();
    this.questionInsert.correctPoints = [];
    this.addCorrectPoint();
    this.questionInsert.options = [];
    this.addOption();
  }

  onTypeClick(questionType: any) {
    this.questionTypeChecker = questionType;
  }

  async imageAdded(event) {
    await this.read(event);
  }

  read(event: any): void {
    let image: File = event.target.files[0];
    this.fileSize = event.target.files[0].size;
    if (this.fileSize > 1048576) {
      this.fileError = true;
    } else {
      this.fileError = false;
    }
    let fileReader: FileReader = new FileReader();

    fileReader.onloadend = (_) => {
      this.imageToSend = fileReader.result;
    };

    fileReader.readAsDataURL(image);
  }

  getQuestionTypes() {
    return this.testServ.getQuestionTypes()
      .subscribe(questionTypes => this.questionTypesToShow = questionTypes);
  }

  addOption() {
    const option = new Option();
    option.optionContent = '';
    option.worthMarks = 0;
    this.questionInsert.options.push(option);

    return false;
  }

  removeOption(i) {
    this.questionInsert.options.splice(i, 1);
    return false;
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

  removeCorrectPoint(i) {
    this.questionInsert.correctPoints.splice(i, 1);
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

  removeAlternative(i, j) {
    this.questionInsert.correctPoints[i].alternatives.splice(j, 1);
    return false;
  }

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

      if (!this.questionInsert.correctPoints[i].marksWorth || this.questionInsert.correctPoints[i].marksWorth > this.questionInsert.question.maxScore || this.questionInsert.correctPoints[i].marksWorth < (-1 * this.questionInsert.question.maxScore)) {
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
