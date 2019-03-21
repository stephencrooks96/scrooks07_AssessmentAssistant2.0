import {AfterViewInit, ChangeDetectorRef, Component, DoCheck, OnInit} from '@angular/core';
import {Chart} from 'chart.js';
import {MarkingService} from "../services/marking.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DomSanitizer} from "@angular/platform-browser";
import {
  Alternative,
  Answer,
  AnswerData,
  CorrectPoint,
  Question,
  ResultChartPojo,
  Tests,
  User
} from "../modelObjs/objects.model";
import {NgForm} from "@angular/forms";
import {ModulesService} from "../services/modules.service";
import {KatexOptions} from "ng-katex";

const errorColour: string = "#dc3545";
const normalColour: string = "#202529";

@Component({
  selector: 'app-review-marking',
  templateUrl: './review-marking.component.html',
  styleUrls: ['./review-marking.component.css']
})
export class ReviewMarkingComponent implements OnInit, DoCheck, AfterViewInit {

  chart;
  chartToShow = true;
  questionChart;
  testID;
  resultChartData = new ResultChartPojo();
  resultQuestionChartData: ResultChartPojo[] = [];
  resultChartCheck = false;
  chartCheck = false;
  chartCount = 0;
  resultChartCount = 0;
  test = new Tests();
  review = false;
  TUTOR = 1;
  chosenOptions;
  showStudent = true;
  showQuestions = false;
  scripts: AnswerData[];
  studentSet: User[];
  questionSet: Question[];
  moduleAssoc: number;
  studentDetail = new User();
  questionDetail = new Question();
  answerDetail = new AnswerData();
  editScoreShow = false;
  scoreError;
  checkScore;
  filter: number = 1;
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
  options: KatexOptions = {
    displayMode: true,
  };

  constructor(private modServ: ModulesService, private cdr: ChangeDetectorRef, private markServ: MarkingService, private router: Router, private route: ActivatedRoute, private testServ: TestService, private modalService: NgbModal, private sanitizer: DomSanitizer) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  /**
   * Called on initialisation of component
   */
  ngOnInit() {
    this.approvalFeedback = [];
    this.approvalFeedback.push("");
    this.approvalFeedback.push("#B2E1B8");
    this.getResultChart(this.testID);
    this.getQuestionResultChart(this.testID);
    this.getByTestID(this.testID);
    this.getScriptsByTestID(this.testID);
  }

  /**
   * Gets the test information
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
   * Gets the question result chart
   * How the class performed on average for each question
   * @param testID
   */
  getQuestionResultChart(testID) {
    return this.markServ.getQuestionResultChart(testID).subscribe(resultChartData => this.resultQuestionChartData = resultChartData);
  }

  /**
   * Gets the result chart of how each user performed in the test
   * @param testID
   */
  getResultChart(testID) {
    return this.markServ.getResultChart(testID).subscribe(resultChartData => this.resultChartData = resultChartData);
  }

  /**
   * Called at intervals during running of component
   */
  ngDoCheck() {
    if (this.resultChartData) {
      if (this.resultChartData.labels && this.resultChartData.colors && this.resultChartData.scores) {
        if (this.resultChartData.labels.length > 0 && this.resultChartData.colors.length > 0 && this.resultChartData.scores.length > 0) {
          this.chartCheck = true;
          if (this.chartCount == 0) {
            this.chart = this.mainChartInit();
            this.chartCount++;
          }
        }
      }
      if (this.resultQuestionChartData && this.resultQuestionChartData[0] && this.resultQuestionChartData[1]) {
        if (this.resultQuestionChartData[0].labels && this.resultQuestionChartData[0].scores && this.resultQuestionChartData[1].labels && this.resultQuestionChartData[1].scores) {
          if (this.resultQuestionChartData[1].labels.length > 0 && this.resultQuestionChartData[1].scores.length > 0 && this.resultQuestionChartData[0].labels.length > 0 && this.resultQuestionChartData[0].scores.length > 0) {
            this.resultChartCheck = true;

            if (this.resultChartCount == 0) {
              this.questionChart = this.byQuestionChartInit();
              this.resultChartCount++;
            }
          }
        }
      }
    }
  }

  /**
   * Called after component initialises
   */
  ngAfterViewInit() {
    this.chart = this.mainChartInit();
    this.questionChart = this.byQuestionChartInit();
    this.cdr.detectChanges();
  }

  /**
   * Generates the question chart as mentioned above
   */
  byQuestionChartInit() {
    return new Chart('byQuestion', {
      type: 'bar',
      data: {
        datasets: [{
          label: 'Average Score',
          data: this.resultQuestionChartData[1].scores,
          backgroundColor: '#007bff',
          borderColor: '#007bff'
        }, {
          label: 'Total Score',
          data: this.resultQuestionChartData[0].scores,
          backgroundColor: '#dc3545',
          borderColor: '#dc3545',
          pointRadius: 10,
          // Changes this dataset to become a line
          type: 'line',
          fill: false,
          showLine: false
        }],
        labels: this.resultQuestionChartData[0].labels
      },
      options: {
        responsive: true,
        scales: {
          yAxes: [{
            ticks: {
              min: 0,
              max: this.resultQuestionChartData[0].classAverage + 1,
              stepSize: 1
            }
          }],
          xAxes: [{
            display: false,
          }]
        }
      }
    });
  }

