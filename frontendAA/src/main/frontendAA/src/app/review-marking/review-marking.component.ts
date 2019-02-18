import {AfterViewInit, ChangeDetectorRef, Component, DoCheck, OnInit} from '@angular/core';
import {Chart} from 'chart.js';
import {MarkingService} from "../services/marking.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DomSanitizer} from "@angular/platform-browser";
import {Alternative, Answer, AnswerData, CorrectPoint, ResultChartPojo, Tests, User} from "../modelObjs/objects.model";
import {Test} from "tslint";
import {NgForm} from "@angular/forms";
import {ModulesService} from "../services/modules.service";

const errorColour: string = "#dc3545";
const normalColour: string = "#202529";

@Component({
  selector: 'app-review-marking',
  templateUrl: './review-marking.component.html',
  styleUrls: ['./review-marking.component.css']
})
export class ReviewMarkingComponent implements OnInit, DoCheck, AfterViewInit {

  chart;
  testID;
  resultChartData = new ResultChartPojo();
  chartCheck = false;
  chartCount = 0;
  test = new Tests();
  review = false;
  TUTOR = 1;
  chosenOptions;
  showStudent = true;
  showQuestions = false;
  scripts: AnswerData[];
  studentSet: User[];
  moduleAssoc : number;
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
  approvalFeedback : string[];
  altToRemove : number=-1;
  correctPointToRemove : number=-1;
  addAlternativeEdit = false;
  addCorrectPointEdit = false;
  alternativeToInsert = new Alternative();
  correctPointToInsert = new CorrectPoint();
  phraseError = false;
  feedbackInsertError = false;
  marksWorthError = false;
  alternativePhraseError = false;
  generalError = false;

  constructor(private modServ: ModulesService, private cdr: ChangeDetectorRef, private markServ: MarkingService, private router: Router, private route: ActivatedRoute, private testServ: TestService, private modalService: NgbModal, private sanitizer: DomSanitizer) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.approvalFeedback = [];
    this.approvalFeedback.push("");
    this.approvalFeedback.push("#B2E1B8");
    this.getResultChart(this.testID);
    this.getByTestID(this.testID);
    this.getScriptsByTestID(this.testID);
  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => {
          this.test = test;
        }
      );
  }

  getResultChart(testID) {
    return this.markServ.getResultChart(testID).subscribe(resultChartData => this.resultChartData = resultChartData);
  }

  ngDoCheck() {
    if (this.resultChartData) {
      if (this.resultChartData.labels && this.resultChartData.colors && this.resultChartData.scores) {
        if (this.resultChartData.labels.length > 1 && this.resultChartData.colors.length > 1 && this.resultChartData.scores.length > 1) {
          this.chartCheck = true;

          if (this.chartCount == 0) {
            this.chart = this.mainChartInit();
            this.chartCount++;
          }
        }
      }
    }
  }

  ngAfterViewInit() {
    this.chart = this.mainChartInit();
    this.cdr.detectChanges();
  }

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

  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
  }


  getModuleAssociation(moduleID) {
    return this.modServ.getModuleAssociation(moduleID)
      .subscribe(moduleAssoc => this.moduleAssoc = moduleAssoc);
  }

  getCorrectPoints(questionID) {
    return this.markServ.getCorrectPoints(questionID, this.testID)
      .subscribe(correctPoints => this.answerDetail.questionAndAnswer.correctPoints = correctPoints);
  }

  approve(answerID) {
    return this.markServ.approve(answerID)
      .subscribe(ans => {
        this.getScriptsByTestID(this.testID);
      });
  }

  removeAlt(altID) {
    this.markServ.removeAlternative(altID, this.testID)
      .subscribe(success =>{
        this.getScriptsByTestID(this.testID);
        this.getCorrectPoints(this.answerDetail.questionAndAnswer.question.question.questionID);
      });
  }

  publishGrades() {
    this.markServ.publishGrades(this.testID)
      .subscribe(success =>{
        this.getByTestID(this.testID);
      });
  }

  publishResults() {
    this.markServ.publishResults(this.testID)
      .subscribe(success =>{
        this.getByTestID(this.testID);
      });
  }

  removeCorrectPoint(correctPointID) {
    this.markServ.removeCorrectPoint(correctPointID, this.testID)
      .subscribe(success =>{
        this.getScriptsByTestID(this.testID);
        this.getCorrectPoints(this.answerDetail.questionAndAnswer.question.question.questionID);
      });
  }

  getScriptsByTestID(testID) {
    return this.markServ.getScriptsByTestIDTutor(testID)
      .subscribe(scripts => {
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
          this.studentDetail = this.studentSet[0];
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

  initAlternative() {
    const alternative = new Alternative();
    alternative.alternativeID = 0;
    alternative.correctPointID = 0;
    alternative.alternativePhrase = '';
    this.correctPointToInsert.alternatives.push(alternative);
    return false;
  }

  removeAlternative(j) {
    this.correctPointToInsert.alternatives.splice(j, 1);
    return false;
  }

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
