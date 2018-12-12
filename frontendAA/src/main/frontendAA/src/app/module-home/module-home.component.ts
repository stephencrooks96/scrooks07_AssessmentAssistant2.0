import {Component, OnInit} from '@angular/core';
import {ModulesService} from "../services/modules.service";
import {UserService} from "../services/user.service";
import {
  ModuleFE,
  ModuleWithTutorFE,
  Performance,
  TestAndGrade,
  TestMarking,
  Tests,
  User
} from "../modelObjs/objects.model";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {tap} from "rxjs/operators";
import {TestService} from "../services/test.service";

@Component({
  selector: 'app-module-home',
  templateUrl: './module-home.component.html',
  styleUrls: ['./module-home.component.css']
})
export class ModuleHomeComponent implements OnInit {

  moduleTutor = new ModuleWithTutorFE();
  tutor = new User();
  module = new ModuleFE();
  moduleID: number;
  activeTests: Tests[];
  scheduledTests: Tests[];
  testDrafts: Tests[];
  marking: TestMarking[];
  testsForReview: TestMarking[];
  activeResults: TestAndGrade[];
  moduleAssoc: string = "";
  activeTestCheck = true;
  activeResultCheck = false;
  addTestCheck = false;
  performanceCheck = false;
  scheduledTestCheck = false;
  markingCheck = false;
  testDraftCheck = false;
  reviewMarkingCheck = false;
  show: number = 0;
  testInsert = new Tests();
  performanceList: Performance[];

  constructor(private modServ: ModulesService, private route: ActivatedRoute, private userServ: UserService, private testService: TestService, private router: Router) {
    this.moduleID = +this.route.snapshot.paramMap.get('moduleID');

  }

  ngOnInit() {
    this.getPerformance(this.moduleID);
    this.getModuleAndTutor(this.moduleID);
    this.getModuleAssociation(this.moduleID);
    this.getActiveTests(this.moduleID);
    this.getActiveResults(this.moduleID);
    this.getScheduledTests(this.moduleID);
    this.getMarking(this.moduleID);
    this.getTestDrafts(this.moduleID);
    this.getReviewMarking(this.moduleID);

  }

  calculateProgress(marked, toBeMarkedByYou, toBeMarkedByTAs): number {
    return Math.round((marked / (marked + toBeMarkedByYou + toBeMarkedByTAs)) * 100.00);
  }

  getPerformance(moduleID) {

    return this.modServ.getPerformance(moduleID)
      .subscribe(performance => this.performanceList = performance);
  }

  getModuleAndTutor(moduleID) {

    return this.modServ.getModuleAndTutor(moduleID)
      .subscribe(moduleTutor => this.moduleTutor = moduleTutor);
  }

  addTest() {
    this.testInsert.testTitle = this.testInsert.testTitle.trim();
    this.testInsert.moduleID = this.moduleID;
    if (!this.testInsert.testTitle) {
      return;
    }
    this.testService.addTest(this.testInsert as Tests)
      .subscribe(test => {
        this.router.navigate(['/editTest', test.testID]);
      }, error => {
        return;
      });
  }

  getModuleAssociation(moduleID) {

    return this.modServ.getModuleAssociation(moduleID)
      .subscribe(moduleAssoc => this.moduleAssoc = moduleAssoc);
  }

  getActiveTests(moduleID) {
    return this.modServ.getActiveTests(moduleID)
      .subscribe(tests => this.activeTests = tests);
  }

  getActiveResults(moduleID) {
    return this.modServ.getActiveResults(moduleID)
      .subscribe(tests => this.activeResults = tests);
  }

  getScheduledTests(moduleID) {
    return this.modServ.getScheduledTests(moduleID)
      .subscribe(tests => this.scheduledTests = tests);
  }

  getMarking(moduleID) {
    return this.modServ.getMarking(moduleID)
      .subscribe(tests => this.marking = tests);
  }

  getTestDrafts(moduleID) {
    return this.modServ.getTestDrafts(moduleID)
      .subscribe(tests => this.testDrafts = tests);
  }

  getReviewMarking(moduleID) {
    return this.modServ.getReviewMarking(moduleID)
      .subscribe(tests => this.testsForReview = tests);
  }

}
