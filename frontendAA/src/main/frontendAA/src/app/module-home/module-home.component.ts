import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ModulesService} from "../services/modules.service";
import {UserService} from "../services/user.service";
import {
  Associate,
  ModuleFE,
  ModuleWithTutorFE,
  Performance,
  TestAndGrade,
  TestMarking,
  Tests,
  User
} from "../modelObjs/objects.model";
import {ActivatedRoute, Router} from "@angular/router";
import {interval, Observable, Subscription} from "rxjs";
import {TestService} from "../services/test.service";
import {NgForm} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";




@Component({
  selector: 'app-module-home',
  templateUrl: './module-home.component.html',
  styleUrls: ['./module-home.component.css']
})
export class ModuleHomeComponent implements OnInit {

  @ViewChild('assocInput') assocInput: ElementRef;
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
  moduleAssoc: Observable<number>;
  activeTestCheck = true;
  activeResultCheck = false;
  addTestCheck = false;
  performanceCheck = false;
  scheduledTestCheck = false;
  markingCheck = false;
  testDraftCheck = false;
  reviewMarkingCheck = false;
  addAssociationsCheck = false;
  show: number = 0;
  performanceList: Performance[];
  activeSub: Subscription;
  subCheck: Boolean[];
  answeredSet: number[];
  fileError: boolean;
  fileErrorMessage: string;
  fileSuccess: boolean = false;
  associationsFile: any;
  associations = [];
  userToRemove : string;
  contactAssociates : false;
  associates : Associate[];

  constructor(private testServ: TestService, private modServ: ModulesService, private route: ActivatedRoute, private modalService: NgbModal, private userServ: UserService, private testService: TestService, private router: Router) {
    this.moduleID = +this.route.snapshot.paramMap.get('moduleID');
  }

  ngOnInit() {
    this.checkValid(this.moduleID);
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
    this.getAssociates(this.moduleID);

    // Checks for new tests every minute
    this.activeSub = interval(60000)
      .subscribe((val) => {
        this.getActiveTests(this.moduleID);
        this.getScheduledTests(this.moduleID);
        this.getMarking(this.moduleID);
        this.getReviewMarking(this.moduleID);
      });
  }

  getAssociates(moduleID) {
    return this.modServ.getAssociates(moduleID)
      .subscribe(associates => this.associates = associates);
  }

  /**
   * From ngBootstrap framework
   * @param modal
   */
  open(modal) {
    this.modalService.open(modal, {ariaLabelledBy: 'modal-basic-title'});
  }

  removeAssociation(username) {
    return this.modServ.removeAssociation(username, this.moduleID)
      .subscribe(user => this.getAssociates(this.moduleID));
  }

  readAssociations(csv: any) {
    this.associationsFile = csv.target.files[0];

    let fileReader = new FileReader();
    let stopPush;
    fileReader.readAsText(this.associationsFile);

    fileReader.onload = () => {
      this.fileSuccess = false;
      this.fileError = false;
      this.fileErrorMessage = "";
      // Split on rows
      let data = fileReader.result.toString().split(/[\r\n]/);

      // Split header on commas
      let headers = data[0].split(',');
      let spliceArray = [];
      for (let i = 0; i < data.length; i++) {
        if (data[i] == "") {
          spliceArray.push([i]);
        }
      }
      for (let i = 0; i < spliceArray.length; i++) {
        data.splice(spliceArray[i] - i, 1);
      }
      for (let i = 1; i < data.length; i++) {
        stopPush = false;
        // split content based on comma
        let subData = data[i].split(',');
        let assocToAdd = new Associate();
        if (subData.length === headers.length) {
          for (let j = 0; j < headers.length; j++) {
            if (headers[j] == "Email") {
              assocToAdd.username = subData[j];
            } else if (headers[j] == "AssociationType") {
              assocToAdd.associateType = subData[j];
            } else if (headers[j] == "FirstName") {
              assocToAdd.firstName = subData[j];
            } else if (headers[j] == "LastName") {
              assocToAdd.lastName = subData[j];
            }
          }
          if (!assocToAdd.associateType && !assocToAdd.username && !assocToAdd.firstName && !assocToAdd.lastName) {
            stopPush = true;
          } else if (!assocToAdd.associateType || !assocToAdd.username) {
            this.fileError = true;
            this.fileErrorMessage = "One or more cells have been left empty.";
            this.assocInput.nativeElement.value = null;
            return;
          } else if (assocToAdd.associateType != "S" && assocToAdd.associateType != "TA") {
            this.fileError = true;
            this.fileErrorMessage = "AssociationType can only be S or TA.";
            this.assocInput.nativeElement.value = null;
            return;
          }
        } else {
          this.fileError = true;
          this.fileErrorMessage = "Column count does not match headings.";
          this.assocInput.nativeElement.value = null;
          return;
        }
        if (!stopPush) {
          this.associations.push(assocToAdd);
        }
      }
      this.fileSuccess = true;
    };

  }

  addAssociations(form: NgForm) {

    if (this.fileError) {
      return;
    }

    this.modServ.addAssociations(this.moduleID, this.associations)
      .subscribe(_ => {
        form.reset();
        this.assocInput.nativeElement.value = null;
        this.fileSuccess = false;
        this.fileError = false;
        this.getAssociates(this.moduleID);
      }, _ => {
        return;
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
    this.moduleAssoc = this.modServ.getModuleAssociation(moduleID);
  }

  checkValid(moduleID) {
    return this.modServ.getModuleAssociation(moduleID)
      .subscribe(checkValid => {
        if (!checkValid) {
          this.router.navigate(['/myModules']);
        }
      });
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
