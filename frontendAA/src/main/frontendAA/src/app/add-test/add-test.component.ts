import {Component, OnInit} from '@angular/core';
import {Tests} from "../modelObjs/objects.model";
import {ModuleHomeComponent} from "../module-home/module-home.component";
import {TestService} from "../services/test.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";

const errorColour: string = "#dc3545";
const normalColour: string = "#202529";

@Component({
  selector: 'app-add-test',
  templateUrl: './add-test.component.html',
  styleUrls: ['./add-test.component.css']
})
export class AddTestComponent implements OnInit {


  testInsert = new Tests();
  today = new Date();
  nextYear = new Date();
  addTestForm;
  checkTitle: string = normalColour;
  checkStartDate: string = normalColour;
  checkEndDate: string = normalColour;
  titleError = false;
  endDateError = false;
  dateError = false;
  startDateError = false;
  generalError;

  constructor(private modHome: ModuleHomeComponent, private testService: TestService, private router: Router) {
  }

  ngOnInit() {
    this.nextYear.setFullYear(this.nextYear.getFullYear() + 1);
  }

  addTest() {
    this.testInsert.testTitle = this.testInsert.testTitle.trim();
    this.testInsert.moduleID = this.modHome.moduleID;
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
    this.testService.addTest(this.testInsert as Tests)
      .subscribe(test => {
        this.router.navigate(['/editTest', test.testID]);
      }, error => {
        return;
      });
  }

}
