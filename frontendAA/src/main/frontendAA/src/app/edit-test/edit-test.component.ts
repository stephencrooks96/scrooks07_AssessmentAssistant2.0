import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TestService} from "../services/test.service";
import {Tests, TutorQuestionPojo} from "../modelObjs/objects.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NgForm} from "@angular/forms";

const errorColour: string = "#dc3545";
const normalColour: string = "#202529";


@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.css']
})
export class EditTestComponent implements OnInit {

  testID: number;
  test = new Tests();
  showAdd = true;
  showExist = false;
  showDetail = false;
  questions: TutorQuestionPojo[];
  questionDetail = new TutorQuestionPojo();
  testInsert = new Tests();
  today = new Date();
  nextYear = new Date();
  checkTitle: string = normalColour;
  checkStartDate: string = normalColour;
  checkEndDate: string = normalColour;
  titleError = false;
  endDateError = false;
  dateError = false;
  startDateError = false;
  generalError;
  editTestShow = false;
  dateAsString : string="";

  constructor(private router: Router, private route: ActivatedRoute, private testServ: TestService, private modalService: NgbModal) {
    this.testID = +this.route.snapshot.paramMap.get('testID');
  }

  ngOnInit() {
    this.getByTestID(this.testID);
    this.getQuestions(this.testID);
    this.nextYear.setFullYear(this.nextYear.getFullYear() + 1);
  }

  getByTestID(testID) {
    return this.testServ.getByTestID(testID)
      .subscribe(test => {
        this.test = test;
        this.testInsert = test;
      }
  );
  }

  getQuestions(testID) {
    return this.testServ.getQuestions(testID).subscribe(questions => this.questions = questions);
  }

  /**
   * From ngBootstrap framework
   * @param modal
   */
  open(modal) {
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
  }

  deleteTest(testID) {
    this.testServ.deleteTest(testID).subscribe(
      success => {
        if (success) {
          this.router.navigate(['/moduleHome', this.test.moduleID]);
        }
      }
    );
  }

  scheduleTest(testID) {
    this.testServ.scheduleTest(testID).subscribe(
      success => {
        if (success) {
          this.router.navigate(['/moduleHome', this.test.moduleID]);
        }
      }
    );
  }

  editTest(form: NgForm) {
    this.testInsert.testTitle = this.testInsert.testTitle.trim();
    this.generalError = false;
    if (!this.testInsert.testTitle || this.testInsert.testTitle.length < 1 || this.testInsert.testTitle.length > 50) {
      this.checkTitle = errorColour;
      this.titleError = true;
      this.generalError = true;
    }
    if (this.testInsert.startDateTime.valueOf() >= this.testInsert.endDateTime.valueOf()) {
      this.dateError = true;
      this.generalError = true;
    }
    if (this.testInsert.startDateTime.valueOf() < new Date().valueOf() || this.testInsert.startDateTime.valueOf() > new Date().setFullYear(new Date().getFullYear() + 1).valueOf()) {
      this.startDateError = true;
      this.checkStartDate = errorColour;
      this.generalError = true;
    }
    if (this.testInsert.endDateTime.valueOf() < new Date().valueOf() || this.testInsert.endDateTime.valueOf() > new Date().setFullYear(new Date().getFullYear() + 1).valueOf()) {
      this.endDateError = true;
      this.checkEndDate = errorColour;
      this.generalError = true;
    }
    if (this.generalError == true) {
      return;
    }
    this.test.testTitle = this.testInsert.testTitle;
    this.test.startDateTime = this.testInsert.startDateTime;
    this.test.endDateTime = this.testInsert.endDateTime;
    this.editTestShow = false;
    this.testServ.editTest(this.test as Tests)
      .subscribe(test => {
        form.reset();
        this.getByTestID(this.testID);
      }, error => {
        return;
      });
  }

}