  /**
   * Generates the result chart as mentioned above
   */
  mainChartInit() {
    return new Chart('main', {
      type: 'line',
      data: {
        labels: this.resultChartData.labels,
        datasets: [{
          label: "Class Average: " + this.resultChartData.classAverage,
          fill: false,
          data: this.resultChartData.scores,
          backgroundColor: "#28a745",
          borderColor: "#28a745",
          pointBackgroundColor: this.resultChartData.colors,
          pointBorderColor: "#343a40",
          pointHoverRadius: 20,
          pointRadius: 7,
          showLine: false
        }]
      },
      options: {
        responsive: true,

        scales: {
          yAxes: [{
            ticks: {
              min: 0,
              max: 110,
              stepSize: 10
            }
          }]
        }
      }
    });
  }

  /**
   * Gets the question image ready for view by
   * converting from bse64 to actual visible image
   * @param base64
   */
  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
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
   * Gets the correct points for a question
   * @param questionID
   */
  getCorrectPoints(questionID) {
    return this.markServ.getCorrectPoints(questionID, this.testID)
      .subscribe(correctPoints => this.answerDetail.questionAndAnswer.correctPoints = correctPoints);
  }

  /**
   * Approve the marking of an answer
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
   * Decides that the test should now publish grades
   */
  publishGrades() {
    this.markServ.publishGrades(this.testID)
      .subscribe(success => {
        this.getByTestID(this.testID);
      });
  }

  /**
   * Decides that the test should now publish results
   */
  publishResults() {
    this.markServ.publishResults(this.testID)
      .subscribe(success => {
        this.getByTestID(this.testID);
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
   * Gets the scripts owned by the marker for this given test
   * For the marker to mark
   * In this case it is the tutor always
   * @param testID
   */
  getScriptsByTestID(testID) {
    return this.markServ.getScriptsByTestIDTutor(testID)
      .subscribe(scripts => {
          this.scripts = scripts;
          this.studentSet = [];
          this.questionSet = [];
          this.chosenOptions = [];
          for (let x = 0; x < this.scripts.length; x++) {
            let addStu = true;
            for (let y = 0; y < this.studentSet.length; y++) {
              if (this.studentSet[y].userID == this.scripts[x].student.userID) {
                addStu = false;
              }
            }
            if (addStu) {
              this.studentSet.push(this.scripts[x].student);
            }
            let addQ = true;
            for (let y = 0; y < this.questionSet.length; y++) {
              if (this.questionSet[y].questionID == this.scripts[x].questionAndAnswer.question.question.questionID) {
                addQ = false;
              }
            }
            if (addQ) {
              this.questionSet.push(this.scripts[x].questionAndAnswer.question.question);
            }
          }
          this.studentDetail = this.studentSet[0];
          this.questionDetail = this.questionSet[0];
          for (let x = 0; x < this.scripts.length; x++) {
            if (this.scripts[x].student.userID == this.studentDetail.userID && this.answerDetail.student.userID == null) {
              this.answerDetail = this.scripts[x];
              break;
            } else if (this.scripts[x].questionAndAnswer.answer.answerID == this.answerDetail.questionAndAnswer.answer.answerID) {
              this.answerDetail = this.scripts[x];
            }
          }
        }
      );
  }

  /**
   * Chooses a new answer to view in detail based on user
   * @param studentID
   */
  newAnswerDetailUser(studentID) {
    for (let x = 0; x < this.scripts.length; x++) {
      if (this.scripts[x].student.userID == studentID) {
        this.answerDetail = this.scripts[x];
        break;
      }
    }
  }

  /**
   * Chooses a new answer to view in detail based on a new question clicked
   * @param questionID
   */
  newAnswerDetailQuestion(questionID) {
    for (let x = 0; x < this.scripts.length; x++) {
      if (this.scripts[x].questionAndAnswer.question.question.questionID == questionID) {
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
   * Manually edits a score and sends it to database
   * @param form
   */
  editScore(form: NgForm) {
    if (this.answerDetail.questionAndAnswer.answer.score > this.answerDetail.questionAndAnswer.question.question.maxScore || this.answerDetail.questionAndAnswer.answer.score < -1 * this.answerDetail.questionAndAnswer.question.question.maxScore) {
      this.checkScore = errorColour;
      this.scoreError = true;
      return;
    }
    this.markServ.editAnswer(this.answerDetail.questionAndAnswer.answer as Answer)
      .subscribe(answer => {
        form.reset();
        this.editScoreShow = false;
        this.answerDetail.questionAndAnswer.answer = answer;
      }, error => {
        return;
      });
  }

  /**
   * Initialises a new correct point to be added to a question
   */
  initCorrectPoint() {
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
    this.correctPointToInsert = correctPoint;
    return false;
  }

  /**
   * Adds a new text based alternative to a correct point
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
   * Adds a new math based alternative to a correct point
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
   * Adds a new correct point to a question and saves it in database
   * Validation is performed and appropriate error messages output
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
   * Adds a new alternative to the correct point and sends it to the database
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
   * Manually edits feedback on an answer and send it to database
   * Triggers auto-marking on backend
   * @param form
   */
  editFeedback(form: NgForm) {
    if (this.answerDetail.questionAndAnswer.answer.feedback.length > 56535 || this.answerDetail.questionAndAnswer.answer.feedback.length < 0) {
      this.checkFeedback = errorColour;
      this.feedbackError = true;
      return;
    }
    this.markServ.editAnswer(this.answerDetail.questionAndAnswer.answer as Answer)
      .subscribe(answer => {
        form.reset();
        this.editFeedbackShow = false;
        this.answerDetail.questionAndAnswer.answer = answer;
      }, error => {
        return;
      });
  }
}
