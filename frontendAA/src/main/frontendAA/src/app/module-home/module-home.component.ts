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
import {interval, Subscription} from "rxjs";
import {TestService} from "../services/test.service";




@Component({
  selector: 'app-module-home',
  templateUrl: './module-home.component.html',
  styleUrls: ['./module-home.component.css']
})
export class ModuleHomeComponent implements OnInit {

  TUTOR: number = 1;
  STUDENT: number = 2;
  TEACHING_ASSISTANT: number = 3;

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
  moduleAssoc: number;
  activeTestCheck = true;
  activeResultCheck = false;
  addTestCheck = false;
  performanceCheck = false;
  scheduledTestCheck = false;
  markingCheck = false;
  testDraftCheck = false;
  reviewMarkingCheck = false;
  show: number = 0;
  performanceList: Performance[];
  activeSub: Subscription;
  subCheck: Boolean[];
  answeredSet: number[];

  constructor(private testServ: TestService, private modServ: ModulesService, private route: ActivatedRoute, private userServ: UserService, private testService: TestService, private router: Router) {
    this.moduleID = +this.route.snapshot.paramMap.get('moduleID');

  }

  ngOnInit() {
    this.getAnsweredTests();
    this.getPerformance(this.moduleID);
    this.getModuleAndTutor(this.moduleID);
    this.getModuleAssociation(this.moduleID);
    this.getActiveTests(this.moduleID);
    this.getActiveResults(this.moduleID);
    this.getScheduledTests(this.moduleID);
    this.getMarking(this.moduleID);
    this.getTestDrafts(this.moduleID);
    this.getReviewMarking(this.moduleID);

    // Checks for new tests every minute
    this.activeSub = interval(60000)
      .subscribe((val) => {
        this.getActiveTests(this.moduleID);
      });
  }

  calculateProgress(marked, toBeMarkedByYou, toBeMarkedByTAs): number {
    return Math.round((marked / (marked + toBeMarkedByYou + toBeMarkedByTAs)) * 100.00);
  }

  retractScheduled(testID) {
    this.testServ.scheduleTest(testID).subscribe(
      success => {
        this.getScheduledTests(this.moduleID);
        this.getTestDrafts(this.moduleID);
      }
    );
  }

  getPerformance(moduleID) {

    return this.modServ.getPerformance(moduleID)
      .subscribe(performance => this.performanceList = performance);
  }

  getModuleAndTutor(moduleID) {

    return this.modServ.getModuleAndTutor(moduleID)
      .subscribe(moduleTutor => this.moduleTutor = moduleTutor);
  }

  getModuleAssociation(moduleID) {

    return this.modServ.getModuleAssociation(moduleID)
      .subscribe(moduleAssoc => this.moduleAssoc = moduleAssoc);
  }

  getActiveTests(moduleID) {
    return this.modServ.getActiveTests(moduleID)
      .subscribe(tests => {
        this.activeTests = tests;

      });
  }

  getAnsweredTests() {
    return this.testServ.getAnsweredTests()
      .subscribe(answeredSet => {
        this.answeredSet = answeredSet;
      });
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
