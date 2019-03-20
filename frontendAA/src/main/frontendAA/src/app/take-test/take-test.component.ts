import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {Inputs, QuestionAndAnswer, Tests} from "../modelObjs/objects.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {interval, Subscription} from "rxjs";
import {DomSanitizer} from "@angular/platform-browser";
import {NgForm} from "@angular/forms";
import {KatexOptions} from "ng-katex";

const millisecondsToSeconds = 1000;
const secondsInDay = 86400;
const secondsInHour = 3600;
const hoursInDay = 24;
const secondsInMinute = 60;
const minutesInHour = 60;

@Component({
  selector: 'app-take-test',
  templateUrl: './take-test.component.html',
  styleUrls: ['./take-test.component.css']
})
export class TakeTestComponent implements OnInit {

  testID: number;
  test = new Tests();
  questions: QuestionAndAnswer[];
  questionDetail: number = 1;
  timeUp = false;
  timing = false;
  opened = false;
  submitted = false;
  originalLongMessage = "The following questions are indicated to exceed the size limit, pressing next may cause your answers to be truncated. Question(s): ";
  originalShortMessage = "The following questions are indicated to not have been answered - are you sure you want to proceed? Question(s): ";
  options: KatexOptions = {
    displayMode: true,
  };

  timeDifference: number;
  sub: Subscription;
  countdown: string;

  endSub: Subscription;

  tooLong: string = this.originalLongMessage;
  tooShort: string = this.originalShortMessage;

  constructor(private router: Router, private route: ActivatedRoute, private testServ: TestService, private modalService: NgbModal, private sanitizer: DomSanitizer) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  static convertToCountdown(timeDifference) {
    let originalTimeDifference = timeDifference;
    let days, hours, minutes, seconds;

    if (timeDifference >= secondsInDay) {
      days = Math.floor(timeDifference / secondsInDay);
      timeDifference -= days * secondsInDay;
    }

    hours = Math.floor(timeDifference / secondsInHour) % hoursInDay;
    timeDifference -= hours * secondsInHour;

    minutes = Math.floor(timeDifference / secondsInMinute) % minutesInHour;
    timeDifference -= minutes * secondsInMinute;

    seconds = timeDifference % secondsInMinute;

    if (originalTimeDifference >= 86400) {
      return days + 'd ' + hours + 'h ' + minutes + 'm ' + seconds + 's';
    } else {
      return hours + 'h ' + minutes + 'm ' + seconds + 's';
    }
  }


  async ngOnInit() {
    this.getByTestID(this.testID);
    await this.getQuestions(this.testID);
  }

  getAnsweredTests() {
    return this.testServ.getAnsweredTests()
      .subscribe(answeredSet => {
        if (answeredSet.includes(this.testID)) {
          this.backHome();
        }
      });
  }

  backHome() {
    this.router.navigate(['/moduleHome', this.test.moduleID]);
  }

  addTextInput(i) {
    const input = new Inputs();
    input.inputValue = '';
    input.inputIndex = this.questions[i].inputs.length;
    input.math = 0;
    this.questions[i].inputs.push(input);
    return false;
  }

  addMathInput(i) {
    const input = new Inputs();
    input.inputValue = '';
    input.inputIndex = this.questions[i].inputs.length;
    input.math = 1;
    this.questions[i].inputs.push(input);
    return false;
  }

  removeInput(i) {
    this.questions[i].inputs.splice(i, 1);
    return false;
  }

  readyImage(base64): any {
    return this.sanitizer.bypassSecurityTrustResourceUrl("data:image/png;base64," + base64);
  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => {
          this.test = test;

          this.sub = interval(1000).subscribe(() => {
            let date = new Date(this.test.endDateTime);
            this.timeDifference = Math.floor((date.getTime() - new Date().getTime()) / millisecondsToSeconds);
            if (this.timeDifference <= 0) {
              this.sub.unsubscribe();
              this.backHome();
            }

            this.countdown = TakeTestComponent.convertToCountdown(this.timeDifference);
          });
          if (test.practice != 1) {
            this.getAnsweredTests();
          }
        }
      );
  }

  getQuestions(testID) {
    return this.testServ.getQuestionsStudent(testID).subscribe(questions => {
      this.questions = questions;
    });
  }

  /**
   * From ngBootstrap framework
   * @param modal
   */
  open(modal) {
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
  }

  validateTest() {
    for (let q = 0; q < this.questions.length; q++) {
      this.questions[q].answer.questionID = this.questions[q].question.question.questionID;
      this.questions[q].answer.testID = this.testID;

      if (this.questions[q].question.question.questionType == 1) {
        if (this.questions[q].answer.content.trim().length < 1) {
          this.tooShort += (q + 1);
          if (q != this.questions.length - 1) {
            this.tooShort += ",";
          }
        }
      } else if (this.questions[q].question.question.questionType == 3) {
        for (let i = 0; i < this.questions[q].inputs.length; i++) {
          if (this.questions[q].inputs[i].inputValue.trim().length < 1) {
            this.tooLong += (q + 1);
            if (q != this.questions.length - 1) {
              this.tooLong += ",";
            }
            break;
          }
        }

      }


      if (this.questions[q].question.question.questionType == 1) {
        if (this.questions[q].answer.content.length > 65535) {
          this.tooLong += (q + 1);
          if (q != this.questions.length - 1) {
            this.tooLong += ",";
          }
        }
      } else if (this.questions[q].question.question.questionType == 3) {
        for (let i = 0; i < this.questions[q].inputs.length; i++) {
          if (this.questions[q].inputs[i].inputValue.length > 65535) {
            this.tooLong += (q + 1);
            if (q != this.questions.length - 1) {
              this.tooLong += ",";
            }
            break;
          }
        }

      }
    }
  }

  async submitTest(form: NgForm) {


    for (let q = 0; q < this.questions.length; q++) {
      this.questions[q].answer.testID = this.testID;
    }
    this.submitted = true;
    this.testServ.submitTest(this.questions as QuestionAndAnswer[])
      .subscribe(success => {
        form.reset();
        this.tooShort = this.originalShortMessage;
        this.tooLong = this.originalLongMessage;
        this.backHome();
      }, error => {
        return;
      });
  }

}
